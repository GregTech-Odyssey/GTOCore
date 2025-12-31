package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.machine.feature.ISpaceWorkspaceMachine;
import com.gtolib.api.machine.feature.IWorkInSpaceMachine;
import com.gtolib.utils.holder.BooleanHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class SpaceShieldHatch extends MultiblockPartMachine implements ISpaceWorkspaceMachine {

    private BooleanHolder hasLaser;

    public SpaceShieldHatch(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
        if (controller instanceof IWorkInSpaceMachine machine) {
            machine.setWorkspaceProvider(this);
        }
    }

    @Override
    public boolean canShared() {
        return false;
    }

    @Override
    public void addMultiText(List<Component> textList) {
        super.addMultiText(textList);
        if (!PlanetApi.API.isSpace(getLevel())) {
            textList.add(Component.translatable("gtocore.machine.space_shield_hatch.not_in_space"));
            return;
        }
        if (isWorkspaceReady()) {
            textList.add(Component.translatable("gtocore.machine.space_shield_hatch.info").withStyle(ChatFormatting.GREEN));
        } else {
            textList.add(Component.translatable("gtocore.machine.space_shield_hatch.insufficient").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public void removedFromController(@NotNull IMultiController controller) {
        super.removedFromController(controller);
        if (controller instanceof IWorkInSpaceMachine receiver && receiver.getWorkspaceProvider() == this) {
            receiver.setWorkspaceProvider(null);
        }
        hasLaser = null;
    }

    @Override
    public boolean isWorkspaceReady() {
        if (hasLaser == null) {
            hasLaser = new BooleanHolder(Stream.of(getController().getParts())
                    .anyMatch(p -> (PartAbility.INPUT_LASER.isApplicable(p.self().getBlockState().getBlock()))));
        }
        return hasLaser.value;
    }
}
