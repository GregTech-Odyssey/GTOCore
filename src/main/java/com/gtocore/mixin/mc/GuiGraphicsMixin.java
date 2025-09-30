package com.gtocore.mixin.mc;

import com.gtocore.api.placeholder.IPlaceholderEXKT;
import com.gtocore.api.placeholder.ItemStackFluidStack;
import com.gtocore.common.item.DiscItem;
import dev.emi.emi.api.stack.FluidEmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

/**
 * @author REF
 */
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    /**
     * 递归调用守卫。存储正在处理的 *源* 占位符物品堆，以防止无限循环。
     */
    @Unique
    private static final ThreadLocal<ItemStack> gtocore$RENDERING_GUARD = new ThreadLocal<>();

    // --- Shadow 方法 ---
    @Shadow
    protected abstract void renderItem(@Nullable LivingEntity entity, @Nullable Level level, ItemStack stack, int x, int y, int seed, int GuiOffset);

    @Shadow
    public abstract void renderItemDecorations(Font font, ItemStack stack, int x, int y, @Nullable String text);

    /**
     * 获取替换目标（物品或流体）的核心逻辑方法。
     *
     * @param placeholderStack 正在被渲染的物品。
     * @return 如果应该发生替换，则返回一个 ItemStackFluidStack；否则返回 null。
     */
    @Unique
    @Nullable
    private ItemStackFluidStack gtocore$getPlaceholderTarget(@Nullable LivingEntity entity, @Nullable Level level, ItemStack placeholderStack, int x, int y, int seed, int GuiOffset) {
        // 1. 检查物品是否为占位符，并且不在递归守卫中。
        if (!(placeholderStack.getItem() instanceof IPlaceholderEXKT) || gtocore$RENDERING_GUARD.get() == placeholderStack) {
            return null;
        }
        if (Screen.hasShiftDown()) {
            if ((placeholderStack.getItem() instanceof DiscItem discitem)) {
                ItemStackFluidStack target = discitem.getCurrentTarget(placeholderStack, null);

                // 4. 验证目标以确保其不为空。
                if (target != null) {
                    if (target instanceof ItemStackFluidStack.Item itemTarget && !itemTarget.getStack().isEmpty()) {
                        return itemTarget;
                    }
                    if (target instanceof ItemStackFluidStack.Fluid fluidTarget && !fluidTarget.getStack().isEmpty()) {
                        return fluidTarget;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 注入点 1：渲染主模型/图标。
     * 这是我们决定渲染物品模型还是流体贴图的地方。
     */
    @Inject(
            method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderGuiItem(
            @Nullable LivingEntity entity, @Nullable Level level, ItemStack stack,
            int x, int y, int seed, int GuiOffset,
            CallbackInfo ci) {

        ItemStackFluidStack target = this.gtocore$getPlaceholderTarget(entity, level, stack, x, y, seed, GuiOffset);

        if (target != null) {
            // 用 *源* 物品堆设置守卫，以防止递归。
            gtocore$RENDERING_GUARD.set(stack);
            try {
                // 我们现在在受保护的代码块中。
                if (target instanceof ItemStackFluidStack.Item itemTarget) {
                    // 如果是 Item，使用原始方法渲染它。
                    this.renderItem(entity, level, itemTarget.getStack(), x, y, seed, GuiOffset);
                } else if (target instanceof ItemStackFluidStack.Fluid fluidTarget) {
                    FluidEmiStack emiStack = new FluidEmiStack(fluidTarget.getStack().getFluid());
                    emiStack.render((GuiGraphics) (Object) this, x, y, Minecraft.getInstance().getFrameTime());
                }
            } finally {
                // 始终移除守卫。
                gtocore$RENDERING_GUARD.remove();
            }
            // 取消占位符物品模型的原始渲染。
            ci.cancel();
        }
    }

    /**
     * 注入点 2：渲染悬浮装饰（耐久度、数量等）。
     */
    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderGuiItemDecorations(Font font, ItemStack stack, int x, int y, @Nullable String text, CallbackInfo ci) {
        Level level = Minecraft.getInstance().level;
        // 这里我们只需要粗略的参数来触发逻辑。
        ItemStackFluidStack target = this.gtocore$getPlaceholderTarget(null, level, stack, x, y, 0, 0);

        if (target != null) {
            if (target instanceof ItemStackFluidStack.Item itemTarget) {
                // 如果目标是物品，则渲染其装饰。
                this.renderItemDecorations(font, itemTarget.getStack(), x, y, text);
            }
            // 如果目标是流体，我们什么都不做，但仍然取消原始调用。
            // 这可以防止占位符的装饰被渲染在流体之上。
            ci.cancel();
        }
    }
}
