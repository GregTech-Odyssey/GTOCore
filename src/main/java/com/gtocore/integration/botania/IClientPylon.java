package com.gtocore.integration.botania;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;

import java.awt.*;
import java.util.Random;

public interface IClientPylon {

    static void particle(BlockPos pylon, double worldTime, Vec3 point, Level level, int color) {
        if (!level.isClientSide) {
            return;
        }
        worldTime += new Random(pylon.hashCode()).nextInt(1000);
        worldTime /= 5;

        float r0 = 0.75F + (float) Math.random() * 0.05F;
        double x = pylon.getX() + 0.5 + Math.cos(worldTime) * r0;
        double z = pylon.getZ() + 0.5 + Math.sin(worldTime) * r0;

        Vec3 ourCoords = new Vec3(x, pylon.getY() + 0.25, z);
        point = point.subtract(0, 0.5, 0);
        Vec3 movementVector = point.subtract(ourCoords).normalize().scale(0.2);
        double rc = 0.45;
        Vec3 thisVec = pylon.getCenter().add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);
        Vec3 receiverVec = point.add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);

        Vec3 motion = receiverVec.subtract(thisVec).scale(0.04F);
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        float size = 0.125F + 0.125F * (float) Math.random();

        WispParticleData data = WispParticleData.wisp(size, r, g, b).withNoClip(true);
        level.addAlwaysVisibleParticle(data, thisVec.x, thisVec.y, thisVec.z, motion.x, motion.y, motion.z);

        WispParticleData data0 = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 1);
        level.addParticle(data0, x, pylon.getY() + 0.25, z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
        if (level.random.nextInt(3) == 0) {
            WispParticleData data1 = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F);
            level.addParticle(data1, x, pylon.getY() + 0.25, z, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
        }
        if (level.random.nextBoolean()) {
            SparkleParticleData data2 = SparkleParticleData.sparkle((float) Math.random(), r, g, b, 2);
            level.addParticle(data2, pylon.getX() + Math.random(), pylon.getY() + Math.random() * 1.5, pylon.getZ() + Math.random(), 0, 0, 0);
        }
    }
}
