package com.gtocore.common.item;

import com.gtocore.api.gui.ktflexible.misc.InitFancyMachineUIWidget;
import com.gtocore.common.data.translation.GTOItemTooltips;
import com.gtocore.common.saved.WirelessNetworkSavedData;
import com.gtocore.integration.ae.wireless.WirelessMachine;
import com.gtocore.integration.ae.wireless.WirelessNodeUIKt;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.IAddInformation;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum MEWirelessMachineConfigurator implements IItemUIFactory, IAddInformation {

    INSTANCE;

    public static @NotNull String getConfiguringNetworkId(Player player) {
        var item = player.getMainHandItem();
        if (item.getItem() instanceof ComponentItem cItem && cItem.getComponents().contains(INSTANCE)) {
            var nbt = item.getOrCreateTag();
            if (nbt.contains("configuringNetworkId")) {
                var nid = nbt.getString("configuringNetworkId");
                if (WirelessNetworkSavedData.Companion.findNetworkById(nid) != null) {
                    return nid;
                }
            }
        }
        return "";
    }

    public static void setConfiguringNetworkId(Player player, String networkId) {
        var item = player.getMainHandItem();
        if (item.getItem() instanceof ComponentItem cItem && cItem.getComponents().contains(INSTANCE)) {
            var nbt = item.getOrCreateTag();
            nbt.putString("configuringNetworkId", networkId);
        }
    }

    @Override
    public ModularUI createUI(HeldItemUIFactory.HeldItemHolder holder, Player entityPlayer) {
        var tab = WirelessNodeUIKt.createWirelessUIProvider(entityPlayer);
        var fancyUI = new InitFancyMachineUIWidget(tab, 176, 166,
                () -> WirelessNetworkSavedData.write(entityPlayer));
        fancyUI.getSideTabsWidget().setMainTab(tab);
        return new ModularUI(176, 166, holder, entityPlayer).widget(fancyUI);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        var player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        var target = context.getClickedPos();
        var world = context.getLevel();
        var tile = world.getBlockEntity(target);
        if (tile instanceof MetaMachineBlockEntity mbe && mbe.getMetaMachine() instanceof WirelessMachine machine) {
            if (world instanceof ServerLevel) {
                var nid = getConfiguringNetworkId(player);
                var machineNid = machine.getConnectedNetworkId();
                if (player.isShiftKeyDown()) {
                    if (!machineNid.isEmpty()) {
                        setConfiguringNetworkId(player, machineNid);
                    }
                } else if (!machineNid.equals(nid) &&
                        WirelessNetworkSavedData.Companion.findNetworkById(nid) != null) {
                            machine.leaveNetwork();
                            machine.joinNetwork(nid);
                        }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendTooltips(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                               TooltipFlag isAdvanced) {
        tooltipComponents.addAll(GTOItemTooltips.INSTANCE.getMEWirelessMachineConfigurator().get());
    }
}
