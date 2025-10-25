package com.gtocore.mixin.eae;

import net.minecraft.world.item.Item;

import com.glodblock.github.extendedae.common.blocks.matrix.BlockAssemblerMatrixBase;
import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileAssemblerMatrixBase.class)
public abstract class TileAssemblerMatrixBaseMixin {

    @Shadow(remap = false)
    public abstract BlockAssemblerMatrixBase<?> getMatrixBlock();

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    protected Item getItemFromBlockEntity() {
        return this.getMatrixBlock().getPresentItem();
    }
}
