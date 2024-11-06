package com.gto.gtocore.api.pattern.util;

import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

public interface IValueContainer<T> {

    void operate(Block block, Object data);

    T getValue();

    static IValueContainer<Object> noop() {
        return new NOOP();
    }

    class NOOP implements IValueContainer<Object> {

        @Override
        public void operate(Block block, Object data) {}

        @Override
        public @Nullable Object getValue() {
            return null;
        }
    }
}
