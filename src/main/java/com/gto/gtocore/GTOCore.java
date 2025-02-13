package com.gto.gtocore;

import com.gto.gtocore.api.capability.recipe.GTORecipeCapabilities;
import com.gto.gtocore.api.data.GTOWorldGenLayers;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.registries.GTORegistration;
import com.gto.gtocore.client.ClientProxy;
import com.gto.gtocore.common.CommonProxy;
import com.gto.gtocore.common.data.GTOBedrockFluids;
import com.gto.gtocore.common.data.GTOCovers;
import com.gto.gtocore.common.data.GTOCreativeModeTabs;
import com.gto.gtocore.common.data.GTOOres;
import com.gto.gtocore.data.Datagen;
import com.gto.gtocore.init.GTOBlocks;
import com.gto.gtocore.init.GTOElements;
import com.gto.gtocore.init.GTOItems;
import com.gto.gtocore.init.GTOSoundEntries;

import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.addon.events.KJSRecipeKeyEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.BedrockOreDefinition;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTFluids;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import com.enderio.base.common.init.EIOFluids;
import earth.terrarium.adastra.common.registry.ModFluids;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

@Mod(GTOCore.MOD_ID)
public final class GTOCore {

    public static final String MOD_ID = "gtocore";
    public static final String NAME = "GTO Core";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final IGTAddon GTADDON = new GTAddon();

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public GTOCore() {
        DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

    private static class GTAddon implements IGTAddon {

        @Override
        public String addonModId() {
            return MOD_ID;
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
            REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_BLOCK);
            GTOBlocks.init();
            REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_ITEM);
            GTOItems.init();
            GTMaterials.Oxygen.getProperty(PropertyKey.FLUID).getStorage().store(FluidStorageKeys.GAS, ModFluids.OXYGEN, null);
            GTMaterials.Hydrogen.getProperty(PropertyKey.FLUID).getStorage().store(FluidStorageKeys.GAS, ModFluids.HYDROGEN, null);
            GTFluids.handleNonMaterialFluids(GTMaterials.Oil, ModFluids.OIL);
            GTFluids.handleNonMaterialFluids(GTMaterials.RocketFuel, () -> EIOFluids.ROCKET_FUEL.get().getSource());
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
    }
}
