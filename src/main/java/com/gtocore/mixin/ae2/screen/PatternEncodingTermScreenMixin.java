package com.gtocore.mixin.ae2.screen;

import com.gtocore.integration.ae.client.AESearchPatternProviderListBox;
import com.gtocore.integration.ae.hooks.IExtendedPatternEncodingTerm;

import com.gtolib.api.ae2.gui.FixedRepoSlot;
import com.gtolib.api.ae2.gui.hooks.IAEBaseScreenLifecycle;
import com.gtolib.api.ae2.gui.hooks.IFixedRepoSlotHandlingScreen;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import appeng.api.stacks.AEItemKey;
import appeng.client.gui.Icon;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ActionButton;
import appeng.core.definitions.AEItems;
import appeng.menu.SlotSemantics;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import com.llamalad7.mixinextras.sugar.Local;
import earth.terrarium.adastra.mixins.common.SlotAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PatternEncodingTermScreen.class)
public class PatternEncodingTermScreenMixin<C extends PatternEncodingTermMenu> extends MEStorageScreen<C> implements IAEBaseScreenLifecycle, IExtendedPatternEncodingTerm {

    @Unique
    private FixedRepoSlot gtolib$fixedSlot;
    @Unique
    private AESearchPatternProviderListBox gto$listBox;
    @Unique
    private ActionButton gto$encodeButton;

    public PatternEncodingTermScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$onInit(PatternEncodingTermMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci, @Local(name = "encodeBtn") ActionButton encodeBtn) {
        gtolib$fixedSlot = new FixedRepoSlot(((IFixedRepoSlotHandlingScreen) this).gtolib$getRepo(), AEItemKey.of(AEItems.BLANK_PATTERN), 0, 0);
        gtolib$fixedSlot.setEmptyTooltipMessage(
                List.of(Component.translatable("ldlib.gui.editor.name.canPutItems")
                        .append(AEItemKey.of(AEItems.BLANK_PATTERN).getDisplayName()).withStyle(ChatFormatting.AQUA)));
        gtolib$fixedSlot.setIcon(Icon.BACKGROUND_BLANK_PATTERN);

        gto$listBox = new AESearchPatternProviderListBox(this);
        gto$listBox.setVisible(false);
        gto$listBox.setCatchScrollbar(false);
        widgets.add("gto$listBox", gto$listBox);

        gto$encodeButton = encodeBtn;
    }

    @Override
    public ActionButton gto$getEncodeButton() {
        return gto$encodeButton;
    }

    @Override
    public void gtolib$initBeforePositionSlot() {
        ((IFixedRepoSlotHandlingScreen) this).gtolib$getMenu().slots.remove(gtolib$fixedSlot);
    }

    @Override
    public void gtolib$initBeforeWidgetsInitialized() {
        List<Slot> slots = ((IFixedRepoSlotHandlingScreen) this).gtolib$getMenu().getSlots(SlotSemantics.BLANK_PATTERN);
        if (!slots.isEmpty() && slots.getFirst() instanceof RestrictedInputSlot slot) {
            ((SlotAccessor) gtolib$fixedSlot).setX(slot.x);
            ((SlotAccessor) gtolib$fixedSlot).setY(slot.y);
            slot.setActive(false);
            ((IFixedRepoSlotHandlingScreen) this).gtolib$getMenu().slots.add(gtolib$fixedSlot);
        }
    }

    @Override
    public AESearchPatternProviderListBox gto$getPatternDestDisplay() {
        return gto$listBox;
    }

    @Override
    public IExtendedPatternEncodingTerm.Menu gto$getMenu() {
        return (Menu) menu;
    }
}
