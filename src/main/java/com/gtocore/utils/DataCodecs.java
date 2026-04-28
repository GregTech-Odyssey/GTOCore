package com.gtocore.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

import com.gto.datasynclib.datasream.codec.DataCodec;
import com.gto.datasynclib.datasream.data.Data;
import com.gto.datasynclib.datasream.data.DataOps;

public final class DataCodecs {

    public static final DataCodec<CompoundTag> COMPOUND_TAG = new DataCodec<>() {

        @Override
        public CompoundTag decode(Data data) {
            return (CompoundTag) DataOps.INSTANCE.convertTo(NbtOps.INSTANCE, data);
        }

        @Override
        public Data encode(CompoundTag obj) {
            return NbtOps.INSTANCE.convertTo(DataOps.INSTANCE, obj);
        }

        static {
            DataCodec.registerCodec(CompoundTag.class, COMPOUND_TAG);
        }
    };
}
