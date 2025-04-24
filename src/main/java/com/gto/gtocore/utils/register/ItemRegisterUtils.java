package com.gto.gtocore.utils.register;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.data.chemical.material.GTOMaterial;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.item.ToolTipsItem;
import com.gto.gtocore.api.playerskill.SkillType;
import com.gto.gtocore.api.playerskill.utils.UtilsTintableModel;
import com.gto.gtocore.common.data.GTOCovers;
import com.gto.gtocore.common.item.KineticRotorItem;
import com.gto.gtocore.common.item.playerskill.MysteriousBoostPotionBehaviour;
import com.gto.gtocore.common.item.playerskill.SkillUpgradePackageBehavior;
import com.gto.gtocore.utils.ColorUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.CoverPlaceBehavior;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import appeng.api.stacks.AEKeyType;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import appeng.core.localization.GuiText;
import appeng.items.materials.MaterialItem;
import appeng.items.materials.StorageComponentItem;
import appeng.items.storage.BasicStorageCell;
import com.google.common.collect.ImmutableTable;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.common.data.GTItems.attach;
import static com.gto.gtocore.api.playerskill.SkillRegistry.*;
import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;
import static com.gto.gtocore.common.data.GTOItems.*;

public final class ItemRegisterUtils {

    private ItemRegisterUtils() {}

    public static final Map<String, String> LANG = GTCEu.isDataGen() ? new HashMap<>() : null;

    public static @NotNull <T extends Item> ItemBuilder<T, Registrate> item(String name, String cn, NonNullFunction<Item.Properties, T> factory) {
        if (LANG != null) {
            if (LANG.containsKey(name)) {
                GTOCore.LOGGER.error("Repetitive Key: {}", name);
                throw new IllegalStateException();
            }
            if (LANG.containsValue(cn)) {
                GTOCore.LOGGER.error("Repetitive Value: {}", cn);
                throw new IllegalStateException();
            }
            LANG.put(name, cn);
        }
        return REGISTRATE.item(name, factory);
    }

    public static void InitUpgrades() {
        String storageCellGroup = GuiText.StorageCells.getTranslationKey();
        String portableCellGroup = GuiText.PortableCells.getTranslationKey();

        var itemCells = List.of(
                ITEM_STORAGE_CELL_1M, ITEM_STORAGE_CELL_4M, ITEM_STORAGE_CELL_16M, ITEM_STORAGE_CELL_64M,
                ITEM_STORAGE_CELL_256M);
        for (var itemCell : itemCells) {
            Upgrades.add(AEItems.FUZZY_CARD, itemCell, 1, storageCellGroup);
            Upgrades.add(AEItems.INVERTER_CARD, itemCell, 1, storageCellGroup);
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, itemCell, 1, storageCellGroup);
            Upgrades.add(AEItems.VOID_CARD, itemCell, 1, storageCellGroup);
        }

        var fluidCells = List.of(
                FLUID_STORAGE_CELL_1M, FLUID_STORAGE_CELL_4M, FLUID_STORAGE_CELL_16M, FLUID_STORAGE_CELL_64M,
                FLUID_STORAGE_CELL_256M);
        for (var fluidCell : fluidCells) {
            Upgrades.add(AEItems.INVERTER_CARD, fluidCell, 1, storageCellGroup);
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, fluidCell, 1, storageCellGroup);
            Upgrades.add(AEItems.VOID_CARD, fluidCell, 1, storageCellGroup);
        }

        Upgrades.add(AEItems.FUZZY_CARD, SUPER_PORTABLE_ITEM_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.INVERTER_CARD, SUPER_PORTABLE_ITEM_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, SUPER_PORTABLE_ITEM_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.VOID_CARD, SUPER_PORTABLE_ITEM_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.ENERGY_CARD, SUPER_PORTABLE_ITEM_CELL, 2, portableCellGroup);

        Upgrades.add(AEItems.INVERTER_CARD, SUPER_PORTABLE_FLUID_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, SUPER_PORTABLE_FLUID_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.VOID_CARD, SUPER_PORTABLE_FLUID_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.ENERGY_CARD, SUPER_PORTABLE_FLUID_CELL, 2, portableCellGroup);
    }

    public static void generateMaterialItem(GTRegistrate registrate, TagPrefix tagPrefix, Material material, ImmutableTable.Builder<TagPrefix, Material, ItemEntry<TagPrefixItem>> MATERIAL_ITEMS_BUILDER) {
        MATERIAL_ITEMS_BUILDER.put(tagPrefix, material, registrate
                .item(tagPrefix.idPattern().formatted(material.getName()), properties -> new TagPrefixItem(properties, tagPrefix, material))
                .onRegister(TagPrefixItem::onRegister)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .transform(GTItems.unificationItem(tagPrefix, material))
                .properties(p -> {
                    p.stacksTo(tagPrefix.maxStackSize());
                    if (tagPrefix instanceof GTOTagPrefix prefix) {
                        int maxDamage = prefix.maxDamage();
                        if (maxDamage > 0) p.durability(maxDamage);
                    }
                    if (material instanceof GTOMaterial mat) {
                        Rarity rarity = mat.gtocore$rarity();
                        if (rarity != null) p.rarity(rarity);
                    }
                    return p;
                })
                .model(NonNullBiConsumer.noop())
                .color(() -> TagPrefixItem::tintColor)
                .onRegister(GTItems::cauldronInteraction)
                .register());
    }

    public static ItemEntry<StorageComponentItem> registerStorageComponentItem(int tier) {
        return item("cell_component_" + tier + "m", tier + "M ME存储组件", p -> new StorageComponentItem(p, 1048576 * tier))
                .register();
    }

    public static ItemEntry<BasicStorageCell> registerStorageCell(int tier,
                                                                  ItemEntry<StorageComponentItem> StorageComponent,
                                                                  boolean isItem) {
        ItemDefinition<MaterialItem> CELL_HOUSING = isItem ? AEItems.ITEM_CELL_HOUSING : AEItems.FLUID_CELL_HOUSING;
        return item((isItem ? "item" : "fluid") + "_storage_cell_" + tier + "m", tier + "M " + (isItem ? "物品" : "流体") + "存储元件", p -> new BasicStorageCell(
                p.stacksTo(1),
                StorageComponent,
                CELL_HOUSING,
                3 + 0.5 * Math.log(tier) / Math.log(4),
                1024 * tier,
                1,
                isItem ? 128 : 36,
                isItem ? AEKeyType.items() : AEKeyType.fluids()))
                .register();
    }

    public static ItemEntry<ComponentItem> registerTieredCover(int amperage) {
        return item(GTValues.VN[GTValues.MAX].toLowerCase(Locale.ROOT) + "_" +
                (amperage == 1 ? "" : amperage + "a_") + "wireless_energy_receive_cover", (amperage == 1 ? "" : amperage + "安") + GTValues.VN[GTValues.MAX] + "无线能源接收器", ComponentItem::create)
                .lang(GTValues.VNF[GTValues.MAX] + " " + (amperage == 1 ? "" : amperage + "A ") + "Wireless Energy Receive Cover")
                .onRegister(attach(new TooltipBehavior(lines -> {
                    lines.add(Component.translatable("item.gtmthings.wireless_energy_receive_cover.tooltip.1"));
                    lines.add(Component.translatable("item.gtmthings.wireless_energy_receive_cover.tooltip.2"));
                    lines.add(Component.translatable("item.gtmthings.wireless_energy_receive_cover.tooltip.3",
                            GTValues.V[GTValues.MAX] * amperage));
                }), new CoverPlaceBehavior(amperage == 1 ? GTOCovers.MAX_WIRELESS_ENERGY_RECEIVE :
                        GTOCovers.MAX_WIRELESS_ENERGY_RECEIVE_4A)))
                .register();
    }

    public static <T extends ComponentItem> NonNullConsumer<T> attachRenderer(ICustomRenderer customRenderer) {
        return !GTCEu.isClientSide() ? NonNullConsumer.noop() : (item) -> item.attachComponents(customRenderer);
    }

    public static ItemEntry<KineticRotorItem> registerRotor(String id, String cn, int durability, int min, int max, int material) {
        return item(id, cn + "动力转子", p -> new KineticRotorItem(p, durability, min, max, material)).register();
    }

    public static ItemEntry<ToolTipsItem>[] registerCircuits(String name, String cn, int[] tiers, Function<Integer, Component> componentFunction) {
        ItemEntry<ToolTipsItem>[] entries = new ItemEntry[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            String id = name + "_" + GTValues.VN[tier].toLowerCase();
            ItemEntry<ToolTipsItem> register = item(id, GTOValues.VOLTAGE_NAMESCN[tier] + cn, p -> new ToolTipsItem(p, new Supplier[] { () -> Component.translatable("gtocore.tooltip.item." + name).withStyle(ChatFormatting.GRAY), () -> componentFunction.apply(tier) }))
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/circuit/" + id)))
                    .lang(GTValues.VOLTAGE_NAMES[tier] + ' ' + FormattingUtil.toEnglishName(name))
                    .tag(CustomTags.CIRCUITS_ARRAY[tier])
                    .register();
            entries[tier] = register;
        }
        return entries;
    }

    public static ItemEntry<ComponentItem>[] registerSkillUpgradePackage(SkillType skillType) {
        ItemEntry[] entries = new ItemEntry[GTValues.TIER_COUNT];
        if (!skillType.generateUpgradePackage) {
            GTOCore.LOGGER.error("SkillType {} does not generate upgrade package , but it was registered incorrectly, please use upgradePackageBonus", skillType.getId());
            return entries;
        }
        Map<SkillType, int[]> baseColorMap = Map.of(
                STRENGTH, new int[] { 0Xff0000, 0xff6b6b }, // 红色
                LIFE_INTENSITY, new int[] { 0x4fe82c, 0x9cfa87 }, // 绿色
                PHYSIQUE, new int[] { 0x6149fc, 0xa091ff } // 蓝色
        );
        int[] baseColor = baseColorMap.get(skillType);
        int[] stepGradient = ColorUtils.generateStepGradient(baseColor[0], baseColor[1], 70, 4);
        for (int tier : GTValues.ALL_TIERS) {
            entries[tier] = item(skillType.getId().toLowerCase() + "_skill_upgrade_package_" + GTValues.VN[tier].toLowerCase(),
                    GTOValues.VNFR[tier] + skillType.getChineseName() + " 能力提升包", ComponentItem::create)
                    .model((ctx, prov) -> UtilsTintableModel.createTintableModel(ctx, prov,
                            "item/skill/normal/normal_border",
                            "item/skill/normal/tier_border",
                            "item/skill/liquid_bottle/bottle",
                            "item/skill/liquid_bottle/liquid1",
                            "item/skill/liquid_bottle/liquid2",
                            "item/skill/liquid_bottle/liquid3",
                            "item/skill/liquid_bottle/liquid4"))
                    .tag(TagUtil.optionalTag(BuiltInRegistries.ITEM, GTOCore.id("skill_upgrade_package")))
                    .tag(TagUtil.optionalTag(BuiltInRegistries.ITEM, GTOCore.id("skill_upgrade_package_" + skillType.getId().toLowerCase())))
                    .onRegister(attach(new SkillUpgradePackageBehavior(tier, skillType)))
                    .color(() -> () -> (stack, tintIndex) -> {
                        if (tintIndex == 1) { // tier_border
                            float light_factor = 0.3f + ((tier + 1) * (0.7f / GTValues.TIER_COUNT));
                            int a = 0xFF;
                            int r = Math.min(255, (int) ((baseColor[0] >> 16 & 0xFF) * light_factor));
                            int g = Math.min(255, (int) ((baseColor[0] & 0xFF) * light_factor));
                            int b = Math.min(255, (int) ((baseColor[0]) * light_factor));
                            return (a << 24) | (r << 16) | (g << 8) | b;
                        } else {
                            return switch (tintIndex) {
                                case 3 -> stepGradient[0];
                                case 4 -> stepGradient[1];
                                case 5 -> stepGradient[2];
                                case 6 -> stepGradient[3];
                                default -> -1;
                            };
                        }
                    })
                    .register();
        }
        return entries;
    }

    public static ItemEntry<ComponentItem>[] registerMysteriousBoostDrink() {
        ItemEntry[] entries = new ItemEntry[GTValues.TIER_COUNT];
        for (int tier : GTValues.ALL_TIERS) {
            entries[tier] = item("gto_overseer_coke_" + GTValues.VN[tier].toLowerCase(),
                    "GTO牌 " + GTOValues.VNFR[tier] + " 监工可乐", ComponentItem::create)
                    .model((ctx, prov) -> UtilsTintableModel.createTintableModel(ctx, prov,
                            "item/skill/normal/normal_border",
                            "item/skill/normal/tier_border",
                            "item/skill/mysterious_boost_medicine/0_bottle",
                            "item/skill/mysterious_boost_medicine/1_face",
                            "item/skill/mysterious_boost_medicine/2_liquid1",
                            "item/skill/mysterious_boost_medicine/3_liquid2",
                            "item/skill/mysterious_boost_medicine/4_liquid3",
                            "item/skill/mysterious_boost_medicine/5_gtologo",
                            "item/skill/mysterious_boost_medicine/6_gtologobase",
                            "item/skill/mysterious_boost_medicine/7_straw"))
                    .color(() -> () -> (stack, tintIndex) -> {
                        if (tintIndex == 1) { // tier_border
                            float light_factor = 0.3f + ((tier + 1) * (0.7f / GTValues.TIER_COUNT));
                            int a = 0xFF;
                            int r = Math.min(255, (int) ((0x6149fc >> 16 & 0xFF) * light_factor));
                            int g = Math.min(255, (int) ((0x6149fc & 0xFF) * light_factor));
                            int b = Math.min(255, (int) ((0x6149fc) * light_factor));
                            return (a << 24) | (r << 16) | (g << 8) | b;
                        } else {
                            RandomSource rng = GTValues.RNG;
                            int[] stepGradient = ColorUtils.generateStepGradient(rng.nextInt(0x1000000), rng.nextInt(0x1000000), 70, 3);
                            return switch (tintIndex) {
                                case 4 -> stepGradient[0];
                                case 5 -> stepGradient[1];
                                case 6 -> stepGradient[2];
                                default -> -1;
                            };
                        }
                    })
                    .onRegister(attach(new MysteriousBoostPotionBehaviour(tier)))
                    .register();
        }
        return entries;
    }

    public static ItemEntry<ToolTipsItem> registerCircuit(String id, String cn, TagKey<Item> tagKey, Supplier<String> componentSupplier) {
        return item(id, cn, p -> new ToolTipsItem(p, new Supplier[] { () -> Component.literal(componentSupplier.get()) }))
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/circuit/" + id)))
                .tag(tagKey)
                .register();
    }

    public static ItemEntry<Item> registerEssence(String id, String cn) {
        return item(id + "_essence", cn + "精华", Item::new)
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/essence/" + id)))
                .tag(TagUtil.optionalTag(BuiltInRegistries.ITEM, GTOCore.id("vein_essence")))
                .register();
    }

    public static ItemEntry<Item> registerAlgae(String id, String cn) {
        return item(id + "_algae", cn + "藻", Item::new)
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/algae/" + id)))
                .tag(TagUtil.optionalTag(BuiltInRegistries.ITEM, GTOCore.id("algae")))
                .register();
    }

    public static ItemEntry<Item> registerAlgaeFiber(String id, String cn) {
        return item(id + "_algae_fiber", cn + "藻纤维", Item::new)
                .tag(TagUtil.optionalTag(BuiltInRegistries.ITEM, GTOCore.id("algae_fiber")))
                .register();
    }

    public static ItemEntry<Item> registerCustomModel(String id, String cn) {
        return item(id, cn, Item::new).model(NonNullBiConsumer.noop()).register();
    }

    public static ItemEntry<Item> register(String id, String cn) {
        return item(id, cn, Item::new).register();
    }

    public static ItemEntry<Item> registerTexture(String id, String cn, String texture) {
        return item(id, cn, Item::new)
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/" + texture)))
                .register();
    }

    public static ItemEntry<Item> registerLang(String id, String en, String cn) {
        return item(id, cn, Item::new)
                .lang(en).register();
    }

    public static ItemEntry<ToolTipsItem> registerTooltip(String id, String cn, Supplier<Component> componentSupplier) {
        return item(id, cn, p -> new ToolTipsItem(p, new Supplier[] { componentSupplier })).register();
    }
}
