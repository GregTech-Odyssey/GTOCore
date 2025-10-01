package com.gtocore.client;

import com.gtocore.common.network.ClientMessage;
import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;
import com.gtolib.utils.ClientUtil;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.io.ConfigIO;
import dev.toma.configuration.config.value.BooleanValue;
import org.lwjgl.glfw.GLFW;

public final class KeyBind {

    private static final KeyMapping flyingspeedKey = new KeyMap("key.gtocore.flyingspeed", InputConstants.KEY_X, 0);
    private static final KeyMapping nightvisionKey = new KeyMap("key.gtocore.nightvision", InputConstants.KEY_Z, 1) {

        @Override
        public void setDown(boolean isDown) {
            if (isDownOld != isDown && isDown && ClientUtil.getPlayer() != null) {
                ConfigHolder.getConfig(GTOCore.MOD_ID).ifPresent(config -> {
                    ((BooleanValue) (config.getValueMap().get("nightVision"))).set(!GTOConfig.INSTANCE.nightVision);
                    ConfigIO.saveClientValues(config);
                    ConfigIO.reloadClientValues(config);
                });
            }
            super.setDown(isDown);
        }
    };
    public static final KeyMapping vajraKey = new KeyMap("key.gtocore.vajra", InputConstants.KEY_J, 2);
    private static final KeyMapping driftKey = new KeyMap("key.gtocore.drift", InputConstants.KEY_I, 3);
    public static final KeyMapping debugInspectKey = new KeyMapping("key.gtocore.debug_inspect",
            KeyConflictContext.GUI, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.keybinding.gtocore");

    public static void init() {
        KeyMappingRegistry.register(flyingspeedKey);
        KeyMappingRegistry.register(nightvisionKey);
        KeyMappingRegistry.register(vajraKey);
        KeyMappingRegistry.register(driftKey);
        KeyMappingRegistry.register(debugInspectKey);
    }

    private static class KeyMap extends KeyMapping {

        boolean isDownOld;
        private final int type;

        KeyMap(String name, int keyCode, int type) {
            super(name, keyCode, "key.keybinding.gtocore");
            this.type = type;
        }

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown && ClientUtil.getPlayer() != null) {
                ClientMessage.send("key", buf -> buf.writeVarInt(type));
            }
            isDownOld = isDown;
        }
    }
}
