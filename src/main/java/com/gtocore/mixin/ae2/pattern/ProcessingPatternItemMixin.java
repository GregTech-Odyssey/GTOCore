package com.gtocore.mixin.ae2.pattern;

import com.gtolib.api.ae2.MyPatternDetailsHelper;
import com.gtolib.utils.RLUtils;

import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.stacks.AEItemKey;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(ProcessingPatternItem.class)
public abstract class ProcessingPatternItemMixin extends EncodedPatternItem {

    protected ProcessingPatternItemMixin(Properties properties) {
        super(properties);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public AEProcessingPattern decode(AEItemKey what, Level level) {
        if (what == null || !what.hasTag()) {
            return null;
        }

        try {
            return MyPatternDetailsHelper.CACHE.get(what);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level level, List<Component> lines, TooltipFlag advancedTooltips) {
        var tag = stack.getTag();
        if (tag == null) return;
        if (tag.tags.get("uuid") instanceof IntArrayTag arrayTag) {
            if (level == null) {
                level = net.minecraft.client.Minecraft.getInstance().level;
            }
            Player player = null;
            if (level != null) {
                player = level.getPlayerByUUID(NbtUtils.loadUUID(arrayTag));
            }
            lines.add(Component.translatable("tooltip.item.pattern.uuid", player == null ? "Unknown" : player.getName()));
        }
        if (tag.tags.containsKey("recipe") && !tag.getString("recipe").isEmpty()) {
            lines.add(Component.translatable("gtocore.pattern.recipe"));
            var key = RLUtils.parse(tag.getString("recipe").split("/")[0]).toLanguageKey();
            lines.add(Component.translatable("gtocore.pattern.type", Component.translatable(key)));
        }

        super.appendHoverText(stack, level, lines, advancedTooltips);
    }
}
