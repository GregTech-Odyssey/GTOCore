package com.gtocore.client.renderer.machine;

import com.gtocore.common.machine.multiblock.part.research.ExResearchBasePartMachine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IWorkable;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.client.renderer.machine.TieredHullMachineRenderer;
import com.gregtechceu.gtceu.client.util.StaticFaceBakery;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.research.HPCAMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExResearchPartRenderer extends TieredHullMachineRenderer {

    private final ResourceLocation texture, activeTexture, activeEmissiveTexture, damagedTexture, damagedActiveTexture,
            damagedActiveEmissiveTexture;

    public ExResearchPartRenderer(int tire,
                                  ResourceLocation texture,
                                  @Nullable ResourceLocation activeTexture,
                                  @Nullable ResourceLocation activeEmissiveTexture,
                                  @Nullable ResourceLocation damagedTexture,
                                  @Nullable ResourceLocation damagedActiveTexture,
                                  @Nullable ResourceLocation damagedActiveEmissiveTexture) {
        super(GTValues.ZPM, switch (tire) {
            case 1 -> GTCEu.id("block/computer_casing");
            case 2 -> GTCEu.id("block/advanced_computer_casing");
            case 3 -> GTOCore.id("block/variant/biocomputer_shell_active");
            case 4 -> GTOCore.id("block/graviton_computer_casing");
            default -> GTOCore.id("block/variant/biocomputer_shell_active");
        });
        this.texture = texture;
        this.activeTexture = activeTexture;
        this.activeEmissiveTexture = activeEmissiveTexture;
        this.damagedTexture = damagedTexture;
        this.damagedActiveTexture = damagedActiveTexture;
        this.damagedActiveEmissiveTexture = damagedActiveEmissiveTexture;
    }

    @Override
    public void renderMachine(List<BakedQuad> quads, MachineDefinition definition, @Nullable MetaMachine machine,
                              Direction frontFacing, @Nullable Direction side, RandomSource rand,
                              @Nullable Direction modelFacing, ModelState modelState) {
        super.renderMachine(quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
        if (machine instanceof ExResearchBasePartMachine hpcaComponent) {
            ResourceLocation texture, emissiveTexture = null;
            var controller = hpcaComponent.isFormed() ? hpcaComponent.getController() : null;
            if (controller != null && (controller instanceof IWorkable workable && workable.isActive())) {
                if (hpcaComponent.isDamaged()) {
                    texture = damagedActiveTexture;
                    emissiveTexture = damagedActiveEmissiveTexture;
                } else {
                    texture = activeTexture;
                    emissiveTexture = activeEmissiveTexture;
                }
            } else {
                if (hpcaComponent.isDamaged()) {
                    texture = this.damagedTexture;
                } else {
                    texture = this.texture;
                }
            }
            if (texture == null) {
                texture = this.texture;
            }
            if (texture != null) {
                Direction facing = frontFacing;
                // 始终在 HPCA 中将其向外渲染，以防它没有在结构中向外放置。
                // 特别检查 HPCA，因为这些组件可能会在其他多块中使用。
                if (controller instanceof HPCAMachine hpca) {
                    facing = RelativeDirection.RIGHT.getRelative(hpca.getFrontFacing(), hpca.getUpwardsFacing(),
                            hpca.isFlipped());
                }
                facing = ModelFactory.modelFacing(frontFacing, facing);
                quads.add(StaticFaceBakery.bakeFace(StaticFaceBakery.SLIGHTLY_OVER_BLOCK, facing,
                        ModelFactory.getBlockSprite(texture), modelState, -1, 0, true, true));
                if (emissiveTexture != null) {
                    quads.add(StaticFaceBakery.bakeFace(StaticFaceBakery.SLIGHTLY_OVER_BLOCK, facing,
                            ModelFactory.getBlockSprite(emissiveTexture),
                            modelState, -101, 15, true, false));
                }
            }
        } else {
            ResourceLocation texture = this.texture;
            if (texture != null) {
                quads.add(StaticFaceBakery.bakeFace(
                        StaticFaceBakery.SLIGHTLY_OVER_BLOCK, Direction.NORTH,
                        ModelFactory.getBlockSprite(texture), modelState, -1, 0, true, true));
            }
        }
    }
}
