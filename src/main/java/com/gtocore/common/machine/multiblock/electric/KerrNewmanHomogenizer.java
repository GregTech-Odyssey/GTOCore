package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.client.renderer.fx.BlackHole;
import com.gtocore.client.renderer.fx.FXManager;

import com.gtolib.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.world.phys.Vec3;

public final class KerrNewmanHomogenizer extends CrossRecipeMultiblockMachine {

    private TickableSubscription clientTicker;
    private boolean fxActive = false;

    public KerrNewmanHomogenizer(MetaMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallel);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        clientTicker = subscribeClientTick(this::tickClient, 6);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (clientTicker != null) {
            clientTicker.unsubscribe();
            clientTicker = null;
        }
        fxActive = false;
    }

    @Override
    public void onStructureInvalidClient() {
        super.onStructureInvalidClient();
        fxActive = false;
    }

    private void tickClient() {
        if (isActive()) {
            KerrNewmanFX.addFX(this);
        } else if (fxActive) {
            fxActive = false;
        }
    }

    private static class KerrNewmanFX {

        private static void addFX(KerrNewmanHomogenizer machine) {
            if (!machine.fxActive) {
                var back = machine.getFrontFacing().getOpposite();
                Vec3 center = new Vec3(
                        0.5 + 34 * back.getStepX() + machine.getPos().getX(),
                        0.5 + 34 * back.getStepY() + machine.getPos().getY(),
                        0.5 + 34 * back.getStepZ() + machine.getPos().getZ());
                FXManager.addFX(new BlackHole(center, 2.5f, 4.5f) {

                    @Override
                    public void tick() {
                        super.tick();
                        if (!machine.fxActive) markEnding();
                    }
                });
                machine.fxActive = true;
            }
        }
    }
}
