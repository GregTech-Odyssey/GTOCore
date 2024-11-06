package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.generated.WireCombiningHandler;
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

@Mixin(WireCombiningHandler.class)
public class WireCombiningHandlerMixin {

    @Shadow(remap = false)
    @Final
    private static Map<TagPrefix, TagPrefix> cableToWireMap;

    @Shadow(remap = false)
    @Final
    private static TagPrefix[] WIRE_DOUBLING_ORDER;

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
        TagPrefix.wireGtSingle.executeHandler(provider, PropertyKey.WIRE, WireCombiningHandlerMixin::gTLCore$processWireCompression);

        for (TagPrefix cablePrefix : cableToWireMap.keySet()) {
            cablePrefix.executeHandler(provider, PropertyKey.WIRE, WireCombiningHandlerMixin::gTLCore$processCableStripping);
        }
    }

    @Unique
    private static void gTLCore$processWireCompression(TagPrefix prefix, Material material, WireProperties property,
                                                       Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        for (int startTier = 0; startTier < 4; startTier++) {
            for (int i = 1; i < 5 - startTier; i++) {
                GTORecipeTypes.LOOM_RECIPES.recipeBuilder(GTOCore.id("loom_" + material.getName() + "_wires_" + i + "_" + startTier))
                        .inputItems(WIRE_DOUBLING_ORDER[startTier], material, 1 << i)
                        .circuitMeta((int) Math.pow(2, i))
                        .outputItems(WIRE_DOUBLING_ORDER[startTier + i], material, 1)
                        .EUt(7)
                        .duration(mass * i)
                        .save(provider);
            }
        }

        for (int i = 1; i < 5; i++) {
            GTORecipeTypes.UNPACKER_RECIPES.recipeBuilder(GTOCore.id("pack_" + material.getName() + "_wires_" + i + "_single"))
                    .inputItems(WIRE_DOUBLING_ORDER[i], material, 1)
                    .outputItems(WIRE_DOUBLING_ORDER[0], material, (int) Math.pow(2, i))
                    .duration(mass * i)
                    .save(provider);
        }
    }

    @Unique
    private static void gTLCore$processCableStripping(TagPrefix prefix, Material material, WireProperties property,
                                                      Consumer<FinishedRecipe> provider) {
        Material rubber = GTMaterials.Rubber;
        int voltageTier = GTUtil.getTierByVoltage(property.getVoltage());
        if (voltageTier > GTValues.UV) {
            rubber = GTMaterials.StyreneButadieneRubber;
        } else if (voltageTier > GTValues.EV) {
            rubber = GTMaterials.SiliconeRubber;
        }
        GTORecipeTypes.UNPACKER_RECIPES.recipeBuilder(GTOCore.id("strip_" + material.getName() + "_" + prefix.name.toLowerCase()))
                .inputItems(prefix, material)
                .outputItems(cableToWireMap.get(prefix), material)
                .outputItems(TagPrefix.plate, rubber,
                        (int) (prefix.secondaryMaterials().get(0).amount() / GTValues.M))
                .duration(100).EUt(GTValues.VA[GTValues.ULV])
                .save(provider);
    }
}
