package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.data.CraftingComponents;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.*;
import static com.gto.gtocore.api.GTOValues.COMPONENT_ASSEMBLY_CASING_TIER;
import static com.gto.gtocore.common.data.GTOMaterials.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;

public interface ComponentRecipes {

    static void init(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("ulv_electric_motor"), GTOItems.ULV_ELECTRIC_MOTOR.asStack(),
                "CWR", "WMW", "RWC", 'C', new UnificationEntry(GTOTagPrefix.curvedPlate, Copper), 'W', new UnificationEntry(wireGtSingle, Tin),
                'R', new UnificationEntry(rod, Copper), 'M', new UnificationEntry(rod, IronMagnetic));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("ulv_electric_piston"), GTOItems.ULV_ELECTRIC_PISTON.asStack(),
                "PPP", "CRR", "CMG", 'P', new UnificationEntry(plate, Copper), 'C', new UnificationEntry(cableGtSingle, Lead),
                'R', new UnificationEntry(rod, Copper), 'M', GTOItems.ULV_ELECTRIC_MOTOR.asStack(), 'G', new UnificationEntry(gearSmall, Copper));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("ulv_electric_conveyor"), GTOItems.ULV_CONVEYOR_MODULE.asStack(),
                "RRR", "MCM", "RRR", 'R', new UnificationEntry(plate, Rubber), 'M', GTOItems.ULV_ELECTRIC_MOTOR.asStack(),
                'C', new UnificationEntry(cableGtSingle, Lead));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("ulv_electric_pump"), GTOItems.ULV_ELECTRIC_PUMP.asStack(),
                "SFR", "sPw", "RMC", 'S', new UnificationEntry(screw, Iron), 'R', new UnificationEntry(ring, Rubber),
                'F', new UnificationEntry(rotor, Iron), 'P', new UnificationEntry(pipeNormalFluid, Copper), 'M', GTOItems.ULV_ELECTRIC_MOTOR.asStack(), 'C', new UnificationEntry(cableGtSingle, Lead));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("ulv_robot_arm"), GTOItems.ULV_ROBOT_ARM.asStack(),
                "CCC", "MRM", "PVR", 'R', new UnificationEntry(rod, Copper), 'C', new UnificationEntry(cableGtSingle, Lead),
                'M', GTOItems.ULV_ELECTRIC_MOTOR.asStack(), 'P', GTOItems.ULV_ELECTRIC_PISTON.asStack(), 'V', VACUUM_TUBE.asStack());
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("ulv_fluid_regulator"), GTOItems.ULV_FLUID_REGULATOR.asStack(),
                "SFR", "sPw", "RMC", 'S', new UnificationEntry(screw, Iron), 'R', VACUUM_TUBE.asStack(),
                'F', new UnificationEntry(rotor, Iron), 'P', GTOItems.ULV_ELECTRIC_PUMP.asStack(), 'M', GTOItems.ULV_ELECTRIC_MOTOR.asStack(), 'C', new UnificationEntry(cableGtSingle, Lead));

        assembler(provider, LV, ChemicalHelper.get(gem, Quartzite), ChemicalHelper.get(gem, EnderPearl), Steel, Tin, SteelMagnetic, Copper, Rubber, Bronze, Tin, Brass, ManganesePhosphide);
        assembler(provider, MV, ChemicalHelper.get(gemFlawless, Emerald), ChemicalHelper.get(gem, EnderEye), Aluminium, Copper, SteelMagnetic, Cupronickel, Rubber, Steel, Bronze, Electrum, MagnesiumDiboride);
        assembler(provider, HV, ChemicalHelper.get(gem, EnderEye), QUANTUM_EYE.asStack(), StainlessSteel, Silver, SteelMagnetic, Electrum, Rubber, VanadiumSteel, Steel, Chromium, MercuryBariumCalciumCuprate);
        assembler(provider, EV, QUANTUM_EYE.asStack(), ChemicalHelper.get(gem, NetherStar), Titanium, Aluminium, NeodymiumMagnetic, Kanthal, SiliconeRubber, StainlessSteel, Aluminium, Platinum, UraniumTriplatinum);
        assembler(provider, IV, QUANTUM_STAR.asStack(), QUANTUM_STAR.asStack(), TungstenSteel, Tungsten, NeodymiumMagnetic, Graphene, SiliconeRubber, TungstenCarbide, Titanium, Iridium, SamariumIronArsenicOxide);

        assembly_line(provider, LuV, ChemicalHelper.get(pipeSmallFluid, NiobiumTitanium), QUANTUM_STAR.asStack(), ChemicalHelper.get(rodLong, SamariumMagnetic), HSSS, Ruridit, NiobiumTitanium, SolderingAlloy, Lubricant, HSSE, SiliconeRubber, HSSS, Palladium, Ruthenium, IndiumTinBariumTitaniumCuprate);
        assembly_line(provider, ZPM, ChemicalHelper.get(pipeNormalFluid, Polybenzimidazole), QUANTUM_STAR.asStack(), ChemicalHelper.get(rodLong, SamariumMagnetic), Osmiridium, Europium, VanadiumGallium, SolderingAlloy, Lubricant, MarM200Steel, StyreneButadieneRubber, NaquadahAlloy, Trinium, Duranium, UraniumRhodiumDinaquadide);
        assembly_line(provider, UV, ChemicalHelper.get(pipeLargeFluid, Naquadah), GRAVI_STAR.asStack(), ChemicalHelper.get(rodLong, SamariumMagnetic), Tritanium, Americium, YttriumBariumCuprate, SolderingAlloy, Lubricant, Naquadria, StyreneButadieneRubber, Tritanium, Naquadah, Tritanium, EnrichedNaquadahTriniumEuropiumDuranide);
        assembly_line(provider, UHV, ChemicalHelper.get(pipeNormalFluid, Neutronium), GRAVI_STAR.asStack(), GTOItems.MAGNETIC_LONG_NETHERITE_ROD.asStack(), Orichalcum, AbyssalAlloy, Europium, MutatedLivingSolder, Lubricant, HighDurabilityCompoundSteel, StyreneButadieneRubber, Neutronium, AbyssalAlloy, FluxedElectrum, RutheniumTriniumAmericiumNeutronate);
        assembly_line(provider, UEV, ChemicalHelper.get(pipeNormalFluid, Enderium), GRAVI_STAR.asStack(), GTOItems.MAGNETIC_LONG_NETHERITE_ROD.asStack(), HastelloyX78, TitanSteel, Mithril, MutatedLivingSolder, Lubricant, Bohrium, StyreneButadieneRubber, Quantanium, TitanSteel, Dalisenite, Enderite);
        assembly_line(provider, UIV, ChemicalHelper.get(pipeNormalFluid, FullerenePolymerMatrixPulp), GTOItems.UNSTABLE_STAR.asStack(), ChemicalHelper.get(rodLong, AttunedTengam), Infuscolium, Adamantine, Neutronium, SuperMutatedLivingSolder, Lubricant, Taranium, StyreneButadieneRubber, Adamantium, Adamantine, ArceusAlloy2B, Echoite);
        assembly_line(provider, UXV, ChemicalHelper.get(pipeNormalFluid, HeavyQuarkDegenerateMatter), GTOItems.UNSTABLE_STAR.asStack(), ChemicalHelper.get(rodLong, AttunedTengam), HastelloyK243, NaquadriaticTaranium, Taranium, SuperMutatedLivingSolder, Lubricant, AstralTitanium, StyreneButadieneRubber, Vibranium, NaquadriaticTaranium, TitanPrecisionSteel, Legendarium);
        assembly_line(provider, OpV, ChemicalHelper.get(pipeLargeFluid, HeavyQuarkDegenerateMatter), GTOItems.UNSTABLE_STAR.asStack(), ChemicalHelper.get(rodLong, AttunedTengam), Vibramantium, Starmetal, CrystalMatrix, SuperMutatedLivingSolder, Lubricant, CelestialTungsten, StyreneButadieneRubber, Draconium, Starmetal, Hikarium, AwakenedDraconium);

        GTORecipeTypes.COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id("max_field_generator"))
                .inputItems(TagPrefix.plateDouble, GTOMaterials.Chaos, 288)
                .inputItems(CustomTags.MAX_CIRCUITS, 96)
                .inputItems(GTOItems.MAX_EMITTER.asStack(96))
                .inputItems(GTOItems.NUCLEAR_STAR.asStack(192))
                .inputItems(TagPrefix.frameGt, GTOMaterials.Infinity, 48)
                .inputItems(TagPrefix.wireFine, GTOMaterials.Cosmic, 3072)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 96)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(1769472))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(3538944))
                .inputFluids(GTOMaterials.BlackDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.WhiteDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.Shirabon.getFluid(110592))
                .inputFluids(GTOMaterials.Infinity.getFluid(27648))
                .outputItems(GTOItems.MAX_FIELD_GENERATOR.asStack(64))
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        GTORecipeTypes.COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id("max_sensor"))
                .circuitMeta(7)
                .inputItems(CustomTags.MAX_CIRCUITS, 96)
                .inputItems(TagPrefix.plate, GTOMaterials.TranscendentMetal, 192)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asStack(48))
                .inputItems(GTOItems.NUCLEAR_STAR.asStack(192))
                .inputItems(TagPrefix.frameGt, GTOMaterials.Infinity, 48)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 96)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(1769472))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(3538944))
                .inputFluids(GTOMaterials.BlackDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.WhiteDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.Shirabon.getFluid(110592))
                .inputFluids(GTOMaterials.Infinity.getFluid(27648))
                .outputItems(GTOItems.MAX_SENSOR.asStack(64))
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        GTORecipeTypes.COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id("max_electric_piston"))
                .circuitMeta(2)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asStack(48))
                .inputItems(TagPrefix.plateDouble, GTOMaterials.TranscendentMetal, 192)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 48)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(884736))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(1769472))
                .inputFluids(GTMaterials.Lubricant.getFluid(3072000))
                .inputFluids(GTOMaterials.TranscendentMetal.getFluid(148992))
                .inputFluids(GTOMaterials.Infinity.getFluid(27648))
                .outputItems(GTOItems.MAX_ELECTRIC_PISTON.asStack(64))
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        GTORecipeTypes.COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id("max_electric_pump"))
                .circuitMeta(3)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.TranscendentMetal, 96)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asStack(48))
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 48)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(884736))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(1769472))
                .inputFluids(GTMaterials.Lubricant.getFluid(3072000))
                .inputFluids(GTMaterials.StyreneButadieneRubber.getFluid(110592))
                .inputFluids(GTOMaterials.TranscendentMetal.getFluid(33792))
                .inputFluids(GTOMaterials.BlackDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.WhiteDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.Infinity.getFluid(41472))
                .inputFluids(GTMaterials.Neutronium.getFluid(442368))
                .outputItems(GTOItems.MAX_ELECTRIC_PUMP.asStack(64))
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        GTORecipeTypes.COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id("max_conveyor_module"))
                .circuitMeta(5)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asStack(96))
                .inputItems(TagPrefix.plateDouble, GTOMaterials.TranscendentMetal, 96)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 48)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(884736))
                .inputFluids(GTMaterials.Lubricant.getFluid(3072000))
                .inputFluids(GTMaterials.StyreneButadieneRubber.getFluid(1050624))
                .inputFluids(GTOMaterials.TranscendentMetal.getFluid(44544))
                .inputFluids(GTOMaterials.BlackDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.WhiteDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.Infinity.getFluid(27648))
                .outputItems(GTOItems.MAX_CONVEYOR_MODULE.asStack(64))
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        GTORecipeTypes.COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id("max_electric_motor"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rodLong, GTOMaterials.Magmatter, 48)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 48)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(768000))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(1536000))
                .inputFluids(GTMaterials.Lubricant.getFluid(3072000))
                .inputFluids(GTOMaterials.BlackDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.WhiteDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.Infinity.getFluid(27648))
                .inputFluids(GTOMaterials.TranscendentMetal.getFluid(95232))
                .outputItems(GTOItems.MAX_ELECTRIC_MOTOR.asStack(64))
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        GTORecipeTypes.COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id("max_robot_arm"))
                .circuitMeta(4)
                .inputItems(CustomTags.UXV_CIRCUITS, 192)
                .inputItems(CustomTags.OpV_CIRCUITS, 96)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asStack(192))
                .inputItems(GTOItems.MAX_ELECTRIC_PISTON.asStack(48))
                .inputItems(CustomTags.MAX_CIRCUITS, 48)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 96)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(884736))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(1769472))
                .inputFluids(GTMaterials.Lubricant.getFluid(3072000))
                .inputFluids(GTOMaterials.TranscendentMetal.getFluid(152064))
                .inputFluids(GTOMaterials.Infinity.getFluid(27648))
                .outputItems(GTOItems.MAX_ROBOT_ARM.asStack(64))
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();

        GTORecipeTypes.COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id("max_emitter"))
                .circuitMeta(6)
                .inputItems(CustomTags.MAX_CIRCUITS, 96)
                .inputItems(GTOItems.MAX_ELECTRIC_MOTOR.asStack(48))
                .inputItems(GTOItems.NUCLEAR_STAR.asStack(192))
                .inputItems(TagPrefix.frameGt, GTOMaterials.Infinity, 48)
                .inputItems(TagPrefix.foil, GTOMaterials.Cosmic, 3072)
                .inputItems(TagPrefix.cableGtHex, GTOMaterials.CosmicNeutronium, 96)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(1769472))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(3538944))
                .inputFluids(GTOMaterials.TranscendentMetal.getFluid(55296))
                .inputFluids(GTOMaterials.BlackDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.WhiteDwarfMatter.getFluid(110592))
                .inputFluids(GTOMaterials.Shirabon.getFluid(110592))
                .inputFluids(GTOMaterials.Infinity.getFluid(27648))
                .outputItems(GTOItems.MAX_EMITTER.asStack(64))
                .EUt(2013265920)
                .duration(12000)
                .addData(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER, 14)
                .save();
    }

    private static void assembler(Consumer<FinishedRecipe> provider, int tier, ItemStack emitter_gem, ItemStack field_gem, Material... material) {
        ItemStack motor = (ItemStack) MOTOR.get(tier);
        ItemStack conveyor = (ItemStack) CONVEYOR.get(tier);
        ItemStack pump = (ItemStack) PUMP.get(tier);
        ItemStack piston = (ItemStack) PISTON.get(tier);
        ItemStack robot_arm = (ItemStack) ROBOT_ARM.get(tier);
        ItemStack emitter = (ItemStack) EMITTER.get(tier);
        ItemStack sensor = (ItemStack) SENSOR.get(tier);
        ItemStack field_generator = (ItemStack) FIELD_GENERATOR.get(tier);
        TagKey<Item> circuit = (TagKey<Item>) CIRCUIT.get(tier);
        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("motor_%s", VN[tier].toLowerCase())))
                .inputItems(GTOTagPrefix.motorEnclosure, material[0])
                .inputItems(rod, material[0], 2)
                .inputItems(rod, material[2])
                .inputItems(wireGtSingle, material[3], 4)
                .inputItems(cableGtSingle, material[1], 2)
                .outputItems(motor)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("motor_%s", VN[tier].toLowerCase())))
                .circuitMeta(1)
                .inputItems(rod, material[2], 12)
                .inputItems(wireGtSingle, material[3], 48)
                .inputItems(cableGtSingle, material[1], 24)
                .inputFluids(material[0].getFluid(L * 36))
                .outputItems(motor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("conveyor_%s", VN[tier].toLowerCase())))
                .inputItems(motor.copyWithCount(2))
                .inputItems(ring, material[0], 2)
                .inputItems(cableGtSingle, material[1], 2)
                .inputFluids(material[4].getFluid(L * 6))
                .outputItems(conveyor)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("conveyor_%s", VN[tier].toLowerCase())))
                .circuitMeta(2)
                .inputItems(motor.copyWithCount(24))
                .inputItems(cableGtSingle, material[1], 24)
                .inputFluids(material[0].getFluid(L * 6))
                .inputFluids(material[4].getFluid(L * 72))
                .outputItems(conveyor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("pump_%s", VN[tier].toLowerCase())))
                .inputItems(GTOTagPrefix.pumpBarrel, material[0])
                .inputItems(pipeNormalFluid, material[5])
                .inputItems(motor.copyWithCount(1))
                .inputItems(screw, material[6], 2)
                .inputItems(rotor, material[6])
                .inputItems(cableGtSingle, material[1], 2)
                .inputFluids(material[4].getFluid(L / 2))
                .outputItems(pump)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("pump_%s", VN[tier].toLowerCase())))
                .circuitMeta(3)
                .inputItems(motor.copyWithCount(12))
                .inputItems(cableGtSingle, material[1], 24)
                .inputFluids(material[0].getFluid(L * 30))
                .inputFluids(material[5].getFluid(L * 36))
                .inputFluids(material[6].getFluid(L * 51))
                .inputFluids(material[4].getFluid(L * 6))
                .outputItems(pump.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("fluid_regulator_%s", VN[tier].toLowerCase())))
                .inputItems(pump)
                .inputItems(circuit, 2)
                .circuitMeta(1)
                .outputItems((ItemStack) CraftingComponents.FLUID_REGULATOR.get(tier))
                .EUt(VA[tier])
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("piston_%s", VN[tier].toLowerCase())))
                .inputItems(GTOTagPrefix.pistonHousing, material[0])
                .inputItems(rod, material[0], 2)
                .inputItems(cableGtSingle, material[1], 2)
                .inputItems(plate, material[0])
                .inputItems(gearSmall, material[0])
                .inputItems(motor)
                .outputItems(piston)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("piston_%s", VN[tier].toLowerCase())))
                .circuitMeta(4)
                .inputItems(motor.copyWithCount(12))
                .inputItems(cableGtSingle, material[1], 24)
                .inputFluids(material[0].getFluid(L * (36 + 6 * 12 + 12)))
                .outputItems(piston.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("arm_%s", VN[tier].toLowerCase())))
                .inputItems(cableGtSingle, material[1], 3)
                .inputItems(rod, material[0], 2)
                .inputItems(motor.copyWithCount(2))
                .inputItems(piston)
                .inputItems(circuit)
                .outputItems(robot_arm)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("arm_%s", VN[tier].toLowerCase())))
                .circuitMeta(5)
                .inputItems(motor.copyWithCount(12))
                .inputItems(piston.copyWithCount(12))
                .inputItems(cableGtSingle, material[1], 36)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asStack(12))
                .inputFluids(material[0].getFluid(L * 24))
                .outputItems(robot_arm.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("emitter_%s", VN[tier].toLowerCase())))
                .inputItems(GTOTagPrefix.emitterBases, material[0])
                .inputItems(rod, material[7], 2)
                .inputItems(cableGtSingle, material[1], 2)
                .inputItems(circuit, 2)
                .inputItems(emitter_gem)
                .outputItems(emitter)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("emitter_%s", VN[tier].toLowerCase())))
                .circuitMeta(6)
                .inputItems(emitter_gem.copyWithCount(12))
                .inputItems(cableGtSingle, material[1], 24)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asStack(24))
                .inputFluids(material[0].getFluid(L * 48))
                .inputFluids(material[7].getFluid(L * 12))
                .outputItems(emitter.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("sensor_%s", VN[tier].toLowerCase())))
                .inputItems(GTOTagPrefix.sensorCasing, material[0])
                .inputItems(rod, material[7])
                .inputItems(cableGtSingle, material[1])
                .inputItems(circuit, 2)
                .inputItems(emitter_gem)
                .outputItems(sensor)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("sensor_%s", VN[tier].toLowerCase())))
                .circuitMeta(7)
                .inputItems(emitter_gem.copyWithCount(12))
                .inputItems(cableGtSingle, material[1], 12)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asStack(24))
                .inputFluids(material[0].getFluid(L * 54))
                .inputFluids(material[7].getFluid(L * 6))
                .outputItems(sensor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("field_generator_%s", VN[tier].toLowerCase())))
                .inputItems(GTOTagPrefix.fieldGeneratorCasing, material[0])
                .inputItems(emitter)
                .inputItems(field_gem)
                .inputItems(circuit, 2)
                .inputItems(wireGtQuadruple, material[8], 4)
                .outputItems(field_generator)
                .duration(100).EUt(VA[tier - 1]).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("field_generator_%s", VN[tier].toLowerCase())))
                .circuitMeta(8)
                .inputItems(emitter.copyWithCount(12))
                .inputItems(field_gem.copyWithCount(12))
                .inputItems(wireGtQuadruple, material[8], 48)
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asStack(24))
                .inputFluids(material[0].getFluid(L * 96))
                .outputItems(field_generator.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(400).EUt(VA[tier]).save();
    }

    private static void assembly_line(Consumer<FinishedRecipe> provider, int tier, ItemStack pipe, ItemStack emitter_gem, ItemStack magnetic, Material... material) {
        int multiplier = tier > ZPM ? 4 : tier == ZPM ? 2 : 1;
        ItemStack motor = (ItemStack) MOTOR.get(tier);
        ItemStack conveyor = (ItemStack) CONVEYOR.get(tier);
        ItemStack pump = (ItemStack) PUMP.get(tier);
        ItemStack piston = (ItemStack) PISTON.get(tier);
        ItemStack robot_arm = (ItemStack) ROBOT_ARM.get(tier);
        ItemStack emitter = (ItemStack) EMITTER.get(tier);
        ItemStack sensor = (ItemStack) SENSOR.get(tier);
        ItemStack field_generator = (ItemStack) FIELD_GENERATOR.get(tier);
        TagKey<Item> circuit = (TagKey<Item>) CIRCUIT.get(tier);

        GTORecipeBuilder builder_motor = ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id(String.format("motor_%s", VN[tier].toLowerCase())))
                .inputItems(GTOTagPrefix.motorEnclosure, material[0])
                .inputItems(magnetic)
                .inputItems(rodLong, material[0], 4)
                .inputItems(ring, material[0], 4)
                .inputItems(round, material[0], 8)
                .inputFluids(material[3].getFluid(L * multiplier))
                .inputFluids(material[4].getFluid(250 * multiplier))
                .inputFluids(material[5].getFluid(L * multiplier))
                .outputItems(motor)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_motor.inputItems(wireFine, material[1], 64)
                    .inputItems(wireFine, material[1], 64)
                    .stationResearch(b -> b
                            .researchStack((ItemStack) MOTOR.get(tier - 1))
                            .CWUt(1 << (tier - 3))
                            .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_motor.inputItems(wireFine, material[1], 64)
                    .scannerResearch(b -> b
                            .researchStack(ELECTRIC_MOTOR_LuV.asStack())
                            .duration(1200)
                            .EUt(VA[IV]));
        } else {
            builder_motor.inputItems(wireFine, material[1], 32)
                    .scannerResearch(b -> b
                            .researchStack(ELECTRIC_MOTOR_IV.asStack())
                            .duration(900)
                            .EUt(VA[EV]));
        }
        builder_motor.inputItems(cableGtSingle, material[2], 2).save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("motor_%s", VN[tier].toLowerCase())))
                .circuitMeta(1)
                .inputItems(magnetic.copyWithCount(12))
                .inputItems(cableGtSingle, material[2], 24)
                .inputFluids(material[0].getFluid(L * 95))
                .inputFluids(material[1].getFluid(L * 48 * multiplier))
                .inputFluids(material[3].getFluid(L * 12 * multiplier))
                .inputFluids(material[4].getFluid(3000 * multiplier))
                .inputFluids(material[5].getFluid(L * 12 * multiplier))
                .outputItems(motor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        GTORecipeBuilder builder_conveyor = ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id(String.format("conveyor_%s", VN[tier].toLowerCase())))
                .inputItems(motor.copyWithCount(2))
                .inputItems(plate, material[0], 2)
                .inputItems(ring, material[0], 4)
                .inputItems(round, material[0], 16)
                .inputItems(screw, material[0], 4)
                .inputItems(cableGtSingle, material[2], 2)
                .inputFluids(material[3].getFluid(L * multiplier))
                .inputFluids(material[4].getFluid(250 * multiplier))
                .inputFluids(material[6].getFluid(L * 8 * multiplier))
                .inputFluids(material[5].getFluid(L * multiplier))
                .outputItems(conveyor)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_conveyor.stationResearch(b -> b
                    .researchStack((ItemStack) CONVEYOR.get(tier - 1))
                    .CWUt(1 << (tier - 3))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_conveyor.scannerResearch(b -> b
                    .researchStack(CONVEYOR_MODULE_LuV.asStack())
                    .duration(1200)
                    .EUt(VA[IV]));
        } else {
            builder_conveyor.scannerResearch(b -> b
                    .researchStack(CONVEYOR_MODULE_IV.asStack())
                    .duration(900)
                    .EUt(VA[EV]));
        }
        builder_conveyor.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("conveyor_%s", VN[tier].toLowerCase())))
                .circuitMeta(2)
                .inputItems(motor.copyWithCount(24))
                .inputItems(cableGtSingle, material[2], 24)
                .inputFluids(material[0].getFluid(L * 63))
                .inputFluids(material[3].getFluid(L * 12 * multiplier))
                .inputFluids(material[4].getFluid(3000 * multiplier))
                .inputFluids(material[5].getFluid(L * 12 * multiplier))
                .inputFluids(material[6].getFluid(L * 96 * multiplier))
                .outputItems(conveyor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        GTORecipeBuilder builder_pump = ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id(String.format("pump_%s", VN[tier].toLowerCase())))
                .inputItems(GTOTagPrefix.pumpBarrel, material[0])
                .inputItems(motor)
                .inputItems(pipe)
                .inputItems(screw, material[0], 8)
                .inputItems(rotor, material[0])
                .inputItems(cableGtSingle, material[2], 2)
                .inputFluids(material[3].getFluid(L * multiplier))
                .inputFluids(material[4].getFluid(250 * multiplier))
                .inputFluids(material[6].getFluid(L * multiplier))
                .inputFluids(material[5].getFluid(L * multiplier))
                .outputItems(pump)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_pump.stationResearch(b -> b
                    .researchStack((ItemStack) PUMP.get(tier - 1))
                    .CWUt(1 << (tier - 3))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_pump.scannerResearch(b -> b
                    .researchStack(ELECTRIC_PUMP_LuV.asStack())
                    .duration(1200)
                    .EUt(VA[IV]));
        } else {
            builder_pump.scannerResearch(b -> b
                    .researchStack(ELECTRIC_PUMP_IV.asStack())
                    .duration(900)
                    .EUt(VA[EV]));
        }
        builder_pump.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("pump_%s", VN[tier].toLowerCase())))
                .circuitMeta(3)
                .inputItems(motor.copyWithCount(12))
                .inputItems(pipe.copyWithCount(12))
                .inputItems(cableGtSingle, material[2], 24)
                .inputFluids(material[0].getFluid(L * 90))
                .inputFluids(material[3].getFluid(L * 12 * multiplier))
                .inputFluids(material[4].getFluid(3000 * multiplier))
                .inputFluids(material[5].getFluid(L * 12 * multiplier))
                .inputFluids(material[6].getFluid(L * 12 * multiplier))
                .outputItems(pump.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(String.format("fluid_regulator_%s", VN[tier].toLowerCase())))
                .inputItems(pump)
                .inputItems(circuit, 2)
                .circuitMeta(1)
                .outputItems((ItemStack) CraftingComponents.FLUID_REGULATOR.get(tier))
                .EUt(VA[tier])
                .duration(100)
                .save();

        GTORecipeBuilder builder_piston = ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id(String.format("piston_%s", VN[tier].toLowerCase())))
                .inputItems(GTOTagPrefix.pistonHousing, material[0])
                .inputItems(motor)
                .inputItems(plate, material[0], 2)
                .inputItems(ring, material[0], 4)
                .inputItems(round, material[0], 16)
                .inputItems(rod, material[0], 4)
                .inputItems(gear, material[0])
                .inputItems(gearSmall, material[0], 2)
                .inputItems(cableGtSingle, material[2], 2)
                .inputFluids(material[3].getFluid(L * multiplier))
                .inputFluids(material[4].getFluid(250 * multiplier))
                .inputFluids(material[5].getFluid(L * multiplier))
                .outputItems(piston)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_piston.stationResearch(b -> b
                    .researchStack((ItemStack) PISTON.get(tier - 1))
                    .CWUt(1 << (tier - 3))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_piston.scannerResearch(b -> b
                    .researchStack(ELECTRIC_PISTON_LuV.asStack())
                    .duration(1200)
                    .EUt(VA[IV]));
        } else {
            builder_piston.scannerResearch(b -> b
                    .researchStack(ELECTRIC_PISTON_IV.asStack())
                    .duration(900)
                    .EUt(VA[EV]));
        }
        builder_piston.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("piston_%s", VN[tier].toLowerCase())))
                .circuitMeta(4)
                .inputItems(motor.copyWithCount(12))
                .inputItems(cableGtSingle, material[2], 24)
                .inputFluids(material[0].getFluid(L * 192))
                .inputFluids(material[3].getFluid(L * 12 * multiplier))
                .inputFluids(material[4].getFluid(3000 * multiplier))
                .inputFluids(material[5].getFluid(L * 12 * multiplier))
                .outputItems(piston.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        GTORecipeBuilder builder_arm = ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id(String.format("arm_%s", VN[tier].toLowerCase())))
                .inputItems(rodLong, material[0], 4)
                .inputItems(gear, material[0])
                .inputItems(gearSmall, material[0], 3)
                .inputItems(motor.copyWithCount(2))
                .inputItems(piston)
                .inputItems(circuit)
                .inputItems((TagKey<Item>) CIRCUIT.get(tier - 1), 2)
                .inputItems((TagKey<Item>) CIRCUIT.get(tier - 2), 4)
                .inputItems(cableGtSingle, material[2], 4)
                .inputFluids(material[3].getFluid(L * 2 * multiplier))
                .inputFluids(material[4].getFluid(250 * multiplier))
                .inputFluids(material[5].getFluid(L * multiplier))
                .outputItems(robot_arm)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_arm.stationResearch(b -> b
                    .researchStack((ItemStack) ROBOT_ARM.get(tier - 1))
                    .CWUt(1 << (tier - 3))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_arm.scannerResearch(b -> b
                    .researchStack(ROBOT_ARM_LuV.asStack())
                    .duration(1200)
                    .EUt(VA[IV]));
        } else {
            builder_arm.scannerResearch(b -> b
                    .researchStack(ROBOT_ARM_IV.asStack())
                    .duration(900)
                    .EUt(VA[EV]));
        }
        builder_arm.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("arm_%s", VN[tier].toLowerCase())))
                .circuitMeta(5)
                .inputItems(motor.copyWithCount(24))
                .inputItems(piston.copyWithCount(12))
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asStack(12))
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier - 1].asStack(24))
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier - 3].asStack(36))
                .inputItems(cableGtSingle, material[2], 48)
                .inputFluids(material[0].getFluid(L * 132))
                .inputFluids(material[3].getFluid(L * 24 * multiplier))
                .inputFluids(material[4].getFluid(3000 * multiplier))
                .inputFluids(material[5].getFluid(L * 12 * multiplier))
                .outputItems(robot_arm.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        GTORecipeBuilder builder_emitter = ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id(String.format("emitter_%s", VN[tier].toLowerCase())))
                .inputItems(frameGt, material[7])
                .inputItems(GTOTagPrefix.emitterBases, material[0])
                .inputItems(motor)
                .inputItems(rodLong, material[0], 2)
                .inputItems(emitter_gem.copyWithCount(2))
                .inputItems(circuit, 2)
                .inputItems(foil, material[8], 64)
                .inputItems(foil, material[9], 64)
                .inputItems(cableGtSingle, material[2], 4)
                .inputFluids(material[3].getFluid(L * 2 * multiplier))
                .inputFluids(material[5].getFluid(L * multiplier))
                .outputItems(emitter)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_emitter.stationResearch(b -> b
                    .researchStack((ItemStack) EMITTER.get(tier - 1))
                    .CWUt((int) ((1 << (tier - 3)) * 1.5))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_emitter.stationResearch(b -> b
                    .researchStack((ItemStack) EMITTER.get(ZPM - 1))
                    .CWUt(1 << (tier - 4))
                    .EUt(VA[tier - 1]));
        } else {
            builder_emitter.scannerResearch(b -> b
                    .researchStack(EMITTER_IV.asStack())
                    .duration(600)
                    .EUt(VA[IV]));
        }
        builder_emitter.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("emitter_%s", VN[tier].toLowerCase())))
                .circuitMeta(6)
                .inputItems(motor.copyWithCount(12))
                .inputItems(emitter_gem.copyWithCount(24))
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asStack(24))
                .inputItems(cableGtSingle, material[2], 48)
                .inputFluids(material[0].getFluid(L * 72))
                .inputFluids(material[3].getFluid(L * 24 * multiplier))
                .inputFluids(material[5].getFluid(L * 12 * multiplier))
                .inputFluids(material[7].getFluid(L * 24))
                .inputFluids(material[8].getFluid(L * 192))
                .inputFluids(material[9].getFluid(L * 192))
                .outputItems(emitter.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        GTORecipeBuilder builder_sensor = ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id(String.format("sensor_%s", VN[tier].toLowerCase())))
                .inputItems(frameGt, material[7])
                .inputItems(GTOTagPrefix.sensorCasing, material[0])
                .inputItems(motor)
                .inputItems(plate, material[0], 2)
                .inputItems(emitter_gem.copyWithCount(2))
                .inputItems(circuit, 2)
                .inputItems(foil, material[8], 64)
                .inputItems(foil, material[9], 64)
                .inputItems(cableGtSingle, material[2], 4)
                .inputFluids(material[3].getFluid(L * 2 * multiplier))
                .inputFluids(material[5].getFluid(L * multiplier))
                .outputItems(sensor)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_sensor.stationResearch(b -> b
                    .researchStack((ItemStack) SENSOR.get(tier - 1))
                    .CWUt((int) ((1 << (tier - 3)) * 1.5))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_sensor.stationResearch(b -> b
                    .researchStack((ItemStack) SENSOR.get(ZPM - 1))
                    .CWUt(1 << (tier - 4))
                    .EUt(VA[tier - 1]));
        } else {
            builder_sensor.scannerResearch(b -> b
                    .researchStack(SENSOR_IV.asStack())
                    .duration(600)
                    .EUt(VA[IV]));
        }
        builder_sensor.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("sensor_%s", VN[tier].toLowerCase())))
                .circuitMeta(7)
                .inputItems(motor.copyWithCount(12))
                .inputItems(emitter_gem.copyWithCount(24))
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asStack(24))
                .inputItems(cableGtSingle, material[2], 48)
                .inputFluids(material[0].getFluid(L * 78))
                .inputFluids(material[3].getFluid(L * 24 * multiplier))
                .inputFluids(material[5].getFluid(L * 12 * multiplier))
                .inputFluids(material[7].getFluid(L * 24))
                .inputFluids(material[8].getFluid(L * 192))
                .inputFluids(material[9].getFluid(L * 192))
                .outputItems(sensor.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();

        GTORecipeBuilder builder_field_generator = ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id(String.format("field_generator_%s", VN[tier].toLowerCase())))
                .inputItems(frameGt, material[7])
                .inputItems(GTOTagPrefix.fieldGeneratorCasing, material[0])
                .inputItems(emitter_gem)
                .inputItems(emitter.copyWithCount(2))
                .inputItems(circuit, 2)
                .inputItems(wireFine, material[10], 64)
                .inputItems(wireFine, material[10], 64)
                .inputItems(cableGtSingle, material[2], 4)
                .inputFluids(material[3].getFluid(L * 2 * multiplier))
                .inputFluids(material[5].getFluid(L * multiplier))
                .outputItems(field_generator)
                .duration(600).EUt(VA[tier]);

        if (tier > ZPM) {
            builder_field_generator.stationResearch(b -> b
                    .researchStack((ItemStack) FIELD_GENERATOR.get(tier - 1))
                    .CWUt((int) ((1 << (tier - 3)) * 1.5))
                    .EUt(VA[tier - 1]));
        } else if (tier == ZPM) {
            builder_field_generator.stationResearch(b -> b
                    .researchStack((ItemStack) FIELD_GENERATOR.get(ZPM - 1))
                    .CWUt(1 << (tier - 4))
                    .EUt(VA[tier - 1]));
        } else {
            builder_field_generator.scannerResearch(b -> b
                    .researchStack(FIELD_GENERATOR_IV.asStack())
                    .duration(600)
                    .EUt(VA[IV]));
        }
        builder_field_generator.save();

        COMPONENT_ASSEMBLY_RECIPES.recipeBuilder(GTOCore.id(String.format("field_generator_%s", VN[tier].toLowerCase())))
                .circuitMeta(8)
                .inputItems(emitter.copyWithCount(24))
                .inputItems(emitter_gem.copyWithCount(24))
                .inputItems(GTOItems.UNIVERSAL_CIRCUIT[tier].asStack(24))
                .inputItems(cableGtSingle, material[2], 48)
                .inputFluids(material[0].getFluid(L * 96))
                .inputFluids(material[3].getFluid(L * 24 * multiplier))
                .inputFluids(material[5].getFluid(L * 12 * multiplier))
                .inputFluids(material[7].getFluid(L * 24))
                .inputFluids(material[10].getFluid(L * 384))
                .outputItems(field_generator.copyWithCount(16))
                .addData(COMPONENT_ASSEMBLY_CASING_TIER, tier)
                .duration(2400).EUt(VA[tier]).save();
    }
}
