package com.gtocore.api.machine;

import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.IdleReason;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMufflerMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;

import committee.nova.mods.avaritia.init.registry.ModItems;

public interface IGTOMufflerMachine extends IMufflerMachine, IControllable, ITieredMachine {

    ItemStack ASH = ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Ash);
    ItemStack NEUTRON_PILE = ModItems.neutron_pile.get().getDefaultInstance();

    int gtolib$getRecoveryChance();

    default boolean isMufflerPulseDisabled() {
        return (GTOConfig.INSTANCE.disableMufflerPart && !GTOCore.isExpert()) || gtolib$getRecoveryChance() == 100;
    }

    @Override
    default GTRecipe modifyRecipe(GTRecipe recipe) {
        if (isMufflerPulseDisabled()) return recipe;
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
        if (isMufflerPulseDisabled()) return true;
        if (GTOCore.isExpert() && controller instanceof ITieredMachine machine && machine.getTier() > getTier() + 1) {
            if (controller.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                enhancedRecipeLogic.gtolib$setIdleReason(IdleReason.MUFFLER_INSUFFICIENT.reason());
            }
            return false;
        }
        if (gtolib$checkAshFull()) {
            if (controller.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                enhancedRecipeLogic.gtolib$setIdleReason(IdleReason.MUFFLER_OBSTRUCTED.reason());
            }
            return false;
        }
        return true;
    }

    @Override
    default boolean onWorking(IWorkableMultiController controller) {
        if (isMufflerPulseDisabled() && !isWorkingEnabled()) return true;
        if (controller.getRecipeLogic().progress % 80 == 0) {
            if (GTOCore.isExpert() && !isMufflerPulseDisabled() && gtolib$checkAshFull()) return false;
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

    default boolean gtolib$checkAshFull() {
        return false;
    }

    default void gtolib$insertAsh(IWorkableMultiController controller) {
        var count = gtolib$getRecoveryChance();
        if (count >= 100 || count >= GTValues.RNG.nextInt(100)) {
            if (isWorkingEnabled()) {
                GTRecipe lastRecipe = controller.getRecipeLogic().getLastRecipe();
                if (lastRecipe != null && lastRecipe.getInputEUt() >= GTValues.V[GTValues.UV] && GTValues.RNG.nextFloat() < 1e-5f * count) {
                    ItemStack ash = NEUTRON_PILE.copy();
                    if (count > 1e5)
                        ash.setCount((int) (count / 1e5) + (count >= GTValues.RNG.nextInt((int) 1e5) ? 0 : 1));
                    recoverItemsTable(ash);
                }
                if (GTValues.RNG.nextBoolean()) {
                    MultiblockMachineBuilder.MufflerProductionGenerator supplier = controller.self().getDefinition().getRecoveryItems();
                    if (supplier != null) {
                        ItemStack ash = supplier.getMuffledProduction(controller.self(), lastRecipe).copy();
                        if (count > 100)
                            ash.setCount(count / 100 + (count >= GTValues.RNG.nextInt(100) ? 0 : 1));
                        recoverItemsTable(ash);
                    }
                }
            }
        } else if (GTOCore.isExpert() || GTValues.RNG.nextBoolean()) {
            recoverItemsTable(ASH);
        }
    }

    default void gtolib$addMufflerEffect() {}
}
