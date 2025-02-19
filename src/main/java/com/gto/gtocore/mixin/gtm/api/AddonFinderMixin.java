package com.gto.gtocore.mixin.gtm.api;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.capability.recipe.GTORecipeCapabilities;
import com.gto.gtocore.api.data.GTOWorldGenLayers;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.registries.GTORegistration;
import com.gto.gtocore.common.data.*;
import com.gto.gtocore.data.Datagen;

import com.gregtechceu.gtceu.api.addon.AddonFinder;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.addon.events.KJSRecipeKeyEvent;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.BedrockOreDefinition;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
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
        cache = List.of(gtocore$GTADDON);
        cir.setReturnValue(cache);
    }

    @Inject(method = "getAddon", at = @At("RETURN"), remap = false, cancellable = true)
    private static void getAddon(String modId, CallbackInfoReturnable<IGTAddon> cir) {
        cir.setReturnValue(modId.equals(GTOCore.MOD_ID) ? gtocore$GTADDON : null);
    }

    @Unique
    private static final IGTAddon gtocore$GTADDON = new IGTAddon() {

        @Override
        public String addonModId() {
            return GTOCore.MOD_ID;
        }

        @Override
        public GTRegistrate getRegistrate() {
            return GTORegistration.REGISTRATE;
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
            GTORegistration.REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_BLOCK);
            GTOBlocks.init();
            GTORegistration.REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_ITEM);
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
        public void registerFluidVeins() {
            GTOBedrockFluids.init();
        }

        @Override
        public void registerBedrockOreVeins() {
            GTOOres.BEDROCK_ORES_DEFINITION.forEach(GTRegistries.BEDROCK_ORE_DEFINITIONS::registerOrOverride);
            GTOOres.BEDROCK_ORES.forEach((id, bedrockOre) -> {
                BedrockOreDefinition definition = BedrockOreDefinition.builder(id).size(9).dimensions(bedrockOre.dimensions()).weight(bedrockOre.weight()).materials(bedrockOre.materials()).yield(2, 8).depletedYield(1).depletionAmount(1).depletionChance(100).register();
                GTOOres.BEDROCK_ORES_DEFINITION.put(id, definition);
            });
            GTOOres.BEDROCK_ORES.clear();
        }

        @Override
        public void registerOreVeins() {
            GTOOres.init();
        }

        @Override
        public void registerWorldgenLayers() {
            GTOWorldGenLayers.init();
        }

        @Override
        public void registerRecipeCapabilities() {
            GTORecipeCapabilities.init();
        }

        @Override
        public void registerRecipeKeys(KJSRecipeKeyEvent event) {
            GTORecipeCapabilities.registerRecipeKeys(event);
        }
    };
}
