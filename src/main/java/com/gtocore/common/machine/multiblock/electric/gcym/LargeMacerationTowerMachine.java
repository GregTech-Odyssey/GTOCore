package com.gtocore.common.machine.multiblock.electric.gcym;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public class LargeMacerationTowerMachine extends GCYMMultiblockMachine {

    private AABB grindBound = new AABB(BlockPos.ZERO);
    private final List<IItemHandler> handlers = new ArrayList<>();

    private TickableSubscription hurtSub;

    public LargeMacerationTowerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        updateBounds();
        var items = gtolib$getInputFlat().get(ItemRecipeCapability.CAP);
        if (items != null) {
            for (var holder : items) {
                if (holder instanceof IItemHandler ih) {
                    handlers.add(ih);
                }
            }
            hurtSub = subscribeServerTick(this::spinWheels);
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        unsubscribe(hurtSub);
        hurtSub = null;
        handlers.clear();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        unsubscribe(hurtSub);
        hurtSub = null;
        handlers.clear();
    }

    private void updateBounds() {
        var fl = RelativeDirection.offsetPos(getPos(), getFrontFacing(), getUpwardsFacing(), isFlipped(), 1, 2, 0);
        var br = RelativeDirection.offsetPos(getPos(), getFrontFacing(), getUpwardsFacing(), isFlipped(), 2, -2, -4);
        grindBound = new AABB(fl, br);
    }

    private void spinWheels() {
        if (isRemote() || getLevel() == null || recipeLogic.isSuspend() || getOffsetTimer() % 20 != 0) return;

        List<ItemEntity> itemEntities = new ArrayList<>();
        for (var entity : getLevel().getEntities(null, grindBound)) {
            if (entity instanceof ItemEntity ie) {
                itemEntities.add(ie);
            } else {
                if (recipeLogic.isWorking()) {
                    entity.hurt(entity.damageSources().cramming(), getTier());
                }
            }
        }

        if (handlers.isEmpty()) return;

        for (ItemEntity item : itemEntities) {
            if (item.isRemoved()) continue;
            for (var holder : handlers) {
                item.setItem(ItemHandlerHelper.insertItem(holder, item.getItem(), false));
                if (item.getItem().isEmpty()) {
                    item.discard();
                    break;
                }
            }
        }
    }
}
