package com.gtocore.api.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMufflerMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gtocore.config.GTOConfig;
import com.gtolib.GTOCore;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.IdleReason;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.world.item.ItemStack;

public interface IGTOMufflerMachine extends IMufflerMachine, IMultiPart, IControllable {

    ItemStack ASH = ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Ash);
    ItemStack NEUTRON_PILE = ModItems.neutron_pile.get().getDefaultInstance();

    int gtolib$getRecoveryChance();

    default boolean isMufflerValid() {
        return GTOConfig.INSTANCE.disableMufflerPart || gtolib$getRecoveryChance() == 100;
    }

    @Override
    default GTRecipe modifyRecipe(GTRecipe recipe) {
        if (isMufflerValid()) return recipe;
        if (!isFrontFaceFree()) {
            for (var c : getControllers()) {
                if (c instanceof IRecipeLogicMachine recipeLogicMachine && recipeLogicMachine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                    enhancedRecipeLogic.gtolib$setIdleReason(IdleReason.MUFFLER_OBSTRUCTED.reason());
                }
            }
            return null;
        }
        return recipe;
    }

    @Override
    default boolean beforeWorking(IWorkableMultiController controller) {
        if (isMufflerValid()) return true;
        if (gtolib$checkAshFull()) {
            for (var c : getControllers()) {
                if (c instanceof IRecipeLogicMachine recipeLogicMachine && recipeLogicMachine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                    enhancedRecipeLogic.gtolib$setIdleReason(IdleReason.MUFFLER_OBSTRUCTED.reason());
                }
            }
            return false;
        }
        return true;
    }

    @Override
    default boolean onWorking(IWorkableMultiController controller) {
        if (isMufflerValid() && !isWorkingEnabled()) return true;
        if (controller.getRecipeLogic().progress % 80 == 0) {
            if (GTOCore.isExpert() && gtolib$checkAshFull()) return false;
            gtolib$addMufflerEffect();
            gtolib$insertAsh(controller);
        }
        return true;
    }

    @Override
    default boolean afterWorking(IWorkableMultiController controller) {
        gtolib$insertAsh(controller);
        return true;
    }

    default boolean gtolib$checkAshFull(){return false;};

    default void gtolib$insertAsh(IWorkableMultiController controller) {
        var count = gtolib$getRecoveryChance();
        if(count>=GTValues.RNG.nextInt(100)) {
            if (isWorkingEnabled()) {
                GTRecipe lastRecipe = controller.getRecipeLogic().getLastRecipe();
                if (lastRecipe != null && lastRecipe.getInputEUt() >= GTValues.V[GTValues.UV] && GTValues.RNG.nextFloat() < 1e-5f*count) {
                    ItemStack ash = NEUTRON_PILE.copy();
                    if(count>100)
                        ash.setCount((int) (count/1e5));
                    recoverItemsTable(ash);
                }
                if(GTValues.RNG.nextBoolean()){
                    MultiblockMachineBuilder.MufflerProductionGenerator supplier = controller.self().getDefinition().getRecoveryItems();
                    if (supplier != null) {
                        ItemStack ash = supplier.getMuffledProduction(controller.self(), lastRecipe);
                        if(count>100)
                            ash.setCount((int) (count/100));
                        recoverItemsTable(ash);
                    }

                }
            }
        }else if(GTOCore.isExpert() || GTValues.RNG.nextBoolean()) {
            recoverItemsTable(ASH);
        }
    }

    default void gtolib$addMufflerEffect() {
    }

}
