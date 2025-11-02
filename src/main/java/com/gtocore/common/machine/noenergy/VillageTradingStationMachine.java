package com.gtocore.common.machine.noenergy;

import com.gtocore.common.data.translation.GTOMachineTooltips;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

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
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gtocore.common.data.GTOItems.*;

public class VillageTradingStationMachine extends SimpleNoEnergyMachine {

    public VillageTradingStationMachine(MetaMachineBlockEntity holder) {
        super(holder, 14, i -> 0);
        this.setAutoOutputItems(true);
        villagers = new VillageHolder(this);
    }

    /////////////////////////////////////
    // *********** 信息存储 *********** //
    /////////////////////////////////////

    private TickableSubscription tickSubs;

    // 村民存储
    @Persisted
    private VillageHolder villagers;
    // 村民锁定
    @Persisted
    private boolean[] isLocked = new boolean[9];
    // 村民配方选择
    @Persisted
    private int[] selected = new int[9];
    // 村民配方锁定（是否启动交易）
    @Persisted
    private boolean[] startUp = new boolean[9];

    // 村民信息存储（交易配方等）
    private final VillagerTradeRecord[] VillagersDataset = new VillagerTradeRecord[9];
    // 村民当前选中的交易配方（展示用）
    private final ItemStackHandler RecipesHandler = new ItemStackHandler(3 * 9);

    // 补货时间间隔
    private int replenishmentInterval = 2400;
    // 交易倍数
    private int tradingMultiple = 1;

    // 输入输出物品存储
    @Persisted
    private final NotifiableItemStackHandler input = new NotifiableItemStackHandler(this, 256, IO.IN, IO.IN, CustomItemStackHandler::new);
    @Persisted
    private final NotifiableItemStackHandler output = new NotifiableItemStackHandler(this, 256, IO.OUT, IO.OUT, CustomItemStackHandler::new);

    /////////////////////////////////////
    // *********** 核心方法 *********** //
    /////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            // 启动服务器端定时更新（每刻执行）
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
        }
        // 初始化村民交易数据
        for (int i = 0; i < 9; i++) {
            incomingVillagersDataset(i);
            selectedRecipes(i);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    /**
     * 定时更新逻辑：处理交易执行和补货
     */
    private void tickUpdate() {
        if (getOffsetTimer() % 100 != 0 || isRemote()) {
            return; // 每100刻（约5秒）执行一次，且只在服务器端运行
        }

        int daytime = getOffsetTimer() % 24000;
        // 每天0点重置补货次数
        if (daytime == 0) dailyRestockingAttempts();
        // 到达补货间隔时，重置村民交易次数
        if (daytime % replenishmentInterval == 0) villagersRestock();
        // 执行交易逻辑
        executeTrades();
    }

    /**
     * 直接执行交易：检查输入物品，扣除材料，添加产物
     */
    private void executeTrades() {
        for (int slot = 0; slot < 9; slot++) {
            // 跳过未锁定、未启动或无配方的村民
            if (!isLocked(slot) || !isStartUp(slot) || VillagersDataset[slot] == null) {
                continue;
            }

            List<VillagerRecipe> recipes = VillagersDataset[slot].recipes;
            if (recipes.isEmpty() || selected[slot] >= recipes.size()) {
                continue;
            }

            VillagerRecipe trade = recipes.get(selected[slot]);
            int remainingUses = trade.maxUses - trade.uses;
            if (remainingUses <= 0) {
                continue;
            }

            for (int i = 0; i < remainingUses; i++) {
                // 1. 检查输入物品是否足够（考虑交易倍数）
                int requiredBuyCount = trade.buy.getCount() * tradingMultiple;
                int requiredBuyBCount = trade.buyB.isEmpty() ? 0 : trade.buyB.getCount() * tradingMultiple;

                boolean hasEnoughBuy = hasEnoughItems(input, trade.buy, requiredBuyCount);
                boolean hasEnoughBuyB = requiredBuyBCount == 0 || hasEnoughItems(input, trade.buyB, requiredBuyBCount);

                if (!hasEnoughBuy || !hasEnoughBuyB) {
                    break; // 材料不足，跳过交易
                }

                // 再次确认材料是否足够maxPossibleTrades次
                if (!hasEnoughItems(input, trade.buy, trade.buy.getCount() * tradingMultiple) ||
                        (requiredBuyBCount > 0 && !hasEnoughItems(input, trade.buyB, trade.buyB.getCount() * tradingMultiple))) {
                    continue;
                }

                // 3. 扣除输入物品
                deductItems(input, trade.buy, trade.buy.getCount() * tradingMultiple);
                if (requiredBuyBCount > 0) {
                    deductItems(input, trade.buyB, trade.buyB.getCount() * tradingMultiple);
                }

                // 4. 添加输出物品
                ItemStack outputStack = trade.sell.copy();
                outputStack.setCount(outputStack.getCount() * tradingMultiple);
                addItems(output, outputStack);

                // 5. 更新交易次数
                trade.uses++;
            }
        }
    }

    /////////////////////////////////////
    // ******** 物品操作工具方法 ******** //
    /////////////////////////////////////

    /**
     * 检查物品处理器中是否有足够数量的目标物品
     * 
     * @param handler       物品处理器（如input）
     * @param target        目标物品
     * @param requiredCount 所需数量
     * @return 是否足够
     */
    private boolean hasEnoughItems(NotifiableItemStackHandler handler, ItemStack target, int requiredCount) {
        if (target.isEmpty() || requiredCount <= 0) {
            return true;
        }
        int total = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (ItemStack.isSameItemSameTags(stack, target)) {
                total += stack.getCount();
                if (total >= requiredCount) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从物品处理器中扣除指定数量的物品
     * 
     * @param handler 物品处理器（如input）
     * @param target  目标物品
     * @param count   扣除数量
     */
    private void deductItems(NotifiableItemStackHandler handler, ItemStack target, int count) {
        if (target.isEmpty() || count <= 0) {
            return;
        }
        int remaining = count;
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
     *
     * @param handler 物品处理器（如output）
     * @param stack   要添加的物品
     */
    private void addItems(NotifiableItemStackHandler handler, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        ItemStack remaining = stack.copy();
        int maxStackSize = remaining.getMaxStackSize(); // 获取该物品的最大堆叠数

        // 第一步：尝试堆叠到已有相同物品的槽位
        for (int i = 0; i < handler.getSlots() && !remaining.isEmpty(); i++) {
            ItemStack existing = handler.getStackInSlot(i);
            if (existing.isEmpty()) {
                continue; // 空槽位留到第二步处理
            }
            // 仅堆叠相同物品且未达最大堆叠
            if (ItemStack.isSameItemSameTags(existing, remaining) && existing.getCount() < maxStackSize) {
                int addAmount = Math.min(remaining.getCount(), maxStackSize - existing.getCount());
                existing.grow(addAmount);
                remaining.shrink(addAmount);
            }
        }

        // 第二步：处理剩余物品（拆分到空槽位，确保不超过最大堆叠）
        while (!remaining.isEmpty()) {
            boolean foundSlot = false;
            // 遍历所有槽位找空槽
            for (int i = 0; i < handler.getSlots() && !remaining.isEmpty(); i++) {
                ItemStack existing = handler.getStackInSlot(i);
                if (existing.isEmpty()) {
                    // 计算当前空槽能放的最大数量（不超过物品最大堆叠）
                    int putAmount = Math.min(remaining.getCount(), maxStackSize);
                    // 创建新的物品栈放入空槽
                    ItemStack toPut = remaining.copy();
                    toPut.setCount(putAmount);
                    handler.setStackInSlot(i, toPut);
                    // 减少剩余数量
                    remaining.shrink(putAmount);
                    foundSlot = true;
                }
            }
            // 如果没找到空槽且还有剩余，跳出循环（无法继续放置）
            if (!foundSlot) {
                break;
            }
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
            VillagersDataset[slot] = null;
            setStartUp(slot);
        }
        selectedRecipes(slot);
    }

    public void releaseLocked(int slot) {
        isLocked[slot] = false;
    }

    private void selectedNext(int slot) {
        if (!isLocked(slot) || isStartUp(slot)) return;
        if (VillagersDataset[slot] != null && !VillagersDataset[slot].recipes.isEmpty()) {
            selected[slot] = (selected[slot] + 1) % VillagersDataset[slot].recipes.size();
            selectedRecipes(slot);
        } else {
            releaseStartUp(slot);
        }
    }

    private boolean isStartUp(int slot) {
        return startUp[slot];
    }

    private void setStartUp(int slot) {
        if (isLocked(slot)) {
            startUp[slot] = !startUp[slot];
            if (VillagersDataset[slot] != null && VillagersDataset[slot].recipes.isEmpty()) {
                releaseStartUp(slot);
            }
        } else {
            startUp[slot] = false;
        }
    }

    private void releaseStartUp(int slot) {
        startUp[slot] = false;
    }

    private void incomingVillagersDataset(int slot) {
        ItemStack villager = villagers.getStackInSlot(slot);
        if (!villager.isEmpty() && isLocked[slot]) {
            VillagersDataset[slot] = parseTradeData(villager.getOrCreateTag());
        } else {
            VillagersDataset[slot] = null;
            releaseLocked(slot);
        }
    }

    private void selectedRecipes(int slot) {
        if (VillagersDataset[slot] != null && isLocked(slot)) {
            List<VillagerRecipe> recipes = VillagersDataset[slot].recipes;
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
    // ************ UI组件 ************ //
    /////////////////////////////////////

    @Override
    public Widget createUIWidget() {
        int width = 336;
        int height = 144;
        var group = new WidgetGroup(0, 0, width + 8, height + 8);

        WidgetGroup group_title = new DraggableScrollableWidgetGroup(4, 4, width, height)
                .setBackground(GuiTextures.DISPLAY);

        group_title.addWidget(new ComponentPanelWidget(4, 5,
                textList -> GTOMachineTooltips.INSTANCE.getVillageTradingStationIntroduction().apply(textList))
                .setMaxWidthLimit(width - 8));

        group.addWidget(group_title);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);

        // 交易控制页（显示村民和配方）
        sideTabs.attachSubTab(new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return new ItemStackTexture(self().getDefinition().asItem());
            }

            @Override
            public Component getTitle() {
                return Component.translatable(self().getDefinition().getDescriptionId());
            }

            final int width = 256;
            final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup group_title = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);
                group_title.addWidget(new ComponentPanelWidget(4, 4, this::addDisplayTextTitle));
                group.addWidget(group_title);
                group.addWidget(getLockButton());
                group.addWidget(getInputOutputSlots());
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
                int startY = 16;
                WidgetGroup group = new WidgetGroup(startX, startY, groupXSize, ySize);

                for (int i = 0; i < 9; i++) {
                    final int slotIndex = i;
                    int buttonX = i * (xSize + buttonSpacing);
                    WidgetGroup VillagerGroup = new WidgetGroup(buttonX, 0, xSize, ySize);
                    VillagerGroup.setLayout(Layout.VERTICAL_CENTER);
                    // 锁定按钮
                    VillagerGroup.addWidget(new ComponentPanelWidget(0, 2,
                            (textList) -> textList.add(ComponentPanelWidget.withButton(
                                    isLocked(slotIndex) ? Component.literal("\uD83D\uDD12") : Component.literal("\uD83D\uDD13"),
                                    String.valueOf(slotIndex))))
                            .clickHandler((a, b) -> setLocked(Integer.parseInt(a))));
                    // 村民槽位
                    VillagerGroup.addWidget(new SlotWidget(villagers, i, 0, 18 - 6, true, true)
                            .setBackground(GuiTextures.SLOT));
                    // 配方输入1、输入2、输出槽位
                    for (int j = 0; j < 3; j++) {
                        SlotWidget itemWidget = new SlotWidget(RecipesHandler, 3 * i + j, 0, 18 * (j + 2) - 6)
                                .setCanPutItems(false).setCanTakeItems(false)
                                .setBackgroundTexture(new ResourceTexture(GTOCore.id("textures/gui/villager_recipe_slot_" + j + ".png")));
                        VillagerGroup.addWidget(itemWidget);
                    }
                    // 切换配方按钮
                    VillagerGroup.addWidget(new ComponentPanelWidget(0, 2 + 18 * 5 - 6,
                            (textList) -> textList.add(ComponentPanelWidget.withButton(
                                    Component.literal("\uD83D\uDD01"), String.valueOf(slotIndex))))
                            .clickHandler((a, b) -> selectedNext(Integer.parseInt(a))));
                    // 启动交易按钮
                    VillagerGroup.addWidget(new ComponentPanelWidget(0, 2 + 18 * 5 + 6,
                            (textList) -> textList.add(ComponentPanelWidget.withButton(
                                    isStartUp(slotIndex) ? Component.literal("\uD83D\uDD12") : Component.literal("\uD83D\uDD13"),
                                    String.valueOf(slotIndex))))
                            .clickHandler((a, b) -> setStartUp(Integer.parseInt(a))));
                    // 交易次数显示
                    VillagerGroup.addWidget(new ComponentPanelWidget(0, 2 + 18 * 5 + 18,
                            (textList) -> {
                                if (VillagersDataset[slotIndex] != null && isLocked(slotIndex) &&
                                        !VillagersDataset[slotIndex].recipes.isEmpty() && selected[slotIndex] < VillagersDataset[slotIndex].recipes.size()) {
                                    VillagerRecipe recipe = VillagersDataset[slotIndex].recipes.get(selected[slotIndex]);
                                    textList.add(Component.literal(String.valueOf(recipe.uses)));
                                    textList.add(Component.literal(String.valueOf(recipe.maxUses)));
                                } else {
                                    textList.add(Component.literal(String.valueOf(0)));
                                    textList.add(Component.literal(String.valueOf(0)));
                                }
                            }));

                    group.addWidget(VillagerGroup);
                }
                return group;
            }

            // 显示输入输出槽位（简化为前8个槽位预览）
            private Widget getInputOutputSlots() {
                WidgetGroup group = new WidgetGroup(10, 100, 240, 40);
                // 输入槽位预览
                group.addWidget(new LabelWidget(0, 0, Component.literal("Input:")));
                for (int i = 0; i < 4; i++) {
                    group.addWidget(new SlotWidget(input, i, 40 + i * 18, 0, true, false)
                            .setBackground(GuiTextures.SLOT));
                }
                // 输出槽位预览
                group.addWidget(new LabelWidget(0, 20, Component.literal("Output:")));
                for (int i = 0; i < 4; i++) {
                    group.addWidget(new SlotWidget(output, i, 40 + i * 18, 20, false, true)
                            .setBackground(GuiTextures.SLOT));
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
    // ************ 辅助类与方法 ************ //
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

    // 村民交易记录类
    private static class VillagerTradeRecord {

        private int restocksToday;
        private int maxRestocksToday;
        private List<VillagerRecipe> recipes;

        public VillagerTradeRecord(int restocksToday, int maxRestocksToday, List<VillagerRecipe> recipes) {
            this.restocksToday = restocksToday;
            this.maxRestocksToday = maxRestocksToday;
            this.recipes = recipes;
        }
    }

    // 村民交易配方类
    private static class VillagerRecipe {

        private final ItemStack buy;
        private final ItemStack buyB;
        private final ItemStack sell;
        private final int maxUses;
        private int uses;

        public VillagerRecipe(ItemStack buy, ItemStack buyB, ItemStack sell, int maxUses, int uses) {
            this.buy = buy;
            this.buyB = buyB;
            this.sell = sell;
            this.maxUses = maxUses;
            this.uses = uses;
        }
    }

    // 解析村民NBT数据获取交易配方
    private static VillagerTradeRecord parseTradeData(CompoundTag originalOuterNbt) {
        if (!originalOuterNbt.contains("villager", 10)) {
            return new VillagerTradeRecord(0, 2, new ArrayList<>());
        }
        CompoundTag villagerCoreNbt = originalOuterNbt.getCompound("villager");

        int restocksToday = villagerCoreNbt.getInt("RestocksToday");
        int maxRestocksToday = villagerCoreNbt.contains("MaxRestocksToday", 3) ? villagerCoreNbt.getInt("MaxRestocksToday") : 2;

        List<VillagerRecipe> recipes = new ArrayList<>();
        if (!villagerCoreNbt.contains("Offers", 10)) {
            return new VillagerTradeRecord(restocksToday, maxRestocksToday, recipes);
        }
        CompoundTag offersTag = villagerCoreNbt.getCompound("Offers");
        if (!offersTag.contains("Recipes", 9)) {
            return new VillagerTradeRecord(restocksToday, maxRestocksToday, recipes);
        }
        ListTag recipesList = offersTag.getList("Recipes", 10);

        for (int i = 0; i < recipesList.size(); i++) {
            CompoundTag recipeTag = recipesList.getCompound(i);
            ItemStack buy = parseItemStack(recipeTag.getCompound("buy"));
            ItemStack buyB = parseItemStack(recipeTag.getCompound("buyB"));
            ItemStack sell = parseItemStack(recipeTag.getCompound("sell"));
            int maxUses = recipeTag.getInt("maxUses");
            int uses = recipeTag.getInt("uses");
            recipes.add(new VillagerRecipe(buy, buyB, sell, maxUses, uses));
        }

        return new VillagerTradeRecord(restocksToday, maxRestocksToday, recipes);
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
        for (VillagerTradeRecord record : VillagersDataset) {
            if (record == null || record.recipes == null) continue;
            // if (record.restocksToday < record.maxRestocksToday)continue;
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
        for (VillagerTradeRecord record : VillagersDataset) {
            if (record != null) record.restocksToday = 0;
        }
    }

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
}
