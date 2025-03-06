package com.gto.gtocore.mixin.placebo;

import net.minecraft.world.item.crafting.RecipeManager;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.shadowsoffire.placebo.recipe.RecipeHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(RecipeHelper.class)
public interface RecipeHelperAccessor {

    @Accessor(value = "PROVIDERS", remap = false)
    static Multimap<String, Consumer<RecipeHelper.RecipeFactory>> getProviders() {
        return HashMultimap.create();
    }

    @Invoker(value = "addRecipes", remap = false)
    static void addRecipes(RecipeManager mgr) {}
}
