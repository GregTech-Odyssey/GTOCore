package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.entity.IEnhancedPlayer;
import com.gto.gtocore.api.misc.PlanetManagement;
import com.gto.gtocore.client.ClientCache;
import com.gto.gtocore.common.network.ServerMessage;
import com.gto.gtocore.utils.ServerUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
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

    @Shadow
    public abstract void onUpdateAbilities();

    @Unique
    private boolean gTOCore$amprosium;
    @Unique
    private boolean gTOCore$disableDrift;
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

    @Override
    public boolean gTOCore$isDisableDrift() {
        return gTOCore$disableDrift;
    }

    @Override
    public boolean gTOCore$isAmprosium() {
        return gTOCore$amprosium;
    }

    @Override
    public void gtocore$setDrift(boolean drift) {
        if ((Object) this instanceof ServerPlayer player) {
            gTOCore$disableDrift = drift;
            ServerMessage.disableDrift(player, gTOCore$disableDrift);
        }
    }

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSharedFlag(IZ)V"))
    private void travel(Vec3 travelVector, CallbackInfo ci) {
        if (xxa == 0 && zza == 0 && ClientCache.disableDrift) {
            setDeltaMovement(getDeltaMovement().multiply(0.5, 1, 0.5));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        gTOCore$spaceState = compound.getBoolean("spaceState");
        gTOCore$wardenState = compound.getBoolean("wardenState");
        gTOCore$disableDrift = compound.getBoolean("disableDrift");
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("spaceState", gTOCore$spaceState);
        compound.putBoolean("wardenState", gTOCore$wardenState);
        compound.putBoolean("disableDrift", gTOCore$disableDrift);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (tickCount % 40 == 0) {
            Level level = level();
            MinecraftServer server = level.getServer();
            if (server == null) return;
            gTOCore$amprosium = false;
            MaterialStack materialStack = ChemicalHelper.getMaterial(getItemInHand(InteractionHand.MAIN_HAND));
            if (materialStack == null) {
                materialStack = ChemicalHelper.getMaterial(getItemInHand(InteractionHand.OFF_HAND));
            }
            if (materialStack != null && materialStack.material() == GTMaterials.Neutronium) {
                gTOCore$amprosium = true;
            }
            String name = getName().getString();
            String armorSlots = getArmorSlots().toString();
            gTOCore$wardenState = Objects.equals(armorSlots, "[1 warden_boots, 1 warden_leggings, 1 warden_chestplate, 1 warden_helmet]");
            boolean inf = Objects.equals(armorSlots, "[1 infinity_boots, 1 infinity_pants, 1 infinity_chestplate, 1 infinity_helmet]");
            if (level.dimension().location().equals(GTODimensions.CREATE)) {
                if (!inf) {
                    gTOCore$discard(server);
                }
                ServerUtils.runCommandSilent(server, "execute at " + name + " run kill @e[distance=..100,name=!" + name + ",type=!item]");
            } else if (level.dimension().location().equals(GTODimensions.OTHERSIDE)) {
                if (!(gTOCore$wardenState || inf)) {
                    gTOCore$discard(server);
                }
            } else if ((Object) this instanceof ServerPlayer serverPlayer) {
                if (GTCEu.isProd() && GTODimensions.ALL_PLANET.containsKey(level.dimension().location()) && !PlanetManagement.isUnlocked(serverPlayer, level.dimension().location())) {
                    serverPlayer.displayClientMessage(Component.translatable("gtocore.ununlocked"), false);
                    gTOCore$discard(server);
                }
            }
            CompoundTag data = getPersistentData();
            boolean canFly = gTOCore$wardenState;
            if (gTOCore$canFly && !canFly) {
                data.putBoolean("night_vision", false);
                data.putInt("fly_speed", 1);
                abilities.setFlyingSpeed(0.05F);
                onUpdateAbilities();
            }
            gTOCore$canFly = canFly;
            gTOCore$spaceState = inf || Objects.equals(armorSlots, "[1 quarktech_boots, 1 quarktech_leggings, 1 advanced_quarktech_chestplate, 1 quarktech_helmet]") || Objects.equals(armorSlots, "[1 quarktech_boots, 1 quarktech_leggings, 1 quarktech_chestplate, 1 quarktech_helmet]");
            if (gTOCore$wardenState) {
                addEffect(new MobEffectInstance(MobEffects.SATURATION, 200, 0, false, false));
                if (data.getBoolean("night_vision")) {
                    addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false));
                }
            }
        }
    }

    @Unique
    private void gTOCore$discard(MinecraftServer server) {
        ServerUtils.teleportToDimension(server, this, GTODimensions.OVERWORLD, new Vec3(0, 100, 0));
        kill();
    }
}
