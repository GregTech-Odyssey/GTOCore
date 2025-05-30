package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidPipeProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.IMaterialProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ItemPipeProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.pipelike.duct.DuctPipeType;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.ULV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.NO_SMASHING;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.EXTRUDER_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.PACKER_RECIPES;

public final class GTOPipeRecipeHandler {

    private static void addDuctRecipes(Consumer<FinishedRecipe> provider, Material material, int outputAmount) {
        VanillaRecipeHelper.addShapedRecipe(provider, "small_duct_%s".formatted(material.getName()),
                GTBlocks.DUCT_PIPES[DuctPipeType.SMALL.ordinal()].asStack(outputAmount << 1), "w", "X", "h",
                'X', new UnificationEntry(plate, material));
        VanillaRecipeHelper.addShapedRecipe(provider, "medium_duct_%s".formatted(material.getName()),
                GTBlocks.DUCT_PIPES[DuctPipeType.NORMAL.ordinal()].asStack(outputAmount), " X ", "wXh", " X ",
                'X', new UnificationEntry(plate, material));
        VanillaRecipeHelper.addShapedRecipe(provider, "large_duct_%s".formatted(material.getName()),
                GTBlocks.DUCT_PIPES[DuctPipeType.LARGE.ordinal()].asStack(outputAmount), "XwX", "X X", "XhX",
                'X', new UnificationEntry(plate, material));
        VanillaRecipeHelper.addShapedRecipe(provider, "huge_duct_%s".formatted(material.getName()),
                GTBlocks.DUCT_PIPES[DuctPipeType.HUGE.ordinal()].asStack(outputAmount), "XwX", "X X", "XhX",
                'X', new UnificationEntry(plateDouble, material));
    }

    private static void processPipeQuadruple(TagPrefix pipePrefix, Material material, FluidPipeProperties property,
                                             Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        ItemStack smallPipe = ChemicalHelper.get(pipeSmallFluid, material);
        ItemStack quadPipe = ChemicalHelper.get(pipePrefix, material);
        VanillaRecipeHelper.addShapedRecipe(provider, String.format("quadruple_%s_pipe", material.getName()),
                quadPipe, "XX", "XX",
                'X', smallPipe);

        PACKER_RECIPES.recipeBuilder("package_" + material.getName() + "_quadruple_pipe")
                .inputItems(GTUtil.copyAmount(4, smallPipe))
                .circuitMeta(4)
                .outputItems(quadPipe)
                .duration(30)
                .EUt(VA[ULV])
                .save(provider);
    }

    private static void processPipeNonuple(TagPrefix pipePrefix, Material material, FluidPipeProperties property,
                                           Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        ItemStack smallPipe = ChemicalHelper.get(pipeSmallFluid, material);
        ItemStack nonuplePipe = ChemicalHelper.get(pipePrefix, material);
        VanillaRecipeHelper.addShapedRecipe(provider, String.format("nonuple_%s_pipe", material.getName()),
                nonuplePipe, "XXX", "XXX", "XXX",
                'X', smallPipe);

        PACKER_RECIPES.recipeBuilder("package_" + material.getName() + "_nonuple_pipe")
                .inputItems(GTUtil.copyAmount(9, smallPipe))
                .circuitMeta(9)
                .outputItems(nonuplePipe)
                .duration(40)
                .EUt(VA[ULV])
                .save(provider);
    }

    public static void init(Consumer<FinishedRecipe> provider) {
        pipeTinyFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, GTOPipeRecipeHandler::rocessPipeTiny);
        pipeSmallFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, GTOPipeRecipeHandler::rocessPipeSmall);
        pipeNormalFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, GTOPipeRecipeHandler::rocessPipeNormal);
        pipeLargeFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, GTOPipeRecipeHandler::rocessPipeLarge);
        pipeHugeFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, GTOPipeRecipeHandler::rocessPipeHuge);

        pipeQuadrupleFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, GTOPipeRecipeHandler::processPipeQuadruple);
        pipeNonupleFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, GTOPipeRecipeHandler::processPipeNonuple);

        pipeSmallItem.executeHandler(provider, PropertyKey.ITEM_PIPE, GTOPipeRecipeHandler::rocessPipeSmall);
        pipeNormalItem.executeHandler(provider, PropertyKey.ITEM_PIPE, GTOPipeRecipeHandler::rocessPipeNormal);
        pipeLargeItem.executeHandler(provider, PropertyKey.ITEM_PIPE, GTOPipeRecipeHandler::rocessPipeLarge);
        pipeHugeItem.executeHandler(provider, PropertyKey.ITEM_PIPE, GTOPipeRecipeHandler::rocessPipeHuge);

        pipeSmallRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, GTOPipeRecipeHandler::rocessRestrictivePipe);
        pipeNormalRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, GTOPipeRecipeHandler::rocessRestrictivePipe);
        pipeLargeRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, GTOPipeRecipeHandler::rocessRestrictivePipe);
        pipeHugeRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, GTOPipeRecipeHandler::rocessRestrictivePipe);

        addDuctRecipes(provider, Steel, 2);
        addDuctRecipes(provider, StainlessSteel, 4);
        addDuctRecipes(provider, TungstenSteel, 8);
    }

    private static void rocessRestrictivePipe(TagPrefix pipePrefix, Material material, ItemPipeProperties property,
                                              Consumer<FinishedRecipe> provider) {
        TagPrefix unrestrictive;
        if (pipePrefix == pipeSmallRestrictive) unrestrictive = pipeSmallItem;
        else if (pipePrefix == pipeNormalRestrictive) unrestrictive = pipeNormalItem;
        else if (pipePrefix == pipeLargeRestrictive) unrestrictive = pipeLargeItem;
        else if (pipePrefix == pipeHugeRestrictive) unrestrictive = pipeHugeItem;
        else return;

        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_" + pipePrefix.name)
                .inputItems(unrestrictive, material)
                .inputItems(ring, Iron, 2)
                .outputItems(pipePrefix, material)
                .duration(20)
                .EUt(VA[ULV])
                .save(provider);

        VanillaRecipeHelper.addShapedRecipe(provider,
                FormattingUtil.toLowerCaseUnder(pipePrefix + "_" + material.getName()),
                ChemicalHelper.get(pipePrefix, material), "PR", "Rh",
                'P', new UnificationEntry(unrestrictive, material), 'R', ChemicalHelper.get(ring, Iron));
    }

    private static void rocessPipeTiny(TagPrefix pipePrefix, Material material, IMaterialProperty property,
                                       Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_tiny_pipe")
                .inputItems(ingot, material, 1)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_TINY)
                .outputItems(GTUtil.copyAmount(2, pipeStack))
                .duration(mass)
                .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_tiny_pipe_dust")
                    .inputItems(dust, material, 1)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_TINY)
                    .outputItems(GTUtil.copyAmount(2, pipeStack))
                    .duration(mass)
                    .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("tiny_%s_pipe", material.getName()),
                    GTUtil.copyAmount(2, pipeStack), " s ", "hXw",
                    'X', new UnificationEntry(GTOTagPrefix.curvedPlate, material));
        }
    }

    private static void rocessPipeSmall(TagPrefix pipePrefix, Material material, IMaterialProperty property,
                                        Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_small_pipe")
                .inputItems(ingot, material, 1)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_SMALL)
                .outputItems(pipeStack)
                .duration(mass)
                .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_small_pipe_dust")
                    .inputItems(dust, material, 1)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_SMALL)
                    .outputItems(pipeStack)
                    .duration(mass)
                    .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("small_%s_pipe", material.getName()),
                    pipeStack, "wXh",
                    'X', new UnificationEntry(GTOTagPrefix.curvedPlate, material));
        }
    }

    private static void rocessPipeNormal(TagPrefix pipePrefix, Material material, IMaterialProperty property,
                                         Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_pipe")
                .inputItems(ingot, material, 3)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_NORMAL)
                .outputItems(pipeStack)
                .duration(mass * 3)
                .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_pipe_dust")
                    .inputItems(dust, material, 3)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_NORMAL)
                    .outputItems(pipeStack)
                    .duration(mass * 3)
                    .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("medium_%s_pipe", material.getName()),
                    pipeStack, "XXX", "w h",
                    'X', new UnificationEntry(GTOTagPrefix.curvedPlate, material));
        }
    }

    private static void rocessPipeLarge(TagPrefix pipePrefix, Material material, IMaterialProperty property,
                                        Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_large_pipe")
                .inputItems(ingot, material, 6)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_LARGE)
                .outputItems(pipeStack)
                .duration(mass * 6)
                .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_large_pipe_dust")
                    .inputItems(dust, material, 6)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_LARGE)
                    .outputItems(pipeStack)
                    .duration(mass * 6)
                    .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("large_%s_pipe", material.getName()),
                    pipeStack, "XXX", "w h", "XXX",
                    'X', new UnificationEntry(GTOTagPrefix.curvedPlate, material));
        }
    }

    private static void rocessPipeHuge(TagPrefix pipePrefix, Material material, IMaterialProperty property,
                                       Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_huge_pipe")
                .inputItems(ingot, material, 12)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_HUGE)
                .outputItems(pipeStack)
                .duration(mass * 24)
                .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_huge_pipe_dust")
                    .inputItems(dust, material, 12)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_HUGE)
                    .outputItems(pipeStack)
                    .duration(mass * 24)
                    .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600 && plateDouble.doGenerateItem(material)) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("huge_%s_pipe", material.getName()),
                    pipeStack, "XXX", "w h", "XXX",
                    'X', new UnificationEntry(plateDouble, material));
        }
    }
}
