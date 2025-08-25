package com.gtocore.common.machine.electric;

import com.gtolib.api.machine.part.ItemHatchPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

public class TesseractMachine extends MetaMachine implements IFancyUIMachine, IMachineModifyDrops {

    private static final Set<Capability<?>> CAPABILITIES = Set.of(ForgeCapabilities.ITEM_HANDLER, ForgeCapabilities.FLUID_HANDLER);

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            TesseractMachine.class, MetaMachine.MANAGED_FIELD_HOLDER);

    @Override
    public void onDrops(List<ItemStack> list) {
        clearInventory(inventory.storage);
    }

    private WeakReference<BlockEntity> blockEntityReference;

    @Persisted
    public BlockPos pos;

    @Persisted
    protected NotifiableItemStackHandler inventory;

    private boolean call;

    public TesseractMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.NONE);
        inventory.storage.setOnContentsChangedAndfreeze(() -> {
            call = false;
            pos = null;
            blockEntityReference = null;
            ItemStack card = inventory.storage.getStackInSlot(0);
            if (card.isEmpty()) return;
            CompoundTag posTags = card.getTag();
            if (posTags == null || !posTags.contains("x") || !posTags.contains("y") || !posTags.contains("z")) return;
            var pos = new BlockPos(posTags.getInt("x"), posTags.getInt("y"), posTags.getInt("z"));
            if (pos.equals(getPos())) return;
            this.pos = pos;
        });
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public Widget createUIWidget() {
        return ItemHatchPartMachine.createSLOTWidget(inventory);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (call) return null;
        if (pos != null && CAPABILITIES.contains(cap)) {
            LazyOptional<T> result = null;
            call = true;
            if (blockEntityReference == null) {
                var be = getLevel().getBlockEntity(pos);
                if (be != null) {
                    blockEntityReference = new WeakReference<>(be);
                    result = be.getCapability(cap, side);
                } else {
                    pos = null;
                }
            } else {
                var blockEntity = blockEntityReference.get();
                if (blockEntity == null || blockEntity.isRemoved()) {
                    blockEntity = getLevel().getBlockEntity(pos);
                    if (blockEntity != null) {
                        blockEntityReference = new WeakReference<>(blockEntity);
                        result = blockEntity.getCapability(cap, side);
                    } else {
                        pos = null;
                    }
                } else {
                    result = blockEntity.getCapability(cap, side);
                }
            }
            call = false;
            return result;
        }
        return null;
    }
}
