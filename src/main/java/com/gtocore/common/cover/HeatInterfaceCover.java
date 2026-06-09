package com.gtocore.common.cover;

import com.gtolib.api.capability.IHeatContainer;
import com.gtolib.api.machine.heat.HeatHandler;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;

import com.gto.datasynclib.annotations.SaveToDisk;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class HeatInterfaceCover extends CoverBehavior {

    private MetaMachine machine;

    @SaveToDisk
    private final HeatHandler handler;

    public HeatInterfaceCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
        var tier = coverHolder.holder() instanceof MetaMachineBlockEntity blockEntity ? blockEntity.definition.getTier() + 1 : 1;
        handler = new HeatHandler(coverHolder.holder(), 800L * tier, tier, tier, 0.01);
        handler.addChangedListener(() -> {
            if (getMachine() instanceof IRecipeLogicMachine recipeLogicMachine) {
                recipeLogicMachine.getRecipeLogic().updateTickSubscription();
            }
        });
    }

    @Nullable
    public <T> T getGTCapability(Class<T> cap) {
        if (cap == IHeatContainer.class) {
            return cap.cast(handler);
        }
        return null;
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && (machine = MetaMachine.getMachine(coverHolder.holder())) != null && machine.holder.getGTCapability(IHeatContainer.class, null) == null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        machine = MetaMachine.getMachine(coverHolder.holder());
        handler.onLoad();
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        handler.onUnLoad();
    }

    @Nullable
    private MetaMachine getMachine() {
        if (machine == null) {
            machine = MetaMachine.getMachine(coverHolder.holder());
        }
        return machine;
    }
}
