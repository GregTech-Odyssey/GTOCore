package com.gtocore.data.transaction.data.trade;

import com.gtocore.api.gui.StackTexture;
import com.gtocore.common.data.GTOItems;
import com.gtocore.data.transaction.manager.TradeEntry;
import com.gtocore.data.transaction.manager.TradingManager;

import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import vazkii.botania.common.item.BotaniaItems;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.LV;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.block;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.tag.GTOTagPrefix.COIN;
import static com.gtocore.data.transaction.data.GTOTrade.*;
import static com.gtocore.data.transaction.data.TradeLang.TECH_OPERATOR_COIN;
import static com.gtocore.data.transaction.data.TradeLang.addTradeLang;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.UNLOCK_BASE;
import static com.gtocore.utils.PlayerHeadUtils.itemStackAddNbtString;

public final class WelfareGroup {

    /**
     * 员工福利兑换中心
     * <p>
     * - 福利兑换 壹
     * - 福利兑换 贰
     */
    public static void init() {
        int GroupIndex = TradingManager.INSTANCE.addShopGroup(
                addTradeLang("员工福利兑换中心", "Employee Benefits Redemption Center"),
                GuiTextures.GREGTECH_LOGO,
                GuiTextures.GREGTECH_LOGO);

        int ShopIndex1 = TradingManager.INSTANCE.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("格雷科技™员工会员店", "Gray Tech™ Employee Membership Store"),
                UNLOCK_BASE,
                Set.of(TECH_OPERATOR_COIN),
                GuiTextures.GREGTECH_LOGO);

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                new TradeEntry.Builder()
                        .texture(new StackTexture(BotaniaItems.manaCookie))
                        .addDescription(Component.translatable(addTradeLang(
                                "欢迎来到格雷科技™员工会员专卖店",
                                "Welcome to the Grey Tech™ Employee Membership Store")))
                        .addDescription(Component.translatable(addTradeLang("希望你喜欢", "Hope you like it")))
                        .outputItem(new ItemStack(BotaniaItems.manaCookie))
                        .outputItem(ChemicalHelper.get(ingot, Bronze, 64))
                        .preCheck((a, b) -> checkTag(a, b, "Welcome to the Grey Tech™ Employee Membership Store"))
                        .onExecute((a, b, c) -> performTag(a, b, c, "Welcome to the Grey Tech™ Employee Membership Store"))
                        .build());

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, new ItemStack(BotaniaItems.manaCookie, 64), TECH_OPERATOR_COIN, 16));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                SimpleLotteryTrading(UNLOCK_BASE, TECH_OPERATOR_COIN, 32,
                        List.of(Component.translatable(addTradeLang("切勿沉迷", "Avoid excessive indulgence"))),
                        List.of(lotteryItem(1, ChemicalHelper.get(COIN, Copper, 6400)),
                                lotteryItem(10, ChemicalHelper.get(COIN, Copper, 640)),
                                lotteryItem(500, ChemicalHelper.get(COIN, Copper, 64)),
                                lotteryItem(1000, ChemicalHelper.get(COIN, Copper, 32)),
                                lotteryItem(8500, ChemicalHelper.get(COIN, Copper, 16)))));

        {
            ItemStack stack = itemStackAddNbtString(GTOItems.PROSPECTOR_MANA_ULV.asStack(), "{mana:2000000}");
            TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                    simpleItemTrading(true, UNLOCK_BASE, stack, TECH_OPERATOR_COIN, 16));

            TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                    new TradeEntry.Builder()
                            .texture(new StackTexture(stack))
                            .addDescription(Component.translatable(addTradeLang(
                                    "建议完全用完了魔力再来这里兑换，当然如果你有自己的植物魔法产线就最好了",
                                    "It is recommended to redeem rewards here only after using up all your mana. Of course, it would be best if you have your own plant magic production line")))
                            .inputItem(GTOItems.PROSPECTOR_MANA_ULV.asStack())
                            .inputItem(ChemicalHelper.get(ingot, Bronze, 32))
                            .outputItem(stack)
                            .build());
        }

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, ChemicalHelper.get(block, Bronze, 16), TECH_OPERATOR_COIN, 64));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, RegistriesUtils.getItemStack("functionalstorage:fluid_1"), TECH_OPERATOR_COIN, 32));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, GTOItems.PRECISION_STEAM_MECHANISM.asStack(), TECH_OPERATOR_COIN, 128));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, GTItems.STICKY_RESIN.asStack(16), TECH_OPERATOR_COIN, 4));

        {
            ItemStack stack = itemStackAddNbtString(AEItems.PORTABLE_ITEM_CELL16K.stack(), "{internalCurrentPower:20000.0d}");
            TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                    simpleItemTrading(true, UNLOCK_BASE, stack, TECH_OPERATOR_COIN, 128));
        }

        {
            ItemStack stack = itemStackAddNbtString(AEItems.PORTABLE_FLUID_CELL16K.stack(), "{internalCurrentPower:20000.0d}");
            TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                    simpleItemTrading(true, UNLOCK_BASE, stack, TECH_OPERATOR_COIN, 128));

        }
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, GTMachines.SUPER_TANK[LV].asStack(), TECH_OPERATOR_COIN, 32));

        {
            ItemStack stack = itemStackAddNbtString(Objects.requireNonNull(GTMaterialItems.TOOL_ITEMS.get(DamascusSteel, GTToolType.MINING_HAMMER)).asStack(),
                    "{DisallowContainerItem:0b,Enchantments:[{id:\"minecraft:unbreaking\",lvl:3s}],GT.Behaviours:{AoEColumn:1,AoELayer:0,AoERow:1},GT.Tool:{Damage:0},HideFlags:2}");
            TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                    simpleItemTrading(true, UNLOCK_BASE, stack, TECH_OPERATOR_COIN, 48));
        }

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                SimpleLotteryTrading(UNLOCK_BASE, TECH_OPERATOR_COIN, 16,
                        List.of(Component.translatable(addTradeLang(
                                "喝了可乐之后在小机器上跳来跳去可以加速小机器工作",
                                "Jumping up and down on the small machine after drinking Coke can speed up its operation"))),
                        List.of(lotteryItem(25, GTOItems.MYSTERIOUS_BOOST_DRINK[2].asStack()),
                                lotteryItem(75, GTOItems.MYSTERIOUS_BOOST_DRINK[1].asStack()),
                                lotteryItem(900, GTOItems.MYSTERIOUS_BOOST_DRINK[0].asStack()))));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                SimpleLotteryTrading(UNLOCK_BASE, TECH_OPERATOR_COIN, 1024,
                        List.of(Component.translatable(addTradeLang(
                                "喝了可乐之后在小机器上跳来跳去可以加速小机器工作",
                                "Jumping up and down on the small machine after drinking Coke can speed up its operation"))),
                        List.of(lotteryItem(25, GTOItems.MYSTERIOUS_BOOST_DRINK[5].asStack()),
                                lotteryItem(75, GTOItems.MYSTERIOUS_BOOST_DRINK[4].asStack()),
                                lotteryItem(900, GTOItems.MYSTERIOUS_BOOST_DRINK[3].asStack()))));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                SimpleLotteryTrading(UNLOCK_BASE, TECH_OPERATOR_COIN, 16,
                        List.of(Component.translatable(addTradeLang("炼金大师套装", "Alchemy Master Set"))),
                        List.of(lotteryItem(50, new ItemStack(Blocks.FURNACE, 27)),
                                lotteryItem(50, RegistriesUtils.getItemStack("jumbofurnace:jumbo_furnace")),
                                lotteryItem(50, new ItemStack(Items.COAL, 128)))));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, ChemicalHelper.get(ingot, Steel, 32), TECH_OPERATOR_COIN, 128));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, Items.ENDER_EYE.getDefaultInstance(), TECH_OPERATOR_COIN, 8));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, new ItemStack(Blocks.CLAY, 64), TECH_OPERATOR_COIN, 8));

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                simpleItemTrading(true, UNLOCK_BASE, Adventure.Items.MYTHIC_MATERIAL.get().getDefaultInstance(), TECH_OPERATOR_COIN, 64));

        {
            ItemStack stack = RegistriesUtils.getItemStack("constructionwand:infinity_wand");
            TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                    new TradeEntry.Builder()
                            .texture(new StackTexture(stack))
                            .addDescription(Component.translatable(addTradeLang("建筑大师套装", "Master Builder Set")))
                            .addDescription(Component.translatable(addTradeLang("成为建筑大师的必经之路", "The essential path to becoming an architectural master")))
                            .inputCurrency(TECH_OPERATOR_COIN, 128)
                            .outputItem(stack)
                            .outputItem(RegistriesUtils.getItemStack("constructionwand:core_angel"))
                            .build());
        }

        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, RegistriesUtils.getItemStack("factory_blocks:factory", 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.WHITE_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.ORANGE_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.MAGENTA_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.LIGHT_BLUE_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.YELLOW_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.LIME_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.PINK_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.GRAY_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.LIGHT_GRAY_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.CYAN_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.PURPLE_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.BLUE_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.BROWN_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.GREEN_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.RED_CONCRETE, 64)));
        TradingManager.INSTANCE.addTradeEntryByIndices(GroupIndex, ShopIndex1,
                freeItemTrading(UNLOCK_BASE, new ItemStack(Items.BLACK_CONCRETE, 64)));

        int ShopIndex2 = TradingManager.INSTANCE.addShopByGroupIndex(
                GroupIndex,
                addTradeLang("福利兑换 贰", "Welfare Redemption 2"),
                UNLOCK_BASE,
                Set.of(TECH_OPERATOR_COIN),
                GuiTextures.GREGTECH_LOGO);
    }
}
