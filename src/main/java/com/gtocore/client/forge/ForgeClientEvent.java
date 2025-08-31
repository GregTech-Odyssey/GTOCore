package com.gtocore.client.forge;

import com.gtocore.client.ClientCache;
import com.gtocore.client.Tooltips;
import com.gtocore.common.data.GTOCommands;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.item.StructureDetectBehavior;
import com.gtocore.common.item.StructureWriteBehavior;
import com.gtocore.common.network.ClientMessage;

import com.gtolib.GTOCore;
import com.gtolib.IItem;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.hepdd.gtmthings.common.block.machine.electric.WirelessEnergyMonitor;
import com.hepdd.gtmthings.data.CustomItems;
import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public final class ForgeClientEvent {

    public static int highlightingTime = 0;
    public static int highlightingRadius;
    public static BlockPos highlightingPos;
    private static boolean lastShiftState = false;

    private static final String ITEM_PREFIX = "item." + GTOCore.MOD_ID;
    private static final String BLOCK_PREFIX = "block." + GTOCore.MOD_ID;

    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (highlightingTime > 0) {
                highlightingTime--;
            }
            if (ClientCache.highlightTime > 0) {
                ClientCache.highlightTime--;
            }
        }
    }

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null) return;
        ItemStack stack = event.getItemStack();
        String translationKey = stack.getDescriptionId();
        if (translationKey.startsWith(ITEM_PREFIX) || translationKey.startsWith(BLOCK_PREFIX)) {
            String tooltipKey = translationKey + ".tooltip";
            if (I18n.exists(tooltipKey)) {
                event.getToolTip().add(1, Component.translatable(tooltipKey));
            }
        }
        // int maxStep = MultiStepItemHelper.getMaxStep(stack);
        // if (maxStep > 0) {
        // event.getToolTip().add(Component.translatable("gtocore.tooltip.item.craft_step",
        // MultiStepItemHelper.getStep(stack) + " / " + maxStep));
        // }
        Item item = stack.getItem();
        var arr = ((IItem) item).gtolib$getToolTips();
        if (arr != null) {
            for (int i = arr.length - 1; i >= 0; i--) {
                event.getToolTip().add(1, arr[i].get());
            }
        }
        var lang = Tooltips.TOOL_TIPS_MAP.get(item);
        if (lang != null) {
            for (int i = 0; i < lang.length(); i++) {
                event.getToolTip().add(Component.translatable("gtocore.tooltip.item." + ItemUtils.getIdLocation(item).getPath() + "." + i));
            }
        } else {
            String[] tooltips = Tooltips.TOOL_TIPS_KEY_MAP.get(item);
            if (tooltips != null) {
                if (tooltips.length == 1) event.getToolTip().add(Component.translatable(tooltips[0]));
                else event.getToolTip().add(Component.translatable(tooltips[0], tooltips[1]));
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (Minecraft.getInstance().player instanceof IEnhancedPlayer) {
            boolean isShiftDown = Screen.hasShiftDown();
            if (isShiftDown != lastShiftState) {
                ClientMessage.send("shiftKeypress", buf -> buf.writeBoolean(isShiftDown));
                lastShiftState = isShiftDown;
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
            BlockPos[] poses;
            if (highlightingTime > 0) {
                highlightSphere(camera, poseStack, highlightingPos, highlightingRadius);
            }
            if (WirelessEnergyMonitor.p > 0) {
                if (GTValues.CLIENT_TIME % 20L == 0L) {
                    --WirelessEnergyMonitor.p;
                }
                BlockPos pose = WirelessEnergyMonitor.pPos;
                if (pose == null) {
                    return;
                }
                highlightBlock(camera, poseStack, 0, 0, 1, pose, pose);
            }
            ItemStack itemStack = player.getMainHandItem();
            Item item = itemStack.getItem();
            if (item != Items.AIR && itemStack.hasTag()) {
                if (GTCEu.isDev() && StructureWriteBehavior.isItem(itemStack)) {
                    poses = StructureWriteBehavior.getPos(itemStack);
                    if (poses != null) {
                        highlightBlock(camera, poseStack, 0, 0, 1, poses);
                    }
                } else if (StructureDetectBehavior.isItem(itemStack)) {
                    poses = StructureDetectBehavior.getPos(itemStack);
                    if (poses != null && poses.length >= 1) {
                        if (poses[0] != null) {
                            highlightBlock(camera, poseStack, 0, 0, 1, poses[0], poses[0]);
                        }
                        if (poses.length == 2 && poses[1] != null) {
                            highlightBlock(camera, poseStack, 0, 0, 1, poses[1], poses[1]);
                        }
                    }
                } else if (Highlighting.HIGHLIGHTING_ITEM.contains(item)) {
                    var tag = itemStack.getTag();
                    BlockPos blockPos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
                    highlightBlock(camera, poseStack, 0, 0, 1, blockPos, blockPos);
                }
            }
            if (ClientCache.machineNotFormedHighlight) {
                MultiblockControllerMachine.HIGHLIGHT_CACHE.forEach((pos) -> highlightBlock(camera, poseStack, 1, 0.1f, 0.1f, pos, pos));
            }
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent evt) {
        GTOCommands.initClient(evt.getDispatcher());
    }

    private static void highlightBlock(Camera camera, PoseStack poseStack, float r, float g, float b, BlockPos... poses) {
        Vec3 pos = camera.getPosition();
        float lightR = (1.0f + r * 4f) / 5.0f;
        float lightG = (1.0f + g * 4f) / 5.0f;
        float lightB = (1.0f + b * 4f) / 5.0f;
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
        RenderBufferUtils.renderCubeFace(poseStack, buffer, poses[0].getX(), poses[0].getY(), poses[0].getZ(), poses[1].getX() + 1, poses[1].getY() + 1, poses[1].getZ() + 1, lightR, lightG, lightB, 0.25f, true);
        tesselator.end();
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.lineWidth(3);
        RenderBufferUtils.drawCubeFrame(poseStack, buffer, poses[0].getX(), poses[0].getY(), poses[0].getZ(), poses[1].getX() + 1, poses[1].getY() + 1, poses[1].getZ() + 1, r, g, b, 0.5f);
        tesselator.end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        poseStack.popPose();
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

    private static class Highlighting {

        private static final Set<Item> HIGHLIGHTING_ITEM = Set.of(
                CustomItems.WIRELESS_ITEM_TRANSFER_COVER.asItem(),
                CustomItems.WIRELESS_FLUID_TRANSFER_COVER.asItem(),
                CustomItems.ADVANCED_WIRELESS_ITEM_TRANSFER_COVER.asItem(),
                CustomItems.ADVANCED_WIRELESS_FLUID_TRANSFER_COVER.asItem(),
                GTOItems.COORDINATE_CARD.asItem());
    }
}
