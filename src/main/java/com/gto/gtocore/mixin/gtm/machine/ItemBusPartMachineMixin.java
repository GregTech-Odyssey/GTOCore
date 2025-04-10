package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.feature.multiblock.IDistinctRecipeHolder;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBusPartMachine.class)
public class ItemBusPartMachineMixin extends TieredIOPartMachine {

    @Shadow(remap = false)
    private boolean isDistinct;

    public ItemBusPartMachineMixin(IMachineBlockEntity holder, int tier, IO io) {
        super(holder, tier, io);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected int getInventorySize() {
        int sizeRoot = 1 + getTier();
        return sizeRoot * sizeRoot;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void setDistinct(boolean distinct) {
        isDistinct = (io != IO.OUT && distinct);
        getHandlerList().setDistinctAndNotify(isDistinct);
        for (IMultiController controller : getControllers()) {
            if (controller instanceof IDistinctRecipeHolder recipeHolder) {
                recipeHolder.arrangeDistinct();
            }
        }
    }
}
