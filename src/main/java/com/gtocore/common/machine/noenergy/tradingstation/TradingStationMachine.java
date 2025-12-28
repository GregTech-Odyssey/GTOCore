package com.gtocore.common.machine.noenergy.tradingstation;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.api.gui.InteractiveImageWidget;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.translation.GTOMachineTooltips;
import com.gtocore.data.transaction.manager.TradeData;
import com.gtocore.data.transaction.manager.TradeEntry;
import com.gtocore.data.transaction.manager.TradingManager;
import com.gtocore.data.transaction.manager.UnlockManager;

import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.TankWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IAutoOutputBoth;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.hepdd.gtmthings.utils.TeamUtil;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static com.gtocore.common.item.GregMembershipCardItem.*;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.UNLOCK_SHOP;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.UNLOCK_TRADE;
import static com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup.ScrollWheelDirection.HORIZONTAL;

public class TradingStationMachine extends MetaMachine implements IFancyUIMachine, IAutoOutputBoth, IMachineLife, IControllable {

    /////////////////////////////////////
    // *********** 数据存储 *********** //
    /////////////////////////////////////

    /** 输入输出存储 */
    @Getter
    @Persisted
    @DescSynced
    private final NotifiableItemStackHandler inputItem;
    @Getter
    @Persisted
    @DescSynced
    private final NotifiableItemStackHandler outputItem;
    @Getter
    @Persisted
    @DescSynced
    private final NotifiableFluidTank inputFluid;
    @Getter
    @Persisted
    @DescSynced
    private final NotifiableFluidTank outputFluid;

    /** 其他位置存储 */
    @Persisted
    private final CustomItemStackHandler cardHandler;

    private static final int Item_slots_in_a_row = 4;
    private static final int Fluid_slots_in_a_row = 4;

    /** 玩家信息 */
    @Getter
    @Persisted
    private UUID uuid;
    @Getter
    @Persisted
    List<UUID> sharedUUIDs = new ArrayList<>();
    @Getter
    @Persisted
    private UUID teamUUID;

    /** 交易信息 */
    @Persisted
    @DescSynced
    private int groupSelected = 0;
    private int shopSelected = -1;

    /////////////////////////////////////
    // ********* 生命周期管理 ********* //
    /////////////////////////////////////

    public TradingStationMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder);

        cardHandler = new CustomItemStackHandler();
        cardHandler.setFilter(i -> i.getItem() == GTOItems.GREG_MEMBERSHIP_CARD.asItem());
        cardHandler.setOnContentsChanged(() -> initializationInformation(cardHandler.getStackInSlot(0)));

        inputItem = new NotifiableItemStackHandler(this, 32 * tier, IO.IN, IO.BOTH);
        outputItem = new NotifiableItemStackHandler(this, 32 * tier, IO.OUT, IO.OUT);
        inputFluid = new NotifiableFluidTank(this, tier * 4, 1000 * (8000 << tier), IO.IN, IO.BOTH);
        outputFluid = new NotifiableFluidTank(this, tier * 4, 1000 * (8000 << tier), IO.OUT, IO.OUT);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        initializationInformation(cardHandler.getStackInSlot(0));
        if (!isRemote()) {
            outputItemChangeSub = outputItem.addChangedListener(this::updateAutoOutputSubscription);
            outputFluidChangeSub = outputFluid.addChangedListener(this::updateAutoOutputSubscription);
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (autoOutputSubs != null) {
            autoOutputSubs.unsubscribe();
            autoOutputSubs = null;
        }
        if (outputItemChangeSub != null) {
            outputItemChangeSub.unsubscribe();
            outputItemChangeSub = null;
        }
        if (outputFluidChangeSub != null) {
            outputFluidChangeSub.unsubscribe();
            outputFluidChangeSub = null;
        }
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(cardHandler);
        clearInventory(inputItem.storage);
        clearInventory(outputItem.storage);
    }

    /////////////////////////////////////
    // ************ UI实现 ************ //

    private static final int width = 336;
    private static final int height = 144;

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, width + 8, height + 8);

        WidgetGroup mainGroup = new WidgetGroup(4, 4, width, height);
        mainGroup.setBackground(GuiTextures.DISPLAY);

        // 底边展开面板
        mainGroup.addWidget(new DraggableScrollableWidgetGroup(4, 34, width - 90, height - 34)
                .setYScrollBarWidth(2)
                .setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1))
                .addWidget(new ComponentPanelWidget(0, 0, GTOMachineTooltips.INSTANCE.getPanGalaxyGregTechTradingStationIntroduction().get()).setMaxWidthLimit(width - 90)));

        Level level = getLevel();
        ServerLevel serverLevel = getLevel() instanceof ServerLevel ? (ServerLevel) getLevel() : null;

        // 左侧卡片槽和信息
        mainGroup.addWidget(new SlotWidget(cardHandler, 0, 10, 10)
                .setBackgroundTexture(GuiTextures.SLOT).setHoverTooltips(trans(11)));

        Object2ObjectMap<UUID, String> WalletPlayers = WalletUtils.getAllWalletPlayers(serverLevel);

        mainGroup.addWidget(new ComponentPanelWidget(34, 14, textList -> {
            if (uuid == null) {
                textList.add(trans(2));
                return;
            }
            String playerName = WalletPlayers.getOrDefault(uuid, "Unknown");
            boolean hasShared = !sharedUUIDs.isEmpty();
            boolean hasTeam = Optional.ofNullable(teamUUID).filter(t -> !t.equals(uuid)).isPresent();
            if (hasShared || hasTeam) {
                String sharedText = sharedUUIDs.stream()
                        .map(shareUuid -> WalletPlayers.getOrDefault(shareUuid, "Unknown"))
                        .collect(Collectors.joining(", "));
                MutableComponent sharedComponent = Component.literal(sharedText);
                Optional.ofNullable(teamUUID)
                        .filter(t -> !t.equals(uuid))
                        .map(t -> TeamUtil.GetName(level, uuid))
                        .ifPresent(sharedComponent::append);
                if (sharedComponent.getString().isEmpty()) {
                    sharedComponent = trans(4);
                }
                textList.add(ComponentPanelWidget.withHoverTextTranslate(
                        trans(3, playerName),
                        sharedComponent));
            } else {
                textList.add(trans(3, playerName));
            }
        }).setMaxWidthLimit(256 - 34));

        // 刷新
        mainGroup.addWidget(new InteractiveImageWidget(237, 10, 9, 9, GTOGuiTextures.REFRESH)
                .textSupplier(texts -> texts.add(trans(8)))
                .clickHandler((data, clickData) -> {
                    initializationInformation(cardHandler.getStackInSlot(0));
                    Player player = mainGroup.getGui().entityPlayer;
                    if (!isRemote() && player != null) {
                        if (shouldOpenUI(player, InteractionHand.MAIN_HAND, null)) {
                            tryToOpenUI(player, InteractionHand.MAIN_HAND, null);
                        }
                    }
                }));

        // 左右分区线
        mainGroup.addWidget(new ImageWidget(253, 2, 2, 140, GuiTextures.SLOT));

        // 右侧商店组切换面板
        mainGroup.addWidget(ShopGroupSwitchWidget());

        group.addWidget(mainGroup);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);

        return group;
    }

    @Override
    public IGuiTexture getTabIcon() {
        return GuiTextures.GREGTECH_LOGO;
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setId("fancy_side_tabs");
        sideTabs.clearSubTabs();
        sideTabs.setMainTab(this);

        // 添加固定标签
        List<IFancyUIProvider> fixedTabs = new ArrayList<>();
        if (groupSelected == 0) {
            fixedTabs.add(ItemStorageUI());
            fixedTabs.add(FluidStorageUI());
            fixedTabs.add(TransactionUnlock());
            fixedTabs.add(CombinedDirectionalFancyConfigurator.of(this, this));
        }

        // 动态生成商店标签
        List<IFancyUIProvider> originalShopTabs = shopGroup();
        List<IFancyUIProvider> displayShopTabs = new ArrayList<>(originalShopTabs);

        // 添加所有标签
        fixedTabs.forEach(sideTabs::attachSubTab);
        displayShopTabs.forEach(sideTabs::attachSubTab);

        // 标签切换监听器
        sideTabs.setOnTabSwitch((oldTab, newTab) -> {
            if (newTab instanceof ShopTabProvider newShopTab) {
                // 只允许选择当前组的商店标签
                if (newShopTab.groupIndex == groupSelected) {
                    shopSelected = originalShopTabs.indexOf(newShopTab);
                } else {
                    shopSelected = -1;
                    sideTabs.selectTab(sideTabs.getMainTab());
                }
            } else {
                shopSelected = -1;
            }
            sideTabs.detectAndSendChanges();

            ModularUI modularUI = sideTabs.getGui();
            if (modularUI != null && modularUI.getModularUIGui() != null) {
                modularUI.getModularUIGui().init();
            }
        });

        if (shopSelected != -1 && shopSelected < originalShopTabs.size()) {
            sideTabs.selectTab(originalShopTabs.get(shopSelected));
        } else {
            sideTabs.selectTab(sideTabs.getMainTab());
        }

        sideTabs.detectAndSendChanges();
    }

    private WidgetGroup ShopGroupSwitchWidget() {
        WidgetGroup mainGroup = new WidgetGroup(256, 0, 80, height - 8);
        mainGroup.setLayout(Layout.VERTICAL_CENTER);
        mainGroup.setLayoutPadding(10);

        TradingManager.TradingShopGroup SwitchedShopGroup = TradingManager.INSTANCE.getShopGroup(groupSelected);
        if (SwitchedShopGroup == null) SwitchedShopGroup = TradingManager.INSTANCE.getShopGroup(0);
        if (SwitchedShopGroup != null) {
            mainGroup.addWidget(new ImageWidget(0, 0, 80, 13,
                    new TextTexture(Component.translatable(SwitchedShopGroup.getName()).copy().getString())
                            .setDropShadow(false)
                            .setType(TextTexture.TextType.ROLL)
                            .setWidth(80)));
            mainGroup.addWidget(new ImageWidget(0, 10, 64, 64, SwitchedShopGroup.getTexture1()));
        }

        WidgetGroup SwitchWidget = new WidgetGroup(0, 80, 79, 39);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 8; x++) {
                int index = y * 8 + x;
                TradingManager.TradingShopGroup shopGroup = TradingManager.INSTANCE.getShopGroup(index);
                if (shopGroup != null) {
                    SwitchWidget.addWidget(new InteractiveImageWidget(x * 10, y * 10, 9, 9, shopGroup.getTexture2())
                            .textSupplier(texts -> texts.add(Component.translatable(shopGroup.getName())))
                            .clickHandler((data, clickData) -> {
                                if (groupSelected != index) {
                                    groupSelected = index;
                                    shopSelected = -1;
                                    markAsDirty();

                                    Player player = SwitchWidget.getGui().entityPlayer;
                                    if (!isRemote() && player != null) {
                                        if (shouldOpenUI(player, InteractionHand.MAIN_HAND, null)) {
                                            tryToOpenUI(player, InteractionHand.MAIN_HAND, null);
                                        }
                                    }
                                }
                            }).setBackground(GTOGuiTextures.BOXED_BACKGROUND));
                } else {
                    SwitchWidget.addWidget(new ImageWidget(x * 10, y * 10, 9, 9, GTOGuiTextures.BOXED_BACKGROUND));
                }
            }
        }
        mainGroup.addWidget(SwitchWidget);

        return mainGroup;
    }

    // 库存展示
    private @NotNull IFancyUIProvider ItemStorageUI() {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return new ItemStackTexture(Blocks.CHEST.asItem());
            }

            @Override
            public Component getTitle() {
                return Component.translatable("gtocore.trading_station.item_storage");
            }

            @Override
            public List<Component> getTabTooltips() {
                return Collections.singletonList(Component.translatable("gtocore.trading_station.item_storage"));
            }

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, 176, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, 169, height)
                        .setBackground(GuiTextures.DISPLAY).setYScrollBarWidth(2).setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1));

                int itemHigh = inputItem.getSize() / Item_slots_in_a_row;
                WidgetGroup Item_slot = new WidgetGroup(2, 4, 168, itemHigh * 18 + 10);
                Item_slot.addWidget(new ComponentPanelWidget(0, 0, List.of(Component.translatable("gtocore.trading_station.item_storage"))));
                for (int y = 0; y < itemHigh; y++) {
                    for (int x = 0; x < Item_slots_in_a_row; x++) {
                        int slotIndex = y * Item_slots_in_a_row + x;
                        if (inputItem.getSize() > slotIndex) {
                            Item_slot.addWidget(new SlotWidget(inputItem, slotIndex, x * 18, 10 + y * 18, true, true)
                                    .setBackground(GuiTextures.SLOT));
                            Item_slot.addWidget(new SlotWidget(outputItem, slotIndex, x * 18 + Item_slots_in_a_row * 18 + 18, 10 + y * 18, true, false)
                                    .setBackground(GuiTextures.SLOT));
                        } else break;
                    }
                }
                mainGroup.addWidget(Item_slot);
                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }
        };
    }

    private @NotNull IFancyUIProvider FluidStorageUI() {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return new ItemStackTexture(Items.BUCKET);
            }

            @Override
            public Component getTitle() {
                return Component.translatable("gtocore.trading_station.fluid_storage");
            }

            @Override
            public List<Component> getTabTooltips() {
                return Collections.singletonList(Component.translatable("gtocore.trading_station.fluid_storage"));
            }

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, 176, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, 169, height)
                        .setBackground(GuiTextures.DISPLAY).setYScrollBarWidth(2).setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1));

                int fluidHigh = inputFluid.getSize() / Fluid_slots_in_a_row;
                WidgetGroup Fluid_slot = new WidgetGroup(2, 4, 168, fluidHigh * 18 + 10);
                Fluid_slot.addWidget(new ComponentPanelWidget(0, 0, List.of(Component.translatable("gtocore.trading_station.fluid_storage"))));
                for (int y = 0; y < fluidHigh; y++) {
                    for (int x = 0; x < Fluid_slots_in_a_row; x++) {
                        int slotIndex = y * Fluid_slots_in_a_row + x;
                        if (inputFluid.getSize() > slotIndex) {
                            Fluid_slot.addWidget(new TankWidget(inputFluid, slotIndex, x * 18, 10 + y * 18, true, true)
                                    .setBackground(GuiTextures.SLOT_DARK));
                            Fluid_slot.addWidget(new TankWidget(outputFluid, slotIndex, x * 18 + Fluid_slots_in_a_row * 18 + 18, 10 + y * 18, true, true)
                                    .setBackground(GuiTextures.SLOT_DARK));
                        } else break;
                    }
                }
                mainGroup.addWidget(Fluid_slot);

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }
        };
    }

    // 交易解锁
    private @NotNull IFancyUIProvider TransactionUnlock() {
        return new IFancyUIProvider() {

            @DescSynced
            private String upgradeSelect = null;

            private int internalPageSelected = 0;

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.BUTTON_LOCK;
            }

            @Override
            public Component getTitle() {
                return Component.translatable("gtocore.trading_station.unlock_shop");
            }

            @Override
            public List<Component> getTabTooltips() {
                return Collections.singletonList(Component.translatable("gtocore.trading_station.unlock_shop"));
            }

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new WidgetGroup(4, 4, width, height);
                mainGroup.setBackground(GuiTextures.DISPLAY);

                // 右侧交易项容器
                WidgetGroup tradeContainer = new WidgetGroup(0, 12, 204, 101);

                // 左侧：当前等级和升级按钮
                WidgetGroup leftPanel = new DraggableScrollableWidgetGroup(0, 5, 110, height - 10);
                leftPanel.setLayout(Layout.VERTICAL_CENTER);
                leftPanel.setLayoutPadding(8);

                leftPanel.addWidget(new ComponentPanelWidget(0, 0, textList -> textList.add(trans(21))).setSpace(8).setCenter(true));

                leftPanel.addWidget(new ComponentPanelWidget(0, 10, textList -> {
                    Set<String> keySet = UnlockManager.INSTANCE.getKeySet();
                    if (keySet == null) return;
                    for (String key : keySet) {
                        textList.add(ComponentPanelWidget.withButton(Component.translatable(key),
                                key).copy().withStyle(ChatFormatting.AQUA));
                    }

                }).clickHandler(((upgrade, clickData) -> {
                    upgradeSelect = upgrade;
                    internalPageSelected = 0;
                    updateWidget(tradeContainer, widget);
                })).setSpace(8).setCenter(true));

                // 右侧：升级列表
                WidgetGroup rightPanel = new DraggableScrollableWidgetGroup(110, 8, width - 110, height - 10);
                rightPanel.setLayout(Layout.VERTICAL_CENTER);
                rightPanel.setLayoutPadding(4);

                rightPanel.addWidget(new ComponentPanelWidget(0, 0, textList -> {
                    if (upgradeSelect != null) textList.add(Component.translatable(upgradeSelect));
                }));

                // 2. 交易项容器（显示升级所需资源）
                updateWidget(tradeContainer, widget);
                rightPanel.addWidget(tradeContainer);

                // 3. 分页控件（仅当交易项数量 > 10 时显示）
                rightPanel.addWidget(new ComponentPanelWidget(0, 5, textList -> {
                    int tradeCount = getCurrentTradeCount();
                    if (tradeCount <= 10) return;

                    int totalPage = tradeCount / 10 + (tradeCount % 10 == 0 ? 0 : 1);
                    textList.add(Component.empty()
                            .append(ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_page"))
                            .append(Component.literal(" " + (internalPageSelected + 1) + "/" + totalPage + " "))
                            .append(ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_page")));
                }).clickHandler((data, clickData) -> {
                    int tradeCount = getCurrentTradeCount();
                    int totalPage = tradeCount / 10 + (tradeCount % 10 == 0 ? 0 : 1);
                    if (totalPage <= 1) return;
                    switch (data) {
                        case "previous_page" -> internalPageSelected = Mth.clamp(internalPageSelected - 1, 0, totalPage - 1);
                        case "next_page" -> internalPageSelected = Mth.clamp(internalPageSelected + 1, 0, totalPage - 1);
                    }
                    updateWidget(tradeContainer, widget);
                    widget.detectAndSendChanges();
                }));

                // 组装主面板
                mainGroup.addWidget(leftPanel);
                mainGroup.addWidget(rightPanel);
                mainGroup.addWidget(new ImageWidget(109, 5, 2, height - 10, GuiTextures.SLOT));

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }

            private int getCurrentTradeCount() {
                if (upgradeSelect == null) return 0;
                return UnlockManager.INSTANCE.getEntryTradeCount(upgradeSelect);
            }

            private void updateWidget(WidgetGroup container, FancyMachineUIWidget widget) {
                container.clearAllWidgets();
                if (upgradeSelect == null) return;
                container.addWidget(tradeGroup_10(upgradeSelect, internalPageSelected));
                widget.detectAndSendChanges();
            }

            /**
             * 构建 2 行 5 列的交易项组（适配容器尺寸）
             */
            private WidgetGroup tradeGroup_10(String key, int pageIndex) {
                WidgetGroup tradeGroup = new WidgetGroup(0, 0, 204, 101);
                int startIndex = pageIndex * 10;

                for (int row = 0; row < 2; row++) {
                    for (int col = 0; col < 5; col++) {
                        int X = col * (40 + 1);
                        int Y = row * (50 + 1);
                        int entryIndex = startIndex + row * 5 + col;
                        TradeEntry tradeEntry = UnlockManager.INSTANCE.getTradeEntry(key, entryIndex);
                        if (tradeEntry != null) {
                            tradeGroup.addWidget(trade(X, Y, true, tradeEntry));
                        } else {
                            tradeGroup.addWidget(emptyTrade(X, Y));
                        }
                    }
                }
                return tradeGroup;
            }
        };
    }

    /////////////////////////////////////
    // ********* UI单元构建 ********* //
    /////////////////////////////////////

    // 一个交易组
    private List<IFancyUIProvider> shopGroup() {
        List<IFancyUIProvider> shopGroupTabs = new ArrayList<>();

        for (int shop = 0; shop < TradingManager.INSTANCE.getShopCount(groupSelected); shop++) {
            TradingManager.TradingShop tradingShop = TradingManager.INSTANCE.getShopByIndices(groupSelected, shop);

            shopGroupTabs.add(new ShopTabProvider(this, groupSelected, shop, tradingShop));
        }
        return shopGroupTabs;
    }

    // 16个交易的组
    private WidgetGroup tradeGroup_16(int groupIndex, int shopIndex, int pageIndex, boolean unlockShop) {
        WidgetGroup tradeGroup = new WidgetGroup(0, 0, 327, 102);

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 8; col++) {
                int X = col * 41;
                int Y = row * 51;
                int entryIndex = pageIndex * 16 + row * 8 + col;
                boolean isIndexValid = TradingManager.INSTANCE.isTradeIndexValid(groupIndex, shopIndex, entryIndex);
                if (isIndexValid) {
                    tradeGroup.addWidget(trade(X, Y, unlockShop, TradingManager.INSTANCE.getTradeEntryByIndices(groupIndex, shopIndex, entryIndex)));
                } else {
                    tradeGroup.addWidget(emptyTrade(X, Y));
                }
            }
        }

        return tradeGroup;
    }

    // 单个交易
    private WidgetGroup trade(int x, int y, boolean unlockShop, TradeEntry entry) {
        WidgetGroup trade = new WidgetGroup(x, y, 40, 50);
        trade.setBackground(GTOGuiTextures.BOXED_BACKGROUND);

        ServerLevel serverLevel = getLevel() instanceof ServerLevel ? (ServerLevel) getLevel() : null;

        TradeData tradeData = new TradeData(this.getLevel(), this.getPos(), inputItem, outputItem, inputFluid, outputFluid, uuid, sharedUUIDs, teamUUID);
        boolean unlock = WalletUtils.containsTagValueInWallet(uuid, serverLevel, UNLOCK_TRADE, entry.unlockCondition());
        boolean canExecute = entry.canExecuteCount(tradeData) != 0;

        trade.addWidget(new InteractiveImageWidget(2, 7, 36, 36, entry.texture())
                .textSupplier(texts -> {
                    if (!unlock) texts.add(Component.translatable("gtocore.trade_group.unlock", entry.unlockCondition()).withStyle(ChatFormatting.DARK_RED));
                    if (!canExecute) texts.add(Component.translatable("gtocore.trade_group.unsatisfied").withStyle(ChatFormatting.DARK_RED));
                    int k = 0;
                    if (unlock && canExecute) {
                        k = entry.check(tradeData);
                        texts.add(Component.translatable("gtocore.trade_group.amount", FormattingUtil.formatNumbers(k)).withStyle(ChatFormatting.GOLD));
                    }
                    texts.addAll(entry.getDescription());
                    if (k >= 10) texts.add(Component.translatable("gtocore.trade_group.repeatedly1"));
                    if (k >= 100) texts.add(Component.translatable("gtocore.trade_group.repeatedly2"));
                })
                .clickHandler((data, clickData) -> {
                    if (!unlockShop) return;
                    if (!unlock) return;
                    int multiplier = clickData.isCtrlClick ? (clickData.isShiftClick ? 100 : 10) : 1;
                    entry.executeTrade(tradeData, multiplier);
                }));

        return trade;
    }

    private ImageWidget emptyTrade(int x, int y) {
        return new ImageWidget(x, y, 40, 50, GTOGuiTextures.BOXED_BACKGROUND);
    }

    /////////////////////////////////////
    // ********* 辅助类与方法 ********* //
    /////////////////////////////////////

    private static final String TEXT_HEADER = "gtocore.trading_station.textList.";

    // 机器基础翻译键
    private static @NotNull MutableComponent trans(int id, Object... args) {
        if (args.length == 1 && args[0] instanceof Object[]) args = (Object[]) args[0];
        return Component.translatable(TEXT_HEADER + id, args);
    }

    // 玩家信息初始化
    private void initializationInformation(ItemStack card) {
        if (card.getItem() == GTOItems.GREG_MEMBERSHIP_CARD.asItem()) {
            this.uuid = getSingleUuid(card);
            this.sharedUUIDs = getSharedUuids(card);
            if (uuid != null) {
                this.teamUUID = TeamUtil.getTeamUUID(uuid);
            }
        } else {
            this.uuid = null;
            this.sharedUUIDs = new ArrayList<>();
            this.teamUUID = null;
        }
    }

    /////////////////////////////////////
    // **** 内部类：ShopTabProvider **** //
    /////////////////////////////////////

    // 一个商店
    private static class ShopTabProvider implements IFancyUIProvider {

        private final TradingStationMachine machine;
        private final int groupIndex;
        @Getter
        private final int shopIndex;
        private final TradingManager.TradingShop tradingShop;
        private int localPageSelected = 0;

        private ShopTabProvider(TradingStationMachine machine, int groupIndex, int shopIndex, TradingManager.TradingShop tradingShop) {
            this.machine = machine;
            this.groupIndex = groupIndex;
            this.shopIndex = shopIndex;
            this.tradingShop = tradingShop;
        }

        @Override
        public IGuiTexture getTabIcon() {
            return tradingShop.getTexture();
        }

        @Override
        public Component getTitle() {
            return Component.translatable(tradingShop.getName());
        }

        @Override
        public List<Component> getTabTooltips() {
            return Collections.singletonList(Component.translatable(tradingShop.getName()));
        }

        @Override
        public Widget createMainPage(FancyMachineUIWidget widget) {
            var group = new WidgetGroup(0, 0, width + 8, height + 8);
            WidgetGroup mainGroup = new WidgetGroup(4, 4, width, height);
            mainGroup.setBackground(GuiTextures.DISPLAY);

            int tradeCount = TradingManager.INSTANCE.getTradeCount(groupIndex, shopIndex);
            int totalPage = tradeCount / 16 + (tradeCount % 16 == 0 ? 0 : 1);

            WidgetGroup shopGroup = new WidgetGroup(0, 0, width, height);
            shopGroup.setLayout(Layout.VERTICAL_CENTER);
            shopGroup.setLayoutPadding(3);

            ServerLevel serverLevel = machine.getLevel() instanceof ServerLevel ? (ServerLevel) machine.getLevel() : null;

            boolean unlockShop = WalletUtils.containsTagValueInWallet(machine.getUuid(), serverLevel, UNLOCK_SHOP, tradingShop.getUnlockCondition());

            shopGroup.addWidget(new LabelWidget(0, 0, Component.translatable(tradingShop.getName())));
            WidgetGroup componentGroup = new DraggableScrollableWidgetGroup(4, 12, width - 8, 10)
                    .setScrollWheelDirection(HORIZONTAL);
            componentGroup.addWidget(new ComponentPanelWidget(0, 0, textList -> {
                MutableComponent component = Component.empty();
                if (!unlockShop) {
                    component.append(trans(20, tradingShop.getUnlockCondition()).withStyle(ChatFormatting.RED));
                }
                for (String string : tradingShop.getCurrencies()) {
                    component.append(
                            Component.empty().append(Component.literal("["))
                                    .append(Component.translatable("gtocore.currency." + string))
                                    .append(Component.literal("-"))
                                    .append(Component.literal(FormattingUtil.formatNumbers(WalletUtils.getCurrencyAmount(machine.getUuid(), serverLevel, string))))
                                    .append(Component.literal("]")));
                    textList.add(component);
                }
            }));
            shopGroup.addWidget(componentGroup);

            WidgetGroup tradeContainer = new WidgetGroup(0, 36, 327, 102);
            updateTradeContainer(tradeContainer, localPageSelected, unlockShop);
            shopGroup.addWidget(tradeContainer);

            shopGroup.addWidget(new ComponentPanelWidget(0, 140, textList -> textList.add(Component.empty()
                    .append(ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_page"))
                    .append(Component.literal("<" + (localPageSelected + 1) + "/" + totalPage + ">"))
                    .append(ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_page"))))
                    .clickHandler((data, clickData) -> {
                        switch (data) {
                            case "previous_page" -> localPageSelected = Mth.clamp(localPageSelected - 1, 0, totalPage - 1);
                            case "next_page" -> localPageSelected = Mth.clamp(localPageSelected + 1, 0, totalPage - 1);
                        }
                        updateTradeContainer(tradeContainer, localPageSelected, unlockShop);
                        widget.detectAndSendChanges();
                    }));

            mainGroup.addWidget(shopGroup);
            group.addWidget(mainGroup);
            group.setBackground(GuiTextures.BACKGROUND_INVERSE);

            return group;
        }

        private void updateTradeContainer(WidgetGroup container, int pageIndex, boolean unlockShop) {
            container.clearAllWidgets();
            container.addWidget(machine.tradeGroup_16(groupIndex, shopIndex, pageIndex, unlockShop));
        }
    }

    /////////////////////////////////////
    // ********* 自动输出实现 ********* //
    /////////////////////////////////////

    @Persisted
    @DescSynced
    private Direction outputFacingItems = Direction.DOWN;
    @Persisted
    @DescSynced
    private Direction outputFacingFluids = Direction.DOWN;
    @Persisted
    @DescSynced
    private boolean autoOutputItems = false;
    @Persisted
    @DescSynced
    private boolean autoOutputFluids = false;
    @Nullable
    private TickableSubscription autoOutputSubs;
    @Nullable
    private ISubscription outputItemChangeSub;
    @Nullable
    private ISubscription outputFluidChangeSub;
    private boolean allowInputFromOutputSideItems;
    private boolean allowInputFromOutputSideFluids;

    @Override
    public boolean hasAutoOutputItem() {
        return outputItem.getSlots() > 0;
    }

    @Override
    public boolean isAutoOutputItems() {
        return autoOutputItems;
    }

    @Override
    public void setAutoOutputItems(boolean autoOutputItems) {
        if (hasAutoOutputItem()) {
            this.autoOutputItems = autoOutputItems;
            updateAutoOutputSubscription();
        }
    }

    @Nullable
    @Override
    public Direction getOutputFacingItems() {
        return hasAutoOutputItem() ? outputFacingItems : null;
    }

    @Override
    public void setOutputFacingItems(@Nullable Direction direction) {
        if (hasAutoOutputItem() && direction != null) {
            this.outputFacingItems = direction;
            clearDirectionCache();
            updateAutoOutputSubscription();
        }
    }

    @Override
    public boolean hasAutoOutputFluid() {
        return outputFluid.getTanks() > 0;
    }

    @Override
    public boolean isAutoOutputFluids() {
        return autoOutputFluids;
    }

    @Override
    public void setAutoOutputFluids(boolean autoOutputFluids) {
        if (hasAutoOutputFluid()) {
            this.autoOutputFluids = autoOutputFluids;
            updateAutoOutputSubscription();
        }
    }

    @Nullable
    @Override
    public Direction getOutputFacingFluids() {
        return hasAutoOutputFluid() ? outputFacingFluids : null;
    }

    @Override
    public void setOutputFacingFluids(@Nullable Direction direction) {
        if (hasAutoOutputFluid() && direction != null) {
            this.outputFacingFluids = direction;
            clearDirectionCache();
            updateAutoOutputSubscription();
        }
    }

    private void updateAutoOutputSubscription() {
        if (getLevel() == null || isRemote()) return;

        boolean canOutputItems = autoOutputItems && !outputItem.isEmpty() && getOutputFacingItems() != null && blockEntityDirectionCache.hasAdjacentItemHandler(
                getLevel(),
                getPos().relative(getOutputFacingItems()),
                getOutputFacingItems().getOpposite());

        boolean canOutputFluids = autoOutputFluids && !outputFluid.isEmpty() && getOutputFacingFluids() != null && blockEntityDirectionCache.hasAdjacentFluidHandler(
                getLevel(),
                getPos().relative(getOutputFacingFluids()),
                getOutputFacingFluids().getOpposite());

        if (canOutputItems || canOutputFluids) {
            if (autoOutputSubs == null || !autoOutputSubs.stillSubscribed) {
                autoOutputSubs = subscribeServerTick(this::autoOutput, 20);
            }
        } else {
            if (autoOutputSubs != null) {
                autoOutputSubs.unsubscribe();
                autoOutputSubs = null;
            }
        }
    }

    private void autoOutput() {
        if (autoOutputItems && getOutputFacingItems() != null) {
            outputItem.exportToNearby(getOutputFacingItems());
        }
        if (autoOutputFluids && getOutputFacingFluids() != null) {
            outputFluid.exportToNearby(getOutputFacingFluids());
        }
        updateAutoOutputSubscription();
    }

    @Override
    public void setAllowInputFromOutputSideItems(boolean allowInputFromOutputSideItems) {
        this.clearDirectionCache();
        this.allowInputFromOutputSideItems = allowInputFromOutputSideItems;
    }

    @Override
    public void setAllowInputFromOutputSideFluids(boolean allowInputFromOutputSideFluids) {
        this.clearDirectionCache();
        this.allowInputFromOutputSideFluids = allowInputFromOutputSideFluids;
    }

    @Override
    public boolean isAllowInputFromOutputSideItems() {
        return allowInputFromOutputSideItems;
    }

    @Override
    public boolean isAllowInputFromOutputSideFluids() {
        return allowInputFromOutputSideFluids;
    }

    @Override
    public void onNeighborChanged(@NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        updateAutoOutputSubscription();
    }

    /////////////////////////////////////
    // ********** 是否运行中 ********** //
    /////////////////////////////////////

    @Persisted
    private boolean working = false;

    @Override
    public boolean isWorkingEnabled() {
        return working;
    }

    @Override
    public void setWorkingEnabled(boolean var1) {
        this.working = var1;
    }
}
