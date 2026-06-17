package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.item.AffixCanvas;
import com.gtocore.common.item.ApothItem;
import com.gtocore.data.record.EnchantmentRecord;

import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import com.gto.datasynclib.util.holder.IntHolder;
import com.gto.datasynclib.util.holder.LongHolder;
import com.gto.datasynclib.util.holder.ObjHolder;
import com.gto.fastcollection.O2IOpenCacheHashMap;
import com.gto.registrate.util.entry.ItemEntry;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketHelper;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketedGems;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemInstance;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemItem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.gtocore.common.data.GTOItems.AFFIX_ESSENCE;
import static com.gtocore.common.data.GTOItems.ENCHANTMENT_ESSENCE;
import static com.gtocore.data.record.ApotheosisAffixRecord.AFFIX_ITEM_MAP;
import static com.gtocore.data.record.EnchantmentRecord.ENCHANTMENT_ITEM_MAP;

public class ThePrimordialReconstructor extends ManaMultiblockMachine implements ICustomRecipeLogicHolder {

    private static final String DEFAULT_RARITY = "apotheosis:common";
    private static final List<ResourceLocation> APOTHEOSIS_RARITY_IDS = List.of(
            ResourceLocation.fromNamespaceAndPath("apotheosis", "common"),
            ResourceLocation.fromNamespaceAndPath("apotheosis", "uncommon"),
            ResourceLocation.fromNamespaceAndPath("apotheosis", "rare"),
            ResourceLocation.fromNamespaceAndPath("apotheosis", "epic"),
            ResourceLocation.fromNamespaceAndPath("apotheosis", "mythic"),
            ResourceLocation.fromNamespaceAndPath("apotheosis", "ancient"));

    private record GemKey(ResourceLocation gem, ResourceLocation rarity) {}

    private record EnchantmentLevel(Enchantment enchantment, int level) {}

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
     * 9 宝石粉碎
     * 10 强行附魔给予
     * 11 强行刻印给予
     * 12 强行修改稀有度
     * 13 强行增加镶孔
     * 14 强行镶嵌宝石
     */
    private int circuit = 0;

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.model", circuit));
        textList.add(Component.translatable("gtocore.machine.the_primordial_reconstructor.mode." + circuit));
    }

    /**
     * 构建物品解构配方
     */
    private GTRecipeDefinition getDisassembleRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder disassembleRecipeBuilder = getRecipeBuilder();
        List<ItemStack> inputsItems = new ArrayList<>();
        List<ItemStack> outputsItems = new ArrayList<>();
        unit.forEachItems(true, (stack, amount) -> {
            if (hasApotheosisData(stack) || hasEquipmentEnchantments(stack)) {
                if (disassembleEquipment(stack, inputsItems, outputsItems)) {
                    inputsItems.add(stack);
                }
            } else if (circuit == 4 && stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("Damage")) {
                inputsItems.add(stack);
            }
            if (circuit == 2 || circuit == 4)
                if (stack.is(Items.ENCHANTED_BOOK))
                    if (disassembleEnchantments(stack, outputsItems)) {
                        inputsItems.add(stack);
                        outputsItems.add(new ItemStack(Items.BOOK));
                    }
            if (circuit == 3 || circuit == 4)
                if (stack.getItem() == GTOItems.AFFIX_CANVAS.asItem())
                    if (disassembleAffixCanvas(stack, outputsItems)) {
                        inputsItems.add(stack);
                        outputsItems.add(new ItemStack(GTOItems.AFFIX_CANVAS));
                    }
            return false;
        });
        if (!inputsItems.isEmpty() || !outputsItems.isEmpty()) {
            inputsItems.forEach(disassembleRecipeBuilder::inputItems);
            outputsItems.forEach(disassembleRecipeBuilder::outputItems);
            disassembleRecipeBuilder.duration(20);
            return disassembleRecipeBuilder.build();
        }
        return null;
    }

    /**
     * 将物品解构为宝石、附魔书和材料
     *
     * @param inputsItems  输入列表
     * @param outputsItems 输出列表
     */
    private boolean disassembleEquipment(ItemStack stack, List<ItemStack> inputsItems, List<ItemStack> outputsItems) {
        boolean find = false;

        // 提取附魔
        if (circuit == 1 || circuit == 3) {
            if (extractEnchantmentsToBook(stack, outputsItems)) {
                inputsItems.add(new ItemStack(Items.BOOK));
                find = true;
            }
        } else if (circuit == 2 || circuit == 4) {
            if (extractEnchantmentsToEssence(stack, outputsItems))
                find = true;
        }

        // 提取词缀
        if (circuit == 1 || circuit == 2) {
            if (extractAffix1(stack, outputsItems)) {
                inputsItems.add(new ItemStack(GTOItems.AFFIX_CANVAS));
                find = true;
            }
        } else if (circuit == 3 || circuit == 4) {
            if (extractAffix2(stack, outputsItems))
                find = true;
        }

        // 提取宝石
        if (extractGems(stack, outputsItems)) {
            inputsItems.add(new ItemStack(Adventure.Items.SIGIL_OF_WITHDRAWAL.get()));
            find = true;
        }

        // 电路4强行粉碎
        if (circuit == 4) find = true;

        return generateMaterials(stack, outputsItems) || find;
    }

    /**
     * Reads all enchantments from an item and creates one enchanted book containing them.
     *
     * @param stack        装备物品
     * @param outputsItems 输出列表
     * @return 是否成功提取
     */
    private static boolean extractEnchantmentsToBook(ItemStack stack, List<ItemStack> outputsItems) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (enchantments.isEmpty()) return false;

        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(enchantments, enchantedBook);
        outputsItems.add(enchantedBook);
        return true;
    }

    /**
     * Reads all enchantments from an item and outputs matching enchantment essence.
     *
     * @param stack        装备物品
     * @param outputsItems 输出列表
     * @return 是否成功提取
     */
    private static boolean extractEnchantmentsToEssence(ItemStack stack, List<ItemStack> outputsItems) {
        return outputEnchantmentEssence(EnchantmentHelper.getEnchantments(stack), outputsItems);
    }

    /**
     * 从NBT中提取宝石
     *
     * @param stack        装备物品
     * @param outputsItems 输出列表
     * @return 提取的宝石数量
     */
    private static boolean extractGems(ItemStack stack, List<ItemStack> outputsItems) {
        List<ItemStack> gems = SocketHelper.getGems(stack).stream()
                .filter(GemInstance::isValid)
                .map(GemInstance::gemStack)
                .map(ItemStack::copy)
                .toList();
        outputsItems.addAll(gems);
        return !gems.isEmpty();
    }

    /**
     * 从NBT中提取词缀并应用到铭刻之布
     *
     * @param stack        完整的物品堆
     * @param outputsItems 接收词缀的物品列表
     * @return 是否成功提取
     */
    private static boolean extractAffix1(ItemStack stack, List<ItemStack> outputsItems) {
        Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(stack);
        if (affixes.isEmpty()) return false;

        List<ResourceLocation> affixIds = new ArrayList<>(affixes.size());
        for (DynamicHolder<? extends Affix> affix : affixes.keySet()) {
            affixIds.add(affix.getId());
        }

        outputsItems.add(AffixCanvas.createWithAffixes(affixIds));

        return true;
    }

    /**
     * 从NBT中提取词缀并输出为刻印精粹
     *
     * @param stack        完整的物品堆
     * @param outputsItems 接收词缀的物品列表
     * @return 是否成功提取
     */
    private static boolean extractAffix2(ItemStack stack, List<ItemStack> outputsItems) {
        Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(stack);
        if (affixes.isEmpty()) return false;

        boolean found = false;
        for (DynamicHolder<? extends Affix> affix : affixes.keySet()) {
            ItemEntry<ApothItem> essence = AFFIX_ESSENCE.get(affix.getId().toString());
            if (essence != null) {
                outputsItems.add(new ItemStack(essence));
                found = true;
            }
        }
        return found;
    }

    /**
     * 根据稀有度生成材料
     *
     * @param stack        装备物品
     * @param outputsItems 输出列表
     */
    private static boolean generateMaterials(ItemStack stack, List<ItemStack> outputsItems) {
        DynamicHolder<LootRarity> rarity = AffixHelper.getRarity(stack);
        if (rarity.isBound()) {
            outputsItems.add(new ItemStack(rarity.get().getMaterial(), 2));
            return true;
        }
        return false;
    }

    /**
     * 将附魔书分解为附魔精粹
     *
     * @param stack        要分解的附魔书
     * @param outputsItems 输出列表
     */
    private static boolean disassembleEnchantments(ItemStack stack, List<ItemStack> outputsItems) {
        return outputEnchantmentEssence(EnchantmentHelper.getEnchantments(stack), outputsItems);
    }

    /**
     * 将铭刻之布分解为刻印精粹
     *
     * @param stack        要分解的铭刻之布
     * @param outputsItems 输出列表
     */
    private static boolean disassembleAffixCanvas(ItemStack stack, List<ItemStack> outputsItems) {
        boolean found = false;
        for (ResourceLocation affixId : AffixCanvas.readAffixes(stack)) {
            ItemEntry<ApothItem> essence = AFFIX_ESSENCE.get(affixId.toString());
            if (essence == null) continue;
            outputsItems.add(new ItemStack(essence));
            found = true;
        }
        return found;
    }

    /**
     * Converts enchantment levels to essence counts with the original power-of-two cost curve.
     */
    private static boolean outputEnchantmentEssence(Map<Enchantment, Integer> enchantments, List<ItemStack> outputsItems) {
        boolean found = false;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            ResourceLocation enchantmentId = EnchantmentHelper.getEnchantmentId(entry.getKey());
            if (enchantmentId == null) continue;
            int count = getEnchantmentEssenceCount(entry.getValue());
            if (count <= 0) continue;
            ItemEntry<ApothItem> essence = ENCHANTMENT_ESSENCE.get(enchantmentId.toString());
            if (essence == null) essence = ENCHANTMENT_ESSENCE.get("original");
            if (essence == null) continue;
            outputsItems.add(new ItemStack(essence, count));
            found = true;
        }
        return found;
    }

    private static int getEnchantmentEssenceCount(int level) {
        if (level <= 0) return 0;
        return 1 << (Math.min(level, 30) - 1);
    }

    /**
     * 附魔精粹合成附魔书配方
     */
    private GTRecipeDefinition getEnchantmentsLoadRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder enchantmentsLoadRecipeBuilder = getRecipeBuilder();
        ObjHolder<Item> essence = new ObjHolder<>();
        LongHolder count = new LongHolder();

        unit.forEachItems(true, (stack, amount) -> {
            Item stackItem = stack.getItem();
            if (essence.value == null) {
                var enchantment = ENCHANTMENT_ITEM_MAP.get(stackItem);
                if (enchantment != null)
                    essence.value = stackItem;
            }
            if (essence.value != null && essence.value == stackItem)
                count.value += amount;
            return false;
        });

        int lvl = Math.min(64 - Long.numberOfLeadingZeros(count.value), 30);
        if (essence.value != null && lvl > 0) {
            var enchantment = ENCHANTMENT_ITEM_MAP.get(essence.value);
            enchantmentsLoadRecipeBuilder.inputItems(Items.BOOK);
            enchantmentsLoadRecipeBuilder.inputItems(essence.value, 1 << (lvl - 1));
            enchantmentsLoadRecipeBuilder.outputItems(EnchantmentRecord.getEnchantedBookByEnchantmentId(enchantment.enchantmentId(), (short) lvl));
            enchantmentsLoadRecipeBuilder.duration(20);
            enchantmentsLoadRecipeBuilder.MANAt(256);
            return enchantmentsLoadRecipeBuilder.build();
        }

        return null;
    }

    /**
     * 构建附魔书合并配方
     */
    private GTRecipeDefinition getEnchantedBooksMergeRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder mergeRecipeBuilder = getRecipeBuilder();
        List<EnchantmentLevel> allEnchantments = new ArrayList<>();
        IntHolder totalBooks = new IntHolder(0);
        unit.forEachItems(true, (stack, amount) -> {
            if (stack.is(Items.ENCHANTED_BOOK)) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                if (!enchantments.isEmpty()) {
                    totalBooks.value++;
                    enchantments.forEach((enchantment, level) -> allEnchantments.add(new EnchantmentLevel(enchantment, level)));
                    mergeRecipeBuilder.inputItems(stack);
                }
            }
            return false;
        });

        if (totalBooks.value < 2 || allEnchantments.isEmpty()) return null;

        mergeMatchingEnchantments(allEnchantments);
        List<ItemStack> outputBooks = createEnchantedBooks(allEnchantments);

        for (ItemStack outputBookItem : outputBooks) {
            mergeRecipeBuilder.outputItems(outputBookItem);
        }

        int remainingBooks = totalBooks.value - outputBooks.size();
        if (remainingBooks > 0) {
            mergeRecipeBuilder.outputItems(Items.BOOK, remainingBooks);
        } else if (remainingBooks < 0) {
            mergeRecipeBuilder.inputItems(Items.BOOK, -remainingBooks);
        }

        mergeRecipeBuilder.duration(20);
        mergeRecipeBuilder.MANAt(512);

        return mergeRecipeBuilder.build();
    }

    /**
     * Repeatedly upgrades pairs of equal enchantments at the same level.
     */
    private static void mergeMatchingEnchantments(List<EnchantmentLevel> enchantments) {
        boolean changed;
        do {
            changed = false;
            Map<Enchantment, Int2IntMap> enchantmentLevelCounts = new IdentityHashMap<>();
            for (EnchantmentLevel enchantment : enchantments) {
                Int2IntMap levelCounts = enchantmentLevelCounts.computeIfAbsent(enchantment.enchantment(), k -> new Int2IntOpenHashMap());
                levelCounts.put(enchantment.level(), levelCounts.getOrDefault(enchantment.level(), 0) + 1);
            }

            enchantments.clear();
            for (Map.Entry<Enchantment, Int2IntMap> enchantEntry : enchantmentLevelCounts.entrySet()) {
                Enchantment enchantment = enchantEntry.getKey();
                for (Int2IntMap.Entry levelEntry : enchantEntry.getValue().int2IntEntrySet()) {
                    int level = levelEntry.getIntKey();
                    int count = levelEntry.getIntValue();
                    int upgraded = count / 2;
                    int remainder = count % 2;

                    for (int i = 0; i < upgraded; i++) {
                        enchantments.add(new EnchantmentLevel(enchantment, level + 1));
                        changed = true;
                    }
                    if (remainder > 0) {
                        enchantments.add(new EnchantmentLevel(enchantment, level));
                    }
                }
            }
        } while (changed);
    }

    private static @NotNull List<ItemStack> createEnchantedBooks(List<EnchantmentLevel> allEnchantments) {
        List<ItemStack> outputBooks = new ArrayList<>();
        List<EnchantmentLevel> remainingEnchantments = new ArrayList<>(allEnchantments);

        while (!remainingEnchantments.isEmpty()) {
            ItemStack outputBook = new ItemStack(Items.ENCHANTED_BOOK);
            Map<Enchantment, Integer> bookEnchantments = new LinkedHashMap<>();
            Set<Enchantment> addedEnchantments = Collections.newSetFromMap(new IdentityHashMap<>());
            Iterator<EnchantmentLevel> iterator = remainingEnchantments.iterator();
            while (iterator.hasNext()) {
                EnchantmentLevel enchantment = iterator.next();
                if (addedEnchantments.add(enchantment.enchantment())) {
                    bookEnchantments.put(enchantment.enchantment(), enchantment.level());
                    iterator.remove();
                }
            }

            EnchantmentHelper.setEnchantments(bookEnchantments, outputBook);
            outputBooks.add(outputBook);
        }
        return outputBooks;
    }

    /**
     * 刻印精粹合成铭刻之布配方
     */
    private GTRecipeDefinition getAffixCanvasLoadRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder affixCanvasLoadRecipeBuilder = getRecipeBuilder();

        Set<Item> uniqueItems = new ReferenceOpenHashSet<>();
        unit.forEachItems(true, (stack, amount) -> {
            Item stackItem = stack.getItem();
            var affix = AFFIX_ITEM_MAP.get(stackItem);
            if (affix != null) uniqueItems.add(stackItem);
            return false;
        });
        if (uniqueItems.isEmpty()) return null;

        List<ResourceLocation> affixIds = new ArrayList<>(uniqueItems.size());
        for (Item item : uniqueItems) {
            ResourceLocation affixId = ResourceLocation.tryParse(AFFIX_ITEM_MAP.get(item).affixId());
            if (affixId == null) continue;
            affixIds.add(affixId);
            affixCanvasLoadRecipeBuilder.inputItems(item);
        }
        if (affixIds.isEmpty()) return null;

        affixCanvasLoadRecipeBuilder.inputItems(GTOItems.AFFIX_CANVAS);
        affixCanvasLoadRecipeBuilder.outputItems(AffixCanvas.createWithAffixes(affixIds));
        affixCanvasLoadRecipeBuilder.duration(20);
        affixCanvasLoadRecipeBuilder.MANAt(512);

        return affixCanvasLoadRecipeBuilder.build();
    }

    /**
     * 宝石合成
     */
    private GTRecipeDefinition getGemSynthesisRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder gemSynthesisRecipeBuilder = getRecipeBuilder();

        Object2IntOpenHashMap<GemKey> gemCounts = new O2IOpenCacheHashMap<>();
        unit.forEachItems(true, (stack, amount) -> {
            if (stack.getItem() == Adventure.Items.GEM.get()) {
                GemInstance gem = GemInstance.unsocketed(stack);
                if (gem.isValidUnsocketed()) {
                    gemCounts.addTo(new GemKey(gem.gem().getId(), gem.rarity().getId()), stack.getCount());
                }
            }
            return false;
        });
        if (gemCounts.isEmpty()) return null;

        boolean hasRecipe = false;
        for (Object2IntMap.Entry<GemKey> entry : gemCounts.object2IntEntrySet()) {
            int pairCount = entry.getIntValue() / 2;
            if (pairCount <= 0) continue;

            DynamicHolder<Gem> gem = GemRegistry.INSTANCE.holder(entry.getKey().gem());
            DynamicHolder<LootRarity> rarity = RarityRegistry.INSTANCE.holder(entry.getKey().rarity());
            if (!gem.isBound() || !rarity.isBound()) continue;

            DynamicHolder<LootRarity> upgradedRarity = RarityRegistry.next(rarity);
            if (!upgradedRarity.isBound() || upgradedRarity.get() == rarity.get()) continue;

            gemSynthesisRecipeBuilder.inputItems(createGemStack(gem, rarity, pairCount * 2));
            gemSynthesisRecipeBuilder.inputItems(rarity.get().getMaterial(), pairCount * 3);
            gemSynthesisRecipeBuilder.inputItems(Adventure.Items.GEM_DUST.get(), pairCount * getUpgradeDustCost(rarity));
            gemSynthesisRecipeBuilder.outputItems(createGemStack(gem, upgradedRarity, 1), pairCount);
            hasRecipe = true;
        }
        if (!hasRecipe) return null;

        gemSynthesisRecipeBuilder.duration(20);
        return gemSynthesisRecipeBuilder.build();
    }

    private static final Map<String, Integer> RARITY_TO_DUST_COUNT = Map.of(
            "apotheosis:common", 2,
            "apotheosis:uncommon", 3,
            "apotheosis:rare", 4,
            "apotheosis:epic", 5,
            "apotheosis:mythic", 6,
            "apotheosis:ancient", 10);

    /**
     * 宝石合成
     */
    private GTRecipeDefinition getGemCrushingRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder gemCrushingRecipeBuilder = getRecipeBuilder();

        List<ItemStack> inputsItems = new ArrayList<>();
        LongHolder totalDustCount = new LongHolder(0);

        unit.forEachItems(true, (stack, amount) -> {
            if (stack.getItem() == Adventure.Items.GEM.get()) {
                int stackDust = stack.getCount() * RARITY_TO_DUST_COUNT.getOrDefault(getRarityId(getGemRarity(stack)), 1);
                inputsItems.add(stack);
                if (totalDustCount.value + stackDust >= Integer.MAX_VALUE) {
                    totalDustCount.value = Integer.MAX_VALUE;
                    return true;
                } else {
                    totalDustCount.value += stackDust;
                }
            }
            return false;
        });

        if (inputsItems.isEmpty() || totalDustCount.value <= 0) return null;
        inputsItems.forEach(gemCrushingRecipeBuilder::inputItems);
        gemCrushingRecipeBuilder.outputItems(Adventure.Items.GEM_DUST.get(), (int) totalDustCount.value);

        gemCrushingRecipeBuilder.duration(10);
        return gemCrushingRecipeBuilder.build();
    }

    /**
     * 强行为物品添加附魔
     */
    private GTRecipeDefinition getForcedEnchantmentRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder forcedEnchantmentRecipeBuilder = getRecipeBuilder();

        ObjHolder<ItemStack> enchantedBook = new ObjHolder<>();
        ObjHolder<ItemStack> nonEnchantedItem = new ObjHolder<>();
        unit.forEachItems(true, (stack, amount) -> {
            Item stackItem = stack.getItem();
            if (stackItem == GTItems.PROGRAMMED_CIRCUIT.asItem()) return false;
            if (enchantedBook.value == null && stack.is(Items.ENCHANTED_BOOK)) {
                if (!EnchantmentHelper.getEnchantments(stack).isEmpty()) enchantedBook.value = stack;
                return false;
            }
            if (nonEnchantedItem.value == null && stackItem != Items.ENCHANTED_BOOK) {
                nonEnchantedItem.value = stack;
                return false;
            }
            return enchantedBook.value != null && nonEnchantedItem.value != null;
        });
        if (enchantedBook.value == null || nonEnchantedItem.value == null) return null;

        ItemStack inputBook = enchantedBook.value.copy();
        ItemStack inputItem = nonEnchantedItem.value.copy();

        forcedEnchantmentRecipeBuilder.inputItems(inputBook, 1);
        forcedEnchantmentRecipeBuilder.inputItems(inputItem, 1);

        if (!mergeEnchantments(inputItem, EnchantmentHelper.getEnchantments(inputBook))) return null;

        forcedEnchantmentRecipeBuilder.outputItems(inputItem, 1);
        forcedEnchantmentRecipeBuilder.outputItems(Items.BOOK);
        forcedEnchantmentRecipeBuilder.duration(5);
        forcedEnchantmentRecipeBuilder.MANAt(512);

        return forcedEnchantmentRecipeBuilder.build();
    }

    /**
     * Applies book enchantments to a target stack, keeping the highest level for duplicates.
     */
    private static boolean mergeEnchantments(ItemStack targetStack, Map<Enchantment, Integer> enchantmentsToAdd) {
        if (enchantmentsToAdd.isEmpty()) return false;

        Map<Enchantment, Integer> mergedEnchantments = new LinkedHashMap<>(EnchantmentHelper.getEnchantments(targetStack));
        boolean changed = false;
        for (Map.Entry<Enchantment, Integer> entry : enchantmentsToAdd.entrySet()) {
            int currentLevel = mergedEnchantments.getOrDefault(entry.getKey(), 0);
            if (entry.getValue() > currentLevel) {
                mergedEnchantments.put(entry.getKey(), entry.getValue());
                changed = true;
            }
        }
        if (!changed) return false;

        EnchantmentHelper.setEnchantments(mergedEnchantments, targetStack);
        return true;
    }

    /**
     * 强行为物品添加刻印
     */
    private GTRecipeDefinition getForcedAffixRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder forcedAffixRecipeBuilder = getRecipeBuilder();

        ObjHolder<ItemStack> affixCanvas = new ObjHolder<>();
        ObjHolder<ItemStack> nonAffixItem = new ObjHolder<>();
        unit.forEachItems(true, (stack, amount) -> {
            Item stackItem = stack.getItem();
            if (stackItem == GTItems.PROGRAMMED_CIRCUIT.asItem()) return false;
            if (affixCanvas.value == null && stackItem == GTOItems.AFFIX_CANVAS.asItem()) {
                if (AffixCanvas.hasAffixes(stack)) affixCanvas.value = stack;
                return false;
            }
            if (nonAffixItem.value == null && stackItem != GTOItems.AFFIX_CANVAS.asItem()) {
                nonAffixItem.value = stack;
                return false;
            }
            return affixCanvas.value != null && nonAffixItem.value != null;
        });
        if (affixCanvas.value == null || nonAffixItem.value == null) return null;

        ItemStack inputAffixCanvas = affixCanvas.value.copy();
        ItemStack inputItem = nonAffixItem.value.copy();

        forcedAffixRecipeBuilder.inputItems(inputAffixCanvas, 1);
        forcedAffixRecipeBuilder.inputItems(inputItem, 1);

        DynamicHolder<LootRarity> rarity = ensureApotheosisRarity(inputItem);
        if (!rarity.isBound()) return null;

        List<ResourceLocation> affixIds = AffixCanvas.readAffixes(inputAffixCanvas);
        if (affixIds.isEmpty()) return null;

        Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = new HashMap<>(AffixHelper.getAffixes(inputItem));
        boolean addedAffix = false;
        for (ResourceLocation affixId : affixIds) {
            DynamicHolder<Affix> affix = AffixRegistry.INSTANCE.holder(affixId);
            if (!affix.isBound()) continue;
            affixes.put(affix, new AffixInstance(affix, inputItem, rarity, 1.0f));
            addedAffix = true;
        }
        if (!addedAffix) return null;
        AffixHelper.setAffixes(inputItem, affixes);

        forcedAffixRecipeBuilder.outputItems(inputItem, 1);
        forcedAffixRecipeBuilder.outputItems(GTOItems.AFFIX_CANVAS);
        forcedAffixRecipeBuilder.duration(5);
        forcedAffixRecipeBuilder.MANAt(512);

        return forcedAffixRecipeBuilder.build();
    }

    /**
     * 强行为物品更改稀有度等级
     */
    private GTRecipeDefinition getForcedRarityUpRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder forcedRarityUpRecipeBuilder = getRecipeBuilder();

        ObjHolder<ItemStack> rarityUpItem = new ObjHolder<>();
        ObjHolder<ItemStack> materialItem = new ObjHolder<>();
        unit.forEachItems(true, (stack, amount) -> {
            Item stackItem = stack.getItem();
            if (stackItem == GTItems.PROGRAMMED_CIRCUIT.asItem() || stackItem == Adventure.Items.SIGIL_OF_REBIRTH.get())
                return false;
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

        DynamicHolder<LootRarity> rarity = RarityRegistry.getMaterialRarity(materialItem.value.getItem());
        if (!rarity.isBound()) return null;

        ItemStack inputRarityUpItem = rarityUpItem.value.copy();
        ItemStack inputMaterialItem = materialItem.value.copy();

        forcedRarityUpRecipeBuilder.inputItems(inputRarityUpItem, 1);
        forcedRarityUpRecipeBuilder.inputItems(inputMaterialItem, 2);
        forcedRarityUpRecipeBuilder.inputItems(Adventure.Items.SIGIL_OF_REBIRTH.get());

        AffixHelper.setRarity(inputRarityUpItem, rarity.get());

        forcedRarityUpRecipeBuilder.outputItems(inputRarityUpItem, 1);
        forcedRarityUpRecipeBuilder.duration(5);
        forcedRarityUpRecipeBuilder.MANAt(512);

        return forcedRarityUpRecipeBuilder.build();
    }

    /**
     * 强行为物品添加镶孔
     */
    private GTRecipeDefinition getForcedAddSocketRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder forcedAddSocketRecipeBuilder = getRecipeBuilder();

        ObjHolder<ItemStack> addSocketItem = new ObjHolder<>();
        IntHolder sigilCount = new IntHolder();
        unit.forEachItems(true, (stack, amount) -> {
            Item stackItem = stack.getItem();
            if (stackItem == GTItems.PROGRAMMED_CIRCUIT.asItem()) return false;
            if (stackItem == Adventure.Items.SIGIL_OF_SOCKETING.get()) {
                sigilCount.value += (int) amount;
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

        forcedAddSocketRecipeBuilder.inputItems(inputAddSocketItem, 1);

        ensureApotheosisRarity(inputAddSocketItem);
        int currentSockets = SocketHelper.getSockets(inputAddSocketItem);
        if (currentSockets >= 16) return null;
        int costSigil = Math.min(16 - currentSockets, sigilCount.value);
        SocketHelper.setSockets(inputAddSocketItem, currentSockets + costSigil);

        forcedAddSocketRecipeBuilder.inputItems(Adventure.Items.SIGIL_OF_SOCKETING.get(), costSigil);
        forcedAddSocketRecipeBuilder.outputItems(inputAddSocketItem, 1);
        forcedAddSocketRecipeBuilder.duration(5);
        forcedAddSocketRecipeBuilder.MANAt(512);

        return forcedAddSocketRecipeBuilder.build();
    }

    /**
     * 强行为物品镶嵌宝石
     */
    private GTRecipeDefinition getForcedMosaicGemRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder forcedMosaicGemRecipeBuilder = getRecipeBuilder();

        ObjHolder<ItemStack> addGemItem = new ObjHolder<>();
        List<ItemStack> gemItems = new ArrayList<>();
        unit.forEachItems(true, (stack, amount) -> {
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
        forcedMosaicGemRecipeBuilder.inputItems(inputAddGemItem, 1);

        ensureApotheosisRarity(inputAddGemItem);
        int socketCount = SocketHelper.getSockets(inputAddGemItem);
        if (socketCount <= 0) return null;

        List<GemInstance> socketedGems = new ArrayList<>(SocketHelper.getGems(inputAddGemItem).gems());
        while (socketedGems.size() < socketCount) socketedGems.add(GemInstance.EMPTY);
        while (socketedGems.size() > socketCount) socketedGems.removeLast();

        List<ItemStack> inputGemItems = new ArrayList<>();
        boolean full = false;
        for (ItemStack inputGemItem : gemItems) {
            int stackCount = inputGemItem.getCount();
            for (int i = 0; i < stackCount; i++) {
                // 查找第一个空位
                int emptySlot = -1;
                for (int k = 0; k < socketedGems.size(); k++) {
                    if (!socketedGems.get(k).isValid()) {
                        emptySlot = k;
                        break;
                    }
                }
                if (emptySlot == -1) {
                    full = true;
                    break;
                }
                ItemStack gemToInsert = inputGemItem.copyWithCount(1);
                socketedGems.set(emptySlot, GemInstance.socketed(inputAddGemItem, gemToInsert));
                inputGemItems.add(gemToInsert);
            }
            if (full) break;
        }
        if (inputGemItems.isEmpty()) return null;
        SocketHelper.setGems(inputAddGemItem, new SocketedGems(socketedGems));

        for (ItemStack inputGemItem : inputGemItems) forcedMosaicGemRecipeBuilder.inputItems(inputGemItem, 1);

        forcedMosaicGemRecipeBuilder.outputItems(inputAddGemItem, 1);
        forcedMosaicGemRecipeBuilder.duration(5);
        forcedMosaicGemRecipeBuilder.MANAt(512);

        return forcedMosaicGemRecipeBuilder.build();
    }

    private static boolean hasApotheosisData(ItemStack stack) {
        return AffixHelper.hasAffixes(stack) || AffixHelper.getRarity(stack).isBound() || SocketHelper.getSockets(stack) > 0;
    }

    private static boolean hasEquipmentEnchantments(ItemStack stack) {
        return !stack.is(Items.ENCHANTED_BOOK) && !EnchantmentHelper.getEnchantments(stack).isEmpty();
    }

    private static DynamicHolder<LootRarity> ensureApotheosisRarity(ItemStack stack) {
        DynamicHolder<LootRarity> rarity = AffixHelper.getRarity(stack);
        if (rarity.isBound()) return rarity;

        rarity = getDefaultRarity();
        if (rarity.isBound()) AffixHelper.setRarity(stack, rarity.get());
        return rarity;
    }

    private static DynamicHolder<LootRarity> getDefaultRarity() {
        DynamicHolder<LootRarity> rarity = RarityRegistry.byLegacyId(DEFAULT_RARITY);
        if (rarity.isBound()) return rarity;

        List<DynamicHolder<LootRarity>> orderedRarities = RarityRegistry.INSTANCE.getOrderedRarities();
        return orderedRarities.isEmpty() ? RarityRegistry.INSTANCE.emptyHolder() : orderedRarities.getFirst();
    }

    private static DynamicHolder<LootRarity> getGemRarity(ItemStack gemStack) {
        GemInstance gem = GemInstance.unsocketed(gemStack);
        return gem.isValidUnsocketed() ? gem.rarity() : getDefaultRarity();
    }

    private static String getRarityId(DynamicHolder<LootRarity> rarity) {
        return rarity.isBound() ? rarity.getId().toString() : DEFAULT_RARITY;
    }

    private static int getUpgradeDustCost(DynamicHolder<LootRarity> rarity) {
        return rarity.isBound() ? Math.max(1, rarity.get().ordinal() * 2 + 1) : 1;
    }

    private static ItemStack createGemStack(DynamicHolder<Gem> gem, DynamicHolder<LootRarity> rarity, int count) {
        if (!gem.isBound() || !rarity.isBound()) return ItemStack.EMPTY;
        ItemStack gemStack = new ItemStack(Adventure.Items.GEM.get(), count);
        GemItem.setGem(gemStack, gem.get());
        AffixHelper.setRarity(gemStack, rarity.get());
        return gemStack;
    }

    private static ItemStack createUnboundGemStack(ResourceLocation gemId, ResourceLocation rarityId, int count) {
        ItemStack gemStack = new ItemStack(Adventure.Items.GEM.get(), count);
        gemStack.getOrCreateTag().putString(GemItem.GEM, gemId.toString());
        CompoundTag affixData = gemStack.getOrCreateTagElement(AffixHelper.AFFIX_DATA);
        affixData.putString(AffixHelper.RARITY, rarityId.toString());
        return gemStack;
    }

    private static ResourceLocation getRarityIdByOrdinal(int rarity) {
        if (rarity < 0 || rarity >= APOTHEOSIS_RARITY_IDS.size()) return APOTHEOSIS_RARITY_IDS.getFirst();
        return APOTHEOSIS_RARITY_IDS.get(rarity);
    }

    /**
     * 通过字符串获取宝石
     */
    public static ItemStack getGem(int rarity, String gem) {
        ResourceLocation gemId = ResourceLocation.tryParse(gem);
        if (gemId == null) throw new IllegalArgumentException("Invalid Apotheosis gem id: " + gem);

        ResourceLocation rarityId = getRarityIdByOrdinal(rarity);
        DynamicHolder<Gem> gemHolder = GemRegistry.INSTANCE.holder(gemId);
        DynamicHolder<LootRarity> rarityHolder = RarityRegistry.INSTANCE.holder(rarityId);
        if (gemHolder.isBound() && rarityHolder.isBound())
            return GemRegistry.createGemStack(gemHolder.get(), rarityHolder.get());

        return createUnboundGemStack(gemId, rarityId, 1);
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        circuit = unit.getCircuit(false);
        GTRecipeDefinition recipe = null;
        switch (circuit) {
            case 1, 2, 3, 4 -> recipe = getDisassembleRecipe(unit);
            case 5 -> recipe = getEnchantmentsLoadRecipe(unit);
            case 6 -> recipe = getEnchantedBooksMergeRecipe(unit);
            case 7 -> recipe = getAffixCanvasLoadRecipe(unit);
            case 8 -> recipe = getGemSynthesisRecipe(unit);
            case 9 -> recipe = getGemCrushingRecipe(unit);
            case 10 -> recipe = getForcedEnchantmentRecipe(unit);
            case 11 -> recipe = getForcedAffixRecipe(unit);
            case 12 -> recipe = getForcedRarityUpRecipe(unit);
            case 13 -> recipe = getForcedAddSocketRecipe(unit);
            case 14 -> recipe = getForcedMosaicGemRecipe(unit);
        }
        return recipe;
    }
}
