package com.gtocore.common.data;

import net.minecraft.network.FriendlyByteBuf;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;

import com.gto.datasynclib.DataSyncCodec;
import com.gto.datasynclib.datasream.codec.ByteStreamCodec;
import com.gto.datasynclib.datasream.codec.DataCodec;
import com.gto.datasynclib.datasream.data.Data;
import com.gto.datasynclib.util.DataCodecs;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class GTOCodecs {

    public static void init() {
        DataSyncCodec.register(AEItemKey.class, GTOCodecs.AE_ITEM_KEY_STREAM_CODEC, GTOCodecs.AE_ITEM_KEY_DATA_CODEC);
        DataSyncCodec.register(AEFluidKey.class, GTOCodecs.AE_FLUID_KEY_STREAM_CODEC, GTOCodecs.AE_FLUID_KEY_DATA_CODEC);
        DataSyncCodec.register(AEKey.class, GTOCodecs.AE_KEY_STREAM_CODEC, GTOCodecs.AE_KEY_DATA_CODEC);
        DataSyncCodec.register(GenericStack.class, GTOCodecs.GENERIC_STACK_STREAM_CODEC, GTOCodecs.GENERIC_STACK_DATA_CODEC);
    }

    public final DataCodec<AEItemKey> AE_ITEM_KEY_DATA_CODEC = new DataCodec<>() {

        @Override
        public AEItemKey decode(@NotNull Data data, int dataVersion) {
            return AEItemKey.fromTag(DataCodecs.COMPOUND_TAG_CODEC.decode(data, dataVersion));
        }

        @Override
        public @NotNull Data encode(AEItemKey obj) {
            return DataCodecs.COMPOUND_TAG_CODEC.encode(obj.toTag());
        }
    };

    public final DataCodec<AEFluidKey> AE_FLUID_KEY_DATA_CODEC = new DataCodec<>() {

        @Override
        public AEFluidKey decode(@NotNull Data data, int dataVersion) {
            return AEFluidKey.fromTag(DataCodecs.COMPOUND_TAG_CODEC.decode(data, dataVersion));
        }

        @Override
        public @NotNull Data encode(AEFluidKey obj) {
            return DataCodecs.COMPOUND_TAG_CODEC.encode(obj.toTag());
        }
    };

    public final DataCodec<AEKey> AE_KEY_DATA_CODEC = new DataCodec<>() {

        @Override
        public AEKey decode(@NotNull Data data, int dataVersion) {
            return AEKey.fromTagGeneric(DataCodecs.COMPOUND_TAG_CODEC.decode(data, dataVersion));
        }

        @Override
        public @NotNull Data encode(AEKey obj) {
            return DataCodecs.COMPOUND_TAG_CODEC.encode(obj.toTagGeneric());
        }
    };

    public final DataCodec<GenericStack> GENERIC_STACK_DATA_CODEC = new DataCodec<>() {

        @Override
        public GenericStack decode(@NotNull Data data, int dataVersion) {
            return GenericStack.readTag(DataCodecs.COMPOUND_TAG_CODEC.decode(data, dataVersion));
        }

        @Override
        public @NotNull Data encode(GenericStack obj) {
            return DataCodecs.COMPOUND_TAG_CODEC.encode(GenericStack.writeTag(obj));
        }
    };

    public final ByteStreamCodec<AEItemKey> AE_ITEM_KEY_STREAM_CODEC = new ByteStreamCodec<>() {

        @Override
        public void encode(FriendlyByteBuf buf, AEItemKey obj) {
            obj.writeToPacket(buf);
        }

        @Override
        public AEItemKey decode(FriendlyByteBuf buf) {
            return AEItemKey.fromPacket(buf);
        }
    };

    public final ByteStreamCodec<AEFluidKey> AE_FLUID_KEY_STREAM_CODEC = new ByteStreamCodec<>() {

        @Override
        public void encode(FriendlyByteBuf buf, AEFluidKey obj) {
            obj.writeToPacket(buf);
        }

        @Override
        public AEFluidKey decode(FriendlyByteBuf buf) {
            return AEFluidKey.fromPacket(buf);
        }
    };

    public final ByteStreamCodec<AEKey> AE_KEY_STREAM_CODEC = new ByteStreamCodec<>() {

        @Override
        public void encode(FriendlyByteBuf buf, AEKey obj) {
            AEKey.writeKey(buf, obj);
        }

        @Override
        public AEKey decode(FriendlyByteBuf buf) {
            return AEKey.readKey(buf);
        }
    };

    public final ByteStreamCodec<GenericStack> GENERIC_STACK_STREAM_CODEC = new ByteStreamCodec<>() {

        @Override
        public void encode(FriendlyByteBuf buf, GenericStack obj) {
            AEKey.writeKey(buf, obj.what());
            buf.writeVarLong(obj.amount());
        }

        @Override
        public GenericStack decode(FriendlyByteBuf buf) {
            var what = AEKey.readKey(buf);
            if (what == null) {
                buf.readVarLong();
                return null;
            }
            return new GenericStack(what, buf.readVarLong());
        }
    };
}
