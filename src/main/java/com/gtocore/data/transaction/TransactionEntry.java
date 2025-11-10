package com.gtocore.data.transaction;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public class TransactionEntry {

    // 渲染
    @Setter
    private IGuiTexture rendering = new ItemStackTexture(Blocks.GRASS_BLOCK.asItem());
    // 交易描述
    private final List<Component> lastDescription = new ArrayList<>();
    private Consumer<List<Component>> descriptionSupplier;
    // 解锁条件
    @Setter
    private Component unlockCondition;
    // 交易前调用
    @Setter
    private PreTransactionCall preTransactionCall;
    // 交易时调用
    @Setter
    private TransactionRunnable onTransactionCall;

    public void setDescription(List<Component> components) {
        this.lastDescription.addAll(components);
    }

    public void setDescriptionSupplier(Consumer<List<Component>> descriptionSupplier) {
        this.descriptionSupplier = descriptionSupplier;
        if (descriptionSupplier != null) {
            descriptionSupplier.accept(this.lastDescription);
        }
    }

    public boolean preTransactionCheck(boolean isOnPlayerEntity, UUID playerUUID, ServerLevel world, BlockPos pos, List<ItemStack> itemStacks) {
        return preTransactionCall != null && preTransactionCall.check(isOnPlayerEntity, playerUUID, world, pos, itemStacks);
    }

    public void onTransaction(boolean isOnPlayerEntity, UUID playerUUID, ServerLevel world, BlockPos pos, List<ItemStack> itemStacks) {
        if (onTransactionCall != null) {
            onTransactionCall.run(isOnPlayerEntity, playerUUID, world, pos, itemStacks);
        }
    }

    // 交易前检查的新接口
    public interface PreTransactionCall {

        boolean check(boolean isOnPlayerEntity, UUID playerUUID, ServerLevel world, BlockPos pos, List<ItemStack> itemStacks);
    }

    // 交易时调用的新接口
    public interface TransactionRunnable {

        void run(boolean isOnPlayerEntity, UUID playerUUID, ServerLevel world, BlockPos pos, List<ItemStack> itemStacks);
    }

    // 自定义 ClickData 类，这里只是简单示例，可根据实际需求完善
    public static class ClickData {
        // 可以添加更多字段和方法
    }

    // 链式构建器内部类
    public static class TransactionEntryBuilder {

        private final TransactionEntry transactionEntry = new TransactionEntry();

        public TransactionEntryBuilder setRendering(IGuiTexture rendering) {
            transactionEntry.rendering = rendering;
            return this;
        }

        public TransactionEntryBuilder setDescriptionSupplier(Consumer<List<Component>> descriptionSupplier) {
            transactionEntry.setDescriptionSupplier(descriptionSupplier);
            return this;
        }

        public TransactionEntryBuilder setUnlockCondition(Component unlockCondition) {
            transactionEntry.unlockCondition = unlockCondition;
            return this;
        }

        public TransactionEntryBuilder setPreTransactionCall(PreTransactionCall preTransactionCall) {
            transactionEntry.preTransactionCall = preTransactionCall;
            return this;
        }

        public TransactionEntryBuilder setOnTransactionCall(TransactionRunnable onTransactionCall) {
            transactionEntry.onTransactionCall = onTransactionCall;
            return this;
        }

        public TransactionEntry build() {
            return transactionEntry;
        }
    }
}
