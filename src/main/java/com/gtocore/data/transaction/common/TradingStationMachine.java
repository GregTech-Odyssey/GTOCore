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
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nullable;

import static com.gtocore.common.item.GrayMembershipCardItem.*;

public class TradingStationMachine extends MetaMachine implements IFancyUIMachine, IAutoOutputBoth, IMachineLife, IControllable {

    /////////////////////////////////////
    // ********* 生命周期管理 ********* //
    /////////////////////////////////////

    public TradingStationMachine(MetaMachineBlockEntity holder) {
        super(holder);
        cardHandler = new CustomItemStackHandler();
        cardHandler.setFilter(i -> i.getItem().equals(GTOItems.GRAY_MEMBERSHIP_CARD.asItem()));
        cardHandler.setOnContentsChanged(() -> initializationInformation(cardHandler.getStackInSlot(0)));

        inputItem = new NotifiableItemStackHandler(this, 256, IO.IN, IO.BOTH);
        outputItem = new NotifiableItemStackHandler(this, 256, IO.OUT, IO.BOTH);
        inputFluid = new NotifiableFluidTank(this, 4, 1000 * 64, IO.IN, IO.BOTH);
        outputFluid = new NotifiableFluidTank(this, 4, 1000 * 64, IO.OUT, IO.BOTH);
    }

    // 生命周期管理：注册/取消监听器
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
    public void onMachinePlaced(@org.jetbrains.annotations.Nullable LivingEntity player, ItemStack stack) {}

    @Override
    public void onMachineRemoved() {
        clearInventory(cardHandler);
        clearInventory(inputItem.storage);
        clearInventory(outputItem.storage);
    }

    /////////////////////////////////////
    // *********** 数据存储 *********** //
    /////////////////////////////////////

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

    /////////////////////////////////////
    // ********* 核心交易逻辑 ********* //
    /////////////////////////////////////

    private void ConfirmTransactionContext() {}

    /////////////////////////////////////
    // *********** 交易管理 *********** //
    /////////////////////////////////////

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
        if (teamUUID.equals(TeamUtil.getTeamUUID(playerUUID))) return true;
        if (Objects.requireNonNull(getLevel()).isClientSide) {
            player.displayClientMessage(trans(1).withStyle(ChatFormatting.RED), true);
        }
        shopSelected = -1;
        storeGroupSwitchingInitialization();
        return false;
    }

    @DescSynced
    private boolean isCollapse = true;

    private final int width = 336;
    private final int height = 144;

    @Override
    public Widget createUIWidget() {
        // 交易页面布局
        int w = 4 + 22 + 50 + 3 + 50 + 11 + 4;
        int h = 4 + 40 + 1 + 40 + 1 + 40 + 1 + 40 + 2 + 40 + 1 + 40 + 1 + 40 + 1 + 40 + 4;
        // 主页布局
        w = 256 + 80;
        var group = new WidgetGroup(0, 0, width + 8, height + 8);

        WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                .setBackground(GuiTextures.DISPLAY);

        // 底边展开
        mainGroup.addWidget(new ComponentPanelWidget(4, 134, textList -> {
            textList.add(Component.empty()
                    .append(ComponentPanelWidget.withHoverTextTranslate(ComponentPanelWidget.withButton(trans(isCollapse ? 5 : 6), "isCollapse"), trans(7))));
            if (!isCollapse) GTOMachineTooltips.INSTANCE.getPanGalaxyGrayTechTradingStationIntroduction().apply(textList);
        }).clickHandler((a, b) -> { if (a.equals("isCollapse")) isCollapse = !isCollapse; }).setMaxWidthLimit(width - 8));

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
                if (!sharedUUIDs.isEmpty() || !teamUUID.equals(uuid)) {
                    MutableComponent shared = trans(4);
                    if (!sharedUUIDs.isEmpty()) for (UUID shareuuid : sharedUUIDs) shared.append(WalletPlayers.get(shareuuid)).append(", ");
                    if (!teamUUID.equals(uuid)) shared.append(TeamUtil.GetName(level, uuid));
                    textList.add(ComponentPanelWidget.withHoverTextTranslate(trans(3, WalletPlayers.get(uuid)), shared));
                } else {
                    textList.add(trans(3, WalletPlayers.get(uuid)));
                }
            }
        }).setMaxWidthLimit(256 - 34));

        mainGroup.addWidget(new InteractiveImageWidget(237, 10, 9, 9, GTOGuiTextures.REFRESH)
                .textSupplier(texts -> texts.add(trans(8)))
                .clickHandler((data, clickData) -> {
                    initializationInformation(cardHandler.getStackInSlot(0));
                }));

        mainGroup.addWidget(new LabelWidget(10, 50, Component.literal(groupSize + "/" + shopSize + "/" + transactionSize)));
        mainGroup.addWidget(new LabelWidget(10, 60, Component.literal(groupSelected + "/" + shopSelected + "/" + pageSelected)));

        // 左右分区
        mainGroup.addWidget(new ImageWidget(255, 2, 2, 140, GuiTextures.SLOT));

        // 右侧

        group.addWidget(mainGroup);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    public IGuiTexture getTabIcon() {
        return GuiTextures.GREGTECH_LOGO;
    }

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

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }
        };
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);

        sideTabs.attachSubTab(transactionRecords());

        List<IFancyUIProvider> shopGroupTabs = shopGroup();

        shopGroupTabs.forEach(sideTabs::attachSubTab);

        if (groupSelected == 0) {
            IFancyUIProvider directionalTab = CombinedDirectionalFancyConfigurator.of(this, this);
            sideTabs.attachSubTab(directionalTab);
        }

        sideTabs.setOnTabSwitch((oldTab, newTab) -> {
            if (shopGroupTabs.contains(newTab)) {
                shopSelected = shopGroupTabs.indexOf(newTab);
            } else {
                shopSelected = -1;
            }
            storeGroupSwitchingInitialization();
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
    private int groupSelected = 0;
    private int shopSelected = -1;
    private int pageSelected = 0;

    private void storeGroupSwitchingInitialization() {
        groupSize = manager.getGroupCount();
        shopSize = (groupSelected >= 0 && groupSelected < groupSize) ? manager.getShopCount(groupSelected) : 0;
        transactionSize = (shopSelected >= 0 && shopSelected < shopSize) ? manager.getTransactionCount(groupSelected, shopSelected) : 0;
    }

    private List<IFancyUIProvider> shopGroup() {
        List<IFancyUIProvider> shopGroup = new ArrayList<>();

        for (int shop = 0; shop < shopSize; shop++) {
            TradingManager.TradingShop tradingShop = manager.getShopByIndices(groupSelected, shop);

            ServerLevel serverLevel = getLevel() instanceof ServerLevel ? (ServerLevel) getLevel() : null;
            boolean unlock = WalletUtils.containsTagValueInWallet(uuid, serverLevel, TransactionLang.UNLOCK_SHOP, tradingShop.getUnlockCondition());
            // if (!unlock) continue;

            shopGroup.add(new IFancyUIProvider() {

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
                    var group = new WidgetGroup(0, 0, width + 8, height + 8);

                    WidgetGroup mainGroup = new WidgetGroup(4, 4, width, height);
                    mainGroup.setBackground(GuiTextures.DISPLAY);

                    WidgetGroup shopGroup = new WidgetGroup(0, 0, width, height);
                    shopGroup.setLayout(Layout.VERTICAL_CENTER);
                    shopGroup.setLayoutPadding(3);

                    shopGroup.addWidget(new LabelWidget(0, 0, Component.translatable(tradingShop.getName())));

                    shopGroup.addWidget(new LabelWidget(0, 0, Component.literal("一个一个商店")));

                    shopGroup.addWidget(transactionGroup(26, groupSelected, shopSelected, pageSelected));

                    int totalPage = transactionSize / 16 + (transactionSize % 16 == 0 ? 0 : 1);
                    shopGroup.addWidget(new ComponentPanelWidget(0, 0, textList -> textList.add(Component.empty()
                            .append(ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_page"))
                            .append(Component.literal("<" + (pageSelected + 1) + "/" + totalPage + ">"))
                            .append(ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_page")))).clickHandler((data, clickData) -> {
                                switch (data) {
                                    case "previous_page" -> pageSelected = Mth.clamp(pageSelected - 1, 0, totalPage - 1);
                                    case "next_page" -> pageSelected = Mth.clamp(pageSelected + 1, 0, totalPage - 1);
                                }
                            }));

                    mainGroup.addWidget(shopGroup);
                    group.addWidget(mainGroup);
                    group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                    return group;
                }
            });
        }
        return shopGroup;
    }

    private WidgetGroup transactionGroup(int y, int groupIndex, int shopIndex, int pageIndex) {
        WidgetGroup transactionGroup = new WidgetGroup(0, y, 328, 103);

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 8; col++) {
                int X = col * 41 + (col > 3 ? 1 : 0);
                int Y = row * 53;
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

        Level level = getLevel();
        ServerLevel serverLevel = getLevel() instanceof ServerLevel ? (ServerLevel) getLevel() : null;

        boolean unlock = WalletUtils.containsTagValueInWallet(uuid, serverLevel, TransactionLang.UNLOCK_TRANSACTION, entry.unlockCondition());
        boolean canExecute = entry.canExecute(this);

        transaction.addWidget(new InteractiveImageWidget(2, 7, 36, 36, entry.texture())
                .textSupplier(texts -> {
                    if (!unlock) texts.add(Component.translatable("gtocore.transaction_group.unlock"));
                    if (!canExecute) texts.add(Component.translatable("gtocore.transaction_group.unsatisfied"));
                    texts.addAll(entry.getDescription());
                })
                .clickHandler((data, clickData) -> {
                    // if (!unlock) return;
                    int multiplier = clickData.isCtrlClick ? (clickData.isShiftClick ? 100 : 10) : 1;
                    entry.execute(this, multiplier);
                }));

        transaction.addWidget(new ComponentPanelWidget(2, 6, textList -> {
            if (!unlock) {
                Component lock = Component.literal("\uD83D\uDD12\uD83D\uDD12\uD83D\uDD12\uD83D\uDD12\uD83D\uDD12\uD83D\uDD12").withStyle(ChatFormatting.AQUA);
                textList.add(lock);
                textList.add(lock);
                textList.add(lock);
                textList.add(lock);
            }
        }).setMaxWidthLimit(60));

        return transaction;
    }

    private IGuiTexture getIGuiTexture(String unlockCondition) {
        if (getLevel() instanceof ServerLevel serverLevel) {
            boolean unlock = WalletUtils.containsTagValueInWallet(uuid, serverLevel, TransactionLang.UNLOCK_TRANSACTION, unlockCondition);
            if (!unlock) return GuiTextures.BUTTON_LOCK;
        }
        return IGuiTexture.EMPTY;
    }

    private ImageWidget emptyTransaction(int x, int y) {
        return new ImageWidget(x, y, 40, 50, GTOGuiTextures.BOXED_BACKGROUND);
    }

    /////////////////////////////////////
    // ********* 辅助类与方法 ********* //
    /////////////////////////////////////

    private static final String TEXT_HEADER = "gtocore.trading_station.textList.";

    private static @NotNull MutableComponent trans(int id, Object... args) {
        if (args.length == 1 && args[0] instanceof Object[]) args = (Object[]) args[0];
        return Component.translatable(TEXT_HEADER + id, args);
    }

    /**
     * 生成带UUID的玩家头物品
     */
    private static ItemStack createPlayerHead(UUID uuid) {
        ItemStack head = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag tag = head.getOrCreateTag();
        CompoundTag skullOwner = new CompoundTag();
        skullOwner.putUUID("Id", uuid);
        tag.put("SkullOwner", skullOwner);
        return head;
    }

    /**
     * 初始化玩家信息
     */
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
    private Direction outputFacingItems = Direction.DOWN; // 物品输出方向
    @Persisted
    @DescSynced
    private Direction outputFacingFluids = Direction.DOWN; // 流体输出方向
    @Persisted
    @DescSynced
    private boolean autoOutputItems = false; // 物品自动输出开关
    @Persisted
    @DescSynced
    private boolean autoOutputFluids = false; // 流体自动输出开关
    @Nullable
    private TickableSubscription autoOutputSubs; // 自动输出定时任务订阅
    @Nullable
    private ISubscription outputItemChangeSub; // 物品输出槽变化监听器
    @Nullable
    private ISubscription outputFluidChangeSub; // 流体输出槽变化监听器

    // 物品自动输出接口实现
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

    // 流体自动输出接口实现
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

    // 自动输出订阅管理：控制定时任务的启动与停止
    private void updateAutoOutputSubscription() {
        if (getLevel() == null || isRemote()) return;

        // 检查物品输出条件：开关启用 + 输出槽非空 + 方向有效 + 存在接收方
        boolean canOutputItems = autoOutputItems && !outputItem.isEmpty() && getOutputFacingItems() != null && blockEntityDirectionCache.hasAdjacentItemHandler(
                getLevel(),
                getPos().relative(getOutputFacingItems()),
                getOutputFacingItems().getOpposite());

        // 检查流体输出条件：开关启用 + 输出槽非空 + 方向有效 + 存在接收方
        boolean canOutputFluids = autoOutputFluids && !outputFluid.isEmpty() && getOutputFacingFluids() != null && blockEntityDirectionCache.hasAdjacentFluidHandler(
                getLevel(),
                getPos().relative(getOutputFacingFluids()),
                getOutputFacingFluids().getOpposite());

        // 满足任一条件则启动定时任务（每20tick执行一次，即1秒/次）
        if (canOutputItems || canOutputFluids) {
            if (autoOutputSubs == null || autoOutputSubs.stillSubscribed) {
                autoOutputSubs = subscribeServerTick(this::autoOutput, 20);
            }
        } else {
            // 不满足条件则取消订阅
            if (autoOutputSubs != null) {
                autoOutputSubs.unsubscribe();
                autoOutputSubs = null;
            }
        }
    }

    // 自动输出执行逻辑：将物品/流体传输到目标方向
    private void autoOutput() {
        // 输出物品到目标方向
        if (autoOutputItems && getOutputFacingItems() != null) {
            outputItem.exportToNearby(getOutputFacingItems());
        }
        // 输出流体到目标方向
        if (autoOutputFluids && getOutputFacingFluids() != null) {
            outputFluid.exportToNearby(getOutputFacingFluids());
        }
        // 输出后重新检查条件（避免空槽时继续运行）
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

    // 邻居方块变化时更新输出状态
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
