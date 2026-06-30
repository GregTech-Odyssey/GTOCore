package com.gtocore.client.model;

import com.gtolib.utils.RLUtils;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class ShaderItemModelLoader implements IGeometryLoader<ShaderItemModelLoader.ShaderItemGeometry> {

    public static final ShaderItemModelLoader INSTANCE = new ShaderItemModelLoader();

    private ShaderItemModelLoader() {}

    @Override
    public ShaderItemGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
        JsonObject glslItem = jsonObject.getAsJsonObject("custom_shader");
        if (glslItem == null) {
            throw new IllegalStateException("Missing 'custom_shader' object.");
        }
        if (!glslItem.has("shader")) {
            throw new IllegalStateException("Missing 'custom_shader.shader' property.");
        }
        if (glslItem.has("mask")) {
            throw new IllegalStateException("'custom_shader.mask' is no longer supported. Runtime projected masks are used automatically.");
        }

        ResourceLocation shader = RLUtils.parse(GsonHelper.getAsString(glslItem, "shader"));
        Map<String, UniformValue> params = readParams(glslItem);

        JsonObject baseModelJson = jsonObject.deepCopy();
        baseModelJson.remove("custom_shader");
        baseModelJson.remove("loader");
        BlockModel baseModel = context.deserialize(baseModelJson, BlockModel.class);
        return new ShaderItemGeometry(baseModel, shader, params);
    }

    private static Map<String, UniformValue> readParams(JsonObject glslItem) {
        Map<String, UniformValue> params = new LinkedHashMap<>();
        if (!glslItem.has("params")) {
            return params;
        }
        JsonObject jsonParams = GsonHelper.getAsJsonObject(glslItem, "params");
        for (Map.Entry<String, JsonElement> entry : jsonParams.entrySet()) {
            params.put(entry.getKey(), UniformValue.fromJson(entry.getValue()));
        }
        return params;
    }

    public static class ShaderItemGeometry implements IUnbakedGeometry<ShaderItemGeometry> {

        private final BlockModel baseModel;
        private final ResourceLocation shaderLocation;
        private final Map<String, UniformValue> params;

        public ShaderItemGeometry(BlockModel baseModel, ResourceLocation shaderLocation, Map<String, UniformValue> params) {
            this.baseModel = baseModel;
            this.shaderLocation = shaderLocation;
            this.params = params;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter,
                               ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            BakedModel bakedBase = baseModel.bake(baker, baseModel, spriteGetter, modelState, modelLocation, true);
            return new ShaderItemBakedModel(bakedBase, ProjectedItemMaskProvider.INSTANCE, shaderLocation, params);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver, IGeometryBakingContext context) {
            baseModel.resolveParents(resolver);
        }
    }

    public record UniformValue(float[] values) {

        public static UniformValue fromJson(JsonElement jsonElement) {
            if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
                return new UniformValue(new float[] { jsonElement.getAsFloat() });
            }
            if (jsonElement.isJsonArray()) {
                JsonArray array = jsonElement.getAsJsonArray();
                float[] values = new float[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    values[i] = array.get(i).getAsFloat();
                }
                return new UniformValue(values);
            }
            throw new IllegalStateException("Only numeric uniforms are currently supported in custom_shader.params");
        }
    }
}
