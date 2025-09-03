package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class GearDeconstructionCenter extends ManaMultiblockMachine {

    public GearDeconstructionCenter(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean keepSubscribing() {
        return isFormed() && getLevel() != null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    private Recipe getRecipe() {
        Level level = getLevel();
        if (level == null || !isFormed()) {
            return null;
        }

        // 使用原子引用来捕获构建的配方
        AtomicReference<Recipe> resultRecipe = new AtomicReference<>(null);
        AtomicBoolean foundValidInput = new AtomicBoolean(false);

        // 创建新的配方构建器
        RecipeBuilder recipeBuilder = getRecipeBuilder();

        forEachInputItems(stack -> {
            if (foundValidInput.get())
                return false; // 已经找到有效输入并处理，停止迭代

            ITagManager<Item> tagManager = ForgeRegistries.ITEMS.tags();
            if (tagManager == null)
                return false;

            // 检测标签，工具和 armor 不处理
            if (!tagManager.getTag(Tags.Items.TOOLS).contains(stack.getItem()) &&
                    !tagManager.getTag(Tags.Items.ARMORS).contains(stack.getItem()))
                return false;

            // 处理有效的装备分解
            List<ItemStack> inputs = new ArrayList<>();
            List<ItemStack> outputs = new ArrayList<>();
            disassembleEquipment(stack, inputs, outputs);

            // 如果有有效的输出，构建配方
            if (!outputs.isEmpty()) {
                for (ItemStack input : inputs) recipeBuilder.inputItems(input);
                for (ItemStack output : outputs) recipeBuilder.outputItems(output);
                recipeBuilder.duration(20);
                resultRecipe.set(recipeBuilder.buildRawRecipe());
                foundValidInput.set(true);
                return true;
            }

            return false;
        });

        return resultRecipe.get();
    }

    /**
     * 将Apotheosis装备分解为宝石、附魔书和材料
     *
     * @param equipment 要分解的装备
     * @param inputs    输入列表
     * @param outputs   输出列表
     */
    private void disassembleEquipment(ItemStack equipment, List<ItemStack> inputs, List<ItemStack> outputs) {
        // 添加装备本身到输入列表
        inputs.add(equipment.copy());

        // 检查输入是否有效
        if (equipment.isEmpty()) {
            return;
        }

        CompoundTag nbt = equipment.getTag();
        if (nbt == null) {
            return;
        }

        // 提取宝石并计算数量
        int gemCount = extractGems(nbt, outputs);
        if (gemCount > 0) {
            ItemStack sigilStack = RegistriesUtils.getItemStack("apotheosis:sigil_of_withdrawal", gemCount);
            inputs.add(sigilStack);
        }

        // 提取附魔并创建附魔书，计算数量
        int enchantmentCount = extractEnchantments(nbt, outputs);
        if (enchantmentCount > 0) {
            ItemStack bookStack = new ItemStack(Items.BOOK, enchantmentCount);
            inputs.add(bookStack);
        }

        // 根据稀有度生成材料
        generateMaterials(nbt, outputs);
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

        if (nbt.contains("affix_data", Tag.TAG_COMPOUND)) {
            CompoundTag affixData = nbt.getCompound("affix_data");
            if (affixData.contains("gems", Tag.TAG_LIST)) {
                ListTag gems = affixData.getList("gems", Tag.TAG_COMPOUND);
                gemCount = gems.size();

                for (int i = 0; i < gemCount; i++) {
                    CompoundTag gemData = gems.getCompound(i);

                    // 创建宝石物品堆
                    ItemStack gemStack = RegistriesUtils.getItemStack("apotheosis:gem");

                    // 如果宝石有自定义NBT，则复制
                    if (gemData.contains("tag", Tag.TAG_COMPOUND))
                        gemStack.setTag(gemData.getCompound("tag").copy());

                    outputs.add(gemStack);
                }
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
        if (nbt.contains("Enchantments", Tag.TAG_LIST)) {
            ListTag enchantments = nbt.getList("Enchantments", Tag.TAG_COMPOUND);
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
    private static void generateMaterials(CompoundTag nbt, List<ItemStack> outputs) {
        String materialType = "apotheosis:common_material";

        // 确定材料类型基于装备稀有度
        if (nbt.contains("affix_data", Tag.TAG_COMPOUND)) {
            CompoundTag affixData = nbt.getCompound("affix_data");
            if (affixData.contains("rarity", Tag.TAG_STRING)) {
                String rarity = affixData.getString("rarity");

                switch (rarity) {
                    case "apotheosis:ancient" -> materialType = "apotheosis:ancient_material";
                    case "apotheosis:mythic" -> materialType = "apotheosis:mythic_material";
                    case "apotheosis:epic" -> materialType = "apotheosis:epic_material";
                    case "apotheosis:rare" -> materialType = "apotheosis:rare_material";
                    case "apotheosis:uncommon" -> materialType = "apotheosis:uncommon_material";
                    case "apotheosis:common" -> materialType = "apotheosis:common_material";
                }
            }
        }

        ItemStack materialStack = RegistriesUtils.getItemStack(materialType, 2);
        outputs.add(materialStack);
    }
}
