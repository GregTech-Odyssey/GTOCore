package com.gtocore.common.machine.electric;

import com.gtocore.api.capability.FluidHandlerList;
import com.gtocore.api.capability.ItemHandlerList;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.utils.LazyOptionalUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

public class AdvancedTesseractMachine extends MetaMachine implements IFancyUIMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AdvancedTesseractMachine.class, MetaMachine.MANAGED_FIELD_HOLDER);

    private final WeakReference<BlockEntity>[] blockEntityReference = new WeakReference[20];

    @Persisted
    public final List<BlockPos> poss = new ObjectArrayList<>(20);

    @Persisted
    protected NotifiableItemStackHandler inventory;

    private final List<IItemHandler> itemHandlers = new ObjectArrayList<>(20);
    private final List<IFluidHandler> fluidHandlers = new ObjectArrayList<>(20);

    public AdvancedTesseractMachine(IMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 20, IO.NONE, IO.NONE);
        inventory.storage.setOnContentsChanged(() -> {
            poss.clear();
            for (int i = 0; i < 20; i++) {
                blockEntityReference[i] = null;
                ItemStack card = inventory.storage.getStackInSlot(i);
                if (card.isEmpty()) continue;
                CompoundTag posTags = card.getTag();
                if (posTags == null || !posTags.contains("posX") || !posTags.contains("posY") || !posTags.contains("posZ")) continue;
                var pos = new BlockPos(posTags.getInt("posX"), posTags.getInt("posY"), posTags.getInt("posZ"));
                if (pos.equals(getPos())) continue;
                poss.add(pos);
            }
        });
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 18 * 5 + 16, 18 * 4 + 16);
        var container = new WidgetGroup(4, 4, 18 * 5 + 8, 18 * 4 + 8);
        int index = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                container.addWidget(new SlotWidget(inventory.storage, index++, 4 + x * 18, 4 + y * 18, true, true).setBackgroundTexture(GuiTextures.SLOT));
            }
        }
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(container);
        return group;
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            itemHandlers.clear();
            var size = poss.size();
            for (int i = 0; i < size; i++) {
                var c = getCapability(ForgeCapabilities.ITEM_HANDLER, side, poss.get(i), i);
                if (c != null) {
                    var h = LazyOptionalUtil.get(c);
                    if (h != null) {
                        itemHandlers.add(h);
                    }
                }
            }
            var s = itemHandlers.size();
            if (s > 0) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(() -> s > 1 ? new ItemHandlerList(itemHandlers.toArray(new IItemHandler[0])) : itemHandlers.get(0)));
            }
        } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
            fluidHandlers.clear();
            var size = poss.size();
            for (int i = 0; i < size; i++) {
                var c = getCapability(ForgeCapabilities.FLUID_HANDLER, side, poss.get(i), i);
                if (c != null) {
                    var h = LazyOptionalUtil.get(c);
                    if (h != null) {
                        fluidHandlers.add(h);
                    }
                }
            }
            var s = itemHandlers.size();
            if (s > 0) {
                return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, LazyOptional.of(() -> s > 1 ? new FluidHandlerList(fluidHandlers.toArray(new IFluidHandler[0])) : fluidHandlers.get(0)));
            }
        }
        return null;
    }

    private <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side, @Nullable BlockPos pos, int i) {
        if (pos == null) return null;
        var reference = blockEntityReference[i];
        if (reference == null) {
            var be = getLevel().getBlockEntity(pos);
            if (be != null) {
                blockEntityReference[i] = new WeakReference<>(be);
                return be.getCapability(cap, side);
            } else {
                poss.set(i, null);
            }
        } else {
            var blockEntity = reference.get();
            if (blockEntity == null || blockEntity.isRemoved()) {
                blockEntity = getLevel().getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntityReference[i] = new WeakReference<>(blockEntity);
                    return blockEntity.getCapability(cap, side);
                } else {
                    poss.set(i, null);
                }
            } else {
                return blockEntity.getCapability(cap, side);
            }
        }
        return null;
    }
}
