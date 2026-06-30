package com.gtocore.utils;

import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class StxckUtil {

    public final String EXTRA_ITEM_COUNT_TAG = "StxckExtraItemCount";
    public EntityDataAccessor<Integer> DATA_EXTRA_ITEM_COUNT;

    public void setDataExtraItemCount(EntityDataAccessor<Integer> entityDataAccessor) {
        if (DATA_EXTRA_ITEM_COUNT != null) return;
        DATA_EXTRA_ITEM_COUNT = entityDataAccessor;
    }

    public void refillItemStack(ItemEntity entity) {
        var extraItemCount = getExtraItemCount(entity);
        if (extraItemCount <= 0) return;
        var stack = entity.getItem();
        var item = stack.item;
        if (item != null) {
            var maxSize = item.getMaxStackSize();
            if (stack.getCount() == maxSize) return;
            var x = maxSize - stack.getCount();
            var refillCount = Math.min(x, extraItemCount);
            stack.grow(refillCount);
            setExtraItemCount(entity, extraItemCount - refillCount);
            entity.setItem(stack.copy());
        }
    }

    public boolean areMergable(ItemEntity itemEntity, ItemEntity itemEntity1) {
        var max = getMaxSize();
        if (max - getExtraItemCount(itemEntity) < getTotalCount(itemEntity1) && max - getExtraItemCount(itemEntity1) < getTotalCount(itemEntity)) return false;
        return ItemStackHashStrategy.ITEM_AND_TAG.equals(itemEntity.getItem(), itemEntity1.getItem());
    }

    public void grow(ItemEntity entity, int count) {
        setExtraItemCount(entity, getExtraItemCount(entity) + count);
        refillItemStack(entity);
    }

    public boolean isMergable(ItemEntity entity) {
        var pickupDelay = entity.pickupDelay;
        var age = entity.age;
        return entity.isAlive() && pickupDelay != 32767 && age != -32768 && age < 6000;
    }

    public int getTotalCount(ItemEntity entity) {
        return entity.getItem().getCount() + getExtraItemCount(entity);
    }

    public int getExtraItemCount(Entity entity) {
        return entity.getEntityData().get(DATA_EXTRA_ITEM_COUNT);
    }

    public void setExtraItemCount(Entity entity, int count) {
        entity.getEntityData().set(DATA_EXTRA_ITEM_COUNT, count);
    }

    public boolean tryRefillItemStackOnEntityRemove(Entity entity, Entity.RemovalReason reason) {
        if (entity.getType() != EntityType.ITEM || reason != Entity.RemovalReason.DISCARDED) return false;
        var itemEntity = (ItemEntity) entity;
        if (getExtraItemCount(itemEntity) <= 0) return false;
        var copied = itemEntity.getItem().copy();
        itemEntity.setItem(copied);
        copied.setCount(0);
        refillItemStack(itemEntity);
        return true;
    }

    public void tryToMerge(ItemEntity itemEntity, ItemEntity itemEntity1) {
        tryToMerge(itemEntity, itemEntity1, false);
    }

    public void tryToMerge(ItemEntity itemEntity, ItemEntity itemEntity1, boolean mergeToExisted) {
        if (Objects.equals(itemEntity.getOwner(), itemEntity1.getOwner()) && areMergable(itemEntity, itemEntity1)) {
            if (mergeToExisted || getTotalCount(itemEntity1) < getTotalCount(itemEntity)) {
                merge(itemEntity, itemEntity1);
            } else {
                merge(itemEntity1, itemEntity);
            }
        }
    }

    public void merge(ItemEntity consumer, ItemEntity supplier) {
        consumer.pickupDelay = Math.max(consumer.pickupDelay, supplier.pickupDelay);
        consumer.age = Math.min(consumer.age, supplier.age);
        grow(consumer, getTotalCount(supplier));
        setExtraItemCount(supplier, 0);
        supplier.setItem(ItemStack.EMPTY);
        supplier.discard();
    }

    public boolean isBlackListItem(ItemStack itemStack) {
        return false;
    }

    public int getMaxSize() {
        return Integer.MAX_VALUE;
    }

    public double getMaxMergeDistanceHorizontal() {
        return 1.25;
    }

    public double getMaxMergeDistanceVertical() {
        return 0;
    }

    public int getMinItemCountRenderDistance() {
        return 8;
    }

    public double getOverlaySizeMultiplier() {
        return 0.8;
    }

    public int getCountTextMode() {
        return 2;
    }

    public boolean alwaysShowItemCount() {
        return false;
    }
}
