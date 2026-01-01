package com.gtocore.data.record;

import com.gtocore.common.item.ApothItem;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;

import net.minecraft.world.item.Item;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.entry.ItemEntry;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.util.*;

import static com.gtolib.utils.register.ItemRegisterUtils.item;

public record ApotheosisAffixRecord(String affixId, String enId, String cnId, int color, String processedId) {

    public static ApotheosisAffixRecord create(String apotheosisAffixId, String enId, String cnId) {
        int color = generateColorFromId(apotheosisAffixId);
        String processedId = apotheosisAffixId.indexOf(':') > 0 ? apotheosisAffixId.substring(apotheosisAffixId.indexOf(':') + 1).replace("/", "_") : apotheosisAffixId;
        return new ApotheosisAffixRecord(apotheosisAffixId, enId, cnId, color, processedId);
    }

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

    private static List<ApotheosisAffixRecord> AFFIXS = new ArrayList<>();

    static {
        ApotheosisAffixRecord.addRecord("original", "original", "原始");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/ironforged", "Ironforged · of Iron", "铁铸 · 铁");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/vampiric", "Vampiric · of Bloodletting", "吸血 · 放血");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/special/festive", "Festive · of Partying", "节庆 · 派对");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/mob_effect/fleeting", "Fleeting · of the Ranger", "疾驰 · 游侠");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/attribute/elven", "Elven · of the First Archer", "精灵 · 冠军射手");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/attribute/shredding", "Shredding · of Penetration", "粉碎 · 穿刺");
        ApotheosisAffixRecord.addRecord("apotheosis:shield/special/catalyzing", "Catalyzing · of the Converted", "催化 · 转化");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/mob_effect/sophisticated", "Sophisticated · of the Ancient Library", "老练 · 古代图书馆");
        ApotheosisAffixRecord.addRecord("apotheosis:shield/attribute/stalwart", "Stalwart · of Stubbornness", "坚毅 · 不屈");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/dmg_reduction/blast_forged", "Blast-Forged · of the Forge God", "爆诞 · 锻造之神");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/attribute/berserking", "Berserking · of the Berserker", "狂暴 · 狂战士");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/stalwart", "Stalwart · of Stubbornness", "坚定 · 顽固");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/mob_effect/ivy_laced", "Ivy Laced · of Natural Toxins", "藤绕 · 自然毒素");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/mob_effect/satanic", "Satanic · of the Burning Hells", "邪恶 · 诅咒");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/violent", "Violent · of Slaying", "暴力 · 杀戮");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/special/spectral", "Spectral · of the Forlorn", "光灵 · 被遗弃者");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/winged", "Winged · of the Skies", "有翼 · 天空");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/attribute/annihilating", "Annihilating · of the Titan", "歼灭 · 泰坦");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/attribute/forceful", "Forceful · of the Bull", "强力 · 公牛");
        ApotheosisAffixRecord.addRecord("apotheosis:shield/mob_effect/withering", "Withering · of the Dark Skeleton", "凋零 · 黑暗骷髅");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/mob_effect/nimble", "Nimble · of the Fox", "灵敏 · 狐狸");
        ApotheosisAffixRecord.addRecord("apotheosis:breaker/mob_effect/spelunkers", "Spelunker's · of Cave Exploration", "探穴者 · 洞穴探险者");
        ApotheosisAffixRecord.addRecord("apotheosis:breaker/special/omnetic", "Omnetic · of the Singularity", "全能 · 奇点");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/mob_effect/caustic", "Caustic · of the Caustic Realm", "腐蚀 · 腐蚀领域");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/attribute/agile", "Agile · of Dexterity", "敏捷 · 迅敏");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/blessed", "Blessed · of Health", "祝福 · 生命");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/dmg_reduction/feathery", "Feathery · of the Eagle", "覆羽 · 鹰");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/attribute/giant_slaying", "Giant Slaying · of Colossal Strikes", "爆杀 · 巨人打击");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/aquatic", "Aquatic · of the Ocean", "水行 · 海洋");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/spiritual", "Spiritual · of the Witch Doctor", "灵魂 · 巫医");
        ApotheosisAffixRecord.addRecord("apotheosis:shield/special/retreating", "Retreating · of the Jester", "后撤 · 弄臣");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/elastic", "Elastic · of the Climb", "灵活 · 攀爬");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/attribute/murderous", "Murderous · of the Minotaur", "凶残 · 米诺陶");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/intricate", "Intricate · of Critical Thinking", "复杂 · 批判性思维");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/attribute/streamlined", "Streamlined · of the Sniper", "流线型 · 狙击手");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/mob_effect/ensnaring", "Ensnaring · of the Predator", "诱捕 · 捕食者");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/mob_effect/acidic", "Acidic · of Corrosion", "酸蚀 · 侵蚀");
        ApotheosisAffixRecord.addRecord("apotheosis:breaker/attribute/lengthy", "Lengthy · of Grasping", "伸长 · 抓握");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/lacerating", "Lacerating · of Surgical Precision", "撕裂 · 精准打击");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/mob_effect/revitalizing", "Revitalizing · of the Wellspring", "新生 · 源泉");
        ApotheosisAffixRecord.addRecord("apotheosis:breaker/attribute/lucky", "Lucky · of the Serendipitous", "幸运 · 偶然");
        ApotheosisAffixRecord.addRecord("apotheosis:breaker/attribute/destructive", "Destructive · of Quarrying", "破坏 · 凿岩机");
        ApotheosisAffixRecord.addRecord("apotheosis:breaker/mob_effect/swift", "Swift · of Smashing", "迅捷 · 粉碎");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/special/cleaving", "Cleaving · of the Butcher", "挥劈 · 屠夫");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/attribute/windswept", "Windswept · of the Current", "风袭 · 气流");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/elongated", "Elongated · of Distant Battles", "延展 · 远战");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/mob_effect/grievous", "Grievous · of Wounding", "重创 · 伤口");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/piercing", "Piercing · of the Sunderer", "穿刺 · 撕裂者");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/gravitational", "Gravitational · of Gravity", "重力 · 重力");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/windswept", "Windswept · the Windrider", "风袭 · 乘风");
        ApotheosisAffixRecord.addRecord("apotheosis:breaker/special/enlightened", "Enlightened · of the Lightbringer", "启明 · 携光者");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/special/magical", "Magical · of the Arcane", "魔法 · 奥秘");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/infernal", "Infernal · of the Sun", "地狱 · 太阳");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/attribute/decimating", "Decimating · of the God-King", "屠戮 · 神王");
        ApotheosisAffixRecord.addRecord("apotheosis:breaker/special/radial", "Radial · of the Earthbreaker", "范围 · 裂地者");
        ApotheosisAffixRecord.addRecord("apotheosis:shield/special/psychic", "Psychic · of the Vengeful", "通灵 · 睚眦");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/mob_effect/weakening", "Weakening · of Weakness", "弱化 · 虚弱");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/mob_effect/blinding", "Blinding · of Darkness", "失明 · 黑暗");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/attribute/nullifying", "Nullifying · of the Magebreaker", "抹除 · 破法者");
        ApotheosisAffixRecord.addRecord("apotheosis:telepathic", "Telepathic · of Space and Time", "念动 · 时空");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/special/thunderstruck", "Thunderstruck · of the Storm", "雷鸣 · 风暴");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/mob_effect/bloodletting", "Caustic · of the Slayer", "腐蚀 · 杀戮者");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/fortunate", "Fortunate · of the Four-Leaf Clover", "幸运 · 四叶草");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/attribute/steel_touched", "Steel Touched · of the Defender", "钢触 · 捍卫者");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/dmg_reduction/runed", "Runed · of the Spellwarden", "符文 · 咒法监守");
        ApotheosisAffixRecord.addRecord("apotheosis:shield/attribute/ironforged", "Ironforged · the Unyielding", "铁铸 · 不屈");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/dmg_reduction/blockading", "Blockading · of the Blockade", "封锁 · 屏障");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/dmg_reduction/dwarven", "Dwarven · of the Volcano", "矮人 · 火山");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/mob_effect/bursting", "Bursting · of Vitality", "迸发 · 活力");
        ApotheosisAffixRecord.addRecord("apotheosis:durable", "Durable · of Durability", "耐用 · 耐久");
        ApotheosisAffixRecord.addRecord("apotheosis:shield/mob_effect/venomous", "Venomous · of the Snake", "淬毒 · 毒蛇");
        ApotheosisAffixRecord.addRecord("apotheosis:ranged/mob_effect/shulkers", "Shulk-Touched · of Levitation", "潜影 · 飘浮");
        ApotheosisAffixRecord.addRecord("apotheosis:heavy_weapon/special/executing", "Executing · of the Executioner", "行刑 · 刽子手");
        ApotheosisAffixRecord.addRecord("apotheosis:breaker/attribute/experienced", "Experienced · of the Scholar", "经验 · 学者");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/glacial", "Glacial · of the Frostlands", "冰川 · 冻土");
        ApotheosisAffixRecord.addRecord("apotheosis:shield/attribute/steel_touched", "Steel Touched · the Oathkeeper", "钢触 · 不破之誓");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/mob_effect/elusive", "Elusive · of Evasion", "灵巧 · 闪避");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/graceful", "Graceful · of the Duelist", "优雅 · 决斗者");
        ApotheosisAffixRecord.addRecord("apotheosis:armor/mob_effect/bolstering", "Bolstering · of Fortitude", "支撑 · 坚韧");
        ApotheosisAffixRecord.addRecord("apotheosis:sword/attribute/spellbreaking", "Spellbreaking · of the Petricite Golem", "破法 · 岩石傀儡");
        ApotheosisAffixRecord.addRecord("apotheosis:shield/mob_effect/devilish", "Devilish · of the Veteran", "残忍 · 老兵");
        ApotheosisAffixRecord.addRecord("apotheosis:ftbu", "Chainbound · of Veinseeking", "连锁 · 矿脉爆破");
        ApotheosisAffixRecord.addRecord("apotheosis:bedrock_ore", "Prospecting · of the Bedrock Seer", "勘探 · 基岩透视者");
        ApotheosisAffixRecord.addRecord("apotheosis:bedrock_fluid_ore", "Flowseeking · of the Oil Penetrator", "寻流 · 石油之眼");
        ApotheosisAffixRecord.addRecord("apotheosis:stress", "Stress · of the Brinkbreaker", "应力 · 千钧一发");
        ApotheosisAffixRecord.addRecord("apotheosis:kinetic", "Kinetic · of the Momentum Master", "动能 · 势如破竹");
    }

    private static void addRecord(String affixId, String enId, String cnId) {
        AFFIXS.add(ApotheosisAffixRecord.create(affixId, enId, cnId));
    }

    public final static Map<Item, ApotheosisAffixRecord> AFFIX_ITEM_MAP = new Reference2ReferenceOpenHashMap<>();

    public static Map<String, ItemEntry<ApothItem>> registerAffixEssence() {
        ImmutableMap.Builder<String, ItemEntry<ApothItem>> entries = ImmutableMap.builder();
        for (var record : AFFIXS) {
            String itemId = "affix_essence_" + record.processedId();
            String cnName = "刻印精粹 (" + record.cnId() + ")";
            String enName = "Affix Essence (" + record.enId() + ")";

            ItemEntry<ApothItem> entry = item(itemId, cnName, p -> ApothItem.create(p, record.color()))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/apoth/fabric0"), GTOCore.id("item/apoth/fabric1")))
                    .lang(enName)
                    .color(() -> ApothItem::color)
                    .tag(Tags.AFFIX_ESSENCE)
                    .onRegister(i -> AFFIX_ITEM_MAP.put(i, record))
                    .register();
            entries.put(record.affixId(), entry);
        }
        AFFIXS = null;
        return entries.build();
    }
}
