package com.gtocore.mixin.arsnouveau;

import com.hollingsworth.arsnouveau.setup.reward.Rewards;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Rewards.class)
public final class RewardsMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void init() {}
}
