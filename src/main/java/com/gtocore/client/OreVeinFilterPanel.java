package com.gtocore.client;

import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.integration.xei.widgets.GTOreVeinWidget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Overlay panel drawn directly on the Xaero world map for filtering which GregTech ore veins are
 * shown. Toggling a row updates {@link OreVeinFilter} immediately, so the live map behind the panel
 * reflects the change on the next frame. Only veins that can generate in the dimension the map is
 * currently showing are listed; a search box (pinyin-aware via {@link PinyinMatch}) narrows the list.
 * State is fully static since only one world map is open at a time. Both the toggle button and the
 * panel are drawn and click-tested here, driven by {@code GuiMapOreVeinFilterMixin}.
 */
@OnlyIn(Dist.CLIENT)
public final class OreVeinFilterPanel {

    private static final int PANEL_WIDTH = 240;
    private static final int ROW_HEIGHT = 14;
    private static final int CHECKBOX = 10;
    private static final int CONTROL_H = 14;
    private static final int SEARCH_H = 14;
    private static final int BTN = 20;

    public static boolean OPEN = false;

    private record VeinEntry(String oreName, String display, Set<ResourceKey<Level>> dims) {}

    private static final List<VeinEntry> ALL = new ArrayList<>();
    private static String query = "";
    private static int scrollOffset;
    // The search box only receives keys once clicked; until then keys fall through to the map.
    private static boolean focused;

    private OreVeinFilterPanel() {}

    public static void toggleOpen() {
        OPEN = !OPEN;
        focused = false;
        if (OPEN) {
            query = "";
            scrollOffset = 0;
        } else {
            OreVeinFilter.save();
        }
    }

    public static void close() {
        if (OPEN) {
            OPEN = false;
            focused = false;
            OreVeinFilter.save();
        }
    }

    private static void ensureBuilt() {
        if (!ALL.isEmpty()) return;
        for (GTOreDefinition def : GTRegistries.ORE_VEINS.values()) {
            String oreName = GTOreVeinWidget.getOreName(def);
            String display = Component.translatable("gtceu.jei.ore_vein." + oreName).getString();
            ALL.add(new VeinEntry(oreName, display, def.dimensionFilter()));
        }
        ALL.sort((a, b) -> a.display.compareToIgnoreCase(b.display));
    }

    /** Veins that can spawn in the displayed dimension and match the current search query. */
    private static List<VeinEntry> visible(ResourceKey<Level> dim) {
        ensureBuilt();
        List<VeinEntry> out = new ArrayList<>();
        for (VeinEntry e : ALL) {
            // empty dimensionFilter means the vein is not dimension-restricted -> always show
            if (dim != null && !e.dims.isEmpty() && !e.dims.contains(dim)) continue;
            if (!PinyinMatch.matches(e.display, query)) continue;
            out.add(e);
        }
        return out;
    }

    private static int panelHeight(int screenH) {
        return Math.min(screenH - 40, 240);
    }

    private static int panelX(int screenW) {
        return (screenW - PANEL_WIDTH) / 2;
    }

    private static int panelY(int screenH) {
        return (screenH - panelHeight(screenH)) / 2;
    }

    private static int searchY(int screenH) {
        return panelY(screenH) + 38;
    }

    private static int listTop(int screenH) {
        return panelY(screenH) + 38 + SEARCH_H + 4;
    }

    private static int listBottom(int screenH) {
        return panelY(screenH) + panelHeight(screenH) - 6;
    }

    /** The 20x20 toggle button drawn on the map (above GregTech's ore-vein layer toggle). */
    public static boolean inButton(double mx, double my, int x, int y) {
        return inRect(mx, my, x, y, BTN, BTN);
    }

    public static void renderButton(GuiGraphics g, int x, int y, int mouseX, int mouseY) {
        boolean hover = inButton(mouseX, mouseY, x, y);
        int bg = OPEN ? 0xC0303030 : (hover ? 0xC0202020 : 0x90101010);
        g.fill(x, y, x + BTN, y + BTN, bg);
        g.renderOutline(x, y, BTN, BTN, (hover || OPEN) ? 0xFFFFFFFF : 0xFF909090);
        int color = OPEN ? 0xFF8CE38C : 0xFFFFFFFF; // hamburger glyph, green while open
        int bx = x + 5;
        int cy = y + 6;
        g.fill(bx, cy, bx + 10, cy + 2, color);
        g.fill(bx, cy + 4, bx + 10, cy + 6, color);
        g.fill(bx, cy + 8, bx + 10, cy + 10, color);
    }

    public static void render(GuiGraphics g, Font font, int screenW, int screenH, int mouseX, int mouseY, ResourceKey<Level> dim) {
        List<VeinEntry> list = visible(dim);
        int px = panelX(screenW);
        int py = panelY(screenH);
        int ph = panelHeight(screenH);
        int top = listTop(screenH);
        int bottom = listBottom(screenH);
        clampScroll(list, screenH);

        g.fill(px, py, px + PANEL_WIDTH, py + ph, 0xF0101010);
        g.renderOutline(px, py, PANEL_WIDTH, ph, 0xFF8080A0);
        g.drawString(font, Component.translatable("gtocore.screen.ore_vein_filter.title"), px + 8, py + 7, 0xFFFFFFFF, false);

        // close (X)
        int xx = px + PANEL_WIDTH - 14;
        boolean xHover = inRect(mouseX, mouseY, xx, py + 4, 10, 10);
        g.drawString(font, "x", xx + 2, py + 6, xHover ? 0xFFFF6060 : 0xFFB0B0B0, false);

        // show all / hide all (operate on the currently visible subset)
        int ctrlY = py + 22;
        int ctrlW = (PANEL_WIDTH - 24) / 2;
        drawControl(g, font, px + 8, ctrlY, ctrlW, mouseX, mouseY, "gtocore.screen.ore_vein_filter.show_all");
        drawControl(g, font, px + 16 + ctrlW, ctrlY, ctrlW, mouseX, mouseY, "gtocore.screen.ore_vein_filter.hide_all");

        // search box
        int sx = px + 8;
        int sy = searchY(screenH);
        int sw = PANEL_WIDTH - 16;
        g.fill(sx, sy, sx + sw, sy + SEARCH_H, 0xC0000000);
        g.renderOutline(sx, sy, sw, SEARCH_H, focused ? 0xFFFFFFFF : 0xFF707070);
        int textY = sy + (SEARCH_H - font.lineHeight) / 2;
        if (query.isEmpty() && !focused) {
            g.drawString(font, Component.translatable("gtocore.screen.ore_vein_filter.search"), sx + 4, textY, 0xFF707070, false);
        } else {
            g.drawString(font, focused ? query + "_" : query, sx + 4, textY, 0xFFFFFFFF, false);
        }

        g.enableScissor(px + 4, top, px + PANEL_WIDTH - 4, bottom);
        int y = top - scrollOffset;
        for (VeinEntry e : list) {
            if (y + ROW_HEIGHT >= top && y <= bottom) {
                boolean hovered = mouseY >= top && mouseY < bottom && inRect(mouseX, mouseY, px + 8, y, PANEL_WIDTH - 22, ROW_HEIGHT);
                if (hovered) g.fill(px + 6, y, px + PANEL_WIDTH - 8, y + ROW_HEIGHT, 0x40FFFFFF);
                boolean shown = !OreVeinFilter.isHidden(e.oreName);
                int boxY = y + (ROW_HEIGHT - CHECKBOX) / 2;
                g.renderOutline(px + 8, boxY, CHECKBOX, CHECKBOX, 0xFFB0B0B0);
                if (shown) g.fill(px + 10, boxY + 2, px + 8 + CHECKBOX - 2, boxY + CHECKBOX - 2, 0xFF4CC94C);
                g.drawString(font, e.display, px + 8 + CHECKBOX + 5, y + (ROW_HEIGHT - font.lineHeight) / 2,
                        shown ? 0xFFFFFFFF : 0xFF808080, false);
            }
            y += ROW_HEIGHT;
        }
        g.disableScissor();

        renderScrollbar(g, list, screenW, screenH);
    }

    private static void drawControl(GuiGraphics g, Font font, int x, int y, int w, int mouseX, int mouseY, String key) {
        boolean hover = inRect(mouseX, mouseY, x, y, w, CONTROL_H);
        g.fill(x, y, x + w, y + CONTROL_H, hover ? 0xC0404040 : 0xC0202020);
        g.renderOutline(x, y, w, CONTROL_H, 0xFF707070);
        Component label = Component.translatable(key);
        g.drawString(font, label, x + (w - font.width(label)) / 2, y + (CONTROL_H - font.lineHeight) / 2, 0xFFFFFFFF, false);
    }

    private static void renderScrollbar(GuiGraphics g, List<VeinEntry> list, int screenW, int screenH) {
        int top = listTop(screenH);
        int bottom = listBottom(screenH);
        int viewport = bottom - top;
        int content = list.size() * ROW_HEIGHT;
        if (content <= viewport) return;
        int barX = panelX(screenW) + PANEL_WIDTH - 6;
        g.fill(barX, top, barX + 3, bottom, 0x40FFFFFF);
        int thumbH = Math.max(12, (int) ((float) viewport / content * viewport));
        int maxScroll = content - viewport;
        int thumbY = top + (int) ((float) scrollOffset / maxScroll * (viewport - thumbH));
        g.fill(barX, thumbY, barX + 3, thumbY + thumbH, 0xFFB0B0B0);
    }

    public static boolean mouseClicked(double mouseX, double mouseY, int button, int screenW, int screenH, ResourceKey<Level> dim) {
        if (button != 0) return false;
        int px = panelX(screenW);
        int py = panelY(screenH);
        int ph = panelHeight(screenH);
        if (!inRect(mouseX, mouseY, px, py, PANEL_WIDTH, ph)) {
            focused = false; // clicking the map outside the panel drops search focus
            return false;
        }

        if (inRect(mouseX, mouseY, px + PANEL_WIDTH - 14, py + 4, 10, 10)) {
            close();
            return true;
        }
        // Clicking the search box focuses it; clicking anything else in the panel unfocuses it.
        focused = inRect(mouseX, mouseY, px + 8, searchY(screenH), PANEL_WIDTH - 16, SEARCH_H);
        if (focused) {
            return true;
        }
        List<VeinEntry> list = visible(dim);
        int ctrlY = py + 22;
        int ctrlW = (PANEL_WIDTH - 24) / 2;
        if (inRect(mouseX, mouseY, px + 8, ctrlY, ctrlW, CONTROL_H)) {
            for (VeinEntry e : list) OreVeinFilter.setHidden(e.oreName, false);
            OreVeinFilter.save();
            return true;
        }
        if (inRect(mouseX, mouseY, px + 16 + ctrlW, ctrlY, ctrlW, CONTROL_H)) {
            for (VeinEntry e : list) OreVeinFilter.setHidden(e.oreName, true);
            OreVeinFilter.save();
            return true;
        }
        int top = listTop(screenH);
        int bottom = listBottom(screenH);
        if (mouseY >= top && mouseY < bottom) {
            int index = (int) ((mouseY - top + scrollOffset) / ROW_HEIGHT);
            if (index >= 0 && index < list.size()) {
                OreVeinFilter.toggle(list.get(index).oreName);
                OreVeinFilter.save();
            }
        }
        return true; // consume any click inside the panel
    }

    public static boolean mouseScrolled(double mouseX, double mouseY, double delta, int screenW, int screenH, ResourceKey<Level> dim) {
        int px = panelX(screenW);
        int py = panelY(screenH);
        int ph = panelHeight(screenH);
        if (!inRect(mouseX, mouseY, px, py, PANEL_WIDTH, ph)) return false;
        scrollOffset -= (int) (delta * ROW_HEIGHT);
        clampScroll(visible(dim), screenH);
        return true;
    }

    public static boolean charTyped(char chr) {
        if (!focused) return false; // keys only go to the box once it has been clicked
        if (chr < ' ' || chr == 127) return false;
        query += chr;
        scrollOffset = 0;
        return true;
    }

    public static boolean keyPressed(int keyCode) {
        if (!focused) return false; // not typing -> let map shortcuts / keybinds work
        switch (keyCode) {
            case GLFW.GLFW_KEY_BACKSPACE -> {
                if (!query.isEmpty()) query = query.substring(0, query.length() - 1);
                scrollOffset = 0;
                return true;
            }
            case GLFW.GLFW_KEY_ESCAPE -> {
                if (!query.isEmpty()) {
                    query = ""; // first ESC clears the text
                } else {
                    focused = false; // then ESC just drops focus, leaving the panel open
                }
                scrollOffset = 0;
                return true;
            }
            default -> {
                // Focused: swallow every other key so map shortcuts / keybinds do not fire underneath.
                return true;
            }
        }
    }

    private static void clampScroll(List<VeinEntry> list, int screenH) {
        int max = Math.max(0, list.size() * ROW_HEIGHT - (listBottom(screenH) - listTop(screenH)));
        scrollOffset = Math.max(0, Math.min(scrollOffset, max));
    }

    private static boolean inRect(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }
}
