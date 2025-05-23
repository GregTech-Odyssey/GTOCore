package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.api.data.GTOWorldGenLayers;
import com.gto.gtocore.api.entity.IEnhancedPlayer;
import com.gto.gtocore.common.network.KeyMessage;
import com.gto.gtocore.utils.ServerUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = Player.class, priority = 0)
public abstract class PlayerMixin extends LivingEntity implements IEnhancedPlayer {

    @Shadow
    @Final
    private Abilities abilities;

    @Unique
    private boolean gTOCore$canFly;
    @Unique
    private boolean gTOCore$spaceState;
    @Unique
    private boolean gTOCore$wardenState;

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean gTOCore$canFly() {
        return gTOCore$canFly;
    }

    @Override
    public boolean gTOCore$isSpaceState() {
        return gTOCore$spaceState;
    }

    @Override
    public boolean gTOCore$isWardenState() {
        return gTOCore$wardenState;
    }

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSharedFlag(IZ)V"))
    private void travel(Vec3 travelVector, CallbackInfo ci) {
        if (xxa == 0 && zza == 0 && KeyMessage.disableDrift) {
            setDeltaMovement(getDeltaMovement().multiply(0.5, 1, 0.5));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        gTOCore$spaceState = compound.getBoolean("spaceState");
        gTOCore$wardenState = compound.getBoolean("wardenState");
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("spaceState", gTOCore$spaceState);
        compound.putBoolean("wardenState", gTOCore$wardenState);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (tickCount % 20 == 0) {
            Level level = level();
            MinecraftServer server = level.getServer();
            if (server == null) return;
            String name = getName().getString();
            String armorSlots = getArmorSlots().toString();
            gTOCore$wardenState = Objects.equals(armorSlots, "[1 warden_boots, 1 warden_leggings, 1 warden_chestplate, 1 warden_helmet]");
            boolean inf = Objects.equals(armorSlots, "[1 infinity_boots, 1 infinity_pants, 1 infinity_chestplate, 1 infinity_helmet]");
            if (level.dimension().location().equals(GTOWorldGenLayers.CREATE)) {
                if (!inf) {
                    gTOCore$discard(server, name);
                }
                ServerUtils.runCommandSilent(server, "execute at " + name + " run kill @e[distance=..100,name=!" + name + ",type=!item]");
            } else if (level.dimension().location().equals(GTOWorldGenLayers.OTHERSIDE)) {
                if (!(gTOCore$wardenState || inf)) {
                    gTOCore$discard(server, name);
                }
            }
            gTOCore$canFly = gTOCore$wardenState;
            gTOCore$spaceState = inf || Objects.equals(armorSlots, "[1 quarktech_boots, 1 quarktech_leggings, 1 advanced_quarktech_chestplate, 1 quarktech_helmet]") || Objects.equals(armorSlots, "[1 quarktech_boots, 1 quarktech_leggings, 1 quarktech_chestplate, 1 quarktech_helmet]");
            CompoundTag data = getPersistentData();
            if (gTOCore$canFly) {
                addEffect(new MobEffectInstance(MobEffects.SATURATION, 200, 0, false, false));
                if (data.getBoolean("night_vision")) {
                    addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false));
                }
            } else {
                data.putBoolean("night_vision", false);
                data.putInt("fly_speed", 1);
                abilities.setFlyingSpeed(0.05F);
            }
        }
    }

    @Unique
    private void gTOCore$discard(MinecraftServer server, String name) {
        ServerUtils.runCommandSilent(server, "execute in minecraft:overworld as " + name + " run tp 0 100 0");
        kill();
    }
}
