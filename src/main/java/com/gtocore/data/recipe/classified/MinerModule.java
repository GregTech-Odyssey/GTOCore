package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.utils.holder.IntObjectHolder;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import appeng.core.definitions.AEBlocks;
import earth.terrarium.adastra.common.registry.ModFluids;

import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.MINER_MODULE_RECIPES;

final class MinerModule {

    private static final Item[] drones = {
            GTOItems.SPACE_DRONE_MK1.asItem(), GTOItems.SPACE_DRONE_MK2.asItem(), GTOItems.SPACE_DRONE_MK3.asItem(),
            GTOItems.SPACE_DRONE_MK4.asItem(), GTOItems.SPACE_DRONE_MK5.asItem(), GTOItems.SPACE_DRONE_MK6.asItem()
    };
    private static final FluidStack[][] fuels = {
            { GTMaterials.RocketFuel.getFluid(10000), GTOMaterials.RocketFuelRp1.getFluid(6000) },
            { GTOMaterials.RocketFuelRp1.getFluid(10000), GTOMaterials.DenseHydrazineFuelMixture.getFluid(6000) },
            { GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000), GTOMaterials.RocketFuelH8n4c2o4.getFluid(6000) },
            { GTOMaterials.RocketFuelCn3h7o3.getFluid(10000), GTOMaterials.RocketFuelH8n4c2o4.getFluid(6000) },
            { GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000), new FluidStack(ModFluids.CRYO_FUEL.get(), 6000) },
            { new FluidStack(ModFluids.CRYO_FUEL.get(), 10000), GTOMaterials.StellarEnergyRocketFuel.getFluid(6000) }
    };
    private static final int[] baseTime = { 600, 400 };

    @SafeVarargs
    private static void buildMinerModuleInputOres(int circuit, int minDroneTire, IntObjectHolder<Material>... oreHolders) {
        ItemStack[] blocks = new ItemStack[oreHolders.length];
        for (int i = 0; i < oreHolders.length; i++) {
            IntObjectHolder<Material> holder = oreHolders[i];
            blocks[i] = ChemicalHelper.get(TagPrefix.ore, holder.obj, holder.number);
        }
        buildMinerModule(circuit, minDroneTire, blocks);
    }

    private static void buildMinerModule(int circuit, int minDroneTire, ItemStack[] blocks) {
        for (int i = minDroneTire - 1; i < 6; i++) {
            for (int j = 0; j < fuels[minDroneTire - 1].length; j++) {
                var build = MINER_MODULE_RECIPES.recipeBuilder("space_ore_" + circuit + "_" + i + "_" + j)
                        .notConsumable(drones[i], 16)
                        .circuitMeta(circuit)
                        .inputFluids(fuels[minDroneTire - 1][j])
                        .EUt(VA[8 + i])
                        .duration(baseTime[j] >> i);
                for (ItemStack block : blocks) build.outputItems(block);
                build.save();
            }
        }
    }

    public static void init() {
        buildMinerModuleInputOres(1, 1, ore(280, Tetrahedrite), ore(140, Copper), ore(60, Bentonite), ore(40, Magnetite), ore(40, Olivine), ore(20, GlauconiteSand));
        buildMinerModuleInputOres(2, 1, ore(180, Almandine), ore(120, Pyrope), ore(60, Sapphire), ore(60, GreenSapphire), ore(70, Stibnite), ore(120, Uraninite));
        buildMinerModuleInputOres(3, 1, ore(90, Bastnasite), ore(30, Molybdenum), ore(60, Goethite), ore(240, YellowLimonite), ore(240, Hematite), ore(120, Malachite));
        buildMinerModuleInputOres(4, 1, ore(120, Soapstone), ore(80, Talc), ore(80, GlauconiteSand), ore(40, Pentlandite), ore(30, Neodymium), ore(60, Monazite));
        buildMinerModuleInputOres(5, 1, ore(180, Redstone), ore(120, Ruby), ore(60, Grossular), ore(40, Spessartine), ore(40, Ostrum), ore(20, Tantalite));
        buildMinerModule(6, 1, new ItemStack[] { ChemicalHelper.get(TagPrefix.ore, Chalcopyrite, 250), ChemicalHelper.get(TagPrefix.ore, Zeolite, 10), ChemicalHelper.get(TagPrefix.ore, Cassiterite, 10), ChemicalHelper.get(TagPrefix.ore, Realgar, 50), ChemicalHelper.get(TagPrefix.ore, Cinnabar, 60), new ItemStack(AEBlocks.SKY_STONE_BLOCK.block().asItem(), 80) });
        buildMinerModuleInputOres(7, 1, ore(120, Saltpeter), ore(80, Diatomite), ore(80, Electrotine), ore(40, Alunite), ore(240, Coal), ore(40, Rubidium));
        buildMinerModuleInputOres(8, 1, ore(90, Beryllium), ore(120, Emerald), ore(40, Chalcopyrite), ore(160, Iron), ore(160, Pyrite), ore(160, Copper));
        buildMinerModuleInputOres(9, 1, ore(60, Grossular), ore(40, Pyrolusite), ore(20, Tantalite), ore(240, Magnetite), ore(160, VanadiumMagnetite), ore(80, Gold));
        buildMinerModuleInputOres(10, 1, ore(120, Lazurite), ore(80, Sodalite), ore(80, Lapis), ore(40, Calcite), ore(150, Wulfenite), ore(30, Calorite));
        buildMinerModuleInputOres(11, 1, ore(120, Galena), ore(80, Silver), ore(40, Lead), ore(100, Molybdenite), ore(50, Molybdenum), ore(50, Powellite));
        buildMinerModuleInputOres(12, 1, ore(140, Zircon), ore(60, YellowLimonite), ore(60, Kyanite), ore(40, Mica), ore(40, Bauxite), ore(20, Pollucite));
        buildMinerModuleInputOres(13, 1, ore(60, Desh), ore(80, CertusQuartz), ore(90, Goethite), ore(160, Cassiterite), ore(60, Hematite), ore(30, Gold));
        buildMinerModuleInputOres(14, 1, ore(40, Barite), ore(120, GarnetRed), ore(80, GarnetYellow), ore(80, Amethyst), ore(40, Opal), ore(20, AlienAlgae));
        buildMinerModuleInputOres(15, 1, ore(210, BlueTopaz), ore(140, Topaz), ore(240, BasalticMineralSand), ore(160, GraniticMineralSand), ore(160, FullersEarth), ore(80, Gypsum));
        buildMinerModuleInputOres(16, 1, ore(150, RockSalt), ore(10, Salt), ore(50, Lepidolite), ore(50, Spodumene), ore(140, Chalcocite), ore(70, Bornite));
        buildMinerModule(17, 1, new ItemStack[] { ChemicalHelper.get(TagPrefix.ore, Redstone, 180), ChemicalHelper.get(TagPrefix.ore, Ruby, 120), ChemicalHelper.get(TagPrefix.ore, Cinnabar, 60), ChemicalHelper.get(TagPrefix.ore, NetherQuartz, 240), ChemicalHelper.get(TagPrefix.ore, Quartzite, 80), new ItemStack(Blocks.ANCIENT_DEBRIS.asItem(), 50) });
        buildMinerModuleInputOres(18, 1, ore(120, Apatite), ore(80, TricalciumPhosphate), ore(40, Pyrochlore), ore(300, Sulfur), ore(200, Pyrite), ore(100, Sphalerite));
        buildMinerModuleInputOres(19, 1, ore(180, Magnetite), ore(120, VanadiumMagnetite), ore(240, CassiteriteSand), ore(160, GarnetSand), ore(160, Asbestos), ore(80, Diatomite));
        buildMinerModuleInputOres(20, 1, ore(240, Oilsands), ore(60, Gold), ore(80, InfusedGold), ore(160, Bauxite), ore(80, Ilmenite), ore(80, Aluminium));
        buildMinerModuleInputOres(21, 1, ore(60, Bornite), ore(40, Cooperite), ore(120, Graphite), ore(80, Diamond), ore(40, Coal), ore(40, Titanium));
        buildMinerModuleInputOres(22, 1, ore(120, Garnierite), ore(80, Nickel), ore(80, Cobaltite), ore(40, Pentlandite), ore(40, Platinum), ore(20, Palladium));
        buildMinerModuleInputOres(23, 1, ore(120, Scheelite), ore(80, Tungstate), ore(40, Lithium), ore(20, Tellurium), ore(30, Tungsten), ore(180, Pitchblende));
        buildMinerModuleInputOres(24, 1, ore(180, Naquadah), ore(120, Chromite), ore(60, Plutonium239), ore(30, NaquadahEnriched), ore(90, TriniumCompound), ore(30, Indium));
        buildMinerModuleInputOres(25, 2, ore(20, Jasper), ore(140, GarnetRed), ore(60, Topaz), ore(40, Emerald), ore(40, Amethyst), ore(20, Celestine));
        buildMinerModuleInputOres(26, 3, ore(140, Iron), ore(140, Tin), ore(60, Nickel), ore(60, Uruium), ore(40, Force), ore(20, Cobalt));
        buildMinerModuleInputOres(27, 3, ore(120, Bloodstone), ore(80, Redstone), ore(120, GarnetRed), ore(40, Ruby), ore(40, Almandine), ore(40, Pyrope));
        buildMinerModuleInputOres(28, 4, ore(80, Naquadah), ore(40, AdamantineCompounds), ore(60, RareEarthMetal), ore(40, Monazite), ore(40, Bastnasite), ore(20, NaquadahEnriched));
        buildMinerModuleInputOres(29, 4, ore(40, GnomeCrystal), ore(40, SalamanderCrystal), ore(80, Uraninite), ore(40, Orichalcum), ore(60, Mithril), ore(80, Salt));
        buildMinerModuleInputOres(30, 5, ore(80, Enderium), ore(120, Sodalite), ore(60, Celestine), ore(80, Lapis), ore(60, Bauxite), ore(40, Pitchblende));
        buildMinerModuleInputOres(31, 5, ore(40, Silver), ore(60, Platinum), ore(60, Tartarite), ore(80, Vibranium), ore(120, Aluminium), ore(120, Iron));
        buildMinerModuleInputOres(32, 6, ore(12, Lazurite), ore(80, Sapphire), ore(60, Starmetal), ore(80, GreenSapphire), ore(120, GarnetYellow), ore(80, Pollucite));
    }

    private static IntObjectHolder<Material> ore(int count, Material mat) {
        return new IntObjectHolder<>(count, mat);
    }
}
