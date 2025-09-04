package com.gtocore.mixin.gtm.api;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.*;
import com.gtocore.data.Datagen;

import com.gtolib.GTOCore;
import com.gtolib.api.capability.recipe.GTORecipeCapabilities;
import com.gtolib.api.data.GTOWorldGenLayers;
import com.gtolib.api.registries.GTORegistration;

import com.gregtechceu.gtceu.api.addon.AddonFinder;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AddonFinder.class)
public class AddonFinderMixin {

    @Shadow(remap = false)
    protected static List<IGTAddon> cache;

    @Inject(method = "getAddons", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/addon/AddonFinder;getInstances(Ljava/lang/Class;Ljava/lang/Class;)Ljava/util/List;"), remap = false, cancellable = true)
    private static void getAddons(CallbackInfoReturnable<List<IGTAddon>> cir) {
        cache = List.of(gtolib$GTADDON);
        cir.setReturnValue(cache);
    }

    @Inject(method = "getAddon", at = @At("RETURN"), remap = false, cancellable = true)
    private static void getAddon(String modId, CallbackInfoReturnable<IGTAddon> cir) {
        cir.setReturnValue(modId.equals(GTOCore.MOD_ID) ? gtolib$GTADDON : null);
    }

    @Unique
    private final static IGTAddon gtolib$GTADDON = new IGTAddon() {

        @Override
        public String addonModId() {
            return GTOCore.MOD_ID;
        }

        @Override
        public GTRegistrate getRegistrate() {
            return GTORegistration.GTO;
        }

        @Override
        public boolean requiresHighTier() {
            return true;
        }

        @Override
        public void initializeAddon() {
            Datagen.init();
        }

        @Override
        public void registerSounds() {
            GTOSoundEntries.init();
        }

        @Override
        public void registerCovers() {
            GTOCovers.init();
            GTORegistration.GTO.creativeModeTab(() -> GTOCreativeModeTabs.GTO_BLOCK);
            GTOBlocks.init();
            GTORegistration.GTO.creativeModeTab(() -> GTOCreativeModeTabs.GTO_ITEM);
            GTOItems.init();
        }

        @Override
        public void registerElements() {
            GTOElements.init();
        }

        @Override
        public void registerTagPrefixes() {
            GTOTagPrefix.init();
        }

        @Override
        public void registerWorldgenLayers() {
            GTOWorldGenLayers.init();
        }

        @Override
        public void registerRecipeCapabilities() {
            GTORecipeCapabilities.init();
        }
    };
}
