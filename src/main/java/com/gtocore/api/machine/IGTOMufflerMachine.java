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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;

public interface IGTOMufflerMachine extends IMufflerMachine, IMultiPart, IControllable {

    ItemStack ASH = ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Ash);
    ItemStack NeutronPile = ModItems.neutron_pile.get().getDefaultInstance();

    int getAshNotProduceChance();

    default boolean gtolib$isValid() {
        return GTOConfig.INSTANCE.disableMufflerPart || getAshNotProduceChance() == 100;
    }

    @Override
    default GTRecipe modifyRecipe(GTRecipe recipe) {
        if (gtolib$isValid()) return recipe;
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
    default boolean onWorking(IWorkableMultiController controller) {
        if (gtolib$isValid() && !isWorkingEnabled()) return true;
        if (gto$checkAshFull()) return false;
        if (controller.getRecipeLogic().progress % 80 == 0) {
            List<LivingEntity> entities = self().getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(self().getPos().relative(this.self().getFrontFacing())));
            entities.forEach(e -> {
                e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 2));
                e.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 1));
            });
            gtolib$insertAsh(controller);
        }
        return true;
    }

    @Override
    default boolean afterWorking(IWorkableMultiController controller) {
        gtolib$insertAsh(controller);
        return true;
    }

    default boolean beforeWorking(IWorkableMultiController controller) {
        if (gtolib$isValid()) return true;
        if (gto$checkAshFull()) {
            for (var c : getControllers()) {
                if (c instanceof IRecipeLogicMachine recipeLogicMachine && recipeLogicMachine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                    enhancedRecipeLogic.gtolib$setIdleReason(IdleReason.MUFFLER_OBSTRUCTED.reason());
                }
            }
            return false;
        }
        return true;
    }

    boolean gto$checkAshFull();
    boolean gto$getAshFull();

    default void gtolib$insertAsh(IWorkableMultiController controller) {
        var count=getAshNotProduceChance();
        while ((count-=GTValues.RNG.nextInt(100))>=0){
            if (isWorkingEnabled()) {
                GTRecipe lastRecipe = controller.getRecipeLogic().getLastRecipe();
                ItemStack ash = null;
                if (lastRecipe != null && lastRecipe.getInputEUt() >= GTValues.V[GTValues.UV] && GTValues.RNG.nextFloat() < 1e-3f) {
                    ash = NeutronPile;
                } else {
                    MultiblockMachineBuilder.MufflerProductionGenerator supplier = controller.self().getDefinition().getRecoveryItems();
                    if (supplier != null) {
                        ash = supplier.getMuffledProduction(controller.self(), lastRecipe);
                    }

                }
                recoverItemsTable(ash);
            }
        }
        if (GTValues.RNG.nextInt(100) >= getAshNotProduceChance() &&(GTOCore.isExpert() || GTValues.RNG.nextBoolean())) {
            recoverItemsTable(ASH);
        }
    }
}
