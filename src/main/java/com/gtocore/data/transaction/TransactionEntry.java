package com.gtocore.data.transaction;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.components.ChatComponent;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class TransactionEntry {
    // 渲染
    @Setter
    private IGuiTexture rendering;
    // 交易描述
    private Consumer<List<ChatComponent>> description;
    // 解锁条件
    @Setter
    private String unlockCondition;
    // 交易前调用
    @Setter
    private Supplier<Boolean> preTransactionCall;
    // 交易时调用
    @Setter
    private Runnable onTransactionCall;

    public void setDescription(List<ChatComponent> components) {
        if (description!= null) {
            description.accept(components);
        }
    }

    public boolean preTransactionCheck() {
        return preTransactionCall!= null && preTransactionCall.get();
    }

    public void onTransaction() {
        if (onTransactionCall!= null) {
            onTransactionCall.run();
        }
    }

    public void setDescription(Consumer<List<ChatComponent>> description) {
        this.description = description;
    }

    // 链式构建器内部类
    public static class TransactionEntryBuilder {
        private final TransactionEntry transactionEntry = new TransactionEntry();

        public TransactionEntryBuilder setRendering(IGuiTexture rendering) {
            transactionEntry.rendering = rendering;
            return this;
        }

        public TransactionEntryBuilder setDescription(Consumer<List<ChatComponent>> description) {
            transactionEntry.description = description;
            return this;
        }

        public TransactionEntryBuilder setUnlockCondition(String unlockCondition) {
            transactionEntry.unlockCondition = unlockCondition;
            return this;
        }

        public TransactionEntryBuilder setPreTransactionCall(Supplier<Boolean> preTransactionCall) {
            transactionEntry.preTransactionCall = preTransactionCall;
            return this;
        }

        public TransactionEntryBuilder setOnTransactionCall(Runnable onTransactionCall) {
            transactionEntry.onTransactionCall = onTransactionCall;
            return this;
        }

        public TransactionEntry build() {
            return transactionEntry;
        }
    }
}
