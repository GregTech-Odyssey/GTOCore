package com.gtocore.mixin.mae2;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;

import net.minecraft.core.Direction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stone.mae2.parts.p2p.EUP2PTunnelPart;

@Mixin(EUP2PTunnelPart.InputHandler.class)
public class EUP2PTunnelPartMixin {

    @Redirect(method = "acceptEnergyFromNetwork", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/capability/IEnergyContainer;acceptEnergyFromNetwork(Lnet/minecraft/core/Direction;JJ)J"), remap = false)
    private long modifyMaxEnergy(IEnergyContainer instance, Direction direction, long voltage, long amperage) {
        return instance.acceptEnergyFromNetwork(direction, voltage * 9 / 10, amperage);
    }
}
