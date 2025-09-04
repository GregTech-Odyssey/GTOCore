package com.gtocore.mixin.gtm.registry;

import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.api.registries.GTOMachineBuilder;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.register.MachineRegisterUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.*;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.client.renderer.machine.SimpleGeneratorMachineRenderer;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.GTValues.V;
import static com.gtolib.api.registries.GTORegistration.GTM;

@Mixin(GTMachineUtils.class)
public final class GTMachineUtilsMixin {

    @Inject(method = "registerTieredMachines", at = @At("HEAD"), remap = false, cancellable = true)
    private static void registerTieredMachines(String name, BiFunction<MetaMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, GTOMachineBuilder, MachineDefinition> builder, int[] tiers, CallbackInfoReturnable<MachineDefinition[]> cir) {
        if (name.equals("macerator")) {
            cir.setReturnValue(MachineRegisterUtils.registerTieredGTMMachines("macerator",
                    (holder, tier) -> new SimpleTieredMachine(holder, tier, GTMachineUtils.defaultTankSizeFunction), (tier, builder1) -> builder1
                            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("macerator"), GTRecipeTypes.MACERATOR_RECIPES))
                            .nonYAxisRotation()
                            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
                            .addOutputLimit(ItemRecipeCapability.CAP, switch (tier) {
                                case 1, 2 -> 1;
                                case 3 -> 3;
                                default -> 4;
                            })
                            .recipeModifier(RecipeModifierFunction.OVERCLOCKING)
                            .workableTieredHullRenderer(GTCEu.id("block/machines/macerator"))
                            .tooltips(GTMachineUtils.workableTiered(tier, V[tier], V[tier] << 6, GTRecipeTypes.MACERATOR_RECIPES, GTMachineUtils.defaultTankSizeFunction.apply(tier), true))
                            .register(),
                    GTMachineUtils.ELECTRIC_TIERS));
        }
    }

    @Inject(method = "registerSimpleGenerator", at = @At("HEAD"), remap = false, cancellable = true)
    private static void registerSimpleGenerator(String name, GTRecipeType recipeType, Int2IntFunction tankScalingFunction, float hazardStrengthPerOperation, int[] tiers, CallbackInfoReturnable<MachineDefinition[]> cir) {
        cir.setReturnValue(MachineRegisterUtils.registerTieredGTMMachines(name,
                (holder, tier) -> new SimpleGeneratorMachine(holder, tier, hazardStrengthPerOperation * tier, tankScalingFunction),
                (tier, builder) -> builder
                        .editableUI(SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                        .allRotation()
                        .recipeType(recipeType)
                        .recipeModifier(RecipeModifierFunction.SIMPLE_GENERATOR_MACHINEMODIFIER)
                        .addOutputLimit(ItemRecipeCapability.CAP, 0)
                        .addOutputLimit(FluidRecipeCapability.CAP, 0)
                        .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTCEu.id("block/generators/" + name)))
                        .tooltips(Component.translatable("gtocore.machine.efficiency.tooltip", GTOUtils.getGeneratorEfficiency(recipeType, tier)).append("%"))
                        .tooltips(Component.translatable("gtceu.universal.tooltip.amperage_out", GTOUtils.getGeneratorAmperage(tier)))
                        .tooltips(GTMachineUtils.workableTiered(tier, V[tier], V[tier] << 6, recipeType, tankScalingFunction.apply(tier), false))
                        .register(),
                tiers));
    }

    @Inject(method = "registerSimpleMachines(Ljava/lang/String;Lcom/gregtechceu/gtceu/api/recipe/GTRecipeType;Lit/unimi/dsi/fastutil/ints/Int2IntFunction;Z[I)[Lcom/gregtechceu/gtceu/api/machine/MachineDefinition;", at = @At("HEAD"), remap = false, cancellable = true)
    private static void registerSimpleMachines(String name, GTRecipeType recipeType, Int2IntFunction tankScalingFunction, boolean hasPollutionDebuff, int[] tiers, CallbackInfoReturnable<MachineDefinition[]> cir) {
        cir.setReturnValue(MachineRegisterUtils.registerTieredGTMMachines(name,
                (holder, tier) -> new SimpleTieredMachine(holder, tier, tankScalingFunction), (tier, builder) -> builder
                        .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                        .nonYAxisRotation()
                        .recipeType(recipeType)
                        .recipeModifier(RecipeModifierFunction.OVERCLOCKING)
                        .workableTieredHullRenderer(GTCEu.id("block/machines/" + name))
                        .tooltips(GTMachineUtils.workableTiered(tier, GTValues.V[tier], GTValues.V[tier] << 6, recipeType,
                                tankScalingFunction.apply(tier), true))
                        .register(),
                tiers));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MultiblockMachineDefinition registerLargeCombustionEngine(String name, int tier, Supplier<? extends Block> casing, Supplier<? extends Block> gear, Supplier<? extends Block> intake, ResourceLocation casingTexture, ResourceLocation overlayModel) {
        return MachineRegisterUtils.registerLargeCombustionEngine(GTM, name, null, tier, GTRecipeTypes.COMBUSTION_GENERATOR_FUELS, casing, gear, intake, casingTexture, overlayModel, true);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MultiblockMachineDefinition registerLargeTurbine(String name, int tier, GTRecipeType recipeType, Supplier<? extends Block> casing, Supplier<? extends Block> gear, ResourceLocation casingTexture, ResourceLocation overlayModel, boolean needsMuffler) {
        return MachineRegisterUtils.registerLargeTurbine(GTM, name, null, tier, false, recipeType, casing, gear, casingTexture, overlayModel, true);
    }
}
