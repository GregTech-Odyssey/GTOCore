package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.common.data.GTOLoots;

import com.gtolib.GTOCore;
import com.gtolib.api.item.ItemStackSet;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

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

import com.gto.datasynclib.util.holder.IntHolder;

import static net.minecraft.sounds.SoundEvents.*;
import static net.minecraft.world.item.Items.GOLD_INGOT;
import static net.minecraft.world.level.Level.NETHER;

public class ElfExchangeMachine extends ManaMultiblockMachine implements ICustomRecipeLogicHolder {

    private PiglinMerchant piglin;

    public ElfExchangeMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    private int piglinSoundPlayCD = 0;
    private static final SoundEvent[] soundEntries = new SoundEvent[] {
            PIGLIN_CELEBRATE, PIGLIN_JEALOUS, PIGLIN_ADMIRING_ITEM, PIGLIN_AMBIENT, PIGLIN_RETREAT
    };

    @Override
    public void onWorking() {
        if (piglinSoundPlayCD > 0) piglinSoundPlayCD--;
        else if (piglin != null && getLevel() instanceof ServerLevel level) {
            SoundEvent soundEvent = soundEntries[level.random.nextInt(soundEntries.length)];
            level.playSound(null, getPos(), soundEvent, SoundSource.BLOCKS);
            piglinSoundPlayCD = 10 + level.random.nextInt(100);
        }
        super.onWorking();
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        int mode = unit.getCircuit(false);
        if (getLevel() instanceof ServerLevel level && level.dimension() == NETHER && mode > 0) {
            RecipeBuilder builder = getRecipeBuilder().duration(120).MANAt(10);
            LootTable lootTable = level.getServer().getLootData().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);
            if (piglin == null) piglin = new PiglinMerchant(level);

            LootParams lootContext = new LootParams.Builder(level)
                    .withParameter(LootContextParams.THIS_ENTITY, piglin)
                    .create(LootContextParamSets.PIGLIN_BARTER);
            ItemStackSet itemStacks = new ItemStackSet();

            var maxParallel = ParallelLogic.getMaxParallelAmount(this, unit, builder.copy(GTOCore.id("test")).inputItems(GOLD_INGOT).outputItems(Items.STICK).build().toRuntime(), MachineUtils.getHatchParallel(this));
            if (maxParallel == 0) return null;
            IntHolder nbt = new IntHolder();
            builder.MANAt(10 * maxParallel);
            builder.inputItems(ItemIngredient.of(GOLD_INGOT, maxParallel));
            var parallel = Math.min(1024, maxParallel);
            var multiplier = maxParallel / parallel;
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
            return builder.build();
        }
        return null;
    }

    @Override
    public boolean alwaysSearchRecipe() {
        return true;
    }

    @Override
    public boolean searchRecipe() {
        return true;
    }

    private static class PiglinMerchant extends Piglin {

        PiglinMerchant(ServerLevel level) {
            super(EntityType.PIGLIN, level);
        }
    }
}
