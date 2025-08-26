package com.gtocore.common.entity;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.utils.explosion.SphereExplosion;

import com.gregtechceu.gtceu.common.data.GTEntityTypes;
import com.gregtechceu.gtceu.common.entity.GTExplosiveEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NukeBombEntity extends GTExplosiveEntity {

    public NukeBombEntity(Level world, double x, double y, double z, @Nullable LivingEntity owner) {
        super(GTEntityTypes.INDUSTRIAL_TNT.get(), world, x, y, z, owner);
    }

    @Override
    protected float getStrength() {
        return 100.0F;
    }

    @Override
    public boolean dropsAllBlocks() {
        return false;
    }

    @Override
    protected int getRange() {
        return 40;
    }

    @Override
    public @NotNull BlockState getExplosiveState() {
        return GTOBlocks.NUKE_BOMB.getDefaultState();
    }

    @Override
    protected void explode() {
        SphereExplosion.explosion(blockPosition(), level(), 80, false, true);
    }
}
