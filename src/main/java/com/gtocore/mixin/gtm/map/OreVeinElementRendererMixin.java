package com.gtocore.mixin.gtm.map;

import com.gtolib.api.gui.GTOGuiTextures;

import com.gregtechceu.gtceu.api.data.worldgen.ores.GeneratedVeinMetadata;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.integration.map.xaeros.worldmap.ore.OreVeinElement;
import com.gregtechceu.gtceu.integration.map.xaeros.worldmap.ore.OreVeinElementRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureManager;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.map.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;

@Mixin(OreVeinElementRenderer.class)
public class OreVeinElementRendererMixin {

    @Inject(method = "renderElement(ILcom/gregtechceu/gtceu/integration/map/xaeros/worldmap/ore/OreVeinElement;ZLnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphics;DDDDFDDLnet/minecraft/client/renderer/texture/TextureManager;Lnet/minecraft/client/gui/Font;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lxaero/map/graphics/renderer/multitexture/MultiTextureRenderTypeRendererProvider;IDFDDZF)Z", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 1), remap = false)
    private void renderElement(int location, OreVeinElement element, boolean hovered, Minecraft mc, GuiGraphics graphics, double cameraX, double cameraZ, double mouseX, double mouseZ, float brightness, double scale, double screenSizeBasedScale, TextureManager textureManager, Font fontRenderer, MultiBufferSource.BufferSource renderTypeBuffers, MultiTextureRenderTypeRendererProvider rendererProvider, int elementIndex, double optionalDepth, float optionalScale, double partialX, double partialY, boolean cave, float partialTicks, CallbackInfoReturnable<Boolean> cir, @Local GeneratedVeinMetadata vein) {
        if (vein.depleted()) {
            int iconSize = ConfigHolder.INSTANCE.compat.minimap.oreIconSize;
            GTOGuiTextures.DELETE.draw(graphics, 0, 0, (float) -iconSize / 2, (float) -iconSize / 2, iconSize, iconSize);
        }
    }
}
