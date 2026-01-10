package com.gtocore.common.machine.tesseract;

import com.gtolib.api.ae2.AEKeyTypeMap;
import com.gtolib.api.ae2.IPatternProviderLogic;
import com.gtolib.api.ae2.PatternProviderTargetCache;
import com.gtolib.api.ae2.machine.ICustomCraftingMachine;
import com.gtolib.utils.ServerUtils;
import com.gtolib.utils.holder.BooleanHolder;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.helpers.patternprovider.PatternProviderTarget;
import appeng.me.helpers.IGridConnectedBlockEntity;
import appeng.me.storage.CompositeStorage;
import appeng.me.storage.ExternalStorageFacade;
import com.fast.fastcollection.O2OOpenCacheHashMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class DirectedTesseractMachine extends MetaMachine implements
                                      IFancyUIMachine,
                                      IMachineLife,
                                      ICustomCraftingMachine,
                                      IMultiTesseract {

    public static final Multiset<ImmutableList<TesseractDirectedTarget>> HIGHLIGHTS = HashMultiset.create();

    @Getter
    private final List<IItemHandler> itemHandlers = new ArrayList<>(20);
    @Getter
    private final List<IFluidHandler> fluidHandlers = new ArrayList<>(20);

    @Getter
    @Setter
    private boolean called;

    @Persisted
    @DescSynced
    @Getter
    public final List<TesseractDirectedTarget> targets;
    @Persisted
    private final UnfinishedPushList unfinishedPushLists;
    private WeakReference<BlockEntity>[] blockEntityReference;
    private final ConditionalSubscriptionHandler task;

    public DirectedTesseractMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.unfinishedPushLists = new UnfinishedPushList(this);
        targets = new ArrayList<>();
        task = new ConditionalSubscriptionHandler(this, unfinishedPushLists::push, 20, unfinishedPushLists::hasWorkToDo);
    }

    @Override
    public boolean customPush() {
        return true;
    }

    public void setTargets(Collection<TesseractDirectedTarget> newTargets) {
        targets.clear();
        targets.addAll(newTargets);
        targets.sort(TesseractDirectedTarget.SORTER);
        blockEntityReference = new WeakReference[targets.size()];
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        IFancyUIMachine.super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                GuiTextures.LIGHT_ON, GuiTextures.LIGHT_ON, () -> false,
                (clickData, pressed) -> {
                    if (clickData.isRemote && getLevel() != null) {
                        HIGHLIGHTS.add(ImmutableList.copyOf(targets), 200);
                    }
                })
                .setTooltipsSupplier(pressed -> Collections.singletonList(Component.translatable(HIGHLIGHT_TEXT))));
    }

    public BlockEntity getBlockEntity(int index) {
        if (blockEntityReference == null) {
            if (targets.isEmpty()) {
                return null;
            }
            blockEntityReference = new WeakReference[targets.size()];
        }
        if (blockEntityReference[index] != null) {
            var be = blockEntityReference[index].get();
            if (be != null) {
                return be.isRemoved() ? null : be;
            }
        }
        var target = targets.get(index);
        var dim = ServerUtils.getServer().getLevel(target.pos().dimension());
        if (dim == null) {
            return null;
        }
        var be = dim.getBlockEntity(target.pos().pos());
        blockEntityReference[index] = new WeakReference<>(be);
        if (be != null) {
            return be.isRemoved() ? null : be;
        }
        return null;
    }

    @Override
    public IPatternProviderLogic.PushResult pushPattern(IPatternProviderLogic logic, IActionSource actionSource, BooleanHolder success, Operate operate, Set<AEKey> patternInputs, IPatternDetails patternDetails, ObjectHolder<KeyCounter[]> inputHolder, Supplier<IPatternProviderLogic.PushResult> pushPatternSuccess, BooleanSupplier canPush, Direction direction, Direction adjBeSide) {
        if (!(patternDetails instanceof AEProcessingPattern processingPattern)) return IPatternProviderLogic.PushResult.REJECTED;
        var sparseInputs = processingPattern.getSparseInputs();

        if (unfinishedPushLists.hasWorkToDo() ||
                targets.isEmpty() ||
                sparseInputs.length > targets.size()) {
            return IPatternProviderLogic.PushResult.NOWHERE_TO_PUSH;
        }

        boolean terminateWhenBlocked = false;

        Map<TesseractDirectedTarget, GenericStack> remainingStacks = new O2OOpenCacheHashMap<>(sparseInputs.length);
        Map<PatternProviderTarget, GenericStack> readyToPushStacks = new O2OOpenCacheHashMap<>(sparseInputs.length);
        boolean cycleSuccess = true;
        for (var i = 0; i < sparseInputs.length; i++) {
            var targetAt = targets.get(i);
            var be = getBlockEntity(i);
            var subPushStack = sparseInputs[i];
            if (be == null) {
                return IPatternProviderLogic.PushResult.NOWHERE_TO_PUSH;
            }
            var toPush = PatternProviderTargetCache.find(be, logic, targetAt.face(), actionSource, targetAt.pos().pos().asLong());
            if (toPush == null) {
                return IPatternProviderLogic.PushResult.NOWHERE_TO_PUSH;
            }
            var blocked = toPush.containsPatternInput(patternInputs);
            if (blocked) {
                return IPatternProviderLogic.PushResult.NOWHERE_TO_PUSH;
            }
            var haveEnoughSpace = toPush.insert(subPushStack.what(), subPushStack.amount(), Actionable.SIMULATE) == subPushStack.amount();
            if (!haveEnoughSpace) {
                remainingStacks.put(targetAt, subPushStack);
                continue;
            }
            readyToPushStacks.put(toPush, subPushStack);
        }

        remainingStacks.forEach(unfinishedPushLists::addTask);
        readyToPushStacks.forEach((toPush, stack) -> {
            toPush.insert(stack.what(), stack.amount(), Actionable.MODULATE);
        });
        unfinishedPushLists.push();
        return pushPatternSuccess.get();
    }

    @Override
    public int getTotalBlockEntities() {
        return targets.size();
    }

    @Override
    public Direction getSideForBlockEntity(int i, @Nullable Direction side) {
        return targets.get(i).face();
    }

    @Override
    public boolean onMarkerInteract(Player player, List<TesseractDirectedTarget> targets) {
        if (targets.isEmpty()) {
            return false;
        }
        if (getLevel() == null || getLevel().isClientSide()) {
            return true;
        }
        setTargets(targets);
        player.displayClientMessage(Component.translatable(WRITE_SUCCESS_TEXT), true);
        return true;
    }

    @Override
    public void onMachineRemoved() {
        unfinishedPushLists.unfinishedStacks.forEach(stack -> {
            if (getLevel() != null && stack.what() instanceof AEItemKey item) {
                Block.popResource(getLevel(), getHolder().getBlockPos(), item.toStack(Math.toIntExact(stack.amount())));
            }
        });
    }

    private static MEStorage getMEStorage(TesseractDirectedTarget target, MinecraftServer levelGetter) {
        var dim = levelGetter.getLevel(target.pos().dimension());
        if (dim == null) {
            return null;
        }
        var be = dim.getBlockEntity(target.pos().pos());
        if (be == null) {
            return null;
        }
        if (be instanceof IGridConnectedBlockEntity gbe && gbe.getGridNode() != null) {
            return gbe.getGridNode().getGrid().getStorageService().getInventory();
        }
        var item = be.getCapability(ForgeCapabilities.ITEM_HANDLER, target.face()).map(ExternalStorageFacade::of).orElse(null);
        var fluid = be.getCapability(ForgeCapabilities.FLUID_HANDLER, target.face()).map(ExternalStorageFacade::of).orElse(null);
        if (item != null && fluid != null) return new CompositeStorage(new AEKeyTypeMap<>(item, fluid));
        if (item != null) return item;
        return fluid;
    }

    private static class UnfinishedPushList implements IManaged {

        final DirectedTesseractMachine machine;

        @Persisted
        final List<TesseractDirectedTarget> unfinishedPushes = new ArrayList<>();
        @Persisted
        final List<GenericStack> unfinishedStacks = new ArrayList<>();

        public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(UnfinishedPushList.class);
        @Getter
        private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);

        private UnfinishedPushList(DirectedTesseractMachine machine) {
            this.machine = machine;
        }

        @Override
        public ManagedFieldHolder getFieldHolder() {
            return MANAGED_FIELD_HOLDER;
        }

        @Override
        public void onChanged() {
            machine.onChanged();
            machine.task.updateSubscription();
        }

        void push() {
            if (machine.getLevel() instanceof ServerLevel level) {
                machine.task.updateSubscription();
                var server = level.getServer();
                for (int i = 0; i < unfinishedPushes.size(); i++) {
                    var target = unfinishedPushes.get(i);
                    var stack = unfinishedStacks.get(i);
                    var meStorage = getMEStorage(target, server);
                    if (meStorage != null) {
                        var inserted = meStorage.insert(stack.what(), stack.amount(), Actionable.MODULATE, IActionSource.empty());
                        if (inserted == stack.amount()) {
                            unfinishedPushes.remove(i);
                            unfinishedStacks.remove(i);
                            i--;
                        } else {
                            unfinishedStacks.set(i, new GenericStack(stack.what(), stack.amount() - inserted));
                        }
                    }
                }
            }
        }

        boolean hasWorkToDo() {
            return !unfinishedPushes.isEmpty();
        }

        void addTask(TesseractDirectedTarget target, GenericStack stack) {
            unfinishedPushes.add(target);
            unfinishedStacks.add(stack);
            machine.onChanged();
            machine.task.updateSubscription();
        }
    }
}
