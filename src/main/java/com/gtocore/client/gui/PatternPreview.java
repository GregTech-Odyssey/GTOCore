package com.gtocore.client.gui;

import com.gtocore.integration.emi.multipage.MultiblockInfoEmiRecipe;

import com.gtolib.api.gui.PatternSlotWidget;
import com.gtolib.api.gui.SelectedSlotWidget;
import com.gtolib.api.item.ItemHandlerModifiable;
import com.gtolib.api.machine.MultiblockDefinition;
import com.gtolib.api.machine.feature.multiblock.IMultiStructureMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer;
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
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@OnlyIn(Dist.CLIENT)
public final class PatternPreview extends WidgetGroup {

    private static boolean isPartHighlighting = false;

    private final MultiblockInfoEmiRecipe recipe;
    private boolean isLoaded;
    private static TrackedDummyWorld LEVEL;
    private static final Map<MultiblockMachineDefinition, MBPattern[]> CACHE = new Reference2ReferenceOpenHashMap<>();
    private final SceneWidget sceneWidget;
    private final DraggableScrollableWidgetGroup scrollableWidgetGroup;
    private final MBPattern[] patterns;
    private final List<SimplePredicate> predicates = new ArrayList<>();
    private int index;
    private int layer;
    private PatternSlotWidget[] slotWidgets;
    private SlotWidget[] candidates;

    private PatternPreview(MultiblockInfoEmiRecipe recipe, MultiblockMachineDefinition controllerDefinition) {
        super(0, 0, 160, 160);
        this.recipe = recipe;
        setClientSideWidget();
        layer = -1;
        addWidget(sceneWidget = new MySceneWidget().setOnSelected(this::onPosSelected).setRenderFacing(false).setRenderFacing(false));
        scrollableWidgetGroup = new DraggableScrollableWidgetGroup(3, 132, 154, 22).setXScrollBarHeight(4).setXBarStyle(GuiTextures.SLIDER_BACKGROUND, GuiTextures.BUTTON).setScrollable(true).setDraggable(true).setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.HORIZONTAL);
        scrollableWidgetGroup.setScrollYOffset(0);
        addWidget(scrollableWidgetGroup);
        if (ConfigHolder.INSTANCE.client.useVBO) {
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.recordRenderCall(sceneWidget::useCacheBuffer);
            } else {
                sceneWidget.useCacheBuffer();
            }
        }
        addWidget(new ImageWidget(3, 3, 160, 10, new TextTexture(controllerDefinition.getDescriptionId(), -1).setType(TextTexture.TextType.ROLL).setWidth(170).setDropShadow(true)));
        if (CACHE.containsKey(controllerDefinition)) {
            patterns = CACHE.get(controllerDefinition);
        } else {
            MultiblockDefinition definition = MultiblockDefinition.of(controllerDefinition);
            MultiblockDefinition.Pattern[] pattern = definition.getPatterns();
            patterns = new MBPattern[pattern.length];
            for (int i = 0; i < pattern.length; i++) {
                patterns[i] = initializePattern(definition, pattern[i], i);
            }
            CACHE.put(controllerDefinition, patterns);
            definition.clear();
        }
        addWidget(new ButtonWidget(138, 30, 18, 18, new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), new TextTexture("1").setSupplier(() -> "P:" + index)), this::updatePatternIndex).setHoverBorderTexture(1, -1));
        addWidget(new ButtonWidget(138, 50, 18, 18, new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), new TextTexture("1").setSupplier(() -> layer >= 0 ? "L:" + layer : "ALL")), this::updateLayer).setHoverBorderTexture(1, -1));
        addWidget(new ButtonWidget(138, 70, 18, 18, new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture(), new TextTexture("1").setSupplier(() -> isPartHighlighting ? "H:ON" : "H:OFF")), cd -> isPartHighlighting = !isPartHighlighting).setHoverBorderTexture(1, -1));

        sceneWidget.setAfterWorldRender((w) -> {
            if (!isPartHighlighting) return;
            patterns[index].partsSet.forEach(
                    pos -> {
                        var poseStack = new PoseStack();
                        var pos0 = BlockPos.of(pos);
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
        });
        setPage();
        recipe.patterns = patterns;
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
        setPage(); // 在改变 index 后更新页面
    }

    private void updateLayer(ClickData clickData) {
        MBPattern pattern = patterns[index];
        int maxLayerIndex = pattern.maxY - pattern.minY;

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
        setupScene(pattern);
    }

    private void setupScene(MBPattern pattern) {
        LongStream longStream = pattern.predicateMap.keySet().longStream();
        if (pattern.controllerBase.isFormed()) {
            LongSet set = pattern.controllerBase.getMultiblockState().getMatchContext().getOrDefault("renderMask", LongSets.EMPTY_SET);
            if (!set.isEmpty()) {
                sceneWidget.setRenderedCore(longStream.filter(pos -> !set.contains(pos)).mapToObj(BlockPos::of).filter(pos -> layer == -1 || layer + pattern.minY == pos.getY()).collect(Collectors.toList()), null);
            } else {
                sceneWidget.setRenderedCore(longStream.mapToObj(BlockPos::of).filter(pos -> layer == -1 || layer + pattern.minY == pos.getY()).toList(), null);
            }
        } else {
            sceneWidget.setRenderedCore(longStream.mapToObj(BlockPos::of).filter(pos -> layer == -1 || layer + pattern.minY == pos.getY()).toList(), null);
        }
        sceneWidget.setCenter(pattern.center.getCenter().toVector3f());
    }

    public static PatternPreview getPatternWidget(MultiblockInfoEmiRecipe recipe, MultiblockMachineDefinition controllerDefinition) {
        if (LEVEL == null) {
            if (Minecraft.getInstance().level == null) {
                GTCEu.LOGGER.error("Try to init pattern previews before level load");
                throw new IllegalStateException();
            }
            LEVEL = new TrackedDummyWorld();
        }
        return new PatternPreview(recipe, controllerDefinition);
    }

    private void setPage() {
        List<ItemStack> itemList;
        if (index < patterns.length && index >= 0) {
            layer = -1;
            MBPattern pattern = patterns[index];
            setupScene(pattern);
            itemList = pattern.parts;
            recipe.i = index;
        } else {
            return;
        }
        if (slotWidgets != null) {
            for (SlotWidget slotWidget : slotWidgets) {
                scrollableWidgetGroup.removeWidget(slotWidget);
            }
        }
        slotWidgets = new PatternSlotWidget[itemList.size()];
        for (int i = 0; i < slotWidgets.length; i++) {
            slotWidgets[i] = new PatternSlotWidget(new ItemHandlerModifiable(itemList.get(i)), i, 4 + i * 18, 0);
            scrollableWidgetGroup.addWidget(slotWidgets[i]);
        }
    }

    private void onFormedSwitch(boolean isFormed) {
        MBPattern pattern = patterns[index];
        IMultiController controllerBase = pattern.controllerBase;
        if (isFormed) {
            layer = -1;
            loadControllerFormed(pattern.predicateMap.keySet(), controllerBase, index);
        } else {
            sceneWidget.setRenderedCore(pattern.predicateMap.keySet().longStream().mapToObj(BlockPos::of).toList(), null);
            controllerBase.onStructureInvalid();
        }
    }

    private void onPosSelected(BlockPos pos, Direction facing) {
        if (index >= patterns.length || index < 0) return;
        TraceabilityPredicate predicate = patterns[index].predicateMap.get(pos.asLong());
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
                List<ItemStack> itemStacks = simplePredicate.getCandidates();
                if (!itemStacks.isEmpty()) {
                    candidateStacks.add(itemStacks);
                    predicateTips.add(simplePredicate.getToolTips(predicate));
                }
            }
            candidates = new SlotWidget[candidateStacks.size()];
            CycleItemStackHandler itemHandler = new CycleItemStackHandler(candidateStacks);
            int maxCol = (132 - (((slotWidgets.length - 1) / 9 + 1) * 18) - 35) % 18;
            for (int i = 0; i < candidateStacks.size(); i++) {
                int finalI = i;
                candidates[i] = new SelectedSlotWidget(candidateStacks.get(i), itemHandler, i, 3 + (i / maxCol) * 18, 3 + (i % maxCol) * 18).setBackgroundTexture(new ColorRectTexture(1342177279)).setOnAddedTooltips((slot, list) -> list.addAll(predicateTips.get(finalI)));
                addWidget(candidates[i]);
            }
        }
    }

    private void loadControllerFormed(LongSet poses, IMultiController controllerBase, int index) {
        BlockPattern pattern;
        if (controllerBase instanceof IMultiStructureMachine machine) {
            pattern = machine.getMultiPattern().get(index);
        } else {
            var subPattern = controllerBase.getSubPattern();
            if (subPattern != null && index > 0) {
                pattern = subPattern[index - 1].get();
            } else {
                pattern = controllerBase.getPattern();
            }
        }
        var state = controllerBase.getMultiblockState();
        if (pattern != null && pattern.checkPatternAt(state, true)) {
            controllerBase.onStructureFormed();
        }
        state.clearCache();
        if (controllerBase.isFormed()) {
            LongSet set = state.getMatchContext().getOrDefault("renderMask", LongSets.EMPTY_SET);
            if (!set.isEmpty()) {
                sceneWidget.setRenderedCore(poses.longStream().filter(pos -> !set.contains(pos)).mapToObj(BlockPos::of).toList(), null);
            } else {
                sceneWidget.setRenderedCore(poses.longStream().mapToObj(BlockPos::of).toList(), null);
            }
        } else {
            GTCEu.LOGGER.warn("Pattern formed checking failed: {}", controllerBase.self().getDefinition());
        }
    }

    private MBPattern initializePattern(MultiblockDefinition definition, MultiblockDefinition.Pattern pattern, int index) {
        var pair = pattern.initialize(definition, index);
        var patternMap = pair.getSecond();
        var pos = pair.getFirst();
        Long2ReferenceOpenHashMap<BlockInfo> blockMap = new Long2ReferenceOpenHashMap<>(patternMap.values().stream().mapToInt(LongOpenHashSet::size).sum());
        patternMap.forEach((b, i) -> i.forEach(p -> blockMap.put(p, b)));
        IMultiController controllerBase = blockMap.get(pos.asLong()).getBlockEntity(pos) instanceof MetaMachineBlockEntity blockEntity ? blockEntity.metaMachine instanceof IMultiController controller ? controller : null : null;
        for (var it = blockMap.long2ReferenceEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            LEVEL.addBlock(BlockPos.of(entry.getLongKey()), entry.getValue());
        }
        if (controllerBase != null) {
            controllerBase.self().holder.getSelf().setLevel(LEVEL);
            LEVEL.setInnerBlockEntity(controllerBase.self().holder.getSelf());
        }
        Long2ObjectOpenHashMap<TraceabilityPredicate> predicateMap = controllerBase == null ? null : new Long2ObjectOpenHashMap<>();
        if (controllerBase != null) {
            loadControllerFormed(predicateMap.keySet(), controllerBase, index);
            predicateMap = controllerBase.getMultiblockState().getMatchContext().getPredicates();
        }
        return controllerBase == null ? null : new MBPattern(blockMap, pattern.parts(), predicateMap, controllerBase);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 1/* right button */) {
            dragX *= 0.1;
            dragY *= 0.1;
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
    @OnlyIn(Dist.CLIENT)
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        if (scrollableWidgetGroup.isMouseOverElement(mouseX, mouseY)) {
            return super.mouseWheelMove(mouseX, mouseY, wheelDelta);
        }
        if (sceneWidget.isMouseOverElement(mouseX, mouseY)) {
            double rotationPitch = Math.toRadians(sceneWidget.getRotationPitch());
            double rotationYaw = Math.toRadians(sceneWidget.getRotationYaw());
            float moveX = -(float) (wheelDelta * Math.cos(rotationYaw) * Math.cos(rotationPitch));
            float moveY = -(float) (wheelDelta * Math.sin(rotationYaw));
            float moveZ = -(float) (wheelDelta * Math.cos(rotationYaw) * Math.sin(rotationPitch));
            sceneWidget.setCenter(sceneWidget.getCenter().add(moveX, moveY, moveZ));
            return true;
        }
        return super.mouseWheelMove(mouseX, mouseY, wheelDelta);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!isLoaded && Minecraft.getInstance().screen instanceof RecipeScreen) {
            setPage();
            isLoaded = true;
        }
    }

    @Override
    public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
    }

    public static class MBPattern {

        @NotNull
        public final List<ItemStack> parts;
        @NotNull
        private final Long2ObjectOpenHashMap<TraceabilityPredicate> predicateMap;
        @NotNull
        private final IMultiController controllerBase;
        private final LongSet partsSet;
        private final int maxY;
        private final int minY;
        private final BlockPos center;

        private MBPattern(@NotNull Long2ReferenceOpenHashMap<BlockInfo> blockMap, @NotNull List<ItemStack> parts, @NotNull Long2ObjectOpenHashMap<TraceabilityPredicate> predicateMap, @NotNull IMultiController controllerBase) {
            this.parts = parts;
            this.partsSet = new LongOpenHashSet();
            this.predicateMap = predicateMap;
            this.controllerBase = controllerBase;
            this.center = controllerBase.self().getPos();
            for (var entry : predicateMap.long2ObjectEntrySet()) {
                var pos = entry.getLongKey();
                var predicate = entry.getValue();
                predicate.common.stream()
                        .map(s -> s.blockInfo.get())
                        .filter(Objects::nonNull)
                        .filter(s -> s.hasBlockEntity() &&
                                s.getBlockEntity(BlockPos.of(entry.getLongKey())) instanceof MetaMachineBlockEntity mmbe &&
                                mmbe.getMetaMachine() instanceof MultiblockPartMachine)
                        .forEach(s -> partsSet.add(pos));
                predicate.limited.stream()
                        .map(s -> s.blockInfo.get())
                        .filter(Objects::nonNull)
                        .filter(s -> s.hasBlockEntity() &&
                                s.getBlockEntity(BlockPos.of(entry.getLongKey())) instanceof MetaMachineBlockEntity mmbe &&
                                mmbe.getMetaMachine() instanceof MultiblockPartMachine)
                        .forEach(s -> partsSet.add(pos));
            }
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (var it = blockMap.long2ReferenceEntrySet().fastIterator(); it.hasNext();) {
                var y = BlockPos.getY(it.next().getLongKey());
                min = Math.min(min, y);
                max = Math.max(max, y);
            }

            minY = min;
            maxY = max;
        }
    }

    private static final class MySceneWidget extends SceneWidget {

        private MySceneWidget() {
            super(3, 3, 150, 150, LEVEL);
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
