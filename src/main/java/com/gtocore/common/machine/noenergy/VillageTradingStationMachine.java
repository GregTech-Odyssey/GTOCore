package com.gtocore.common.machine.noenergy;

import com.gtocore.common.data.translation.GTOMachineTooltips;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

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
import com.gregtechceu.gtceu.api.machine.feature.IAutoOutputItem;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gtocore.common.data.GTOItems.*;

public class VillageTradingStationMachine extends MetaMachine implements IAutoOutputItem, IFancyUIMachine {

    // 定时任务订阅
    private TickableSubscription tickSubs;
    @Nullable
    private TickableSubscription autoOutputSubs;
    @Nullable
    private ISubscription exportItemSubs;

    // 输入输出物品存储
    @Persisted
    private final NotifiableItemStackHandler input;
    @Persisted
    private final NotifiableItemStackHandler output;

    // 村民存储与配置
    @Persisted
    private VillageHolder villagers;
    @Persisted
    private boolean[] isLocked = new boolean[9];
    @Persisted
    private int[] selected = new int[9];
    @Persisted
    private boolean[] startUp = new boolean[9];
    @Persisted
    private final ObjectArrayList<VillagerTradeRecord> villagersDataset = new ObjectArrayList<>();
    private final ItemStackHandler RecipesHandler = new ItemStackHandler(3 * 9);

    // 补货与交易参数
    private int replenishmentInterval = 2400;
    private int tradingMultiple = 1;

    // 最大交易次数 32*
    private static final Item[] FIELD_GENERATOR = {
            FIELD_GENERATOR_LV.asItem(), FIELD_GENERATOR_MV.asItem(), FIELD_GENERATOR_HV.asItem(), FIELD_GENERATOR_EV.asItem(),
            FIELD_GENERATOR_IV.asItem(), FIELD_GENERATOR_LuV.asItem(), FIELD_GENERATOR_ZPM.asItem(), FIELD_GENERATOR_UV.asItem() };

    // 最大补货次数 5*
    private static final Item[] SENSOR = {
            SENSOR_LV.asItem(), SENSOR_MV.asItem(), SENSOR_HV.asItem(), SENSOR_EV.asItem(),
            SENSOR_IV.asItem(), SENSOR_LuV.asItem(), SENSOR_ZPM.asItem(), SENSOR_UV.asItem() };

    // 补货时间间隔 -200*
    private static final Item[] EMITTER = {
            EMITTER_LV.asItem(), EMITTER_MV.asItem(), EMITTER_HV.asItem(),
            EMITTER_EV.asItem(), EMITTER_IV.asItem(), EMITTER_LuV.asItem(),
            EMITTER_ZPM.asItem(), EMITTER_UV.asItem(), EMITTER_UHV.asItem() };

    // 多倍交易 4*
    private static final Item[] INTEGRATED_CONTROL_CORE = {
            INTEGRATED_CONTROL_CORE_UV.asItem(), INTEGRATED_CONTROL_CORE_UHV.asItem(), INTEGRATED_CONTROL_CORE_UEV.asItem(), INTEGRATED_CONTROL_CORE_UIV.asItem() };

    public VillageTradingStationMachine(MetaMachineBlockEntity holder) {
        super(holder);
        input = new NotifiableItemStackHandler(this, 256, IO.IN, IO.IN, CustomItemStackHandler::new);
        output = new NotifiableItemStackHandler(this, 256, IO.OUT, IO.OUT, CustomItemStackHandler::new);
        villagers = new VillageHolder(this);
        outputFacingItems = hasFrontFacing() ? getFrontFacing().getOpposite() : Direction.UP;
    }

    /////////////////////////////////////
    // ********* 生命周期管理 ********* //
    /////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        initializeDataset();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(this::tickUpdate);
            exportItemSubs = output.addChangedListener(this::updateAutoOutputSubscription);
        }
        for (int i = 0; i < 9; i++) {
            incomingVillagersDataset(i);
            selectedRecipes(i);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) tickSubs.unsubscribe();
        if (exportItemSubs != null) exportItemSubs.unsubscribe();
        if (autoOutputSubs != null) autoOutputSubs.unsubscribe();
    }

    private void initializeDataset() {
        while (villagersDataset.size() < 9) villagersDataset.add(null);
        if (villagersDataset.size() > 9) villagersDataset.size(9);
    }

    /////////////////////////////////////
    // ********* 核心交易逻辑 ********* //
    /////////////////////////////////////

    /**
     * 定时更新
     */
    private void tickUpdate() {
        if (getOffsetTimer() % 100 != 0) return;

        int daytime = getOffsetTimer() % 24000;
        // 每天0点重置补货次数
        if (daytime == 0) dailyRestockingAttempts();
        // 定时补货
        if (daytime % replenishmentInterval == 0) villagersRestock();
        // 执行交易
        if (daytime % 200 == 0) {
            for (int slot = 0; slot < 9; slot++) {
                executeTrades(slot);
            }
        }
    }

    /**
     * 执行交易逻辑（与原逻辑一致）
     */
    private void executeTrades(int slot) {
        if (!isLocked(slot) || !isStartUp(slot) || villagersDataset.get(slot) == null) return;

        List<VillagerRecipe> recipes = villagersDataset.get(slot).recipes;
        if (recipes.isEmpty() || selected[slot] >= recipes.size()) return;

        VillagerRecipe trade = recipes.get(selected[slot]);
        int remainingUses = trade.maxUses - trade.uses;
        if (remainingUses <= 0) return;

        int maxPossibleTrades = getMaxPossibleTrades(input, trade.buy, trade.buyB);
        if (maxPossibleTrades <= 0) return;

        int actualTrades = Math.min((maxPossibleTrades / tradingMultiple), remainingUses) * tradingMultiple;
        if (actualTrades <= 0) return;

        deductItems(input, trade.buy, actualTrades);
        if (!trade.buyB.isEmpty()) {
            deductItems(input, trade.buyB, actualTrades);
        }

        ItemStack outputStack = trade.sell.copy();
        outputStack.setCount(trade.sell.getCount() * actualTrades);
        addItems(output, outputStack);

        trade.uses += actualTrades / tradingMultiple;
    }

    // 计算两个输入物品堆能支持的最大交易次数
    private int getMaxPossibleTrades(NotifiableItemStackHandler input, ItemStack buy, ItemStack buyB) {
        int totalBuy = 0;
        for (int i = 0; i < input.getSlots(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (ItemStack.isSameItemSameTags(stack, buy)) {
                totalBuy += stack.getCount();
            }
        }
        int maxByBuy = buy.getCount() > 0 ? totalBuy / buy.getCount() : Integer.MAX_VALUE;

        int maxByBuyB = Integer.MAX_VALUE;
        if (!buyB.isEmpty() && buyB.getCount() > 0) {
            int totalBuyB = 0;
            for (int i = 0; i < input.getSlots(); i++) {
                ItemStack stack = input.getStackInSlot(i);
                if (ItemStack.isSameItemSameTags(stack, buyB)) {
                    totalBuyB += stack.getCount();
                }
            }
            maxByBuyB = totalBuyB / buyB.getCount();
        }

        return Math.min(Math.min(maxByBuy, maxByBuyB), 128);
    }

    /**
     * 从物品处理器中扣除指定数量的物品
     */
    private void deductItems(NotifiableItemStackHandler handler, ItemStack target, int count) {
        if (target.isEmpty() || count <= 0) return;

        int remaining = target.getCount() * count;
        for (int i = 0; i < handler.getSlots() && remaining > 0; i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (ItemStack.isSameItemSameTags(stack, target)) {
                int take = Math.min(stack.getCount(), remaining);
                stack.shrink(take);
                if (stack.isEmpty()) {
                    handler.setStackInSlot(i, ItemStack.EMPTY);
                }
                remaining -= take;
            }
        }
    }

    /**
     * 向物品处理器中添加物品
     */
    private void addItems(NotifiableItemStackHandler handler, ItemStack stack) {
        if (stack.isEmpty()) return;

        ItemStack remaining = stack.copy();
        int maxStackSize = remaining.getMaxStackSize();

        for (int i = 0; i < handler.getSlots() && !remaining.isEmpty(); i++) {
            ItemStack existing = handler.getStackInSlot(i);
            if (existing.isEmpty()) continue;
            if (ItemStack.isSameItemSameTags(existing, remaining) && existing.getCount() < maxStackSize) {
                int addAmount = Math.min(remaining.getCount(), maxStackSize - existing.getCount());
                existing.grow(addAmount);
                remaining.shrink(addAmount);
            }
        }

        while (!remaining.isEmpty()) {
            boolean foundSlot = false;
            for (int i = 0; i < handler.getSlots() && !remaining.isEmpty(); i++) {
                if (handler.getStackInSlot(i).isEmpty()) {
                    int putAmount = Math.min(remaining.getCount(), maxStackSize);
                    ItemStack toPut = remaining.copy();
                    toPut.setCount(putAmount);
                    handler.setStackInSlot(i, toPut);
                    remaining.shrink(putAmount);
                    foundSlot = true;
                }
            }
            if (!foundSlot) break;
        }
    }

    /////////////////////////////////////
    // ********* 村民与配方管理 ********* //
    /////////////////////////////////////

    public boolean isLocked(int slot) {
        return isLocked[slot];
    }

    public void setLocked(int slot) {
        isLocked[slot] = !isLocked[slot];
        selected[slot] = 0;
        if (isLocked[slot]) {
            incomingVillagersDataset(slot);
        } else {
            villagersDataset.set(slot, null);
            setStartUp(slot);
        }
        selectedRecipes(slot);
    }

    private void selectedNext(int slot) {
        if (!isLocked(slot) || isStartUp(slot)) return;
        if (villagersDataset.get(slot) != null && !villagersDataset.get(slot).recipes.isEmpty()) {
            selected[slot] = (selected[slot] + 1) % villagersDataset.get(slot).recipes.size();
            selectedRecipes(slot);
        } else {
            isLocked[slot] = false;
        }
    }

    private boolean isStartUp(int slot) {
        return startUp[slot];
    }

    private void setStartUp(int slot) {
        if (isLocked(slot)) {
            startUp[slot] = !startUp[slot];
            if (villagersDataset.get(slot) != null && villagersDataset.get(slot).recipes.isEmpty()) {
                startUp[slot] = false;
            }
            if (startUp[slot]) executeTrades(slot);
        } else {
            startUp[slot] = false;
        }
    }

    private void incomingVillagersDataset(int slot) {
        ItemStack villager = villagers.getStackInSlot(slot);
        if (!villager.isEmpty() && isLocked[slot]) {
            villagersDataset.set(slot, parseTradeData(villager.getOrCreateTag()));
        } else {
            villagersDataset.set(slot, null);
            isLocked[slot] = false;
        }
    }

    private void selectedRecipes(int slot) {
        if (villagersDataset.get(slot) != null && isLocked(slot)) {
            List<VillagerRecipe> recipes = villagersDataset.get(slot).recipes;
            if (!recipes.isEmpty() && selected[slot] < recipes.size()) {
                VillagerRecipe recipe = recipes.get(selected[slot]);
                RecipesHandler.setStackInSlot(slot * 3, recipe.buy.copy());
                RecipesHandler.setStackInSlot(slot * 3 + 1, recipe.buyB.copy());
                RecipesHandler.setStackInSlot(slot * 3 + 2, recipe.sell.copy());
            } else {
                clearRecipeSlots(slot);
            }
        } else {
            clearRecipeSlots(slot);
        }
    }

    private void clearRecipeSlots(int slot) {
        RecipesHandler.setStackInSlot(slot * 3, ItemStack.EMPTY);
        RecipesHandler.setStackInSlot(slot * 3 + 1, ItemStack.EMPTY);
        RecipesHandler.setStackInSlot(slot * 3 + 2, ItemStack.EMPTY);
    }

    /////////////////////////////////////
    // *********** UI实现 *********** //
    /////////////////////////////////////

    @Override
    public Widget createUIWidget() {
        int width = 336;
        int height = 144;
        var group = new WidgetGroup(0, 0, width + 8, height + 8);

        WidgetGroup groupTitle = new DraggableScrollableWidgetGroup(4, 4, width, height)
                .setBackground(GuiTextures.DISPLAY);

        groupTitle.addWidget(new ComponentPanelWidget(4, 5,
                textList -> GTOMachineTooltips.INSTANCE.getVillageTradingStationIntroduction().apply(textList))
                .setMaxWidthLimit(width - 8));

        group.addWidget(groupTitle);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);

        // 交易控制页
        sideTabs.attachSubTab(new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return new ItemStackTexture(Objects.requireNonNull(getDefinition().asItem()));
            }

            @Override
            public Component getTitle() {
                return Component.translatable(getDefinition().getDescriptionId());
            }

            final int width = 256;
            final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);
                mainGroup.addWidget(new ComponentPanelWidget(4, 4, this::addDisplayTextTitle));
                mainGroup.addWidget(getLockButton());

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);
                return group;
            }

            private void addDisplayTextTitle(List<Component> textList) {
                int lockedCount = 0;
                for (int i = 0; i < 9; i++) if (isLocked(i)) lockedCount++;
                textList.add(Component.literal(String.valueOf(lockedCount)));
            }

            private @NotNull WidgetGroup getLockButton() {
                int xSize = 18;
                int ySize = 128;
                int buttonSpacing = 0;
                int groupXSize = (xSize + buttonSpacing) * 9 - buttonSpacing;
                int startX = (width + 8 - groupXSize) / 2;
                int startY = 4;
                WidgetGroup group = new WidgetGroup(startX, startY, groupXSize, ySize);

                for (int i = 0; i < 9; i++) {
                    final int slot = i;
                    int buttonX = i * (xSize + buttonSpacing);
                    WidgetGroup villagerGroup = new WidgetGroup(buttonX, 0, xSize, ySize);
                    villagerGroup.setLayout(Layout.VERTICAL_CENTER);

                    // 锁定按钮
                    villagerGroup.addWidget(new ComponentPanelWidget(0, 2,
                            (textList) -> textList.add(ComponentPanelWidget.withButton(
                                    isLocked(slot) ? Component.literal("\uD83D\uDD12") : Component.literal("\uD83D\uDD13"),
                                    String.valueOf(slot))))
                            .clickHandler((a, b) -> setLocked(Integer.parseInt(a))));

                    // 村民槽位
                    villagerGroup.addWidget(new SlotWidget(villagers, i, 0, 18 - 6, true, true)
                            .setBackground(GuiTextures.SLOT));

                    // 配方输入1、输入2、输出槽位
                    for (int j = 0; j < 3; j++) {
                        SlotWidget itemWidget = new SlotWidget(RecipesHandler, 3 * i + j, 0, 18 * (j + 2) - 6)
                                .setCanPutItems(false).setCanTakeItems(false)
                                .setBackgroundTexture(new ResourceTexture(GTOCore.id("textures/gui/villager_recipe_slot_" + j + ".png")));
                        villagerGroup.addWidget(itemWidget);
                    }

                    // 切换配方按钮
                    villagerGroup.addWidget(new ComponentPanelWidget(0, 2 + 18 * 5 - 6,
                            (textList) -> textList.add(ComponentPanelWidget.withButton(
                                    Component.literal("\uD83D\uDD01"), String.valueOf(slot))))
                            .clickHandler((a, b) -> selectedNext(Integer.parseInt(a))));

                    // 启动交易按钮
                    villagerGroup.addWidget(new ComponentPanelWidget(0, 2 + 18 * 5 + 6,
                            (textList) -> textList.add(ComponentPanelWidget.withButton(
                                    isStartUp(slot) ? Component.literal("\uD83D\uDD12") : Component.literal("\uD83D\uDD13"),
                                    String.valueOf(slot))))
                            .clickHandler((a, b) -> setStartUp(Integer.parseInt(a))));

                    // 交易次数显示
                    villagerGroup.addWidget(new ComponentPanelWidget(0, 2 + 18 * 5 + 18,
                            (textList) -> {
                                VillagerTradeRecord record = villagersDataset.get(slot);
                                if (record != null && isLocked(slot)) {
                                    if (!record.recipes.isEmpty() && selected[slot] < record.recipes.size()) {
                                        VillagerRecipe recipe = record.recipes.get(selected[slot]);
                                        textList.add(Component.literal(String.valueOf(recipe.uses)));
                                        textList.add(Component.literal(String.valueOf(recipe.maxUses)));
                                    } else {
                                        textList.add(Component.literal(String.valueOf(0)));
                                        textList.add(Component.literal(String.valueOf(0)));
                                    }
                                    textList.add(Component.literal(String.valueOf(record.restocksToday)));
                                    textList.add(Component.literal(String.valueOf(record.maxRestocksToday)));
                                }
                            }));

                    group.addWidget(villagerGroup);
                }
                return group;
            }
        });

        sideTabs.attachSubTab(new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return IGuiTexture.EMPTY;
            }

            @Override
            public Component getTitle() {
                return Component.empty();
            }

            final int width = 256;
            final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup group_title = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                group.addWidget(group_title);

                group.setBackground(GuiTextures.BACKGROUND_INVERSE);
                return group;
            }
        });

        // 方向配置页
        sideTabs.attachSubTab(CombinedDirectionalFancyConfigurator.of(this, this));
    }

    /////////////////////////////////////
    // ********* 辅助类与方法 ********* //
    /////////////////////////////////////

    private static class VillageHolder extends NotifiableItemStackHandler {

        private final VillageTradingStationMachine machine;

        private VillageHolder(VillageTradingStationMachine machine) {
            super(machine, 9, IO.NONE, IO.BOTH, CustomItemStackHandler::new);
            this.machine = machine;
            setFilter(i -> i.getItem().equals(RegistriesUtils.getItem("easy_villagers:villager")));
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (machine.isLocked(slot)) return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (machine.isLocked(slot)) return stack;
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem().equals(RegistriesUtils.getItem("easy_villagers:villager"));
        }
    }

    // 村民交易配方类
    private static class VillagerRecipe implements IManaged {

        private final VillageTradingStationMachine machine;

        @Persisted
        private final ItemStack buy;
        @Persisted
        private final ItemStack buyB;
        @Persisted
        private final ItemStack sell;
        @Persisted
        private final int maxUses;
        @Persisted
        private int uses;

        public VillagerRecipe(VillageTradingStationMachine machine, ItemStack buy, ItemStack buyB, ItemStack sell, int maxUses, int uses) {
            this.machine = machine;
            this.buy = buy;
            this.buyB = buyB;
            this.sell = sell;
            this.maxUses = maxUses;
            this.uses = uses;
        }

        private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(VillagerRecipe.class);

        @Override
        public ManagedFieldHolder getFieldHolder() {
            return MANAGED_FIELD_HOLDER;
        }

        @Override
        public IManagedStorage getSyncStorage() {
            return machine.getSyncStorage();
        }

        @Override
        public void onChanged() {
            machine.onChanged();
        }
    }

    // 村民交易类
    private static class VillagerTradeRecord implements IManaged {

        private final VillageTradingStationMachine machine;

        @Persisted
        private int restocksToday;
        @Persisted
        private int maxRestocksToday;
        @Persisted
        private final ObjectArrayList<VillagerRecipe> recipes;

        public VillagerTradeRecord(VillageTradingStationMachine machine, int restocksToday, int maxRestocksToday, List<VillagerRecipe> recipes) {
            this.machine = machine;
            this.restocksToday = restocksToday;
            this.maxRestocksToday = maxRestocksToday;
            this.recipes = new ObjectArrayList<>(recipes);
        }

        private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(VillagerTradeRecord.class);

        @Override
        public ManagedFieldHolder getFieldHolder() {
            return MANAGED_FIELD_HOLDER;
        }

        @Override
        public IManagedStorage getSyncStorage() {
            return machine.getSyncStorage();
        }

        @Override
        public void onChanged() {
            machine.onChanged();
        }
    }

    // 解析村民NBT数据获取交易配方
    private VillagerTradeRecord parseTradeData(CompoundTag originalOuterNbt) {
        if (!originalOuterNbt.contains("villager", 10)) {
            return new VillagerTradeRecord(this, 0, 2, new ArrayList<>());
        }
        CompoundTag villagerCoreNbt = originalOuterNbt.getCompound("villager");

        int restocksToday = villagerCoreNbt.getInt("RestocksToday");
        int maxRestocksToday = villagerCoreNbt.contains("MaxRestocksToday", 3) ? villagerCoreNbt.getInt("MaxRestocksToday") : 2;

        List<VillagerRecipe> recipes = new ArrayList<>();
        if (!villagerCoreNbt.contains("Offers", 10)) {
            return new VillagerTradeRecord(this, restocksToday, maxRestocksToday, recipes);
        }
        CompoundTag offersTag = villagerCoreNbt.getCompound("Offers");
        if (!offersTag.contains("Recipes", 9)) {
            return new VillagerTradeRecord(this, restocksToday, maxRestocksToday, recipes);
        }
        ListTag recipesList = offersTag.getList("Recipes", 10);

        for (int i = 0; i < recipesList.size(); i++) {
            CompoundTag recipeTag = recipesList.getCompound(i);
            ItemStack buy = parseItemStack(recipeTag.getCompound("buy"));
            ItemStack buyB = parseItemStack(recipeTag.getCompound("buyB"));
            ItemStack sell = parseItemStack(recipeTag.getCompound("sell"));
            int maxUses = recipeTag.getInt("maxUses");
            int uses = recipeTag.getInt("uses");
            recipes.add(new VillagerRecipe(this, buy, buyB, sell, maxUses, uses));
        }

        return new VillagerTradeRecord(this, restocksToday, maxRestocksToday, recipes);
    }

    // 解析NBT为物品栈
    private static ItemStack parseItemStack(CompoundTag itemTag) {
        String itemId = itemTag.getString("id");
        ItemStack item = RegistriesUtils.getItemStack(itemId);
        if (item.getItem().equals(Items.BARRIER)) return ItemStack.EMPTY;
        int count = Math.max(1, itemTag.getByte("Count"));
        item.setCount(count);
        return item;
    }

    // 村民补货：重置交易次数
    private void villagersRestock() {
        for (VillagerTradeRecord record : villagersDataset) {
            if (record == null) continue;
            // if (record.restocksToday < record.maxRestocksToday) continue;
            for (VillagerRecipe recipe : record.recipes) {
                if (recipe != null && recipe.uses != 0) {
                    record.restocksToday = record.restocksToday + 1;
                    break;
                }
            }
            for (VillagerRecipe recipe : record.recipes) {
                if (recipe != null) recipe.uses = 0;
            }
        }
    }

    // 每日重置补货次数
    private void dailyRestockingAttempts() {
        for (VillagerTradeRecord record : villagersDataset) {
            if (record != null) record.restocksToday = 0;
        }
    }

    /////////////////////////////////////
    // ********* 自动输出实现 ********* //
    /////////////////////////////////////

    @Persisted
    @DescSynced
    @RequireRerender
    private Direction outputFacingItems;
    @Persisted
    @DescSynced
    @RequireRerender
    private boolean autoOutputItems;
    @Persisted
    private boolean allowInputFromOutputSideItems;

    @Override
    public boolean hasAutoOutputItem() {
        return true;
    }

    @Override
    @Nullable
    public Direction getOutputFacingItems() {
        if (hasAutoOutputItem()) {
            return outputFacingItems;
        }
        return null;
    }

    @Override
    public void setOutputFacingItems(@Nullable Direction outputFacing) {
        if (hasAutoOutputItem()) {
            clearDirectionCache();
            outputFacingItems = outputFacing;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void setAutoOutputItems(boolean allow) {
        if (hasAutoOutputItem()) {
            autoOutputItems = allow;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public boolean isAutoOutputItems() {
        return this.autoOutputItems;
    }

    @Override
    public void setAllowInputFromOutputSideItems(final boolean allowInputFromOutputSideItems) {
        clearDirectionCache();
        this.allowInputFromOutputSideItems = allowInputFromOutputSideItems;
    }

    @Override
    public boolean isAllowInputFromOutputSideItems() {
        return this.allowInputFromOutputSideItems;
    }

    private void updateAutoOutputSubscription() {
        if (getLevel() == null) return;
        Direction outputFacing = getOutputFacingItems();
        boolean needOutput = autoOutputItems && !output.isEmpty() && outputFacing != null && blockEntityDirectionCache.hasAdjacentItemHandler(getLevel(), getPos().relative(outputFacing), outputFacing.getOpposite());
        if (needOutput) {
            autoOutputSubs = subscribeServerTick(autoOutputSubs, this::autoOutput);
        } else if (autoOutputSubs != null) {
            autoOutputSubs.unsubscribe();
            autoOutputSubs = null;
        }
    }

    private void autoOutput() {
        if (getOffsetTimer() % 20 == 0) {
            if (autoOutputItems && getOutputFacingItems() != null) {
                output.exportToNearby(getOutputFacingItems());
            }
        }
        updateAutoOutputSubscription();
    }
}
