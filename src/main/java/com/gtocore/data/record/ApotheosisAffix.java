package com.gtocore.data.record;

import com.gtocore.common.item.ApothItem;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;

import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;

import static com.gtolib.utils.register.ItemRegisterUtils.item;

public class ApotheosisAffix {

    public record ApotheosisAffixRecord(
                                        int serialNumber,
                                        String affixId,
                                        String enId,
                                        String cnId,
                                        int color) {

        public static ApotheosisAffixRecord create(int serialNumber, String apotheosisAffixId,
                                                   String enId, String cnId) {
            int color = generateColorFromId(apotheosisAffixId);
            return new ApotheosisAffixRecord(serialNumber, apotheosisAffixId,
                    enId, cnId, color);
        }
    }

    // 从apotheosisAffixId生成颜色的方法
    private static int generateColorFromId(String apotheosisAffixId) {
        int hash = apotheosisAffixId.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        r = Math.max(r, 0x30);
        g = Math.max(g, 0x30);
        b = Math.max(b, 0x30);
        return (r << 16) | (g << 8) | b;
    }

    private static final List<ApotheosisAffixRecord> records = ApotheosisAffix.initializeApotheosisAffixRecords();

    // 根据serialNumber查询单个结果
    public static ApotheosisAffixRecord findBySerialNumber(int serialNumber) {
        return records.stream()
                .filter(r -> r.serialNumber() == serialNumber)
                .findFirst()
                .orElse(null);
    }

    // 根据apotheosisAffixId查询单个结果
    public static ApotheosisAffixRecord findByApotheosisAffixId(String apotheosisAffixId) {
        return records.stream()
                .filter(r -> r.affixId().equals(apotheosisAffixId))
                .findFirst()
                .orElse(null);
    }

    // 根据apotheosisAffixId获取serialNumber
    public static Integer getSerialNumberByApotheosisAffixId(String apotheosisAffixId) {
        ApotheosisAffixRecord record = findByApotheosisAffixId(apotheosisAffixId);
        return record != null ? record.serialNumber() : 0;
    }

    // 根据serialNumber获取apotheosisAffixId
    public static String getApotheosisAffixIdBySerialNumber(int serialNumber) {
        ApotheosisAffixRecord record = findBySerialNumber(serialNumber);
        return record != null ? record.affixId() : "original";
    }

    // 根据精粹数量
    public static int getAffixSize() {
        return initializeApotheosisAffixRecords().size();
    }

    public static List<ApotheosisAffixRecord> initializeApotheosisAffixRecords() {
        List<ApotheosisAffixRecord> records = new ArrayList<>();

        records.add(ApotheosisAffixRecord.create(0, "original", "original", "原始"));
        records.add(ApotheosisAffixRecord.create(1, "apotheosis:armor/attribute/ironforged", "Ironforged · of Iron", "铁铸 · 铁"));
        records.add(ApotheosisAffixRecord.create(2, "apotheosis:sword/attribute/vampiric", "Vampiric · of Bloodletting", "吸血 · 放血"));
        records.add(ApotheosisAffixRecord.create(3, "apotheosis:sword/special/festive", "Festive · of Partying", "节庆 · 派对"));
        records.add(ApotheosisAffixRecord.create(4, "apotheosis:ranged/mob_effect/fleeting", "Fleeting · of the Ranger", "疾驰 · 游侠"));
        records.add(ApotheosisAffixRecord.create(5, "apotheosis:ranged/attribute/elven", "Elven · of the First Archer", "精灵 · 冠军射手"));
        records.add(ApotheosisAffixRecord.create(6, "apotheosis:heavy_weapon/attribute/shredding", "Shredding · of Penetration", "粉碎 · 穿刺"));
        records.add(ApotheosisAffixRecord.create(7, "apotheosis:shield/special/catalyzing", "Catalyzing · of the Converted", "催化 · 转化"));
        records.add(ApotheosisAffixRecord.create(8, "apotheosis:sword/mob_effect/sophisticated", "Sophisticated · of the Ancient Library", "老练 · 古代图书馆"));
        records.add(ApotheosisAffixRecord.create(9, "apotheosis:shield/attribute/stalwart", "Stalwart · of Stubbornness", "坚毅 · 不屈"));
        records.add(ApotheosisAffixRecord.create(10, "apotheosis:armor/dmg_reduction/blast_forged", "Blast-Forged · of the Forge God", "爆诞 · 锻造之神"));
        records.add(ApotheosisAffixRecord.create(11, "apotheosis:heavy_weapon/attribute/berserking", "Berserking · of the Berserker", "狂暴 · 狂战士"));
        records.add(ApotheosisAffixRecord.create(12, "apotheosis:armor/attribute/stalwart", "Stalwart · of Stubbornness", "坚定 · 顽固"));
        records.add(ApotheosisAffixRecord.create(13, "apotheosis:ranged/mob_effect/ivy_laced", "Ivy Laced · of Natural Toxins", "藤绕 · 自然毒素"));
        records.add(ApotheosisAffixRecord.create(14, "apotheosis:ranged/mob_effect/satanic", "Satanic · of the Burning Hells", "邪恶 · 诅咒"));
        records.add(ApotheosisAffixRecord.create(15, "apotheosis:sword/attribute/violent", "Violent · of Slaying", "暴力 · 杀戮"));
        records.add(ApotheosisAffixRecord.create(16, "apotheosis:ranged/special/spectral", "Spectral · of the Forlorn", "光灵 · 被遗弃者"));
        records.add(ApotheosisAffixRecord.create(17, "apotheosis:armor/attribute/winged", "Winged · of the Skies", "有翼 · 天空"));
        records.add(ApotheosisAffixRecord.create(18, "apotheosis:heavy_weapon/attribute/annihilating", "Annihilating · of the Titan", "歼灭 · 泰坦"));
        records.add(ApotheosisAffixRecord.create(19, "apotheosis:heavy_weapon/attribute/forceful", "Forceful · of the Bull", "强力 · 公牛"));
        records.add(ApotheosisAffixRecord.create(20, "apotheosis:shield/mob_effect/withering", "Withering · of the Dark Skeleton", "凋零 · 黑暗骷髅"));
        records.add(ApotheosisAffixRecord.create(21, "apotheosis:armor/mob_effect/nimble", "Nimble · of the Fox", "灵敏 · 狐狸"));
        records.add(ApotheosisAffixRecord.create(22, "apotheosis:breaker/mob_effect/spelunkers", "Spelunker's · of Cave Exploration", "探穴者 · 洞穴探险者"));
        records.add(ApotheosisAffixRecord.create(23, "apotheosis:breaker/special/omnetic", "Omnetic · of the Singularity", "全能 · 奇点"));
        records.add(ApotheosisAffixRecord.create(24, "apotheosis:heavy_weapon/mob_effect/caustic", "Caustic · of the Caustic Realm", "腐蚀 · 腐蚀领域"));
        records.add(ApotheosisAffixRecord.create(25, "apotheosis:ranged/attribute/agile", "Agile · of Dexterity", "敏捷 · 迅敏"));
        records.add(ApotheosisAffixRecord.create(26, "apotheosis:armor/attribute/blessed", "Blessed · of Health", "祝福 · 生命"));
        records.add(ApotheosisAffixRecord.create(27, "apotheosis:armor/dmg_reduction/feathery", "Feathery · of the Eagle", "覆羽 · 鹰"));
        records.add(ApotheosisAffixRecord.create(28, "apotheosis:heavy_weapon/attribute/giant_slaying", "Giant Slaying · of Colossal Strikes", "爆杀 · 巨人打击"));
        records.add(ApotheosisAffixRecord.create(29, "apotheosis:armor/attribute/aquatic", "Aquatic · of the Ocean", "水行 · 海洋"));
        records.add(ApotheosisAffixRecord.create(30, "apotheosis:armor/attribute/spiritual", "Spiritual · of the Witch Doctor", "灵魂 · 巫医"));
        records.add(ApotheosisAffixRecord.create(31, "apotheosis:shield/special/retreating", "Retreating · of the Jester", "后撤 · 弄臣"));
        records.add(ApotheosisAffixRecord.create(32, "apotheosis:armor/attribute/elastic", "Elastic · of the Climb", "灵活 · 攀爬"));
        records.add(ApotheosisAffixRecord.create(33, "apotheosis:heavy_weapon/attribute/murderous", "Murderous · of the Minotaur", "凶残 · 米诺陶"));
        records.add(ApotheosisAffixRecord.create(34, "apotheosis:sword/attribute/intricate", "Intricate · of Critical Thinking", "复杂 · 批判性思维"));
        records.add(ApotheosisAffixRecord.create(35, "apotheosis:ranged/attribute/streamlined", "Streamlined · of the Sniper", "流线型 · 狙击手"));
        records.add(ApotheosisAffixRecord.create(36, "apotheosis:ranged/mob_effect/ensnaring", "Ensnaring · of the Predator", "诱捕 · 捕食者"));
        records.add(ApotheosisAffixRecord.create(37, "apotheosis:ranged/mob_effect/acidic", "Acidic · of Corrosion", "酸蚀 · 侵蚀"));
        records.add(ApotheosisAffixRecord.create(38, "apotheosis:breaker/attribute/lengthy", "Lengthy · of Grasping", "伸长 · 抓握"));
        records.add(ApotheosisAffixRecord.create(39, "apotheosis:sword/attribute/lacerating", "Lacerating · of Surgical Precision", "撕裂 · 精准打击"));
        records.add(ApotheosisAffixRecord.create(40, "apotheosis:armor/mob_effect/revitalizing", "Revitalizing · of the Wellspring", "新生 · 源泉"));
        records.add(ApotheosisAffixRecord.create(41, "apotheosis:breaker/attribute/lucky", "Lucky · of the Serendipitous", "幸运 · 偶然"));
        records.add(ApotheosisAffixRecord.create(42, "apotheosis:breaker/attribute/destructive", "Destructive · of Quarrying", "破坏 · 凿岩机"));
        records.add(ApotheosisAffixRecord.create(43, "apotheosis:breaker/mob_effect/swift", "Swift · of Smashing", "迅捷 · 粉碎"));
        records.add(ApotheosisAffixRecord.create(44, "apotheosis:heavy_weapon/special/cleaving", "Cleaving · of the Butcher", "挥劈 · 屠夫"));
        records.add(ApotheosisAffixRecord.create(45, "apotheosis:ranged/attribute/windswept", "Windswept · of the Current", "风袭 · 气流"));
        records.add(ApotheosisAffixRecord.create(46, "apotheosis:sword/attribute/elongated", "Elongated · of Distant Battles", "延展 · 远战"));
        records.add(ApotheosisAffixRecord.create(47, "apotheosis:ranged/mob_effect/grievous", "Grievous · of Wounding", "重创 · 伤口"));
        records.add(ApotheosisAffixRecord.create(48, "apotheosis:sword/attribute/piercing", "Piercing · of the Sunderer", "穿刺 · 撕裂者"));
        records.add(ApotheosisAffixRecord.create(49, "apotheosis:armor/attribute/gravitational", "Gravitational · of Gravity", "重力 · 重力"));
        records.add(ApotheosisAffixRecord.create(50, "apotheosis:armor/attribute/windswept", "Windswept · the Windrider", "风袭 · 乘风"));
        records.add(ApotheosisAffixRecord.create(51, "apotheosis:breaker/special/enlightened", "Enlightened · of the Lightbringer", "启明 · 携光者"));
        records.add(ApotheosisAffixRecord.create(52, "apotheosis:ranged/special/magical", "Magical · of the Arcane", "魔法 · 奥秘"));
        records.add(ApotheosisAffixRecord.create(53, "apotheosis:sword/attribute/infernal", "Infernal · of the Sun", "地狱 · 太阳"));
        records.add(ApotheosisAffixRecord.create(54, "apotheosis:heavy_weapon/attribute/decimating", "Decimating · of the God-King", "屠戮 · 神王"));
        records.add(ApotheosisAffixRecord.create(55, "apotheosis:breaker/special/radial", "Radial · of the Earthbreaker", "范围 · 裂地者"));
        records.add(ApotheosisAffixRecord.create(56, "apotheosis:shield/special/psychic", "Psychic · of the Vengeful", "通灵 · 睚眦"));
        records.add(ApotheosisAffixRecord.create(57, "apotheosis:sword/mob_effect/weakening", "Weakening · of Weakness", "弱化 · 虚弱"));
        records.add(ApotheosisAffixRecord.create(58, "apotheosis:armor/mob_effect/blinding", "Blinding · of Darkness", "失明 · 黑暗"));
        records.add(ApotheosisAffixRecord.create(59, "apotheosis:heavy_weapon/attribute/nullifying", "Nullifying · of the Magebreaker", "抹除 · 破法者"));
        records.add(ApotheosisAffixRecord.create(60, "apotheosis:telepathic", "Telepathic · of Space and Time", "念动 · 时空"));
        records.add(ApotheosisAffixRecord.create(61, "apotheosis:sword/special/thunderstruck", "Thunderstruck · of the Storm", "雷鸣 · 风暴"));
        records.add(ApotheosisAffixRecord.create(62, "apotheosis:heavy_weapon/mob_effect/bloodletting", "Caustic · of the Slayer", "腐蚀 · 杀戮者"));
        records.add(ApotheosisAffixRecord.create(63, "apotheosis:armor/attribute/fortunate", "Fortunate · of the Four-Leaf Clover", "幸运 · 四叶草"));
        records.add(ApotheosisAffixRecord.create(64, "apotheosis:armor/attribute/steel_touched", "Steel Touched · of the Defender", "钢触 · 捍卫者"));
        records.add(ApotheosisAffixRecord.create(65, "apotheosis:armor/dmg_reduction/runed", "Runed · of the Spellwarden", "符文 · 咒法监守"));
        records.add(ApotheosisAffixRecord.create(66, "apotheosis:shield/attribute/ironforged", "Ironforged · the Unyielding", "铁铸 · 不屈"));
        records.add(ApotheosisAffixRecord.create(67, "apotheosis:armor/dmg_reduction/blockading", "Blockading · of the Blockade", "封锁 · 屏障"));
        records.add(ApotheosisAffixRecord.create(68, "apotheosis:armor/dmg_reduction/dwarven", "Dwarven · of the Volcano", "矮人 · 火山"));
        records.add(ApotheosisAffixRecord.create(69, "apotheosis:armor/mob_effect/bursting", "Bursting · of Vitality", "迸发 · 活力"));
        records.add(ApotheosisAffixRecord.create(70, "apotheosis:durable", "Durable · of Durability", "耐用 · 耐久"));
        records.add(ApotheosisAffixRecord.create(71, "apotheosis:shield/mob_effect/venomous", "Venomous · of the Snake", "淬毒 · 毒蛇"));
        records.add(ApotheosisAffixRecord.create(72, "apotheosis:ranged/mob_effect/shulkers", "Shulk-Touched · of Levitation", "潜影 · 飘浮"));
        records.add(ApotheosisAffixRecord.create(73, "apotheosis:heavy_weapon/special/executing", "Executing · of the Executioner", "行刑 · 刽子手"));
        records.add(ApotheosisAffixRecord.create(74, "apotheosis:breaker/attribute/experienced", "Experienced · of the Scholar", "经验 · 学者"));
        records.add(ApotheosisAffixRecord.create(75, "apotheosis:sword/attribute/glacial", "Glacial · of the Frostlands", "冰川 · 冻土"));
        records.add(ApotheosisAffixRecord.create(76, "apotheosis:shield/attribute/steel_touched", "Steel Touched · the Oathkeeper", "钢触 · 不破之誓"));
        records.add(ApotheosisAffixRecord.create(77, "apotheosis:sword/mob_effect/elusive", "Elusive · of Evasion", "灵巧 · 闪避"));
        records.add(ApotheosisAffixRecord.create(78, "apotheosis:sword/attribute/graceful", "Graceful · of the Duelist", "优雅 · 决斗者"));
        records.add(ApotheosisAffixRecord.create(79, "apotheosis:armor/mob_effect/bolstering", "Bolstering · of Fortitude", "支撑 · 坚韧"));
        records.add(ApotheosisAffixRecord.create(80, "apotheosis:sword/attribute/spellbreaking", "Spellbreaking · of the Petricite Golem", "破法 · 岩石傀儡"));
        records.add(ApotheosisAffixRecord.create(81, "apotheosis:shield/mob_effect/devilish", "Devilish · of the Veteran", "残忍 · 老兵"));
        return records;
    }

    public static ItemEntry<ApothItem>[] registerAffixEssence() {
        List<ApotheosisAffixRecord> records = initializeApotheosisAffixRecords();
        ItemEntry<ApothItem>[] entries = new ItemEntry[getAffixSize()];
        for (ApotheosisAffixRecord record : records) {
            String id = record.affixId().substring(record.affixId().indexOf(':') + 1).replace("/", "_");
            String cn = "刻印精粹 " + "(" + record.cnId() + ")";
            String en = "Affix Essence " + "(" + record.enId() + ")";
            entries[record.serialNumber()] = item("affix_essence_" + record.serialNumber(), cn, p -> ApothItem.create(p, record.color()))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/apoth/fabric0"), GTOCore.id("item/apoth/fabric1")))
                    .lang(en)
                    .color(() -> ApothItem::color)
                    .tag(Tags.AFFIX_ESSENCE)
                    .register();
        }
        return entries;
    }
}
