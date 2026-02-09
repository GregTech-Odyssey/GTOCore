package com.gtocore.integration.ae;

import com.gtocore.utils.AEKeySubstitutionMap;
import com.gtocore.utils.AEPatternRefresher;

import com.gtolib.GTOCore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.helpers.IConfigInvHost;
import appeng.helpers.externalstorage.GenericStackInv;
import appeng.items.parts.PartModels;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.parts.PartModel;
import appeng.parts.reporting.AbstractDisplayPart;
import appeng.util.ConfigInventory;

import java.util.ArrayList;
import java.util.List;

public class PatternContentAccessTerminalPart extends AbstractDisplayPart implements IConfigInvHost {

    @PartModels
    public static final ResourceLocation MODEL_OFF = GTOCore.id(
            "part/pattern_content_access_terminal_off");
    @PartModels
    public static final ResourceLocation MODEL_ON = GTOCore.id(
            "part/pattern_content_access_terminal_on");

    public static final IPartModel MODELS_OFF = new PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF);
    public static final IPartModel MODELS_ON = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON);
    public static final IPartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL);

    private final ConfigInventory config;

    private volatile AEKeySubstitutionMap substitutionMap = AEKeySubstitutionMap.EMPTY;

    public PatternContentAccessTerminalPart(IPartItem<?> partItem) {
        super(partItem, false);
        this.config = ConfigInventory.configTypes(32 * 9, this::onConfigChanged);
    }

    private void onConfigChanged() {
        updateSubstitutionMap();
        refreshPatterns();
        getHost().markForSave();
    }

    private void updateSubstitutionMap() {
        List<List<AEKey>> priorityGroups = new ArrayList<>();

        for (int row = 0; row < 32; row++) {
            List<AEKey> rowKeys = new ArrayList<>();
            for (int col = 0; col < 9; col++) {
                GenericStack stack = this.config.getStack(row * 9 + col);
                if (stack != null && stack.what() != null) {
                    rowKeys.add(stack.what());
                }
            }
            if (!rowKeys.isEmpty()) {
                priorityGroups.add(rowKeys);
            }
        }
        this.substitutionMap = new AEKeySubstitutionMap(priorityGroups);
    }

    public AEKey getReplacement(AEKey stack) {
        return this.substitutionMap.getSubstitution(stack);
    }

    public void refreshPatterns() {
        if (getMainNode() == null || !getMainNode().isOnline()) return;
        var grid = getMainNode().getGrid();
        if (grid != null) {
            AEPatternRefresher.refresh(grid);
        }
    }

    @Override
    public boolean onPartActivate(Player player, InteractionHand hand, Vec3 pos) {
        if (!super.onPartActivate(player, hand, pos) && !isClientSide()) {
            MenuOpener.open(PatternContentAccessTerminalMenu.TYPE, player, MenuLocators.forPart(this));
        }
        return true;
    }

    @Override
    public IPartModel getStaticModels() {
        return this.selectModel(MODELS_OFF, MODELS_ON, MODELS_HAS_CHANNEL);
    }

    @Override
    public GenericStackInv getConfig() {
        return this.config;
    }

    @Override
    public void writeToNBT(CompoundTag data) {
        super.writeToNBT(data);
        this.config.writeToChildTag(data, "AEKeys");
    }

    @Override
    public void readFromNBT(CompoundTag data) {
        super.readFromNBT(data);
        this.config.readFromChildTag(data, "AEKeys");
        updateSubstitutionMap();
    }
}
