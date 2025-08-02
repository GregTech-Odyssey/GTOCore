package com.gtocore.common.machine.electric;

import com.gtolib.api.machine.part.ItemHatchPartMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
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
import java.util.Set;

public class TesseractMachine extends MetaMachine implements IFancyUIMachine {

    private static final Set<Capability<?>> CAPABILITIES = Set.of(ForgeCapabilities.ITEM_HANDLER, ForgeCapabilities.FLUID_HANDLER);

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            TesseractMachine.class, MetaMachine.MANAGED_FIELD_HOLDER);

    private WeakReference<BlockEntity> blockEntityReference;

    @Persisted
    public BlockPos pos;

    @Persisted
    protected NotifiableItemStackHandler inventory;

    public TesseractMachine(IMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.NONE);
        inventory.storage.setOnContentsChanged(() -> {
            pos = null;
            blockEntityReference = null;
            ItemStack card = inventory.storage.getStackInSlot(0);
            if (card.isEmpty()) return;
            CompoundTag posTags = card.getTag();
            if (posTags == null || !posTags.contains("posX") || !posTags.contains("posY") || !posTags.contains("posZ")) return;
            var pos = new BlockPos(posTags.getInt("posX"), posTags.getInt("posY"), posTags.getInt("posZ"));
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
        if (pos != null && CAPABILITIES.contains(cap)) {
            if (blockEntityReference == null) {
                var be = getLevel().getBlockEntity(pos);
                if (be != null) {
                    blockEntityReference = new WeakReference<>(be);
                    return be.getCapability(cap, side);
                } else {
                    pos = null;
                }
            } else {
                var blockEntity = blockEntityReference.get();
                if (blockEntity == null || blockEntity.isRemoved()) {
                    blockEntity = getLevel().getBlockEntity(pos);
                    if (blockEntity != null) {
                        blockEntityReference = new WeakReference<>(blockEntity);
                        return blockEntity.getCapability(cap, side);
                    } else {
                        pos = null;
                    }
                } else {
                    return blockEntity.getCapability(cap, side);
                }
            }
        }
        return null;
    }
}
