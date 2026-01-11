package com.gtocore.eio_travel.client.travel;

import com.gtocore.common.data.GTOMachines;
import com.gtocore.eio_travel.AbstractTravelTarget;
import com.gtocore.eio_travel.ITravelTarget;
import com.gtocore.eio_travel.api.TravelHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class TravelAnchorHud implements IGuiOverlay {

    public static final TravelAnchorHud INSTANCE = new TravelAnchorHud();

    static final int CURSOR_GAP = 20;
    static final int ITEM_TEXT_GAP = 6;
    static final int ITEM_SIZE = 16;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = gui.getMinecraft();
        Player player = minecraft.player;

        if (player == null || !TravelHandler.canBlockTeleport(player)) {
            return;
        }

        TravelHandler.getElevatorAnchorTarget(player, Direction.UP)
                .ifPresent(target -> showElevatorTarget(guiGraphics, minecraft.font, screenWidth, screenHeight, target, Direction.UP));

        TravelHandler.getElevatorAnchorTarget(player, Direction.DOWN)
                .ifPresent(target -> showElevatorTarget(guiGraphics, minecraft.font, screenWidth, screenHeight, target, Direction.DOWN));
    }

    private static void showElevatorTarget(GuiGraphics guiGraphics, Font font, int screenWidth, int screenHeight, ITravelTarget target, Direction direction) {
        String txt = switch (direction) {
            case UP -> "↑";
            case DOWN -> "↓";
            case EAST -> "→";
            case WEST -> "←";
            default -> "";
        };

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        if (target instanceof AbstractTravelTarget anchorTarget) {
            String anchorName = anchorTarget.getName();
            if (!anchorName.isEmpty()) {
                txt = anchorName + " " + txt;
            }

            // Draw icon as an item
            Item icon = anchorTarget.getIcon();
            if (icon == Blocks.AIR.asItem()) {
                icon = GTOMachines.TRAVEL_ANCHOR.asItem();
            }
            ItemStack itemStack = icon.getDefaultInstance();
            int x = centerX + getOffset(CURSOR_GAP, ITEM_SIZE, direction.getStepX());
            int y = centerY + getOffset(CURSOR_GAP, ITEM_SIZE, -direction.getStepY());

            guiGraphics.renderItem(itemStack, x, y);
        }

        int textWidth = font.width(txt);
        int offsetAmount = CURSOR_GAP + ITEM_SIZE + ITEM_TEXT_GAP;
        int textX = centerX + getOffset(offsetAmount, textWidth, direction.getStepX());
        int textY = centerY + getOffset(offsetAmount, font.lineHeight, -direction.getStepY());

        // Text background
        int bgFromX = textX - 2;
        int bgFromY = textY - 2;
        int bgToX = bgFromX + textWidth + 3;
        int bgToY = bgFromY + font.lineHeight + 3;
        guiGraphics.fill(bgFromX, bgFromY, bgToX, bgToY, 0x87000000);

        // Text
        guiGraphics.drawString(font, txt, textX, textY, 16777215, false);
    }

    private static int getOffset(int offsetAmount, int size, int direction) {
        return (offsetAmount + size / 2) * direction - (size / 2);
    }
}
