package com.gtocore.mixin.ae2.screen;

import com.gtocore.integration.ae.hooks.IPushResultsHandler;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.crafting.CraftingCPUScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.me.crafting.CraftingCPUMenu;
import appeng.menu.me.crafting.CraftingStatusEntry;
import com.glodblock.github.extendedae.client.render.EAEHighlightHandler;
import com.glodblock.github.extendedae.util.MessageUtil;
import com.glodblock.github.glodium.network.packet.sync.IActionHolder;
import com.glodblock.github.glodium.network.packet.sync.Paras;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
@Mixin(CraftingCPUScreen.class)
public abstract class CraftingCPUScreenMixin<T extends CraftingCPUMenu> extends AEBaseScreen<T> implements IActionHolder {

    @Shadow(remap = false)
    protected abstract List<CraftingStatusEntry> getVisualEntries();

    @Unique
    private Button gto$pauseButton;

    public CraftingCPUScreenMixin(T menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gto$onInit(CraftingCPUMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {
        gto$pauseButton = Button.builder(Component.translatable("gtocore.ae.appeng.craft.pause_job"), b -> {
            if (menu instanceof IPushResultsHandler handler) {
                handler.gto$setPaused(!handler.gto$isPaused());
            }
        })
                .build();
    }

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/Button;active:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void gto$onUpdateBefore(GuiGraphics guiGraphics, int mouseX, int mouseY, float btn, CallbackInfo ci) {
        gto$pauseButton.active = !getVisualEntries().isEmpty();
        if (menu instanceof IPushResultsHandler handler) {
            gto$pauseButton.setMessage(Component.translatable(handler.gto$isPaused() ? "gtocore.ae.appeng.craft.resume_job" : "gtocore.ae.appeng.craft.pause_job"));
            gto$pauseButton.setTooltip(Tooltip.create(Component.translatable(handler.gto$isPaused() ? "gtocore.ae.appeng.craft.resume_job.desc" : "gtocore.ae.appeng.craft.pause_job.desc")));
        }
    }

    @Override
    protected void init() {
        super.init();
        gto$pauseButton.setPosition(getGuiLeft() + 103, getGuiTop() - 25 + imageHeight);
        gto$pauseButton.setWidth(50);
        gto$pauseButton.setHeight(20);
        addRenderableWidget(gto$pauseButton);
    }

    @Override
    public @NotNull Map<String, Consumer<Paras>> getActionMap() {
        return Map.of("hightlightBlock", this::gto$hightlightBlock);
    }

    @Unique
    private void gto$hightlightBlock(Paras params) {
        var size = params.getParaAmount();
        Preconditions.checkArgument(size % 2 == 0);
        for (int i = 0; i < size / 2; i++) {
            BlockPos pos = BlockPos.of(params.<Long>get(2 * i));
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(params.get(2 * i + 1)));
            Component message = MessageUtil.createEnhancedHighlightMessage(getPlayer(), pos, dim, "chat.ex_pattern_access_terminal.pos");
            getPlayer().displayClientMessage(message, false);
            EAEHighlightHandler.highlight(pos, dim, System.currentTimeMillis() + (long) (20 * 1000));
        }
    }
}
