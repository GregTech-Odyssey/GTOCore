package com.gtocore.mixin.ae2;

import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.gui.hooks.IGuiDataSyncPacketExt;
import com.gtolib.api.ae2.me2in1.Me2in1Menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import appeng.core.sync.packets.GuiDataSyncPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDataSyncPacket.class)
public class GuiDataSyncPacketMixin implements IGuiDataSyncPacketExt {

    @Final
    @Shadow(remap = false)
    private int containerId;
    @Final
    @Shadow(remap = false)
    private FriendlyByteBuf data;

    /// S2C packet handling for inner menus of Me2in1Menu
    @Inject(remap = false, at = @At("HEAD"), method = "clientPacketData", cancellable = true)
    private void clientPacketData(Player player, CallbackInfo ci) {
        AbstractContainerMenu c = player.containerMenu;
        if (c instanceof Me2in1Menu baseMenu &&
                c.containerId == this.containerId &&
                gtolib$isInnerMenuPacket()) {
            baseMenu.getEncoding().receiveServerSyncData(((GuiDataSyncPacket) (Object) this));
            ci.cancel();
        }
    }

    @Inject(remap = false, at = @At("TAIL"), method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V")
    private void hookReceive(FriendlyByteBuf buf, CallbackInfo ci) {
        // This is a hack to ensure that the packet is not handled by inner menus
        // of Me2in1Menu, as they will handle it themselves.
        try {
            int lastBooleanIndex = buf.writerIndex() - 1;
            gtolib$isInnerMenuPacket = buf.getBoolean(lastBooleanIndex);
        } catch (Exception e) {
            // If the packet is not from Me2in1Menu, we don't care about this field
            if (GTOConfig.INSTANCE.aeLog) {
                GTOCore.LOGGER.warn("Failed to read gtolib$isInnerMenuPacket from GuiDataSyncPacket, assuming false", e);
            }
            gtolib$isInnerMenuPacket = false;
        }
    }

    @Unique
    private boolean gtolib$isInnerMenuPacket = false;

    @Override
    public boolean gtolib$isInnerMenuPacket() {
        return gtolib$isInnerMenuPacket;
    }

    @Override
    public void gtolib$setInnerMenuPacket(boolean innerMenuPacket) {
        this.gtolib$isInnerMenuPacket = innerMenuPacket;
    }

    /// C2S packet handled by Me2in1Menu, no need for inner menus to handle it
    // @Inject(remap = false, at = @At("HEAD"), method = "serverPacketData", cancellable = true)
    // public void serverPacketData(ServerPlayer player, CallbackInfo ci) {
    // AbstractContainerMenu c = player.containerMenu;
    // if (c instanceof Me2in1Menu baseMenu && c.containerId == this.containerId) {
    // baseMenu.getEncoding().receiveClientAction((GuiDataSyncPacket) (Object) this);
    // ci.cancel();
    // }
    // }
}
