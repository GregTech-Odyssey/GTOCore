package com.gtocore.common.machine.noenergy;

import com.gtocore.common.data.translation.GTOMachineTooltips;

import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

import static com.gregtechceu.gtceu.api.GTValues.HV;

public class VillageTradingStationMachine extends SimpleNoEnergyMachine {

    public VillageTradingStationMachine(MetaMachineBlockEntity holder) {
        super(holder, HV, i -> 0);
    }

    /////////////////////////////////////
    // *********** 信息存储 *********** //
    /////////////////////////////////////

    private TickableSubscription tickSubs;

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
        if (getOffsetTimer() % 100 == 0) {
            getRecipeLogic().updateTickSubscription();
            // 控制补货操作
            // 运行交易逻辑
        }
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    private Recipe getRecipe() {
        return null;
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

            @Override
            public IGuiTexture getTabIcon() {
                return new ItemStackTexture(self().getDefinition().asItem());
            }

            @Override
            public Component getTitle() {
                return Component.translatable(self().getDefinition().getDescriptionId());
            }
        });

        sideTabs.attachSubTab(new IFancyUIProvider() {

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

            @Override
            public IGuiTexture getTabIcon() {
                return IGuiTexture.EMPTY;
            }

            @Override
            public Component getTitle() {
                return Component.empty();
            }
        });

        CombinedDirectionalFancyConfigurator directionalConfigurator = CombinedDirectionalFancyConfigurator.of(this.self(), this.self());
        if (directionalConfigurator != null) {
            sideTabs.attachSubTab(directionalConfigurator);
        }
    }

    /////////////////////////////////////
    // *********** 辅助方法 *********** //
    /////////////////////////////////////
}
