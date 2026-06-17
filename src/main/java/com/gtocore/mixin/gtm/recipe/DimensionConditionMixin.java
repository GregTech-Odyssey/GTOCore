package com.gtocore.mixin.gtm.recipe;

import com.gtolib.api.data.GTODimensions;

import com.gregtechceu.gtceu.common.recipe.condition.DimensionCondition;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = DimensionCondition.class, remap = false)
public abstract class DimensionConditionMixin {

    @Final
    @Shadow
    public ResourceKey<Level> dimension;

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public Component getTooltips() {
        return Component.translatable("recipe.condition.dimension.tooltip", Component.translatable(GTODimensions.getTranslationKey(this.dimension)));
    }
}
