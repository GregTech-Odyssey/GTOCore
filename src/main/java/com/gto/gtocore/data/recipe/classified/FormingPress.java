package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;

import java.util.function.Consumer;

interface FormingPress {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("carbon_rotor"))
                .inputItems(new ItemStack(Blocks.CHAIN.asItem()))
                .inputItems(TagPrefix.rod, GTMaterials.Magnalium, 2)
                .inputItems(TagPrefix.bolt, GTMaterials.Magnalium, 8)
                .inputItems(GTItems.CARBON_FIBER_PLATE.asStack(18))
                .outputItems(GTOItems.CARBON_ROTOR.asStack())
                .EUt(120)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("logic_processor1"))
                .notConsumable(new ItemStack(AEItems.SILICON_PRESS.asItem()))
                .notConsumable(new ItemStack(AEItems.LOGIC_PROCESSOR_PRESS.asItem()))
                .inputItems(TagPrefix.dust, GTMaterials.Gold)
                .inputItems(TagPrefix.dust, GTMaterials.Silicon)
                .inputItems(TagPrefix.dust, GTMaterials.Redstone)
                .outputItems(new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()))
                .EUt(480)
                .duration(20)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("logic_processor"))
                .notConsumable(new ItemStack(AEItems.SILICON_PRESS.asItem()))
                .notConsumable(new ItemStack(AEItems.LOGIC_PROCESSOR_PRESS.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Gold)
                .inputItems(new ItemStack(AEItems.SILICON.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Redstone)
                .outputItems(new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()))
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("raw_imprinted_resonatic_circuit_board"))
                .inputItems(TagPrefix.dust, GTOMaterials.CircuitCompound, 4)
                .inputItems(TagPrefix.dust, GTOMaterials.MagnetoResonatic)
                .outputItems(GTOItems.RAW_IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack())
                .EUt(480)
                .duration(300)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("scintillator_crystal"))
                .inputItems(TagPrefix.plate, GTOMaterials.Vibranium)
                .inputItems(TagPrefix.dust, GTOMaterials.ThalliumThuliumDopedCaesiumIodide)
                .inputItems(TagPrefix.dust, GTOMaterials.PolycyclicAromaticMixture)
                .inputItems(TagPrefix.dust, GTOMaterials.CadmiumTungstate)
                .inputItems(TagPrefix.dust, GTOMaterials.BismuthGermanate)
                .inputItems(TagPrefix.plate, GTOMaterials.Mithril, 2)
                .outputItems(GTOItems.SCINTILLATOR_CRYSTAL.asStack())
                .EUt(1966080)
                .duration(280)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("engineering_processor1"))
                .notConsumable(new ItemStack(AEItems.SILICON_PRESS.asItem()))
                .notConsumable(new ItemStack(AEItems.ENGINEERING_PROCESSOR_PRESS.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Diamond)
                .inputItems(new ItemStack(AEItems.SILICON.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Redstone)
                .outputItems(new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()))
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("reactor_fuel_rod"))
                .notConsumable(GTItems.SHAPE_EXTRUDER_CELL.asStack())
                .inputItems(TagPrefix.ingot, GTMaterials.SteelMagnetic)
                .outputItems(GTOItems.REACTOR_FUEL_ROD.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("calculation_processor1"))
                .notConsumable(new ItemStack(AEItems.SILICON_PRESS.asItem()))
                .notConsumable(new ItemStack(AEItems.CALCULATION_PROCESSOR_PRESS.asItem()))
                .inputItems(TagPrefix.dust, GTMaterials.CertusQuartz)
                .inputItems(TagPrefix.dust, GTMaterials.Silicon)
                .inputItems(TagPrefix.dust, GTMaterials.Redstone)
                .outputItems(new ItemStack(AEItems.CALCULATION_PROCESSOR.asItem()))
                .EUt(480)
                .duration(20)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("grindball_soapstone"))
                .notConsumable(GTItems.SHAPE_MOLD_BALL.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Soapstone, 16)
                .inputItems(TagPrefix.ingot, GTMaterials.SolderingAlloy, 2)
                .outputItems(GTOItems.GRINDBALL_SOAPSTONE.asStack())
                .EUt(7680)
                .duration(800)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("cosmic_ram_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .inputItems(GTItems.RANDOM_ACCESS_MEMORY_WAFER.asStack())
                .inputItems(GTOItems.PREPARED_COSMIC_SOC_WAFER.asStack())
                .outputItems(GTOItems.COSMIC_RAM_WAFER.asStack())
                .EUt(31457280)
                .duration(550)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("fishbig_fabric"))
                .inputItems(TagPrefix.foil, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter, 64)
                .inputItems(TagPrefix.foil, GTOMaterials.Shirabon, 64)
                .inputItems(GTOItems.TWO_WAY_FOIL.asStack(64))
                .inputItems(TagPrefix.foil, GTOMaterials.Cosmic, 64)
                .inputItems(TagPrefix.foil, GTOMaterials.CosmicNeutronium, 64)
                .inputItems(TagPrefix.foil, GTOMaterials.Eternity, 64)
                .outputItems(GTOItems.FISHBIG_FABRIC.asStack())
                .EUt(131941395333120L)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("wood_gear"))
                .inputItems(TagPrefix.plate, GTMaterials.Wood, 4)
                .notConsumable(GTItems.SHAPE_MOLD_GEAR.asStack())
                .outputItems(TagPrefix.gear, GTMaterials.Wood)
                .EUt(16)
                .duration(60)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("tungsten_carbide_reactor_fuel_rod"))
                .notConsumable(GTItems.SHAPE_EXTRUDER_CELL.asStack())
                .inputItems(TagPrefix.ingot, GTMaterials.NeodymiumMagnetic)
                .inputItems(TagPrefix.ingot, GTMaterials.TungstenCarbide)
                .outputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asStack())
                .EUt(120)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("optical_soc_containment_housing"))
                .inputItems(GTItems.ELITE_CIRCUIT_BOARD.asStack())
                .inputItems(TagPrefix.foil, GTMaterials.Titanium)
                .inputItems(TagPrefix.foil, GTMaterials.YttriumBariumCuprate)
                .inputItems(TagPrefix.foil, GTMaterials.NickelZincFerrite)
                .inputItems(TagPrefix.foil, GTMaterials.UraniumRhodiumDinaquadide)
                .inputItems(TagPrefix.bolt, GTMaterials.Darmstadtium, 4)
                .outputItems(GTOItems.OPTICAL_SOC_CONTAINMENT_HOUSING.asStack())
                .EUt(122880)
                .duration(290)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("heavy_duty_plate_2"))
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_1.asStack())
                .inputItems(TagPrefix.plateDouble, GTMaterials.Titanium)
                .inputItems(TagPrefix.plateDouble, GTMaterials.DamascusSteel, 2)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_2.asStack())
                .EUt(480)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("calculation_processor"))
                .notConsumable(new ItemStack(AEItems.SILICON_PRESS.asItem()))
                .notConsumable(new ItemStack(AEItems.CALCULATION_PROCESSOR_PRESS.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.CertusQuartz)
                .inputItems(new ItemStack(AEItems.SILICON.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Redstone)
                .outputItems(new ItemStack(AEItems.CALCULATION_PROCESSOR.asItem()))
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("engineering_processor"))
                .notConsumable(new ItemStack(AEItems.SILICON_PRESS.asItem()))
                .notConsumable(new ItemStack(AEItems.ENGINEERING_PROCESSOR_PRESS.asItem()))
                .inputItems(TagPrefix.dust, GTMaterials.Diamond)
                .inputItems(TagPrefix.dust, GTMaterials.Silicon)
                .inputItems(TagPrefix.dust, GTMaterials.Redstone)
                .outputItems(new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()))
                .EUt(480)
                .duration(20)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("grindball_aluminium"))
                .notConsumable(GTItems.SHAPE_MOLD_BALL.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium, 16)
                .inputItems(TagPrefix.ingot, GTMaterials.SolderingAlloy, 2)
                .outputItems(GTOItems.GRINDBALL_ALUMINIUM.asStack())
                .EUt(7680)
                .duration(800)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("netherite_rod"))
                .inputItems(new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.asItem()))
                .inputItems(TagPrefix.rod, GTMaterials.Neodymium, 2)
                .inputItems(TagPrefix.ingot, GTMaterials.Netherite)
                .outputItems(GTOItems.NETHERITE_ROD.asStack(2))
                .EUt(480)
                .duration(400)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("exotic_ram_wafer"))
                .inputItems(GTOItems.OPTICAL_RAM_WAFER.asStack())
                .inputItems(GTItems.NOR_MEMORY_CHIP_WAFER.asStack())
                .inputItems(GTItems.NAND_MEMORY_CHIP_WAFER.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.Amethyst)
                .inputItems(TagPrefix.plate, GTMaterials.Technetium)
                .outputItems(GTOItems.EXOTIC_RAM_WAFER.asStack())
                .EUt(7864320)
                .duration(350)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("optical_ram_wafer"))
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER.asStack())
                .inputItems(GTItems.RANDOM_ACCESS_MEMORY_WAFER.asStack())
                .inputItems(GTOItems.PHOTON_CARRYING_WAFER.asStack())
                .outputItems(GTOItems.OPTICAL_RAM_WAFER.asStack())
                .EUt(1966080)
                .duration(150)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder(GTOCore.id("supracausal_ram_wafer"))
                .inputItems(GTOItems.COSMIC_RAM_WAFER.asStack())
                .inputItems(GTOItems.EXOTIC_RAM_WAFER.asStack())
                .inputItems(GTOItems.PELLET_ANTIMATTER.asStack())
                .inputItems(TagPrefix.foil, GTOMaterials.Legendarium)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.Hikarium)
                .outputItems(GTOItems.SUPRACAUSAL_RAM_WAFER.asStack())
                .EUt(125829120)
                .duration(750)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);
    }
}
