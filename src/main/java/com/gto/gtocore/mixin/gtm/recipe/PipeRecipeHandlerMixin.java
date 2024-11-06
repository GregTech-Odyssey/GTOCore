package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidPipeProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.IMaterialProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ItemPipeProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.generated.PipeRecipeHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.NO_SMASHING;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

@Mixin(PipeRecipeHandler.class)
public abstract class PipeRecipeHandlerMixin {

    @Shadow(remap = false)
    private static void addDuctRecipes(Consumer<FinishedRecipe> provider, Material material, int outputAmount) {}

    @Shadow(remap = false)
    private static int getVoltageMultiplier(Material material) {
        return 0;
    }

    @Shadow(remap = false)
    private static void processPipeQuadruple(TagPrefix pipePrefix, Material material, FluidPipeProperties property, Consumer<FinishedRecipe> provider) {}

    @Shadow(remap = false)
    private static void processPipeNonuple(TagPrefix pipePrefix, Material material, FluidPipeProperties property, Consumer<FinishedRecipe> provider) {}

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
        pipeTinyFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandlerMixin::gTLCore$processPipeTiny);
        pipeSmallFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandlerMixin::gTLCore$processPipeSmall);
        pipeNormalFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandlerMixin::gTLCore$processPipeNormal);
        pipeLargeFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandlerMixin::gTLCore$processPipeLarge);
        pipeHugeFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandlerMixin::gTLCore$processPipeHuge);

        pipeQuadrupleFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandlerMixin::processPipeQuadruple);
        pipeNonupleFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandlerMixin::processPipeNonuple);

        pipeSmallItem.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandlerMixin::gTLCore$processPipeSmall);
        pipeNormalItem.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandlerMixin::gTLCore$processPipeNormal);
        pipeLargeItem.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandlerMixin::gTLCore$processPipeLarge);
        pipeHugeItem.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandlerMixin::gTLCore$processPipeHuge);

        pipeSmallRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandlerMixin::gTLCore$processRestrictivePipe);
        pipeNormalRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandlerMixin::gTLCore$processRestrictivePipe);
        pipeLargeRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandlerMixin::gTLCore$processRestrictivePipe);
        pipeHugeRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandlerMixin::gTLCore$processRestrictivePipe);

        addDuctRecipes(provider, Steel, 2);
        addDuctRecipes(provider, StainlessSteel, 4);
        addDuctRecipes(provider, TungstenSteel, 8);
    }

    @Unique
    private static void gTLCore$processRestrictivePipe(TagPrefix pipePrefix, Material material, ItemPipeProperties property,
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

    @Unique
    private static void gTLCore$processPipeTiny(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                                Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_tiny_pipe")
                .inputItems(ingot, material, 1)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_TINY)
                .outputItems(GTUtil.copyAmount(2, pipeStack))
                .duration(mass)
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_tiny_pipe_dust")
                    .inputItems(dust, material, 1)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_TINY)
                    .outputItems(GTUtil.copyAmount(2, pipeStack))
                    .duration(mass)
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("tiny_%s_pipe", material.getName()),
                    GTUtil.copyAmount(2, pipeStack), " s ", "hXw",
                    'X', new UnificationEntry(GTOTagPrefix.curvedPlate, material));
        }
    }

    @Unique
    private static void gTLCore$processPipeSmall(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                                 Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_small_pipe")
                .inputItems(ingot, material, 1)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_SMALL)
                .outputItems(pipeStack)
                .duration(mass)
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_small_pipe_dust")
                    .inputItems(dust, material, 1)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_SMALL)
                    .outputItems(pipeStack)
                    .duration(mass)
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("small_%s_pipe", material.getName()),
                    pipeStack, "wXh",
                    'X', new UnificationEntry(GTOTagPrefix.curvedPlate, material));
        }
    }

    @Unique
    private static void gTLCore$processPipeNormal(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                                  Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_pipe")
                .inputItems(ingot, material, 3)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_NORMAL)
                .outputItems(pipeStack)
                .duration(mass * 3)
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_pipe_dust")
                    .inputItems(dust, material, 3)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_NORMAL)
                    .outputItems(pipeStack)
                    .duration(mass * 3)
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("medium_%s_pipe", material.getName()),
                    pipeStack, "XXX", "w h",
                    'X', new UnificationEntry(GTOTagPrefix.curvedPlate, material));
        }
    }

    @Unique
    private static void gTLCore$processPipeLarge(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                                 Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_large_pipe")
                .inputItems(ingot, material, 6)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_LARGE)
                .outputItems(pipeStack)
                .duration(mass * 6)
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_large_pipe_dust")
                    .inputItems(dust, material, 6)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_LARGE)
                    .outputItems(pipeStack)
                    .duration(mass * 6)
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("large_%s_pipe", material.getName()),
                    pipeStack, "XXX", "w h", "XXX",
                    'X', new UnificationEntry(GTOTagPrefix.curvedPlate, material));
        }
    }

    @Unique
    private static void gTLCore$processPipeHuge(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                                Consumer<FinishedRecipe> provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        int mass = (int) material.getMass();
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_huge_pipe")
                .inputItems(ingot, material, 12)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_HUGE)
                .outputItems(pipeStack)
                .duration(mass * 24)
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_huge_pipe_dust")
                    .inputItems(dust, material, 12)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_HUGE)
                    .outputItems(pipeStack)
                    .duration(mass * 24)
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else if (mass < 240 && material.getBlastTemperature() < 3600 && plateDouble.doGenerateItem(material)) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("huge_%s_pipe", material.getName()),
                    pipeStack, "XXX", "w h", "XXX",
                    'X', new UnificationEntry(plateDouble, material));
        }
    }
}
