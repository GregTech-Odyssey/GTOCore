package com.gtocore.mixin.ftbu;

import com.gtocore.integration.apotheosis.FTBUltimineAffix;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.block.OreBlock;
import com.gregtechceu.gtceu.api.item.IGTTool;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import com.hollingsworth.arsnouveau.common.items.SpellBook;
import dev.ftb.mods.ftbultimine.shape.ShapeContext;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(ShapeContext.class)
public class ShapeContextMixin {

    @Shadow(remap = false)
    @Final
    private int maxBlocks;

    @Shadow(remap = false)
    @Final
    private ServerPlayer player;

    @Shadow(remap = false)
    @Final
    private BlockState original;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public int maxBlocks() {
        int ret;
        if (player.isCreative()) {
            return maxBlocks;
        }
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();
        int base = 128 >> GTOCore.difficulty;
        switch (item) {
            case SpellBook spellBook -> {
                base <<= spellBook.tier.value;
                ret = Math.min(base, maxBlocks);
            }
            case IGTTool gtTool -> {
                String type = gtTool.getToolType().name;
                if (type.contains("_vajra") || (original.getBlock() instanceof OreBlock && ("mining_hammer".equals(type) || type.contains("_drill"))) || (original.getSoundType() == SoundType.WOOD && "lv_chainsaw".equals(type)))
                    base <<= 2;
                if (gtTool.isElectric()) base *= 1 << (gtTool.getElectricTier());
                ret = Math.min(base, maxBlocks);
            }
            case DiggerItem ignored -> ret = Math.min(64 >> GTOCore.difficulty, maxBlocks);
            default -> ret = 1;
        }

        AtomicReference<Float> affixBonus = new AtomicReference<>(1.0f);
        AffixHelper.streamAffixes(stack).filter(afx -> afx.affix().get() instanceof FTBUltimineAffix).findFirst().ifPresent(afx -> {
            float extra = ((FTBUltimineAffix) afx.affix().get()).getBuff(afx.rarity().get(), afx.level());
            affixBonus.set(affixBonus.get() + extra);
        });
        ret = Math.min((int) (ret * affixBonus.get()), maxBlocks);

        return ret;
    }
}
