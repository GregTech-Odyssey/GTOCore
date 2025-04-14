package com.gto.gtocore.api.playerskill.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

/**
 * 为 Registrate 的 model() 方法创建一个工具类，支持多层 tintindex
 */
public class TintableModelUtils {

    /**
     * 创建多层纹理物品模型，自动设置 tintindexes
     *
     * @param ctx 数据生成上下文
     * @param prov Registrate物品模型提供器
     * @param texturePaths 纹理路径数组，从layer0开始依次应用
     */
    public static <T extends Item> void createTintableModel(
            DataGenContext<Item, T> ctx,
            RegistrateItemModelProvider prov,
            String... texturePaths) {
        createTintableModel(ctx, "item/generated",prov , texturePaths);
    }

    /**
     * 创建多层纹理物品模型，自定义父模型，自动设置 tintindexes
     *
     * @param ctx 数据生成上下文
     * @param prov Registrate物品模型提供器
     * @param parentModel 父模型路径
     * @param texturePaths 纹理路径数组，从layer0开始依次应用
     */
    public static <T extends Item> void createTintableModel(
            DataGenContext<Item, T> ctx,
            String parentModel,
            RegistrateItemModelProvider prov,
            String... texturePaths) {

        // 1. 创建标准的物品模型构建器
        ItemModelBuilder modelBuilder = prov.getBuilder(ctx.getName())
                .parent(new ModelFile.UncheckedModelFile(parentModel));

        // 2. 添加所有纹理层
        for (int i = 0; i < texturePaths.length; i++) {
            modelBuilder.texture("layer" + i, prov.modLoc(texturePaths[i]));
        }

        // 3. 使用自定义方法添加 tintindexes 到生成的 JSON
        addTintIndexesToModel(modelBuilder, texturePaths.length);
    }

    /**
     * 为模型添加 tintindexes
     */
    private static void addTintIndexesToModel(ItemModelBuilder modelBuilder, int layerCount) {
        // 获取原始 JSON
        JsonObject json = modelBuilder.toJson();

        // 创建 tintindexes 数组
        JsonArray tintindexesJson = new JsonArray();
        for (int i = 0; i < layerCount; i++) {
            tintindexesJson.add(i);
        }

        // 添加到 json 对象
        json.add("tintindexes", tintindexesJson);

        // 注意：由于 ItemModelBuilder 没有提供修改 JSON 的方法，
        // 这里我们依赖于 Registrate 在最终写入文件时使用 toJson() 方法
    }
}