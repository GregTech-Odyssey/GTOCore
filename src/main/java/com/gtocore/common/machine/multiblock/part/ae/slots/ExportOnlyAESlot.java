package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;

import net.minecraft.nbt.CompoundTag;

import appeng.api.stacks.GenericStack;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import org.jetbrains.annotations.Nullable;

public abstract class ExportOnlyAESlot implements IConfigurableSlot, ITagSerializable<CompoundTag>, IContentChangeAware {

    private static final String CONFIG_TAG = "config";
    private static final String STOCK_TAG = "stock";
    private Runnable onContentsChanged = () -> {};
    @Nullable
    GenericStack config;
    @Nullable
    GenericStack stock;

    ExportOnlyAESlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
        this.config = config;
        this.stock = stock;
    }

    ExportOnlyAESlot() {
        this(null, null);
    }

    @Nullable
    public GenericStack requestStack() {
        if (this.stock != null && this.stock.amount() <= 0) {
            this.stock = null;
        }
        if (this.config == null || (this.stock != null && !this.config.what().matches(this.stock))) {
            return null;
        }
        if (this.stock == null) {
            return copy(this.config);
        }
        if (this.stock.amount() <= this.config.amount()) {
            return copy(this.config, this.config.amount() - this.stock.amount());
        }
        return null;
    }

    @Nullable
    public GenericStack exceedStack() {
        if (this.stock != null && this.stock.amount() <= 0) {
            this.stock = null;
        }
        if (this.config == null && this.stock != null) {
            return copy(this.stock);
        }
        if (this.config != null && this.stock != null) {
            if (this.config.what().matches(this.stock) && this.config.amount() < this.stock.amount()) {
                return copy(this.stock, this.stock.amount() - this.config.amount());
            }
            if (!this.config.what().matches(this.stock)) {
                return copy(this.stock);
            }
        }
        return null;
    }

    void onContentsChanged() {
        if (onContentsChanged != null) {
            onContentsChanged.run();
        }
    }

    protected abstract void addStack(GenericStack stack);

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (this.config != null) {
            CompoundTag configTag = GenericStack.writeTag(this.config);
            tag.put(CONFIG_TAG, configTag);
        }
        if (this.stock != null) {
            CompoundTag stockTag = GenericStack.writeTag(this.stock);
            tag.put(STOCK_TAG, stockTag);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains(CONFIG_TAG)) {
            this.config = GenericStack.readTag(tag.getCompound(CONFIG_TAG));
        }
        if (tag.contains(STOCK_TAG)) {
            this.stock = GenericStack.readTag(tag.getCompound(STOCK_TAG));
        }
    }

    static GenericStack copy(GenericStack stack) {
        return new GenericStack(stack.what(), stack.amount());
    }

    public static GenericStack copy(GenericStack stack, long amount) {
        return new GenericStack(stack.what(), amount);
    }

    @Override
    @Nullable
    public GenericStack getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(@Nullable final GenericStack config) {
        this.config = config;
    }

    @Override
    @Nullable
    public GenericStack getStock() {
        return this.stock;
    }

    @Override
    public void setOnContentsChanged(final Runnable onContentsChanged) {
        this.onContentsChanged = onContentsChanged;
    }

    @Override
    public Runnable getOnContentsChanged() {
        return this.onContentsChanged;
    }
}
