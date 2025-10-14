package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.machine.feature.ISpaceWorkspaceMachine;
import com.gtolib.api.machine.feature.IWorkInSpaceMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.part.LaserHatchPartMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import com.hepdd.gtmthings.common.block.machine.multiblock.part.CreativeLaserHatchPartMachine;
import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.api.GTValues.UEV;

public class SpaceShieldHatch extends TieredPartMachine implements ISpaceWorkspaceMachine {

    private boolean hasLaser;

    public SpaceShieldHatch(MetaMachineBlockEntity holder) {
        super(holder, UEV);
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
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        hasLaser = Stream.of(getControllers().first().getParts()).anyMatch(p -> p instanceof LaserHatchPartMachine || p instanceof CreativeLaserHatchPartMachine);
        return super.modifyRecipe(recipe);
    }

    @Override
    public void addMultiText(List<Component> textList) {
        super.addMultiText(textList);
        if (!PlanetApi.API.isSpace(getLevel())) {
            textList.add(Component.translatable("gtocore.machine.space_shield_hatch.not_in_space"));
            return;
        }
        if (hasLaser) {
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
        hasLaser = false;
    }

    @Override
    public boolean isWorkspaceReady() {
        return hasLaser;
    }
}
