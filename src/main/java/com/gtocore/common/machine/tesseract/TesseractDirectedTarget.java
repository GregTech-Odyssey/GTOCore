package com.gtocore.common.machine.tesseract;

import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public record TesseractDirectedTarget(GlobalPos pos, Direction face, int order) implements Comparable<TesseractDirectedTarget> {

    public static final Codec<TesseractDirectedTarget> CODEC = RecordCodecBuilder.create(i -> i.group(
            GlobalPos.CODEC.fieldOf("pos").forGetter(TesseractDirectedTarget::pos),
            Direction.CODEC.fieldOf("face").forGetter(TesseractDirectedTarget::face),
            Codec.INT.fieldOf("order").forGetter(TesseractDirectedTarget::order)).apply(i, TesseractDirectedTarget::new));
    public static final Codec<List<TesseractDirectedTarget>> LIST_CODEC = new ListCodec<>(CODEC);
    public static final Comparator<TesseractDirectedTarget> SORTER = Comparator
            .comparingInt((TesseractDirectedTarget pf) -> {
                if (pf.order() < 0) {
                    return Integer.MAX_VALUE + pf.order();
                } else {
                    return pf.order();
                }
            });

    @Override
    public int compareTo(@NotNull TesseractDirectedTarget o) {
        return SORTER.compare(this, o);
    }

    public static void writeToBuffer(FriendlyByteBuf buffer, TesseractDirectedTarget tesseractDirectedTarget) {
        buffer.writeGlobalPos(tesseractDirectedTarget.pos());
        buffer.writeShort((short) tesseractDirectedTarget.face().get3DDataValue());
        buffer.writeVarInt(tesseractDirectedTarget.order());
    }

    public static TesseractDirectedTarget readFromBuffer(FriendlyByteBuf buffer) {
        GlobalPos pos = buffer.readGlobalPos();
        Direction face = Direction.from3DDataValue(buffer.readShort());
        int order = buffer.readVarInt();
        return new TesseractDirectedTarget(pos, face, order);
    }

    public static class Payload extends ObjectTypedPayload<TesseractDirectedTarget> {

        @Override
        public @Nullable Tag serializeNBT() {
            return CODEC.encodeStart(NbtOps.INSTANCE, getPayload())
                    .result().orElse(new CompoundTag());
        }

        @Override
        public void deserializeNBT(Tag tag) {
            TesseractDirectedTarget result = CODEC.parse(NbtOps.INSTANCE, tag)
                    .result().orElse(null);
            setPayload(result);
        }

        @Override
        public void writePayload(FriendlyByteBuf buf) {
            writeToBuffer(buf, getPayload());
        }

        @Override
        public void readPayload(FriendlyByteBuf buf) {
            TesseractDirectedTarget result = readFromBuffer(buf);
            setPayload(result);
        }
    }
}
