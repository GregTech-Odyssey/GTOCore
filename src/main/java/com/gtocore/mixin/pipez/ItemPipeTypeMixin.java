package com.gtocore.mixin.pipez;

import net.minecraft.core.Direction;

import de.maxhenkel.pipez.blocks.tileentity.PipeLogicTileEntity;
import de.maxhenkel.pipez.blocks.tileentity.types.ItemPipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ItemPipeType.class)
public class ItemPipeTypeMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getSpeed(PipeLogicTileEntity tileEntity, Direction direction) {
        return 20;
    }
}
