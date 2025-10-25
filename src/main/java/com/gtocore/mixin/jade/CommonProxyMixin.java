package com.gtocore.mixin.jade;

import com.gtocore.common.blockentity.TesseractBlockEntity;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternPartMachineKt;
import com.gtocore.integration.jade.GTOJadePlugin;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.fluid.FluidHandlerList;
import com.gregtechceu.gtceu.api.transfer.item.ItemHandlerList;
import com.gregtechceu.gtceu.common.machine.multiblock.part.MufflerPartMachine;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.items.IItemHandler;

import appeng.integration.modules.jade.JadeModule;
import dev.shadowsoffire.apotheosis.adventure.compat.AdventureHwylaPlugin;
import dev.shadowsoffire.apotheosis.ench.compat.EnchHwylaPlugin;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import snownee.jade.Jade;
import snownee.jade.addon.core.CorePlugin;
import snownee.jade.addon.universal.UniversalPlugin;
import snownee.jade.addon.vanilla.VanillaPlugin;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.util.CommonProxy;

import java.util.List;

@Mixin(CommonProxy.class)
public class CommonProxyMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void loadComplete(FMLLoadCompleteEvent event) {
        List<IWailaPlugin> plugins = List.of(new VanillaPlugin(), new UniversalPlugin(), new CorePlugin(), new JadeModule(), new GTOJadePlugin(), new AdventureHwylaPlugin(), new EnchHwylaPlugin());
        for (IWailaPlugin plugin : plugins) {
            plugin.register(WailaCommonRegistration.INSTANCE);
            if (CommonProxy.isPhysicallyClient()) {
                plugin.registerClient(WailaClientRegistration.INSTANCE);
            }
        }
        Jade.loadComplete();
    }

    @Redirect(method = "createItemCollector", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/capabilities/CapabilityProvider;getCapability(Lnet/minecraftforge/common/capabilities/Capability;)Lnet/minecraftforge/common/util/LazyOptional;"), remap = false)
    private static <T> LazyOptional<T> createItemCollector(CapabilityProvider instance, Capability<T> capability) {
        if (instance instanceof MetaMachineBlockEntity blockEntity && !(blockEntity instanceof TesseractBlockEntity)) {
            if (blockEntity.metaMachine instanceof MEPatternPartMachineKt<?>) return LazyOptional.empty();
            if (blockEntity.metaMachine instanceof MufflerPartMachine mufflerPartMachine) {
                return LazyOptional.of(mufflerPartMachine::getInventory).cast();
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
                return LazyOptional.of(() -> new ItemHandlerList(filteredTraits.toArray(new IItemHandler[0]))).cast();
            }
        }
        return instance.getCapability(capability);
    }

    @Redirect(method = "wrapFluidStorage", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/capabilities/CapabilityProvider;getCapability(Lnet/minecraftforge/common/capabilities/Capability;)Lnet/minecraftforge/common/util/LazyOptional;"), remap = false)
    private static <T> LazyOptional<T> wrapFluidStorage(CapabilityProvider instance, Capability<T> capability) {
        if (instance instanceof MetaMachineBlockEntity blockEntity) {
            if (blockEntity.metaMachine instanceof MEPatternPartMachineKt<?>) return LazyOptional.empty();
            var ts = blockEntity.metaMachine.getTraits();
            List<IFluidHandler> filteredTraits = new ObjectArrayList<>(ts.size());
            for (var t : ts) {
                if (t instanceof IFluidHandler) {
                    filteredTraits.add((IFluidHandler) t);
                }
            }
            if (!filteredTraits.isEmpty()) {
                return LazyOptional.of(() -> new FluidHandlerList(filteredTraits.toArray(new IFluidHandler[0]))).cast();
            }
        }
        return instance.getCapability(capability);
    }
}
