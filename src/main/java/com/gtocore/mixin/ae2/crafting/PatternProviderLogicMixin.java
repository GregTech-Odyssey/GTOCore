package com.gtocore.mixin.ae2.crafting;

import com.gtocore.common.machine.electric.AdvancedTesseractMachine;

import com.gtolib.api.ae2.*;
import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;

import appeng.api.config.Actionable;
import appeng.api.config.LockCraftingMode;
import appeng.api.config.Settings;
import appeng.api.config.YesNo;
import appeng.api.crafting.IPatternDetails;
import appeng.api.implementations.blockentities.ICraftingMachine;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.helpers.patternprovider.PatternProviderTarget;
import appeng.util.ConfigManager;
import appeng.util.inv.AppEngInternalInventory;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(value = PatternProviderLogic.class, remap = false)
public abstract class PatternProviderLogicMixin implements IPatternProviderLogic {

    @Unique
    private final Long2ObjectOpenHashMap<IPatternDetails> gtolib$cachePatternPos = new Long2ObjectOpenHashMap<>();
    @Unique
    private final Int2ObjectOpenHashMap<IPatternDetails> gtolib$cachePatternDir = new Int2ObjectOpenHashMap<>();
    @Final
    @Shadow
    private PatternProviderLogicHost host;
    @Final
    @Shadow
    private IManagedGridNode mainNode;
    @Final
    @Shadow
    private IActionSource actionSource;
    @Final
    @Shadow
    private ConfigManager configManager;
    @Final
    @Shadow
    private List<IPatternDetails> patterns;
    @Final
    @Shadow
    private Set<AEKey> patternInputs;
    @Shadow
    @Final
    private AppEngInternalInventory patternInventory;
    @Shadow
    @Final
    private List<GenericStack> sendList;
    private int pushedCount = 1024;
    @Shadow
    private Direction sendDirection;
    @Unique
    private PatternProviderTargetCache[] gtolib$targetCaches;
    @Unique
    private IPatternDetails gtolib$currentPattern;
    @Unique
    private GlobalPos gto$pos;

    @Shadow
    public abstract LockCraftingMode getCraftingLockedReason();

    @Shadow
    protected abstract Set<Direction> getActiveSides();

    @Shadow
    protected abstract void onPushPatternSuccess(IPatternDetails pattern);

    @Shadow
    public abstract boolean isBlocking();

    @Shadow
    protected abstract boolean adapterAcceptsAll(PatternProviderTarget target, KeyCounter[] inputHolder);

    @Shadow
    protected abstract void addToSendList(AEKey what, long amount);

    @Inject(method = "<init>(Lappeng/api/networking/IManagedGridNode;Lappeng/helpers/patternprovider/PatternProviderLogicHost;I)V", at = @At("TAIL"), remap = false)
    private void init(IManagedGridNode mainNode, PatternProviderLogicHost host, int patternInventorySize, CallbackInfo ci) {
        configManager.registerSetting(GTOSettings.BLOCKING_TYPE, BlockingType.NONE);
        gtolib$targetCaches = new PatternProviderTargetCache[6];
    }

    @Inject(at = @At("TAIL"), method = "readFromNBT")
    private void readFromNBT(CompoundTag tag, CallbackInfo ci) {
        if (!this.isBlocking()) {
            configManager.putSetting(GTOSettings.BLOCKING_TYPE, BlockingType.NONE);
            configManager.putSetting(Settings.BLOCKING_MODE, YesNo.YES);
        }
    }

    @Override
    public PushResult gtolib$pushPattern(IPatternDetails patternDetails, ObjectHolder<KeyCounter[]> inputHolder, Supplier<PushResult> pushPatternSuccess) {
        if (!this.mainNode.isActive()) return PushResult.GRID_NODE_MISSING;
        if (!this.patterns.contains(patternDetails)) return PushResult.PATTERN_DOES_NOT_EXIST;
        var be = host.getBlockEntity();
        var cache = IDirectionCacheBlockEntity.getBlockEntityDirectionCache(be);
        if (cache == null) return PushResult.REJECTED;
        var setting = configManager.getSetting(GTOSettings.BLOCKING_TYPE);
        if (setting == BlockingType.ALL || setting == BlockingType.CONTAIN) {
            pushedCount = 1;
        } else {
            pushedCount = 1024;
        }
        BooleanSupplier canPush = () -> getCraftingLockedReason() == LockCraftingMode.NONE && sendList.isEmpty();
        if (!canPush.getAsBoolean()) return PushResult.PATTERN_PROVIDER_LOCKED;

        gtolib$currentPattern = patternDetails;

        var level = be.getLevel();
        var pos = be.getBlockPos();
        boolean success = false;
        boolean molecular = !patternDetails.supportsPushInputsToExternalInventory();
        for (var direction : getActiveSides()) {
            var adjBe = cache.getAdjacentBlockEntity(level, pos, direction);
            if (adjBe == null) continue;
            var adjBeSide = direction.getOpposite();
            if (molecular) {
                var craftingMachine = ICraftingMachine.of(level, pos.relative(direction), adjBeSide, adjBe);
                if (craftingMachine != null) {
                    var result = gtolib$pushCraftingMachine(craftingMachine, patternDetails, inputHolder, pushPatternSuccess, adjBeSide);
                    if (result.success()) success = true;
                    if (result.needBreak()) return result;
                }
            } else {
                if (adjBe instanceof MetaMachineBlockEntity machineBlockEntity) {
                    if (machineBlockEntity.metaMachine instanceof AdvancedTesseractMachine machine && machine.roundRobin) {
                        var size = machine.poss.size();
                        List<PatternProviderTarget> targets = new ObjectArrayList<>(size);
                        for (int i = 0; i < size; ++i) {
                            var targetPos = machine.poss.get(i);
                            var target = PatternProviderTargetCache.find(machine.getBlockEntity(targetPos, i), this, adjBeSide, actionSource, targetPos.asLong());
                            if (target == null) continue;
                            targets.add(target);
                        }
                        int count = 1000;
                        while (count > 0) {
                            count--;
                            boolean done = true;
                            for (var target : targets) {
                                if (target.containsPatternInput(patternInputs)) continue;
                                var result = gtolib$pushTarget(patternDetails, inputHolder, pushPatternSuccess, canPush, direction, target, false);
                                if (result.success()) success = true;
                                if (result.needBreak()) return result;
                                if (result == PushResult.SUCCESS) done = false;
                            }
                            if (done) break;
                        }
                    } else {
                        var target = PatternProviderTargetCache.find(adjBe, this, adjBeSide, actionSource, 0);
                        if (target == null || target.containsPatternInput(patternInputs)) continue;
                        var result = gtolib$pushTarget(patternDetails, inputHolder, pushPatternSuccess, canPush, direction, target, true);
                        if (result.success()) success = true;
                        if (result.needBreak()) return result;
                    }
                } else {
                    var target = findAdapter(direction);
                    if (target == null || target.containsPatternInput(patternInputs)) continue;
                    var result = gtolib$pushTarget(patternDetails, inputHolder, pushPatternSuccess, canPush, direction, target, true);
                    if (result.success()) success = true;
                    if (result.needBreak()) return result;
                }
            }
        }
        return success ? PushResult.SUCCESS : PushResult.NOWHERE_TO_PUSH;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        return false;
    }

    @Unique
    private PushResult gtolib$pushCraftingMachine(ICraftingMachine craftingMachine, IPatternDetails patternDetails, ObjectHolder<KeyCounter[]> inputHolder, Supplier<PushResult> pushPatternSuccess, Direction adjBeSide) {
        boolean success = false;
        while (true) {
            if (inputHolder.value != null && craftingMachine.acceptsPlans() && craftingMachine.pushPattern(patternDetails, inputHolder.value, adjBeSide)) {
                onPushPatternSuccess(patternDetails);
                success = true;
                var result = pushPatternSuccess.get();
                if (result.needBreak()) return result;
                continue;
            }
            return success ? PushResult.SUCCESS : PushResult.REJECTED;
        }
    }

    @Unique
    private PushResult gtolib$pushTarget(IPatternDetails patternDetails, ObjectHolder<KeyCounter[]> inputHolder, Supplier<PushResult> pushPatternSuccess, BooleanSupplier canPush, Direction direction, PatternProviderTarget adapter, boolean continuous) {
        int count = this.pushedCount;
        boolean success = false;
        while (count > 0) {
            count--;
            if (inputHolder.value != null && this.adapterAcceptsAll(adapter, inputHolder.value)) {
                patternDetails.pushInputsToExternalInventory(inputHolder.value, (what, amount) -> {
                    var inserted = adapter.insert(what, amount, Actionable.MODULATE);
                    if (inserted < amount) {
                        this.addToSendList(what, amount - inserted);
                    }
                });
                if (adapter instanceof PatternProviderTargetCache.WrapMeStorage storage) {
                    if (storage.pos() != 0) gtolib$cachePatternPos.put(storage.pos(), patternDetails);
                    else gtolib$cachePatternDir.put(storage.dir(), patternDetails);
                }
                onPushPatternSuccess(patternDetails);
                success = true;
                this.sendDirection = direction;
                var result = pushPatternSuccess.get();
                if (result.needBreak()) return result;
                if (canPush.getAsBoolean()) {
                    if (continuous) continue;
                    return PushResult.SUCCESS;
                }
            }
            break;
        }
        return success ? PushResult.SUCCESS : PushResult.REJECTED;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    @Nullable
    private PatternProviderTarget findAdapter(Direction side) {
        if (gtolib$targetCaches[side.get3DDataValue()] == null) {
            gtolib$targetCaches[side.get3DDataValue()] = new PatternProviderTargetCache(host.getBlockEntity(), this, side, actionSource);
        }
        @Nullable
        PatternProviderTarget ret = gtolib$targetCaches[side.get3DDataValue()].find(side.get3DDataValue());
        return ret;
    }

    @Override
    public BlockingType gtolib$getBlocking() {
        return configManager.getSetting(GTOSettings.BLOCKING_TYPE);
    }

    @Override
    public IPatternDetails gtolib$getCurrentPattern() {
        return this.gtolib$currentPattern;
    }

    @Override
    public IPatternDetails gtolib$getCachePattern(long pos, int dir) {
        if (pos != 0) return gtolib$cachePatternPos.get(pos);
        else return gtolib$cachePatternDir.get(dir);
    }

    /**
     * @author xinxinsuried
     * @reason 更细时根据网络中的me整理机替换物品
     */
    @Overwrite
    public void updatePatterns() {
        patterns.clear();
        patternInputs.clear();

        for (var stack : this.patternInventory) {
            var details = MyPatternDetailsHelper.decodePattern(stack, this.host.getBlockEntity(), mainNode.getGrid());

            if (details != null) {
                patterns.add(details);

                for (var iinput : details.getInputs()) {
                    for (var inputCandidate : iinput.getPossibleInputs()) {
                        patternInputs.add(inputCandidate.what().dropSecondary());
                    }
                }
            }
        }

        ICraftingProvider.requestUpdate(mainNode);
    }

    @Override
    public GlobalPos gto$getPos() {
        if (gto$pos != null) return gto$pos;
        return gto$pos = GlobalPos.of(host.getBlockEntity().getLevel().dimension(), host.getBlockEntity().getBlockPos());
    }
}
