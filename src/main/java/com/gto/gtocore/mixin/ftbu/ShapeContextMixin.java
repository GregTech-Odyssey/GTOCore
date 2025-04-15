package com.gto.gtocore.mixin.ftbu;

import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.block.OreBlock;
import com.gregtechceu.gtceu.api.item.IGTTool;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import dev.ftb.mods.ftbultimine.shape.ShapeContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
            ret = maxBlocks;
            return ret;
        }
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();
        if (item instanceof IGTTool gtTool) {
            String type = gtTool.getToolType().name;
            int base = 128 >> GTOConfig.getDifficulty();
            if (type.contains("_vajra") || (original.getBlock() instanceof OreBlock && ("mining_hammer".equals(type) || type.contains("_drill"))) || (original.getSoundType() == SoundType.WOOD && "lv_chainsaw".equals(type)))
                base <<= 2;
            if (gtTool.isElectric()) base *= 1 << (gtTool.getElectricTier());
            ret = Math.min(base, maxBlocks);
        } else if (item instanceof DiggerItem) {
            ret = Math.min(64 >> GTOConfig.getDifficulty(), maxBlocks);
        } else {
            ret = 1;
        }
        return ret;
    }
}
