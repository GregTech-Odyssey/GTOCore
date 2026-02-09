package com.gtocore.mixin.eae;

import com.gtocore.integration.jech.PinYinUtils;

import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.IPatternAccessTermMenu;
import com.gtolib.api.ae2.ShowMolecularAssembler;
import com.gtolib.api.ae2.gui.hooks.IExtendedGuiEx;
import com.gtolib.api.ae2.me2in1.Me2in1Menu;
import com.gtolib.api.ae2.me2in1.Me2in1Screen;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.patternaccess.PatternContainerRecord;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AETextField;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.util.inv.AppEngInternalInventory;
import com.fast.fastcollection.OpenCacheHashSet;
import com.glodblock.github.extendedae.client.button.HighlightButton;
import com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal;
import com.glodblock.github.extendedae.container.ContainerExPatternTerminal;
import com.glodblock.github.extendedae.util.FCUtil;
import com.glodblock.github.extendedae.util.MessageUtil;
import com.google.common.collect.HashMultimap;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(GuiExPatternTerminal.class)
public abstract class GuiExPatternTerminalMixin<T extends ContainerExPatternTerminal> extends AEBaseScreen<T> implements IExtendedGuiEx {

    @Unique
    private static final AppEngInternalInventory gto$emptyInv = new AppEngInternalInventory(0);
    @Unique
    private static final int gto$COLUMNS = 9;

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
    protected abstract boolean itemStackMatchesSearchTerm(ItemStack itemStack, List<String> searchTerm, boolean checkOut);

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
        gtolib$showMolecularAssembler = new ServerSettingToggleButton<>(GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS,
                ShowMolecularAssembler.ALL);
        this.addToLeftToolbar(gtolib$showMolecularAssembler);

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

    @Inject(method = "updateBeforeRender", at = @At("TAIL"), remap = false)
    private void updateBeforeRender(CallbackInfo ci) {
        this.gtolib$showMolecularAssembler.set(((IPatternAccessTermMenu) this.getMenu()).gtolib$getShownMolecularAssemblers());
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
    private void sort(ArrayList<PatternContainerRecord> list, Comparator<PatternContainerRecord> comparator) {}

    @Inject(remap = false, method = "refreshList", at = @At("HEAD"), cancellable = true)
    private void refreshList0(CallbackInfo ci) {
        if (this.gto$getSearchProviderField() == null) {
            return;
        }
        ci.cancel();
        this.byGroup.clear();
        this.highlightBtns.forEach((k, v) -> this.removeWidget(v));
        this.highlightBtns.clear();
        this.matchedStack.clear();
        this.matchedProvider.clear();

        final String outputFilter = this.searchOutField.getValue().toLowerCase().trim();
        final String inputFilter = this.searchInField.getValue().toLowerCase().trim();
        final List<String> outputFilters = FCUtil.tokenize(outputFilter);
        final List<String> inputFilters = FCUtil.tokenize(inputFilter);
        final String patternFilter = this.gto$getSearchProviderField().getValue().toLowerCase();

        final Set<Object> cachedSearch = this.getCacheForSearchTerm("out:" + outputFilter + "in:" + inputFilter + "pat:" + patternFilter);
        final boolean rebuild = cachedSearch.isEmpty();

        for (PatternContainerRecord entry : this.byId.values()) {
            // ignore inventory if not doing a full rebuild or cache already marks it as miss.
            if (!rebuild && !cachedSearch.contains(entry)) {
                continue;
            }

            // Shortcut to skip any filter if search term is ""/empty
            boolean skipSearch = outputFilter.isEmpty() && inputFilter.isEmpty();
            boolean found = skipSearch && patternFilter.isEmpty();

            boolean match = PinYinUtils.match(entry.getSearchName(), patternFilter);

            // Search if the current inventory holds a pattern containing the search term.
            if (!skipSearch && match) {
                boolean midRes;
                for (ItemStack itemStack : entry.getInventory()) {
                    if (!outputFilter.isEmpty()) {
                        midRes = this.itemStackMatchesSearchTerm(itemStack, outputFilters, true);
                    } else {
                        midRes = true;
                    }
                    if (!inputFilter.isEmpty() && midRes) {
                        midRes = this.itemStackMatchesSearchTerm(itemStack, inputFilters, false);
                    }
                    if (midRes) {
                        found = true;
                    }
                }
            }
            // if found, filter skipped or machine name matching the search term, add it
            if (found || (match && skipSearch)) {
                this.byGroup.put(entry.getGroup(), entry);
                cachedSearch.add(entry);
                if (match) {
                    this.matchedProvider.add(entry);
                }
            } else {
                cachedSearch.remove(entry);
            }
        }

        this.groups.clear();
        this.groups.addAll(this.byGroup.keySet());

        this.rows.clear();
        this.rows.ensureCapacity(this.getMaxRows());

        var row = this.rows;
        for (var group : this.groups) {
            row.add(gto$constructGroupHeaderRow(group));

            var containers = new ArrayList<>(this.byGroup.get(group));
            Collections.sort(containers);
            for (var container : containers) {
                var inventory = container.getInventory();
                // noinspection SizeReplaceableByIsEmpty
                if (inventory.size() > 0) {
                    var info = this.infoMap.get(container.getServerId());
                    if (info == null) {
                        continue;
                    }
                    var btn = new HighlightButton();
                    btn.setMultiplier(this.playerToBlockDis(info.pos()));
                    btn.setTarget(info.pos(), info.face(), info.world());
                    btn.setSuccessJob(() -> {
                        if (this.getPlayer() != null && info.pos() != null && info.world() != null) {
                            Component message = MessageUtil.createEnhancedHighlightMessage(this.getPlayer(), info.pos(), info.world(), "chat.ex_pattern_access_terminal.pos");
                            this.getPlayer().displayClientMessage(message, false);
                        }
                    });
                    btn.setTooltip(Tooltip.create(Component.translatable("gui.expatternprovider.ex_pattern_access_terminal.tooltip.03")));
                    btn.setVisibility(false);
                    this.highlightBtns.put(this.rows.size(), this.addRenderableWidget(btn));
                }
                for (var offset = 0; offset < inventory.size(); offset += gto$COLUMNS) {
                    var slots = Math.min(inventory.size() - offset, gto$COLUMNS);
                    var containerRow = gto$constructSlotRow(container, offset, slots);
                    row.add(containerRow);
                }
            }
        }

        // lines may have changed - recalculate scroll bar.
        this.resetScrollbar();
    }

    @Unique
    private static Object gto$constructSlotRow(PatternContainerRecord container, int offset, int slots) {
        try {
            var slotRowClass = Class.forName("com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal$SlotsRow");
            var constructor = slotRowClass.getDeclaredConstructor(PatternContainerRecord.class, int.class, int.class);
            constructor.setAccessible(true);
            return constructor.newInstance(container, offset, slots);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Unique
    private static Object gto$constructGroupHeaderRow(PatternContainerGroup group) {
        try {
            var groupHeaderRowClass = Class.forName("com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal$GroupHeaderRow");
            var constructor = groupHeaderRowClass.getDeclaredConstructor(PatternContainerGroup.class);
            constructor.setAccessible(true);
            return constructor.newInstance(group);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void gto$refreshSearch() {
        gto$eae$refreshList();
    }

    @Shadow(remap = false, prefix = "gto$eae$")
    private void gto$eae$refreshList() {}
}
