package com.gtocore.client.renderer.machine;

import com.gtocore.common.machine.monitor.*;
import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.client.renderer.machine.MachineRenderer;
import com.gregtechceu.gtceu.client.util.StaticFaceBakery;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class MonitorRenderer extends MachineRenderer {

    public static final ResourceLocation MONITOR_OVERLAY = GTOCore.id("block/overlay/machine/overlay_monitor");
    public static final ResourceLocation MONITOR_OVERLAY_FULL_CTM = GTOCore.id("block/overlay/machine/overlay_monitor_full_ctm");
    private static final Map<BlockEntity, Manager.GridNetwork> renderedThisTick = new ConcurrentHashMap<>();
    public static final Map<Manager.GridFacedPoint, Manager.GridNetwork> gridToNetworkCLIENT = new ConcurrentHashMap<>();
    // Minimum width for text rendering, measured in pixels.
    // This ensures that text elements are not rendered too small to be legible.
    private static final int MIN_WIDTH_TEXT = 100;
    private static final float MARGIN = 0.15f;

    public MonitorRenderer() {
        super(GTCEu.id("block/cube/tinted/all"));
        this.setTextureOverride(Map.of("all", GTCEu.id("block/casings/solid/machine_casing_solid_steel")));
    }

    @Override
    public void renderMachine(List<BakedQuad> quads, MachineDefinition definition, @Nullable MetaMachine machine, Direction frontFacing, @Nullable Direction side, RandomSource rand, Direction modelFacing, ModelState modelState) {
        super.renderMachine(quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
        if (!(machine instanceof IMonitor monitor)) {
            // machine is null when rendering is at GUI context
            // use the default monitor overlay
            quads.add(StaticFaceBakery.bakeFace(frontFacing, ModelFactory.getBlockSprite(MONITOR_OVERLAY)));
            return;
        }
        Manager.MonitorCTM ctm = Manager.MonitorCTM.getConnection(
                monitor.getFrontFacing(),
                monitor.getPos(),
                monitor.getLevel());
        Direction trueFacing = RelativeDirection.FRONT.getRelative(machine.getFrontFacing(), machine.getUpwardsFacing(), false);
        trueFacing = ModelFactory.modelFacing(frontFacing, trueFacing);
        var cube = StaticFaceBakery.SLIGHTLY_OVER_BLOCK;
        quads.add(
                StaticFaceBakery.bakeQuad(
                        new Vector3f((float) cube.minX * 16.0F, (float) cube.minY * 16.0F, (float) cube.minZ * 16.0F),
                        new Vector3f((float) cube.maxX * 16.0F, (float) cube.maxY * 16.0F, (float) cube.maxZ * 16.0F),
                        new BlockElementFace(
                                null,
                                -1,
                                "",
                                new BlockFaceUV(new float[] {
                                        (float) ctm.getU() / FULL_CTM_WIDTH * 16.0f,
                                        (float) ctm.getV() / FULL_CTM_HEIGHT * 16.0f,
                                        (ctm.getU() + 16.0F) / FULL_CTM_WIDTH * 16.0f,
                                        (ctm.getV() + 16.0F) / FULL_CTM_HEIGHT * 16.0f }, 0)),
                        ModelFactory.getBlockSprite(MONITOR_OVERLAY_FULL_CTM),
                        trueFacing,
                        ModelFactory.getRotation(frontFacing),
                        null,
                        true,
                        0));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onAdditionalModel(Consumer<ResourceLocation> registry) {
        super.onAdditionalModel(registry);
        registry.accept(GTOCore.id("obj/neutron_star"));
    }

    @Override
    public boolean isGlobalRenderer(BlockEntity blockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(BlockEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasTESR(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof IMachineBlockEntity holder && holder.getMetaMachine() instanceof IMonitor monitor && monitor.getLevel() != null) {
            Manager.GridNetwork network = Manager.GridNetwork.fromClientBlock(
                    monitor.getFrontFacing(),
                    monitor.getPos(),
                    monitor.getLevel());
            if (network != null &&
                    (renderedThisTick.containsKey(blockEntity) || renderedThisTick.values().stream().noneMatch(n -> n.equals(network)))) {
                renderedThisTick.put(blockEntity, network);
                {
                    stack.pushPose();

                    var box = Shapes.create(network.aabb());
                    var pos = monitor.getPos();
                    if (GTOConfig.INSTANCE.dev) {
                        LevelRenderer.renderVoxelShape(
                                stack,
                                buffer.getBuffer(RenderType.lines()),
                                box,
                                -pos.getX(),
                                -pos.getY(),
                                -pos.getZ(),
                                0f,
                                1f,
                                0f,
                                1f,
                                false);
                    }

                    var front = monitor.getFrontFacing();
                    var yaw = front.toYRot() + 180.0f;
                    var pitch = toPitchAngle(front);

                    stack.translate(
                            network.getOriginPos().getX() - pos.getX() + (front == Direction.WEST ? -0.02f : 1.02f),
                            network.getOriginPos().getY() - pos.getY() + (front == Direction.DOWN ? -0.02f : 1.02f),
                            network.getOriginPos().getZ() - pos.getZ() + (front == Direction.NORTH ? -0.02f : 1.02f));

                    stack.mulPose(Axis.YN.rotationDegrees(yaw));
                    stack.mulPose(Axis.XP.rotationDegrees(pitch));
                    var font = Minecraft.getInstance().font;
                    List<IDisplayComponent> info = network.getForDisplay();
                    if (info.isEmpty()) {
                        info.add(DisplayComponent.text(GTOCore.id("null"), Component.translatable("gtocore.machine.monitor.no_information").getVisualOrderText()));
                    }
                    {
                        stack.pushPose();
                        stack.translate(
                                switch (front) {
                                    case WEST, SOUTH -> 1 - MARGIN;
                                    case EAST, NORTH -> network.width3D() - 1 - MARGIN;
                                    default -> 0f;
                                },
                                network.height3D() - 1 - MARGIN,
                                -0f);
                        var factor = getGlobalScaleFactor(info, MIN_WIDTH_TEXT, network.width3D() - MARGIN * 2f, network.height3D() - MARGIN * 2f);
                        stack.scale(-factor, -factor, -factor);
                        int cumulatedHeight = 0;
                        for (IDisplayComponent iDisplayComponent : info) {
                            if (iDisplayComponent.getDisplayType() == DisplayType.STYLED_TEXT) {
                                FormattedCharSequence text = iDisplayComponent.getDisplayValue();
                                font.drawInBatch(text, 0, cumulatedHeight, 0xFFFFFFFF, false, stack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0x000000, LightTexture.FULL_BRIGHT);
                            } else if (iDisplayComponent.getDisplayType() == DisplayType.CUSTOM_RENDERER) {
                                stack.pushPose();
                                iDisplayComponent.renderDisplay(network, blockEntity, partialTicks, stack, buffer, combinedLight, combinedOverlay);
                                stack.popPose();
                            }
                            cumulatedHeight += iDisplayComponent.getVisualHeight() + 2;
                        }
                        stack.popPose();
                    }
                    stack.popPose();
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static float toPitchAngle(Direction dir) {
        return switch (dir) {
            case DOWN -> 90.0f;
            case UP -> 270.0f;
            default -> 0.0f;
        };
    }

    private static float getGlobalScaleFactor(List<IDisplayComponent> info, int minWidth, float displayWidth, float displayHeight) {
        if (info == null || info.isEmpty()) {
            return 1.0f;
        }
        var maxWidth = info.stream().mapToInt(IDisplayComponent::getVisualWidth).max().orElse(0);
        maxWidth = Math.max(maxWidth, minWidth) + 10;
        var maxHeight = info.stream().mapToInt(IDisplayComponent::getVisualHeight).sum() + info.size() * 4 + 10;
        var scaleX = displayWidth / (maxWidth + 0.01f);
        var scaleY = displayHeight / (maxHeight + 0.01f);
        return Math.min(scaleX, scaleY);
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("deprecation")
    public void onPrepareTextureAtlas(ResourceLocation atlasName, Consumer<ResourceLocation> register) {
        super.onPrepareTextureAtlas(atlasName, register);
        if (atlasName.equals(TextureAtlas.LOCATION_BLOCKS)) {
            register.accept(MONITOR_OVERLAY);
            register.accept(MONITOR_OVERLAY_FULL_CTM);
        }
    }

    @Mod.EventBusSubscriber(Dist.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public static class RenderedCacheClearer {

        @SubscribeEvent()
        public static void clearRenderCache(ScreenEvent.Render.Pre event) {
            renderedThisTick.clear();
        }
    }

    private static final int FULL_CTM_WIDTH = 64;
    private static final int FULL_CTM_HEIGHT = 64;
}
