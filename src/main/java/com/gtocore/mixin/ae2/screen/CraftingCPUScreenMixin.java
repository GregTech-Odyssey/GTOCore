package com.gtocore.mixin.ae2.screen;

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
import com.glodblock.github.extendedae.client.render.EAEHighlightHandler;
import com.glodblock.github.extendedae.util.MessageUtil;
import com.glodblock.github.glodium.network.packet.sync.IActionHolder;
import com.glodblock.github.glodium.network.packet.sync.Paras;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
@Mixin(CraftingCPUScreen.class)
public class CraftingCPUScreenMixin<T extends CraftingCPUMenu> extends AEBaseScreen<T> implements IActionHolder {

    public CraftingCPUScreenMixin(T menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
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
