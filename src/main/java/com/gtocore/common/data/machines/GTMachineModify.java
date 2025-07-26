package com.gtocore.common.data.machines;

import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.misc.PlanetManagement;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.api.recipe.modifier.RecipeModifierFunctionList;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.utils.memoization.GTMemoizer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.machines.GTMultiMachines.PRIMITIVE_BLAST_FURNACE;
import static com.gtocore.common.data.GTOMachines.PRIMITIVE_BLAST_FURNACE_HATCH;

public final class GTMachineModify {

    public static void init() {
        GTMultiMachines.MULTI_SMELTER.setRecipeTypes(new GTRecipeType[] { GTRecipeTypes.FURNACE_RECIPES });
        GTMultiMachines.MULTI_SMELTER.setTooltipBuilder((itemStack, components) -> components.add(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip", Component.translatable("gtceu.electric_furnace"))));
        GTMultiMachines.MULTI_SMELTER.setRecipeModifier(new RecipeModifierFunctionList(RecipeModifierFunction::multiSmelterParallel));
        GTMultiMachines.LARGE_CHEMICAL_REACTOR.setRecipeTypes(new GTRecipeType[] { GTORecipeTypes.CHEMICAL });
        GTMultiMachines.LARGE_CHEMICAL_REACTOR.setRecipeModifier(RecipeModifier.NO_MODIFIER);
        GTMultiMachines.LARGE_BOILER_BRONZE.setRecipeModifier(RecipeModifierFunction.LARGE_BOILER_MODIFIER);
        GTMultiMachines.LARGE_BOILER_STEEL.setRecipeModifier(RecipeModifierFunction.LARGE_BOILER_MODIFIER);
        GTMultiMachines.LARGE_BOILER_TITANIUM.setRecipeModifier(RecipeModifierFunction.LARGE_BOILER_MODIFIER);
        GTMultiMachines.LARGE_BOILER_TUNGSTENSTEEL.setRecipeModifier(RecipeModifierFunction.LARGE_BOILER_MODIFIER);
        GTMultiMachines.ELECTRIC_BLAST_FURNACE.setRecipeModifier(new RecipeModifierFunctionList(RecipeModifierFunction::ebfOverclock));
        GTMultiMachines.PYROLYSE_OVEN.setRecipeModifier(new RecipeModifierFunctionList(RecipeModifierFunction::pyrolyseOvenOverclock));
        GTMultiMachines.CRACKER.setRecipeModifier(new RecipeModifierFunctionList(RecipeModifierFunction::crackerOverclock));
        GTMultiMachines.IMPLOSION_COMPRESSOR.setRecipeModifier(RecipeModifierFunction.OVERCLOCKING);
        GTMultiMachines.DISTILLATION_TOWER.setRecipeModifier(RecipeModifierFunction.OVERCLOCKING);
        GTMultiMachines.VACUUM_FREEZER.setRecipeModifier(RecipeModifierFunction.OVERCLOCKING);
        GTMultiMachines.ASSEMBLY_LINE.setRecipeModifier(RecipeModifierFunction.OVERCLOCKING);
        GTMultiMachines.STEAM_GRINDER.setPatternFactory(GTMemoizer.memoize(() -> FactoryBlockPattern.start(GTMultiMachines.STEAM_GRINDER)
                .aisle("XXX", "XXX", "XXX")
                .aisle("XXX", "X#X", "XXX")
                .aisle("XXX", "XSX", "XXX")
                .where('S', controller(blocks(GTMultiMachines.STEAM_GRINDER.getBlock())))
                .where('#', air())
                .where('X', blocks(CASING_BRONZE_BRICKS.get())
                        .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                        .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                        .or(abilities(PartAbility.STEAM).setExactLimit(1))
                        .or(blocks(GTOMachines.STEAM_VENT_HATCH.getBlock()).setExactLimit(1)))
                .build()));

        GTMultiMachines.STEAM_OVEN.setPatternFactory(GTMemoizer.memoize(() -> FactoryBlockPattern.start(GTMultiMachines.STEAM_OVEN)
                .aisle("FFF", "XXX", " X ")
                .aisle("FFF", "X#X", " X ")
                .aisle("FFF", "XSX", " X ")
                .where('S', controller(blocks(GTMultiMachines.STEAM_OVEN.getBlock())))
                .where('#', air())
                .where(' ', any())
                .where('X', blocks(CASING_BRONZE_BRICKS.get())
                        .or(Predicates.abilities(PartAbility.STEAM_IMPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                        .or(Predicates.abilities(PartAbility.STEAM_EXPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1)))
                .where('F', blocks(FIREBOX_BRONZE.get())
                        .or(Predicates.abilities(PartAbility.STEAM).setExactLimit(1))
                        .or(blocks(GTOMachines.STEAM_VENT_HATCH.getBlock()).setExactLimit(1)))
                .build()));

        GTMultiMachines.PRIMITIVE_BLAST_FURNACE.setPatternFactory(GTMemoizer.memoize(() -> FactoryBlockPattern.start(GTMultiMachines.PRIMITIVE_BLAST_FURNACE)
                .aisle("XXX", "XXX", "XXX", "XXX")
                .aisle("XXX", "X#X", "X#X", "X#X")
                .aisle("XXX", "XYX", "XXX", "XXX")
                .where('X', blocks(CASING_PRIMITIVE_BRICKS.get()).or(blocks(PRIMITIVE_BLAST_FURNACE_HATCH.get()).setMaxGlobalLimited(5)))
                .where('#', air())
                .where('Y', controller(blocks(PRIMITIVE_BLAST_FURNACE.getBlock())))
                .build()));

        GTMultiMachines.LARGE_BOILER_BRONZE.setPatternFactory(GTMemoizer.memoize(() -> FactoryBlockPattern.start(GTMultiMachines.LARGE_BOILER_BRONZE)
                .aisle("XXX", "CCC", "CCC", "CCC")
                .aisle("XXX", "CPC", "CPC", "CCC")
                .aisle("XXX", "CSC", "CCC", "CCC")
                .where('S', Predicates.controller(blocks(GTMultiMachines.LARGE_BOILER_BRONZE.getBlock())))
                .where('P', blocks(CASING_BRONZE_PIPE.get()))
                .where('X', blocks(FIREBOX_BRONZE.get()).setMinGlobalLimited(5)
                        .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMinGlobalLimited(1).setPreviewCount(1))
                        .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1)))
                .where('C', blocks(CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(20).or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMinGlobalLimited(1).setPreviewCount(1)))
                .build()));

        GTMultiMachines.DISTILLATION_TOWER.setPatternFactory(GTMemoizer.memoize(() -> {
            TraceabilityPredicate exportPredicate = abilities(PartAbility.EXPORT_FLUIDS_1X).or(blocks(GTAEMachines.FLUID_EXPORT_HATCH_ME.get())).setMaxLayerLimited(1);
            TraceabilityPredicate maint = autoAbilities(true, false, false).setMaxGlobalLimited(1);
            return FactoryBlockPattern.start(GTMultiMachines.DISTILLATION_TOWER, RIGHT, BACK, UP)
                    .aisle("YSY", "YYY", "YYY")
                    .aisle("ZZZ", "Z#Z", "ZZZ")
                    .aisle("XXX", "X#X", "XXX").setRepeatable(0, 10)
                    .aisle("XXX", "XXX", "XXX")
                    .where('S', Predicates.controller(blocks(GTMultiMachines.DISTILLATION_TOWER.getBlock())))
                    .where('Y', blocks(CASING_STAINLESS_CLEAN.get())
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setExactLimit(1))
                            .or(maint))
                    .where('Z', blocks(CASING_STAINLESS_CLEAN.get())
                            .or(exportPredicate)
                            .or(maint))
                    .where('X', blocks(CASING_STAINLESS_CLEAN.get()).or(exportPredicate))
                    .where('#', Predicates.air())
                    .build();
        }));

        for (int tier : GTMachineUtils.ELECTRIC_TIERS) {
            GTMachines.MACERATOR[tier].setRecipeModifier(RecipeModifierFunction.OVERCLOCKING);
            GTMachines.ROCK_CRUSHER[tier].setRecipeModifier(RecipeModifierFunction.OVERCLOCKING);
            if (tier < GTValues.IV) GTMachines.AIR_SCRUBBER[tier].setRecipeModifier(RecipeModifierFunction.OVERCLOCKING);
            if (tier > GTValues.LV) {
                GTMachines.SCANNER[tier].setOnWorking(machine -> {
                    if (machine.getProgress() == machine.getMaxProgress() - 1) {
                        MachineUtils.forEachInputItems(machine, itemStack -> {
                            CompoundTag tag = itemStack.getTag();
                            if (tag != null) {
                                String planet = tag.getString("planet");
                                if (!planet.isEmpty()) {
                                    UUID uuid = tag.getUUID("uuid");
                                    PlanetManagement.unlock(uuid, new ResourceLocation(planet));
                                    itemStack.setCount(0);
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
                    return true;
                });
            }
        }
    }
}
