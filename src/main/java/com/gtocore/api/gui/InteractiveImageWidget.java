package com.gtocore.api.gui;

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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

// 支持编辑器配置
@Configurable(name = "ldlib.gui.editor.register.widget.interactive_image", collapse = false)
public class InteractiveImageWidget extends Widget {

    // ========================== 可配置属性 ==========================
    @Getter
    @Configurable(name = "ldlib.gui.editor.name.border")
    @NumberRange(range = {-100.0, 100.0})
    private int border;

    @Getter
    @Configurable(name = "ldlib.gui.editor.name.border_color")
    @NumberColor
    private int borderColor = 0xFF000000; // 默认黑色

    // ========================== 文本管理 (服务端驱动) ==========================
    /**
     * 【服务端】文本供应器。仅在服务端设置和调用，用于动态生成 Tooltip 文本。
     */
    protected @Nullable Consumer<List<Component>> textSupplier;

    /**
     * 【服务端+客户端】同步后的文本列表。
     * 服务端：存储生成的文本。
     * 客户端：接收服务端同步的文本，并用于渲染。
     */
    @Getter
    protected List<Component> lastText = new ArrayList<>();

    // ========================== 客户端专属属性 ==========================
    @OnlyIn(Dist.CLIENT)
    private Supplier<IGuiTexture> textureSupplier;

    @OnlyIn(Dist.CLIENT)
    private IGuiTexture currentImage;

    // ========================== 点击逻辑 ==========================
    protected BiConsumer<String, ClickData> clickHandler;

    // ========================== 构造方法 ==========================
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

    // ========================== 文本构造工具方法 ==========================
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

    // ========================== 配置方法 ==========================
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
        if (this.border != border || this.borderColor != color) {
            this.border = border;
            this.borderColor = color;
            if (!isRemote()) { // 服务端发起同步
                writeUpdateInfo(1, this::writeBorderData);
            }
        }
        return this;
    }

    /**
     * 【服务端】设置文本供应器。
     * @param textSupplier 用于生成 Tooltip 文本的供应器
     * @return this
     */
    public InteractiveImageWidget textSupplier(@Nullable Consumer<List<Component>> textSupplier) {
        this.textSupplier = textSupplier;
        // 初始化文本（仅在服务端执行）
        if (textSupplier != null && !isRemote()) {
            this.lastText.clear();
            textSupplier.accept(this.lastText);
        }
        return this;
    }

    public InteractiveImageWidget clickHandler(BiConsumer<String, ClickData> clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    // ========================== 网络同步 (核心) ==========================
    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        // 同步边框信息
        writeBorderData(buffer);
        // 同步初始文本
        buffer.writeVarInt(lastText.size());
        for (Component text : lastText) {
            buffer.writeComponent(text);
        }
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        // 读取边框信息
        readBorderData(buffer);
        // 读取初始文本
        lastText.clear();
        int count = buffer.readVarInt();
        for (int i = 0; i < count; i++) {
            lastText.add(buffer.readComponent());
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        // 服务端在初始化时，通过 textSupplier 生成初始文本
        if (textSupplier != null && !isRemote()) {
            lastText.clear();
            textSupplier.accept(lastText);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        // 【服务端】每刻检测文本是否变化，如果变化则同步给客户端
        if (textSupplier != null && !isRemote()) {
            List<Component> newText = new ArrayList<>();
            textSupplier.accept(newText);
            if (!lastText.equals(newText)) {
                lastText = newText;
                // 发送文本更新包 (ID=2)
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
                break;
            default:
                super.readUpdateInfo(id, buffer);
        }
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        if (id == 3) { // 图片点击事件
            ClickData clickData = ClickData.readFromBuf(buffer);
            String componentData = buffer.readUtf();
            if (clickHandler != null) {
                clickHandler.accept(componentData, clickData);
            } else {
                super.handleClientAction(id, buffer);
            }
        }
    }

    private void writeBorderData(FriendlyByteBuf buffer) {
        buffer.writeInt(border);
        buffer.writeInt(borderColor);
    }

    private void readBorderData(FriendlyByteBuf buffer) {
        border = buffer.readInt();
        borderColor = buffer.readInt();
    }

    // ========================== 客户端渲染与交互 ==========================
    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        Position pos = getPosition();
        Size size = getSize();

        // 1. 绘制图片
        if (currentImage != null) {
            currentImage.draw(graphics, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }

        // 2. 绘制边框
        if (border > 0) {
            DrawerHelper.drawBorder(graphics, pos.x, pos.y, size.width, size.height, borderColor, border);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        // 【客户端】鼠标悬停时，使用从服务端同步来的 lastText 渲染 Tooltip
        if (isMouseOver(mouseX, mouseY) && !lastText.isEmpty()) {
            // Minecraft 1.20.1 Forge 正确的方法调用，直接传入 Component 列表
            graphics.renderComponentTooltip(Minecraft.getInstance().font, lastText, mouseX, mouseY);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 只处理图片本身的点击事件
        if (isMouseOver((int) mouseX, (int) mouseY)) {
            String data = "image_click";
            ClickData clickData = new ClickData();

            // 触发客户端点击逻辑（如果有）
            if (clickHandler != null) {
                clickHandler.accept(data, clickData);
            }

            // 向服务端同步点击事件
            writeClientAction(3, buf -> {
                clickData.writeToBuf(buf);
                buf.writeUtf(data);
            });

            playButtonClickSound();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // ========================== 工具方法 ==========================
    @OnlyIn(Dist.CLIENT)
    private boolean isMouseOver(int mouseX, int mouseY) {
        Position absolutePos = getPosition();
        Size size = getSize();
        return mouseX >= absolutePos.x && mouseX <= absolutePos.x + size.width && mouseY >= absolutePos.y && mouseY <= absolutePos.y + size.height;
    }

    /**
     * 判断当前是否为客户端环境。
     * @return true if on client side, false if on server side.
     */
    public boolean isRemote() {
        return getGui() == null || getGui().getModularUIGui() != null;
    }

    @OnlyIn(Dist.CLIENT)
    public IGuiTexture getImage() {
        return currentImage;
    }
}