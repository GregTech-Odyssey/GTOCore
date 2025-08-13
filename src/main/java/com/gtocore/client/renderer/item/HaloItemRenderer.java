package com.gtocore.client.renderer.item;

import com.gtolib.utils.ClientUtil;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import committee.nova.mods.avaritia.api.client.render.buffer.AlphaOverrideVertexConsumer;
import org.joml.Matrix4f;

import java.util.function.IntSupplier;

public final class HaloItemRenderer implements IRenderer {

    public static final HaloItemRenderer WHITE_HALO = HaloItemRenderer.create(0, 0xFFFFFFFF, 4, RLUtils.avaritia("misc/halo"));
    public static final HaloItemRenderer QUANTUM_CHROMO_DYNAMICALLY_HALO = HaloItemRenderer.create(0, () -> com.gtolib.utils.ColorUtils.createARGBColor(MaterialsColorMap.quantumColor.getAsInt(), 150), () -> GTValues.RNG.nextInt(4) + 2, RLUtils.avaritia("misc/halo"));
    public static final HaloItemRenderer COSMIC_HALO = create(0, 0xB2001539, 6, RLUtils.avaritia("misc/halo"));
    public static final HaloItemRenderer NEUTRONIUM_HALO = HaloItemRenderer.create(0, 0x99FFFFFF, 8, RLUtils.avaritia("misc/halo_noise"));
    public static final HaloItemRenderer COSMIC_NEUTRONIUM_HALO = HaloItemRenderer.create(0.05F, 0x992F1909, 10, RLUtils.avaritia("misc/halo_noise"));
    public static final HaloItemRenderer CHAOS_HALO = HaloItemRenderer.create(0.2F, 0, 0, null);
    public static final HaloItemRenderer CHAOS_INFINITY_HALO = HaloItemRenderer.create(0.25F, () -> 0xFF000000, () -> GTValues.RNG.nextInt(4) + 6, RLUtils.avaritia("misc/halo"));
    public static final HaloItemRenderer MAGNETOHYDRODYNAMICALLY_CONSTRAINED_STAR_MATTER_HALO = HaloItemRenderer.create(0, 0xB2FF4125, 6, RLUtils.avaritia("misc/halo"));
    public static final HaloItemRenderer INFINITY_HALO = HaloItemRenderer.create(0.1F, 0xFF000000, 10, RLUtils.avaritia("misc/halo"));
    public static final HaloItemRenderer ETERNITY_HALO = HaloItemRenderer.create(0.1F, 0xFF000000, 10, RLUtils.avaritia("misc/halo"));
    public static final HaloItemRenderer MAGMATTER_HALO = HaloItemRenderer.create(0.15F, 0xFF212121, 10, RLUtils.avaritia("misc/halo"));
    public static final HaloItemRenderer RADIOACTIVE = HaloItemRenderer.create(0F, 0xFF218121, 9, RLUtils.avaritia("misc/halo_noise"));

    private static HaloItemRenderer create(float pulse, int colour, int size, ResourceLocation textures) {
        return create(pulse, () -> colour, () -> size, textures);
    }

    private static HaloItemRenderer create(float pulse, IntSupplier colour, IntSupplier size, ResourceLocation textures) {
        return GTCEu.isClientSide() ? new HaloItemRenderer(pulse, colour, size, textures) : null;
    }

    private final float pulse;
    private final IntSupplier colour;
    private final IntSupplier size;
    private final ResourceLocation texture;

    private HaloItemRenderer(float pulse, IntSupplier colour, IntSupplier size, ResourceLocation texture) {
        this.pulse = pulse;
        this.colour = colour;
        this.size = size;
        this.texture = texture;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model) {
        model = ClientUtil.getVanillaModel(stack, null, null);
        if (transformType == ItemDisplayContext.GUI) {
            if (texture != null) {
                poseStack.pushPose();
                poseStack.translate(-0.5F, -0.5F, -0.5F);
                Tesselator tess = Tesselator.getInstance();
                BufferBuilder buf = tess.getBuilder();
                buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                RenderSystem.enableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                int colour = this.colour.getAsInt();
                RenderSystem.setShaderColor(ColorUtils.red(colour), ColorUtils.green(colour), ColorUtils.blue(colour), ColorUtils.alpha(colour));
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
                TextureAtlasSprite sprite = ModelFactory.getBlockSprite(texture);
                float minU = sprite.getU0();
                float maxU = sprite.getU1();
                float minV = sprite.getV0();
                float maxV = sprite.getV1();
                float spread = size.getAsInt() / 16.0F;
                float min = 0.0F - spread;
                float max = 1.0F + spread;
                Matrix4f pos = poseStack.last().pose();
                buf.vertex(pos, max, max, 0).uv(maxU, minV).endVertex();
                buf.vertex(pos, min, max, 0).uv(minU, minV).endVertex();
                buf.vertex(pos, min, min, 0).uv(minU, maxV).endVertex();
                buf.vertex(pos, max, min, 0).uv(maxU, maxV).endVertex();
                tess.end();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.popPose();
            }
            ClientUtil.vanillaRender(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model);
            if (pulse != 0 && !(stack.getItem() instanceof BlockItem)) {
                poseStack.pushPose();
                poseStack.translate(-0.5F, -0.5F, -0.5F);
                float scale = GTValues.RNG.nextFloat() * pulse + 1;
                float trans = (1 - scale) / 2;
                poseStack.translate(trans, trans, 0);
                poseStack.scale(scale, scale, 1.0001F);
                model.getTransforms().getTransform(transformType).apply(leftHand, poseStack);
                VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil());
                ClientUtil.getItemRenderer().renderModelLists(model, stack, combinedLight, combinedOverlay, poseStack, new AlphaOverrideVertexConsumer(vertexConsumer, (float) 0.6));
                poseStack.popPose();
            }
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        } else {
            ClientUtil.vanillaRender(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model);
        }
    }
}
