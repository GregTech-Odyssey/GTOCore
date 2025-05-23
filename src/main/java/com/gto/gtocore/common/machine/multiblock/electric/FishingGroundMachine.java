package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.item.ItemStackSet;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.server.level.ServerLevel;
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

public class FishingGroundMachine extends ElectricMultiblockMachine {

    private static final ItemStack FISHING_ROD = new ItemStack(Items.FISHING_ROD);

    private FishingHook fishingHook;

    public FishingGroundMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (!hasProxies()) return null;
        GTRecipe recipe = null;
        int mode = MachineUtils.checkingCircuit(this, false);
        if (mode > 0) {
            GTORecipeBuilder builder = GTORecipeBuilder.ofRaw().duration(20).EUt(480);
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
                recipe = GTORecipeModifiers.accurateParallel(this, builder.copy(GTOCore.id("test")).outputItems(Items.STICK).buildRawRecipe(), MachineUtils.getHatchParallel(this));
                builder.EUt(RecipeHelper.getInputEUt(recipe));
                for (int i = 0; i < recipe.parallels; i++) {
                    if (GTValues.RNG.nextInt(100) > 99) {
                        itemStacks.add(GTItems.COIN_GOLD_ANCIENT.asStack());
                    } else {
                        itemStacks.addAll(lootTable.getRandomItems(lootContext));
                    }
                }
                itemStacks.forEach(builder::outputItems);
                recipe = builder.buildRawRecipe();
            }
        } else {
            GTRecipe match = getRecipeType().getLookup().findRecipe(this);
            if (match != null) {
                recipe = fullModifyRecipe(match.copy());
            }
        }
        if (recipe != null && recipe.matchRecipe(this).isSuccess() && recipe.matchTickRecipe(this).isSuccess()) {
            return recipe;
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public @NotNull CustomRecipeLogic getRecipeLogic() {
        return (CustomRecipeLogic) super.getRecipeLogic();
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
