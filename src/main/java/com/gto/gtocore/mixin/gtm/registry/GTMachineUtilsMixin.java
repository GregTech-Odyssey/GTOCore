package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.machine.multiblock.generator.GeneratorArrayMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.*;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.client.renderer.machine.SimpleGeneratorMachineRenderer;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.LaserHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.storage.BufferMachine;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

@Mixin(GTMachineUtils.class)
public final class GTMachineUtilsMixin {

    @Unique
    private static MachineDefinition[] gtoCore$registerTieredMachines(String name,
                                                                      BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
                                                                      BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
                                                                      int... tiers) {
        return GTOMachines.registerMachineDefinitions(name, null, factory, builder, REGISTRATE, tiers);
    }

    @Inject(method = "registerTieredMachines", at = @At("HEAD"), remap = false, cancellable = true)
    private static void registerTieredMachines(String name, BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder, int[] tiers, CallbackInfoReturnable<MachineDefinition[]> cir) {
        switch (name) {
            case "input_bus":
                cir.setReturnValue(gtoCore$registerTieredMachines("input_bus",
                        (holder, tier) -> new ItemBusPartMachine(holder, tier, IO.IN),
                        (tier, builder1) -> builder1
                                .rotationState(RotationState.ALL)
                                .abilities(tier == 0 ? new PartAbility[] { PartAbility.IMPORT_ITEMS, PartAbility.STEAM_IMPORT_ITEMS, GTOPartAbility.ITEMS_INPUT } : new PartAbility[] { PartAbility.IMPORT_ITEMS, GTOPartAbility.ITEMS_INPUT })
                                .overlayTieredHullRenderer("item_bus.import")
                                .tooltips(Component.translatable("gtceu.machine.item_bus.import.tooltip"),
                                        Component.translatable("gtceu.universal.tooltip.item_storage_capacity", (1 + tier) * (1 + tier)))
                                .register(),
                        ALL_TIERS));
                break;
            case "output_bus":
                cir.setReturnValue(gtoCore$registerTieredMachines("output_bus",
                        (holder, tier) -> new ItemBusPartMachine(holder, tier, IO.OUT),
                        (tier, builder1) -> builder1
                                .rotationState(RotationState.ALL)
                                .abilities(tier == 0 ? new PartAbility[] { PartAbility.EXPORT_ITEMS, PartAbility.STEAM_EXPORT_ITEMS, GTOPartAbility.ITEMS_OUTPUT } : new PartAbility[] { PartAbility.EXPORT_ITEMS, GTOPartAbility.ITEMS_OUTPUT })
                                .overlayTieredHullRenderer("item_bus.export")
                                .tooltips(Component.translatable("gtceu.machine.item_bus.export.tooltip"), Component.translatable("gtceu.universal.tooltip.item_storage_capacity", (1 + tier) * (1 + tier)))
                                .register(),
                        ALL_TIERS));
                break;
            case "dual_input_hatch":
                cir.setReturnValue(gtoCore$registerTieredMachines(
                        "dual_input_hatch",
                        (holder, tier) -> new DualHatchPartMachine(holder, tier, IO.IN),
                        (tier, builder1) -> builder1
                                .rotationState(RotationState.ALL)
                                .abilities(PartAbility.IMPORT_ITEMS)
                                .overlayTieredHullRenderer("dual_hatch.import")
                                .tooltips(Component.translatable("gtceu.machine.dual_hatch.import.tooltip"),
                                        Component.translatable("gtceu.universal.tooltip.item_storage_capacity", tier * tier),
                                        Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity_mult", tier, DualHatchPartMachine.getTankCapacity(DualHatchPartMachine.INITIAL_TANK_CAPACITY, tier)),
                                        Component.translatable("gtceu.universal.enabled"))
                                .tooltipBuilder(GTOMachines.GTO_MODIFY)
                                .register(),
                        tiersBetween(LV, GTCEuAPI.isHighTier() ? MAX : UHV)));
                break;
            case "dual_output_hatch":
                cir.setReturnValue(gtoCore$registerTieredMachines(
                        "dual_output_hatch",
                        (holder, tier) -> new DualHatchPartMachine(holder, tier, IO.OUT),
                        (tier, builder1) -> builder1
                                .rotationState(RotationState.ALL)
                                .abilities(PartAbility.EXPORT_ITEMS)
                                .overlayTieredHullRenderer("dual_hatch.export")
                                .tooltips(Component.translatable("gtceu.machine.dual_hatch.export.tooltip"),
                                        Component.translatable("gtceu.universal.tooltip.item_storage_capacity", tier * tier),
                                        Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity_mult", tier, DualHatchPartMachine.getTankCapacity(DualHatchPartMachine.INITIAL_TANK_CAPACITY, tier)),
                                        Component.translatable("gtceu.universal.enabled"))
                                .tooltipBuilder(GTOMachines.GTO_MODIFY)
                                .register(),
                        tiersBetween(LV, GTCEuAPI.isHighTier() ? MAX : UHV)));
                break;
            case "buffer":
                cir.setReturnValue(gtoCore$registerTieredMachines("buffer",
                        BufferMachine::new,
                        (tier, builder1) -> builder1
                                .rotationState(RotationState.NONE)
                                .tieredHullRenderer(GTCEu.id("block/machine/buffer"))
                                .tooltips(Component.translatable("gtceu.machine.buffer.tooltip"), Component.translatable("gtceu.universal.tooltip.item_storage_capacity", BufferMachine.getInventorySize(tier)),
                                        Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity_mult", BufferMachine.getTankSize(tier), FluidHatchPartMachine.getTankCapacity(DualHatchPartMachine.INITIAL_TANK_CAPACITY, tier)))
                                .tooltipBuilder(GTOMachines.GTO_MODIFY)
                                .register(),
                        tiersBetween(LV, GTCEuAPI.isHighTier() ? MAX : UHV)));
                break;
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MachineDefinition[] registerLaserHatch(IO io, int amperage, PartAbility ability) {
        String name = io == IO.IN ? "target" : "source";
        return gtoCore$registerTieredMachines(amperage + "a_laser_" + name + "_hatch",
                (holder, tier) -> new LaserHatchPartMachine(holder, io, tier, amperage), (tier, builder) -> builder
                        .rotationState(RotationState.ALL)
                        .tooltips(Component.translatable("gtceu.machine.laser_hatch." + name + ".tooltip"),
                                Component.translatable("gtceu.machine.laser_hatch.both.tooltip"),
                                Component.translatable("gtceu.universal.disabled"))
                        .abilities(ability)
                        .overlayTieredHullRenderer("laser_hatch." + name)
                        .register(),
                tiersBetween(IV, MAX));
    }

    @Inject(method = "registerSimpleGenerator", at = @At("HEAD"), remap = false, cancellable = true)
    private static void registerSimpleGenerator(String name, GTRecipeType recipeType, Int2IntFunction tankScalingFunction, float hazardStrengthPerOperation, int[] tiers, CallbackInfoReturnable<MachineDefinition[]> cir) {
        cir.setReturnValue(gtoCore$registerTieredMachines(name,
                (holder, tier) -> new SimpleGeneratorMachine(holder, tier, hazardStrengthPerOperation * tier,
                        tankScalingFunction),
                (tier, builder) -> builder
                        .editableUI(SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                        .rotationState(RotationState.ALL)
                        .recipeType(recipeType)
                        .recipeModifier(GTORecipeModifiers::simpleGeneratorMachineModifier)
                        .addOutputLimit(ItemRecipeCapability.CAP, 0)
                        .addOutputLimit(FluidRecipeCapability.CAP, 0)
                        .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTCEu.id("block/generators/" + name)))
                        .tooltips(Component.translatable("gtocore.machine.efficiency.tooltip", GeneratorArrayMachine.getEfficiency(recipeType, tier)).append("%"))
                        .tooltips(Component.translatable("gtocore.universal.tooltip.ampere_out", GeneratorArrayMachine.getAmperage(tier)))
                        .tooltips(GTMachineUtils.workableTiered(tier, V[tier], V[tier] * 64 * GeneratorArrayMachine.getAmperage(tier), recipeType, tankScalingFunction.apply(tier), false))
                        .tooltipBuilder(GTOMachines.GTO_MODIFY)
                        .register(),
                tiers));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MultiblockMachineDefinition registerLargeCombustionEngine(String name, int tier, Supplier<? extends Block> casing, Supplier<? extends Block> gear, Supplier<? extends Block> intake, ResourceLocation casingTexture, ResourceLocation overlayModel) {
        return GTOMachines.registerLargeCombustionEngine(REGISTRATE, name, null, tier, GTRecipeTypes.COMBUSTION_GENERATOR_FUELS, casing, gear, intake, casingTexture, overlayModel, true);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MultiblockMachineDefinition registerLargeTurbine(String name, int tier, GTRecipeType recipeType, Supplier<? extends Block> casing, Supplier<? extends Block> gear, ResourceLocation casingTexture, ResourceLocation overlayModel, boolean needsMuffler) {
        return GTOMachines.registerLargeTurbine(REGISTRATE, name, null, tier, false, recipeType, casing, gear, casingTexture, overlayModel, true);
    }
}
