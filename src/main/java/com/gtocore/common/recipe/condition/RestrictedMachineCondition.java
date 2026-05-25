package com.gtocore.common.recipe.condition;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandlerHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class RestrictedMachineCondition extends RecipeCondition {

    private static final RestrictedMachineCondition MULTIBLOCK = new RestrictedMachineCondition(GTOCore.id("multiblock"));

    public static RestrictedMachineCondition multiblock() {
        return MULTIBLOCK;
    }

    private final ResourceLocation id;

    private MachineDefinition definition;

    public RestrictedMachineCondition(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public Component getTooltips() {
        if (id.equals(MULTIBLOCK.id)) {
            return Component.translatable("gtocore.recipe.restricted_machine", Component.translatable("gtceu.multiblock.title"));
        }
        if (definition == null) {
            definition = GTRegistries.MACHINES.get(id);
        }
        MachineDefinition machineDefinition = definition;
        return Component.translatable("gtocore.recipe.restricted_machine", machineDefinition == null ? "null" : Component.translatable(machineDefinition.getDescriptionId()));
    }

    @Override
    public boolean testCondition(IRecipeHandlerHolder holder, RecipeHandlerUnit unit, GTRecipeDefinition recipe) {
        MachineDefinition machineDefinition = holder.self().getDefinition();
        if (this.equals(MULTIBLOCK)) {
            return holder instanceof MultiblockControllerMachine;
        }
        return machineDefinition.getId().equals(id);
    }
}
