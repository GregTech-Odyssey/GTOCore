package com.gto.gtocore.common.recipe.condition;

import com.gto.gtocore.common.data.GTORecipeConditions;
import com.gto.gtocore.common.saved.RecipeRunLimitSavaedData;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.common.machine.owner.IMachineOwner;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@NoArgsConstructor
public final class RunLimitCondition extends RecipeCondition {

    public static final Codec<RunLimitCondition> CODEC = RecordCodecBuilder
            .create(instance -> isReverse(instance)
                    .and(Codec.INT.fieldOf("count").forGetter(val -> val.count))
                    .apply(instance, RunLimitCondition::new));

    private int count;

    public RunLimitCondition(int count) {
        this.count = count;
    }

    private RunLimitCondition(boolean isReverse, int count) {
        super(isReverse);
        this.count = count;
    }

    @Override
    public RecipeConditionType<?> getType() {
        return GTORecipeConditions.RUN_LIMIT;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("gtocore.recipe.runlimit.count", count);
    }

    @Override
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        MetaMachine machine = recipeLogic.getMachine();
        IMachineOwner owner = machine.holder.getOwner();
        if (owner == null) return false;
        UUID uuid = owner.getUUID();
        if (uuid == null) return false;
        int runLimit = RecipeRunLimitSavaedData.get(uuid, recipe.id);
        if (runLimit < count) {
            RecipeRunLimitSavaedData.set(uuid, recipe.id, runLimit + 1);
            return true;
        }
        return false;
    }

    @Override
    public RecipeCondition createTemplate() {
        return new RunLimitCondition();
    }

    @NotNull
    @Override
    public JsonObject serialize() {
        JsonObject config = super.serialize();
        config.addProperty("count", count);
        return config;
    }

    @Override
    public RecipeCondition deserialize(@NotNull JsonObject config) {
        super.deserialize(config);
        count = GsonHelper.getAsInt(config, "count", 0);
        return this;
    }

    @Override
    public RecipeCondition fromNetwork(FriendlyByteBuf buf) {
        super.fromNetwork(buf);
        count = buf.readInt();
        return this;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        super.toNetwork(buf);
        buf.writeInt(count);
    }
}
