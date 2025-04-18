package com.gto.gtocore.mixin.mc;

import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.Lists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemModelGenerator.class)
public class ItemLayerModelMixin {

    @Shadow
    @Final
    @Mutable
    public static List<String> LAYERS;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void extendLayers(CallbackInfo ci) {
        // 清除原始的列表
        LAYERS.clear();

        // 添加20个层
        List<String> newLayers = Lists.newArrayList();
        for (int i = 0; i < 20; i++) {
            newLayers.add("layer" + i);
        }

        // 将新的层列表添加到原始列表中
        LAYERS.addAll(newLayers);
    }
}
