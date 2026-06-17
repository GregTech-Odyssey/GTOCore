package com.gtocore.mixin.ae2.crafting;

import com.gtocore.common.machine.multiblock.electric.processing.ProcessingPlantMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.stacks.AEItemKey;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = PatternContainerGroup.class, remap = false)
public class PatternContainerGroupMixin {

    @Unique
    private static Component gto$getMachineName(MetaMachine machine) {
        var title = Component.translatable(machine.getDefinition().getDescriptionId());
        if (machine instanceof ProcessingPlantMachine processingPlantMachine) {
            ItemStack stack = processingPlantMachine.getMachineStorage().getStackInSlot(0);
            if (stack.getItem() instanceof MetaMachineItem metaMachineItem) {
                return title.copy()
                        .append(" - ")
                        .append(Component.translatable(metaMachineItem.getDefinition().getDescriptionId()));
            }
        }
        return title;
    }

    @Inject(method = "fromMachine", at = @At("HEAD"), cancellable = true)
    private static void gto$fromMachine(Level level, BlockPos pos, Direction side,
                                        CallbackInfoReturnable<PatternContainerGroup> cir) {
        if (!(level.getBlockEntity(pos) instanceof MetaMachineBlockEntity blockEntity)) {
            return;
        }

        MetaMachine machine = blockEntity.getMetaMachine();
        if (machine == null) {
            return;
        }

        if (machine instanceof MultiblockPartMachine partMachine && partMachine.isFormed()) {
            IMultiController controller = partMachine.getControllers().stream().findFirst().orElse(null);
            if (controller == null) {
                return;
            }

            var controllerMachine = controller.self();
            var controllerDefinition = controllerMachine.getDefinition();
            cir.setReturnValue(new PatternContainerGroup(
                    AEItemKey.of(controllerDefinition.asStack()),
                    gto$getMachineName(controllerMachine),
                    List.of(Component.translatable(machine.getDefinition().getDescriptionId()))));
            return;
        }

        var definition = machine.getDefinition();
        cir.setReturnValue(new PatternContainerGroup(
                AEItemKey.of(definition.asStack()),
                gto$getMachineName(machine),
                List.of()));
    }
}
