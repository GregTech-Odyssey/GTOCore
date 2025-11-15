package com.gtocore.data.transaction.common;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.api.gui.InteractiveImageWidget;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.translation.GTOMachineTooltips;
import com.gtocore.data.transaction.data.TransactionLang;
import com.gtocore.data.transaction.recipe.entry.TradingManager;
import com.gtocore.data.transaction.recipe.entry.TransactionEntry;

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

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.utils.TeamUtil;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.gtocore.common.item.GrayMembershipCardItem.getSharedUuids;
import static com.gtocore.common.item.GrayMembershipCardItem.getSingleUuid;
import static com.gtocore.common.item.GrayMembershipCardItem.isUuidPresent;

public class TradingStationMachine extends MetaMachine implements IFancyUIMachine, IAutoOutputBoth, IMachineLife, IControllable {

    /////////////////////////////////////
    // ********* 生命周期管理 ********* //
    /////////////////////////////////////

    public TradingStationMachine(MetaMachineBlockEntity holder) {
        super(holder);
        cardHandler = new CustomItemStackHandler();
        cardHandler.setFilter(i -> i.getItem().equals(GTOItems.GRAY_MEMBERSHIP_CARD.asItem()));
        cardHandler.setOnContentsChanged(() -> initializationInformation(cardHandler.getStackInSlot(0)));

        inputItem = new NotifiableItemStackHandler(this, itemStorageSize, IO.IN, IO.BOTH);
        outputItem = new NotifiableItemStackHandler(this, itemStorageSize, IO.OUT, IO.BOTH);
        inputFluid = new NotifiableFluidTank(this, 4, 1000 * 2000000, IO.IN, IO.BOTH);
        outputFluid = new NotifiableFluidTank(this, 4, 1000 * 2000000, IO.OUT, IO.BOTH);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        initializationInformation(cardHandler.getStackInSlot(0));
        storeGroupSwitchingInitialization();
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
    public void onMachinePlaced(@Nullable LivingEntity player, ItemStack stack) {}

    @Override
    public void onMachineRemoved() {
        clearInventory(cardHandler);
        clearInventory(inputItem.storage);
        clearInventory(outputItem.storage);
    }

    /////////////////////////////////////
    // *********** 数据存储 *********** //
    /////////////////////////////////////

    // 输入输出存储
    @Getter
    @Persisted
    private final NotifiableItemStackHandler inputItem;
    @Getter
    @Persisted
    private final NotifiableItemStackHandler outputItem;
    @Getter
    @Persisted
    private final NotifiableFluidTank inputFluid;
    @Getter
    @Persisted
    private final NotifiableFluidTank outputFluid;

    @Persisted
    private final CustomItemStackHandler cardHandler;

    // 玩家信息
    @Getter
    @Persisted
    private UUID uuid;
    @Getter
    @Persisted
    List<UUID> sharedUUIDs = new ArrayList<>();
    @Getter
    @Persisted
    private UUID teamUUID;

    private ItemStack OwnerHead = ItemStack.EMPTY;
    @Persisted
    private int Openness = 0;

    // 库存大小
    @Persisted
    private int itemStorageSize = 50;
    @Persisted
    private int fluidStorageSize = 4;

    /////////////////////////////////////
    // ************ UI实现 ************ //
    /////////////////////////////////////

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack card = cardHandler.getStackInSlot(0);
        initializationInformation(card);
        if (uuid == null) return true;
        UUID playerUUID = player.getUUID();
        if (isUuidPresent(card, playerUUID)) return true;
        if (teamUUID != null && teamUUID.equals(TeamUtil.getTeamUUID(playerUUID))) return true;
        if (Objects.requireNonNull(getLevel()).isClientSide) {
            player.displayClientMessage(trans(1).withStyle(ChatFormatting.RED), true);
        }
        shopSelected = -1;
        storeGroupSwitchingInitialization();
        return false;
    }

    @DescSynced
    private boolean collapseDescription = true;

    private final int width = 336;
    private final int height = 144;

    @Override
    public Widget createUIWidget() {
        int w = 256 + 80;
        var group = new WidgetGroup(0, 0, width + 8, height + 8);

        WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                .setBackground(GuiTextures.DISPLAY);

        // 底边展开
        mainGroup.addWidget(new ComponentPanelWidget(4, 134, textList -> {
            textList.add(ComponentPanelWidget.withHoverTextTranslate(ComponentPanelWidget.withButton(trans(collapseDescription ? 5 : 6), "isCollapse"), trans(7)));
            if (!collapseDescription)
                GTOMachineTooltips.INSTANCE.getPanGalaxyGrayTechTradingStationIntroduction().apply(textList);
        }).clickHandler((a, b) -> {
            if (a.equals("isCollapse")) {
                collapseDescription = !collapseDescription;
                ModularUI modularUI = mainGroup.getGui();
                if (modularUI != null && modularUI.getModularUIGui() != null) {
                    modularUI.getModularUIGui().init();
                }
            }
        }).setMaxWidthLimit(width - 8));

        Level level = getLevel();
        ServerLevel serverLevel = getLevel() instanceof ServerLevel ? (ServerLevel) getLevel() : null;

        // 左侧
        mainGroup.addWidget(new SlotWidget(cardHandler, 0, 10, 10)
                .setBackgroundTexture(GuiTextures.SLOT));

        Object2ObjectMap<UUID, String> WalletPlayers = WalletUtils.getAllWalletPlayers(serverLevel);

        mainGroup.addWidget(new ComponentPanelWidget(32, 14, textList -> {
            if (uuid == null) {
                textList.add(trans(2));
            } else {
                if (!sharedUUIDs.isEmpty() || (teamUUID != null && !teamUUID.equals(uuid))) {
                    MutableComponent shared = trans(4);
                    if (!sharedUUIDs.isEmpty()) {
                        StringJoiner joiner = new StringJoiner(", ");
                        for (UUID shareuuid : sharedUUIDs) {
                            joiner.add(WalletPlayers.getOrDefault(shareuuid, "Unknown"));
                        }
                        shared = Component.literal(joiner.toString());
                    }
                    if (teamUUID != null && !teamUUID.equals(uuid)) {
                        shared.append(TeamUtil.GetName(level, uuid));
                    }
                    textList.add(ComponentPanelWidget.withHoverTextTranslate(trans(3, WalletPlayers.getOrDefault(uuid, "Unknown")), shared));
                } else {
                    textList.add(trans(3, WalletPlayers.getOrDefault(uuid, "Unknown")));
                }
            }
        }).setMaxWidthLimit(256 - 34));

        mainGroup.addWidget(new InteractiveImageWidget(237, 10, 9, 9, GTOGuiTextures.REFRESH)
                .textSupplier(texts -> texts.add(trans(8)))
                .clickHandler((data, clickData) -> {
                    initializationInformation(cardHandler.getStackInSlot(0));
                    ModularUI modularUI = mainGroup.getGui();
                    if (modularUI != null && modularUI.getModularUIGui() != null) {
                        modularUI.getModularUIGui().init();
                    }
                }));

        storeGroupSwitchingInitialization();
        mainGroup.addWidget(new LabelWidget(10, 50, Component.literal(groupSize + "/" + shopSize + "/" + transactionSize)));
        mainGroup.addWidget(new LabelWidget(10, 60, Component.literal(groupSelected + "/" + shopSelected)));

        // 左右分区
        mainGroup.addWidget(new ImageWidget(253, 2, 2, 140, GuiTextures.SLOT));

        // 右侧
        mainGroup.addWidget(ShopGroupSwitchWidget());

        group.addWidget(mainGroup);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public IGuiTexture getTabIcon() {
        return GuiTextures.GREGTECH_LOGO;
    }

    private WidgetGroup ShopGroupSwitchWidget() {
        WidgetGroup mainGroup = new WidgetGroup(256, 4, 80, height - 8);
        mainGroup.setLayout(Layout.VERTICAL_CENTER);
        mainGroup.setLayoutPadding(4);

        storeGroupSwitchingInitialization();
        TradingManager.TradingShopGroup SwitchedShopGroup = manager.getShopGroup(groupSelected);
        if (SwitchedShopGroup == null) SwitchedShopGroup = manager.getShopGroup(0);
        if (SwitchedShopGroup != null) {
            mainGroup.addWidget(new LabelWidget(0, 0, Component.translatable(SwitchedShopGroup.getName())));
            mainGroup.addWidget(new ImageWidget(0, 10, 64, 78, SwitchedShopGroup.getTexture1()));
        }

        WidgetGroup SwitchWidget = new WidgetGroup(0, 80, 79, 39);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 8; x++) {
                int index = y * 8 + x;
                TradingManager.TradingShopGroup shopGroup = manager.getShopGroup(index);
                if (shopGroup != null) {
                    SwitchWidget.addWidget(new InteractiveImageWidget(x * 10, y * 10, 9, 9, shopGroup.getTexture2())
                            .textSupplier(texts -> texts.add(Component.translatable(shopGroup.getName())))
                            .clickHandler((data, clickData) -> {
                                groupSelected = index;
                                shopSelected = -1;
                                markAsDirty();
                                storeGroupSwitchingInitialization();

                                ModularUI modularUI = mainGroup.getGui();
                                if (modularUI != null && modularUI.getModularUIGui() != null) {
                                    TabsWidget tabsWidget = (TabsWidget) modularUI.getFirstWidgetById("fancy_side_tabs");
                                    if (tabsWidget != null) {
                                        tabsWidget.clearSubTabs();
                                        tabsWidget.setMainTab(this);
                                        tabsWidget.selectTab(tabsWidget.getMainTab());

                                        if (groupSelected == 0) {
                                            tabsWidget.attachSubTab(transactionRecords());
                                        }
                                        List<IFancyUIProvider> shopGroupTabs = shopGroup();
                                        shopGroupTabs.forEach(tabsWidget::attachSubTab);
                                        if (groupSelected == 0) {
                                            IFancyUIProvider directionalTab = CombinedDirectionalFancyConfigurator.of(this, this);
                                            tabsWidget.attachSubTab(directionalTab);
                                        }

                                        tabsWidget.setOnTabSwitch((oldTab, newTab) -> {
                                            shopSelected = (newTab instanceof ShopTabProvider) ? ((ShopTabProvider) newTab).getShopIndex() : -1;
                                            storeGroupSwitchingInitialization();
                                            modularUI.getModularUIGui().init();
                                        });
                                    }
                                    modularUI.getModularUIGui().init();
                                    modularUI.initWidgets();
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

    private final int Item_slots_in_a_row = 8;
    private final int Fluid_slots_in_a_row = 1;

    private @NotNull IFancyUIProvider transactionRecords() {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return GTOItems.PALM_SIZED_BANK.asStack().getDisplayName();
            }

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                int itemHigh = itemStorageSize / Item_slots_in_a_row + (itemStorageSize % Item_slots_in_a_row == 0 ? 0 : 1);
                WidgetGroup Item_slot = new WidgetGroup(2, 4, 290, itemHigh * 18 + 10);
                Item_slot.addWidget(new ComponentPanelWidget(0, 0, textList -> textList.add(trans(9))));
                for (int y = 0; y < itemHigh; y++) {
                    for (int x = 0; x < Item_slots_in_a_row; x++) {
                        int slotIndex = y * Item_slots_in_a_row + x;
                        if (itemStorageSize > slotIndex) {
                            Item_slot.addWidget(new SlotWidget(inputItem, slotIndex, x * 18, 10 + y * 18, true, true)
                                    .setBackground(GuiTextures.SLOT));
                            Item_slot.addWidget(new SlotWidget(outputItem, slotIndex, x * 18 + Item_slots_in_a_row * 18 + 2, 10 + y * 18, true, true)
                                    .setBackground(GuiTextures.SLOT));
                        } else break;
                    }
                }
                mainGroup.addWidget(Item_slot);

                int fluidHigh = fluidStorageSize / Fluid_slots_in_a_row;
                WidgetGroup Fluid_slot = new WidgetGroup(296, 4, 38, fluidHigh * 18 + 10);
                Fluid_slot.addWidget(new ComponentPanelWidget(0, 0, textList -> textList.add(trans(10))));
                for (int y = 0; y < fluidHigh; y++) {
                    for (int x = 0; x < Fluid_slots_in_a_row; x++) {
                        int slotIndex = y * Fluid_slots_in_a_row + x;
                        if (fluidStorageSize > slotIndex) {
                            Fluid_slot.addWidget(new TankWidget(inputFluid, slotIndex, 0, 10 + y * 18, true, true)
                                    .setBackground(GuiTextures.SLOT));
                            Fluid_slot.addWidget(new TankWidget(outputFluid, slotIndex, Fluid_slots_in_a_row * 18 + 2, 10 + y * 18, true, true)
                                    .setBackground(GuiTextures.SLOT));
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

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setId("fancy_side_tabs");
        sideTabs.setMainTab(this);
        sideTabs.selectTab(sideTabs.getMainTab());

        if (groupSelected == 0) {
            sideTabs.attachSubTab(transactionRecords());
        }

        List<IFancyUIProvider> shopGroupTabs = shopGroup();
        shopGroupTabs.forEach(sideTabs::attachSubTab);

        if (groupSelected == 0) {
            IFancyUIProvider directionalTab = CombinedDirectionalFancyConfigurator.of(this, this);
            sideTabs.attachSubTab(directionalTab);
        }

        sideTabs.setOnTabSwitch((oldTab, newTab) -> {
            shopSelected = (newTab instanceof ShopTabProvider) ? ((ShopTabProvider) newTab).getShopIndex() : -1;
            storeGroupSwitchingInitialization();
            ModularUI modularUI = sideTabs.getGui();
            if (modularUI != null && modularUI.getModularUIGui() != null) {
                modularUI.getModularUIGui().init();
            }
        });
    }

    /////////////////////////////////////
    // ********* UI单元构建 ********* //
    /////////////////////////////////////

    TradingManager manager = TradingManager.getInstance();

    private int groupSize;
    private int shopSize;
    private int transactionSize;

    @Persisted
    @DescSynced
    private int groupSelected = 0;
    private int shopSelected = -1;

    private void storeGroupSwitchingInitialization() {
        groupSize = manager.getGroupCount();
        shopSize = (groupSelected >= 0 && groupSelected < groupSize) ? manager.getShopCount(groupSelected) : 0;
        transactionSize = (shopSelected >= 0 && shopSelected < shopSize) ? manager.getTransactionCount(groupSelected, shopSelected) : 0;
    }

    private List<IFancyUIProvider> shopGroup() {
        List<IFancyUIProvider> shopGroupTabs = new ArrayList<>();
        storeGroupSwitchingInitialization();

        for (int shop = 0; shop < shopSize; shop++) {
            TradingManager.TradingShop tradingShop = manager.getShopByIndices(groupSelected, shop);

            ServerLevel serverLevel = getLevel() instanceof ServerLevel ? (ServerLevel) getLevel() : null;
            boolean unlock = WalletUtils.containsTagValueInWallet(uuid, serverLevel, TransactionLang.UNLOCK_SHOP, tradingShop.getUnlockCondition());
            // if (!unlock) continue;

            shopGroupTabs.add(new ShopTabProvider(this, groupSelected, shop, tradingShop));
        }
        return shopGroupTabs;
    }

    private WidgetGroup transactionGroup(int y, int groupIndex, int shopIndex, int pageIndex) {
        WidgetGroup transactionGroup = new WidgetGroup(0, y, 327, 102);

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 8; col++) {
                int X = col * 41;
                int Y = row * 51;
                int entryIndex = pageIndex * 16 + row * 8 + col;
                boolean isIndexValid = manager.isTransactionIndexValid(groupIndex, shopIndex, entryIndex);
                if (isIndexValid) {
                    transactionGroup.addWidget(transaction(X, Y, manager.getTransactionEntryByIndices(groupIndex, shopIndex, entryIndex)));
                } else {
                    transactionGroup.addWidget(emptyTransaction(X, Y));
                }
            }
        }

        return transactionGroup;
    }

    private WidgetGroup transaction(int x, int y, TransactionEntry entry) {
        WidgetGroup transaction = new WidgetGroup(x, y, 40, 50);
        transaction.setBackground(GTOGuiTextures.BOXED_BACKGROUND);

        ServerLevel serverLevel = getLevel() instanceof ServerLevel ? (ServerLevel) getLevel() : null;

        boolean unlock = WalletUtils.containsTagValueInWallet(uuid, serverLevel, TransactionLang.UNLOCK_TRANSACTION, entry.unlockCondition());
        boolean canExecute = entry.canExecute(this);

        transaction.addWidget(new InteractiveImageWidget(2, 7, 36, 36, entry.texture())
                .textSupplier(texts -> {
                    if (!unlock) texts.add(Component.translatable("gtocore.transaction_group.unlock", entry.unlockCondition()).withStyle(ChatFormatting.DARK_RED));
                    if (!canExecute) texts.add(Component.translatable("gtocore.transaction_group.unsatisfied").withStyle(ChatFormatting.DARK_RED));
                    texts.addAll(entry.getDescription());
                })
                .clickHandler((data, clickData) -> {
                    if (!unlock) return;
                    int multiplier = clickData.isCtrlClick ? (clickData.isShiftClick ? 100 : 10) : 1;
                    entry.execute(this, multiplier);
                }));

        return transaction;
    }

    private ImageWidget emptyTransaction(int x, int y) {
        return new ImageWidget(x, y, 40, 50, GTOGuiTextures.BOXED_BACKGROUND);
    }

    /////////////////////////////////////
    // ********* 内部类：ShopTabProvider ********* //
    /////////////////////////////////////

    /**
     * 独立的商店标签页提供者，每个标签页维护自己的页码状态
     */
    private static class ShopTabProvider implements IFancyUIProvider {

        private final TradingStationMachine machine;
        private final int groupIndex;
        @Getter
        private final int shopIndex;
        private final TradingManager.TradingShop tradingShop;
        private int localPageSelected = 0;

        public ShopTabProvider(TradingStationMachine machine, int groupIndex, int shopIndex, TradingManager.TradingShop tradingShop) {
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
        public Widget createMainPage(FancyMachineUIWidget widget) {
            var group = new WidgetGroup(0, 0, machine.width + 8, machine.height + 8);
            WidgetGroup mainGroup = new WidgetGroup(4, 4, machine.width, machine.height);
            mainGroup.setBackground(GuiTextures.DISPLAY);

            int transactionCount = machine.manager.getTransactionCount(groupIndex, shopIndex);
            int totalPage = transactionCount / 16 + (transactionCount % 16 == 0 ? 0 : 1);

            WidgetGroup shopGroup = new WidgetGroup(0, 0, machine.width, machine.height);
            shopGroup.setLayout(Layout.VERTICAL_CENTER);
            shopGroup.setLayoutPadding(3);

            shopGroup.addWidget(new LabelWidget(0, 0, Component.translatable(tradingShop.getName())));
            shopGroup.addWidget(new LabelWidget(0, 12, Component.translatable("一个一个商店")));

            WidgetGroup transactionContainer = new WidgetGroup(0, 36, 327, 102);
            updateTransactionContainer(transactionContainer, localPageSelected);
            shopGroup.addWidget(transactionContainer);

            shopGroup.addWidget(new ComponentPanelWidget(0, 140, textList -> textList.add(Component.empty()
                    .append(ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_page"))
                    .append(Component.literal("<" + (localPageSelected + 1) + "/" + totalPage + ">"))
                    .append(ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_page")))).clickHandler((data, clickData) -> {
                        switch (data) {
                            case "previous_page" -> localPageSelected = Mth.clamp(localPageSelected - 1, 0, totalPage - 1);
                            case "next_page" -> localPageSelected = Mth.clamp(localPageSelected + 1, 0, totalPage - 1);
                        }
                        updateTransactionContainer(transactionContainer, localPageSelected);
                        if (widget.getGui() != null) {
                            widget.getGui().getModularUIGui().init();
                        }
                    }));

            mainGroup.addWidget(shopGroup);
            group.addWidget(mainGroup);
            group.setBackground(GuiTextures.BACKGROUND_INVERSE);

            return group;
        }

        /**
         * 局部刷新交易条目，避免整体UI重绘
         */
        private void updateTransactionContainer(WidgetGroup container, int pageIndex) {
            container.clearAllWidgets();
            container.addWidget(machine.transactionGroup(0, groupIndex, shopIndex, pageIndex));
        }
    }

    /////////////////////////////////////
    // ********* 辅助类与方法 ********* //
    /////////////////////////////////////

    private static final String TEXT_HEADER = "gtocore.trading_station.textList.";

    private static @NotNull MutableComponent trans(int id, Object... args) {
        if (args.length == 1 && args[0] instanceof Object[]) args = (Object[]) args[0];
        return Component.translatable(TEXT_HEADER + id, args);
    }

    private static ItemStack createPlayerHead(UUID uuid) {
        ItemStack head = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag tag = head.getOrCreateTag();
        CompoundTag skullOwner = new CompoundTag();
        skullOwner.putUUID("Id", uuid);
        tag.put("SkullOwner", skullOwner);
        return head;
    }

    private void initializationInformation(ItemStack card) {
        if (card.getItem().equals(GTOItems.GRAY_MEMBERSHIP_CARD.asItem())) {
            this.uuid = getSingleUuid(card);
            this.sharedUUIDs = getSharedUuids(card);
            if (uuid != null) {
                this.teamUUID = TeamUtil.getTeamUUID(uuid);
                this.OwnerHead = createPlayerHead(uuid);
            }
        } else {
            this.uuid = null;
            this.sharedUUIDs = new ArrayList<>();
            this.teamUUID = null;
            this.OwnerHead = ItemStack.EMPTY;
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
            if (autoOutputSubs == null || autoOutputSubs.stillSubscribed) {
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
    public void setAllowInputFromOutputSideItems(final boolean allowInputFromOutputSideItems) {}

    @Override
    public void setAllowInputFromOutputSideFluids(final boolean allowInputFromOutputSideFluids) {}

    @Override
    public boolean isAllowInputFromOutputSideItems() {
        return false;
    }

    @Override
    public boolean isAllowInputFromOutputSideFluids() {
        return false;
    }

    @Override
    public void onNeighborChanged(@NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        updateAutoOutputSubscription();
    }

    @Persisted
    private boolean working = false;

    @Override
    public boolean isWorkingEnabled() {
        return working;
    }

    @Override
    public void setWorkingEnabled(boolean var1) {
        this.working = !working;
    }
}
