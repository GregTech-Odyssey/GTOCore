package com.gtocore.api.gui;

import com.gtolib.GTOCore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import lombok.Getter;

import javax.annotation.Nonnull;

@LDLRegister(name = "stack_texture", group = "texture")
public class StackTexture extends TransformTexture {

    // ===================== 可配置字段 =====================
    @Getter
    @Configurable(name = "ldlib.gui.editor.name.items")
    public ItemStack[] itemStacks = new ItemStack[0];

    @Getter
    @Configurable(name = "ldlib.gui.editor.name.fluids")
    public FluidStack[] fluidStacks = new FluidStack[0];

    // ===================== Getter 方法 =====================
    @Getter
    @Configurable(name = "ldlib.gui.editor.name.color")
    private int color = 0xFFFFFFFF; // 默认白色（无叠加效果）

    // ===================== 常量配置 =====================
    private static final int COUNT_OFFSET = 2; // 数量文本向内偏移像素数

    // ===================== 动画相关字段 =====================
    private int index = 0; // 当前动画帧索引
    private int ticks = 0; // 动画计时器（单位：游戏刻）
    private long lastTick = -1; // 上一次更新的游戏刻（防止同一帧多次更新）

    // ===================== 构造函数 =====================
    public StackTexture() {
        this(new ItemStack(Items.APPLE)); // 默认苹果物品
    }

    /**
     * 用物品栈创建纹理（支持单个/多个物品动画）
     */
    public StackTexture(@Nonnull ItemStack... itemStacks) {
        this.itemStacks = itemStacks;
        this.fluidStacks = new FluidStack[0];
        resetAnimation();
    }

    /**
     * 用物品创建纹理（自动转为物品栈）
     */
    public StackTexture(@Nonnull Item... items) {
        this.itemStacks = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            this.itemStacks[i] = new ItemStack(items[i]);
        }
        this.fluidStacks = new FluidStack[0];
        resetAnimation();
    }

    /**
     * 用流体栈创建纹理（支持单个/多个流体动画）
     */
    public StackTexture(@Nonnull FluidStack... fluidStacks) {
        this.fluidStacks = fluidStacks;
        this.itemStacks = new ItemStack[0];
        resetAnimation();
    }

    /**
     * 通过 Object 创建纹理（自动识别类型）
     */
    public StackTexture(Object stackObject) {
        setStack(stackObject);
    }

    // ===================== Setter 方法（链式调用） =====================
    /**
     * 设置物品栈（切换为物品模式，重置动画）
     */
    public StackTexture setItems(@Nonnull ItemStack... itemStacks) {
        this.itemStacks = itemStacks;
        this.fluidStacks = new FluidStack[0];
        resetAnimation();
        return this;
    }

    /**
     * 设置流体栈（切换为流体模式，重置动画）
     */
    public StackTexture setFluids(@Nonnull FluidStack... fluidStacks) {
        this.fluidStacks = fluidStacks;
        this.itemStacks = new ItemStack[0];
        resetAnimation();
        return this;
    }

    /**
     * 通过 Object 设置栈（自动识别类型，支持链式调用）
     */
    public StackTexture setStack(Object stackObject) {
        switch (stackObject) {
            case null -> {
                this.itemStacks = new ItemStack[0];
                this.fluidStacks = new FluidStack[0];
                resetAnimation();
                return this;
            }

            case Item item -> setItems(new ItemStack(item));
            case ItemStack itemStack -> setItems(itemStack);
            case FluidStack fluidStack -> setFluids(fluidStack);
            case ItemStack[] itemStacks -> setItems(itemStacks);
            case FluidStack[] fluidStacks -> setFluids(fluidStacks);
            default -> {
                this.itemStacks = new ItemStack[0];
                this.fluidStacks = new FluidStack[0];
                resetAnimation();
                GTOCore.LOGGER.error("Unsupported stack type: {}\nSupported types: Item, ItemStack, FluidStack, ItemStack[], FluidStack[]", stackObject.getClass().getName());
                return this;
            }
        }

        return this;
    }

    /**
     * 设置叠加颜色（支持链式调用）
     */
    @Override
    public StackTexture setColor(int color) {
        this.color = color;
        return this;
    }

    // ===================== 动画更新逻辑 =====================
    @OnlyIn(Dist.CLIENT)
    @Override
    public void updateTick() {
        // 防止同一游戏刻多次更新（避免动画加速）
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            long currentTick = mc.level.getGameTime();
            if (currentTick == lastTick) return;
            lastTick = currentTick;
        }

        // 计算总帧数（取物品/流体数组长度的最大值）
        int totalFrames = Math.max(itemStacks.length, fluidStacks.length);
        if (totalFrames <= 1) return; // 单帧无需动画

        // 每 20 游戏刻（1 秒）切换一帧
        if (++ticks % 20 == 0) {
            index = (index + 1) % totalFrames;
        }
    }

    /**
     * 重置动画状态（切换物品/流体时调用）
     */
    private void resetAnimation() {
        index = 0;
        ticks = 0;
        lastTick = -1;
    }

    // ===================== 核心绘制逻辑（复用 ItemStackTexture 对齐逻辑） =====================
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void drawInternal(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
        updateTick(); // 绘制前更新动画帧

        Object currentStack = getCurrentStack();
        if (currentStack == null) return;

        graphics.pose().pushPose();
        // 1. 缩放：16x16 标准尺寸 → 目标宽高
        float scaleX = (float) width / 16.0F;
        float scaleY = (float) height / 16.0F;
        graphics.pose().scale(scaleX, scaleY, 1.0F);

        // 2. 平移补偿：将实际起始坐标（x,y）转换为缩放后的相对坐标，抵消偏移
        graphics.pose().translate(
                x * 16.0F / (float) width,
                y * 16.0F / (float) height,
                0.0F);

        // 3. 绘制物品/流体：直接在缩放后坐标系的 (0,0) 绘制，精准对齐无偏移
        if (currentStack instanceof ItemStack itemStack) {
            // 物品绘制：Z轴偏移 10，确保在背景之上
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 10.0F);
            DrawerHelper.drawItemStack(graphics, itemStack, 0, 0, color, null);
            graphics.pose().popPose();
        } else if (currentStack instanceof FluidStack fluidStack) {
            // 流体绘制：Z轴偏移 10，光泽层 11
            graphics.pose().pushPose();
            drawFluidStack(graphics, fluidStack, 0, 0); // 直接传 0,0
            graphics.pose().popPose();
        }

        // 绘制数量（单独处理，避免被缩放影响文本大小）
        graphics.pose().popPose(); // 恢复矩阵以正常绘制文本
        drawCount(graphics, currentStack, x, y, width, height);
    }

    /**
     * 获取当前动画帧的物品/流体栈
     */
    private Object getCurrentStack() {
        if (itemStacks.length > 0) {
            // 防止索引越界（当动画帧数 > 物品数组长度时）
            return itemStacks[Math.min(index, itemStacks.length - 1)];
        } else if (fluidStacks.length > 0) {
            return fluidStacks[Math.min(index, fluidStacks.length - 1)];
        }
        return null; // 无物品/流体时返回 null
    }

    // ===================== 流体绘制逻辑（对齐优化） =====================
    @OnlyIn(Dist.CLIENT)
    private void drawFluidStack(GuiGraphics graphics, FluidStack fluidStack, int drawX, int drawY) {
        if (fluidStack.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        FluidType fluidType = fluidStack.getFluid().getFluidType();
        IClientFluidTypeExtensions renderExt = IClientFluidTypeExtensions.of(fluidType);
        if (renderExt == null) {
            drawMissingTexture(graphics, drawX, drawY);
            return;
        }

        // 1. 获取静态纹理 Sprite（确保纹理完整）
        ResourceLocation stillTex = renderExt.getStillTexture(fluidStack);
        if (stillTex == null) {
            drawMissingTexture(graphics, drawX, drawY);
            return;
        }
        TextureAtlasSprite stillSprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(stillTex);
        if (stillSprite == null) {
            drawMissingTexture(graphics, drawX, drawY);
            return;
        }

        // 2. 计算最终颜色（流体颜色 + 叠加颜色）
        int fluidColor = renderExt.getTintColor(fluidStack);
        int finalColor = combineColors(fluidColor, this.color);
        float r = ((finalColor >> 16) & 0xFF) / 255.0F;
        float g = ((finalColor >> 8) & 0xFF) / 255.0F;
        float b = (finalColor & 0xFF) / 255.0F;
        float a = ((finalColor >> 24) & 0xFF) / 255.0F;

        // 3. 流体绘制：Z轴偏移 10，满尺寸填充
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 10.0F);
        graphics.blit(
                drawX, drawY,
                0,
                16, 16,
                stillSprite,
                r, g, b, a);
        graphics.pose().popPose();

        // 4. 绘制光泽层：Z轴偏移 11，在流体之上
        if (fluidType.getLightLevel() > 0) {
            ResourceLocation flowingTex = renderExt.getFlowingTexture(fluidStack);
            if (flowingTex != null) {
                TextureAtlasSprite flowingSprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(flowingTex);
                if (flowingSprite != null) {
                    graphics.pose().pushPose();
                    graphics.pose().translate(0.0F, 0.0F, 11.0F);
                    graphics.blit(
                            drawX, drawY,
                            0,
                            16, 16,
                            flowingSprite,
                            1.0F, 1.0F, 1.0F, 0.4F);
                    graphics.pose().popPose();
                }
            }
        }
    }

    // ===================== 辅助方法：绘制缺失纹理 =====================
    @OnlyIn(Dist.CLIENT)
    private void drawMissingTexture(GuiGraphics graphics, int drawX, int drawY) {
        Minecraft mc = Minecraft.getInstance();
        TextureAtlasSprite missingSprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(
                net.minecraft.client.renderer.texture.TextureManager.INTENTIONAL_MISSING_TEXTURE);
        // Z轴偏移 0，避免遮挡其他元素
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 0.0F);
        graphics.blit(
                drawX, drawY,
                0,
                16, 16,
                missingSprite,
                1.0F, 1.0F, 1.0F, 1.0F);
        graphics.pose().popPose();
    }

    // ===================== 辅助方法：颜色混合 =====================
    private int combineColors(int fluidColor, int overlayColor) {
        // 补全流体颜色的 Alpha 通道（0xRRGGBB → 0xFFRRGGBB）
        if ((fluidColor & 0xFF000000) == 0) {
            fluidColor |= 0xFF000000;
        }

        // 叠加颜色为白色时，直接返回流体颜色（无叠加效果）
        if (overlayColor == 0xFFFFFFFF) {
            return fluidColor;
        }

        // 分解 RGBA 分量
        float a1 = ((fluidColor >> 24) & 0xFF) / 255.0F;
        float r1 = ((fluidColor >> 16) & 0xFF) / 255.0F;
        float g1 = ((fluidColor >> 8) & 0xFF) / 255.0F;
        float b1 = (fluidColor & 0xFF) / 255.0F;

        float a2 = ((overlayColor >> 24) & 0xFF) / 255.0F;
        float r2 = ((overlayColor >> 16) & 0xFF) / 255.0F;
        float g2 = ((overlayColor >> 8) & 0xFF) / 255.0F;
        float b2 = (overlayColor & 0xFF) / 255.0F;

        // 混合模式：透明度相乘 + 颜色分量相乘
        float a = a1 * a2;
        float r = r1 * r2;
        float g = g1 * g2;
        float b = b1 * b2;

        // 转换回 0xAABBGGRR 格式
        return ((int) (a * 255) << 24) |
                ((int) (r * 255) << 16) |
                ((int) (g * 255) << 8) |
                (int) (b * 255);
    }

    // ===================== 数量绘制逻辑 =====================
    @OnlyIn(Dist.CLIENT)
    private void drawCount(GuiGraphics graphics, Object stack, float x, float y, int width, int height) {
        String countText = getCountText(stack);
        if (countText.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        // 文本缩放（保持原逻辑，确保文本大小适配图标）
        float textScale = Math.min(width, height) / 16.0F * 0.5F;
        textScale = Math.clamp(textScale, 0.4F, 0.6F);

        graphics.pose().pushPose();
        // 向内偏移 COUNT_OFFSET 像素（默认 2 像素），坐标计算精准
        float textWidth = mc.font.width(countText) * textScale;
        float textHeight = mc.font.lineHeight * textScale;
        // 右下角定位 + 向内偏移（X 轴向左偏移，Y 轴向上偏移）
        float textX = x + width - textWidth - COUNT_OFFSET;
        float textY = y + height - textHeight - COUNT_OFFSET;

        graphics.pose().translate(textX, textY, 200.0F);
        graphics.pose().scale(textScale, textScale, 1.0F);

        // 绘制文本阴影（增强可读性）
        graphics.drawString(mc.font, countText, 1, 1, 0x000000, false);
        // 绘制文本主体（白色清晰可见）
        graphics.drawString(mc.font, countText, 0, 0, 0xFFFFFF, false);

        graphics.pose().popPose();
    }

    /**
     * 获取物品/流体的数量文本
     */
    private String getCountText(Object stack) {
        long count;
        if (stack instanceof ItemStack itemStack) {
            return "";
            // 不知道为什么物品堆原本就会渲染一个数量，所以这里直接返回空字符串
            // if (itemStack.getCount() <= 1) return "";
            // count = itemStack.getCount();
        } else if (stack instanceof FluidStack fluidStack) {
            if (fluidStack.getAmount() <= 0) return "";
            count = fluidStack.getAmount();
        } else {
            return "";
        }

        String[] units = { "", "K", "M", "B", "T", "Q", "X", "Z" };
        int unitIndex = 0;
        long currentCount = count;

        while (currentCount >= 10000 && unitIndex < units.length - 1) {
            currentCount = currentCount / 1000;
            unitIndex++;
        }

        return currentCount + units[unitIndex];
    }
}
