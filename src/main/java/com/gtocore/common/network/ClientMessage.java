package com.gtocore.common.network;

import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import com.gtocore.integration.ae.hooks.IConfirmLongMenu;

import com.gtolib.api.misc.PlanetManagement;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.utils.SortUtils;

import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class ClientMessage {

    public static void sendData(String channel, @Nullable CompoundTag data) {
        send(channel, buf -> buf.writeNbt(data));
    }

    public static void send(String channel, @NotNull Consumer<FriendlyByteBuf> data) {
        new FromClientMessage(channel, data).sendToServer();
    }

    public static void checkPlanetIsUnlocked(ResourceLocation planet) {
        if (PlanetManagement.isClientUnlocked(planet)) return;
        send("planetIsUnlocked", buf -> buf.writeResourceLocation(planet));
    }

    public static void organCapAsync(CompoundTag tag) { // ServerToClient 全量同步
        sendData("organCapAsync", tag);
    }

    static void handle(String channel, @NotNull ServerPlayer serverPlayer, FriendlyByteBuf data) {
        switch (channel) {
            case "key" -> KeyMessage.pressAction(serverPlayer, data.readVarInt());
            case "planetIsUnlocked" -> PlanetManagement.checkIsUnlocked(serverPlayer, data.readResourceLocation());
            case "shiftKeypress" -> IEnhancedPlayer.of(serverPlayer).getPlayerData().shiftState = data.readBoolean();
            case "sortInventory" -> {
                if (serverPlayer.containerMenu instanceof ModularUIContainer container) {
                    SortUtils.sort(container);
                }
            }
            case "pattern_buffer_index" -> {
                if (serverPlayer.containerMenu instanceof ModularUIContainer container) {
                    for (var widget : container.getModularUI().mainGroup.widgets) {
                        if (widget instanceof FancyMachineUIWidget uiWidget && uiWidget.getMainPage() instanceof MEPatternBufferPartMachine machine) {
                            machine.onMouseClicked(data.readVarInt());
                        }
                    }
                }
            }
            case "craftLongTask" -> {
                if (serverPlayer.containerMenu instanceof IConfirmLongMenu menu) {
                    menu.gtocore$confirmLong(data.readVarLong(), data.readBoolean(), data.readBoolean());
                }
            }
        }
    }
}
