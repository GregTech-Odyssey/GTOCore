package com.gtocore.data.transaction;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * 游戏内交易条目模板，封装交易的显示、描述、条件和执行逻辑。
 * 可通过 {@link TransactionEntryBuilder} 构建具体实例。
 */
@Getter
public class TransactionEntry {

    // 渲染：交易在GUI中显示的图标（默认空纹理，避免null）
    @Setter
    private IGuiTexture rendering = new ItemStackTexture(Blocks.GRASS_BLOCK.asItem());

    // 交易描述：存储最终显示的文本（对外提供不可修改的视图）
    private final List<Component> lastDescription = new ArrayList<>();
    // 描述提供器：动态生成描述文本的逻辑
    private Consumer<List<Component>> descriptionSupplier;

    // 解锁条件：显示给玩家的解锁要求文本
    @Setter
    private String unlockCondition; // 默认空文本

    // 交易前检查逻辑：验证是否允许执行交易
    @Setter
    private PreTransactionCall preTransactionCall;

    // 交易执行逻辑：交易通过检查后执行的动作
    @Setter
    private TransactionRunnable onTransactionCall;

    /**
     * 直接设置静态交易描述（会清空现有描述后添加）
     * 
     * @param components 描述文本组件列表
     */
    public void setDescription(List<Component> components) {
        lastDescription.clear(); // 先清空旧描述，避免累积
        lastDescription.addAll(components);
    }

    /**
     * 设置动态描述生成器（会触发一次描述生成，覆盖现有描述）
     * 
     * @param descriptionSupplier 生成描述的逻辑（接收列表并添加内容）
     */
    public void setDescriptionSupplier(Consumer<List<Component>> descriptionSupplier) {
        this.descriptionSupplier = descriptionSupplier;
        lastDescription.clear(); // 清空旧描述，避免新旧混合
        if (descriptionSupplier != null) {
            descriptionSupplier.accept(this.lastDescription);
        }
    }

    /**
     * 执行交易前检查
     * 
     * @param context 交易上下文（包含玩家、世界、位置等信息）
     * @return true表示检查通过，允许交易；false则阻止
     */
    public boolean preTransactionCheck(TransactionContext context) {
        // 若没有设置检查逻辑，默认允许交易（更合理的默认行为）
        return preTransactionCall == null || preTransactionCall.check(context);
    }

    /**
     * 执行交易动作
     * 
     * @param context 交易上下文
     */
    public void onTransaction(TransactionContext context) {
        if (onTransactionCall != null) {
            onTransactionCall.run(context);
        }
    }

    /**
     * 交易上下文：封装交易相关的所有信息（玩家、世界、位置等），简化参数传递
     *
     * @param isOnPlayerEntity Getter方法（仅提供读取，不允许修改上下文） 是否在玩家实体上触发
     * @param playerUUID       玩家唯一标识
     * @param world            游戏世界
     * @param pos              交易发生的位置
     * @param itemStacks       涉及的物品栈
     */

    public record TransactionContext(boolean isOnPlayerEntity, UUID playerUUID, ServerLevel world, BlockPos pos, List<ItemStack> itemStacks) {

        public TransactionContext(boolean isOnPlayerEntity, UUID playerUUID, ServerLevel world, BlockPos pos, List<ItemStack> itemStacks) {
            this.isOnPlayerEntity = isOnPlayerEntity;
            this.playerUUID = playerUUID;
            this.world = world;
            this.pos = pos;
            this.itemStacks = List.copyOf(itemStacks);
        }
    }

    /**
     * 交易前检查接口：通过上下文判断是否允许交易
     */
    public interface PreTransactionCall {

        boolean check(TransactionContext context);
    }

    /**
     * 交易执行接口：通过上下文执行交易动作
     */
    public interface TransactionRunnable {

        void run(TransactionContext context);
    }

    /**
     * 链式构建器：简化TransactionEntry的创建，提供清晰的配置流程
     */
    public static class TransactionEntryBuilder {

        private final TransactionEntry transactionEntry = new TransactionEntry();

        /** 设置GUI渲染纹理 */
        public TransactionEntryBuilder rendering(IGuiTexture rendering) {
            transactionEntry.rendering = rendering;
            return this;
        }

        /** 设置静态描述文本 */
        public TransactionEntryBuilder description(List<Component> components) {
            transactionEntry.setDescription(components);
            return this;
        }

        /** 设置动态描述生成器 */
        public TransactionEntryBuilder descriptionSupplier(Consumer<List<Component>> supplier) {
            transactionEntry.setDescriptionSupplier(supplier);
            return this;
        }

        /** 设置解锁条件文本 */
        public TransactionEntryBuilder unlockCondition(String condition) {
            transactionEntry.unlockCondition = condition;
            return this;
        }

        /** 设置交易前检查逻辑 */
        public TransactionEntryBuilder preCheck(PreTransactionCall preCheck) {
            transactionEntry.preTransactionCall = preCheck;
            return this;
        }

        /** 设置交易执行逻辑 */
        public TransactionEntryBuilder onExecute(TransactionRunnable execute) {
            transactionEntry.onTransactionCall = execute;
            return this;
        }

        /** 构建最终的交易实例 */
        public TransactionEntry build() {
            return transactionEntry;
        }
    }
}
