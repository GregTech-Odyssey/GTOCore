package com.gtocore.common.cover;

import com.gtolib.api.capability.IHeatContainer;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.common.cover.detector.DetectorCover;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class HeatDetectorCover extends DetectorCover {

    public HeatDetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    protected void update() {
        var container = GTCapabilityHelper.getBlockEntityGTCapability(IHeatContainer.class, coverHolder.holder(), null);
        if (container == null) return;
        setRedstoneSignalOutput(container.getSignal());
    }
}
