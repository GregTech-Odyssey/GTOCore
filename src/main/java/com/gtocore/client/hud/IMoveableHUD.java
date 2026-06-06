package com.gtocore.client.hud;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import dev.emi.emi.config.EmiConfig;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@DataGeneratorScanned
public interface IMoveableHUD extends IGuiOverlay, GuiEventListener, Renderable {

    Map<String, IMoveableHUD> REGISTERED_HUDS = new LinkedHashMap<>();

    Set<IMoveableHUD> activeHuds = new ReferenceOpenHashSet<>();

    static void registerHUD(RegisterGuiOverlaysEvent event, String id, IMoveableHUD hud) {
        REGISTERED_HUDS.put(id, hud);
        event.registerAboveAll(id, hud);
    }

    @RegisterLanguage(cn = "左键开关HUD显示（已启用）", en = "Left click to toggle HUD display (Enabled)")
    String HUD_TOGGLE_ON = "gtocore.hud.toggle.on";
    @RegisterLanguage(cn = "左键开关HUD显示（已禁用）", en = "Left click to toggle HUD display (Disabled)")
    String HUD_TOGGLE_OFF = "gtocore.hud.toggle.off";
    @RegisterLanguage(cn = "右键可开启HUD拖拽模式", en = "Right click to enable HUD drag mode")
    String HUD_DRAG = "gtocore.hud.drag";

    @Override
    /// renderInGameHud
    default void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (!isEnabled() || mc.level == null || mc.options.renderDebug || mc.options.hideGui) {
            return;
        }
        renderGeneral(guiGraphics, partialTick, screenWidth, screenHeight);
    }

    /// renderInContainerScreen
    @Override
    default void render(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        renderGeneral(guiGraphics, v,
                screenWidth,
                screenHeight);
    }

    /// can store shared logic here
    default void renderGeneral(GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {}

    default Rect2i getPropertyAnchorBounds(int screenWidth, int screenHeight) {
        return getBounds(screenWidth, screenHeight);
    }

    Rect2i getBounds(int screenWidth, int screenHeight);

    @Override
    default boolean isMouseOver(double mouseX, double mouseY) {
        return getBounds(Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                Minecraft.getInstance().getWindow().getGuiScaledHeight()).contains((int) mouseX, (int) mouseY);
    }

    @Override
    default void setFocused(boolean b) {}

    @Override
    default boolean isFocused() {
        return false;
    }

    Component getDisplayName();

    default void toggleEnabled() {
        setEnabled(!isEnabled());
    }

    default void setEnabled(boolean enabled) {}

    default void setTopLeftPosition(int x, int y, int screenWidth, int screenHeight) {}

    default boolean isEnabled() {
        return false;
    }

    default boolean isPositionDragging() {
        return false;
    }

    static boolean addActiveHud(IMoveableHUD hud) {
        if (EmiConfig.enabled) EmiConfig.enabled = false;
        return activeHuds.add(hud);
    }

    static boolean removeActiveHud(IMoveableHUD hud) {
        var b = activeHuds.remove(hud);
        if (!EmiConfig.enabled && activeHuds.isEmpty()) EmiConfig.enabled = true;
        return b;
    }

    @Mod.EventBusSubscriber(modid = GTOCore.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    class EventHandler {

        @SubscribeEvent
        public static void onGuiRender(ScreenEvent.Render.Post event) {
            if (event.getScreen() instanceof HUDScreen) return;
            for (IMoveableHUD hud : activeHuds) {
                if (hud.isEnabled()) {
                    hud.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
                }
            }
        }

        @SubscribeEvent
        public static void onMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
            if (event.getScreen() instanceof HUDScreen) return;
            boolean handled = false;
            for (IMoveableHUD hud : activeHuds) {
                if (hud.isEnabled() && !handled) {
                    handled = hud.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton());
                }
            }
            event.setCanceled(handled);
        }

        @SubscribeEvent
        public static void onMouseDragged(ScreenEvent.MouseDragged.Pre event) {
            if (event.getScreen() instanceof HUDScreen) return;
            boolean handled = false;
            for (IMoveableHUD hud : activeHuds) {
                if (hud.isEnabled() && !handled) {
                    handled = hud.mouseDragged(event.getMouseX(), event.getMouseY(),
                            event.getMouseButton(), event.getDragX(), event.getDragY());
                }
            }
            event.setCanceled(handled);
        }

        @SubscribeEvent
        public static void onMouseReleased(ScreenEvent.MouseButtonReleased.Pre event) {
            if (event.getScreen() instanceof HUDScreen) return;
            boolean handled = false;
            for (IMoveableHUD hud : activeHuds) {
                if (hud.isEnabled() && !handled) {
                    handled = hud.mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton());
                }
            }
            event.setCanceled(handled);
        }

        @SubscribeEvent
        public static void onClosing(ScreenEvent.Closing event) {
            if (event.getScreen() instanceof HUDScreen) return;
            activeHuds.clear();
            EmiConfig.enabled = true;
        }
    }

    static void drawOutline(GuiGraphics guiGraphics, Rect2i bounds, int color) {
        if (bounds == null || bounds.getWidth() <= 0 || bounds.getHeight() <= 0) {
            return;
        }
        int left = bounds.getX();
        int top = bounds.getY();
        int right = left + bounds.getWidth() - 1;
        int bottom = top + bounds.getHeight() - 1;
        guiGraphics.hLine(left, right, top, color);
        guiGraphics.hLine(left, right, bottom, color);
        guiGraphics.vLine(left, top, bottom, color);
        guiGraphics.vLine(right, top, bottom, color);
    }
}
