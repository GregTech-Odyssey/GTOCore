package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.mojang.datafixers.util.Pair;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.HIGH_SIFTER_OUTPUT;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DistilledWater;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Stone;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.COMPRESSOR_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.FORGE_HAMMER_RECIPES;
import static com.gto.gtocore.common.data.GTORecipeTypes.INTEGRATED_ORE_PROCESSOR;

public final class GTOOreRecipeHandler {

    private static boolean doesMaterialUseNormalFurnace(Material material) {
        return !material.hasProperty(PropertyKey.BLAST) && !material.hasFlag(MaterialFlags.NO_ORE_SMELTING);
    }

    public static void init(Consumer<FinishedRecipe> provider) {
        for (Map.Entry<TagPrefix, OreType> entry : ORES.entrySet()) {
            Supplier<Material> material = entry.getValue().material();
            if (material != null && material.get() == Stone) {
                entry.getKey().executeHandler(provider, PropertyKey.ORE, GTOOreRecipeHandler::processOre);
            }
        }

        rawOre.executeHandler(provider, PropertyKey.ORE, GTOOreRecipeHandler::processRawOre);

        crushed.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processCrushedOre);
        crushedPurified.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processCrushedPurified);
        crushedRefined.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processCrushedCentrifuged);
        dustImpure.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processDirtyDust);
        dustPure.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processPureDust);
    }

    private static void processOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider) {
        ItemStack crushedStack = ChemicalHelper.get(crushed, material);
        int oreMultiplier = property.getOreMultiplier();
        int oreTypeMultiplier = GTOConfig.INSTANCE.oreMultiplier;
        long mass = material.getMass();
        int dur = (int) Math.max(8, Math.sqrt(mass) * 2 * oreTypeMultiplier);
        crushedStack.setCount(crushedStack.getCount() * oreMultiplier);

        GTRecipeBuilder forge_builder = FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + material.getName() + "_ore_to_raw_ore")
                .inputItems(orePrefix.getItemTags(material)[0])
                .duration((dur << 2) / 5)
                .EUt(16);

        ItemStack outputStack = material.hasProperty(PropertyKey.GEM) && !gem.isIgnored(material) ?
                ChemicalHelper.get(gem, material, crushedStack.getCount()) : crushedStack;

        forge_builder.outputItems(GTUtil.copyAmount(oreMultiplier * oreTypeMultiplier, outputStack));
        forge_builder.save(provider);

        TagKey<Item> tag = orePrefix.getItemTags(material)[0];
        Material byproductMaterial = GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
        ItemStack byproductStack = ChemicalHelper.get(gem, byproductMaterial).isEmpty() ?
                ChemicalHelper.get(dust, byproductMaterial) : ChemicalHelper.get(gem, byproductMaterial);

        Material smeltingMaterial = property.getDirectSmeltResult() != null ? property.getDirectSmeltResult() : material;

        if (!crushedStack.isEmpty()) {
            GTRecipeBuilder builder = GTORecipeTypes.CRUSHER_RECIPES
                    .recipeBuilder(GTOCore.id("crusher_" + material.getName() + "_ore_to_raw_ore"))
                    .inputItems(tag)
                    .outputItems(GTUtil.copyAmount(oreMultiplier << 1, crushedStack))
                    .chancedOutput(byproductStack, 1400, 850)
                    .EUt(30)
                    .duration(dur);

            for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                    builder.chancedOutput(ChemicalHelper.getGem(secondaryMaterial), 6700, 800);
                }
            }

            builder.save(provider);

            int crushedAmount = oreMultiplier << 1;

            GTRecipeBuilder opBuilder1 = INTEGRATED_ORE_PROCESSOR
                    .recipeBuilder(GTOCore.id("integrated_ore_processor_1_" + material.getName()))
                    .circuitMeta(1)
                    .inputItems(tag)
                    .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                    .chancedOutput(byproductStack, 1400, 850)
                    .chancedOutput(ChemicalHelper.get(dust, byproductMaterial, property.getByProductMultiplier() * crushedAmount), 1400, 850)
                    .duration((int) (dur + (dur + (mass << 2)) * crushedAmount))
                    .EUt(30);

            for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                    opBuilder1.chancedOutput(ChemicalHelper.getGem(secondaryMaterial).copyWithCount(crushedAmount), 6700, 800);
                }
            }

            Material byproduct1 = GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
            if (byproduct1.hasProperty(PropertyKey.DUST)) {
                opBuilder1.chancedOutput(dust, byproduct1, crushedAmount, "1/9", 0);
            }
            opBuilder1.save(provider);

            // 2 破碎-洗矿-热离-研磨
            Material byproductMaterial1 = GTUtil.selectItemInList(1, material, property.getOreByProducts(), Material.class);
            ItemStack byproductStack2 = ChemicalHelper.get(dust, GTUtil.selectItemInList(2, material, property.getOreByProducts(), Material.class), crushedAmount);
            ItemStack crushedCentrifugedStack = ChemicalHelper.get(crushedRefined, material);

            if (!crushedCentrifugedStack.isEmpty()) {
                GTRecipeBuilder opBuilder2 = INTEGRATED_ORE_PROCESSOR
                        .recipeBuilder(GTOCore.id("integrated_ore_processor_2_" + material.getName()))
                        .circuitMeta(2)
                        .inputItems(tag)
                        .inputFluids(DistilledWater.getFluid(100 * crushedAmount))
                        .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                        .chancedOutput(byproductStack, 1400, 850)
                        .chancedOutput(dust, byproductMaterial, crushedAmount, "1/3", 0)
                        .outputItems(dust, Stone, crushedAmount)
                        .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/3", 0)
                        .chancedOutput(byproductStack2, 1400, 850)
                        .duration(dur + (200 + 200 + dur) * crushedAmount)
                        .EUt(30);
                for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                    if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                        opBuilder2.chancedOutput(ChemicalHelper.getGem(secondaryMaterial).copyWithCount(crushedAmount), 6700, 800);
                    }
                }
                opBuilder2.save(provider);
            }

            // 3 破碎-洗矿-研磨-离心
            ItemStack byproductStack1 = ChemicalHelper.get(dust, byproductMaterial1, crushedAmount);
            GTRecipeBuilder opBuilder3 = INTEGRATED_ORE_PROCESSOR
                    .recipeBuilder(GTOCore.id("integrated_ore_processor_3_" + material.getName()))
                    .circuitMeta(3)
                    .inputItems(tag)
                    .inputFluids(DistilledWater.getFluid(100 * crushedAmount))
                    .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                    .chancedOutput(byproductStack, 1400, 850)
                    .chancedOutput(dust, byproductMaterial, crushedAmount, "1/3", 0)
                    .outputItems(dust, Stone, crushedAmount)
                    .chancedOutput(byproductStack1, 1400, 850)
                    .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                    .duration(dur + (200 + dur + 16) * crushedAmount)
                    .EUt(30);
            for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                    opBuilder3.chancedOutput(ChemicalHelper.getGem(secondaryMaterial).copyWithCount(crushedAmount), 6700, 800);
                }
            }
            opBuilder3.save(provider);

            // 4 破碎-洗矿-筛选-离心
            if (material.hasProperty(PropertyKey.GEM)) {
                ItemStack exquisiteStack = ChemicalHelper.get(gemExquisite, material);
                ItemStack flawlessStack = ChemicalHelper.get(gemFlawless, material);
                ItemStack gemStack = ChemicalHelper.get(gem, material);
                if (material.hasFlag(HIGH_SIFTER_OUTPUT)) {
                    GTRecipeBuilder opBuilder4 = INTEGRATED_ORE_PROCESSOR
                            .recipeBuilder(GTOCore.id("integrated_ore_processor_4_" + material.getName()))
                            .circuitMeta(4)
                            .inputItems(tag)
                            .inputFluids(DistilledWater.getFluid(100 * crushedAmount))
                            .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                            .chancedOutput(byproductStack, 1400, 850)
                            .chancedOutput(dust, byproductMaterial, crushedAmount, "1/3", 0)
                            .outputItems(dust, Stone, crushedAmount)
                            .chancedOutput(exquisiteStack, 500, 150)
                            .chancedOutput(flawlessStack, 1500, 200)
                            .chancedOutput(gemStack, 5000, 1000)
                            .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                            .duration(dur + (200 + 210 + 16) * crushedAmount)
                            .EUt(30);
                    for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                        if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                            opBuilder4.chancedOutput(ChemicalHelper.getGem(secondaryMaterial).copyWithCount(crushedAmount), 6700, 800);
                        }
                    }
                    opBuilder4.save(provider);
                } else {
                    GTRecipeBuilder opBuilder4 = INTEGRATED_ORE_PROCESSOR
                            .recipeBuilder(GTOCore.id("integrated_ore_processor_4_" + material.getName()))
                            .circuitMeta(4)
                            .inputItems(tag)
                            .inputFluids(DistilledWater.getFluid(100 * crushedAmount))
                            .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                            .chancedOutput(byproductStack, 1400, 850)
                            .chancedOutput(dust, byproductMaterial, crushedAmount, "1/3", 0)
                            .outputItems(dust, Stone, crushedAmount)
                            .chancedOutput(exquisiteStack, 300, 100)
                            .chancedOutput(flawlessStack, 1000, 150)
                            .chancedOutput(gemStack, 3500, 500)
                            .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                            .duration(dur + (200 + 210 + 16) * crushedAmount)
                            .EUt(30);
                    for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                        if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                            opBuilder4.chancedOutput(ChemicalHelper.getGem(secondaryMaterial).copyWithCount(crushedAmount), 6700, 800);
                        }
                    }
                    opBuilder4.save(provider);
                }
            }

            if (property.getWashedIn().getFirst() != null) {
                Material washingByproduct = GTUtil.selectItemInList(3, material, property.getOreByProducts(), Material.class);
                Pair<Material, Integer> washedInTuple = property.getWashedIn();
                // 5 破碎-浸洗-热离-研磨
                if (!crushedCentrifugedStack.isEmpty()) {
                    GTRecipeBuilder opBuilder5 = INTEGRATED_ORE_PROCESSOR
                            .recipeBuilder(GTOCore.id("integrated_ore_processor_5_" + material.getName()))
                            .circuitMeta(5)
                            .inputItems(tag)
                            .inputFluids(washedInTuple.getFirst().getFluid(washedInTuple.getSecond() * crushedAmount))
                            .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                            .chancedOutput(byproductStack, 1400, 850)
                            .chancedOutput(ChemicalHelper.get(dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                            .chancedOutput(ChemicalHelper.get(dust, Stone, crushedAmount), 4000, 650)
                            .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/3", 0)
                            .chancedOutput(byproductStack2, 1400, 850)
                            .duration(dur + (200 + 200 + dur) * crushedAmount)
                            .EUt(30);
                    for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                        if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                            opBuilder5.chancedOutput(ChemicalHelper.getGem(secondaryMaterial).copyWithCount(crushedAmount), 6700, 800);
                        }
                    }
                    opBuilder5.save(provider);
                }

                // 6 破碎-浸洗-研磨-离心
                GTRecipeBuilder opBuilder6 = INTEGRATED_ORE_PROCESSOR
                        .recipeBuilder(GTOCore.id("integrated_ore_processor_6_" + material.getName()))
                        .circuitMeta(6)
                        .inputItems(tag)
                        .inputFluids(washedInTuple.getFirst().getFluid(washedInTuple.getSecond() * crushedAmount))
                        .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                        .chancedOutput(byproductStack, 1400, 850)
                        .chancedOutput(ChemicalHelper.get(dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                        .chancedOutput(ChemicalHelper.get(dust, Stone, crushedAmount), 4000, 650)
                        .chancedOutput(byproductStack1, 1400, 850)
                        .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                        .duration(dur + (200 + dur + 16) * crushedAmount)
                        .EUt(30);
                for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                    if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                        opBuilder6.chancedOutput(ChemicalHelper.getGem(secondaryMaterial).copyWithCount(crushedAmount), 6700, 800);
                    }
                }
                opBuilder6.save(provider);

                // 7 破碎-浸洗-筛选-离心
                if (material.hasProperty(PropertyKey.GEM)) {
                    ItemStack exquisiteStack = ChemicalHelper.get(gemExquisite, material);
                    ItemStack flawlessStack = ChemicalHelper.get(gemFlawless, material);
                    ItemStack gemStack = ChemicalHelper.get(gem, material);
                    if (material.hasFlag(HIGH_SIFTER_OUTPUT)) {
                        GTRecipeBuilder opBuilder7 = INTEGRATED_ORE_PROCESSOR
                                .recipeBuilder(GTOCore.id("integrated_ore_processor_7_" + material.getName()))
                                .circuitMeta(7)
                                .inputItems(tag)
                                .inputFluids(washedInTuple.getFirst().getFluid(washedInTuple.getSecond() * crushedAmount))
                                .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                                .chancedOutput(byproductStack, 1400, 850)
                                .chancedOutput(ChemicalHelper.get(dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                                .chancedOutput(ChemicalHelper.get(dust, Stone, crushedAmount), 4000, 650)
                                .chancedOutput(exquisiteStack, 500, 150)
                                .chancedOutput(flawlessStack, 1500, 200)
                                .chancedOutput(gemStack, 5000, 1000)
                                .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                                .duration(dur + (200 + 210 + 16) * crushedAmount)
                                .EUt(30);
                        for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                            if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                                opBuilder7.chancedOutput(ChemicalHelper.getGem(secondaryMaterial).copyWithCount(crushedAmount), 6700, 800);
                            }
                        }
                        opBuilder7.save(provider);
                    } else {
                        GTRecipeBuilder opBuilder7 = INTEGRATED_ORE_PROCESSOR
                                .recipeBuilder(GTOCore.id("integrated_ore_processor_7_" + material.getName()))
                                .circuitMeta(7)
                                .inputItems(tag)
                                .inputFluids(washedInTuple.getFirst().getFluid(washedInTuple.getSecond() * crushedAmount))
                                .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                                .chancedOutput(byproductStack, 1400, 850)
                                .chancedOutput(ChemicalHelper.get(dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                                .chancedOutput(ChemicalHelper.get(dust, Stone, crushedAmount), 4000, 650)
                                .chancedOutput(exquisiteStack, 300, 100)
                                .chancedOutput(flawlessStack, 1000, 150)
                                .chancedOutput(gemStack, 3500, 500)
                                .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                                .duration(dur + (200 + 210 + 16) * crushedAmount)
                                .EUt(30);
                        for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                            if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                                opBuilder7.chancedOutput(ChemicalHelper.getGem(secondaryMaterial).copyWithCount(crushedAmount), 6700, 800);
                            }
                        }
                        opBuilder7.save(provider);
                    }
                }
            }

            ItemStack ingotStack = material.hasProperty(PropertyKey.INGOT) ? ChemicalHelper.get(ingot, smeltingMaterial) :
                    material.hasProperty(PropertyKey.GEM) ? ChemicalHelper.get(gem, smeltingMaterial) :
                            ChemicalHelper.get(dust, smeltingMaterial);

            ingotStack.setCount(ingotStack.getCount() * oreMultiplier * oreTypeMultiplier);

            if (!ingotStack.isEmpty() && doesMaterialUseNormalFurnace(smeltingMaterial) && !orePrefix.isIgnored(material)) {
                float xp = Math.round(((1 + oreTypeMultiplier * 0.5f) * 0.5f - 0.05f) * 10.0f) / 10.0f;
                VanillaRecipeHelper.addSmeltingRecipe(provider, "smelt_" + material.getName() + "_ore_to_ingot", tag, ingotStack, xp);
                VanillaRecipeHelper.addBlastingRecipe(provider, "smelt_" + material.getName() + "_ore_to_ingot", tag, ingotStack, xp);
            }
        }
    }

    private static void processRawOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider) {
        int oreTypeMultiplier = GTOConfig.INSTANCE.oreMultiplier;
        long mass = material.getMass();
        int dur = (int) Math.max(6, Math.sqrt(mass) * oreTypeMultiplier * 2 / 3);
        ItemStack crushedStack = ChemicalHelper.get(crushed, material, material.getProperty(PropertyKey.ORE).getOreMultiplier() * oreTypeMultiplier / 2);
        Material smeltingMaterial = property.getDirectSmeltResult() == null ? material : property.getDirectSmeltResult();
        ItemStack ingotStack = material.hasProperty(PropertyKey.INGOT) ? ChemicalHelper.get(ingot, smeltingMaterial, material.getProperty(PropertyKey.ORE).getOreMultiplier()) :
                material.hasProperty(PropertyKey.GEM) ? ChemicalHelper.get(gem, smeltingMaterial, material.getProperty(PropertyKey.ORE).getOreMultiplier()) :
                        ChemicalHelper.get(dust, smeltingMaterial, material.getProperty(PropertyKey.ORE).getOreMultiplier());

        if (crushedStack.isEmpty()) return;

        GTRecipeBuilder builder = FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + orePrefix.name + "_" + material.getName() + "_to_crushed_ore")
                .inputItems(orePrefix, material)
                .duration((dur << 2) / 5).EUt(16);

        builder.outputItems(material.hasProperty(PropertyKey.GEM) && !gem.isIgnored(material) ? ChemicalHelper.get(gem, material, crushedStack.getCount()) : crushedStack);
        builder.save(provider);

        GTRecipeBuilder builder2 = GTORecipeTypes.CRUSHER_RECIPES.recipeBuilder("crusher_" + orePrefix.name + "_" + material.getName() + "_ore_to_crushed_ore")
                .inputItems(orePrefix, material)
                .outputItems(GTUtil.copyAmount(crushedStack.getCount() << 1, crushedStack))
                .EUt(30).duration(dur);

        Material byproductMaterial = GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
        ItemStack byproductStack = ChemicalHelper.get(gem, byproductMaterial);
        if (byproductStack.isEmpty()) byproductStack = ChemicalHelper.get(dust, byproductMaterial);
        builder2.chancedOutput(byproductStack, 1000, 300);

        for (MaterialStack secondaryMaterial : ore.secondaryMaterials()) {
            if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial);
                builder2.chancedOutput(dustStack, 500, 100);
                break;
            }
        }
        builder2.save(provider);

        int crushedAmount = crushedStack.getCount() << 1;

        // 1 破碎-研磨-离心
        GTRecipeBuilder opBuilder1 = INTEGRATED_ORE_PROCESSOR.recipeBuilder(GTOCore.id("raw_integrated_ore_processor_1_" + material.getName()))
                .circuitMeta(1)
                .inputItems(orePrefix, material)
                .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                .chancedOutput(byproductStack, 1000, 300)
                .duration((int) (dur + (dur + (mass << 2)) * crushedAmount))
                .EUt(30);

        Material byproduct1 = GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
        if (byproduct1.hasProperty(PropertyKey.DUST)) {
            opBuilder1.chancedOutput(dust, byproduct1, crushedAmount, "1/9", 0);
        }
        opBuilder1.save(provider);

        // 2 破碎-洗矿-热离-研磨
        Material byproductMaterial1 = GTUtil.selectItemInList(1, material, property.getOreByProducts(), Material.class);
        ItemStack byproductStack2 = ChemicalHelper.get(dust, GTUtil.selectItemInList(2, material, property.getOreByProducts(), Material.class), crushedAmount);
        ItemStack crushedCentrifugedStack = ChemicalHelper.get(crushedRefined, material);
        if (!crushedCentrifugedStack.isEmpty()) {
            GTRecipeBuilder opBuilder2 = INTEGRATED_ORE_PROCESSOR.recipeBuilder(GTOCore.id("raw_integrated_ore_processor_2_" + material.getName()))
                    .circuitMeta(2)
                    .inputItems(orePrefix, material)
                    .inputFluids(DistilledWater.getFluid(100 * crushedAmount))
                    .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                    .chancedOutput(byproductStack, 1000, 300)
                    .chancedOutput(dust, byproductMaterial, crushedAmount, "1/3", 0)
                    .outputItems(dust, Stone, crushedAmount)
                    .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/3", 0)
                    .chancedOutput(byproductStack2, 1400, 850)
                    .duration(dur + (200 + 200 + dur) * crushedAmount)
                    .EUt(30);
            opBuilder2.save(provider);
        }

        // 3 破碎-洗矿-研磨-离心
        ItemStack byproductStack1 = ChemicalHelper.get(dust, byproductMaterial1, crushedAmount);
        GTRecipeBuilder opBuilder3 = INTEGRATED_ORE_PROCESSOR.recipeBuilder(GTOCore.id("raw_integrated_ore_processor_3_" + material.getName()))
                .circuitMeta(3)
                .inputItems(orePrefix, material)
                .inputFluids(DistilledWater.getFluid(100 * crushedAmount))
                .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                .chancedOutput(byproductStack, 1000, 300)
                .chancedOutput(dust, byproductMaterial, crushedAmount, "1/3", 0)
                .outputItems(dust, Stone, crushedAmount)
                .chancedOutput(byproductStack1, 1400, 850)
                .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                .duration(dur + (200 + dur + 16) * crushedAmount)
                .EUt(30);

        for (MaterialStack secondaryMaterial : ore.secondaryMaterials()) {
            if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial);
                opBuilder3.chancedOutput(dustStack, 500, 100);
                break;
            }
        }
        opBuilder3.save(provider);

        // 4 破碎-洗矿-筛选-离心
        if (material.hasProperty(PropertyKey.GEM)) {
            ItemStack exquisiteStack = ChemicalHelper.get(gemExquisite, material);
            ItemStack flawlessStack = ChemicalHelper.get(gemFlawless, material);
            ItemStack gemStack = ChemicalHelper.get(gem, material);
            GTRecipeBuilder opBuilder4 = INTEGRATED_ORE_PROCESSOR.recipeBuilder(GTOCore.id("raw_integrated_ore_processor_4_" + material.getName()))
                    .circuitMeta(4)
                    .inputItems(orePrefix, material)
                    .inputFluids(DistilledWater.getFluid(100 * crushedAmount))
                    .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                    .chancedOutput(byproductStack, 1000, 300)
                    .chancedOutput(dust, byproductMaterial, crushedAmount, "1/3", 0)
                    .outputItems(dust, Stone, crushedAmount)
                    .chancedOutput(exquisiteStack, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 500 : 300, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 150 : 100)
                    .chancedOutput(flawlessStack, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 1500 : 1000, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 200 : 150)
                    .chancedOutput(gemStack, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 5000 : 3500, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 1000 : 500)
                    .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                    .duration(dur + (200 + 210 + 16) * crushedAmount)
                    .EUt(30);
            opBuilder4.save(provider);
        }

        if (property.getWashedIn().getFirst() != null) {
            Material washingByproduct = GTUtil.selectItemInList(3, material, property.getOreByProducts(), Material.class);
            Pair<Material, Integer> washedInTuple = property.getWashedIn();

            // 5 破碎-浸洗-热离-研磨
            if (!crushedCentrifugedStack.isEmpty()) {
                GTRecipeBuilder opBuilder5 = INTEGRATED_ORE_PROCESSOR.recipeBuilder(GTOCore.id("raw_integrated_ore_processor_5_" + material.getName()))
                        .circuitMeta(5)
                        .inputItems(orePrefix, material)
                        .inputFluids(washedInTuple.getFirst().getFluid(washedInTuple.getSecond() * crushedAmount))
                        .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                        .chancedOutput(byproductStack, 1000, 300)
                        .chancedOutput(ChemicalHelper.get(dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                        .chancedOutput(ChemicalHelper.get(dust, Stone, crushedAmount), 4000, 650)
                        .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/3", 0)
                        .chancedOutput(byproductStack2, 1400, 850)
                        .duration(dur + (200 + 200 + dur) * crushedAmount)
                        .EUt(30);
                opBuilder5.save(provider);
            }

            // 6 破碎-浸洗-研磨-离心
            GTRecipeBuilder opBuilder6 = INTEGRATED_ORE_PROCESSOR.recipeBuilder(GTOCore.id("raw_integrated_ore_processor_6_" + material.getName()))
                    .circuitMeta(6)
                    .inputItems(orePrefix, material)
                    .inputFluids(washedInTuple.getFirst().getFluid(washedInTuple.getSecond() * crushedAmount))
                    .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                    .chancedOutput(byproductStack, 1000, 300)
                    .chancedOutput(ChemicalHelper.get(dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                    .chancedOutput(ChemicalHelper.get(dust, Stone, crushedAmount), 4000, 650)
                    .chancedOutput(byproductStack1, 1400, 850)
                    .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                    .duration(dur + (200 + dur + 16) * crushedAmount)
                    .EUt(30);
            opBuilder6.save(provider);

            // 7 破碎-浸洗-筛选-离心
            if (material.hasProperty(PropertyKey.GEM)) {
                ItemStack exquisiteStack = ChemicalHelper.get(gemExquisite, material);
                ItemStack flawlessStack = ChemicalHelper.get(gemFlawless, material);
                ItemStack gemStack = ChemicalHelper.get(gem, material);
                GTRecipeBuilder opBuilder7 = INTEGRATED_ORE_PROCESSOR.recipeBuilder(GTOCore.id("raw_integrated_ore_processor_7_" + material.getName()))
                        .circuitMeta(7)
                        .inputItems(orePrefix, material)
                        .inputFluids(washedInTuple.getFirst().getFluid(washedInTuple.getSecond() * crushedAmount))
                        .outputItems(ChemicalHelper.get(dust, material, crushedAmount))
                        .chancedOutput(byproductStack, 1000, 300)
                        .chancedOutput(ChemicalHelper.get(dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                        .chancedOutput(ChemicalHelper.get(dust, Stone, crushedAmount), 4000, 650)
                        .chancedOutput(exquisiteStack, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 500 : 300, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 150 : 100)
                        .chancedOutput(flawlessStack, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 1500 : 1000, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 200 : 150)
                        .chancedOutput(gemStack, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 5000 : 3500, material.hasFlag(HIGH_SIFTER_OUTPUT) ? 1000 : 500)
                        .chancedOutput(dust, byproductMaterial1, crushedAmount, "1/9", 0)
                        .duration(dur + (200 + 210 + 16) * crushedAmount)
                        .EUt(30);
                opBuilder7.save(provider);
            }
        }

        if (!ingotStack.isEmpty() && doesMaterialUseNormalFurnace(smeltingMaterial) && !orePrefix.isIgnored(material)) {
            float xp = Math.round(((1 + property.getOreMultiplier() * 0.33f) / 3) * 10.0f) / 10.0f;
            VanillaRecipeHelper.addSmeltingRecipe(provider, "smelt_" + orePrefix.name + "_" + material.getName() + "_ore_to_ingot", ChemicalHelper.getTag(orePrefix, material), GTUtil.copyAmount(ingotStack.getCount(), ingotStack), xp);
            VanillaRecipeHelper.addBlastingRecipe(provider, "smelt_" + orePrefix.name + "_" + material.getName() + "_ore_to_ingot", ChemicalHelper.getTag(orePrefix, material), GTUtil.copyAmount(ingotStack.getCount(), ingotStack), xp);
        }

        COMPRESSOR_RECIPES.recipeBuilder("compress_" + material.getName() + "to_ore_block")
                .inputItems(rawOre, material, 9)
                .outputItems(rawOreBlock, material)
                .duration(300).EUt(2).save(provider);

        FORGE_HAMMER_RECIPES.recipeBuilder("decompress_" + material.getName() + "to_raw_ore")
                .inputItems(rawOreBlock, material)
                .outputItems(rawOre, material, 9)
                .duration(300).EUt(2).save(provider);
    }
}
