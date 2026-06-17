package com.gtocore.data.transaction.manager;

import com.gtolib.api.wireless.WirelessManaContainer;
import com.gtolib.utils.WalletUtils;

import com.gregtechceu.gtceu.api.gui.GuiTextures;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.fast.fastcollection.O2LOpenCacheHashMap;
import com.google.common.collect.ImmutableList;
import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.gtocore.data.transaction.TradingStationTool.*;
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
        description = ImmutableList.copyOf(description != null ? description : List.of());
        inputGroup = inputGroup != null ? inputGroup : new TradeGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);
        outputGroup = outputGroup != null ? outputGroup : new TradeGroup(List.of(), List.of(), new O2LOpenCacheHashMap<>(), BigInteger.ZERO, BigInteger.ZERO);
    }

    // ------------------- 核心业务方法 -------------------

    /**
     * 执行交易前的额外条件检查
     */
    public int canExecuteCount(TradeData data) {
        if (preCheck == null) return -1;
        return preCheck.test(data, this);
    }

    /**
     * 输入资源检查
     */
    private int checkInputEnough(TradeData data) {
        if (!(data.level() instanceof ServerLevel serverLevel)) return 0;

        int inputItem = inputGroup().items().isEmpty() ? Integer.MAX_VALUE : checkMaxMultiplier(data.inputItem(), inputGroup().items());
        if (inputItem == 0) return 0;

        int inputFluid = inputGroup().fluids().isEmpty() ? Integer.MAX_VALUE : checkMaxConsumeMultiplier(data.inputFluid(), inputGroup().fluids());
        if (inputFluid == 0) return 0;

        int outputFluid = outputGroup().fluids().isEmpty() ? Integer.MAX_VALUE : checkMaxCapacityMultiplier(data.outputFluid(), outputGroup().fluids());
        if (outputFluid == 0) return 0;

        int inputCurrencies = inputGroup().currencies().isEmpty() ? Integer.MAX_VALUE : (int) Math.min(inputGroup().currencies().object2LongEntrySet().stream()
                .filter(entry -> entry.getLongValue() != 0)
                .mapToLong(entry -> WalletUtils.getCurrencyAmount(data.uuid(), serverLevel, entry.getKey()) / entry.getLongValue())
                .min().orElse(0L), Integer.MAX_VALUE);
        if (inputCurrencies == 0) return 0;

        int inputEnergy = inputGroup().energy().equals(BigInteger.ZERO) ? Integer.MAX_VALUE : WirelessEnergyContainer.getOrCreateContainer(data.teamUUID()).getStorage()
                .divide(inputGroup().energy())
                .min(BigInteger.valueOf(Integer.MAX_VALUE)).intValueExact();
        if (inputEnergy == 0) return 0;

        int inputMana = inputGroup().mana().equals(BigInteger.ZERO) ? Integer.MAX_VALUE : WirelessManaContainer.getOrCreateContainer(data.teamUUID()).getStorage()
                .divide(inputGroup().mana())
                .min(BigInteger.valueOf(Integer.MAX_VALUE)).intValueExact();
        if (inputMana == 0) return 0;

        return IntStream.of(inputItem, inputFluid, outputFluid, inputCurrencies, inputEnergy, inputMana)
                .min().orElse(0);
    }

    /**
     * 运行交易的实际输入输出
     */
    private void executeInputOutput(TradeData data, int multiplier) {
        if (!(data.level() instanceof ServerLevel serverLevel)) return;

        if (!inputGroup().items().isEmpty()) {
            deductMultipliedItems(data.inputItem(), inputGroup().items(), multiplier);
        }
        if (!outputGroup().items().isEmpty()) {
            addMultipliedItems(data.outputItem(), outputGroup().items(), multiplier, serverLevel, data.pos());
        }
        if (!inputGroup().fluids().isEmpty()) {
            deductMultipliedFluids(data.inputFluid(), inputGroup().fluids(), multiplier);
        }
        if (!outputGroup().fluids().isEmpty()) {
            addMultipliedFluids(data.outputFluid(), outputGroup().fluids(), multiplier);
        }
        if (!inputGroup().currencies().isEmpty()) {
            inputGroup().currencies().forEach((currencyId, singleAmount) -> WalletUtils.subtractCurrency(data.uuid(), serverLevel, currencyId, singleAmount * multiplier));
        }
        if (!outputGroup().currencies().isEmpty()) {
            outputGroup().currencies().forEach((currencyId, singleAmount) -> WalletUtils.addCurrency(data.uuid(), serverLevel, currencyId, singleAmount * multiplier));
        }
        if (!inputGroup().energy().equals(BigInteger.ZERO)) {
            WirelessEnergyContainer energyContainer = WirelessEnergyContainer.getOrCreateContainer(data.teamUUID());
            energyContainer.setStorage(energyContainer.getStorage().subtract(inputGroup().energy().multiply(BigInteger.valueOf(multiplier))));
        }
        if (!outputGroup().energy().equals(BigInteger.ZERO)) {
            WirelessEnergyContainer energyContainer = WirelessEnergyContainer.getOrCreateContainer(data.teamUUID());
            energyContainer.setStorage(energyContainer.getStorage().add(outputGroup().energy().multiply(BigInteger.valueOf(multiplier))));
        }
        if (!inputGroup().mana().equals(BigInteger.ZERO)) {
            WirelessManaContainer manaContainer = WirelessManaContainer.getOrCreateContainer(data.teamUUID());
            manaContainer.setStorage(manaContainer.getStorage().subtract(inputGroup().mana().multiply(BigInteger.valueOf(multiplier))));
        }
        if (!outputGroup().mana().equals(BigInteger.ZERO)) {
            WirelessManaContainer manaContainer = WirelessManaContainer.getOrCreateContainer(data.teamUUID());
            manaContainer.setStorage(manaContainer.getStorage().add(outputGroup().mana().multiply(BigInteger.valueOf(multiplier))));
        }
    }

    /**
     * 可执行交易的次数
     */
    public int check(TradeData data) {
        if (!(data.level() instanceof ServerLevel)) return 0;
        int multiplier = Integer.MAX_VALUE;
        int preCheckMaxCount = canExecuteCount(data);
        if (preCheckMaxCount == 0) return 0;
        else if (preCheckMaxCount > 0) {
            multiplier = preCheckMaxCount;
        }
        int resourceMaxCount = checkInputEnough(data);
        if (resourceMaxCount <= 0) return 0;
        return Math.min(multiplier, resourceMaxCount);
    }

    /**
     * 执行完整交易（资源变更+回调）
     */
    public void executeTrade(TradeData data, int requestedMultiplier) {
        if (!(data.level() instanceof ServerLevel)) return;
        int finalMultiplier = Math.min(check(data), requestedMultiplier);
        if (finalMultiplier <= 0) {
            data.level().playSound(null, data.pos(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.8F, 1.4F);
            return;
        }
        executeInputOutput(data, finalMultiplier);
        if (onExecute != null) {
            onExecute.run(data, this, finalMultiplier);
        }
        data.level().playSound(null, data.pos(), SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS, 1.8F, 1.4F);
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
            items = ImmutableList.copyOf(items);
            fluids = ImmutableList.copyOf(fluids);
            currencies = new O2LOpenCacheHashMap<>(currencies);
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

        // =================== TradeGroup 的 Builder ===================
        public static class Builder {

            private final List<ItemStack> items = new ArrayList<>();
            private final List<FluidStack> fluids = new ArrayList<>();
            private final O2LOpenCacheHashMap<String> currencies = new O2LOpenCacheHashMap<>();
            private BigInteger energy = BigInteger.ZERO;
            private BigInteger mana = BigInteger.ZERO;

            public void addItem(ItemStack stack) {
                if (!stack.isEmpty()) {
                    this.items.add(stack);
                }
            }

            public void addFluid(FluidStack stack) {
                if (!stack.isEmpty()) {
                    this.fluids.add(stack);
                }
            }

            public void addCurrency(String currencyId, long amount) {
                if (amount > 0) {
                    this.currencies.put(currencyId, amount);
                }
            }

            public void withEnergy(BigInteger energy) {
                this.energy = energy;
            }

            public void withMana(BigInteger mana) {
                this.mana = mana;
            }

            public void withEnergy(long energy) {
                withEnergy(BigInteger.valueOf(energy));
            }

            public void withMana(long mana) {
                withMana(BigInteger.valueOf(mana));
            }

            public TradeGroup build() {
                return new TradeGroup(items, fluids, currencies, energy, mana);
            }
        }
    }

    // ------------------- 函数式接口 -------------------
    @FunctionalInterface
    public interface PreTradeCheck {

        int test(TradeData data, TradeEntry entry);
    }

    @FunctionalInterface
    public interface TradeRunnable {

        void run(TradeData data, TradeEntry entry, int multiplier);
    }

    // ------------------- TradeEntry 的链式构建器 -------------------
    public static class Builder {

        private IGuiTexture texture;
        private final List<Component> description = new ArrayList<>();
        private String unlockCondition;
        private PreTradeCheck preCheck;
        private TradeRunnable onExecute;
        private TradeGroup.Builder inputGroupBuilder = new TradeGroup.Builder();
        private TradeGroup.Builder outputGroupBuilder = new TradeGroup.Builder();

        // ------------------- 配置方法（链式调用） -------------------
        public Builder texture(IGuiTexture texture) {
            this.texture = texture;
            return this;
        }

        public Builder description(List<Component> components) {
            this.description.clear();
            if (components != null) {
                this.description.addAll(components);
            }
            return this;
        }

        public Builder addDescription(Component component) {
            this.description.add(component);
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
        public Builder input(TradeGroup.Builder builder) {
            this.inputGroupBuilder = builder;
            return this;
        }

        public Builder inputItem(ItemStack stack) {
            this.inputGroupBuilder.addItem(stack);
            return this;
        }

        public Builder inputFluid(FluidStack stack) {
            this.inputGroupBuilder.addFluid(stack);
            return this;
        }

        public Builder inputCurrency(String currencyId, long amount) {
            this.inputGroupBuilder.addCurrency(currencyId, amount);
            return this;
        }

        public Builder inputEnergy(long energy) {
            this.inputGroupBuilder.withEnergy(energy);
            return this;
        }

        public Builder inputEnergy(BigInteger energy) {
            this.inputGroupBuilder.withEnergy(energy);
            return this;
        }

        public Builder inputMana(long mana) {
            this.inputGroupBuilder.withMana(mana);
            return this;
        }

        public Builder inputMana(BigInteger mana) {
            this.inputGroupBuilder.withMana(mana);
            return this;
        }

        // ------------------- 输出资源配置 -------------------
        public Builder output(TradeGroup.Builder builder) {
            this.outputGroupBuilder = builder;
            return this;
        }

        public Builder outputItem(ItemStack stack) {
            this.outputGroupBuilder.addItem(stack);
            return this;
        }

        public Builder outputFluid(FluidStack stack) {
            this.outputGroupBuilder.addFluid(stack);
            return this;
        }

        public Builder outputCurrency(String currencyId, long amount) {
            this.outputGroupBuilder.addCurrency(currencyId, amount);
            return this;
        }

        public Builder outputEnergy(long energy) {
            this.outputGroupBuilder.withEnergy(energy);
            return this;
        }

        public Builder outputEnergy(BigInteger energy) {
            this.outputGroupBuilder.withEnergy(energy);
            return this;
        }

        public Builder outputMana(long mana) {
            this.outputGroupBuilder.withMana(mana);
            return this;
        }

        public Builder outputMana(BigInteger mana) {
            this.outputGroupBuilder.withMana(mana);
            return this;
        }

        /**
         * 构建不可变 TradeEntry 实例
         */
        public TradeEntry build() {
            return new TradeEntry(
                    texture,
                    ImmutableList.copyOf(description),
                    unlockCondition,
                    preCheck,
                    onExecute,
                    inputGroupBuilder.build(),
                    outputGroupBuilder.build());
        }
    }
}
