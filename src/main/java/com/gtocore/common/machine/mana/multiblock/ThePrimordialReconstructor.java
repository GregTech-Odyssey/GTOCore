package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.common.data.GTOItems;
import com.gtocore.data.record.ApotheosisAffix;
import com.gtocore.data.record.Enchantment;

import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.holder.IntHolder;
import com.gtolib.utils.holder.LongHolder;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.utils.collection.O2IOpenCacheHashMap;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;
import com.gregtechceu.gtceu.utils.collection.OpenCacheHashSet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import dev.shadowsoffire.apotheosis.adventure.Adventure;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.gtocore.common.data.GTOItems.AFFIX_ESSENCE;
import static com.gtocore.common.data.GTOItems.ENCHANTMENT_ESSENCE;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;
import static net.minecraft.nbt.Tag.TAG_LIST;

public class ThePrimordialReconstructor extends ManaMultiblockMachine {

    public ThePrimordialReconstructor(MetaMachineBlockEntity holder) {
        super(holder);
    }

    /**
     * 通过电路判断运行逻辑
     * 1 物品解构
     * 2 物品解构 + 附魔粉碎
     * 3 物品解构 + 刻印粉碎
     * 4 完全粉碎
     * 5 附魔书制作
     * 6 附魔书合并
     * 7 铭刻之布合成
     * 8 宝石合成
     * 9 强行附魔给予
     * 10 强行刻印给予
     * 11 强行修改稀有度
     * 12 强行增加镶孔
     * 13 强行镶嵌宝石
     */
    private static int circuit = 0;

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.model", circuit));
        textList.add(Component.translatable("gtocore.machine.the_primordial_reconstructor.mode." + circuit));
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    private Recipe getRecipe() {
        circuit = checkingCircuit(false);
        Recipe recipe = null;
        switch (circuit) {
            case 1, 2, 3, 4 -> recipe = getDisassembleRecipe();
            case 5 -> recipe = getEnchantmentsLoadRecipe();
            case 6 -> recipe = getEnchantedBooksMergeRecipe();
            case 7 -> recipe = getAffixCanvasLoadRecipe();
            case 8 -> recipe = getGemSynthesisRecipe();
            case 9 -> recipe = getForcedEnchantmentRecipe();
            case 10 -> recipe = getForcedAffixRecipe();
            case 11 -> recipe = getForcedRarityUpRecipe();
            case 12 -> recipe = getForcedAddSocketRecipe();
            case 13 -> recipe = getForcedMosaicGemRecipe();
        }
        if (recipe != null) if (RecipeRunner.matchRecipe(this, recipe)) return recipe;
        return null;
    }

    /**
     * 构建物品解构配方
     */
    private Recipe getDisassembleRecipe() {
        RecipeBuilder disassembleRecipeBuilder = getRecipeBuilder();
        List<ItemStack> inputsItems = new ObjectArrayList<>();
        List<ItemStack> outputsItems = new ObjectArrayList<>();
        IntHolder count = new IntHolder(0);
        forEachInputItems(stack -> {
            CompoundTag nbt = stack.getTag();
            if (nbt != null) {
                if (nbt.contains("affix_data") || nbt.contains("Enchantments")) {
                    if (disassembleEquipment(nbt, inputsItems, outputsItems)) {
                        inputsItems.add(stack);
                        count.value++;
                    }
                } else if (circuit == 4 && nbt.contains("Damage")) {
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
        if (!inputsItems.isEmpty() || !outputsItems.isEmpty()) {
            inputsItems.forEach(disassembleRecipeBuilder::inputItems);
            outputsItems.forEach(disassembleRecipeBuilder::outputItems);
            disassembleRecipeBuilder.duration(count.value);
            return disassembleRecipeBuilder.buildRawRecipe();
        }
        return null;
    }

    /**
     * 将物品解构为宝石、附魔书和材料
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

        // 电路4强行粉碎
        if (circuit == 4) find = true;

        return generateMaterials(nbt, outputsItems) || find;
    }

    /**
     * 从NBT中提取所有附魔并创建一本包含所有附魔的附魔书
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
     * 从NBT中提取所有附魔并输出附魔精粹
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
                int id = Enchantment.getSerialNumberByEnchantmentId(enchantment.getString("id"));
                int lvl = 1 << (enchantment.getInt("lvl") - 1);
                outputsItems.add(new ItemStack(ENCHANTMENT_ESSENCE[id], lvl));
            }
            return true;
        }
        return false;
    }

    /**
     * 从NBT中提取宝石
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
                    int id = ApotheosisAffix.getSerialNumberByApotheosisAffixId(affixKey);
                    outputsItems.add(new ItemStack(AFFIX_ESSENCE[id]));
                }
                return true;
            }
        }
        return false;
    }

    // 使用Map存储稀有度与材料的对应关系
    private static final Map<String, Item> RARITY_MATERIAL_MAP = Map.of(
            "apotheosis:ancient", Adventure.Items.ANCIENT_MATERIAL.get(),
            "apotheosis:mythic", Adventure.Items.MYTHIC_MATERIAL.get(),
            "apotheosis:epic", Adventure.Items.EPIC_MATERIAL.get(),
            "apotheosis:rare", Adventure.Items.RARE_MATERIAL.get(),
            "apotheosis:uncommon", Adventure.Items.UNCOMMON_MATERIAL.get(),
            "apotheosis:common", Adventure.Items.COMMON_MATERIAL.get());

    /**
     * 根据稀有度生成材料
     *
     * @param nbt          装备的NBT数据
     * @param outputsItems 输出列表
     */
    private static boolean generateMaterials(CompoundTag nbt, List<ItemStack> outputsItems) {
        // 确定材料类型基于装备稀有度
        if (nbt.tags.get("affix_data") instanceof CompoundTag data && data.tags.get("rarity") instanceof StringTag tag) {
            Item materialType = RARITY_MATERIAL_MAP.getOrDefault(tag.getAsString(), RARITY_MATERIAL_MAP.get("default"));
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
                int id = Enchantment.getSerialNumberByEnchantmentId(enchantment.getString("id"));
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
                int id = ApotheosisAffix.getSerialNumberByApotheosisAffixId(affix.getString("id"));
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
        ObjectHolder<Item> essence = new ObjectHolder<>(null);
        LongHolder count = new LongHolder(0);

        forEachInputItems(stack -> {
            Item stackItem = stack.getItem();
            if (essence.value == null)
                if (getPrefix(stackItem.toString()).equals("enchantment_essence"))
                    essence.value = stackItem;
            if (essence.value != null && essence.value.equals(stackItem))
                count.value += stack.getCount();
            return false;
        });

        int lvl = 64 - Long.numberOfLeadingZeros(count.value);
        if (essence.value != null && lvl > 0) {
            String enchantment = Enchantment.getEnchantmentIdBySerialNumber(extractNumber(essence.value.toString()));

            enchantmentsLoadRecipeBuilder.inputItems(Items.BOOK);
            enchantmentsLoadRecipeBuilder.inputItems(essence.value, 1 << (lvl - 1));
            enchantmentsLoadRecipeBuilder.outputItems(Enchantment.getEnchantedBookByEnchantmentId(enchantment, (short) lvl));
            enchantmentsLoadRecipeBuilder.duration(lvl);
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
        List<Object2IntMap.Entry<String>> allEnchantments = new ArrayList<>();
        IntHolder totalBooks = new IntHolder(0);
        // 遍历输入物品，收集所有附魔书中的附魔信息
        forEachInputItems(stack -> {
            if (stack.getItem() == Items.ENCHANTED_BOOK) {
                totalBooks.value++;
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.contains("StoredEnchantments", TAG_LIST)) {
                    ListTag enchantmentsList = tag.getList("StoredEnchantments", TAG_COMPOUND);
                    for (int i = 0; i < enchantmentsList.size(); i++) {
                        CompoundTag enchantTag = enchantmentsList.getCompound(i);
                        String enchantId = enchantTag.getString("id");
                        int level = enchantTag.getShort("lvl");
                        allEnchantments.add(new AbstractObject2IntMap.BasicEntry<>(enchantId, level));
                    }
                    mergeRecipeBuilder.inputItems(stack);
                }
            }
            return false;
        });

        if (totalBooks.value < 2 || allEnchantments.isEmpty()) return null;
        // 反复合并相同附魔的相同等级
        boolean changed;
        do {
            changed = false;
            Object2ObjectOpenHashMap<String, Int2IntMap> enchantmentLevelCounts = new O2OOpenCacheHashMap<>();
            // 统计每种附魔每个等级的数量
            for (Object2IntMap.Entry<String> entry : allEnchantments) {
                String enchantId = entry.getKey();
                int level = entry.getIntValue();
                // 获取或创建内层Map
                Int2IntMap levelCounts = enchantmentLevelCounts.computeIfAbsent(enchantId, k -> new Int2IntOpenHashMap());
                // 增加该等级的数量
                levelCounts.put(level, levelCounts.getOrDefault(level, 0) + 1);
            }
            // 清空原列表，准备重新添加合并后的附魔
            allEnchantments.clear();
            // 处理每种附魔
            for (Object2ObjectMap.Entry<String, Int2IntMap> enchantEntry : enchantmentLevelCounts.object2ObjectEntrySet()) {
                String enchantId = enchantEntry.getKey();
                Int2IntMap levelCounts = enchantEntry.getValue();
                for (Int2IntMap.Entry levelEntry : levelCounts.int2IntEntrySet()) {
                    int level = levelEntry.getIntKey();
                    int count = levelEntry.getIntValue();
                    // 如果有两个或以上相同等级的相同附魔，合并为更高等级
                    if (count >= 2) {
                        int pairs = count / 2;
                        int remainder = count % 2;
                        // 添加合并后的更高等级附魔
                        for (int i = 0; i < pairs; i++) {
                            allEnchantments.add(new AbstractObject2IntMap.BasicEntry<>(enchantId, level + 1));
                            changed = true; // 标记有变化，需要再次遍历
                        }
                        // 添加剩余的附魔
                        if (remainder > 0) {
                            allEnchantments.add(new AbstractObject2IntMap.BasicEntry<>(enchantId, level));
                        }
                    } else {
                        // 数量不足2个，直接添加
                        allEnchantments.add(new AbstractObject2IntMap.BasicEntry<>(enchantId, level));
                    }
                }
            }
        } while (changed); // 如果有合并发生，继续遍历直到无法再合并

        // 后续输出逻辑保持不变
        List<ItemStack> outputBooks = new ArrayList<>();
        List<Object2IntMap.Entry<String>> remainingEnchantments = new ArrayList<>(allEnchantments);

        while (!remainingEnchantments.isEmpty()) {
            ItemStack outputBook = new ItemStack(Items.ENCHANTED_BOOK);
            CompoundTag bookTag = outputBook.getOrCreateTag();
            ListTag storedEnchantments = new ListTag();
            Set<String> addedEnchantments = new OpenCacheHashSet<>();
            // 遍历剩余附魔，添加到当前书中
            Iterator<Object2IntMap.Entry<String>> iterator = remainingEnchantments.iterator();
            while (iterator.hasNext()) {
                Object2IntMap.Entry<String> enchantment = iterator.next();
                String enchantId = enchantment.getKey();
                if (!addedEnchantments.contains(enchantId)) {
                    CompoundTag enchantTag = new CompoundTag();
                    enchantTag.putString("id", enchantId);
                    enchantTag.putShort("lvl", (short) enchantment.getIntValue());
                    storedEnchantments.add(enchantTag);
                    addedEnchantments.add(enchantId);
                    iterator.remove();
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
        if (remainingBooks > 0) {
            mergeRecipeBuilder.outputItems(Items.BOOK, remainingBooks);
        } else if (remainingBooks < 0) {
            mergeRecipeBuilder.inputItems(Items.BOOK, -remainingBooks);
        }

        mergeRecipeBuilder.duration(totalBooks.value);
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
            affixEntry.putString("id", ApotheosisAffix.getApotheosisAffixIdBySerialNumber(extractNumber(item.toString())));
            affixList.add(affixEntry);
            affixCanvasLoadRecipeBuilder.inputItems(item);
        }
        affixTag.put("affix_list", affixList);
        affixCanvas.setTag(affixTag);

        affixCanvasLoadRecipeBuilder.inputItems(GTOItems.AFFIX_CANVAS);
        affixCanvasLoadRecipeBuilder.outputItems(affixCanvas);
        affixCanvasLoadRecipeBuilder.duration(uniqueItems.size());
        affixCanvasLoadRecipeBuilder.MANAt(512);

        return affixCanvasLoadRecipeBuilder.buildRawRecipe();
    }

    /**
     * 宝石合成
     */
    private Recipe getGemSynthesisRecipe() {
        RecipeBuilder GemSynthesisRecipeBuilder = getRecipeBuilder();

        ObjectArrayList<ItemStack> inputsGems = new ObjectArrayList<>();
        forEachInputItems(stack -> {
            if (stack.getItem() == Adventure.Items.GEM.get()) {
                inputsGems.add(stack);
            }
            return false;
        });
        if (inputsGems.isEmpty()) return null;

        Object2IntOpenHashMap<CompoundTag> nbtCountMap = new O2IOpenCacheHashMap<>();
        // 计算每个唯一NBT的总数量
        for (ItemStack stack : inputsGems) {
            CompoundTag nbt = stack.getTag() != null ? stack.getTag() : new CompoundTag();
            int count = stack.getCount();
            nbtCountMap.addTo(nbt, count);
        }
        // 创建合并后的堆叠列表
        ObjectArrayList<ItemStack> mergedGems = new ObjectArrayList<>();
        for (Object2IntMap.Entry<CompoundTag> entry : nbtCountMap.object2IntEntrySet()) {
            ItemStack mergedStack = new ItemStack(Adventure.Items.GEM.get(), entry.getIntValue());
            CompoundTag nbt = entry.getKey();
            if (!nbt.isEmpty()) mergedStack.setTag(nbt.copy());
            mergedGems.add(mergedStack);
        }
        // 将所有奇数的ItemStack数量减1变为偶数
        Iterator<ItemStack> iterator = mergedGems.iterator();
        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();
            int count = stack.getCount();
            if (count % 2 != 0) {
                if (count > 1) stack.setCount(count - 1);
                else iterator.remove();
            }
        }
        if (mergedGems.isEmpty()) return null;

        // 根据稀有度将宝石分配到不同的列表中
        @SuppressWarnings("unchecked")
        ObjectArrayList<ItemStack>[] gemsByRarity = new ObjectArrayList[5];
        for (int i = 0; i < 5; i++) gemsByRarity[i] = new ObjectArrayList<>();
        for (ItemStack gem : mergedGems) {
            String rarity = getGemRarity(gem);
            if ("apotheosis:ancient".equals(rarity)) continue;
            for (int i = 0; i < RARITIES.length; i++) {
                if (RARITIES[i].equals(rarity)) {
                    gemsByRarity[i].add(gem);
                    break;
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (!gemsByRarity[i].isEmpty()) break;
            if (i == 4) return null;
        }
        int timeMultiplier = 0;
        for (int i = 0; i < 5; i++) {
            ObjectArrayList<ItemStack> gems = gemsByRarity[i];
            if (gems.isEmpty()) continue;
            for (ItemStack gem : gems) {
                int count = gem.getCount() / 2;
                Item materialType = RARITY_MATERIAL_MAP.getOrDefault(RARITIES[i], RARITY_MATERIAL_MAP.get("default"));
                GemSynthesisRecipeBuilder.inputItems(gem);
                GemSynthesisRecipeBuilder.inputItems(materialType, count * 3);
                GemSynthesisRecipeBuilder.inputItems(Adventure.Items.GEM_DUST.get(), count * (i * 2 + 1));

                String originalRarity = getGemRarity(gem);
                int currentIndex = -1;
                for (int k = 0; k < RARITIES.length; k++) {
                    if (RARITIES[k].equals(originalRarity)) {
                        currentIndex = k;
                        break;
                    }
                }
                String upgradedRarity = RARITIES[currentIndex + 1];
                ItemStack upgradedGem = new ItemStack(Adventure.Items.GEM.get(), 1);
                if (gem.getTag() != null) upgradedGem.setTag(gem.getTag().copy());
                CompoundTag tag = upgradedGem.getTag();
                if (tag != null) {
                    CompoundTag affixData = tag.getCompound("affix_data");
                    affixData.putString("rarity", upgradedRarity);
                }
                timeMultiplier += count;
                GemSynthesisRecipeBuilder.outputItems(upgradedGem, count);
            }
        }
        GemSynthesisRecipeBuilder.duration(timeMultiplier);
        return GemSynthesisRecipeBuilder.buildRawRecipe();
    }

    /**
     * 强行为物品添加附魔
     */
    private Recipe getForcedEnchantmentRecipe() {
        RecipeBuilder forcedEnchantmentRecipeBuilder = getRecipeBuilder();

        ObjectHolder<ItemStack> EnchantedBook = new ObjectHolder<>(null);
        ObjectHolder<ItemStack> NonEnchantedItem = new ObjectHolder<>(null);
        forEachInputItems(stack -> {
            Item stackItem = stack.getItem();
            if (stackItem == GTItems.PROGRAMMED_CIRCUIT.asItem()) return false;
            if (EnchantedBook.value == null && stackItem == Items.ENCHANTED_BOOK) {
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.contains("StoredEnchantments", TAG_LIST)) EnchantedBook.value = stack;
                return false;
            }
            if (NonEnchantedItem.value == null && stackItem != Items.ENCHANTED_BOOK) {
                NonEnchantedItem.value = stack;
                return false;
            }
            return EnchantedBook.value != null && NonEnchantedItem.value != null;
        });
        if (EnchantedBook.value == null || NonEnchantedItem.value == null) return null;

        ItemStack inputBook = EnchantedBook.value.copy();
        ItemStack inputItem = NonEnchantedItem.value.copy();

        forcedEnchantmentRecipeBuilder.inputItems(inputBook, 1);
        forcedEnchantmentRecipeBuilder.inputItems(inputItem, 1);

        CompoundTag bookTag = inputBook.getTag();
        ListTag enchantmentsList = null;
        if (bookTag != null) enchantmentsList = bookTag.getList("StoredEnchantments", 10);

        CompoundTag targetTag = inputItem.getOrCreateTag();
        ListTag targetEnchantments;
        if (targetTag.contains("Enchantments", 9)) targetEnchantments = targetTag.getList("Enchantments", 10);
        else targetEnchantments = new ListTag();
        if (enchantmentsList != null) {
            for (int i = 0; i < enchantmentsList.size(); i++) {
                CompoundTag enchantmentTag = enchantmentsList.getCompound(i);
                targetEnchantments.add(enchantmentTag);
            }
        }
        targetTag.put("Enchantments", targetEnchantments);
        inputItem.setTag(targetTag);

        forcedEnchantmentRecipeBuilder.outputItems(inputItem, 1);
        forcedEnchantmentRecipeBuilder.outputItems(Items.BOOK);
        forcedEnchantmentRecipeBuilder.duration(5);
        forcedEnchantmentRecipeBuilder.MANAt(512);

        return forcedEnchantmentRecipeBuilder.buildRawRecipe();
    }

    /**
     * 强行为物品添加刻印
     */
    private Recipe getForcedAffixRecipe() {
        RecipeBuilder forcedAffixRecipeBuilder = getRecipeBuilder();

        ObjectHolder<ItemStack> affixCanvas = new ObjectHolder<>(null);
        ObjectHolder<ItemStack> NonAffixItem = new ObjectHolder<>(null);
        forEachInputItems(stack -> {
            Item stackItem = stack.getItem();
            if (stackItem == GTItems.PROGRAMMED_CIRCUIT.asItem()) return false;
            if (affixCanvas.value == null && stackItem == GTOItems.AFFIX_CANVAS.asItem()) {
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.contains("affix_list", TAG_LIST)) affixCanvas.value = stack;
                return false;
            }
            if (NonAffixItem.value == null && stackItem != GTOItems.AFFIX_CANVAS.asItem()) {
                NonAffixItem.value = stack;
                return false;
            }
            return affixCanvas.value != null && NonAffixItem.value != null;
        });
        if (affixCanvas.value == null || NonAffixItem.value == null) return null;

        ItemStack inputAffixCanvas = affixCanvas.value.copy();
        ItemStack inputItem = NonAffixItem.value.copy();

        forcedAffixRecipeBuilder.inputItems(inputAffixCanvas, 1);
        forcedAffixRecipeBuilder.inputItems(inputItem, 1);

        // 检查AFFIX_CANVAS是否有正确的NBT数据
        if (inputAffixCanvas.isEmpty() ||
                !inputAffixCanvas.hasTag() || inputAffixCanvas.getTag() == null ||
                !inputAffixCanvas.getTag().contains("affix_list", CompoundTag.TAG_LIST)) {
            return null;
        }

        ensureAffixData(inputItem);

        // 获取目标物品的affix_data和affixes
        CompoundTag targetNbt = inputItem.getOrCreateTag();
        CompoundTag affixData = targetNbt.getCompound("affix_data");
        CompoundTag affixes = affixData.getCompound("affixes");

        // 从AFFIX_CANVAS获取词缀列表
        ListTag affixList = inputAffixCanvas.getTag().getList("affix_list", CompoundTag.TAG_COMPOUND);
        // 应用所有词缀
        for (int i = 0; i < affixList.size(); i++) {
            CompoundTag affixEntry = affixList.getCompound(i);
            if (affixEntry.contains("id", CompoundTag.TAG_STRING)) {
                String affixId = affixEntry.getString("id");
                affixes.putFloat(affixId, 1.0f);
            }
        }

        forcedAffixRecipeBuilder.outputItems(inputItem, 1);
        forcedAffixRecipeBuilder.outputItems(GTOItems.AFFIX_CANVAS);
        forcedAffixRecipeBuilder.duration(5);
        forcedAffixRecipeBuilder.MANAt(512);

        return forcedAffixRecipeBuilder.buildRawRecipe();
    }

    /**
     * 强行为物品更改稀有度等级
     */
    private Recipe getForcedRarityUpRecipe() {
        RecipeBuilder ForcedRarityUpRecipeBuilder = getRecipeBuilder();

        ObjectHolder<ItemStack> rarityUpItem = new ObjectHolder<>(null);
        ObjectHolder<ItemStack> materialItem = new ObjectHolder<>(null);
        forEachInputItems(stack -> {
            Item stackItem = stack.getItem();
            if (stackItem == GTItems.PROGRAMMED_CIRCUIT.asItem() || stackItem == Adventure.Items.SIGIL_OF_REBIRTH.get()) return false;
            if (rarityUpItem.value == null)
                if (stackItem != Adventure.Items.COMMON_MATERIAL.get() && stackItem != Adventure.Items.UNCOMMON_MATERIAL.get() && stackItem != Adventure.Items.RARE_MATERIAL.get() && stackItem != Adventure.Items.EPIC_MATERIAL.get() && stackItem != Adventure.Items.MYTHIC_MATERIAL.get() && stackItem != Adventure.Items.ANCIENT_MATERIAL.get()) {
                    rarityUpItem.value = stack;
                }
            if (materialItem.value == null)
                if (stackItem == Adventure.Items.COMMON_MATERIAL.get() || stackItem == Adventure.Items.UNCOMMON_MATERIAL.get() || stackItem == Adventure.Items.RARE_MATERIAL.get() || stackItem == Adventure.Items.EPIC_MATERIAL.get() || stackItem == Adventure.Items.MYTHIC_MATERIAL.get() || stackItem == Adventure.Items.ANCIENT_MATERIAL.get()) {
                    materialItem.value = stack;
                }
            return rarityUpItem.value != null && materialItem.value != null;
        });
        if (rarityUpItem.value == null || materialItem.value == null) return null;

        String rarity = "apotheosis:" + getPrefix(materialItem.value.getItem().toString());

        ItemStack inputRarityUpItem = rarityUpItem.value.copy();
        ItemStack inputMaterialItem = materialItem.value.copy();

        ForcedRarityUpRecipeBuilder.inputItems(inputRarityUpItem, 1);
        ForcedRarityUpRecipeBuilder.inputItems(inputMaterialItem, 2);
        ForcedRarityUpRecipeBuilder.inputItems(Adventure.Items.SIGIL_OF_REBIRTH.get());

        // 确保物品有affix_data
        ensureAffixData(inputRarityUpItem);
        // 获取affix_data并设置稀有度
        CompoundTag nbt = inputRarityUpItem.getOrCreateTag();
        CompoundTag affixData = nbt.getCompound("affix_data");
        affixData.putString("rarity", rarity);

        ForcedRarityUpRecipeBuilder.outputItems(inputRarityUpItem, 1);
        ForcedRarityUpRecipeBuilder.duration(5);
        ForcedRarityUpRecipeBuilder.MANAt(512);

        return ForcedRarityUpRecipeBuilder.buildRawRecipe();
    }

    /**
     * 强行为物品添加镶孔
     */
    private Recipe getForcedAddSocketRecipe() {
        RecipeBuilder ForcedAddSocketRecipeBuilder = getRecipeBuilder();

        ObjectHolder<ItemStack> addSocketItem = new ObjectHolder<>(null);
        IntHolder sigilCount = new IntHolder(0);
        forEachInputItems(stack -> {
            Item stackItem = stack.getItem();
            if (stackItem == GTItems.PROGRAMMED_CIRCUIT.asItem()) return false;
            if (stackItem == Adventure.Items.SIGIL_OF_SOCKETING.get()) {
                sigilCount.value += stack.getCount();
                return false;
            }
            if (addSocketItem.value == null)
                if (stackItem != Adventure.Items.COMMON_MATERIAL.get() && stackItem != Adventure.Items.UNCOMMON_MATERIAL.get() && stackItem != Adventure.Items.RARE_MATERIAL.get() && stackItem != Adventure.Items.EPIC_MATERIAL.get() && stackItem != Adventure.Items.MYTHIC_MATERIAL.get() && stackItem != Adventure.Items.ANCIENT_MATERIAL.get()) {
                    addSocketItem.value = stack;
                }
            return addSocketItem.value != null && sigilCount.value != 0;
        });
        if (addSocketItem.value == null || sigilCount.value == 0) return null;

        ItemStack inputAddSocketItem = addSocketItem.value.copy();

        ForcedAddSocketRecipeBuilder.inputItems(inputAddSocketItem, 1);

        // 确保物品有affix_data
        ensureAffixData(inputAddSocketItem);
        // 获取affix_data
        CompoundTag nbt = inputAddSocketItem.getOrCreateTag();
        CompoundTag affixData = nbt.getCompound("affix_data");
        int currentSockets = affixData.getInt("sockets");
        if (currentSockets >= 16) return null;
        int costSigil = Math.min(16 - currentSockets, sigilCount.value);
        affixData.putInt("sockets", currentSockets + costSigil);

        ForcedAddSocketRecipeBuilder.inputItems(Adventure.Items.SIGIL_OF_SOCKETING.get(), costSigil);
        ForcedAddSocketRecipeBuilder.outputItems(inputAddSocketItem, 1);
        ForcedAddSocketRecipeBuilder.duration(costSigil);
        ForcedAddSocketRecipeBuilder.MANAt(512);

        return ForcedAddSocketRecipeBuilder.buildRawRecipe();
    }

    /**
     * 强行为物品镶嵌宝石
     */
    private Recipe getForcedMosaicGemRecipe() {
        RecipeBuilder ForcedMosaicGemRecipeBuilder = getRecipeBuilder();

        ObjectHolder<ItemStack> addGemItem = new ObjectHolder<>(null);
        List<ItemStack> gemItems = new ObjectArrayList<>();
        forEachInputItems(stack -> {
            Item stackItem = stack.getItem();
            if (stackItem == GTItems.PROGRAMMED_CIRCUIT.asItem()) return false;
            if (addGemItem.value == null && stackItem != Adventure.Items.GEM.get())
                addGemItem.value = stack;
            if (gemItems.size() < 16 && stackItem == Adventure.Items.GEM.get())
                gemItems.add(stack);
            return addGemItem.value != null && gemItems.size() > 16;
        });
        if (addGemItem.value == null || gemItems.isEmpty()) return null;

        ItemStack inputAddGemItem = addGemItem.value.copy();
        ForcedMosaicGemRecipeBuilder.inputItems(inputAddGemItem, 1);

        // 确保物品有affix_data
        ensureAffixData(inputAddGemItem);
        CompoundTag nbt = inputAddGemItem.getOrCreateTag();
        CompoundTag affixData = nbt.getCompound("affix_data");
        ListTag gems = affixData.getList("gems", CompoundTag.TAG_COMPOUND);
        int socketCount = affixData.getInt("sockets");
        // 确保gems列表有足够的空间
        while (gems.size() < socketCount) {
            CompoundTag airTag = new CompoundTag();
            airTag.putByte("Count", (byte) 1);
            airTag.putString("id", "minecraft:air");
            gems.add(airTag);
        }
        while (gems.size() > socketCount) {
            gems.remove(gems.size() - 1);
        }

        List<ItemStack> inputGemItems = new ObjectArrayList<>();
        boolean full = false;
        for (ItemStack inputGemItem : gemItems) {
            int stackCount = inputGemItem.getCount();
            for (int i = 0; i < stackCount; i++) {
                // 查找第一个空位
                int emptySlot = -1;
                for (int k = 0; k < gems.size(); k++) {
                    CompoundTag gemTag = gems.getCompound(k);
                    if (gemTag.contains("id") && (gemTag.getString("id").equals("minecraft:air"))) {
                        emptySlot = k;
                        break;
                    }
                }
                if (emptySlot == -1) {
                    full = true;
                    break;
                }
                // 将宝石添加到空位
                CompoundTag gemTag = new CompoundTag();
                gemTag.putByte("Count", (byte) 1);
                gemTag.putString("id", "apotheosis:gem");
                if (inputGemItem.getTag() != null) {
                    gemTag.put("tag", inputGemItem.getTag().copy());
                }
                gems.set(emptySlot, gemTag);
                inputGemItems.add(inputGemItem.copyWithCount(1));
            }
            if (full) break;
        }

        for (ItemStack inputGemItem : inputGemItems) ForcedMosaicGemRecipeBuilder.inputItems(inputGemItem, 1);

        ForcedMosaicGemRecipeBuilder.outputItems(inputAddGemItem, 1);
        ForcedMosaicGemRecipeBuilder.duration(inputGemItems.size());
        ForcedMosaicGemRecipeBuilder.MANAt(512);

        return ForcedMosaicGemRecipeBuilder.buildRawRecipe();
    }

    // 定义所有可能的稀有度
    private static final String[] RARITIES = {
            "apotheosis:common",
            "apotheosis:uncommon",
            "apotheosis:rare",
            "apotheosis:epic",
            "apotheosis:mythic",
            "apotheosis:ancient"
    };

    /**
     * 确保物品堆包含affix_data标签，并且包含所有必要的子标签
     *
     * @param itemStack 要检查的物品堆
     */
    private static void ensureAffixData(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        // 检查是否已存在affix_data
        if (!tag.contains("affix_data")) {
            CompoundTag affixData = new CompoundTag();
            tag.put("affix_data", affixData);
        }
        CompoundTag affixData = tag.getCompound("affix_data");
        ensureAffixSubTags(affixData);
    }

    /**
     * 确保affix_data包含所有必要的子标签
     *
     * @param affixData 要检查的affix_data CompoundTag
     */
    private static void ensureAffixSubTags(CompoundTag affixData) {
        if (!affixData.contains("affixes")) {
            CompoundTag affixes = new CompoundTag();
            affixData.put("affixes", affixes);
        }
        if (!affixData.contains("gems")) {
            ListTag gems = new ListTag();
            affixData.put("gems", gems);
        }
        if (!affixData.contains("name")) {
            Component nameComponent = Component.translatable("%2$s", "", "");
            affixData.putString("name", Component.Serializer.toJson(nameComponent));
        }
        if (!affixData.contains("rarity")) {
            affixData.putString("rarity", "apotheosis:common");
        }
        if (!affixData.contains("sockets")) {
            affixData.putInt("sockets", 0);
        }
    }

    /**
     * 获取宝石的稀有度（从NBT中读取）
     */
    private static String getGemRarity(ItemStack gemStack) {
        if (gemStack.hasTag()) {
            CompoundTag tag = gemStack.getTag();
            if (tag != null && tag.contains("affix_data")) {
                CompoundTag affixData = tag.getCompound("affix_data");
                if (affixData.contains("rarity")) return affixData.getString("rarity");
            }
        }
        return "apotheosis:common";
    }

    /**
     * 通过字符串获取宝石
     */
    public static ItemStack getGem(int rarity, String gem) {
        if (rarity > 5) rarity = 0;
        ItemStack gemStack = new ItemStack(Adventure.Items.GEM.get());
        CompoundTag rootTag = new CompoundTag();
        CompoundTag affixData = new CompoundTag();
        affixData.putString("rarity", RARITIES[rarity]);
        rootTag.put("affix_data", affixData);
        rootTag.putString("gem", gem);
        gemStack.setTag(rootTag);
        return gemStack;
    }
}
