package com.gtocore.client.renderer.machine;

import com.gtolib.GTOCore;
import com.gtolib.api.renderer.machine.WorkableTieredHullMachineRenderer;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeaterRenderer extends WorkableTieredHullMachineRenderer implements IHeaterRenderer {

    private static final ResourceLocation MODEL = GTOCore.id("block/machines/heater/basic");

    public HeaterRenderer(int tier) {
        super(tier, MODEL);
    }

    @Override
    public void renderMachine(List<BakedQuad> quads,
                              MachineDefinition definition,
                              @Nullable MetaMachine machine,
                              Direction frontFacing,
                              @Nullable Direction side,
                              RandomSource rand,
                              Direction modelFacing,
                              ModelState modelState) {
        renderHeater(quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
        super.renderMachine(quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
    }
}
