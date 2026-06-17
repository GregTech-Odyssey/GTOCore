package com.gtocore.common.item;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.GTOCore;
import com.gtolib.api.pattern.DebugBlockPattern;
import com.gtolib.utils.*;
import com.gtolib.utils.iostream.IOStreamCodec;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public final class StructureWriteBehavior implements IItemUIFactory {

    public static final StructureWriteBehavior INSTANCE = new StructureWriteBehavior();

    private static final String EXPORT_MBS_FILE = "structure_pattern.mbs";
    private static final String EXPORT_TEXT_FILE = "structure_pattern.txt";

    private static Map<Block, BiConsumer<StringBuilder, Character>> SPECIAL_BLOCK_WRITERS;

    private record ExportContext(
                                 ServerPlayer player,
                                 ItemStack stack,
                                 String partId,
                                 Block partBlock,
                                 DebugBlockPattern pattern,
                                 RelativeDirection[] directions) {}

    @Override
    public ModularUI createUI(HeldItemUIFactory.HeldItemHolder playerInventoryHolder, Player entityPlayer) {
        var container = new WidgetGroup(8, 8, 160, 54);
        container.addWidget(new ImageWidget(4, 4, 152, 46, GuiTextures.DISPLAY))
                .addWidget(new LabelWidget(7, 7, () -> {
                    int x = 0;
                    int y = 0;
                    int z = 0;
                    if (getPos(playerInventoryHolder.getHeld()) != null) {
                        BlockPos[] blockPos = getPos(playerInventoryHolder.getHeld());
                        if (blockPos != null) {
                            x = 1 + blockPos[1].getX() - blockPos[0].getX();
                            y = 1 + blockPos[1].getY() - blockPos[0].getY();
                            z = 1 + blockPos[1].getZ() - blockPos[0].getZ();
                        }
                    }
                    return LocalizationUtils.format("structure_writer.structural_scale", x, y, z);
                }).setTextColor(0xFAF9F6)).addWidget(new LabelWidget(7, 20, () -> {
                    var direction = getDir(playerInventoryHolder.getHeld());
                    var dirs = DebugBlockPattern.getDir(direction);
                    return LocalizationUtils.format("structure_writer.export_order", dirs[0].name(), dirs[1].name(), dirs[2].name());
                }).setTextColor(0xFAF9F6));
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return new ModularUI(176, 120, playerInventoryHolder, entityPlayer)
                .background(GuiTextures.BACKGROUND)
                .widget(container)
                .widget(new ButtonWidget(9, 91, 77, 20, new GuiTextureGroup(GuiTextures.BUTTON, new TextTexture(playerInventoryHolder.getHeld().getOrCreateTag().getBoolean("export") ? "导出模式" : "绑定模式")),
                        clickData -> switchMode(playerInventoryHolder)))
                .widget(new ButtonWidget(90, 91, 77, 20, new GuiTextureGroup(GuiTextures.BUTTON, new TextTexture("导出为日志")),
                        clickData -> exportLog(playerInventoryHolder)))
                .widget(new ButtonWidget(9, 68, 77, 20, new GuiTextureGroup(GuiTextures.BUTTON, new TextTexture("沿X轴旋转")),
                        clickData -> changeDirX(playerInventoryHolder)))
                .widget(new ButtonWidget(90, 68, 77, 20, new GuiTextureGroup(GuiTextures.BUTTON, new TextTexture("沿Y轴旋转")),
                        clickData -> changeDirY(playerInventoryHolder)));
    }

    private static void exportLog(HeldItemUIFactory.HeldItemHolder playerInventoryHolder) {
        if (!(playerInventoryHolder.getPlayer() instanceof ServerPlayer player)) return;
        var context = createExportContext(playerInventoryHolder, player);
        if (context == null) return;

        var generatedCode = buildGeneratedPatternCode(context);
        writeExportFiles(context, generatedCode);
        logGeneratedPattern(context, generatedCode);
    }

    private static ExportContext createExportContext(HeldItemUIFactory.HeldItemHolder holder, ServerPlayer player) {
        BlockPos[] bounds = getPos(holder.getHeld());
        if (bounds == null) return null;

        ItemStack stack = holder.getHeld();
        String partId = stack.getOrCreateTag().getString("part");
        if (partId.isEmpty()) {
            player.displayClientMessage(Component.literal("未绑定仓室方块"), false);
            return null;
        }

        Direction direction = getDir(stack);
        RelativeDirection[] directions = DebugBlockPattern.getDir(direction);
        DebugBlockPattern pattern = new DebugBlockPattern(
                holder.getPlayer().level(),
                bounds[0].getX(),
                bounds[0].getY(),
                bounds[0].getZ(),
                bounds[1].getX(),
                bounds[1].getY(),
                bounds[1].getZ());
        pattern.changeDir(directions[0], directions[1], directions[2]);
        return new ExportContext(player, stack, partId, RegistriesUtils.getBlock(partId), pattern, directions);
    }

    private static String buildGeneratedPatternCode(ExportContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n.block(").append(convertBlockToString(context.partBlock(), context.partId(), StringUtils.decompose(context.partId()), true)).append(")\n");
        builder.append(".pattern(definition -> MultiBlockFileReader.start(definition)\n");
        context.pattern().legend.forEach((block, character) -> appendWhereClause(builder, context, block, character));
        if (context.pattern().hasAir) builder.append(".where(' ', any())\n");
        builder.append(".build())\n");
        return builder.toString();
    }

    private static void appendWhereClause(StringBuilder builder, ExportContext context, Block block, Character character) {
        if (character.equals(' ')) return;
        var specialWriters = specialBlockWriters();
        if (specialWriters.containsKey(block)) {
            builder.append(".where('").append(character).append("', ");
            specialWriters.get(block).accept(builder, character);
            builder.append("\n");
            return;
        }
        if (block == net.minecraft.world.level.block.Blocks.COBBLESTONE) {
            appendMachinePartPredicate(builder, context, character);
            return;
        }
        appendBlockPredicate(builder, block, character);
    }

    private static void appendMachinePartPredicate(StringBuilder builder, ExportContext context, Character character) {
        builder.append(".where('").append(character).append("', blocks(")
                .append(convertBlockToString(context.partBlock(), context.partId(), StringUtils.decompose(context.partId()), false))
                .append(")\n")
                .append(context.stack().getOrCreateTag().getBoolean("laser") ? ".or(GTOPredicates.autoLaserAbilities(definition.getRecipeTypes()))\n.or(abilities(MAINTENANCE).setExactLimit(1)))\n" : ".or(autoAbilities(definition.getRecipeTypes()))\n.or(abilities(MAINTENANCE).setExactLimit(1)))\n");
    }

    private static void appendBlockPredicate(StringBuilder builder, Block block, Character character) {
        String id = ItemUtils.getId(block);
        String[] parts = StringUtils.decompose(id);
        boolean isGT = Objects.equals(parts[0], "gtceu");
        boolean isGTO = Objects.equals(parts[0], GTOCore.MOD_ID);
        if ((isGT || isGTO) && parts[1].contains("_frame")) {
            builder.append(".where('").append(character).append("', GTOPredicates.frame(").append(isGT ? "GTMaterials." : "GTOMaterials.")
                    .append(FormattingUtil.lowerUnderscoreToUpperCamel(StringUtils.lastDecompose('_', parts[1])[0])).append("))\n");
            return;
        }
        builder.append(".where('").append(character).append("', blocks(")
                .append(convertBlockToString(block, id, parts, false)).append("))\n");
    }

    private static void writeExportFiles(ExportContext context, String generatedCode) {
        var directions = context.directions();
        MultiBlockFileReader.save(new File(GTOCore.getFile(), EXPORT_MBS_FILE), context.pattern().pattern, directions[0], directions[1], directions[2]);
        FileUtils.saveToFile(generatedCode, new File(GTOCore.getFile(), EXPORT_TEXT_FILE), IOStreamCodec.STRING_CODEC);
    }

    private static void logGeneratedPattern(ExportContext context, String generatedCode) {
        StringBuilder log = new StringBuilder(generatedCode);
        for (String[] strings : context.pattern().pattern) {
            log.append(".aisle(\"%s\")\n".formatted(Joiner.on("\", \"").join(strings)));
        }
        GTOCore.LOGGER.info(log.toString());
    }

    private static Map<Block, BiConsumer<StringBuilder, Character>> specialBlockWriters() {
        if (SPECIAL_BLOCK_WRITERS == null) {
            SPECIAL_BLOCK_WRITERS = ImmutableMap.<Block, BiConsumer<StringBuilder, Character>>builder()
                    .put(net.minecraft.world.level.block.Blocks.OAK_LOG, (b, c) -> b.append("controller(definition))"))
                    .put(net.minecraft.world.level.block.Blocks.DIRT, (b, c) -> b.append("heatingCoils())"))
                    .put(net.minecraft.world.level.block.Blocks.WHITE_WOOL, (b, c) -> b.append("air())"))
                    .put(net.minecraft.world.level.block.Blocks.GLASS, (b, c) -> b.append("GTOPredicates.glass())"))
                    .put(net.minecraft.world.level.block.Blocks.GLOWSTONE, (b, c) -> b.append("GTOPredicates.light())"))
                    .put(GTOBlocks.ABS_WHITE_CASING.get(), (b, c) -> b.append("GTOPredicates.absBlocks())"))
                    .put(net.minecraft.world.level.block.Blocks.FURNACE, (b, c) -> b.append("abilities(MUFFLER))"))
                    .build();
        }
        return SPECIAL_BLOCK_WRITERS;
    }

    private static String convertBlockToString(Block b, String id, String[] parts, boolean supplier) {
        if (StringIndex.BLOCK_LINK_MAP.containsKey(b)) {
            return StringIndex.BLOCK_LINK_MAP.get(b) + (supplier ? "" : ".get()");
        }
        if (Objects.equals(parts[0], GTOCore.MOD_ID)) {
            return "Blocks." + parts[1].toUpperCase() + (supplier ? "" : ".get()");
        }
        if (Objects.equals(parts[0], "minecraft")) {
            return (supplier ? "() -> " : "") + "Blocks." + parts[1].toUpperCase();
        }
        return "RegistriesUtils.get" + (supplier ? "Supplier" : "") + "Block(\"" + id + "\")";
    }

    private static void switchMode(HeldItemUIFactory.HeldItemHolder playerInventoryHolder) {
        if (playerInventoryHolder.getPlayer() instanceof ServerPlayer) {
            ItemStack itemStack = playerInventoryHolder.getHeld();
            itemStack.getOrCreateTag().putBoolean("export", !itemStack.getOrCreateTag().getBoolean("export"));
        }
    }

    private static void changeDirX(HeldItemUIFactory.HeldItemHolder playerInventoryHolder) {
        if (getPos(playerInventoryHolder.getHeld()) != null &&
                playerInventoryHolder.getPlayer() instanceof ServerPlayer) {
            ItemStack itemStack = playerInventoryHolder.getHeld();
            Direction direction = getDir(itemStack);
            direction = direction.getClockWise(Direction.Axis.X);
            setDir(itemStack, direction);
        }
    }

    private static void changeDirY(HeldItemUIFactory.HeldItemHolder playerInventoryHolder) {
        if (getPos(playerInventoryHolder.getHeld()) != null &&
                playerInventoryHolder.getPlayer() instanceof ServerPlayer) {
            ItemStack itemStack = playerInventoryHolder.getHeld();
            Direction direction = getDir(itemStack);
            direction = direction.getClockWise(Direction.Axis.Y);
            setDir(itemStack, direction);
        }
    }

    public static boolean isItem(ItemStack stack) {
        if (stack.isEmpty()) return false;

        if (stack.getItem() instanceof ComponentItem item) {
            return item.getComponents().contains(INSTANCE);
        }
        return false;
    }

    private static Direction getDir(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTagElement("structure_writer");
        if (!tag.contains("dir")) return Direction.WEST;
        return Direction.byName(tag.getString("dir"));
    }

    private static void setDir(ItemStack stack, Direction dir) {
        CompoundTag tag = stack.getOrCreateTagElement("structure_writer");
        tag.putString("dir", dir.getName());
    }

    public static BlockPos[] getPos(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTagElement("structure_writer");
        if (!tag.contains("minX")) return null;
        return new BlockPos[] {
                new BlockPos(tag.getInt("minX"), tag.getInt("minY"), tag.getInt("minZ")),
                new BlockPos(tag.getInt("maxX"), tag.getInt("maxY"), tag.getInt("maxZ"))
        };
    }

    private static void addPos(ItemStack stack, BlockPos pos) {
        CompoundTag tag = stack.getOrCreateTagElement("structure_writer");
        if (!tag.contains("minX") || tag.getInt("minX") > pos.getX()) {
            tag.putInt("minX", pos.getX());
        }
        if (!tag.contains("maxX") || tag.getInt("maxX") < pos.getX()) {
            tag.putInt("maxX", pos.getX());
        }

        if (!tag.contains("minY") || tag.getInt("minY") > pos.getY()) {
            tag.putInt("minY", pos.getY());
        }
        if (!tag.contains("maxY") || tag.getInt("maxY") < pos.getY()) {
            tag.putInt("maxY", pos.getY());
        }

        if (!tag.contains("minZ") || tag.getInt("minZ") > pos.getZ()) {
            tag.putInt("minZ", pos.getZ());
        }
        if (!tag.contains("maxZ") || tag.getInt("maxZ") < pos.getZ()) {
            tag.putInt("maxZ", pos.getZ());
        }
    }

    private static void removePos(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTagElement("structure_writer");
        tag.remove("minX");
        tag.remove("maxX");
        tag.remove("minY");
        tag.remove("maxY");
        tag.remove("minZ");
        tag.remove("maxZ");
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        var player = context.getPlayer();
        if (player == null) return InteractionResult.SUCCESS;
        ItemStack stack = player.getItemInHand(context.getHand());
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.getBoolean("export")) {
            if (!player.isShiftKeyDown()) {
                addPos(stack, context.getClickedPos());
            } else {
                removePos(stack);
            }
        } else if (player.isShiftKeyDown()) {
            boolean l = !itemStack.getOrCreateTag().getBoolean("laser");
            player.displayClientMessage(Component.literal("导出到").append(l ? "激光" : "普通").append("机器"), true);
            itemStack.getOrCreateTag().putBoolean("laser", l);
        } else {
            Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
            player.displayClientMessage(Component.literal("已设置 ").append(block.getName()).append(" 为仓室方块"), true);
            tag.putString("part", ItemUtils.getId(block));
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.isShiftKeyDown()) {
            removePos(stack);
        } else {
            if (player instanceof ServerPlayer serverPlayer) {
                HeldItemUIFactory.INSTANCE.openUI(serverPlayer, usedHand);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
