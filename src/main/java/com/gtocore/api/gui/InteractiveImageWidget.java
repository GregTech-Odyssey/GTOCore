package com.gtocore.api.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

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
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// 支持编辑器配置
@Configurable(name = "ldlib.gui.editor.register.widget.interactive_image", collapse = false)
public class InteractiveImageWidget extends Widget {

    // ========================== 可配置属性 ==========================
    @Getter
    @Configurable(name = "ldlib.gui.editor.name.border")
    @NumberRange(range = { -100.0, 100.0 })
    private int border;

    @Getter
    @Configurable(name = "ldlib.gui.editor.name.border_color")
    @NumberColor
    private int borderColor = 0xFF000000; // 默认黑色

    // ========================== 文本管理 (服务端驱动) ==========================
    /**
     * 【服务端+单机客户端】文本供应器。
     * 服务端：核心生成逻辑。
     * 单机客户端：临时生成文本（兜底）。
     * 局域网客户端：为 null，不执行。
     */
    protected @Nullable Consumer<List<Component>> textSupplier;

    /**
     * 【服务端+客户端】同步后的文本列表。
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
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            this.currentImage = image;
            this.textureSupplier = () -> image;
            return null;
        });
    }

    public InteractiveImageWidget(int x, int y, int width, int height, Supplier<IGuiTexture> textureSupplier) {
        super(x, y, width, height);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            this.textureSupplier = textureSupplier;
            this.currentImage = textureSupplier.get();
            return null;
        });
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
            if (!isRemote()) {
                writeUpdateInfo(1, this::writeBorderData);
            }
        }
        return this;
    }

    public InteractiveImageWidget textSupplier(@Nullable Consumer<List<Component>> textSupplier) {
        this.textSupplier = textSupplier;
        // 仅服务端初始化文本（客户端不执行）
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

    // ========================== 网络同步 (核心，与 ComponentPanelWidget 一致) ==========================
    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
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
        // 服务端初始化文本（与 ComponentPanelWidget 一致）
        if (textSupplier != null && !isRemote()) {
            lastText.clear();
            textSupplier.accept(lastText);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        // 服务端文本变化同步（与 ComponentPanelWidget 一致）
        if (textSupplier != null && !isRemote()) {
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

        if (currentImage != null) {
            currentImage.draw(graphics, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }

        if (border > 0) {
            DrawerHelper.drawBorder(graphics, pos.x, pos.y, size.width, size.height, borderColor, border);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        // 悬停显示文本（与 ComponentPanelWidget 渲染逻辑一致）
        if (isMouseOver(mouseX, mouseY) && !lastText.isEmpty()) {
            updateScreen();
            graphics.renderComponentTooltip(Minecraft.getInstance().font, lastText, mouseX, mouseY);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver((int) mouseX, (int) mouseY)) {
            String data = "image_click";
            ClickData clickData = new ClickData();

            if (clickHandler != null) {
                clickHandler.accept(data, clickData);
            }

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

    @OnlyIn(Dist.CLIENT)
    public IGuiTexture getImage() {
        return currentImage;
    }
}
