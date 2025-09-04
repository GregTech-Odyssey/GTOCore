package com.gtocore.mixin.gtm.machine;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DataAccessHatchMachine;

import net.minecraft.world.item.Item;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

@Mixin(DataAccessHatchMachine.class)
public class DataAccessHatchMachineMixin extends TieredPartMachine {

    @Unique
    private static final Map<Item, Integer> gtolib$DATA = Map.of(
            GTOItems.NEURAL_MATRIX.asItem(), GTValues.UHV,
            GTOItems.ATOMIC_ARCHIVES.asItem(), GTValues.UIV,
            GTOItems.OBSIDIAN_MATRIX.asItem(), GTValues.UXV,
            GTOItems.MICROCOSM.asItem(), GTValues.OpV);

    @Shadow(remap = false)
    @Final
    public NotifiableItemStackHandler importItems;

    @Shadow(remap = false)
    @Final
    private Set<GTRecipe> recipes;

    @Shadow(remap = false)
    @Final
    private boolean isCreative;

    public DataAccessHatchMachineMixin(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(MetaMachineBlockEntity holder, int tier, boolean isCreative, CallbackInfo ci) {
        importItems.setFilter(i -> gtolib$DATA.getOrDefault(i.getItem(), 0) <= tier);
    }

    @Inject(method = "getInventorySize", at = @At("TAIL"), remap = false, cancellable = true)
    private void getInventorySize(CallbackInfoReturnable<Integer> cir) {
        switch (getTier()) {
            case GTValues.UHV: {
                cir.setReturnValue(25);
                break;
            }
            case GTValues.UIV: {
                cir.setReturnValue(36);
                break;
            }
            case GTValues.OpV: {
                cir.setReturnValue(49);
                break;
            }
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public @Nullable GTRecipe modifyRecipe(GTRecipe recipe) {
        if (this.isCreative) return recipe;
        if (recipe instanceof Recipe gtoRecipe) {
            var root = gtoRecipe.rootRecipe;
            if (root == null || root.researchData == null) return recipe;
            if (recipes.contains(root)) return recipe;
        }
        return null;
    }
}
