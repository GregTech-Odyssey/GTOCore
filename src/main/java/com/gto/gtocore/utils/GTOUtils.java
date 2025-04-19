package com.gto.gtocore.utils;

import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;
import com.gto.gtocore.api.data.GTODimensions;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import committee.nova.mods.avaritia.api.utils.ItemUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.gregtechceu.gtceu.api.GTValues.*;

public final class GTOUtils {

    private GTOUtils() {}

    public static boolean isGeneration(TagPrefix tagPrefix, Material material) {
        Predicate<Material> condition = tagPrefix.generationCondition();
        if (condition == null) return true;
        return condition.test(material);
    }

    public static List<ItemStack> getInputItems(GTRecipe recipe) {
        return recipe.getInputContents(ItemRecipeCapability.CAP).stream()
                .filter(content -> content.chance > 0)
                .map(content -> ItemRecipeCapability.CAP.of(content.getContent()))
                .map(ingredient -> ingredient.getItems()[0])
                .collect(Collectors.toList());
    }

    public static List<FluidStack> getInputFluids(GTRecipe recipe) {
        return recipe.getInputContents(FluidRecipeCapability.CAP).stream()
                .filter(content -> content.chance > 0)
                .map(content -> FluidRecipeCapability.CAP.of(content.getContent()))
                .map(ingredient -> ingredient.getStacks()[0])
                .collect(Collectors.toList());
    }

    public static List<ItemStack> getOutputItems(GTRecipe recipe) {
        return recipe.getOutputContents(ItemRecipeCapability.CAP).stream()
                .filter(content -> content.chance == ChanceLogic.getMaxChancedValue())
                .map(content -> ItemRecipeCapability.CAP.of(content.getContent()))
                .map(ingredient -> ingredient.getItems()[0])
                .collect(Collectors.toList());
    }

    public static List<FluidStack> getOutputFluids(GTRecipe recipe) {
        return recipe.getOutputContents(FluidRecipeCapability.CAP).stream()
                .filter(content -> content.chance == ChanceLogic.getMaxChancedValue())
                .map(content -> FluidRecipeCapability.CAP.of(content.getContent()))
                .map(ingredient -> ingredient.getStacks()[0])
                .collect(Collectors.toList());
    }

    public static long getInputMANAt(GTRecipe recipe) {
        return recipe.getTickInputContents(ManaRecipeCapability.CAP).stream().map(Content::getContent).mapToLong(ManaRecipeCapability.CAP::of).sum();
    }

    public static long getOutputMANAt(GTRecipe recipe) {
        return recipe.getTickOutputContents(ManaRecipeCapability.CAP).stream().map(Content::getContent).mapToLong(ManaRecipeCapability.CAP::of).sum();
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
            return VA[HV];
        } else if (t > 2700) {
            return VA[MV];
        } else if (t > 0) {
            return VA[LV];
        }
        return VA[ULV];
    }

    public static boolean canSeeSunClearly(Level world, BlockPos blockPos) {
        if (!world.canSeeSky(blockPos.above())) return false;
        if (GTODimensions.isVoid(world.dimension().location())) return true;
        Biome biome = world.getBiome(blockPos.above()).value();
        if (world.isRaining()) {
            if (biome.warmEnoughToRain(blockPos.above()) || biome.coldEnoughToSnow(blockPos.above())) {
                return false;
            }
        }
        if (world.getBiome(blockPos.above()).is(BiomeTags.IS_END)) return false;
        return world.isDay();
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
        return GlobalPos.of(GTODimensions.getDimensionKey(key), BlockPos.of(pos));
    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        return map.entrySet().stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    public static ItemStack loadItemStack(CompoundTag tag) {
        Item item = ItemUtils.getItem(tag.getString("id"));
        ItemStack stack = item.getDefaultInstance();
        if (tag.contains("tag", Tag.TAG_COMPOUND)) {
            stack.setTag(tag.getCompound("tag"));
            if (stack.getTag() != null) {
                stack.getItem().verifyTagAfterLoad(stack.getTag());
            }
        }

        if (stack.getItem().canBeDepleted()) {
            stack.setDamageValue(stack.getDamageValue());
        }
        return stack;
    }

    public static FluidStack loadFluidStack(CompoundTag tag) {
        Fluid fluid = FluidUtils.getFluid(tag.getString("FluidName"));
        FluidStack stack = new FluidStack(fluid, 1);
        if (tag.contains("Tag", Tag.TAG_COMPOUND)) {
            stack.setTag(tag.getCompound("Tag"));
        }
        return stack;
    }
}
