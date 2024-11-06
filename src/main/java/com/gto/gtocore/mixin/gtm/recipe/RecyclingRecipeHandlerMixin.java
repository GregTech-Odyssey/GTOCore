package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;

import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.data.recipe.generated.RecyclingRecipeHandler;

import net.minecraft.data.recipes.FinishedRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;

@Mixin(RecyclingRecipeHandler.class)
public class RecyclingRecipeHandlerMixin {

    @Unique
    private static final List<Object> gTLCore$PREFIXES = Arrays.asList(
            ingot, gem, rod, plate, ring, rodLong, foil, bolt, screw,
            nugget, gearSmall, gear, frameGt, plateDense, spring, springSmall,
            block, wireFine, rotor, lens, turbineBlade, round, plateDouble, dust,
            GTOTagPrefix.curvedPlate,
            (Predicate<TagPrefix>) orePrefix -> orePrefix.name().startsWith("gem"),
            (Predicate<TagPrefix>) orePrefix -> orePrefix.name().startsWith("wireGt"),
            (Predicate<TagPrefix>) orePrefix -> orePrefix.name().startsWith("pipe"));

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
        for (TagPrefix orePrefix : TagPrefix.values()) {
            if (gTLCore$PREFIXES.stream().anyMatch(object -> {
                if (object instanceof TagPrefix)
                    return object == orePrefix;
                else if (object instanceof Predicate)
                    return ((Predicate<TagPrefix>) object).test(orePrefix);
                else return false;
            })) orePrefix.executeHandler(provider, PropertyKey.DUST, RecyclingRecipeHandler::processCrushing);
        }
    }
}
