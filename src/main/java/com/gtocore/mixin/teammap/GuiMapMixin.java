package com.gtocore.mixin.teammap;

import com.gtocore.integration.teammap.bridge.GuiMapBridge;
import com.gtocore.integration.teammap.client.NativeGtTeamOverlay;
import com.gtocore.integration.teammap.client.TeamOverlayRenderer;

import com.gregtechceu.gtceu.integration.map.GroupingMapRenderer;
import com.gregtechceu.gtceu.integration.map.layer.builtin.BedrockOreRenderLayer;
import com.gregtechceu.gtceu.integration.map.layer.builtin.FluidRenderLayer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.gui.GuiMap;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(value = GuiMap.class, remap = false)
public abstract class GuiMapMixin implements GuiMapBridge {

    @Shadow
    private double cameraX;
    @Shadow
    private double cameraZ;
    @Shadow
    private double scale;
    @Shadow
    private ResourceKey<Level> lastViewedDimensionId;
    @Shadow
    private int mouseBlockPosX;
    @Shadow
    private int mouseBlockPosZ;
    @Shadow
    private ResourceKey<Level> mouseBlockDim;

    @Unique
    private boolean gtoTeamMap$enabled;
    @Unique
    private Button gtoTeamMap$button;

    @Override
    public double gtoTeamMap$getCameraX() {
        return cameraX;
    }

    @Override
    public double gtoTeamMap$getCameraZ() {
        return cameraZ;
    }

    @Override
    public double gtoTeamMap$getScale() {
        return scale;
    }

    @Override
    public ResourceKey<Level> gtoTeamMap$getViewedDimension() {
        return lastViewedDimensionId;
    }

    @Inject(method = "m_7856_", at = @At("TAIL"))
    private void gtoTeamMap$addToggleButton(CallbackInfo ci) {
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        gtoTeamMap$button = Button.builder(Component.literal("队"), this::gtoTeamMap$toggle)
                .bounds(0, Math.max(0, height - 80), 20, 20)
                .build();
        ((GuiMap) (Object) this).addButton(gtoTeamMap$button);
        gtoTeamMap$updateButton();
    }

    @Unique
    private void gtoTeamMap$toggle(Button ignored) {
        if (!com.gtocore.integration.teammap.client.ClientTeamData.hasTeam()) return;
        gtoTeamMap$enabled = !gtoTeamMap$enabled;
        gtoTeamMap$updateButton();
    }

    @Unique
    private void gtoTeamMap$updateButton() {
        if (gtoTeamMap$button == null) return;
        boolean hasTeam = com.gtocore.integration.teammap.client.ClientTeamData.hasTeam();
        if (!hasTeam) gtoTeamMap$enabled = false;
        gtoTeamMap$button.active = hasTeam;
        gtoTeamMap$button.setMessage(Component.literal("队").withStyle(
                gtoTeamMap$enabled ? ChatFormatting.GREEN : ChatFormatting.WHITE));
        String key = !hasTeam ? "gui.gto_team_map_share.no_team" : gtoTeamMap$enabled ? "gui.gto_team_map_share.team_map_on" : "gui.gto_team_map_share.team_map_off";
        gtoTeamMap$button.setTooltip(Tooltip.create(Component.translatable(key)));
    }

    @Inject(method = "m_88315_",
            slice = @Slice(from = @At(value = "INVOKE",
                                      target = "Lxaero/map/graphics/renderer/multitexture/MultiTextureRenderTypeRendererProvider;draw(Lxaero/map/graphics/renderer/multitexture/MultiTextureRenderTypeRenderer;)V",
                                      ordinal = 2)),
            at = @At(value = "INVOKE",
                     target = "Lcom/mojang/blaze3d/vertex/PoseStack;m_85841_(FFF)V",
                     ordinal = 0,
                     shift = At.Shift.AFTER))
    private void gtoTeamMap$renderTeamLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTick,
                                            CallbackInfo ci) {
        gtoTeamMap$updateButton();
        if (!gtoTeamMap$enabled) return;
        TeamOverlayRenderer.render(graphics, (GuiMap) (Object) this);
    }

    @Inject(method = "renderPreDropdown", at = @At("TAIL"))
    private void gtoTeamMap$renderTeamProspectionTooltip(GuiGraphics graphics, int mouseX, int mouseY,
                                                         float partialTick, CallbackInfo ci) {
        if (mouseBlockDim == null || !com.gtocore.integration.teammap.client.ClientTeamData.hasTeam()) return;
        int chunkX = Math.floorDiv(mouseBlockPosX, 16);
        int chunkZ = Math.floorDiv(mouseBlockPosZ, 16);
        GroupingMapRenderer renderer = GroupingMapRenderer.getInstance();
        ArrayList<Component> tooltip = new ArrayList<>();
        if (renderer.doShowLayer("bedrock_fluids")) {
            var fluid = NativeGtTeamOverlay.teamFluid(mouseBlockDim, chunkX, chunkZ);
            if (fluid != null) tooltip.addAll(FluidRenderLayer.getTooltip(fluid));
        }
        if (renderer.doShowLayer("bedrock_ore_veins")) {
            var ores = NativeGtTeamOverlay.teamBedrockOre(mouseBlockDim, chunkX, chunkZ);
            if (ores != null && ores.length > 0) tooltip.addAll(BedrockOreRenderLayer.getTooltip(ores));
        }
        if (!tooltip.isEmpty()) graphics.renderTooltip(Minecraft.getInstance().font, tooltip,
                Optional.empty(), mouseX, mouseY);
    }
}
