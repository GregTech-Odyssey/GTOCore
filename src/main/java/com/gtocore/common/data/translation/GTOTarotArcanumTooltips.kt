package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.api.lang.initialize
import com.gtocore.api.lang.translatedTo
import com.gtocore.api.misc.AutoInitialize

object GTOTarotArcanumTooltips : AutoInitialize<GTOTarotArcanumTooltips>() {

    val PandS = { other: ComponentSupplier -> (("预测与综合：" translatedTo "Prediction and Synthesis: ") + other).gold() }.initialize()
    val Sephirah = { other: ComponentSupplier -> (("卡巴拉源质：" translatedTo "Kabbalistic Sephirah: ") + other).lightPurple() }.initialize()
    val Letter = { other: ComponentSupplier -> (("希伯来字母：" translatedTo "Hebrew Letter: ") + other).yellow() }.initialize()
    val Note = { other: ComponentSupplier -> (("注释：" translatedTo "Note: ") + other).gray() }.initialize()
    val Timetable = { other: ComponentSupplier -> (("时间表：" translatedTo "Timetable: ") + other).red() }.initialize()
    val Description = { other: ComponentSupplier -> (("描述：" translatedTo "Description: ") + other).gold() }.initialize()
    val Modulating = { other: ComponentSupplier -> (("调节属性：" translatedTo "Modulating Attribute: ") + other).yellow() }.initialize()
    val Transcendental = { other: ComponentSupplier -> (("超验法则：" translatedTo "Transcendental Axiom: ") + other).green() }.initialize()
    val Forecasting = { other: ComponentSupplier -> (("预测要素：" translatedTo "Forecasting Element: ") + other).aqua() }.initialize()
    val GTOTarotArcanum = ("⭐ 永恒塔罗牌" translatedTo "⭐ Eternal Tarot").scrollFullColor().initialize()

    val TarotArcanum_0 = ComponentListSupplier {
        setTranslationPrefix("Tarot-The Tree of Life")
        add(("根生虚无间，十源耀大千" translatedTo "Rooted in Ein Sof, ten shine through realms")) { rainbowGradient(2f, 224, true) }
        add(("三柱承天序，慈悲严正衡" translatedTo "Three pillars hold, love and justice balance")) { rainbowGradient(2f, 192, true) }
        add(("四界递流转，造化各有司" translatedTo "Four worlds flow, each shapes creation")) { rainbowGradient(2f, 160, true) }
        add(("智解生万物，爱润众生命" translatedTo "Wisdom and insight birth all; love nourishes")) { rainbowGradient(2f, 128, true) }
        add(("廿二路连源，塔罗显真意" translatedTo "Twenty-two paths link Sephiroth; Tarot reveals truth")) { rainbowGradient(2f, 96, true) }
        add(("王冠王国环，流溢永不断" translatedTo "Crown to Kingdom cycles, light flows endlessly")) { rainbowGradient(2f, 64, true) }
        add(("爱化万形影，合一返本源" translatedTo "Love shapes all forms; unite to return")) { rainbowGradient(2f, 32, true) }
        add(("圣文藏秘钥，神显驻世间" translatedTo "Holy Script holds the key; Divine Presence dwells")) { rainbowGradient(2f, 0, true) }
        add("本整合包并未使用韦特或者托特塔罗牌，" translatedTo "This modpack does not use the Waite or Thoth Tarot decks,") { gray() }
        add("而是使用了一种自构建牌组，" translatedTo "but instead employs a self-constructed deck,") { gray() }
        add("可能与常见体系有极大的区别。" translatedTo "which may differ significantly from conventional systems.") { gray() }

        add(GTOTarotArcanum)
    }

    val TarotArcanum_1 = ComponentListSupplier {
        setTranslationPrefix("Tarot-The Magician")
        add(PandS("(神圣)原人。宝剑，意志之力。" translatedTo "Man. Sword, willpower."))
        add(Sephirah("王冠 (Kether)" translatedTo "Kether"))
        add(Letter("阿列夫 (Aleph - א)" translatedTo "Aleph (א)"))
        add(Transcendental("“行汝所思，如汝所想”" translatedTo "“Be thou in thy deeds as thou art in thy thoughts.”"))
        add(Forecasting("预示着对物质障碍的掌控、新的社会关系、成功的开端、有助于项目发展的忠诚友人的汇聚、以及会阻碍发展的嫉妒之友。" translatedTo "It promises dominion of material obstacles, new social relations, happy initiatives, the concourse of loyal friends who aid the development of projects, and jealous friends who obstruct it."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_2 = ComponentListSupplier {
        setTranslationPrefix("Tarot-The Priestess")
        add(PandS("魔术师之配偶。秘传科学。吉兆。" translatedTo "The wife of The Magician. Occult science. Favorable."))
        add(Sephirah("智慧 (Chokmah)" translatedTo "Chokmah"))
        add(Letter("贝特 (Beth - ב)" translatedTo "Beth (ב)"))
        add(Transcendental("“风与浪，总惠于知航之人”" translatedTo "“Winds and waves always go in favor of the one who knows how to sail.”"))
        add(Forecasting("吸引与排斥，损失与收益，上升与下降；对行动的有利启示，试图阻止行动向良善目标推进的次要者之隐秘反对。" translatedTo "Attractions and repulsions, loss and profit, ascensions and descents; favorable inspirations to the initiative and the secret opposition from secondary ones who try to stop that initiative going towards a good goal."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_3 = ComponentListSupplier {
        setTranslationPrefix("Tarot-The Empress")
        add(PandS("神圣母亲。物质与灵性的生产与丰饶。" translatedTo "The Divine Mother. Material and spiritual production."))
        add(Sephirah("理解 (Binah)" translatedTo "Binah"))
        add(Letter("吉梅尔 (Gimel - ג)" translatedTo "Gimel (ג)"))
        add(Transcendental("“汝之织机，纺汝可用之布，亦纺汝无用之布”" translatedTo "“Thy loom is weaving cloth for thou to use and cloth that thou shalt not use.”"))
        add(Forecasting("物质之倍增，商业之兴旺，丰裕，财富，成功；必克之障碍，及克之之时，与所付努力相称之满足。" translatedTo "Multiplication of material goods, prosperity in business, abundance, wealth, success; obstacles which must be defeated, and satisfaction in proportion to efforts made while we are defeating them."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_4 = ComponentListSupplier {
        setTranslationPrefix("Tarot-The Emperor")
        add(PandS("统御，进展，成功，仁慈。" translatedTo "Command, progress, success, mercy."))
        add(Sephirah("慈悲 (Chesed)" translatedTo "Chesed"))
        add(Letter("达列特 (Daleth - ד)" translatedTo "Daleth (ד)"))
        add(Transcendental("“祈佑汝手之劳，且使心入思绪”" translatedTo "“Give blessings unto the labor of thy hands, and place thy heart into thy thoughts.”"))
        add(Forecasting("物质收益之保障，高阶事业之根基，所付努力之利好结果，达成目标之困境；友朋兼为助力与阻碍。命运吉凶并存。" translatedTo "Guarantees material gains, having the basis for higher enterprises, favorable results on spent efforts and troubled conditions in order to achieve them; friends are simultaneously a help and obstacle. Fate is proper and adverse at the same time."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_5 = ComponentListSupplier {
        setTranslationPrefix("Tarot-The Hierarch")
        add(PandS("严苛，律法。业力，火星，战争。" translatedTo "Rigor, the law. Karma, Mars, war."))
        add(Sephirah("力量 (Geburah)" translatedTo "Geburah"))
        add(Letter("赫 (Hei - ה)" translatedTo "Hei (ה)"))
        add(Transcendental("“昔闻其名，今观其形，且感其心”" translatedTo "“I have heard of thee by the hearing of the ear, but now mine eye seeth thee, and my heart feeleth thee.”"))
        add(Forecasting("自由与束缚，新体验，有益教诲之习得，爱情与风流韵事，波折而终顺遂之历程。吉兆之友与凶兆之友；往来之人物，为离而来者，为归而去者。" translatedTo "Freedom and restrictions, new experiences, acquisition of advantageous teachings, love and love affairs, journeys of frustrated prosperity. Propitious friends and friends who are of a sinister augury; beings and things that come and go, those who come in order to leave, those who go in order to return."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_6 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Indecision")
        add(PandS("恋人。胜利，好运。" translatedTo "The Lover. Victory, good luck."))
        add(Sephirah("美丽 (Tiphereth)" translatedTo "Tiphereth"))
        add(Letter("瓦夫 (Vav - ו)" translatedTo "Vav (ו)"))
        add(Transcendental("“主啊，赐我劳作，亦赐坚韧以承之”" translatedTo "“Thou art giving me labor, oh Lord, and fortitude with it.”"))
        add(Forecasting("两性关系中之特权与义务。力量之对抗。分离与离异。所追之物之获得，炽热渴望之实现 —— 其中或有满足，或有失望。" translatedTo "Privilege and obligations in the relations of sexes. Antagonism of forces. Separations and divorces. Possession of that which is pursued and ardent desires which are fulfilled, where some of them satisfy and others disappoint."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_7 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Triumph")
        add(PandS("战争之车。战争，挣扎，赎罪，痛苦，苦涩。" translatedTo "The Chariot of War. Wars, struggles, atonement, pain, bitterness."))
        add(Sephirah("胜利 (Netzach)" translatedTo "Netzach"))
        add(Letter("扎因 (Zayin - ז)" translatedTo "Zayin (ז)"))
        add(Transcendental("“当学识入汝心，智慧甘汝魂，彼时求则得之”" translatedTo "“When science shall enter into thy heart and wisdom shall be sweet unto thy soul, then ask and it shall be granted unto thee.”"))
        add(Forecasting("磁性力量之保障，理智与直觉之结合；正义与补偿，荣誉与耻辱，坚毅追求之物之达成，满足与失望。" translatedTo "Guarantees magnetic power, well-aimed intellection (union of intellect and intuition); justice and reparations, honor and dishonour, achievement of that which is pursued with determination, satisfactions and disappointments."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_8 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Justice")
        add(PandS("约伯的奥秘。苦难，考验，痛苦。" translatedTo "The arcanum of Job. Sufferings, tests, pains."))
        add(Sephirah("荣耀 (Hod)" translatedTo "Hod"))
        add(Letter("赫特 (Chet - ח)" translatedTo "Chet (ח)"))
        add(Transcendental("“汝当于心中筑坛，勿以心为坛。”" translatedTo "“Thou shalt edify an altar within thy heart, but thou shalt not make an altar of thy heart.”"))
        add(Forecasting("报应之保障，惩罚与奖赏，感恩与忘恩，所供服务之补偿。" translatedTo "Guarantees retributions, punishments and rewards, gratitudes and ungratefulness, compensations for given services."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_9 = ComponentListSupplier {
        setTranslationPrefix("Tarot-The Hermit")
        add(PandS("入门/启蒙。独处，苦修。" translatedTo "Initiation. Solitude, sufferings."))
        add(Sephirah("基础 (Yesod)" translatedTo "Yesod"))
        add(Letter("泰特 (Teth - ט)" translatedTo "Teth (ט)"))
        add(Transcendental("“登彼山而望应许之地，然勿越界前往”" translatedTo "“Rise unto the mount and contemplate the Promised Land. But, thou shalt not go over thither.”"))
        add(Forecasting("探索所需学识之保障，探索时所用之方法，借学识行事时之谨慎；有益之交与自负之交；助力与阻碍之友；理性之光与直觉之光 —— 前者照当下，后者照未来。" translatedTo "It guarantees science in order for the making of discoveries; a method for when making them; caution when being served by them; advantageous associations and conceited associations; friends who help and who obstruct; light of reason and light of intuition, the first light for the immediate and the second light for that which will be."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_10 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Retribution")
        add(PandS("命运之轮。好交易，变迁。" translatedTo "The Wheel of Fortune. Good business, changes."))
        add(Sephirah("王国 (Malkuth)" translatedTo "Malkuth"))
        add(Letter("尤德 (Iod - י)" translatedTo "Iod (י)"))
        add(Note("当吾人祈求时，天使常以时钟示之作为回应。修行者当定睛时钟所示之时辰，此乃命运之钟，答案藏于时辰之中。在秘传教义隐喻里，时钟恒为回应之具，吾人需习得解读时钟之法。" translatedTo "When we make a petition, oftentimes the angels answer us by showing a clock. The disciple must fix his sight on the hour of the clock. This is the clock of destiny. The answer is in the hour. In esoteric allegory, one is always answered by the clock. We have to learn how to understand the clock."))
        add(Timetable("阿波罗尼乌斯第一时：“神秘学之超验研究”" translatedTo "First Hour of Apollonius: “Transcendental study of occultism.”"))
        add(Transcendental("“以经验换得之学识，价高；汝所欠缺且需购求之学识，价更高”" translatedTo "“The knowledge that thou buyest with thine experience is expensive, and the knowledge that thou lacks and thou need to buy is even more expensive.”"))
        add(Forecasting("吉凶之保障，升降起伏，合法所有与可疑所有；过往偶然事件之启示，及以独特方式重现之情境。" translatedTo "Guarantees good and bad fortune, elevation and descents, legitimate possessions and doubtful possessions; recommendations of past contingencies and circumstances which are repeated in a distinct way."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_11 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Persuasion")
        add(PandS("被驯服的狮子。律法的恩宠。无所畏惧。火星。" translatedTo "The tame lion. Favor of the law. Let there be no fear. Mars."))
        add(Letter("卡夫 (Kaf - כ)" translatedTo "Kaf (כ)"))
        add(Timetable("阿波罗尼乌斯第二时：“火之深渊与星光美德，绕巨龙与火焰(神秘力量之研究)成一圆环”" translatedTo "Second Hour of Apollonius: “The abysses of the fire and the astral virtues form a circle through (around) the dragons and the fire (studies of the occult forces).”"))
        add(Transcendental("“怀希望而喜乐，历患难而忍耐，汝当恒守祈祷。”" translatedTo "“Joyful in hope, suffered in tribulation, be thou constant in thy prayer.”"))
        add(Forecasting("方向掌控之保障(此方向引向元素支配)；活力，重获青春，亲友之得失(因家事故)；痛苦，障碍，嫉妒，背叛，及承受失望之顺从。" translatedTo "Guarantees control of the direction which is followed that leads towards the dominion of the elements; vitality, rejuvenation, acquisition, and loss of friends because of family matters; pains, obstacles, jealousy, treason and resignation in order to bear the disappointments."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_12 = ComponentListSupplier {
        setTranslationPrefix("Tarot-The Apostolate")
        add(PandS("牺牲。考验与苦痛。A.Z.F.奥秘将吾等带离苦痛。" translatedTo "Sacrifice. Tests and pains. The Arcanum A.Z.F. takes us out of pain."))
        add(Letter("拉梅德 (Lamed - ל)" translatedTo "Lamed (ל)"))
        add(Timetable("阿波罗尼乌斯第三时：“巨蛇、猎犬与火焰(性魔法，昆达里尼之修习)”" translatedTo "Third Hour of Apollonius: “The serpents, the dogs, and the fire (Sexual Magic, work with the Kundalini).”"))
        add(Transcendental("“虽日则太阳令汝疲惫，夜则月亮令汝忧伤，汝勿涉足湿滑之地，值岗之时亦勿沉睡”" translatedTo "“Even though the sun makes thou fatigued during the day and the moon makes thou grievous during the night, thou shalt not take thy feet unto the slippery, neither thou shall sleep when thou art on guard.”"))
        add(Forecasting("对立之事之保障，痛苦，衰败；生活某些境遇中之物质损失，另些境遇中之收益；令人振奋之预感，令人沮丧之预感。" translatedTo "00Guarantees contrarieties, anguish, downfalls; material loss in some conditions of life and profit in others; presentiments which enliven and presentiments which discourage.0"))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_13 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Immortality")
        add(PandS("死亡与复活。转化，预示着彻底的转变。" translatedTo "Death and resurrection. Transformations, indicates total change."))
        add(Letter("梅姆 (Mem - מ)" translatedTo "Mem (מ)"))
        add(Timetable("阿波罗尼乌斯第四时：“新手将夜中游荡于坟墓之间，亲历幻视之恐怖，并受制于魔法与戈提亚术(此指修行者将见自身于星光层面遭数百万黑魔法师攻击，彼等幽暗魔法师试图将其驱离光明之道)”" translatedTo "Fourth Hour of Apollonius: “The neophyte will wander at night among the sepulchers, will experience the horror of visions, and will be submitted to magic and goethia (this means that the disciple will see that he is being attacked by millions of black magicians within the astral plane. Those tenebrous magicians attempt to drive the disciple away from the luminous path).”"))
        add(Transcendental("“黑夜已过，新日降临，汝当披光之武器”" translatedTo "“Night has passed and a new day has arrived, then thou shall be dressed with weapons of light.”"))
        add(Forecasting("失望之保障，幻灭，情感之消亡，所求之拒绝，崩塌，灵魂之纯粹欢愉与喜悦，伴随痛苦之改善，友人之助力；境遇之更新 —— 良者转劣，劣者转良。" translatedTo "Guarantees disappointments, disillusions, death of affections, refusals for that which is solicited, collapse, pure enjoyments and gladness for the soul, improvements with painful enjoyment, help of friends; renewal of conditions, the good ones for the worse and the bad ones for the better."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_14 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Temperance")
        add(PandS("结合，联盟。长寿，稳定，恒常不变。" translatedTo "Marriage, association. Longevity, stability, no change."))
        add(Letter("农 (Nun - נ)" translatedTo "Nun (נ)"))
        add(Timetable("阿波罗尼乌斯第五时：“天界之上水(此阶段修行者习得纯净与贞洁，因领悟自身精液之价值)”" translatedTo "Fifth Hour of Apollonius: “The superior waters of heaven (during this time the disciple learns to be pure and chaste because he comprehends the value of his seminal liquor).”"))
        add(Transcendental("“汝勿如风中稻草，亦勿如稻草前之风”" translatedTo "“Thou shalt not be as straw before the wind; neither shalt thou be as the wind before the straw.”"))
        add(Forecasting("敌意之预示，相互之情感，义务，联合，化学结合，利益之联结；苦楚之爱，忠诚之爱，背叛之爱；留存之物与离去之物 —— 前者为离去而来，后者为归来而去。" translatedTo "Predicts enmities, reciprocal affections, obligations, combinations, chemical combinations, and combining of interests; afflicted loves, devoted loves, betrayed loves; things that remain and things that depart, the first are in order to leave, the second are in order to return."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_15 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Passion")
        add(PandS("堤丰·巴风特。爱情中的挫败。预示危险。" translatedTo "Typhon Baphomet. Failure in love. Announces dangers."))
        add(Letter("萨梅赫 (Samech - ס)" translatedTo "Samech (ס)"))
        add(Timetable("阿波罗尼乌斯第六时：“此处因恐惧需保持静默、静止(此象征门槛守护者之可怖试炼，需莫大勇气方能战胜之)”" translatedTo "Sixth Hour of Apollonius: “Here it is necessary to remain quiet, still, due to fear (this signifies the terrible ordeal of the Guardian of the Threshold, before whom a lot of courage is needed in order to overcome him).”"))
        add(Transcendental("“彼等令我守护葡萄园，然我自身之葡萄园却未守护”" translatedTo "“They made me the keeper of the vineyards; but mine own vineyard have I not kept.”"))
        add(Forecasting("争议之预示，激情，厄运；借由合法途径之兴旺与厄运；令感受者与被影响者皆苦之有害情感；强烈之焦虑与激烈之局势。" translatedTo "It predicts controversies, passions, fatalities; prosperity through legality and fatality; noxious affections for the one who feels them, and for the one who is affected by them; vehement anxiety and violent situations."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_16 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Fragility")
        add(PandS("被雷霆击碎之塔。惩罚，可怕的坠落。避开此日期。" translatedTo "The Fulminated Tower. Punishment, terrible fall. Avoid this date."))
        add(Letter("阿因 (Ayin - ﬠ)" translatedTo "Ayin (ﬠ)"))
        add(Timetable("阿波罗尼乌斯第七时：“火可慰藉无生之物，若有祭司 —— 足够纯净之人 —— 取火而施，将其与圣油相混并祝圣，仅需敷于患处，即可治愈一切疾病(此处修行者将见自身物质财富受威胁，事业受挫)”" translatedTo "Seventh Hour of Apollonius: “Fire comforts inanimate beings, and if any priest, a sufficiently purified man, steals the fire and then projects it, if he mixes this fire with sacred oil and consecrates it, then he will achieve the healing of all sicknesses by simply applying it to the afflicted areas (here the initiate sees that his material wealth is threatened and his business fails).”"))
        add(Transcendental("“黎明之光，正午之光，黄昏之光，重要者，唯其为光也”" translatedTo "“Light at dawn, light of midday, light of nightfall, what is important is that it is light.”"))
        add(Forecasting("意外事故之预示，风暴，骚动，死亡；因境遇之好坏所生观念带来之益处；爱与恨、冷漠与热忱、背叛与忠诚中之相互性。" translatedTo "Predicts unexpected accidents, tempests, commotions, deaths; benefits because of concepts from good and bad circumstances; reciprocity in love and hatred, in indifference and in zeal, in treason and in loyalty."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_17 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Hope")
        add(PandS("希望之星。寓意怀抱希望并耐心等待。" translatedTo "The Star of Hope. Signifies to hope and wait."))
        add(Letter("佩 (Peh - פ)" translatedTo "Peh (פ)"))
        add(Timetable("阿波罗尼乌斯第八时：“诸元素之星光美德，各类种子之星光美德”" translatedTo "Eighth Hour of Apollonius: “The astral virtues of the elements, of the seeds of every genre.”"))
        add(Transcendental("“有人需征兆方信，有人需智慧方行，然怀望之心，以其望承载万物”" translatedTo "“Some men require signs in order to believe, others require wisdom in order to act, but the hopeful heart bears everything within its hopes.”"))
        add(Forecasting("直觉之预示，支持，启迪，新生，短暂之苦楚与短暂之满足，不悦与和解，匮乏；背弃与益处。" translatedTo "It predicts Intuition, support, illumination, births, brief afflictions and brief satisfactions, displeasures and reconciliations, privations; abandonments and benefits."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_18 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Twilight")
        add(PandS("隐秘的敌人。隐藏的敌人随时可能跳出。疾病，事业停滞。" translatedTo "Occult enemies. Hidden enemies jump out at any moment. Illnesses, no business."))
        add(Letter("萨迪 (Tzadi - צ)" translatedTo "Tzadi (צ)"))
        add(Timetable("阿波罗尼乌斯第九时：“此处诸事未竟。修行者拓展感知，直至超越太阳系之限、黄道之外，抵达无限之门槛，触及可知世界之边界。神圣之光向其显现，与此同时，新的恐惧与危险亦随之而来(此为小奥秘之研习，即修行者需攀登之九座拱廊)”" translatedTo "Ninth Hour of Apollonius: “Here nothing is finished yet. The initiate increases his perception until he surpasses the limits of the solar system, beyond the zodiac. He arrives at the threshold of the infinite. He reaches the limits of the intelligible world. The Divine Light is revealed unto him and with all of this, new fears and dangers also appear (it is the study of the Minor Mysteries, the nine arcades on which the student must ascend).”"))
        add(Transcendental("“愿汝之慈悲如不竭之仓，汝之忍耐亦如慈悲般不竭”" translatedTo "“May thy charity be an inexhaustible granary, and thy patience no less inexhaustible than thy charity.”"))
        add(Forecasting("不稳定之预示，无常，伏击，混乱，变迁，未定之境，冗长之斟酌，意外之阻碍，迟缓之结果，表面之胜利与失败。" translatedTo "It predicts instability, inconstancy, ambush, confusion, changes, uncertain situations, long deliberations, unexpected impediments, tardy results, apparent triumphs and failings."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_19 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Inspiration")
        add(PandS("璀璨的太阳。成功，好运，哲学石。" translatedTo "The radiant Sun. Success, good luck, the Philosophical Stone."))
        add(Letter("库夫 (Kuf - ק)" translatedTo "Kuf (ק)"))
        add(Timetable("阿波罗尼乌斯第十时：“天门敞开，人自昏沉中觉醒(此为大奥秘第二次重大启蒙之第十阶段，使修行者得以以太体游历，此乃施洗约翰之智慧)”" translatedTo "Tenth Hour of Apollonius: “The doors of heaven open and man comes out of his lethargy (this is the number ten of the second great Initiation of Major Mysteries, that allows the initiate to travel in the ethereal body. This is the wisdom of John the Baptist)."))
        add(Transcendental("“执汝信仰之盾，坚定迈步前行，无论风顺与否，纵诸风皆逆”" translatedTo "“Take the shield of thy faith and advance with a determined step, no matter if the wind is in thy favor or all the winds are against thy favor.”"))
        add(Forecasting("力量增长之预示，决断之成功，行事之喜悦，因个人与他人努力之念所获之益处，遗产，所求之物之明晰，及焚毁所求之火。" translatedTo "It predicts the increment of power, success in determination, joy in the acts that are performed, benefits for the concept of personal efforts and efforts of others, inheritances, clarity in that which is desired and fire that consumes the desired."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_20 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Resurrection")
        add(PandS("死者的复活。有利的转变，善加利用。终结弱点。" translatedTo "The resurrection of the dead. Favorable changes, take advantage of them. Put an end to weaknesses."))
        add(Letter("雷什 (Resh - ר)" translatedTo "Resh (ר)"))
        add(Timetable("阿波罗尼乌斯第十一时：“天使、基路伯与撒拉弗振翅飞旋；天界欢欣；自亚当而生之大地与太阳觉醒(此过程属大奥秘之重大启蒙，唯律法之威在此统御)”" translatedTo "Eleventh Hour of Apollonius: “The Angels, Cherubim, and Seraphim fly with the sound of whirring wings; there is rejoicing in heaven; the earth and the sun which surge from Adam awaken (this process belongs to the great Initiations of Major Mysteries, where only the terror of the Law reigns).”"))
        add(Transcendental("“苹果树开花，葡萄园结实，皆因审慎播种”" translatedTo "“Flower in the apple tree, fruit in the vineyard sown with prudence.”"))
        add(Forecasting("和谐抉择之预示，顺遂之创举，劳作，收益。因善与恶所获之补偿。抵消背叛之友行径之忠诚之友。因所享之善而生之嫉妒。因失去而生之苦楚。" translatedTo "It predicts harmonious choices, fortunate initiatives, labor, profit. Compensations because of good and because of bad. Loyal friends that annul the action of traitorous friends. Jealousy for the good that is enjoyed. Afflictions because of loss."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_21 = ComponentListSupplier {
        setTranslationPrefix("Tarot-Transmutation")
        add(PandS("愚人，疯狂。向邪恶的彻底堕落。魔法钥匙，Olin如尼文。对立面，希兰·阿比夫的敌人。" translatedTo "The Fool, madness. Total demoralization towards evil. The magic key, the rune Olin. Antithesis, enemies of Hiram Abiff."))
        add(Letter("辛 (Shin - ש)" translatedTo "Shin (ש)"))
        add(Timetable("阿波罗尼乌斯第十二时：“火之高塔震动(此为大师凯旋进入涅槃之无限极乐，或更确切言，大师为爱人道而舍弃涅槃极乐，遂化为慈悲之菩萨)”" translatedTo "Twelfth Hour of Apollonius: “The towers of fire disturb (this is the triumphant entrance of the master into the limitless bliss of Nirvana, or better, the master’s renunciation of the bliss of Nirvana for the love of humanity, where then he is converted into a Bodhisattva of compassion).”"))
        add(Transcendental("“吾魂未入其奥秘，吾舟亦未抵其港湾”" translatedTo "“My soul does not enter into His secret, neither my ship into His port.”"))
        add(Forecasting("被排斥于所享之物外之预示，求而不得之沮丧。与所夸耀之物相关之毁灭。孤立之险，背信之赠，虚妄之诺，幻灭；诸事之终结与诸事之开端。" translatedTo "It predicts exclusion from something that is enjoyed, frustration when trying to achieve what is wanted. Ruin in relation to that which we boast of. It is danger of isolation, perfidious gifts, deceitful promises, disillusions; the end of some things and the beginning of others."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_22 = ComponentListSupplier {
        setTranslationPrefix("Tarot-The Return")
        add(PandS("真理，生命之冠。凯旋。一切圆满。力量，强力，好运。" translatedTo "The truth, the Crown of Life. Triumph. Everything comes out well. Power, strength, good luck."))
        add(Letter("塔夫 (Tav - ת)" translatedTo "Tav (ת)"))
        add(Timetable("“有第十三时，此乃解脱之时”" translatedTo "“There is a thirteenth hour; this is the hour of liberation.”"))
        add(Transcendental("“日升日落，疾驰归其诞生之地”" translatedTo "“The sun ariseth and the sun goeth down and it hasteth to its place where it was born.”"))
        add(Forecasting("长寿之预示，遗产，名望，正当享乐之愉悦，争夺情感之对手，关怀吾辈之友人，障碍与克障之能，未定之境与廓清其境之偶然事件。" translatedTo "Predicts longevity, inheritances, notability, delight of honest enjoyments, rivals that dispute the affections, friends that care for us, obstacles and aptitude in order to defeat them, uncertain situations and contingencies that clarify them."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_23 = ComponentListSupplier {
        // 耕者
        setTranslationPrefix("Tarot-The Plower")
        add(Description("耕者正耕耘土地与自身意识，象征人类自我实现之美德。" translatedTo "The plower is in the act of cultivating the earth and his consciousness. Symbolizes the virtue of a human’s self-realization."))
        add(Modulating("关联于水星的运行、字母 T 及数字 5，代表元素智慧在习得收获经验之果过程中的作用。" translatedTo "It is associated to the action of the planet Mercury, the letter T, the number 5. It represents the elemental intelligence in its labor of knowing how to harvest the fruits of experience."))
        add(Transcendental("“吾之磨坊，为吾磨粉，亦为邻人磨粉”" translatedTo "“My mill is grinding flour for me and flour for my neighbor.”"))
        add(Forecasting("预示有影响力的友人、需其相助之事，及若笃信友谊便能获得援助之能；象征借由该等友人及自身意志力实现的提升。" translatedTo "It promises powerful friends, the necessity of their help, and the ability to attain it if one has faith in friendship. It signifies elevation by means of those friends and one’s own willpower."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_24 = ComponentListSupplier {
        // 织者
        setTranslationPrefix("Tarot-The Weaver")
        add(Description("织者正培育家庭美德，象征身为家庭与荣誉守护者的勤勉女性。" translatedTo "The weaver in the act of cultivating domestic virtues. Symbolizes the diligent woman as keeper of home and honor."))
        add(Modulating("关联于金星的运行、字母U及数字6，代表元素智慧在将努力运用于所学之物成果过程中的作用。" translatedTo "It is associated with the action of the planet Venus, the letter U, and the number 6. It represents the elemental intelligence in its labor of applying exertion to the fruits of learned things."))
        add(Transcendental("“吾之织机，织网不绝；一布为吾之荣誉，一布为致敬之礼。”" translatedTo "“My loom is weaving net after net; a cloth for my honor and cloth to honor.”"))
        add(Forecasting("预示有序的理财、孕期的贞洁、女性的庇护，及义务的妥善履行。" translatedTo "It promises organized economy, chastity in maternity, feminine protection and good performance of obligations."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_25 = ComponentListSupplier {
        // 阿尔戈英雄
        setTranslationPrefix("Tarot-The Argonaut")
        add(Description("阿尔戈英雄正航行探索未知之地，象征人类凭借自身灵感所具之美德。" translatedTo "The argonaut in the act of sailing in search of the unknown. Symbolizes the virtue of a human’s own inspiration."))
        add(Modulating("关联于海王星、字母V及数字7，代表勇敢面对未知危险的人。" translatedTo "It is associated to the planet Neptune, the letter V, and the number 7. It represents the valiant man who confronts the dangers of the unknown."))
        add(Transcendental("“吾之航船，持续航行；昼亦航行，夜亦航行。”" translatedTo "“My boat is sailing, sailing persistently; sailing by night, sailing by day.”"))
        add(Forecasting("预示离别、迁徙、舍弃、变迁、家庭不和，有所得亦有诸多所失。" translatedTo "It promises absences, emigrations, abandonment, changes, domestic discords, something that is acquired and much that is lost."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_26 = ComponentListSupplier {
        // 奇能者
        setTranslationPrefix("Tarot-The Prodigy")
        add(Description("奇能者正完成创举，象征人类创造惊人之物、探寻奇妙之境所具之美德。" translatedTo "The prodigy in the act of consummation. Symbolizes the human virtue of creating what is astounding and which searches the marvelous."))
        add(Modulating("关联于土星、字母X及数字8，代表时间作为正义与显化之力的作用。" translatedTo "It is associated to the planet Saturn, the letter X, and the number 8. It represents the action of time as justice and power of manifestation."))
        add(Transcendental("“时有撕裂，时有缝补；时有缄默，时有言说。”" translatedTo "“A time to rend, and a time to sew; a time to keep silence, and a time to speak.”"))
        add(Forecasting("预示终将发生的事件、潜在的危险、意外的变故、未曾预想的启示。" translatedTo "It promises episodes that are fulfilled, threatening dangers, surprising events, unthought teachings."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_27 = ComponentListSupplier {
        // 意外者
        setTranslationPrefix("Tarot-The Unexpected")
        add(Description("意外者正显现身形，象征人类潜意识运作过程所具之美德。" translatedTo "The unexpected in the act of becoming manifested. Symbolizes the virtue of subconscious human processes."))
        add(Modulating("关联于火星、字母Y及数字9，代表内在生命作为外在世界决定因素的存在。" translatedTo "It is associated to the planet Mars, the letter Y, and the number 9. It represents inner life as a determining cause of the outer."))
        add(Transcendental("“勿以过多蜜糖增甜，勿以虚妄荣耀立身。”" translatedTo "“Neither excess honey to sweeten, nor vainglory to thrive.”"))
        add(Forecasting("预示有条件的成功、意外变故、共谋之事、背叛之举、有所发现（其中部分为时较晚）。" translatedTo "It promises conditioned triumphs, surprises, conjurations, treasons, findings, discoveries, some of them tardy."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_28 = ComponentListSupplier {
        // 犹疑者
        setTranslationPrefix("Tarot-Uncertainty")
        add(Description("犹疑者处于斟酌权衡之中，象征人类自主决断所具之美德。" translatedTo "Uncertainty in the act of deliberation. Symbolizes the virtue of a human’s own determination."))
        add(Modulating("关联于冥王星、字母Z及数字1，代表判断力作为行为决定因素的作用。" translatedTo "It is associated to the planet Pluto, the letter Z, and the number 1. It represents judgment as a determinant cause of acts."))
        add(Transcendental("“勿于他人处寻汝自身所有，亦勿于自身处寻他人所有。”" translatedTo "“Do not seek in others for what is in thee; neither seek in thee for what is in others.”"))
        add(Forecasting("预示延迟、阻碍、对立、待解的谜团，及破解谜团所需的知识。" translatedTo "It promises delays, obstacles, contrarieties, mysteries to be solved and science to solve them."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_29 = ComponentListSupplier {
        // 居家者
        setTranslationPrefix("Tarot-Domesticity")
        add(Description("居家者处于和睦共处之中，象征人类借由劝服实现掌控所具之美德。" translatedTo "Domesticity as an act of concord. Symbolizes the virtue of human domination by means of persuasion."))
        add(Modulating("关联于月亮、字母B及数字2，代表自然之安宁、元素之平衡、田园之喜乐。" translatedTo "It is associated to the Moon, the letter B, and the number 2. It represents Nature’s peace, equilibrium of elements, pastoral joy."))
        add(Transcendental("“愿汝之眼眸化为青春之眸，愿汝之言语化为长者之智。”" translatedTo "“Let your eyes become youthful eyes, and let thy word become an elder’s prudence.”"))
        add(Forecasting("预示懊悔、迟疑、困惑、怯懦、田园生活、有利的事务、虽有挑战却能收获满意结果的事业。" translatedTo "It promises remorse, indecisions, perplexities, timidity, pastoral life, advantageous affairs, enterprises that bring struggles but with satisfactory outcomes."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_30 = ComponentListSupplier {
        // 交易者
        setTranslationPrefix("Tarot-Exchange")
        add(Description("交易者处于互惠互利之中，象征人类社会生活所具之美德。" translatedTo "Exchange as an act of reciprocal convenience. Symbolizes the virtue of human life in society."))
        add(Modulating("关联于木星、字母C及数字3，代表借由商业往来实现的个体拓展。" translatedTo "It is associated to the planet Jupiter, the letter C, and the number 3. It represents individual expansion by means of commercial conviviality."))
        add(Transcendental("“悉心耕种汝之土地，勿以贪婪收取汝之物产。”" translatedTo "“Reap thy land carefully, but do not harvest your goods with avarice.”"))
        add(Forecasting("预示社交生活、人际沟通、商业往来、交通出行、事务繁杂、议论争执（然多无定论）。" translatedTo "It promises social life, intercommunications, commerce, traffic, variety, discussion, although many times without solution."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_31 = ComponentListSupplier {
        // 阻碍者
        setTranslationPrefix("Tarot-Impediments")
        add(Description("阻碍者成为提升自身效能的动力，象征人类对抗阻力时所具之反应美德。" translatedTo "Impediments as incentives for the developing of one’s own efficiency. Symbolizes the virtue of human reaction against the opposition."))
        add(Modulating("关联于天王星、字母CH及数字4，代表净化原则作为进步要素的作用。" translatedTo "It is associated to the planet Uranus, the letters CH, and the number 4. It represents the principle of depuration as an element of progress."))
        add(Transcendental("“勿行无情之评判，亦勿为无断之宽恕。”" translatedTo "“Do not pass merciless judgment, neither have mercy without judgment.”"))
        add(Forecasting("预示期待、承诺、家庭计策、限制约束、低质交易，及未必对双方皆有利的处境。" translatedTo "It promises expectations, promises, domestic devices, restrictions, inferior quality trading and a position not always beneficial for both parties."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_32 = ComponentListSupplier {
        // 盛显者
        setTranslationPrefix("Tarot-Magnificence")
        add(Description("盛显者处于物质共享之中，象征人类彰显自身价值所具之美德。" translatedTo "Magnificence as an act of material communion. Symbolizes the virtue of evidencing a human’s own value."))
        add(Modulating("关联于水星、字母D及数字5，代表炫耀原则作为引发争议原因的作用。" translatedTo "It is associated to the planet Mercury, the letter D, and the number 5. It represents the principle of ostentation as a cause of discussion."))
        add(Transcendental("“竭尽汝之才智，勿竭汝之善心。”" translatedTo "“Exhaust the resources of thine intelligence, but do not exhaust those of thine heart.”"))
        add(Forecasting("预示因易怒或多言过度引发的危险、物质财富的炫耀、诉讼纠纷、成为成败诱因的奢华，及同级者间的对立或合作。" translatedTo "It promises dangers because of choleric or loquacious excess, ostentation of material elements, litigations, luxury as a cause of triumph or failure, oppositions or cooperations between people of equal hierarchy."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_33 = ComponentListSupplier {
        // 同盟者
        setTranslationPrefix("Tarot-Alliance")
        add(Description("同盟者处于平等共融之中，象征人类借由同一性实现亲和所具之美德。" translatedTo "Alliance as an act of communion between equals. Symbolizes the virtue of human affinity through identity."))
        add(Modulating("关联于金星、字母E及数字6，代表借由联合实现自我成就的原则。" translatedTo "It is associated to the planet Venus, the letter E, and the number 6. It represents the principle of one’s own realization by means of association."))
        add(Transcendental("“为青春之爱喜悦，更为成熟之爱欢欣。”" translatedTo "“Rejoice with the love of thy youth, and rejoice even more with the love of thine maturity.”"))
        add(Forecasting("预示精准的往来、持久的同盟、日益增长的繁荣、稳步的进步、顺遂的偶然事件。" translatedTo "It promises dealings with precision, lasting alliances, increasing of prosperity, progress, happy contingencies."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_34 = ComponentListSupplier {
        // 革新者
        setTranslationPrefix("Tarot-Innovation")
        add(Description("革新者作为进化的要素，象征人类坚定不移努力所具之美德。" translatedTo "Innovation as an element of evolution. Symbolizes the virtue of undeviating human effort."))
        add(Modulating("关联于海王星、字母F及数字7，代表以自我启发为行动指引的原则。" translatedTo "It is associated to the planet Neptune, the letter F, and the number 7. It represents the principle of self-inspiration as guidance for activities."))
        add(Transcendental("“长寿在汝右手，功业荣誉在汝左手。”" translatedTo "“Length of days upon thy right hand and works and honor upon thy left.”"))
        add(Forecasting("预示发明创造、新兴事业、诱惑之事、鲁莽之举、有所发现（部分顺遂，部分令人不安）。" translatedTo "It promises inventions, new enterprises, temptations, temerity, findings­—some fortunate, others disconcerting."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_35 = ComponentListSupplier {
        // 悲戚者
        setTranslationPrefix("Tarot-Grief")
        add(Description("悲戚者作为道德苦楚的表现，象征人类苦难在净化作用中所具之美德。" translatedTo "Grief as an act of moral affliction. Symbolizes the virtue of human tribulation in its depurating action."))
        add(Modulating("关联于土星、字母G及数字8，代表认知自身不足的原则。" translatedTo "It is associated to the planet Saturn, the letter G, and the number 8. It represents the principle of the knowledge of one’s own insufficiency."))
        add(Transcendental("“当下之物源于过往，过往之物即为当下。”" translatedTo "“That which is now comes from that which has been, and what has been is what shall be now.”"))
        add(Forecasting("预示警报、惊惶、忧思、悲伤、阻碍、意外变故。" translatedTo "It promises alarms, consternations, melancholies, sadness, obstacles, unexpected events."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_36 = ComponentListSupplier {
        // 启蒙者
        setTranslationPrefix("Tarot-Initiation")
        add(Description("启蒙者作为焕发生机的行为，象征人类力量逐步实现所具之美德。" translatedTo "Initiation as a revivifying act. Symbolizes the virtue of progressive actualization of human powers."))
        add(Modulating("关联于火星、字母H及数字9，代表培养自身美德的原则。" translatedTo "It is associated to the planet Mars, the letter H, and the number 9. It represents the principle of developing one’s own virtues."))
        add(Transcendental("“研习可乐，歌唱可乐，聆听可乐。”" translatedTo "“Pleasant is the studio, pleasant it is to sing, pleasant it is to listen.”"))
        add(Forecasting("预示新生、新的开端、前提条件、即将到来的好运、事业成功、放纵行为、衰退之势。" translatedTo "It promises births, new beginnings, premises, forthcoming fortune, success in businesses, dissipations, declinations."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_37 = ComponentListSupplier {
        // 艺智者
        setTranslationPrefix("Tarot-Art and Science")
        add(Description("艺智者作为个体进化的要素，象征人类借由自律推动进步所具之美德。" translatedTo "Art and science as factors of individual evolution. Symbolizes the virtue of a human’s own discipline as an element of progress."))
        add(Modulating("关联于太阳、字母I及数字1，代表借由应用知识实现创造的原则。" translatedTo "It is associated to the Sun, the letter I, and the number 1. It represents the principle of creation by means of applied knowledge."))
        add(Transcendental("“应许之地在吾眼前，愿吾双足助吾抵达。”" translatedTo "“Behold the Promised Land before mine eyes; help me, oh foot, to arrive there.”"))
        add(Forecasting("预示真挚的友谊、有力的援助、正直品格、公平公正、必需的资源、强迫之事、堕落之举、受人追捧（然非全然有益）。" translatedTo "It promises sincere friendship, magnificent aids, probity, equity, imperative resources, compulsions, perversions, popularity, but not entirely edifying."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_38 = ComponentListSupplier {
        // 双态者
        setTranslationPrefix("Tarot-Biplicity")
        add(Description("双态者作为聪慧的表现，象征人类主动获取知识所具之美德。" translatedTo "Biplicity as an act of sagacity. Symbolizes the human virtue of knowledge voluntarily induced."))
        add(Modulating("关联于月亮、字母J及数字2，代表以对立事物为比较与选择要素的原则。" translatedTo "It is associated to the Moon, the letter J, and the number 2. It represents the principle of the antonymous as an element of comparison and selection."))
        add(Transcendental("“贤德女子，见汝行事者，未必皆见汝美德。”" translatedTo "“Virtuous woman, not all of those who see thy works can see thy virtues.”"))
        add(Forecasting("预示女性对自身事务的影响、处理事务时的聪慧与迟钝、美德与丑闻、正直与邪恶、贞洁与堕落。" translatedTo "It promises the influence of women in one’s affairs, wisdom and torpidity when resolving them, virtue and scandal, honesty and vice, chastity and corruption."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_39 = ComponentListSupplier {
        // 见证者
        setTranslationPrefix("Tarot-Testimony")
        add(Description("见证者作为无可辩驳的证据，象征人类借由验证获取证明所具之美德。" translatedTo "Testimony as an irrecusable evidence. Symbolizes the human virtue of verification for proof."))
        add(Modulating("关联于木星、字母K及数字3，代表以论证为信服要素的原则。" translatedTo "It is associated to the planet Jupiter, the letter K, and the number 3. It represents the principle of demonstration as an element of conviction."))
        add(Transcendental("“让汝之意图在自身面前作证，让汝之行事在他人面前作证。”" translatedTo "“Place thy intentions as witnesses before thyself; however, place thy works as witnesses before others.”"))
        add(Forecasting("预示顺从、接近、比较、宴饮、旅行、延迟的婚姻、疑似或真实的通奸、意外或预料中的到访。" translatedTo "It promises conformity, approximations, comparison, banquets, traveling, delayed matrimonies, presumed or real adultery, unforeseen or foreseen arrivals."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_40 = ComponentListSupplier {
        // 预感者
        setTranslationPrefix("Tarot-Presentiment")
        add(Description("预感者作为本能的认知，象征人类预先知晓未来之事所具之美德。" translatedTo "Presentiment as an instinctual knowledge. Symbolizes the human virtue of anticipated knowledge of what shall occur."))
        add(Modulating("关联于天王星、字母L及数字4，代表以预知为自然能力的原则。" translatedTo "It is associated to the planet Uranus, the letter L, and the number 4. It represents the principle of prescience as a natural faculty."))
        add(Transcendental("“勿做眼目贪吝之男，勿做耳根谄媚之女。”" translatedTo "“Do not be a man with open eyes to covetousness, nor a woman with open ears to flattery.”"))
        add(Forecasting("预示回报丰厚的工作、专注投入、深思熟虑、吸引、好感、诱惑、破败之险、令人难堪的情感。" translatedTo "It promises a very well-retributive work, application, reflection, attraction, sympathy, seduction, threat of ruin, mortifying affections."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_41 = ComponentListSupplier {
        // 不安者
        setTranslationPrefix("Tarot-Uneasiness")
        add(Description("不安者作为沮丧的态度，象征人类始终追求至善所具之美德。" translatedTo "Uneasiness as an act of disheartened attitude. Symbolizes the human virtue of always seeking for the best."))
        add(Modulating("关联于水星、字母LL及数字5，代表城市生活的原则。" translatedTo "It is associated to the planet Mercury, the letters LL, and the number 5. It represents the principle of urban life."))
        add(Transcendental("“强者之弓已断，弱者之弓得力。”" translatedTo "“The bows of the mighty ones were broken, and the bows of the weak were fitted with strength.”"))
        add(Forecasting("预示愤慨、暴力、人群涌动、人丁兴旺的家庭、青涩恋情、反义层面的欢愉。" translatedTo "It promises indignation, violence, group of people in motion, bountiful family, puppy loves, joyful in the opposite sense."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_42 = ComponentListSupplier {
        // 卓越者
        setTranslationPrefix("Tarot-Preeminence")
        add(Description("卓越者作为对优越性的认可，象征人类接纳等级秩序所具之美德。" translatedTo "Preeminence as acknowledgment of superiority. Symbolizes the human virtue of acceptation of hierarchical order."))
        add(Modulating("关联于金星、字母M及数字6，代表权力与自愿服从的原则。" translatedTo "It is associated to the planet Venus, the letter M, and the number 6. It represents the principle of power and voluntary obedience."))
        add(Transcendental("“若身处至尊之位、手握至强之权，当配至优之德。”" translatedTo "“If utmost in dignity and power, be then utmost in thy merits.”"))
        add(Forecasting("预示优势、胜利、浮夸、优越性、真诚、意外的爱情。" translatedTo "It promises advantages, triumph, pomposity, superiority, sincerity, surprising loves."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_43 = ComponentListSupplier {
        // 想象者
        setTranslationPrefix("Tarot-Imagination")
        add(Description("想象者作为愉悦的欣喜之举，象征人类表达满足所具之美德。" translatedTo "Imagination as a heartened rejoicing act. Symbolizes the human virtue of the expression of contentment."))
        add(Modulating("关联于海王星、字母N及数字7，代表由诱发思想构成的创造原则。" translatedTo "It is associated to the planet Neptune, the letter N, and the number 7. It represents the creative principle of induced ideas."))
        add(Transcendental("“吾心之喜乐，饰吾之容颜。”" translatedTo "“Joyfulness of my heart, beautify my face.”"))
        add(Forecasting("预示满足、惬意、得体、谦逊、享乐、宴庆、愉快的筹备、强烈的激情、禁忌的爱情、离婚、兴旺的事业。" translatedTo "It promises satisfactions, contentment, decency, modesty, enjoyments, parties, pleasant preparations, violent passions, forbidden loves, divorce, prosperous businesses."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_44 = ComponentListSupplier {
        // 思想者
        setTranslationPrefix("Tarot-Thought")
        add(Description("思想作为创造的要素，象征人类为可理解之物赋予可感知形态所具之美德。" translatedTo "Thought as a creative element. Symbolizes the human virtue of giving sensible shape to that whose shape is intelligible."))
        add(Modulating("关联于土星、字母Ñ（涅）及数字8，代表自我塑造的创造原则。" translatedTo "It is associated to the planet Saturn, the letter Ñ, and the number 8. It represents the creative principle of self-edification."))
        add(Transcendental("“以智克疑，怀望享己之所有。”" translatedTo "“Overcome suspicion wisely, and enjoy what is yours in hope.”"))
        add(Forecasting("预示计划、斟酌、决议、决断、相互理解，经艰辛奋斗后借由异性之力实现所求之物。" translatedTo "It promises projects, deliberation, resolutions, determinations, mutual understanding, fulfillment of what is desired after arduous struggles, by means of a person of the opposite sex."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_45 = ComponentListSupplier {
        // 重生者
        setTranslationPrefix("Tarot-Regeneration")
        add(Description("重生作为反复之举，象征人类铭记过往、预判未来所具之美德。" translatedTo "Regeneration as a reiterative act. Symbolizes the human virtue of remembering the past and preventing the future."))
        add(Modulating("关联于火星、字母O及数字9，代表记忆与具象化的原则。" translatedTo "It is associated to the planet Mars, the letter O, and the number 9. It represents the principle of memorization and visualization."))
        add(Transcendental("“万物适时则美，熟透则香。”" translatedTo "“Everything is beautiful in its time, and everything is flavorful in its ripeness.”"))
        add(Forecasting("预示灵性重生、身体重获青春、对过往之事的反思与未来之事的初果、活力、衰朽、来自幼年与老年的印记、亲属对感情事务的影响、破裂与舍弃。" translatedTo "It promises spiritual resurrection, physical rejuvenation, reconsideration of that which was and some first-fruits of that which will be, vigor, decrepitude, something from infancy and from elderhood, influence of relatives in loving matters, breakage and abandonment."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_46 = ComponentListSupplier {
        // 遗产者
        setTranslationPrefix("Tarot-Patrimony")
        add(Description("遗产作为延续之举，象征人类借由继承获得所有所具之美德。" translatedTo "Patrimony as an act of continuity. Symbolizes the human virtue of possession by inheritance."))
        add(Modulating("关联于太阳、字母P及数字1，代表自然延续的原则。" translatedTo "It is associated to the Sun, the letter P, and the number 1. It represents the principle of natural prosecution."))
        add(Transcendental("“安于小者，自启丰裕之门。”" translatedTo "“The one who is content in the little will open the doors of plethora.”"))
        add(Forecasting("预示友谊遗赠、凭自身权利获得的遗产、馈赠、遗嘱、传统、先祖、家庭、夫妻亲和力；对男性而言：因女性而遇险；对女性而言：受诱惑之险。" translatedTo "It promises friendship bequest, inheritances by own rights, donations, testaments, traditions, forefathers, family, conjugal affinity; for a man: danger because of a woman; for a woman: danger of seduction."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_47 = ComponentListSupplier {
        // 演绎者
        setTranslationPrefix("Tarot-Deduction")
        add(Description("演绎作为认知之举，象征人类借由自身灵感获得认知所具之美德。" translatedTo "Deduction as an act of knowledge. Symbolizes the human virtue of knowing by means of one’s own inspiration."))
        add(Modulating("关联于月亮、字母Q及数字2，代表作为启迪要素的演绎原则。" translatedTo "It is associated to the Moon, the letter Q, and the number 2. It represents the principle of deduction as an element of illumination."))
        add(Transcendental("“于光中闪耀、于火中承热，此乃汝今时及往后之使命。”" translatedTo "“To be resplendent in the light and the heat in the fire is now thy mission, and from now on.”"))
        add(Forecasting("预示新的启迪、新的教义、灵性苦楚、成熟的希望、即将到来的喜乐或苦难、亲属的支持；贞洁且持久的爱情。" translatedTo "It promises new lights, new teachings, spiritual afflictions, mature hopes, coming joys or sufferings, support of relatives; virtuous and lasting loves."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_48 = ComponentListSupplier {
        // 圆满者
        setTranslationPrefix("Tarot-Consummation")
        add(Description("圆满作为圆满之举，象征人类自身确信所具之美德。" translatedTo "The consummation as an act of plenitude. Symbolizes the human virtue of one’s own conviction."))
        add(Modulating("关联于木星、字母R及数字3，代表行为决断的原则。" translatedTo "It is associated to the planet Jupiter, the letter R, and the number 3. It represents the principle of determination of conduct."))
        add(Transcendental("“饮汝蓄水池之滴，或汝泉眼之流。”" translatedTo "“Drink the drops of thy cistern or the flowing of thy well.”"))
        add(Forecasting("预示成就、学识、胜利、结论、决议、无望的爱情、无法挽回之物。" translatedTo "It promises achievements, science, victory, conclusions, resolutions, impossible loves, that which is already irremediable."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_49 = ComponentListSupplier {
        // 多能者
        setTranslationPrefix("Tarot-Versatility")
        add(Description("多能作为互惠之举，象征人类等价对应所具之美德。" translatedTo "Versatility as an act of reciprocity. It symbolizes the human virtue of correspondence in equivalents."))
        add(Modulating("关联于天王星、字母S及数字4，代表作为繁荣要素的置换原则。" translatedTo "It is associated to the planet Uranus, the letter S, and the number 4. It represents the principle of permutation as element of prosperity."))
        add(Transcendental("“欣然劳作之人，其劳作可乐；应得闲暇者，其闲暇可乐。”" translatedTo "“Pleasant is the work to whom labors with contentment, and pleasant is the leisure to the one who deserves it.”"))
        add(Forecasting("预示迁移、变迁、新的情感、爱情与事业中的竞争、种种变迁（部分向好）。" translatedTo "It promises removal, changes, new affections, rivalry in loves and undertakings, vicissitudes, but some for the better."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_50 = ComponentListSupplier {
        // 亲和者
        setTranslationPrefix("Tarot-Affinity")
        add(Description("亲和作为唤醒情感的有效动因，象征人类个人吸引力所具之美德。" translatedTo "Affinity as an efficient cause for the awakening of emotions. Symbolizes the human virtue of personal attractiveness."))
        add(Modulating("关联于水星、字母T及数字5，代表作为行为调节力量的自然诱因原则。" translatedTo "It is associated to the planet Mercury, the letter T, and the number 5. It represents the principle of natural incentive as a modulating power of actions."))
        add(Transcendental("“女子啊，汝乃火中之烬；男子啊，汝乃燃烬之风。”" translatedTo "“Woman, thou art an ember in the fire; and thou, man, the wind that livens it.”"))
        add(Forecasting("预示幻象、激情、欲望、爱恋、幻觉、鲁莽、被忽视的危险。" translatedTo "It promises illusions, passions, appetites, desires, love, hallucinations, recklessness, dangers that are ignored."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_51 = ComponentListSupplier {
        // 献策者
        setTranslationPrefix("Tarot-Counseling")
        add(Description("献策作为审慎建议之举，象征人类尊崇知识、对所知负责所具之美德。" translatedTo "Counseling as an act of prudent advice. Symbolizes the human virtue of reverence to knowledge and responsibility of what one knows."))
        add(Modulating("关联于金星、字母U及数字6，代表对既定秩序尊崇的原则。" translatedTo "It is associated to the planet Venus, the letter U, and the number 6. It represents the principle of respect to established order."))
        add(Transcendental("“智者之言可比白银，而听从践行者可比黄金。”" translatedTo "“Unto silver is likened the word uttered by the sage, yet unto gold is likened the one who listens and follows him.”"))
        add(Forecasting("预示审判、司法事务、与权贵之人的往来、该群体中存在的危险敌人；在武职或司法领域的好运、背信之险。" translatedTo "It promises judgement, judicial matters, relations with people of authority, dangerous enemies among those people; fortune in the field of weapons or the magistracy, danger of perfidies."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_52 = ComponentListSupplier {
        // 谋算者
        setTranslationPrefix("Tarot-Premeditation")
        add(Description("谋算作为谋划之举，象征人类预先构想渴望结果所具之美德。" translatedTo "Premeditation as an act of calculation. Symbolizes the human virtue that preconceives the longed-for results."))
        add(Modulating("关联于海王星、字母V及数字7，代表要素评估的原则。" translatedTo "It is associated to the planet Neptune, the letter V, and the number 7. It represents the principle of evaluation of factors."))
        add(Transcendental("“汝之一言一语皆当用心，然勿将所有言语皆藏于心。”" translatedTo "“Place thine heart in all thy words, but do not place all the words within thine heart.”"))
        add(Forecasting("预示诡计、伪装姿态、恶意、掩饰、狡诈、匮乏、贫困、女性间的怨恨及由此引发的争斗。" translatedTo "It promises artifices, faking manners, malice, dissimulation, astuteness, sterility, indigence, feminine hatreds and fights as a consequence of such hatreds."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_53 = ComponentListSupplier {
        // 怨恨者
        setTranslationPrefix("Tarot-Resentment")
        add(Description("怨恨作为心碎之举，象征人类自我防卫所具之美德。" translatedTo "Resentment as a wounded heartened act. Symbolizes the human virtue of one’s own defense."))
        add(Modulating("关联于土星、字母X及数字8，代表报复原则。" translatedTo "It is associated to the planet Saturn, the letter X, and the number 8. It represents the reprisal principle."))
        add(Transcendental("“勿挥复仇之剑，亦勿惧惩戒之剑。”" translatedTo "“Do not swing a revengeful sword, neither fear the corrective sword.”"))
        add(Forecasting("预示愤怒、鲁莽、无能、攻击、防卫、批评、诽谤、火灾之险、与有身份者的争斗。" translatedTo "It promises rage, imprudence, ineptitude, attacks, defense, criticisms, calumnies, danger by the fire, fights against people of status."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_54 = ComponentListSupplier {
        // 审视者
        setTranslationPrefix("Tarot-Examination")
        add(Description("审视作为关乎结果的振奋性斟酌，象征人类秉持正义行事所具之美德。" translatedTo "Examination as a heartening deliberation upon conclusion. Symbolizes the human virtue of proceeding with justice."))
        add(Modulating("关联于火星、字母Y及数字9，代表自由探讨的原则。" translatedTo "It is associated to the planet Mars, the letter Y, and the number 9. It represents the principle of free debate."))
        add(Transcendental("“遍历汝乡，拾取遗穗，然勿寻怨，亦勿积过。”" translatedTo "“Search around thy village and harvest the fallen grains, but do not search for resentments, neither collect misdemeanors.”"))
        add(Forecasting("预示推测、查问、指控、背叛；隐秘的敌人；诽谤中伤。" translatedTo "It promises speculations, enquiries, imputations, treasons; occult enemies; defamation."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_55 = ComponentListSupplier {
        // 悔悟者
        setTranslationPrefix("Tarot-Contrition")
        add(Description("悔悟作为忏悔之举，象征人类承认自身过错所具之美德。" translatedTo "Contrition as an act of repentance. Symbolizes the human virtue of recognizing one’s own error."))
        add(Modulating("关联于冥王星、字母A及数字1，代表主动弥补的原则。" translatedTo "It is associated to the planet Pluto, the letter A, and the number 1. It represents the principle of voluntary repair."))
        add(Transcendental("“勿在汝果园播杂种，亦勿在汝心收获之。”" translatedTo "“Do not sow an assortment of seeds in thine orchard, neither harvest them within thine heart.”"))
        add(Forecasting("预示哀叹、灵性苦楚、不快之事、未得满足的收益、借由道德苦楚获得物质财富。" translatedTo "It promises lamentations, spiritual afflictions, unpleasantness, unsatisfied profit, acquisition of material wealth by means of moral suffering."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_56 = ComponentListSupplier {
        // 朝圣者
        setTranslationPrefix("Tarot-Pilgrimage")
        add(Description("朝圣作为内在净化之举，象征人类承受苦难所具之美德。" translatedTo "Pilgrimage as an act of interior purification. Symbolizes the human virtue of affliction."))
        add(Modulating("关联于月亮、字母B及数字2，代表自我救赎的原则。" translatedTo "It is associated to the Moon, the letter B, and the number 2. It represents the principle of one’s own redemption."))
        add(Transcendental("“慰藉受苦者，振奋其心；处自身患难，亦当保持热忱。”" translatedTo "“Hearten and comfort the afflicted one, and keep thyself enthused within thine own tribulations.”"))
        add(Forecasting("预示独身生活、待克服的怯懦、与圣所或朝圣者相关之事、苦难、隐秘的危险、权贵间的敌意。" translatedTo "It promises celibacy, timidity to overcome, matters related with sanctuaries or with people who attend them, sufferings, mysterious dangers, enmity amongst people of status."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_57 = ComponentListSupplier {
        // 竞争者
        setTranslationPrefix("Tarot-Rivalry")
        add(Description("竞争作为对手间的较量之举，象征人类自我尊重所具之美德。" translatedTo "Rivalry as a proof of competition between opponents. Symbolizes the human virtue of one’s own esteem."))
        add(Modulating("关联于木星、字母C及数字3，代表技艺能力的原则。" translatedTo "It is associated to the planet Jupiter, the letter C, and the number 3. It represents the principle of skill."))
        add(Transcendental("“义人虽七次跌倒，若其真为义人，必七次兴起。”" translatedTo "“For a just man falls seven times, and if he indeed is just, seven times he rises up again.”"))
        add(Forecasting("预示微妙处境、危急时刻、反对之声、事件的交织、批评、辩论、命中注定之事、命运的实现。" translatedTo "It promises delicate circumstances, critical moments, oppositions, conjunction of events, criticism, debates, what is fatal, the fulfillment of destiny."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_58 = ComponentListSupplier {
        // 重整者
        setTranslationPrefix("Tarot-Requalification")
        add(Description("重整作为重新考量事实之举，象征人类权衡对立双方所具之美德。" translatedTo "Requalification as an act of reconsideration of facts. Symbolizes the human virtue of evaluation of opposites."))
        add(Modulating("关联于天王星、字母CH及数字4，代表通过检验推测的原则。" translatedTo "It is associated to the planet Uranus, the letters CH, and the number 4. It represents the principle of conjecturing by exam."))
        add(Transcendental("“若邻人令汝困惑，当与己心商讨此事。”" translatedTo "“If thy neighbor leaves thee confused, consult the case with thine own heart.”"))
        add(Forecasting("预示危险的警示、及时的观察、指责、无凭无据的主张、失去的地位、落空的希望、行事的意愿、承受的勇气。" translatedTo "It promises warning of dangers, observations right on time, reproaches, pretensions without merit, lost positions, unsuccessful hopes, desire to do, courage to suffer."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_59 = ComponentListSupplier {
        // 启示者
        setTranslationPrefix("Tarot-Revelation")
        add(Description("启示作为揭露隐秘之举，象征人类使不可理解之物变得可理解所具之美德。" translatedTo "Revelation as an act of unveiling the hidden. Symbolizes the human virtue of making intelligible that which was unintelligible."))
        add(Modulating("关联于水星、字母D及数字5，代表显化呈现的原则。" translatedTo "It is associated to the planet Mercury, the letter D, and the number 5. It represents the principle of manifestation."))
        add(Transcendental("“金属经火试炼，人值亦经赞誉或指责者之口试炼。”" translatedTo "“As the metals are tested in the fire, likewise the value of a man is tested in the mouth of who praises or censures him.”"))
        add(Forecasting("预示声明、许可、讯息、信使、矛盾的消息、旅行、逆境、多言、秘密的泄露。" translatedTo "It promises declarations, authorizations, messages, messengers, contradictory news, voyages, adversity, loquacity, violation of secrets."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_60 = ComponentListSupplier {
        // 进化者
        setTranslationPrefix("Tarot-Evolution")
        add(Description("进化作为转变的过程，象征人类意识逐步觉醒所具之美德。" translatedTo "Evolution as a process of transformation. Symbolizes the human virtue of the successive awakening of the consciousness."))
        add(Modulating("关联于金星、字母E及数字6，代表形态转变的原则。" translatedTo "It is associated to the planet Venus, the letter E, and the number 6. It represents the principle of metamorphosis."))
        add(Transcendental("“播种者与浇灌者，于种子而言无别。”" translatedTo "“The one who sows and the one who waters the sown are the same to the seed.”"))
        add(Forecasting("预示损失、运势的逆转、缩减、消逝的情感、逝去的生命、因观念固执导致的破败之险、不利的变迁、痛苦的事件。" translatedTo "It promises detriments, setback of fortune, diminishing, affections that die, beings which are buried, danger of ruin because of fixation of ideas, adverse changes, painful events."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_61 = ComponentListSupplier {
        // 独处者
        setTranslationPrefix("Tarot-Solitude")
        add(Description("独处作为振奋性专注之举，象征人类内在沉思所具之美德。" translatedTo "Solitude as an act of heartening concentration. Symbolizes the human virtue of interior contemplation."))
        add(Modulating("关联于海王星、字母F及数字7，代表隔绝独处的原则。" translatedTo "It is associated to the planet Neptune, the letter F, and the number 7. It represents the principle of isolation."))
        add(Transcendental("“清偿汝所欠；向征税者缴税，向敬拜者致敬，向尊贵者献礼。”" translatedTo "“Pay what thou owe; pay tax to the tax collector, pay worship to the worshiper, and pay honors to the honorable.”"))
        add(Forecasting("预示克制、谨慎、退隐、无能、警惕、节俭、良好品行、主动或被迫脱离社交生活。" translatedTo "It promises reserve, precaution, retirement, inability, vigilance, economy, good behavior, voluntary or unavoidable retirement from social life."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_62 = ComponentListSupplier {
        // 放逐者
        setTranslationPrefix("Tarot-Proscription")
        add(Description("放逐作为驱散之举，象征人类抵制并排斥有害之物所具之美德。" translatedTo "Proscription as an act of dispersion. Symbolizes the human virtue of counteracting and pushing away the harmful."))
        add(Modulating("关联于土星、字母G及数字8，代表互斥不相容的原则。" translatedTo "It is associated to the planet Saturn, the letter G, and the number 8. It represents the principle of incompatibility."))
        add(Transcendental("“守口者，自守其魂。”" translatedTo "“The one who guards his tongue guards his soul.”"))
        add(Forecasting("预示反对、决裂、分裂、对抗、隐居、离别、厌恶、争议、尴尬的事业。" translatedTo "It promises oppositions, ruptures, divisions, antagonisms, reclusions, absences, aversions, controversies, embarrassing enterprises."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_63 = ComponentListSupplier {
        // 共融者
        setTranslationPrefix("Tarot-Communion")
        add(Description("共融作为伙伴联结与认可之举，象征人类分享所具之美德。" translatedTo "Communion as an act of partnership and recognition. Symbolizes the human virtue of sharing."))
        add(Modulating("关联于火星、字母H及数字9，代表多样性中的统一原则。" translatedTo "It is associated to the planet Mars, the letter H, and the number 9. It represents the principle of unity within diversity."))
        add(Transcendental("“予欲播种而无种者以种子，予欲成功而无方者以忠告。”" translatedTo "“Give a seed to the one who wants to sow and does not have it, and advice to the one who wants to succeed and does not know how.”"))
        add(Forecasting("预示关爱、温柔、亲密、亲和、互通、共同抵御第三方、为共同理想的奋斗。" translatedTo "It promises affection, tenderness, intimacy, affinity, correspondences, mutual protection against third parties, fights for common ideals."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_64 = ComponentListSupplier {
        // 热忱者
        setTranslationPrefix("Tarot-Zeal")
        add(Description("热忱作为振奋昂扬的表达，象征人类热切渴望所具之美德。" translatedTo "Zeal as an heartening, exalted expression. Symbolizes the human virtue of a fervent longing."))
        add(Modulating("关联于太阳、字母A及数字1，代表内在火焰原则。" translatedTo "It is associated to the Sun, the letter A, and the number 1. It represents the inner fire principle."))
        add(Transcendental("“坚毅即智慧，动力乃推动其之渴望。”" translatedTo "“Fortitude is wisdom, and power the longing that moves it.”"))
        add(Forecasting("预示极端的激情、暴躁的脾气、愤怒、敌意、需奋力争取的事业、借由活力获得的成功、感情事务的顺遂。" translatedTo "It promises extreme passions, fetching temper; rage, animosity, enterprises that requires fights; conquests because of vigor, success in matters of love."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_65 = ComponentListSupplier {
        // 求知者
        setTranslationPrefix("Tarot-Learning")
        add(Description("求知作为教诲与警示之举，象征人类借由经验获取知识所具之美德。" translatedTo "Learning as a teaching and warning. Symbolizes the human virtue of knowledge through experience."))
        add(Modulating("关联于月亮、字母J及数字2，代表自我约束原则。" translatedTo "It is associated to the Moon, the letter J, and the number 2. It represents the principle of one’s own discipline."))
        add(Transcendental("“无论昼夜，为己增识者，亦增其忧。”" translatedTo "“Day or night the one who increases knowledge for his own good also increases his sorrow.”"))
        add(Forecasting("预示内在与外在的教诲、缺陷与美德、上升与下降、年长者与上位者的庇护；未来之喜乐，将胜过当下所谓的喜乐。" translatedTo "It promises internal and external teachings, defects and virtues, ascensions and descents, protection granted by a person major in age and in status; more joy for the coming future than the joy which the present is supposedly presenting."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_66 = ComponentListSupplier {
        // 困惑者
        setTranslationPrefix("Tarot-Perplexity")
        add(Description("困惑作为抉择时的犹豫之举，象征人类抉择所具之美德。" translatedTo "Perplexity as an act of indecision when choosing. Symbolizes the human virtue of selection."))
        add(Modulating("关联于木星、字母K及数字3，代表决议中的评估原则。" translatedTo "It is associated to the planet Jupiter, the letter K, and the number 3. It represents the principle of appraisal in resolutions."))
        add(Transcendental("“搬石者必为石伤，劈木者必为木危。”" translatedTo "“Whosoever removes stones shall be hurt therewith, and he that cleaves wood shall be endangered thereby.”"))
        add(Forecasting("预示摇摆不定、悬而未决之事、有条件的庇护、上升与下降、借由权贵女性支持获得的财富、部分事务安稳而部分事务动荡，然凡事皆有希望。" translatedTo "It promises vacillations, matters in suspense, conditional protection, ascensions and descents, wealth by the support of influential ladies, security in some things and insecurity in others, yet hope in everything."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_67 = ComponentListSupplier {
        // 友善者
        setTranslationPrefix("Tarot-Friendship")
        add(Description("友善作为情感中奉献的行动，象征人类尊崇所具之美德。" translatedTo "Friendship as an action of devotion in affection. Symbolizes the human virtue of veneration."))
        add(Modulating("关联于天王星、字母L及数字4，代表纯粹之爱原则。" translatedTo "It is associated to the planet Uranus, the letter L, and the number 4. It represents the principle of pure love."))
        add(Transcendental("“援手施与快，践诺脚步疾，携此二者，吾可处山丘亦可处幽谷。”" translatedTo "“A quick hand to give and a fast foot to fulfill, with you, both, I am on the hill or in the valley.”"))
        add(Forecasting("预示借由尽责获得的收益、心境的平和、意外的到访、克服障碍的胜利、友谊的支持、忠诚的情感。" translatedTo "It promises profit by means of accomplished duties, placidness of spirit, unexpected arrivals, triumph over obstacles, support from friendship, devoted affections."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_68 = ComponentListSupplier {
        // 思辨者
        setTranslationPrefix("Tarot-Speculation")
        add(Description("思辨作为价值评估之举，象征人类推理所具之美德。" translatedTo "Speculation as an act of appraisal for value. Symbolizes the human virtue of reasoning."))
        add(Modulating("关联于水星、字母LL及数字5，代表定向勤勉原则。" translatedTo "It is associated to the planet Mercury, the letters LL, and the number 5. It represents the principle of directed diligence."))
        add(Transcendental("“愿汝目视无惧，汝手行有爱。”" translatedTo "“May thine eyes see without fear and thine hands with love.”"))
        add(Forecasting("预示明智的劳作、有益的学识、物质财富的丰裕、慷慨大方、财务相关的利好消息、事业规划的准确测算。" translatedTo "It promises intelligent laboring; beneficial learning; abundance of material goods; generosity, liberality; favorable news on monetary matters; right calculation for undertakings."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_69 = ComponentListSupplier {
        // 机遇者
        setTranslationPrefix("Tarot-Chance")
        add(Description("机遇作为本能可知的法则，象征人类超验认知所具之美德。" translatedTo "Chance as a law known by the instinct. Symbolizes the human virtue of transcendental knowledge."))
        add(Modulating("关联于金星、字母M及数字6，代表以基础能力为人生指引的原则。" translatedTo "It is associated to the planet Venus, the letter M, and the number 6. It represents the principle of the primary faculties as guidance in the way of life."))
        add(Transcendental("“凡劳作皆有其果，凡果实皆有其劳。”" translatedTo "“There is a fruit in any labor, and there is a labor in any fruit.”"))
        add(Forecasting("预示回报、补偿、顺遂的变迁、职位晋升的机遇、不动产事务的成功、不经意间实现的愿望、偶然的吸引力。" translatedTo "It promises retributions, compensations, satisfactory changes; opportunity for ranking ascension, success in real estate properties, thoughtless accomplishment of wishes; fortuitous attractions."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_70 = ComponentListSupplier {
        // 合作者
        setTranslationPrefix("Tarot-Cooperation")
        add(Description("合作作为共同努力的协作之举，象征人类懂得互补所具之美德。" translatedTo "Cooperation as an act of collaboration in effort. Symbolizes the human virtue of knowing how to complement each other."))
        add(Modulating("关联于海王星、字母N及数字7，代表互惠原则。" translatedTo "It is associated to the planet Neptune, the letter N, and the number 7. It represents the principle of reciprocity."))
        add(Transcendental("“无人无学识，无学识无人。”" translatedTo "“There is no man without science, neither science without man.”"))
        add(Forecasting("预示真挚的情感、兑现的承诺、无根基希望的破灭、借由死亡或隐秘原因获得的兴旺、借由艺术或科学应用实现的进步。" translatedTo "It promises sincere affections, fulfilled promises, the vanishing of hopes without foundation; prosperity by means of death or mysterious causes, progress by application of arts or sciences."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_71 = ComponentListSupplier {
        // 贪婪者
        setTranslationPrefix("Tarot-Avarice")
        add(Description("贪婪作为算计性的自私之举，象征人类对权力渴望所具的扭曲美德。" translatedTo "Avarice as a calculative selfish act. Symbolizes a twisted human virtue of the longing for power."))
        add(Modulating("关联于土星、字母S及数字8，代表预防原则。" translatedTo "It is associated to the planet Saturn, the letter S, and the number 8. It represents the principle of prevention."))
        add(Transcendental("“贪婪本贪婪，以苦难解渴，虽丰却不悦。”" translatedTo "“Greedy is greed, quenched with miseries and unpleasantly abundant.”"))
        add(Forecasting("预示高利贷、引发懊悔的利己计划、大希望与小成果、盗窃之险、维系现有职位或已获地位的困难。" translatedTo "It promises usury, egotistical plans that bring remorse, great hopes and small results, danger of robbery, difficulties in preserving the place that is occupied or the acquired status."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_72 = ComponentListSupplier {
        // 净化者
        setTranslationPrefix("Tarot-Purification")
        add(Description("净化作为净化之举，象征人类自我塑造所具之美德。" translatedTo "Purification as a depurative act. Symbolizes the human virtue of self-edification."))
        add(Modulating("关联于火星、字母O及数字9，代表自我解放原则。" translatedTo "It is associated to the planet Mars, the letter O, and the number 9. It represents the principle of one’s own liberation."))
        add(Transcendental("“听父之教诲，守母之训诫。”" translatedTo "“Listen to the doctrine of thy father and forsake not the law of thy mother.”"))
        add(Forecasting("预示纯真、心境的烦忧、既照明又发热的光、借由辛勤努力获得的好运、顺遂的收获、满意的消息。" translatedTo "It promises ingenuousness, vexation of spirit, light that illuminates and heats, fortune by means of a laborious effort, fortunate acquisitions, satisfactory news."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_73 = ComponentListSupplier {
        // 爱欲者
        setTranslationPrefix("Tarot-Love and Desire")
        add(Description("爱与欲望作为自然的驱动力，象征人类拥有带来愉悦之物所具之美德。" translatedTo "Love and desire as natural stimuli. Symbolizes the human virtue of possessing the elements that provide delight."))
        add(Modulating("关联于太阳、字母P及数字1，代表自然科学原则。" translatedTo "It is associated to the Sun, the letter P, and the number 1. It represents the principle of natural science."))
        add(Transcendental("“吾织机之梭，织就吾衣之布。”" translatedTo "“Shuttle of my loom, weaving the cloth that shall become my coat.”"))
        add(Forecasting("预示幻象、炽热的激情、吸引力、野心、寄望于异性意愿之事、意外获得的财物，及因虚伪友人干预而失却之险。" translatedTo "It promises illusions, ardent passion, attractions, ambitions, hope in something that depends on the will of persons of the opposite sex, unexpected goods, and danger of losing them because the interference of false friends."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_74 = ComponentListSupplier {
        // 奉献者
        setTranslationPrefix("Tarot-Offering")
        add(Description("奉献作为祈福之举，象征人类向至高者致敬所具之美德。" translatedTo "The offering as a propitiatory act. Symbolizes the human virtue of rendering cult to the highest."))
        add(Modulating("关联于月亮、字母Q及数字2，代表崇敬之爱原则。" translatedTo "It is associated to the Moon, the letter Q, and the number 2. It represents the reverent love principle."))
        add(Transcendental("“汝予受苦者之物，当如精面置于汝佳盘。”" translatedTo "“Like an offering of flour on the best of thy plates must be thy giving to the afflicted heart.”"))
        add(Forecasting("预示虔诚、崇敬、热忱、爱恋的激情、和谐与纷争、启迪性的思想、非凡之举、多变的创举、多为放纵而非有益的成就。" translatedTo "It promises devotion, adoration, fervor, loving passion, harmony and discord, inspirational thoughts, acts of prodigy, variable initiatives, more dissipating than edifying achievements."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_75 = ComponentListSupplier {
        // 慷慨者
        setTranslationPrefix("Tarot-Generosity")
        add(Description("慷慨作为高尚之举，象征人类大方所具之美德。" translatedTo "Generosity as a high-minded act. Symbolizes the human virtue of liberality."))
        add(Modulating("关联于木星、字母R及数字3，代表无私原则。" translatedTo "It is associated to the planet Jupiter, the letter R, and the number 3. It represents the principle of abnegation."))
        add(Transcendental("“果实悦口，无论收受，皆甘甜。”" translatedTo "“Fruits pleasant to the palate, so sweet when receiving or giving them.”"))
        add(Forecasting("预示施舍、最终满意的定论、回报、无需他人离世即得的遗产、财富的获取、稳定的好运。" translatedTo "It promises alms, satisfactory definitions in the end, rewards, inheritance without death; acquisition of wealth; stable fortune."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_76 = ComponentListSupplier {
        // 施予者
        setTranslationPrefix("Tarot-The Dispenser")
        add(Description("施予者正施行恩惠，象征人类仁慈所具之美德。" translatedTo "The dispenser in the act of distributing grace. Symbolizes the human virtue of mercy."))
        add(Modulating("关联于天王星、字母S及数字4，代表人类眷顾原则。" translatedTo "It is associated to the planet Uranus, the letter S, and the number 4. It represents the principle of human providence."))
        add(Transcendental("“异乡人啊，请来吾处，食吾之饼，享吾族之羹。”" translatedTo "“Approach us, foreigner, and eat thereof from our bread and from the sauce of my people.”"))
        add(Forecasting("预示高尚的考量、重要地位、名声、慷慨、新生、借由明智规划的事业获得的好运、繁重的劳作与化之为用的天赋。" translatedTo "It promises noble considerations, importance, celebrity, generosity, birth, fortune by means of intelligently directed enterprises; abundant laboring and genius in order to make it useful."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_77 = ComponentListSupplier {
        // 迷失者
        setTranslationPrefix("Tarot-Disorientation")
        add(Description("迷失作为忍耐之举，象征人类认知与反思所具之美德。" translatedTo "Disorientation as an act of forbearance. Symbolizes the human virtue of acknowledgement and reflection."))
        add(Modulating("关联于水星、字母T及数字5，代表观念联想原则。" translatedTo "It is associated to the planet Mercury, the letter T, and the number 5. It represents the principle of association of ideas."))
        add(Transcendental("“勤勉之足需路径，辛劳之手需工具。”" translatedTo "“Paths are requested for a diligent foot, and tools for a laborious hand.”"))
        add(Forecasting("预示尴尬的处境、思想的混乱、抉择的困惑、意外的障碍、吉凶难料的可能——结局如何，取决于自身偏向何方的灵感。" translatedTo "It promises embarrassing situations, confusion of ideas, perplexity upon decisions, unexpected obstacles, probabilities of good and bad fortune, depending on one’s own inspiration towards one or the other."))

        add(GTOTarotArcanum)
    }

    val TarotArcanum_78 = ComponentListSupplier {
        // 复兴者
        setTranslationPrefix("Tarot-Renaissance")
        add(Description("复兴作为启蒙之举，象征人类实现接连卓越所具之美德。" translatedTo "Renaissance as an act of initiation. Symbolizes the human virtue of attaining successive pre-eminences."))
        add(Modulating("关联于金星、字母U及数字6，代表自然进化原则。" translatedTo "It is associated to the planet Venus, the letter U, and the number 6. It represents the principle of natural evolution."))
        add(Transcendental("“正午之阳，午夜之月，无论吉凶，皆当感恩。”" translatedTo "“Sun of midday, moon of midnight, thank goodness for good or bad fortune.”"))
        add(Forecasting("预示狂喜、纯粹的喜悦、满足、精神与物质的惬意、荣誉与好运。" translatedTo "It promises ecstasies, pure joy, satisfactions, moral and material contentment, honors and luck."))

        add(GTOTarotArcanum)
    }
}
