package com.gtocore.client.renderer.machine;

import com.gtocore.common.machine.mana.ManaHeaterMachine;

import com.gtolib.GTOCore;
import com.gtolib.api.renderer.machine.WorkableManaTieredHullMachineRenderer;
import com.gtolib.api.renderer.model.WorkableOverlayModel;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ManaHeaterRenderer extends WorkableManaTieredHullMachineRenderer implements IHeaterRenderer {

    private static final ResourceLocation MODEL = GTOCore.id("block/machines/heater/mana");
    private static final ResourceLocation MODEL_ALT = GTOCore.id("block/machines/heater/mana_alt");
    private final WorkableOverlayModel altModel;

    public ManaHeaterRenderer(int tier) {
        super(tier, MODEL);
        this.altModel = new WorkableOverlayModel(MODEL_ALT);
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

    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("deprecation")
    public void onPrepareTextureAtlas(ResourceLocation atlasName, Consumer<ResourceLocation> register) {
        super.onPrepareTextureAtlas(atlasName, register);
        if (atlasName.equals(TextureAtlas.LOCATION_BLOCKS)) {
            altModel.registerTextureAtlas(register);
        }
    }

    @Override
    protected WorkableOverlayModel modelToUse(MachineDefinition definition, @Nullable MetaMachine machine) {
        return (machine instanceof ManaHeaterMachine manaHeater && manaHeater.hasSalamanderInput()) ?
                altModel : super.modelToUse(definition, machine);
    }
}
