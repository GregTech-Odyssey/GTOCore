package com.gtocore.common.machine.noenergy;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.common.data.translation.GTOMachineTooltips;

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
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMachines;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gtocore.common.data.GTOItems.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VillageTradingStationMachine extends MetaMachine implements IAutoOutputItem, IFancyUIMachine, IMachineLife {

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
    @DescSynced
    private final VillageHolder villagers;
    @Persisted
    @DescSynced
    private final boolean[] isLocked = new boolean[10];
    @Persisted
    @DescSynced
    private final int[] selected = new int[10];
    @Persisted
    @DescSynced
    private final boolean[] startUp = new boolean[10];

    private final VillagerRecipe[][] villagersDataset = new VillagerRecipe[10][];
    private final ItemStackHandler RecipesHandler = new ItemStackHandler(3 * 10);

    // 升级物品
    @Persisted
    private final CustomItemStackHandler upgrade;
    @Persisted
    private final CustomItemStackHandler enhance;

    // 最大交易次数 32*
    private static final Item[] FIELD_GENERATOR = {
            FIELD_GENERATOR_LV.asItem(), FIELD_GENERATOR_MV.asItem(), FIELD_GENERATOR_HV.asItem(), FIELD_GENERATOR_EV.asItem(),
            FIELD_GENERATOR_IV.asItem(), FIELD_GENERATOR_LuV.asItem(), FIELD_GENERATOR_ZPM.asItem(), FIELD_GENERATOR_UV.asItem() };

    // 补货与交易参数
    @Persisted
    private int replenishmentInterval = 2400;
    @Persisted
    private int tradingMultiple = 1;

    @Persisted
    private int tire = 0;
    // 补货时间间隔 -225* 多倍交易 4*
    private static final Map<Item, Integer> ENHANCE_INDEX_MAP = Map.ofEntries(
            Map.entry(GTMachines.WORLD_ACCELERATOR[LV].asItem(), 1),
            Map.entry(GTMachines.WORLD_ACCELERATOR[MV].asItem(), 2),
            Map.entry(GTMachines.WORLD_ACCELERATOR[HV].asItem(), 3),
            Map.entry(GTMachines.WORLD_ACCELERATOR[EV].asItem(), 4),
            Map.entry(GTMachines.WORLD_ACCELERATOR[IV].asItem(), 5),
            Map.entry(GTMachines.WORLD_ACCELERATOR[LuV].asItem(), 6),
            Map.entry(GTMachines.WORLD_ACCELERATOR[ZPM].asItem(), 7),
            Map.entry(GTMachines.WORLD_ACCELERATOR[UV].asItem(), 8),
            Map.entry(INTEGRATED_CONTROL_CORE_UV.asItem(), 9),
            Map.entry(INTEGRATED_CONTROL_CORE_UHV.asItem(), 10),
            Map.entry(INTEGRATED_CONTROL_CORE_UEV.asItem(), 11),
            Map.entry(INTEGRATED_CONTROL_CORE_UIV.asItem(), 12));
    private static final Item[] ENHANCE_ITEMS = {
            Items.AIR,
            GTMachines.WORLD_ACCELERATOR[LV].asItem(), GTMachines.WORLD_ACCELERATOR[MV].asItem(), GTMachines.WORLD_ACCELERATOR[HV].asItem(), GTMachines.WORLD_ACCELERATOR[EV].asItem(),
            GTMachines.WORLD_ACCELERATOR[IV].asItem(), GTMachines.WORLD_ACCELERATOR[LuV].asItem(), GTMachines.WORLD_ACCELERATOR[ZPM].asItem(), GTMachines.WORLD_ACCELERATOR[UV].asItem(),
            INTEGRATED_CONTROL_CORE_UV.asItem(), INTEGRATED_CONTROL_CORE_UHV.asItem(), INTEGRATED_CONTROL_CORE_UEV.asItem(), INTEGRATED_CONTROL_CORE_UIV.asItem() };

    private static final Item VILLAGER_ITEM = RegistriesUtils.getItem("easy_villagers:villager");

    public VillageTradingStationMachine(MetaMachineBlockEntity holder) {
        super(holder);
        input = new NotifiableItemStackHandler(this, 256, IO.IN, IO.IN);
        output = new NotifiableItemStackHandler(this, 256, IO.OUT, IO.OUT);
        villagers = new VillageHolder(this);
        upgrade = new CustomItemStackHandler();
        enhance = new CustomItemStackHandler();
        enhance.setOnContentsChanged(() -> {
            tire = ENHANCE_INDEX_MAP.getOrDefault(enhance.getStackInSlot(0).getItem(), 0);
            if (tire < 9) {
                replenishmentInterval = 2400 - 225 * tire;
                tradingMultiple = 1;
            } else {
                replenishmentInterval = 600;
                tradingMultiple = (tire - 8) * 4;
            }
        });
        outputFacingItems = hasFrontFacing() ? getFrontFacing().getOpposite() : Direction.DOWN;
    }

    /////////////////////////////////////
    // ********* 生命周期管理 ********* //
    /////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        input.notifyListeners();
        output.notifyListeners();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate, 100);
            exportItemSubs = output.addChangedListener(this::updateAutoOutputSubscription);
        }
        for (int i = 0; i < 10; i++) {
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

    /////////////////////////////////////
    // ********* 核心交易逻辑 ********* //
    /////////////////////////////////////

    private int replenishment;
    private int executeTrades;

    // 定时更新
    private void tickUpdate() {
        // 定时补货
        if (tickSubs.lastTick > replenishment) {
            villagersRestock();
            replenishment = tickSubs.lastTick + replenishmentInterval;
        }
        // 执行交易
        if (tickSubs.lastTick > executeTrades) {
            executeTrades = tickSubs.lastTick + 200;
            for (int slot = 0; slot < 9; slot++) {
                executeTrades(slot);
            }
        }
    }

    // 执行交易逻辑
    private void executeTrades(int slot) {
        if (!isLocked(slot) || !isStartUp(slot) || villagersDataset[slot] == null || villagersDataset[slot].length == 0) return;

        VillagerRecipe[] recipes = villagersDataset[slot];
        if (selected[slot] >= recipes.length) return;

        VillagerRecipe trade = recipes[selected[slot]];
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

        villagersDataset[slot][selected[slot]].uses += actualTrades / tradingMultiple;
        syncUsesAndMaxUsesToVillagerItem(slot);
    }

    // 计算两个输入物品堆能支持的最大交易次数
    private int getMaxPossibleTrades(IItemHandlerModifiable input, ItemStack buy, ItemStack buyB) {
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

    // 从物品处理器中扣除指定数量的物品
    private void deductItems(IItemHandlerModifiable handler, ItemStack target, int count) {
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

    // 向物品处理器中添加物品
    private void addItems(IItemHandlerModifiable handler, ItemStack stack) {
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

    private boolean isLocked(int slot) {
        return isLocked[slot];
    }

    public void setLocked(int slot) {
        isLocked[slot] = !isLocked[slot];
        selected[slot] = 0;
        if (isLocked[slot]) {
            incomingVillagersDataset(slot);
        } else {
            syncUsesAndMaxUsesToVillagerItem(slot);
            villagersDataset[slot] = null;
            setStartUp(slot);
        }
        selectedRecipes(slot);
    }

    private void selectedNext(int slot) {
        if (!isLocked(slot) || isStartUp(slot)) return;
        VillagerRecipe[] recipes = villagersDataset[slot];
        if (recipes != null && recipes.length > 0) {
            selected[slot] = (selected[slot] + 1) % recipes.length;
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
            VillagerRecipe[] recipes = villagersDataset[slot];
            // 数组判断
            if (recipes == null || recipes.length == 0) {
                startUp[slot] = false;
            }
            if (startUp[slot]) executeTrades(slot);
        } else {
            startUp[slot] = false;
        }
    }

    // 加载村民的配方数据到数据集
    private void incomingVillagersDataset(int slot) {
        ItemStack villager = villagers.getStackInSlot(slot);
        if (!villager.isEmpty() && isLocked[slot]) {
            List<VillagerRecipe> recipeList = parseTradeData(villager.getOrCreateTag());
            CompoundTag villagerCoreNbt = villager.getOrCreateTag().getCompound("villager");
            CompoundTag offersTag = villagerCoreNbt.getCompound("Offers");
            ListTag nbtRecipes = offersTag.getList("Recipes", CompoundTag.TAG_COMPOUND);
            if (recipeList.size() != nbtRecipes.size()) {
                recipeList = recipeList.subList(0, Math.min(recipeList.size(), nbtRecipes.size()));
            }
            villagersDataset[slot] = recipeList.toArray(new VillagerRecipe[0]);
        } else {
            villagersDataset[slot] = null;
            isLocked[slot] = false;
        }
    }

    // 选中配方并更新UI显示
    private void selectedRecipes(int slot) {
        VillagerRecipe[] recipes = villagersDataset[slot];
        if (recipes != null && isLocked(slot) && recipes.length > 0 && selected[slot] < recipes.length) {
            VillagerRecipe recipe = recipes[selected[slot]];
            RecipesHandler.setStackInSlot(slot * 3, recipe.buy.copy());
            RecipesHandler.setStackInSlot(slot * 3 + 1, recipe.buyB.copy());
            RecipesHandler.setStackInSlot(slot * 3 + 2, recipe.sell.copy());
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
        final int width = 336;
        final int height = 144;
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
                return new ItemStackTexture(VILLAGER_ITEM);
            }

            @Override
            public Component getTitle() {
                return Component.translatable(getDefinition().getDescriptionId());
            }

            static final int width = 192;
            static final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);
                mainGroup.addWidget(getVillagerGroups());

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);
                return group;
            }

            private @NotNull WidgetGroup getVillagerGroups() {
                int xSize = 18;
                int groupXSize = xSize * 9;
                int startX = (width - groupXSize) / 2;
                int startY = 16;
                WidgetGroup group = new WidgetGroup(startX, startY, groupXSize, 128);
                for (int i = 0; i < 9; i++) {
                    group.addWidget(VillagerGroup(i, i * xSize));
                }
                return group;
            }
        });

        // 升级控制页
        sideTabs.attachSubTab(new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return new ItemStackTexture(VILLAGER_ITEM);
            }

            @Override
            public Component getTitle() {
                return Component.empty();
            }

            static final int width = 192;
            static final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                WidgetGroup villagerGroups = new WidgetGroup(15, 16, 162, 128);
                villagerGroups.addWidget(VillagerGroup(9, 0));

                mainGroup.addWidget(villagerGroups);
                mainGroup.addWidget(getUpgradeGroup());
                mainGroup.addWidget(getEnhanceGroup());

                group.addWidget(mainGroup);

                group.setBackground(GuiTextures.BACKGROUND_INVERSE);
                return group;
            }

            private @NotNull WidgetGroup getUpgradeGroup() {
                WidgetGroup villagerGroup = new WidgetGroup(22 + 15, 18 * 5 - 9 + 16, 150, 50);

                VillagerRecipe[] recipes = villagersDataset[9];
                villagerGroup.addWidget(new ComponentPanelWidget(20, 0,
                        (textList) -> {
                            if (recipes != null && isLocked(9) && recipes.length > 0 && selected[9] < recipes.length) {
                                int upGread = recipes[selected[9]].maxUses / 32;
                                if (recipes[selected[9]].maxUses < 256) {
                                    ItemStack item = FIELD_GENERATOR[upGread].getDefaultInstance();
                                    int count = Math.min(getMaxPossibleTrades(upgrade, item, ItemStack.EMPTY), (upGread + 1) * 32 - recipes[selected[9]].maxUses);
                                    textList.add(ComponentPanelWidget.withButton(
                                            Component.literal("[ + " + count + " → " + (recipes[selected[9]].maxUses + count) + " ]"), "add_max_uses"));
                                    textList.add(Component.translatable("gtocore.machine.village_trading_station.increase", item.getDisplayName()));
                                } else {
                                    textList.add(Component.empty());
                                    textList.add(Component.translatable("gtocore.machine.village_trading_station.upper_limit"));
                                }
                            }
                        }).clickHandler((a, b) -> {
                            if (recipes != null && isLocked(9) && recipes.length > 0 && selected[9] < recipes.length) {
                                int upGread = recipes[selected[9]].maxUses / 32;
                                if (recipes[selected[9]].maxUses < 256) {
                                    ItemStack item = FIELD_GENERATOR[upGread].getDefaultInstance();
                                    int count = Math.min(getMaxPossibleTrades(upgrade, item, ItemStack.EMPTY), (upGread + 1) * 32 - recipes[selected[9]].maxUses);
                                    if (count > 0) {
                                        deductItems(upgrade, item, count);
                                        villagersDataset[9][selected[9]].maxUses += count;
                                        syncUsesAndMaxUsesToVillagerItem(9);
                                    }
                                }
                            }
                        }).setMaxWidthLimit(120));

                villagerGroup.addWidget(new SlotWidget(upgrade, 0, 0, 0)
                        .setBackgroundTexture(GuiTextures.SLOT));

                return villagerGroup;
            }

            private @NotNull WidgetGroup getEnhanceGroup() {
                WidgetGroup villagerGroup = new WidgetGroup(22 + 15, 18 - 9 + 16, 150, 100);

                villagerGroup.addWidget(new ComponentPanelWidget(0, 9,
                        (textList) -> {
                            if (tire < 12) {
                                textList.add(Component.literal("     ")
                                        .append(Component.translatable("gtocore.machine.village_trading_station.enhance",
                                                ENHANCE_ITEMS[tire + 1].getDefaultInstance().getDisplayName())));
                            } else {
                                textList.add(Component.literal("     ")
                                        .append(Component.translatable("gtocore.machine.village_trading_station.upper_limit")));
                            }
                            textList.add(Component.translatable("gtocore.machine.village_trading_station.replenishment_interval", replenishmentInterval));
                            textList.add(Component.translatable("gtocore.machine.village_trading_station.trading_multiple", tradingMultiple));
                        }).setMaxWidthLimit(120));

                villagerGroup.addWidget(new SlotWidget(enhance, 0, 0, 0)
                        .setBackgroundTexture(GuiTextures.SLOT));

                return villagerGroup;
            }
        });

        // 方向配置页
        sideTabs.attachSubTab(CombinedDirectionalFancyConfigurator.of(this, this));
    }

    private @NotNull WidgetGroup VillagerGroup(int slot, int X) {
        WidgetGroup villagerGroup = new WidgetGroup(X, 0, 18, 128);
        villagerGroup.setLayout(Layout.VERTICAL_CENTER);

        // 锁定按钮
        villagerGroup.addWidget(new ComponentPanelWidget(0, 2,
                (textList) -> textList.add(ComponentPanelWidget.withButton(
                        isLocked(slot) ? Component.literal("\uD83D\uDD12") : Component.literal("\uD83D\uDD13"),
                        String.valueOf(slot))))
                .clickHandler((a, b) -> setLocked(Integer.parseInt(a))));

        // 村民槽位
        villagerGroup.addWidget(new SlotWidget(villagers, slot, 0, 18 - 6, true, true)
                .setBackground(GuiTextures.SLOT));

        // 配方输入1、输入2、输出槽位
        for (int j = 0; j < 3; j++) {
            SlotWidget itemWidget = new SlotWidget(RecipesHandler, 3 * slot + j, 0, 18 * (j + 2) - 6)
                    .setCanPutItems(false).setCanTakeItems(false)
                    .setBackgroundTexture(GTOGuiTextures.VILLAGER_RECIPE_SLOTS[j]);
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
                    VillagerRecipe[] recipes = villagersDataset[slot];
                    if (recipes != null && isLocked(slot) && recipes.length > 0 && selected[slot] < recipes.length) {
                        VillagerRecipe recipe = recipes[selected[slot]];
                        textList.add(Component.literal(String.valueOf(recipe.uses)));
                        textList.add(Component.literal(String.valueOf(recipe.maxUses)));
                    } else {
                        textList.add(Component.literal("0"));
                        textList.add(Component.literal("0"));
                    }
                }));
        return villagerGroup;
    }

    /////////////////////////////////////
    // ********* 辅助类与方法 ********* //
    /////////////////////////////////////

    private static class VillageHolder extends CustomItemStackHandler {

        private final VillageTradingStationMachine machine;

        private VillageHolder(VillageTradingStationMachine machine) {
            super(10);
            this.machine = machine;
            this.setFilter(i -> i.getItem().equals(VILLAGER_ITEM));
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
    }

    // 村民交易配方类
    private static class VillagerRecipe {

        private final ItemStack buy;
        private final ItemStack buyB;
        private final ItemStack sell;
        private int maxUses;
        private int uses;

        private VillagerRecipe(ItemStack buy, ItemStack buyB, ItemStack sell, int maxUses, int uses) {
            this.buy = buy;
            this.buyB = buyB;
            this.sell = sell;
            this.maxUses = maxUses;
            this.uses = uses;
        }
    }

    // 解析村民NBT数据获取交易配方（仅返回配方列表）
    private List<VillagerRecipe> parseTradeData(CompoundTag originalOuterNbt) {
        List<VillagerRecipe> recipes = new ArrayList<>();
        if (!originalOuterNbt.contains("villager", CompoundTag.TAG_COMPOUND)) {
            return recipes;
        }
        CompoundTag villagerCoreNbt = originalOuterNbt.getCompound("villager");

        if (!villagerCoreNbt.contains("Offers", CompoundTag.TAG_COMPOUND)) {
            return recipes;
        }
        CompoundTag offersTag = villagerCoreNbt.getCompound("Offers");

        if (!offersTag.contains("Recipes", ListTag.TAG_LIST)) {
            return recipes;
        }
        ListTag recipesList = offersTag.getList("Recipes", CompoundTag.TAG_COMPOUND);

        for (int i = 0; i < recipesList.size(); i++) {
            CompoundTag recipeTag = recipesList.getCompound(i);
            ItemStack buy = parseItemStack(recipeTag.getCompound("buy"));
            ItemStack buyB = parseItemStack(recipeTag.getCompound("buyB"));
            ItemStack sell = parseItemStack(recipeTag.getCompound("sell"));
            int maxUses = recipeTag.getInt("maxUses");
            int uses = recipeTag.getInt("uses");
            recipes.add(new VillagerRecipe(buy, buyB, sell, maxUses, uses));
        }
        return recipes;
    }

    // 解析NBT为物品栈
    private static ItemStack parseItemStack(CompoundTag itemTag) {
        String itemId = itemTag.getString("id");
        ItemStack item = RegistriesUtils.getItemStack(itemId);
        if (item.getItem().equals(Items.AIR)) return ItemStack.EMPTY;
        int count = Math.max(1, itemTag.getByte("Count"));
        item.setCount(count);
        return item;
    }

    // 村民补货：直接重置所有配方的使用次数（无次数限制）
    private void villagersRestock() {
        for (int slot = 0; slot < 10; slot++) {
            VillagerRecipe[] recipes = villagersDataset[slot];
            if (recipes == null) continue;
            for (VillagerRecipe recipe : recipes) {
                if (recipe != null) recipe.uses = 0;
            }
        }
    }

    // 同步内存中的uses和maxUses到村民物品的NBT数据
    private void syncUsesAndMaxUsesToVillagerItem(int slot) {
        VillagerRecipe[] datasetRecipes = villagersDataset[slot];
        ItemStack villagerStack = villagers.getStackInSlot(slot);
        if (villagerStack.isEmpty() || !villagerStack.getItem().equals(VILLAGER_ITEM) || datasetRecipes == null || datasetRecipes.length == 0) {
            return;
        }
        CompoundTag outerNbt = getCompoundTag(villagerStack, datasetRecipes);
        villagerStack.setTag(outerNbt);
        boolean wasLocked = isLocked[slot];
        if (wasLocked) isLocked[slot] = false;
        villagers.setStackInSlot(slot, villagerStack);
        if (wasLocked) isLocked[slot] = true;
    }

    private static @NotNull CompoundTag getCompoundTag(ItemStack villagerStack, VillagerRecipe[] datasetRecipes) {
        CompoundTag outerNbt = villagerStack.getOrCreateTag();
        CompoundTag villagerCoreNbt = outerNbt.getCompound("villager");
        CompoundTag offersTag = villagerCoreNbt.getCompound("Offers");
        ListTag recipesList = offersTag.getList("Recipes", CompoundTag.TAG_COMPOUND);
        int syncCount = Math.min(datasetRecipes.length, recipesList.size());
        for (int i = 0; i < syncCount; i++) {
            VillagerRecipe datasetRecipe = datasetRecipes[i];
            CompoundTag nbtRecipe = recipesList.getCompound(i);
            nbtRecipe.putInt("uses", datasetRecipe.uses);
            nbtRecipe.putInt("maxUses", datasetRecipe.maxUses);
            recipesList.set(i, nbtRecipe);
        }
        offersTag.put("Recipes", recipesList);
        villagerCoreNbt.put("Offers", offersTag);
        outerNbt.put("villager", villagerCoreNbt);
        return outerNbt;
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
            autoOutputSubs = subscribeServerTick(autoOutputSubs, this::autoOutput, 20);
        } else if (autoOutputSubs != null) {
            autoOutputSubs.unsubscribe();
            autoOutputSubs = null;
        }
    }

    private void autoOutput() {
        if (autoOutputItems && getOutputFacingItems() != null) {
            output.exportToNearby(getOutputFacingItems());
        }
        updateAutoOutputSubscription();
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(input.storage);
        clearInventory(output.storage);
        clearInventory(villagers);
        clearInventory(upgrade);
        clearInventory(enhance);
    }
}
