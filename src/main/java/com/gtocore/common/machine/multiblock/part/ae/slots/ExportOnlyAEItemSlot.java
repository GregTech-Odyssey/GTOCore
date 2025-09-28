package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.utils.GTMath;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExportOnlyAEItemSlot extends ExportOnlyAESlot {

    ItemStack stack = null;

    public ExportOnlyAEItemSlot() {
        super();
    }

    ExportOnlyAEItemSlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
        super(config, stock);
    }

    @Override
    public void addStack(GenericStack stack) {
        if (this.stock == null) {
            this.stock = stack;
            this.stack = null;
        } else {
            this.stock = GenericStack.sum(this.stock, stack);
            if (this.stack != null) {
                this.stack.setCount(MathUtil.saturatedCast(this.stack.getCount() + stack.amount()));
            }
        }
        onContentsChanged();
    }

    @Override
    public void setStock(@Nullable GenericStack stack) {
        if (this.stock == null && stack == null) {
            return;
        } else if (stack == null) {
            this.stock = null;
        } else {
            if (stack.equals(stock)) return;
            this.stock = stack;
        }
        this.stack = null;
        onContentsChanged();
    }

    public ItemStack getReadOnlyStack() {
        if (this.stock != null && this.stock.what() instanceof AEItemKey itemKey) {
            return itemKey.getReadOnlyStack();
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getStack() {
        if (this.stock != null) {
            if (stack == null) stack = this.stock.what() instanceof AEItemKey itemKey ? itemKey.toStack(GTMath.saturatedCast(this.stock.amount())) : ItemStack.EMPTY;
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public long extractItem(long amount, boolean simulate, boolean notify) {
        if (this.stock != null) {
            long extracted = Math.min(this.stock.amount(), amount);
            if (!(this.stock.what() instanceof AEItemKey)) return 0;
            if (!simulate) {
                this.stock = ExportOnlyAESlot.copy(this.stock, this.stock.amount() - extracted);
                if (this.stock.amount() == 0) {
                    this.stock = null;
                    stack = null;
                } else if (stack != null) stack.setCount(MathUtil.saturatedCast(stock.amount()));
                if (notify) onContentsChanged();
            }
            return extracted;
        }
        return 0;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == 0 && this.stock != null) {
            int extracted = (int) Math.min(this.stock.amount(), amount);
            if (!(this.stock.what() instanceof AEItemKey itemKey)) return ItemStack.EMPTY;
            ItemStack result = itemKey.toStack(extracted);
            if (!simulate) {
                this.stock = ExportOnlyAESlot.copy(this.stock, this.stock.amount() - extracted);
                if (this.stock.amount() == 0) {
                    this.stock = null;
                    stack = null;
                } else if (stack != null) stack.setCount(MathUtil.saturatedCast(stock.amount()));
                onContentsChanged();
            }
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ExportOnlyAEItemSlot copy() {
        return new ExportOnlyAEItemSlot(
                this.config == null ? null : copy(this.config),
                this.stock == null ? null : copy(this.stock));
    }
}
