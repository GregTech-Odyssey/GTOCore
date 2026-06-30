package com.gtocore.mixin.ae2.eae;

import com.gtocore.integration.ae.PatternEncoderStats;
import com.gtocore.integration.ae.client.PatternEncoderStatsButton;
import com.gtocore.integration.ae.client.PatternEncoderStatsScreen;

import com.gtolib.api.ae2.gui.hooks.IExtendedGuiEx;
import com.gtolib.api.ae2.me2in1.Me2in1Menu;
import com.gtolib.api.ae2.me2in1.Me2in1Screen;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.patternaccess.PatternContainerRecord;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AETextField;
import appeng.client.gui.widgets.Scrollbar;
import appeng.client.gui.widgets.ServerSettingToggleButton;

import gto_ae.api.config.ExtendedSettings;
import gto_ae.menu.ShowMolecularAssembler;

import com.glodblock.github.extendedae.client.button.HighlightButton;
import com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal;
import com.glodblock.github.extendedae.container.ContainerExPatternTerminal;
import com.google.common.collect.HashMultimap;
import com.gto.fastcollection.OpenCacheHashSet;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(GuiExPatternTerminal.class)
public abstract class GuiExPatternTerminalMixin<T extends ContainerExPatternTerminal> extends AEBaseScreen<T> implements IExtendedGuiEx {

    @Unique
    private static final int gto$COLUMNS = 9;
    @Unique
    private static final int gto$TEXT_WIDTH = 145;
    @Unique
    private static final int gto$TEXT_SCROLL_PAUSE_MS = 500;
    @Unique
    private static final int gto$TEXT_SCROLL_PIXELS_PER_SECOND = 30;
    @Unique
    private PatternContainerGroup gto$scrollingGroup;
    @Unique
    private long gto$scrollingGroupHoverStart;

    @Shadow(remap = false)
    @Final
    private Map<String, Set<Object>> cachedSearches;
    @Shadow(remap = false)
    @Final
    private AETextField searchOutField;
    @Shadow(remap = false)
    @Final
    private AETextField searchInField;

    @Shadow(remap = false)
    @Final
    private HashMultimap<PatternContainerGroup, PatternContainerRecord> byGroup;
    @Shadow(remap = false)
    @Final
    private HashMap<Integer, HighlightButton> highlightBtns;
    @Shadow(remap = false)
    @Final
    private Set<ItemStack> matchedStack;
    @Shadow(remap = false)
    @Final
    private Set<PatternContainerRecord> matchedProvider;
    @Shadow(remap = false)
    @Final
    private HashMap<Long, PatternContainerRecord> byId;

    @Shadow(remap = false)
    protected abstract boolean itemStackMatchesSearchTerm(ItemStack itemStack, List<String> filterTokens, boolean checkOut);

    @Shadow(remap = false)
    @Final
    private ArrayList<PatternContainerGroup> groups;
    @Shadow(remap = false)
    @Final
    private ArrayList<Object> rows;

    @Shadow(remap = false)
    protected abstract int getMaxRows();

    @Shadow(remap = false)
    @Final
    private HashMap<Long, GuiExPatternTerminal.PatternProviderInfo> infoMap;

    @Shadow(remap = false)
    protected abstract double playerToBlockDis(BlockPos pos);

    @Shadow(remap = false)
    protected abstract void resetScrollbar();

    @Unique
    private ServerSettingToggleButton<ShowMolecularAssembler> gtolib$showMolecularAssembler;

    protected GuiExPatternTerminalMixin(T menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onInit(ContainerExPatternTerminal menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {
        gtolib$showMolecularAssembler = new ServerSettingToggleButton<>(ExtendedSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS,
                ShowMolecularAssembler.ALL);
        this.addToLeftToolbar(gtolib$showMolecularAssembler);
        this.addToLeftToolbar(new PatternEncoderStatsButton(btn -> switchToScreen(
                new PatternEncoderStatsScreen<>(this, PatternEncoderStats.collect(byId.values())))));

        if (((AEBaseScreen<?>) this) instanceof Me2in1Screen<?>) {
            this.searchInField.setTooltipMessage(Collections.singletonList(Component.translatable("gtocore.ae.appeng.me2in1.search_in")));
            this.searchOutField.setTooltipMessage(Collections.singletonList(Component.translatable("gtocore.ae.appeng.me2in1.search_out")));
        }
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lcom/glodblock/github/extendedae/client/gui/GuiExPatternTerminal;setInitialFocus(Lnet/minecraft/client/gui/components/events/GuiEventListener;)V"))
    private void onSetFocus(GuiExPatternTerminal<?> instance, GuiEventListener guiEventListener) {
        if (!(this.getMenu() instanceof Me2in1Menu)) {
            instance.setInitialFocus(guiEventListener);
        }
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lappeng/client/gui/AEBaseScreen;init()V"))
    private void onSuperInit(CallbackInfo ci) {
        if (this.getMenu() instanceof Me2in1Menu) {
            var r = (this.height - GUI_HEADER_HEIGHT - GUI_FOOTER_HEIGHT - GUI_TOP_AND_BOTTOM_PADDING + MAGIC_NUMBER) / ROW_HEIGHT;
            this.imageHeight = GUI_HEADER_HEIGHT + GUI_FOOTER_HEIGHT + r * ROW_HEIGHT;
        }
    }

    @Inject(method = "updateBeforeRender", at = @At("TAIL"), remap = false)
    private void updateBeforeRender(CallbackInfo ci) {
        this.gtolib$showMolecularAssembler.set(getMenu().gtolib$showMolecularAssembler);
        if (this.getMenu() instanceof Me2in1Menu) {
            scrollbar.setVisible(false);
        }
    }

    @Redirect(
              method = "drawFG",
              at = @At(
                       value = "INVOKE",
                       target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)I",
                       remap = true),
              remap = false)
    private int gto$drawScrollingGroupName(GuiGraphics guiGraphics, Font font, FormattedCharSequence originalText,
                                           int x, int y, int color, boolean shadow) {
        int visibleRow = (y - 57) / ROW_HEIGHT;
        int rowIndex = scrollbar.getCurrentScroll() + visibleRow;
        if (visibleRow < 0 || rowIndex < 0 || rowIndex >= rows.size() ||
                !(rows.get(rowIndex) instanceof GuiExPatternTerminalGroupHeaderAccessor header)) {
            return guiGraphics.drawString(font, originalText, x, y, color, shadow);
        }

        PatternContainerGroup group = header.gto$getGroup();
        int groupCount = byGroup.get(group).size();
        FormattedText displayName = groupCount > 1 ?
                Component.empty().append(group.name()).append(" (" + groupCount + ')') : group.name();
        int fullTextWidth = font.width(displayName);
        if (fullTextWidth <= gto$TEXT_WIDTH || !gto$isGroupRowHovered(visibleRow)) {
            if (group.equals(gto$scrollingGroup)) {
                gto$scrollingGroup = null;
            }
            return guiGraphics.drawString(font,
                    Language.getInstance().getVisualOrder(font.substrByWidth(displayName, gto$TEXT_WIDTH)),
                    x, y, color, shadow);
        }

        if (!group.equals(gto$scrollingGroup)) {
            gto$scrollingGroup = group;
            gto$scrollingGroupHoverStart = Util.getMillis();
        }
        int scrollOffset = gto$getTextScrollOffset(fullTextWidth - gto$TEXT_WIDTH);
        guiGraphics.enableScissor(leftPos + x, topPos + y,
                leftPos + x + gto$TEXT_WIDTH, topPos + y + font.lineHeight);
        int result = guiGraphics.drawString(font, Language.getInstance().getVisualOrder(displayName),
                x - scrollOffset, y, color, shadow);
        guiGraphics.disableScissor();
        return result;
    }

    @Unique
    private boolean gto$isGroupRowHovered(int visibleRow) {
        Minecraft minecraft = Minecraft.getInstance();
        double mouseX = minecraft.mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() /
                minecraft.getWindow().getScreenWidth();
        double mouseY = minecraft.mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() /
                minecraft.getWindow().getScreenHeight();
        return mouseX >= leftPos + 22 && mouseX < leftPos + 22 + gto$COLUMNS * ROW_HEIGHT &&
                mouseY >= topPos + 51 + visibleRow * ROW_HEIGHT &&
                mouseY < topPos + 51 + (visibleRow + 1) * ROW_HEIGHT;
    }

    @Unique
    private int gto$getTextScrollOffset(int maxOffset) {
        long travelTime = Math.max(1, maxOffset * 1000L / gto$TEXT_SCROLL_PIXELS_PER_SECOND);
        long cycleTime = gto$TEXT_SCROLL_PAUSE_MS * 2L + travelTime * 2L;
        long elapsed = (Util.getMillis() - gto$scrollingGroupHoverStart) % cycleTime;
        if (elapsed < gto$TEXT_SCROLL_PAUSE_MS) {
            return 0;
        }
        elapsed -= gto$TEXT_SCROLL_PAUSE_MS;
        if (elapsed < travelTime) {
            return (int) (maxOffset * elapsed / travelTime);
        }
        elapsed -= travelTime;
        if (elapsed < gto$TEXT_SCROLL_PAUSE_MS) {
            return maxOffset;
        }
        elapsed -= gto$TEXT_SCROLL_PAUSE_MS;
        return maxOffset - (int) (maxOffset * elapsed / travelTime);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private Set<Object> getCacheForSearchTerm(String searchTerm) {
        Set<Object> cache = this.cachedSearches.computeIfAbsent(searchTerm, k -> new OpenCacheHashSet<>());
        if (cache.isEmpty() && searchTerm.length() > 1) {
            cache.addAll(this.getCacheForSearchTerm(searchTerm.substring(0, searchTerm.length() - 1)));
        }
        return cache;
    }

    @Redirect(method = "refreshList", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;sort(Ljava/util/Comparator;)V"), remap = false)
    private void sort(ArrayList<PatternContainerRecord> list, Comparator<PatternContainerRecord> c) {}

    @Inject(remap = false, method = "refreshList", at = @At("HEAD"), cancellable = true)
    private void refreshList0(CallbackInfo ci) {
        if (this.gto$getSearchProviderField() == null) {
            return;
        }
        ci.cancel();
        gto$refreshSearch();
    }

    @Override
    public void gto$refreshSearch() {
        gto$eae$refreshList();
    }

    @Shadow(remap = false, prefix = "gto$eae$")
    private void gto$eae$refreshList() {}

    @Shadow(remap = false)
    @Final
    private Scrollbar scrollbar;

    @Shadow(remap = false)
    @Final
    private static int GUI_HEADER_HEIGHT;

    @Shadow(remap = false)
    @Final
    private static int GUI_FOOTER_HEIGHT;

    @Shadow(remap = false)
    @Final
    private static int GUI_TOP_AND_BOTTOM_PADDING;

    @Shadow(remap = false)
    @Final
    private static int ROW_HEIGHT;

    @Shadow(remap = false)
    @Final
    private static int MAGIC_NUMBER;

    public void gto$resetExPatternTerminalScrollbar() {
        resetScrollbar();
    }

    public HashMultimap<PatternContainerGroup, PatternContainerRecord> gto$getByGroup() {
        return byGroup;
    }

    public HashMap<Long, PatternContainerRecord> gto$getById() {
        return byId;
    }

    public HashMap<Integer, HighlightButton> gto$getHighlisghtsButtons() {
        return highlightBtns;
    }

    public AETextField gto$searchOutField() {
        return searchOutField;
    }

    public AETextField gto$searchInField() {
        return searchInField;
    }

    public Set<ItemStack> gto$matchedStack() {
        return matchedStack;
    }

    public Set<PatternContainerRecord> gto$matchedProvider() {
        return matchedProvider;
    }

    public Map<String, Set<Object>> gto$cachedSearches() {
        return cachedSearches;
    }

    public HashMap<Long, GuiExPatternTerminal.PatternProviderInfo> gto$infoMap() {
        return infoMap;
    }

    public boolean gto$itemStackMatchesSearchTerm(ItemStack itemStack, List<String> searchTerm, boolean checkOut) {
        return itemStackMatchesSearchTerm(itemStack, searchTerm, checkOut);
    }
}
