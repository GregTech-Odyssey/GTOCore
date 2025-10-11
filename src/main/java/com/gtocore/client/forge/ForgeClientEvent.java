package com.gtocore.client.forge;

import com.gtocore.client.ClientCache;
import com.gtocore.client.GTOClientCommands;
import com.gtocore.client.Tooltips;
import com.gtocore.client.renderer.RenderHelper;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.item.StructureDetectBehavior;
import com.gtocore.common.item.StructureWriteBehavior;
import com.gtocore.common.network.ClientMessage;

import com.gtolib.GTOCore;
import com.gtolib.IItem;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.utils.collection.O2IOpenCacheHashMap;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.hepdd.gtmthings.common.block.machine.electric.WirelessEnergyMonitor;
import com.hepdd.gtmthings.data.CustomItems;
import com.mojang.blaze3d.vertex.PoseStack;
import snownee.jade.util.Color;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public final class ForgeClientEvent {

    public static int highlightingTime = 0;
    public static int highlightingRadius;
    public static BlockPos highlightingPos;
    private static boolean lastShiftState = false;
    public static final O2IOpenCacheHashMap<HighlightNeed> CUstomHighlightNeeds = new O2IOpenCacheHashMap<>();

    private static final String ITEM_PREFIX = "item." + GTOCore.MOD_ID;
    private static final String BLOCK_PREFIX = "block." + GTOCore.MOD_ID;

    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (highlightingTime > 0) {
                highlightingTime--;
            }
            if (ClientCache.highlightTime > 0) {
                ClientCache.highlightTime--;
            }
            CUstomHighlightNeeds.object2IntEntrySet().fastForEach(
                    entry -> {
                        int time = entry.getIntValue();
                        if (time > 0) {
                            CUstomHighlightNeeds.put(entry.getKey(), --time);
                        } else {
                            CUstomHighlightNeeds.removeInt(entry.getKey());
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null) return;
        ItemStack stack = event.getItemStack();
        String translationKey = stack.getDescriptionId();
        if (translationKey.startsWith(ITEM_PREFIX) || translationKey.startsWith(BLOCK_PREFIX)) {
            String tooltipKey = translationKey + ".tooltip";
            if (I18n.exists(tooltipKey)) {
                event.getToolTip().add(1, Component.translatable(tooltipKey));
            }
        }
        Item item = stack.getItem();
        var arr = ((IItem) item).gtolib$getToolTips();
        if (arr != null) {
            for (int i = arr.length - 1; i >= 0; i--) {
                event.getToolTip().add(1, arr[i].get());
            }
        }
        var lang = Tooltips.TOOL_TIPS_MAP.get(item);
        if (lang != null) {
            for (int i = 0; i < lang.length(); i++) {
                event.getToolTip().add(Component.translatable("gtocore.tooltip.item." + ItemUtils.getIdLocation(item).getPath() + "." + i));
            }
        } else {
            String[] tooltips = Tooltips.TOOL_TIPS_KEY_MAP.get(item);
            if (tooltips != null) {
                if (tooltips.length == 1) event.getToolTip().add(Component.translatable(tooltips[0]));
                else event.getToolTip().add(Component.translatable(tooltips[0], tooltips[1]));
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (Minecraft.getInstance().player instanceof IEnhancedPlayer) {
            boolean isShiftDown = Screen.hasShiftDown();
            if (isShiftDown != lastShiftState) {
                ClientMessage.send("shiftKeypress", buf -> buf.writeBoolean(isShiftDown));
                lastShiftState = isShiftDown;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelStageEvent event) {
        RenderLevelStageEvent.Stage stage = event.getStage();
        if (stage == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            LocalPlayer player = mc.player;
            if (level == null || player == null) return;
            PoseStack poseStack = event.getPoseStack();
            Camera camera = event.getCamera();
            BlockPos[] poses;
            if (highlightingTime > 0) {
                RenderHelper.highlightSphere(camera, poseStack, highlightingPos, highlightingRadius);
            }
            if (WirelessEnergyMonitor.p > 0) {
                if (GTValues.CLIENT_TIME % 20L == 0L) {
                    --WirelessEnergyMonitor.p;
                }
                BlockPos pose = WirelessEnergyMonitor.pPos;
                if (pose == null) {
                    return;
                }
                RenderHelper.highlightBlock(camera, poseStack, 0, 0, 1, pose, pose);
            }
            ItemStack itemStack = player.getMainHandItem();
            Item item = itemStack.getItem();
            if (item != Items.AIR && itemStack.hasTag()) {
                if (GTCEu.isDev() && StructureWriteBehavior.isItem(itemStack)) {
                    poses = StructureWriteBehavior.getPos(itemStack);
                    if (poses != null) {
                        RenderHelper.highlightBlock(camera, poseStack, 0, 0, 1, poses);
                    }
                } else if (StructureDetectBehavior.isItem(itemStack)) {
                    poses = StructureDetectBehavior.getPos(itemStack);
                    if (poses != null && poses.length >= 1) {
                        for (var pos : poses) {
                            if (pos == null) continue;
                            RenderHelper.highlightBlock(camera, poseStack, 0, 0, 1, pos, pos);
                        }
                    }
                } else if (Highlighting.HIGHLIGHTING_ITEM.contains(item)) {
                    var tag = itemStack.getTag();
                    BlockPos blockPos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
                    RenderHelper.highlightBlock(camera, poseStack, 0, 0, 1, blockPos, blockPos);
                }
            }
            CUstomHighlightNeeds.object2IntEntrySet().fastForEach(
                    entry -> {
                        Color color = Color.rgb(entry.getKey().color);
                        RenderHelper.highlightBlock(camera, poseStack, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, entry.getKey().start, entry.getKey().end);
                    });
            if (ClientCache.machineNotFormedHighlight) {
                MultiblockControllerMachine.HIGHLIGHT_CACHE.forEach(p -> {
                    var pos = BlockPos.of(p);
                    RenderHelper.highlightBlock(camera, poseStack, 1, 0.1f, 0.1f, pos, pos);
                });
            }
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent evt) {
        GTOClientCommands.init(evt.getDispatcher());
    }

    /**
     * 高亮指定维度内两个坐标点之间的立方体区域
     * 
     * @param dimension     目标维度
     * @param start         立方体对角点1
     * @param end           立方体对角点2
     * @param color         高亮颜色 (ARGB)
     * @param durationTicks 持续时间(tick)，20 tick = 1秒，默认 1200 tick = 60秒
     */
    public static void highlightRegion(ResourceKey<Level> dimension, BlockPos start, BlockPos end, int color, int durationTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (mc.level.dimension() != dimension) return;
        ForgeClientEvent.HighlightNeed need = new ForgeClientEvent.HighlightNeed(start, end, color);
        CUstomHighlightNeeds.put(need, durationTicks);
    }

    /**
     * 手动解除某个区域的高亮
     */
    public static void stopHighlight(BlockPos start, BlockPos end) {
        CUstomHighlightNeeds.object2IntEntrySet().removeIf(entry -> entry.getKey().start.equals(start) && entry.getKey().end.equals(end));
    }

    private static class Highlighting {

        private static final Set<Item> HIGHLIGHTING_ITEM = Set.of(
                CustomItems.WIRELESS_ITEM_TRANSFER_COVER.asItem(),
                CustomItems.WIRELESS_FLUID_TRANSFER_COVER.asItem(),
                CustomItems.ADVANCED_WIRELESS_ITEM_TRANSFER_COVER.asItem(),
                CustomItems.ADVANCED_WIRELESS_FLUID_TRANSFER_COVER.asItem(),
                GTOItems.COORDINATE_CARD.asItem());
    }

    public record HighlightNeed(BlockPos start, BlockPos end, int color) {}
}
