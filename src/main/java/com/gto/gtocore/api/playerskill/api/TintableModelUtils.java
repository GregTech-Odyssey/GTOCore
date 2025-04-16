package com.gto.gtocore.api.playerskill.api;

import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;

/**
 * 可着色物品模型工具类
 * 提供创建支持颜色染色的物品模型的实用方法
 */
public class TintableModelUtils {

    /**
     * 创建一个可着色的物品模型，使用默认的"item/generated"父模型
     * 
     * @param <T>          物品类型
     * @param ctx          数据生成上下文
     * @param prov         物品模型提供者
     * @param texturePaths 纹理路径数组
     */
    public static <T extends Item> void createTintableModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider prov, String... texturePaths) {
        createTintableModel(ctx, "item/generated", prov, texturePaths);
    }

    /**
     * 创建一个可着色的物品模型，使用指定的父模型
     * 此方法通过添加tintindexes属性让物品支持颜色染色
     * 
     * @param <T>          物品类型
     * @param ctx          数据生成上下文
     * @param parentModel  父模型路径
     * @param prov         物品模型提供者
     * @param texturePaths 纹理路径数组，每个路径对应模型的一个层
     */
    public static <T extends Item> void createTintableModel(DataGenContext<Item, T> ctx, String parentModel, RegistrateItemModelProvider prov, String... texturePaths) {
        // 创建模型构建器并设置父模型
        ItemModelBuilder builder = prov.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(parentModel));

        // 创建可染色层索引数组
        JsonArray tints = new JsonArray();
        JsonObject json = builder.toJson();

        // 添加所有纹理层并设置它们为可染色
        for (int i = 0; i < texturePaths.length; i++) {
            builder.texture("layer" + i, prov.modLoc(texturePaths[i]));
            tints.add(i);
        } ;

        // 将染色索引添加到JSON模型中
        json.add("tintindexes", tints);
    }
}
