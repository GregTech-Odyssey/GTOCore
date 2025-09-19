package com.gtocore.mixin.ae2.screen;

import com.gtolib.api.ae2.gui.hooks.IStylelessCompositeWidget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.api.stacks.GenericStack;
import appeng.client.Point;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.WidgetContainer;
import appeng.client.gui.me.crafting.CraftConfirmScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.me.crafting.CraftConfirmMenu;
import com.hepdd.ae2emicraftingforge.client.helper.mapper.EmiStackHelper;
import dev.emi.emi.config.SidebarType;
import dev.emi.emi.runtime.EmiFavorites;
import dev.emi.emi.screen.EmiScreenManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.function.Consumer;

@Mixin(CraftConfirmScreen.class)
public class CraftConfirmScreenMixin extends AEBaseScreen<CraftConfirmMenu> {

    @Unique
    private Button gto$addMissing;

    public CraftConfirmScreenMixin(CraftConfirmMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @org.spongepowered.asm.mixin.injection.At("TAIL"), remap = false)
    private void gto$onInit(CraftConfirmMenu menu, Inventory playerInventory, Component title, ScreenStyle style, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        gto$addMissing = Button.builder(Component.translatable("gtocore.ae.appeng.craft.add_missing_to_emi"), this::gto$addMissing)
                .bounds(82, 181, 50, 20)
                .tooltip(Tooltip.create(Component.translatable("gtocore.ae.appeng.craft.add_missing_to_emi.desc")))
                .build();
        widgets.add(
                "addMissingToEmi", new IStylelessCompositeWidget() {

                    @Override
                    public void initialize(String id, WidgetContainer widgetContainer) {}

                    @Override
                    public void populateScreen(Consumer<AbstractWidget> addWidget, Rect2i bounds, AEBaseScreen<?> screen) {
                        gto$addMissing.setPosition(82 + bounds.getX(), 181 + bounds.getY());
                        addWidget.accept(gto$addMissing);
                    }

                    @Override
                    public AEBaseScreen<?> getScreen() {
                        return (AEBaseScreen<?>) (Object) this;
                    }

                    @Override
                    public void setPosition(Point position) {}

                    @Override
                    public void setSize(int width, int height) {}

                    @Override
                    public Rect2i getBounds() {
                        return new Rect2i(0, 0, 0, 0);
                    }
                });
    }

    @Unique
    private void gto$addMissing(Button button) {
        var plan = menu.getPlan();
        if (plan == null) {
            return;
        }
        plan.getEntries().forEach(entry -> {
            if (entry.getMissingAmount() > 0) {
                EmiFavorites.addFavorite(
                        EmiStackHelper.toEmiStack(
                                new GenericStack(entry.getWhat(), entry.getWhat().getAmountPerUnit())));
                EmiScreenManager.repopulatePanels(SidebarType.FAVORITES);
            }
        });
    }
}
