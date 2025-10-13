package com.gtocore.mixin.patchouli;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

@Mixin(PageDoubleRecipeRegistry.class)
public class PageDoubleRecipeRegistryMixin {

    @Redirect(method = "loadRecipe(Lnet/minecraft/world/level/Level;Lvazkii/patchouli/client/book/BookContentsBuilder;Lvazkii/patchouli/client/book/BookEntry;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/world/item/crafting/Recipe;", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), remap = false)
    private void redirectWarn(org.apache.logging.log4j.Logger logger, String msg, Object arg1, Object arg2) {}
}
