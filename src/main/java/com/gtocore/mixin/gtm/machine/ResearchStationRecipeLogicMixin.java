package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.capability.IObjectHolder;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.research.ResearchStationMachine;

import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ResearchStationMachine.ResearchStationRecipeLogic.class)
public class ResearchStationRecipeLogicMixin extends RecipeLogic {

    public ResearchStationRecipeLogicMixin(IRecipeLogicMachine machine) {
        super(machine);
    }

    @Redirect(method = "handleRecipeIO", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/capability/IObjectHolder;setDataItem(Lnet/minecraft/world/item/ItemStack;)V"), remap = false)
    public void setDataItem(IObjectHolder instance, ItemStack itemStack) {
        instance.setDataItem(itemStack.copy());
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected boolean matchTickRecipeNoOutput(GTRecipe recipe) {
        return RecipeRunner.matchTickRecipe(machine, (Recipe) recipe);
    }
}
