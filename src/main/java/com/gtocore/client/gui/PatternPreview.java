package com.gtocore.client.gui;

import com.gtocore.integration.emi.multipage.MultiblockInfoEmiRecipe;

import com.gtolib.api.annotation.NewDataAttributes;
import com.gtolib.api.gui.PatternSlotWidget;
import com.gtolib.api.gui.SelectedSlotWidget;
import com.gtolib.api.item.ItemStackHandler;
import com.gtolib.api.machine.MultiblockDefinition;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.integration.xei.handlers.item.CycleItemStackHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.ObjectArrays;
import com.gto.fastcollection.O2OOpenCacheHashMap;
import com.gto.registrate.ICustomfCategoryFill;
import com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer;
import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.lowdragmc.lowdraglib.client.utils.RenderUtils;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.lowdragmc.lowdraglib.utils.BlockPosFace;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.emi.emi.screen.RecipeScreen;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;
import java.util.stream.LongStream;

@OnlyIn(Dist.CLIENT)
public final class PatternPreview extends WidgetGroup {

    private static final int COMPACT_WIDTH = 160;
    private static final int COMPACT_HEIGHT = 160;
    private static final int PARTS_SCROLL_BAR_HEIGHT = 4;

    private static boolean isPartHighlighting = false;

    private final MultiblockInfoEmiRecipe recipe;
    private final boolean fullscreen;
    private int partsY;
    private boolean isLoaded;
    private static TrackedDummyWorld LEVEL;
    private static final Map<MultiblockMachineDefinition, MBPattern[]> CACHE = new Reference2ReferenceOpenHashMap<>();
    private final SceneWidget sceneWidget;
    private final DraggableScrollableWidgetGroup scrollableWidgetGroup;
    private final ImageWidget titleWidget;
    private final TextTexture titleTexture;
    private ImageWidget structureSizeWidget;
    private TextTexture structureSizeTexture;
    private ImageWidget controlsHintWidget;
    private TextTexture controlsHintTexture;
    private final ButtonWidget patternButton;
    private final ButtonWidget layerButton;
    private final ButtonWidget highlightButton;
    private final ButtonWidget modulesButton;
    private final ButtonWidget fullscreenToggleButton;
    private final MBPattern[] patterns;
    private final boolean moduleOverlayAvailable;
    private final List<SimplePredicate> predicates = new ArrayList<>();
    private int index;
    private int layer;
    private boolean showAllModules;
    @Nullable
    private IMultiController overlayController;
    private final O2OOpenCacheHashMap<BlockPos, OverlayOriginalBlock> overlayOriginalBlocks = new O2OOpenCacheHashMap<>();
    private PatternSlotWidget[] slotWidgets;
    private SlotWidget[] candidates;

    private PatternPreview(MultiblockInfoEmiRecipe recipe, MultiblockMachineDefinition controllerDefinition) {
        this(recipe, controllerDefinition, COMPACT_WIDTH, COMPACT_HEIGHT, false, null);
    }

    private PatternPreview(MultiblockInfoEmiRecipe recipe, MultiblockMachineDefinition controllerDefinition,
                           int width, int height, boolean fullscreen, Runnable closeAction) {
        super(0, 0, width, height);
        this.recipe = recipe;
        this.fullscreen = fullscreen;
        setClientSideWidget();
        layer = -1;
        int sceneX = fullscreen ? 8 : 3;
        int sceneY = fullscreen ? 22 : 3;
        int controlsX = fullscreen ? width - 28 : 138;
        int sceneWidth = fullscreen ? width - 44 : 150;
        int sceneHeight = fullscreen ? height - 82 : 150;
        partsY = fullscreen ? height - 27 : 132;

        addWidget(sceneWidget = new MySceneWidget(sceneX, sceneY, sceneWidth, sceneHeight)
                .setOnSelected(this::onPosSelected)
                .setRenderFacing(false));
        scrollableWidgetGroup = new DraggableScrollableWidgetGroup(
                fullscreen ? 8 : 3,
                partsY,
                fullscreen ? width - 16 : 154,
                22)
                .setXScrollBarHeight(PARTS_SCROLL_BAR_HEIGHT)
                .setXBarStyle(GuiTextures.SLIDER_BACKGROUND, GuiTextures.BUTTON)
                .setScrollable(true)
                .setDraggable(true)
                .setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.HORIZONTAL);
        scrollableWidgetGroup.setScrollYOffset(0);
        addWidget(scrollableWidgetGroup);
        if (ConfigHolder.INSTANCE.client.useVBO) {
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.recordRenderCall(sceneWidget::useCacheBuffer);
            } else {
                sceneWidget.useCacheBuffer();
            }
        }
        titleTexture = new TextTexture(controllerDefinition.getDescriptionId(), -1)
                .setType(TextTexture.TextType.ROLL)
                .setWidth(fullscreen ? width - 48 : 132)
                .setDropShadow(true);
        addWidget(titleWidget = new ImageWidget(
                fullscreen ? 8 : 3,
                fullscreen ? 6 : 3,
                fullscreen ? width - 44 : 132,
                10,
                titleTexture));
        var hasModule = controllerDefinition.getSubPatternFactory() != null && controllerDefinition.getSubPatternFactory().length > 0 && hasModuleTooltip(controllerDefinition);;
        if (CACHE.containsKey(controllerDefinition)) {
            patterns = CACHE.get(controllerDefinition);
        } else {
            MultiblockDefinition definition = MultiblockDefinition.of(controllerDefinition);
            MultiblockDefinition.Pattern[] pattern = definition.getPatterns();
            patterns = new MBPattern[pattern.length];
            for (int i = 0; i < pattern.length; i++) {
                patterns[i] = initializePattern(definition, pattern[i], i, hasModule);
            }
            CACHE.put(controllerDefinition, patterns);
            definition.clear();
        }
        moduleOverlayAvailable = fullscreen && patterns.length > 1 && hasModule;
        index = Math.clamp(recipe.i, 0, patterns.length - 1);

        int firstControlY = fullscreen ? 28 : 30;
        int controlSpacing = fullscreen ? 24 : 20;
        addWidget(patternButton = createControlButton(controlsX, firstControlY,
                () -> "P:" + index, this::updatePatternIndex,
                "gtocore.multiblock_preview.pattern_control"));
        addWidget(layerButton = createControlButton(controlsX, firstControlY + controlSpacing,
                () -> layer >= 0 ? "L:" + layer : "ALL", this::updateLayer,
                "gtocore.multiblock_preview.layer_control"));
        addWidget(highlightButton = createControlButton(controlsX, firstControlY + controlSpacing * 2,
                () -> isPartHighlighting ? "H:ON" : "H:OFF",
                cd -> isPartHighlighting = !isPartHighlighting,
                "gtocore.multiblock_preview.highlight_control"));
        if (moduleOverlayAvailable) {
            addWidget(modulesButton = createControlButton(controlsX, firstControlY + controlSpacing * 3,
                    () -> showAllModules ? "M:*" : "M:1",
                    cd -> {
                        showAllModules = !showAllModules;
                        setPage(true);
                    },
                    "gtocore.multiblock_preview.modules_control"));
        } else {
            modulesButton = null;
        }

        if (fullscreen) {
            structureSizeTexture = new TextTexture("1", -1)
                    .setSupplier(this::getVisibleStructureSizeLine)
                    .setWidth(width - 20)
                    .setDropShadow(true);
            addWidget(structureSizeWidget = new ImageWidget(8, height - 54, width - 16, 10, structureSizeTexture));

            Runnable exitFullscreen = Objects.requireNonNull(closeAction);
            addWidget(fullscreenToggleButton = createControlButton(controlsX, 4, () -> "X", cd -> closeFullscreen(exitFullscreen),
                    "gtocore.multiblock_preview.exit_fullscreen"));
            controlsHintTexture = new TextTexture("gtocore.multiblock_preview.controls", -1)
                    .setType(TextTexture.TextType.ROLL)
                    .setWidth(width - 20)
                    .setDropShadow(true);
            addWidget(controlsHintWidget = new ImageWidget(8, height - 42, width - 16, 10, controlsHintTexture));
        } else {
            addWidget(fullscreenToggleButton = createControlButton(controlsX, firstControlY + controlSpacing * 3,
                    () -> "F", cd -> openFullscreen(),
                    "gtocore.multiblock_preview.fullscreen"));
        }

        sceneWidget.setAfterWorldRender((w) -> {
            if (!isPartHighlighting) return;
            for (MBPattern pattern : getVisiblePatterns()) {
                pattern.partsSet.forEach(
                        pos -> {
                            var pos0 = toRenderedPos(pattern, pos);
                            if (!isLayerVisible(pos0.getY())) return;
                            var poseStack = new PoseStack();
                            RenderSystem.disableDepthTest();
                            // RenderSystem.disableCull();
                            RenderSystem.enableBlend();
                            RenderSystem.blendFunc(770, 1);
                            poseStack.pushPose();
                            poseStack.translate((double) pos0.getX() + (double) 0.5F, (double) pos0.getY() + (double) 0.5F, (double) pos0.getZ() + (double) 0.5F);
                            poseStack.scale(1.02f, 1.02f, 1.02f);
                            Tesselator tesselator = Tesselator.getInstance();
                            BufferBuilder buffer = tesselator.getBuilder();
                            RenderSystem.setShader(GameRenderer::getPositionColorShader);
                            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                            RenderUtils.renderCubeFace(poseStack, buffer, -0.5F, -0.5F, -0.5F, 0.5F, 0.5F, 0.5F, 0.2f, 0.6f, 0.2f, 0.3f);
                            tesselator.end();
                            poseStack.popPose();
                            RenderSystem.blendFunc(770, 771);
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                            // RenderSystem.enableCull();
                            RenderSystem.enableDepthTest();
                        });
                pattern.placeHolderSet.forEach(
                        pos -> {
                            var poseStack = new PoseStack();
                            var pos0 = toRenderedPos(pattern, pos);
                            if (!isLayerVisible(pos0.getY())) return;
                            RenderSystem.disableDepthTest();
                            // RenderSystem.disableCull();
                            RenderSystem.enableBlend();
                            RenderSystem.blendFunc(770, 1);
                            poseStack.pushPose();
                            poseStack.translate((double) pos0.getX() + (double) 0.5F, (double) pos0.getY() + (double) 0.5F, (double) pos0.getZ() + (double) 0.5F);
                            poseStack.scale(1.02f, 1.02f, 1.02f);
                            Tesselator tesselator = Tesselator.getInstance();
                            BufferBuilder buffer = tesselator.getBuilder();
                            RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
                            RenderSystem.lineWidth(6);
                            buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
                            // RenderUtils.renderCubeFace(poseStack, buffer, -0.5F, -0.5F, -0.5F, 0.5F, 0.5F, 0.5F,
                            // 0.2f,
                            // 0.2f, 0.6f, 0.3f);
                            RenderBufferUtils.drawCubeFrame(poseStack, buffer, -0.5F, -0.5F, -0.5F, 0.5F, 0.5F, 0.5F, 0.2f, 0.2f, 0.6f, 0.8f);
                            tesselator.end();
                            poseStack.popPose();
                            RenderSystem.blendFunc(770, 771);
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                            // RenderSystem.enableCull();
                            RenderSystem.enableDepthTest();
                        });
            }
        });
        setPage(false);
        recipe.patterns = patterns;
    }

    private ButtonWidget createControlButton(int x, int y, java.util.function.Supplier<String> label,
                                             java.util.function.Consumer<ClickData> onClick, String tooltipKey) {
        ButtonWidget button = new ButtonWidget(
                x, y, 18, 18,
                new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(),
                        new TextTexture("1").setSupplier(label)),
                onClick)
                .setHoverBorderTexture(1, -1);
        button.setHoverTooltips(Component.translatable(tooltipKey));
        return button;
    }

    void closeFullscreen(Runnable closeAction) {
        restoreOverlayBlocks();
        closeAction.run();
    }

    private void openFullscreen() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null) {
            minecraft.setScreen(new MultiblockPreviewScreen(
                    MultiblockPreviewScreen.prepareParent(minecraft.screen), recipe));
        }
    }

    void resizeFullscreen(int width, int height) {
        if (!fullscreen) return;
        setSize(width, height);

        int controlsX = width - 28;
        partsY = height - 27;

        sceneWidget.setSize(width - 44, height - 82);
        scrollableWidgetGroup.setSelfPosition(8, partsY);
        scrollableWidgetGroup.setSize(width - 16, 22);
        titleWidget.setSize(width - 44, 10);
        titleTexture.setWidth(width - 48);

        patternButton.setSelfPosition(controlsX, 28);
        layerButton.setSelfPosition(controlsX, 52);
        highlightButton.setSelfPosition(controlsX, 76);
        if (modulesButton != null) {
            modulesButton.setSelfPosition(controlsX, 100);
        }
        fullscreenToggleButton.setSelfPosition(controlsX, 4);

        if (controlsHintWidget != null) {
            controlsHintWidget.setSelfPosition(8, height - 42);
            controlsHintWidget.setSize(width - 16, 10);
            controlsHintTexture.setWidth(width - 20);
        }
        if (structureSizeWidget != null) {
            structureSizeWidget.setSelfPosition(8, height - 54);
            structureSizeWidget.setSize(width - 16, 10);
            structureSizeTexture.setWidth(width - 20);
        }
        updateCandidatePositions();
        updatePartsScrollBar();
    }

    private void updatePatternIndex(ClickData clickData) {
        switch (clickData.button) {
            case 0: // 鼠标左键: 增加 index
                index = (index + 1 >= patterns.length) ? 0 : index + 1;
                break;
            case 1: // 鼠标右键: 减少 index
                index = (index - 1 < 0) ? patterns.length - 1 : index - 1;
                break;
            case 2: // 鼠标中键: 复位 index 到 0
                index = 0;
                break;
        }
        setPage(false); // 在改变 index 后更新页面
    }

    private void updateLayer(ClickData clickData) {
        MBPattern pattern = patterns[index];
        int maxLayerIndex = getVisibleMaxY() - getVisibleMinY();

        // 根据鼠标按键更新 layer 的值
        switch (clickData.button) {
            case 0: // 鼠标左键: 增加 layer (循环)
                layer++;
                if (layer > maxLayerIndex) {
                    layer = -1; // 超出最大值，回到 "ALL"
                }
                break;
            case 1: // 鼠标右键: 减少 layer (循环)
                layer--;
                if (layer < -1) {
                    layer = maxLayerIndex; // 低于 "ALL"，回到最大值
                }
                break;
            case 2: // 鼠标中键: 复位 layer
                layer = -1; // 直接回到 "ALL"
                break;
        }
        setupScene(pattern, false);
    }

    private void updateFormed(MBPattern pattern, boolean switchShowAllModules) {
        // 在 layer 值更新后，处理相关的状态切换逻辑
        // 这段逻辑是从您原来的方法中平移过来的，现在它能正确处理所有情况
        if (layer == -1) {
            // 如果当前是 "ALL" 视图，且机器结构未显示为“已形成”，则切换
            if (!pattern.controllerBase.isFormed()) {
                onFormedSwitch(true);
            }
        } else {
            // 如果当前是单层视图，且机器结构显示为“已形成”，则切换
            if (pattern.controllerBase.isFormed()) {
                onFormedSwitch(false);
            }
        }
    }

    private void setupScene(MBPattern pattern, boolean switchShowAllModules) {
        restoreOverlayBlocks();
        if (showAllModules && moduleOverlayAvailable) {
            LongSet poses = new LongOpenHashSet();
            for (MBPattern visiblePattern : getVisiblePatterns()) {
                addAlignedPatternBlocks(visiblePattern, poses);
            }
            IMultiController overlayController = updateOverlayFormed();
            LongStream renderedPoses = poses.longStream();
            if (overlayController != null && overlayController.isFormed() && layer == -1) {
                LongSet renderMask = overlayController.getMultiblockState().getMatchContext().getOrDefault(Predicates.DataKey.RENDER_MASK, LongSets.EMPTY_SET);
                if (!renderMask.isEmpty()) {
                    renderedPoses = renderedPoses.filter(pos -> !renderMask.contains(pos));
                }
            }
            sceneWidget.setRenderedCore(renderedPoses.mapToObj(BlockPos::of).toList(), null);
            sceneWidget.setCenter(patterns[0].center.getCenter().toVector3f());
            return;
        }
        updateFormed(pattern, false);
        sceneWidget.setRenderedCore(renderedPositions(pattern).mapToObj(BlockPos::of).toList(), null);
        sceneWidget.setCenter(pattern.center.getCenter().toVector3f());
    }

    @Nullable
    private IMultiController updateOverlayFormed() {
        if (!(LEVEL.getBlockEntity(patterns[0].center) instanceof MetaMachineBlockEntity blockEntity) ||
                !(blockEntity.metaMachine instanceof IMultiController controller)) {
            return null;
        }
        if (controller.isFormed()) {
            controller.onStructureInvalid();
        }
        if (layer != -1) {
            return controller;
        }
        controller.setWaitingTime(0);
        if (controller.checkPattern()) {
            controller.onStructureFormed();
            overlayController = controller;
        } else {
            GTCEu.LOGGER.warn("Pattern formed checking failed: {}", controller.self().getDefinition());
            overlayController = null;
        }
        return controller;
    }

    private LongStream renderedPositions(MBPattern pattern) {
        LongStream longStream = pattern.predicateMap.keySet().longStream();
        if (pattern.controllerBase.isFormed()) {
            LongSet set = pattern.controllerBase.getMultiblockState().getMatchContext().getOrDefault(Predicates.DataKey.RENDER_MASK, LongSets.EMPTY_SET);
            if (!set.isEmpty()) {
                longStream = longStream.filter(pos -> !set.contains(pos));
            }
        }
        return longStream.filter(pos -> layer == -1 || layer + pattern.minY == BlockPos.getY(pos));
    }

    private void addAlignedPatternBlocks(MBPattern pattern, LongSet poses) {
        if (pattern.blockMap == null) return;
        BlockPos baseCenter = patterns[0].center;
        int dx = baseCenter.getX() - pattern.center.getX();
        int dy = baseCenter.getY() - pattern.center.getY();
        int dz = baseCenter.getZ() - pattern.center.getZ();
        int visibleMinY = getVisibleMinY();
        for (var it = pattern.blockMap.long2ReferenceEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            if (isRenderMasked(pattern, entry.getLongKey())) continue;
            BlockPos pos = BlockPos.of(entry.getLongKey()).offset(dx, dy, dz);
            if (layer != -1 && layer + visibleMinY != pos.getY()) continue;
            recordOverlayOriginalBlock(pos);
            LEVEL.addBlock(pos, entry.getValue());
            poses.add(pos.asLong());
        }
    }

    private void recordOverlayOriginalBlock(BlockPos pos) {
        overlayOriginalBlocks.computeIfAbsent(pos.immutable(), k -> new OverlayOriginalBlock(LEVEL.renderedBlocks.get(pos), LEVEL.blockEntities.get(pos)));
    }

    void restoreOverlayBlocks() {
        invalidateOverlayController();
        if (overlayOriginalBlocks.isEmpty()) return;
        overlayOriginalBlocks.forEach((pos, originalBlock) -> {
            if (originalBlock.blockInfo() == null) {
                LEVEL.removeBlock(pos);
            } else {
                LEVEL.addBlock(pos, originalBlock.blockInfo());
            }
            if (originalBlock.blockEntity() == null) {
                LEVEL.blockEntities.remove(pos);
            } else {
                LEVEL.blockEntities.put(pos, originalBlock.blockEntity());
            }
        });
        overlayOriginalBlocks.clear();
    }

    private void invalidateOverlayController() {
        if (overlayController == null) return;
        if (overlayController.isFormed()) {
            overlayController.onStructureInvalid();
        }
        overlayController = null;
    }

    private BlockPos toRenderedPos(MBPattern pattern, long pos) {
        BlockPos blockPos = BlockPos.of(pos);
        if (!showAllModules || !moduleOverlayAvailable) return blockPos;
        return blockPos.offset(
                patterns[0].center.getX() - pattern.center.getX(),
                patterns[0].center.getY() - pattern.center.getY(),
                patterns[0].center.getZ() - pattern.center.getZ());
    }

    private BlockPos fromRenderedPos(MBPattern pattern, BlockPos pos) {
        if (!showAllModules || !moduleOverlayAvailable) return pos;
        return pos.offset(
                pattern.center.getX() - patterns[0].center.getX(),
                pattern.center.getY() - patterns[0].center.getY(),
                pattern.center.getZ() - patterns[0].center.getZ());
    }

    private boolean isLayerVisible(int renderedY) {
        return layer == -1 || layer + getVisibleMinY() == renderedY;
    }

    private int getVisibleMinY() {
        if (!showAllModules || !moduleOverlayAvailable) return patterns[index].minY;
        int minY = Integer.MAX_VALUE;
        for (MBPattern pattern : getVisiblePatterns()) {
            minY = Math.min(minY, pattern.minY + patterns[0].center.getY() - pattern.center.getY());
        }
        return minY;
    }

    private int getVisibleMaxY() {
        if (!showAllModules || !moduleOverlayAvailable) return patterns[index].maxY;
        int maxY = Integer.MIN_VALUE;
        for (MBPattern pattern : getVisiblePatterns()) {
            maxY = Math.max(maxY, pattern.maxY + patterns[0].center.getY() - pattern.center.getY());
        }
        return maxY;
    }

    private String getVisibleStructureSize() {
        StructureBounds bounds = getVisibleStructureBounds();
        return (bounds.maxX() - bounds.minX() + 1) + "x" +
                (bounds.maxY() - bounds.minY() + 1) + "x" +
                (bounds.maxZ() - bounds.minZ() + 1);
    }

    private String getVisibleStructureSizeLine() {
        return Component.translatable("gtocore.multiblock_preview.structure_size").getString() + "：" + getVisibleStructureSize();
    }

    private StructureBounds getVisibleStructureBounds() {
        if (!showAllModules || !moduleOverlayAvailable) {
            MBPattern pattern = patterns[index];
            return new StructureBounds(pattern.minX, pattern.maxX, pattern.minY, pattern.maxY, pattern.minZ, pattern.maxZ);
        }
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (MBPattern pattern : getVisiblePatterns()) {
            int dx = patterns[0].center.getX() - pattern.center.getX();
            int dy = patterns[0].center.getY() - pattern.center.getY();
            int dz = patterns[0].center.getZ() - pattern.center.getZ();
            minX = Math.min(minX, pattern.minX + dx);
            maxX = Math.max(maxX, pattern.maxX + dx);
            minY = Math.min(minY, pattern.minY + dy);
            maxY = Math.max(maxY, pattern.maxY + dy);
            minZ = Math.min(minZ, pattern.minZ + dz);
            maxZ = Math.max(maxZ, pattern.maxZ + dz);
        }
        return new StructureBounds(minX, maxX, minY, maxY, minZ, maxZ);
    }

    public static PatternPreview getPatternWidget(MultiblockInfoEmiRecipe recipe, MultiblockMachineDefinition controllerDefinition) {
        initializeLevel();
        return new PatternPreview(recipe, controllerDefinition);
    }

    static PatternPreview getFullscreenPatternWidget(MultiblockInfoEmiRecipe recipe, int width, int height,
                                                     Runnable closeAction) {
        initializeLevel();
        return new PatternPreview(recipe, recipe.definition, width, height, true, closeAction);
    }

    private static void initializeLevel() {
        if (LEVEL == null) {
            if (Minecraft.getInstance().level == null) {
                GTCEu.LOGGER.error("Try to init pattern previews before level load");
                throw new IllegalStateException();
            }
            LEVEL = new TrackedDummyWorld();
        }
    }

    private void setPage(boolean switchShowAllModules) {
        List<ItemStack> itemList;
        if (index < patterns.length && index >= 0) {
            layer = -1;
            MBPattern pattern = patterns[index];
            setupScene(pattern, switchShowAllModules);
            itemList = getVisibleParts(pattern);
            recipe.i = index;
        } else {
            return;
        }
        if (slotWidgets != null) {
            for (SlotWidget slotWidget : slotWidgets) {
                scrollableWidgetGroup.removeWidget(slotWidget);
            }
        }
        scrollableWidgetGroup.setScrollXOffset(0);
        slotWidgets = new PatternSlotWidget[itemList.size()];
        for (int i = 0; i < slotWidgets.length; i++) {
            slotWidgets[i] = new PatternSlotWidget(new ItemStackHandler(itemList.get(i)), i, 4 + i * 18, 0);
            scrollableWidgetGroup.addWidget(slotWidgets[i]);
        }
        updatePartsScrollBar();
    }

    private void updatePartsScrollBar() {
        int contentWidth = slotWidgets.length == 0 ? 0 : 4 + slotWidgets.length * 18;
        boolean needsScrolling = contentWidth > scrollableWidgetGroup.getSizeWidth();
        scrollableWidgetGroup.setScrollable(needsScrolling);
        if (!needsScrolling) {
            scrollableWidgetGroup.setScrollXOffset(0);
        }
        scrollableWidgetGroup.setXScrollBarHeight(needsScrolling ? PARTS_SCROLL_BAR_HEIGHT : 0);
    }

    private void onFormedSwitch(boolean isFormed) {
        MBPattern pattern = patterns[index];
        IMultiController controllerBase = pattern.controllerBase;
        if (isFormed) {
            loadControllerFormed(pattern.pattern, pattern.predicateMap.keySet(), controllerBase);
        } else {
            sceneWidget.setRenderedCore(pattern.predicateMap.keySet().longStream().mapToObj(BlockPos::of).toList(), null);
            controllerBase.onStructureInvalid();
        }
    }

    private void onPosSelected(BlockPos pos, Direction facing) {
        if (index >= patterns.length || index < 0) return;
        TraceabilityPredicate predicate = getSelectedPredicate(pos);
        if (predicate != null) {
            predicates.clear();
            predicates.addAll(predicate.common);
            predicates.addAll(predicate.limited);
            predicates.removeIf(p -> p == null || p.candidates == null); // why it happens?
            if (candidates != null) {
                for (SlotWidget candidate : candidates) {
                    removeWidget(candidate);
                }
            }
            List<List<ItemStack>> candidateStacks = new ArrayList<>();
            List<List<Component>> predicateTips = new ArrayList<>();
            for (SimplePredicate simplePredicate : predicates) {
                List<ItemStack> itemStacks = new ArrayList<>();
                for (ItemStack stack : simplePredicate.getCandidates()) {
                    if (stack.getItem() instanceof ICustomfCategoryFill customfCategoryFill) {
                        customfCategoryFill.fillItemCategory(itemStacks::add);
                    } else {
                        itemStacks.add(stack);
                    }
                }
                if (!itemStacks.isEmpty()) {
                    candidateStacks.add(itemStacks);
                    predicateTips.add(simplePredicate.getToolTips(predicate));
                }
            }
            candidates = new SlotWidget[candidateStacks.size()];
            CycleItemStackHandler itemHandler = new CycleItemStackHandler(candidateStacks);
            int maxRows = Math.max(1, (partsY - 6) / 18);
            for (int i = 0; i < candidateStacks.size(); i++) {
                int finalI = i;
                candidates[i] = new SelectedSlotWidget(
                        candidateStacks.get(i), itemHandler, i,
                        3 + (i / maxRows) * 18,
                        3 + (i % maxRows) * 18)
                        .setBackgroundTexture(new ColorRectTexture(1342177279))
                        .setOnAddedTooltips((slot, list) -> list.addAll(predicateTips.get(finalI)));
                addWidget(candidates[i]);
            }
        }
    }

    private TraceabilityPredicate getSelectedPredicate(BlockPos pos) {
        if (!showAllModules || !moduleOverlayAvailable) {
            return patterns[index].predicateMap.get(pos.asLong());
        }
        List<MBPattern> visiblePatterns = getVisiblePatterns();
        for (int i = visiblePatterns.size() - 1; i >= 0; i--) {
            MBPattern pattern = visiblePatterns.get(i);
            TraceabilityPredicate predicate = pattern.predicateMap.get(fromRenderedPos(pattern, pos).asLong());
            if (predicate != null) return predicate;
        }
        return null;
    }

    private List<MBPattern> getVisiblePatterns() {
        if (!showAllModules || !moduleOverlayAvailable) {
            return List.of(patterns[index]);
        }
        return Arrays.asList(patterns).subList(0, index + 1);
    }

    private List<ItemStack> getVisibleParts(MBPattern pattern) {
        if (!showAllModules || !moduleOverlayAvailable) return pattern.parts;
        Long2ReferenceOpenHashMap<BlockInfo> visibleBlocks = new Long2ReferenceOpenHashMap<>();
        for (MBPattern visiblePattern : getVisiblePatterns()) {
            addAlignedPatternBlocks(visiblePattern, visibleBlocks);
        }
        List<ItemStack> countedItems = new ArrayList<>();
        for (var it = visibleBlocks.long2ReferenceEntrySet().fastIterator(); it.hasNext();) {
            ItemStack stack = it.next().getValue().getItemStackForm();
            if (!stack.isEmpty()) mergeItemStack(countedItems, stack);
        }
        List<ItemStack> itemList = new ArrayList<>();
        for (MBPattern visiblePattern : getVisiblePatterns()) {
            for (ItemStack stack : visiblePattern.parts) {
                int itemIndex = indexOfItemStack(countedItems, stack);
                if (itemIndex >= 0) {
                    itemList.add(countedItems.remove(itemIndex));
                }
            }
        }
        itemList.addAll(countedItems);
        return itemList;
    }

    private void addAlignedPatternBlocks(MBPattern pattern, Long2ReferenceOpenHashMap<BlockInfo> blocks) {
        if (pattern.blockMap == null) return;
        BlockPos baseCenter = patterns[0].center;
        int dx = baseCenter.getX() - pattern.center.getX();
        int dy = baseCenter.getY() - pattern.center.getY();
        int dz = baseCenter.getZ() - pattern.center.getZ();
        int visibleMinY = getVisibleMinY();
        for (var it = pattern.blockMap.long2ReferenceEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            if (isRenderMasked(pattern, entry.getLongKey())) continue;
            BlockPos pos = BlockPos.of(entry.getLongKey()).offset(dx, dy, dz);
            if (layer != -1 && layer + visibleMinY != pos.getY()) continue;
            blocks.put(pos.asLong(), entry.getValue());
        }
    }

    private boolean isRenderMasked(MBPattern pattern, long pos) {
        if (layer != -1) return false;
        if (!pattern.controllerBase.isFormed()) return false;
        LongSet set = pattern.controllerBase.getMultiblockState().getMatchContext().getOrDefault(Predicates.DataKey.RENDER_MASK, LongSets.EMPTY_SET);
        return set.contains(pos);
    }

    private static void mergeItemStack(List<ItemStack> itemList, ItemStack stack) {
        for (ItemStack itemStack : itemList) {
            if (ItemStack.isSameItemSameTags(itemStack, stack)) {
                itemStack.grow(stack.getCount());
                return;
            }
        }
        itemList.add(stack.copy());
    }

    private static int indexOfItemStack(List<ItemStack> itemList, ItemStack stack) {
        for (int i = 0; i < itemList.size(); i++) {
            if (ItemStack.isSameItemSameTags(itemList.get(i), stack)) return i;
        }
        return -1;
    }

    private record OverlayOriginalBlock(BlockInfo blockInfo, BlockEntity blockEntity) {}

    private record StructureBounds(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {}

    private static boolean hasModuleTooltip(MultiblockMachineDefinition definition) {
        var tooltipBuilder = definition.getTooltipBuilder();
        if (tooltipBuilder == null) return false;
        List<Component> tooltips = new ArrayList<>();
        tooltipBuilder.accept(definition.asStack(), tooltips);
        return tooltips.stream().anyMatch(PatternPreview::hasModuleTooltipKey);
    }

    private static boolean hasModuleTooltipKey(Component component) {
        String moduleTooltipKeyPrefix = NewDataAttributes.PREFIX_TEMPLATE + "." + NewDataAttributes.ALLOW_MODULE.getKey();
        if (component.getContents() instanceof TranslatableContents contents && contents.getKey().startsWith(moduleTooltipKeyPrefix)) {
            return true;
        }
        for (Component sibling : component.getSiblings()) {
            if (hasModuleTooltipKey(sibling)) return true;
        }
        return false;
    }

    private void updateCandidatePositions() {
        if (candidates == null) return;
        int maxRows = Math.max(1, (partsY - 6) / 18);
        for (int i = 0; i < candidates.length; i++) {
            candidates[i].setSelfPosition(3 + (i / maxRows) * 18, 3 + (i % maxRows) * 18);
        }
    }

    private void loadControllerFormed(BlockPattern pattern, LongSet poses, IMultiController controllerBase) {
        var state = controllerBase.getMultiblockState();
        state.clearCache();
        if (controllerBase.isFormed()) controllerBase.onStructureInvalid();
        if (pattern != null && pattern.checkPatternAt(state, true)) {
            controllerBase.onStructureFormed();
        }
        state.clearCache();
        if (controllerBase.isFormed()) {
            LongSet set = state.getMatchContext().getOrDefault(Predicates.DataKey.RENDER_MASK, LongSets.EMPTY_SET);
            if (!set.isEmpty()) {
                sceneWidget.setRenderedCore(poses.longStream().filter(pos -> !set.contains(pos)).mapToObj(BlockPos::of).toList(), null);
            } else {
                sceneWidget.setRenderedCore(poses.longStream().mapToObj(BlockPos::of).toList(), null);
            }
        } else {
            GTCEu.LOGGER.warn("Pattern formed checking failed: {}", controllerBase.self().getDefinition());
        }
    }

    private MBPattern initializePattern(MultiblockDefinition definition, MultiblockDefinition.Pattern pattern, int index, boolean hasModule) {
        var triple = pattern.initialize(definition, index);
        var patternMap = triple.getThird();
        var pos = triple.getFirst();
        Long2ReferenceOpenHashMap<BlockInfo> blockMap = new Long2ReferenceOpenHashMap<>(patternMap.values().stream().mapToInt(LongOpenHashSet::size).sum());
        patternMap.forEach((b, i) -> i.forEach(p -> blockMap.put(p, b)));
        IMultiController controllerBase = blockMap.get(pos.asLong()).getBlockEntity(pos) instanceof MetaMachineBlockEntity blockEntity ? blockEntity.metaMachine instanceof IMultiController controller ? controller : null : null;
        for (var it = blockMap.long2ReferenceEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            LEVEL.addBlock(BlockPos.of(entry.getLongKey()), entry.getValue());
        }
        if (controllerBase != null) {
            controllerBase.self().holder.setLevel(LEVEL);
            LEVEL.setInnerBlockEntity(controllerBase.self().holder);
        }
        Long2ObjectOpenHashMap<TraceabilityPredicate> predicateMap = controllerBase == null ? null : new Long2ObjectOpenHashMap<>();
        if (controllerBase != null) {
            loadControllerFormed(triple.getSecond(), predicateMap.keySet(), controllerBase);
            predicateMap = controllerBase.getMultiblockState().getMatchContext().getPredicates();
        }
        return controllerBase == null ? null : new MBPattern(hasModule, blockMap, pattern.parts(), triple.getSecond(), predicateMap, controllerBase);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 1/* right button */ && sceneWidget.isMouseOverElement(mouseX, mouseY)) {
            double panScale = sceneWidget.getZoom() /
                    Math.clamp(sceneWidget.getSizeWidth(), 1, sceneWidget.getSizeHeight());
            dragX *= panScale;
            dragY *= panScale;
            double rotationPitch = Math.toRadians(sceneWidget.getRotationPitch());
            double rotationYaw = Math.toRadians(sceneWidget.getRotationYaw());
            float moveX = -(float) (dragY * Math.sin(rotationYaw) * Math.cos(rotationPitch) + dragX * Math.sin(rotationPitch));
            float moveY = (float) (dragY * Math.cos(rotationYaw));
            float moveZ = (float) (-dragY * Math.sin(rotationYaw) * Math.sin(rotationPitch) + dragX * Math.cos(rotationPitch));
            sceneWidget.setCenter(sceneWidget.getCenter().add(moveX, moveY, moveZ));
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!fullscreen && recipe.i != index && recipe.i >= 0 && recipe.i < patterns.length) {
            index = recipe.i;
            setPage(false);
        }
        if (!isLoaded && Minecraft.getInstance().screen instanceof RecipeScreen) {
            setPage(false);
            isLoaded = true;
        }
    }

    @Override
    public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
    }

    public static final class MBPattern {

        @NotNull
        public final List<ItemStack> parts;
        @NotNull
        private final BlockPattern pattern;
        @NotNull
        private final Long2ObjectOpenHashMap<TraceabilityPredicate> predicateMap;
        @NotNull
        private final IMultiController controllerBase;
        @Nullable
        private final Long2ReferenceOpenHashMap<BlockInfo> blockMap;
        private final LongSet partsSet;
        private final LongSet placeHolderSet;
        private final int minX;
        private final int maxX;
        private final int maxY;
        private final int minY;
        private final int minZ;
        private final int maxZ;
        private final BlockPos center;

        private MBPattern(boolean hasModule, @NotNull Long2ReferenceOpenHashMap<BlockInfo> blockMap, @NotNull List<ItemStack> parts, BlockPattern pattern, @NotNull Long2ObjectOpenHashMap<TraceabilityPredicate> predicateMap, @NotNull IMultiController controllerBase) {
            this.parts = parts;
            this.pattern = pattern;
            this.partsSet = new LongOpenHashSet();
            this.placeHolderSet = new LongOpenHashSet();
            this.predicateMap = predicateMap;
            this.controllerBase = controllerBase;
            this.blockMap = hasModule ? blockMap : null;
            this.center = controllerBase.self().getPos();
            for (var entry : predicateMap.long2ObjectEntrySet()) {
                var pos = entry.getLongKey();
                var predicate = entry.getValue();
                var simplePredicates = ObjectArrays.concat(predicate.common.toArray(new SimplePredicate[0]), predicate.limited.toArray(new SimplePredicate[0]), SimplePredicate.class);
                Arrays.stream(simplePredicates)
                        .map(s -> s.blockInfo.get())
                        .filter(Objects::nonNull)
                        .forEach(s -> {
                            if (s.hasBlockEntity() &&
                                    s.getBlockEntity(BlockPos.of(entry.getLongKey())) instanceof MetaMachineBlockEntity mmbe &&
                                    mmbe.getMetaMachine() instanceof MultiblockPartMachine)
                                partsSet.add(pos);
                            if (s.getItemStackForm().is(Items.BARRIER)) placeHolderSet.add(pos);
                        });
            }
            int minX = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minZ = Integer.MAX_VALUE;
            int maxZ = Integer.MIN_VALUE;
            for (var it = blockMap.long2ReferenceEntrySet().fastIterator(); it.hasNext();) {
                long pos = it.next().getLongKey();
                int x = BlockPos.getX(pos);
                int y = BlockPos.getY(pos);
                int z = BlockPos.getZ(pos);
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
                minZ = Math.min(minZ, z);
                maxZ = Math.max(maxZ, z);
            }

            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
            this.minZ = minZ;
            this.maxZ = maxZ;
        }
    }

    private static final class MySceneWidget extends SceneWidget {

        private MySceneWidget(int x, int y, int width, int height) {
            super(x, y, width, height, LEVEL);
        }

        @Override
        public void renderBlockOverLay(WorldSceneRenderer renderer) {
            PoseStack poseStack = new PoseStack();
            hoverPosFace = null;
            hoverItem = null;
            if (isMouseOverElement(currentMouseX, currentMouseY)) {
                BlockHitResult hit = renderer.getLastTraceResult();
                if (hit != null) {
                    if (core.contains(hit.getBlockPos())) {
                        hoverPosFace = new BlockPosFace(hit.getBlockPos(), hit.getDirection());
                    } else if (!useOrtho) {
                        Vector3f hitPos = hit.getLocation().toVector3f();
                        Level world = renderer.world;
                        Vec3 eyePos = new Vec3(renderer.getEyePos());
                        hitPos.mul(2);
                        Vec3 endPos = new Vec3((hitPos.x - eyePos.x), (hitPos.y - eyePos.y), (hitPos.z - eyePos.z));
                        double min = Float.MAX_VALUE;
                        for (BlockPos pos : core) {
                            BlockState blockState = world.getBlockState(pos);
                            if (blockState.getBlock() == Blocks.AIR) {
                                continue;
                            }
                            hit = world.clipWithInteractionOverride(eyePos, endPos, pos, blockState.getShape(world, pos), blockState);
                            if (hit != null && hit.getType() != HitResult.Type.MISS) {
                                double dist = eyePos.distanceToSqr(hit.getLocation());
                                if (dist < min) {
                                    min = dist;
                                    hoverPosFace = new BlockPosFace(hit.getBlockPos(), hit.getDirection());
                                }
                            }
                        }
                    }
                }
            }
            if (hoverPosFace != null) {
                var state = getDummyWorld().getBlockState(hoverPosFace.pos);
                hoverItem = state.getBlock().getCloneItemStack(getDummyWorld(), hoverPosFace.pos, state);
            }
            BlockPosFace tmp = dragging ? clickPosFace : hoverPosFace;
            if (selectedPosFace != null || tmp != null) {
                if (selectedPosFace != null && renderFacing) {
                    drawFacingBorder(poseStack, selectedPosFace, -16711936);
                }
                if (tmp != null && !tmp.equals(selectedPosFace) && renderFacing) {
                    drawFacingBorder(poseStack, tmp, -1);
                }
            }
            if (selectedPosFace != null && renderSelect) {
                RenderUtils.renderBlockOverLay(poseStack, selectedPosFace.pos, 0.6F, 0, 0, 1.03F);
            }
            if (this.afterWorldRender != null) {
                this.afterWorldRender.accept(this);
            }
        }
    }
}
