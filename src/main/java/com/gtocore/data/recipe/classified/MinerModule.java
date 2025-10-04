package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

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

    private static void buildMinerModuleInputOres(int circuit, int minDroneTire, int[] oresCount, Material... ores) {
        if (oresCount.length != ores.length) throw new IllegalStateException("MINER_MODULE_RECIPES error");
        ItemStack[] blocks = new ItemStack[ores.length];
        for (int i = 0; i < ores.length; i++) blocks[i] = ChemicalHelper.get(TagPrefix.ore, ores[i], oresCount[i]);
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
        buildMinerModuleInputOres(1, 1, new int[] { 280, 140, 60, 40, 40, 20 }, Tetrahedrite, Copper, Bentonite, Magnetite, Olivine, GlauconiteSand);
        buildMinerModuleInputOres(2, 1, new int[] { 180, 120, 60, 60, 70, 120 }, Almandine, Pyrope, Sapphire, GreenSapphire, Stibnite, Uraninite);
        buildMinerModuleInputOres(3, 1, new int[] { 90, 30, 60, 240, 240, 120 }, Bastnasite, Molybdenum, Goethite, YellowLimonite, Hematite, Malachite);
        buildMinerModuleInputOres(4, 1, new int[] { 120, 80, 80, 40, 30, 60 }, Soapstone, Talc, GlauconiteSand, Pentlandite, Neodymium, Monazite);
        buildMinerModuleInputOres(5, 1, new int[] { 180, 120, 60, 40, 40, 20 }, Redstone, Ruby, Grossular, Spessartine, Ostrum, Tantalite);
        buildMinerModule(6, 1, new ItemStack[] { ChemicalHelper.get(TagPrefix.ore, Chalcopyrite, 250), ChemicalHelper.get(TagPrefix.ore, Zeolite, 10), ChemicalHelper.get(TagPrefix.ore, Cassiterite, 10), ChemicalHelper.get(TagPrefix.ore, Realgar, 50), ChemicalHelper.get(TagPrefix.ore, Cinnabar, 60), new ItemStack(AEBlocks.SKY_STONE_BLOCK.block().asItem(), 80) });
        buildMinerModuleInputOres(7, 1, new int[] { 120, 80, 80, 40, 240, 40 }, Saltpeter, Diatomite, Electrotine, Alunite, Coal, Rubidium);
        buildMinerModuleInputOres(8, 1, new int[] { 90, 120, 40, 160, 160, 160 }, Beryllium, Emerald, Chalcopyrite, Iron, Pyrite, Copper);
        buildMinerModuleInputOres(9, 1, new int[] { 60, 40, 20, 240, 160, 80 }, Grossular, Pyrolusite, Tantalite, Magnetite, VanadiumMagnetite, Gold);
        buildMinerModuleInputOres(10, 1, new int[] { 120, 80, 80, 40, 150, 30 }, Lazurite, Sodalite, Lapis, Calcite, Wulfenite, Calorite);
        buildMinerModuleInputOres(11, 1, new int[] { 120, 80, 40, 100, 50, 50 }, Galena, Silver, Lead, Molybdenite, Molybdenum, Powellite);
        buildMinerModuleInputOres(12, 1, new int[] { 140, 60, 60, 40, 40, 20 }, Zircon, YellowLimonite, Kyanite, Mica, Bauxite, Pollucite);
        buildMinerModuleInputOres(13, 1, new int[] { 60, 80, 90, 160, 60, 30 }, Desh, CertusQuartz, Goethite, Cassiterite, Hematite, Gold);
        buildMinerModuleInputOres(14, 1, new int[] { 40, 120, 80, 80, 40, 20 }, Barite, GarnetRed, GarnetYellow, Amethyst, Opal, AlienAlgae);
        buildMinerModuleInputOres(15, 1, new int[] { 210, 140, 240, 160, 160, 80 }, BlueTopaz, Topaz, BasalticMineralSand, GraniticMineralSand, FullersEarth, Gypsum);
        buildMinerModuleInputOres(16, 1, new int[] { 150, 10, 50, 50, 140, 70 }, RockSalt, Salt, Lepidolite, Spodumene, Chalcocite, Bornite);
        buildMinerModule(17, 1, new ItemStack[] { ChemicalHelper.get(TagPrefix.ore, Redstone, 180), ChemicalHelper.get(TagPrefix.ore, Ruby, 120), ChemicalHelper.get(TagPrefix.ore, Cinnabar, 60), ChemicalHelper.get(TagPrefix.ore, NetherQuartz, 240), ChemicalHelper.get(TagPrefix.ore, Quartzite, 80), new ItemStack(Blocks.ANCIENT_DEBRIS.asItem(), 50) });
        buildMinerModuleInputOres(18, 1, new int[] { 120, 80, 40, 300, 200, 100 }, Apatite, TricalciumPhosphate, Pyrochlore, Sulfur, Pyrite, Sphalerite);
        buildMinerModuleInputOres(19, 1, new int[] { 180, 120, 240, 160, 160, 80 }, Magnetite, VanadiumMagnetite, CassiteriteSand, GarnetSand, Asbestos, Diatomite);
        buildMinerModuleInputOres(20, 1, new int[] { 240, 60, 80, 160, 80, 80 }, Oilsands, Gold, InfusedGold, Bauxite, Ilmenite, Aluminium);
        buildMinerModuleInputOres(21, 1, new int[] { 60, 40, 120, 80, 40, 40 }, Bornite, Cooperite, Graphite, Diamond, Coal, Titanium);
        buildMinerModuleInputOres(22, 1, new int[] { 120, 80, 80, 40, 40, 20 }, Garnierite, Nickel, Cobaltite, Pentlandite, Platinum, Palladium);
        buildMinerModuleInputOres(23, 1, new int[] { 120, 80, 40, 20, 30, 180 }, Scheelite, Tungstate, Lithium, Tellurium, Tungsten, Pitchblende);
        buildMinerModuleInputOres(24, 1, new int[] { 180, 120, 60, 30, 90, 30 }, Naquadah, Chromite, Plutonium239, NaquadahEnriched, TriniumCompound, Indium);
        buildMinerModuleInputOres(25, 2, new int[] { 20, 140, 60, 40, 40, 20 }, Jasper, GarnetRed, Topaz, Emerald, Amethyst, Celestine);
        buildMinerModuleInputOres(26, 3, new int[] { 140, 140, 60, 60, 40, 20 }, Iron, Tin, Nickel, Uruium, Force, Cobalt);
        buildMinerModuleInputOres(27, 3, new int[] { 120, 80, 120, 40, 40, 40 }, Bloodstone, Redstone, GarnetRed, Ruby, Almandine, Pyrope);
        buildMinerModuleInputOres(28, 4, new int[] { 80, 40, 60, 40, 40, 20 }, Naquadah, AdamantineCompounds, RareEarthMetal, Monazite, Bastnasite, NaquadahEnriched);
        buildMinerModuleInputOres(29, 4, new int[] { 40, 40, 80, 40, 60, 80 }, GnomeCrystal, SalamanderCrystal, Uraninite, Orichalcum, Mithril, Salt);
        buildMinerModuleInputOres(30, 5, new int[] { 80, 120, 60, 80, 60, 40 }, Enderium, Sodalite, Celestine, Lapis, Bauxite, Pitchblende);
        buildMinerModuleInputOres(31, 5, new int[] { 40, 60, 60, 80, 120, 120 }, Silver, Platinum, Tartarite, Vibranium, Aluminium, Iron);
        buildMinerModuleInputOres(32, 6, new int[] { 12, 80, 60, 80, 120, 80 }, Lazurite, Sapphire, Starmetal, GreenSapphire, GarnetYellow, Pollucite);
    }
}
