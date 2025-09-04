package com.gtocore.mixin.gtm.api;

import com.gregtechceu.gtceu.api.block.AppearanceBlock;
import com.gregtechceu.gtceu.api.block.MetaMachineBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MetaMachineBlock.class)
public class MetaMachineBlockMixin extends AppearanceBlock {

    public MetaMachineBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {}
}
