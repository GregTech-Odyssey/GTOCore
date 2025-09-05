package com.gtocore.common.machine.mana.multiblock;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gtocore.common.data.GTOItems;
import com.gtocore.data.record.ApotheosisAffix;
import com.gtocore.data.record.Enchantment;

import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.holder.IntHolder;
import com.gtolib.utils.holder.LongHolder;

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

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.gtocore.common.data.GTOItems.AFFIX_ESSENCE;
import static com.gtocore.common.data.GTOItems.ENCHANTMENT_ESSENCE;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;
import static net.minecraft.nbt.Tag.TAG_LIST;

public class ThePrimordialReconstructor extends ManaMultiblockMachine {

    public ThePrimordialReconstructor(MetaMachineBlockEntity holder) {
        super(holder);
    }

    /**
     * 通过电路判断拆解程度
     * 1 装备粉碎
     * 2 装备粉碎 + 附魔粉碎
     * 3 装备粉碎 + 刻印粉碎
     * 4 完全粉碎
     * 5 附魔书制作
     * 6 附魔书合并
     * 7 铭刻之布合成
     */
    private static int circuit = 0;

    private static final List<Enchantment.EnchantmentRecord> EnchantmentRecords = Enchantment.initializeEnchantmentRecords();
    private static final Enchantment enchantmentRegistry = new Enchantment(EnchantmentRecords);

    private static final List<ApotheosisAffix.ApotheosisAffixRecord> AffixRecords = ApotheosisAffix.initializeApotheosisAffixRecords();
    private static final ApotheosisAffix apotheosisAffixRegistry = new ApotheosisAffix(AffixRecords);

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    private Recipe getRecipe() {
        circuit = checkingCircuit(false);
        Recipe recipe = null;
        if (circuit == 1 || circuit == 2 || circuit == 3 || circuit == 4) recipe = getDisassembleRecipe();
        if (circuit == 5) recipe = getEnchantmentsLoadRecipe();
        if (circuit == 6) recipe = getEnchantedBooksMergeRecipe();
        if (circuit == 7) recipe = getAffixCanvasLoadRecipe();
        if (circuit == 8) recipe = getForcedEnchantmentRecipe();
        if (recipe != null) if (RecipeRunner.matchRecipe(this, recipe)) return recipe;
        return null;
    }

    /**
     * 构建装备分解配方
     */
    private Recipe getDisassembleRecipe() {
        RecipeBuilder disassembleRecipeBuilder = getRecipeBuilder();
        List<ItemStack> inputsItems = new ObjectArrayList<>();
        List<ItemStack> outputsItems = new ObjectArrayList<>();
        IntHolder count = new IntHolder(0);
        forEachInputItems(stack -> {
            var tags = stack.getItem().builtInRegistryHolder().tags;
            CompoundTag nbt = stack.getTag();
            if (nbt != null) {
                if (tags.contains(Tags.Items.TOOLS) || tags.contains(Tags.Items.ARMORS))
                    if (disassembleEquipment(nbt, inputsItems, outputsItems)) {
                        inputsItems.add(stack);
                        count.value++;
                    }
                if (circuit == 2 || circuit == 4)
                    if (stack.getItem().equals(Items.ENCHANTED_BOOK.asItem()))
                        if (disassembleEnchantments(nbt, outputsItems)) {
                            inputsItems.add(stack);
                            outputsItems.add(new ItemStack(Items.BOOK));
                            count.value++;
                        }
                if (circuit == 3 || circuit == 4)
                    if (stack.getItem().equals(GTOItems.AFFIX_CANVAS.asItem()))
                        if (disassembleAffixCanvas(nbt, outputsItems)) {
                            inputsItems.add(stack);
                            outputsItems.add(new ItemStack(GTOItems.AFFIX_CANVAS));
                            count.value++;
                        }
            }
            return false;
        });
        if (!outputsItems.isEmpty()) {
            inputsItems.forEach(disassembleRecipeBuilder::inputItems);
            outputsItems.forEach(disassembleRecipeBuilder::outputItems);
            disassembleRecipeBuilder.duration(20 * count.value);
            return disassembleRecipeBuilder.buildRawRecipe();
        }
        return null;
    }

    /**
     * 将Apotheosis装备分解为宝石、附魔书和材料
     *
     * @param nbt          要分解的装备的nbt
     * @param inputsItems  输入列表
     * @param outputsItems 输出列表
     */
    private static boolean disassembleEquipment(CompoundTag nbt, List<ItemStack> inputsItems, List<ItemStack> outputsItems) {
        boolean find = false;

        // 提取附魔
        if (circuit == 1 || circuit == 3) {
            if (extractEnchantments1(nbt, outputsItems)) {
                inputsItems.add(new ItemStack(Items.BOOK));
                find = true;
            }
        } else if (circuit == 2 || circuit == 4) {
            if (extractEnchantments2(nbt, outputsItems))
                find = true;
        }

        // 提取词缀
        if (circuit == 1 || circuit == 2) {
            if (extractAffix1(nbt, outputsItems)) {
                inputsItems.add(new ItemStack(GTOItems.AFFIX_CANVAS));
                find = true;
            }
        } else if (circuit == 3 || circuit == 4) {
            if (extractAffix2(nbt, outputsItems))
                find = true;
        }

        // 提取宝石
        if (extractGems(nbt, outputsItems)) {
            inputsItems.add(new ItemStack(Adventure.Items.SIGIL_OF_WITHDRAWAL.get()));
            find = true;
        }

        return generateMaterials(nbt, outputsItems) || find;
    }

    /**
     * 从装备NBT中提取所有附魔并创建一本包含所有附魔的附魔书
     *
     * @param nbt          装备的NBT数据
     * @param outputsItems 输出列表
     * @return 是否成功提取
     */
    private static boolean extractEnchantments1(CompoundTag nbt, List<ItemStack> outputsItems) {
        if (nbt.tags.get("Enchantments") instanceof ListTag enchantments && !enchantments.isEmpty()) {
            int enchantmentCount = enchantments.size();

            ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
            CompoundTag bookTag = new CompoundTag();
            ListTag storedEnchantments = new ListTag();

            for (int i = 0; i < enchantmentCount; i++) {
                CompoundTag enchantment = enchantments.getCompound(i);
                storedEnchantments.add(enchantment.copy());
            }

            bookTag.put("StoredEnchantments", storedEnchantments);
            enchantedBook.setTag(bookTag);

            outputsItems.add(enchantedBook);

            return true;
        }
        return false;
    }

    /**
     * 从装备NBT中提取所有附魔并输出附魔精粹
     *
     * @param nbt          装备的NBT数据
     * @param outputsItems 输出列表
     * @return 提取到的附魔数量
     */
    private static boolean extractEnchantments2(CompoundTag nbt, List<ItemStack> outputsItems) {
        if (nbt.tags.get("Enchantments") instanceof ListTag enchantments && !enchantments.isEmpty()) {
            int enchantmentCount = enchantments.size();
            for (int i = 0; i < enchantmentCount; i++) {
                CompoundTag enchantment = enchantments.getCompound(i);
                int id = enchantmentRegistry.getSerialNumberByEnchantmentId(enchantment.getString("id"));
                int lvl = 1 << (enchantment.getInt("lvl") - 1);
                outputsItems.add(new ItemStack(ENCHANTMENT_ESSENCE[id], lvl));
            }
            return true;
        }
        return false;
    }

    /**
     * 从装备NBT中提取宝石
     *
     * @param nbt          装备的NBT数据
     * @param outputsItems 输出列表
     * @return 提取的宝石数量
     */
    private static boolean extractGems(CompoundTag nbt, List<ItemStack> outputsItems) {
        if (nbt.tags.get("affix_data") instanceof CompoundTag data && data.tags.get("gems") instanceof ListTag gems) {
            for (int i = 0; i < gems.size(); i++) {
                CompoundTag gemData = gems.getCompound(i);
                ItemStack gemStack = Adventure.Items.GEM.get().getDefaultInstance();
                if (gemData.tags.get("tag") instanceof CompoundTag tag) {
                    gemStack.setTag(tag.copy());
                }
                outputsItems.add(gemStack);
            }
            return true;
        }
        return false;
    }

    /**
     * 从NBT中提取词缀并应用到铭刻之布
     * 
     * @param nbt          完整的NBT数据
     * @param outputsItems 接收词缀的物品列表
     * @return 是否成功提取
     */
    private static boolean extractAffix1(CompoundTag nbt, List<ItemStack> outputsItems) {
        if (nbt.contains("affix_data", TAG_COMPOUND)) {
            CompoundTag affixData = nbt.getCompound("affix_data");

            if (affixData.contains("affixes", TAG_COMPOUND)) {
                CompoundTag affixes = affixData.getCompound("affixes");

                Set<String> affixKeys = affixes.getAllKeys();

                ItemStack affixCanvas = new ItemStack(GTOItems.AFFIX_CANVAS);
                CompoundTag affixTag = new CompoundTag();
                ListTag affixList = new ListTag();

                for (String affixKey : affixKeys) {
                    CompoundTag affixEntry = new CompoundTag();
                    affixEntry.putString("id", affixKey);
                    affixList.add(affixEntry);
                }

                affixTag.put("affix_list", affixList);
                affixCanvas.setTag(affixTag);

                outputsItems.add(affixCanvas);

                return true;
            }
        }
        return false;
    }

    /**
     * 从NBT中提取词缀并输出为刻印精粹
     *
     * @param nbt          完整的NBT数据
     * @param outputsItems 接收词缀的物品列表
     * @return 是否成功提取
     */
    private static boolean extractAffix2(CompoundTag nbt, List<ItemStack> outputsItems) {
        if (nbt.contains("affix_data", TAG_COMPOUND)) {
            CompoundTag affixData = nbt.getCompound("affix_data");
            if (affixData.contains("affixes", TAG_COMPOUND)) {
                CompoundTag affixes = affixData.getCompound("affixes");
                for (String affixKey : affixes.getAllKeys()) {
                    int id = apotheosisAffixRegistry.getSerialNumberByApotheosisAffixId(affixKey);
                    outputsItems.add(new ItemStack(AFFIX_ESSENCE[id]));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 根据装备稀有度生成材料
     *
     * @param nbt          装备的NBT数据
     * @param outputsItems 输出列表
     */
    private static boolean generateMaterials(CompoundTag nbt, List<ItemStack> outputsItems) {
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
            outputsItems.add(new ItemStack(materialType, 2));
            return true;
        }
        return false;
    }

    /**
     * 将附魔书分解为附魔精粹
     *
     * @param nbt          要分解的附魔书的nbt
     * @param outputsItems 输出列表
     */
    private static boolean disassembleEnchantments(CompoundTag nbt, List<ItemStack> outputsItems) {
        if (nbt.tags.get("StoredEnchantments") instanceof ListTag enchantments && !enchantments.isEmpty()) {
            int enchantmentCount = enchantments.size();
            for (int i = 0; i < enchantmentCount; i++) {
                CompoundTag enchantment = enchantments.getCompound(i);
                int id = enchantmentRegistry.getSerialNumberByEnchantmentId(enchantment.getString("id"));
                int lvl = 1 << (enchantment.getInt("lvl") - 1);
                outputsItems.add(new ItemStack(ENCHANTMENT_ESSENCE[id], lvl));
            }
            return true;
        }
        return false;
    }

    /**
     * 将铭刻之布分解为刻印精粹
     *
     * @param nbt          要分解的铭刻之布的nbt
     * @param outputsItems 输出列表
     */
    private static boolean disassembleAffixCanvas(CompoundTag nbt, List<ItemStack> outputsItems) {
        if (nbt.tags.get("affix_list") instanceof ListTag affixes && !affixes.isEmpty()) {
            int affixCount = affixes.size();
            for (int i = 0; i < affixCount; i++) {
                CompoundTag affix = affixes.getCompound(i);
                int id = apotheosisAffixRegistry.getSerialNumberByApotheosisAffixId(affix.getString("id"));
                outputsItems.add(new ItemStack(AFFIX_ESSENCE[id]));
            }
            return true;
        }
        return false;
    }

    /**
     * 根据获取的字符串获取最后一个 _ 后的数字
     */
    private static int extractNumber(String text) {
        return Integer.parseInt(text.substring(text.lastIndexOf('_') + 1));
    }

    /**
     * 根据获取的字符串获取最后一个 _ 前的字符串
     */
    private static String getPrefix(String text) {
        int lastUnderscoreIndex = text.lastIndexOf('_');
        if (lastUnderscoreIndex == -1) return text;
        return text.substring(0, lastUnderscoreIndex);
    }

    /**
     * 附魔精粹合成附魔书配方
     */
    private Recipe getEnchantmentsLoadRecipe() {
        RecipeBuilder enchantmentsLoadRecipeBuilder = getRecipeBuilder();
        AtomicReference<Item> essence = new AtomicReference<>();
        LongHolder count = new LongHolder(0);

        forEachInputItems(stack -> {
            Item stackItem = stack.getItem();
            if (essence.get() == null)
                if (getPrefix(stackItem.toString()).equals("enchantment_essence"))
                    essence.set(stackItem);
            if (essence.get() != null && essence.get().equals(stackItem))
                count.value += stack.getCount();
            return false;
        });

        int lvl = 64 - Long.numberOfLeadingZeros(count.value);
        if (essence.get() != null && lvl > 0) {

            ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
            CompoundTag bookTag = enchantedBook.getOrCreateTag();
            ListTag storedEnchantments = bookTag.getList("StoredEnchantments", TAG_COMPOUND);
            CompoundTag enchantTag = new CompoundTag();
            enchantTag.putString("id", enchantmentRegistry.getEnchantmentIdBySerialNumber(extractNumber(essence.get().toString())));
            enchantTag.putShort("lvl", (short) lvl);
            storedEnchantments.add(enchantTag);
            bookTag.put("StoredEnchantments", storedEnchantments);
            enchantedBook.setTag(bookTag);

            enchantmentsLoadRecipeBuilder.inputItems(Items.BOOK);
            enchantmentsLoadRecipeBuilder.inputItems(essence.get(), 1 << (lvl - 1));
            enchantmentsLoadRecipeBuilder.outputItems(enchantedBook);
            enchantmentsLoadRecipeBuilder.duration(20);
            enchantmentsLoadRecipeBuilder.MANAt(256);
            return enchantmentsLoadRecipeBuilder.buildRawRecipe();
        }

        return null;
    }

    /**
     * 构建附魔书合并配方
     */
    private Recipe getEnchantedBooksMergeRecipe() {
        RecipeBuilder mergeRecipeBuilder = getRecipeBuilder();

        // 存储所有附魔信息 (附魔ID, 等级)
        List<Map.Entry<String, Integer>> allEnchantments = new ArrayList<>();
        IntHolder totalBooks = new IntHolder(0);

        // 遍历输入物品，收集所有附魔书中的附魔信息
        forEachInputItems(stack -> {
            if (stack.getItem() == Items.ENCHANTED_BOOK) {
                totalBooks.value++;

                // 提取附魔信息
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.contains("StoredEnchantments", TAG_LIST)) {
                    ListTag enchantmentsList = tag.getList("StoredEnchantments", TAG_COMPOUND);
                    for (int i = 0; i < enchantmentsList.size(); i++) {
                        CompoundTag enchantTag = enchantmentsList.getCompound(i);
                        String enchantId = enchantTag.getString("id");
                        int level = enchantTag.getShort("lvl");
                        // 添加到所有附魔列表中
                        allEnchantments.add(new AbstractMap.SimpleEntry<>(enchantId, level));
                    }
                    mergeRecipeBuilder.inputItems(stack);
                }
            }
            return false;
        });

        // 如果没有附魔书或附魔，返回null
        if (totalBooks.value < 2 || allEnchantments.isEmpty()) return null;

        // 反复合并相同附魔的相同等级
        boolean changed;
        do {
            changed = false;
            Map<String, Map<Integer, Integer>> enchantmentLevelCounts = new HashMap<>();

            // 统计每种附魔每个等级的数量
            for (Map.Entry<String, Integer> enchantment : allEnchantments) {
                String enchantId = enchantment.getKey();
                int level = enchantment.getValue();

                enchantmentLevelCounts
                        .computeIfAbsent(enchantId, k -> new HashMap<>())
                        .merge(level, 1, Integer::sum);
            }

            // 清空原列表，准备重新添加合并后的附魔
            allEnchantments.clear();

            // 处理每种附魔
            for (Map.Entry<String, Map<Integer, Integer>> enchantEntry : enchantmentLevelCounts.entrySet()) {
                String enchantId = enchantEntry.getKey();
                Map<Integer, Integer> levelCounts = enchantEntry.getValue();

                for (Map.Entry<Integer, Integer> levelEntry : levelCounts.entrySet()) {
                    int level = levelEntry.getKey();
                    int count = levelEntry.getValue();

                    // 如果有两个或以上相同等级的相同附魔，合并为更高等级
                    if (count >= 2) {
                        int pairs = count / 2;
                        int remainder = count % 2;

                        // 添加合并后的更高等级附魔
                        for (int i = 0; i < pairs; i++) {
                            allEnchantments.add(new AbstractMap.SimpleEntry<>(enchantId, level + 1));
                            changed = true; // 标记有变化，需要再次遍历
                        }

                        // 添加剩余的附魔
                        if (remainder > 0) {
                            allEnchantments.add(new AbstractMap.SimpleEntry<>(enchantId, level));
                        }
                    } else {
                        // 数量不足2个，直接添加
                        allEnchantments.add(new AbstractMap.SimpleEntry<>(enchantId, level));
                    }
                }
            }
        } while (changed); // 如果有合并发生，继续遍历直到无法再合并

        // 将合并后的附魔分配到附魔书中
        List<ItemStack> outputBooks = new ArrayList<>();
        List<Map.Entry<String, Integer>> remainingEnchantments = new ArrayList<>(allEnchantments);

        // 循环创建附魔书，直到所有附魔都被分配
        while (!remainingEnchantments.isEmpty()) {
            ItemStack outputBook = new ItemStack(Items.ENCHANTED_BOOK);
            CompoundTag bookTag = outputBook.getOrCreateTag();
            ListTag storedEnchantments = new ListTag();

            // 当前书已添加的附魔ID集合，确保同种附魔不会重复添加
            Set<String> addedEnchantments = new HashSet<>();

            // 遍历剩余附魔，添加到当前书中
            Iterator<Map.Entry<String, Integer>> iterator = remainingEnchantments.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> enchantment = iterator.next();
                String enchantId = enchantment.getKey();

                // 如果当前书还没有这种附魔，则添加
                if (!addedEnchantments.contains(enchantId)) {
                    CompoundTag enchantTag = new CompoundTag();
                    enchantTag.putString("id", enchantId);
                    enchantTag.putShort("lvl", enchantment.getValue().shortValue());
                    storedEnchantments.add(enchantTag);
                    addedEnchantments.add(enchantId);
                    iterator.remove(); // 从剩余列表中移除
                }
            }

            bookTag.put("StoredEnchantments", storedEnchantments);
            outputBook.setTag(bookTag);
            outputBooks.add(outputBook);
        }

        for (ItemStack outputBookItem : outputBooks) {
            mergeRecipeBuilder.outputItems(outputBookItem);
        }

        int remainingBooks = totalBooks.value - outputBooks.size();
        if (remainingBooks > 0) mergeRecipeBuilder.outputItems(Items.BOOK, remainingBooks);
        else if (remainingBooks < 0) mergeRecipeBuilder.inputItems(Items.BOOK, -remainingBooks);

        mergeRecipeBuilder.duration(10 * totalBooks.value);
        mergeRecipeBuilder.MANAt(512);

        return mergeRecipeBuilder.buildRawRecipe();
    }

    /**
     * 刻印精粹合成铭刻之布配方
     */
    private Recipe getAffixCanvasLoadRecipe() {
        RecipeBuilder affixCanvasLoadRecipeBuilder = getRecipeBuilder();

        Set<Item> uniqueItems = new HashSet<>();
        forEachInputItems(stack -> {
            Item stackItem = stack.getItem();
            if (getPrefix(stackItem.toString()).equals("affix_essence")) uniqueItems.add(stackItem);
            return false;
        });
        if (uniqueItems.isEmpty()) return null;

        ItemStack affixCanvas = new ItemStack(GTOItems.AFFIX_CANVAS);
        CompoundTag affixTag = new CompoundTag();
        ListTag affixList = new ListTag();
        for (Item item : uniqueItems) {
            CompoundTag affixEntry = new CompoundTag();
            affixEntry.putString("id", apotheosisAffixRegistry.getApotheosisAffixIdBySerialNumber(extractNumber(item.toString())));
            affixList.add(affixEntry);
            affixCanvasLoadRecipeBuilder.inputItems(item);
        }
        affixTag.put("affix_list", affixList);
        affixCanvas.setTag(affixTag);

        affixCanvasLoadRecipeBuilder.inputItems(GTOItems.AFFIX_CANVAS);
        affixCanvasLoadRecipeBuilder.outputItems(affixCanvas);
        affixCanvasLoadRecipeBuilder.duration(10 * uniqueItems.size());
        affixCanvasLoadRecipeBuilder.MANAt(512);

        return affixCanvasLoadRecipeBuilder.buildRawRecipe();
    }

    /**
     * 强行为物品添加附魔
     */
    private Recipe getForcedEnchantmentRecipe() {
        RecipeBuilder forcedEnchantmentRecipeBuilder = getRecipeBuilder();

        AtomicReference<ItemStack> EnchantedBook = new AtomicReference<>();
        AtomicReference<ItemStack> NonEnchantedItem = new AtomicReference<>();
        forEachInputItems(stack -> {
            if (EnchantedBook.get() == null && stack.getItem() == Items.ENCHANTED_BOOK) {
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.contains("StoredEnchantments", TAG_LIST)) EnchantedBook.set(stack);
                return false;
            }
            if (NonEnchantedItem.get() == null && stack.getItem() != Items.ENCHANTED_BOOK && stack.getItem() != GTItems.PROGRAMMED_CIRCUIT.asItem()) {
                NonEnchantedItem.set(stack);
                return false;
            }
            return EnchantedBook.get() != null && NonEnchantedItem.get() != null;
        });
        if (EnchantedBook.get() == null || NonEnchantedItem.get() == null) return null;

        EnchantedBook.get().setCount(1);
        ItemStack inputBook = EnchantedBook.get();
        NonEnchantedItem.get().setCount(1);
        ItemStack inputItem = NonEnchantedItem.get();

        forcedEnchantmentRecipeBuilder.inputItems(inputBook);
        forcedEnchantmentRecipeBuilder.inputItems(inputItem);

        CompoundTag bookTag = inputBook.getTag();
        ListTag enchantmentsList = bookTag.getList("StoredEnchantments", 10);

        CompoundTag targetTag = inputItem.getOrCreateTag();
        ListTag targetEnchantments;
        if (targetTag.contains("Enchantments", 9)) targetEnchantments = targetTag.getList("Enchantments", 10);
        else targetEnchantments = new ListTag();
        for (int i = 0; i < enchantmentsList.size(); i++) {
            CompoundTag enchantmentTag = enchantmentsList.getCompound(i);
            targetEnchantments.add(enchantmentTag);
        }
        targetTag.put("Enchantments", targetEnchantments);
        inputItem.setTag(targetTag);

        forcedEnchantmentRecipeBuilder.outputItems(inputItem);
        forcedEnchantmentRecipeBuilder.duration(20);
        forcedEnchantmentRecipeBuilder.MANAt(512);

        return forcedEnchantmentRecipeBuilder.buildRawRecipe();
    }
}
