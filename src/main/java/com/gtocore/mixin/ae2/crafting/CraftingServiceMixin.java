package com.gtocore.mixin.ae2.crafting;

import com.gtolib.api.ae2.crafting.OptimizedCalculation;
import com.gtolib.api.ae2.machine.CraftingInterfacePartMachine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.*;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.crafting.CraftingLink;
import appeng.crafting.CraftingLinkNexus;
import appeng.hooks.ticking.TickHandler;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.service.CraftingService;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Mixin(CraftingService.class)
public abstract class CraftingServiceMixin {

    @Shadow(remap = false)
    @Final
    private static ExecutorService CRAFTING_POOL;

    @Shadow(remap = false)
    @Final
    private IGrid grid;

    @Mutable
    @Shadow(remap = false)
    @Final
    private Set<CraftingCPUCluster> craftingCPUClusters;

    @Mutable
    @Shadow(remap = false)
    @Final
    private Map<UUID, CraftingLinkNexus> craftingLinks;

    @Shadow(remap = false)
    private boolean updateList;

    @Inject(method = "onServerEndTick", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"), remap = false, cancellable = true)
    private void onServerEndTick(CallbackInfo ci) {
        if ((TickHandler.instance().getCurrentTick() & 1) == 0) {
            ci.cancel();
        }
    }

    @Inject(method = "addNode", at = @At("TAIL"), remap = false)
    private void addNode(IGridNode gridNode, CompoundTag savedData, CallbackInfo ci) {
        if (gridNode.getOwner() instanceof CraftingInterfacePartMachine) {
            updateList = true;
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void updateCPUClusters() {
        this.craftingCPUClusters.clear();
        for (var machine : this.grid.getMachines(CraftingInterfacePartMachine.class)) {
            for (var cluster : machine.getClusters()) {
                this.craftingCPUClusters.add(cluster);
                ICraftingLink maybeLink = cluster.craftingLogic.getLastLink();
                if (maybeLink != null) {
                    this.addLink((CraftingLink) maybeLink);
                }
            }
        }
        for (var blockEntity : this.grid.getMachines(CraftingBlockEntity.class)) {
            final CraftingCPUCluster cluster = blockEntity.getCluster();
            if (cluster != null) {
                this.craftingCPUClusters.add(cluster);
                ICraftingLink maybeLink = cluster.craftingLogic.getLastLink();
                if (maybeLink != null) {
                    this.addLink((CraftingLink) maybeLink);
                }
            }
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void addLink(CraftingLink link) {
        if (link.isStandalone()) {
            return;
        }
        link.setNexus(craftingLinks.computeIfAbsent(link.getCraftingID(), id -> new CraftingLinkNexus(link.getCraftingID())));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public Future<ICraftingPlan> beginCraftingCalculation(Level level, ICraftingSimulationRequester simRequester,
                                                          AEKey what, long amount, CalculationStrategy strategy) {
        if (level == null || simRequester == null) {
            throw new IllegalArgumentException("Invalid Crafting Job Request");
        }
        return CRAFTING_POOL.submit(() -> OptimizedCalculation.executeV2(grid, simRequester, what, amount, strategy));
    }

    @Redirect(method = "submitJob", at = @At(value = "INVOKE", target = "Lappeng/api/networking/crafting/ICraftingPlan;simulation()Z"), remap = false)
    private boolean ignoreCantCraftWhileManuallySubmitted(ICraftingPlan instance, @Local(argsOnly = true) IActionSource src) {
        return src.player().isEmpty() && instance.simulation();
    }
}
