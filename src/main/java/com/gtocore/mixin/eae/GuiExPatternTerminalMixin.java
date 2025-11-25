package com.gtocore.mixin.eae;

import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.IPatternAccessTermMenu;
import com.gtolib.api.ae2.ShowMolecularAssembler;
import com.gtolib.api.ae2.gui.hooks.IExtendedGuiEx;
import com.gtolib.api.ae2.me2in1.Me2in1Menu;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.patternaccess.PatternContainerRecord;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AETextField;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import com.fast.fastcollection.OpenCacheHashSet;
import com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal;
import com.glodblock.github.extendedae.container.ContainerExPatternTerminal;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

@Mixin(GuiExPatternTerminal.class)
public abstract class GuiExPatternTerminalMixin<T extends ContainerExPatternTerminal> extends AEBaseScreen<T> implements IExtendedGuiEx {

    @Shadow(remap = false)
    @Final
    private Map<String, Set<Object>> cachedSearches;
    @Shadow(remap = false)
    @Final
    private AETextField searchOutField;
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

    @Override
    public AETextField gtolib$getSearchOutField() {
        return searchOutField;
    }
}
