package com.gtocore.mixin.ae2.menu;

import com.gtolib.api.ae2.me2in1.ExtendedEncodingMenu;

import net.minecraft.network.FriendlyByteBuf;

import appeng.menu.AEBaseMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;

@Mixin(AEBaseMenu.class)
public class AEBaseMenuMixin {

    // @Inject(method = "sendPacketToClient", at = @At("HEAD"), remap = false)
    // private void gtolib$sendPacketToClient(BasePacket packet, CallbackInfo ci) {
    // if ((AEBaseMenu) (Object) this instanceof ExtendedEncodingMenu &&
    // packet instanceof IGuiDataSyncPacketExt sync) {
    // sync.gtolib$setInnerMenuPacket(true);
    // }
    // }

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
