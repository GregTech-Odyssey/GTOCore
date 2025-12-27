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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import com.fast.fastcollection.O2LOpenCacheHashMap;
import com.hepdd.gtmthings.api.gui.widget.SimpleNumberInputWidget;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.gtocore.common.item.GregMembershipCardItem.createWithUuidAndSharedList;
import static com.gtocore.data.transaction.data.TradeLang.*;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.*;

public class PalmSizedBankBehavior implements IItemUIFactory, IFancyUIProvider {

    public static final PalmSizedBankBehavior INSTANCE = new PalmSizedBankBehavior();

    private static final String TEXT_HEADER = "gtocore.palm_sized_bank.textList.";

    private static @NotNull String text(int id) {
        return TEXT_HEADER + id;
    }

    private static @NotNull MutableComponent trans(int id, Object... args) {
        if (args.length == 1 && args[0] instanceof Object[]) args = (Object[]) args[0];
        return Component.translatable(TEXT_HEADER + id, args);
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
        list.add(trans(1));
        list.add(trans(2));
        list.add(trans(3));
        list.add(trans(4));
        list.add(Component.empty());

        if (hasWallet) {
            list.add(trans(6, player.getName().getString()).withStyle(ChatFormatting.WHITE));
            list.add(trans(7, player.getUUID().toString()).withStyle(ChatFormatting.GRAY));
            mainGroup.addWidget(new ComponentPanelWidget(10, 10, list).setMaxWidthLimit(width - 20));
        } else {
            list.add(ComponentPanelWidget.withButton(trans(8), "Create a wallet"));
            mainGroup.addWidget(new ComponentPanelWidget(10, 10, list).clickHandler((a, b) -> {
                if (player instanceof ServerPlayer serverPlayer) {
                    ServerLevel serverLevel = serverPlayer.serverLevel();
                    WalletUtils.createAndInitializeWallet(player.getUUID(), serverLevel, player.getName().getString());
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

    /**
     * 资产概览
     */
    private @NotNull IFancyUIProvider assetOverview(PalmSizedBankBehavior parentBehavior) {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return GTOItems.PALM_SIZED_BANK.asStack().getDisplayName();
            }

            static final int width = 256;
            static final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;

                ServerLevel serverLevel = player instanceof ServerPlayer serverPlayer ? serverPlayer.serverLevel() : null;

                mainGroup.addWidget(new ComponentPanelWidget(8, 4, List1 -> {
                    Object2LongMap<String> syncedCurrencyMap = WalletUtils.getCurrencyMap(player.getUUID(), serverLevel);
                    List1.add(Component.literal("-------------------"));
                    List1.add(trans(11));
                    List1.add(Component.literal("-------------------"));
                    for (Object2LongMap.Entry<String> entry : syncedCurrencyMap.object2LongEntrySet()) {
                        List1.add(Component.translatable("gtocore.currency." + entry.getKey()).withStyle(ChatFormatting.AQUA));
                    }
                    List1.add(Component.literal("-------------------"));
                }));

                mainGroup.addWidget(new ComponentPanelWidget(width / 2 + 4, 4, List2 -> {
                    Object2LongMap<String> syncedCurrencyMap = WalletUtils.getCurrencyMap(player.getUUID(), serverLevel);
                    List2.add(Component.literal("-------------------"));
                    List2.add(trans(12));
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

    /**
     * 转账
     */
    private @NotNull IFancyUIProvider transfer(PalmSizedBankBehavior parentBehavior) {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return GTOItems.PALM_SIZED_BANK.asStack().getDisplayName();
            }

            @DescSynced
            private UUID uuid = null;
            @DescSynced
            private String string = null;
            @DescSynced
            private boolean confirm1 = false;
            @DescSynced
            private int tradeAmount = 0;

            static final int width = 256;
            static final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;

                ServerLevel serverLevel = player instanceof ServerPlayer serverPlayer ? serverPlayer.serverLevel() : null;

                Object2ObjectMap<UUID, String> WalletPlayers = WalletUtils.getAllWalletPlayers(serverLevel);
                Set<String> CurrencySet = WalletUtils.getCurrencyMap(player.getUUID(), serverLevel).keySet();

                mainGroup.addWidget(new ComponentPanelWidget(8, 4, List1 -> {
                    List1.add(Component.literal("-------------------"));
                    List1.add(trans(50));
                    List1.add(trans(51));
                    List1.add(trans(52));
                    List1.add(ComponentPanelWidget.withButton(trans(53), "confirm1"));
                    List1.add(Component.literal("-------------------"));
                    for (Object2ObjectMap.Entry<UUID, String> entry : WalletPlayers.object2ObjectEntrySet()) {
                        List1.add(ComponentPanelWidget.withHoverTextTranslate(
                                ComponentPanelWidget.withButton(Component.literal("§b" + entry.getValue() + "§r"), entry.getKey().toString()),
                                Component.literal(entry.getValue())));
                    }
                }).clickHandler((a, b) -> {
                    if (a.equals("confirm1"))
                        confirm1 = true;
                    else {
                        uuid = UUID.fromString(a);
                        confirm1 = false;
                    }
                }));

                mainGroup.addWidget(new ComponentPanelWidget(width / 2 + 4, 4, List2 -> {
                    List2.add(Component.literal("-------------------"));
                    List2.add(uuid != null ? Component.translatable(WalletPlayers.get(uuid)) : Component.empty());
                    List2.add(string != null ? Component.translatable("gtocore.currency." + string) : Component.empty());
                    List2.add(Component.empty());
                    List2.add(confirm1 ?
                            ComponentPanelWidget.withButton(trans(54), "confirm2") :
                            Component.empty());
                    List2.add(Component.literal("-------------------"));
                    for (String entry : CurrencySet) {
                        List2.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.currency." + entry), entry));
                    }
                }).clickHandler((a, b) -> {
                    if (a.equals("confirm2")) {
                        long Amount = WalletUtils.getCurrencyAmount(player.getUUID(), serverLevel, string);
                        if (Amount >= tradeAmount) {
                            WalletUtils.subtractCurrency(player.getUUID(), serverLevel, string, tradeAmount);
                            WalletUtils.addCurrency(uuid, serverLevel, string, tradeAmount);
                        } else {
                            tradeAmount = Math.toIntExact(Amount);
                        }
                    } else {
                        string = a;
                        confirm1 = false;
                    }
                }));

                mainGroup.addWidget(new SimpleNumberInputWidget(width / 2 + 5, 4 + 31, width / 4, 8,
                        () -> tradeAmount,
                        newValue -> {
                            tradeAmount = newValue;
                            confirm1 = false;
                        }));

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }
        };
    }

    /**
     * 交易记录
     */
    private @NotNull IFancyUIProvider tradeRecords(PalmSizedBankBehavior parentBehavior) {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return GTOItems.PALM_SIZED_BANK.asStack().getDisplayName();
            }

            @DescSynced
            private String choose = null;

            static final int width = 256;
            static final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;

                ServerLevel serverLevel = player instanceof ServerPlayer serverPlayer ? serverPlayer.serverLevel() : null;

                mainGroup.addWidget(new ComponentPanelWidget(width - 11, 0,
                        list -> list.add(ComponentPanelWidget.withButton(Component.literal(" ↩ "), "return")))
                        .clickHandler((a, b) -> choose = null));

                Set<String> syncedTransactionKeys = WalletUtils.getTransactionKeys(player.getUUID(), serverLevel);

                mainGroup.addWidget(new ComponentPanelWidget(8, 4, List1 -> {
                    if (choose == null) {
                        List1.add(Component.literal("-------------------"));
                        List1.add(trans(21));
                        List1.add(Component.literal("-------------------"));
                        for (String entry : syncedTransactionKeys) {
                            List1.add(ComponentPanelWidget.withButton(Component.literal("§b" + entry + "§r"), entry));
                        }
                        List1.add(Component.literal("-------------------"));
                    } else {
                        List1.add(Component.literal("-------------------"));
                        List1.add(trans(21));
                        List1.add(trans(22));
                        List1.add(trans(23));
                        List1.add(trans(24));
                        List1.add(trans(25));
                        List1.add(Component.literal("-------------------"));
                        List1.add(trans(26));
                        Long2LongMap minuteMap = WalletUtils.getTransactionMinuteMap(player.getUUID(), serverLevel, choose);
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
                        List2.add(trans(22));
                        List2.add(Component.literal("-------------------"));
                        for (String entry : syncedTransactionKeys) {
                            List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionTotalAmount(player.getUUID(), serverLevel, entry))));
                        }
                        List2.add(Component.literal("-------------------"));
                    } else {
                        List2.add(Component.literal("-------------------"));
                        List2.add(Component.literal("§b" + choose + "§r"));
                        List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionTotalAmount(player.getUUID(), serverLevel, choose))));
                        List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionType(player.getUUID(), serverLevel, choose))));
                        List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionMinuteAmount(player.getUUID(), serverLevel, choose, WalletUtils.getGameMinuteKey(player)))));
                        List2.add(Component.literal(String.valueOf(WalletUtils.getTransactionMinuteAmount(player.getUUID(), serverLevel, choose, WalletUtils.getGameMinuteKey(player) - 1))));
                        List2.add(Component.literal("-------------------"));
                        List2.add(Component.empty());
                        Long2LongMap minuteMap = WalletUtils.getTransactionMinuteMap(player.getUUID(), serverLevel, choose);
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

    /**
     * 钱包标签表
     */
    private @NotNull IFancyUIProvider tagList(PalmSizedBankBehavior parentBehavior) {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return GTOItems.PALM_SIZED_BANK.asStack().getDisplayName();
            }

            @DescSynced
            private String choose = null;

            static final int width = 256;
            static final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;

                ServerLevel serverLevel = player instanceof ServerPlayer serverPlayer ? serverPlayer.serverLevel() : null;

                mainGroup.addWidget(new ComponentPanelWidget(width - 11, 0,
                        list -> list.add(ComponentPanelWidget.withButton(Component.literal(" ↩ "), "return")))
                        .clickHandler((a, b) -> choose = null));

                mainGroup.addWidget(new ComponentPanelWidget(8, 4, List1 -> {
                    List1.add(Component.literal("-------------------"));
                    List1.add(trans(30));
                    List1.add(Component.literal("-------------------"));
                    if (choose == null) {
                        Set<String> tagKeysSet = WalletUtils.getAllTagKeysFromWallet(player.getUUID(), serverLevel);
                        for (String entry : tagKeysSet) {
                            List1.add(ComponentPanelWidget.withButton(Component.translatable(entry), entry).copy().withStyle(ChatFormatting.AQUA));
                        }
                    } else List1.add(Component.translatable(choose).withStyle(ChatFormatting.AQUA));
                }).clickHandler((a, b) -> choose = a));

                mainGroup.addWidget(new ComponentPanelWidget(width / 2 + 4, 4, List2 -> {
                    List2.add(Component.literal("-------------------"));
                    List2.add(trans(31));
                    List2.add(Component.literal("-------------------"));
                    if (choose != null) {
                        Set<String> tagsSet = WalletUtils.getTagsFromWallet(player.getUUID(), serverLevel, choose);
                        for (String entry : tagsSet) {
                            List2.add(Component.literal(entry));
                        }
                    }
                }));

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }
        };
    }

    /**
     * 申请会员卡
     */
    private @NotNull IFancyUIProvider generateCard(PalmSizedBankBehavior parentBehavior) {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return GTOItems.PALM_SIZED_BANK.asStack().getDisplayName();
            }

            static final int width = 256;
            static final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;

                ServerLevel serverLevel = player instanceof ServerPlayer serverPlayer ? serverPlayer.serverLevel() : null;

                Object2ObjectMap<UUID, String> WalletPlayers = WalletUtils.getAllWalletPlayers(serverLevel);
                Set<UUID> shared = new HashSet<>();

                mainGroup.addWidget(new ComponentPanelWidget(10, 16,
                        list -> list.add(ComponentPanelWidget.withHoverTextTranslate(
                                ComponentPanelWidget.withButton(trans(41), "getGrayMembershipCard"),
                                trans(40, Component.translatable("gtocore.currency." + TECH_OPERATOR_COIN), 15))))
                        .clickHandler((a, b) -> {
                            if (WalletUtils.getCurrencyAmount(player.getUUID(), serverLevel, TECH_OPERATOR_COIN) >= 15) {
                                ItemEntity itemEntity = player.spawnAtLocation(createWithUuidAndSharedList(player.getUUID(), new ArrayList<>(shared)));
                                WalletUtils.subtractCurrency(player.getUUID(), serverLevel, TECH_OPERATOR_COIN, 15);
                                if (itemEntity != null) itemEntity.setNoPickUpDelay();
                            }
                        }));

                mainGroup.addWidget(new LabelWidget(10, 32, trans(42)));

                mainGroup.addWidget(new ComponentPanelWidget(8, 4, List1 -> {
                    List1.add(Component.literal("-------------------"));
                    List1.add(Component.empty());
                    List1.add(Component.empty());
                    List1.add(Component.empty());
                    List1.add(Component.literal("-------------------"));
                    for (Object2ObjectMap.Entry<UUID, String> entry : WalletPlayers.object2ObjectEntrySet()) {
                        List1.add(ComponentPanelWidget.withHoverTextTranslate(
                                ComponentPanelWidget.withButton(Component.literal("§b" + entry.getValue() + "§r"), entry.getKey().toString()), Component.literal(entry.getKey().toString())));
                    }
                }).clickHandler((a, b) -> shared.add(UUID.fromString(a))));

                mainGroup.addWidget(new ComponentPanelWidget(width / 2 + 4, 4, List2 -> {
                    List2.add(Component.literal("-------------------"));
                    List2.add(Component.empty());
                    List2.add(Component.empty());
                    List2.add(Component.empty());
                    List2.add(Component.literal("-------------------"));
                    for (UUID entry : shared) {
                        List2.add(ComponentPanelWidget.withHoverTextTranslate(
                                ComponentPanelWidget.withButton(Component.literal(WalletPlayers.get(entry)), entry.toString()),
                                Component.literal(entry.toString())));
                    }
                }).clickHandler((a, b) -> shared.remove(UUID.fromString(a))));

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
        sideTabs.attachSubTab(transfer(this));
        sideTabs.attachSubTab(tradeRecords(this));
        sideTabs.attachSubTab(tagList(this));
        sideTabs.attachSubTab(generateCard(this));
    }

    private static void initNewPlayerCurrencies(UUID playerUUID, ServerLevel world) {
        O2LOpenCacheHashMap<String> initialCurrencies = new O2LOpenCacheHashMap<>();
        initialCurrencies.put(TECH_OPERATOR_COIN, 37);
        WalletUtils.setCurrencies(playerUUID, world, initialCurrencies);
        WalletUtils.addTagToWallet(playerUUID, world, UNLOCK_SHOP, UNLOCK_BASE);
        WalletUtils.addTagToWallet(playerUUID, world, UNLOCK_TRADE, UNLOCK_BASE);
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
