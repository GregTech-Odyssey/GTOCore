package com.gto.gtocore.client.forge;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.client.Tooltips;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.item.StructureWriteBehavior;
import com.gto.gtocore.utils.StringUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.data.GTMachines;

import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

@Mod.EventBusSubscriber(modid = GTOCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ForgeClientEventListener {

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null) return;
        Item item = event.getItemStack().getItem();
        if (Tooltips.TOOL_TIPS_MAP.containsKey(item)) {
            for (int i = 0; i < Tooltips.TOOL_TIPS_MAP.get(item).length; i++) {
                event.getToolTip().add(Component.translatable("gtocore.tooltip.item." + item.kjs$getIdLocation().getPath() + "." + i));
            }
            if (Tooltips.FLICKER_TOOL_TIPS_MAP.containsKey(item)) {
                event.getToolTip().add(Component.literal(Tooltips.FLICKER_TOOL_TIPS_MAP.get(item).get()));
            }
            return;
        }
        if (Tooltips.suprachronalCircuitSet.contains(item)) {
            for (int tier : GTMachines.ALL_TIERS) {
                if (GTOItems.SUPRACHRONAL_CIRCUIT[tier].is(item)) {
                    event.getToolTip().add(Component.translatable("gtocore.tooltip.item.suprachronal_circuit").withStyle(ChatFormatting.GRAY));
                    event.getToolTip().add(Component.literal(StringUtil.white_blue(I18n.get("gtocore.tooltip.item.tier_circuit", GTValues.VN[tier]))));
                    return;
                }
            }
        }
        if (Tooltips.magnetoresonaticcircuitSet.contains(item)) {
            for (int tier : GTMachines.ALL_TIERS) {
                if (tier > GTValues.UIV) return;
                if (GTOItems.MAGNETO_RESONATIC_CIRCUIT[tier].is(item)) {
                    event.getToolTip().add(Component.translatable("gtocore.tooltip.item.magneto_resonatic_circuit").withStyle(ChatFormatting.GRAY));
                    event.getToolTip().add(Component.translatable("gtocore.tooltip.item.tier_circuit", GTValues.VN[tier]).withStyle(ChatFormatting.LIGHT_PURPLE));
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelStageEvent event) {
        RenderLevelStageEvent.Stage stage = event.getStage();
        if (stage == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            LocalPlayer player = mc.player;
            if (level == null || player == null) return;
            PoseStack poseStack = event.getPoseStack();
            Camera camera = event.getCamera();
            ItemStack held = player.getMainHandItem();
            if (StructureWriteBehavior.isItemStructureWriter(held)) {
                BlockPos[] poses = StructureWriteBehavior.getPos(held);
                if (poses == null) return;
                Vec3 pos = camera.getPosition();

                poseStack.pushPose();
                poseStack.translate(-pos.x, -pos.y, -pos.z);

                RenderSystem.disableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.disableCull();
                RenderSystem.blendFunc(
                        GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder buffer = tesselator.getBuilder();

                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                RenderSystem.setShader(GameRenderer::getPositionColorShader);

                RenderBufferUtils.renderCubeFace(
                        poseStack,
                        buffer,
                        poses[0].getX(),
                        poses[0].getY(),
                        poses[0].getZ(),
                        poses[1].getX() + 1,
                        poses[1].getY() + 1,
                        poses[1].getZ() + 1,
                        0.2f,
                        0.2f,
                        1f,
                        0.25f,
                        true);

                tesselator.end();

                buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
                RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
                RenderSystem.lineWidth(3);

                RenderBufferUtils.drawCubeFrame(
                        poseStack,
                        buffer,
                        poses[0].getX(),
                        poses[0].getY(),
                        poses[0].getZ(),
                        poses[1].getX() + 1,
                        poses[1].getY() + 1,
                        poses[1].getZ() + 1,
                        0.0f,
                        0.0f,
                        1f,
                        0.5f);

                tesselator.end();

                RenderSystem.enableCull();

                RenderSystem.disableBlend();
                RenderSystem.enableDepthTest();
                poseStack.popPose();
            }
        }
    }
}
