package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.data.GTOLoots;

import com.gtolib.GTOCore;
import com.gtolib.api.item.ItemStackSet;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;
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
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.sounds.SoundEvents.*;

public class FishingGroundMachine extends ElectricMultiblockMachine {

    private static final ItemStack FISHING_ROD = new ItemStack(Items.FISHING_ROD);

    private FishingHook fishingHook;

    public FishingGroundMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Nullable
    private Recipe getRecipe() {
        Recipe recipe = null;
        int mode = checkingCircuit(false);
        if (mode > 0) {
            RecipeBuilder builder = getRecipeBuilder().duration(20).EUt(480);
            if (getLevel() instanceof ServerLevel level) {
                LootTable lootTable = level.getServer().getLootData().getLootTable(switch (mode) {
                    case 2 -> BuiltInLootTables.FISHING_FISH;
                    case 3 -> BuiltInLootTables.FISHING_JUNK;
                    case 4 -> BuiltInLootTables.FISHING_TREASURE;
                    default -> BuiltInLootTables.FISHING;
                });
                if (fishingHook == null) fishingHook = new MyFishingHook(level);
                LootParams lootContext = new LootParams.Builder(level)
                        .withOptionalParameter(LootContextParams.THIS_ENTITY, fishingHook)
                        .withParameter(LootContextParams.TOOL, FISHING_ROD)
                        .withParameter(LootContextParams.ORIGIN, new Vec3(getPos().getX(), getPos().getY(), getPos().getZ()))
                        .create(LootContextParamSets.FISHING);
                ItemStackSet itemStacks = new ItemStackSet();
                recipe = ParallelLogic.accurateParallel(this, builder.copy(GTOCore.id("test")).outputItems(Items.STICK).buildRawRecipe(), MachineUtils.getHatchParallel(this));
                if (recipe == null) return null;
                IntHolder nbt = new IntHolder(0);
                builder.EUt(recipe.getInputEUt());
                var parallel = Math.min(1024, recipe.parallels);
                var multiplier = recipe.parallels / parallel;
                GTOLoots.modifyLoot = false;
                for (int i = 0; i < parallel; i++) {
                    lootTable.getRandomItems(lootContext).forEach(itemStack -> {
                        if (itemStack.hasTag()) {
                            if (nbt.value > 100) return;
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
            }
        } else {
            recipe = getRecipeType().lookup().findRecipe(this);
            if (recipe != null) {
                recipe = fullModifyRecipe(recipe.copy());
                fishingHook = null;
            }
        }
        if (recipe != null && RecipeRunner.matchRecipe(this, recipe) && RecipeRunner.matchTickRecipe(this, recipe)) {
            return recipe;
        }
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    private int piglinSoundPlayCD = 0;
    private static final SoundEvent[] soundEntries = new SoundEvent[] {
            FISHING_BOBBER_RETRIEVE, FISHING_BOBBER_SPLASH, FISHING_BOBBER_THROW, FISH_SWIM
    };

    @Override
    public boolean onWorking() {
        if (piglinSoundPlayCD > 0) piglinSoundPlayCD--;
        else if (fishingHook != null && getLevel() instanceof ServerLevel level) {
            SoundEvent soundEvent = soundEntries[level.random.nextInt(soundEntries.length)];
            level.playSound(null, getPos(), soundEvent, SoundSource.BLOCKS);
            piglinSoundPlayCD = 10 + level.random.nextInt(100);
        }
        return super.onWorking();
    }

    private static class MyFishingHook extends FishingHook {

        MyFishingHook(ServerLevel level) {
            super(EntityType.FISHING_BOBBER, level);
        }

        @Override
        public boolean isOpenWaterFishing() {
            return true;
        }
    }
}
