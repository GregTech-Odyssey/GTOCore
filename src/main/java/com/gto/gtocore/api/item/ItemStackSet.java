package com.gto.gtocore.api.item;

import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ItemStackSet implements Set<ItemStack> {

    private final Map<NBTItem, ItemStack> map = new Object2ObjectOpenHashMap<>();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof ItemStack stack) {
            return map.containsKey(NBTItem.of(stack));
        }
        return false;
    }

    @Override
    public @NotNull Iterator<ItemStack> iterator() {
        return map.values().iterator();
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return map.values().toArray();
    }

    @Override
    public @NotNull <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return map.values().toArray(a);
    }

    @Override
    public boolean add(ItemStack itemStack) {
        if (itemStack.isEmpty()) return false;
        NBTItem content = NBTItem.of(itemStack);
        ItemStack stack = map.get(content);
        if (stack == null) {
            map.put(content, itemStack.copy());
        } else {
            stack.setCount(stack.getCount() + itemStack.getCount());
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends ItemStack> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        map.clear();
    }
}
