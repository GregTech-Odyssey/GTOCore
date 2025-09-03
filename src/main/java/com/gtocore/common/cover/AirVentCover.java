package com.gtocore.common.cover;

import com.gtocore.common.machine.multiblock.part.InfiniteIntakeHatchPartMachine;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AirVentCover extends CoverBehavior {

    private TickableSubscription subscription;
    private MetaMachine machine;

    public AirVentCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && (machine = MetaMachine.getMachine(coverHolder.holder())) != null && machine.getFluidHandlerCap(attachedSide, false) != null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (coverHolder.getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, () -> {
                machine = MetaMachine.getMachine(coverHolder.holder());
                subscription = coverHolder.subscribeServerTick(subscription, this::update);
            }));
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    private void update() {
        if (machine != null && machine.getOffsetTimer() % 20 == 0 && machine.getNeighborBlockState(attachedSide).isAir()) {
            var fluid = InfiniteIntakeHatchPartMachine.AIR_MAP.get(coverHolder.getLevel().dimension().location());
            if (fluid == null) {
                subscription.unsubscribe();
                return;
            }
            var handler = machine.getFluidHandlerCap(attachedSide, false);
            if (handler == null) return;
            handler.fill(new FluidStack(fluid, 200), IFluidHandler.FluidAction.EXECUTE);
        }
    }
}
