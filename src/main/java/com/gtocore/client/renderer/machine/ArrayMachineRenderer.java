package com.gtocore.client.renderer.machine;

import com.gtolib.api.renderer.machine.WorkableCasingMachineRenderer;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ArrayMachineRenderer extends WorkableCasingMachineRenderer {

    public ArrayMachineRenderer(ResourceLocation baseCasing, ResourceLocation workableModel) {
        super(baseCasing, workableModel, false);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderMachine(List<BakedQuad> quads, MachineDefinition definition, @Nullable MetaMachine machine, Direction frontFacing, @Nullable Direction side, RandomSource rand, Direction modelFacing, ModelState modelState) {
        super.renderMachine(quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
        // if (machine instanceof MultiblockControllerMachine controllerMachine && controllerMachine.isFormed() &&
        // machine instanceof IArrayMachine arrayMachine) {
        // MachineDefinition heldDefinition = arrayMachine.getMachineDefinition();
        // if (heldDefinition != null && heldDefinition.getRenderer() instanceof MachineRenderer machineRenderer) {
        // List<BakedQuad> machineQuad = new ArrayList<>();
        // machineRenderer.renderMachine(machineQuad, definition, machine, frontFacing, side, rand, modelFacing,
        // modelState);
        // for (BakedQuad quad : machineQuad) {
        // Quad bakedQuad = Quad.from(quad);
        // for (int i = 0; i < 4; i++) {
        // Vector3f pos = bakedQuad.getVert(i);
        // bakedQuad = bakedQuad.withVert(i, new Vector3f(pos.x() - frontFacing.getStepX(), pos.y() + 1, pos.z() -
        // frontFacing.getStepZ()));
        // }
        // quads.add(bakedQuad.rebake());
        // }
        // }
        // }
    }
}
