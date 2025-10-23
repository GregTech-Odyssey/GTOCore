package com.gtocore.data.lootTables.GTOLootTool;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.phys.Vec3;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomLogicNumberProvider implements NumberProvider {

    public static final CustomLogicNumberProvider INSTANCE = new CustomLogicNumberProvider((
                                                                                            thisEntity, lastDamagePlayer, damageSource, killerEntity, directKiller,
                                                                                            origin, blockState, blockEntity, tool, explosionRadius, level) -> ConstantValue.exactly(0));

    @FunctionalInterface
    public interface NumberLogic {

        NumberProvider calculate(
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
                                 @NotNull ServerLevel level);
    }

    private final NumberLogic logic;

    public CustomLogicNumberProvider(NumberLogic logic) {
        this.logic = logic;
    }

    @Override
    public int getInt(@NotNull LootContext context) {
        return getNumberProvider(context).getInt(context);
    }

    @Override
    public float getFloat(@NotNull LootContext context) {
        return getNumberProvider(context).getFloat(context);
    }

    private NumberProvider getNumberProvider(LootContext context) {
        ServerLevel level = context.getLevel();
        if (level.isClientSide()) {
            return ConstantValue.exactly(0);
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

        return logic.calculate(
                thisEntity, lastDamagePlayer, damageSource, killerEntity, directKiller,
                origin, blockState, blockEntity, tool, explosionRadius, level);
    }

    @Override
    public @NotNull LootNumberProviderType getType() {
        return GTONumberProviders.CUSTOM_LOGIC.get();
    }

    private <T> T getParam(LootContext context, LootContextParam<T> param) {
        return context.getParamOrNull(param);
    }

    private <T> T getParamOrDefault(LootContext context, LootContextParam<T> param, T defaultValue) {
        T value = context.getParamOrNull(param);
        return value != null ? value : defaultValue;
    }

    public static class Builder {

        private final NumberLogic logic;

        public Builder(NumberLogic logic) {
            this.logic = logic;
        }

        public CustomLogicNumberProvider build() {
            return new CustomLogicNumberProvider(logic);
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CustomLogicNumberProvider> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void serialize(@NotNull JsonObject json, @NotNull CustomLogicNumberProvider provider, @NotNull JsonSerializationContext context) {}

        @Override
        public @NotNull CustomLogicNumberProvider deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return CustomLogicNumberProvider.INSTANCE;
        }
    }
}
