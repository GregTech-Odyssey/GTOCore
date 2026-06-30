package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.ae2.stacks.AEManaKeyHandler;
import com.gtocore.common.machine.multiblock.storage.MultiblockMEStorageMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.FluidTankProxyTrait;
import com.gregtechceu.gtceu.api.machine.trait.ItemHandlerProxyTrait;
import com.gregtechceu.gtceu.api.recipe.handler.IO;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import appeng.api.storage.MEStorage;
import appeng.capabilities.Capabilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class MEStorageHatch extends MultiblockPartMachine {

    @NotNull
    private final ItemHandlerProxyTrait item;
    @NotNull
    private final FluidTankProxyTrait fluid;
    @NotNull
    private final AEManaKeyHandler manaHandler;

    @NotNull
    private LazyOptional<MEStorage> capabilityStorage = LazyOptional.empty();
    @NotNull
    private LazyOptional<ManaReceiver> capabilityMana = LazyOptional.empty();

    public MEStorageHatch(MetaMachineBlockEntity holder) {
        super(holder);
        this.item = new ItemHandlerProxyTrait(this, IO.BOTH);
        this.fluid = new FluidTankProxyTrait(this, IO.BOTH);
        this.manaHandler = new AEManaKeyHandler();
    }

    @Override
    public @Nullable <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.STORAGE) {
            if (side == null || side == getFrontFacing()) {
                return capabilityStorage.cast();
            }
        } else if (cap == BotaniaForgeCapabilities.MANA_RECEIVER) {
            if (side == null || side == getFrontFacing()) {
                return capabilityMana.cast();
            }
            return LazyOptional.empty();
        }
        return null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        manaHandler.setPos(getPos());
        manaHandler.setLevel(getLevel());
    }

    @Override
    public void onUnload() {
        super.onUnload();
        item.setProxy(null);
        fluid.setProxy(null);
        manaHandler.setLevel(null);
        capabilityStorage.invalidate();
        capabilityStorage = LazyOptional.empty();
        capabilityMana.invalidate();
        capabilityMana = LazyOptional.empty();
    }

    @Override
    public void addedToController(IMultiController controller) {
        super.addedToController(controller);
        if (controller instanceof MultiblockMEStorageMachine machine) {
            var item = machine.getItemHandlerCap(null, false);
            if (item != null) this.item.setProxy(item);
            var fluid = machine.getFluidHandlerCap(null, false);
            if (fluid != null) this.fluid.setProxy(fluid);
            var mana = machine.getCapability(BotaniaForgeCapabilities.MANA_RECEIVER, null);
            if (mana != null && mana.isPresent()) {
                manaHandler.setMap(machine.getKeyMap());
                manaHandler.setStorageSupplier(machine.getStorageSupplier());
                manaHandler.setCapacity(machine.getCapacity());
                manaHandler.setOnChange(machine.getOnChange());
                capabilityMana = LazyOptional.of(() -> manaHandler);
            }
            capabilityStorage = LazyOptional.of(() -> machine);
            this.notifyNeighborsUpdate();
        }
    }

    @Override
    public void removedFromController(IMultiController controller) {
        super.removedFromController(controller);
        item.setProxy(null);
        fluid.setProxy(null);
        manaHandler.setMap(null);
        manaHandler.setStorageSupplier(null);
        manaHandler.setCapacity(0);
        manaHandler.setOnChange(null);
        capabilityStorage.invalidate();
        capabilityStorage = LazyOptional.empty();
        capabilityMana.invalidate();
        capabilityMana = LazyOptional.empty();
        this.notifyNeighborsUpdate();
    }

    @Override
    public boolean canShared() {
        return false;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }
}
