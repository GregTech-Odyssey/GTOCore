package com.gtocore.data.recipe.generated;

import com.gtolib.GTOCore;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.server.ServerLifecycleHooks;

import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidContainerIngredient;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import com.gtocore.common.item.ItemMap;
import com.gtocore.common.data.GTOItems;

import it.unimi.dsi.fastutil.objects.Reference2CharLinkedOpenHashMap;
import org.jetbrains.annotations.NotNull;

import static com.gregtechceu.gtceu.common.data.GTMaterials.Lava;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Water;

/**
 * 用于自动为包含水桶或熔岩桶的原版合成配方注入流体容器配方变体的工具类。
 * <p>
 * 该类会在服务端启动时遍历所有原版合成配方，筛选出包含水桶或熔岩桶的配方，
 * 并为其注册流体容器配方变体，使其支持流体容器作为原材料。
 */
public final class VanillaFluidRecipeHandler {

    /**
     * 初始化方法。
     * <p>
     * 在服务端启动时调用，遍历所有原版合成配方，筛选出包含水桶或熔岩桶的配方，
     * 并为其注册流体容器配方变体。
     */
    public static void init() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            GTOCore.LOGGER.warn("[VanillaFluidRecipeHandler] Server not available, skipping recipe injection");
            return;
        }
        
        var manager = server.getRecipeManager();
        var access = server.registryAccess();
        int count = 0;
        var recipes = manager.getAllRecipesFor(RecipeType.CRAFTING);
        
        for (var recipe : recipes) {
            if (!containsBucket(recipe)) continue;
            
            if (recipe instanceof ShapedRecipe shaped) {
                if (registerFluidContainerVariant(shaped, access)) count++;
            }
        }
        
        GTOCore.LOGGER.info("[VanillaFluidRecipeHandler] Injected {} fluid container recipe variants", count);
    }
    
    /**
     * 判断指定配方是否包含水桶或熔岩桶作为原材料。
     *
     * @param recipe 要判断的配方
     * @return 如果包含水桶或熔岩桶则返回true，否则返回false
     */
    private static boolean containsBucket(@NotNull Recipe<?> recipe) {
        for (Ingredient ing : recipe.getIngredients()) {
            if (ing.test(new ItemStack(Items.WATER_BUCKET)) || ing.test(new ItemStack(Items.LAVA_BUCKET))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 为指定的ShapedRecipe注册流体容器配方变体。
     * <p>
     * 会将配方中的水桶和熔岩桶替换为对应的流体容器原材料，并调用VanillaRecipeHelper进行注册。
     *
     * @param shaped 需要注册变体的ShapedRecipe
     * @param access 注册表访问器
     * @return 注册成功返回true，失败返回false
     */
    private static boolean registerFluidContainerVariant(@NotNull ShapedRecipe shaped, @NotNull RegistryAccess access) {
        int w = shaped.getWidth();
        int h = shaped.getHeight();
        NonNullList<Ingredient> ings = shaped.getIngredients();
        ItemStack output = shaped.getResultItem(access);
        ResourceLocation id = shaped.getId();
        
        // Build pattern strings
        String[] pattern = new String[h];
        Reference2CharLinkedOpenHashMap<Ingredient> charMap = new Reference2CharLinkedOpenHashMap<>();
        char nextChar = 'A';
        
        for (int y = 0; y < h; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < w; x++) {
                int idx = y * w + x;
                if (idx >= ings.size() || ings.get(idx).isEmpty()) {
                    row.append(' ');
                } else {
                    Ingredient ing = ings.get(idx);
                    if (!charMap.containsKey(ing)) {
                        charMap.put(ing, nextChar++);
                    }
                    row.append(charMap.getChar(ing));
                }
            }
            pattern[y] = row.toString();
        }
        
        // Build ingredient mapping with FluidContainerIngredient replacements
        Object[] mappingArgs = new Object[charMap.size() * 2];
        int argIndex = 0;
        
        for (var entry : charMap.reference2CharEntrySet()) {
            Ingredient ing = entry.getKey();
            char ch = entry.getCharValue();
            
            mappingArgs[argIndex++] = ch;
            
            if (ing.test(new ItemStack(Items.WATER_BUCKET))) {
                mappingArgs[argIndex++] = new FluidContainerIngredient(Water.getFluid(1000));
            } else if (ing.test(new ItemStack(Items.LAVA_BUCKET))) {
                mappingArgs[argIndex++] = new FluidContainerIngredient(Lava.getFluid(1000));
            } else {
                ItemStack[] stacks = ing.getItems();
                if (stacks.length > 0) {
                    mappingArgs[argIndex++] = convertToProperIngredient(stacks[0]);
                } else {
                    mappingArgs[argIndex++] = ing; // fallback
                }
            }
        }
        
        try {
            VanillaRecipeHelper.addShapedFluidContainerRecipe(
                GTOCore.id("fluid_" + id.getPath()),
                output,
                pattern,
                mappingArgs
            );
            return true;
        } catch (Exception e) {
            GTOCore.LOGGER.warn("[VanillaFluidRecipeHandler] Failed to register variant for {}: {}", id, e.getMessage());
            return false;
        }
    }

    private static Object convertToProperIngredient(@NotNull ItemStack stack) {
        if (ItemMap.UNIVERSAL_CIRCUITS.contains(stack.getItem())) {
            for (int tier : GTMachineUtils.ALL_TIERS) {
                if (GTOItems.UNIVERSAL_CIRCUIT[tier].is(stack.getItem())) {
                    return FastSizedIngredient.create(CustomTags.CIRCUITS_ARRAY[tier], stack.getCount());
                }
            }
        }
        return FastSizedIngredient.create(stack);
    }

}
