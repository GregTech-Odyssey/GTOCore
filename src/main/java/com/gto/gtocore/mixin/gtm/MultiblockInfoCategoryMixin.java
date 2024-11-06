package com.gto.gtocore.mixin.gtm;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.integration.jei.multipage.MultiblockInfoCategory;
import com.gregtechceu.gtceu.integration.jei.multipage.MultiblockInfoWrapper;

import com.lowdragmc.lowdraglib.Platform;

import mezz.jei.api.registration.IRecipeRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.List;

@Mixin(MultiblockInfoCategory.class)
public class MultiblockInfoCategoryMixin {

    /**
     * @author mod_author
     * @reason debug
     */
    @Overwrite(remap = false)
    public static void registerRecipes(IRecipeRegistration registry) {
        if (Platform.isDevEnv()) return;
        List<MultiblockInfoWrapper> wrappers = new ArrayList<>();
        for (MachineDefinition machine : GTRegistries.MACHINES.values()) {
            if (machine instanceof MultiblockMachineDefinition multiblockMachine && multiblockMachine.isRenderXEIPreview()) {
                wrappers.add(new MultiblockInfoWrapper(multiblockMachine));
            }
        }
        registry.addRecipes(MultiblockInfoCategory.RECIPE_TYPE, wrappers);
    }
}
