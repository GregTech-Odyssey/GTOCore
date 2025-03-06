package com.gto.gtocore.mixin.placebo;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.item.crafting.RecipeManager;

import dev.shadowsoffire.placebo.recipe.RecipeHelper;
import dev.shadowsoffire.placebo.util.RunnableReloader;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RecipeHelper.class)
public final class RecipeHelperMixin {

    /**
     * @author .
     * @reason .
     */
    @ApiStatus.Internal
    @Overwrite(remap = false)
    public static PreparableReloadListener getReloader(RecipeManager mgr) {
        return RunnableReloader.of(() -> {});
    }
}
