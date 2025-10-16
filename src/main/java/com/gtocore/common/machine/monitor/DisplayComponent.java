package com.gtocore.common.machine.monitor;

import com.gtocore.api.gui.graphic.impl.GTOLineChartClientComponent;
import com.gtocore.api.gui.graphic.impl.GTOLineChartToolTipComponent;
import com.gtocore.api.gui.graphic.impl.GTOProgressClientComponent;
import com.gtocore.api.gui.graphic.impl.GTOProgressToolTipComponent;
import com.gtocore.api.gui.helper.GuiIn3DHelper;
import com.gtocore.api.gui.helper.ProgressBarColorStyle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.api.client.AEKeyRendering;
import appeng.api.stacks.AmountFormat;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.me.common.StackSizeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public abstract class DisplayComponent implements IDisplayComponent {

    private final ResourceLocation id;

    private DisplayComponent(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Text extends DisplayComponent {

        private FormattedCharSequence text = FormattedCharSequence.EMPTY;

        private Text(ResourceLocation id) {
            super(id);
        }

        @Override
        public Text setInformation(Object... information) {
            if (information.length > 0 && information[0] instanceof FormattedCharSequence text0) {
                this.text = text0;
            } else {
                this.text = FormattedCharSequence.EMPTY; // Default to empty if no valid text provided
            }
            return this;
        }

        @Override
        public FormattedCharSequence getDisplayValue() {
            return text;
        }

        @Override
        public DisplayType getDisplayType() {
            return DisplayType.STYLED_TEXT;
        }

        @Override
        public int getVisualWidth() {
            var font = Minecraft.getInstance().font;
            return font.width(text);
        }

        @Override
        public int getVisualHeight() {
            var font = Minecraft.getInstance().font;
            return font.lineHeight;
        }
    }

    public static Text text(ResourceLocation id) {
        return new Text(id);
    }

    public static Text text(ResourceLocation id, FormattedCharSequence text) {
        return text(id).setInformation(text);
    }

    public static class ProgressBar extends DisplayComponent {

        private final GTOProgressToolTipComponent toolTipComponent;

        private ProgressBar(ResourceLocation id) {
            super(id);
            this.toolTipComponent = new GTOProgressToolTipComponent(0, "", ProgressBarColorStyle.Companion.getDEFAULT_GREEN());
        }

        public ProgressBar setInformation(float progress, String text, ProgressBarColorStyle style, int height) {
            return setInformation(progress, height, text, style);
        }

        @Override
        public ProgressBar setInformation(Object... information) {
            if (information.length >= 2 && information[0] instanceof Float progressValue && information[1] instanceof Integer heightValue) {
                if (information.length >= 4 && information[2] instanceof String textValue && information[3] instanceof ProgressBarColorStyle styleValue) {
                    toolTipComponent.setText(textValue);
                    toolTipComponent.setProgressColorStyle(styleValue);
                }
                toolTipComponent.setPercentage(progressValue);
                toolTipComponent.setHeight(heightValue);
            } else {
                toolTipComponent.setPercentage(0);
            }
            return this;
        }

        @Override
        public int getVisualWidth() {
            return toolTipComponent.getWidth();
        }

        @Override
        public int getVisualHeight() {
            return toolTipComponent.getHeight();
        }

        @Override
        public DisplayType getDisplayType() {
            return DisplayType.CUSTOM_RENDERER;
        }

        @Override
        public void renderDisplay(
                                  Manager.GridNetwork network,
                                  BlockEntity blockEntity,
                                  float partialTicks,
                                  PoseStack stack,
                                  MultiBufferSource buffer,
                                  int combinedLight,
                                  int combinedOverlay,
                                  int lastLineX,
                                  int startY) {
            GuiIn3DHelper.renderIn3D(stack, (gui, pose) -> {
                pose.scale(1f, 1f, 0.01f);
                new GTOProgressClientComponent(toolTipComponent).renderImage(Minecraft.getInstance().font, 0, startY, gui);
            });
        }
    }

    public static ProgressBar progressBar(ResourceLocation id, float progress) {
        return progressBar(id, progress, 15);
    }

    public static ProgressBar progressBar(ResourceLocation id, float progress, int height) {
        return new ProgressBar(id).setInformation(progress, height);
    }

    public static ProgressBar progressBar(ResourceLocation id, float progress, String text) {
        return progressBar(id, progress, text, 15);
    }

    public static ProgressBar progressBar(ResourceLocation id, float progress, String text, int height) {
        return new ProgressBar(id).setInformation(progress, text, ProgressBarColorStyle.Companion.getDEFAULT_GREEN(), height);
    }

    public static ProgressBar progressBar(ResourceLocation id, float progress, String text, ProgressBarColorStyle style) {
        return progressBar(id, progress, text, style, 15);
    }

    public static ProgressBar progressBar(ResourceLocation id, float progress, String text, ProgressBarColorStyle style, int height) {
        return new ProgressBar(id).setInformation(progress, text, style, height);
    }

    public static class LineChart extends DisplayComponent {

        private final GTOLineChartToolTipComponent toolTipComponent;

        private LineChart(ResourceLocation id) {
            super(id);
            // 初始化一个空的图表数据容器
            this.toolTipComponent = new GTOLineChartToolTipComponent(
                    new ArrayList<>(), // 初始数据为空
                    0xFF2ECC71,      // 默认线条颜色
                    true             // 默认填充区域
            );
        }

        @Override
        public LineChart setInformation(Object... information) {
            // 解析传入的数据
            if (information.length > 0 && information[0] instanceof List) {
                try {
                    // 进行类型安全的转换
                    @SuppressWarnings("unchecked")
                    List<BigInteger> dataList = (List<BigInteger>) information[0];
                    toolTipComponent.setData(dataList);
                } catch (ClassCastException e) {
                    // 如果传入的 List 不是 BigInteger 类型，则清空数据以避免错误
                    toolTipComponent.setData(new ArrayList<>());
                }
            } else {
                toolTipComponent.setData(new ArrayList<>());
            }
            // 未来可以扩展，从 information 中解析更多参数，如颜色、高度等
            return this;
        }

        @Override
        public int getVisualWidth() {
            return toolTipComponent.getWidth();
        }

        @Override
        public int getVisualHeight() {
            return toolTipComponent.getHeight();
        }

        @Override
        public DisplayType getDisplayType() {
            return DisplayType.CUSTOM_RENDERER;
        }

        @Override
        public void renderDisplay(
                                  Manager.GridNetwork network,
                                  BlockEntity blockEntity,
                                  float partialTicks,
                                  PoseStack stack,
                                  MultiBufferSource buffer,
                                  int combinedLight,
                                  int combinedOverlay,
                                  int lastLineX,
                                  int startY) {
            // 与 ProgressBar 类似, 创建渲染器并调用其渲染方法
            GuiIn3DHelper.renderIn3D(stack, (gui, pose) -> {
                pose.scale(1f, 1f, 0.01f);
                new GTOLineChartClientComponent(toolTipComponent).renderImage(Minecraft.getInstance().font, 0, startY, gui);
            });
        }
    }

    /**
     * 静态工厂方法，用于方便地创建 LineChart 组件.
     */
    public static LineChart lineChart(ResourceLocation id, List<BigInteger> data) {
        return new LineChart(id).setInformation(data);
    }

    public static class TextWithStack extends Text {

        @Nullable
        private GenericStack genericStack; // Optional text to display on the AEGenericStack

        private TextWithStack(ResourceLocation id) {
            super(id);
        }

        @Override
        public TextWithStack setInformation(Object... information) {
            super.setInformation(information);
            if (information.length > 1 && information[1] instanceof GenericStack stack) {
                this.genericStack = new GenericStack(stack.what(), stack.amount());
            }
            return this;
        }

        @Override
        public void renderDisplay(Manager.GridNetwork network, BlockEntity blockEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, int lastLineX, int startY) {
            if (genericStack != null) {
                GuiIn3DHelper.renderIn3D(
                        stack, (guiGraphics, pose) -> {

                            pose.scale(1f, 1f, 1e-3f);
                            AEKeyRendering.drawInGui(
                                    Minecraft.getInstance(),
                                    guiGraphics,
                                    super.getVisualWidth() + 2,
                                    startY,
                                    genericStack.what());
                            pose.translate(0, 0, 1e-3f);
                            if (genericStack.amount() > 0) {
                                String amtText = genericStack.what().formatAmount(genericStack.amount(), AmountFormat.SLOT);
                                StackSizeRenderer.renderSizeLabel(guiGraphics, Minecraft.getInstance().font,
                                        super.getVisualWidth() + 2,
                                        startY, amtText, false);
                            }
                        });

            }
        }

        @Override
        public DisplayType getDisplayType() {
            return DisplayType.BOTH;
        }

        @Override
        public int getVisualWidth() {
            return super.getVisualWidth() + (genericStack != null ? 18 : 0); // Add space for stack icon if present
        }

        @Override
        public int getVisualHeight() {
            var iconHeight = genericStack != null ? 16 : 0; // Icon height if stack is present
            return Math.max(super.getVisualHeight(), iconHeight);
        }
    }

    public static Text textWithStack(ResourceLocation id, FormattedCharSequence text, @Nullable GenericStack stack) {
        if (stack == null) {
            return text(id, text);
        }
        return new TextWithStack(id)
                .setInformation(text, stack);
    }
}
