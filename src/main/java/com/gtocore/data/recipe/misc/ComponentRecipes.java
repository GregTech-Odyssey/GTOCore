package com.gtocore.data.recipe.misc;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.ExResearchMachines;
import com.gtocore.data.CraftingComponents;

import com.gtolib.GTOCore;
import com.gtolib.api.GTOValues;
import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtolib.api.GTOValues.COMPONENT_ASSEMBLY_CASING_TIER;

public final class ComponentRecipes {

    public static void init() {
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_electric_motor"), GTOItems.ULV_ELECTRIC_MOTOR.asItem(),
                "CWR", "WMW", "RWC", 'C', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, Copper), 'W', new MaterialEntry(wireGtDouble, Tin),
                'R', new MaterialEntry(rod, Copper), 'M', new MaterialEntry(rod, IronMagnetic));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_electric_piston"), GTOItems.ULV_ELECTRIC_PISTON.asItem(),
                "PPP", "CRR", "CMG", 'P', new MaterialEntry(plate, Copper), 'C', new MaterialEntry(cableGtSingle, Lead),
                'R', new MaterialEntry(rod, Copper), 'M', GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'G', new MaterialEntry(gearSmall, Copper));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_electric_conveyor"), GTOItems.ULV_CONVEYOR_MODULE.asItem(),
                "RRR", "MCM", "RRR", 'R', new MaterialEntry(plate, Rubber), 'M', GTOItems.ULV_ELECTRIC_MOTOR.asItem(),
                'C', new MaterialEntry(cableGtSingle, Lead));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_electric_pump"), GTOItems.ULV_ELECTRIC_PUMP.asItem(),
                "SFR", "sPw", "RMC", 'S', new MaterialEntry(screw, Iron), 'R', new MaterialEntry(ring, Rubber),
                'F', new MaterialEntry(rotor, Iron), 'P', new MaterialEntry(pipeNormalFluid, Copper), 'M', GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'C', new MaterialEntry(cableGtSingle, Lead));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_robot_arm"), GTOItems.ULV_ROBOT_ARM.asItem(),
                "CCC", "MRM", "PVR", 'R', new MaterialEntry(rod, Copper), 'C', new MaterialEntry(cableGtSingle, Lead),
                'M', GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'P', GTOItems.ULV_ELECTRIC_PISTON.asItem(), 'V', VACUUM_TUBE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ulv_fluid_regulator"), GTOItems.ULV_FLUID_REGULATOR.asItem(),
                "SFR", "sPw", "RMC", 'S', new MaterialEntry(screw, Iron), 'R', VACUUM_TUBE.asItem(),
                'F', new MaterialEntry(rotor, Iron), 'P', GTOItems.ULV_ELECTRIC_PUMP.asItem(), 'M', GTOItems.ULV_ELECTRIC_MOTOR.asItem(), 'C', new MaterialEntry(cableGtSingle, Lead));

        assembler(LV, GTOChemicalHelper.getItem(gem, Quartzite), GTOChemicalHelper.getItem(gem, EnderPearl), Steel, Tin, SteelMagnetic, Copper, Rubber, Bronze, Tin, Brass, ManganesePhosphide);
        assembler(MV, GTOChemicalHelper.getItem(gemFlawless, Emerald), GTOChemicalHelper.getItem(gem, EnderEye), Aluminium, Copper, SteelMagnetic, Cupronickel, Rubber, Steel, Bronze, Electrum, MagnesiumDiboride);
        assembler(HV, GTOChemicalHelper.getItem(gem, EnderEye), QUANTUM_EYE.asItem(), StainlessSteel, Silver, SteelMagnetic, Electrum, Rubber, VanadiumSteel, Steel, Chromium, MercuryBariumCalciumCuprate);
        assembler(EV, QUANTUM_EYE.asItem(), GTOChemicalHelper.getItem(gem, NetherStar), Titanium, Aluminium, NeodymiumMagnetic, Kanthal, SiliconeRubber, StainlessSteel, Aluminium, Platinum, UraniumTriplatinum);
        assembler(IV, QUANTUM_STAR.asItem(), QUANTUM_STAR.asItem(), TungstenSteel, Tungsten, NeodymiumMagnetic, Graphene, SiliconeRubber, TungstenCarbide, Titanium, Iridium, SamariumIronArsenicOxide);

        assembly_line(LuV, GTOChemicalHelper.getItem(pipeSmallFluid, NiobiumTitanium), QUANTUM_STAR.asItem(), GTOChemicalHelper.getItem(rodLong, SamariumMagnetic), HSSS, Ruridit, NiobiumTitanium, SolderingAlloy, Lubricant, HSSE, SiliconeRubber, HSSS, Palladium, Ruthenium, IndiumTinBariumTitaniumCuprate);
        assembly_line(ZPM, GTOChemicalHelper.getItem(pipeNormalFluid, Polybenzimidazole), QUANTUM_STAR.asItem(), GTOChemicalHelper.getItem(rodLong, SamariumMagnetic), Osmiridium, Europium, VanadiumGallium, SolderingAlloy, Lubricant, MarM200Steel, StyreneButadieneRubber, NaquadahAlloy, Trinium, Duranium, UraniumRhodiumDinaquadide);
        assembly_line(UV, GTOChemicalHelper.getItem(pipeLargeFluid, Naquadah), GRAVI_STAR.asItem(), GTOChemicalHelper.getItem(rodLong, SamariumMagnetic), Tritanium, Americium, YttriumBariumCuprate, SolderingAlloy, Lubricant, Naquadria, StyreneButadieneRubber, Tritanium, Naquadah, Tritanium, EnrichedNaquadahTriniumEuropiumDuranide);
        assembly_line(UHV, GTOChemicalHelper.getItem(pipeNormalFluid, Neutronium), GRAVI_STAR.asItem(), GTOChemicalHelper.getItem(rodLong, EnergeticNetherite), Orichalcum, AbyssalAlloy, Europium, MutatedLivingSolder, Lubricant, HighDurabilityCompoundSteel, StyreneButadieneRubber, Neutronium, AbyssalAlloy, FluxedElectrum, RutheniumTriniumAmericiumNeutronate);
        assembly_line(UEV, GTOChemicalHelper.getItem(pipeNormalFluid, Enderium), GRAVI_STAR.asItem(), GTOChemicalHelper.getItem(rodLong, EnergeticNetherite), HastelloyX78, TitanSteel, Mithril, MutatedLivingSolder, Lubricant, Bohrium, StyreneButadieneRubber, Quantanium, TitanSteel, Dalisenite, Enderite);
        assembly_line(UIV, GTOChemicalHelper.getItem(pipeNormalFluid, FullerenePolymerMatrixPulp), GTOItems.UNSTABLE_STAR.asItem(), GTOChemicalHelper.getItem(rodLong, AttunedTengam), Infuscolium, Adamantine, Neutronium, SuperMutatedLivingSolder, Lubricant, Taranium, StyreneButadieneRubber, Adamantium, Adamantine, ArceusAlloy2B, Echoite);
        assembly_line(UXV, GTOChemicalHelper.getItem(pipeNormalFluid, HeavyQuarkDegenerateMatter), GTOItems.UNSTABLE_STAR.asItem(), GTOChemicalHelper.getItem(rodLong, AttunedTengam), HastelloyK243, NaquadriaticTaranium, Taranium, SuperMutatedLivingSolder, Lubricant, AstralTitanium, StyreneButadieneRubber, Vibranium, NaquadriaticTaranium, TitanPrecisionSteel, Legendarium);
        assembly_line(OpV, GTOChemicalHelper.getItem(pipeLargeFluid, HeavyQuarkDegenerateMatter), GTOItems.UNSTABLE_STAR.asItem(), GTOChemicalHelper.getItem(rodLong, AttunedTengam), Vibramantium, Starmetal, CrystalMatrix, SuperMutatedLivingSolder, Lubricant, CelestialTungsten, StyreneButadieneRubber, Draconium, Starmetal, Hikarium, AwakenedDraconium);

        integrated_control_core(UV, GTResearchMachines.HPCA_ADVANCED_COMPUTATION_COMPONENT.asItem(), TOOL_DATA_MODULE.asItem(), GTOChemicalHelper.getItem(pipeLargeFluid, Naquadah), Tritanium, Americium, EnrichedNaquadahTriniumEuropiumDuranide, SolderingAlloy, Lubricant, StyreneButadieneRubber, Naquadria);
        integrated_control_core(UHV, GTResearchMachines.HPCA_ADVANCED_COMPUTATION_COMPONENT.asItem(), GTOItems.NEURAL_MATRIX.asItem(), GTOChemicalHelper.getItem(pipeNormalFluid, Neutronium), Orichalcum, AbyssalAlloy, Europium, RutheniumTriniumAmericiumNeutronate, MutatedLivingSolder, Lubricant, StyreneButadieneRubber, HighDurabilityCompoundSteel);
        integrated_control_core(UEV, ExResearchMachines.NICH_COMPUTING_COMPONENTS.asItem(), GTOItems.NEURAL_MATRIX.asItem(), GTOChemicalHelper.getItem(pipeNormalFluid, Enderium), HastelloyX78, TitanSteel, Mithril, Dalisenite, MutatedLivingSolder, Lubricant, StyreneButadieneRubber, Bohrium);
        integrated_control_core(UIV, ExResearchMachines.NICH_COMPUTING_COMPONENTS.asItem(), GTOItems.ATOMIC_ARCHIVES.asItem(), GTOChemicalHelper.getItem(pipeNormalFluid, FullerenePolymerMatrixPulp), Infuscolium, Adamantine, Neutronium, Echoite, SuperMutatedLivingSolder, Lubricant, StyreneButadieneRubber, Taranium);
        integrated_control_core(UXV, ExResearchMachines.GWCA_COMPUTING_COMPONENTS.asItem(), GTOItems.ATOMIC_ARCHIVES.asItem(), GTOChemicalHelper.getItem(pipeNormalFluid, HeavyQuarkDegenerateMatter), HastelloyK243, NaquadriaticTaranium, Taranium, Legendarium, SuperMutatedLivingSolder, Lubricant, StyreneButadieneRubber, AstralTitanium);
        integrated_control_core(OpV, ExResearchMachines.GWCA_COMPUTING_COMPONENTS.asItem(), GTOItems.OBSIDIAN_MATRIX.asItem(), GTOChemicalHelper.getItem(pipeLargeFluid, HeavyQuarkDegenerateMatter), Vibramantium, Starmetal, CrystalMatrix, AwakenedDraconium, SuperMutatedLivingSolder, Lubricant, StyreneButadieneRubber, CelestialTungsten);

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder("max_integrated_control_core")
                .circuitMeta(9)
                .inputItems(GTOItems.CLOSED_TIMELIKE_CURVE_GUIDANCE_UNIT.asItem(), 48)
                .inputItems(GTOItems.OBSIDIAN_MATRIX.asItem(), 96)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.ChaosInfinityAlloy, 288)
                .inputItems(CustomTags.MAX_CIRCUITS, 96)
                .inputItems(GTOItems.MAX_EMITTER.asItem(), 96)
                .inputItems(GTOItems.NUCLEAR_STAR.asItem(), 192)
                .inputItems(TagPrefix.wireFine, GTOMaterials.Cosmic, 3072)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 96)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 1769472)
                .inputFluids(GTOMaterials.SpacetimeContinuum, 3538944)
                .inputFluids(GTOMaterials.BlackDwarfMatter, 110592)
                .inputFluids(GTOMaterials.WhiteDwarfMatter, 110592)
                .inputFluids(GTOMaterials.Shirabon, 110592)
                .inputFluids(GTOMaterials.Infinity, 27648)
                .outputItems(GTOItems.INTEGRATED_CONTROL_CORE_MAX.asItem(), 64)
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder("max_field_generator")
                .inputItems(TagPrefix.plateDouble, GTOMaterials.ChaosInfinityAlloy, 288)
                .inputItems(CustomTags.MAX_CIRCUITS, 96)
                .inputItems(GTOItems.MAX_EMITTER.asItem(), 96)
                .inputItems(GTOItems.NUCLEAR_STAR.asItem(), 192)
                .inputItems(TagPrefix.frameGt, GTOMaterials.Infinity, 48)
                .inputItems(TagPrefix.wireFine, GTOMaterials.Cosmic, 3072)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 96)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 1769472)
                .inputFluids(GTOMaterials.SpacetimeContinuum, 3538944)
                .inputFluids(GTOMaterials.BlackDwarfMatter, 110592)
                .inputFluids(GTOMaterials.WhiteDwarfMatter, 110592)
                .inputFluids(GTOMaterials.Shirabon, 110592)
                .inputFluids(GTOMaterials.Infinity, 27648)
                .outputItems(GTOItems.MAX_FIELD_GENERATOR.asItem(), 64)
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder("max_sensor")
                .circuitMeta(7)
                .inputItems(CustomTags.MAX_CIRCUITS, 96)
                .inputItems(TagPrefix.plate, GTOMaterials.TranscendentMetal, 192)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asItem(), 48)
                .inputItems(GTOItems.NUCLEAR_STAR.asItem(), 192)
                .inputItems(TagPrefix.frameGt, GTOMaterials.Infinity, 48)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 96)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 1769472)
                .inputFluids(GTOMaterials.SpacetimeContinuum, 3538944)
                .inputFluids(GTOMaterials.BlackDwarfMatter, 110592)
                .inputFluids(GTOMaterials.WhiteDwarfMatter, 110592)
                .inputFluids(GTOMaterials.Shirabon, 110592)
                .inputFluids(GTOMaterials.Infinity, 27648)
                .outputItems(GTOItems.MAX_SENSOR.asItem(), 64)
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder("max_electric_piston")
                .circuitMeta(2)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asItem(), 48)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.TranscendentMetal, 192)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 48)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 884736)
                .inputFluids(GTOMaterials.SpacetimeContinuum, 1769472)
                .inputFluids(GTOMaterials.DimensionAllyshiftedSuperFluid, 3072000)
                .inputFluids(GTOMaterials.TranscendentMetal, 148992)
                .inputFluids(GTOMaterials.Infinity, 27648)
                .outputItems(GTOItems.MAX_ELECTRIC_PISTON.asItem(), 64)
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder("max_electric_pump")
                .circuitMeta(3)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.TranscendentMetal, 96)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asItem(), 48)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 48)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 884736)
                .inputFluids(GTOMaterials.SpacetimeContinuum, 1769472)
                .inputFluids(GTOMaterials.DimensionAllyshiftedSuperFluid, 3072000)
                .inputFluids(GTMaterials.StyreneButadieneRubber, 110592)
                .inputFluids(GTOMaterials.TranscendentMetal, 33792)
                .inputFluids(GTOMaterials.BlackDwarfMatter, 110592)
                .inputFluids(GTOMaterials.WhiteDwarfMatter, 110592)
                .inputFluids(GTOMaterials.Infinity, 41472)
                .inputFluids(GTOMaterials.Neutron, 442368)
                .outputItems(GTOItems.MAX_ELECTRIC_PUMP.asItem(), 64)
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder("max_conveyor_module")
                .circuitMeta(5)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asItem(), 96)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.TranscendentMetal, 96)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 48)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 884736)
                .inputFluids(GTOMaterials.DimensionAllyshiftedSuperFluid, 3072000)
                .inputFluids(GTMaterials.StyreneButadieneRubber, 1050624)
                .inputFluids(GTOMaterials.TranscendentMetal, 44544)
                .inputFluids(GTOMaterials.BlackDwarfMatter, 110592)
                .inputFluids(GTOMaterials.WhiteDwarfMatter, 110592)
                .inputFluids(GTOMaterials.Infinity, 27648)
                .outputItems(GTOItems.MAX_CONVEYOR_MODULE.asItem(), 64)
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder("max_electric_motor")
                .circuitMeta(1)
                .inputItems(TagPrefix.rodLong, GTOMaterials.Magmatter, 48)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 48)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 768000)
                .inputFluids(GTOMaterials.SpacetimeContinuum, 1536000)
                .inputFluids(GTOMaterials.DimensionAllyshiftedSuperFluid, 3072000)
                .inputFluids(GTOMaterials.BlackDwarfMatter, 110592)
                .inputFluids(GTOMaterials.WhiteDwarfMatter, 110592)
                .inputFluids(GTOMaterials.Infinity, 27648)
                .inputFluids(GTOMaterials.TranscendentMetal, 95232)
                .outputItems(GTOItems.MAX_ELECTRIC_MOTOR.asItem(), 64)
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder("max_robot_arm")
                .circuitMeta(4)
                .inputItems(CustomTags.UXV_CIRCUITS, 192)
                .inputItems(CustomTags.OpV_CIRCUITS, 96)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asItem(), 192)
                .inputItems(GTOItems.MAX_ELECTRIC_PISTON.asItem(), 48)
                .inputItems(CustomTags.MAX_CIRCUITS, 48)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 96)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 884736)
                .inputFluids(GTOMaterials.SpacetimeContinuum, 1769472)
                .inputFluids(GTOMaterials.DimensionAllyshiftedSuperFluid, 3072000)
                .inputFluids(GTOMaterials.TranscendentMetal, 152064)
                .inputFluids(GTOMaterials.Infinity, 27648)
                .outputItems(GTOItems.MAX_ROBOT_ARM.asItem(), 64)
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder("max_emitter")
                .circuitMeta(6)
                .inputItems(CustomTags.MAX_CIRCUITS, 96)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asItem(), 48)
                .inputItems(GTOItems.NUCLEAR_STAR.asItem(), 192)
                .inputItems(TagPrefix.frameGt, GTOMaterials.Infinity, 48)
                .inputItems(TagPrefix.foil, GTOMaterials.Cosmic, 3072)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 96)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder, 1769472)
                .inputFluids(GTOMaterials.SpacetimeContinuum, 3538944)
                .inputFluids(GTOMaterials.TranscendentMetal, 55296)
                .inputFluids(GTOMaterials.BlackDwarfMatter, 110592)
                .inputFluids(GTOMaterials.WhiteDwarfMatter, 110592)
                .inputFluids(GTOMaterials.Shirabon, 110592)
                .inputFluids(GTOMaterials.Infinity, 27648)
                .outputItems(GTOItems.MAX_EMITTER.asItem(), 64)
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();
    }

    private static void assembler(int tier, Item emitter_gem, Item field_gem, Material... material) {
        ItemStack motor = (ItemStack) MOTOR.get(tier);
        ItemStack conveyor = (ItemStack) CONVEYOR.get(tier);
        ItemStack pump = (ItemStack) PUMP.get(tier);
        ItemStack piston = (ItemStack) PISTON.get(tier);
        ItemStack robot_arm = (ItemStack) ROBOT_ARM.get(tier);
        ItemStack emitter = (ItemStack) EMITTER.get(tier);
        ItemStack sensor = (ItemStack) SENSOR.get(tier);
        ItemStack field_generator = (ItemStack) FIELD_GENERATOR.get(tier);
        TagKey<Item> circuit = (TagKey<Item>) CIRCUIT.get(tier);
        ASSEMBLER_RECIPES.recipeBuilder(String.format("motor_%s", VN[tier].toLowerCase()))
                .inputItems(GTOTagPrefix.MOTOR_ENCLOSURE, material[0])
                .inputItems(rod, material[0], 2)
                .inputItems(rod, material[2])
                .inputItems(round, material[0], 4)
                .inputItems(wireGtDouble, material[3], 4)
                .inputItems(cableGtSingle, material[1], 2)
                .outputItems(motor)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("motor_%s", VN[tier].toLowerCase()))
                .circuitMeta(1)
                .inputItems(rod, material[2], 12)
                .inputItems(wireGtDouble, material[3], 48)
                .inputItems(cableGtSingle, material[1], 24)
                .inputFluids(material[0], L * 40)
                .outputItems(motor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(String.format("conveyor_%s", VN[tier].toLowerCase()))
                .inputItems(motor.copyWithCount(2))
                .inputItems(rod, material[0], 2)
                .inputItems(ring, material[0], 4)
                .inputItems(cableGtSingle, material[1], 2)
                .inputFluids(material[4], L * 6)
                .outputItems(conveyor)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("conveyor_%s", VN[tier].toLowerCase()))
                .circuitMeta(2)
                .inputItems(motor.copyWithCount(24))
                .inputItems(cableGtSingle, material[1], 24)
                .inputFluids(material[0], L * 24)
                .inputFluids(material[4], L * 72)
                .outputItems(conveyor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(String.format("pump_%s", VN[tier].toLowerCase()))
                .inputItems(GTOTagPrefix.PUMP_BARREL, material[0])
                .inputItems(pipeNormalFluid, material[5])
                .inputItems(motor.copyWithCount(1))
                .inputItems(screw, material[6], 2)
                .inputItems(rotor, material[6])
                .inputItems(ring, material[4], 2)
                .inputItems(cableGtSingle, material[1], 2)
                .outputItems(pump)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("pump_%s", VN[tier].toLowerCase()))
                .circuitMeta(3)
                .inputItems(motor.copyWithCount(12))
                .inputItems(cableGtSingle, material[1], 24)
                .inputFluids(material[0], L * 30)
                .inputFluids(material[5], L * 36)
                .inputFluids(material[6], L * 51)
                .inputFluids(material[4], L * 6)
                .outputItems(pump.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(String.format("fluid_regulator_%s", VN[tier].toLowerCase()))
                .inputItems(pump)
                .inputItems(circuit, 2)
                .inputItems(GTOTagPrefix.CURVED_PLATE, material[0], 2)
                .circuitMeta(1)
                .outputItems((ItemStack) CraftingComponents.FLUID_REGULATOR.get(tier))
                .EUt(VA[tier])
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(String.format("piston_%s", VN[tier].toLowerCase()))
                .inputItems(GTOTagPrefix.PISTON_HOUSING, material[0])
                .inputItems(rod, material[0], 2)
                .inputItems(cableGtSingle, material[1], 2)
                .inputItems(plate, material[0])
                .inputItems(gearSmall, material[0])
                .inputItems(motor)
                .outputItems(piston)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("piston_%s", VN[tier].toLowerCase()))
                .circuitMeta(4)
                .inputItems(motor.copyWithCount(12))
                .inputItems(cableGtSingle, material[1], 24)
                .inputFluids(material[0], L * (36 + 6 * 12 + 12))
                .outputItems(piston.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(String.format("arm_%s", VN[tier].toLowerCase()))
                .inputItems(cableGtSingle, material[1], 3)
                .inputItems(rod, material[0], 2)
                .inputItems(motor.copyWithCount(2))
                .inputItems(piston)
                .inputItems(circuit)
                .outputItems(robot_arm)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("arm_%s", VN[tier].toLowerCase()))
                .circuitMeta(5)
                .inputItems(motor.copyWithCount(12))
                .inputItems(piston.copyWithCount(12))
                .inputItems(cableGtSingle, material[1], 36)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asItem(), 12)
                .inputFluids(material[0], L * 24)
                .outputItems(robot_arm.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(String.format("emitter_%s", VN[tier].toLowerCase()))
                .inputItems(GTOTagPrefix.EMITTER_BASES, material[0])
                .inputItems(rod, material[7], 2)
                .inputItems(cableGtSingle, material[1], 2)
                .inputItems(circuit, 2)
                .inputItems(emitter_gem)
                .outputItems(emitter)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("emitter_%s", VN[tier].toLowerCase()))
                .circuitMeta(6)
                .inputItems(emitter_gem, 12)
                .inputItems(cableGtSingle, material[1], 24)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asItem(), 24)
                .inputFluids(material[0], L * 48)
                .inputFluids(material[7], L * 12)
                .outputItems(emitter.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(String.format("sensor_%s", VN[tier].toLowerCase()))
                .inputItems(GTOTagPrefix.SENSOR_CASING, material[0])
                .inputItems(rod, material[7])
                .inputItems(cableGtSingle, material[1])
                .inputItems(circuit, 2)
                .inputItems(emitter_gem)
                .outputItems(sensor)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("sensor_%s", VN[tier].toLowerCase()))
                .circuitMeta(7)
                .inputItems(emitter_gem, 12)
                .inputItems(cableGtSingle, material[1], 12)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asItem(), 24)
                .inputFluids(material[0], L * 54)
                .inputFluids(material[7], L * 6)
                .outputItems(sensor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(String.format("field_generator_%s", VN[tier].toLowerCase()))
                .inputItems(GTOTagPrefix.FIELD_GENERATOR_CASING, material[0])
                .inputItems(emitter)
                .inputItems(field_gem)
                .inputItems(circuit, 2)
                .inputItems(wireGtQuadruple, material[8], 4)
                .outputItems(field_generator)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("field_generator_%s", VN[tier].toLowerCase()))
                .circuitMeta(8)
                .inputItems(emitter.copyWithCount(12))
                .inputItems(field_gem, 12)
                .inputItems(wireGtQuadruple, material[8], 48)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asItem(), 24)
                .inputFluids(material[0], L * 96)
                .outputItems(field_generator.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();
    }

    private static void assembly_line(int tier, Item pipe, Item emitter_gem, Item magnetic, Material... material) {
        int fluidMultiplier = getFluidMultiplier(tier);
        ItemStack motor = (ItemStack) MOTOR.get(tier);
        ItemStack conveyor = (ItemStack) CONVEYOR.get(tier);
        ItemStack pump = (ItemStack) PUMP.get(tier);
        ItemStack piston = (ItemStack) PISTON.get(tier);
        ItemStack robot_arm = (ItemStack) ROBOT_ARM.get(tier);
        ItemStack emitter = (ItemStack) EMITTER.get(tier);
        ItemStack sensor = (ItemStack) SENSOR.get(tier);
        ItemStack field_generator = (ItemStack) FIELD_GENERATOR.get(tier);
        TagKey<Item> circuit = (TagKey<Item>) CIRCUIT.get(tier);

        RecipeBuilder builder_motor = ASSEMBLY_LINE_RECIPES.recipeBuilder(String.format("motor_%s", VN[tier].toLowerCase()))
                .inputItems(GTOTagPrefix.MOTOR_ENCLOSURE, material[0])
                .inputItems(magnetic)
                .inputItems(rodLong, material[0], 4)
                .inputItems(ring, material[0], 4)
                .inputItems(round, material[0], 8)
                .inputFluids(material[3], L * fluidMultiplier)
                .inputFluids(material[4], 250 * fluidMultiplier)
                .inputFluids(material[5], L * fluidMultiplier)
                .outputItems(motor)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_motor.inputItems(wireFine, material[1], 64)
                    .inputItems(wireFine, material[1], 64)
                    .researchStation(b -> b
                            .researchStack((ItemStack) MOTOR.get(tier - 1))
                            .CWUt(1 << (tier - 3))
                            .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_motor.inputItems(wireFine, material[1], 64)
                    .scanner(b -> b
                            .researchStack(ELECTRIC_MOTOR_LuV.asItem())
                            .duration(1200)
                            .EUt(VA[IV]));
        } else {
            builder_motor.inputItems(wireFine, material[1], 32)
                    .scanner(b -> b
                            .researchStack(ELECTRIC_MOTOR_IV.asItem())
                            .duration(900)
                            .EUt(VA[EV]));
        }
        builder_motor.inputItems(cableGtSingle, material[2], 2).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("motor_%s", VN[tier].toLowerCase()))
                .circuitMeta(1)
                .inputItems(magnetic, 12)
                .inputItems(cableGtSingle, material[2], 24)
                .inputFluids(material[0], L * 95)
                .inputFluids(material[1], L * 48 * fluidMultiplier)
                .inputFluids(material[3], L * 12 * fluidMultiplier)
                .inputFluids(material[4], 3000 * fluidMultiplier)
                .inputFluids(material[5], L * 12 * fluidMultiplier)
                .outputItems(motor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        RecipeBuilder builder_conveyor = ASSEMBLY_LINE_RECIPES.recipeBuilder(String.format("conveyor_%s", VN[tier].toLowerCase()))
                .inputItems(motor.copyWithCount(2))
                .inputItems(plate, material[0], 2)
                .inputItems(ring, material[0], 4)
                .inputItems(round, material[0], 16)
                .inputItems(screw, material[0], 4)
                .inputItems(cableGtSingle, material[2], 2)
                .inputFluids(material[3], L * fluidMultiplier)
                .inputFluids(material[4], 250 * fluidMultiplier)
                .inputFluids(material[6], L * 8 * fluidMultiplier)
                .inputFluids(material[5], L * fluidMultiplier)
                .outputItems(conveyor)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_conveyor.researchStation(b -> b
                    .researchStack((ItemStack) CONVEYOR.get(tier - 1))
                    .CWUt(1 << (tier - 3))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_conveyor.scanner(b -> b
                    .researchStack(CONVEYOR_MODULE_LuV.asItem())
                    .duration(1200)
                    .EUt(VA[IV]));
        } else {
            builder_conveyor.scanner(b -> b
                    .researchStack(CONVEYOR_MODULE_IV.asItem())
                    .duration(900)
                    .EUt(VA[EV]));
        }
        builder_conveyor.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("conveyor_%s", VN[tier].toLowerCase()))
                .circuitMeta(2)
                .inputItems(motor.copyWithCount(24))
                .inputItems(cableGtSingle, material[2], 24)
                .inputFluids(material[0], L * 63)
                .inputFluids(material[3], L * 12 * fluidMultiplier)
                .inputFluids(material[4], 3000 * fluidMultiplier)
                .inputFluids(material[5], L * 12 * fluidMultiplier)
                .inputFluids(material[6], L * 96 * fluidMultiplier)
                .outputItems(conveyor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        RecipeBuilder builder_pump = ASSEMBLY_LINE_RECIPES.recipeBuilder(String.format("pump_%s", VN[tier].toLowerCase()))
                .inputItems(GTOTagPrefix.PUMP_BARREL, material[0])
                .inputItems(motor)
                .inputItems(pipe)
                .inputItems(screw, material[0], 8)
                .inputItems(rotor, material[0])
                .inputItems(cableGtSingle, material[2], 2)
                .inputFluids(material[3], L * fluidMultiplier)
                .inputFluids(material[4], 250 * fluidMultiplier)
                .inputFluids(material[6], L * fluidMultiplier)
                .inputFluids(material[5], L * fluidMultiplier)
                .outputItems(pump)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_pump.researchStation(b -> b
                    .researchStack((ItemStack) PUMP.get(tier - 1))
                    .CWUt(1 << (tier - 3))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_pump.scanner(b -> b
                    .researchStack(ELECTRIC_PUMP_LuV.asItem())
                    .duration(1200)
                    .EUt(VA[IV]));
        } else {
            builder_pump.scanner(b -> b
                    .researchStack(ELECTRIC_PUMP_IV.asItem())
                    .duration(900)
                    .EUt(VA[EV]));
        }
        builder_pump.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("pump_%s", VN[tier].toLowerCase()))
                .circuitMeta(3)
                .inputItems(motor.copyWithCount(12))
                .inputItems(pipe, 12)
                .inputItems(cableGtSingle, material[2], 24)
                .inputFluids(material[0], L * 90)
                .inputFluids(material[3], L * 12 * fluidMultiplier)
                .inputFluids(material[4], 3000 * fluidMultiplier)
                .inputFluids(material[5], L * 12 * fluidMultiplier)
                .inputFluids(material[6], L * 12 * fluidMultiplier)
                .outputItems(pump.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(String.format("fluid_regulator_%s", VN[tier].toLowerCase()))
                .inputItems(pump)
                .inputItems(circuit, 2)
                .inputItems(GTOTagPrefix.CURVED_PLATE, material[0], 2)
                .circuitMeta(1)
                .outputItems((ItemStack) CraftingComponents.FLUID_REGULATOR.get(tier))
                .EUt(VA[tier])
                .duration(100)
                .save();

        RecipeBuilder builder_piston = ASSEMBLY_LINE_RECIPES.recipeBuilder(String.format("piston_%s", VN[tier].toLowerCase()))
                .inputItems(GTOTagPrefix.PISTON_HOUSING, material[0])
                .inputItems(motor)
                .inputItems(plate, material[0], 2)
                .inputItems(ring, material[0], 4)
                .inputItems(round, material[0], 16)
                .inputItems(rod, material[0], 4)
                .inputItems(gear, material[0])
                .inputItems(gearSmall, material[0], 2)
                .inputItems(cableGtSingle, material[2], 2)
                .inputFluids(material[3], L * fluidMultiplier)
                .inputFluids(material[4], 250 * fluidMultiplier)
                .inputFluids(material[5], L * fluidMultiplier)
                .outputItems(piston)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_piston.researchStation(b -> b
                    .researchStack((ItemStack) PISTON.get(tier - 1))
                    .CWUt(1 << (tier - 3))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_piston.scanner(b -> b
                    .researchStack(ELECTRIC_PISTON_LuV.asItem())
                    .duration(1200)
                    .EUt(VA[IV]));
        } else {
            builder_piston.scanner(b -> b
                    .researchStack(ELECTRIC_PISTON_IV.asItem())
                    .duration(900)
                    .EUt(VA[EV]));
        }
        builder_piston.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("piston_%s", VN[tier].toLowerCase()))
                .circuitMeta(4)
                .inputItems(motor.copyWithCount(12))
                .inputItems(cableGtSingle, material[2], 24)
                .inputFluids(material[0], L * 192)
                .inputFluids(material[3], L * 12 * fluidMultiplier)
                .inputFluids(material[4], 3000 * fluidMultiplier)
                .inputFluids(material[5], L * 12 * fluidMultiplier)
                .outputItems(piston.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        RecipeBuilder builder_arm = ASSEMBLY_LINE_RECIPES.recipeBuilder(String.format("arm_%s", VN[tier].toLowerCase()))
                .inputItems(rodLong, material[0], 4)
                .inputItems(gear, material[0])
                .inputItems(gearSmall, material[0], 3)
                .inputItems(motor.copyWithCount(2))
                .inputItems(piston)
                .inputItems(circuit)
                .inputItems((TagKey<Item>) CIRCUIT.get(tier - 1), 2)
                .inputItems((TagKey<Item>) CIRCUIT.get(tier - 2), 4)
                .inputItems(cableGtSingle, material[2], 4)
                .inputFluids(material[3], L * 2 * fluidMultiplier)
                .inputFluids(material[4], 250 * fluidMultiplier)
                .inputFluids(material[5], L * fluidMultiplier)
                .outputItems(robot_arm)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_arm.researchStation(b -> b
                    .researchStack((ItemStack) ROBOT_ARM.get(tier - 1))
                    .CWUt(1 << (tier - 3))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_arm.scanner(b -> b
                    .researchStack(ROBOT_ARM_LuV.asItem())
                    .duration(1200)
                    .EUt(VA[IV]));
        } else {
            builder_arm.scanner(b -> b
                    .researchStack(ROBOT_ARM_IV.asItem())
                    .duration(900)
                    .EUt(VA[EV]));
        }
        builder_arm.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("arm_%s", VN[tier].toLowerCase()))
                .circuitMeta(5)
                .inputItems(motor.copyWithCount(24))
                .inputItems(piston.copyWithCount(12))
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asItem(), 12)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier - 1].asItem(), 24)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier - 3].asItem(), 36)
                .inputItems(cableGtSingle, material[2], 48)
                .inputFluids(material[0], L * 132)
                .inputFluids(material[3], L * 24 * fluidMultiplier)
                .inputFluids(material[4], 3000 * fluidMultiplier)
                .inputFluids(material[5], L * 12 * fluidMultiplier)
                .outputItems(robot_arm.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        RecipeBuilder builder_emitter = ASSEMBLY_LINE_RECIPES.recipeBuilder(String.format("emitter_%s", VN[tier].toLowerCase()))
                .inputItems(frameGt, material[7])
                .inputItems(GTOTagPrefix.EMITTER_BASES, material[0])
                .inputItems(motor)
                .inputItems(rodLong, material[0], 2)
                .inputItems(emitter_gem, 2)
                .inputItems(circuit, 2)
                .inputItems(foil, material[8], 64)
                .inputItems(foil, material[9], 64)
                .inputItems(cableGtSingle, material[2], 4)
                .inputFluids(material[3], L * 2 * fluidMultiplier)
                .inputFluids(material[5], L * fluidMultiplier)
                .outputItems(emitter)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_emitter.researchStation(b -> b
                    .researchStack((ItemStack) EMITTER.get(tier - 1))
                    .CWUt((int) ((1 << (tier - 3)) * 1.5))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_emitter.researchStation(b -> b
                    .researchStack((ItemStack) EMITTER.get(ZPM - 1))
                    .CWUt(1 << (tier - 4))
                    .EUt(VA[tier - 1]));
        } else {
            builder_emitter.scanner(b -> b
                    .researchStack(EMITTER_IV.asItem())
                    .duration(600)
                    .EUt(VA[IV]));
        }
        builder_emitter.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("emitter_%s", VN[tier].toLowerCase()))
                .circuitMeta(6)
                .inputItems(motor.copyWithCount(12))
                .inputItems(emitter_gem, 24)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asItem(), 24)
                .inputItems(cableGtSingle, material[2], 48)
                .inputFluids(material[0], L * 72)
                .inputFluids(material[3], L * 24 * fluidMultiplier)
                .inputFluids(material[5], L * 12 * fluidMultiplier)
                .inputFluids(material[7], L * 24)
                .inputFluids(material[8], L * 192)
                .inputFluids(material[9], L * 192)
                .outputItems(emitter.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        RecipeBuilder builder_sensor = ASSEMBLY_LINE_RECIPES.recipeBuilder(String.format("sensor_%s", VN[tier].toLowerCase()))
                .inputItems(frameGt, material[7])
                .inputItems(GTOTagPrefix.SENSOR_CASING, material[0])
                .inputItems(motor)
                .inputItems(plate, material[0], 2)
                .inputItems(emitter_gem, 2)
                .inputItems(circuit, 2)
                .inputItems(foil, material[8], 64)
                .inputItems(foil, material[9], 64)
                .inputItems(cableGtSingle, material[2], 4)
                .inputFluids(material[3], L * 2 * fluidMultiplier)
                .inputFluids(material[5], L * fluidMultiplier)
                .outputItems(sensor)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_sensor.researchStation(b -> b
                    .researchStack((ItemStack) SENSOR.get(tier - 1))
                    .CWUt((int) ((1 << (tier - 3)) * 1.5))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_sensor.researchStation(b -> b
                    .researchStack((ItemStack) SENSOR.get(ZPM - 1))
                    .CWUt(1 << (tier - 4))
                    .EUt(VA[tier - 1]));
        } else {
            builder_sensor.scanner(b -> b
                    .researchStack(SENSOR_IV.asItem())
                    .duration(600)
                    .EUt(VA[IV]));
        }
        builder_sensor.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("sensor_%s", VN[tier].toLowerCase()))
                .circuitMeta(7)
                .inputItems(motor.copyWithCount(12))
                .inputItems(emitter_gem, 24)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asItem(), 24)
                .inputItems(cableGtSingle, material[2], 48)
                .inputFluids(material[0], L * 78)
                .inputFluids(material[3], L * 24 * fluidMultiplier)
                .inputFluids(material[5], L * 12 * fluidMultiplier)
                .inputFluids(material[7], L * 24)
                .inputFluids(material[8], L * 192)
                .inputFluids(material[9], L * 192)
                .outputItems(sensor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        RecipeBuilder builder_field_generator = ASSEMBLY_LINE_RECIPES.recipeBuilder(String.format("field_generator_%s", VN[tier].toLowerCase()))
                .inputItems(frameGt, material[7])
                .inputItems(GTOTagPrefix.FIELD_GENERATOR_CASING, material[0])
                .inputItems(emitter_gem)
                .inputItems(emitter.copyWithCount(2))
                .inputItems(circuit, 2)
                .inputItems(wireFine, material[10], 64)
                .inputItems(wireFine, material[10], 64)
                .inputItems(cableGtSingle, material[2], 4)
                .inputFluids(material[3], L * 2 * fluidMultiplier)
                .inputFluids(material[5], L * fluidMultiplier)
                .outputItems(field_generator)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_field_generator.researchStation(b -> b
                    .researchStack((ItemStack) FIELD_GENERATOR.get(tier - 1))
                    .CWUt((int) ((1 << (tier - 3)) * 1.5))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_field_generator.researchStation(b -> b
                    .researchStack((ItemStack) FIELD_GENERATOR.get(ZPM - 1))
                    .CWUt(1 << (tier - 4))
                    .EUt(VA[tier - 1]));
        } else {
            builder_field_generator.scanner(b -> b
                    .researchStack(FIELD_GENERATOR_IV.asItem())
                    .duration(600)
                    .EUt(VA[IV]));
        }
        builder_field_generator.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("field_generator_%s", VN[tier].toLowerCase()))
                .circuitMeta(8)
                .inputItems(emitter.copyWithCount(24))
                .inputItems(emitter_gem, 12)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asItem(), 24)
                .inputItems(cableGtSingle, material[2], 48)
                .inputFluids(material[0], L * 96)
                .inputFluids(material[3], L * 24 * fluidMultiplier)
                .inputFluids(material[5], L * 12 * fluidMultiplier)
                .inputFluids(material[7], L * 24)
                .inputFluids(material[10], L * 384)
                .outputItems(field_generator.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();
    }

    private static int getFluidMultiplier(int tier) {
        return switch (tier) {
            case 12, 13, 14, 15, 16 -> 16;
            case 9, 10, 11 -> 8;
            case 8, 7 -> 4;
            case 6 -> 2;
            default -> 1;
        };
    }

    /**
     *
     * @param tier                 Tier
     * @param computationComponent Computation Component
     * @param computationUnit      Computation Component & Computation Unit
     * @param pipe                 Fluid Pipe
     * @param material
     * 
     *                             <pre>
     * <code>
     *     Material mainMaterial = material[0];
     *     Material wireMaterial = material[1];
     *     Material superConductorMaterial = material[2];
     *     Material solderMaterial = material[3];
     *     Material insulatorMaterial = material[4];
     *     Material rubberMaterial = material[5];
     *     Material enclosureMaterial = material[6];
     * </code>
     *                             </pre>
     */
    private static void integrated_control_core(int tier, Item computationComponent, Item computationUnit, Item pipe, Material... material) {
        int fluidMultiplier = getFluidMultiplier(tier);
        ItemStack field_generator = (ItemStack) FIELD_GENERATOR.get(tier);
        ItemStack sensor = (ItemStack) SENSOR.get(tier);
        ItemStack pump = (ItemStack) PUMP.get(tier);
        TagKey<Item> circuit = (TagKey<Item>) CIRCUIT.get(tier);

        ASSEMBLY_LINE_RECIPES.recipeBuilder(String.format("integrated_control_core_%s", VN[tier].toLowerCase()))
                .inputItems(ring, material[0], 16)
                .inputItems(circuit, 2)
                .inputItems(field_generator)
                .inputItems(sensor)
                .inputItems(pump)
                .inputItems(computationComponent, 2)
                .inputItems(computationUnit)
                .inputItems(foil, material[6], 64)
                .inputItems(wireFine, material[1], 64)
                .inputItems(wireGtSingle, material[2], 8)
                .inputItems(pipe, 4)
                .inputFluids(material[3], L * 2 * fluidMultiplier)
                .inputFluids(material[4], L * 4 * fluidMultiplier)
                .inputFluids(material[5], L * fluidMultiplier)
                .outputItems((ItemStack) CraftingComponents.INTEGRATED_CONTROL_CORE.get(tier))
                .researchStation(b -> b
                        .researchStack(tier == UV ? GTOItems.WETWARE_SOC.asStack() : (ItemStack) CraftingComponents.INTEGRATED_CONTROL_CORE.get(tier - 1))
                        .CWUt((int) ((1 << (tier - 3)) * 1.5))
                        .EUt(VA[tier - 1]))
                .duration(1200).EUt(VA[tier]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(String.format("integrated_control_core_%s", VN[tier].toLowerCase()))
                .circuitMeta(9)
                .inputItems(field_generator.copyWithCount(12))
                .inputItems(sensor.copyWithCount(12))
                .inputItems(pump.copyWithCount(12))
                .inputItems(computationUnit, 12)
                .inputItems(computationComponent, 24)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asItem(), 48)
                .inputItems(cableGtSingle, material[2], 96)
                .inputFluids(material[0], L * 96)
                .inputFluids(material[3], L * 24 * fluidMultiplier)
                .inputFluids(material[5], L * 12 * fluidMultiplier)
                .inputFluids(material[6], L * 24)
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .outputItems(((ItemStack) CraftingComponents.INTEGRATED_CONTROL_CORE.get(tier)).copyWithCount(16))
                .duration(2400).EUt(VA[tier]).save();
    }
}
