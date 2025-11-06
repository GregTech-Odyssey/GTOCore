package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;

import java.awt.*;

public class PalmSizedBankBehavior implements IItemUIFactory, IFancyUIProvider {

    public static final PalmSizedBankBehavior INSTANCE = new PalmSizedBankBehavior();

    private static final String TEXT_HEADER = "gtocore.palm_sized_bank.textList.";

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        openUI(itemStack.getItem(), context.getLevel(), player, context.getHand());
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        ItemStack heldItem = player.getItemInHand(usedHand);
        openUI(item, level, player, usedHand);
        return InteractionResultHolder.success(heldItem);
    }

    @Override
    public ModularUI createUI(HeldItemUIFactory.HeldItemHolder holder, Player player) {
        return new ModularUI(176, 166, holder, player)
                .widget(new FancyMachineUIWidget(this, 176, 166));
    }

    // 重写：构建主页面UI（抽取子方法，分离UI创建逻辑）
    @Override
    public Widget createMainPage(FancyMachineUIWidget widget) {
        final int width = 192;
        final int height = 144;
        WidgetGroup group = new WidgetGroup(0, 0, width + 8, height + 8);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);

        DraggableScrollableWidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                .setBackground(GuiTextures.DISPLAY); // 内容区域背景

        group.addWidget(mainGroup);

        // 2. 获取玩家实例并处理异常
        Player player = getPlayerFromWidget(widget);

        mainGroup.addWidget(new ComponentPanelWidget(10, 10, textList -> {
            textList.add(Component.translatable(TEXT_HEADER + 1));
            textList.add(Component.translatable(TEXT_HEADER + 2));
            textList.add(Component.translatable(TEXT_HEADER + 3));
            textList.add(Component.translatable(TEXT_HEADER + 4));
            textList.add(Component.empty());
            if (player == null) {
                textList.add(Component.translatable(TEXT_HEADER + 5).withStyle(ChatFormatting.RED));
            } else {
                textList.add(Component.translatable(TEXT_HEADER + 6, player.getName().getString()).withStyle(ChatFormatting.WHITE));
                textList.add(Component.translatable(TEXT_HEADER + 7, player.getUUID().toString()));
            }
        }).setMaxWidthLimit(width - 20));

        return group;
    }

    // 重写：标签图标（明确返回类型）
    @Override
    public IGuiTexture getTabIcon() {
        return new ItemStackTexture(GTOItems.PALM_SIZED_BANK.asItem());
    }

    // 重写：标题（直接关联物品显示名）
    @Override
    public Component getTitle() {
        return GTOItems.PALM_SIZED_BANK.asStack().getDisplayName();
    }

    // 重写：侧边标签（仅保留主标签）
    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);
    }

    // ------------------------------
    // 以下为抽取的辅助方法（重写核心逻辑的关键）
    // ------------------------------

    /** 打开UI的统一方法（避免代码重复） */
    private void openUI(Item item, Level level, Player player, InteractionHand hand) {
        IItemUIFactory.super.use(item, level, player, hand);
    }

    /** 从Widget中获取玩家实例（封装获取逻辑，便于维护） */
    private Player getPlayerFromWidget(FancyMachineUIWidget widget) {
        ModularUI modularUI = widget.getGui();
        if (modularUI == null) return null;
        return modularUI.entityPlayer;
    }
}
