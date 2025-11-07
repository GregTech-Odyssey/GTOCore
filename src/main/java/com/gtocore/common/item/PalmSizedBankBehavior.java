package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;
import com.gregtechceu.gtceu.utils.collection.O2LOpenCacheHashMap;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PalmSizedBankBehavior implements IItemUIFactory, IFancyUIProvider {

    public static final PalmSizedBankBehavior INSTANCE = new PalmSizedBankBehavior();

    private static final String TEXT_HEADER = "gtocore.palm_sized_bank.textList.";

    @Getter
    @DescSynced
    private Object2LongMap<String> currencyMap = new Object2LongOpenHashMap<>();

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
                .setBackground(GuiTextures.DISPLAY);

        group.addWidget(mainGroup);

        Player player = getPlayerFromWidget(widget);
        if (player == null) return group;

        boolean hasWallet;
        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = serverPlayer.serverLevel();
            hasWallet = WalletUtils.hasWallet(player.getUUID(), serverLevel);
        } else {
            hasWallet = false;
        }

        List<Component> list = new ArrayList<>();
        list.add(Component.translatable(text(1)));
        list.add(Component.translatable(text(2)));
        list.add(Component.translatable(text(3)));
        list.add(Component.translatable(text(4)));
        list.add(Component.empty());

        if (hasWallet) {
            list.add(Component.translatable(text(6), player.getName().getString()).withStyle(ChatFormatting.WHITE));
            list.add(Component.translatable(text(7), player.getUUID().toString()));
            mainGroup.addWidget(new ComponentPanelWidget(10, 10, list).setMaxWidthLimit(width - 20));
        } else {
            list.add(ComponentPanelWidget.withButton(Component.translatable(text(8)), "Create a wallet"));
            mainGroup.addWidget(new ComponentPanelWidget(10, 10, list).clickHandler((a, b) -> {
                if (player instanceof ServerPlayer serverPlayer) {
                    ServerLevel serverLevel = serverPlayer.serverLevel();
                    WalletUtils.createAndInitializeWallet(player.getUUID(), player.getName().getString(), serverLevel);
                    initNewPlayerCurrencies(player.getUUID(), serverLevel);
                }
            }).setMaxWidthLimit(width - 20));
        }

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

    private IFancyUIProvider assetOverview(PalmSizedBankBehavior parentBehavior) {
        return new IFancyUIProvider() {

            @Override
            public IGuiTexture getTabIcon() {
                return GuiTextures.GREGTECH_LOGO;
            }

            @Override
            public Component getTitle() {
                return Component.translatable(text(10));
            }

            final int width = 256;
            final int height = 144;

            @Override
            public Widget createMainPage(FancyMachineUIWidget widget) {
                var group = new WidgetGroup(0, 0, width + 8, height + 8);

                WidgetGroup mainGroup = new DraggableScrollableWidgetGroup(4, 4, width, height)
                        .setBackground(GuiTextures.DISPLAY);

                Player player = getPlayerFromWidget(widget);
                if (player == null) return group;

                addCurrencyRow(mainGroup, parentBehavior);

                group.addWidget(mainGroup);
                group.setBackground(GuiTextures.BACKGROUND_INVERSE);

                return group;
            }

            private void addCurrencyRow(WidgetGroup mainGroup, PalmSizedBankBehavior behavior) {
                Object2LongMap<String> syncedCurrencyMap = behavior.currencyMap;
                List<Component> currencyComponents = new ArrayList<>();
                List<Component> amountComponents = new ArrayList<>();
                for (Object2LongMap.Entry<String> entry : syncedCurrencyMap.object2LongEntrySet()) {
                    currencyComponents.add(Component.translatable("gtocore.bank.currency." + entry.getKey()));
                    amountComponents.add(Component.literal(Long.toString(entry.getLongValue())));
                }
                mainGroup.addWidget(new LabelWidget(8, 8, Component.translatable(text(11))));
                mainGroup.addWidget(new LabelWidget(width / 2 + 4, 8, Component.translatable(text(12))));
                mainGroup.addWidget(new ComponentPanelWidget(8, 24, currencyComponents));
                mainGroup.addWidget(new ComponentPanelWidget(width / 2 + 4, 24, amountComponents));
            }
        };
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);
        sideTabs.attachSubTab(assetOverview(this));
    }

    public static void initNewPlayerCurrencies(UUID playerUUID, ServerLevel world) {
        O2LOpenCacheHashMap<String> initialCurrencies = new O2LOpenCacheHashMap<>();
        initialCurrencies.put("coins", 9200000000000000000L);
        initialCurrencies.put("gems", 10);
        initialCurrencies.put("tokens", 50);
        initialCurrencies.put("aaa", 50);
        initialCurrencies.put("bbb", 500);
        initialCurrencies.put("ccc", 5000);
        initialCurrencies.put("ddd", 50);
        initialCurrencies.put("eee", 50);
        initialCurrencies.put("fff", 50);
        initialCurrencies.put("ggg", 50);
        initialCurrencies.put("hhh", 50);
        initialCurrencies.put("iii", 50);
        initialCurrencies.put("jjj", 5000000000000000000L);
        WalletUtils.setCurrencies(playerUUID, world, initialCurrencies);
    }

    // 辅助方法
    private void openUI(Item item, Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = serverPlayer.serverLevel();
            updateCurrencyMap(WalletUtils.getCurrencyMap(player.getUUID(), serverLevel));
        }
        IItemUIFactory.super.use(item, level, player, hand);
    }

    private void updateCurrencyMap(Object2LongMap<String> newCurrencyMap) {
        this.currencyMap.clear();
        if (newCurrencyMap != null) {
            this.currencyMap.putAll(newCurrencyMap);
        }
    }

    private static Player getPlayerFromWidget(FancyMachineUIWidget widget) {
        ModularUI modularUI = widget.getGui();
        return (modularUI != null) ? modularUI.entityPlayer : null;
    }
}
