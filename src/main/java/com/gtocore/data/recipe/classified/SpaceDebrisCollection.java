package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.recipe.condition.GalaxyCondition;

import com.gtolib.api.data.Galaxy;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.IntFunction;

import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.GTValues.ZPM;
import static com.gtocore.common.data.GTORecipeTypes.SPACE_DEBRIS_COLLECTION_RECIPES;

final class SpaceDebrisCollection {

    public static void init() {
        cosmicPile(Galaxy.SOLAR, 1, 120,
                output(GTOMaterials.CosmicDust, 2500, 500),
                output(GTOMaterials.FrozenVolatileIce, 2500, 500),
                output(GTOMaterials.AerogelPorousParticles, 2500, 500),
                output(GTOMaterials.MetalCompoundParticles, 2500, 500));
        cosmicPile(Galaxy.PROXIMA_CENTAURI, 2, 300,
                output(GTOMaterials.CosmicDust, 2300, 600),
                output(GTOMaterials.FrozenVolatileIce, 2100, 400),
                output(GTOMaterials.AerogelPorousParticles, 2800, 100),
                output(GTOMaterials.PlasmaQuenchedBeads, 1500, 300),
                output(GTOMaterials.DarkMatterBoundNodes, 1300, 200));
        cosmicPile(Galaxy.BARNARDA, 3, 500,
                output(GTOMaterials.CosmicDust, 1700, 300),
                output(GTOMaterials.FrozenVolatileIce, 2100, 700),
                output(GTOMaterials.AerogelPorousParticles, 1200, 100),
                output(GTOMaterials.PlasmaQuenchedBeads, 2500, 300),
                output(GTOMaterials.AlienAlgae, 1240, 600),
                output(GTOItems.NEUTRON_PILE.asItem(), 60, 10),
                output(GTOMaterials.DarkMatterBoundNodes, 800, 200),
                output(GTOMaterials.StellarMatterRemnantDust, 400, 100));
    }

    private static Item[] drones = null;
    private static final IntFunction<Object> chargeable = (tier) -> switch (tier) {
        case 2 -> CustomTags.EV_BATTERIES;
        case 3 -> CustomTags.IV_BATTERIES;
        case 4 -> CustomTags.LuV_BATTERIES;
        case 5 -> CustomTags.ZPM_BATTERIES;
        case 6 -> CustomTags.UV_BATTERIES;
        case 7 -> CustomTags.UHV_BATTERIES;
        case 8 -> GTOItems.REALLY_MAX_BATTERY;
        default -> null;
    };

    private static void cosmicPile(
                                   Galaxy galaxy,
                                   int circuitMeta,
                                   int time,
                                   CosmicPileOutput... outputs) {
        if (drones == null) {
            drones = new Item[] {
                    GTOItems.SMALL_SHUTTLE_MK1.asItem(),
                    GTOItems.SMALL_SHUTTLE_MK2.asItem(),
                    GTOItems.SPACE_DRONE_MK1.asItem(),
                    GTOItems.SPACE_DRONE_MK2.asItem(),
                    GTOItems.SPACE_DRONE_MK3.asItem(),
                    GTOItems.SPACE_DRONE_MK4.asItem(),
                    GTOItems.SPACE_DRONE_MK5.asItem(),
                    GTOItems.SPACE_DRONE_MK6.asItem()
            };
        }
        for (int i = 0; i < drones.length; i++) {
            var builder = SPACE_DEBRIS_COLLECTION_RECIPES.builder("cosmic_pile_" + galaxy.name().toLowerCase() + "_drone_mk" + i)
                    .EUt(VA[ZPM] / 2)
                    .duration(time * ((int) Math.pow(1.5, i)))
                    .addCondition(new GalaxyCondition(galaxy));
            if (i >= 2) {
                Object battery = chargeable.apply(i);
                if (battery instanceof TagKey<?> tag) {
                    builder.inputItems(Ingredient.of((TagKey<Item>) tag));
                } else if (battery instanceof ItemLike item) {
                    builder.inputItems(Ingredient.of(item));
                }
                builder.notConsumable(drones[i]);
            } else {
                builder.inputItems(drones[i]);
            }
            for (CosmicPileOutput output : outputs) {
                builder.chancedOutput(output.item.asItem(), output.baseMultiplier * (1 << i), output.chancePer100Milli, output.chanceBoostPer100Milli);
            }
            builder.circuitMeta(circuitMeta)
                    .save();
        }
    }

    private record CosmicPileOutput(
                                    ItemLike item,
                                    int baseMultiplier,
                                    int chancePer100Milli,
                                    int chanceBoostPer100Milli) {}

    private static CosmicPileOutput output(ItemLike item, int chancePer100Milli, int chanceBoostPer100Milli) {
        return new CosmicPileOutput(item, 1, chancePer100Milli, chanceBoostPer100Milli);
    }

    private static CosmicPileOutput output(Material material, int chancePer100Milli, int chanceBoostPer100Milli) {
        return new CosmicPileOutput(ChemicalHelper.get(TagPrefix.dust, material).getItem(), 1, chancePer100Milli, chanceBoostPer100Milli);
    }
}
