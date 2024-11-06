package com.gto.gtocore.client;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.network.KeyMessage;

import net.minecraft.client.KeyMapping;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;

public class KeyBind {

    public static final KeyMapping flyingspeedKey = new KeyMap("key.gtocore.flyingspeed", InputConstants.KEY_X, 0);
    public static final KeyMapping nightvisionKey = new KeyMap("key.gtocore.nightvision", InputConstants.KEY_Z, 1);
    public static final KeyMapping pearlKey = new KeyMap("key.gtocore.pearl", InputConstants.KEY_Z, 2);
    public static final KeyMapping vajraKey = new KeyMap("key.gtocore.vajra", InputConstants.KEY_J, 3);

    public static void init() {
        KeyMappingRegistry.register(flyingspeedKey);
        KeyMappingRegistry.register(nightvisionKey);
        KeyMappingRegistry.register(pearlKey);
        KeyMappingRegistry.register(vajraKey);
    }

    private static class KeyMap extends KeyMapping {

        private boolean isDownOld = false;
        private final int type;

        public KeyMap(String name, int keyCode, int type) {
            super(name, keyCode, "key.keybinding.gtocore");
            this.type = type;
        }

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                GTOCore.PACKET_HANDLER.sendToServer(new KeyMessage(type, 0));
            }
            isDownOld = isDown;
        }
    }
}
