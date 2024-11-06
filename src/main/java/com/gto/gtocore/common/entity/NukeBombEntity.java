package com.gto.gtocore.common.entity;

import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.utils.GTOExplosion;

import com.gregtechceu.gtceu.common.data.GTEntityTypes;
import com.gregtechceu.gtceu.common.entity.GTExplosiveEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NukeBombEntity extends GTExplosiveEntity {

    public NukeBombEntity(Level world, double x, double y, double z, @Nullable LivingEntity owner) {
        super(GTEntityTypes.INDUSTRIAL_TNT.get(), world, x, y, z, owner);
    }

    public NukeBombEntity(EntityType<? extends NukeBombEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected float getStrength() {
        return 100F;
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
        GTOExplosion nukeExplosion = new GTOExplosion(this.blockPosition(), level(), 50);
        nukeExplosion.finalizeExplosion(true);
    }
}
