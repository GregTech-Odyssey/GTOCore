package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.ae2.gui.hooks.IStylelessCompositeWidget;
import com.gtolib.api.ae2.gui.hooks.IWidgetsGetter;

import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.Rect2i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.Point;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.ICompositeWidget;
import appeng.client.gui.WidgetContainer;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
@Mixin(WidgetContainer.class)
public class WidgetContainerMixin implements IWidgetsGetter {

    @Final
    @Shadow(remap = false)
    private Map<String, AbstractWidget> widgets;

    @Override
    public Map<String, IStylelessCompositeWidget> gtolib$getCompositeStylelessWidgets() {
        return gtolib$compositeStylelessWidgets;
    }

    @Override
    public Map<String, ICompositeWidget> gtolib$getCompositeWidgets() {
        return compositeWidgets;
    }

    @Override
    public Map<String, AbstractWidget> gtolib$getWidgets() {
        return widgets;
    }

    @Final
    @Shadow(remap = false)
    private Map<String, ICompositeWidget> compositeWidgets;
    @Unique
    private final Map<String, IStylelessCompositeWidget> gtolib$compositeStylelessWidgets = new O2OOpenCacheHashMap<>();

    @Inject(method = "add(Ljava/lang/String;Lappeng/client/gui/ICompositeWidget;)V",
            at = @At(value = "HEAD"),
            remap = false,
            cancellable = true)
    private void gtolib$addCompositeWidget(String id, ICompositeWidget widget, CallbackInfo ci) {
        if (widget instanceof IStylelessCompositeWidget styleless) {
            Preconditions.checkState(!widgets.containsKey(id), "%s already used for widget", id);
            Preconditions.checkState(!compositeWidgets.containsKey(id), "%s already used for widget", id);
            styleless.initialize(id, (WidgetContainer) (Object) this);

            if (gtolib$compositeStylelessWidgets.put(id, styleless) == null) {
                ci.cancel();
            } else throw new IllegalStateException("Duplicate id: " + id);
        }
    }

    @Inject(method = "populateScreen",
            at = @At(value = "HEAD"),
            remap = false)
    private void gtolib$populateScreen(Consumer<AbstractWidget> addWidget, Rect2i bounds, AEBaseScreen<?> screen, CallbackInfo ci) {
        // For composite widgets, just position them. Positions for these widgets are generally relative to the dialog
        Rect2i relativeBounds = new Rect2i(0, 0, bounds.getWidth(), bounds.getHeight());
        for (var entry : gtolib$compositeStylelessWidgets.entrySet()) {
            var widget = entry.getValue();
            widget.adjustPosition(relativeBounds);

            widget.populateScreen(addWidget, bounds, screen);
        }
    }

    /**
     * Tick {@link IStylelessCompositeWidget} instances that are not automatically ticked as part of being a normal
     * widget.
     * 
     * @see IStylelessCompositeWidget#tick()
     */
    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    public void tick(CallbackInfo ci) {
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible()) {
                widget.tick();
            }
        }
    }

    /**
     * @see IStylelessCompositeWidget#updateBeforeRender()
     * @see WidgetContainer#updateBeforeRender()
     */
    @Inject(method = "updateBeforeRender", at = @At("HEAD"), remap = false)
    public void updateBeforeRender(CallbackInfo ci) {
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible()) {
                widget.updateBeforeRender();
            }
        }
    }

    /**
     * @see IStylelessCompositeWidget#drawBackgroundLayer(GuiGraphics, Rect2i, Point)
     * @see WidgetContainer#drawBackgroundLayer(GuiGraphics, Rect2i, Point)
     */
    @Inject(method = "drawBackgroundLayer", at = @At("HEAD"), remap = false)
    public void drawBackgroundLayer(GuiGraphics guiGraphics, Rect2i bounds, Point mouse, CallbackInfo ci) {
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible()) {
                widget.drawBackgroundLayer(guiGraphics, bounds, mouse);
            }
        }
    }

    /**
     * @see IStylelessCompositeWidget#drawForegroundLayer(GuiGraphics, Rect2i, Point)
     * @see WidgetContainer#drawForegroundLayer(GuiGraphics, Rect2i, Point)
     */
    @Inject(method = "drawForegroundLayer", at = @At("HEAD"), remap = false)
    public void drawForegroundLayer(GuiGraphics poseStack, Rect2i bounds, Point mouse, CallbackInfo ci) {
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible()) {
                widget.drawForegroundLayer(poseStack, bounds, mouse);
            }
        }
    }

    /**
     * @see IStylelessCompositeWidget#onMouseDown(Point, int)
     * @see WidgetContainer#onMouseDown(Point, int)
     */
    @Inject(method = "onMouseDown", at = @At("HEAD"), remap = false, cancellable = true)
    public void onMouseDown(Point mousePos, int btn, CallbackInfoReturnable<Boolean> cir) {
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible() && (widget.wantsAllMouseDownEvents() || mousePos.isIn(widget.getBounds())) && widget.onMouseDown(mousePos, btn)) {
                cir.setReturnValue(true);
            }
        }
    }

    /**
     * @see IStylelessCompositeWidget#onMouseUp(Point, int)
     * @see WidgetContainer#onMouseUp(Point, int)
     */
    @Inject(method = "onMouseUp", at = @At("HEAD"), remap = false, cancellable = true)
    public void onMouseUp(Point mousePos, int btn, CallbackInfoReturnable<Boolean> cir) {
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible() && (widget.wantsAllMouseUpEvents() || mousePos.isIn(widget.getBounds())) && widget.onMouseUp(mousePos, btn)) {
                cir.setReturnValue(true);
            }
        }
    }

    /**
     * @see IStylelessCompositeWidget#onMouseDrag(Point, int)
     * @see WidgetContainer#onMouseDrag(Point, int)
     */
    @Inject(method = "onMouseDrag", at = @At("HEAD"), remap = false, cancellable = true)
    public void onMouseDrag(Point mousePos, int btn, CallbackInfoReturnable<Boolean> cir) {
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible() && widget.onMouseDrag(mousePos, btn)) {
                cir.setReturnValue(true);
            }
        }
    }

    /**
     * @see IStylelessCompositeWidget#onMouseWheel(Point, double)
     * @see WidgetContainer#onMouseWheel(Point, double)
     */
    @Inject(method = "onMouseWheel", at = @At("HEAD"), remap = false, cancellable = true)
    void onMouseWheel(Point mousePos, double wheelDelta, CallbackInfoReturnable<Boolean> cir) {
        // First pass: dispatch wheel event to widgets the mouse is over
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible() && mousePos.isIn(widget.getBounds()) && widget.onMouseWheel(mousePos, wheelDelta)) {
                cir.setReturnValue(true);
            }
        }

        // Second pass: send the event to capturing widgets
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible() && widget.wantsAllMouseWheelEvents() && widget.onMouseWheel(mousePos, wheelDelta)) {
                cir.setReturnValue(true);
            }
        }
    }

    /**
     * @see IStylelessCompositeWidget#addExclusionZones(List, Rect2i)
     * @see WidgetContainer#addExclusionZones(List, Rect2i)
     */
    @Inject(method = "addExclusionZones", at = @At("HEAD"), remap = false)
    public void addExclusionZones(List<Rect2i> exclusionZones, Rect2i bounds, CallbackInfo ci) {
        for (var widget : gtolib$compositeStylelessWidgets.values()) {
            if (widget.isVisible()) {
                widget.addExclusionZones(exclusionZones, bounds);
            }
        }
    }

    @Redirect(method = "getTooltip",
              at = @At(value = "INVOKE",
                       target = "Ljava/util/Map;values()Ljava/util/Collection;"),
              remap = false)
    private <V> Collection<V> gtolib$getTooltip(Map<?, V> instance) {
        if (instance == this.compositeWidgets) {
            return (Collection<V>) gtolib$getMixedCompositeWidgets()
                    .map(Map::values)
                    .orElseGet(() -> this.compositeWidgets.values());
        }
        return instance.values();
    }

    /**
     * Check if there's any content or compound widget at the given screen-relative mouse position.
     */
    @Redirect(method = "hitTest",
              at = @At(value = "INVOKE",
                       target = "Ljava/util/Map;values()Ljava/util/Collection;"),
              remap = false)
    private <V> Collection<V> gtolib$hitTest(Map<?, V> instance, Point mousePos) {
        if (instance == this.compositeWidgets) {
            return (Collection<V>) gtolib$getMixedCompositeWidgets()
                    .map(Map::values)
                    .orElseGet(() -> this.compositeWidgets.values());
        }
        return instance.values();
    }

    @Unique
    private @NotNull Optional<Map<String, ICompositeWidget>> gtolib$getMixedCompositeWidgets() {
        if (this.compositeWidgets.isEmpty() && gtolib$compositeStylelessWidgets.isEmpty()) {
            return Optional.empty();
        }
        Map<String, ICompositeWidget> mixed = new O2OOpenCacheHashMap<>(this.compositeWidgets);
        mixed.putAll(gtolib$compositeStylelessWidgets);
        return Optional.of(mixed);
    }
}
