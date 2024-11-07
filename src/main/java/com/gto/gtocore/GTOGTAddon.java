package com.gto.gtocore;

import com.gto.gtocore.api.capability.recipe.GTORecipeCapabilities;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.registries.GTORegistration;
import com.gto.gtocore.common.data.GTOBedrockFluids;
import com.gto.gtocore.common.data.GTOCovers;
import com.gto.gtocore.common.data.GTOElements;
import com.gto.gtocore.common.data.GTOSoundEntries;
import com.gto.gtocore.config.GTConfigModify;
import com.gto.gtocore.data.GTODatagen;
import com.gto.gtocore.data.recipe.RemoveRecipe;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import com.lowdragmc.lowdraglib.Platform;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@GTAddon
public class GTOGTAddon implements IGTAddon {

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
        GTODatagen.init();
    }

    @Override
    public void registerSounds() {
        GTOSoundEntries.init();
    }

    @Override
    public void registerCovers() {
        GTOCovers.init();
    }

    @Override
    public void registerElements() {
        GTConfigModify.init();
        GTOElements.init();
    }

    @Override
    public void registerTagPrefixes() {
        GTOTagPrefix.init();
    }

    @Override
    public void removeRecipes(Consumer<ResourceLocation> consumer) {
        RemoveRecipe.init(consumer);
    }

    @Override
    public void registerFluidVeins() {
        if (!Platform.isDevEnv()) {
            GTOBedrockFluids.init();
        }
    }

    @Override
    public void registerRecipeCapabilities() {
        GTORecipeCapabilities.init();
    }
}
