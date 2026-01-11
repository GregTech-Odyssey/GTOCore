package com.gtocore.common.syncdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import appeng.api.stacks.GenericStack;
import com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload;
import org.jetbrains.annotations.Nullable;

public class GenericStackPayload extends ObjectTypedPayload<GenericStack> {

    @Override
    public @Nullable Tag serializeNBT() {
        return GenericStack.writeTag(payload);
    }

    @Override
    public void deserializeNBT(Tag tag) {
        payload = GenericStack.readTag((CompoundTag) tag);
    }

    @Override
    public void writePayload(FriendlyByteBuf buf) {
        GenericStack.writeBuffer(payload, buf);
    }

    @Override
    public void readPayload(FriendlyByteBuf buf) {
        payload = GenericStack.readBuffer(buf);
    }
}
