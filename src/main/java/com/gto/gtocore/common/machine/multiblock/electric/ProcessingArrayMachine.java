package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.item.MachineItemStackHandler;
import com.gto.gtocore.api.machine.feature.IArrayMachine;
import com.gto.gtocore.api.machine.feature.IParallelMachine;
import com.gto.gtocore.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
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
import lombok.Getter;
import lombok.Setter;

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

    @Getter
    @Setter
    private MachineDefinition machineDefinitionCache;
    @Getter
    @Setter
    private GTRecipeType[] RecipeTypeCache;

    @Getter
    @DescSynced
    @Persisted
    private final NotifiableItemStackHandler inventory;

    private final int arrayTier;

    public ProcessingArrayMachine(IMachineBlockEntity holder, int tier) {
        super(holder, GTOValues.GLASS_TIER);
        this.arrayTier = tier;
        inventory = createMachineStorage();
    }

    private NotifiableItemStackHandler createMachineStorage() {
        NotifiableItemStackHandler storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH, slots -> new MachineItemStackHandler(() -> getMachineLimit(arrayTier), this::onStorageChanged));
        storage.setFilter(i -> storageFilter(i, this));
        return storage;
    }

    private static boolean storageFilter(ItemStack itemStack, ProcessingArrayMachine machine) {
        if (itemStack.getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            if (definition instanceof MultiblockMachineDefinition) return false;
            if (definition.getTier() > machine.getCasingTiers().get(GTOValues.GLASS_TIER)) return false;
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
    public GTRecipeType getRecipeType() {
        return recipeTypes()[getActiveRecipeType()];
    }

    @Override
    public int getTier() {
        MachineDefinition definition = getMachineDefinition();
        return Math.min(definition == null ? 0 : definition.getTier(), tier);
    }

    @Override
    @Nullable
    public GTRecipe getRealRecipe(GTRecipe recipe) {
        if (!inventory.getStackInSlot(0).isEmpty()) {
            return GTORecipeModifiers.laserLossOverclocking(this, GTORecipeModifiers.accurateParallel(this, recipe, getMaxParallel()));
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
    public int getMaxParallel() {
        return Math.min(getMachineLimit(arrayTier), Math.max(1, inventory.getStackInSlot(0).getCount()));
    }

    @Override
    public Item getStorageItem() {
        return inventory.getStackInSlot(0).getItem();
    }
}
