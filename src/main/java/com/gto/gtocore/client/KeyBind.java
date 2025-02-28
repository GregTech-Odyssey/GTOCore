package com.gto.gtocore.client;

import com.gto.gtocore.common.network.ClientMessage;

import net.minecraft.client.KeyMapping;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;

final class KeyBind {

    private static final KeyMapping flyingspeedKey = new KeyMap("key.gtocore.flyingspeed", InputConstants.KEY_X, 0);
    private static final KeyMapping nightvisionKey = new KeyMap("key.gtocore.nightvision", InputConstants.KEY_Z, 1);
    private static final KeyMapping vajraKey = new KeyMap("key.gtocore.vajra", InputConstants.KEY_J, 2);
    private static final KeyMapping driftKey = new KeyMap("key.gtocore.drift", InputConstants.KEY_I, 3);

    public static void init() {
        KeyMappingRegistry.register(flyingspeedKey);
        KeyMappingRegistry.register(nightvisionKey);
        KeyMappingRegistry.register(vajraKey);
        KeyMappingRegistry.register(driftKey);
    }

    private static class KeyMap extends KeyMapping {

        private boolean isDownOld;
        private final int type;

        KeyMap(String name, int keyCode, int type) {
            super(name, keyCode, "key.keybinding.gtocore");
            this.type = type;
        }

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ClientMessage.sendData(String.valueOf(type), null);
            }
            isDownOld = isDown;
        }
    }
}
