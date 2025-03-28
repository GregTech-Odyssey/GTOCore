package com.gto.gtocore.mixin.gtm.ae;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.integration.ae2.machine.MEPatternBufferPartMachine;
import com.gregtechceu.gtceu.utils.GTMath;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(MEPatternBufferPartMachine.InternalSlot.class)
public abstract class MEPatternBufferPartMachineMixin {

    @Shadow(remap = false)
    @Final
    private Object2LongOpenCustomHashMap<ItemStack> itemInventory;

    @Shadow(remap = false)
    @Final
    private Object2LongOpenHashMap<FluidStack> fluidInventory;

    @Shadow(remap = false)
    private List<ItemStack> itemStacks;

    @Shadow(remap = false)
    private List<FluidStack> fluidStacks;

    @Shadow(remap = false)
    public abstract void onContentsChanged();

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void add(AEKey what, long amount) {
        if (amount <= 0L) return;
        if (what instanceof AEItemKey itemKey) {
            var stack = itemKey.toStack();
            synchronized (itemInventory) {
                itemInventory.addTo(stack, amount);
            }
        } else if (what instanceof AEFluidKey fluidKey) {
            var stack = fluidKey.toStack(1);
            synchronized (fluidInventory) {
                fluidInventory.addTo(stack, amount);
            }
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public List<ItemStack> getItems() {
        if (itemStacks == null) {
            itemStacks = new ArrayList<>();
            synchronized (itemInventory) {
                itemInventory.object2LongEntrySet().stream().map(e -> GTMath.splitStacks(e.getKey(), e.getLongValue())).forEach(itemStacks::addAll);
            }
        }
        return itemStacks;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public List<FluidStack> getFluids() {
        if (fluidStacks == null) {
            synchronized (fluidInventory) {
                fluidStacks = fluidInventory.object2LongEntrySet().stream().map(e -> new FluidStack(e.getKey(), GTMath.saturatedCast(e.getLongValue()))).toList();
            }
        }
        return fluidStacks;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public @Nullable List<Ingredient> handleItemInternal(List<Ingredient> left, boolean simulate) {
        boolean changed = false;
        for (var it = left.listIterator(); it.hasNext();) {
            var ingredient = it.next();
            if (ingredient.isEmpty()) {
                it.remove();
                continue;
            }
            var items = ingredient.getItems();
            if (items.length == 0 || items[0].isEmpty()) {
                it.remove();
                continue;
            }
            int amount = items[0].getCount();
            synchronized (itemInventory) {
                for (var it2 = itemInventory.object2LongEntrySet().iterator(); it2.hasNext();) {
                    var entry = it2.next();
                    var stack = entry.getKey();
                    var count = entry.getLongValue();
                    if (stack.isEmpty() || count == 0) {
                        it2.remove();
                        continue;
                    }
                    if (!ingredient.test(stack)) continue;
                    int extracted = Math.min(GTMath.saturatedCast(count), amount);
                    if (!simulate && extracted > 0) {
                        changed = true;
                        count -= extracted;
                        if (count == 0) it2.remove();
                        else entry.setValue(count);
                    }
                    amount -= extracted;
                    if (amount <= 0) {
                        it.remove();
                        break;
                    }
                }
            }
            if (amount > 0) {
                if (ingredient instanceof SizedIngredient si) {
                    si.setAmount(amount);
                } else {
                    items[0].setCount(amount);
                }
            }
        }
        if (changed) onContentsChanged();
        return left.isEmpty() ? null : left;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public @Nullable List<FluidIngredient> handleFluidInternal(List<FluidIngredient> left, boolean simulate) {
        boolean changed = false;
        for (var it = left.listIterator(); it.hasNext();) {
            var ingredient = it.next();
            if (ingredient.isEmpty()) {
                it.remove();
                continue;
            }
            var fluids = ingredient.getStacks();
            if (fluids.length == 0 || fluids[0].isEmpty()) {
                it.remove();
                continue;
            }
            int amount = fluids[0].getAmount();
            synchronized (fluidInventory) {
                for (var it2 = fluidInventory.object2LongEntrySet().iterator(); it2.hasNext();) {
                    var entry = it2.next();
                    var stack = entry.getKey();
                    var count = entry.getLongValue();
                    if (stack.isEmpty() || count == 0) {
                        it2.remove();
                        continue;
                    }
                    if (!ingredient.test(stack)) continue;
                    int extracted = Math.min(GTMath.saturatedCast(count), amount);
                    if (!simulate && extracted > 0) {
                        changed = true;
                        count -= extracted;
                        if (count == 0) it2.remove();
                        else entry.setValue(count);
                    }
                    amount -= extracted;
                    if (amount <= 0) {
                        it.remove();
                        break;
                    }
                }
            }
            if (amount > 0) {
                ingredient.setAmount(amount);
            }
        }
        if (changed) onContentsChanged();
        return left.isEmpty() ? null : left;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag itemsTag = new ListTag();
        synchronized (itemInventory) {
            for (var entry : itemInventory.object2LongEntrySet()) {
                var ct = entry.getKey().serializeNBT();
                ct.putLong("real", entry.getLongValue());
                itemsTag.add(ct);
            }
        }
        if (!itemsTag.isEmpty()) tag.put("inventory", itemsTag);
        ListTag fluidsTag = new ListTag();
        synchronized (fluidInventory) {
            for (var entry : fluidInventory.object2LongEntrySet()) {
                var ct = entry.getKey().writeToNBT(new CompoundTag());
                ct.putLong("real", entry.getLongValue());
                fluidsTag.add(ct);
            }
        }
        if (!fluidsTag.isEmpty()) tag.put("fluidInventory", fluidsTag);
        return tag;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void deserializeNBT(CompoundTag tag) {
        ListTag items = tag.getList("inventory", Tag.TAG_COMPOUND);
        for (Tag t : items) {
            if (!(t instanceof CompoundTag ct)) continue;
            var stack = ItemStack.of(ct);
            var count = ct.getLong("real");
            if (!stack.isEmpty() && count > 0) {
                synchronized (itemInventory) {
                    itemInventory.put(stack, count);
                }
            }
        }
        ListTag fluids = tag.getList("fluidInventory", Tag.TAG_COMPOUND);
        for (Tag t : fluids) {
            if (!(t instanceof CompoundTag ct)) continue;
            var stack = FluidStack.loadFluidStackFromNBT(ct);
            var amount = ct.getLong("real");
            if (!stack.isEmpty() && amount > 0) {
                synchronized (fluidInventory) {
                    fluidInventory.put(stack, amount);
                }
            }
        }
    }
}
