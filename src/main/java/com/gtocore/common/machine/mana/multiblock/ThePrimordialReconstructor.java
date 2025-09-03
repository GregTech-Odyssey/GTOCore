package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.common.data.GTOItems;
import com.gtocore.data.record.ApotheosisAffix;
import com.gtocore.data.record.Enchantment;

import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
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

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.gtocore.common.data.GTOItems.AFFIX_ESSENCE;
import static com.gtocore.common.data.GTOItems.ENCHANTMENT_ESSENCE;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;

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
     * 5 附魔书/铭刻之布制作
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
        if (circuit == 1 || circuit == 2 || circuit == 3 || circuit == 4) return getDisassembleRecipe();
        if (circuit == 5) return getEnchantmentsLoadRecipe();
        return null;
    }

    /**
     * 构建精粹合成附魔书配方
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
}
