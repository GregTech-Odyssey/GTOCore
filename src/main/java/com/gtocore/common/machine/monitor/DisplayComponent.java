package com.gtocore.common.machine.monitor;

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

        private float progress; // 0.0 to 1.0
        private int width;
        private String text = ""; // Optional text to display on the progress bar
        private ProgressBarColorStyle style = ProgressBarColorStyle.Companion.getDEFAULT_GREEN(); // Default style

        private ProgressBar(ResourceLocation id) {
            super(id);
        }

        @Override
        public IDisplayComponent setInformation(Object... information) {
            if (information.length >= 3 && information[0] instanceof Float progressValue && information[1] instanceof Integer widthValue && information[2] instanceof Integer heightValue) {
                this.progress = progressValue;
                this.width = widthValue;
                if (information.length >= 5 && information[3] instanceof String text0 &&
                        information[4] instanceof ProgressBarColorStyle style0) {
                    // Optionally handle text if needed, currently unused
                    this.text = text0;
                    this.style = style0;
                }
            } else {
                this.progress = 0.0f; // Default to 0 if no valid values provided
                this.width = 100; // Default width
            }
            return this;
        }

        @Override
        public int getVisualWidth() {
            return width;
        }

        @Override
        public int getVisualHeight() {
            return 16;
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
            GuiIn3DHelper.renderIn3D(
                    stack,
                    (gui, pose) -> {

                        // guiPose.translate(0, 0, -400);
                        pose.scale(1f, 1f, 1e-3f);
                        new GTOProgressClientComponent(new GTOProgressToolTipComponent(progress, text, style)).renderImage(Minecraft.getInstance().font, 0, startY, gui);
                    });
        }
    }

    public static ProgressBar progressBar(ResourceLocation id, float progress, int width, int height) {
        return (ProgressBar) new ProgressBar(id)
                .setInformation(progress, width, height);
    }

    public static ProgressBar progressBar(ResourceLocation id, float progress, int width, int height, String text, ProgressBarColorStyle style) {
        return (ProgressBar) new ProgressBar(id)
                .setInformation(progress, width, height, text, style);
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
