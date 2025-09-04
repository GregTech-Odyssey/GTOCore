package com.gtocore.mixin.ae2.screen;

import com.gtolib.api.ae2.BlockingType;
import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.IPatternProviderMenu;
import com.gtolib.api.ae2.gui.hooks.IScrollableInvScreen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.Scrollbar;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.menu.SlotSemantic;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.PatternProviderMenu;
import appeng.menu.slot.AppEngSlot;
import com.glodblock.github.extendedae.container.ContainerExPatternProvider;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PatternProviderScreen.class)
public abstract class PatternProviderScreenMixin<C extends PatternProviderMenu> extends AEBaseScreen<C>
                                                implements IScrollableInvScreen<PatternProviderMenu, PatternProviderScreen<PatternProviderMenu>> {

    @Unique
    private SettingToggleButton<BlockingType> gtolib$enhancedblockingmodebutton;

    protected PatternProviderScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Redirect(
              method = "<init>", // 目标：构造函数
              at = @At(
                       value = "INVOKE",
                       target = "Lappeng/client/gui/implementations/PatternProviderScreen;addToLeftToolbar(Lnet/minecraft/client/gui/components/Button;)Lnet/minecraft/client/gui/components/Button;",
                       ordinal = 0),
              remap = false // 如果目标方法非Minecraft原生方法，需禁用重映射
    )
    public Button redirectFirstAddToLeftToolbar(
                                                PatternProviderScreen<?> instance,
                                                Button button// 原始方法参数
    ) {
        this.gtolib$enhancedblockingmodebutton = new ServerSettingToggleButton<>(GTOSettings.BLOCKING_TYPE, BlockingType.NONE);
        this.addToLeftToolbar(this.gtolib$enhancedblockingmodebutton);
        return button;
    }

    @Inject(method = "updateBeforeRender", at = @At("TAIL"), remap = false)
    private void updateBeforeRender(CallbackInfo ci) {
        this.gtolib$enhancedblockingmodebutton.set(((IPatternProviderMenu) menu).gtolib$getBlocking());
    }

    @Unique
    Scrollbar gto$ae$scrollBar;
    @Unique
    private PatternProviderMenu gto$ae$menu;

    @Unique
    @Override
    @SuppressWarnings("unchecked")
    public PatternProviderScreen<PatternProviderMenu> gto$ae$getSelf() {
        return (PatternProviderScreen<PatternProviderMenu>) (Object) this;
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false)
    private void onInit(PatternProviderMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {
        gto$ae$menu = menu;
        // Initialize the scrollbar if needed
        gto$ae$initScrollBar();
    }

    @Unique
    @Override
    public boolean gto$ae$shouldAddScrollBar() {
        return gto$ae$menu.getSlots(SlotSemantics.ENCODED_PATTERN).size() > 36 && (gto$ae$menu instanceof ContainerExPatternProvider);
    }

    @Inject(
            method = "updateBeforeRender",
            at = @At("TAIL"),
            remap = false)
    protected void gto$ae$updateBeforeRender(CallbackInfo ci) {
        // Update the scrollbar position and visibility
        gto$ae$repositionSlots();
    }

    @Unique
    @Override
    public List<AppEngSlot> gto$ae$getScrollerSlots() {
        return gto$ae$menu.getSlots(SlotSemantics.ENCODED_PATTERN).stream()
                .map(AppEngSlot.class::cast)
                .toList();
    }

    @Unique
    @Override
    public SlotSemantic gto$ae$getScrollableSlotSemantic() {
        return SlotSemantics.ENCODED_PATTERN;
    }

    @Unique
    @Override
    public @NotNull PatternProviderMenu gto$ae$getMenu() {
        return gto$ae$menu;
    }

    @Unique
    @Override
    public void gto$ae$setScrollBar(Scrollbar scrollBar) {
        this.gto$ae$scrollBar = scrollBar;
    }

    @Unique
    @Override
    public Scrollbar gto$ae$getScrollBar() {
        return gto$ae$scrollBar;
    }

    @Unique
    @Override
    public int gto$ae$getEffectiveRowCount() {
        return 4;
    }

    @Unique
    @Override
    public int gto$ae$getBGHeight() {
        return 105;
    }

    @Unique
    @Override
    public String gto$ae$getStylesheetName() {
        return "patternProvider";
    }
}
