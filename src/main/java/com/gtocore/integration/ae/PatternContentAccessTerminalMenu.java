package com.gtocore.integration.ae;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantic;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.slot.FakeSlot;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;

public class PatternContentAccessTerminalMenu extends AEBaseMenu {

    private final int Rows = 32;
    public final FakeSlot[] TargetAEKeys = new FakeSlot[Rows * 9];

    public static final MenuType<PatternContentAccessTerminalMenu> TYPE = MenuTypeBuilder
            .create(PatternContentAccessTerminalMenu::new, PatternContentAccessTerminalPart.class)
            .build("patterncontentaccessterminal");

    public PatternContentAccessTerminalMenu(int id, Inventory ip, PatternContentAccessTerminalPart part) {
        super(TYPE, id, ip, part);
        for (int i = 0; i < Rows * 9; i++) {
            TargetAEKeys[i] = new FakeSlot(part.getConfig().createMenuWrapper(), i);
            TargetAEKeys[i].x = 8 + (i % 9) * 18;
            TargetAEKeys[i].y = 18 + (i / 9) * 18;
            TargetAEKeys[i].setEmptyTooltip(() -> Collections.singletonList(Component.translatable(PatternContentAccessTerminalScreen.REPLACEMENT_LIST_TOOLTIP).withStyle(ChatFormatting.GRAY)));
            SlotSemantic semantics;
            if (i % 9 == 0) {
                semantics = SlotSemantics.PROCESSING_OUTPUTS;
            } else {
                semantics = SlotSemantics.PROCESSING_INPUTS;
            }
            this.addSlot(TargetAEKeys[i], semantics);

        }
        createPlayerInventorySlots(ip);
    }

    public int getFirstItemAtRow(int row) {
        var targetSlots = ArrayUtils.subarray(TargetAEKeys, row * 9, (row + 1) * 9);
        for (int i = 0; i < targetSlots.length; i++) {
            if (!targetSlots[i].getDisplayStack().isEmpty()) {
                return i;
            }
        }
        return -1;
    }
}
