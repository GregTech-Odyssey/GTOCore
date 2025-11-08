package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;
import com.gregtechceu.gtceu.utils.collection.O2LOpenCacheHashMap;

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
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PalmSizedBankBehavior implements IItemUIFactory, IFancyUIProvider {

    public static final PalmSizedBankBehavior INSTANCE = new PalmSizedBankBehavior();

    private static final String TEXT_HEADER = "gtocore.palm_sized_bank.textList.";

    @Persisted
    @DescSynced
    private String choose = null;

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
                .setBackground(GuiTextures.DISPLAY);

        group.addWidget(mainGroup);

        Player player = getPlayerFromWidget(widget);
        if (player == null) return group;

        boolean hasWallet;
        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = serverPlayer.serverLevel();
            hasWallet = WalletUtils.hasWallet(player.getUUID(), serverLevel);
        } else {
            hasWallet = false;
        }

        List<Component> list = new ArrayList<>();
        list.add(Component.translatable(text(1)));
        list.add(Component.translatable(text(2)));
        list.add(Component.translatable(text(3)));
        list.add(Component.translatable(text(4)));
        list.add(Component.empty());

        if (hasWallet) {
            list.add(Component.translatable(text(6), player.getName().getString()).withStyle(ChatFormatting.WHITE));
            list.add(Component.translatable(text(7), player.getUUID().toString()).withStyle(ChatFormatting.GRAY));
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

    private @NotNull IFancyUIProvider assetOverview(PalmSizedBankBehavior parentBehavior) {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return Component.translatable(text(10));
            }

            final int width = 256;
            final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;

                ServerLevel serverLevel;
                if (player instanceof ServerPlayer serverPlayer) {
                    serverLevel = serverPlayer.serverLevel();
                } else {
                    serverLevel = null;
                }

                {
                    mainGroup.addWidget(new ComponentPanelWidget(50, 0,
                            list -> list.add(ComponentPanelWidget.withButton(Component.literal("add aaa"), "add aaa")))
                            .clickHandler((a, b) -> WalletUtils.addCurrency(player.getUUID(), serverLevel, "aaa", 100)));
                    mainGroup.addWidget(new ComponentPanelWidget(100, 0,
                            list -> list.add(ComponentPanelWidget.withButton(Component.literal("add gems"), "add gems")))
                            .clickHandler((a, b) -> WalletUtils.addCurrency(player.getUUID(), serverLevel, "gems", 100)));
                }

                mainGroup.addWidget(new ComponentPanelWidget(8, 4, List1 -> {
                    Object2LongMap<String> syncedCurrencyMap = WalletUtils.getCurrencyMap(player.getUUID(), serverLevel);
                    List1.add(Component.literal("-------------------"));
                    List1.add(Component.translatable(text(11)));
                    List1.add(Component.literal("-------------------"));
                    for (Object2LongMap.Entry<String> entry : syncedCurrencyMap.object2LongEntrySet()) {
                        List1.add(Component.translatable("gtocore.bank.currency." + entry.getKey()));
                    }
                    List1.add(Component.literal("-------------------"));
                }));

                mainGroup.addWidget(new ComponentPanelWidget(width / 2 + 4, 4, List2 -> {
                    Object2LongMap<String> syncedCurrencyMap = WalletUtils.getCurrencyMap(player.getUUID(), serverLevel);
                    List2.add(Component.literal("-------------------"));
                    List2.add(Component.translatable(text(12)));
                    List2.add(Component.literal("-------------------"));
                    for (Object2LongMap.Entry<String> entry : syncedCurrencyMap.object2LongEntrySet()) {
                        List2.add(Component.literal(Long.toString(entry.getLongValue())));
                    }
                    List2.add(Component.literal("-------------------"));
                }));

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }
        };
    }

    private @NotNull IFancyUIProvider transactionRecords(PalmSizedBankBehavior parentBehavior) {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return Component.translatable(text(20));
            }

            final int width = 256;
            final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;

                ServerLevel serverLevel;
                if (player instanceof ServerPlayer serverPlayer) {
                    serverLevel = serverPlayer.serverLevel();
                } else {
                    serverLevel = null;
                }

                {
                    mainGroup.addWidget(new ComponentPanelWidget(0, 0,
                            list -> list.add(ComponentPanelWidget.withButton(Component.literal("add"), "add wallet")))
                            .clickHandler((a, b) -> WalletUtils.addNoneStrategyTransaction(player.getUUID(), "normal", 5, serverLevel)));

                    mainGroup.addWidget(new ComponentPanelWidget(20, 0,
                            list -> list.add(ComponentPanelWidget.withButton(Component.literal("add"), "add wallet")))
                            .clickHandler((a, b) -> WalletUtils.addNoneStrategyTransaction(player.getUUID(), "unmarshal", 5, serverLevel)));

                    mainGroup.addWidget(new ComponentPanelWidget(40, 0,
                            list -> list.add(ComponentPanelWidget.withButton(Component.literal("add"), "add wallet")))
                            .clickHandler((a, b) -> WalletUtils.addNoneStrategyTransaction(player.getUUID(), "epic", 5, serverLevel)));

                    mainGroup.addWidget(new ComponentPanelWidget(60, 0,
                            list -> list.add(ComponentPanelWidget.withButton(Component.literal("add"), "add wallet")))
                            .clickHandler((a, b) -> WalletUtils.addNoneStrategyTransaction(player.getUUID(), "timely", 5, serverLevel)));
                }

                mainGroup.addWidget(new ComponentPanelWidget(width - 11, 0,
                        list -> list.add(ComponentPanelWidget.withButton(Component.literal(" ↩ "), "return")))
                        .clickHandler((a, b) -> choose = null));

                Set<String> syncedTransactionKeys = WalletUtils.getTransactionKeys(player.getUUID(), serverLevel);

                mainGroup.addWidget(new ComponentPanelWidget(8, 4, List1 -> {
                    if (choose == null) {
                        List1.add(Component.literal("-------------------"));
                        List1.add(Component.translatable(text(21)));
                        List1.add(Component.literal("-------------------"));
                        for (String entry : syncedTransactionKeys) {
                            List1.add(ComponentPanelWidget.withButton(Component.literal("§b" + entry + "§r"), entry));
                        }
                        List1.add(Component.literal("-------------------"));
                    } else {
                        List1.add(Component.literal("-------------------"));
                        List1.add(Component.translatable(text(21)));
                        List1.add(Component.translatable(text(22)));
                        List1.add(Component.translatable(text(23)));
                        List1.add(Component.translatable(text(24)));
                        List1.add(Component.translatable(text(25)));
                        List1.add(Component.literal("-------------------"));
                        List1.add(Component.translatable(text(26)));
                        Long2LongMap minuteMap = WalletUtils.getTransactionMinuteMap(player.getUUID(), choose, serverLevel);
                        List<Long> keys = new ArrayList<>(minuteMap.keySet());
                        keys.sort(Collections.reverseOrder());
                        for (Long key : keys) {
                            List1.add(Component.literal(String.valueOf(key)));
                        }
                        List1.add(Component.literal("-------------------"));
                    }
                }).clickHandler((a, b) -> choose = a));

                mainGroup.addWidget(new ComponentPanelWidget(width / 2 + 4, 4, List2 -> {
                    if (choose == null) {
                        List2.add(Component.literal("-------------------"));
                        List2.add(Component.translatable(text(22)));
                        List2.add(Component.literal("-------------------"));
                        for (String entry : syncedTransactionKeys) {
                            List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionTotalAmount(player.getUUID(), entry, serverLevel))));
                        }
                        List2.add(Component.literal("-------------------"));
                    } else {
                        List2.add(Component.literal("-------------------"));
                        List2.add(Component.literal("§b" + choose + "§r"));
                        List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionTotalAmount(player.getUUID(), choose, serverLevel))));
                        List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionType(player.getUUID(), choose, serverLevel))));
                        List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionMinuteAmount(player.getUUID(), choose, WalletUtils.getGameMinuteKey(player), serverLevel))));
                        List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionMinuteAmount(player.getUUID(), choose, WalletUtils.getGameMinuteKey(player) - 1, serverLevel))));
                        List2.add(Component.literal("-------------------"));
                        List2.add(Component.empty());
                        Long2LongMap minuteMap = WalletUtils.getTransactionMinuteMap(player.getUUID(), choose, serverLevel);
                        List<Long> keys = new ArrayList<>(minuteMap.keySet());
                        keys.sort(Collections.reverseOrder());
                        for (Long key : keys) {
                            List2.add(Component.literal(Long.toString(minuteMap.get(key))));
                        }
                        List2.add(Component.literal("-------------------"));
                    }
                }));

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }
        };
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);
        sideTabs.attachSubTab(assetOverview(this));
        sideTabs.attachSubTab(transactionRecords(this));
    }

    public static void initNewPlayerCurrencies(UUID playerUUID, ServerLevel world) {
        O2LOpenCacheHashMap<String> initialCurrencies = new O2LOpenCacheHashMap<>();
        initialCurrencies.put("coins", 9200000000000000000L);
        initialCurrencies.put("gems", 10);
        initialCurrencies.put("tokens", 50);
        initialCurrencies.put("aaa", 50);
        initialCurrencies.put("bbb", 500);
        initialCurrencies.put("ccc", 5000);
        initialCurrencies.put("ddd", 50);
        initialCurrencies.put("eee", 50);
        initialCurrencies.put("fff", 50);
        initialCurrencies.put("ggg", 50);
        initialCurrencies.put("hhh", 50);
        initialCurrencies.put("iii", 50);
        initialCurrencies.put("jjj", 5000000000000000000L);
        WalletUtils.setCurrencies(playerUUID, world, initialCurrencies);
    }

    // 辅助方法
    private void openUI(Item item, Level level, Player player, InteractionHand hand) {
        initializationParameters(player);
        IItemUIFactory.super.use(item, level, player, hand);
    }

    private void initializationParameters(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = serverPlayer.serverLevel();
            WalletUtils.updatePlayerName(player.getUUID(), serverLevel, player.getName().getString());
        }
    }

    private static Player getPlayerFromWidget(FancyMachineUIWidget widget) {
        ModularUI modularUI = widget.getGui();
        return (modularUI != null) ? modularUI.entityPlayer : null;
    }
}
