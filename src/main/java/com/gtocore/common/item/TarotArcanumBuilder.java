package com.gtocore.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TarotArcanumBuilder {

    private final int color;
    private final int serialNumber;
    private final List<Consumer<List<Component>>> tooltipConsumers = new ArrayList<>();

    public TarotArcanumBuilder(int color, int serialNumber) {
        this.color = color;
        this.serialNumber = serialNumber;
    }

    public TarotArcanumBuilder withTooltip(Consumer<List<Component>> tooltipConsumer) {
        this.tooltipConsumers.add(tooltipConsumer);
        return this;
    }

    public TarotArcanum build(Item.Properties properties) {
        return TarotArcanum.create(properties, color, serialNumber, tooltipConsumers);
    }

    // 静态工厂方法
    public static TarotArcanumBuilder of(int color, int serialNumber) {
        return new TarotArcanumBuilder(color, serialNumber);
    }
}
