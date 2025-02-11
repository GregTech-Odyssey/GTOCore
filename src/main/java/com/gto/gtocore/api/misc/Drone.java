package com.gto.gtocore.api.misc;

import com.gto.gtocore.common.item.DroneBehavior;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.item.capability.ElectricItem;

import net.minecraft.world.item.ItemStack;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public final class Drone {

    @Getter
    private String workState;

    private int work;

    @Getter
    private final int range;

    private final ElectricItem electricItem;

    private final ItemStack itemStack;

    private Drone(int range, ElectricItem electricItem, ItemStack itemStack) {
        this.range = range;
        this.electricItem = electricItem;
        this.itemStack = itemStack;
    }

    @Nullable
    public static Drone create(ItemStack itemStack) {
        DroneBehavior behavior = DroneBehavior.getDroneBehavior(itemStack);
        if (behavior != null) {
            Drone drone = new Drone(behavior.getRange(), (ElectricItem) GTCapabilityHelper.getElectricItem(itemStack), itemStack);
            drone.work = itemStack.getOrCreateTag().getInt("work");
            drone.workState = itemStack.getOrCreateTag().getString("workState");
            return drone;
        }
        return null;
    }

    public boolean isWork() {
        return work > 0;
    }

    public void work() {
        if (isWork()) {
            work--;
            itemStack.getOrCreateTag().putInt("work", work);
        } else if (!workState.isEmpty()) {
            workState = "";
            itemStack.getOrCreateTag().putString("workState", "");
        }
    }

    public boolean start(int time, int eu, String state) {
        eu = eu * (1 << electricItem.getTier());
        if (eu > electricItem.getCharge()) return false;
        electricItem.setCharge(electricItem.getCharge() - eu);
        work = time / (electricItem.getTier() - 2);
        workState = state;
        itemStack.getOrCreateTag().putString("workState", workState);
        itemStack.getOrCreateTag().putInt("work", work);
        return true;
    }
}
