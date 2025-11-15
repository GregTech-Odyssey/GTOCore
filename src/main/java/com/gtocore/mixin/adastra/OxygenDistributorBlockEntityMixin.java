package com.gtocore.mixin.adastra;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.sugar.Local;
import earth.terrarium.adastra.common.blockentities.machines.OxygenDistributorBlockEntity;
import earth.terrarium.adastra.common.blockentities.machines.OxygenLoaderBlockEntity;
import earth.terrarium.adastra.common.container.BiFluidContainer;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.gregtechceu.gtceu.common.data.GTMaterials.Oxygen;

@Mixin(OxygenDistributorBlockEntity.class)
public abstract class OxygenDistributorBlockEntityMixin extends OxygenLoaderBlockEntity {

    @Unique
    private WrappedBlockFluidContainer gtocore$fluidContainer;

    public OxygenDistributorBlockEntityMixin(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Redirect(method = { "canCraftDistribution", "consumeDistribution" }, at = @At(value = "INVOKE", target = "Learth/terrarium/adastra/common/container/BiFluidContainer;output()Learth/terrarium/botarium/common/fluid/impl/SimpleFluidContainer;", remap = false), remap = false)
    private SimpleFluidContainer redirectCanCraftDistribution(BiFluidContainer instance) {
        return instance.input();
    }

    @Redirect(method = { "canCraftDistribution", "consumeDistribution" }, at = @At(value = "INVOKE", target = "Learth/terrarium/botarium/common/fluid/impl/SimpleFluidContainer;internalExtract(Learth/terrarium/botarium/common/fluid/base/FluidHolder;Z)Learth/terrarium/botarium/common/fluid/base/FluidHolder;", remap = false), remap = false)
    private FluidHolder redirectExtract(SimpleFluidContainer instance, FluidHolder fluid, boolean simulate, @Local(argsOnly = true) long fluidAmount) {
        return instance.internalExtract(FluidHolder.of(Oxygen.getFluid(), fluidAmount), simulate);
    }

    @Override
    public WrappedBlockFluidContainer getFluidContainer() {
        return this.gtocore$fluidContainer != null ? this.gtocore$fluidContainer : (this.gtocore$fluidContainer = new WrappedBlockFluidContainer(this,
                new BiFluidContainer(FluidConstants.fromMillibuckets(1024000), 1, 1, (tank, holder) -> holder.getFluid() == Oxygen.getFluid(), (a, b) -> true)));
    }
}
