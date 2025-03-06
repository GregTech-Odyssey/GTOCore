package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.network.FromClientMessage;
import com.gto.gtocore.common.network.FromServerMessage;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;

public interface GTONet {

    SimpleNetworkManager NET = SimpleNetworkManager.create(GTOCore.MOD_ID);

    MessageType SEND_DATA_FROM_CLIENT = NET.registerC2S("from_client", FromClientMessage::new);
    MessageType SEND_DATA_FROM_SERVER = NET.registerS2C("from_server", FromServerMessage::new);

    static void init() {}
}
