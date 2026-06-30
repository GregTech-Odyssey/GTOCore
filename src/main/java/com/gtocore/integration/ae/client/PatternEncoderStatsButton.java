package com.gtocore.integration.ae.client;

import com.gtolib.GTOCore;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import appeng.client.gui.widgets.IconButton;

import gto_ae.hooks.gui.IIcon;

import org.jetbrains.annotations.Nullable;

public final class PatternEncoderStatsButton extends IconButton {

    private static final IIcon ICON = new TextureIcon(GTOCore.id("textures/gui/pattern_encoder_stats.png"));

    public PatternEncoderStatsButton(OnPress onPress) {
        super(onPress);
        setMessage(Component.translatable("gtocore.pattern_encoder_stats.button"));
    }

    @Override
    protected @Nullable IIcon getIIcon() {
        return ICON;
    }

    private record TextureIcon(ResourceLocation texture) implements IIcon {

        @Override
        public ResourceLocation getIconTexture() {
            return texture;
        }

        @Override
        public int getIconTextureWidth() {
            return 16;
        }

        @Override
        public int getIconTextureHeight() {
            return 16;
        }

        @Override
        public int getIconX() {
            return 0;
        }

        @Override
        public int getIconY() {
            return 0;
        }

        @Override
        public int getIconWidth() {
            return 16;
        }

        @Override
        public int getIconHeight() {
            return 16;
        }
    }
}
