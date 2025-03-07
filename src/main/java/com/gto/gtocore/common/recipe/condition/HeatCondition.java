package com.gto.gtocore.common.recipe.condition;

import com.gto.gtocore.api.machine.feature.IHeaterMachine;
import com.gto.gtocore.common.data.GTORecipeConditions;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public final class HeatCondition extends RecipeCondition {

    public static final Codec<HeatCondition> CODEC = RecordCodecBuilder
            .create(instance -> isReverse(instance)
                    .and(Codec.INT.fieldOf("temperature").forGetter(val -> val.temperature))
                    .apply(instance, HeatCondition::new));

    private int temperature;

    public HeatCondition(int temperature) {
        this.temperature = temperature;
    }

    private HeatCondition(boolean isReverse, int temperature) {
        super(isReverse);
        this.temperature = temperature;
    }

    @Override
    public RecipeConditionType<?> getType() {
        return GTORecipeConditions.HEAT;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("gtocore.recipe.heat.temperature", temperature);
    }

    @Override
    public boolean test(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        MetaMachine machine = recipeLogic.getMachine();
        Level level = machine.getLevel();
        BlockPos pos = machine.getPos();
        if (level != null) {
            for (Direction side : GTUtil.DIRECTIONS) {
                if (checkNeighborHeat(level, pos.relative(side))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkNeighborHeat(Level level, BlockPos neighborPos) {
        if (MetaMachine.getMachine(level, neighborPos) instanceof IHeaterMachine heaterMachine) {
            return heaterMachine.getTemperature() >= temperature;
        }
        return false;
    }

    @Override
    public RecipeCondition createTemplate() {
        return new HeatCondition();
    }

    @NotNull
    @Override
    public JsonObject serialize() {
        JsonObject config = super.serialize();
        config.addProperty("temperature", temperature);
        return config;
    }

    @Override
    public RecipeCondition deserialize(@NotNull JsonObject config) {
        super.deserialize(config);
        temperature = GsonHelper.getAsInt(config, "temperature", 0);
        return this;
    }

    @Override
    public RecipeCondition fromNetwork(FriendlyByteBuf buf) {
        super.fromNetwork(buf);
        temperature = buf.readInt();
        return this;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        super.toNetwork(buf);
        buf.writeInt(temperature);
    }
}
