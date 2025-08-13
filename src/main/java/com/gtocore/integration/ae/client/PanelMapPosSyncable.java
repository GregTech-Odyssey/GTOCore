package com.gtocore.integration.ae.client;

import com.gtolib.ae2.me2in1.panel.PanelPosMap;

import net.minecraft.network.FriendlyByteBuf;

import appeng.menu.guisync.PacketWritable;

/// 混淆后的记录不会被{@link Class#isRecord()}识别为记录类型
/// 无法被{@link DataSynchronization#collectFields(Object, Class)}收集
/// 因此需要一份非混淆的记录类来实现{@link PacketWritable}接口
public record PanelMapPosSyncable(PanelPosMap map) implements PacketWritable {

    @Override
    public void writeToPacket(FriendlyByteBuf data) {
        this.map.writeToPacket(data);
    }

    public PanelMapPosSyncable(FriendlyByteBuf buf) {
        this(new PanelPosMap(buf));
    }
}
