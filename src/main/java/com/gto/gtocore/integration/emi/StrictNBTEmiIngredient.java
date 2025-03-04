package com.gto.gtocore.integration.emi;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("all")
class StrictNBTEmiIngredient extends ItemEmiStack {

    StrictNBTEmiIngredient(Item item, @NotNull CompoundTag nbt, long amount) {
        super(item, nbt, amount);
    }

    @Override
    public boolean isEqual(EmiStack stack) {
        if (stack instanceof ItemEmiStack itemEmiStack) {
            if (itemEmiStack.getKey() == getKey()) {
                CompoundTag nbt = itemEmiStack.getNbt();
                if (nbt == null) return true;
                return nbt.equals(getNbt());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getNbt());
    }
}
