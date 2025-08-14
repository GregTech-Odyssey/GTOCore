package com.gtocore.common.machine.multiblock.electric.processing;

import com.gtolib.api.GTOValues;
import com.gtolib.api.item.MachineItemStackHandler;
import com.gtolib.api.machine.feature.multiblock.IArrayMachine;
import com.gtolib.api.machine.feature.multiblock.IParallelMachine;
import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeType;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProcessingArrayMachine extends TierCasingMultiblockMachine implements IParallelMachine, IArrayMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ProcessingArrayMachine.class, TierCasingMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private MachineDefinition machineDefinitionCache;
    private GTRecipeType[] RecipeTypeCache;
    @DescSynced
    @Persisted
    private final NotifiableItemStackHandler inventory;
    private final int arrayTier;

    public ProcessingArrayMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, GTOValues.GLASS_TIER);
        this.arrayTier = tier;
        inventory = createMachineStorage();
    }

    private NotifiableItemStackHandler createMachineStorage() {
        NotifiableItemStackHandler storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH, slots -> new MachineItemStackHandler(() -> getMachineLimit(arrayTier)));
        storage.setFilter(i -> storageFilter(i, getCasingTier(GTOValues.GLASS_TIER)));
        storage.addChangedListener(this::onStorageChanged);
        return storage;
    }

    static boolean storageFilter(ItemStack itemStack, int tier) {
        if (itemStack.getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            if (definition instanceof MultiblockMachineDefinition) return false;
            if (definition.getTier() > tier) return false;
            GTRecipeType[] recipeTypes = definition.getRecipeTypes();
            if (recipeTypes != null && recipeTypes.length == 1) {
                return GTRecipeTypes.ELECTRIC.equals(recipeTypes[0].group);
            }
        }
        return false;
    }

    @Override
    public GTRecipeType[] getRecipeTypes() {
        return recipeTypes();
    }

    @Override
    public RecipeType getRecipeType() {
        return (RecipeType) recipeTypes()[getActiveRecipeType()];
    }

    @Override
    public int getTier() {
        MachineDefinition definition = getMachineDefinition();
        return Math.min(definition == null ? 0 : definition.getTier(), tier);
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        if (inventory.getStackInSlot(0).isEmpty()) return false;
        return super.beforeWorking(recipe);
    }

    @Override
    @Nullable
    public Recipe getRealRecipe(Recipe recipe) {
        if (!inventory.getStackInSlot(0).isEmpty()) {
            return RecipeModifierFunction.laserLossOverclocking(this, ParallelLogic.accurateParallel(this, recipe, (int) getMaxParallel()));
        }
        return null;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        MachineUtils.addRecipeTypeText(textList, this);
    }

    public static Block getCasingState(int tier) {
        return tier == GTValues.IV ? GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get() : GTBlocks.CASING_HSSE_STURDY.get();
    }

    public static int getMachineLimit(Integer tier) {
        return tier == GTValues.IV ? 16 : 64;
    }

    @Override
    public long getMaxParallel() {
        return Math.min(getMachineLimit(arrayTier), Math.max(1, inventory.getStackInSlot(0).getCount()));
    }

    @Override
    public Item getStorageItem() {
        return inventory.getStackInSlot(0).getItem();
    }

    @Override
    public void setMachineDefinitionCache(final MachineDefinition machineDefinitionCache) {
        this.machineDefinitionCache = machineDefinitionCache;
    }

    @Override
    public MachineDefinition getMachineDefinitionCache() {
        return this.machineDefinitionCache;
    }

    @Override
    public void setRecipeTypeCache(final GTRecipeType[] RecipeTypeCache) {
        this.RecipeTypeCache = RecipeTypeCache;
    }

    @Override
    public GTRecipeType[] getRecipeTypeCache() {
        return this.RecipeTypeCache;
    }

    public NotifiableItemStackHandler getInventory() {
        return this.inventory;
    }
}
