package com.gtocore.common.data;

import com.gtolib.GTOCore;
import com.gtolib.api.player.organ.function.OrganItem;
import com.gtolib.api.player.organ.function.OrganType;
import com.gtolib.api.player.organ.function.item.behaviour.ElectricWingBehaviour;
import com.gtolib.api.player.organ.function.item.impl.DurabilityWingOrganItem;
import com.gtolib.api.player.organ.function.item.impl.MechanicalWingOrganItem;
import com.gtolib.api.player.organ.function.item.impl.TierOrganItem;
import com.gtolib.api.player.organ.function.item.tool.OrganEditorBehaviour;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTItems.attach;
import static com.gtolib.utils.register.ItemRegisterUtils.item;

public final class OrganItems {

    static void init() {}

    public static final ItemEntry<TierOrganItem>[] ORGAN_TIER_SPINE = Utils.registerTierOrganItem(OrganType.SPINE);
    public static final ItemEntry<TierOrganItem>[] ORGAN_TIER_LIVER = Utils.registerTierOrganItem(OrganType.LIVER);
    public static final ItemEntry<TierOrganItem>[] ORGAN_TIER_LUNGS = Utils.registerTierOrganItem(OrganType.LUNGS);
    public static final ItemEntry<TierOrganItem>[] ORGAN_TIER_HEART = Utils.registerTierOrganItem(OrganType.HEART);
    public static final ItemEntry<TierOrganItem>[] ORGAN_TIER_EYES = Utils.registerTierOrganItem(OrganType.EYES);
    public static final ItemEntry<TierOrganItem>[] ORGAN_TIER_RIGHT_ARM = Utils.registerTierOrganItem(OrganType.RIGHT_ARM);
    public static final ItemEntry<TierOrganItem>[] ORGAN_TIER_LEFT_ARM = Utils.registerTierOrganItem(OrganType.LEFT_ARM);
    public static final ItemEntry<TierOrganItem>[] ORGAN_TIER_RIGHT_LEG = Utils.registerTierOrganItem(OrganType.RIGHT_LEG);
    public static final ItemEntry<TierOrganItem>[] ORGAN_TIER_LEFT_LEG = Utils.registerTierOrganItem(OrganType.LEFT_LEG);
    public static final Map<OrganType, ItemEntry<TierOrganItem>[]> ORGAN_TIER_MAP = Map.of(
            OrganType.SPINE, ORGAN_TIER_SPINE,
            OrganType.LIVER, ORGAN_TIER_LIVER,
            OrganType.LUNGS, ORGAN_TIER_LUNGS,
            OrganType.HEART, ORGAN_TIER_HEART,
            OrganType.EYES, ORGAN_TIER_EYES,
            OrganType.RIGHT_ARM, ORGAN_TIER_RIGHT_ARM,
            OrganType.LEFT_ARM, ORGAN_TIER_LEFT_ARM,
            OrganType.RIGHT_LEG, ORGAN_TIER_RIGHT_LEG,
            OrganType.LEFT_LEG, ORGAN_TIER_LEFT_LEG);

    public static final ItemEntry<DurabilityWingOrganItem> ORGAN_FAIRY_WING = Utils.registerOrganItem("fairy_wing", "仙灵之翼", OrganType.WING, "item/organ/part/wing/fairy_wing", properties -> new DurabilityWingOrganItem(properties, OrganType.WING, 2, 3600 * 3));
    public static final ItemEntry<DurabilityWingOrganItem> ORGAN_FRAGILE_AND_CONTAMINATED_MANA_STEEL_WING = Utils.registerOrganItem("fragile_and_contaminated_mana_steel_wing", "易碎且被沾污的魔力钢翅膀", OrganType.WING, "item/organ/part/wing/mana_steel_wing", properties -> new DurabilityWingOrganItem(properties, OrganType.WING, 2, 600));
    public static final ItemEntry<MechanicalWingOrganItem> TRANSFORM_MECHANICAL_WING_TIER_1 = Utils.registerOrganItem(
            "mechanical_wing_tier_1",
            "机械翅膀 Tier 1",
            OrganType.WING,
            "item/organ/part/wing/mechanical_wing",
            properties -> new MechanicalWingOrganItem(properties, GTValues.EV),
            builder -> builder.onRegister(attach(new ElectricWingBehaviour(GTValues.V[GTValues.EV] * 3600 * 6 / 16, GTValues.EV))));
    public static final ItemEntry<MechanicalWingOrganItem> TRANSFORM_MECHANICAL_WING_TIER_2 = Utils.registerOrganItem("mechanical_wing_tier_2",
            "机械翅膀 Tier 2",
            OrganType.WING,
            "item/organ/part/wing/mechanical_wing",
            properties -> new MechanicalWingOrganItem(properties, GTValues.LuV),
            builder -> builder.onRegister(attach(new ElectricWingBehaviour(GTValues.V[GTValues.LuV] * 3600 * 36 / 16, GTValues.LuV))));

    public static final ItemEntry<ComponentItem> VISCERAL_EDITOR_WOOD = item("organ_visceral_editor_wood", "器官木制低精度内脏编辑器", properties -> ComponentItem.create(properties.stacksTo(1)))
            .onRegister(attach(new OrganEditorBehaviour(1)))
            .onRegister(attach(new TooltipBehavior(components -> {
                components.add(Component.translatable("gtocore.player.organ.can_modifier_your_body"));
                components.add(Component.translatable("gtocore.player.organ.precision_very_low"));
                components.add(Component.translatable("gtocore.player.organ.even_make_die"));
            })))
            .lang("Low Precision Wooden Visceral Editor")
            .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/organ/item/visceral_editor_wood")))
            .register();
    public static final ItemEntry<ComponentItem> VISCERAL_EDITOR = item("organ_visceral_editor", "器官高精度内脏编辑器", properties -> ComponentItem.create(properties.stacksTo(1)))
            .onRegister(attach(new OrganEditorBehaviour(2)))
            .onRegister(attach(new TooltipBehavior(components -> {
                components.add(Component.translatable("gtocore.player.organ.can_modifier_your_body"));
                components.add(Component.translatable("gtocore.player.organ.precision_very_high"));
            })))
            .lang("High Precision Visceral Editor")
            .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/organ/item/visceral_editor")))
            .register();

    private static final class Utils {

        private static final int NUMBER_TIER = 5;

        private static @NotNull <T extends OrganItem> ItemEntry<T> registerOrganItem(String name, String cn, OrganType organType, String eUrl, NonNullFunction<Item.Properties, T> itemFactory, Consumer<ItemBuilder<T, Registrate>> builderConsumer) {
            ItemBuilder<T, Registrate> part = item("organ_part_" + name.toLowerCase(), "器官部件 " + cn, itemFactory)
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id(eUrl)))
                    .tag(TagUtils.createTag(GTOCore.id("organ_part")));
            builderConsumer.accept(part);
            return part.register();
        }

        private static @NotNull <T extends OrganItem> ItemEntry<T> registerOrganItem(String name, String cn, OrganType organType, String eUrl, NonNullFunction<Item.Properties, T> itemFactory) {
            return registerOrganItem(name, cn, organType, eUrl, itemFactory, tRegistrateItemBuilder -> {});
        }

        private static ItemEntry<TierOrganItem>[] registerTierOrganItem(OrganType type) {
            @SuppressWarnings("unchecked")
            ItemEntry<TierOrganItem>[] result = new ItemEntry[NUMBER_TIER];
            for (int i = 0; i < NUMBER_TIER; i++) {
                String name = type.getId() + "_tier_" + i;
                String cn = type.getCn() + " (Tier " + i + ")";
                String eUrl = "item/organ/part/%s/%s".formatted(type.getId(), "tier" + i);
                int finalI = i;
                NonNullFunction<Item.Properties, TierOrganItem> factory = properties -> new TierOrganItem(properties, type, finalI, 2);
                result[i] = registerOrganItem(name, cn, type, eUrl, factory);
            }
            return result;
        }
    }
}
