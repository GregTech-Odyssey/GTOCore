package com.gtocore.common.machine.multiblock.storage;

import com.gtocore.api.ae2.stacks.AEFluidKeyStackHandler;
import com.gtocore.api.ae2.stacks.AEItemKeyStackHandler;
import com.gtocore.api.ae2.stacks.AEManaKeyHandler;
import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.common.block.BlockMap;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTORecipeDataKeys;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IWailaDisplayProvider;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.transfer.fluid.ICustomFluidStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.ICustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.*;
import appeng.api.storage.MEStorage;
import appeng.capabilities.Capabilities;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.datasream.data.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;

import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;

@Slf4j
public final class MultiblockMEStorageMachine extends MultiblockControllerMachine implements MEStorage, IDropSaveMachine, IWailaDisplayProvider {

    public static final int MIN_DEPTH = 2;
    public static final int MAX_DEPTH = 14;

    private static final Predicate<BlockState> PREDICATE = s -> {
        var block = s.getBlock();
        return block == GTBlocks.STEEL_HULL.get() || block == GTOMachines.VAULT_HATCH.get();
    };

    private int lDist = 0, rDist = 0, uDist = 0, dDist = 0, bDist = 0;

    @SaveToDisk
    @Getter
    @NotNull
    private final AEKeyMap<AEKey> keyMap = new AEKeyMap<>();

    private int cells;
    private long storage;
    @Getter
    private long capacity;

    @Getter
    @NotNull
    private final LongSupplier storageSupplier = () -> storage;
    @Getter
    private final Runnable onChange = this::saveChanges;

    @Nullable
    private final AEKeyType type;
    @Nullable
    private final AEItemKeyStackHandler itemStackHandler;
    @Nullable
    private final AEFluidKeyStackHandler fluidStackHandler;
    @Nullable
    private final AEManaKeyHandler manaHandler;

    @NotNull
    private LazyOptional<MEStorage> capabilityStorage = LazyOptional.of(() -> this);
    @NotNull
    private LazyOptional<ManaReceiver> capabilityMana;

    public MultiblockMEStorageMachine(MetaMachineBlockEntity holder, @Nullable AEKeyType type) {
        super(holder);
        this.type = type;
        if (type == AEKeyType.items() || type == null) {
            itemStackHandler = new AEItemKeyStackHandler();
            itemStackHandler.setMap(keyMap);
            itemStackHandler.setStorage(this);
            itemStackHandler.setStorageSupplier(storageSupplier);
            itemStackHandler.setOnChange(onChange);
        } else {
            itemStackHandler = null;
        }
        if (type == AEKeyType.fluids() || type == null) {
            fluidStackHandler = new AEFluidKeyStackHandler();
            fluidStackHandler.setMap(keyMap);
            fluidStackHandler.setStorage(this);
            fluidStackHandler.setStorageSupplier(storageSupplier);
            fluidStackHandler.setOnChange(onChange);
        } else {
            fluidStackHandler = null;
        }
        if (type == null) {
            manaHandler = new AEManaKeyHandler();
            manaHandler.setMap(keyMap);
            manaHandler.setStorageSupplier(storageSupplier);
            manaHandler.setOnChange(onChange);
            capabilityMana = LazyOptional.of(() -> manaHandler);
        } else {
            manaHandler = null;
            capabilityMana = LazyOptional.empty();
        }
    }

    @Override
    @Nullable
    public ICustomItemStackHandler getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return isFormed ? itemStackHandler : null;
    }

    @Override
    @Nullable
    public ICustomFluidStackHandler getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return isFormed ? fluidStackHandler : null;
    }

    @Override
    public Supplier<BlockPattern>[] getPattern() {
        if (getLevel() != null && updateStructureDimensions()) {
            if (lDist < 1) lDist = 1;
            if (rDist < 1) rDist = 1;
            if (uDist < 1) uDist = 1;
            if (dDist < 1) dDist = 1;
            if (bDist < MIN_DEPTH) bDist = MIN_DEPTH;
            var iWidth = lDist + rDist;
            var iHeight = uDist + dDist;
            var width = iWidth + 1;
            var height = iHeight + 1;
            var backLayer = new String[height];
            for (int y = 0; y < height; y++) {
                var row = new StringBuilder(width);
                row.repeat("W", width);
                backLayer[y] = row.toString();
            }
            var storageLayer = new String[height];
            for (int y = 0; y < height; y++) {
                var row = new StringBuilder(width);
                for (int x = 0; x < width; x++) {
                    if (x == 0 || x == iWidth || y == 0 || y == iHeight) {
                        row.append('W');
                    } else {
                        row.append('S');
                    }
                }
                storageLayer[y] = row.toString();
            }
            var frontLayer = new String[height];
            for (int y = 0; y < height; y++) {
                var row = new StringBuilder(width);
                for (int x = 0; x < width; x++) {
                    if (x == lDist && y == dDist) {
                        row.append('C');
                    } else {
                        row.append('W');
                    }
                }
                frontLayer[y] = row.toString();
            }

            int interiorWidth = iWidth - 1;
            int interiorHeight = iHeight - 1;
            int interiorDepth = bDist - 1;
            cells = interiorWidth * interiorHeight * interiorDepth;
            return new Supplier[] { () -> FactoryBlockPattern.start()
                    .aisle(backLayer)
                    .aisle(storageLayer).setRepeatable(interiorDepth)
                    .aisle(frontLayer)
                    .where('C', Predicates.controller(getDefinition()))
                    .where('W', Predicates.blocks(GTBlocks.STEEL_HULL.get()).or(blocks(GTOMachines.VAULT_HATCH.get()).setMaxGlobalLimited(cells)))
                    .where('S', GTOPredicates.hermeticCasing())
                    .build()
            };
        }
        return super.getPattern();
    }

    private boolean updateStructureDimensions() {
        var world = getLevel();
        if (world == null) return false;
        var controllerPos = getPos();
        var front = getFrontFacing();
        var back = front.getOpposite();
        var left = front.getCounterClockWise();
        var right = left.getOpposite();
        var up = Direction.UP;
        var down = Direction.DOWN;
        lDist = getBlockDistance(world, controllerPos, PREDICATE, left, MAX_DEPTH);
        if (lDist < 1) return false;
        rDist = getBlockDistance(world, controllerPos, PREDICATE, right, MAX_DEPTH - lDist);
        if (rDist < 1) return false;
        uDist = getBlockDistance(world, controllerPos, PREDICATE, up, MAX_DEPTH);
        if (uDist < 1) return false;
        dDist = getBlockDistance(world, controllerPos, PREDICATE, down, MAX_DEPTH - uDist);
        if (dDist < 1) return false;
        bDist = getBlockDistance(world, controllerPos, s -> {
            if (PREDICATE.test(s)) return true;
            return BlockMap.test(s.getBlock(), BlockMap.HERMETIC_CASING);
        }, back, MAX_DEPTH);
        return bDist >= MIN_DEPTH;
    }

    private static int getBlockDistance(Level world, BlockPos pos, Predicate<BlockState> predicate, Direction direction, int maxDepth) {
        var mutable = pos.mutable();
        var distance = 0;
        for (int i = 1; i <= maxDepth; i++) {
            if (predicate.test(world.getBlockState(mutable.move(direction)))) {
                distance = i;
            } else {
                break;
            }
        }
        return distance;
    }

    @Override
    public void onStructureFormed() {
        capacity = cells * 800L * (getMultiblockState().getMatchContext().getOrDefault(GTORecipeDataKeys.HERMETIC_CASING_TIER, 0) + 1);
        if (type == AEKeyType.items()) capacity *= 4;
        if (itemStackHandler != null) itemStackHandler.setCapacity(capacity);
        if (fluidStackHandler != null) fluidStackHandler.setCapacity(capacity);
        if (manaHandler != null) manaHandler.setCapacity(capacity);
        super.onStructureFormed();
        notifyNeighborsUpdate();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        capacity = 0;
        if (itemStackHandler != null) itemStackHandler.setCapacity(0);
        if (fluidStackHandler != null) fluidStackHandler.setCapacity(0);
        if (manaHandler != null) manaHandler.setCapacity(0);
        this.notifyNeighborsUpdate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        capabilityStorage = LazyOptional.of(() -> this);
        if (manaHandler != null) {
            capabilityMana = LazyOptional.of(() -> manaHandler);
            manaHandler.setPos(getPos());
            manaHandler.setLevel(getLevel());
        }
        long totalAmount = 0;
        for (var e : keyMap) {
            totalAmount += e.getLongValue() / (e.getKey().getAmountPerByte() / 8);
        }
        this.storage = totalAmount;
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (manaHandler != null) manaHandler.setLevel(null);
        capabilityStorage.invalidate();
        capabilityMana.invalidate();
    }

    @Override
    public void loadFromItem(CompoundTag tag) {
        if (tag.get("keymap") instanceof ByteArrayTag byteArrayTag) {
            getFieldDataManager().readFieldFromData(Data.readData(byteArrayTag.getAsByteArray()), 0, "keyMap");
        }
    }

    @Override
    public void saveToItem(CompoundTag tag) {
        tag.putByteArray("keymap", getFieldDataManager().writeFieldToData("keyMap").writeToBytes());
    }

    @Override
    public @Nullable <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!isFormed) return null;
        if (cap == Capabilities.STORAGE) {
            if (side == null || side == getFrontFacing()) {
                return capabilityStorage.cast();
            }
            return LazyOptional.empty();
        } else if (cap == BotaniaForgeCapabilities.MANA_RECEIVER) {
            if (side == null || side == getFrontFacing()) {
                return capabilityMana.cast();
            }
            return LazyOptional.empty();
        }
        return null;
    }

    @Override
    public Component getDescription() {
        return getDefinition().asItem().getDescription();
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return capacity > storage;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        var type = what.getType();
        if (!isFormed || (this.type != null && type != this.type)) return 0;
        amount = Math.min((type.getAmountPerByte() / 8) * (capacity - storage), amount);
        if (amount < 1) return 0;
        if (mode == Actionable.MODULATE) {
            keyMap.insert(what, amount);
            saveChanges();
        }
        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (mode == Actionable.MODULATE) {
            var extract = keyMap.extract(what, amount);
            if (extract > 0) saveChanges();
            return extract;
        } else {
            return Math.min(amount, keyMap.getAmount(what));
        }
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        var map = keyMap;
        if (map.isEmpty()) return;
        out.addAll(map.size(), m -> map.fastForEach(m::insert));
    }

    private void saveChanges() {
        holder.setChanged();
        long totalAmount = 0;
        for (var e : keyMap) {
            totalAmount += e.getLongValue() / (e.getKey().getAmountPerByte() / 8);
        }
        this.storage = totalAmount;
    }

    @Override
    public void appendWailaTooltip(CompoundTag compoundTag, ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        var ints = compoundTag.getIntArray("dimensions");
        if (ints.length != 0) iTooltip.add(Component.translatable("gtceu.multiblock.dimensions.1", ints[0], ints[1], ints[2]));
        var capacity = compoundTag.getLong("capacity");
        var storage = compoundTag.getLong("storage");
        iTooltip.add(Component.translatable("gtocore.lang.template.capacity.-990262758", FormattingUtil.formatNumbers(capacity)));
        iTooltip.add(Component.translatable("ae2.gto_extension.craft_used_percent", FormattingUtil.formatNumbers(storage * 100D / capacity)));
    }

    @Override
    public void appendWailaData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        compoundTag.putLong("capacity", capacity);
        compoundTag.putLong("storage", storage);
        if (!isFormed) return;
        compoundTag.putIntArray("dimensions", new int[] { lDist + rDist + 1, uDist + dDist + 1, bDist + 1 });
    }
}
