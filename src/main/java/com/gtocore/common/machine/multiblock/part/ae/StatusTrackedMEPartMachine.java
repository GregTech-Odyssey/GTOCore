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
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyMap;

import gto_ae.api.util.DirectionalGlobalPos;
import gto_ae.helpers.facility_management.IStatusTracked;
import gto_ae.helpers.facility_management.ThroughputCounter;
import gto_ae.helpers.facility_management.WorkingStatus;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
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
    @Getter
    Reference2LongMap<AEKey> configuredSetting = new AEKeyMap<>();

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
        AEItemKey icon = AEItemKey.of(self().getDefinition().get());;
        MutableComponent title = self().getDefinition().get().getName();
        if (!getControllers().isEmpty()) {
            var controller = getControllers().iterator().next();
            title.append(" - ").append(controller.self().getDefinition().get().getName());
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
