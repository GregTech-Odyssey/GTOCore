package com.gtocore.api.misc.codec

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.common.util.INBTSerializable

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

interface CodecAbleTyped<SELF : CodecAbleTyped<SELF, COMPANION>, COMPANION : CodecAbleTypedCompanion<SELF>> : INBTSerializable<CompoundTag> {
    companion object {
        private val interfacePropertyNames: Set<String> by lazy {
            CodecAbleTyped::class.memberProperties.mapTo(mutableSetOf()) { it.name }
        }
        private val classPropertyCache = ConcurrentHashMap<KClass<*>, List<KMutableProperty1<Any, Any?>>>()

        @Suppress("UNCHECKED_CAST")
        private fun getCopyableProperties(klass: KClass<*>): List<KMutableProperty1<Any, Any?>> = classPropertyCache.computeIfAbsent(klass) {
            klass.memberProperties
                .asSequence()
                .filter { it.name !in interfacePropertyNames }
                .filterIsInstance<KMutableProperty1<*, *>>()
                .onEach { it.isAccessible = true }
                .map { it as KMutableProperty1<Any, Any?> }
                .toList()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getCompanion(): COMPANION {
        val companionInstance = this::class.companionObjectInstance
        return companionInstance as? COMPANION ?: throw IllegalStateException("${this::class.simpleName} 的伴生对象未实现 CodecAble")
    }

    @Suppress("UNCHECKED_CAST")
    fun encodeToNbt(): CompoundTag = getCompanion().getCodec().encodeStart(NbtOps.INSTANCE, this as SELF).getOrThrow(false) { "Failed to encode $this" } as CompoundTag
    fun encodeToBuffer(buf: FriendlyByteBuf) {
        buf.writeNbt(encodeToNbt())
    }

    override fun serializeNBT(): CompoundTag = encodeToNbt()

    // 引用传递，不会创建新对象
    override fun deserializeNBT(p0: CompoundTag) {
        copyFrom(getCompanion().decodeFromNbt(p0))
    }

    @Suppress("UNCHECKED_CAST")
    fun copyFrom(other: SELF) {
        val thisKlass = this::class
        val otherKlass = other::class
        require(thisKlass == otherKlass) { "Cannot copy properties from $otherKlass to $thisKlass" }
        getCopyableProperties(thisKlass).forEach {
            val value = it.get(other)
            it.set(this, value)
        }
    }
}
