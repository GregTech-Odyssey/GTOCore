package com.gtocore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import appeng.api.stacks.AEKey;
import appeng.client.gui.me.common.ContentToast;

import java.util.List;

public final class Message {

    public static void pickCraftToast(FriendlyByteBuf b) {
        AEKey aeKey = AEKey.readKey(b);
        int stateCode = b.readInt();
        Minecraft.getInstance().getToasts().addToast(new ContentToast(aeKey) {

            @Override
            protected Component getTitle() {
                if (stateCode == 0) {
                    return Component.translatable("gtocore.ae.appeng.pick_craft.all_right.title");
                }
                return Component.translatable("gtocore.ae.appeng.pick_craft.error.title");
            }

            @Override
            protected void addInfoLines(List<FormattedCharSequence> lines) {
                var text = stateCode == 0 ?
                        Component.translatable("gtocore.ae.appeng.pick_craft.all_right") :
                        Component.translatable("gtocore.ae.appeng.pick_craft.error." + stateCode);
                lines.addAll(Minecraft.getInstance().font.split(text, width() - 30 - 5));
            }
        });
    }
}
