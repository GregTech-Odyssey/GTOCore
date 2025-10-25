package com.gtocore.data.record;

import com.gtocore.common.item.ApothItem;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;

import static com.gtolib.utils.register.ItemRegisterUtils.item;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;

public class EnchantmentRecord {

    public record EnchantmentRecords(
                                     int serialNumber,
                                     String enchantmentId,
                                     int maxLevels,
                                     String simplifiedId,
                                     String translationKey,
                                     int color) {

        public static EnchantmentRecords create(int serialNumber, String enchantmentId,
                                                int maxLevels, String simplifiedId,
                                                String translationKey) {
            int color = generateColorFromId(enchantmentId);
            return new EnchantmentRecords(serialNumber, enchantmentId, maxLevels,
                    simplifiedId, translationKey, color);
        }
    }

    // 从enchantmentId生成颜色的方法
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

    private static final List<EnchantmentRecords> records = com.gtocore.data.record.EnchantmentRecord.initializeEnchantmentRecords();

    // 根据serialNumber查询单个结果
    public static EnchantmentRecords findBySerialNumber(int serialNumber) {
        return records.stream()
                .filter(r -> r.serialNumber() == serialNumber)
                .findFirst()
                .orElse(null);
    }

    // 根据enchantmentId查询单个结果
    public static EnchantmentRecords findByEnchantmentId(String enchantmentId) {
        return records.stream()
                .filter(r -> r.enchantmentId().equals(enchantmentId))
                .findFirst()
                .orElse(null);
    }

    // 根据enchantmentId获取serialNumber
    public static int getSerialNumberByEnchantmentId(String enchantmentId) {
        EnchantmentRecords record = findByEnchantmentId(enchantmentId);
        return record != null ? record.serialNumber() : 0;
    }

    // 根据serialNumber获取enchantmentId
    public static String getEnchantmentIdBySerialNumber(int serialNumber) {
        EnchantmentRecords record = findBySerialNumber(serialNumber);
        return record != null ? record.enchantmentId() : "original";
    }

    // 根据精粹数量
    public static int getEnchantmentSize() {
        return initializeEnchantmentRecords().size();
    }

    // 获取附魔书
    public static ItemStack getEnchantedBookByEnchantmentId(String enchantment, int lvl) {
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

    public static ItemStack getEnchantedBookBySerialNumber(int serialNumber, int lvl) {
        String enchantment = getEnchantmentIdBySerialNumber(serialNumber);
        return getEnchantedBookByEnchantmentId(enchantment, lvl);
    }

    // 初始化附魔记录列表
    public static List<EnchantmentRecords> initializeEnchantmentRecords() {
        List<EnchantmentRecords> records = new ArrayList<>();

        records.add(EnchantmentRecords.create(0, "original", 0, "原始", "original"));
        records.add(EnchantmentRecords.create(1, "apotheosis:bane_of_illagers", 5, "灾厄村民杀手", "enchantment.apotheosis.bane_of_illagers"));
        records.add(EnchantmentRecords.create(2, "apotheosis:berserkers_fury", 3, "狂战士之怒", "enchantment.apotheosis.berserkers_fury"));
        records.add(EnchantmentRecords.create(3, "apotheosis:capturing", 5, "捕捉", "enchantment.apotheosis.capturing"));
        records.add(EnchantmentRecords.create(4, "apotheosis:chainsaw", 1, "链锯", "enchantment.apotheosis.chainsaw"));
        records.add(EnchantmentRecords.create(5, "apotheosis:chromatic", 1, "色差", "enchantment.apotheosis.chromatic"));
        records.add(EnchantmentRecords.create(6, "apotheosis:crescendo", 5, "叠装弩箭", "enchantment.apotheosis.crescendo"));
        records.add(EnchantmentRecords.create(7, "apotheosis:earths_boon", 3, "地球恩惠", "enchantment.apotheosis.earths_boon"));
        records.add(EnchantmentRecords.create(8, "apotheosis:endless_quiver", 1, "无尽箭袋", "enchantment.apotheosis.endless_quiver"));
        records.add(EnchantmentRecords.create(9, "apotheosis:exploitation", 1, "剥削", "enchantment.apotheosis.exploitation"));
        records.add(EnchantmentRecords.create(10, "apotheosis:growth_serum", 1, "生长血清", "enchantment.apotheosis.growth_serum"));
        records.add(EnchantmentRecords.create(11, "apotheosis:icy_thorns", 3, "寒冰荆棘", "enchantment.apotheosis.icy_thorns"));
        records.add(EnchantmentRecords.create(12, "apotheosis:infusion", 1, "灌注", "enchantment.apotheosis.infusion"));
        records.add(EnchantmentRecords.create(13, "apotheosis:knowledge", 3, "岁月学识", "enchantment.apotheosis.knowledge"));
        records.add(EnchantmentRecords.create(14, "apotheosis:life_mending", 3, "生命修补", "enchantment.apotheosis.life_mending"));
        records.add(EnchantmentRecords.create(15, "apotheosis:miners_fervor", 5, "矿工热忱", "enchantment.apotheosis.miners_fervor"));
        records.add(EnchantmentRecords.create(16, "apotheosis:natures_blessing", 3, "自然祝福", "enchantment.apotheosis.natures_blessing"));
        records.add(EnchantmentRecords.create(17, "apotheosis:obliteration", 1, "分裂", "enchantment.apotheosis.obliteration"));
        records.add(EnchantmentRecords.create(18, "apotheosis:rebounding", 3, "弹飞", "enchantment.apotheosis.rebounding"));
        records.add(EnchantmentRecords.create(19, "apotheosis:reflective", 5, "防御反击", "enchantment.apotheosis.reflective"));
        records.add(EnchantmentRecords.create(20, "apotheosis:scavenger", 3, "清道夫", "enchantment.apotheosis.scavenger"));
        records.add(EnchantmentRecords.create(21, "apotheosis:shield_bash", 4, "盾击", "enchantment.apotheosis.shield_bash"));
        records.add(EnchantmentRecords.create(22, "apotheosis:spearfishing", 5, "叉鱼", "enchantment.apotheosis.spearfishing"));
        records.add(EnchantmentRecords.create(23, "apotheosis:splitting", 1, "拆分", "enchantment.apotheosis.splitting"));
        records.add(EnchantmentRecords.create(24, "apotheosis:stable_footing", 1, "稳定立足", "enchantment.apotheosis.stable_footing"));
        records.add(EnchantmentRecords.create(25, "apotheosis:tempting", 1, "引诱", "enchantment.apotheosis.tempting"));
        records.add(EnchantmentRecords.create(26, "ars_nouveau:mana_boost", 3, "魔力提升", "enchantment.ars_nouveau.mana_boost"));
        records.add(EnchantmentRecords.create(27, "ars_nouveau:mana_regen", 3, "魔力再生", "enchantment.ars_nouveau.mana_regen"));
        records.add(EnchantmentRecords.create(28, "ars_nouveau:reactive", 3, "反应", "enchantment.ars_nouveau.reactive"));
        records.add(EnchantmentRecords.create(29, "deeperdarker:catalysis", 3, "催发", "enchantment.deeperdarker.catalysis"));
        records.add(EnchantmentRecords.create(30, "deeperdarker:sculk_smite", 5, "幽匿杀手", "enchantment.deeperdarker.sculk_smite"));
        records.add(EnchantmentRecords.create(31, "enderio:auto_smelt", 1, "自动烧炼", "enchantment.enderio.auto_smelt"));
        records.add(EnchantmentRecords.create(32, "enderio:repellent", 4, "驱散传送", "enchantment.enderio.repellent"));
        records.add(EnchantmentRecords.create(33, "enderio:shimmer", 1, "微光闪闪", "enchantment.enderio.shimmer"));
        records.add(EnchantmentRecords.create(34, "enderio:soulbound", 1, "灵魂绑定", "enchantment.enderio.soulbound"));
        records.add(EnchantmentRecords.create(35, "enderio:withering", 1, "凋亡", "enchantment.enderio.withering"));
        records.add(EnchantmentRecords.create(36, "enderio:xp_boost", 3, "经验汲取", "enchantment.enderio.xp_boost"));
        records.add(EnchantmentRecords.create(37, "farmersdelight:backstabbing", 3, "背刺", "enchantment.farmersdelight.backstabbing"));
        records.add(EnchantmentRecords.create(38, "minecraft:aqua_affinity", 1, "水下速掘", "enchantment.minecraft.aqua_affinity"));
        records.add(EnchantmentRecords.create(39, "minecraft:bane_of_arthropods", 5, "节肢杀手", "enchantment.minecraft.bane_of_arthropods"));
        records.add(EnchantmentRecords.create(40, "minecraft:binding_curse", 1, "绑定诅咒", "enchantment.minecraft.binding_curse"));
        records.add(EnchantmentRecords.create(41, "minecraft:blast_protection", 4, "爆炸保护", "enchantment.minecraft.blast_protection"));
        records.add(EnchantmentRecords.create(42, "minecraft:channeling", 1, "引雷", "enchantment.minecraft.channeling"));
        records.add(EnchantmentRecords.create(43, "minecraft:depth_strider", 3, "深海探索者", "enchantment.minecraft.depth_strider"));
        records.add(EnchantmentRecords.create(44, "minecraft:efficiency", 5, "效率", "enchantment.minecraft.efficiency"));
        records.add(EnchantmentRecords.create(45, "minecraft:feather_falling", 4, "摔落缓冲", "enchantment.minecraft.feather_falling"));
        records.add(EnchantmentRecords.create(46, "minecraft:fire_aspect", 2, "火焰附加", "enchantment.minecraft.fire_aspect"));
        records.add(EnchantmentRecords.create(47, "minecraft:fire_protection", 4, "火焰保护", "enchantment.minecraft.fire_protection"));
        records.add(EnchantmentRecords.create(48, "minecraft:flame", 1, "火矢", "enchantment.minecraft.flame"));
        records.add(EnchantmentRecords.create(49, "minecraft:fortune", 3, "时运", "enchantment.minecraft.fortune"));
        records.add(EnchantmentRecords.create(50, "minecraft:frost_walker", 2, "冰霜行者", "enchantment.minecraft.frost_walker"));
        records.add(EnchantmentRecords.create(51, "minecraft:impaling", 5, "穿刺", "enchantment.minecraft.impaling"));
        records.add(EnchantmentRecords.create(52, "minecraft:infinity", 1, "无限", "enchantment.minecraft.infinity"));
        records.add(EnchantmentRecords.create(53, "minecraft:knockback", 2, "击退", "enchantment.minecraft.knockback"));
        records.add(EnchantmentRecords.create(54, "minecraft:looting", 3, "抢夺", "enchantment.minecraft.looting"));
        records.add(EnchantmentRecords.create(55, "minecraft:loyalty", 3, "忠诚", "enchantment.minecraft.loyalty"));
        records.add(EnchantmentRecords.create(56, "minecraft:luck_of_the_sea", 3, "海之眷顾", "enchantment.minecraft.luck_of_the_sea"));
        records.add(EnchantmentRecords.create(57, "minecraft:lure", 3, "饵钓", "enchantment.minecraft.lure"));
        records.add(EnchantmentRecords.create(58, "minecraft:mending", 1, "经验修补", "enchantment.minecraft.mending"));
        records.add(EnchantmentRecords.create(59, "minecraft:multishot", 1, "多重射击", "enchantment.minecraft.multishot"));
        records.add(EnchantmentRecords.create(60, "minecraft:piercing", 4, "穿透", "enchantment.minecraft.piercing"));
        records.add(EnchantmentRecords.create(61, "minecraft:power", 5, "力量", "enchantment.minecraft.power"));
        records.add(EnchantmentRecords.create(62, "minecraft:projectile_protection", 4, "弹射物保护", "enchantment.minecraft.projectile_protection"));
        records.add(EnchantmentRecords.create(63, "minecraft:protection", 4, "保护", "enchantment.minecraft.protection"));
        records.add(EnchantmentRecords.create(64, "minecraft:punch", 2, "冲击", "enchantment.minecraft.punch"));
        records.add(EnchantmentRecords.create(65, "minecraft:quick_charge", 3, "快速装填", "enchantment.minecraft.quick_charge"));
        records.add(EnchantmentRecords.create(66, "minecraft:respiration", 3, "水下呼吸", "enchantment.minecraft.respiration"));
        records.add(EnchantmentRecords.create(67, "minecraft:riptide", 3, "激流", "enchantment.minecraft.riptide"));
        records.add(EnchantmentRecords.create(68, "minecraft:sharpness", 5, "锋利", "enchantment.minecraft.sharpness"));
        records.add(EnchantmentRecords.create(69, "minecraft:silk_touch", 1, "精准采集", "enchantment.minecraft.silk_touch"));
        records.add(EnchantmentRecords.create(70, "minecraft:smite", 5, "亡灵杀手", "enchantment.minecraft.smite"));
        records.add(EnchantmentRecords.create(71, "minecraft:soul_speed", 3, "灵魂疾行", "enchantment.minecraft.soul_speed"));
        records.add(EnchantmentRecords.create(72, "minecraft:sweeping", 3, "横扫之刃", "enchantment.minecraft.sweeping"));
        records.add(EnchantmentRecords.create(73, "minecraft:swift_sneak", 3, "迅捷潜行", "enchantment.minecraft.swift_sneak"));
        records.add(EnchantmentRecords.create(74, "minecraft:thorns", 3, "荆棘", "enchantment.minecraft.thorns"));
        records.add(EnchantmentRecords.create(75, "minecraft:unbreaking", 3, "耐久", "enchantment.minecraft.unbreaking"));
        records.add(EnchantmentRecords.create(76, "minecraft:vanishing_curse", 1, "消失诅咒", "enchantment.minecraft.vanishing_curse"));
        records.add(EnchantmentRecords.create(77, "mythicbotany:hammer_mobility", 5, "迅锤", "enchantment.mythicbotany.hammer_mobility"));
        return records;
    }

    public static ItemEntry<ApothItem>[] registerEnchantmentEssence() {
        List<EnchantmentRecords> records = initializeEnchantmentRecords();
        ItemEntry<ApothItem>[] entries = new ItemEntry[getEnchantmentSize()];
        for (EnchantmentRecords record : records) {
            String id = record.enchantmentId().substring(record.enchantmentId().indexOf(':') + 1);
            String cn = "附魔精粹 " + "(" + record.simplifiedId() + ")";
            String en = "Enchantment Essence " + "(" + FormattingUtil.toEnglishName(id) + ")";
            entries[record.serialNumber()] = item("enchantment_essence_" + record.serialNumber(), cn, p -> ApothItem.create(p, record.color()))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/apoth/orb0"), GTOCore.id("item/apoth/orb1")))
                    .lang(en)
                    .color(() -> ApothItem::color)
                    .tag(Tags.ENCHANTMENT_ESSENCE)
                    .register();
        }
        return entries;
    }
}
