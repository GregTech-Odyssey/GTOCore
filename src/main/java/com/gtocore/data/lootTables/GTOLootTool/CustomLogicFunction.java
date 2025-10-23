package com.gtocore.data.lootTables.GTOLootTool;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomLogicFunction implements LootItemFunction {

    @FunctionalInterface
    public interface LootLogic {

        void execute(
                     @NotNull ServerLevel level,
                     @Nullable Entity thisEntity,
                     @Nullable Player lastDamagePlayer,
                     @Nullable DamageSource damageSource,
                     @Nullable Entity killerEntity,
                     @Nullable Entity directKiller,
                     @Nullable Vec3 origin,
                     @Nullable BlockState blockState,
                     @Nullable BlockEntity blockEntity,
                     @NotNull ItemStack tool,
                     @Nullable Float explosionRadius,
                     @NotNull ItemStack stack);
    }

    private final LootLogic logic;

    public CustomLogicFunction(LootLogic logic) {
        this.logic = logic;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        ServerLevel level = context.getLevel();
        if (level.isClientSide()) {
            return stack;
        }

        Entity thisEntity = getParam(context, LootContextParams.THIS_ENTITY);
        Player lastDamagePlayer = getParam(context, LootContextParams.LAST_DAMAGE_PLAYER);
        DamageSource damageSource = getParam(context, LootContextParams.DAMAGE_SOURCE);
        Entity killerEntity = getParam(context, LootContextParams.KILLER_ENTITY);
        Entity directKiller = getParam(context, LootContextParams.DIRECT_KILLER_ENTITY);
        Vec3 origin = getParam(context, LootContextParams.ORIGIN);
        BlockState blockState = getParam(context, LootContextParams.BLOCK_STATE);
        BlockEntity blockEntity = getParam(context, LootContextParams.BLOCK_ENTITY);
        ItemStack tool = getParamOrDefault(context, LootContextParams.TOOL, ItemStack.EMPTY);
        Float explosionRadius = getParam(context, LootContextParams.EXPLOSION_RADIUS);

        logic.execute(
                level, thisEntity, lastDamagePlayer, damageSource, killerEntity, directKiller,
                origin, blockState, blockEntity, tool, explosionRadius, stack);

        return stack;
    }

    private <T> T getParam(LootContext context, LootContextParam<T> param) {
        return context.getParamOrNull(param);
    }

    private <T> T getParamOrDefault(LootContext context, LootContextParam<T> param, T defaultValue) {
        T value = context.getParamOrNull(param);
        return value != null ? value : defaultValue;
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
