package com.gtocore.client.model;

import com.gtolib.GTOCore;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShaderItemModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    public static <T extends ModelBuilder<T>> ShaderItemModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
        return new ShaderItemModelBuilder<>(parent, existingFileHelper);
    }

    private ResourceLocation shader;
    private final Map<String, JsonElement> params = new LinkedHashMap<>();

    protected ShaderItemModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(GTOCore.id("custom_shader"), parent, existingFileHelper);
    }

    public ShaderItemModelBuilder<T> shader(ResourceLocation shader) {
        Preconditions.checkNotNull(shader, "shader must not be null");
        this.shader = shader;
        return this;
    }

    public ShaderItemModelBuilder<T> param(String name, float value) {
        Preconditions.checkNotNull(name, "param name must not be null");
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("value", value);
        params.put(name, wrapper.get("value"));
        return this;
    }

    public ShaderItemModelBuilder<T> param(String name, final float... values) {
        Preconditions.checkNotNull(values, "param values must not be null");
        Preconditions.checkArgument(values.length > 0, "param values must not be empty");
        if (values.length == 1) {
            return param(name, values[0]);
        }
        JsonArray array = new JsonArray();
        for (float value : values) {
            array.add(value);
        }
        params.put(name, array);
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);

        Preconditions.checkNotNull(shader, "shader must not be null");

        JsonObject shaderObject = new JsonObject();
        shaderObject.addProperty("shader", shader.toString());
        if (!params.isEmpty()) {
            JsonObject paramsObject = new JsonObject();
            params.forEach(paramsObject::add);
            shaderObject.add("params", paramsObject);
        }

        json.add("custom_shader", shaderObject);
        return json;
    }
}
