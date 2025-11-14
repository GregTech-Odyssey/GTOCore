package com.gtocore.data.transaction.common;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.api.gui.InteractiveImageWidget;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.translation.GTOMachineTooltips;
import com.gtocore.data.transaction.data.TransactionLang;
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
import static com.gtocore.data.transaction.recipe.TransactionRegistration.createJungleBeaconTrade;

public class TradingStationMachine extends MetaMachine implements IFancyUIMachine, IAutoOutputBoth, IMachineLife, IControllable {

    /////////////////////////////////////
    // ********* 生命周期管理 ********* //
    /////////////////////////////////////

    public TradingStationMachine(MetaMachineBlockEntity holder) {
        super(holder);
        cardHandler = new CustomItemStackHandler();
        cardHandler.setFilter(i -> i.getItem().equals(GTOItems.GRAY_MEMBERSHIP_CARD.asItem()));
        cardHandler.setOnContentsChanged(() -> InitializationInformation(cardHandler.getStackInSlot(0)));

        inputItem = new NotifiableItemStackHandler(this, 256, IO.IN, IO.BOTH);
        outputItem = new NotifiableItemStackHandler(this, 256, IO.OUT, IO.BOTH);
        inputFluid = new NotifiableFluidTank(this, 4, 1000 * 64, IO.IN, IO.BOTH);
        outputFluid = new NotifiableFluidTank(this, 4, 1000 * 64, IO.OUT, IO.BOTH);
    }

    // 生命周期管理：注册/取消监听器
    @Override
    public void onLoad() {
        super.onLoad();
        InitializationInformation(cardHandler.getStackInSlot(0));
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
        InitializationInformation(card);
        if (uuid == null) return true;
        UUID playerUUID = player.getUUID();
        if (isUuidPresent(card, playerUUID)) return true;
        if (teamUUID.equals(TeamUtil.getTeamUUID(playerUUID))) return true;
        if (Objects.requireNonNull(getLevel()).isClientSide) {
            player.displayClientMessage(trans(1).withStyle(ChatFormatting.RED), true);
        }
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

        Level level = getLevel();
        ServerLevel serverlevel;
        if (level instanceof ServerLevel server) serverlevel = server;
        else serverlevel = null;

        mainGroup.addWidget(new SlotWidget(cardHandler, 0, 10, 10)
                .setBackgroundTexture(GuiTextures.SLOT));

        Object2ObjectMap<UUID, String> WalletPlayers = WalletUtils.getAllWalletPlayers(serverlevel);

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
                .clickHandler((data, clickData) -> InitializationInformation(cardHandler.getStackInSlot(0))));

        mainGroup.addWidget(new ImageWidget(255, 2, 2, 140, GuiTextures.SLOT));

        mainGroup.addWidget(new ComponentPanelWidget(4, 134, textList -> {
            textList.add(Component.empty()
                    .append(ComponentPanelWidget.withHoverTextTranslate(ComponentPanelWidget.withButton(trans(isCollapse ? 5 : 6), "isCollapse"), trans(7))));
            if (!isCollapse) GTOMachineTooltips.INSTANCE.getPanGalaxyGrayTechTradingStationIntroduction().apply(textList);
        }).clickHandler((a, b) -> { if (a.equals("isCollapse")) isCollapse = !isCollapse; }).setMaxWidthLimit(width - 8));

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

                mainGroup.addWidget(transaction(20, 20, createJungleBeaconTrade()));

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
        sideTabs.attachSubTab(transactionRecords());
        sideTabs.attachSubTab(transactionRecords());
        // 方向配置页
        sideTabs.attachSubTab(CombinedDirectionalFancyConfigurator.of(this, this));
    }

    private WidgetGroup transaction(int x, int y, TransactionEntry entry) {
        WidgetGroup transaction = new WidgetGroup(x, y, 40, 50);
        transaction.setBackground(GTOGuiTextures.BOXED_BACKGROUND);

        ServerLevel serverlevel;
        if (getLevel() instanceof ServerLevel server) serverlevel = server;
        else serverlevel = null;

        boolean unlock = WalletUtils.containsTagValueInWallet(uuid, serverlevel, TransactionLang.UNLOCK_TRANSACTION, entry.unlockCondition());
        boolean canExecute = entry.canExecute(this);

        transaction.addWidget(new InteractiveImageWidget(2, 7, 36, 36, entry.texture())
                .textSupplier(texts -> {
                    if (!unlock) texts.add(Component.translatable("gtocore.transaction_group.unlock"));
                    if (!canExecute) texts.add(Component.translatable("gtocore.transaction_group.unsatisfied"));
                    texts.addAll(entry.getDescription());
                })
                .clickHandler((data, clickData) -> {
                    if (!unlock) return;
                    int multiplier = clickData.isCtrlClick ? (clickData.isShiftClick ? 100 : 10) : 1;
                    entry.execute(this, multiplier);
                }));

        transaction.addWidget(new ImageWidget(1, 6, 38, 38, unlock ? IGuiTexture.EMPTY : GuiTextures.BUTTON_LOCK));

        return transaction;
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
    private void InitializationInformation(ItemStack card) {
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
        if (getLevel() == null || isRemote()) return; // 仅在服务端执行

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
