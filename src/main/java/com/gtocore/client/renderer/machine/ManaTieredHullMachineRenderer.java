package com.gtocore.client.renderer.machine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.client.renderer.machine.MachineRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;

class ManaTieredHullMachineRenderer extends MachineRenderer {

    ManaTieredHullMachineRenderer(int tier, ResourceLocation modelLocation) {
        super(modelLocation);
        if (tier < 7) {
            setTextureOverride(Map.of("side",
                    GTOCore.id("block/casings/mana_voltage/%s".formatted(GTValues.VN[tier].toLowerCase(Locale.ROOT)))));
        } else {
            setTextureOverride(Map.of(
                    "bottom",
                    GTCEu.id("block/casings/voltage/%s/bottom".formatted(GTValues.VN[tier].toLowerCase(Locale.ROOT))),
                    "top", GTCEu.id("block/casings/voltage/%s/top".formatted(GTValues.VN[tier].toLowerCase(Locale.ROOT))),
                    "side",
                    GTCEu.id("block/casings/voltage/%s/side".formatted(GTValues.VN[tier].toLowerCase(Locale.ROOT)))));

        }
    }

    @NotNull
    @Override
    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(override.get("side"));
    }
}
