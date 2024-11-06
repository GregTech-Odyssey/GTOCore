package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.trait.NotifiableCircuitItemStackHandler;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBusPartMachine.class)
public class ItemBusPartMachineMixin extends TieredIOPartMachine {

    public ItemBusPartMachineMixin(IMachineBlockEntity holder, int tier, IO io) {
        super(holder, tier, io);
    }

    @Shadow(remap = false)
    protected int getInventorySize() {
        return 0;
    }

    @Inject(method = "getInventorySize", at = @At("HEAD"), remap = false, cancellable = true)
    protected void inventorySize(CallbackInfoReturnable<Integer> cir) {
        int sizeRoot = 1 + getTier();
        cir.setReturnValue(sizeRoot * sizeRoot);
    }

    @Inject(method = "createInventory", at = @At("HEAD"), remap = false, cancellable = true)
    protected void createInventory(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        if (io == IO.IN && this.getDefinition() != GTMachines.STEAM_IMPORT_BUS) {
            cir.setReturnValue(new NotifiableItemStackHandler(this, getInventorySize(), IO.IN)
                    .setFilter((itemStack -> !IntCircuitBehaviour.isIntegratedCircuit(itemStack))));
        }
    }

    @Inject(method = "createCircuitItemHandler", at = @At("HEAD"), remap = false, cancellable = true)
    protected void createCircuitItemHandler(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        if (args.length > 0 && args[0] instanceof IO i && i == IO.IN) {
            cir.setReturnValue(new NotifiableCircuitItemStackHandler(this));
        }
    }
}
