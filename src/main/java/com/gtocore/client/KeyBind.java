package com.gtocore.client;

import com.gtolib.utils.ClientUtil;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import org.lwjgl.glfw.GLFW;

public final class KeyBind {

    public static final KeyMapping vajraKey = new KeyMap("key.gtocore.vajra", InputConstants.KEY_J, 2);
    public static final KeyMapping debugInspectKey = new KeyMapping("key.gtocore.debug_inspect",
            KeyConflictContext.GUI, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.keybinding.gtocore");
    public static final KeyMapping movableHudToggle = new KeyMapping("key.gtocore.movable_hud_toggle",
            InputConstants.KEY_Z, "key.keybinding.gtocore");

    public static void init() {
        KeyMappingRegistry.register(vajraKey);
        KeyMappingRegistry.register(debugInspectKey);
        KeyMappingRegistry.register(movableHudToggle);
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
            if (type >= 0 && isDownOld != isDown && isDown && ClientUtil.getPlayer() != null) {
                KeyMessage.NETWORK_PACK.send(b -> b.writeVarInt(type));
            }
            isDownOld = isDown;
        }
    }
}
