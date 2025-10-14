package com.gtocore.data.recipe.generated;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOFluidStorageKey;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeCategories;

import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.*;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.material.GTOMaterialFlags.*;
import static com.gtocore.api.data.tag.GTOTagPrefix.FIBER_MESH;
import static com.gtocore.common.data.GTOMaterials.Kevlar;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtocore.data.recipe.processing.CompositeMaterialsProcessing.getFiberExtrusionTemperature;

final class GTOMaterialRecipeHandler {

    private static final List<TagPrefix> GEM_ORDER = Arrays.asList(gem, gemFlawless, gemExquisite);

    static void run(Material material) {
        if (GTOUtils.isGeneration(ingot, material)) {
            processIngot(material);
            processNugget(material);
        }

        if (GTOUtils.isGeneration(block, material)) {
            processBlock(material);
        }
        if (GTOUtils.isGeneration(frameGt, material)) {
            processFrame(material);
        }

        processDust(material);
        if (GTOUtils.isGeneration(dustSmall, material)) {
            processSmallDust(material);
        }
        if (GTOUtils.isGeneration(dustTiny, material)) {
            processTinyDust(material);
        }

        if (material.shouldGenerateRecipesFor(gemExquisite)) {
            for (TagPrefix orePrefix : Arrays.asList(gem, gemFlawless, gemExquisite)) {
                processGemConversion(orePrefix, material);
            }
        }
    }

    private static void processIngot(Material material) {
        ItemStack stack = ChemicalHelper.get(ingot, material);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        if (material.hasFlag(MORTAR_GRINDABLE)) {
            VanillaRecipeHelper.addShapedRecipe(String.format("mortar_grind_%s", material.getName()),
                    ChemicalHelper.get(dust, material), "X", "m", 'X', stack);
        }

        if (material.hasFlag(GENERATE_ROD)) {
            if (mass < 240 && material.getBlastTemperature() < 3600)
                VanillaRecipeHelper.addShapedRecipe(String.format("stick_%s", material.getName()),
                        ChemicalHelper.get(rod, material),
                        "f ", " X",
                        'X', stack);

            if (!material.hasFlag(NO_WORKING)) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_to_rod")
                        .inputItems(stack)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_ROD)
                        .outputItems(rod, material, 2)
                        .duration(mass << 1)
                        .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                        .save();
            }
        }

        if (material.hasFluid()) {
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_to_ingot")
                    .notConsumable(GTItems.SHAPE_MOLD_INGOT)
                    .inputFluids(material.getFluid(L))
                    .outputItems(stack)
                    .duration(mass).EUt(VA[ULV])
                    .save();
        }

        if (material.hasAnyOfFlags(NO_SMASHING, COMPOSITE_MATERIAL)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_to_ingot")
                    .inputItems(dust, material)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_INGOT)
                    .outputItems(stack)
                    .duration(mass)
                    .EUt(4L * GTOUtils.getVoltageMultiplier(material))
                    .save();
        }

        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_smelt_" + material.getName() + "_to_nugget")
                .EUt(VA[ULV]).duration(mass)
                .inputItems(stack)
                .notConsumable(GTItems.SHAPE_MOLD_NUGGET)
                .outputItems(nugget, material, 9)
                .category(GTRecipeCategories.INGOT_MOLDING)
                .save();

        if (!ChemicalHelper.get(block, material).isEmpty()) {
            int amount = (int) (block.getMaterialAmount(material) / M);
            ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_smelt_" + material.getName() + "_to_ingot")
                    .EUt(16).duration(Math.max(1, mass * amount / 4))
                    .inputItems(block, material)
                    .notConsumable(GTItems.SHAPE_MOLD_INGOT)
                    .outputItems(stack.copyWithCount(amount))
                    .category(GTRecipeCategories.INGOT_MOLDING)
                    .save();

            COMPRESSOR_RECIPES.recipeBuilder("compress_" + material.getName() + "_to_block")
                    .EUt(VA[ULV]).duration(Math.max(1, mass * amount / 2))
                    .inputItems(stack.copyWithCount(amount))
                    .outputItems(block, material)
                    .save();
        }

        if (GTOUtils.isGeneration(ingotHot, material) && material.hasFlag(CAN_BE_COOLED_DOWN_BY_BATHING)) {
            CHEMICAL_BATH_RECIPES.builder("%s_cool_down".formatted(material))
                    .inputItems(ingotHot, material)
                    .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                    .outputItems(ingot, material)
                    .duration(mass * 5).EUt(VA[MV]).save();
        }

        if (material.hasFlag(GENERATE_PLATE) && !material.hasFlag(NO_WORKING)) {

            if (!material.hasFlag(NO_SMASHING)) {
                ItemStack plateStack = ChemicalHelper.get(plate, material);
                if (!plateStack.isEmpty()) {
                    ROLLING_RECIPES.recipeBuilder("rolling_" + material.getName() + "_to_plate")
                            .inputItems(stack)
                            .outputItems(plateStack)
                            .EUt(30).duration(Math.max(1, mass / 2))
                            .circuitMeta(1)
                            .save();

                    FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + material.getName() + "_to_plate")
                            .inputItems(stack.copyWithCount(3))
                            .outputItems(plateStack.copyWithCount(2))
                            .EUt(16).duration(Math.max(1, mass / 2))
                            .save();
                    if (mass < 240 && material.getBlastTemperature() < 3600) {
                        VanillaRecipeHelper.addShapedRecipe(String.format("plate_%s", material.getName()),
                                plateStack, "h", "I", "I", 'I', stack);

                    }
                }
            }

            int voltageMultiplier = GTOUtils.getVoltageMultiplier(material) << 3;
            if (!ChemicalHelper.get(plate, material).isEmpty()) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_to_plate")
                        .inputItems(stack)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_PLATE)
                        .outputItems(plate, material)
                        .duration(mass << 1)
                        .EUt(voltageMultiplier)
                        .save();

                if (material.hasFlag(NO_SMASHING)) {
                    EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_plate")
                            .inputItems(dust, material)
                            .notConsumable(GTItems.SHAPE_EXTRUDER_PLATE)
                            .outputItems(plate, material)
                            .duration(mass)
                            .EUt(voltageMultiplier)
                            .save();
                }
            }
        }
    }

    private static void processNugget(Material material) {
        ItemStack nuggetStack = ChemicalHelper.get(nugget, material, 9);
        if (nuggetStack.isEmpty()) return;
        if (material.hasProperty(PropertyKey.INGOT)) {
            ItemStack ingotStack = ChemicalHelper.get(ingot, material.hasFlag(IS_MAGNETIC) ? material.getProperty(PropertyKey.INGOT).getMacerateInto() : material);

            if (!ConfigHolder.INSTANCE.recipes.disableManualCompression) {
                if (!ingot.isIgnored(material)) {
                    VanillaRecipeHelper.addShapelessRecipe(
                            String.format("nugget_disassembling_%s", material.getName()),
                            nuggetStack.copyWithCount(9), new MaterialEntry(ingot, material));
                }
                if (!nugget.isIgnored(material)) {
                    VanillaRecipeHelper.addShapedRecipe(
                            String.format("nugget_assembling_%s", material.getName()),
                            ingotStack, "XXX", "XXX", "XXX", 'X', new MaterialEntry(nugget, material));
                }
            }

            COMPRESSOR_RECIPES.recipeBuilder("compress_" + material.getName() + "_nugget_to_ingot")
                    .inputItems(nuggetStack)
                    .outputItems(ingotStack)
                    .EUt(VA[ULV]).duration((int) material.getMass()).save();

            ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_smelt_" + material.getName() + "_nugget_to_ingot")
                    .EUt(VA[ULV]).duration((int) material.getMass())
                    .inputItems(nuggetStack)
                    .notConsumable(GTItems.SHAPE_MOLD_INGOT)
                    .outputItems(ingotStack)
                    .category(GTRecipeCategories.INGOT_MOLDING)
                    .save();

            if (material.hasFluid()) {
                FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_to_nugget")
                        .notConsumable(GTItems.SHAPE_MOLD_NUGGET)
                        .inputFluids(material.getFluid(L))
                        .outputItems(nuggetStack)
                        .duration((int) material.getMass())
                        .EUt(VA[ULV])
                        .save();
            }
        } else if (material.hasProperty(PropertyKey.GEM)) {
            ItemStack gemStack = ChemicalHelper.get(gem, material);

            if (!ConfigHolder.INSTANCE.recipes.disableManualCompression) {
                if (!gem.isIgnored(material)) {
                    VanillaRecipeHelper.addShapelessRecipe(
                            String.format("nugget_disassembling_%s", material.getName()),
                            nuggetStack.copyWithCount(9), new MaterialEntry(gem, material));
                }
                if (!nugget.isIgnored(material)) {
                    VanillaRecipeHelper.addShapedRecipe(
                            String.format("nugget_assembling_%s", material.getName()),
                            gemStack, "XXX", "XXX", "XXX", 'X', new MaterialEntry(nugget, material));
                }
            }
        }
    }

    private static void processBlock(Material material) {
        ItemStack blockStack = ChemicalHelper.get(TagPrefix.block, material);
        if (blockStack.isEmpty()) return;
        int mass = (int) material.getMass();
        int amount = (int) (block.getMaterialAmount(material) / M);
        if (material.hasFluid()) {
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_block")
                    .notConsumable(GTItems.SHAPE_MOLD_BLOCK)
                    .inputFluids(material.getFluid(amount * L))
                    .outputItems(blockStack)
                    .duration(mass * amount).EUt(VA[ULV])
                    .save();
        }

        if (material.hasAnyOfFlags(NO_SMASHING, COMPOSITE_MATERIAL) && material.hasFlag(GENERATE_PLATE)) {
            ItemStack plateStack = ChemicalHelper.get(plate, material);
            if (!plateStack.isEmpty()) {
                CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_block_to_plate")
                        .inputItems(blockStack)
                        .outputItems(plateStack.copyWithCount(amount))
                        .duration(mass << 3).EUt(VA[LV])
                        .save();
            }
        }

        if (!material.hasFlag(EXCLUDE_BLOCK_CRAFTING_RECIPES)) {
            if (!ConfigHolder.INSTANCE.recipes.disableManualCompression) {
                int size = (int) (block.getMaterialAmount(material) / M);
                int sizeSqrt = Math.round(Mth.sqrt(size));

                if (!material.hasFlag(EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES) && sizeSqrt * sizeSqrt == size && !block.isIgnored(material)) {
                    String patternString = "B".repeat(Math.max(0, sizeSqrt));
                    String[] pattern = new String[sizeSqrt];
                    Arrays.fill(pattern, patternString);
                    MaterialEntry blockEntry;
                    if (material.hasProperty(PropertyKey.GEM)) {
                        blockEntry = new MaterialEntry(gem, material);
                    } else if (material.hasProperty(PropertyKey.INGOT)) {
                        blockEntry = new MaterialEntry(ingot, material);
                    } else {
                        blockEntry = new MaterialEntry(dust, material);
                    }

                    VanillaRecipeHelper.addShapedRecipe(String.format("block_compress_%s", material.getName()),
                            blockStack, pattern, 'B', blockEntry);

                    VanillaRecipeHelper.addShapelessRecipe(
                            String.format("block_decompress_%s", material.getName()),
                            ChemicalHelper.get(blockEntry.tagPrefix(), blockEntry.material()).copyWithCount(size),
                            new MaterialEntry(block, material));
                }
            }

            if (material.hasProperty(PropertyKey.INGOT)) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_block")
                        .inputItems(ingot, material, amount)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_BLOCK)
                        .outputItems(blockStack)
                        .duration(mass << 1).EUt(8L * GTOUtils.getVoltageMultiplier(material))
                        .save();

            } else if (material.hasProperty(PropertyKey.GEM)) {
                COMPRESSOR_RECIPES.recipeBuilder("compress_" + material.getName() + "_gem_to_block")
                        .inputItems(gem, material, amount)
                        .outputItems(blockStack)
                        .duration(Math.max(1, mass * amount / 4)).EUt(VA[ULV]).save();

                FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + material.getName() + "_block_to_gem")
                        .inputItems(blockStack)
                        .outputItems(gem, material, amount)
                        .duration(100).EUt(24).save();
            }
        }
    }

    private static void processFrame(Material material) {
        ItemStack stack = ChemicalHelper.get(frameGt, material);
        if (stack.isEmpty()) return;
        boolean isWoodenFrame = material.hasProperty(PropertyKey.WOOD);
        if (material.getMass() < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("frame_%s", material.getName()),
                    stack.copyWithCount(2), "SSS", isWoodenFrame ? "SsS" : "SwS", "SSS",
                    'S', new MaterialEntry(rod, material));

        ASSEMBLER_RECIPES.recipeBuilder("assemble_" + material.getName() + "_frame")
                .inputItems(rod, material, 4)
                .circuitMeta(4)
                .outputItems(stack)
                .EUt(VA[ULV]).duration(64)
                .save();
    }

    private static void processGemConversion(TagPrefix gemPrefix, Material material) {
        if (material.hasFlag(MORTAR_GRINDABLE)) {
            VanillaRecipeHelper.addShapedRecipe(String.format("gem_to_dust_%s_%s", material.getName(), FormattingUtil.toLowerCaseUnderscore(gemPrefix.name)), ChemicalHelper.getDust(material, gemPrefix.getMaterialAmount(material)), "X", "m", 'X', new MaterialEntry(gemPrefix, material));
        }

        TagPrefix prevPrefix = GTUtil.getItem(GEM_ORDER, GEM_ORDER.indexOf(gemPrefix) - 1, null);
        ItemStack prevStack = prevPrefix == null ? ItemStack.EMPTY : ChemicalHelper.get(prevPrefix, material, 2);
        if (!prevStack.isEmpty() && prevPrefix != null) {
            CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_" + FormattingUtil.toLowerCaseUnderscore(gemPrefix.name) + "_to_" + FormattingUtil.toLowerCaseUnderscore(prevPrefix.name))
                    .inputItems(gemPrefix, material)
                    .outputItems(prevStack)
                    .duration(20)
                    .EUt(16)
                    .save();

            LASER_ENGRAVER_RECIPES.recipeBuilder("engrave_" + material.getName() + "_" + FormattingUtil.toLowerCaseUnderscore(gemPrefix.name) + "_to_" + FormattingUtil.toLowerCaseUnderscore(prevPrefix.name))
                    .inputItems(prevStack)
                    .notConsumable(lens, MarkerMaterials.Color.White)
                    .inputFluids(DistilledWater.getFluid(10))
                    .outputItems(gemPrefix, material)
                    .duration(300)
                    .EUt(240)
                    .save();
        }
    }

    static Reference2ReferenceMap<Fluid, Fluid> inertGas2HighPressureCache = null;

    private static void processDust(Material material) {
        ItemStack dustStack = ChemicalHelper.get(TagPrefix.dust, material);
        if (dustStack.isEmpty()) return;
        String id = "%s_%s_".formatted(FormattingUtil.toLowerCaseUnderscore(TagPrefix.dust.name),
                material.getName().toLowerCase(Locale.ROOT));
        OreProperty oreProperty = material.hasProperty(PropertyKey.ORE) ? material.getProperty(PropertyKey.ORE) : null;
        int mass = (int) material.getMass();

        if (material.hasFluid()) {
            if (inertGas2HighPressureCache == null) {
                inertGas2HighPressureCache = new Reference2ReferenceOpenHashMap<>();
            }
            Fluid molten = material.getFluid(FluidStorageKeys.MOLTEN);
            FastFluidIngredient N2 = FastFluidIngredient.of(GTMaterials.Nitrogen.getFluid(4 * mass));
            FastFluidIngredient N2HP = FastFluidIngredient.of(GTOMaterials.HighPressureNitrogen.getFluid(5 * mass));

            FluidIngredient inert = material.hasProperty(PropertyKey.BLAST) ? Optional.ofNullable(material.getProperty(PropertyKey.BLAST).getGasTier())
                    .map(gas -> gas.getFluid().getStacks()[0])
                    .map(fs -> FastFluidIngredient.of(new FluidStack(fs.getFluid(), fs.getAmount() * mass / 500 + 30 + mass)))
                    .orElse(N2) : N2;
            FluidIngredient inertHighPressure = material.hasProperty(PropertyKey.BLAST) ?
                    Optional.ofNullable(material.getProperty(PropertyKey.BLAST).getGasTier())
                            .map(gas -> gas.getFluid().getStacks()[0])
                            .map(fs -> {
                                Fluid fluid = fs.getFluid();
                                int amount = fs.getAmount() * mass / 450 + 40 + mass / 5 * 6;
                                if (inertGas2HighPressureCache.containsKey(fluid)) {
                                    return new FluidStack(inertGas2HighPressureCache.get(fluid), amount);
                                }
                                Fluid HP = GTCEuAPI.materialManager.getRegisteredMaterials().stream().filter(m -> m.hasFluid() && m.getFluid() == fluid).findAny().orElseThrow().getFluid(GTOFluidStorageKey.HIGH_PRESSURE_GAS);
                                inertGas2HighPressureCache.put(fluid, HP);
                                return new FluidStack(HP, amount);
                            }).map(FastFluidIngredient::of)
                            .orElse(N2HP) :
                    N2HP;
            ATOMIZATION_CONDENSATION_RECIPES.recipeBuilder("atomize_condense_" + id + "to_dust")
                    .inputFluids(material.getFluid(L))
                    .inputFluids(inertHighPressure)
                    .outputItems(dustStack)
                    .outputFluids(inert)
                    .duration(mass / 2 + 1).EUt(VA[LV] / 2)
                    .category(GTORecipeCategories.CONDENSE_FLUID_TO_DUST)
                    .save();
            if (molten != null) {
                boolean needLiquidHelium = material.getProperty(PropertyKey.ALLOY_BLAST).getTemperature() >= 5000;
                var b = ATOMIZATION_CONDENSATION_RECIPES.recipeBuilder("atomize_condense_" + id + "to_dust_from_molten")
                        .inputFluids(molten, L)
                        .inputFluids(inertHighPressure)
                        .outputItems(dustStack)
                        .outputFluids(inert)
                        .duration((int) (mass * 1.5f)).EUt(GTOUtils.getVoltageMultiplier(material))
                        .category(GTORecipeCategories.CONDENSE_MOLTEN_TO_DUST);
                if (needLiquidHelium) {
                    b.inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 500))
                            .outputFluids(GTMaterials.Helium.getFluid(250));
                }
                b.save();
            }
        }

        if (material.hasFlag(GENERATE_MXene)) {
            Material A;
            int temp = Math.max(3700 + 10 * mass, material.getBlastTemperature());
            if (temp >= 5000) {
                A = GTMaterials.Gallium;
            } else A = GTMaterials.Aluminium;

            CRYSTALLIZATION_RECIPES.builder("mxene_precursor_" + id)
                    .inputItems(dust, material, 4)
                    .inputItems(dustTiny, material, 16)
                    .outputItems(GTOTagPrefix.AluminumContainedMXenePrecursor, material)
                    .inputFluids(A, 288)
                    .inputFluids(GTOMaterials.HighPressureArgon, 1000)
                    .EUt(128)
                    .blastFurnaceTemp(temp)
                    .duration(2400)
                    .save();
            REACTION_FURNACE_RECIPES.builder("mxene_etching_" + id)
                    .inputItems(GTOTagPrefix.AluminumContainedMXenePrecursor, material)
                    .inputItems(GTOTagPrefix.dust, GTOMaterials.SodiumFluoride, 8)
                    .outputItems(GTOTagPrefix.MXene, material)
                    .inputFluids(GTMaterials.HydrofluoricAcid, 13000)
                    .outputFluids(GTOMaterials.DiluteHydrofluoricAcid, 13000)
                    .EUt(555)
                    .blastFurnaceTemp(temp - 2000)
                    .duration(20 * mass + 10)
                    .save();
        }
        if (material.hasFlag(GENERATE_FIBER)) {
            if (!material.hasFlag(IS_CARBON_FIBER)) {
                int fiberTemp = getFiberExtrusionTemperature(material);
                if (fiberTemp == 0) {
                    fiberTemp = Math.max(800, material.getBlastTemperature());
                }
                int amount = (material == BorosilicateGlass || material == Kevlar) ? 8 : 1;
                FIBER_EXTRUSION_RECIPES.builder("fiber_" + id + "from_extrusion")
                        .inputItems(dust, material)
                        .outputItems(GTOTagPrefix.FIBER, material, amount)
                        .EUt(50 + mass * 2L)
                        .duration(200 + mass * 4)
                        .blastFurnaceTemp(fiberTemp)
                        .save();

                DRAWING_RECIPES.recipeBuilder("drawing_fiber_" + id)
                        .circuitMeta(4)
                        .inputItems(dust, material, 256)
                        .outputItems(GTOTagPrefix.FIBER, material, amount * 256)
                        .addData("spool", 5)
                        .duration((200 + mass * 4) * 512)
                        .EUt((50 + mass * 2L) * 16)
                        .blastFurnaceTemp(4300 + fiberTemp)
                        .save();
            }
            LOOM_RECIPES.builder("fiber_mesh_" + id + "_from_fiber")
                    .inputItems(GTOTagPrefix.FIBER, material, 2)
                    .outputItems(GTOTagPrefix.FIBER_MESH, material, 1)
                    .circuitMeta(4)
                    .EUt(30 + mass * 2L)
                    .duration(500 + mass * 4)
                    .save();
        }

        if (material.hasProperty(PropertyKey.GEM)) {
            ItemStack gemStack = ChemicalHelper.get(gem, material);

            if (material.hasFlag(CRYSTALLIZABLE)) {
                AUTOCLAVE_RECIPES.recipeBuilder("autoclave_" + id + "_water")
                        .inputItems(dustStack)
                        .inputFluids(GTMaterials.Water.getFluid(250))
                        .chancedOutput(gemStack, 7000, 1000)
                        .duration(1200).EUt(24)
                        .save();

                AUTOCLAVE_RECIPES.recipeBuilder("autoclave_" + id + "_distilled")
                        .inputItems(dustStack)
                        .inputFluids(DistilledWater.getFluid(50))
                        .outputItems(gemStack)
                        .duration(600).EUt(24)
                        .save();
            }

            if (!material.hasFlag(EXPLOSIVE) && !material.hasFlag(FLAMMABLE)) {
                ItemStack dustStack4 = dustStack.copyWithCount(4);
                ItemStack gemStack3 = gemStack.copyWithCount(3);
                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_powderbarrel")
                        .inputItems(dustStack4)
                        .outputItems(gemStack3)
                        .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                        .explosivesType(new ItemStack(GTBlocks.POWDERBARREL, 8))
                        .save();

                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_tnt")
                        .inputItems(dustStack4)
                        .outputItems(gemStack3)
                        .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                        .explosivesAmount(4)
                        .save();

                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_dynamite")
                        .inputItems(dustStack4)
                        .outputItems(gemStack3)
                        .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                        .explosivesType(GTItems.DYNAMITE.asStack(2))
                        .save();

                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_itnt")
                        .inputItems(dustStack4)
                        .outputItems(gemStack3)
                        .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                        .explosivesType(new ItemStack(GTBlocks.INDUSTRIAL_TNT))
                        .save();

                ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("electric_implode_" + id)
                        .inputItems(dustStack4)
                        .outputItems(gemStack3)
                        .save();
            }

            if (oreProperty != null) {
                Material smeltingResult = oreProperty.getDirectSmeltResult();
                if (!smeltingResult.isNull()) {
                    VanillaRecipeHelper.addSmeltingRecipe(id + "_ingot",
                            dustStack, ChemicalHelper.get(ingot, smeltingResult));
                }
            }

        } else if (material.hasProperty(PropertyKey.INGOT)) {
            if (!material.hasAnyOfFlags(FLAMMABLE, NO_SMELTING)) {

                boolean hasHotIngot = ingotHot.doGenerateItem(material);
                ItemStack ingotStack = ChemicalHelper.get(hasHotIngot ? ingotHot : ingot, material);
                if (ingotStack.isEmpty() && oreProperty != null) {
                    Material smeltingResult = oreProperty.getDirectSmeltResult();
                    if (smeltingResult.isNull()) {
                        ingotStack = ChemicalHelper.get(ingot, smeltingResult);
                    }
                }
                int blastTemp = material.getBlastTemperature();

                if (blastTemp <= 0) {
                    // smelting magnetic dusts is handled elsewhere
                    if (!material.hasFlag(IS_MAGNETIC)) {
                        // do not register inputs by ore dict here. Let other mods register their own dust -> ingots
                        VanillaRecipeHelper.addSmeltingRecipe(id + "_demagnetize_from_dust",
                                dustStack, ingotStack);
                    }
                } else {
                    IngotProperty ingotProperty = material.getProperty(PropertyKey.INGOT);
                    BlastProperty blastProperty = material.getProperty(PropertyKey.BLAST);

                    processEBFRecipe(material, blastProperty, ingotStack);

                    if (!ingotProperty.getMagneticMaterial().isNull()) {
                        processEBFRecipe(ingotProperty.getMagneticMaterial(), blastProperty, ingotStack);
                    }
                }
            }
        } else {
            if (material.hasFlag(GENERATE_PLATE) && !material.hasFlag(EXCLUDE_PLATE_COMPRESSOR_RECIPE)) {
                COMPRESSOR_RECIPES.recipeBuilder("compress_plate_" + id)
                        .inputItems(dustStack)
                        .outputItems(plate, material)
                        .EUt(VA[ULV])
                        .duration((int) material.getMass())
                        .save();
            }

            // Some Ores with Direct Smelting Results have neither ingot nor gem properties
            if (oreProperty != null) {
                Material smeltingResult = oreProperty.getDirectSmeltResult();
                if (smeltingResult.isNull()) {
                    ItemStack ingotStack = ChemicalHelper.get(ingot, smeltingResult);
                    if (!ingotStack.isEmpty()) {
                        VanillaRecipeHelper.addSmeltingRecipe(id + "_dust_to_ingot", dustStack, ingotStack);
                    }
                }
            }
        }
    }

    private static void processSmallDust(Material material) {
        ItemStack stack1 = ChemicalHelper.get(TagPrefix.dustSmall, material, 4);
        if (stack1.isEmpty()) return;
        ItemStack stack2 = ChemicalHelper.get(dust, material);
        PACKER_RECIPES.recipeBuilder("package_" + material.getName() + "_small_dust")
                .inputItems(stack1)
                .outputItems(stack2)
                .save();

        UNPACKER_RECIPES.recipeBuilder("unpackage_" + material.getName() + "_small_dust")
                .inputItems(stack2)
                .circuitMeta(1)
                .outputItems(stack1)
                .save();
    }

    private static void processTinyDust(Material material) {
        ItemStack stack1 = ChemicalHelper.get(TagPrefix.dustTiny, material, 9);
        if (stack1.isEmpty()) return;
        ItemStack stack2 = ChemicalHelper.get(dust, material);
        PACKER_RECIPES.recipeBuilder("package_" + material.getName() + "_tiny_dust")
                .inputItems(stack1)
                .outputItems(stack2)
                .save();

        UNPACKER_RECIPES.recipeBuilder("unpackage_" + material.getName() + "_tiny_dust")
                .inputItems(stack2)
                .circuitMeta(2)
                .outputItems(stack1)
                .save();
    }

    private static void processEBFRecipe(Material material, BlastProperty property, ItemStack output) {
        if (material.hasFlag(COMPOSITE_MATERIAL)) return;
        int blastTemp = property.getBlastTemperature();
        BlastProperty.GasTier gasTier = property.getGasTier();
        int duration = property.getDurationOverride();
        if (duration <= 0) {
            duration = Math.max(1, (int) (material.getMass() * blastTemp / 50L));
        }
        int EUt = property.getEUtOverride();
        if (EUt <= 0) EUt = VA[MV];

        RecipeBuilder blastBuilder = BLAST_RECIPES.recipeBuilder("blast_" + material.getName())
                .inputItems(dust, material)
                .outputItems(output)
                .blastFurnaceTemp(blastTemp)
                .EUt(EUt);

        if (gasTier != null) {
            FluidIngredient gas = property.getGasTier().getFluid();

            blastBuilder.copy("blast_" + material.getName())
                    .circuitMeta(1)
                    .duration(duration)
                    .save();

            blastBuilder.copy("blast_" + material.getName() + "_gas")
                    .circuitMeta(2)
                    .inputFluids(gas)
                    .duration((int) (duration * 0.67))
                    .save();
        } else {
            blastBuilder.duration(duration);
            if (material == Silicon) {
                blastBuilder.circuitMeta(1);
            }
            blastBuilder.save();
        }

        // Add Vacuum Freezer recipe if required.
        if (ingotHot.doGenerateItem(material)) {
            int vacuumEUt = property.getVacuumEUtOverride() != -1 ? property.getVacuumEUtOverride() : VA[MV];
            int vacuumDuration = property.getVacuumDurationOverride() != -1 ? property.getVacuumDurationOverride() :
                    (int) material.getMass() * 3;
            if (blastTemp < 5000) {
                VACUUM_RECIPES.recipeBuilder("cool_hot_" + material.getName() + "_ingot")
                        .inputItems(ingotHot, material)
                        .outputItems(ingot, material)
                        .duration(vacuumDuration)
                        .EUt(vacuumEUt)
                        .save();
            } else {
                VACUUM_RECIPES.recipeBuilder("cool_hot_" + material.getName() + "_ingot")
                        .inputItems(ingotHot, material)
                        .inputFluids(Helium.getFluid(FluidStorageKeys.LIQUID, 500))
                        .outputItems(ingot, material)
                        .outputFluids(Helium.getFluid(250))
                        .duration(vacuumDuration)
                        .EUt(vacuumEUt)
                        .save();
            }
        }

        AlloyBlastProperty alloyBlastProperty = material.getProperty(PropertyKey.ALLOY_BLAST);
        if (alloyBlastProperty != null) {
            alloyBlastProperty.getRecipeProducer().produce(material, property);
        }
    }
}
