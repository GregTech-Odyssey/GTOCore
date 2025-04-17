package com.gto.gtocore.client.renderer.machine;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.client.renderer.machine.IControllerRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.gregtechceu.gtceu.client.util.StaticFaceBakery;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomPartRenderer extends WorkableCasingMachineRenderer implements IControllerRenderer {

    private final ResourceLocation partModel;

    public CustomPartRenderer(ResourceLocation baseCasing, ResourceLocation workableModel, ResourceLocation partModel) {
        super(baseCasing, workableModel);
        this.partModel = partModel;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderPartModel(List<BakedQuad> quads, IMultiController machine, IMultiPart part, Direction frontFacing, @Nullable Direction side, RandomSource rand, Direction modelFacing, ModelState modelState) {
        if (frontFacing != null && side != null) {
            quads.add(StaticFaceBakery.bakeFace(side, ModelFactory.getBlockSprite(partModel), modelState));
        }
    }
}
