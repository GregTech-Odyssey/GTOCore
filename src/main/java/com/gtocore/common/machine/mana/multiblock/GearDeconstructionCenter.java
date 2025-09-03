package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.holder.IntHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import dev.shadowsoffire.apotheosis.adventure.Adventure;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public class GearDeconstructionCenter extends ManaMultiblockMachine {

    public GearDeconstructionCenter(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    private Recipe getRecipe() {
        // 创建新的配方构建器
        RecipeBuilder recipeBuilder = getRecipeBuilder();
        List<ItemStack> inputs = new ObjectArrayList<>();
        List<ItemStack> outputs = new ObjectArrayList<>();
        IntHolder count = new IntHolder(0);
        forEachInputItems(stack -> {
            // 检测标签，工具和 armor 不处理
            var tags = stack.getItem().builtInRegistryHolder().tags;
            if (!tags.contains(Tags.Items.TOOLS) && !tags.contains(Tags.Items.ARMORS)) return false;
            // 处理有效的装备分解
            if (disassembleEquipment(stack, inputs, outputs)) count.value++;
            return false;
        });
        if (!outputs.isEmpty()) {
            inputs.forEach(recipeBuilder::inputItems);
            outputs.forEach(recipeBuilder::outputItems);
            recipeBuilder.duration(20 * count.value);
            return recipeBuilder.buildRawRecipe();
        }
        return null;
    }

    /**
     * 将Apotheosis装备分解为宝石、附魔书和材料
     *
     * @param equipment 要分解的装备
     * @param inputs    输入列表
     * @param outputs   输出列表
     */
    private static boolean disassembleEquipment(ItemStack equipment, List<ItemStack> inputs, List<ItemStack> outputs) {
        // 添加装备本身到输入列表
        inputs.add(equipment);

        CompoundTag nbt = equipment.getTag();
        if (nbt == null) {
            return false;
        }
        boolean find = false;
        // 提取宝石并计算数量
        int gemCount = extractGems(nbt, outputs);
        if (gemCount > 0) {
            inputs.add(new ItemStack(Adventure.Items.SIGIL_OF_WITHDRAWAL.get(), gemCount));
            find = true;
        }

        // 提取附魔并创建附魔书，计算数量
        int enchantmentCount = extractEnchantments(nbt, outputs);
        if (enchantmentCount > 0) {
            inputs.add(new ItemStack(Items.BOOK, enchantmentCount));
            find = true;
        }

        // 根据稀有度生成材料
        return generateMaterials(nbt, outputs) || find;
    }

    /**
     * 从装备NBT中提取宝石
     *
     * @param nbt     装备的NBT数据
     * @param outputs 输出列表
     * @return 提取的宝石数量
     */
    private static int extractGems(CompoundTag nbt, List<ItemStack> outputs) {
        int gemCount = 0;

        if (nbt.tags.get("affix_data") instanceof CompoundTag data && data.tags.get("gems") instanceof ListTag gems) {
            gemCount = gems.size();

            for (int i = 0; i < gemCount; i++) {
                CompoundTag gemData = gems.getCompound(i);

                // 创建宝石物品堆
                ItemStack gemStack = Adventure.Items.GEM.get().getDefaultInstance();

                // 如果宝石有自定义NBT，则复制
                if (gemData.tags.get("tag") instanceof CompoundTag tag) {
                    gemStack.setTag(tag.copy());
                }

                outputs.add(gemStack);
            }
        }

        return gemCount;
    }

    /**
     * 从装备NBT中提取附魔并创建附魔书
     *
     * @param nbt     装备的NBT数据
     * @param outputs 输出列表
     * @return 创建的附魔书数量
     */
    private static int extractEnchantments(CompoundTag nbt, List<ItemStack> outputs) {
        int enchantmentCount = 0;

        // 提取常规附魔
        if (nbt.tags.get("Enchantments") instanceof ListTag enchantments) {
            enchantmentCount = enchantments.size();

            for (int i = 0; i < enchantmentCount; i++) {
                CompoundTag enchantment = enchantments.getCompound(i);

                // 创建附魔书
                ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                CompoundTag bookTag = new CompoundTag();
                ListTag storedEnchantments = new ListTag();

                // 复制附魔数据
                storedEnchantments.add(enchantment.copy());
                bookTag.put("StoredEnchantments", storedEnchantments);
                enchantedBook.setTag(bookTag);

                outputs.add(enchantedBook);
            }
        }

        return enchantmentCount;
    }

    /**
     * 根据装备稀有度生成材料
     *
     * @param nbt     装备的NBT数据
     * @param outputs 输出列表
     */
    private static boolean generateMaterials(CompoundTag nbt, List<ItemStack> outputs) {
        // 确定材料类型基于装备稀有度
        if (nbt.tags.get("affix_data") instanceof CompoundTag data && data.tags.get("rarity") instanceof StringTag tag) {
            Item materialType;
            switch (tag.getAsString()) {
                case "apotheosis:ancient" -> materialType = Adventure.Items.ANCIENT_MATERIAL.get();
                case "apotheosis:mythic" -> materialType = Adventure.Items.MYTHIC_MATERIAL.get();
                case "apotheosis:epic" -> materialType = Adventure.Items.EPIC_MATERIAL.get();
                case "apotheosis:rare" -> materialType = Adventure.Items.RARE_MATERIAL.get();
                case "apotheosis:uncommon" -> materialType = Adventure.Items.UNCOMMON_MATERIAL.get();
                default -> materialType = Adventure.Items.COMMON_MATERIAL.get();
            }
            outputs.add(materialType.getDefaultInstance());
            return true;
        }
        return false;
    }
}
