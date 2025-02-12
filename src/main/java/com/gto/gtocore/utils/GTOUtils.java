package com.gto.gtocore.utils;

import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;
import com.gto.gtocore.api.data.GTOWorldGenLayers;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.*;
import java.util.function.Predicate;

import static com.gregtechceu.gtceu.api.GTValues.*;

public final class GTOUtils {

    public static boolean isGeneration(TagPrefix tagPrefix, Material material) {
        Predicate<Material> condition = tagPrefix.generationCondition();
        if (condition == null) return true;
        return condition.test(material);
    }

    public static int getInputMANAt(GTRecipe recipe) {
        return recipe.getTickInputContents(ManaRecipeCapability.CAP).stream().map(Content::getContent).mapToInt(ManaRecipeCapability.CAP::of).sum();
    }

    public static int getOutputMANAt(GTRecipe recipe) {
        return recipe.getTickOutputContents(ManaRecipeCapability.CAP).stream().map(Content::getContent).mapToInt(ManaRecipeCapability.CAP::of).sum();
    }

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
        int t = material.getBlastTemperature();
        if (t > 8460) {
            return VA[MV];
        } else if (t > 2700) {
            return VA[LV];
        }
        return VA[ULV];
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

    public static GlobalPos readGlobalPos(String dimension, long pos) {
        if (dimension.isEmpty()) return null;
        if (pos == 0) return null;
        ResourceLocation key = ResourceLocation.tryParse(dimension);
        if (key == null) return null;
        return GlobalPos.of(GTOWorldGenLayers.getDimension(key), BlockPos.of(pos));
    }
}
