package com.gto.gtocore.mixin.mc;

import com.gregtechceu.gtceu.api.GTValues;
import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.entity.IEnhancedPlayer;
import com.gto.gtocore.api.misc.PlanetManagement;
import com.gto.gtocore.client.ClientCache;
import com.gto.gtocore.common.data.GTOEffects;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.network.ServerMessage;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.utils.ServerUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.item.armor.ArmorComponentItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTDamageTypes;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;

@Mixin(value = Player.class, priority = 0)
public abstract class PlayerMixin extends LivingEntity implements IEnhancedPlayer {

    @Shadow
    @Final
    private Abilities abilities;

    @Shadow
    public abstract void onUpdateAbilities();

    @Shadow
    public abstract FoodData getFoodData();

    @Shadow
    @Final
    private Inventory inventory;
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
        if (tickCount % 20 == 0) {
            Level level = level();
            if (level.isClientSide) return;
            MinecraftServer server = level.getServer();
            if (server == null) return;
            if (getFoodData().getFoodLevel() > (GTOConfig.getDifficulty() == 1 ? 5 : 15) && getHealth() < getMaxHealth() - 4 && tickCount % 80 == 0 && getRandom().nextBoolean()) {
                heal(Math.max(1, (int) Math.log(getMaxHealth() * Math.max(1, 4 - GTOConfig.getDifficulty()) / 4)));
            }
            gTOCore$amprosium = false;
            boolean hasHotIronIngot = false;
            for (ItemStack stack : inventory.items) {
                if (stack.isEmpty()) continue;
                if (stack.getItem() == GTOItems.HOT_IRON_INGOT.asItem()) {
                    hasHotIronIngot = true;
                    break;
                } else if (!gTOCore$amprosium && ChemicalHelper.getMaterialStack(stack).material() == GTMaterials.Neutronium) {
                    gTOCore$amprosium = true;
                }
            }
            if (hasHotIronIngot) {
                float heatDamage = ((850 + 273 - 1750) / 1000.0F) + 2;
                ItemStack armor = getItemBySlot(EquipmentSlot.CHEST);
                if (!armor.isEmpty() && armor.getItem() instanceof ArmorComponentItem armorItem) {
                    heatDamage *= armorItem.getArmorLogic().getHeatResistance();
                }
                if (heatDamage > 0.0) {
                    hurt(GTDamageTypes.HEAT.source(level), heatDamage);
                }
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
            gTOCore$spaceState = inf;
            if (gTOCore$wardenState) {
                addEffect(new MobEffectInstance(MobEffects.SATURATION, 200, 0, false, false));
                if (data.getBoolean("night_vision")) {
                    addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false));
                }
            }
        }
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;setFoodLevel(I)V"))
    private void gTOCore$setFoodLevel(FoodData foodData, int foodLevel) {}

    @Unique
    private void gTOCore$discard(MinecraftServer server) {
        ServerUtils.teleportToDimension(server, this, GTODimensions.OVERWORLD, new Vec3(0, 100, 0));
        kill();
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"))
    private void jumpFromGround(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel serverLevel) {
            Optional.ofNullable(this.getEffect(GTOEffects.MYSTERIOUS_BOOST_EFFECT.get())).ifPresent(effect -> {
                BlockEntity blockEntity = this.level().getBlockEntity(this.getOnPos());
                if (blockEntity instanceof MetaMachineBlockEntity machineEntity) {
                    MetaMachine metaMachine = machineEntity.getMetaMachine();
                    if (metaMachine instanceof WorkableTieredMachine machine && machine.isActive()) {
                        RecipeLogic recipeLogic = machine.getRecipeLogic();
                        int progress = recipeLogic.getProgress();
                        int maxProgress = recipeLogic.getMaxProgress();
                        Optional.ofNullable(recipeLogic.getLastRecipe()).ifPresent(recipe -> {
                            int recipeEUtTier = RecipeHelper.getRecipeEUtTier(recipe);
                            if (effect.getAmplifier() >= recipeEUtTier) {
                                recipeLogic.setProgress(Math.min(progress + Math.min((int) (((double) 1 / GTValues.RNG.nextInt(6,10)) * maxProgress), 20 * 30), maxProgress - 1)); // 最多减1/3或者30秒，取低者
                                serverLevel.sendParticles(
                                        ParticleTypes.FIREWORK,
                                        blockEntity.getBlockPos().getX(),
                                        blockEntity.getBlockPos().getY() + 6,
                                        blockEntity.getBlockPos().getZ(),
                                        3,  // 粒子数量
                                        0.3, // X方向扩散
                                        0.2, // Y方向扩散
                                        0.3, // Z方向扩散
                                        0.02 // 粒子速度
                                );
                            }
                        });
                    }
                }
            });
        }
    }
}
