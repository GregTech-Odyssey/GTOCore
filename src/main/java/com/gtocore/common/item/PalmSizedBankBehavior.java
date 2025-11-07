package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PalmSizedBankBehavior implements IItemUIFactory, IFancyUIProvider {

    public static final PalmSizedBankBehavior INSTANCE = new PalmSizedBankBehavior();

    private static final String TEXT_HEADER = "gtocore.palm_sized_bank.textList.";

    private static @NotNull String text(int id) {
        return TEXT_HEADER + id;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        openUI(itemStack.getItem(), context.getLevel(), player, context.getHand());
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        ItemStack heldItem = player.getItemInHand(usedHand);
        openUI(item, level, player, usedHand);
        return InteractionResultHolder.success(heldItem);
    }

    @Override
    public ModularUI createUI(HeldItemUIFactory.HeldItemHolder holder, Player player) {
        return new ModularUI(176, 166, holder, player)
                .widget(new FancyMachineUIWidget(this, 176, 166));
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget widget) {
        final int width = 192;
        final int height = 144;
        WidgetGroup group = new WidgetGroup(0, 0, width + 8, height + 8);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);

        DraggableScrollableWidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                .setBackground(GuiTextures.DISPLAY); // 内容区域背景

        group.addWidget(mainGroup);

        // 获取玩家实例
        Player player = getPlayerFromWidget(widget);
        if (player == null) return group;

        // 安全获取ServerLevel：仅在服务器玩家时执行
        boolean hasWallet;
        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = serverPlayer.serverLevel(); // 无需强制转换，直接获取ServerLevel
            hasWallet = WalletUtils.hasWallet(player.getUUID(), serverLevel);
        } else {
            hasWallet = false;
        }

        List<Component> list = new ArrayList<>();
        list.add(Component.translatable(text(1)));
        list.add(Component.translatable(text(2)));
        list.add(Component.translatable(text(3)));
        list.add(Component.translatable(text(4)));

        if (hasWallet) {
            list.add(Component.translatable(text(6), player.getName().getString()).withStyle(ChatFormatting.WHITE));
            list.add(Component.translatable(text(7), player.getUUID().toString()));
            mainGroup.addWidget(new ComponentPanelWidget(10, 10, list).setMaxWidthLimit(width - 20));
        } else {
            list.add(ComponentPanelWidget.withButton(Component.translatable(text(8)), "Create a wallet"));
            mainGroup.addWidget(new ComponentPanelWidget(10, 10, list).clickHandler((a, b) -> {
                if (player instanceof ServerPlayer serverPlayer) {
                    ServerLevel serverLevel = serverPlayer.serverLevel();
                    WalletUtils.createAndInitializeWallet(player.getUUID(), player.getName().getString(), serverLevel);
                    initNewPlayerCurrencies(player.getUUID(), serverLevel);
                }
            }).setMaxWidthLimit(width - 20));
        }

        return group;
    }

    @Override
    public IGuiTexture getTabIcon() {
        return new ItemStackTexture(GTOItems.PALM_SIZED_BANK.asItem());
    }

    @Override
    public Component getTitle() {
        return GTOItems.PALM_SIZED_BANK.asStack().getDisplayName();
    }

    private static IFancyUIProvider assetOverview() {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return Component.translatable(text(10));
            }

            final int width = 336;
            final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;
                else {
                    ServerLevel serverLevel;
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverLevel = serverPlayer.serverLevel();
                    } else {
                        serverLevel = null;
                    }
                    mainGroup.addWidget(new ComponentPanelWidget(0, 0,
                            list -> list.add(ComponentPanelWidget.withButton(Component.translatable(text(8)), "add wallet")))
                            .clickHandler((a, b) -> initNewPlayerCurrencies(player.getUUID(), serverLevel))
                            .setMaxWidthLimit(width - 20));
                    addCurrencyRow(mainGroup, player, serverLevel);

                }
                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }

            private void addCurrencyRow(WidgetGroup mainGroup, Player player, ServerLevel serverLevel) {
                Object2LongMap<String> currencyMap = WalletUtils.getCurrencyMap(player.getUUID(), serverLevel);

                WidgetGroup CurrencyGroup = new WidgetGroup(4, 4, width / 2 - 4, currencyMap.size() * 16 + 12);
                WidgetGroup AmountGroup = new WidgetGroup(width / 2, 4, width / 2 - 4, currencyMap.size() * 16 + 12);
                CurrencyGroup.setLayout(Layout.VERTICAL_CENTER);
                CurrencyGroup.setLayoutPadding(4);
                AmountGroup.setLayout(Layout.VERTICAL_CENTER);
                AmountGroup.setLayoutPadding(4);

                CurrencyGroup.addWidget(new LabelWidget(0, 0, Component.translatable(text(11))));
                AmountGroup.addWidget(new LabelWidget(0, 0, Component.translatable(text(12))));

                CurrencyGroup.addWidget(new LabelWidget(0, 0, Component.literal(String.valueOf(currencyMap.size()))));
                AmountGroup.addWidget(new LabelWidget(0, 0, Component.literal(currencyMap.toString())));

                for (String currency : currencyMap.keySet()) {
                    long amount = currencyMap.getLong(currency);
                    CurrencyGroup.addWidget(new LabelWidget(0, 0, Component.translatable("gtocore.bank.currency." + currency)));
                    AmountGroup.addWidget(new LabelWidget(0, 0, Component.literal(Long.toString(amount))));
                }

                AmountGroup.addWidget(new LabelWidget(0, 0,
                        Component.literal(String.valueOf(WalletUtils.getCurrencyAmount(player.getUUID(), serverLevel, "coins")))));

                mainGroup.addWidget(CurrencyGroup);
                mainGroup.addWidget(AmountGroup);
            }
        };
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);
        sideTabs.attachSubTab(assetOverview());
    }

    public static void initNewPlayerCurrencies(UUID playerUUID, ServerLevel world) {
        Object2LongOpenHashMap<String> initialCurrencies = new Object2LongOpenHashMap<>();
        initialCurrencies.put("coins", 100);       // 初始金币100
        initialCurrencies.put("gems", 10);         // 初始宝石10
        initialCurrencies.put("tokens", 50);       // 初始代币50
        WalletUtils.setCurrencies(playerUUID, world, initialCurrencies);
    }

    // 辅助方法
    private void openUI(Item item, Level level, Player player, InteractionHand hand) {
        IItemUIFactory.super.use(item, level, player, hand);
    }

    private static Player getPlayerFromWidget(FancyMachineUIWidget widget) {
        ModularUI modularUI = widget.getGui();
        return (modularUI != null) ? modularUI.entityPlayer : null;
    }
}
