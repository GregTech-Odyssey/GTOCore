package com.gto.gtocore.common.cover;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.common.cover.PumpCover;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.GTTransferUtils;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;

import org.jetbrains.annotations.NotNull;

public final class SteamPumpCover extends PumpCover {

    private static final Fluid STEAM = GTMaterials.Steam.getFluid();

    public SteamPumpCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide, 1, 1000000);
    }

    @Override
    protected int transferAny(@NotNull IFluidHandlerModifiable source, @NotNull IFluidHandlerModifiable destination, int platformTransferLimit) {
        return GTTransferUtils.transferFluidsFiltered(source, destination, f -> f.getFluid() == STEAM, platformTransferLimit);
    }
}
