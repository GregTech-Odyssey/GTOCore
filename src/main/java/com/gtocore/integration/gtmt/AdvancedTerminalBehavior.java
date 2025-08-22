package com.gtocore.integration.gtmt;

import com.gtocore.common.block.BlockMap;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import com.hepdd.gtmthings.api.gui.widget.TerminalInputWidget;
import com.hepdd.gtmthings.api.misc.Hatch;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

import java.util.*;
import java.util.stream.Stream;

@DataGeneratorScanned
public class AdvancedTerminalBehavior implements IItemUIFactory {

    @RegisterLanguage(cn = "模块搭建", en = "Module Build")
    private static final String MODULE = "gtocore.auto_build.module";

    @RegisterLanguage(cn = "镜像搭建", en = "Mirror Build")
    private static final String FLIP = "gtocore.auto_build.flip";

    @RegisterLanguage(cn = "替换模式", en = "Replace Mode")
    private static final String REPLACE = "gtocore.auto_build.replace";

    @RegisterLanguage(cn = "替换等级方块为设置的等级方块", en = "Replace Tier Block with the set block")
    private static final String REPLACE_A = "gtocore.auto_build.replace.a";

    @RegisterLanguage(cn = "等级方块", en = "Mirror Build")
    private static final String TIER = "gtocore.auto_build.tier";

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            Level level = context.getLevel();
            BlockPos blockPos = context.getClickedPos();
            if (context.getPlayer() != null && !level.isClientSide() && MetaMachine.getMachine(level, blockPos) instanceof IMultiController controller) {
                AutoBuildSetting autoBuildSetting = getAutoBuildSetting(context.getPlayer().getMainHandItem());
                BlockPattern pattern;
                var subPattern = controller.getSubPattern();
                if (autoBuildSetting.module > 0 && subPattern != null) {
                    pattern = subPattern[Math.min(subPattern.length, autoBuildSetting.module) - 1].get();
                } else {
                    pattern = controller.getPattern();
                }
                if (autoBuildSetting.isReplaceMode()) {
                    AdvancedBlockPattern.getAdvancedBlockPattern(pattern).autoBuild(context.getPlayer(), controller.getMultiblockState(), autoBuildSetting);
                    controller.getMultiblockState().cleanCache();
                    controller.requestCheck();
                } else {
                    controller.requestCheck();
                    controller.setWaitingTime(10);
                    AdvancedBlockPattern.getAdvancedBlockPattern(pattern).autoBuild(context.getPlayer(), controller.getMultiblockState(), autoBuildSetting);
                    controller.getMultiblockState().cleanCache();
                    controller.setWaitingTime(0);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public ModularUI createUI(HeldItemUIFactory.HeldItemHolder holder, Player entityPlayer) {
        return new ModularUI(176, 166, holder, entityPlayer).widget(createWidget(entityPlayer));
    }

    private static Widget createWidget(Player entityPlayer) {
        ItemStack handItem = entityPlayer.getMainHandItem();
        var group = new WidgetGroup(0, 0, 190, 142);
        int rowIndex = 1;
        group.addWidget(
                new DraggableScrollableWidgetGroup(4, 4, 182, 134).setUseScissor(false)
                        .setBackground(GuiTextures.DISPLAY)
                        .setYScrollBarWidth(2)
                        .setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1))
                        .addWidget(new LabelWidget(40, 5, "item.gtmthings.advanced_terminal.setting.title"))
                        .addWidget(new LabelWidget(4, 5 + 16 * rowIndex, () -> {
                            var category = BlockMap.MAP.getOrDefault(handItem.getOrCreateTag().getString("block"), new Block[0]);
                            var tier0 = handItem.getOrCreateTag().getInt("tier");
                            if (category.length == 0 || tier0 <= 0 || tier0 > category.length) return Component.translatable(TIER).getString();
                            return Component.translatable(TIER)
                                    .append("(")
                                    .append(Stream.of(category).map(Block::getName).toList().get(tier0 - 1))
                                    .append(")")
                                    .getString();
                        }) {

                            @OnlyIn(Dist.CLIENT)
                            protected void drawTooltipTexts(int mouseX, int mouseY) {
                                if (this.isMouseOverElement(mouseX, mouseY) && this.getHoverElement(mouseX, mouseY) == this && this.gui != null && this.gui.getModularUIGui() != null) {
                                    List<Component> lines = new ArrayList<>(List.of());
                                    int i = 0;
                                    for (var block : BlockMap.MAP.getOrDefault(handItem.getOrCreateTag().getString("block"), new Block[0])) {
                                        i++;
                                        lines.add(Component.literal(String.valueOf(i)).append(":").append(block.getName()));
                                    }
                                    this.gui.getModularUIGui().setHoverTooltip(lines, ItemStack.EMPTY, null, null);
                                }
                            }
                        })
                        .addWidget(new BlockMapSelector(96, 4, 76, 12, (category, tier0) -> {
                            if (category != null && tier0 != null) {
                                var tag = handItem.getOrCreateTag();
                                tag.putString("block", category);
                                tag.putInt("tier", tier0);
                                handItem.setTag(tag);
                            }
                        }))
                        .addWidget(new TerminalInputWidget(140, 5 + 16 * rowIndex++, 20, 16, () -> getTier(handItem),
                                (v) -> setTier(v, handItem))
                                .setMin(0).setMax(100))
                        .addWidget(new LabelWidget(4, 5 + 16 * rowIndex, "item.gtmthings.advanced_terminal.setting.2")
                                .setHoverTooltips(Component.translatable("item.gtmthings.advanced_terminal.setting.2.tooltip")))
                        .addWidget(new TerminalInputWidget(140, 5 + 16 * rowIndex++, 20, 16, () -> getRepeatCount(handItem),
                                (v) -> setRepeatCount(v, handItem))
                                .setMin(0).setMax(1000))
                        .addWidget(new LabelWidget(4, 5 + 16 * rowIndex, "item.gtmthings.advanced_terminal.setting.3")
                                .setHoverTooltips("item.gtmthings.advanced_terminal.setting.3.tooltip"))
                        .addWidget(new TerminalInputWidget(140, 5 + 16 * rowIndex++, 20, 16, () -> getIsBuildHatches(handItem),
                                (v) -> setIsBuildHatches(v, handItem)).setMin(0).setMax(1))
                        .addWidget(new LabelWidget(4, 5 + 16 * rowIndex, REPLACE)
                                .setHoverTooltips(REPLACE_A))
                        .addWidget(new TerminalInputWidget(140, 5 + 16 * rowIndex++, 20, 16, () -> getReplaceMode(handItem),
                                (v) -> setReplaceMode(v, handItem)).setMin(0).setMax(1))
                        .addWidget(new LabelWidget(4, 5 + 16 * rowIndex, "item.gtmthings.advanced_terminal.setting.5")
                                .setHoverTooltips("item.gtmthings.advanced_terminal.setting.5.tooltip"))
                        .addWidget(new TerminalInputWidget(140, 5 + 16 * rowIndex++, 20, 16, () -> getIsUseAE(handItem),
                                (v) -> setIsUseAE(v, handItem)).setMin(0).setMax(1))
                        .addWidget(new LabelWidget(4, 5 + 16 * rowIndex, FLIP))
                        .addWidget(new TerminalInputWidget(140, 5 + 16 * rowIndex++, 20, 16, () -> getIsFlip(handItem),
                                (v) -> setIsFlip(v, handItem)).setMin(0).setMax(1))
                        .addWidget(new LabelWidget(4, 5 + 16 * rowIndex, MODULE))
                        .addWidget(new TerminalInputWidget(140, 5 + 16 * rowIndex++, 20, 16, () -> getModule(handItem),
                                (v) -> setModule(v, handItem)).setMin(0).setMax(100)));

        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private static AutoBuildSetting getAutoBuildSetting(ItemStack itemStack) {
        AutoBuildSetting autoBuildSetting = new AutoBuildSetting();
        var tag = itemStack.getTag();
        if (tag != null && !tag.isEmpty()) {
            autoBuildSetting.tier = tag.getInt("tier");
            autoBuildSetting.repeatCount = tag.getInt("repeatCount");
            autoBuildSetting.noHatchMode = tag.getInt("noHatchMode");
            autoBuildSetting.replaceMode = tag.getInt("replaceMode");
            autoBuildSetting.isUseAE = tag.getInt("isUseAE");
            autoBuildSetting.isFlip = tag.getInt("isFlip");
            autoBuildSetting.module = tag.getInt("module");
            var block = tag.getString("block");
            if (!block.isEmpty()) {
                autoBuildSetting.tierBlock = BlockMap.MAP.get(block);
                autoBuildSetting.blocks = new ReferenceOpenHashSet<>(autoBuildSetting.tierBlock);
            }
        }
        return autoBuildSetting;
    }

    private static int getTier(ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag != null && !tag.isEmpty()) {
            return tag.getInt("tier");
        } else {
            return 0;
        }
    }

    private static void setTier(int coilTier, ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag == null) tag = new CompoundTag();
        tag.putInt("tier", coilTier);
        itemStack.setTag(tag);
    }

    private static int getRepeatCount(ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag != null && !tag.isEmpty()) {
            return tag.getInt("repeatCount");
        } else {
            return 0;
        }
    }

    private static void setRepeatCount(int repeatCount, ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag == null) tag = new CompoundTag();
        tag.putInt("repeatCount", repeatCount);
        itemStack.setTag(tag);
    }

    private static int getIsBuildHatches(ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag != null && !tag.isEmpty()) {
            return tag.getInt("noHatchMode");
        } else {
            return 1;
        }
    }

    private static void setIsBuildHatches(int isBuildHatches, ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag == null) tag = new CompoundTag();
        tag.putInt("noHatchMode", isBuildHatches);
        itemStack.setTag(tag);
    }

    private static int getReplaceMode(ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag != null && !tag.isEmpty()) {
            return tag.getInt("replaceMode");
        } else {
            return 0;
        }
    }

    private static void setReplaceMode(int isReplaceCoil, ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag == null) tag = new CompoundTag();
        tag.putInt("replaceMode", isReplaceCoil);
        itemStack.setTag(tag);
    }

    private static int getIsUseAE(ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag != null && !tag.isEmpty()) {
            return tag.getInt("isUseAE");
        } else {
            return 0;
        }
    }

    private static void setIsUseAE(int isUseAE, ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag == null) tag = new CompoundTag();
        tag.putInt("isUseAE", isUseAE);
        itemStack.setTag(tag);
    }

    private static int getIsFlip(ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag != null && !tag.isEmpty()) {
            return tag.getInt("isFlip");
        } else {
            return 0;
        }
    }

    private static void setIsFlip(int isFlip, ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag == null) tag = new CompoundTag();
        tag.putInt("isFlip", isFlip);
        itemStack.setTag(tag);
    }

    private static int getModule(ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag != null && !tag.isEmpty()) {
            return tag.getInt("module");
        } else {
            return 0;
        }
    }

    private static void setModule(int isFlip, ItemStack itemStack) {
        var tag = itemStack.getTag();
        if (tag == null) tag = new CompoundTag();
        tag.putInt("module", isFlip);
        itemStack.setTag(tag);
    }

    static class AutoBuildSetting {

        Block[] tierBlock;
        Set<Block> blocks = Collections.emptySet();

        int tier, repeatCount, noHatchMode, replaceMode, isUseAE, isFlip, module;

        private AutoBuildSetting() {
            this.tier = 0;
            this.repeatCount = 0;
            this.noHatchMode = 1;
            this.replaceMode = 0;
            this.isUseAE = 0;
            this.isFlip = 0;
            this.module = 0;
        }

        public List<AEKey> apply(Block[] blocks) {
            List<AEKey> candidates = new ArrayList<>();
            if (blocks != null) {
                for (Block block : blocks) {
                    if (tierBlock != null && tier > 0 && blocks.length > 1 && this.blocks.contains(block)) {
                        candidates.add(AEItemKey.of(tierBlock[Math.min(tierBlock.length, tier) - 1].asItem()));
                        return candidates;
                    } else if (block instanceof LiquidBlock fluid) {
                        var realFluid = fluid.getFluid().getSource();
                        candidates.add(AEFluidKey.of(realFluid));
                    } else if (block != Blocks.AIR) {
                        candidates.add(AEItemKey.of(SimplePredicate.toItem(block)));
                    }
                }
            }
            return candidates;
        }

        boolean isPlaceHatch(Block[] blocks) {
            if (this.noHatchMode == 0) return true;
            if (blocks != null && blocks.length > 1) {
                var block = blocks[0];
                return !(block instanceof MetaMachineBlock machineBlock) || !Hatch.Set.contains(machineBlock);
            }
            return true;
        }

        boolean isReplaceMode() {
            return replaceMode == 1;
        }
    }
}
