package com.gto.gtocore.common.cover;

import com.gto.gtocore.api.data.GTODimensions;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AirVentCover extends CoverBehavior {

    private static final FluidStack AIR = GTMaterials.Air.getFluid(200);

    private TickableSubscription subscription;

    public AirVentCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && FluidUtil.getFluidHandler(coverHolder.getLevel(), coverHolder.getPos(), attachedSide).isPresent();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        subscription = coverHolder.subscribeServerTick(subscription, this::update);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void update() {
        if (coverHolder.getOffsetTimer() % 20 == 0 && GTODimensions.isOverworld(coverHolder.getLevel().dimension().location()) && coverHolder.getLevel().getBlockState(coverHolder.getPos().relative(attachedSide)).isAir()) {
            FluidUtil.getFluidHandler(coverHolder.getLevel(), coverHolder.getPos(), attachedSide).ifPresent(h -> h.fill(AIR, IFluidHandler.FluidAction.EXECUTE));
        }
    }
}
