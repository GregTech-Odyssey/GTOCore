package com.gtocore.data.transaction.recipe.entry;

import com.gtocore.data.transaction.common.TradingStationMachine;

import com.gtolib.api.wireless.WirelessManaContainer;
import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.utils.collection.O2LOpenCacheHashMap;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

import static com.gtocore.data.transaction.common.TradingStationTool.*;

/**
 * 游戏内交易条目，封装交易的显示信息、输入输出资源、检查条件和执行逻辑。
 */
public record TransactionEntry(
                               // 界面渲染材质
                               IGuiTexture texture,
                               // 交易描述
                               List<Component> description,
                               // 解锁条件文本
                               String unlockCondition,
                               // 交易前额外检查逻辑
                               PreTransactionCheck preCheck,
                               // 交易执行回调逻辑
                               TransactionRunnable onExecute,
                               // 输入资源组
                               TransactionGroup inputGroup,
                               // 输出资源组
                               TransactionGroup outputGroup) {

    /**
     * 紧凑构造器
     */
    public TransactionEntry {
        description = List.copyOf(description != null ? description : List.of());
        inputGroup = inputGroup != null ? inputGroup : new TransactionGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);
        outputGroup = outputGroup != null ? outputGroup : new TransactionGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);
    }

    // ------------------- 核心业务方法 -------------------

    /**
     * 执行交易前的额外条件检查
     */
    public boolean canExecute(TradingStationMachine machine) {
        return preCheck == null || preCheck.test(machine, this);
    }

    /**
     * 执行交易（仅调用不可变的 onExecute 回调，无状态变更）
     */
    public void execute(TradingStationMachine machine) {
        if (onExecute != null) {
            onExecute.run(machine, this);
        }
    }

    /**
     * 输入资源检查
     */
    private int checkInputEnough(TradingStationMachine machine) {
        int inputItem = Integer.MAX_VALUE;
        if (!inputGroup().items().isEmpty()) {
            inputItem = checkMaxMultiplier(machine.getInputItem(), inputGroup().items());
            if (inputItem == 0) return 0;
        }
        int inputFluid = Integer.MAX_VALUE;
        if (!inputGroup().fluids().isEmpty()) {
            inputFluid = checkMaxConsumeMultiplier(machine.getInputFluid(), inputGroup().fluids());
            if (inputFluid == 0) return 0;
        }
        int outputFluid = Integer.MAX_VALUE;
        if (!outputGroup().fluids().isEmpty()) {
            outputFluid = checkMaxCapacityMultiplier(machine.getOutputFluid(), outputGroup().fluids());
            if (outputFluid == 0) return 0;
        }
        int inputCurrencies = Integer.MAX_VALUE;
        if (!inputGroup().currencies().isEmpty()) {
            inputCurrencies = inputGroup().currencies().object2LongEntrySet().stream()
                    .filter(entry -> entry.getLongValue() != 0)
                    .mapToInt(entry -> {
                        long currencyAmount = WalletUtils.getCurrencyAmount(machine.getUuid(), (ServerLevel) machine.getLevel(), entry.getKey());
                        return (int) (currencyAmount / entry.getLongValue());
                    }).min().orElse(0);
            if (inputCurrencies == 0) return 0;
        }
        int inputEnergy = Integer.MAX_VALUE;
        if (!inputGroup().energy().equals(BigInteger.ZERO)) {
            BigInteger result = WirelessEnergyContainer.getOrCreateContainer(machine.getTeamUUID()).getStorage().divide(inputGroup().energy());
            inputEnergy = (result.compareTo(BigInteger.valueOf(1000000000)) > 0 ? BigInteger.valueOf(1000000000) : result).intValueExact();
            if (inputEnergy == 0) return 0;
        }
        int inputMana = Integer.MAX_VALUE;
        if (!inputGroup().mana().equals(BigInteger.ZERO)) {
            BigInteger result = WirelessManaContainer.getOrCreateContainer(machine.getTeamUUID()).getStorage().divide(inputGroup().mana());
            inputMana = (result.compareTo(BigInteger.valueOf(1000000000)) > 0 ? BigInteger.valueOf(1000000000) : result).intValueExact();
            if (inputMana == 0) return 0;
        }
        return IntStream.of(inputItem, inputFluid, outputFluid, inputCurrencies, inputEnergy, inputMana)
                .min().orElse(0);
    }

    // ------------------- 内部类：交易资源组 -------------------
    public record TransactionGroup(
                                   List<ItemStack> items,
                                   List<FluidStack> fluids,
                                   O2LOpenCacheHashMap<String> currencies,
                                   BigInteger energy,
                                   BigInteger mana) {

        /**
         * 紧凑构造器
         */
        public TransactionGroup {
            items = List.copyOf(items);
            fluids = List.copyOf(fluids);
            currencies = new O2LOpenCacheHashMap<>(currencies);
        }

        // ------------------- 资源操作：返回新实例 -------------------
        public TransactionGroup addItem(ItemStack stack) {
            if (stack.isEmpty()) return this;
            List<ItemStack> newItems = List.copyOf(List.of(stack));
            return new TransactionGroup(newItems, this.fluids, this.currencies, this.energy, this.mana);
        }

        public TransactionGroup addFluid(FluidStack stack) {
            if (stack.isEmpty()) return this;
            List<FluidStack> newFluids = List.copyOf(List.of(stack));
            return new TransactionGroup(this.items, newFluids, this.currencies, this.energy, this.mana);
        }

        public TransactionGroup addCurrency(String currencyId, long amount) {
            if (amount <= 0) return this;
            O2LOpenCacheHashMap<String> newCurrencies = new O2LOpenCacheHashMap<>(this.currencies);
            newCurrencies.put(currencyId, amount);
            return new TransactionGroup(this.items, this.fluids, newCurrencies, this.energy, this.mana);
        }

        public TransactionGroup withEnergy(BigInteger energy) {
            return new TransactionGroup(this.items, this.fluids, this.currencies, energy, this.mana);
        }

        public TransactionGroup withMana(BigInteger mana) {
            return new TransactionGroup(this.items, this.fluids, this.currencies, this.energy, mana);
        }

        public TransactionGroup withEnergy(long energy) {
            return withEnergy(BigInteger.valueOf(energy));
        }

        public TransactionGroup withMana(long mana) {
            return withMana(BigInteger.valueOf(mana));
        }
    }

    // ------------------- 函数式接口 -------------------
    @FunctionalInterface
    public interface PreTransactionCheck {

        boolean test(TradingStationMachine machine, TransactionEntry entry);
    }

    @FunctionalInterface
    public interface TransactionRunnable {

        void run(TradingStationMachine machine, TransactionEntry entry);
    }

    // ------------------- 链式构建器（唯一配置入口） -------------------
    public static class Builder {

        private IGuiTexture texture;
        private List<Component> description = List.of();
        private String unlockCondition;
        private PreTransactionCheck preCheck;
        private TransactionRunnable onExecute;
        private TransactionGroup inputGroup = new TransactionGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);
        private TransactionGroup outputGroup = new TransactionGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);

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

        public Builder preCheck(PreTransactionCheck check) {
            this.preCheck = check;
            return this;
        }

        public Builder onExecute(TransactionRunnable runnable) {
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
         * 构建不可变 TransactionEntry 实例
         */
        public TransactionEntry build() {
            return new TransactionEntry(
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
