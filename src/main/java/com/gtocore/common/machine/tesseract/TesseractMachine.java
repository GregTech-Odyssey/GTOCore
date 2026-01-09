package com.gtocore.common.machine.tesseract;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.machine.part.ItemPartMachine;
import com.gtolib.api.player.IEnhancedPlayer;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

public class TesseractMachine extends MetaMachine implements IFancyUIMachine, IMachineLife, ITesseractMarkerInteractable {

    private static final Set<Capability<?>> CAPABILITIES = Set.of(ForgeCapabilities.ITEM_HANDLER, ForgeCapabilities.FLUID_HANDLER);

    @Override
    public void onMachineRemoved() {
        clearInventory(inventory.storage);
    }

    private WeakReference<BlockEntity> blockEntityReference;

    @Persisted
    public BlockPos pos;

    @Persisted
    protected NotifiableItemStackHandler inventory;

    private boolean call;

    public TesseractMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.NONE);
        inventory.storage.setOnContentsChangedAndfreeze(() -> {
            call = false;
            pos = null;
            blockEntityReference = null;
            ItemStack card = inventory.storage.getStackInSlot(0);
            if (card.isEmpty()) return;
            CompoundTag posTags = card.getTag();
            if (posTags == null || !posTags.contains("x") || !posTags.contains("y") || !posTags.contains("z")) return;
            var pos = new BlockPos(posTags.getInt("x"), posTags.getInt("y"), posTags.getInt("z"));
            if (pos.equals(getPos())) return;
            this.pos = pos;
        });
    }

    @Override
    public Widget createUIWidget() {
        return ItemPartMachine.createSLOTWidget(inventory);
    }

    @Override
    public @Nullable IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        var cap = getCapability(ForgeCapabilities.ITEM_HANDLER, side);
        return cap != null ? cap.orElse(null) instanceof IItemHandlerModifiable m ? m : null : null;
    }

    @Override
    public @Nullable IFluidHandlerModifiable getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        var cap = getCapability(ForgeCapabilities.FLUID_HANDLER, side);
        return cap != null ? cap.orElse(null) instanceof IFluidHandlerModifiable m ? m : null : null;
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (call) return null;
        if (pos != null && CAPABILITIES.contains(cap)) {
            LazyOptional<T> result = null;
            call = true;
            if (blockEntityReference == null) {
                var be = getLevel().getBlockEntity(pos);
                if (be != null) {
                    blockEntityReference = new WeakReference<>(be);
                    result = be.getCapability(cap, side);
                } else {
                    pos = null;
                }
            } else {
                var blockEntity = blockEntityReference.get();
                if (blockEntity == null || blockEntity.isRemoved()) {
                    blockEntity = getLevel().getBlockEntity(pos);
                    if (blockEntity != null) {
                        blockEntityReference = new WeakReference<>(blockEntity);
                        result = blockEntity.getCapability(cap, side);
                    } else {
                        pos = null;
                    }
                } else {
                    result = blockEntity.getCapability(cap, side);
                }
            }
            call = false;
            if (side != null && result != null) {
                var handler = result.orElse(null);
                if (handler instanceof IItemHandlerModifiable modifiable) {
                    CoverBehavior cover = getCoverContainer().getCoverAtSide(side);
                    return cover != null ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(() -> cover.getItemHandlerCap(modifiable))) : result;
                } else if (handler instanceof IFluidHandlerModifiable modifiable) {
                    CoverBehavior cover = getCoverContainer().getCoverAtSide(side);
                    return cover != null ? ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, LazyOptional.of(() -> cover.getFluidHandlerCap(modifiable))) : result;
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public boolean onMarkerInteract(Player player, List<TesseractDirectedTarget> targets) {
        ItemStack card = GTOItems.COORDINATE_CARD.asItem().getDefaultInstance();
        if (inventory.storage.getStackInSlot(0).isEmpty()) {
            card = ItemStack.EMPTY;
            if (card.isEmpty()) {
                var idx = player.getInventory().findSlotMatchingItem(GTOItems.COORDINATE_CARD.asItem().getDefaultInstance());
                if (idx >= 0) {
                    card = player.getInventory().removeItem(idx, 1);
                }
            }
            ae:
            if (card.isEmpty()) {
                var meStorage = IEnhancedPlayer.getMEStorageService((ServerPlayer) player);
                if (meStorage == null) {
                    break ae;
                }
                var cardNum = meStorage.getInventory().extract(AEItemKey.of(GTOItems.COORDINATE_CARD.asItem()), 1, Actionable.MODULATE, IActionSource.ofPlayer(player));
                if (cardNum <= 0) {
                    break ae;
                }
                card = GTOItems.COORDINATE_CARD.asItem().getDefaultInstance();
            }
            if (card.isEmpty()) {
                player.displayClientMessage(Component.translatable(WRITE_FAIL_TEXT), true);
                return true;
            }
        }
        var pos = targets.getFirst().pos().pos();
        CompoundTag posTags = card.getOrCreateTag();
        posTags.putInt("x", pos.getX());
        posTags.putInt("y", pos.getY());
        posTags.putInt("z", pos.getZ());
        inventory.storage.setStackInSlot(0, card);
        player.displayClientMessage(Component.translatable(WRITE_SUCCESS_TEXT), true);
        return true;
    }
}
