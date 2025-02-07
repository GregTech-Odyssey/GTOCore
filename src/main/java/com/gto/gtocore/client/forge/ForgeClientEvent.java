package com.gto.gtocore.client.forge;

import com.gto.gtocore.client.Tooltips;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.item.StructureDetectBehavior;
import com.gto.gtocore.common.item.StructureWriteBehavior;
import com.gto.gtocore.common.machine.multiblock.water.WaterPurificationPlantMachine;
import com.gto.gtocore.data.lang.LangHandler;
import com.gto.gtocore.utils.ItemUtils;
import com.gto.gtocore.utils.StringUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;

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

import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

import java.util.function.Supplier;

import static com.hepdd.gtmthings.client.ForgeClientEventHandler.highlightBlock;

@OnlyIn(Dist.CLIENT)
public final class ForgeClientEvent {

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null) return;
        Item item = event.getItemStack().getItem();
        LangHandler.ENCNS lang = Tooltips.TOOL_TIPS_MAP.get(item);
        if (lang != null) {
            for (int i = 0; i < lang.length(); i++) {
                event.getToolTip().add(Component.translatable("gtocore.tooltip.item." + ItemUtils.getIdLocation(item).getPath() + "." + i));
            }
            Supplier<String> supplier = Tooltips.FLICKER_TOOL_TIPS_MAP.get(item);
            if (supplier != null) {
                event.getToolTip().add(Component.literal(supplier.get()));
            }
            return;
        }
        if (Tooltips.suprachronalCircuitSet.contains(item)) {
            for (int tier : GTMachineUtils.ALL_TIERS) {
                if (GTOItems.SUPRACHRONAL_CIRCUIT[tier].is(item)) {
                    event.getToolTip().add(Component.translatable("gtocore.tooltip.item.suprachronal_circuit").withStyle(ChatFormatting.GRAY));
                    event.getToolTip().add(Component.literal(StringUtils.white_blue(I18n.get("gtocore.tooltip.item.tier_circuit", GTValues.VN[tier]))));
                    return;
                }
            }
        }
        if (Tooltips.magnetoresonaticcircuitSet.contains(item)) {
            for (int tier : GTMachineUtils.ALL_TIERS) {
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
            BlockPos[] poses;
            if (WaterPurificationPlantMachine.time > 0) {
                if (GTValues.CLIENT_TIME % 20 == 0) {
                    WaterPurificationPlantMachine.time--;
                }
                highlightSphere(camera, poseStack, WaterPurificationPlantMachine.pos, 32);
            }
            if (StructureWriteBehavior.isItem(held)) {
                poses = StructureWriteBehavior.getPos(held);
                if (poses != null) {
                    highlightBlock(camera, poseStack, poses);
                }
            } else if (StructureDetectBehavior.isItem(held)) {
                poses = StructureDetectBehavior.getPos();
                if (poses != null) {
                    if (poses[0] != null) {
                        highlightBlock(camera, poseStack, poses[0], poses[0]);
                    }
                    if (poses[1] != null) {
                        highlightBlock(camera, poseStack, poses[1], poses[1]);
                    }
                }
            }
        }
    }

    private static void highlightSphere(Camera camera, PoseStack poseStack, BlockPos blockPos, float radius) {
        Vec3 pos = camera.getPosition();
        poseStack.pushPose();
        poseStack.translate(-pos.x, -pos.y, -pos.z);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderBufferUtils.shapeSphere(poseStack, buffer, blockPos.getX(), blockPos.getY(), blockPos.getZ(), radius, 40, 50, 0.2f, 0.2f, 1.0f, 0.25f);
        tesselator.end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        poseStack.popPose();
    }
}
