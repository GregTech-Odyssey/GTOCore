package com.gto.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.ItemHandlerProxyTrait;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveBlastFurnaceMachine;
import com.gregtechceu.gtceu.utils.GTTransferUtils;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Kovrax
 * @date 2025/3/3
 * @implNote PrimitiveBlastFurnaceHatch
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrimitiveBlastFurnaceHatch extends MultiblockPartMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(PrimitiveBlastFurnaceHatch.class,
            MultiblockPartMachine.MANAGED_FIELD_HOLDER);

    public final ItemHandlerProxyTrait inputInventory, outputInventory;
    @Nullable
    protected TickableSubscription autoIOSubs;
    @Nullable
    protected ISubscription outputInventorySubs;

    public PrimitiveBlastFurnaceHatch(IMachineBlockEntity holder, Object... args) {
        super(holder);
        this.inputInventory = new ItemHandlerProxyTrait(this, IO.IN);
        this.outputInventory = new ItemHandlerProxyTrait(this, IO.OUT);
    }

    //////////////////////////////////////
    // ***** Initialization ******//
    //////////////////////////////////////
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onUnload() {
        super.onUnload();
        inputInventory.setProxy(null);
        outputInventory.setProxy(null);
        if (outputInventorySubs != null) {
            outputInventorySubs.unsubscribe();
            outputInventorySubs = null;
        }
    }

    @Override
    public void addedToController(IMultiController controller) {
        super.addedToController(controller);
        if (controller instanceof PrimitiveBlastFurnaceMachine primitiveBlastFurnace) {
            outputInventorySubs = primitiveBlastFurnace.exportItems.addChangedListener(this::updateAutoIOSubscription);
            inputInventory.setProxy(primitiveBlastFurnace.importItems);
            outputInventory.setProxy(primitiveBlastFurnace.exportItems);
        }
    }

    @Override
    public void removedFromController(IMultiController controller) {
        super.removedFromController(controller);
        inputInventory.setProxy(null);
        outputInventory.setProxy(null);
        if (outputInventorySubs != null) {
            outputInventorySubs.unsubscribe();
            outputInventorySubs = null;
        }
    }

    @Override
    public boolean canShared() {
        return false;
    }

    @Override
    public boolean replacePartModelWhenFormed() {
        return false;
    }

    //////////////////////////////////////
    // ******** Auto IO *********//
    //////////////////////////////////////

    @Override
    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        updateAutoIOSubscription();
    }

    @Override
    public void onRotated(Direction oldFacing, Direction newFacing) {
        super.onRotated(oldFacing, newFacing);
        updateAutoIOSubscription();
    }

    protected void updateAutoIOSubscription() {
        if ((!outputInventory.isEmpty() &&
                GTTransferUtils.hasAdjacentItemHandler(getLevel(), getPos(), getFrontFacing()))
                ) {
            autoIOSubs = subscribeServerTick(autoIOSubs, this::autoIO);
        } else if (autoIOSubs != null) {
            autoIOSubs.unsubscribe();
            autoIOSubs = null;
        }
    }

    protected void autoIO() {
        if (getOffsetTimer() % 5 == 0) {
            outputInventory.exportToNearby(getFrontFacing());
            updateAutoIOSubscription();
        }
    }

    //////////////////////////////////////
    // ********* GUI *********//
    //////////////////////////////////////
    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }
}
