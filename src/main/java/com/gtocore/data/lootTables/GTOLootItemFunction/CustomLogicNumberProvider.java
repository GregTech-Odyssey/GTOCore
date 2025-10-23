package com.gtocore.data.lootTables.GTOLootItemFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.jetbrains.annotations.NotNull;

public class CustomLogicNumberProvider implements NumberProvider {

    public static final CustomLogicNumberProvider INSTANCE = new CustomLogicNumberProvider((p, l, e, pos, t) -> 0);

    @FunctionalInterface
    public interface NumberLogic {

        int calculate(ServerPlayer player, ServerLevel level, Entity entity, BlockPos pos, ItemStack tool);
    }

    private final NumberLogic logic;

    public CustomLogicNumberProvider(NumberLogic logic) {
        this.logic = logic;
    }

    @Override
    public int getInt(LootContext context) {
        ServerLevel level = context.getLevel();
        if (level.isClientSide()) return 0;

        ServerPlayer player = extractPlayer(context);
        Entity relatedEntity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        BlockPos pos = extractPosition(context);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);

        return logic.calculate(player, level, relatedEntity, pos, tool);
    }

    @Override
    public float getFloat(@NotNull LootContext context) {
        return (float) getInt(context);
    }

    @Override
    public @NotNull LootNumberProviderType getType() {
        return GTONumberProviders.CUSTOM_LOGIC.get();
    }

    private ServerPlayer extractPlayer(LootContext context) {
        Entity looter = context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
        return looter instanceof ServerPlayer ? (ServerPlayer) looter : null;
    }

    private BlockPos extractPosition(LootContext context) {
        return context.getParamOrNull(LootContextParams.ORIGIN) != null ? BlockPos.containing(context.getParam(LootContextParams.ORIGIN)) : BlockPos.ZERO;
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
