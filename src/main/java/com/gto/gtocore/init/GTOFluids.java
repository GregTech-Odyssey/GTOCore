package com.gto.gtocore.init;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.fluid.GelidCryotheumFluid;
import com.gto.gtocore.common.fluid.types.GelidCryotheumFluidType;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface GTOFluids {

    DeferredRegister<FluidType> FLUID_TYPE = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, GTOCore.MOD_ID);
    RegistryObject<FluidType> GELID_CRYOTHEUM_TYPE = FLUID_TYPE.register("gelid_cryotheum", GelidCryotheumFluidType::new);

    DeferredRegister<Fluid> FLUID = DeferredRegister.create(ForgeRegistries.FLUIDS, GTOCore.MOD_ID);
    RegistryObject<FlowingFluid> GELID_CRYOTHEUM = FLUID.register("gelid_cryotheum", GelidCryotheumFluid.Source::new);
    RegistryObject<FlowingFluid> FLOWING_GELID_CRYOTHEUM = FLUID.register("flowing_gelid_cryotheum", GelidCryotheumFluid.Flowing::new);
}
