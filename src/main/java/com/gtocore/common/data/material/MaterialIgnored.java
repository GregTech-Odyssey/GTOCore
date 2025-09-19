package com.gtocore.common.data.material;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.enderio.base.common.init.EIOBlocks;
import com.enderio.base.common.init.EIOItems;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import earth.terrarium.adastra.common.registry.ModBlocks;
import earth.terrarium.adastra.common.registry.ModItems;
import io.github.lounode.extrabotany.common.block.ExtraBotanyBlocks;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static committee.nova.mods.avaritia.init.registry.ModBlocks.infinity;
import static committee.nova.mods.avaritia.init.registry.ModItems.*;
import static mythicbotany.register.ModBlocks.alfsteelBlock;
import static mythicbotany.register.ModItems.alfsteelIngot;
import static mythicbotany.register.ModItems.alfsteelNugget;

public final class MaterialIgnored {

    public static void init() {
        dustSmall.setIgnored(Mana);
        dustTiny.setIgnored(Mana);
        plateDouble.setIgnored(Livingwood);
        plateDouble.setIgnored(Dreamwood);
        plateDouble.setIgnored(Shimmerwood);
        plateDouble.setIgnored(Livingclay);
        plateDouble.setIgnored(Livingrock);
        plateDouble.setIgnored(Runerock);
        plateDouble.setIgnored(Shimmerrock);
        plateDouble.setIgnored(ManaGlass);
        plateDouble.setIgnored(ElfGlass);
        plateDouble.setIgnored(BifrostPerm);

        OreProperty oreProp = Bastnasite.getProperty(PropertyKey.ORE);
        oreProp.getOreByProducts().clear();
        oreProp.setOreByProducts(Neodymium, Monazite);

        oreProp = Redstone.getProperty(PropertyKey.ORE);
        oreProp.getOreByProducts().clear();
        oreProp.setOreByProducts(Cinnabar, Ruby, Glowstone);

        oreProp = Neodymium.getProperty(PropertyKey.ORE);
        oreProp.getOreByProducts().clear();
        oreProp.setOreByProducts(SamariumRefinedPowder);

        oreProp = Monazite.getProperty(PropertyKey.ORE);
        oreProp.getOreByProducts().clear();
        oreProp.setOreByProducts(Thorium, Neodymium, Bastnasite);

        TagPrefix.gem.setIgnored(Fluix, () -> AEItems.FLUIX_CRYSTAL);
        TagPrefix.block.setIgnored(Fluix, AEBlocks.FLUIX_BLOCK::block);
        TagPrefix.dust.setIgnored(Fluix, () -> AEItems.FLUIX_DUST);

        TagPrefix.gem.setIgnored(CertusQuartz, () -> AEItems.CERTUS_QUARTZ_CRYSTAL);
        TagPrefix.block.setIgnored(CertusQuartz, AEBlocks.QUARTZ_BLOCK::block);
        TagPrefix.dust.setIgnored(CertusQuartz, () -> AEItems.CERTUS_QUARTZ_DUST);

        TagPrefix.rawOre.setIgnored(Desh, ModItems.RAW_DESH);
        TagPrefix.rawOre.setIgnored(Ostrum, ModItems.RAW_OSTRUM);
        TagPrefix.rawOre.setIgnored(Calorite, ModItems.RAW_CALORITE);
        TagPrefix.rawOreBlock.setIgnored(Desh, ModBlocks.RAW_DESH_BLOCK);
        TagPrefix.rawOreBlock.setIgnored(Ostrum, ModBlocks.RAW_OSTRUM_BLOCK);
        TagPrefix.rawOreBlock.setIgnored(Calorite, ModBlocks.RAW_CALORITE_BLOCK);
        TagPrefix.plate.setIgnored(Iron, ModItems.IRON_PLATE);
        TagPrefix.rod.setIgnored(Iron, ModItems.IRON_ROD);
        TagPrefix.ingot.setIgnored(Steel, ModItems.STEEL_INGOT);
        TagPrefix.nugget.setIgnored(Steel, ModItems.STEEL_NUGGET);
        TagPrefix.plate.setIgnored(Steel, ModItems.STEEL_PLATE);
        TagPrefix.rod.setIgnored(Steel, ModItems.STEEL_ROD);
        TagPrefix.block.setIgnored(Steel, ModBlocks.STEEL_BLOCK);
        TagPrefix.ingot.setIgnored(Desh, ModItems.DESH_INGOT);
        TagPrefix.nugget.setIgnored(Desh, ModItems.DESH_NUGGET);
        TagPrefix.plate.setIgnored(Desh, ModItems.DESH_PLATE);
        TagPrefix.block.setIgnored(Desh, ModBlocks.DESH_BLOCK);
        TagPrefix.ingot.setIgnored(Ostrum, ModItems.OSTRUM_INGOT);
        TagPrefix.nugget.setIgnored(Ostrum, ModItems.OSTRUM_NUGGET);
        TagPrefix.plate.setIgnored(Ostrum, ModItems.OSTRUM_PLATE);
        TagPrefix.block.setIgnored(Ostrum, ModBlocks.OSTRUM_BLOCK);
        TagPrefix.ingot.setIgnored(Calorite, ModItems.CALORITE_INGOT);
        TagPrefix.nugget.setIgnored(Calorite, ModItems.CALORITE_NUGGET);
        TagPrefix.plate.setIgnored(Calorite, ModItems.CALORITE_PLATE);
        TagPrefix.block.setIgnored(Calorite, ModBlocks.CALORITE_BLOCK);

        TagPrefix.dust.setIgnored(Mana, () -> () -> BotaniaItems.manaPowder);
        TagPrefix.ingot.setIgnored(Manasteel, () -> () -> BotaniaItems.manaSteel);
        TagPrefix.nugget.setIgnored(Manasteel, () -> () -> BotaniaItems.manasteelNugget);
        TagPrefix.block.setIgnored(Manasteel, () -> BotaniaBlocks.manasteelBlock);
        TagPrefix.ingot.setIgnored(Terrasteel, () -> () -> BotaniaItems.terrasteel);
        TagPrefix.nugget.setIgnored(Terrasteel, () -> () -> BotaniaItems.terrasteelNugget);
        TagPrefix.block.setIgnored(Terrasteel, () -> BotaniaBlocks.terrasteelBlock);
        TagPrefix.ingot.setIgnored(Elementium, () -> () -> BotaniaItems.elementium);
        TagPrefix.nugget.setIgnored(Elementium, () -> () -> BotaniaItems.elementiumNugget);
        TagPrefix.block.setIgnored(Elementium, () -> BotaniaBlocks.elementiumBlock);
        TagPrefix.ingot.setIgnored(Gaia, () -> () -> BotaniaItems.gaiaIngot);
        TagPrefix.gem.setIgnored(ManaDiamond, () -> () -> BotaniaItems.manaDiamond);
        TagPrefix.block.setIgnored(ManaDiamond, () -> BotaniaBlocks.manaDiamondBlock);
        TagPrefix.gem.setIgnored(Dragonstone, () -> () -> BotaniaItems.dragonstone);
        TagPrefix.block.setIgnored(Dragonstone, () -> BotaniaBlocks.dragonstoneBlock);
        TagPrefix.gem.setIgnored(SourceGem, () -> ItemsRegistry.SOURCE_GEM);
        TagPrefix.block.setIgnored(SourceGem, () -> BlockRegistry.SOURCE_GEM_BLOCK);

        TagPrefix.nugget.setIgnored(Orichalcos, () -> ExtraBotanyItems.orichalcosNugget);
        TagPrefix.ingot.setIgnored(Orichalcos, () -> ExtraBotanyItems.orichalcos);
        TagPrefix.block.setIgnored(Orichalcos, () -> ExtraBotanyBlocks.orichalcosBlock);
        TagPrefix.nugget.setIgnored(Photonium, () -> ExtraBotanyItems.photoniumNugget);
        TagPrefix.ingot.setIgnored(Photonium, () -> ExtraBotanyItems.photonium);
        TagPrefix.block.setIgnored(Photonium, () -> ExtraBotanyBlocks.photoniumBlock);
        TagPrefix.nugget.setIgnored(Shadowium, () -> ExtraBotanyItems.shadowiumNugget);
        TagPrefix.ingot.setIgnored(Shadowium, () -> ExtraBotanyItems.shadowium);
        TagPrefix.block.setIgnored(Shadowium, () -> ExtraBotanyBlocks.shadowiumBlock);
        TagPrefix.nugget.setIgnored(Aerialite, () -> ExtraBotanyItems.aerialiteNugget);
        TagPrefix.ingot.setIgnored(Aerialite, () -> ExtraBotanyItems.aerialite);
        TagPrefix.block.setIgnored(Aerialite, () -> ExtraBotanyBlocks.aerialiteBlock);

        TagPrefix.block.setIgnored(Livingwood, () -> BotaniaBlocks.livingwoodPlanks);
        TagPrefix.block.setIgnored(Dreamwood, () -> BotaniaBlocks.dreamwoodPlanks);
        TagPrefix.block.setIgnored(Shimmerwood, () -> BotaniaBlocks.shimmerwoodPlanks);
        TagPrefix.block.setIgnored(Livingrock, () -> BotaniaBlocks.livingrock);
        TagPrefix.block.setIgnored(Shimmerrock, () -> BotaniaBlocks.shimmerrock);
        TagPrefix.block.setIgnored(ManaGlass, () -> BotaniaBlocks.manaGlass);
        TagPrefix.block.setIgnored(ElfGlass, () -> BotaniaBlocks.elfGlass);
        TagPrefix.block.setIgnored(BifrostPerm, () -> BotaniaBlocks.bifrostPerm);

        TagPrefix.gear.setIgnored(Wood, () -> EIOItems.GEAR_WOOD);
        TagPrefix.dust.setIgnored(Wheat, () -> EIOItems.FLOUR);
        TagPrefix.dust.setIgnored(Coal, () -> EIOItems.POWDERED_COAL);
        TagPrefix.dust.setIgnored(Iron, () -> EIOItems.POWDERED_IRON);
        TagPrefix.dust.setIgnored(Gold, () -> EIOItems.POWDERED_GOLD);
        TagPrefix.dust.setIgnored(Copper, () -> EIOItems.POWDERED_COPPER);
        TagPrefix.dust.setIgnored(Tin, () -> EIOItems.POWDERED_TIN);
        TagPrefix.dust.setIgnored(EnderPearl, () -> EIOItems.POWDERED_ENDER_PEARL);
        TagPrefix.dust.setIgnored(Obsidian, () -> EIOItems.POWDERED_OBSIDIAN);
        TagPrefix.dust.setIgnored(Cobalt, () -> EIOItems.POWDERED_COBALT);
        TagPrefix.dust.setIgnored(Lapis, () -> EIOItems.POWDERED_LAPIS_LAZULI);
        TagPrefix.dust.setIgnored(Soularium, () -> EIOItems.SOUL_POWDER);
        TagPrefix.dust.setIgnored(NetherQuartz, () -> EIOItems.POWDERED_QUARTZ);

        TagPrefix.ingot.setIgnored(CopperAlloy, () -> EIOItems.COPPER_ALLOY_INGOT);
        TagPrefix.nugget.setIgnored(CopperAlloy, () -> EIOItems.COPPER_ALLOY_NUGGET);
        TagPrefix.block.setIgnored(CopperAlloy, () -> EIOBlocks.COPPER_ALLOY_BLOCK);

        TagPrefix.ingot.setIgnored(EnergeticAlloy, () -> EIOItems.ENERGETIC_ALLOY_INGOT);
        TagPrefix.nugget.setIgnored(EnergeticAlloy, () -> EIOItems.ENERGETIC_ALLOY_NUGGET);
        TagPrefix.block.setIgnored(EnergeticAlloy, () -> EIOBlocks.ENERGETIC_ALLOY_BLOCK);

        TagPrefix.ingot.setIgnored(VibrantAlloy, () -> EIOItems.VIBRANT_ALLOY_INGOT);
        TagPrefix.nugget.setIgnored(VibrantAlloy, () -> EIOItems.VIBRANT_ALLOY_NUGGET);
        TagPrefix.block.setIgnored(VibrantAlloy, () -> EIOBlocks.VIBRANT_ALLOY_BLOCK);

        TagPrefix.ingot.setIgnored(RedstoneAlloy, () -> EIOItems.REDSTONE_ALLOY_INGOT);
        TagPrefix.nugget.setIgnored(RedstoneAlloy, () -> EIOItems.REDSTONE_ALLOY_NUGGET);
        TagPrefix.block.setIgnored(RedstoneAlloy, () -> EIOBlocks.REDSTONE_ALLOY_BLOCK);

        TagPrefix.ingot.setIgnored(ConductiveAlloy, () -> EIOItems.CONDUCTIVE_ALLOY_INGOT);
        TagPrefix.nugget.setIgnored(ConductiveAlloy, () -> EIOItems.CONDUCTIVE_ALLOY_NUGGET);
        TagPrefix.block.setIgnored(ConductiveAlloy, () -> EIOBlocks.CONDUCTIVE_ALLOY_BLOCK);

        TagPrefix.ingot.setIgnored(PulsatingAlloy, () -> EIOItems.PULSATING_ALLOY_INGOT);
        TagPrefix.nugget.setIgnored(PulsatingAlloy, () -> EIOItems.PULSATING_ALLOY_NUGGET);
        TagPrefix.block.setIgnored(PulsatingAlloy, () -> EIOBlocks.PULSATING_ALLOY_BLOCK);

        TagPrefix.ingot.setIgnored(DarkSteel, () -> EIOItems.DARK_STEEL_INGOT);
        TagPrefix.nugget.setIgnored(DarkSteel, () -> EIOItems.DARK_STEEL_NUGGET);
        TagPrefix.block.setIgnored(DarkSteel, () -> EIOBlocks.DARK_STEEL_BLOCK);

        TagPrefix.ingot.setIgnored(Soularium, () -> EIOItems.SOULARIUM_INGOT);
        TagPrefix.nugget.setIgnored(Soularium, () -> EIOItems.SOULARIUM_NUGGET);
        TagPrefix.block.setIgnored(Soularium, () -> EIOBlocks.SOULARIUM_BLOCK);

        TagPrefix.ingot.setIgnored(EndSteel, () -> EIOItems.END_STEEL_INGOT);
        TagPrefix.nugget.setIgnored(EndSteel, () -> EIOItems.END_STEEL_NUGGET);
        TagPrefix.block.setIgnored(EndSteel, () -> EIOBlocks.END_STEEL_BLOCK);

        TagPrefix.ingot.setIgnored(CrystalMatrix, crystal_matrix_ingot);
        /// to be added in future from {@link committee.nova.mods.avaritia_integration.init.registry.CoreReg }
        // TagPrefix.nugget.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_nugget);
        // plateDense.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_dense_plate);
        // plate.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_plate);
        // plateDouble.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_double_plate);
        // rod.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_rod);
        // rodLong.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_long_rod);
        // gear.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_gear);
        // ring.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_ring);
        // spring.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_spring);
        // screw.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_screw);
        // dust.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_dust);
        // bolt.setIgnored(CrystalMatrix, CoreReg.crystal_matrix_bolt);
        //
        ingot.setIgnored(BlazeCube, blaze_cube);
        // nugget.setIgnored(BlazeCube, CoreReg.blaze_cube_nugget);
        // block.setIgnored(BlazeCube, blaze_cube_block);
        // plateDense.setIgnored(BlazeCube, CoreReg.blaze_cube_dense_plate);
        // plate.setIgnored(BlazeCube, CoreReg.blaze_cube_plate);
        // plateDouble.setIgnored(BlazeCube, CoreReg.blaze_cube_double_plate);
        // rod.setIgnored(BlazeCube, CoreReg.blaze_cube_rod);
        // rodLong.setIgnored(BlazeCube, CoreReg.blaze_cube_long_rod);
        // gear.setIgnored(BlazeCube, CoreReg.blaze_cube_gear);
        // ring.setIgnored(BlazeCube, CoreReg.blaze_cube_ring);
        // spring.setIgnored(BlazeCube, CoreReg.blaze_cube_spring);
        // screw.setIgnored(BlazeCube, CoreReg.blaze_cube_screw);
        // dust.setIgnored(BlazeCube, CoreReg.blaze_cube_dust);
        // bolt.setIgnored(BlazeCube, CoreReg.blaze_cube_bolt);

        TagPrefix.ingot.setIgnored(Infinity, infinity_ingot);
        TagPrefix.nugget.setIgnored(Infinity, infinity_nugget);
        TagPrefix.block.setIgnored(Infinity, infinity);
        // plateDense.setIgnored(Infinity, CoreReg.infinity_dense_plate);
        // plate.setIgnored(Infinity, CoreReg.infinity_plate);
        // plateDouble.setIgnored(Infinity, CoreReg.infinity_double_plate);
        // rod.setIgnored(Infinity, CoreReg.infinity_rod);
        // rodLong.setIgnored(Infinity, CoreReg.infinity_long_rod);
        // gear.setIgnored(Infinity, CoreReg.infinity_gear);
        // ring.setIgnored(Infinity, CoreReg.infinity_ring);
        // spring.setIgnored(Infinity, CoreReg.infinity_spring);
        // screw.setIgnored(Infinity, CoreReg.infinity_screw);
        // dust.setIgnored(Infinity, CoreReg.infinity_dust);

        TagPrefix.block.setIgnored(Neutron, committee.nova.mods.avaritia.init.registry.ModBlocks.neutron);
        // TagPrefix.ingot.setIgnored(Neutron, neutron_ingot);
        // TagPrefix.nugget.setIgnored(Neutron, neutron_nugget);
        // bolt.setIgnored(Neutron, CoreReg.neutron_bolt);
        // plateDense.setIgnored(Neutron, CoreReg.neutron_dense_plate);
        // plate.setIgnored(Neutron, CoreReg.neutron_plate);
        // plateDouble.setIgnored(Neutron, CoreReg.neutron_double_plate);
        // rod.setIgnored(Neutron, CoreReg.neutron_rod);
        // rodLong.setIgnored(Neutron, CoreReg.neutron_long_rod);
        gear.setIgnored(Neutron, neutron_gear);
        // dust.setIgnored(Neutron, CoreReg.neutron_dust);
        // ring.setIgnored(Neutron, CoreReg.neutron_ring);
        // spring.setIgnored(Neutron, CoreReg.neutron_spring);
        // screw.setIgnored(Neutron, CoreReg.neutron_screw);

        if (GTCEu.isProd()) {
            TagPrefix.ingot.setIgnored(Alfsteel, () -> () -> alfsteelIngot);
            TagPrefix.nugget.setIgnored(Alfsteel, () -> () -> alfsteelNugget);
            TagPrefix.block.setIgnored(Alfsteel, () -> alfsteelBlock);
        }
    }
}
