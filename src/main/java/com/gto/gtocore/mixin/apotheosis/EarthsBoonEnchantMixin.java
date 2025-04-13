package com.gto.gtocore.mixin.apotheosis;

import com.gto.gtocore.common.data.GTOOres;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.BlockEvent;

import dev.shadowsoffire.apotheosis.ench.enchantments.masterwork.EarthsBoonEnchant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EarthsBoonEnchant.class)
public class EarthsBoonEnchantMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void provideBenefits(BlockEvent.BreakEvent e) {
        Player player = e.getPlayer();
        if (player.level().isClientSide) return;
        ItemStack stack = player.getMainHandItem();
        int level = stack.getEnchantmentLevel((EarthsBoonEnchant) (Object) this);
        if (e.getState().is(Tags.Blocks.STONE) && level > 0 && player.getRandom().nextFloat() <= 0.01F * level) {
            Block.popResource(player.level(), e.getPos(), ChemicalHelper.get(TagPrefix.rawOre, GTOOres.selectMaterial(player.level().dimension().location())));
        }
    }
}
