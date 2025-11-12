package com.gtocore.data.transaction;

import com.gregtechceu.gtceu.utils.collection.O2LOpenCacheHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * 游戏内交易条目，封装交易的显示信息、输入输出资源、检查条件和执行逻辑。
 * 可通过 {@link Builder} 链式构建实例。
 */
@Getter
public class TransactionEntry {

    // 界面渲染材质（如商品图标）
    @Setter
    private IGuiTexture texture;

    // 交易描述（静态文本，对外提供不可修改视图）
    private final List<Component> description = new ArrayList<>();
    // 动态描述生成器（可实时更新描述，如"剩余次数: 3"）
    private Consumer<List<Component>> descriptionSupplier;

    // 解锁条件文本（显示给玩家的解锁要求，如"完成新手任务后解锁"）
    @Setter
    private String unlockCondition;

    // 交易前的额外检查逻辑（如"玩家等级≥5"，返回true则允许交易）
    @Setter
    private PreTransactionCheck preCheck;

    // 交易执行时的回调逻辑（如扣减输入、发放输出）
    @Setter
    private TransactionRunnable onExecute;

    // 输入资源组（交易所需的资源）
    private final TransactionGroup inputGroup = new TransactionGroup();
    // 输出资源组（交易产出的资源）
    private final TransactionGroup outputGroup = new TransactionGroup();

    /**
     * 设置静态描述（覆盖现有描述）
     */
    public void setDescription(List<Component> components) {
        this.description.clear();
        this.description.addAll(components);
    }

    /**
     * 设置动态描述生成器（触发一次生成，覆盖现有描述）
     */
    public void setDescriptionSupplier(Consumer<List<Component>> supplier) {
        this.descriptionSupplier = supplier;
        this.description.clear();
        if (supplier != null) {
            supplier.accept(this.description);
        }
    }

    /**
     * 执行交易前的所有检查（基础资源检查 + 额外条件检查）
     * 
     * @return true表示检查通过，允许执行交易
     */
    public boolean canExecute(TransactionContext context) {
        // 1. 基础检查：输入资源是否满足（此处简化，实际需校验玩家是否拥有足够输入）
        boolean hasEnoughInput = checkInputEnough(context);
        // 2. 额外检查：通过preCheck接口扩展
        boolean extraCheckPass = preCheck == null || preCheck.test(context, this);
        return hasEnoughInput && extraCheckPass;
    }

    /**
     * 执行交易（调用回调逻辑，处理输入输出）
     */
    public void execute(TransactionContext context) {
        if (onExecute != null) {
            onExecute.run(context, this);
        }
    }

    /**
     * 基础输入资源检查（示例逻辑，实际需根据玩家背包验证）
     */
    private boolean checkInputEnough(TransactionContext context) {
        // 这里仅做框架示例，实际需检查：
        // - 玩家物品是否满足inputGroup.items
        // - 玩家流体是否满足inputGroup.fluids
        // - 玩家货币是否满足inputGroup.currencies
        // - 玩家电量/魔力量是否满足inputGroup的要求
        return true;
    }

    /**
     * 交易资源组：封装一组资源（物品、流体、货币、电量、魔力）
     * 可作为输入（交易所需）或输出（交易所得）
     */
    @Getter
    public static class TransactionGroup {

        // 物品列表（如[64个木头, 1个钻石]）
        private final List<ItemStack> items = new ArrayList<>();
        // 流体列表（如[1000mB水, 500mB岩浆]）
        private final List<FluidStack> fluids = new ArrayList<>();
        // 货币映射（键：货币类型ID，值：数量列表，支持多类型货币）
        private final O2LOpenCacheHashMap<String> currencies = new O2LOpenCacheHashMap<>();
        /**
         * -- SETTER --
         * 设置电量（可正可负，正数为消耗，负数为产出）
         */
        // 所需/产出的电量（单位：EU）
        @Setter
        private long energy;
        /**
         * -- SETTER --
         * 设置魔力量（可正可负，正数为消耗，负数为产出）
         */
        // 所需/产出的魔力量（单位：Mana）
        @Setter
        private long mana;

        /** 添加物品到资源组 */
        public void addItem(ItemStack stack) {
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }

        /** 添加流体到资源组 */
        public void addFluid(FluidStack stack) {
            if (!stack.isEmpty()) {
                fluids.add(stack);
            }
        }

        /** 添加货币到资源组（键：货币ID，值：数量） */
        public void addCurrency(String currencyId, long amount) {
            if (amount > 0) {
                currencies.put(currencyId, amount);
            }
        }
    }

    /**
     * 交易上下文：封装交易发生时的环境信息
     */
    public record TransactionContext(
                                     UUID playerUUID,    // 玩家唯一标识
                                     ServerLevel world,  // 交易所在世界
                                     BlockPos pos        // 交易发生的坐标（如NPC位置、交易台位置）
    ) {}

    /**
     * 交易前检查接口：额外条件判断（如玩家等级、任务进度等）
     */
    @FunctionalInterface
    public interface PreTransactionCheck {

        /**
         * @param context 交易上下文
         * @param entry   当前交易条目（可访问输入输出组）
         * @return true表示检查通过
         */
        boolean test(TransactionContext context, TransactionEntry entry);
    }

    /**
     * 交易执行接口：处理交易逻辑（扣减输入、发放输出等）
     */
    @FunctionalInterface
    public interface TransactionRunnable {

        /**
         * @param context 交易上下文
         * @param entry   当前交易条目（可修改输入输出组，或直接操作玩家资源）
         */
        void run(TransactionContext context, TransactionEntry entry);
    }

    /**
     * 链式构建器：简化TransactionEntry的创建
     */
    public static class Builder {

        private final TransactionEntry entry = new TransactionEntry();

        /** 设置界面渲染材质 */
        public Builder texture(IGuiTexture texture) {
            entry.texture = texture;
            return this;
        }

        /** 设置静态描述文本 */
        public Builder description(List<Component> components) {
            entry.setDescription(components);
            return this;
        }

        /** 设置动态描述生成器 */
        public Builder descriptionSupplier(Consumer<List<Component>> supplier) {
            entry.setDescriptionSupplier(supplier);
            return this;
        }

        /** 设置解锁条件文本 */
        public Builder unlockCondition(String condition) {
            entry.unlockCondition = condition;
            return this;
        }

        /** 添加输入物品（交易所需物品） */
        public Builder inputItem(ItemStack stack) {
            entry.inputGroup.addItem(stack);
            return this;
        }

        /** 添加输入流体（交易所需流体） */
        public Builder inputFluid(FluidStack stack) {
            entry.inputGroup.addFluid(stack);
            return this;
        }

        /** 添加输入货币（交易所需货币） */
        public Builder inputCurrency(String currencyId, long amount) {
            entry.inputGroup.addCurrency(currencyId, amount);
            return this;
        }

        /** 设置输入电量（消耗的电量） */
        public Builder inputEnergy(long energy) {
            entry.inputGroup.setEnergy(energy);
            return this;
        }

        /** 设置输入魔力（消耗的魔力） */
        public Builder inputMana(long mana) {
            entry.inputGroup.setMana(mana);
            return this;
        }

        /** 添加输出物品（交易产出物品） */
        public Builder outputItem(ItemStack stack) {
            entry.outputGroup.addItem(stack);
            return this;
        }

        /** 添加输出流体（交易产出流体） */
        public Builder outputFluid(FluidStack stack) {
            entry.outputGroup.addFluid(stack);
            return this;
        }

        /** 添加输出货币（交易产出货币） */
        public Builder outputCurrency(String currencyId, long amount) {
            entry.outputGroup.addCurrency(currencyId, amount);
            return this;
        }

        /** 设置输出电量（获得的电量） */
        public Builder outputEnergy(long energy) {
            entry.outputGroup.setEnergy(energy);
            return this;
        }

        /** 设置输出魔力（获得的魔力） */
        public Builder outputMana(long mana) {
            entry.outputGroup.setMana(mana);
            return this;
        }

        /** 设置交易前额外检查逻辑 */
        public Builder preCheck(PreTransactionCheck check) {
            entry.preCheck = check;
            return this;
        }

        /** 设置交易执行逻辑 */
        public Builder onExecute(TransactionRunnable runnable) {
            entry.onExecute = runnable;
            return this;
        }

        /** 构建交易条目实例 */
        public TransactionEntry build() {
            return entry;
        }
    }
}
