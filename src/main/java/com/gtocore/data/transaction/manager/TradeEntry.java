package com.gtocore.data.transaction.manager;

import com.gtocore.data.transaction.common.TradingStationMachine;

import com.gtolib.api.wireless.WirelessManaContainer;
import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.utils.collection.O2LOpenCacheHashMap;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.gtocore.data.transaction.common.TradingStationTool.*;
import static com.gtocore.data.transaction.data.trade.UnlockTrade.UNLOCK_BASE;

/**
 * 游戏内交易条目，封装交易的显示信息、输入输出资源、检查条件和执行逻辑。
 */
public record TradeEntry(
                         // 界面渲染材质
                         IGuiTexture texture,
                         // 交易描述
                         List<Component> description,
                         // 解锁条件文本
                         String unlockCondition,
                         // 交易前额外检查逻辑
                         PreTradeCheck preCheck,
                         // 交易执行回调逻辑
                         TradeRunnable onExecute,
                         // 输入资源组
                         TradeGroup inputGroup,
                         // 输出资源组
                         TradeGroup outputGroup) {

    /**
     * 紧凑构造器
     */
    public TradeEntry {
        texture = texture != null ? texture : GuiTextures.GREGTECH_LOGO;
        unlockCondition = unlockCondition != null ? unlockCondition : UNLOCK_BASE;
        description = List.copyOf(description != null ? description : List.of());
        inputGroup = inputGroup != null ? inputGroup : new TradeGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);
        outputGroup = outputGroup != null ? outputGroup : new TradeGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);
    }

    // ------------------- 核心业务方法 -------------------

    /**
     * 执行交易前的额外条件检查
     */
    public int canExecuteCount(TradingStationMachine machine) {
        if (preCheck == null) return -1;
        return preCheck.test(machine, this);
    }

    /**
     * 输入资源检查
     */
    private int checkInputEnough(TradingStationMachine machine) {
        if (!(machine.getLevel() instanceof ServerLevel serverLevel)) return 0;

        int inputItem = inputGroup().items().isEmpty() ? Integer.MAX_VALUE : checkMaxMultiplier(machine.getInputItem(), inputGroup().items());
        if (inputItem == 0) return 0;

        int inputFluid = inputGroup().fluids().isEmpty() ? Integer.MAX_VALUE : checkMaxConsumeMultiplier(machine.getInputFluid(), inputGroup().fluids());
        if (inputFluid == 0) return 0;

        int outputFluid = outputGroup().fluids().isEmpty() ? Integer.MAX_VALUE : checkMaxCapacityMultiplier(machine.getOutputFluid(), outputGroup().fluids());
        if (outputFluid == 0) return 0;

        int inputCurrencies = inputGroup().currencies().isEmpty() ? Integer.MAX_VALUE : inputGroup().currencies().object2LongEntrySet().stream()
                .filter(entry -> entry.getLongValue() != 0)
                .mapToInt(entry -> (int) (WalletUtils.getCurrencyAmount(machine.getUuid(), serverLevel, entry.getKey()) / entry.getLongValue())).min().orElse(0);
        if (inputCurrencies == 0) return 0;

        int inputEnergy = inputGroup().energy().equals(BigInteger.ZERO) ? Integer.MAX_VALUE : WirelessEnergyContainer.getOrCreateContainer(machine.getTeamUUID()).getStorage()
                .divide(inputGroup().energy())
                .min(BigInteger.valueOf(100000000)).intValueExact();
        if (inputEnergy == 0) return 0;

        int inputMana = inputGroup().mana().equals(BigInteger.ZERO) ? Integer.MAX_VALUE : WirelessManaContainer.getOrCreateContainer(machine.getTeamUUID()).getStorage()
                .divide(inputGroup().mana())
                .min(BigInteger.valueOf(100_000_000)).intValueExact();
        if (inputMana == 0) return 0;

        return IntStream.of(inputItem, inputFluid, outputFluid, inputCurrencies, inputEnergy, inputMana)
                .min().orElse(0);
    }

    /**
     * 运行交易的实际输入输出
     */
    private void executeTrade(TradingStationMachine machine, int multiplier) {
        if (!(machine.getLevel() instanceof ServerLevel serverLevel)) return;

        if (!inputGroup().items().isEmpty()) {
            deductMultipliedItems(machine.getInputItem(), inputGroup().items(), multiplier);
        }
        if (!outputGroup().items().isEmpty()) {
            addMultipliedItems(machine.getOutputItem(), outputGroup().items(), multiplier, serverLevel, machine.getPos());
        }
        if (!inputGroup().fluids().isEmpty()) {
            deductMultipliedFluids(machine.getInputFluid(), inputGroup().fluids(), multiplier);
        }
        if (!outputGroup().fluids().isEmpty()) {
            addMultipliedFluids(machine.getOutputFluid(), outputGroup().fluids(), multiplier);
        }
        if (!inputGroup().currencies().isEmpty()) {
            inputGroup().currencies().forEach((currencyId, singleAmount) -> WalletUtils.subtractCurrency(machine.getUuid(), serverLevel, currencyId, singleAmount * multiplier));
        }
        if (!outputGroup().currencies().isEmpty()) {
            outputGroup().currencies().forEach((currencyId, singleAmount) -> WalletUtils.addCurrency(machine.getUuid(), serverLevel, currencyId, singleAmount * multiplier));
        }
        if (!inputGroup().energy().equals(BigInteger.ZERO)) {
            WirelessEnergyContainer energyContainer = WirelessEnergyContainer.getOrCreateContainer(machine.getTeamUUID());
            energyContainer.setStorage(energyContainer.getStorage().subtract(inputGroup().energy().multiply(BigInteger.valueOf(multiplier))));
        }
        if (!outputGroup().energy().equals(BigInteger.ZERO)) {
            WirelessEnergyContainer energyContainer = WirelessEnergyContainer.getOrCreateContainer(machine.getTeamUUID());
            energyContainer.setStorage(energyContainer.getStorage().add(outputGroup().energy().multiply(BigInteger.valueOf(multiplier))));
        }
        if (!inputGroup().mana().equals(BigInteger.ZERO)) {
            WirelessManaContainer manaContainer = WirelessManaContainer.getOrCreateContainer(machine.getTeamUUID());
            manaContainer.setStorage(manaContainer.getStorage().subtract(inputGroup().mana().multiply(BigInteger.valueOf(multiplier))));
        }
        if (!outputGroup().mana().equals(BigInteger.ZERO)) {
            WirelessManaContainer manaContainer = WirelessManaContainer.getOrCreateContainer(machine.getTeamUUID());
            manaContainer.setStorage(manaContainer.getStorage().add(outputGroup().mana().multiply(BigInteger.valueOf(multiplier))));
        }
    }

    /**
     * 可执行交易的次数
     */
    public int check(TradingStationMachine machine) {
        if (!(machine.getLevel() instanceof ServerLevel)) return 0;
        int multiplier = Integer.MAX_VALUE;
        int preCheckMaxCount = canExecuteCount(machine);
        if (preCheckMaxCount == 0) return 0;
        else if (preCheckMaxCount > 0) {
            multiplier = preCheckMaxCount;
        }
        int resourceMaxCount = checkInputEnough(machine);
        if (resourceMaxCount <= 0) return 0;
        return Math.min(multiplier, resourceMaxCount);
    }

    /**
     * 执行完整交易（资源变更+回调）
     */
    public void execute(TradingStationMachine machine, int requestedMultiplier) {
        if (!(machine.getLevel() instanceof ServerLevel)) return;
        int finalMultiplier = Math.min(check(machine), requestedMultiplier);
        if (finalMultiplier <= 0) {
            machine.getLevel().playSound(null, machine.getPos(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.8F, 1.4F);
            return;
        }
        executeTrade(machine, finalMultiplier);
        if (onExecute != null) {
            onExecute.run(machine, finalMultiplier, this);
        }
        machine.getLevel().playSound(null, machine.getPos(), SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS, 1.8F, 1.4F);
    }

    public List<Component> getDescription() {
        List<Component> componentList = new ArrayList<>(description());
        if (!inputGroup().isEmpty()) componentList.addAll(inputGroup().getComponentList(true));
        if (!outputGroup().isEmpty()) componentList.addAll(outputGroup().getComponentList(false));
        return componentList;
    }

    // ------------------- 内部类：交易资源组 -------------------
    public record TradeGroup(
                             List<ItemStack> items,
                             List<FluidStack> fluids,
                             O2LOpenCacheHashMap<String> currencies,
                             BigInteger energy,
                             BigInteger mana) {

        /**
         * 紧凑构造器
         */
        public TradeGroup {
            items = List.copyOf(items);
            fluids = List.copyOf(fluids);
            currencies = new O2LOpenCacheHashMap<>(currencies);
        }

        // ------------------- 资源操作 -------------------
        public TradeGroup addItem(ItemStack stack) {
            if (stack.isEmpty()) return this;
            List<ItemStack> newItems = Stream.concat(this.items.stream(), Stream.of(stack)).toList();
            return new TradeGroup(newItems, this.fluids, this.currencies, this.energy, this.mana);
        }

        public TradeGroup addFluid(FluidStack stack) {
            if (stack.isEmpty()) return this;
            List<FluidStack> newFluids = Stream.concat(this.fluids.stream(), Stream.of(stack)).toList();
            return new TradeGroup(this.items, newFluids, this.currencies, this.energy, this.mana);
        }

        public TradeGroup addCurrency(String currencyId, long amount) {
            if (amount <= 0) return this;
            O2LOpenCacheHashMap<String> newCurrencies = new O2LOpenCacheHashMap<>(this.currencies);
            newCurrencies.put(currencyId, amount);
            return new TradeGroup(this.items, this.fluids, newCurrencies, this.energy, this.mana);
        }

        public TradeGroup withEnergy(BigInteger energy) {
            return new TradeGroup(this.items, this.fluids, this.currencies, energy, this.mana);
        }

        public TradeGroup withMana(BigInteger mana) {
            return new TradeGroup(this.items, this.fluids, this.currencies, this.energy, mana);
        }

        public TradeGroup withEnergy(long energy) {
            return withEnergy(BigInteger.valueOf(energy));
        }

        public TradeGroup withMana(long mana) {
            return withMana(BigInteger.valueOf(mana));
        }

        /**
         * 检查当前 TradeGroup 是否所有字段都为空（或无效）。
         */
        public boolean isEmpty() {
            boolean isItemsEmpty = items.stream().allMatch(ItemStack::isEmpty);
            boolean isFluidsEmpty = fluids.stream().allMatch(FluidStack::isEmpty);
            boolean isCurrenciesEmpty = currencies.isEmpty() || currencies.values().longStream().allMatch(amount -> amount <= 0);
            boolean isEnergyEmpty = energy.equals(BigInteger.ZERO);
            boolean isManaEmpty = mana.equals(BigInteger.ZERO);
            return isItemsEmpty && isFluidsEmpty && isCurrenciesEmpty && isEnergyEmpty && isManaEmpty;
        }

        public List<Component> getComponentList(boolean input_output) {
            List<Component> list = new ArrayList<>();
            ChatFormatting color = input_output ? ChatFormatting.DARK_RED : ChatFormatting.DARK_GREEN;
            list.add(Component.literal("- ").withStyle(color)
                    .append(input_output ? Component.translatable("gtocore.trade_group.true").withStyle(ChatFormatting.DARK_RED) :
                            Component.translatable("gtocore.trade_group.false").withStyle(ChatFormatting.DARK_GREEN)));
            for (ItemStack itemStack : items) {
                list.add(Component.literal("- ").withStyle(color)
                        .append(Component.literal(String.valueOf(itemStack.getCount())).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal(" "))
                        .append(itemStack.getDisplayName().copy().withStyle(ChatFormatting.GOLD)));
            }
            for (FluidStack fluidStack : fluids) {
                list.add(Component.literal("- ").withStyle(color)
                        .append(Component.literal(String.valueOf(fluidStack.getAmount())).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal(" "))
                        .append(fluidStack.getDisplayName().copy().withStyle(ChatFormatting.LIGHT_PURPLE)));
            }
            currencies.object2LongEntrySet().forEach((entry) -> list.add(Component.literal("- ").withStyle(color)
                    .append(Component.literal(String.valueOf(entry.getLongValue())).withStyle(ChatFormatting.AQUA))
                    .append(Component.literal(" "))
                    .append(Component.translatable("gtocore.currency." + entry.getKey()).withStyle(ChatFormatting.YELLOW))));
            if (!energy.equals(BigInteger.ZERO)) {
                list.add(Component.literal("- ").withStyle(color)
                        .append(Component.literal(energy.toString()).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal(" "))
                        .append(Component.literal("EU").withStyle(ChatFormatting.DARK_AQUA)));
            }
            if (!mana.equals(BigInteger.ZERO)) {
                list.add(Component.literal("- ").withStyle(color)
                        .append(Component.literal(mana.toString()).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal(" "))
                        .append(Component.literal("Mana").withStyle(ChatFormatting.DARK_PURPLE)));
            }
            return list;
        }
    }

    // ------------------- 函数式接口 -------------------
    @FunctionalInterface
    public interface PreTradeCheck {

        int test(TradingStationMachine machine, TradeEntry entry);
    }

    @FunctionalInterface
    public interface TradeRunnable {

        void run(TradingStationMachine machine, int multiplier, TradeEntry entry);
    }

    // ------------------- 链式构建器（唯一配置入口） -------------------
    public static class Builder {

        private IGuiTexture texture;
        private List<Component> description = List.of();
        private String unlockCondition;
        private PreTradeCheck preCheck;
        private TradeRunnable onExecute;
        private TradeGroup inputGroup = new TradeGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);
        private TradeGroup outputGroup = new TradeGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);

        // ------------------- 配置方法（链式调用） -------------------
        public Builder texture(IGuiTexture texture) {
            this.texture = texture;
            return this;
        }

        public Builder description(List<Component> components) {
            this.description = List.copyOf(components != null ? components : List.of());
            return this;
        }

        public Builder unlockCondition(String condition) {
            this.unlockCondition = condition;
            return this;
        }

        public Builder preCheck(PreTradeCheck check) {
            this.preCheck = check;
            return this;
        }

        public Builder onExecute(TradeRunnable runnable) {
            this.onExecute = runnable;
            return this;
        }

        // ------------------- 输入资源配置 -------------------
        public Builder inputItem(ItemStack stack) {
            this.inputGroup = this.inputGroup.addItem(stack);
            return this;
        }

        public Builder inputFluid(FluidStack stack) {
            this.inputGroup = this.inputGroup.addFluid(stack);
            return this;
        }

        public Builder inputCurrency(String currencyId, long amount) {
            this.inputGroup = this.inputGroup.addCurrency(currencyId, amount);
            return this;
        }

        public Builder inputEnergy(long energy) {
            this.inputGroup = this.inputGroup.withEnergy(energy);
            return this;
        }

        public Builder inputMana(long mana) {
            this.inputGroup = this.inputGroup.withMana(mana);
            return this;
        }

        // ------------------- 输出资源配置 -------------------
        public Builder outputItem(ItemStack stack) {
            this.outputGroup = this.outputGroup.addItem(stack);
            return this;
        }

        public Builder outputFluid(FluidStack stack) {
            this.outputGroup = this.outputGroup.addFluid(stack);
            return this;
        }

        public Builder outputCurrency(String currencyId, long amount) {
            this.outputGroup = this.outputGroup.addCurrency(currencyId, amount);
            return this;
        }

        public Builder outputEnergy(long energy) {
            this.outputGroup = this.outputGroup.withEnergy(energy);
            return this;
        }

        public Builder outputMana(long mana) {
            this.outputGroup = this.outputGroup.withMana(mana);
            return this;
        }

        /**
         * 构建不可变 TradeEntry 实例
         */
        public TradeEntry build() {
            return new TradeEntry(
                    texture,
                    description,
                    unlockCondition,
                    preCheck,
                    onExecute,
                    inputGroup,
                    outputGroup);
        }
    }
}
