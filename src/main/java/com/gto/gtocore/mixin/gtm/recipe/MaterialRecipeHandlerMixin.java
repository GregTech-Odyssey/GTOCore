package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.*;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.MaterialRecipeHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

@Mixin(MaterialRecipeHandler.class)
public abstract class MaterialRecipeHandlerMixin {

    @Shadow(remap = false)
    private static int getVoltageMultiplier(Material material) {
        return 0;
    }

    @Shadow(remap = false)
    @Final
    private static List<TagPrefix> GEM_ORDER;

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
        ingot.executeHandler(provider, PropertyKey.INGOT, MaterialRecipeHandlerMixin::gTLCore$processIngot);
        nugget.executeHandler(provider, PropertyKey.DUST, MaterialRecipeHandler::processNugget);

        block.executeHandler(provider, PropertyKey.DUST, MaterialRecipeHandlerMixin::gTLCore$processBlock);
        frameGt.executeHandler(provider, PropertyKey.DUST, MaterialRecipeHandler::processFrame);

        dust.executeHandler(provider, PropertyKey.DUST, MaterialRecipeHandlerMixin::gTLCore$processDust);
        dustSmall.executeHandler(provider, PropertyKey.DUST, MaterialRecipeHandlerMixin::gTLCore$processSmallDust);
        dustTiny.executeHandler(provider, PropertyKey.DUST, MaterialRecipeHandlerMixin::gTLCore$processTinyDust);

        for (TagPrefix orePrefix : Arrays.asList(gem, gemFlawless, gemExquisite)) {
            orePrefix.executeHandler(provider, PropertyKey.GEM, MaterialRecipeHandlerMixin::gTLCore$processGemConversion);
        }
    }

    @Unique
    private static void gTLCore$processIngot(TagPrefix ingotPrefix, Material material, IngotProperty property,
                                             Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        if (material.hasFlag(MORTAR_GRINDABLE)) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("mortar_grind_%s", material.getName()),
                    ChemicalHelper.get(dust, material), "X", "m", 'X', new UnificationEntry(ingotPrefix, material));
        }

        if (material.hasFlag(GENERATE_ROD)) {
            if (mass < 240 && material.getBlastTemperature() < 3600)
                VanillaRecipeHelper.addShapedRecipe(provider, String.format("stick_%s", material.getName()),
                        ChemicalHelper.get(rod, material),
                        "f ", " X",
                        'X', new UnificationEntry(ingotPrefix, material));

            if (!material.hasFlag(NO_WORKING)) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_to_rod")
                        .inputItems(ingotPrefix, material)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_ROD)
                        .outputItems(rod, material, 2)
                        .duration(mass * 4)
                        .EUt(6L * getVoltageMultiplier(material))
                        .save(provider);
            }
        }

        if (material.hasFluid()) {
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_to_ingot")
                    .notConsumable(GTItems.SHAPE_MOLD_INGOT)
                    .inputFluids(material.getFluid(L))
                    .outputItems(ingotPrefix, material)
                    .duration(mass).EUt(VA[ULV])
                    .save(provider);
        }

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_to_ingot")
                    .inputItems(dust, material)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_INGOT)
                    .outputItems(ingot, material)
                    .duration(mass)
                    .EUt(4L * getVoltageMultiplier(material))
                    .save(provider);
        }

        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_smelt_" + material.getName() + "_to_nugget")
                .EUt(VA[ULV]).duration(mass)
                .inputItems(ingot, material)
                .notConsumable(GTItems.SHAPE_MOLD_NUGGET)
                .outputItems(nugget, material, 9)
                .save(provider);

        if (!ChemicalHelper.get(block, material).isEmpty()) {
            int amount = (int) (block.getMaterialAmount(material) / M);
            ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_smelt_" + material.getName() + "_to_ingot")
                    .EUt(VA[ULV]).duration(mass * amount)
                    .inputItems(block, material)
                    .notConsumable(GTItems.SHAPE_MOLD_INGOT)
                    .outputItems(ingot, material, amount)
                    .save(provider);

            COMPRESSOR_RECIPES.recipeBuilder("compress_" + material.getName() + "_to_block")
                    .EUt(2).duration(mass * amount)
                    .inputItems(ingot, material, amount)
                    .outputItems(block, material)
                    .save(provider);
        }

        if (material.hasFlag(GENERATE_PLATE) && !material.hasFlag(NO_WORKING)) {

            if (!material.hasFlag(NO_SMASHING)) {
                ItemStack plateStack = ChemicalHelper.get(plate, material);
                if (!plateStack.isEmpty()) {
                    GTORecipeTypes.ROLLING_RECIPES.recipeBuilder("rolling_" + material.getName() + "_to_plate")
                            .inputItems(ingotPrefix, material)
                            .outputItems(plateStack)
                            .EUt(24).duration(mass)
                            .circuitMeta(1)
                            .save(provider);

                    FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + material.getName() + "_to_plate")
                            .inputItems(ingotPrefix, material, 3)
                            .outputItems(GTUtil.copyAmount(2, plateStack))
                            .EUt(16).duration(mass)
                            .save(provider);
                    if (mass < 240 && material.getBlastTemperature() < 3600)
                        VanillaRecipeHelper.addShapedRecipe(provider, String.format("plate_%s", material.getName()),
                                plateStack, "h", "I", "I", 'I', new UnificationEntry(ingotPrefix, material));
                }
            }

            int voltageMultiplier = getVoltageMultiplier(material);
            if (!ChemicalHelper.get(plate, material).isEmpty()) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_to_plate")
                        .inputItems(ingotPrefix, material)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_PLATE)
                        .outputItems(plate, material)
                        .duration(mass * 2)
                        .EUt(8L * voltageMultiplier)
                        .save(provider);

                if (material.hasFlag(NO_SMASHING)) {
                    EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_plate")
                            .inputItems(dust, material)
                            .notConsumable(GTItems.SHAPE_EXTRUDER_PLATE)
                            .outputItems(plate, material)
                            .duration(mass * 2)
                            .EUt(8L * voltageMultiplier)
                            .save(provider);
                }
            }
        }
    }

    @Unique
    private static void gTLCore$processBlock(TagPrefix blockPrefix, Material material, DustProperty property,
                                             Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack blockStack = ChemicalHelper.get(blockPrefix, material);
        long materialAmount = blockPrefix.getMaterialAmount(material);
        if (material.hasFluid()) {
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_block")
                    .notConsumable(GTItems.SHAPE_MOLD_BLOCK)
                    .inputFluids(material.getFluid((int) (materialAmount * L / M)))
                    .outputItems(blockStack)
                    .duration(mass).EUt(VA[ULV])
                    .save(provider);
        }

        if (material.hasFlag(NO_SMASHING) && material.hasFlag(GENERATE_PLATE)) {
            ItemStack plateStack = ChemicalHelper.get(plate, material);
            if (!plateStack.isEmpty()) {
                CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_block_to_plate")
                        .inputItems(blockPrefix, material)
                        .outputItems(GTUtil.copyAmount((int) (materialAmount / M), plateStack))
                        .duration(mass * 8).EUt(VA[LV])
                        .save(provider);
            }
        }

        if (!material.hasFlag(EXCLUDE_BLOCK_CRAFTING_RECIPES)) {
            if (material.hasProperty(PropertyKey.INGOT)) {
                int voltageMultiplier = getVoltageMultiplier(material);
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_block")
                        .inputItems(ingot, material, (int) (materialAmount / M))
                        .notConsumable(GTItems.SHAPE_EXTRUDER_BLOCK)
                        .outputItems(blockStack)
                        .duration(mass * 2).EUt(8L * voltageMultiplier)
                        .save(provider);

            } else if (material.hasProperty(PropertyKey.GEM)) {
                COMPRESSOR_RECIPES.recipeBuilder("compress_" + material.getName() + "_gem_to_block")
                        .inputItems(gem, material, (int) (block.getMaterialAmount(material) / M))
                        .outputItems(block, material)
                        .duration(300).EUt(2).save(provider);

                FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + material.getName() + "_block_to_gem")
                        .inputItems(block, material)
                        .outputItems(gem, material, (int) (block.getMaterialAmount(material) / M))
                        .duration(100).EUt(24).save(provider);
            }
        }
    }

    @Unique
    private static void gTLCore$processGemConversion(TagPrefix gemPrefix, Material material, GemProperty property,
                                                     Consumer<FinishedRecipe> provider) {
        if (material.hasFlag(MORTAR_GRINDABLE)) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("gem_to_dust_%s_%s", material.getName(), FormattingUtil.toLowerCaseUnder(gemPrefix.name)), ChemicalHelper.getDust(material, gemPrefix.getMaterialAmount(material)), "X", "m", 'X', new UnificationEntry(gemPrefix, material));
        }

        TagPrefix prevPrefix = GTUtil.getItem(GEM_ORDER, GEM_ORDER.indexOf(gemPrefix) - 1, null);
        ItemStack prevStack = prevPrefix == null ? ItemStack.EMPTY : ChemicalHelper.get(prevPrefix, material, 2);
        if (!prevStack.isEmpty() && prevPrefix != null) {
            CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_" + FormattingUtil.toLowerCaseUnder(gemPrefix.name) + "_to_" + FormattingUtil.toLowerCaseUnder(prevPrefix.name))
                    .inputItems(gemPrefix, material)
                    .outputItems(prevStack)
                    .duration(20)
                    .EUt(16)
                    .save(provider);

            LASER_ENGRAVER_RECIPES.recipeBuilder("engrave_" + material.getName() + "_" + FormattingUtil.toLowerCaseUnder(gemPrefix.name) + "_to_" + FormattingUtil.toLowerCaseUnder(prevPrefix.name))
                    .inputItems(prevStack)
                    .notConsumable(lens, MarkerMaterials.Color.White)
                    .inputFluids(DistilledWater.getFluid(10))
                    .outputItems(gemPrefix, material)
                    .duration(300)
                    .EUt(240)
                    .save(provider);
        }
    }

    @Unique
    private static void gTLCore$processDust(TagPrefix dustPrefix, Material mat, DustProperty property,
                                            Consumer<FinishedRecipe> provider) {
        String id = "%s_%s_".formatted(FormattingUtil.toLowerCaseUnder(dustPrefix.name),
                mat.getName().toLowerCase(Locale.ROOT));
        ItemStack dustStack = ChemicalHelper.get(dustPrefix, mat);
        OreProperty oreProperty = mat.hasProperty(PropertyKey.ORE) ? mat.getProperty(PropertyKey.ORE) : null;
        if (mat.hasProperty(PropertyKey.GEM)) {
            ItemStack gemStack = ChemicalHelper.get(gem, mat);

            if (mat.hasFlag(CRYSTALLIZABLE)) {
                AUTOCLAVE_RECIPES.recipeBuilder("autoclave_" + id + "_water")
                        .inputItems(dustStack)
                        .inputFluids(GTMaterials.Water.getFluid(250))
                        .chancedOutput(gemStack, 7000, 1000)
                        .duration(1200).EUt(24)
                        .save(provider);

                AUTOCLAVE_RECIPES.recipeBuilder("autoclave_" + id + "_distilled")
                        .inputItems(dustStack)
                        .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                        .outputItems(gemStack)
                        .duration(600).EUt(24)
                        .save(provider);
            }

            if (!mat.hasFlag(EXPLOSIVE) && !mat.hasFlag(FLAMMABLE)) {
                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_powderbarrel")
                        .inputItems(GTUtil.copyAmount(4, dustStack))
                        .outputItems(GTUtil.copyAmount(3, gemStack))
                        .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                        .explosivesType(new ItemStack(GTBlocks.POWDERBARREL, 8))
                        .save(provider);

                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_tnt")
                        .inputItems(GTUtil.copyAmount(4, dustStack))
                        .outputItems(GTUtil.copyAmount(3, gemStack))
                        .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                        .explosivesAmount(4)
                        .save(provider);

                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_dynamite")
                        .inputItems(GTUtil.copyAmount(4, dustStack))
                        .outputItems(GTUtil.copyAmount(3, gemStack))
                        .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                        .explosivesType(GTItems.DYNAMITE.asStack(2))
                        .save(provider);

                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_itnt")
                        .inputItems(GTUtil.copyAmount(4, dustStack))
                        .outputItems(GTUtil.copyAmount(3, gemStack))
                        .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                        .explosivesType(new ItemStack(GTBlocks.INDUSTRIAL_TNT))
                        .save(provider);

                GTORecipeTypes.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("electric_implode_" + id)
                        .inputItems(GTUtil.copyAmount(4, dustStack))
                        .outputItems(GTUtil.copyAmount(3, gemStack))
                        .save(provider);
            }

            if (oreProperty != null) {
                Material smeltingResult = oreProperty.getDirectSmeltResult();
                if (smeltingResult != null) {
                    VanillaRecipeHelper.addSmeltingRecipe(provider, id + "_ingot",
                            ChemicalHelper.getTag(dustPrefix, mat), ChemicalHelper.get(ingot, smeltingResult));
                }
            }

        } else if (mat.hasProperty(PropertyKey.INGOT)) {
            if (!mat.hasAnyOfFlags(FLAMMABLE, NO_SMELTING)) {

                boolean hasHotIngot = ingotHot.doGenerateItem(mat);
                ItemStack ingotStack = ChemicalHelper.get(hasHotIngot ? ingotHot : ingot, mat);
                if (ingotStack.isEmpty() && oreProperty != null) {
                    Material smeltingResult = oreProperty.getDirectSmeltResult();
                    if (smeltingResult != null) {
                        ingotStack = ChemicalHelper.get(ingot, smeltingResult);
                    }
                }
                int blastTemp = mat.getBlastTemperature();

                if (blastTemp <= 0) {
                    // smelting magnetic dusts is handled elsewhere
                    if (!mat.hasFlag(IS_MAGNETIC)) {
                        // do not register inputs by ore dict here. Let other mods register their own dust -> ingots
                        VanillaRecipeHelper.addSmeltingRecipe(provider, id + "_demagnetize_from_dust",
                                ChemicalHelper.getTag(dustPrefix, mat), ingotStack);
                    }
                } else {
                    IngotProperty ingotProperty = mat.getProperty(PropertyKey.INGOT);
                    BlastProperty blastProperty = mat.getProperty(PropertyKey.BLAST);

                    gTLCore$processEBFRecipe(mat, blastProperty, ingotStack, provider);

                    if (ingotProperty.getMagneticMaterial() != null) {
                        gTLCore$processEBFRecipe(ingotProperty.getMagneticMaterial(), blastProperty, ingotStack, provider);
                    }
                }
            }
        } else {
            if (mat.hasFlag(GENERATE_PLATE) && !mat.hasFlag(EXCLUDE_PLATE_COMPRESSOR_RECIPE)) {
                COMPRESSOR_RECIPES.recipeBuilder("compress_plate_" + id)
                        .inputItems(dustStack)
                        .outputItems(plate, mat)
                        .save(provider);
            }

            // Some Ores with Direct Smelting Results have neither ingot nor gem properties
            if (oreProperty != null) {
                Material smeltingResult = oreProperty.getDirectSmeltResult();
                if (smeltingResult != null) {
                    ItemStack ingotStack = ChemicalHelper.get(ingot, smeltingResult);
                    if (!ingotStack.isEmpty()) {
                        VanillaRecipeHelper.addSmeltingRecipe(provider, id + "_dust_to_ingot",
                                ChemicalHelper.getTag(dustPrefix, mat), ingotStack);
                    }
                }
            }
        }
    }

    @Unique
    private static void gTLCore$processSmallDust(TagPrefix orePrefix, Material material, DustProperty property,
                                                 Consumer<FinishedRecipe> provider) {
        PACKER_RECIPES.recipeBuilder("package_" + material.getName() + "_small_dust")
                .inputItems(orePrefix, material, 4)
                .outputItems(ChemicalHelper.get(dust, material))
                .save(provider);

        GTORecipeTypes.UNPACKER_RECIPES.recipeBuilder("unpackage_" + material.getName() + "_small_dust")
                .inputItems(dust, material)
                .circuitMeta(1)
                .outputItems(GTUtil.copyAmount(4, ChemicalHelper.get(orePrefix, material)))
                .save(provider);
    }

    @Unique
    private static void gTLCore$processTinyDust(TagPrefix orePrefix, Material material, DustProperty property,
                                                Consumer<FinishedRecipe> provider) {
        PACKER_RECIPES.recipeBuilder("package_" + material.getName() + "_tiny_dust")
                .inputItems(orePrefix, material, 9)
                .outputItems(ChemicalHelper.get(dust, material))
                .save(provider);

        GTORecipeTypes.UNPACKER_RECIPES.recipeBuilder("unpackage_" + material.getName() + "_tiny_dust")
                .inputItems(dust, material)
                .circuitMeta(2)
                .outputItems(GTUtil.copyAmount(9, ChemicalHelper.get(orePrefix, material)))
                .save(provider);
    }

    @Unique
    private static void gTLCore$processEBFRecipe(Material material, BlastProperty property, ItemStack output,
                                                 Consumer<FinishedRecipe> provider) {
        int blastTemp = property.getBlastTemperature();
        BlastProperty.GasTier gasTier = property.getGasTier();
        int duration = property.getDurationOverride() / 2;
        if (duration <= 0) {
            duration = Math.max(1, (int) (material.getMass() * blastTemp / 150));
        }
        int EUt = property.getEUtOverride();
        if (EUt <= 0) EUt = VA[MV];

        GTRecipeBuilder blastBuilder = BLAST_RECIPES.recipeBuilder("blast_" + material.getName())
                .inputItems(dust, material)
                .outputItems(output)
                .blastFurnaceTemp(blastTemp)
                .EUt(EUt);

        if (gasTier != null) {
            FluidIngredient gas = CraftingComponent.EBF_GASES.get(gasTier).copy();

            blastBuilder.copy("blast_" + material.getName())
                    .circuitMeta(1)
                    .duration(duration)
                    .save(provider);

            blastBuilder.copy("blast_" + material.getName() + "_gas")
                    .circuitMeta(2)
                    .inputFluids(gas)
                    .duration((int) (duration * 0.6))
                    .save(provider);
        } else {
            blastBuilder.duration(duration);
            if (material == Silicon) {
                blastBuilder.circuitMeta(1);
            }
            blastBuilder.save(provider);
        }

        // Add Vacuum Freezer recipe if required.
        if (ingotHot.doGenerateItem(material)) {
            if (blastTemp < 5000) {
                VACUUM_RECIPES.recipeBuilder("cool_hot_" + material.getName() + "_ingot")
                        .inputItems(ingotHot, material)
                        .outputItems(ingot, material)
                        .duration((int) material.getMass() * 3)
                        .save(provider);
            } else {
                VACUUM_RECIPES.recipeBuilder("cool_hot_" + material.getName() + "_ingot")
                        .inputItems(ingotHot, material)
                        .inputFluids(Helium.getFluid(FluidStorageKeys.LIQUID, 500))
                        .outputItems(ingot, material)
                        .outputFluids(Helium.getFluid(250))
                        .duration((int) material.getMass() * 3)
                        .save(provider);
            }
        }
    }
}
