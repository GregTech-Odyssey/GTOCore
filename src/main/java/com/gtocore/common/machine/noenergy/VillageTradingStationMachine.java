package com.gtocore.common.machine.noenergy;

import com.gtocore.common.data.translation.GTOMachineTooltips;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
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
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
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

    @Persisted
    private VillageHolder villagers;
    @Persisted
    private boolean[] isLocked = new boolean[9];
    @Persisted
    private int[] selected = new int[9];
    @Persisted
    private boolean[] startUp = new boolean[9];

    private final VillagerTradeRecord[] VillagersDataset = new VillagerTradeRecord[9];
    private final ItemStackHandler RecipesHandler = new ItemStackHandler(3 * 9);

    /////////////////////////////////////
    // *********** 核心方法 *********** //
    /////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
        }
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

    private void tickUpdate() {
        if (getOffsetTimer() % 100 == 0 && !isRemote()) {
            getRecipeLogic().updateTickSubscription();
        }
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    private Recipe getRecipe() {
        return null;
    }

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

    private void incomingVillagersDataset(int slot) {
        if (!villagers.getStackInSlot(slot).isEmpty() && isLocked[slot]) {
            VillagersDataset[slot] = parseTradeData(villagers.getStackInSlot(slot).getOrCreateTag());
        } else {
            VillagersDataset[slot] = null;
        }
    }

    private void selectedRecipes(int slot) {
        VillagerTradeRecord VillagersData = VillagersDataset[slot];
        if (VillagersData != null && isLocked(slot)) {
            List<VillagerRecipe> recipes = VillagersData.recipes;
            if (!recipes.isEmpty()) {
                VillagerRecipe recipe = recipes.get(selected[slot]);
                RecipesHandler.setStackInSlot(slot * 3, recipe.buy);
                RecipesHandler.setStackInSlot(slot * 3 + 1, recipe.buyB);
                RecipesHandler.setStackInSlot(slot * 3 + 2, recipe.sell);
            } else {
                RecipesHandler.setStackInSlot(slot * 3, ItemStack.EMPTY);
                RecipesHandler.setStackInSlot(slot * 3 + 1, ItemStack.EMPTY);
                RecipesHandler.setStackInSlot(slot * 3 + 2, ItemStack.EMPTY);
            }
        } else {
            RecipesHandler.setStackInSlot(slot * 3, ItemStack.EMPTY);
            RecipesHandler.setStackInSlot(slot * 3 + 1, ItemStack.EMPTY);
            RecipesHandler.setStackInSlot(slot * 3 + 2, ItemStack.EMPTY);
        }
    }

    private void selectedNext(int slot) {
        VillagerTradeRecord VillagersData = VillagersDataset[slot];
        if (!isStartUp(slot) && VillagersData != null && !VillagersData.recipes.isEmpty()) {
            if (selected[slot] < VillagersData.recipes.size() - 1) {
                selected[slot]++;
            } else {
                selected[slot] = 0;
            }
            selectedRecipes(slot);
        }
    }

    private boolean isStartUp(int slot) {
        return startUp[slot];
    }

    private void setStartUp(int slot) {
        if (isLocked(slot)) {
            startUp[slot] = !startUp[slot];
        } else {
            startUp[slot] = false;
        }
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

                group.setBackground(GuiTextures.BACKGROUND_INVERSE);
                return group;
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
                    VillagerGroup.addWidget(new ComponentPanelWidget(0, 2,
                            (textList) -> textList.add(ComponentPanelWidget.withButton(isLocked(slotIndex) ?
                                    Component.literal("\uD83D\uDD12") : Component.literal("\uD83D\uDD13"), String.valueOf(slotIndex))))
                            .clickHandler((a, b) -> setLocked(Integer.parseInt(a))));
                    VillagerGroup.addWidget(new SlotWidget(villagers, i, 0, 18 - 6, true, true)
                            .setBackground(GuiTextures.SLOT));

                    for (int j = 0; j < 3; j++) {
                        SlotWidget itemWidget = new SlotWidget(RecipesHandler, 3 * i + j, 0, 18 * (j + 2) - 6)
                                .setCanPutItems(false).setCanTakeItems(false)
                                .setBackgroundTexture(new ResourceTexture(GTOCore.id("textures/gui/villager_recipe_slot_" + j + ".png")));
                        VillagerGroup.addWidget(itemWidget);
                    }

                    VillagerGroup.addWidget(new ComponentPanelWidget(0, 2 + 18 * 5 - 6,
                            (textList) -> textList.add(ComponentPanelWidget.withButton(
                                    Component.literal("\uD83D\uDD01"), String.valueOf(slotIndex))))
                            .clickHandler((a, b) -> selectedNext(Integer.parseInt(a))));

                    VillagerGroup.addWidget(new ComponentPanelWidget(0, 2 + 18 * 5 + 6,
                            (textList) -> textList.add(ComponentPanelWidget.withButton(isStartUp(slotIndex) ?
                                    Component.literal("\uD83D\uDD12") : Component.literal("\uD83D\uDD13"), String.valueOf(slotIndex))))
                            .clickHandler((a, b) -> setStartUp(Integer.parseInt(a))));

                    VillagerGroup.addWidget(new ComponentPanelWidget(0, 2 + 18 * 5 + 18,
                            (textList) -> {
                                int uses = 0;
                                int maxUses = 0;
                                VillagerTradeRecord VillagersData = VillagersDataset[slotIndex];
                                if (VillagersData != null && isLocked(slotIndex)) {
                                    List<VillagerRecipe> recipes = VillagersData.recipes;
                                    if (!recipes.isEmpty()) {
                                        VillagerRecipe recipe = recipes.get(selected[slotIndex]);
                                        uses = recipe.uses;
                                        maxUses = recipe.maxUses;
                                    }
                                }
                                textList.add(Component.literal(String.valueOf(uses)));
                                textList.add(Component.literal(String.valueOf(maxUses)));
                            }));

                    group.addWidget(VillagerGroup);
                }
                return group;
            }

            private void addDisplayTextTitle(List<Component> textList) {
                int lockedCount = 0;
                for (int i = 0; i < 9; i++) if (isLocked(i)) lockedCount++;
                textList.add(Component.literal(String.valueOf(lockedCount)));
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

        sideTabs.attachSubTab(CombinedDirectionalFancyConfigurator.of(this, this));
    }

    /////////////////////////////////////
    // ************ 辅助方法 ************ //
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

    private record VillagerTradeRecord(
                                       int restocksToday,
                                       int maxRestocksToday,
                                       List<VillagerRecipe> recipes) {}

    private record VillagerRecipe(
                                  ItemStack buy,
                                  ItemStack buyB,
                                  ItemStack sell,
                                  int maxUses,
                                  int uses) {}

    private static VillagerTradeRecord parseTradeData(CompoundTag originalOuterNbt) {
        if (!originalOuterNbt.contains("villager", 10)) {
            return new VillagerTradeRecord(0, 2, new ArrayList<>());
        }
        CompoundTag villagerCoreNbt = originalOuterNbt.getCompound("villager");

        int restocksToday = villagerCoreNbt.getInt("RestocksToday");
        int maxRestocksToday = villagerCoreNbt.contains("MaxRestocksToday", 3) ? villagerCoreNbt.getInt("MaxRestocksToday") : 2; // 默认2次

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

    private static ItemStack parseItemStack(CompoundTag itemTag) {
        String itemId = itemTag.getString("id");
        ItemStack item = RegistriesUtils.getItemStack(itemId);
        if (item.getItem().equals(Items.BARRIER)) return ItemStack.EMPTY;
        int count = Math.max(1, itemTag.getByte("Count"));
        item.setCount(count);
        return item;
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
