package com.gtocore.mixin.eae;

import com.gtocore.integration.jech.PinYinUtils;

import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.IPatternAccessTermMenu;
import com.gtolib.api.ae2.ShowMolecularAssembler;
import com.gtolib.api.ae2.gui.hooks.IExtendedGuiEx;
import com.gtolib.api.ae2.me2in1.Me2in1Menu;
import com.gtolib.api.ae2.me2in1.Me2in1Screen;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.patternaccess.PatternContainerRecord;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AETextField;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.util.inv.AppEngInternalInventory;
import com.fast.fastcollection.OpenCacheHashSet;
import com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal;
import com.glodblock.github.extendedae.container.ContainerExPatternTerminal;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(GuiExPatternTerminal.class)
public abstract class GuiExPatternTerminalMixin<T extends ContainerExPatternTerminal> extends AEBaseScreen<T> implements IExtendedGuiEx {

    @Unique
    private static final AppEngInternalInventory gto$emptyInv = new AppEngInternalInventory(0);

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
    protected abstract void refreshList();

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

    @ModifyArg(method = "refreshList", at = @At(value = "INVOKE", target = "Lcom/glodblock/github/extendedae/client/gui/GuiExPatternTerminal;getCacheForSearchTerm(Ljava/lang/String;)Ljava/util/Set;"), remap = false)
    private String modifySearchTerm(String original) {
        if (this.gto$getSearchProviderField() != null) {
            return original + "pat:" + this.gto$getSearchProviderField().getValue().toLowerCase();
        }
        return original;
    }

    @Redirect(method = "refreshList", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;sort(Ljava/util/Comparator;)V"), remap = false)
    private void sort(ArrayList<PatternContainerRecord> list, Comparator<PatternContainerRecord> comparator) {}

    @ModifyExpressionValue(method = "refreshList", at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/lang/String;isEmpty()Z"), remap = false)
    private boolean isEmpty(boolean original) {
        if (this.gto$getSearchProviderField() == null) {
            return original;
        }
        return original && this.gto$getSearchProviderField().getValue().isEmpty();
    }

    @ModifyExpressionValue(method = "refreshList", at = @At(value = "INVOKE", ordinal = 0, target = "Lappeng/client/gui/me/patternaccess/PatternContainerRecord;getInventory()Lappeng/util/inv/AppEngInternalInventory;"), remap = false)
    private AppEngInternalInventory getInventory(AppEngInternalInventory original, @Local(name = "inputFilter") String inputFilter, @Local(name = "outputFilter") String outputFilter, @Local(name = "entry") PatternContainerRecord entry) {
        if (this.gto$getSearchProviderField() == null) {
            return original;
        }
        var flag = inputFilter.isEmpty() && outputFilter.isEmpty();
        if (flag) {
            return this.gto$getSearchProviderField().getValue().isEmpty() ? original : gto$emptyInv;
        } else {
            return PinYinUtils.match(entry.getSearchName(), this.gto$getSearchProviderField().getValue().toLowerCase()) ? original : gto$emptyInv;
        }
    }

    @Override
    public void gto$refreshSearch() {
        refreshList();
    }
}
