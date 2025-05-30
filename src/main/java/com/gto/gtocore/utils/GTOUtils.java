package com.gto.gtocore.utils;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.*;

import static com.gregtechceu.gtceu.api.GTValues.*;

public final class GTOUtils {

    public static int adjacentBlock(Level level, BlockPos pos, Block block) {
        int a = 0;
        BlockPos[] coordinates = { pos.offset(1, 0, 0),
                pos.offset(-1, 0, 0),
                pos.offset(0, 1, 0),
                pos.offset(0, -1, 0),
                pos.offset(0, 0, 1),
                pos.offset(0, 0, -1) };
        for (BlockPos blockPos : coordinates) {
            if (level.getBlockState(blockPos).getBlock() == block) {
                a++;
            }
        }
        return a;
    }

    public static double calculateDistance(BlockPos pos1, BlockPos pos2) {
        int deltaX = pos2.getX() - pos1.getX();
        int deltaY = pos2.getY() - pos1.getY();
        int deltaZ = pos2.getZ() - pos1.getZ();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    public static int getVoltageMultiplier(Material material) {
        return material.getBlastTemperature() >= 2800 ? VA[LV] : VA[ULV];
    }

    public static String[] shapeToPattern(List<String[]> shape) {
        List<String> list = new ArrayList<>();
        for (String[] strings : shape) {
            list.addAll(Arrays.asList(strings));
        }
        return list.toArray(new String[0]);
    }

    public static Map<String, Ingredient> symbolMapTokeys(Map<Character, Ingredient> symbolMap) {
        Map<String, Ingredient> keys = new Object2ObjectOpenHashMap<>();
        symbolMap.forEach((k, v) -> keys.put(k.toString(), v));
        keys.put(" ", Ingredient.EMPTY);
        return keys;
    }

    public static Map<String, Ingredient> reconstructKeys(NonNullList<Ingredient> ingredients) {
        Map<String, Ingredient> keys = new Object2ObjectOpenHashMap<>();
        Set<Ingredient> usedIngredients = new ObjectOpenHashSet<>();
        char nextKey = 'A';
        for (Ingredient ingredient : ingredients) {
            if (ingredient != Ingredient.EMPTY && !usedIngredients.contains(ingredient)) {
                String key = String.valueOf(nextKey++);
                keys.put(key, ingredient);
                usedIngredients.add(ingredient);
            }
        }
        return keys;
    }

    public static String[] reconstructPattern(NonNullList<Ingredient> ingredients, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
        String[] pattern = new String[patternHeight];
        for (int i = 0; i < patternHeight; ++i) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < patternWidth; ++j) {
                Ingredient ingredient = ingredients.get(j + patternWidth * i);
                if (ingredient == Ingredient.EMPTY) {
                    row.append(" ");
                } else {
                    for (Map.Entry<String, Ingredient> entry : keys.entrySet()) {
                        if (entry.getValue().equals(ingredient)) {
                            row.append(entry.getKey());
                            break;
                        }
                    }
                }
            }
            pattern[i] = row.toString();
        }
        return pattern;
    }
}
