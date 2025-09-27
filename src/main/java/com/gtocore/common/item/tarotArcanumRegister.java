package com.gtocore.common.item;

import com.gtocore.common.data.translation.GTOTarotArcanumTooltips;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;

import com.tterrag.registrate.util.entry.ItemEntry;

import static com.gtolib.utils.register.ItemRegisterUtils.item;

public final class tarotArcanumRegister {

    private static final int Major = 0xC0C0C0;
    private static final int Minor = 0xc1e7ed;

    @SuppressWarnings("rawtypes")
    public static ItemEntry[] registerTarotArcanum() {
        ItemEntry[] entries = new ItemEntry[79];

        entries[0] = item("tarot_card_0", "生命之树", p -> TarotArcanumBuilder.of(0xffec80, 0)
                .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_0()::apply)
                .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_0_Shift()::apply)
                .build(p))
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/0")))
                .lang("The Tree of Life")
                .color(() -> TarotArcanum::color)
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

            entries[1] = item("tarot_card_1", "魔术师", p -> TarotArcanumBuilder.of(Major, 1)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_1()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/1")))
                    .lang("The Magician")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[2] = item("tarot_card_2", "女祭司", p -> TarotArcanumBuilder.of(Major, 2)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_2()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/2")))
                    .lang("The Priestess")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[3] = item("tarot_card_3", "女皇", p -> TarotArcanumBuilder.of(Major, 3)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_3()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/3")))
                    .lang("The Empress")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[4] = item("tarot_card_4", "皇帝", p -> TarotArcanumBuilder.of(Major, 4)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_4()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/4")))
                    .lang("The Emperor")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[5] = item("tarot_card_5", "教皇", p -> TarotArcanumBuilder.of(Major, 5)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_5()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/5")))
                    .lang("The Hierarch")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[6] = item("tarot_card_6", "犹豫不决", p -> TarotArcanumBuilder.of(Major, 6)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_6()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/6")))
                    .lang("Indecision")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[7] = item("tarot_card_7", "凯旋", p -> TarotArcanumBuilder.of(Major, 7)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_7()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/7")))
                    .lang("Triumph")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[8] = item("tarot_card_8", "正义", p -> TarotArcanumBuilder.of(Major, 8)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_8()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/8")))
                    .lang("Justice")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[9] = item("tarot_card_9", "隐士", p -> TarotArcanumBuilder.of(Major, 9)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_9()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/9")))
                    .lang("The Hermit")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[10] = item("tarot_card_10", "报应", p -> TarotArcanumBuilder.of(Major, 10)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_10()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/10")))
                    .lang("Retribution")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[11] = item("tarot_card_11", "说服", p -> TarotArcanumBuilder.of(Major, 11)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_11()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/11")))
                    .lang("Persuasion")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[12] = item("tarot_card_12", "使徒", p -> TarotArcanumBuilder.of(Major, 12)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_12()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/12")))
                    .lang("The Apostolate")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[13] = item("tarot_card_13", "不朽", p -> TarotArcanumBuilder.of(Major, 13)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_13()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/13")))
                    .lang("Immortality")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[14] = item("tarot_card_14", "节制", p -> TarotArcanumBuilder.of(Major, 14)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_14()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/14")))
                    .lang("Temperance")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[15] = item("tarot_card_15", "激情", p -> TarotArcanumBuilder.of(Major, 15)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_15()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/15")))
                    .lang("Passion")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[16] = item("tarot_card_16", "脆弱", p -> TarotArcanumBuilder.of(Major, 16)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_16()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/16")))
                    .lang("Fragility")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[17] = item("tarot_card_17", "希望", p -> TarotArcanumBuilder.of(Major, 17)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_17()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/17")))
                    .lang("Hope")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[18] = item("tarot_card_18", "黄昏", p -> TarotArcanumBuilder.of(Major, 18)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_18()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/18")))
                    .lang("Twilight")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[19] = item("tarot_card_19", "灵感", p -> TarotArcanumBuilder.of(Major, 19)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_19()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/19")))
                    .lang("Inspiration")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[20] = item("tarot_card_20", "复活", p -> TarotArcanumBuilder.of(Major, 20)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_20()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/20")))
                    .lang("Resurrection")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[21] = item("tarot_card_21", "蜕变", p -> TarotArcanumBuilder.of(Major, 21)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_21()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/21")))
                    .lang("Transmutation")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[22] = item("tarot_card_22", "回归", p -> TarotArcanumBuilder.of(Major, 22)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_22()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back"), GTOCore.id("item/tarot/22")))
                    .lang("The Return")
                    .color(() -> TarotArcanum::color)
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

            entries[23] = item("tarot_card_23", "耕者", p -> TarotArcanumBuilder.of(Minor, 23)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_23()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("The Plower")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[24] = item("tarot_card_24", "织者", p -> TarotArcanumBuilder.of(Minor, 24)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_24()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("The Weaver")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[25] = item("tarot_card_25", "阿尔戈英雄", p -> TarotArcanumBuilder.of(Minor, 25)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_25()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("The Argonaut")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[26] = item("tarot_card_26", "奇能者", p -> TarotArcanumBuilder.of(Minor, 26)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_26()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("The Prodigy")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[27] = item("tarot_card_27", "意外者", p -> TarotArcanumBuilder.of(Minor, 27)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_27()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("The Unexpected")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[28] = item("tarot_card_28", "犹疑者", p -> TarotArcanumBuilder.of(Minor, 28)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_28()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Uncertainty")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[29] = item("tarot_card_29", "居家者", p -> TarotArcanumBuilder.of(Minor, 29)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_29()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Domesticity")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[30] = item("tarot_card_30", "交易者", p -> TarotArcanumBuilder.of(Minor, 30)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_30()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Exchange")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[31] = item("tarot_card_31", "阻碍者", p -> TarotArcanumBuilder.of(Minor, 31)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_31()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Impediments")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[32] = item("tarot_card_32", "盛显者", p -> TarotArcanumBuilder.of(Minor, 32)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_32()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Magnificence")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[33] = item("tarot_card_33", "同盟者", p -> TarotArcanumBuilder.of(Minor, 33)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_33()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Alliance")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[34] = item("tarot_card_34", "革新者", p -> TarotArcanumBuilder.of(Minor, 34)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_34()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Innovation")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[35] = item("tarot_card_35", "悲戚者", p -> TarotArcanumBuilder.of(Minor, 35)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_35()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Grief")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[36] = item("tarot_card_36", "启蒙者", p -> TarotArcanumBuilder.of(Minor, 36)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_36()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Initiation")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[37] = item("tarot_card_37", "艺智者", p -> TarotArcanumBuilder.of(Minor, 37)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_37()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Art and Science")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[38] = item("tarot_card_38", "双态者", p -> TarotArcanumBuilder.of(Minor, 38)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_38()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Biplicity")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[39] = item("tarot_card_39", "见证者", p -> TarotArcanumBuilder.of(Minor, 39)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_39()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Testimony")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[40] = item("tarot_card_40", "预感者", p -> TarotArcanumBuilder.of(Minor, 40)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_40()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Presentiment")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[41] = item("tarot_card_41", "不安者", p -> TarotArcanumBuilder.of(Minor, 41)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_41()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Uneasiness")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[42] = item("tarot_card_42", "卓越者", p -> TarotArcanumBuilder.of(Minor, 42)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_42()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Preeminence")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[43] = item("tarot_card_43", "想象者", p -> TarotArcanumBuilder.of(Minor, 43)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_43()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Imagination")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[44] = item("tarot_card_44", "思想者", p -> TarotArcanumBuilder.of(Minor, 44)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_44()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Thought")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[45] = item("tarot_card_45", "重生者", p -> TarotArcanumBuilder.of(Minor, 45)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_45()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Regeneration")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[46] = item("tarot_card_46", "遗产者", p -> TarotArcanumBuilder.of(Minor, 46)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_46()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Patrimony")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[47] = item("tarot_card_47", "演绎者", p -> TarotArcanumBuilder.of(Minor, 47)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_47()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Deduction")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[48] = item("tarot_card_48", "圆满者", p -> TarotArcanumBuilder.of(Minor, 48)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_48()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Consummation")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[49] = item("tarot_card_49", "多能者", p -> TarotArcanumBuilder.of(Minor, 49)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_49()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Versatility")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[50] = item("tarot_card_50", "亲和者", p -> TarotArcanumBuilder.of(Minor, 50)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_50()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Affinity")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[51] = item("tarot_card_51", "献策者", p -> TarotArcanumBuilder.of(Minor, 51)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_51()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Counseling")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[52] = item("tarot_card_52", "谋算者", p -> TarotArcanumBuilder.of(Minor, 52)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_52()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Premeditation")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[53] = item("tarot_card_53", "怨恨者", p -> TarotArcanumBuilder.of(Minor, 53)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_53()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Resentment")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[54] = item("tarot_card_54", "审视者", p -> TarotArcanumBuilder.of(Minor, 54)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_54()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Examination")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[55] = item("tarot_card_55", "悔悟者", p -> TarotArcanumBuilder.of(Minor, 55)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_55()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Contrition")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[56] = item("tarot_card_56", "朝圣者", p -> TarotArcanumBuilder.of(Minor, 56)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_56()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Pilgrimage")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[57] = item("tarot_card_57", "竞争者", p -> TarotArcanumBuilder.of(Minor, 57)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_57()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Rivalry")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[58] = item("tarot_card_58", "重整者", p -> TarotArcanumBuilder.of(Minor, 58)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_58()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Requalification")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[59] = item("tarot_card_59", "启示者", p -> TarotArcanumBuilder.of(Minor, 59)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_59()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Revelation")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[60] = item("tarot_card_60", "进化者", p -> TarotArcanumBuilder.of(Minor, 60)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_60()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Evolution")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[61] = item("tarot_card_61", "独处者", p -> TarotArcanumBuilder.of(Minor, 61)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_61()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Solitude")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[62] = item("tarot_card_62", "放逐者", p -> TarotArcanumBuilder.of(Minor, 62)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_62()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Proscription")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[63] = item("tarot_card_63", "共融者", p -> TarotArcanumBuilder.of(Minor, 63)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_63()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Communion")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[64] = item("tarot_card_64", "热忱者", p -> TarotArcanumBuilder.of(Minor, 64)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_64()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Zeal")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[65] = item("tarot_card_65", "求知者", p -> TarotArcanumBuilder.of(Minor, 65)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_65()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Learning")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[66] = item("tarot_card_66", "困惑者", p -> TarotArcanumBuilder.of(Minor, 66)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_66()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Perplexity")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[67] = item("tarot_card_67", "友善者", p -> TarotArcanumBuilder.of(Minor, 67)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_67()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Friendship")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[68] = item("tarot_card_68", "思辨者", p -> TarotArcanumBuilder.of(Minor, 68)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_68()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Speculation")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[69] = item("tarot_card_69", "机遇者", p -> TarotArcanumBuilder.of(Minor, 69)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_69()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Chance")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[70] = item("tarot_card_70", "合作者", p -> TarotArcanumBuilder.of(Minor, 70)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_70()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Cooperation")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[71] = item("tarot_card_71", "贪婪者", p -> TarotArcanumBuilder.of(Minor, 71)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_71()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Avarice")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[72] = item("tarot_card_72", "净化者", p -> TarotArcanumBuilder.of(Minor, 72)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_72()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Purification")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[73] = item("tarot_card_73", "爱欲者", p -> TarotArcanumBuilder.of(Minor, 73)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_73()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Love and Desire")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[74] = item("tarot_card_74", "奉献者", p -> TarotArcanumBuilder.of(Minor, 74)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_74()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Offering")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[75] = item("tarot_card_75", "慷慨者", p -> TarotArcanumBuilder.of(Minor, 75)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_75()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Generosity")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[76] = item("tarot_card_76", "施予者", p -> TarotArcanumBuilder.of(Minor, 76)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_76()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("The Dispenser")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[77] = item("tarot_card_77", "迷失者", p -> TarotArcanumBuilder.of(Minor, 77)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_77()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Disorientation")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();

            entries[78] = item("tarot_card_78", "复兴者", p -> TarotArcanumBuilder.of(Minor, 78)
                    .withTooltip(GTOTarotArcanumTooltips.INSTANCE.getTarotArcanum_78()::apply)
                    .build(p))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/tarot/back")))
                    .lang("Renaissance")
                    .color(() -> TarotArcanum::color)
                    .tag(Tags.TAROT_ARCANUM)
                    .register();
        }

        return entries;
    }
}
