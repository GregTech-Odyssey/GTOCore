package com.gto.gtocore.mixin.patchouli;

import net.minecraft.resources.ResourceLocation;

import com.google.gson.JsonElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.patchouli.client.book.BookContentResourceListenerLoader;

import java.util.Map;

@Mixin(BookContentResourceListenerLoader.class)
public interface BookContentResourceListenerLoaderAccessor {

    @Accessor(value = "data", remap = false)
    Map<ResourceLocation, Map<ResourceLocation, JsonElement>> getData();
}
