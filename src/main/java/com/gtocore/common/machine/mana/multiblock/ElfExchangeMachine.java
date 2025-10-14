package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.common.data.GTOLoots;

import com.gtolib.GTOCore;
import com.gtolib.api.item.ItemStackSet;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.MathUtil;
import com.gtolib.utils.holder.IntHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.sounds.SoundEvents.*;
import static net.minecraft.world.item.Items.GOLD_INGOT;
import static net.minecraft.world.level.Level.NETHER;

public class ElfExchangeMachine extends ManaMultiblockMachine {

    private PiglinMerchant piglin;

    public ElfExchangeMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Nullable
    private Recipe getRecipe() {
        Recipe recipe;
        int mode = checkingCircuit(false);
        if (getLevel() instanceof ServerLevel level && level.dimension() == NETHER && mode > 0) {
            RecipeBuilder builder = getRecipeBuilder().duration(120).MANAt(10);
            LootTable lootTable = level.getServer().getLootData().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);
            if (piglin == null) piglin = new PiglinMerchant(level);

            LootParams lootContext = new LootParams.Builder(level)
                    .withParameter(LootContextParams.THIS_ENTITY, piglin)
                    .create(LootContextParamSets.PIGLIN_BARTER);
            ItemStackSet itemStacks = new ItemStackSet();

            recipe = ParallelLogic.accurateParallel(this, builder.copy(GTOCore.id("test")).inputItems(GOLD_INGOT).outputItems(Items.STICK).buildRawRecipe(), MachineUtils.getHatchParallel(this));
            if (recipe == null) return null;
            IntHolder nbt = new IntHolder(0);
            builder.MANAt(recipe.getInputMANAt());
            builder.inputItems(FastSizedIngredient.create(GOLD_INGOT, recipe.parallels));
            var parallel = Math.min(1024, recipe.parallels);
            var multiplier = recipe.parallels / parallel;
            GTOLoots.modifyLoot = false;
            for (int i = 0; i < parallel; i++) {
                lootTable.getRandomItems(lootContext).forEach(itemStack -> {
                    if (itemStack.hasTag()) {
                        if (mode == 2 || nbt.value > 100) return;
                        nbt.value++;
                    }
                    itemStacks.add(itemStack);
                });
            }
            GTOLoots.modifyLoot = true;
            itemStacks.forEach(i -> {
                if (multiplier > 1) {
                    i.setCount(MathUtil.saturatedCast(i.getCount() * multiplier));
                }
                builder.outputItems(i);
            });
            recipe = builder.buildRawRecipe();
        } else {
            recipe = getRecipeType().lookup().findRecipe(this);
            if (recipe != null) {
                recipe = (Recipe) fullModifyRecipe(recipe.copy());
                piglin = null;
            }
        }
        if (recipe != null && RecipeRunner.matchRecipe(this, recipe) && RecipeRunner.matchTickRecipe(this, recipe)) {
            return recipe;
        }
        return null;
    }

    private int piglinSoundPlayCD = 0;
    private static final SoundEvent[] soundEntries = new SoundEvent[] {
            PIGLIN_CELEBRATE, PIGLIN_JEALOUS, PIGLIN_ADMIRING_ITEM, PIGLIN_AMBIENT, PIGLIN_RETREAT
    };

    @Override
    public boolean onWorking() {
        if (piglinSoundPlayCD > 0) piglinSoundPlayCD--;
        else if (piglin != null && getLevel() instanceof ServerLevel level) {
            SoundEvent soundEvent = soundEntries[level.random.nextInt(soundEntries.length)];
            level.playSound(null, getPos(), soundEvent, SoundSource.BLOCKS);
            piglinSoundPlayCD = 10 + level.random.nextInt(100);
        }
        return super.onWorking();
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    private static class PiglinMerchant extends Piglin {

        PiglinMerchant(ServerLevel level) {
            super(EntityType.PIGLIN, level);
        }
    }
}
