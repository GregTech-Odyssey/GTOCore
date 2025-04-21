package com.gto.gtocore.common.cover;

import com.gto.gtocore.common.machine.multiblock.part.InfiniteIntakeHatchPartMachine;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AirVentCover extends CoverBehavior {

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
        if (coverHolder.getOffsetTimer() % 20 == 0 && coverHolder.getLevel().getBlockState(coverHolder.getPos().relative(attachedSide)).isAir()) {
            var fluid = InfiniteIntakeHatchPartMachine.AIR_MAP.get(coverHolder.getLevel().dimension().location());
            if (fluid == null) {
                subscription.unsubscribe();
                return;
            }
            FluidUtil.getFluidHandler(coverHolder.getLevel(), coverHolder.getPos(), attachedSide).ifPresent(h -> h.fill(new FluidStack(fluid, 200), IFluidHandler.FluidAction.EXECUTE));
        }
    }
}
