package com.gtocore.mixin.ae2.menu;

import com.gtolib.api.ae2.me2in1.ExtendedEncodingMenu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import appeng.api.networking.security.IActionHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.common.MEStorageMenu;
import de.mari_023.ae2wtlib.terminal.WTMenuHost;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(AEBaseMenu.class)
public abstract class AEBaseMenuMixin {

    @Shadow(remap = false)
    protected abstract IActionHost getActionHost();

    @Inject(method = "stillValid", at = @At("RETURN"), cancellable = true)
    private void gtolib$stillValid(Player PlayerEntity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        if (getActionHost() instanceof WTMenuHost) {
            if ((Object) this instanceof MEStorageMenu menu) {
                ((MEStorageMenuAccessor) menu).callUpdatePowerStatus();
            }
            cir.setReturnValue(true);
        }
    }

    @ModifyArg(method = "broadcastChanges",
               at = @At(value = "INVOKE", target = "Lappeng/core/sync/packets/GuiDataSyncPacket;<init>(ILjava/util/function/Consumer;)V", remap = false),
               index = 1)
    private Consumer<FriendlyByteBuf> modifyWriteData(Consumer<FriendlyByteBuf> writeData) {
        return buf -> {
            writeData.accept(buf);
            buf.writeBoolean((AEBaseMenu) (Object) this instanceof ExtendedEncodingMenu);
        };
    }

    @ModifyArg(method = "sendAllDataToRemote",
               at = @At(value = "INVOKE", target = "Lappeng/core/sync/packets/GuiDataSyncPacket;<init>(ILjava/util/function/Consumer;)V", remap = false),
               index = 1)
    private Consumer<FriendlyByteBuf> modifyWriteData1(Consumer<FriendlyByteBuf> writeData) {
        return buf -> {
            writeData.accept(buf);
            buf.writeBoolean((AEBaseMenu) (Object) this instanceof ExtendedEncodingMenu);
        };
    }
}
