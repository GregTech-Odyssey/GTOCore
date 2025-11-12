package com.gtocore.api.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// 支持编辑器配置
@Configurable(name = "ldlib.gui.editor.register.widget.interactive_image", collapse = false)
public class InteractiveImageWidget extends Widget {

    // -------------------------- 可配置属性 --------------------------
    @Getter
    @Configurable(name = "ldlib.gui.editor.name.border")
    @NumberRange(range = { -100.0, 100.0 })
    private int border;

    @Getter
    @Configurable(name = "ldlib.gui.editor.name.border_color")
    @NumberColor
    private int borderColor = 0xFF000000; // 默认黑色

    // -------------------------- 文本管理（完全仿照ComponentPanelWidget） --------------------------
    protected @Nullable Consumer<List<Component>> textSupplier; // 动态提供悬停文本
    @Getter
    protected List<Component> lastText = new ArrayList<>(); // 当前悬停文本列表
    protected List<FormattedCharSequence> cacheLines = Collections.emptyList(); // 格式化后的文本行（客户端用）
    protected int hoverTextSpace = 2; // 文本行间距

    // -------------------------- 客户端专属属性 --------------------------
    @OnlyIn(Dist.CLIENT)
    private Supplier<IGuiTexture> textureSupplier;

    @OnlyIn(Dist.CLIENT)
    private IGuiTexture currentImage;

    // -------------------------- 点击逻辑（仿照ComponentPanelWidget） --------------------------
    protected BiConsumer<String, ClickData> clickHandler; // 统一点击处理器

    // -------------------------- 构造方法 --------------------------
    public InteractiveImageWidget() {
        this(0, 0, 50, 50, new ResourceTexture());
    }

    public InteractiveImageWidget(int x, int y, int width, int height, IGuiTexture image) {
        super(x, y, width, height);
        if (isRemote()) {
            this.currentImage = image;
            this.textureSupplier = () -> image;
        }
    }

    public InteractiveImageWidget(int x, int y, int width, int height, Supplier<IGuiTexture> textureSupplier) {
        super(x, y, width, height);
        if (isRemote()) {
            this.textureSupplier = textureSupplier;
            this.currentImage = textureSupplier.get();
        }
    }

    // -------------------------- 文本构造工具方法（完全仿照ComponentPanelWidget） --------------------------
    public static Component withButton(Component textComponent, String componentData) {
        Style style = textComponent.getStyle();
        style = style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "@!" + componentData));
        style = style.withColor(ChatFormatting.YELLOW);
        return textComponent.copy().withStyle(style);
    }

    public static Component withButton(Component textComponent, String componentData, int color) {
        Style style = textComponent.getStyle();
        style = style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "@!" + componentData));
        style = style.withColor(color);
        return textComponent.copy().withStyle(style);
    }

    public static Component withHoverText(Component textComponent, Component hover) {
        Style style = textComponent.getStyle();
        style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover));
        return textComponent.copy().withStyle(style);
    }

    // -------------------------- 配置方法 --------------------------
    @OnlyIn(Dist.CLIENT)
    public InteractiveImageWidget setImage(IGuiTexture image) {
        this.currentImage = image;
        this.textureSupplier = () -> image;
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public InteractiveImageWidget setImageSupplier(Supplier<IGuiTexture> supplier) {
        this.textureSupplier = supplier;
        this.currentImage = supplier.get();
        return this;
    }

    public InteractiveImageWidget setBorder(int border, int color) {
        this.border = border;
        this.borderColor = color;
        if (!isRemote()) {
            writeUpdateInfo(1, this::writeBorderData);
        }
        return this;
    }

    // 设置悬停文本供应器（仿照ComponentPanelWidget的textSupplier）
    public InteractiveImageWidget textSupplier(@Nullable Consumer<List<Component>> textSupplier) {
        this.textSupplier = textSupplier;
        if (textSupplier != null) {
            this.lastText.clear();
            textSupplier.accept(this.lastText);
            if (isRemote()) {
                formatHoverText(); // 客户端立即更新格式化文本
            }
        }
        return this;
    }

    // 设置点击处理器（仿照ComponentPanelWidget的clickHandler）
    public InteractiveImageWidget clickHandler(BiConsumer<String, ClickData> clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    // 设置悬停文本行间距
    public InteractiveImageWidget setHoverTextSpace(int space) {
        this.hoverTextSpace = space;
        if (isRemote()) {
            formatHoverText();
        }
        return this;
    }

    // -------------------------- 网络同步（仿照ComponentPanelWidget） --------------------------
    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        writeBorderData(buffer);
        // 同步悬停文本
        buffer.writeVarInt(lastText.size());
        for (Component text : lastText) {
            buffer.writeComponent(text);
        }
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        readBorderData(buffer);
        // 读取悬停文本
        lastText.clear();
        int count = buffer.readVarInt();
        for (int i = 0; i < count; i++) {
            lastText.add(buffer.readComponent());
        }
        if (isRemote()) {
            formatHoverText();
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        // 初始化时通过textSupplier获取文本（仿照ComponentPanelWidget）
        if (textSupplier != null) {
            lastText.clear();
            textSupplier.accept(lastText);
        }
        if (isRemote()) {
            formatHoverText(); // 客户端格式化文本
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        // 客户端定期更新文本（仿照ComponentPanelWidget）
        if (isRemote() && textSupplier != null) {
            List<Component> newText = new ArrayList<>();
            textSupplier.accept(newText);
            if (!lastText.equals(newText)) {
                lastText = newText;
                formatHoverText();
            }
        }
        // 更新图片
        if (isRemote() && textureSupplier != null) {
            currentImage = textureSupplier.get();
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        // 服务端检测文本变化并同步（仿照ComponentPanelWidget）
        if (textSupplier != null) {
            List<Component> newText = new ArrayList<>();
            textSupplier.accept(newText);
            if (!lastText.equals(newText)) {
                lastText = newText;
                writeUpdateInfo(2, buf -> {
                    buf.writeVarInt(lastText.size());
                    for (Component text : lastText) {
                        buf.writeComponent(text);
                    }
                });
            }
        }
    }

    @Override
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        switch (id) {
            case 1: // 边框更新
                readBorderData(buffer);
                break;
            case 2: // 文本更新
                lastText.clear();
                int count = buffer.readVarInt();
                for (int i = 0; i < count; i++) {
                    lastText.add(buffer.readComponent());
                }
                if (isRemote()) {
                    formatHoverText();
                }
                break;
            default:
                super.readUpdateInfo(id, buffer);
        }
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        if (id == 3) {
            ClickData clickData = ClickData.readFromBuf(buffer);
            String componentData = buffer.readUtf();
            if (clickHandler != null) {
                clickHandler.accept(componentData, clickData);
            } else {
                super.handleClientAction(id, buffer);
            }
        }
    }

    // 边框数据同步辅助方法
    private void writeBorderData(FriendlyByteBuf buffer) {
        buffer.writeInt(border);
        buffer.writeInt(borderColor);
    }

    private void readBorderData(FriendlyByteBuf buffer) {
        border = buffer.readInt();
        borderColor = buffer.readInt();
    }

    // -------------------------- 客户端文本格式化与渲染（仿照ComponentPanelWidget） --------------------------
    @OnlyIn(Dist.CLIENT)
    protected void formatHoverText() {
        Font font = Minecraft.getInstance().font;
        // 不限制宽度，直接按原文本换行
        cacheLines = lastText.stream()
                .flatMap(component -> ComponentRenderUtils.wrapComponents(component, Integer.MAX_VALUE, font).stream())
                .toList();
    }

    @OnlyIn(Dist.CLIENT)
    protected @Nullable Style getHoverTextStyleUnderMouse(double mouseX, double mouseY) {
        // 仅当鼠标在图片上时，检测悬停文本的样式
        if (!isMouseOver((int) mouseX, (int) mouseY)) {
            return null;
        }
        // 计算悬停提示的位置（图片上方/下方）
        Position pos = getPosition();
        // 简化处理：假设提示框左上角为图片上方10px
        int tooltipX = pos.x;
        int tooltipY = pos.y - 10 - cacheLines.size() * (9 + hoverTextSpace);
        // 检测鼠标是否在提示框范围内
        Font font = Minecraft.getInstance().font;
        double mouseYInTooltip = mouseY - tooltipY;
        double selectedLine = mouseYInTooltip / (9 + hoverTextSpace);
        if (selectedLine >= 0 && selectedLine < cacheLines.size()) {
            FormattedCharSequence line = cacheLines.get((int) selectedLine);
            int lineWidth = font.width(line);
            if (mouseX >= tooltipX && mouseX <= tooltipX + lineWidth) {
                int mouseOffset = (int) (mouseX - tooltipX);
                return font.getSplitter().componentStyleAtWidth(line, mouseOffset);
            }
        }
        return null;
    }

    // -------------------------- 客户端渲染与交互 --------------------------
    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        Position pos = getPosition();
        Size size = getSize();

        // 绘制图片
        if (currentImage != null) {
            currentImage.draw(graphics, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }

        // 绘制边框
        if (border > 0) {
            DrawerHelper.drawBorder(graphics, pos.x, pos.y, size.width, size.height, borderColor, border);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver((int) mouseX, (int) mouseY)) {
            String data = "image_click";
            ClickData clickData = new ClickData();

            // 空值检查 + 日志提示
            if (clickHandler != null) {
                clickHandler.accept(data, clickData); // 触发客户端逻辑
            }

            // 强制同步（即使处理器为null，便于排查）
            writeClientAction(3, buf -> {
                clickData.writeToBuf(buf);
                buf.writeUtf(data);
            });

            playButtonClickSound();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // 移除悬停文本的样式检测（不再处理文本内交互）
    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        // 仅显示悬停文本，不处理文本内的嵌套交互
        if (isMouseOver(mouseX, mouseY) && !cacheLines.isEmpty()) {
            graphics.renderComponentTooltip(
                    Minecraft.getInstance().font,
                    lastText,
                    mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isMouseOver(int mouseX, int mouseY) {
        // 获取组件在屏幕上的绝对坐标（相对坐标 + 父容器偏移）
        Position absolutePos = getPosition();
        Size size = getSize();
        return mouseX >= absolutePos.x && mouseX <= absolutePos.x + size.width && mouseY >= absolutePos.y && mouseY <= absolutePos.y + size.height;
    }

    // -------------------------- 工具方法 --------------------------
    public boolean isRemote() {
        return getGui() == null || getGui().getModularUIGui() != null;
    }

    @OnlyIn(Dist.CLIENT)
    public IGuiTexture getImage() {
        return currentImage;
    }
}
