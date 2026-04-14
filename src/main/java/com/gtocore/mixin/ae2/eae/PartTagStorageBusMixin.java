package com.gtocore.mixin.ae2.eae;

import com.gtolib.api.misc.IMapValueCache;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import appeng.api.parts.IPartItem;
import appeng.util.SettingsFrom;
import appeng.util.prioritylist.IPartitionList;

import com.glodblock.github.extendedae.common.me.taglist.TagPriorityList;
import com.glodblock.github.extendedae.common.parts.PartTagStorageBus;
import com.glodblock.github.extendedae.common.parts.base.PartSpecialStorageBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.regex.Pattern;

@Mixin(PartTagStorageBus.class)
public abstract class PartTagStorageBusMixin extends PartSpecialStorageBus {

    @Shadow(remap = false)
    private @NotNull String oreExpWhite;

    @Shadow(remap = false)
    private @NotNull String oreExpBlack;

    @Unique
    private static final Pattern gtocore$whiteBlackPattern = Pattern.compile("<(.*?)><(.*?)>");

    @Unique
    private static final IMapValueCache<String, TagPriorityList> FILTER_CACHE = IMapValueCache.createWeak(
            s -> {
                var m = gtocore$whiteBlackPattern.matcher(s);
                if (m.matches()) {
                    return new TagPriorityList(m.group(1), m.group(2));
                } else {
                    return new TagPriorityList("", "");
                }
            });

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
            this.forceUpdate();
        }
    }

    @Override
    protected IPartitionList createFilter() {
        if (this.filter == null) {
            this.filter = FILTER_CACHE.getCache("<" + oreExpWhite + "><" + oreExpBlack + ">");
        }
        return this.filter;
    }
}
