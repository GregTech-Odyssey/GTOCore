package com.gtocore.mixin.ae2.crafting;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.stacks.AEItemKey;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = PatternContainerGroup.class, remap = false)
public class PatternContainerGroupMixin {

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

            var controllerDefinition = controller.self().getDefinition();
            cir.setReturnValue(new PatternContainerGroup(
                    AEItemKey.of(controllerDefinition.asStack()),
                    Component.translatable(controllerDefinition.getDescriptionId()),
                    List.of(Component.translatable(machine.getDefinition().getDescriptionId()))));
            return;
        }

        var definition = machine.getDefinition();
        cir.setReturnValue(new PatternContainerGroup(
                AEItemKey.of(definition.asStack()),
                Component.translatable(definition.getDescriptionId()),
                List.of()));
    }
}
