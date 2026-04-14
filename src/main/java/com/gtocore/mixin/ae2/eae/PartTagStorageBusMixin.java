package com.gtocore.mixin.ae2.eae;

import com.gtocore.utils.Caches;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import appeng.api.parts.IPartItem;
import appeng.util.SettingsFrom;
import appeng.util.prioritylist.IPartitionList;

import com.glodblock.github.extendedae.common.parts.PartTagStorageBus;
import com.glodblock.github.extendedae.common.parts.base.PartSpecialStorageBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PartTagStorageBus.class)
public abstract class PartTagStorageBusMixin extends PartSpecialStorageBus {

    @Shadow(remap = false)
    private @NotNull String oreExpWhite;

    @Shadow(remap = false)
    private @NotNull String oreExpBlack;

    public PartTagStorageBusMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public void importSettings(SettingsFrom mode, CompoundTag input, @Nullable Player player) {
        super.importSettings(mode, input, player);
        String white, black;
        if (input.contains("ore_dict_exp")) {
            white = input.getString("ore_dict_exp");
        } else {
            white = "";
        }
        if (input.contains("ore_dict_exp_2")) {
            black = input.getString("ore_dict_exp_2");
        } else {
            black = "";
        }
        var flag = !black.equals(oreExpBlack) || !white.equals(oreExpWhite);
        this.oreExpWhite = white;
        this.oreExpBlack = black;
        if (flag) {
            this.filter = null;
            this.forceUpdate();
        }
    }

    @Override
    protected IPartitionList createFilter() {
        if (this.filter == null) {
            this.filter = Caches.getTagPriorityList(oreExpWhite, oreExpBlack);
        }
        return this.filter;
    }
}
