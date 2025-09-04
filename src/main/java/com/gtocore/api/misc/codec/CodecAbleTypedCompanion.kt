package com.gtocore.api.misc.codec

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.FriendlyByteBuf

import com.mojang.serialization.Codec

interface CodecAbleTypedCompanion<T> {
    fun getCodec(): Codec<T>

    // 函数式值传递，会创建新对象
    fun decodeFromNbt(nbt: CompoundTag): T = getCodec().decode(NbtOps.INSTANCE, nbt).getOrThrow(true) { "Failed to decode $this" }.first
    fun decodeFromBuffer(buf: FriendlyByteBuf): T = decodeFromNbt(buf.readNbt() ?: throw IllegalArgumentException("Buffer does not contain NBT data"))
}
