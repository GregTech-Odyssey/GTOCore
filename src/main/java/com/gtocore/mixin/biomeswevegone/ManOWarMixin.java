package com.gtocore.mixin.biomeswevegone;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.potionstudios.biomeswevegone.tags.BWGBiomeTags;
import net.potionstudios.biomeswevegone.world.entity.manowar.ManOWar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ManOWar.class)
public abstract class ManOWarMixin extends Animal {

    protected ManOWarMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public int getMaxAirSupply() {
        if (level() == null) return 0;
        int base = 6000;
        return this.level().getBiome(this.blockPosition()).is(BWGBiomeTags.DRY) ? base / this.getRandom().nextInt(1, 4) : base;
    }
}
