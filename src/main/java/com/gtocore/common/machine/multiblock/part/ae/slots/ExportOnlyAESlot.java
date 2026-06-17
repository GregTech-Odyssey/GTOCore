package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableContentHandler;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import appeng.api.stacks.GenericStack;

import com.gto.datasynclib.AbstractDataSerializable;
import com.gto.datasynclib.LogicalSide;
import com.gto.datasynclib.datasream.data.Data;
import com.gto.datasynclib.util.DataCodecs;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ExportOnlyAESlot extends AbstractDataSerializable implements IConfigurableSlot {

    private static final String CONFIG_TAG = "config";
    private static final String STOCK_TAG = "stock";
    @Nullable
    @Setter
    private NotifiableContentHandler handler;
    @Nullable
    GenericStack config;
    /** 槽位的当前库存，可为空。 */
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

    final void onContentsChanged() {
        if (handler != null) handler.onContentsChanged();
    }

    protected abstract void addStack(GenericStack stack);

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
    public void writeBuf(LogicalSide side, @NotNull FriendlyByteBuf data) {
        GenericStack.writeBuffer(this.config, data);
        GenericStack.writeBuffer(this.stock, data);
    }

    @Override
    public void readBuf(LogicalSide side, @NotNull FriendlyByteBuf data) {
        this.config = GenericStack.readBuffer(data);
        this.stock = GenericStack.readBuffer(data);
    }

    @Override
    public Data writeData() {
        return DataCodecs.COMPOUND_TAG_CODEC.encode(serializeNBT());
    }

    @Override
    public void readData(@NotNull Data data, int dataVersion) {
        deserializeNBT(DataCodecs.COMPOUND_TAG_CODEC.decode(data, dataVersion));
    }
}
