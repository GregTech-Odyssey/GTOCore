package com.gtocore.client.renderer.machine;

import com.gtocore.integration.ae.MeWirelessConnectMachine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.model.WorkableOverlayModel;
import com.gregtechceu.gtceu.client.renderer.machine.MachineRenderer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.client.bakedpipeline.Quad;
import com.lowdragmc.lowdraglib.client.bakedpipeline.QuadTransformers;
import com.lowdragmc.lowdraglib.client.renderer.impl.IModelRenderer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MeWirelessConnectMachineRenderer extends MachineRenderer {

    public static final ResourceLocation ME_OVERLAY = GTOCore.id("block/machines/me_wireless");

    public static final ResourceLocation ME_MODEL = GTOCore.id("block/machine/part/me_wireless");
    protected final IModelRenderer activeOverlayModel;
    protected final WorkableOverlayModel overlayModel;

    public MeWirelessConnectMachineRenderer() {
        super(GTCEu.id("block/high_power_casing"));
        this.overlayModel = new WorkableOverlayModel(ME_OVERLAY);
        this.activeOverlayModel = new IModelRenderer(ME_MODEL);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderMachine(List<BakedQuad> quads, MachineDefinition definition, @Nullable MetaMachine machine,
                              Direction frontFacing, @Nullable Direction side, RandomSource rand, Direction modelFacing,
                              ModelState modelState) {
        super.renderMachine(quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
        // expand the overlay quads ever so slightly to combat z-fighting.
        if (machine instanceof MeWirelessConnectMachine me) {
            overlayModel.bakeQuads(side, modelState, me.isOnline(), true)
                    .forEach(quad -> quads.add(Quad.from(quad, OVERLAY_QUADS_OFFSET_OUTER).rebake()));
            if (me.isOnline()) {
                activeOverlayModel.getRotatedModel(frontFacing).getQuads(definition.defaultBlockState(), side, rand)
                        .forEach(quad -> {
                            QuadTransformers.settingEmissivity(15)
                                    .processInPlace(quad);
                            quads.add(Quad.from(quad, OVERLAY_QUADS_OFFSET).rebake());
                        });
            }
        } else {
            // handle the case where the machine is in a gui-context
            overlayModel.bakeQuads(side, modelState, false, false)
                    .forEach(quad -> quads.add(Quad.from(quad, OVERLAY_QUADS_OFFSET).rebake()));
        }
    }

    @Override
    public void onPrepareTextureAtlas(ResourceLocation atlasName, Consumer<ResourceLocation> register) {
        super.onPrepareTextureAtlas(atlasName, register);
        if (atlasName.equals(TextureAtlas.LOCATION_BLOCKS)) {
            overlayModel.registerTextureAtlas(register);
            Stream.of("top", "bottom", "side").forEach(
                    side -> register.accept(GTOCore.id("block/machines/me_wireless/" + side)));
        }
    }

    public static final float OVERLAY_QUADS_OFFSET = 0.0035f;

    public static final float OVERLAY_QUADS_OFFSET_OUTER = 0.004f;
}
