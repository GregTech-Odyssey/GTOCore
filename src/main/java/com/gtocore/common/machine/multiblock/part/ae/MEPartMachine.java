package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.api.machine.feature.IMEPartMachine;
import com.gtolib.api.machine.feature.multiblock.IExtendedRecipeCapabilityHolder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraftforge.items.IItemHandlerModifiable;

import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
abstract class MEPartMachine extends TieredIOPartMachine implements IMEPartMachine, IDistinctPart, IMachineLife {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MEPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);

    static final int CONFIG_SIZE = 16;

    @Persisted
    private final GridNodeHolder nodeHolder;

    @DescSynced
    boolean isOnline;

    final IActionSource actionSource;

    @Persisted
    protected boolean isDistinct = false;

    MEPartMachine(IMachineBlockEntity holder, IO io) {
        super(holder, GTValues.LuV, io);
        this.nodeHolder = new GridNodeHolder(this);
        this.actionSource = IActionSource.ofMachine(nodeHolder.getMainNode()::getNode);
    }

    @Override
    @Nullable
    public IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Override
    @Nullable
    public IFluidHandlerModifiable getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (holder.self().getPersistentData().getBoolean("isAllFacing")) {
            getMainNode().setExposedOnSides(EnumSet.allOf(Direction.class));
        }
    }

    @Override
    public IManagedGridNode getMainNode() {
        return nodeHolder.getMainNode();
    }

    @Override
    public void onRotated(Direction oldFacing, Direction newFacing) {
        super.onRotated(oldFacing, newFacing);
        getMainNode().setExposedOnSides(EnumSet.of(newFacing));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onPaintingColorChanged(int color) {
        for (var c : getControllers()) {
            if (c instanceof IExtendedRecipeCapabilityHolder recipeCapabilityHolder) {
                recipeCapabilityHolder.arrangeDistinct();
            }
        }
    }

    @Override
    public boolean isDistinct() {
        return isDistinct;
    }

    @Override
    public void setDistinct(boolean isDistinct) {
        this.isDistinct = isDistinct;
        getHandlerList().setDistinct(isDistinct);
        for (IMultiController controller : getControllers()) {
            if (controller instanceof IExtendedRecipeCapabilityHolder recipeHolder) {
                recipeHolder.arrangeDistinct();
            }
        }
    }

    @Override
    public void setOnline(final boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public boolean isOnline() {
        return this.isOnline;
    }

    @Override
    public IActionSource getActionSource() {
        return this.actionSource;
    }
}
