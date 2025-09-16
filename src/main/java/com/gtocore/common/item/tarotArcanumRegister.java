package com.gtocore.common.item;

import com.gtocore.common.data.translation.GTOTarotArcanumTooltips;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.item.TooltipBehavior;

import com.tterrag.registrate.util.entry.ItemEntry;

import static com.gregtechceu.gtceu.common.data.GTItems.attach;
import static com.gtolib.utils.register.ItemRegisterUtils.item;

public final class tarotArcanumRegister {

    private static final int Major = 0xC0C0C0;
    private static final int Minor = 0xc1e7ed;

    public static ItemEntry[] registerTarotArcanum() {
        ItemEntry[] entries = new ItemEntry[79];

        entries[0] = item("tarot_card_0", "生命之树", p -> TarotArcanum.create(p, 0xffec80, 0))
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/0")))
                .color(() -> TarotArcanum::color)
                .lang("The Tree of Life")
                .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_0()::apply)))
                .tag(Tags.TAROT_ARCANUM)
                .register();

        // 大阿卡纳
        {
            /*
             * 1：魔术师(The Magician)
             * 2：女祭司(The Priestess)
             * 3：女皇(The Empress)
             * 4：皇帝(The Emperor)
             * 5：教皇(The Hierarch)
             * 6：犹豫不决(Indecision)
             * 7：凯旋(Triumph)
             * 8：正义(Justice)
             * 9：隐士(The Hermit)
             * 10：报应(Retribution)
             * 11：说服(Persuasion)
             * 12：使徒(The Apostolate)
             * 13：不朽(Immortality)
             * 14：节制(Temperance)
             * 15：激情(Passion)
             * 16：脆弱(Fragility)
             * 17：希望(Hope)
             * 18：黄昏(Twilight)
             * 19：灵感(Inspiration)
             * 20：复活(Resurrection)
             * 21：蜕变(Transmutation)
             * 22：回归(The Return)
             */

            entries[1] = item("tarot_card_1", "魔术师", p -> TarotArcanum.create(p, Major, 1))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/1")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Magician")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_1()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[2] = item("tarot_card_2", "女祭司", p -> TarotArcanum.create(p, Major, 2))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/2")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Priestess")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_2()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[3] = item("tarot_card_3", "女皇", p -> TarotArcanum.create(p, Major, 3))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/3")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Empress")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_3()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[4] = item("tarot_card_4", "皇帝", p -> TarotArcanum.create(p, Major, 4))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/4")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Emperor")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_4()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[5] = item("tarot_card_5", "教皇", p -> TarotArcanum.create(p, Major, 5))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/5")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Hierarch")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_5()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[6] = item("tarot_card_6", "犹豫不决", p -> TarotArcanum.create(p, Major, 6))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/6")))
                    .color(() -> TarotArcanum::color)
                    .lang("Indecision")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_6()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[7] = item("tarot_card_7", "凯旋", p -> TarotArcanum.create(p, Major, 7))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/7")))
                    .color(() -> TarotArcanum::color)
                    .lang("Triumph")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_7()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[8] = item("tarot_card_8", "正义", p -> TarotArcanum.create(p, Major, 8))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/8")))
                    .color(() -> TarotArcanum::color)
                    .lang("Justice")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_8()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[9] = item("tarot_card_9", "隐士", p -> TarotArcanum.create(p, Major, 9))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/9")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Hermit")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_9()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[10] = item("tarot_card_10", "报应", p -> TarotArcanum.create(p, Major, 10))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/10")))
                    .color(() -> TarotArcanum::color)
                    .lang("Retribution")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_10()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[11] = item("tarot_card_11", "说服", p -> TarotArcanum.create(p, Major, 11))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/11")))
                    .color(() -> TarotArcanum::color)
                    .lang("Persuasion")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_11()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[12] = item("tarot_card_12", "使徒", p -> TarotArcanum.create(p, Major, 12))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/12")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Apostolate")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_12()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[13] = item("tarot_card_13", "不朽", p -> TarotArcanum.create(p, Major, 13))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/13")))
                    .color(() -> TarotArcanum::color)
                    .lang("Immortality")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_13()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[14] = item("tarot_card_14", "节制", p -> TarotArcanum.create(p, Major, 14))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/14")))
                    .color(() -> TarotArcanum::color)
                    .lang("Temperance")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_14()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[15] = item("tarot_card_15", "激情", p -> TarotArcanum.create(p, Major, 15))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/15")))
                    .color(() -> TarotArcanum::color)
                    .lang("Passion")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_15()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[16] = item("tarot_card_16", "脆弱", p -> TarotArcanum.create(p, Major, 16))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/16")))
                    .color(() -> TarotArcanum::color)
                    .lang("Fragility")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_16()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[17] = item("tarot_card_17", "希望", p -> TarotArcanum.create(p, Major, 17))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/17")))
                    .color(() -> TarotArcanum::color)
                    .lang("Hope")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_17()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[18] = item("tarot_card_18", "黄昏", p -> TarotArcanum.create(p, Major, 18))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/18")))
                    .color(() -> TarotArcanum::color)
                    .lang("Twilight")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_18()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[19] = item("tarot_card_19", "灵感", p -> TarotArcanum.create(p, Major, 19))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/19")))
                    .color(() -> TarotArcanum::color)
                    .lang("Inspiration")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_19()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[20] = item("tarot_card_20", "复活", p -> TarotArcanum.create(p, Major, 20))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/20")))
                    .color(() -> TarotArcanum::color)
                    .lang("Resurrection")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_20()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[21] = item("tarot_card_21", "蜕变", p -> TarotArcanum.create(p, Major, 21))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/21")))
                    .color(() -> TarotArcanum::color)
                    .lang("Transmutation")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_21()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[22] = item("tarot_card_22", "回归", p -> TarotArcanum.create(p, Major, 22))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/22")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Return")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_22()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();
        }

        // 小阿卡纳
        {
            /*
             * 23：耕者（The Plower）
             * 24：织者（The Weaver）
             * 25：阿尔戈英雄（The Argonaut）
             * 26：奇能者（The Prodigy）
             * 27：意外者（The Unexpected）
             * 28：犹疑者（Uncertainty）
             * 29：居家者（Domesticity）
             * 30：交易者（Exchange）
             * 31：阻碍者（Impediments）
             * 32：盛显者（Magnificence）
             * 33：同盟者（Alliance）
             * 34：革新者（Innovation）
             * 35：悲戚者（Grief）
             * 36：启蒙者（Initiation）
             * 37：艺智者（Art and Science）
             * 38：双态者（Biplicity）
             * 39：见证者（Testimony）
             * 40：预感者（Presentiment）
             * 41：不安者（Uneasiness）
             * 42：卓越者（Preeminence）
             * 43：想象者（Imagination）
             * 44：思想者（Thought）
             * 45：重生者（Regeneration）
             * 46：遗产者（Patrimony）
             * 47：演绎者（Deduction）
             * 48：圆满者（Consummation）
             * 49：多能者（Versatility）
             * 50：亲和者（Affinity）
             * 51：献策者（Counseling）
             * 52：谋算者（Premeditation）
             * 53：怨恨者（Resentment）
             * 54：审视者（Examination）
             * 55：悔悟者（Contrition）
             * 56：朝圣者（Pilgrimage）
             * 57：竞争者（Rivalry）
             * 58：重整者（Requalification）
             * 59：启示者（Revelation）
             * 60：进化者（Evolution）
             * 61：独处者（Solitude）
             * 62：放逐者（Proscription）
             * 63：共融者（Communion）
             * 64：热忱者（Zeal）
             * 65：求知者（Learning）
             * 66：困惑者（Perplexity）
             * 67：友善者（Friendship）
             * 68：思辨者（Speculation）
             * 69：机遇者（Chance）
             * 70：合作者（Cooperation）
             * 71：贪婪者（Avarice）
             * 72：净化者（Purification）
             * 73：爱欲者（Love and Desire）
             * 74：奉献者（Offering）
             * 75：慷慨者（Generosity）
             * 76：施予者（The Dispenser）
             * 77：迷失者（Disorientation）
             * 78：复兴者（Renaissance）
             */

            entries[23] = item("tarot_card_23", "耕者", p -> TarotArcanum.create(p, Minor, 23))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Plower")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_23()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[24] = item("tarot_card_24", "织者", p -> TarotArcanum.create(p, Minor, 24))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Weaver")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_24()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[25] = item("tarot_card_25", "阿尔戈英雄", p -> TarotArcanum.create(p, Minor, 25))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Argonaut")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_25()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[26] = item("tarot_card_26", "奇能者", p -> TarotArcanum.create(p, Minor, 26))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Prodigy")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_26()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[27] = item("tarot_card_27", "意外者", p -> TarotArcanum.create(p, Minor, 27))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Unexpected")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_27()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[28] = item("tarot_card_28", "犹疑者", p -> TarotArcanum.create(p, Minor, 28))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Uncertainty")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_28()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[29] = item("tarot_card_29", "居家者", p -> TarotArcanum.create(p, Minor, 29))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Domesticity")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_29()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[30] = item("tarot_card_30", "交易者", p -> TarotArcanum.create(p, Minor, 30))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Exchange")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_30()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[31] = item("tarot_card_31", "阻碍者", p -> TarotArcanum.create(p, Minor, 31))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Impediments")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_31()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[32] = item("tarot_card_32", "盛显者", p -> TarotArcanum.create(p, Minor, 32))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Magnificence")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_32()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[33] = item("tarot_card_33", "同盟者", p -> TarotArcanum.create(p, Minor, 33))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Alliance")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_33()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[34] = item("tarot_card_34", "革新者", p -> TarotArcanum.create(p, Minor, 34))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Innovation")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_34()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[35] = item("tarot_card_35", "悲戚者", p -> TarotArcanum.create(p, Minor, 35))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Grief")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_35()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[36] = item("tarot_card_36", "启蒙者", p -> TarotArcanum.create(p, Minor, 36))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Initiation")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_36()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[37] = item("tarot_card_37", "艺智者", p -> TarotArcanum.create(p, Minor, 37))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Art and Science")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_37()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[38] = item("tarot_card_38", "双态者", p -> TarotArcanum.create(p, Minor, 38))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Biplicity")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_38()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[39] = item("tarot_card_39", "见证者", p -> TarotArcanum.create(p, Minor, 39))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Testimony")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_39()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[40] = item("tarot_card_40", "预感者", p -> TarotArcanum.create(p, Minor, 40))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Presentiment")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_40()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[41] = item("tarot_card_41", "不安者", p -> TarotArcanum.create(p, Minor, 41))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Uneasiness")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_41()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[42] = item("tarot_card_42", "卓越者", p -> TarotArcanum.create(p, Minor, 42))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Preeminence")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_42()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[43] = item("tarot_card_43", "想象者", p -> TarotArcanum.create(p, Minor, 43))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Imagination")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_43()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[44] = item("tarot_card_44", "思想者", p -> TarotArcanum.create(p, Minor, 44))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Thought")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_44()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[45] = item("tarot_card_45", "重生者", p -> TarotArcanum.create(p, Minor, 45))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Regeneration")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_45()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[46] = item("tarot_card_46", "遗产者", p -> TarotArcanum.create(p, Minor, 46))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Patrimony")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_46()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[47] = item("tarot_card_47", "演绎者", p -> TarotArcanum.create(p, Minor, 47))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Deduction")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_47()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[48] = item("tarot_card_48", "圆满者", p -> TarotArcanum.create(p, Minor, 48))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Consummation")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_48()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[49] = item("tarot_card_49", "多能者", p -> TarotArcanum.create(p, Minor, 49))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Versatility")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_49()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[50] = item("tarot_card_50", "亲和者", p -> TarotArcanum.create(p, Minor, 50))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Affinity")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_50()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[51] = item("tarot_card_51", "献策者", p -> TarotArcanum.create(p, Minor, 51))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Counseling")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_51()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[52] = item("tarot_card_52", "谋算者", p -> TarotArcanum.create(p, Minor, 52))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Premeditation")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_52()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[53] = item("tarot_card_53", "怨恨者", p -> TarotArcanum.create(p, Minor, 53))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Resentment")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_53()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[54] = item("tarot_card_54", "审视者", p -> TarotArcanum.create(p, Minor, 54))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Examination")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_54()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[55] = item("tarot_card_55", "悔悟者", p -> TarotArcanum.create(p, Minor, 55))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Contrition")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_55()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[56] = item("tarot_card_56", "朝圣者", p -> TarotArcanum.create(p, Minor, 56))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Pilgrimage")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_56()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[57] = item("tarot_card_57", "竞争者", p -> TarotArcanum.create(p, Minor, 57))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Rivalry")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_57()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[58] = item("tarot_card_58", "重整者", p -> TarotArcanum.create(p, Minor, 58))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Requalification")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_58()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[59] = item("tarot_card_59", "启示者", p -> TarotArcanum.create(p, Minor, 59))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Revelation")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_59()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[60] = item("tarot_card_60", "进化者", p -> TarotArcanum.create(p, Minor, 60))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Evolution")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_60()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[61] = item("tarot_card_61", "独处者", p -> TarotArcanum.create(p, Minor, 61))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Solitude")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_61()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[62] = item("tarot_card_62", "放逐者", p -> TarotArcanum.create(p, Minor, 62))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Proscription")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_62()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[63] = item("tarot_card_63", "共融者", p -> TarotArcanum.create(p, Minor, 63))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Communion")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_63()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[64] = item("tarot_card_64", "热忱者", p -> TarotArcanum.create(p, Minor, 64))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Zeal")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_64()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[65] = item("tarot_card_65", "求知者", p -> TarotArcanum.create(p, Minor, 65))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Learning")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_65()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[66] = item("tarot_card_66", "困惑者", p -> TarotArcanum.create(p, Minor, 66))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Perplexity")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_66()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[67] = item("tarot_card_67", "友善者", p -> TarotArcanum.create(p, Minor, 67))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Friendship")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_67()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[68] = item("tarot_card_68", "思辨者", p -> TarotArcanum.create(p, Minor, 68))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Speculation")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_68()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[69] = item("tarot_card_69", "机遇者", p -> TarotArcanum.create(p, Minor, 69))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Chance")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_69()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[70] = item("tarot_card_70", "合作者", p -> TarotArcanum.create(p, Minor, 70))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Cooperation")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_70()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[71] = item("tarot_card_71", "贪婪者", p -> TarotArcanum.create(p, Minor, 71))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Avarice")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_71()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[72] = item("tarot_card_72", "净化者", p -> TarotArcanum.create(p, Minor, 72))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Purification")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_72()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[73] = item("tarot_card_73", "爱欲者", p -> TarotArcanum.create(p, Minor, 73))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Love and Desire")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_73()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[74] = item("tarot_card_74", "奉献者", p -> TarotArcanum.create(p, Minor, 74))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Offering")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_74()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[75] = item("tarot_card_75", "慷慨者", p -> TarotArcanum.create(p, Minor, 75))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Generosity")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_75()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[76] = item("tarot_card_76", "施予者", p -> TarotArcanum.create(p, Minor, 76))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("The Dispenser")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_76()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[77] = item("tarot_card_77", "迷失者", p -> TarotArcanum.create(p, Minor, 77))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Disorientation")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_77()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[78] = item("tarot_card_78", "复兴者", p -> TarotArcanum.create(p, Minor, 78))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .color(() -> TarotArcanum::color)
                    .lang("Renaissance")
                    .onRegister(attach(new TooltipBehavior(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_78()::apply)))
                    .tag(Tags.TAROT_ARCANUM)
                    .register();
        }

        return entries;
    }
}
