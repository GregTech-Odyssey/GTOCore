package com.gtocore.data.record;

import com.gtocore.common.item.ApothItem;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.entry.ItemEntry;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gtolib.utils.register.ItemRegisterUtils.item;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;

public record EnchantmentRecord(String enchantmentId, int maxLevels, String simplifiedId, String translationKey, int color, String processedId) {

    public static EnchantmentRecord create(String enchantmentId, int maxLevels,
                                           String simplifiedId, String translationKey) {
        int color = generateColorFromId(enchantmentId);
        String processedId = enchantmentId.indexOf(':') > 0 ? enchantmentId.substring(enchantmentId.indexOf(':') + 1) : enchantmentId;
        return new EnchantmentRecord(enchantmentId, maxLevels, simplifiedId, translationKey, color, processedId);
    }

    private static int generateColorFromId(String enchantmentId) {
        int hash = enchantmentId.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        r = Math.max(r, 0x30);
        g = Math.max(g, 0x30);
        b = Math.max(b, 0x30);
        return (r << 16) | (g << 8) | b;
    }

    private static List<EnchantmentRecord> ENCHANTMENTS = new ArrayList<>();

    static {
        addRecord("original", 0, "原始", "original");
        addRecord("apotheosis:bane_of_illagers", 5, "灾厄村民杀手", "enchantment.apotheosis.bane_of_illagers");
        addRecord("apotheosis:berserkers_fury", 3, "狂战士之怒", "enchantment.apotheosis.berserkers_fury");
        addRecord("apotheosis:capturing", 5, "捕捉", "enchantment.apotheosis.capturing");
        addRecord("apotheosis:chainsaw", 1, "链锯", "enchantment.apotheosis.chainsaw");
        addRecord("apotheosis:chromatic", 1, "色差", "enchantment.apotheosis.chromatic");
        addRecord("apotheosis:crescendo", 5, "叠装弩箭", "enchantment.apotheosis.crescendo");
        addRecord("apotheosis:earths_boon", 3, "地球恩惠", "enchantment.apotheosis.earths_boon");
        addRecord("apotheosis:endless_quiver", 1, "无尽箭袋", "enchantment.apotheosis.endless_quiver");
        addRecord("apotheosis:exploitation", 1, "剥削", "enchantment.apotheosis.exploitation");
        addRecord("apotheosis:growth_serum", 1, "生长血清", "enchantment.apotheosis.growth_serum");
        addRecord("apotheosis:icy_thorns", 3, "寒冰荆棘", "enchantment.apotheosis.icy_thorns");
        addRecord("apotheosis:infusion", 1, "灌注", "enchantment.apotheosis.infusion");
        addRecord("apotheosis:knowledge", 3, "岁月学识", "enchantment.apotheosis.knowledge");
        addRecord("apotheosis:life_mending", 3, "生命修补", "enchantment.apotheosis.life_mending");
        addRecord("apotheosis:miners_fervor", 5, "矿工热忱", "enchantment.apotheosis.miners_fervor");
        addRecord("apotheosis:natures_blessing", 3, "自然祝福", "enchantment.apotheosis.natures_blessing");
        addRecord("apotheosis:obliteration", 1, "分裂", "enchantment.apotheosis.obliteration");
        addRecord("apotheosis:rebounding", 3, "弹飞", "enchantment.apotheosis.rebounding");
        addRecord("apotheosis:reflective", 5, "防御反击", "enchantment.apotheosis.reflective");
        addRecord("apotheosis:scavenger", 3, "清道夫", "enchantment.apotheosis.scavenger");
        addRecord("apotheosis:shield_bash", 4, "盾击", "enchantment.apotheosis.shield_bash");
        addRecord("apotheosis:spearfishing", 5, "叉鱼", "enchantment.apotheosis.spearfishing");
        addRecord("apotheosis:splitting", 1, "拆分", "enchantment.apotheosis.splitting");
        addRecord("apotheosis:stable_footing", 1, "稳定立足", "enchantment.apotheosis.stable_footing");
        addRecord("apotheosis:tempting", 1, "引诱", "enchantment.apotheosis.tempting");
        addRecord("ars_nouveau:mana_boost", 3, "魔力提升", "enchantment.ars_nouveau.mana_boost");
        addRecord("ars_nouveau:mana_regen", 3, "魔力再生", "enchantment.ars_nouveau.mana_regen");
        addRecord("ars_nouveau:reactive", 3, "反应", "enchantment.ars_nouveau.reactive");
        addRecord("deeperdarker:catalysis", 3, "催发", "enchantment.deeperdarker.catalysis");
        addRecord("deeperdarker:sculk_smite", 5, "幽匿杀手", "enchantment.deeperdarker.sculk_smite");
        addRecord("farmersdelight:backstabbing", 3, "背刺", "enchantment.farmersdelight.backstabbing");
        addRecord("minecraft:aqua_affinity", 1, "水下速掘", "enchantment.minecraft.aqua_affinity");
        addRecord("minecraft:bane_of_arthropods", 5, "节肢杀手", "enchantment.minecraft.bane_of_arthropods");
        addRecord("minecraft:binding_curse", 1, "绑定诅咒", "enchantment.minecraft.binding_curse");
        addRecord("minecraft:blast_protection", 4, "爆炸保护", "enchantment.minecraft.blast_protection");
        addRecord("minecraft:channeling", 1, "引雷", "enchantment.minecraft.channeling");
        addRecord("minecraft:depth_strider", 3, "深海探索者", "enchantment.minecraft.depth_strider");
        addRecord("minecraft:efficiency", 5, "效率", "enchantment.minecraft.efficiency");
        addRecord("minecraft:feather_falling", 4, "摔落缓冲", "enchantment.minecraft.feather_falling");
        addRecord("minecraft:fire_aspect", 2, "火焰附加", "enchantment.minecraft.fire_aspect");
        addRecord("minecraft:fire_protection", 4, "火焰保护", "enchantment.minecraft.fire_protection");
        addRecord("minecraft:flame", 1, "火矢", "enchantment.minecraft.flame");
        addRecord("minecraft:fortune", 3, "时运", "enchantment.minecraft.fortune");
        addRecord("minecraft:frost_walker", 2, "冰霜行者", "enchantment.minecraft.frost_walker");
        addRecord("minecraft:impaling", 5, "穿刺", "enchantment.minecraft.impaling");
        addRecord("minecraft:infinity", 1, "无限", "enchantment.minecraft.infinity");
        addRecord("minecraft:knockback", 2, "击退", "enchantment.minecraft.knockback");
        addRecord("minecraft:looting", 3, "抢夺", "enchantment.minecraft.looting");
        addRecord("minecraft:loyalty", 3, "忠诚", "enchantment.minecraft.loyalty");
        addRecord("minecraft:luck_of_the_sea", 3, "海之眷顾", "enchantment.minecraft.luck_of_the_sea");
        addRecord("minecraft:lure", 3, "饵钓", "enchantment.minecraft.lure");
        addRecord("minecraft:mending", 1, "经验修补", "enchantment.minecraft.mending");
        addRecord("minecraft:multishot", 1, "多重射击", "enchantment.minecraft.multishot");
        addRecord("minecraft:piercing", 4, "穿透", "enchantment.minecraft.piercing");
        addRecord("minecraft:power", 5, "力量", "enchantment.minecraft.power");
        addRecord("minecraft:projectile_protection", 4, "弹射物保护", "enchantment.minecraft.projectile_protection");
        addRecord("minecraft:protection", 4, "保护", "enchantment.minecraft.protection");
        addRecord("minecraft:punch", 2, "冲击", "enchantment.minecraft.punch");
        addRecord("minecraft:quick_charge", 3, "快速装填", "enchantment.minecraft.quick_charge");
        addRecord("minecraft:respiration", 3, "水下呼吸", "enchantment.minecraft.respiration");
        addRecord("minecraft:riptide", 3, "激流", "enchantment.minecraft.riptide");
        addRecord("minecraft:sharpness", 5, "锋利", "enchantment.minecraft.sharpness");
        addRecord("minecraft:silk_touch", 1, "精准采集", "enchantment.minecraft.silk_touch");
        addRecord("minecraft:smite", 5, "亡灵杀手", "enchantment.minecraft.smite");
        addRecord("minecraft:soul_speed", 3, "灵魂疾行", "enchantment.minecraft.soul_speed");
        addRecord("minecraft:sweeping", 3, "横扫之刃", "enchantment.minecraft.sweeping");
        addRecord("minecraft:swift_sneak", 3, "迅捷潜行", "enchantment.minecraft.swift_sneak");
        addRecord("minecraft:thorns", 3, "荆棘", "enchantment.minecraft.thorns");
        addRecord("minecraft:unbreaking", 3, "耐久", "enchantment.minecraft.unbreaking");
        addRecord("minecraft:vanishing_curse", 1, "消失诅咒", "enchantment.minecraft.vanishing_curse");
        addRecord("mythicbotany:hammer_mobility", 5, "快速挥锤", "enchantment.mythicbotany.hammer_mobility");
    }

    // 辅助方法：添加记录到Map
    private static void addRecord(String enchantmentId, int maxLevels, String simplifiedId, String translationKey) {
        var record = create(enchantmentId, maxLevels, simplifiedId, translationKey);
        ENCHANTMENTS.add(record);
    }

    // 生成附魔书
    public static ItemStack getEnchantedBookByEnchantmentId(String enchantment, int lvl) {
        if (!isEnchantmentValid(enchantment)) {
            return new ItemStack(Items.ENCHANTED_BOOK);
        }
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        CompoundTag bookTag = enchantedBook.getOrCreateTag();
        ListTag storedEnchantments = bookTag.getList("StoredEnchantments", TAG_COMPOUND);
        CompoundTag enchantTag = new CompoundTag();
        enchantTag.putString("id", enchantment);
        enchantTag.putShort("lvl", (short) lvl);
        storedEnchantments.add(enchantTag);
        bookTag.put("StoredEnchantments", storedEnchantments);
        enchantedBook.setTag(bookTag);
        return enchantedBook;
    }

    private static boolean isEnchantmentValid(String enchantmentId) {
        ResourceLocation rl = RLUtils.parse(enchantmentId);
        return BuiltInRegistries.ENCHANTMENT.containsKey(rl);
    }

    public final static Map<Item, EnchantmentRecord> ENCHANTMENT_ITEM_MAP = new Reference2ReferenceOpenHashMap<>();

    public static Map<String, ItemEntry<ApothItem>> registerEnchantmentEssence() {
        ImmutableMap.Builder<String, ItemEntry<ApothItem>> entries = ImmutableMap.builder();
        for (var record : ENCHANTMENTS) {
            String itemId = "enchantment_essence_" + record.processedId();
            String cnName = "附魔精粹 (" + record.simplifiedId() + ")";
            String enName = "Enchantment Essence (" + FormattingUtil.toEnglishName(record.processedId()) + ")";

            ItemEntry<ApothItem> entry = item(itemId, cnName, p -> ApothItem.create(p, record.color()))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/apoth/orb0"), GTOCore.id("item/apoth/orb1")))
                    .lang(enName)
                    .color(() -> ApothItem::color)
                    .tag(Tags.ENCHANTMENT_ESSENCE)
                    .onRegister(i -> ENCHANTMENT_ITEM_MAP.put(i, record))
                    .register();
            entries.put(record.enchantmentId(), entry);
        }
        ENCHANTMENTS = null;
        return entries.build();
    }
}
