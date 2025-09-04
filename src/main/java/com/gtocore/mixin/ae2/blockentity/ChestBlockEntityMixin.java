package com.gtocore.mixin.ae2.blockentity;

import com.gtolib.api.ae2.CellInventoryFilter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;

import appeng.api.storage.ITerminalHost;
import appeng.api.storage.MEStorage;
import appeng.api.storage.cells.ISaveProvider;
import appeng.blockentity.grid.AENetworkPowerBlockEntity;
import appeng.blockentity.storage.ChestBlockEntity;
import appeng.capabilities.Capabilities;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.filter.IAEItemFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin extends AENetworkPowerBlockEntity implements ITerminalHost {

    protected ChestBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    @Shadow(remap = false)
    protected abstract void updateHandler();

    @Shadow(remap = false)
    private IFluidHandler fluidHandler;

    @Shadow(remap = false)
    protected abstract void onCellContentChanged();

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lappeng/util/inv/AppEngInternalInventory;setFilter(Lappeng/util/inv/filter/IAEItemFilter;)V", ordinal = 1), remap = false)
    private void setFilter(AppEngInternalInventory instance, IAEItemFilter filter) {
        instance.setFilter(new CellInventoryFilter(this::onCellContentChanged));
    }

    @ModifyArg(method = "updateHandler", at = @At(value = "INVOKE", target = "Lappeng/api/storage/StorageCells;getCellInventory(Lnet/minecraft/world/item/ItemStack;Lappeng/api/storage/cells/ISaveProvider;)Lappeng/api/storage/cells/StorageCell;"), index = 1, remap = false)
    private @NotNull ISaveProvider getSaveProvider(@Nullable ISaveProvider host) {
        return () -> this.level.blockEntityChanged(this.worldPosition);
    }

    @Inject(method = "onCellContentChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;blockEntityChanged(Lnet/minecraft/core/BlockPos;)V", remap = true), remap = false, cancellable = true)
    private void onCellContentChanged(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    private void saveAdditional(CompoundTag data, CallbackInfo ci) {
        onCellContentChanged();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        this.updateHandler();
        if (capability == ForgeCapabilities.FLUID_HANDLER && this.fluidHandler != null && facing != getFront()) {
            return (LazyOptional<T>) LazyOptional.of(() -> this.fluidHandler);
        }
        if (capability == Capabilities.STORAGE && facing != getFront()) {
            MEStorage storage = getInventory();
            if (storage != null) {
                return (LazyOptional<T>) LazyOptional.of(() -> storage);
            }
        }
        return super.getCapability(capability, facing);
    }
}
