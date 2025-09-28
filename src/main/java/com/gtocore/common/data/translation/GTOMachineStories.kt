package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.misc.AutoInitialize

/**
 * 用于收纳机器小作文
 *
 * 相关用法请参看 [GTOMachineTooltips]
 */
object GTOMachineStories : AutoInitialize<GTOMachineStories>() {

    /*************************************************
     *           小型多方块机器                      *
     **************************************************/

    // 不宜过长, 4行以内最佳
    // 尤其注意不可把机器性能描述得过于强大
    // 尽量使用**灰色**
    // 类GCYM机器(如大热解/裂化/电弧)不要写

    // 热压成型机
    var thermoPressTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("thermo_press")
        story("为了将金属，陶瓷，塑料以及胶水粘到一起而开发的机器" translatedTo "A machine developed to bond metals, ceramics, plastics, and adhesives together")
    }

    // 电镀槽
    val electroplatingBathTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("electroplating_bath")
        story("掌握了铂系金属处理的技术后" translatedTo "After mastering platinum group metal processing technology")
        story("GTO集团的工程师们开始尝试着这个材料混那个，那个材料混这个" translatedTo "GTO Group engineers began experimenting mixing this material with that")
        story("最开始他们只是在合金冶炼炉里混合" translatedTo "Initially they just alloyed in smelters")
        story("但他们想要的§6§l镀§r铑钯锭怎么可能在那里诞生呢" translatedTo "But how could they get rhodium §6§lPLATED§r palladium ingots there")
    }

    // 雾化冷凝器
    val atomizingCondenserTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("atomizing_condenser")
        story("GTO集团的一名员工在合金冶炼炉前正等待着大锅的熔融合金出锅。" translatedTo "An employee of GTO Group was waiting in front of the alloy smelter for the molten alloy to be ready.")
        story("他是这台机器的操纵员，负责将金属粉末原料送入冶炼炉，再将熔融的合金倒入模具送入真空冷冻机。" translatedTo "He is the operator of this machine, responsible for feeding metal powder raw materials into the smelter, then pouring the molten alloy into molds and sending them to the vacuum freezer.")
        story("这是一项需要高度专注的工作，因为任何失误都可能导致合金质量不达标。" translatedTo "This is a task that requires high concentration, as any mistake could lead to substandard alloy quality.")
        story("一天，当他正忙着操作机器时，突然听到一声巨响。" translatedTo "One day, while he was busy operating the machine, he suddenly heard a loud bang.")
        story("他迅速转身，看到冶炼炉的盖子被炸飞了，熔融的合金喷涌而出。" translatedTo "He quickly turned around and saw the lid of the smelter blown off, with molten alloy spewing out.")
        story("这意味着这锅合金算是白炖了，而且可能遭到经理的一顿痛骂。" translatedTo "This meant that the alloy was wasted, and he might face a severe scolding from the manager.")
        story("他赶紧检查并收拾了现场，结果发现合金并没有完全浪费。" translatedTo "He quickly checked and cleaned up the scene, and found that the alloy was not completely wasted.")
        story("保护气带着熔融合金喷涌而出的时候，部分合金被冷却成了小颗粒。" translatedTo "When the protective gas sprayed out with the molten alloy, some of the alloy was cooled into small particles.")
        story("他小心翼翼地收集了这些颗粒，发现它们的质量和正常铸造出来的合金磨成粉以外没有什么区别。" translatedTo "He carefully collected these particles and found that their quality was no different from alloy ground into powder.")
        story("经理听说后，不但没有责骂，还记录下了这个意外的过程，并据此研发出了这台专门用于从熔融的流体生产金属粉末的机器" translatedTo "The manager, upon hearing this, not only did not scold him but also documented the unexpected process and developed this machine specifically for producing metal powders from molten fluids.")
        story("只不过生产出来的金属粉末还得重新送回高炉才能变回可加工的金属锭。" translatedTo "However, the produced metal powder still needs to be sent back to the blast furnace to be turned back into workable metal ingots.")
    }

    // 大型蒸汽电路组装机
    val LargeSteamCircuitAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("large_steam_circuit_assembler")
        story("后世工程师难以想象先驱者如何用简陋工具打造这台机器" translatedTo "Later engineers can hardly imagine how pioneers built this machine with crude tools")
        story("仅靠蒸汽动力和机械传动却实现了电路产出的倍增效果" translatedTo "Using only steam power and mechanical transmission, yet achieving multiplied circuit output")
        story("或许是魔法？总之没有别的合理解释了" translatedTo "Perhaps magic? There's no other reasonable explanation")
    }

    // 大型蒸汽太阳能锅炉
    val LargeSteamSolarBoilerTooltips = ComponentListSupplier {
        setTranslationPrefix("large_steam_solar_boiler")
        story("靠天吃饭一直是公司老祖宗的传统" translatedTo "Relying on nature has always been the company's ancestral tradition")
        story("然而古法工艺显然不能制作高精尖的太阳能板，也只能平摊着应付一下" translatedTo "But ancient methods can't make high-tech solar panels so we just lay them out flat to get by")
        story("反正不要钱，没有人会嫌弃它的" translatedTo "Anyway it's free so no one complains about it")
    }

    // 珍宝锻炉
    val RarityForgeTooltips = ComponentListSupplier {
        setTranslationPrefix("rarity_forge")
        story("GTO集团与神化集团就商业合作问题最近开展了重要谈话" translatedTo "GTO Group and Apotheosis Group recently held important business cooperation talks")
        story("双方充分交换了意见，达成了高度共识" translatedTo "Both sides fully exchanged views and reached high-level consensus")
        story("会议明确：神化集团有从GTO获取工业品的权利，但也要为GTO提供工具支持" translatedTo "The meeting clarified: Apotheosis has rights to obtain industrial products from GTO but must provide tool support in return")
        story("这个锻炉，正是双方合作成功后的第一个交易平台" translatedTo "This forge is the first trading platform resulting from their successful collaboration")
    }

    // 等静压成型
    val IsostaticPressMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("isostatic_press_machine")
        story("先进的材料学技术一直以来都是格雷科技公司的立身之本" translatedTo "Advanced materials technology has always been the foundation of GregTech Corp")
        story("被广泛应用的先进工业陶瓷一直是公司的拳头产品" translatedTo "Advanced industrial ceramics widely used in various fields are the company's flagship products")
        story("型号CML-202等静压成型机外表与百年前无异" translatedTo "Model CML-202 isostatic press looks no different from its predecessors a century ago")
        story("但其先进自动化成型技术和工艺早已不可同日而语" translatedTo "But its advanced automated forming technology and processes are incomparable")
    }

    // 烧结炉
    val SinteringFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("sintering_furnace")
        story("作为陶瓷生产中的核心设备" translatedTo "As the core equipment")
        story("GTO集团设计人员为这台烧结炉奋战了无数日夜" translatedTo "GTO Staffs fought countless days and nights for this sintering furnace")
        story(("型号HCS-41烧结炉有着完美的成品率" translatedTo "Model HCS-41 sintering furnace has perfect finished product rate"))
        story("生产出的优质陶瓷将成为工业帝国的坚固基石" translatedTo "The high-quality ceramics produced will become the solid foundation of industrial empire")
    }

    // 藻类农场
    val AlgaeFarmTooltips = ComponentListSupplier {
        setTranslationPrefix("algae_farm")
        story("作为植物进化最原始的种类之一，藻类通常不为人注意" translatedTo "As one of the most primitive plant species algae often goes unnoticed")
        story("然而正是这种原始特性，给予了人们从其内部提取高泛化性物质的机会" translatedTo "Yet this very primitiveness offers opportunities to extract highly versatile substances")
        story("GTO集团曾尝试电击疗法，可惜不能如愿" translatedTo "GTO Group tried electroshock therapy but unfortunately it didn't work as hoped")
        story("如今还只能用古法方案静待生长，不过基于已有的藻类去增殖似乎容易很多" translatedTo "Now we still rely on traditional methods patiently waiting for growth though propagating from existing algae seems much easier")
    }

    // 结晶器
    val CrystallizationChamberTooltips = ComponentListSupplier {
        setTranslationPrefix("crystallization_chamber")
        story("你说工业生产的粗硅可以直接用电力高炉烧制成单晶硅？" translatedTo "You think crude silicon from industrial production can be directly smelted into monocrystalline silicon in an electric furnace?")
        story("醒醒！我们这里可是GTO重工集团！" translatedTo "Wake up! This is GTO Heavy Industries Group!")
        story("和这些虫豸在一起，怎么能搞好芯片生产呢！" translatedTo "How can we produce proper chips when working with these vermin!")
        story("通过电子级硅的缓慢结晶，最终培育出能胜任高精度加工的硅晶圆" translatedTo "Through slow crystallization of electronic-grade silicon, we finally cultivate silicon wafers capable of high-precision processing")
    }

    // 光伏电站
    val PhotovoltaicPlantTooltips = { name: String ->
        ComponentListSupplier {
            setTranslationPrefix("photovoltaic_plant")
            story("太阳能光伏发电是公司的研究方向之一" translatedTo "Solar photovoltaic power generation§r is one of the company's research directions")
            story("型号${name}光伏电站技术起初被员工试用" translatedTo "Model $name photovoltaic plant technology§r was initially tested by employees")
            story("由于复杂的生产材料要求和较差发电能力常被冷落" translatedTo "Often neglected due to complex material requirements and poor power generation")
            story("技术人员偶然发现它能高效采集魔力" translatedTo "Technicians accidentally discovered it can §aefficiently collect mana")
            story("改进后的${name}以另一种身份被广泛使用" translatedTo "The improved $name is widely used in another capacity")
        }
    }

    // 虚空流体钻机
    val VoidFluidDrillTooltips = ComponentListSupplier {
        setTranslationPrefix("void_fluid_drill")
        story("虚空流体钻机§r§b是格雷科技在虚空领域的又一力作" translatedTo "Void Fluid Drill§r is another masterpiece of GregTech in the void field")
        story("它可以在虚空中钻取流体" translatedTo "It can drill fluids in the void")
        highlight("放入维度数据，设置电路后即可获取钻出对应世界流体矿床的流体" translatedTo "Insert dimension data and set the circuit to obtain fluids from corresponding world fluid deposits")
    }

    // 能量注入仪
    val EnergyInjectorTooltips = ComponentListSupplier {
        setTranslationPrefix("energy_injector")
        story("电池箱充电太慢？" translatedTo "Battery box charging too slow?")
        story("GTO集团隆重推出§6SCL-1000大型能量注入仪" translatedTo "GTO Corporation proudly introduces the §6SCL-1000 large energy injector")
        story("全新设计超级快充系统" translatedTo "Brand new super fast charging system")
        story("可为物品充电，还可消耗电力修复物品耐久" translatedTo "Can to charge items, Can consume electricity to repair item durability")
        story("\"充电1秒钟，工作一整年\"" translatedTo "\"Charge for 1 second, work for a whole year\"")
    }

    // 聚合反应器
    val PolymerizationReactorTooltips = ComponentListSupplier {
        setTranslationPrefix("polymerization_reactor")
        story("普通的化学反应环境并不能高效处理加聚反应和缩聚反应" translatedTo "Ordinary chemical reaction environments cannot efficiently handle addition and condensation polymerization")
        story("看看这个不锈钢制作的储罐吧，它有着链接原子键的力量" translatedTo "Behold this stainless steel tank - it possesses the power to forge atomic bonds")
        story("GTO所钟爱的PE、PTFE、PBI和PEEK就在这里产生" translatedTo "GTO's beloved batch of PE, PTFE, PBI and PEEK were born right here")
    }

    // 渔场
    val FishingFarmTooltips = ComponentListSupplier {
        setTranslationPrefix("fishing_farm")
        story("喜欢吃鱼？" translatedTo "Like eating fish?")
        story("AFFL-200智能大型渔场是舌尖上的格雷系列常客" translatedTo "AFFL-200 intelligent large fishing farm§r is a regular on GregTech cuisine series")
        story("强大的§e智能养殖系统§t带来强大产能" translatedTo "Powerful §eintelligent breeding system§r brings powerful productivity")
        story("能够满足整个分公司员工的水产食用需求" translatedTo "Can meet the entire branch office employees' §aaquatic food consumption needs")
    }

    // 培养缸
    val CulturingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("culturing_tank")
        story("格雷科技将目光放到鲸鱼座T星的异星藻类上" translatedTo "GregTech set its sights on alien algae from Cetus T star")
        story("AFMS-05培养缸为培养生物细胞材料量身打造" translatedTo "AFMS-05 culturing tank§r is tailor-made for cultivating biological cell materials")
    }

    // 大型培养缸
    val LargeCulturingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("large_culturing_tank")
        story("针对越发庞大的生物材料产能需求" translatedTo "Addressing the increasingly massive biological material production demand§r")
        story("ABFL-411大型培养缸被开发出来" translatedTo "ABFL-411 large culturing tank§r was developed")
        story("拥有更大的培养罐，更先进的培养管理系统" translatedTo "Features larger culture tanks and more advanced cultivation management system")
        story("在体积没有显著提升的情况下极大提高生产效率" translatedTo "Without significant volume increase, greatly improves production efficiency")
    }

    // 化工厂
    val ChemicalFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("chemical_factory")
        story("在化学的世界里，每一个分子都在诉说着自己的故事" translatedTo "In the world of chemistry, every molecule tells its own story")
        story("董事长拿着电子显微镜，尝试着解开元素的秘密" translatedTo "The chairman holds an electron microscope, attempting to unravel the secrets of elements")
        story("其实手下知道，他只是在摆拍而已" translatedTo "But his subordinates know he's just posing for the camera")
    }

    // 衰变加速器
    val DecayAcceleratorTooltips = ComponentListSupplier {
        setTranslationPrefix("decay_accelerator")
        story("铀-238的半衰期整整达到45亿年，显然实验室的科学家没有那么多时间" translatedTo "Uranium-238 have half-lives of up to 4.5 billion years, clearly lab scientists don't have that much time")
        story("这台衰变加速器，利用超导线圈环绕，企图利用电力加速自然演化" translatedTo "This decay accelerator uses superconducting coils in an attempt to accelerate natural evolution with electricity")
        story("实验表明，这确实让放射性物质稍微加快了一点退休速度" translatedTo "Experiments show this does gently encourage radioactive materials to retire faster")
        story("不过效果有限，但总比干等着强" translatedTo "The effect is modest, but still better than just waiting")
    }

    // 回收机
    val RecyclingMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("recycling_machine")
        story("看起来这只是一个很简单的钢制机器，连刚入职的新员工都能轻松搭建" translatedTo "It looks like just a simple steel machine that even newly hired employees can easily build")
        story("其实这台机器是GTO集团环保部门的杀手锏" translatedTo "Actually, this machine is the trump card of GTO Group's environmental department")
        story("即使是最顽固的废料，经过彻底的涅槃，都会在这里找到第二春" translatedTo "Even the most stubborn waste, after thorough nirvana, will find a second life here")
        story("然而，环保部门对它动了多少手脚就不得而知了" translatedTo "However, how much the environmental department has tampered with it remains unknown")
    }

    // 质量发生器
    val MassGeneratorTooltips = ComponentListSupplier {
        setTranslationPrefix("mass_generator")
        story("GTO寰宇部门最近盛行物理学教育，很多员工把爱因斯坦作为自己的偶像" translatedTo "Physics education is trending in GTO Universe Department many employees idolize Einstein")
        story("这台机器作为课程的优秀作业，恰好代表了质能转换的入门级实践" translatedTo "This machine as an excellent course assignment represents beginner's practice in mass-energy conversion")
        story("尽管耗电量＞产出量，但科学价值无可替代" translatedTo "Although power consumption > output its scientific value is irreplaceable")
        story("不过教授还不至于要借助这个作业来发顶会论文" translatedTo "But the professor wouldn't go so far as to use this assignment for top conference papers")
    }

    // 超临界合成机
    val SpsCraftingTooltips = ComponentListSupplier {
        setTranslationPrefix("sps_crafting")
        story("GTO寰宇集团从事量子力学的部门只有寥寥数人，却贡献了非常重要的科学成果" translatedTo "GTO Universe's quantum mechanics department has only a few members yet contributes crucial scientific achievements")
        story("这次的G3.1-15试验品只是一个火柴盒机器，甚至被建筑部门作为笑话" translatedTo "The G3.1-15 prototype is just a matchbox-sized machine even ridiculed by the construction department")
        story("然而外人不明白的是，这里微妙平衡的艺术已经精确到了普朗克尺度" translatedTo "But outsiders don't understand the delicate balance here is precise to Planck scale")
        story("透过玻璃，机器内有些许闪烁，似乎看到了来自薛定谔的猫的目光" translatedTo "Through the glass faint flickers inside seem to reveal the gaze of Schrödinger's cat")
    }

    // 精密组装机
    val PrecisionAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("precision_assembler")
        story("DSW-17型精密组装机的运作原理是什么？" translatedTo "What is the operating principle of the DSW-17 Precision Assembler?")
        story("尽管设计程序代码已经开源到GitHub，但似乎并没有什么人可以复现" translatedTo "Although the design code has been open-sourced on GitHub, few seem able to replicate it")
        story("实际上真正的核心代码存储于隔离仓，只有持有工程师徽章的人员可访问" translatedTo "The actual core code is stored in isolation vaults, accessible only to those with an engineer badge")
    }

    // 熔岩炉
    val LavaFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("lava_furnace")
        story("石头进去，岩浆出来，简单粗暴的地热转换艺术" translatedTo "Rocks in, lava out - the brutally simple art of geothermal conversion")
    }

    // 稀土离心机
    val RareEarthCentrifugalTooltips = ComponentListSupplier {
        setTranslationPrefix("rare_earth_centrifugal")
        story("基础稀土分离专家，用离心力解开大地深处的秘密" translatedTo "Basic rare earth separation expert, unlocking earth's deepest secrets with centrifugal force")
        story("虽然效率有限，但为稀土工业奠定了重要基础" translatedTo "Though efficiency is limited, it lays important foundation for rare earth industry")
    }

    // 溶解罐
    val DissolvingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("dissolving_tank")
        story("GTO从来就不是粗放式发展的公司，也需要很多高精技术人才" translatedTo "GTO has never been an extensive development company it requires many high-precision technical talents")
        story("对于药剂师来说，普通的搅拌会破坏药品性质，只有精确配比的溶解操作才能保证稳定" translatedTo "For pharmacists ordinary stirring damages drug properties only precisely proportioned dissolution ensures stability")
        story("当然，这些药剂师们并不喜欢滴定！毕竟谁愿意整天数着滴管度日呢" translatedTo "Of course these pharmacists dislike titration! Who wants to count droppers all day anyway")
    }

    // 部件组装机
    val ComponentAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("component_assembler")
        story("GTO集团送给菜鸟员工的贴心礼物，让组装变得像搭积木一样简单" translatedTo "GTO Group's thoughtful gift for rookie employees, making assembly as easy as building blocks")
        story("支持IV及以下等级配方处理，有效降低初期部件加工成本" translatedTo "Supports up to IV tier recipes, effectively reducing early-stage component processing costs")
        story("董事长温馨提示：好好对待它，毕竟你的第一份工资可能还没它一个零件贵" translatedTo "Friendly reminder from the chairman: Treat it well, your first salary might not even cover one of its parts")
    }

    // 等离子冷凝器
    val PlasmaCondenserTooltips = ComponentListSupplier {
        setTranslationPrefix("plasma_condenser")
        story("GTO工程师在等离子体控制方面取得重大突破，解决了长期存在的稳定性难题" translatedTo "GTO engineers made a major breakthrough in plasma control solving long-standing stability issues")
        story("通过生成极端低温场，成功将高温等离子体冷凝为可操控液态" translatedTo "Generating extreme cryogenic fields successfully condensing high-temperature plasma into manageable liquid")
        story("这项技术不仅确保了操作安全，还实现了等离子体的高效回收利用" translatedTo "This technology not only ensures operational safety but also enables efficient recovery of plasma")
    }

    // 电路装配线
    val CircuitAssemblyLineTooltips = ComponentListSupplier {
        setTranslationPrefix("circuit_assembly_line")
        story("GTO寰宇格雷科技顶级工程师设计的精密电路制造系统" translatedTo "Precision circuit manufacturing system designed by GTO Universe GregTech's top engineers")
        story("透明的夹层玻璃展示内部机器人有条不紊的精密装配过程，多人协作时效率还能翻倍" translatedTo "Transparent laminated glass reveals orderly precision assembly by internal robots, efficiency doubles with multi-robot collaboration")
        story("显然并没有人关心细节是怎么样的，但能确定的是它确实能造出一些过往传统方法弄不出的小玩意" translatedTo "Apparently no one cares about the details, but it certainly produces some gadgets that traditional methods couldn't make")
    }

    // 裂变反应堆
    val FissionReactorTooltips = ComponentListSupplier {
        setTranslationPrefix("fission_reactor")
        story("刚来的员工是从通用机械集团转来的，带来了这么个家伙" translatedTo "A new employee transferred from Mekanism brought this thing along")
        story("虽然不知道是否构成了知识产权侵权，但总之用着确实效果不错" translatedTo "Not sure if it's intellectual property infringement, but after all it works well")
        story("据他所说，GT这边先进的化工技术可以产出钠钾冷却液，能更充分发挥这台机器的性能" translatedTo "He claims GT's advanced chemical tech can produce NaK coolant to fully unleash this machine's potential")
        story("没有人知道真假，毕竟从一个跳槽的人嘴里又能得到多少实话呢" translatedTo "No one knows the truth, after all how much honesty can you expect from a job-hopper")
    }

    // 工业屠宰场
    val SlaughterhouseTooltips = ComponentListSupplier {
        setTranslationPrefix("slaughterhouse")
        story("作为自动化行业的顶尖集团，GTO的企业宗旨之一就是能自动绝不手动" translatedTo "As a top automation corporation, GTO's motto is automate everything possible")
        story("可惜Mojang的代码并不允许这么做，厂内时常陷入凋灵骷髅头急缺" translatedTo "Unfortunately Mojang's code doesn't allow this, often causing Wither Skeleton skull shortages")
        story("更严重的是因凋零笼故障，去年厂房遭到了凋零的大规模破坏" translatedTo "Worse, last year a faulty Wither cage caused massive facility damage from escaped Withers")
        story("为此，这款自动屠宰场作为黑客出现，它成功绕过了游戏的玩家检查" translatedTo "Thus this automated slaughterhouse emerged as a hack successfully bypassing game player checks")
        story("某些时候它甚至能盗用玩家身份来进行更加专业的工作" translatedTo "Sometimes it can even steal the player's identity to perform more professional work")
    }

    // 大型方块转换室
    val LargeBlockConversionRoomTooltips = ComponentListSupplier {
        setTranslationPrefix("large_block_conversion_room")
        story("当铝青铜外壳首次闭合时，连发明者自己都屏住了呼吸" translatedTo "When the aluminum bronze housing first closed, even the inventor held his breath")
        story("看着普通物品在机器内部蜕变成各种稀有材料，同事们惊叹不已" translatedTo "Watching common item transform into rare materials inside, colleagues were amazed")
        story("值得注意的是，机器有着独特的空间记忆算法，每个方块只被转换一次" translatedTo "Unique spatial memory algorithm ensures each block is converted only once.")
    }

    // 烈焰高炉
    val BlazeBlastFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("blaze_blast_furnace")
        story("从匠魂集团新转来的员工似乎特别喜欢烈焰人，在公司的电力高炉阵列里豢养了一个烈焰人刷怪笼" translatedTo "A new employee transferred from Tinker's Construct seems particularly fond of blazes, keeping a blaze spawner in the company's electric furnace array")
        story("过了一天后，所有的高炉外表沾染了大量的滚烫烈焰，奇怪的是所有机器的速度突然之间翻了一倍" translatedTo "After a day, all furnaces were coated with scalding blaze residue, and strangely all machines suddenly doubled their speed")
        story("这些沾染烈焰的高炉还获得了64物品批量加工的能力，效率呈几何级增长" translatedTo "These blaze-coated furnaces also gained 64-item batch processing capability, with efficiency growing exponentially")
        story("后来刷怪笼被勒令拆除，但这项烈焰加速技术却在公司广泛推广使用" translatedTo "The spawner was later ordered removed, but this blaze acceleration technology was widely adopted across the company")
    }

    // 寒冰冷冻机
    val ColdIceFreezerTooltips = ComponentListSupplier {
        setTranslationPrefix("cold_ice_freezer")
        story("一次液态冰泄漏事故意外催生了这台极低温设备的诞生" translatedTo "A liquid ice leak accident unexpectedly led to the birth of this cryogenic device")
        story("液态冰似乎和低温控制室的铝制框架产生了奇特的反应，经研究这里似乎形成了稳定的超导环境" translatedTo "Liquid ice seems to have reacted strangely with the aluminum frame of the cryo control room, apparently creating a stable superconducting environment")
        story("钨钢管道的强导热性质以涡流方式循环，旋转一轮能一次性冷冻64个样品" translatedTo "Tungsten steel pipes' high thermal conductivity creates vortex circulation, freezing 64 samples in one rotation cycle")
        story("造成这次事故的员工下落不明，不过这台机器却被董事长视为珍宝" translatedTo "The employee responsible for the accident has disappeared, but the chairman treasures this machine like a precious jewel")
    }

    // 通用工厂
    val ProcessingPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("processing_plant")
        story("董事长视察时目睹员工们手忙脚乱地切换机器，萌生了集成化想法" translatedTo "Chairman witnessed employees frantically switching machines during inspection sparking integration idea")
        story("将三十多种小型加工设备巧妙整合，形成流水线式自动化生产体系" translatedTo "Ingeniously integrated 30+ small processing devices into streamlined automated production system")
        story("无需手动切换即可完成多种加工，生产效率提升了数倍" translatedTo "Completes multiple processes without manual switching multiplying production efficiency")
    }

    // 3D打印机
    val ThreeDimensionalPrinterTooltips = ComponentListSupplier {
        setTranslationPrefix("three_dimensional_printer")
        story("员工A317收藏了一份泰勒光盘，却被孩子拿刀刻成马达模样扔进了打印机里" translatedTo "Employee A317 collected a Taylor Swift disc but his child carved it into a motor shape and threw it into the printer")
        story("意外的是竟然真的打印出成品，这孩子或许真是个天才" translatedTo "Surprisingly it actually printed a finished product perhaps this child is truly gifted")
        story("现在这台打印机正批量生产着那个意外发现的高效马达设计" translatedTo "Now this printer mass produces that accidentally discovered efficient motor design")
        story("公司为表彰，给A317送来了全套专辑，但这救不了孩子被毒打的命运" translatedTo "The company gifted full album sets to commend this but couldn't save the child from a spanking")
    }

    // 数字型采矿机
    val DigitalMinerTooltips = ComponentListSupplier {
        setTranslationPrefix("digital_miner")
        story("懒才是科技发展的动力" translatedTo "Laziness is the true driver of technological progress")
        story("DD-1672号采矿机，能耗低，效率稳定，无需维护" translatedTo "DD-1672 miner: low energy consumption, stable efficiency, maintenance-free")
        story("你的不二之选！" translatedTo "Your ultimate choice!")
    }

    // 拆解机
    val DisassemblyTooltips = ComponentListSupplier {
        setTranslationPrefix("disassembly")
        story("还在为退休的部件装配线外壳而惋惜吗？GTO集团早已解锁了逆熵技术！" translatedTo "Still regretting retired component assembly line casings? GTO Group has unlocked reverse entropy technology!")
        story(("如假包换退全款，不收一分手续费！" translatedTo "Full refund guaranteed if fake, no service fees charged!").italic())
    }

    // 艾萨研磨机
    val IsaMillTooltips = ComponentListSupplier {
        setTranslationPrefix("isa_mill")
        story("这款艾萨1672N号研磨机，虽然有着绿皮外表，但其实完全不环保" translatedTo "This Isa 1672N grinder, despite its green appearance, is actually not eco-friendly at all")
        story("排风扇总有一些奇怪的气味，也没有几个员工想要触碰那乱成一团的产物" translatedTo "The exhaust fan always emits strange odors, and few employees want to touch the messy output")
        story("通过滚珠暴力湿法碾碎一切矿石，但不得不承认它效率高的出奇" translatedTo "It violently crushes all ores through wet ball milling, but its efficiency is surprisingly high")
        story("实验室的科学家们只能捏着鼻子承认：有时候暴力确实能解决问题" translatedTo "Lab scientists have to admit while holding their noses: sometimes brute force does solve problems")
    }

    // 工业浮选机
    val IndustrialFlotationCellTooltips = ComponentListSupplier {
        setTranslationPrefix("industrial_flotation_cell")
        story("作为一般提纯工艺，这台艾萨U-276选矿机已经发展相当成熟" translatedTo "As a general purification process this Isa U-276 ore separator has become quite mature")
        story("由于采用松油浮选，车间里总是弥漫着一股风油精的味道" translatedTo "Using pine oil flotation the workshop always smells like essential balm")
        story("别的不论，在这里工作确实很少会被蚊子叮咬" translatedTo "Regardless working here does mean fewer mosquito bites")
        add(("注：安全生产，警钟长鸣！" translatedTo "Tips: Safe production, alarm ringing!").red())
    }

    // 中子活化器
    val NeutronActivatorTooltips = ComponentListSupplier {
        setTranslationPrefix("neutron_activator")
        story("有员工观察到特定动能的中子能可以激活原子核" translatedTo "Employee observed neutrons at specific kinetic energy can activate atomic nuclei")
        story("通过精密控制中子流速度，可以使普通材料产生罕见的核反应转化" translatedTo "Precise neutron velocity control enables rare nuclear reactions in common materials")
        story("董事长听说后，亲自批准研发" translatedTo "Chairman personally approved after hearing")
        story("如今它已经能以接近光速处理核配方，成为材料活化领域的关键技术" translatedTo "Now processes nuclear recipes at near-light speed becoming key technology in material activation")
    }

    // 热交换机
    val HeatExchangerTooltips = ComponentListSupplier {
        setTranslationPrefix("heat_exchanger")
        story("当时工程师们正为热能浪费发愁，一位老员工想到了一个绝妙的主意" translatedTo "Engineers were struggling with heat waste when a veteran employee came up with a brilliant idea")
        story("用钨钢管道让热流体和冷却液亲密接触，此时热量传递变得格外高效" translatedTo "Tungsten steel pipes let hot fluid and coolant get intimate making heat transfer incredibly efficient")
        story("经过反复试验，实验终于成功，而且这台机器还能额外产出珍贵的高级蒸汽" translatedTo "After countless trials, it worked and this machine even produces precious advanced steam as bonus")
        story("现在热能再也不浪费了，既解决了老问题又带来了新收获" translatedTo "Now heat never goes to waste solving an old problem while bringing new gains")
    }

    // 大型虚空采矿厂
    val LargeVoidMinerTooltips = ComponentListSupplier {
        setTranslationPrefix("large_void_miner")
        story("看着仓库中的矿物逐渐捉襟见肘，董事长显得有些担忧" translatedTo "Watching the minerals in the warehouse gradually running short, the chairman looked somewhat worried")
        story("但不用担心，新晋的资源部门员工B2312用晶体电路组建了新的矿物采集厂" translatedTo "But don't worry, new resource department employee B2312 built a new mineral collection plant with crystal circuits")
        story("和以往不同，这台机器可以在一无所有的虚空中采集矿物" translatedTo "Unlike before, this machine can collect minerals from the void where there is nothing")
        story("谁也不知道这样的虚拟矿脉是如何产生的——除了B2312自己" translatedTo "No one knows how such virtual ore veins are generated—except B2312 himself")
        story("唯一明显的变化是，B2312员工的工牌上多了一颗金色的星星" translatedTo "The only obvious change is that employee B2312's badge now has an extra golden star")
    }

    // 无尽流体钻机
    val InfinityFluidDrillingRigTooltips = ComponentListSupplier {
        setTranslationPrefix("infinity_fluid_drilling_rig")
        story("号外号外！勘探队在深地层发现了神奇现象：流体矿脉居然能自我再生！" translatedTo "Extra! Extra! Survey team discovers marvel in deep strata: fluid veins that actually regenerate themselves!")
        story("工程师们连夜设计出这套钻机系统，能像吸管一样持续抽取流体" translatedTo "Engineers designed this drilling system overnight that siphons fluids like a never-ending straw")
        story("董事长一听报告差点没跳起来，谁能想到无限能源其实就在自己家楼底下呢" translatedTo "The chairman almost jumped hearing the report: who knew infinite energy was right under our feet!")
        story("这下好了，从这天开始公司水管的声音就没断过——这可比Minecraft的无限水强多了" translatedTo "Now the company's pipes have been flowing non-stop—way better than Minecraft's infinite water!")
    }

    // 拉丝塔
    val DrawingTowerTooltips = ComponentListSupplier {
        setTranslationPrefix("drawing_tower")
        story("光纤强大的传输能力无人可以比拟，但制作光反射性材料却成为一道难关" translatedTo "Fiber optics' transmission power is unmatched, but creating light-reflective materials posed a major challenge")
        story("需要用线轴尽力拉丝，更高的功率可以带来更高的光纤产率" translatedTo "Requires intense wire drawing with spools, higher power yields greater fiber production rates")
        story("董事长曾批评GTO员工花了一周时间就建造了个这么高的玩意" translatedTo "The chairman once criticized GTO employees for spending a week building such a tall thing")
        story("尽管员工不敢反驳，但事实证明，他们的努力是值得的" translatedTo "Though employees dared not rebut, their efforts proved worthwhile in the end")
    }

    // 煮解池
    val DigestionTankTooltips = ComponentListSupplier {
        setTranslationPrefix("digestion_tank")
        story("GTO集团炊事班的员工C-936，不小心把矿产部门的废料当成盐扔到了烹饪锅里" translatedTo "GTO canteen employee C-936 accidentally mistook mining department waste for salt and threw it into the cooking pot")
        story("正所谓文火慢炖，细嚼慢咽好消化——溶解析出竟起到了提纯作用" translatedTo "As they say: slow simmering and thorough chewing aid digestion - dissolution and precipitation provided purification")
        story("涉事员工C-936喜提罚款500技术员币，批评教育1天" translatedTo "Employee C-936 was 'rewarded' with a 500-technician-coin fine and one day of criticism education")
        story("但既然事已至此，这锅也只能送给矿产部了" translatedTo "But since it happened, this 'cooking pot' had to be gifted to the mining department")
    }

    // 进阶装配线
    val AdvancedAssemblyLineTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_assembly_line")
        story("员工已经厌倦了又慢又麻烦的装配线，发起了改良产能的提案" translatedTo "Employees tired of the slow and troublesome assembly line proposed production capacity improvements")
        story("整个GTO公司都动起来了，各路专家齐聚一堂" translatedTo "The whole GTO company mobilized experts all gathered together")
        story("机械组搭框架，电子组布线路，物流组搞配送，各显神通" translatedTo "Mechanical team built frame, electronic team laid circuits, logistics team optimized delivery each showing their skills")
        story("功夫不负有心人，虽然还有很多局限性，新品相较于过往效率也提高了数倍" translatedTo "Efforts paid off. Despite limitations, the new model's efficiency increased several times over previous versions")
    }

    // 矿石萃取模块
    val OreExtractionModuleTooltips = ComponentListSupplier {
        setTranslationPrefix("ore_extraction_module")
        story("纳米蜂群能够精准分离矿石中的每一种元素，实现分子级别萃取" translatedTo "Nano-swarms precisely separate every element in ores, achieving molecular-level extraction")
        story("董事长盛赞道：这些蜜蜂就像无数微型矿工在同步工作，优雅而高效" translatedTo "Chairman praised: Like countless micro-miners working in sync, elegant and efficient")
        story("一步完成矿石处理，大幅提升矿物回收率和纯度" translatedTo "Completes ore processing in one step, significantly improving mineral recovery rate and purity")
    }

    // 聚合物扭曲模块
    val PolymerTwistingModuleTooltips = ComponentListSupplier {
        setTranslationPrefix("polymer_twisting_module")
        story("GTO集团纳米技术的巅峰之作，复杂聚合物一步加工解决方案" translatedTo "GTO Group's pinnacle of nanotechnology, one-step processing solution for complex polymers")
        story("纳米蜂群精准重排分子链结构，创造出前所未有的新型材料" translatedTo "Nano-swarms precisely rearrange molecular chains to create unprecedented new materials")
        story("首次实现在分子层面上自由编辑物质的梦想" translatedTo "Materials scientists praise: First realization of the dream to freely edit matter at molecular level")
        story("标志着材料工程进入全新时代，开启了无限可能的应用前景" translatedTo "Marks a new era in materials engineering, opening unlimited application possibilities")
    }

    // 生物工程模块
    val BioengineeringModuleTooltips = ComponentListSupplier {
        setTranslationPrefix("bioengineering_module")
        story("生物工程是三个模块中最神秘的存在" translatedTo "The bioengineering module is the most mysterious of the three modules")
        story("无菌室的灯光亮起时，纳米蜂群开始在分子层面编织生命的经纬" translatedTo "When the sterile chamber lights up, nano-swarms begin weaving the fabric of life at molecular level")
        story("生物学家们屏息凝神，见证着有机化合物在精准操控下悄然成型" translatedTo "Biologists hold their breath, witnessing organic compounds taking shape under precise manipulation")
        story("首席工程师激动地宣告：我们正站在解开生命奥秘的门槛之上" translatedTo "The chief engineer excitedly declares we are standing on the threshold of unraveling life's mysteries")
    }

    // 太空电梯
    val SpaceElevatorTooltips = ComponentListSupplier {
        setTranslationPrefix("space_elevator")
        story("GTO的工程师们仰望星空几十年，终于把通往宇宙的天梯变成了现实" translatedTo "GTO engineers looked at the stars for decades finally turning the celestial ladder into reality")
        story("这座巨塔刺破云层，用碳纳米管缆绳把地球和太空紧紧连接在一起" translatedTo "This giant tower pierces the clouds connecting Earth and space with carbon nanotube cables")
        story("落成典礼上，董事长激动地宣布道：从今天开始，我们彻底摆脱了重力束缚" translatedTo "The chairman's voice trembled at the inauguration: Today we break free from gravity's shackles")
        story("现在采矿无人机可以沿着电梯直上云霄，源源不断地运回宇宙深处的矿产了" translatedTo "Now mining drones ascend along the elevator bringing back minerals from the depths of space")
        story("人类终于踏出了征服星辰的第一步，而GTO就是这历史时刻的见证者" translatedTo "Humankind finally takes the first step to conquer the stars with GTO witnessing this historic moment")
    }

    // 净化处理厂
    val WaterPurificationPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("water_purification_plant")

        story("水中的污染物和离子颗粒会在硅片和芯片切割和雕刻的精密过程中造成显著的缺陷" translatedTo "Pollutants and ionic particles in water can cause significant defects during the precise processes of wafer and chip cutting and engraving")
        story("通过一系列越来越精确和复杂的净化过程系统地净化水是至关重要的，而这个多方块结构是操作的核心" translatedTo "It is crucial to systematically purify the water through a series of increasingly precise and complex processes, and this multi-block structure is the core of the operation")
    }

    // 澄清器净化装置
    val ClarifierPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("clarifier_purification_unit")

        story("获得净化水的第一步是通过使用大型物理过滤器过滤掉宏观污染物" translatedTo "The first step to obtaining purified water is to filter out macro contaminants using large physical filters.")
        story("通过快速沙滤进行初级水处理，移除水中85%的细菌与几乎所有浊度（泥土、粉砂、微细有机物、无机物、浮游生物等悬浮物和胶体物）" translatedTo "As through rapid sand filtration for primary water treatment, 85% of bacteria and almost all turbidity (including silt, fine sand, micro-organic matter, inorganic matter, and floating organisms etc. suspended particles and colloids) are removed.")
    }

    // 臭氧净化装置
    val OzonationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("ozonation_purification_unit")

        story("净化水的第二步是偶氮化，这涉及到注入大量的小分子" translatedTo "The second step of water purification is azonation, which involves injecting a large amount of small molecules")
        story("高反应性臭氧气体的气泡进入水中。这可以去除微量元素污染物，如" translatedTo "The bubbles of highly reactive ozone gas enter the water. This can remove trace element contaminants such as")
        story("硫、铁和锰，产生不溶的氧化物化合物，然后被过滤掉" translatedTo "Sulfur, Iron, and Manganese, producing insoluble oxidized compounds, which are then filtered out.")
    }

    // 絮凝净化装置
    val FlocculationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("flocculation_purification_unit")

        story("净化水的第三步是使用澄清剂（在本例中为聚合氯化铝）去除微观污染物，" translatedTo "The third step of water purification uses a flocculent (in this case, Polymeric Aluminum Chloride) to remove microscopic contaminants,")
        story("如灰尘、微塑料和其他污染物，通过絮凝使溶液中的分散悬浮颗粒聚集成更大的团块，以便进一步过滤" translatedTo "such as dust, microplastics, and other pollutants, through flocculation that causes solution dispersed suspended particles to conglomerate into larger clumps for further filtering.")
    }

    // pH中和净化装置
    val PHNeutralizationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("ph_neutralization_purification_unit")

        story("水净化的第四步是中和溶液并将其pH值精确调节到7，使溶液呈惰性，无水以外的氢离子活动" translatedTo "The fourth step of water purification is to neutralize the solution and adjust its pH value precisely to 7, making the solution inert with no hydrogen ion activity other than that of water.")
        story("土壤和地质中的酸和碱会导致水的天然碱度变化，可能会与敏感材料发生腐蚀反应" translatedTo "Acids and bases in soil and geology can cause natural alkalinity variations in water, possibly leading to corrosion reactions with sensitive materials.")
        story("因此，需要使用相应的中和剂来平衡水的pH值" translatedTo "Thus, appropriate neutralizers need to be used to balance the pH value of the water.")
    }

    // 极端温度波动净化装置
    val ExtremeTemperatureFluctuationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("extreme_temperature_fluctuation_purification_unit")

        story("水净化的第五步是蒸发复杂有机聚合物和可能对简单酸、澄清剂和过滤器有抵抗力的极端微生物" translatedTo "The fifth step of water purification evaporates complex organic polymers and extreme microorganisms that may resist simple acids, clarifiers, and filters.")
        story("使用超高压腔室结合极端温度波动，可以使水保持超临界状态，同时蒸发任何残留的污染物，准备进行过滤" translatedTo "Using ultra-high pressure chambers combined with extreme temperature fluctuations allows the water to remain in a supercritical state while evaporating any remaining contaminants in preparation for filtering.")
    }

    // 高能激光净化装置
    val HighEnergyLaserPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("high_energy_laser_purification_unit")

        story("水净化的第六步是识别水中任何残留的负离子，这些离子可能会在未来的晶圆制造中引起电气故障" translatedTo "The sixth step of water purification is identifying any residual anions in the water, which may cause electrical failures in future wafer manufacturing.")
        story("用不同波长的光子束轰击水，将能量传递给外层电子，使它们从原子中脱离并通过水箱壁，确保水完全电极化" translatedTo "Bombarding the water with photons of different wavelengths transfers energy to the outer-layer electrons, causing them to detach from the atoms and through the water tank walls, ensuring the water is fully polarized.")
    }

    // 残余污染物脱气净化装置
    val ResidualDecontaminantDegasserPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("residual_decontaminant_degasser_purification_unit")

        story("水净化的倒数第二步，第七步，是一系列不规则的复杂过程，" translatedTo "The penultimate step of water purification, step seven, consists of a series of irregular complex processes,")
        story("旨在去除前几个步骤的除污剂可能残留的任何残留物，" translatedTo "aimed at removing any residues of decontaminants that may linger from the previous steps,")
        story("根据脱气器检测到的水中物质，它会请求各种材料以完成上述过程" translatedTo "based on the materials detected in the water by the degasser, it will request various materials to complete the above processes.")
    }

    // 绝对重子完美净化装置
    val AbsoluteBaryonicPerfectionPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("absolute_baryonic_perfection_purification_unit")

        story("净化水的最后阶段超越了亚原子粒子，识别出重子内最小的可能缺陷" translatedTo "The final stage of purification transcends subatomic particles, identifying the smallest possible defects within baryons.")
        story("通过正确识别需要的夸克释放催化剂，装置将激活催化剂，稳定偏离的粒子" translatedTo "By correctly identifying the required Quark Releasing Catalysts, the device will activate the catalysts and stabilize off-kilter particles.")
        story("这最终不仅会创造出稳定的重子物质，而且最重要的是，创造出绝对完美净化的水" translatedTo "This ultimately creates not just stable baryonic matter, but most importantly, absolutely purified water.")
    }

    /*************************************************
     *              巨构(通常跨并/激光)                *
     **************************************************/

    // 如果要用AI, 请至少务必给出如下prompt
    // 6行左右
    // 鼓励科幻元素, 也可以融合一些梗
    // 可以围绕GTO寰宇集团, 但不必强求
    // 切勿风格和字眼过于相似, 必须多样化
    // 文本颜色不可乱用, 一句话最好一个颜色
    // 最后一句加星标语用于描述整个机器作用
    // 你可以参照可用的色彩接口... (copy code)

    // 大气收集室
    val AtmosphereCollectorRoomTooltips = ComponentListSupplier {
        setTranslationPrefix("atmosphere_collector_room")
        add(("大气层的美味榨汁机，专门收集行星的呼吸" translatedTo "The atmosphere juicer, specializes in collecting planetary breath")) { aqua() }
        add(("从稀薄的高空到浓郁的对流层，没有它抽不动的气" translatedTo "From thin upper layers to dense troposphere, no gas is too tough to extract")) { gray() }
        highlight("超大容量设计，一瓶更比六瓶强" translatedTo "Extra-large capacity design, one bottle equals six regular collectors")
    }

    // 激光蚀刻工厂
    val EngravingLaserPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("laser_etching_factory")
        add(("这台光子雕刻圣殿，可以用光之刃重塑物质的结构" translatedTo "Photon engraving sanctuary, reshaping matter with blades of light")) { gold() }
        add(("当万亿瓦特的激光聚焦于微米之间，连时空都会为之弯曲" translatedTo "When terawatt lasers focus at micron scale, even spacetime bends")) { gray() }
        add(("精密到可以给病毒刻二维码，虽然没人知道为什么需要这么做" translatedTo "Precise enough to engrave QR codes on viruses, though no one knows why")) { green() }
        highlight("用光速打造未来的科技" translatedTo "Crafting future tech at the speed of light")
    }

    // 部件装配线
    val ComponentAssemblyLineTooltips = ComponentListSupplier {
        setTranslationPrefix("component_assembly_line")
        add("GTO寰宇格雷科技数年心血的结晶，工业自动化里程碑" translatedTo "Culmination of GTO Cosmic GregTech's years of effort, milestone in industrial automation") { gold() }
        add("将复杂部件装配流程整合为完美协同的流水线艺术" translatedTo "Integrating complex component assembly into perfectly synchronized production line artistry") { gray() }
        add("铱金外壳下是数千个精密伺服电机的完美共舞" translatedTo "Beneath iridium shells, thousands of precision servo motors dance in perfect harmony") { aqua() }
        add("虽然建造成本惊人，但带来的效率提升让投资物超所值" translatedTo "Though construction costs are staggering, the efficiency gains make it worth every penny") { yellow() }
        add("从螺丝到芯片，所有基础部件都能在这里高效产出" translatedTo "From screws to chips, all basic components can be efficiently produced here") { green() }
        add("董事长曾说：这是凡间最接近神之造物的工业奇迹" translatedTo "The chairman once said: This is industry's closest approximation to divine creation") { gold() }
        add("维护成本同样高昂，但为了未来必须承受的重量" translatedTo "Maintenance costs are equally high, but a necessary burden for the future") { red() }
        highlight("让基础部件制造进入全新时代" translatedTo "Ushering in a new era of basic component manufacturing")
    }

    // 分子重组仪
    val MolecularTransformerTooltips = ComponentListSupplier {
        setTranslationPrefix("molecular_transformer")
        add("量子级分子操纵器，能在原子层面重新排列物质的基本结构" translatedTo "Quantum-level molecular manipulator capable of rearranging matter at atomic scale") { aqua() }
        add("通过调控化学键的断裂与形成，实现物质性质的彻底转变" translatedTo "By controlling chemical bond breaking and formation achieves complete transformation of material properties") { blue() }
        add("幽蓝的切伦科夫辐射，标志着分子层面的剧烈变化" translatedTo "Emits faint blue Cherenkov radiation indicating intense molecular-level changes") { yellow() }
        add("重组过程不可逆，请确认输入输出物质以避免珍贵材料损失" translatedTo "Transformation process is irreversible confirm input/output materials to avoid losing precious resources") { red() }
        highlight("物质炼金术的巅峰之作重新定义材料科学的边界" translatedTo "Pinnacle of material alchemy redefining the boundaries of materials science")
    }

    // 星核钻机
    val PlanetCoreDrillingTooltips = ComponentListSupplier {
        setTranslationPrefix("planet_core_drilling")
        add("地心熔炉与星核共振器结合体，直接撕裂行星内部物质结构" translatedTo "Combination of mantle furnace and core resonator, directly tearing planetary interior matter structures") { aqua() }
        add("超引力钻头以相对论速度旋转，穿透地幔直达星核深处" translatedTo "Super-gravity drill bit rotates at relativistic speeds, penetrating mantle to reach core depths") { blue() }
        add("每秒提取65536份全谱系矿物，相当于整个世界的矿石洪流" translatedTo "Extracts 65,536 units of full-spectrum minerals per second, equivalent to a world's ore torrent") { lightPurple() }
        add("行星引力场稳定装置确保钻探过程不会导致地壳结构崩塌" translatedTo "Planetary gravity stabilization prevents crustal collapse during drilling operations") { green() }
        add("运行时的震波能在大陆板块间传递，被称作'星核的心跳'" translatedTo "Operational shockwaves travel through continental plates, called 'the heartbeat of the core'") { yellow() }
        highlight("将整颗行星转化为资源" translatedTo "Transform entire planets into resources")
    }

    // 进阶集成矿石处理厂
    val AdvancedIntegratedOreProcessorTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_integrated_ore_processor")
        add("矿石处理的终极答案，从原矿到成品的完美闭环" translatedTo "Ultimate solution for ore processing, perfect closed loop from raw ore to finished product") { blue() }
        add("量子级并行处理架构，同时处理9E个配方线程" translatedTo "Quantum-level parallel processing architecture, handles 9E recipe threads simultaneously") { gray() }
        add("就像拥有无限双手的炼金术士，同时进行所有工序" translatedTo "Like an alchemist with infinite hands, performing all processes at once") { yellow() }
        add("破碎、洗矿、离心、研磨——一步到位的神奇体验" translatedTo "Crushing, washing, centrifuging, maceration - all in one magical experience") { aqua() }
        add("可能会让传统的多机器产线感到失业焦虑" translatedTo "May cause unemployment anxiety in traditional multi-machine production lines") { gold() }
        highlight("重新定义矿石处理" translatedTo "Redefining ore processing")
    }

    // 稀土处理综合设施
    val ComprehensiveTombarthiteProcessingFacilityTooltips = ComponentListSupplier {
        setTranslationPrefix("comprehensive_tombarthite_processing_facility")
        add("稀土处理的工业巨构，将离心分离技术推向极致" translatedTo "Industrial megastructure for rare earth processing, pushing centrifugal separation to the extreme") { blue() }
        add("多重离心阵列协同工作，实现稀土元素的无损精准分离" translatedTo "Multiple centrifugal arrays work in harmony, achieving lossless precise separation of rare earth elements") { yellow() }
        add("量子级密度识别系统，确保每个元素都被完美分选" translatedTo "Quantum-level density recognition system ensures perfect sorting of every element") { aqua() }
        add("从钪到镥，17种稀土元素在这里找到各自的归宿" translatedTo "From scandium to lutetium, all 17 rare earth elements find their destined paths here") { gold() }
        add("运行时的精密振动如同大地的心跳，沉稳而有力" translatedTo "Precision vibrations during operation resemble the earth's heartbeat, steady and powerful") { red() }
        highlight("稀土分离技术的终极体现，让稀有元素变得触手可及" translatedTo "Ultimate embodiment of rare earth separation technology, making rare elements within reach")
    }

    // 木化工厂
    val WoodDistillationTooltips = ComponentListSupplier {
        setTranslationPrefix("wood_distillation")
        add("绿色化学的工业典范，将木质纤维转化为万千化工原料" translatedTo "Industrial典范 of green chemistry, transforming wood fibers into myriad chemical materials") { green() }
        add("催化气体精准调控，让木质素和纤维素高效分离重组" translatedTo "Precise catalytic gas control enables efficient separation and重组 of lignin and cellulose") { gray() }
        add("从木材到甲醇、从木屑到化纤，实现可再生资源的最大化利用" translatedTo "From wood to methanol, from sawdust to chemical fibers, maximizing utilization of renewable resources") { yellow() }
        add("生物催化技术与高温裂解的完美结合，零碳排放的绿色工艺" translatedTo "Perfect combination of biocatalysis and pyrolysis, zero-carbon emission green process") { aqua() }
        add("散发淡淡木质香气，仿佛森林在工业化身" translatedTo "Emits faint woody aroma, like a forest's industrial incarnation") { gold() }
        highlight("可持续发展化工" translatedTo "Chemistry with Sustainability")
    }

    // 石化工厂
    val PetrochemicalPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("petrochemical_plant")
        add("工业炼金术的终极殿堂，将黑色黄金转化为万千化工奇迹" translatedTo "Ultimate temple of industrial alchemy, transforming black gold into countless chemical marvels") { blue() }
        add("裂化线圈与精密分馏塔的完美交响，一步完成全流程" translatedTo "Perfect symphony of cracking coils and precision fractionation towers, completing entire process in one step") { gray() }
        add("从原油到塑料、从重油到化纤，魔法般的物质蜕变" translatedTo "Magical material metamorphosis from crude oil to plastics, from heavy oil to chemical fibers") { yellow() }
        add("裂解、重整、分馏——石化工业的三大乐章在此齐奏" translatedTo "Cracking, reforming, fractionation - three movements of petrochemical industry playing in harmony") { aqua() }
        add("庞大的金属丛林，却是有机化工最精密的艺术舞台" translatedTo "Vast metal jungle, yet the most precise artistic stage for organic chemistry") { red() }
        highlight("让复杂化工变得简单高效" translatedTo "Make complex chemistry simple and efficient")
    }

    // 纳米集成加工中心
    val NanitesIntegratedProcessingCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("nanites_integrated_processing_center")
        add("纳米蜂群技术革命，让传统化工产线成为过去式" translatedTo "Nanites colony technology revolution, making traditional chemical production lines obsolete") { green() }
        add("三模块自由切换：矿物萃取、聚合物扭曲、生物工程" translatedTo "Three modular modes: mineral extraction, polymer distortion, bioengineering") { gray() }
        add("模块化设计让复杂产线成为历史，一机解决所有难题" translatedTo "Modular design makes complex production lines history, one machine solves all problems") { gray() }
        add("自修复、自适应、自优化，智能蜂群重新定义生产效率" translatedTo "Self-repairing, self-adapting, self-optimizing, intelligent swarms redefine production efficiency") { yellow() }
        add("零浪费、零污染、零误差，完美制造的终极答案" translatedTo "Zero waste, zero pollution, zero error - the ultimate answer to perfect manufacturing") { yellow() }
        highlight("工业生产和纳米蜂群的集成" translatedTo "Integration of industrial production and nanobot swarms")
    }

    // 化工复合体
    val ChemicalComplexTooltips = ComponentListSupplier {
        setTranslationPrefix("chemical_complex")
        add("量子级反应控制，让每个分子都在精确的时空点相遇" translatedTo "Quantum-level reaction control, ensuring every molecule meets at precise spacetime coordinates") { gray() }
        add("分子工程的指挥中心，亿万纳米蜂群协同演绎化学交响" translatedTo "Command center of molecular engineering, billions of nanobots performing chemical symphonies") { blue() }
        add("纳米蜂群如同微观交响乐团，指挥着原子间的舞蹈" translatedTo "Nanobot swarms conduct atomic dances like microscopic orchestras") { yellow() }
        add("从简单化合到复杂聚合，化学反应从未如此优雅精准" translatedTo "From simple combinations to complex polymerizations, chemical reactions never so elegant and precise") { aqua() }
        add("纳米蜂群工作时会发出幽蓝光芒，如同微观星云闪烁" translatedTo "Nanobot swarms emit ethereal blue glow during operation, like microscopic nebulae") { gold() }
        highlight("分子合成如同艺术创作" translatedTo "Molecular synthesis is as elegant as artistic creation")
    }

    // 元素复制机
    val ElementCopyingTooltips = ComponentListSupplier {
        setTranslationPrefix("element_copying")
        add("量子级元素复制，从虚无中创造物质" translatedTo "Quantum-level element replication, creating substance from void") { gray() }
        add("插入预设光盘，选择元素，剩下的交给时空打印机" translatedTo "Insert preset discs, select elements, leave the rest to the spacetime printer") { yellow() }
        add("GTO寰宇集团物质重构技术突破，打破元素守恒定律" translatedTo "GTO Cosmic Group's matter reconstruction breakthrough, defying elemental conservation laws") { blue() }
        add("从氢到锎，元素周期表就是你的购物清单" translatedTo "From hydrogen to californium, the periodic table is your shopping list") { aqua() }
        add("炼金术师的终极梦想，现在只需要电力和UU物质" translatedTo "Alchemist's ultimate dream, now only needs electricity and UU matter") { red() }
        highlight("元素复制成为工业常态" translatedTo "Element replication becomes industrial routine")
    }

    // 特大线材轧机
    val MegaWiremillTooltips = ComponentListSupplier {
        setTranslationPrefix("mega_wiremill")
        add("当金属遇见绝对的力量" translatedTo "When metal meets absolute power") { lightPurple() }
        add("万吨压力下诞生的完美导体" translatedTo "Perfect conductors born under thousand tons of pressure") { gray() }
        add("工业血脉的锻造者——将金属转化为能量通道" translatedTo "Forger of industrial veins - transforming metal into energy pathways") { gold() }
        add("超导级精度确保每个原子完美排列" translatedTo "Superconducting precision ensures perfect atomic alignment") { yellow() }
        add("从厚重锭块到纤细如发的完美导线" translatedTo "From bulky ingots to hair-thin perfect wires") { green() }
        add("为整个文明编织能量的神经网络" translatedTo "Weaving neural networks of energy for entire civilizations") { aqua() }
        highlight("线材制造艺术的终极体现" translatedTo "Ultimate embodiment of wire manufacturing art")
    }

    // 超导磁驱冲击装置
    val SuperconductingMagneticPresserTooltips = ComponentListSupplier {
        setTranslationPrefix("superconducting_magnetic_presser")
        add("来自御坂美琴的工艺传承" translatedTo "Misaka Mikoto's craft legacy") { blue() }
        add("以十亿伏特的力量将金属塑造成型" translatedTo "Shaping metal with billion-volt power") { gray() }
        add("电磁加速让材料获得炮弹般的动能" translatedTo "Electromagnetic acceleration gives materials projectile-like kinetic energy") { aqua() }
        add("就像超电磁炮一样精准，但用于制造而非破坏" translatedTo "As precise as Railgun, but for creation instead of destruction") { yellow() }
        add("「この腕で、未来を打ち抜く」" translatedTo "'With this arm, I'll shoot through the future'") { gold() }
        add("可以轻松把硬币加速到三马赫！" translatedTo "Can easily accelerate coins to Mach 3!") { red() }
        highlight("电磁力量的工业应用" translatedTo "Industrial application of electromagnetic power")
    }

    // 重型辊轧机
    val HeavyRollingTooltips = ComponentListSupplier {
        setTranslationPrefix("heavy_rolling")
        add("维度压缩的艺术大师，将三维压入二维" translatedTo "Master of dimensional compression, squeezing 3D into 2D") { blue() }
        add("万吨压力让金属在维度间屈服" translatedTo "Ten-thousand-ton pressure makes metal yield between dimensions") { gray() }
        add("从厚板到薄箔，每一次碾压都是空间的折叠" translatedTo "From thick plates to thin foils, each roll is a fold in space") { aqua() }
        add("「二向箔制造模式」——开玩笑的，大概" translatedTo "'Dimensional Foil Production Mode' — just kidding, probably") { yellow() }
        add("过度碾压可能导致材料进入量子级薄度" translatedTo "Excessive rolling may lead to quantum thinness") { gold() }
        highlight("工业级维度压缩能力" translatedTo "Industrial-grade dimensional compression")
    }

    // 相变魔方
    val PhaseChangeCubeTooltips = ComponentListSupplier {
        setTranslationPrefix("phase_change_cube")
        add("在立方体间重构分子排列，重塑物质的存在形式" translatedTo "Reconstructing molecular arrangements within cubes, reshaping material existence") { blue() }
        add("量子级别的相变控制，精确到每一个原子键" translatedTo "Quantum-level phase change control, precise to every atomic bond") { gray() }
        add("将钢铁化为流水，让熔岩瞬间凝固" translatedTo "Turning steel into flowing water, solidifying lava instantly") { aqua() }
        add("六个面对应六种相变模式，就像真正的魔方" translatedTo "Six faces correspond to six phase change modes, just like a real Rubik's Cube") { yellow() }
        highlight("突破物质形态的界限" translatedTo "Breaking the boundaries of material states")
    }

    // 粒子流矩阵封装机
    val ParticleStreamMatrixFillingMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("particle_stream_matrix_filling_machine")
        add("量子级别的封装艺术，让流体在矩阵中完美定格" translatedTo "Quantum-level encapsulation art, perfectly freezing fluids within matrices") { blue() }
        add("粒子流在磁场引导下精确注入容器，零损耗零污染" translatedTo "Particle streams precisely injected into containers under magnetic guidance, zero loss zero contamination") { gray() }
        add("从普通铁桶到力场容器，适应各种封装需求" translatedTo "From ordinary iron drums to forcefield containers, adaptable to various encapsulation needs") { aqua() }
        add("矩阵式多通道同时作业，效率提升百倍" translatedTo "Matrix-style multi-channel simultaneous operation, hundredfold efficiency improvement") { yellow() }
        add("甚至可以封装等离子体和量子流体这类特殊物质" translatedTo "Capable of encapsulating special materials like plasma and quantum fluids") { gold() }
        add("连光子和声波都能完美封存" translatedTo "Even photons and sound waves can be perfectly preserved") { green() }
        highlight("安全储存最不稳定的物质" translatedTo "Enable safe storage of even the most unstable substances")
    }

    // 熔火之心
    val MoltenCoreTooltips = ComponentListSupplier {
        setTranslationPrefix("molten_core")
        add("地心熔炉的化身，将热量提升至行星核心级别" translatedTo "Incarnation of the planetary core furnace, raising heat to planetary core levels") { red() }
        add("模仿恒星内部环境，让流体达到极限温度" translatedTo "Simulating stellar interior environments, bringing fluids to extreme temperatures") { gray() }
        add("超导线圈产生足以熔化时空的磁场约束热核反应" translatedTo "Superconducting coils generate magnetic fields strong enough to melt spacetime, containing thermonuclear reactions") { red() }
        add("即使是液态钨在这里也会像水一样沸腾" translatedTo "Even liquid tungsten boils like water here") { gold() }
        add("「小心烫伤」的警告在这里显得过于轻描淡写" translatedTo "'Caution: Hot' warnings seem like understatements here") { yellow() }
        add("等离子体模式下可产生超过太阳表面的温度" translatedTo "Plasma mode can generate temperatures exceeding the solar surface") { aqua() }
        highlight("让任何流体都达到理论温度极限" translatedTo "Push any fluid to theoretical temperature limits")
    }

    val smartSifteringHubTooltips = ComponentListSupplier {
        setTranslationPrefix("smart_siftering_hub")

        add("智能筛分中心，矿物处理的未来" translatedTo "Smart Sifting Hub, the future of mineral processing") { gold() }
        add("集成多种筛分技术，实现高效精准的矿物分离" translatedTo "Integrates multiple sifting technologies for efficient and precise mineral separation") { gray() }
        highlight(("物质筛分的智能革命" translatedTo "Intelligent revolution in material sifting")) { gold() }
    }

    // 复合式蒸馏分馏塔
    val CompoundDistillationTowerTooltips = ComponentListSupplier {
        setTranslationPrefix("compound_distillation_tower")
        add("这是现代炼金术的圣殿" translatedTo "This is the temple of modern alchemy") { lightPurple() }
        add("原油的暗夜，在此裂解出光的碎片" translatedTo "The crude oil's darkness, cracks into fragments of light here") { gold() }
        add("用几何的刚毅线条书写分子世界的缱绻情书" translatedTo "Writing tender love letters of the molecular world with geometric rigid lines") { lightPurple() }
        add("最炽热的交融终将成就最极致的纯粹" translatedTo "The most intense fusion will ultimately achieve the most extreme purity") { aqua() }
        add("格雷科技化工设计部门的最新力作" translatedTo "GregTech's chemical design department latest masterpiece") { yellow() }
        add("DHG-1020复合式蒸馏分馏塔" translatedTo "DHG-1020 compound distillation fractionation tower") { gold() }
        highlight("极高效率的多功能化工处理设施" translatedTo "Highly efficient multi-functional chemical processing facility")
    }

    // 巨型酿造厂
    val MegaBrewerTooltips = ComponentListSupplier {
        setTranslationPrefix("mega_brewer")
        add("工业级发酵艺术，将酿造传统提升到史诗级规模" translatedTo "Industrial-scale fermentation art, elevating brewing tradition to epic proportions") { gold() }
        add("精密温控系统确保每个发酵罐都处于完美微生物环境" translatedTo "Precision temperature control ensures perfect microbial environment in every fermentation tank") { gray() }
        add("从麦芽汁到琥珀琼浆，万吨级酿造从未如此精准优雅" translatedTo "From wort to amber nectar, thousand-ton-scale brewing never so precise and elegant") { yellow() }
        add("多阶段并行发酵，让时间与风味在金属巨罐中完美交融" translatedTo "Multi-stage parallel fermentation, perfect fusion of time and flavor in metal giants") { aqua() }
        add("运行时散发醉人香气，整个车间都弥漫着酿造的艺术" translatedTo "Emits intoxicating aromas during operation, the entire workshop filled with brewing artistry") { green() }
        highlight("重新定义酿造规模" translatedTo "Redefining brewing scale")
    }

    // 引雷针
    val LightningRodTooltips = ComponentListSupplier {
        setTranslationPrefix("lightning_rod")
        add("普罗米修斯盗火，我们驾驭雷霆" translatedTo "Prometheus stole fire, we harness thunder") { gold() }
        add("用亿伏特电弧撕裂虚空，从闪电中锻造物质" translatedTo "Tearing the void with billion-volt arcs, forging matter from lightning") { gray() }
        add("索尔的铁锤在此显得渺小，宙斯的雷霆成为生产线" translatedTo "Thor's hammer seems small here, Zeus' thunder becomes production line") { yellow() }
        add("玻璃外壳内电蛇狂舞，宛如囚禁着雷神的愤怒" translatedTo "Electric serpents dance within glass casing, like imprisoned fury of thunder gods") { blue() }
        add("请做好防雷措施" translatedTo "Take lightning precautions") { red() }
        highlight("用雷霆书写工业神话" translatedTo "Writing industrial myths with thunder")
    }

    // 磁能反应炉
    val MagneticEnergyReactionFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("magnetic_energy_reaction_furnace")
        add("电磁创世的熔炉，用电能重塑物质本源" translatedTo "Electromagnetic creation furnace, reshaping material essence with electricity") { aqua() }
        add("强磁场约束下的等离子电弧，达到恒星核心的温度" translatedTo "Plasma arcs constrained by strong magnetic fields, reaching stellar core temperatures") { gray() }
        add("既可无中生有创造物质，也能让万物回归元素本源" translatedTo "Both creating matter from nothing, and returning all things to elemental origins") { yellow() }
        add("产生臭氧气息，如同雨后雷暴般清新而危险" translatedTo "Produce ozone scent, fresh yet dangerous like post-storm thunder") { blue() }
        highlight("让创造与回收在电弧中完美统一" translatedTo "Perfect unity of creation and recycling in electric arcs")
    }

    // 高能激光车床
    val HighEnergyLaserLatheTooltips = ComponentListSupplier {
        setTranslationPrefix("high_energy_laser_lathe")
        add("光子雕刻大师，用激光之刃重塑物质的几何形态" translatedTo "Photon engrave master, reshaping material geometry with laser blades") { aqua() }
        add("透明车间内光子舞蹈，纳米级精度雕刻出完美构件" translatedTo "Photons dance in transparent workshop,纳米-level precision carving perfect components") { gray() }
        add("从金属到钻石，激光所至无物不雕，无材不工" translatedTo "From metal to diamond, nothing uncarvable by laser, no material unworkable") { yellow() }
        add("激光路径如光之织锦，在材料表面编织精密纹路" translatedTo "Laser paths like light tapestry, weaving precise patterns on material surfaces") { gold() }
        add("运行时会投射出绚烂全息图，宛如未来工厂的视觉交响" translatedTo "Projects dazzling holograms during operation, like visual symphony of future factories") { red() }
        add("透过透明外壳，可以亲眼见证光子雕刻的魔法时刻" translatedTo "Through transparent casing, witness the magical moments of photon雕刻 firsthand") { blue() }
        highlight("让激光成为最优雅的雕刻工具" translatedTo "Make laser the most elegant carving tool")
    }

    // 中子丝线切割
    val NeutroniumWireCuttingTooltips = ComponentListSupplier {
        setTranslationPrefix("neutronium_wire_cutting")
        add("星核材料切割专家，用中子丝线分割宇宙最坚硬的物质" translatedTo "Stellar core material cutting expert, using neutron threads to divide the universe's hardest substances") { lightPurple() }
        add("中子简并态丝线，以强相互作用力实现原子级精准切割" translatedTo "Neutron degenerate state threads achieve atomic-level precision cutting through strong interaction force") { gray() }
        add("连中子星物质都能轻松分割，地球材料如同黄油般柔软" translatedTo "Even neutron star material cuts easily, earthly materials feel like butter") { yellow() }
        add("切伦科夫辐射蓝光如同微型超新星爆发" translatedTo "Cherenkov radiation blue glow, like miniature supernova explosions") { aqua() }
        add("绝对零度冷却系统确保中子丝线保持量子干涉状态" translatedTo "Absolute zero cooling system maintains neutron threads in quantum coherence state") { gold() }
        highlight("从此分割变得如同光线般精准" translatedTo "From now on division is as precise as light beams")
    }

    // 纳米吞噬厂
    val NanoPhagocytosisPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("nano_phagocytosis_plant")
        add("物质终结者，纳米蜂群组成的吞噬风暴" translatedTo "Matter terminator, phagocytosis storm composed of nanobot swarms") { gray() }
        add("万亿级纳米机器人协同攻击，以分子精度分解一切物质" translatedTo "Trillions of nanobots attack in unison, decomposing all matter with molecular precision") { gray() }
        add("没有任何材料能抵挡纳米蜂群的集体吞噬" translatedTo "No material can withstand the collective吞噬 of nanobot swarms") { red() }
        add("密集的嗡鸣如同微观世界的蝗虫过境" translatedTo "Dense humming is like locust swarms in the microscopic world") { yellow() }
        add("分解产物自动分类收集，实现100%物质回收利用率" translatedTo "Decomposition products automatically sorted and collected, achieving 100% material recycling") { aqua() }
        add("请勿投入贵重物品，纳米蜂群不会区分目标和垃圾" translatedTo "Do not input valuables, nanobot swarms don't distinguish between targets and trash") { gold() }
        highlight("粉碎技术让物质分解变得如同呼吸般自然" translatedTo "Crushing technology makes material decomposition as natural as breathing")
    }

    // 巨型浸洗池
    val MegaBathTankTooltips = ComponentListSupplier {
        setTranslationPrefix("mega_bath_tank")
        add("工业级浸泡艺术，让流体与物质在巨池中深度交融" translatedTo "Industrial-scale immersion art, deep integration of fluids and materials in giant pools") { blue() }
        add("百万升容量设计，同时处理数千种材料的表面处理" translatedTo "Million-liter capacity design, simultaneously processing surface treatment of thousands of materials") { gray() }
        add("从酸洗到镀膜，从蚀刻到钝化，浸泡改变物质表面命运" translatedTo "From pickling to coating, etching to passivation, immersion alters material surface destinies") { yellow() }
        add("池面泛起的细腻波纹，如同工业版本的温泉疗愈" translatedTo "Delicate ripples form on the surface, like industrial version of hot spring therapy") { gold() }
        add("高浓度表面活性剂可能导致不可逆的蛋白质变性" translatedTo "High-concentration surfactants may cause irreversible protein denaturation") { red() }
        highlight("材料改性的魔法仪式" translatedTo "A magical ritual for material modification")
    }

    // 巨型真空干燥炉
    val MegaVacuumDryingFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("mega_vacuum_drying_furnace")
        add("虚空干燥专家，在真空中完美分离" translatedTo "Void drying expert, perfectly separating in vacuum") { yellow() }
        add("超低气压环境下，水分瞬间升华，只留下纯净的单质粉末" translatedTo "In ultra-low pressure environment, moisture instantly sublimates, leaving only pure elemental powder") { gray() }
        add("从泡沫到粉末的华丽蜕变，真空环境确保零氧化零污染" translatedTo "Magnificent transformation from foam to powder, vacuum environment ensures zero oxidation zero contamination") { yellow() }
        add("多层加热线圈精准控温，让每个矿石颗粒都获得完美干燥" translatedTo "Multi-layer heating coils provide precise temperature control, ensuring perfect drying for every ore particle") { aqua() }
        add("运行时机体发出低沉嗡鸣，如同在真空中演奏工业交响曲" translatedTo "Emits low hum during operation, like performing industrial symphony in vacuum") { gold() }
        highlight("矿石处理在真空中达到完美纯净" translatedTo "Achieve perfect purity in ore processing through vacuum")
    }

    // 分子震荡脱水装置
    val MolecularOscillationDehydratorTooltips = ComponentListSupplier {
        setTranslationPrefix("molecular_oscillation_dehydrator")
        add("智子级脱水技术，用分子震荡剥离每一个水分子" translatedTo "Sophon-level dehydration tech, stripping every water molecule with molecular oscillation") { yellow() }
        add("十一维震荡波穿透物质，让水分无处遁形" translatedTo "11-dimensional oscillation waves penetrate matter, leaving water molecules nowhere to hide") { gray() }
        add("「脱水！脱水！」——三体文明的工业应用" translatedTo "'Dehydrate! Dehydrate!' — Trisolaran civilization's industrial application") { yellow() }
        add("从有机体到矿物，任何含湿材料都能瞬间变得绝对干燥" translatedTo "From organisms to minerals, any moist material can become absolutely dry instantly") { aqua() }
        add("微弱智子发出闪烁，仿佛在向半人马座发送信号" translatedTo "Faint sophon flickering, as if sending signals to Alpha Centauri") { red() }
        highlight("脱水技术的维度突破" translatedTo "Dimensional breakthrough in dehydration tech")
    }

    // 极限压缩装置
    val HorizontalCompressorTooltips = ComponentListSupplier {
        setTranslationPrefix("extreme_compressor")
        add("物质密度艺术家，用兆吨压力重塑原子间距" translatedTo "Matter density artist, reshaping atomic distances with megaton pressure") { yellow() }
        add("行星级液压系统，让最坚硬的金属也变得顺从可塑" translatedTo "Planetary-level hydraulic system, making even the hardest metals compliant and malleable") { gray() }
        add("从锭到块，从粉到板，压缩改变物质的存在形态" translatedTo "From ingots to blocks, from powder to plates, compression alters material existence forms") { yellow() }
        add("量子隧道效应补偿，确保压缩过程中零材料损耗" translatedTo "Quantum tunneling effect compensation ensures zero material loss during compression") { aqua() }
        highlight("让物质密度突破理论极限" translatedTo "Break theoretical limits of material density")
    }

    // 极限电炉
    val ExtremeElectricFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("extreme_electric_furnace")
        add("智能加热线圈精准调控，让每个配方都获得最佳熔炼效果" translatedTo "Smart heating coils provide precise control, ensuring optimal smelting for every recipe") { gray() }
        add("从矿石熔炼到合金制备，电能热转化效率突破99.9%" translatedTo "From ore smelting to alloy preparation, electrical-thermal conversion efficiency突破 99.9%") { yellow() }
        add("多温区协同工作，同时处理固体熔炼和简单合金配方" translatedTo "Multi-zone coordination, simultaneously processing solid smelting and simple alloy recipes") { aqua() }
        add("只有材料熔化的细微声响见证变化" translatedTo "Only subtle sounds of melting materials见证 transformation") { gold() }
        add("虽然温度惊人，但相比等离子熔炉显得格外温文尔雅" translatedTo "Though temperatures are impressive, compared to plasma furnaces it's remarkably 'gentle'") { red() }
        highlight("电能熔炼的完美典范" translatedTo "Perfect example of electric smelting")
    }

    // 高温反应枢纽
    val HighTemperatureReactionHubTooltips = ComponentListSupplier {
        setTranslationPrefix("high_temperature_reaction_hub")
        add("超导加热矩阵精准控温，让每个反应都在最佳热力学窗口进行" translatedTo "Superconducting heating matrix provides precise temperature control, ensuring every reaction occurs in optimal thermodynamic windows") { gray() }
        add("从材料液化到高温合成，热激活能在这里被完美利用" translatedTo "From material liquefaction to high-temperature synthesis, thermal activation energy is perfectly utilized here") { yellow() }
        add("多反应室独立控制温度" translatedTo "Independent temperature control for multiple reaction chambers.") { aqua() }
        add("炽热橙光如同微型太阳在车间中诞生" translatedTo "Hot orange glow, like miniature suns being born in the workshop") { gold() }
        add("热浪辐射范围极大，请确保工作区通风和隔热措施" translatedTo "Heat wave radiation range is extensive, ensure workshop ventilation and insulation") { red() }
        highlight("让热力学成为工业生产的精确工具" translatedTo "Make thermodynamics a precise tool for industrial production")
    }

    // 引力弯折装置
    val GravityBendingDeviceTooltips = ComponentListSupplier {
        setTranslationPrefix("gravity_bending_device")
        add("时空曲率雕刻者，用人工引力重塑材料形态" translatedTo "Spacetime curvature sculptor, reshaping material forms with artificial gravity") { lightPurple() }
        add("局部引力场操控，让最坚硬的板材也优雅弯曲" translatedTo "Local gravitational field manipulation, making even the hardest plates bend gracefully") { gray() }
        add("从平板到曲面，从直杆到圆环，引力是最好的造型师" translatedTo "From flat plates to curved surfaces, straight rods to perfect rings, gravity is the best stylist") { yellow() }
        add("多引力源协同作用，实现复杂三维曲面的精确成型" translatedTo "Multiple gravity sources work in harmony, achieving precise forming of complex 3D surfaces") { aqua() }
        add("运行时空间微微扭曲，光线在设备周围产生引力透镜效应" translatedTo "Space slightly distorts during operation, light producing gravitational lensing effects around the device") { gold() }
        add("在强引力场中移动可以体验时空旅行" translatedTo "Moving in strong gravitational fields can give you spacetime travel experience") { red() }
        highlight("引力成为最优雅的弯折工具" translatedTo "Gravity becomes the most elegant bending tool")
    }

    // 阿拉克涅之手
    val HandOfArachneTooltips = ComponentListSupplier {
        setTranslationPrefix("hand_of_arachne")
        add("古罗马掌管编织的神" translatedTo "The ancient Rome's weaving goddess") { lightPurple() }
        add("量子级导线编织，让电流在更粗的通道中奔腾" translatedTo "Quantum-level wire weaving, allowing current to surge through thicker channels") { gray() }
        add("从单线到16倍粗缆，导电能力呈几何级数增长" translatedTo "From single wires to 16x thick cables, conductivity grows exponentially") { yellow() }
        add("多轴协同编织，如同蜘蛛女神在编织电流的网络" translatedTo "Multi-axis coordinated weaving, like the spider goddess weaving networks of current") { aqua() }
        add("运行时机臂优雅舞动，宛如阿拉克涅在跳工业之舞" translatedTo "Mechanical arms dance gracefully during operation, like Arachne performing an industrial dance") { gold() }
        highlight("导线编织的终极艺术" translatedTo "Ultimate art of wire weaving")
    }

    // 裂解反应枢纽
    val CrackerHubTooltips = ComponentListSupplier {
        setTranslationPrefix("cracker_hub")
        add("用高温高压撕裂碳氢化合物的枷锁" translatedTo "Tearing apart hydrocarbon bonds with high temperature and pressure") { red() }
        add("六根反应柱协同工作，实现复杂分子的精确裂解与重组" translatedTo "Six reaction columns work in harmony, achieving precise cracking and重组 of complex molecules") { gray() }
        add("从重油到轻质燃料，从长链到短链，裂解改变分子命运" translatedTo "From heavy oil to light fuels, long chains to short chains, cracking alters molecular destinies") { yellow() }
        add("管道网络如血管般交织，输送着被高温解放的分子碎片" translatedTo "Pipeline networks intertwine like blood vessels, transporting molecular fragments liberated by high heat") { aqua() }
        add("裂解过程极其剧烈，请确保安全阀正常工作" translatedTo "Cracking process is extremely violent, ensure safety valves are functional") { red() }
        highlight("分子裂解的智慧核心" translatedTo "Intelligent core of molecular cracking")
    }

    // 维度聚焦激光蚀刻阵列
    val DimensionalFocusEngravingArrayTooltips = ComponentListSupplier {
        setTranslationPrefix("dimensional_focus_engraving_array")
        add("光束从不同维度汇聚，在奇点处实现量子级精密蚀刻" translatedTo "Light beams converge from different dimensions, achieving quantum-level precision etching at singularities") { yellow() }
        add("运行时六道光束如神之手指，在中心球体编织光之图案" translatedTo "Six light beams during operation like divine fingers, weaving light patterns on the central sphere") { aqua() }
        add("中心球体会呈现彩虹色辉光，仿佛孕育着微型宇宙" translatedTo "Central sphere displays rainbow luminescence, as if nurturing a miniature universe") { gold() }
        add("需要光刻胶作为蚀刻介质，确保激光精准传递能量" translatedTo "Requires photoresist as etching medium, ensuring precise laser energy transfer") { green() }
        highlight("让雕刻艺术突破时空限制" translatedTo "Break through spacetime limits in carving art")
    }

    // 超级冶炼炉
    val SuperBlastSmelterTooltips = ComponentListSupplier {
        setTranslationPrefix("super_blast_smelter")
        add("用人工太阳的温度熔炼万物" translatedTo "Smelting all things with artificial sun temperatures") { red() }
        add("等离子线圈产生超越地核的热量" translatedTo "Plasma coils generate heat surpassing Earth's core") { gray() }
        add("从钨钢到中子星物质，没有什么是极限温度无法熔化的" translatedTo "From tungsten steel to neutron star matter, nothing can withstand extreme temperatures") { yellow() }
        add("多相合金同步冶炼，精确控制每种元素的配比与融合" translatedTo "Multi-phase alloy simultaneous smelting, precisely controlling each element's ratio and fusion") { aqua() }
        add("炉心闪耀着白炽光芒，如同囚禁着一颗微型恒星" translatedTo "Furnace core glows with incandescent light, like imprisoning a miniature star") { gold() }
        highlight("熔炼技术的终极答案" translatedTo "Ultimate answer to smelting technology")
    }

    // 复合式极端冷却装置
    val CompoundExtremeCoolingUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("compound_extreme_cooling_unit")
        add("绝对零度艺术家" translatedTo "Absolute zero artist") { aqua() }
        add("量子制冷矩阵协同工作，瞬间吸收巨量热能" translatedTo "Quantum refrigeration matrix works协同, instantly absorbing massive thermal energy") { gray() }
        add("从白热锭块到熔融金属，甚至等离子体都能瞬间凝固成型" translatedTo "From white-hot ingots to molten metals, even plasma can instantly solidify and form") { yellow() }
        add("设备表面结满霜晶，如同极地冰川的工业化身" translatedTo "Device surface frosts over, like industrial incarnation of polar glaciers") { red() }
        add("激光定位确保冷却均匀，避免材料因温差应力破裂" translatedTo "Laser positioning ensures uniform cooling, preventing material fracture from thermal stress") { green() }
        highlight("让极端低温成为工业生产的精确工具" translatedTo "Make extreme low temperatures a precise industrial tool")
    }

    // 超导电磁工厂
    val SuperconductingElectromagnetismTooltips = ComponentListSupplier {
        setTranslationPrefix("superconducting_electromagnetism")
        add("麦克斯韦方程组的工业化身，电磁统一的完美体现" translatedTo "Industrial incarnation of Maxwell's equations, perfect embodiment of electromagnetic unity") { blue() }
        add("超流态氦冷却下的零电阻环境，实现量子磁通钉扎效应" translatedTo "Zero-resistance environment under superfluid helium cooling, achieving quantum flux pinning effect") { gray() }
        add("洛伦兹力与法拉第定律在这里协同演绎工业交响" translatedTo "Lorentz force and Faraday's law perform industrial symphony in harmony here") { yellow() }
        add("迈斯纳效应确保完美抗磁性，仿佛在嘲笑欧姆定律的局限" translatedTo "Meissner effect ensures perfect diamagnetism, as if mocking the limitations of Ohm's law") { aqua() }
        add("运行时产生持续的约瑟夫森振荡，如同在向超导先驱们致敬" translatedTo "Continuous Josephson oscillation during operation, like paying homage to superconductivity pioneers") { gold() }
        highlight("电磁学应用的巅峰之作" translatedTo "Pinnacle of electromagnetic applications")
    }

    // 晶体构建者
    val CrystalBuilderTooltips = ComponentListSupplier {
        setTranslationPrefix("crystal_builder")
        add("布拉格父子的梦想工厂，在原子层面编织完美晶格" translatedTo "Bragg父子's dream factory, weaving perfect crystal lattices at atomic level") { blue() }
        add("超高压环境下实现柯塞尔生长模式，培育无缺陷单晶结构" translatedTo "Achieving Kossel growth mode under ultra-high pressure, cultivating defect-free monocrystalline structures") { gray() }
        add("从硅熔体中拉制完美单晶，晶向精度达到角秒级别" translatedTo "Pulling perfect monocrystals from silicon melt, crystal orientation precision reaching arc-second level") { yellow() }
        add("高压反应室如同钻石砧槽，在极端条件下孕育晶体之美" translatedTo "High-pressure chamber like diamond anvil cell, nurturing crystal beauty under extreme conditions") { gold() }
        highlight("晶体生长的艺术" translatedTo "Art of crystal growth")
    }

    // 神圣分离者
    val HolySeparatorTooltips = ComponentListSupplier {
        setTranslationPrefix("holy_separator")
        add("用向心力演绎物质分离的神圣仪式" translatedTo "Perform sacred rituals of material separation with centripetal force") { lightPurple() }
        add("沉降速度差成为分离之匙，密度梯度揭示组分奥秘" translatedTo "Sedimentation velocity differences become separation keys, density gradients reveal composition mysteries") { yellow() }
        add("祭坛般的旋转平台，让离心过程如同神圣的净化仪式" translatedTo "Altar-like rotating platform, making centrifugation like a sacred purification ritual") { aqua() }
        add("庄严的低频嗡鸣，仿佛在吟唱分离的圣歌" translatedTo "Emits solemn low-frequency hum, like chanting separation hymns") { gold() }
        highlight("密度差异成为最优雅的分离艺术" translatedTo "Density differences become the most elegant separation art")
    }

    // 力场压模工厂
    val FieldExtruderFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("field_extruder_factory")
        add("无形之手塑造万般形态" translatedTo "Shape myriad forms with intangible hands") { blue() }
        add("量子力场精准调控，让金属在无形模具中自由流动" translatedTo "Quantum force field precise control, allowing metals to flow freely in intangible molds") { gray() }
        add("从齿轮到管道，从螺丝到轴承，万般部件一念成形" translatedTo "From gears to pipes, screws to bearings, all components form in an instant") { yellow() }
        add("麦克斯韦妖在此工作，用信息熵减实现完美成型" translatedTo "Maxwell's demon works here, achieving perfect forming through information entropy reduction") { aqua() }
        add("力场波动如透明丝绸，在空间中编织出精密构件" translatedTo "Force field undulations like transparent silk, weaving precision components in space") { gold() }
        highlight("部件成型的革命突破" translatedTo "Revolutionary breakthrough in component forming")
    }

    // 中子锻砧
    val NeutronForgingAnvilTooltips = ComponentListSupplier {
        setTranslationPrefix("neutron_forging_anvil")
        add("仿佛听到中子星心跳的声音" translatedTo "As if hearing neutron star heartbeat") { gold() }
        add("强相互作用力锻台，用中子简并压重塑物质本源" translatedTo "Strong interaction force forging platform, reshaping material essence with neutron degeneracy pressure") { lightPurple() }
        add("模拟中子星表面环境，实现宇宙级压力的等静压成型" translatedTo "Simulating neutron star surface conditions, achieving cosmic-level isostatic pressing") { yellow() }
        add("从金属锻造到粉末压实，万亿帕斯卡压力下万物归形" translatedTo "From metal forging to powder compaction, all things take form under trillion pascal pressure") { yellow() }
        add("超高压操作须严格遵循规程" translatedTo "Ultra-high pressure operations must strictly follow procedures") { red() }
        highlight("宇宙级的锻造力量" translatedTo "Cosmic-level forging power")
    }

    // 双子星封装系统
    val GeminiContainmentSystemTooltips = ComponentListSupplier {
        setTranslationPrefix("gemini_containment_system")
        add("量子纠缠封装技术，让每个容器都与内容物建立超维连接" translatedTo "Quantum entanglement packaging, establishing transdimensional connections between containers and contents") { gray() }
        add("一边将万物装入完美包裹，一边让封印之物重见天日" translatedTo "Packaging all things into perfect parcels while releasing sealed items back to light") { yellow() }
        add("泡利不相容原理的工业应用，确保每个封装都完美无瑕" translatedTo "Industrial application of Pauli exclusion principle, ensuring every package is flawless") { aqua() }
        add("如同宇宙正负电荷的完美平衡" translatedTo "Like perfect balance of cosmic positive and negative charges") { gold() }
        highlight("封装技术的镜像" translatedTo "Mirror of packaging technology")
    }

    // 克尔-纽曼均质仪
    val KerrNewmanHomogenizerTooltips = ComponentListSupplier {
        setTranslationPrefix("kerr_newman_homogenizer")
        add("用相对论效应实现完美均质" translatedTo "Achieve perfect homogeneity through relativistic effects") { lightPurple() }
        add("参考系拖拽与电磁场协同，让混合物在时空扭曲中达到绝对均匀" translatedTo "Frame-dragging and electromagnetic fields collaborate, achieving absolute uniformity in spacetime distortion") { gray() }
        add("从胶体到乳液，即使最不相容的物质也能在这里达成和谐" translatedTo "From colloids to emulsions, even the most incompatible substances achieve harmony here") { yellow() }
        add("克尔-纽曼度规在此具现化，旋转电荷产生独特的混合时空" translatedTo "Kerr-Newman metric materialized here, rotating charges create unique mixing spacetime") { aqua() }
        add("搅拌叶片如事件视界般旋转，吞噬所有不均匀性" translatedTo "Mixing blades rotate like event horizons, devouring all inhomogeneity") { gold() }
        highlight("均质化达到宇宙级的完美标准" translatedTo "Relativistic achieves cosmic-level perfect homogeneity standards")
    }

    // 溶解核心
    val DissolutionCoreTooltips = ComponentListSupplier {
        setTranslationPrefix("dissolution_core")
        add("溶剂魔法圣殿，用分子间力解开物质的化学枷锁" translatedTo "Solvent magic sanctuary, unlocking chemical bonds with intermolecular forces") { aqua() }
        add("范德华力与氢键在此成为最优雅的溶解艺术家" translatedTo "Van der Waals forces and hydrogen bonds become the most elegant dissolution artists here") { gray() }
        add("从矿物酸溶到生物煮解，极性分子与非极性分子的完美共舞" translatedTo "From mineral acid dissolution to biological digestion, perfect dance of polar and non-polar molecules") { yellow() }
        add("溶解度参数精准调控，让每种物质找到最合适的溶剂伴侣" translatedTo "Precise control of solubility parameters, helping each material find its perfect solvent partner") { aqua() }
        add("反应釜中溶液如液态彩虹，演绎着溶解与析出的平衡之美" translatedTo "Solutions in reactor like liquid rainbow, performing the beauty of dissolution-precipitation balance") { gold() }
        highlight("让分子间作用力成为工业分离的魔法钥匙" translatedTo "Make intermolecular forces the magic key to industrial separation")
    }

    // 综合气相沉积系统
    val IntegratedVaporDepositionSystemTooltips = ComponentListSupplier {
        setTranslationPrefix("integrated_vapor_deposition_system")
        add("原子级镀膜圣殿，让物质以气态演绎重生之舞" translatedTo "Atomic-level coating sanctuary, where materials perform rebirth dance in gaseous state") { blue() }
        add("PVD与CVD完美融合，物理溅射与化学气相协同作用" translatedTo "Perfect fusion of PVD and CVD, physical sputtering and chemical vapor deposition work synergistically") { gray() }
        add("从纳米镀层到金刚石薄膜，单原子层精度控制沉积过程" translatedTo "From nano-coatings to diamond films, single atomic layer precision controls deposition process") { yellow() }
        add("朗缪尔-布洛杰特技术在此进化，实现三维复杂曲面均匀沉积" translatedTo "Langmuir-Blodgett technique evolved here, achieving uniform deposition on complex 3D surfaces") { aqua() }
        add("真空室中原子如星尘飘落，在基材表面编织完美涂层" translatedTo "Atoms drift like stardust in vacuum chamber, weaving perfect coatings on substrate surfaces") { gold() }
        highlight("表面工程的终极答案" translatedTo "Ultimate answer to surface engineering")
    }

    // 微生物之主
    val MicroorganismMasterTooltips = ComponentListSupplier {
        setTranslationPrefix("microorganism_master")
        add("生命科学的工业圣殿，在绝对洁净中驾驭微生物的力量" translatedTo "Industrial sanctuary of life science, harnessing microbial power in absolute cleanliness") { green() }
        add("绝对洁净空间连一个杂菌都无法存活" translatedTo "Absolute clean space where not a single contaminant survives") { gold() }
        add("自适应紫外辐射系统精准调控每个微生物的生长环境" translatedTo "Adaptive UV radiation system precisely controls every microorganism's growth environment") { gray() }
        add("纳米级环境控制让最脆弱的生物样本也能安全培育" translatedTo "Nanoscale environmental control enables safe cultivation of even the most fragile biological samples") { yellow() }
        add("警告：辐射期间请勿直视内部！" translatedTo "Warning: Avoid looking inside during radiation!") { red() }
        highlight("生物技术的巅峰之作" translatedTo "Pinnacle of biotechnology")
    }

    // 生命熔炉
    val LifeForgeTooltips = ComponentListSupplier {
        setTranslationPrefix("life_forge")
        add("生命炼金圣殿，在工业熔炉中培育生物奇迹" translatedTo "Life alchemy sanctuary, cultivating biological miracles in industrial furnaces") { green() }
        add("自适应辐射场精准调控，为生化反应提供最佳能量环境" translatedTo "Adaptive radiation field precise control, providing optimal energy environment for biochemical reactions") { gray() }
        add("从细胞提取到酶催化，生命过程在这里被工业化重现" translatedTo "From cell extraction to enzyme catalysis, life processes are industrially recreated here") { yellow() }
        add("辐射屏蔽穹顶下，生物分子在能量激流中保持活性" translatedTo "Under radiation shielding dome, biomolecules maintain activity in energy torrents") { aqua() }
        add("生物活性监测系统确保每个反应都在生命友好条件下进行" translatedTo "Bio-activity monitoring system ensures every reaction occurs in life-friendly conditions") { red() }
        highlight("生物技术的熔炉核心" translatedTo "Furnace core of biotechnology")
    }

    // 综合组装车间
    val IntegratedAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("integrated_assembler")
        add("GTO寰宇集团时空工程技术结晶，突破装配维度限制" translatedTo "Crystallization of GTO Cosmic Group's spacetime engineering, breaking dimensional assembly limits") { blue() }
        add("超时空装配单元线性排列，如同跨越维度的传送带" translatedTo "Hyperspatial assembly units linearly arranged like cross-dimensional conveyor belts") { gray() }
        add("从纳米芯片到星舰引擎，万物皆可在此完美组装" translatedTo "From nano-chips to starship engines, everything can be perfectly assembled here") { yellow() }
        add("量子纠缠定位系统确保每个零件在最佳时空点对接" translatedTo "Quantum entanglement positioning ensures each component docks at optimal spacetime points") { aqua() }
        add("「让复杂成为简单，让不可能成为日常」——GTO设计理念" translatedTo "'Make complexity simple, make impossible routine' — GTO design philosophy") { gold() }
        add("时空波动期间请勿手动干预" translatedTo "Do not manually intervene during spacetime fluctuations") { red() }
        highlight("重新定义工业装配的可能性" translatedTo "Redefine industrial assembly possibilities")
    }

    // 精密组装中心
    val PrecisionAssemblyCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("precision_assembly_center")
        add("微米级装配圣殿，用机械之指谱写精密的工业诗篇" translatedTo "Micron-level assembly sanctuary, composing precise industrial poetry with mechanical fingers") { aqua() }
        add("量子视觉系统确保每个零件都在纳米精度下完美对接" translatedTo "Quantum vision system ensures every component docks perfectly at nanometer precision") { gray() }
        add("从微型芯片到精密仪器，组装误差小于原子直径" translatedTo "From microchips to precision instruments, assembly error smaller than atomic diameter") { yellow() }
        add("防震平台与恒温环境，为精密组装提供绝对稳定的舞台" translatedTo "Anti-vibration platform and constant temperature environment provide absolutely stable stage for precision assembly") { gold() }
        add("机械臂移动如芭蕾舞者，在微观世界中演绎装配的艺术" translatedTo "Robotic arms move like ballet dancers, performing the art of assembly in the microscopic world") { green() }
        add("呼吸过重都可能影响精度，需要佩戴专业防护装备" translatedTo "Even heavy breathing may affect precision, professional protective gear needed") { red() }
        highlight("让微观世界的组装变得完美无瑕" translatedTo "Makes microscopic assembly flawless")
    }

    // 纳米蜂群电路组装工厂
    val NanoswarmCircuitAssemblyFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("nanoswarm_circuit_assembly_factory")
        add("在绝对寂静的空间里" translatedTo "In absolutely silent space") { lightPurple() }
        add("一场微观宇宙的创世仪式正在上演" translatedTo "A creation ceremony of the microscopic universe is being performed") { gray() }
        add("纳米蜂群工厂——这里没有熔炉的咆哮" translatedTo "Nanoswarm factory - there is no roar of furnaces here") { gold() }
        add("没有机械臂的挥舞" translatedTo "No waving of mechanical arms") { aqua() }
        add("只有亿万纳米机器人以光的语言低语" translatedTo "Only billions of nanobots whispering in the language of light") { yellow() }
        add("它们如同辛勤的工人，在硅片中建造微观城市" translatedTo "They are like diligent workers, building microscopic cities in silicon") { green() }
        add("为工业帝国不断输送神经" translatedTo "Continuously supplying nerves for the industrial empire") { gold() }
        highlight("最高级电路组装的终极设备" translatedTo "Ultimate equipment for highest-grade circuit assembly")
    }

    // 奈亚拉托提普之触
    val NyarlathotepsTentacleTooltips = ComponentListSupplier {
        setTranslationPrefix("nyarlathoteps_tentacle")
        add("当空间本身成为装配台" translatedTo "When space itself becomes the assembly platform") { lightPurple() }
        add("现实的结构在触须间重新编织" translatedTo "The fabric of reality rewoven between tentacles") { gray() }
        add("GTO寰宇集团的终极造物——超越制造的制造" translatedTo "GTO Cosmic Group's ultimate creation - manufacturing beyond manufacturing") { gold() }
        add("无需机械的运动" translatedTo "No mechanical movement required") { aqua() }
        add("维度触须直接在量子层面操作物质" translatedTo "Dimensional tentacles manipulate matter at the quantum level") { yellow() }
        add("每一次触碰都是物理法则的重新定义" translatedTo "Every touch is a redefinition of physical laws") { green() }
        add("在时空的织布机上编织未来的蓝图" translatedTo "Weaving blueprints of the future on the looms of spacetime") { blue() }
        highlight("让不可能成为生产线上的日常" translatedTo "Make the impossible routine on the production line")
    }

    // 巨型浮游选矿池
    val GiantFlotationTankTooltips = ComponentListSupplier {
        setTranslationPrefix("giant_flotation_tank")
        add("矿物浮选圣殿，用气泡之舞分离大地的馈赠" translatedTo "Mineral flotation sanctuary, separating earth's gifts with bubble dances") { blue() }
        add("界面化学与流体动力学的完美工业演绎" translatedTo "Perfect industrial performance of interface chemistry and fluid dynamics") { gray() }
        add("亿万气泡如微型升降机，精准分选不同密度的矿物颗粒" translatedTo "Billions of bubbles like miniature elevators, precisely sorting mineral particles by density") { yellow() }
        add("接触角与表面张力在此成为分离艺术的关键参数" translatedTo "Contact angle and surface tension become key parameters of separation art here") { aqua() }
        add("泡沫层如液态银河，闪耀着矿物分离的璀璨光芒" translatedTo "Foam layer like liquid galaxy, shimmering with the brilliance of mineral separation") { gold() }
        add("安全生产 警钟长鸣，须规范操作" translatedTo "Safety production, alarm bells toll forever, all must be operated standardly") { red() }
        highlight("让浮选成为大地宝藏的精准分拣师" translatedTo "Make flotation the precise sorter of earth's treasures")
    }

    // 超限绿洲
    val TransliminalOasisTooltips = ComponentListSupplier {
        setTranslationPrefix("transliminal_oasis")
        add("生命绿洲的工业化身，在金属丛林中培育万物生机" translatedTo "Industrial incarnation of life oasis, cultivating all living things in metal jungles") { green() }
        add("光合作用与生物催化在此达到工业化完美平衡" translatedTo "Photosynthesis and biocatalysis achieve industrial perfect balance here") { gray() }
        add("生物活性方块如跳动心脏，为整个系统注入生命能量" translatedTo "Bio-active blocks pulse like beating hearts, infusing life energy into the entire system") { yellow() }
        add("温室穹顶下万物生长，工业与自然达成奇妙和谐" translatedTo "All things grow under the greenhouse dome, industry and nature achieve wondrous harmony") { gold() }
        highlight("农业科技的绿洲" translatedTo "Oasis of agricultural technology")
    }

    // 狂飙巨型聚变反应堆控制电脑
    val KuangbiaoGiantNuclearFusionReactorTooltips = ComponentListSupplier {
        setTranslationPrefix("kuangbiao_giant_nuclear_fusion_reactor")
        add("它所模拟的，是亘古不变的光芒" translatedTo "It simulates the eternal light") { aqua() }
        add("磁场约束下的等离子体以光速旋转，重现宇宙创世时的能量密度" translatedTo "Plasma spins at light speed within magnetic constraints recreating cosmic creation energy density") { blue() }
        add("氘氚聚变产生的能量足以点亮整座城市，却安静得如同沉睡的婴儿" translatedTo "Deuterium-tritium fusion generates enough energy to power entire cities yet remains silent as a sleeping infant") { lightPurple() }
        add("反应堆核心的温度超过一亿摄氏度，是人类制造的最接近太阳的造物" translatedTo "Reactor core exceeds 100 million degrees Celsius humanity's closest creation to a star") { green() }
        add("每秒钟数百万次聚变反应，都在向宇宙证明文明的科技巅峰" translatedTo "Millions of fusion reactions per second prove civilization's technological pinnacle to the cosmos") { yellow() }
        highlight("将恒星之力囚禁于钢铁之中" translatedTo "Imprisoning stellar power within steel civilization")
    }

    // 中子旋涡
    val NeutronVortexTooltips = ComponentListSupplier {
        setTranslationPrefix("neutron_vortex")
        add("中子能级压缩器在量子真空中制造出持续旋转的微型中子星环境" translatedTo "Neutron energy compressors create rotating miniature neutron star environments in quantum vacuum") { aqua() }
        add("未授权操作可能导致局部时空结构崩解" translatedTo "Unauthorized operation may cause local spacetime collapse") { red() }
        add("超流体中子流以接近光速旋转，在时空结构中撕开短暂的微虫洞" translatedTo "Superfluid neutron streams spin at near-light speed tearing transient micro-wormholes in spacetime fabric") { blue() }
        add("原子核在中子漩涡中被强行激发，产生常规物理条件下不可能的反应" translatedTo "Atomic nuclei are forcibly excited in the neutron vortex producing reactions impossible under normal physics") { lightPurple() }
        add("实时量子监控系统确保漩涡稳定性，防止中子逃逸造成维度侵蚀" translatedTo "Real-time quantum monitoring ensures vortex stability preventing neutron escape and dimensional erosion") { green() }
        highlight("将物质转化推向了量子极限" translatedTo "Push matter transformation to quantum limits")
    }

    // 熵流引擎
    val EntropyFluxEngineTooltips = ComponentListSupplier {
        setTranslationPrefix("entropy_flux_engine")
        add("爱因斯坦的梦想，质能方程的现实化身" translatedTo "Einstein's dream, physical manifestation of E=mc²") { green() }
        add("从虚无中创造质量，物理学家的终极玩具" translatedTo "Creating mass from nothing, physicist's ultimate toy") { gray() }
        add("可能轻微违反若干守恒定律" translatedTo "May slightly violate some conservation laws") { lightPurple() }
        add("运行成本：巨额电能；收获：几毫克的质量" translatedTo "Operating cost: massive power; Yield: milligrams of mass") { yellow() }
        add("但谁说梦想需要讲究性价比呢？" translatedTo "But since when do dreams need cost-effectiveness?") { gold() }
        highlight("质能转换技术的巅峰体现" translatedTo "The pinnacle of mass-energy conversion technology")
    }

    // PCB工厂
    val PCBFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("pcb_factory")
        add("纳米蜂群在量子层面编织电路，每个原子都是精准布局的节点" translatedTo "Nano-swarms weave circuits at quantum level, each atom a precisely placed node") { aqua() }
        add("自修复石墨烯基底配合液态金属导线，实现电路板的动态重构" translatedTo "Self-healing graphene substrate with liquid metal wiring enables dynamic circuit reconfiguration") { blue() }
        add("光子蚀刻技术以光速绘制微米级电路，精度超越传统工艺千倍" translatedTo "Photon etching technology draws micron-level circuits at light speed, precision a thousand times better") { lightPurple() }
        add("实时量子检测系统确保每块PCB板都达到绝对零缺陷标准" translatedTo "Real-time quantum inspection ensures every PCB meets absolute zero-defect standards") { green() }
        add("制造过程中产生的热噪声被转化为能量，实现负熵生产循环" translatedTo "Thermal noise from manufacturing is converted to energy, achieving negative entropy production cycle") { yellow() }
        highlight("纳米制造时代的黎明为电子工业带来无限可能" translatedTo "The dawn of nano-manufacturing brings limitless possibilities to electronics industry")
    }

    // 蜂群之心
    val SwarmCoreTooltips = ComponentListSupplier {
        setTranslationPrefix("swarm_core")
        add("纳米蜂群孕育圣殿，万亿智能单元的诞生摇篮" translatedTo "Nanobot swarm growing sanctuary, birth cradle of trillions of intelligent units") { aqua() }
        add("量子自组装技术，让纳米机器人在费曼梦中自我复制" translatedTo "Quantum self-assembly technology, enabling nanobots to self-replicate in Feynman's dreams") { gray() }
        add("从硅基芯片到碳纳米管，每个蜂群单元都承载着工业智慧" translatedTo "From silicon chips to carbon nanotubes, each swarm unit carries industrial wisdom") { yellow() }
        add("自进化算法让蜂群不断优化，但请确保控制协议牢固" translatedTo "Self-evolution algorithms constantly optimize swarms, but ensure control protocols are secure") { red() }
        highlight("纳米技术的核心引擎" translatedTo "Core engine of nanotechnology")
    }

    // 物质生成机
    val MatterFabricatorTooltips = ComponentListSupplier {
        setTranslationPrefix("matter_fabricator")
        add("GTO研发部门的革命性突破，将废弃材料转化为万能的UU物质" translatedTo "GTO R&D's revolutionary breakthrough converting waste materials into universal UU matter") { aqua() }
        add("量子级分子重构系统能分解任何废料，重组成基础物质单元" translatedTo "Quantum-level molecular restructuring system decomposes any waste into basic matter units") { blue() }
        add("从此小到花草，大到宇宙，万物具有了无限可能性" translatedTo "From now on, from flowers to the cosmos, everything has infinite possibilities") { lightPurple() }
        add("能量消耗极大，但相比传统制造方式仍具有显著效率优势" translatedTo "Although energy-intensive it offers significant efficiency advantages over traditional manufacturing") { green() }
        highlight("变废为宝和物质循环的可能性" translatedTo "The possibility of waste-to-resource and material recycling")
    }

    // 磁约束维度震荡装置
    val MagneticConfinementDimensionalityShockDeviceTooltips = ComponentListSupplier {
        setTranslationPrefix("magnetic_confinement_dimensionality_shock_device")
        add("演奏宇宙的弦理论乐章" translatedTo "Play the string theory symphony of the cosmos") { yellow() }
        add("维度工程的奇迹造物，在现实结构中制造可控的时空涟漪" translatedTo "Miracle of dimensional engineering, creating controlled spacetime ripples in reality's fabric") { lightPurple() }
        add("产生多重磁场，将高能等离子体束缚在维度交界处" translatedTo "Generates multi-layered magnetic fields, confining energetic plasma at dimensional boundaries") { gray() }
        add("混沌级别的能量搅拌，让等离子体在现实与虚空间震荡跃迁" translatedTo "Chaos-level energy agitation, making plasma oscillate between reality and void") { lightPurple() }
        add("每一次震荡都在改写局部物理常数，产生不可思议的反应产物" translatedTo "Each oscillation rewrites local physical constants, producing unimaginable reaction products") { aqua() }
        highlight("在混沌中创造秩序" translatedTo "Ultimate solution for energetic plasma processing, creating order from chaos")
    }

    // 进阶质量发生器
    val AdvancedMassFabricatorTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_mass_fabricator")
        add("血红外壳下的创世之力，让质能方程成为生产线上的日常" translatedTo "Creative power within blood red shell, making mass-energy equations routine on production lines") { red() }
        add("线圈编织能量网络，从虚空中编织出实在的质量" translatedTo "Coils weave energy networks, spinning substantial mass from the void") { gray() }
        add("效率突破临界点，现在产出终于超越了能量消耗" translatedTo "Efficiency breakthrough critical point, output now finally exceeds energy consumption") { red() }
        add("从微观粒子到宏观物质，创世过程变得可控可量化" translatedTo "From microscopic particles to macroscopic matter, the creation process becomes controllable and quantifiable") { gold() }
        add("振金外壳发出深红色脉动，如同跳动的心脏" translatedTo "Vibranium casing pulses with deep crimson glow during operation, like a beating heart") { yellow() }
        highlight("质能转换技术的工业级实现" translatedTo "Industrial realization in mass-energy conversion")
    }

    // 进阶超临界合成机
    val AdvancedSpsCraftingTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_sps_crafting")
        add("纯白超临界外壳下的超维度合成奇迹" translatedTo "Transdimensional synthesis miracle within pure white sps casing") { white() }
        add("同时维持多个超临界相，在多重现实中并行合成" translatedTo "Maintaining multiple supercritical phases simultaneously, parallel crafting across multiverse") { gray() }
        add("量子纠缠协调系统，确保每个临界点完美同步" translatedTo "Quantum entanglement coordination system ensures perfect synchronization of every critical point") { aqua() }
        add("外壳呈现珍珠般的光泽流动，美得令人窒息" translatedTo "Casing displays pearl-like luminous flows, breathtakingly beautiful") { yellow() }
        add("突破单一时空限制，现在可以在不同维度同时进行合成" translatedTo "Breaking single spacetime limits, now capable of simultaneous synthesis across dimensions") { aqua() }
        highlight("让物质创造突破维度限制" translatedTo "Ultimate form of supercritical synthesis, breaking dimensional limits in material creation")
    }

    // 恒星终极物质锻造工厂
    val StarUltimateMaterialForgeFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("star_ultimate_material_forge_factory")
        add("创世熔炉的终极形态，在工厂中重现恒星的诞生与毁灭" translatedTo "Ultimate form of creation furnace, recreating stellar birth and destruction in a factory") { gold() }
        add("模拟超新星爆发环境，锻造出只存在于恒星核心的物质" translatedTo "Simulating supernova explosion environments, forging materials found only in stellar cores") { gray() }
        add("从磁流体到时空晶体，制造宇宙中最神秘的终极材料" translatedTo "From magnetofluids to spacetime crystals, manufacturing the universe's most mysterious ultimate materials") { yellow() }
        add("时空在这里被扭曲，物质在这里被重塑，法则在这里被改写" translatedTo "Spacetime twists here, matter reshapes here, physical laws rewritten here") { blue() }
        highlight("工业生产达到创世神的领域" translatedTo "Industrial production comes to the realm of creation gods")
    }

    // 通天之路
    val RoadOfHeavenTooltips = ComponentListSupplier {
        setTranslationPrefix("road_of_heaven")
        add("人类通往宇宙的钢铁脐带" translatedTo "Humanity's steel umbilical cord to the cosmos") { aqua() }
        add("同步轨道空间站与地面基地的量子纠缠能量传输系统" translatedTo "Quantum entanglement energy transmission system between synchronous orbit station and ground base") { blue() }
        add("64个模块化无人机接口可同时调度数百架太空采矿无人机" translatedTo "64 modular drone interfaces, capable of coordinating hundreds of space mining drones simultaneously") { lightPurple() }
        add("碳纳米管缆绳强度超越钻石，长度延伸至地球静止轨道" translatedTo "Carbon nanotube cable strength exceeds diamond, extending to geostationary orbit") { green() }
        add("等离子推进无人机以亚光速穿梭于小行星带采集稀有矿物" translatedTo "Plasma-propelled drones travel at sub-light speed through asteroid belts collecting rare minerals") { yellow() }
        highlight("将地球文明延伸至太阳系的每个角落" translatedTo "Extend Earth's civilization to every corner of the solar system")
    }

    // 基岩钻机
    val BedrockDrillingRigTooltips = ComponentListSupplier {
        setTranslationPrefix("bedrock_drilling_rig")
        add("量子隧穿钻头突破物质稳定性极限，直接作用于基岩原子核" translatedTo "Quantum tunneling drill bit breaks material stability limits, acting directly on bedrock atomic nuclei") { aqua() }
        add("每次钻取都在挑战物理法则" translatedTo "Each drilling operation challenges physical laws") { red() }
        add("从基岩深处提取珍稀元素，但代价是可能永久改变世界底层结构" translatedTo "Extracts rare elements from bedrock depths, but may permanently alter the world's foundation") { yellow() }
        highlight("与宇宙基本法则的危险博弈" translatedTo "Chairman's stern warning: This is a dangerous game with the universe's fundamental laws")
    }

    // 创造之门
    val DoorOfCreateTooltips = ComponentListSupplier {
        setTranslationPrefix("door_of_create")
        add("GTO寰宇最深处的环形结构" translatedTo "GTO Universe's innermost ring structure") { aqua() }
        add("MAX级电压激活时龙息粒子开始舞动，在空间中撕裂出创造维度的通道" translatedTo "MAX voltage activates dragon's breath particles dancing tearing open pathways to the creation dimension") { blue() }
        add("环形门户以非欧几里得几何旋转，连接着现实与创造的边界" translatedTo "Rotating in non-Euclidean geometry the ring portal connects the boundaries of reality and creation") { lightPurple() }
        add("穿越者归来后对所见保持沉默，仿佛承载了无法言说的宇宙奥秘" translatedTo "Returned travelers remain silent about what they saw as if bearing unspeakable cosmic mysteries") { green() }
        add("董事长亲自监督的终极项目，代表着格雷科技探索的最高成就" translatedTo "The ultimate project personally supervised by the chairman representing GregTech's pinnacle of exploration") { yellow() }
        highlight("重新定义存在的可能性" translatedTo "Redefine the possibilities of existence")
    }

    // 创造聚合仪
    val CreateAggregationTooltips = ComponentListSupplier {
        setTranslationPrefix("create_aggregation")
        add("量子现实编织器以MAX电压运转，在维度间隙中撕开裂口" translatedTo "Quantum reality weaver operates at MAX voltage tearing openings in dimensional gaps") { aqua() }
        add("超导算力矩阵同步计算无限可能性，筛选出稳定的创造维度坐标" translatedTo "Supercomputing matrix synchronously calculates infinite possibilities filtering stable creation dimension coordinates") { blue() }
        add("现实结构在仪器周围扭曲变形，物理常数开始出现可观测波动" translatedTo "Reality structure distorts around the device physical constants begin observable fluctuations") { lightPurple() }
        highlight("对宇宙本质的最终探索" translatedTo "Ultimate exploration of cosmic essence")
    }

    // 地幔粉碎者
    val MantleCrusherTooltips = ComponentListSupplier {
        setTranslationPrefix("mantle_crusher")
        add("地球之心的工业化身，驾驭地幔力量粉碎一切" translatedTo "Industrial incarnation of Earth's core, harnessing mantle power to crush all") { gold() }
        add("只管把岩石丢进去，剩下的交给地震级别的机器力量" translatedTo "Just throw the rocks in, and leave the rest to the mantle-level crushing power") { gray() }
        add("从矿石到废料，任何物质在这里都能被粉碎成最细微的颗粒" translatedTo "From ores to waste, any material can be crushed into the finest particles here") { yellow() }
        highlight("碎岩机模式：需要在输入仓中放入对应流体" translatedTo "Rock crusher mode: requires corresponding fluid in input tank")
    }

    // 巨型烧结炉
    val GiantSinteringArrayTooltips = ComponentListSupplier {
        setTranslationPrefix("giant_sintering_furnace")
        add("相传技艺来自景德镇，烧结一切材料于炉中" translatedTo "Legendary techniques from Jingdezhen, sintering all materials in the furnace") { gold() }
        add("用高温与压力将粉末烧结成坚固的块体" translatedTo "Using high temperature and pressure to sinter powders into solid blocks") { gray() }
        highlight("非遗技艺的工业化传承" translatedTo "Industrial inheritance of intangible cultural heritage")
    }

    val giantElectrochemicalWorkstationTooltips = ComponentListSupplier {
        setTranslationPrefix("giant_electrochemical_workstation")
        story("GTO集团的化工技术一直走在世界前列" translatedTo "GTO Group's chemical technology has always been at the forefront of the world")
        story("这台巨型电化学工作站是公司最新的研发成果" translatedTo "This giant electrochemical workstation is the company's latest research and development achievement")
        story("它集成了多种先进的化学处理技术" translatedTo "It integrates multiple advanced chemical processing technologies")
        story("能够高效地进行复杂的化学反应和物质分离" translatedTo "Capable of efficiently performing complex chemical reactions and material separations")
        highlight("工业化学的未来已然到来" translatedTo "The future of industrial chemistry is here")
    }
}
