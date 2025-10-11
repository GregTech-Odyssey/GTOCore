package com.gtocore.mixin.eae;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import com.glodblock.github.extendedae.util.MessageUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MessageUtil.class, remap = false)
public abstract class MessageUtilMixin {

    /**
     * @param dimensionId     原始 Component.literal 的参数，我们不直接使用它，但必须在签名中捕获。
     * @param player          原始 createEnhancedHighlightMessage 方法的参数，用于传递上下文。
     * @param targetPos       原始 createEnhancedHighlightMessage 方法的参数。
     * @param targetDimension 原始 createEnhancedHighlightMessage 方法的核心参数，我们需要它来构建翻译键。
     * @param translatable    原始 createEnhancedHighlightMessage 方法的参数。
     * @return 返回一个新的、可翻译的组件，它将替换掉原本的 Component.literal() 的结果。
     * @author YourName
     * @reason 使用我们自己的可翻译组件来替换原版硬编码的维度ID显示。
     *         <p>
     *         这个 Mixin 使用 @Redirect 来精确地拦截对 Component.literal(dimensionId) 的调用。
     *         当 MessageUtil.createEnhancedHighlightMessage 方法尝试创建维度的字面量组件时，
     *         这个方法会被调用，并用我们返回的自定义翻译组件来替代原有的结果。
     */
    @Redirect(
              method = "createEnhancedHighlightMessage(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceKey;Ljava/lang/String;)Lnet/minecraft/network/chat/Component;",
              at = @At(
                       value = "INVOKE",
                       target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
                       ordinal = 1),
              remap = false)
    private static MutableComponent replaceDimensionComponent(String dimensionId, Player player, BlockPos targetPos, ResourceKey<Level> targetDimension, String translatable) {
        return Component.translatable("gtocore.dimension." + targetDimension.location().getPath());
    }
}
