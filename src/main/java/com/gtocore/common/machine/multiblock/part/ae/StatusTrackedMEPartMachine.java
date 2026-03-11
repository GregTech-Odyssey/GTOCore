package com.gtocore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.stacks.AEItemKey;

import gto_ae.api.util.DirectionalGlobalPos;
import gto_ae.helpers.facility_management.IStatusTracked;
import gto_ae.helpers.facility_management.ThroughputCounter;
import gto_ae.helpers.facility_management.WorkingStatus;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class StatusTrackedMEPartMachine extends MEPartMachine implements IStatusTracked {

    @Getter
    protected final ThroughputCounter throughputCounter = new ThroughputCounter();
    @Getter
    @Setter
    protected WorkingStatus status = WorkingStatus.IDLE;

    public StatusTrackedMEPartMachine(@NotNull MetaMachineBlockEntity holder, @NotNull IO io) {
        super(holder, io);
    }

    @Override
    public ImmutableSet<ICraftingLink> getRequestedJobs() {
        return ImmutableSet.of();
    }

    @Override
    public void openGui(Player player) {
        this.tryToOpenUI(player, InteractionHand.MAIN_HAND,
                new BlockHitResult(getPos().getCenter(), player.getDirection(), getPos(), false));
    }

    @Override
    public PatternContainerGroup getTerminalGroup() {
        AEItemKey icon;
        MutableComponent title = self().getDefinition().get().getName();
        if (!getControllers().isEmpty()) {
            var controller = getControllers().iterator().next();
            icon = AEItemKey.of(controller.self().getDefinition().get());
            title.append(" - ").append(controller.self().getDefinition().get().getName());
        } else {
            icon = AEItemKey.of(self().getDefinition().get());
        }
        return new PatternContainerGroup(icon, title, List.of());
    }

    @Override
    public @Nullable DirectionalGlobalPos getFacilityPosition() {
        if (getLevel() == null) {
            return null;
        }
        return new DirectionalGlobalPos(getLevel().dimension(), getPos(), null);
    }
}
