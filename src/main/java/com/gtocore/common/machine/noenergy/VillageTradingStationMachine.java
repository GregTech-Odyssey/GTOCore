package com.gtocore.common.machine.noenergy;

import com.gtocore.common.data.translation.GTOMachineTooltips;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IAutoOutputItem;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VillageTradingStationMachine extends MetaMachine implements IAutoOutputItem, IFancyUIMachine {

    // 固定缓冲区槽位数量：9格
    private static final int BUFFER_SLOT_COUNT = 9;

    public VillageTradingStationMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.setAutoOutputItems(true);

        importItems = createImportItemHandler();
        exportItems = createExportItemHandler();
    }

    /////////////////////////////////////
    // *********** 信息存储 *********** //
    /////////////////////////////////////

    private TickableSubscription tickSubs;

    @Persisted
    private final NotifiableItemStackHandler importItems;
    @Persisted
    private final NotifiableItemStackHandler exportItems;

    /////////////////////////////////////
    // *********** 配方运行 *********** //
    /////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
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

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                int width = 256;
                int height = 144;
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup group_title = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);
                group_title.addWidget(new ComponentPanelWidget(4, 4, this::addDisplayTextTitle));
                group.addWidget(group_title);

                group.setBackground(GuiTextures.BACKGROUND_INVERSE);
                return group;
            }

            private void addDisplayTextTitle(List<Component> textList) {
                textList.add(Component.literal("输入物品"));
                importItems.forEachItems((index, stack) -> {
                    textList.add(Component.empty().append(index.getDisplayName()).append(String.valueOf(index.getCount())));
                    return false;
                });
                textList.add(Component.literal("输出物品"));
                exportItems.forEachItems((index, stack) -> {
                    textList.add(Component.empty().append(index.getDisplayName()).append(String.valueOf(index.getCount())));
                    return false;
                });
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

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                int width = 256;
                int height = 144;
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup group_title = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                group.addWidget(group_title);

                group.setBackground(GuiTextures.BACKGROUND_INVERSE);
                return group;
            }
        });

        CombinedDirectionalFancyConfigurator directionalConfigurator = CombinedDirectionalFancyConfigurator.of(this.self(), this.self());
        if (directionalConfigurator != null) {
            sideTabs.attachSubTab(directionalConfigurator);
        }
    }

    /////////////////////////////////////
    // *********** 实现方法 *********** //
    /////////////////////////////////////

    public boolean isAutoOutputItems() {
        return true;
    }

    public void setAllowInputFromOutputSideItems(final boolean allowInputFromOutputSideItems) {}

    public boolean isAllowInputFromOutputSideItems() {
        return true;
    }

    @Override
    public void setAutoOutputItems(boolean allow) {}

    @Override
    @Nullable
    public Direction getOutputFacingItems() {
        return Direction.DOWN;
    }

    @Override
    public void setOutputFacingItems(@Nullable Direction outputFacing) {}

    /////////////////////////////////////
    // *********** 缓冲区配置 *********** //
    /////////////////////////////////////

    protected NotifiableItemStackHandler createImportItemHandler() {
        return new NotifiableItemStackHandler(this, BUFFER_SLOT_COUNT, IO.IN) {

            @Override
            public int getSlotLimit(int slot) {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return !(stack.hasTag() && stack.is(com.hepdd.gtmthings.data.CustomItems.VIRTUAL_ITEM_PROVIDER.get()));
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (stack.isEmpty() || slot < 0 || slot >= getSlots()) {
                    return stack;
                }

                if (!isItemValid(slot, stack)) {
                    return stack;
                }

                ItemStack existing = getStackInSlot(slot);
                int maxInsert = getSlotLimit(slot) - existing.getCount();

                if (maxInsert <= 0) {
                    return stack;
                }

                if (!existing.isEmpty() && !ItemStack.isSameItemSameTags(existing, stack)) {
                    return stack;
                }

                int insertAmount = Math.min(stack.getCount(), maxInsert);

                if (!simulate) {
                    if (existing.isEmpty()) {
                        ItemStack newStack = stack.copy();
                        newStack.setCount(insertAmount);
                        setStackInSlot(slot, newStack);
                    } else {
                        existing.grow(insertAmount);
                    }
                    onContentsChanged();
                }

                ItemStack remaining = stack.copy();
                remaining.setCount(stack.getCount() - insertAmount);
                return remaining.isEmpty() ? ItemStack.EMPTY : remaining;
            }
        };
    }

    protected NotifiableItemStackHandler createExportItemHandler() {
        return new NotifiableItemStackHandler(this, BUFFER_SLOT_COUNT, IO.OUT) {

            @Override
            public int getSlotLimit(int slot) {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return true;
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (stack.isEmpty() || slot < 0 || slot >= getSlots()) {
                    return stack;
                }

                if (!isItemValid(slot, stack)) {
                    return stack;
                }

                ItemStack existing = getStackInSlot(slot);
                int maxInsert = getSlotLimit(slot) - existing.getCount();

                if (maxInsert <= 0) {
                    return stack;
                }

                if (!existing.isEmpty() && !ItemStack.isSameItemSameTags(existing, stack)) {
                    return stack;
                }

                int insertAmount = Math.min(stack.getCount(), maxInsert);

                if (!simulate) {
                    if (existing.isEmpty()) {
                        ItemStack newStack = stack.copy();
                        newStack.setCount(insertAmount);
                        setStackInSlot(slot, newStack);
                    } else {
                        existing.grow(insertAmount);
                    }
                    onContentsChanged();
                }

                ItemStack remaining = stack.copy();
                remaining.setCount(stack.getCount() - insertAmount);
                return remaining.isEmpty() ? ItemStack.EMPTY : remaining;
            }
        };
    }
}
