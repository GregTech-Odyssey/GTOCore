package com.gtocore.common.machine.noenergy;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.api.gui.InteractiveImageWidget;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.translation.GTOMachineTooltips;
import com.gtocore.data.transaction.TransactionEntry;

import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.utils.TeamUtil;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nullable;

import static com.gtocore.common.item.GrayMembershipCardItem.*;

public class TradingStationMachine extends MetaMachine implements IFancyUIMachine, IAutoOutputBoth, IMachineLife {

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

    @Persisted
    private final NotifiableItemStackHandler inputItem;
    @Persisted
    private final NotifiableItemStackHandler outputItem;
    @Persisted
    private final NotifiableFluidTank inputFluid;
    @Persisted
    private final NotifiableFluidTank outputFluid;

    @Persisted
    private final CustomItemStackHandler cardHandler;

    @Persisted
    private UUID uuid;
    @Persisted
    List<UUID> sharedUUIDs = new ArrayList<>();
    @Persisted
    private UUID teamUUID;
    @Persisted
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
        sideTabs.attachSubTab(transactionRecords());
        sideTabs.attachSubTab(transactionRecords());
        sideTabs.attachSubTab(transactionRecords());
        sideTabs.attachSubTab(transactionRecords());
        sideTabs.attachSubTab(transactionRecords());
        sideTabs.attachSubTab(transactionRecords());
        sideTabs.attachSubTab(transactionRecords());
        // 方向配置页
        sideTabs.attachSubTab(CombinedDirectionalFancyConfigurator.of(this, this));
    }

    private WidgetGroup transaction(int x, int y, TransactionEntry entry) {
        WidgetGroup transaction = new WidgetGroup(x, y, 40, 50);
        transaction.setBackground(new ResourceTexture("gtocore:textures/gui/transaction_box.png"));

        Level level = getLevel();
        ServerLevel serverlevel;
        if (level instanceof ServerLevel server) serverlevel = server;
        else serverlevel = null;

        BlockPos pos = getPos();

        transaction.addWidget(new InteractiveImageWidget(2, 7, 36, 36, new ResourceTexture("textures/item/bread.png"))
                .textSupplier(texts -> {
                    texts.addAll(entry.getDescription());
                    texts.add(Component.literal("面包图片"));
                    texts.add(Component.literal("点击获取面包")); // 仅提示，不可点击
                }).clickHandler((data, clickData) -> {
                    if (level instanceof ServerLevel serverLevel) {
                        if (!WalletUtils.containsTagValueInWallet(uuid, serverLevel, "unlock", entry.getUnlockCondition())) return;

                        UUID teamUUID = TeamUtil.getTeamUUID(uuid); // originalUUID可以是玩家/机器的UUID
                        // 2. 获取团队对应的电量容器
                        WirelessEnergyContainer container = WirelessEnergyContainer.getOrCreateContainer(teamUUID);
                        // 3. 获取当前总电量
                        BigInteger currentStorage = container.getStorage();
                        // 4. 定义需要减少的电量（示例值）
                        long energyToRemove = 1000; // 假设要减少1000单位电量
                        BigInteger energyToRemoveBig = BigInteger.valueOf(energyToRemove);
                        // 5. 检查总电量是否足够（当前电量 >= 需要减少的电量）
                        if (currentStorage.compareTo(energyToRemoveBig) >= 0) {
                            // 电量足够，执行减少操作
                            long actualRemoved = container.removeEnergy(energyToRemove, null); // 第二个参数为关联的机器实例，可传null
                            // 处理减少后的逻辑（如日志输出）
                        } else {
                            // 电量不足，执行其他逻辑（如提示“电量不足”）
                            // 例如：输出日志或向玩家发送提示
                        }

                        TransactionEntry.TransactionContext context = new TransactionEntry.TransactionContext(uuid, serverLevel, pos);
                        if (entry.canExecute(context)) {
                            entry.execute(context);
                        }
                    }
                }));

        transaction.addWidget(new ImageWidget(10, 10, 100, 100, IGuiTexture.EMPTY));

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
    // ********* 输入输出方法 ********* //
    /////////////////////////////////////

    /**
     * 计算库存对输入物品列表的最大支持倍数（即库存最多能满足多少倍的输入列表消耗）
     * 
     * @param handler       物品库存处理器
     * @param requiredItems 需求物品列表（每个ItemStack的count为单倍需求数量）
     * @return 最大支持倍数（若列表为空或无需求则返回Integer.MAX_VALUE，否则取最小限制倍数）
     */
    private int checkMaxMultiplier(IItemHandlerModifiable handler, List<ItemStack> requiredItems) {
        if (requiredItems.isEmpty()) {
            return Integer.MAX_VALUE; // 无需求则支持无限倍
        }

        int maxMultiplier = Integer.MAX_VALUE;

        for (ItemStack required : requiredItems) {
            if (required.isEmpty()) {
                continue; // 跳过空物品
            }

            int requiredPerMulti = required.getCount();
            if (requiredPerMulti <= 0) {
                continue; // 单倍需求为0，视为不限制该物品
            }

            // 统计库存中该物品的总数量
            int totalInStock = 0;
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack slotStack = handler.getStackInSlot(i);
                if (ItemStack.isSameItemSameTags(slotStack, required)) {
                    totalInStock += slotStack.getCount();
                }
            }

            // 计算该物品支持的最大倍数（总库存 / 单倍需求）
            int itemMaxMulti = totalInStock / requiredPerMulti;
            maxMultiplier = Math.min(maxMultiplier, itemMaxMulti);

            if (maxMultiplier == 0) {
                break; // 已无法支持1倍，提前退出
            }
        }

        return Math.min(maxMultiplier, 128); // 限制最大倍数为128（与现有逻辑保持一致）
    }

    /**
     * 从库存中扣除 x倍的输入物品列表（每个物品扣除数量 = 列表中物品数量 * x）
     * 
     * @param handler    物品库存处理器
     * @param items      待扣除的物品列表（倍数）
     * @param multiplier 倍数x（必须>0，否则不执行）
     */
    private void deductMultipliedItems(IItemHandlerModifiable handler, List<ItemStack> items, int multiplier) {
        if (multiplier <= 0 || items.isEmpty()) {
            return; // 倍数无效或无物品，不执行
        }

        for (ItemStack item : items) {
            if (item.isEmpty()) {
                continue; // 跳过空物品
            }

            int perItemCount = item.getCount();
            if (perItemCount <= 0) {
                continue; // 单倍数量为0，无需扣除
            }

            // 计算总扣除数量 = 单倍数量 * 倍数
            int totalToDeduct = perItemCount * multiplier;
            if (totalToDeduct <= 0) {
                continue;
            }

            // 从库存中扣除（复用现有逻辑的思路）
            int remaining = totalToDeduct;
            for (int i = 0; i < handler.getSlots() && remaining > 0; i++) {
                ItemStack slotStack = handler.getStackInSlot(i);
                if (ItemStack.isSameItemSameTags(slotStack, item)) {
                    int take = Math.min(slotStack.getCount(), remaining);
                    slotStack.shrink(take);
                    if (slotStack.isEmpty()) {
                        handler.setStackInSlot(i, ItemStack.EMPTY);
                    }
                    remaining -= take;
                }
            }
        }
    }

    /**
     * 向库存中添加x倍的输入物品列表，库存不足时将剩余物品掷出掉落
     * 
     * @param handler    物品库存处理器
     * @param items      待添加的物品列表（单倍数量）
     * @param multiplier 倍数x（必须>0，否则不执行）
     */
    private void addMultipliedItems(IItemHandlerModifiable handler, List<ItemStack> items, int multiplier) {
        // 边界校验：倍数无效、无物品、世界或坐标为空时直接返回
        if (multiplier <= 0 || items.isEmpty()) {
            return;
        }

        // 遍历物品列表，处理每个物品的倍数添加
        for (ItemStack item : items) {
            // 跳过空物品或单倍数量为0的物品
            if (item.isEmpty() || item.getCount() <= 0) {
                continue;
            }

            // 计算总添加数量 = 单倍数量 × 倍数
            int totalToAdd = item.getCount() * multiplier;
            if (totalToAdd <= 0) {
                continue;
            }

            // 复制物品堆并设置总数量（避免修改原列表物品）
            ItemStack toAdd = item.copy();
            toAdd.setCount(totalToAdd);
            ItemStack remaining = toAdd.copy(); // 剩余待添加的数量
            int maxStackSize = toAdd.getMaxStackSize(); // 物品最大堆叠数

            // 第一步：填充现有相同物品的堆叠（非空槽位）
            for (int i = 0; i < handler.getSlots() && !remaining.isEmpty(); i++) {
                ItemStack existing = handler.getStackInSlot(i);
                // 只处理相同物品且未达最大堆叠的槽位
                if (!existing.isEmpty() && ItemStack.isSameItemSameTags(existing, remaining) && existing.getCount() < maxStackSize) {
                    int addAmount = Math.min(remaining.getCount(), maxStackSize - existing.getCount());
                    existing.grow(addAmount); // 增加现有堆叠数量
                    remaining.shrink(addAmount); // 减少剩余待添加数量
                }
            }

            // 第二步：填充空槽位（处理剩余未添加的物品）
            while (!remaining.isEmpty()) {
                boolean foundSlot = false; // 标记是否找到空槽位
                for (int i = 0; i < handler.getSlots() && !remaining.isEmpty(); i++) {
                    if (handler.getStackInSlot(i).isEmpty()) {
                        // 计算当前槽位可放置的数量（不超过最大堆叠）
                        int putAmount = Math.min(remaining.getCount(), maxStackSize);
                        ItemStack toPut = remaining.copy();
                        toPut.setCount(putAmount);
                        handler.setStackInSlot(i, toPut); // 放置物品
                        remaining.shrink(putAmount); // 减少剩余数量
                        foundSlot = true;
                    }
                }

                // 若未找到空槽位（库存已满），将剩余物品掷出
                if (!foundSlot) {
                    // 拆分剩余物品为多个最大堆叠（避免单个实体超过最大堆叠）
                    BlockPos pos = getPos();
                    if (getLevel() instanceof ServerLevel server) {
                        while (!remaining.isEmpty()) {
                            int dropCount = Math.min(remaining.getCount(), maxStackSize);
                            ItemStack dropStack = remaining.copy();
                            dropStack.setCount(dropCount);

                            // 生成物品实体：在pos上方1格位置，无拾取延迟
                            ItemEntity itemEntity = new ItemEntity(
                                    server,
                                    pos.getX() + 0.5, // 中心x坐标
                                    pos.getY() + 1,   // 上方1格y坐标
                                    pos.getZ() + 0.5, // 中心z坐标
                                    dropStack);
                            itemEntity.setNoPickUpDelay(); // 立即可拾取
                            server.addFreshEntity(itemEntity); // 添加到世界
                            remaining.shrink(dropCount); // 减少剩余数量
                        }
                        break; // 处理完剩余物品后退出循环
                    }
                }
            }
        }
    }

    /**
     * 计算库存中流体总量能支持多少倍的输入流体列表消耗（单倍为列表中各流体的量）
     * 
     * @param tank           流体库存槽
     * @param requiredFluids 需求流体列表（每个FluidStack的amount为单倍需求）
     * @return 最大支持倍数（若列表为空返回Integer.MAX_VALUE，若某流体不足则返回0）
     */
    private int checkMaxConsumeMultiplier(NotifiableFluidTank tank, List<FluidStack> requiredFluids) {
        if (requiredFluids.isEmpty()) {
            return Integer.MAX_VALUE; // 无需求则支持无限倍
        }

        int maxMultiplier = Integer.MAX_VALUE;

        for (FluidStack required : requiredFluids) {
            if (required.isEmpty() || required.getAmount() <= 0) {
                continue; // 跳过空流体或单倍需求为0的项
            }

            // 统计库存中该流体的总存量（遍历所有槽，累加相同流体的量）
            int totalInTank = 0;
            for (int slot = 0; slot < tank.getTanks(); slot++) {
                FluidStack stored = tank.getFluidInTank(slot);
                if (stored.isFluidEqual(required)) { // 流体类型必须完全匹配
                    totalInTank += stored.getAmount();
                }
            }

            // 计算该流体支持的最大倍数（总存量 ÷ 单倍需求）
            int fluidMaxMulti = totalInTank / required.getAmount();
            maxMultiplier = Math.min(maxMultiplier, fluidMaxMulti);

            if (maxMultiplier == 0) {
                break; // 已无法支持1倍，提前退出
            }
        }

        return maxMultiplier;
    }

    /**
     * 计算库存剩余容量能容纳多少倍的输入流体列表（单倍为列表中各流体的量）
     * 
     * @param tank        流体库存槽
     * @param inputFluids 待添加的流体列表（每个FluidStack的amount为单倍量）
     * @return 最大可容纳倍数（若列表为空返回Integer.MAX_VALUE，若某流体无容量则返回0）
     */
    private int checkMaxCapacityMultiplier(NotifiableFluidTank tank, List<FluidStack> inputFluids) {
        if (inputFluids.isEmpty()) {
            return Integer.MAX_VALUE; // 无输入则支持无限倍
        }

        int maxMultiplier = Integer.MAX_VALUE;

        for (FluidStack input : inputFluids) {
            if (input.isEmpty() || input.getAmount() <= 0) {
                continue; // 跳过空流体或单倍量为0的项
            }

            // 计算库存中该流体的剩余总容量（总容量 - 现有量）
            int totalRemainingCapacity = 0;
            for (int slot = 0; slot < tank.getTanks(); slot++) {
                // 仅统计可容纳该流体的槽（需通过isFluidValid校验）
                if (!tank.isFluidValid(slot, input)) {
                    continue;
                }
                FluidStack stored = tank.getFluidInTank(slot);
                // 空槽或同类型流体槽可容纳
                if (stored.isEmpty() || stored.isFluidEqual(input)) {
                    totalRemainingCapacity += (tank.getTankCapacity(slot) - stored.getAmount());
                }
            }

            // 计算该流体可容纳的最大倍数（剩余容量 ÷ 单倍量）
            int fluidMaxMulti = totalRemainingCapacity / input.getAmount();
            maxMultiplier = Math.min(maxMultiplier, fluidMaxMulti);

            if (maxMultiplier == 0) {
                break; // 已无法容纳1倍，提前退出
            }
        }

        return maxMultiplier;
    }

    /**
     * 从流体库存中扣除x倍的输入流体列表（每个流体扣除量 = 单倍量 × x）
     * 
     * @param tank       流体库存槽
     * @param fluids     待扣除的流体列表（单倍量）
     * @param multiplier 倍数x（必须>0，否则不执行）
     * @return 实际扣除的倍数（若库存不足，可能小于x；完全成功则返回x）
     */
    private int deductMultipliedFluids(NotifiableFluidTank tank, List<FluidStack> fluids, int multiplier) {
        if (multiplier <= 0 || fluids.isEmpty()) {
            return 0; // 无效参数，不执行
        }

        // 按实际倍数扣除每个流体
        for (FluidStack fluid : fluids) {
            if (fluid.isEmpty() || fluid.getAmount() <= 0) {
                continue;
            }

            int totalToDeduct = fluid.getAmount() * multiplier;
            if (totalToDeduct <= 0) {
                continue;
            }

            // 复制流体栈并设置扣除总量，调用drain方法执行扣除
            FluidStack toDeduct = fluid.copy();
            toDeduct.setAmount(totalToDeduct);
            tank.drain(toDeduct, IFluidHandler.FluidAction.EXECUTE);
        }

        return multiplier;
    }

    /**
     * 向流体库存中添加x倍的输入流体列表（每个流体添加量 = 单倍量 × x）
     * 
     * @param tank       流体库存槽
     * @param fluids     待添加的流体列表（单倍量）
     * @param multiplier 倍数x（必须>0，否则不执行）
     * @return 实际添加的倍数（若容量不足，可能小于x；完全成功则返回x）
     */
    private int addMultipliedFluids(NotifiableFluidTank tank, List<FluidStack> fluids, int multiplier) {
        if (multiplier <= 0 || fluids.isEmpty()) {
            return 0; // 无效参数，不执行
        }

        // 按实际倍数添加每个流体
        for (FluidStack fluid : fluids) {
            if (fluid.isEmpty() || fluid.getAmount() <= 0) {
                continue;
            }

            int totalToAdd = fluid.getAmount() * multiplier;
            if (totalToAdd <= 0) {
                continue;
            }

            // 复制流体栈并设置添加总量，调用fill方法执行添加
            FluidStack toAdd = fluid.copy();
            toAdd.setAmount(totalToAdd);
            tank.fill(toAdd, IFluidHandler.FluidAction.EXECUTE);
        }

        return multiplier;
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
}
