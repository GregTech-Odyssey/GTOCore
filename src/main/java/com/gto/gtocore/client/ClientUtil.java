package com.gto.gtocore.client;

import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;

@OnlyIn(Dist.CLIENT)
public class ClientUtil {

    public static Minecraft mc() {
        return Minecraft.getInstance();
    }

    public static BakedModel getBakedModel(ResourceLocation resourceLocation) {
        return mc().getModelManager().getModel(resourceLocation);
    }

    public static ItemRenderer getItemRenderer() {
        return mc().getItemRenderer();
    }

    public static BlockRenderDispatcher blockRenderer() {
        return mc().getBlockRenderer();
    }

    public static ModelBlockRenderer modelRenderer() {
        return blockRenderer().getModelRenderer();
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
