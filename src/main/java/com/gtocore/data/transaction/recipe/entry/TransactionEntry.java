package com.gtocore.data.transaction.recipe.entry;

import com.gregtechceu.gtceu.utils.collection.O2LOpenCacheHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

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
        TransactionGroup outputGroup
) {

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
     * 执行交易前的所有检查（基础资源检查 + 额外条件检查）
     */
    public boolean canExecute(TransactionContext context) {
        boolean hasEnoughInput = checkInputEnough(context);
        boolean extraCheckPass = preCheck == null || preCheck.test(context, this);
        return hasEnoughInput && extraCheckPass;
    }

    /**
     * 执行交易（仅调用不可变的 onExecute 回调，无状态变更）
     */
    public void execute(TransactionContext context) {
        if (onExecute != null) {
            onExecute.run(context, this);
        }
    }

    /**
     * 基础输入资源检查（示例逻辑，实际需对接玩家资源系统）
     */
    private boolean checkInputEnough(TransactionContext context) {
        // 实际实现需检查：
        // 1. 玩家货币是否满足 inputGroup.currencies
        // 2. 玩家魔力是否满足 inputGroup.mana
        // 3. 其他输入资源是否充足
        return true;
    }

    // ------------------- 内部类：交易资源组 -------------------
    public record TransactionGroup(
            List<ItemStack> items,
            List<FluidStack> fluids,
            O2LOpenCacheHashMap<String> currencies,
            BigInteger energy,
            BigInteger  mana
    ) {
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

    // ------------------- 内部类：交易上下文 -------------------
    public record TransactionContext(
            UUID playerUUID,
            ServerLevel world,
            BlockPos pos
    ) {
    }

    // ------------------- 函数式接口 -------------------
    @FunctionalInterface
    public interface PreTransactionCheck {
        boolean test(TransactionContext context, TransactionEntry entry);
    }

    @FunctionalInterface
    public interface TransactionRunnable {
        void run(TransactionContext context, TransactionEntry entry);
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
                    outputGroup
            );
        }
    }
}
