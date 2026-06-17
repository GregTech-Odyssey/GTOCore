package com.gtocore.common.machine.tesseract;

import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.api.transfer.fluid.FluidHandlerList;
import com.gregtechceu.gtceu.api.transfer.fluid.ICustomFluidStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.ICustomItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.ItemHandlerList;
import com.gregtechceu.gtceu.utils.LazyOptionalUtil;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IMultiTesseract extends IMachineFeature, ITesseractMarkerInteractable {

    @Override
    @Nullable
    default <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (isCalled()) return null;
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            getItemHandlers().clear();
            var size = getTotalBlockEntities();
            for (int i = 0; i < size; i++) {
                var c = getBlockEntity(i);
                if (c != null) {
                    setCalled(true);
                    var h = LazyOptionalUtil.get(c.getCapability(ForgeCapabilities.ITEM_HANDLER, getSideForBlockEntity(i, side)));
                    setCalled(false);
                    if (h instanceof ICustomItemStackHandler handler) {
                        getItemHandlers().add(handler);
                    }
                }
            }
            var s = getItemHandlers().size();
            if (s > 0) {
                var result = s > 1 ? new ItemHandlerList(getItemHandlers().toArray(new ICustomItemStackHandler[0])) : getItemHandlers().getFirst();
                if (side != null) {
                    CoverBehavior cover = self().getCoverContainer().getCoverAtSide(side);
                    if (cover != null && result instanceof ICustomItemStackHandler modifiable) {
                        result = cover.getItemHandlerCap(modifiable);
                    }
                }
                IItemHandler finalResult = result;
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(finalResult == null ? null : () -> finalResult));
            }
        } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
            getFluidHandlers().clear();
            var size = getTotalBlockEntities();
            for (int i = 0; i < size; i++) {
                var c = getBlockEntity(i);
                if (c != null) {
                    setCalled(true);
                    var h = LazyOptionalUtil.get(c.getCapability(ForgeCapabilities.FLUID_HANDLER, getSideForBlockEntity(i, side)));
                    setCalled(false);
                    if (h instanceof ICustomFluidStackHandler handler) {
                        getFluidHandlers().add(handler);
                    }
                }
            }
            var s = getFluidHandlers().size();
            if (s > 0) {
                var result = s > 1 ? new FluidHandlerList(getFluidHandlers().toArray(new ICustomFluidStackHandler[0])) : getFluidHandlers().getFirst();
                if (side != null) {
                    CoverBehavior cover = self().getCoverContainer().getCoverAtSide(side);
                    if (cover != null && result instanceof ICustomFluidStackHandler modifiable) {
                        result = cover.getFluidHandlerCap(modifiable);
                    }
                }
                IFluidHandler finalResult = result;
                return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, LazyOptional.of(finalResult == null ? null : () -> finalResult));
            }
        }
        return null;
    }

    List<ICustomItemStackHandler> getItemHandlers();

    List<ICustomFluidStackHandler> getFluidHandlers();

    boolean isCalled();

    void setCalled(boolean call);

    @Nullable
    BlockEntity getBlockEntity(int i);

    int getTotalBlockEntities();

    default Direction getSideForBlockEntity(int i, @Nullable Direction side) {
        return side;
    }
}
