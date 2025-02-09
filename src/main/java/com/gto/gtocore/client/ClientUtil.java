package com.gto.gtocore.client;

import com.gto.gtocore.client.forge.ForgeClientEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;

@OnlyIn(Dist.CLIENT)
public final class ClientUtil {

    public static void highlighting(BlockPos pos, int radius) {
        if (pos == null) return;
        ForgeClientEvent.highlightingTime = 200;
        ForgeClientEvent.highlightingPos = pos;
        ForgeClientEvent.highlightingRadius = radius;
    }

    public static LocalPlayer getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static TextureAtlasSprite getBlockSprite(ResourceLocation location) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);
    }

    public static BakedModel getBakedModel(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().getModelManager().getModel(resourceLocation);
    }

    public static ItemRenderer getItemRenderer() {
        return Minecraft.getInstance().getItemRenderer();
    }

    public static ModelBlockRenderer modelRenderer() {
        return Minecraft.getInstance().getBlockRenderer().getModelRenderer();
    }

    public static BakedModel getVanillaModel(ItemStack stack, ClientLevel level, LivingEntity entity) {
        ItemModelShaper shaper = getItemRenderer().getItemModelShaper();
        BakedModel model = shaper.getItemModel(stack.getItem());
        if (model != null) {
            BakedModel bakedmodel = model.getOverrides().resolve(model, stack, level, entity, 0);
            if (bakedmodel != null) return bakedmodel;
        }
        return shaper.getModelManager().getMissingModel();
    }

    public static void vanillaRender(ItemStack stack, ItemDisplayContext transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model) {
        IItemRendererProvider.disabled.set(true);
        getItemRenderer().render(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model);
        IItemRendererProvider.disabled.set(false);
    }
}
