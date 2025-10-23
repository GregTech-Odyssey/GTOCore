package com.gtocore.data.lootTables.GTOLootItemFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import org.jetbrains.annotations.NotNull;

public class CustomLogicFunction implements LootItemFunction {

    @FunctionalInterface
    public interface LootLogic {

        void execute(ServerPlayer player, ServerLevel level, Entity entity, BlockPos pos, ItemStack tool, ItemStack stack);
    }

    private final LootLogic logic;

    public CustomLogicFunction(LootLogic logic) {
        this.logic = logic;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        ServerLevel level = context.getLevel();
        if (level.isClientSide()) return stack;

        ServerPlayer player = extractPlayer(context);
        Entity relatedEntity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        BlockPos pos = extractPosition(context);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);

        logic.execute(player, level, relatedEntity, pos, tool, stack);
        return stack;
    }

    private ServerPlayer extractPlayer(LootContext context) {
        Entity looter = context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
        return looter instanceof ServerPlayer ? (ServerPlayer) looter : null;
    }

    private BlockPos extractPosition(LootContext context) {
        return context.getParamOrNull(LootContextParams.ORIGIN) != null ? BlockPos.containing(context.getParam(LootContextParams.ORIGIN)) : BlockPos.ZERO;
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return null;
    }

    public static class Builder implements LootItemFunction.Builder {

        private final LootLogic logic;

        public Builder(LootLogic logic) {
            this.logic = logic;
        }

        @Override
        public @NotNull CustomLogicFunction build() {
            return new CustomLogicFunction(logic);
        }
    }
}
