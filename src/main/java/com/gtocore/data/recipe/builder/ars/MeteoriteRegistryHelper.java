package com.gtocore.data.recipe.builder.ars;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import com.arsmeteorites.arsmeteorites.common.RecipeRegistry;
import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Ints;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

public final class MeteoriteRegistryHelper {

    public static void registerMeteoriteType(String inputItemId, double source, int model, Item consumeitem, String[] blockIds, int[] weights) {
        if (model == 3 || model == 4) {
            GTOCore.LOGGER.error("无法注册陨石类型: 输入物品 {}, 陨石模型 {} 需要层信息", inputItemId, model);
            return;
        }
        int[] layer = { 0 };
        registerMeteoriteType(inputItemId, source, model, consumeitem, blockIds, weights, layer);
    }

    public static void registerMeteoriteType(String inputItemId, double source, int model, Item consumeitem, String[] blockIds, int[] weights, int[] layer) {
        Item input = RegistriesUtils.getItem(inputItemId);
        if (input == Items.AIR) {
            GTOCore.LOGGER.error("无法注册陨石类型 {}: 物品不存在", inputItemId);
            return;
        }

        Block[] blocks = new Block[blockIds.length];
        for (int i = 0; i < blockIds.length; i++) {
            blocks[i] = RegistriesUtils.getBlock(blockIds[i]);
            if (blocks[i] == Blocks.AIR) {
                GTOCore.LOGGER.error("无法注册陨石类型 {}: 方块 {} 不存在", inputItemId, blockIds[i]);
                return;
            }
        }

        if (weights.length != blockIds.length) {
            GTOCore.LOGGER.error("无法注册陨石类型 {}: 权重数组长度不匹配", inputItemId);
            return;
        }

        registerMeteoriteType(input, source, model, consumeitem, blocks, weights, layer);
    }

    public static void registerMeteoriteType(Item input, double source, int model, Item catalysts, Block[] meteorites, int[] weights) {
        if (model == 3 || model == 4) {
            GTOCore.LOGGER.error("无法注册陨石类型 {}: 陨石模型 {} 需要层信息", input, model);
            return;
        }
        int[] layer = { 0 };
        registerMeteoriteType(input, source, model, catalysts, meteorites, weights, layer);
    }

    public static void registerMeteoriteType(Item input, double source, Item consumeitem, Block[] stones, int[] weights1, TagPrefix tagPrefix, Material[] material, int[] weights2) {
        Block[] ores = new Block[material.length];
        for (int i = 0; i < material.length; i++) {
            ores[i] = ChemicalHelper.getBlock(tagPrefix, material[i]);
            if (ores[i] == Blocks.AIR) {
                GTOCore.LOGGER.error("陨石的 {}: 对应矿石 {} 不存在", material[i], tagPrefix);
                return;
            }
        }
        Block[] InnerBlock = ObjectArrays.concat(stones, ores, Block.class);
        Block[] allBlock = ObjectArrays.concat(InnerBlock, stones, Block.class);
        int[] weights = Ints.concat(weights1, weights2, weights1);
        int[] layer = { InnerBlock.length, stones.length, 60, 10 };
        registerMeteoriteType(input, source, 3, consumeitem, allBlock, weights, layer);
    }

    public static void registerMeteoriteType(@NotNull Item input, double source, int model, Item consumeitem, Block[] meteorites, int[] weights, int[] layer) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(input);
        String id = itemId.getNamespace() + "-" + itemId.getPath();
        int totalWeight = 0;
        for (int weight : weights) totalWeight += weight;

        int[] newLayer;
        if (model == 3 || model == 4) {
            if (layer.length % 2 != 0) {
                GTOCore.LOGGER.error("无法注册陨石类型 {}: 层级参数错误", input);
                return;
            }

            int TotalLayers = layer.length / 2;

            int m = meteorites.length;
            for (int i = 0; i < TotalLayers; i++) m -= layer[i];
            if (m < 0) {
                GTOCore.LOGGER.error("无法注册陨石类型 {}: 层中方块数量大于总方块数", input);
                return;
            }

            int[] TotalMeteoritesWeights = new int[TotalLayers + 2];
            TotalMeteoritesWeights[TotalLayers] = TotalLayers;
            for (int i = 0; i < TotalLayers; i++) TotalMeteoritesWeights[TotalLayers + 1] += layer[TotalLayers + i];

            int k = 0;
            for (int i = 0; i < TotalLayers; i++) {
                for (int j = 0; j < layer[i]; j++) TotalMeteoritesWeights[i] += weights[k + j];
                k += layer[i];
            }

            newLayer = IntStream.concat(Arrays.stream(layer), Arrays.stream(TotalMeteoritesWeights)).toArray();
        } else newLayer = layer;

        try {
            RecipeRegistry.registerMeteoriteType(new RecipeRegistry.MeteoriteType(id, input, source, model, consumeitem, meteorites, weights, totalWeight, newLayer));
        } catch (IllegalStateException e) {
            GTOCore.LOGGER.error("注册陨石类型失败: {}", e.getMessage());
        }
    }
}
