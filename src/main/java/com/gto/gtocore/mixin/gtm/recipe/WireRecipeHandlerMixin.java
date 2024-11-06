package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.WireRecipeHandler;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.FinishedRecipe;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

@Mixin(WireRecipeHandler.class)
public class WireRecipeHandlerMixin {

    @Shadow(remap = false)
    @Final
    private static Map<TagPrefix, Integer> INSULATION_AMOUNT;

    @Shadow(remap = false)
    private static int getVoltageMultiplier(Material material) {
        return 0;
    }

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
        wireGtSingle.executeHandler(provider, PropertyKey.WIRE, WireRecipeHandlerMixin::gTLCore$processWires);

        wireGtSingle.executeHandler(provider, PropertyKey.WIRE, WireRecipeHandlerMixin::gTLCore$generateCableCovering);
        wireGtDouble.executeHandler(provider, PropertyKey.WIRE, WireRecipeHandlerMixin::gTLCore$generateCableCovering);
        wireGtQuadruple.executeHandler(provider, PropertyKey.WIRE, WireRecipeHandlerMixin::gTLCore$generateCableCovering);
        wireGtOctal.executeHandler(provider, PropertyKey.WIRE, WireRecipeHandlerMixin::gTLCore$generateCableCovering);
        wireGtHex.executeHandler(provider, PropertyKey.WIRE, WireRecipeHandlerMixin::gTLCore$generateCableCovering);
    }

    @Unique
    private static void gTLCore$processWires(TagPrefix wirePrefix, Material material, WireProperties property,
                                             Consumer<FinishedRecipe> provider) {
        TagPrefix prefix = material.hasProperty(PropertyKey.INGOT) ? ingot :
                material.hasProperty(PropertyKey.GEM) ? gem : dust;
        int mass = (int) material.getMass();
        GTRecipeTypes.WIREMILL_RECIPES.recipeBuilder(GTOCore.id("mill_" + material.getName() + "_wire"))
                .inputItems(prefix, material)
                .outputItems(wireGtSingle, material, 2)
                .duration(mass)
                .EUt(getVoltageMultiplier(material))
                .save(provider);

        if (!material.hasFlag(MaterialFlags.NO_WORKING) && material.hasFlag(MaterialFlags.GENERATE_PLATE) && mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("%s_wire_single", material.getName()),
                    ChemicalHelper.get(wireGtSingle, material), "Xx",
                    'X', new UnificationEntry(plate, material));
        }
    }

    @Unique
    private static void gTLCore$generateCableCovering(TagPrefix wirePrefix, Material material, WireProperties property, Consumer<FinishedRecipe> provider) {
        if (property.isSuperconductor()) return;

        TagPrefix cablePrefix = TagPrefix.get("cable" + wirePrefix.name().substring(4));
        int voltageTier = GTUtil.getTierByVoltage(property.getVoltage());
        int insulationAmount = INSULATION_AMOUNT.get(cablePrefix);

        if (voltageTier <= LV) {
            gTLCore$generateManualRecipe(wirePrefix, material, cablePrefix, provider);
        }

        if (voltageTier < IV) {
            GTRecipeBuilder builder = GTORecipeTypes.LAMINATOR_RECIPES
                    .recipeBuilder(GTOCore.id("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase() + "_rubber"))
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wirePrefix, material)
                    .outputItems(cablePrefix, material)
                    .inputFluids(Rubber.getFluid(L * (long) insulationAmount));

            if (voltageTier == EV) {
                builder.inputItems(foil, PolyvinylChloride, insulationAmount);
            }
            builder.save(provider);
        } else if (voltageTier < UHV) {
            GTRecipeBuilder builder = GTORecipeTypes.LAMINATOR_RECIPES
                    .recipeBuilder(GTOCore.id("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase() + "_silicone"))
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wirePrefix, material)
                    .outputItems(cablePrefix, material);

            if (voltageTier >= LuV) {
                builder.inputItems(foil, PolyphenyleneSulfide, insulationAmount);
            }

            builder.inputItems(foil, PolyvinylChloride, insulationAmount);

            builder.inputFluids(SiliconeRubber.getFluid(L * (long) insulationAmount))
                    .save(provider);
        } else {
            GTRecipeBuilder builder = GTORecipeTypes.LAMINATOR_RECIPES
                    .recipeBuilder(GTOCore.id("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase() + "_styrene_butadiene"))
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wirePrefix, material)
                    .outputItems(cablePrefix, material);

            if (voltageTier > UEV) {
                builder.inputItems(GTOItems.HIGHLY_INSULATING_FOIL.asStack(insulationAmount));
            }

            builder.inputItems(foil, PolyphenyleneSulfide, insulationAmount);

            builder.inputFluids(StyreneButadieneRubber.getFluid(L * (long) insulationAmount))
                    .save(provider);
        }
    }

    @Unique
    private static void gTLCore$generateManualRecipe(TagPrefix wirePrefix, Material material, TagPrefix cablePrefix, Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase()))
                .inputItems(wirePrefix, material)
                .inputItems(plate, Rubber, INSULATION_AMOUNT.get(cablePrefix))
                .outputItems(cablePrefix, material)
                .duration(100).EUt(VA[ULV])
                .save(provider);
    }
}
