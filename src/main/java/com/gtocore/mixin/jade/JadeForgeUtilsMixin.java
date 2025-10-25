package com.gtocore.mixin.jade;

import com.gtocore.common.blockentity.TesseractBlockEntity;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternPartMachineKt;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.ItemHandlerList;
import com.gregtechceu.gtceu.common.machine.multiblock.part.MufflerPartMachine;

import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import snownee.jade.addon.universal.ItemIterator;
import snownee.jade.util.JadeForgeUtils;

import java.util.List;
import java.util.function.Function;

@Mixin(JadeForgeUtils.class)
public abstract class JadeForgeUtilsMixin {

    @Shadow(remap = false)
    public static ItemIterator<? extends IItemHandler> fromItemHandler(IItemHandler storage, int fromIndex, Function<Object, @Nullable IItemHandler> containerFinder) {
        return null;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static ItemIterator<? extends IItemHandler> fromItemHandler(IItemHandler storage, int fromIndex) {
        return fromItemHandler(storage, fromIndex, target -> {
            if (target instanceof CapabilityProvider<?> capProvider) {
                if (capProvider instanceof MetaMachineBlockEntity blockEntity && !(blockEntity instanceof TesseractBlockEntity)) {
                    if (blockEntity.metaMachine instanceof MEPatternPartMachineKt<?>) return null;
                    if (blockEntity.metaMachine instanceof MufflerPartMachine mufflerPartMachine) {
                        return mufflerPartMachine.getInventory();
                    }
                    var ts = blockEntity.metaMachine.getTraits();
                    List<IItemHandler> filteredTraits = new ObjectArrayList<>(ts.size());
                    for (var t : ts) {
                        if (t instanceof IItemHandler handler) {
                            if (handler instanceof NotifiableItemStackHandler stackHandler) {
                                filteredTraits.add(stackHandler.storage);
                            } else {
                                filteredTraits.add(handler);
                            }
                        }
                    }
                    if (!filteredTraits.isEmpty()) {
                        return new ItemHandlerList(filteredTraits.toArray(new IItemHandler[0]));
                    }
                }
                return capProvider.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
            }
            return null;
        });
    }
}
