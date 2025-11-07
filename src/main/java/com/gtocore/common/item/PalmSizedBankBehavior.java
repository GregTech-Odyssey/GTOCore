package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import com.gtolib.utils.WalletUtils;
import com.gtolib.utils.holder.IntHolder;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.gtolib.utils.WalletUtils.createAndInitializeWallet;

public class PalmSizedBankBehavior implements IItemUIFactory, IFancyUIProvider {

    public static final PalmSizedBankBehavior INSTANCE = new PalmSizedBankBehavior();

    private static final String TEXT_HEADER = "gtocore.palm_sized_bank.textList.";

    private static @NotNull String text(int id) {
        return TEXT_HEADER + id;
    }

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

        if (player == null) return group;

        boolean hasWallet = WalletUtils.hasWallet(player.getUUID(), (ServerLevel) player.level());

        mainGroup.addWidget(new ComponentPanelWidget(10, 10, textList -> {
            textList.add(Component.translatable(text(1)));
            textList.add(Component.translatable(text(2)));
            textList.add(Component.translatable(text(3)));
            textList.add(Component.translatable(text(4)));
            textList.add(Component.empty());
            if (hasWallet) {
                textList.add(Component.translatable(text(6), player.getName().getString()).withStyle(ChatFormatting.WHITE));
                textList.add(Component.translatable(text(7), player.getUUID().toString()));
            } else {
                textList.add(ComponentPanelWidget.withButton(Component.translatable(text(8)), "Create a wallet"));
                textList.add(Component.translatable(text(8)).withStyle(ChatFormatting.RED));
            }
        }).clickHandler((a, b) -> {
            WalletUtils.createAndInitializeWallet(player.getUUID(), player.getName().getString(), (ServerLevel) player.level());
            initNewPlayerCurrencies(player.getUUID(), (ServerLevel) player.level());
        }).setMaxWidthLimit(width - 20));

        return group;
    }

    @Override
    public IGuiTexture getTabIcon() {
        return new ItemStackTexture(GTOItems.PALM_SIZED_BANK.asItem());
    }

    @Override
    public Component getTitle() {
        return GTOItems.PALM_SIZED_BANK.asStack().getDisplayName();
    }

    private static IFancyUIProvider assetOverview() {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return Component.translatable(text(10));
            }

            final int width = 336;
            final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);
                group.addWidget(mainGroup);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;
                if (!WalletUtils.hasWallet(player.getUUID(), (ServerLevel) player.level())) {
                    mainGroup.addWidget(new LabelWidget(10, 10, Component.literal(text(9))));
                    return group;
                }

                WidgetGroup CurrencyGroup = new WidgetGroup(8, 8, width / 2 - 8, 512);
                WidgetGroup AmountGroup = new WidgetGroup(width / 2, 8, width / 2 - 8, 512);
                CurrencyGroup.setLayout(Layout.VERTICAL_CENTER);
                AmountGroup.setLayout(Layout.VERTICAL_CENTER);
                mainGroup.addWidget(CurrencyGroup);
                mainGroup.addWidget(AmountGroup);

                IntHolder yPosition = new IntHolder(0);
                CurrencyGroup.addWidget(new LabelWidget(0, yPosition.value, Component.translatable(text(11))));
                AmountGroup.addWidget(new LabelWidget(0, yPosition.value, Component.translatable(text(12))));

                WalletUtils.getCurrencyMap(player.getUUID(), (ServerLevel) player.level()).forEach((currency, amount) -> {
                    yPosition.value += 12;
                    CurrencyGroup.addWidget(new LabelWidget(0, yPosition.value, Component.translatable("gtocore.bank.currency." + currency)));
                    AmountGroup.addWidget(new LabelWidget(0, yPosition.value, Component.literal(amount.toString())));
                });

                return group;
            }
        };
    }

    // 重写：侧边标签（仅保留主标签）
    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);

        sideTabs.attachSubTab(assetOverview());
    }

    public static void initNewPlayerCurrencies(UUID playerUUID, ServerLevel world) {
        Object2LongOpenHashMap<String> initialCurrencies = new Object2LongOpenHashMap<>();

        initialCurrencies.put("coins", 100);       // 初始金币100
        initialCurrencies.put("gems", 10);         // 初始宝石10
        initialCurrencies.put("tokens", 50);       // 初始代币50

        WalletUtils.setCurrencies(playerUUID, world, initialCurrencies);
    }

    // ------------------------------
    // 以下为抽取的辅助方法（重写核心逻辑的关键）
    // ------------------------------

    /** 打开UI的统一方法（避免代码重复） */
    private void openUI(Item item, Level level, Player player, InteractionHand hand) {
        IItemUIFactory.super.use(item, level, player, hand);
    }

    /** 从Widget中获取玩家实例（封装获取逻辑，便于维护） */
    private static Player getPlayerFromWidget(FancyMachineUIWidget widget) {
        ModularUI modularUI = widget.getGui();
        if (modularUI == null) return null;
        return modularUI.entityPlayer;
    }
}
