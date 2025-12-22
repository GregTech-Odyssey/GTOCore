package com.gtocore.common.data;

import com.gtocore.common.fluid.GelidCryotheumFluid;
import com.gtocore.common.fluid.types.GelidCryotheumFluidType;

import com.gtolib.GTOCore;
import com.gtolib.api.registries.GTORegistration;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.versions.forge.ForgeVersion;

import com.fast.fastcollection.O2OOpenCacheHashMap;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.FluidEntry;

import java.util.Map;

public final class GTOFluids {

    public static final Map<String, String> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    public static final DeferredRegister<FluidType> FLUID_TYPE = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, GTOCore.MOD_ID);
    public static final RegistryObject<FluidType> GELID_CRYOTHEUM_TYPE = FLUID_TYPE.register("gelid_cryotheum", GelidCryotheumFluidType::new);

    public static final DeferredRegister<Fluid> FLUID = DeferredRegister.create(ForgeRegistries.FLUIDS, GTOCore.MOD_ID);
    public static final RegistryObject<FlowingFluid> GELID_CRYOTHEUM = FLUID.register("gelid_cryotheum", GelidCryotheumFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_GELID_CRYOTHEUM = FLUID.register("flowing_gelid_cryotheum", GelidCryotheumFluid.Flowing::new);

    public static final FluidEntry<? extends ForgeFlowingFluid> NUTRIENT_DISTILLATION = fluid("nutrient_distillation", "营养精华液")
            .properties(p -> p.density(1500).viscosity(3000))
            .register();

    public static final FluidEntry<? extends ForgeFlowingFluid> DEW_OF_THE_VOID = fluid("dew_of_the_void", "虚空露水")
            .properties(p -> p.density(200).viscosity(1000).temperature(175))
            .lang("Fluid of the Void")
            .register();

    public static final FluidEntry<? extends ForgeFlowingFluid> VAPOR_OF_LEVITY = gasFluid("vapor_of_levity", "轻盈之气")
            .properties(p -> p.density(-10).viscosity(100).temperature(5))
            .register();

    public static final FluidEntry<? extends ForgeFlowingFluid> HOOTCH = fluid("hootch", "烈酒")
            .properties(p -> p.density(900).viscosity(1000))
            .register();

    public static final FluidEntry<? extends ForgeFlowingFluid> FIRE_WATER = fluid("fire_water", "火焰水")
            .properties(p -> p.density(900).viscosity(1000).temperature(2000))
            .register();

    public static final FluidEntry<? extends ForgeFlowingFluid> XP_JUICE = fluid("xp_juice", "经验汁")
            .properties(p -> p.lightLevel(10).density(800).viscosity(1500))
            .lang("XP Juice")
            .register();

    public static final FluidEntry<? extends ForgeFlowingFluid> LIQUID_SUNSHINE = fluid("liquid_sunshine", "液态阳光")
            .properties(p -> p.density(200).viscosity(400))
            .register();

    public static final FluidEntry<? extends ForgeFlowingFluid> CLOUD_SEED = fluid("cloud_seed", "云之精华")
            .properties(p -> p.density(500).viscosity(800))
            .register();

    public static final FluidEntry<? extends ForgeFlowingFluid> CLOUD_SEED_CONCENTRATED = fluid("cloud_seed_concentrated", "浓缩云之精华")
            .properties(p -> p.density(1000).viscosity(1200))
            .register();

    private static FluidBuilder<? extends ForgeFlowingFluid, Registrate> fluid(String name, String cn) {
        return baseFluid(name, cn)
                .bucket()
                .model(GTOFluids::bucketModel)
                .tab(GTOCreativeModeTabs.GTO_MATERIAL_FLUID.getKey())
                .build();
    }

    private static FluidBuilder<? extends ForgeFlowingFluid, Registrate> gasFluid(String name, String cn) {
        return baseFluid(name, cn)
                .bucket()
                .model((ctx, prov) -> bucketModel(ctx, prov).flipGas(true))
                .tab(GTOCreativeModeTabs.GTO_MATERIAL_FLUID.getKey())
                .build();
    }

    private static FluidBuilder<? extends ForgeFlowingFluid, Registrate> baseFluid(String name, String cn) {
        var thing = GTORegistration.GTO.fluid(name, GTOCore.id("block/fluid/fluid_" + name + "_still"),
                GTOCore.id("block/fluid/fluid_" + name + "_flowing"));
        if (FMLEnvironment.dist.isClient()) {
            thing.renderType(RenderType::translucent);
        }
        if (LANG != null) LANG.put(name, cn);
        return thing.source(ForgeFlowingFluid.Source::new)
                .block()
                .build();
    }

    private static DynamicFluidContainerModelBuilder<ItemModelBuilder> bucketModel(DataGenContext<Item, BucketItem> ctx, RegistrateItemModelProvider prov) {
        return prov.withExistingParent(ctx.getName(), new ResourceLocation(ForgeVersion.MOD_ID, "item/bucket"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid(ctx.get().getFluid());
    }
}
