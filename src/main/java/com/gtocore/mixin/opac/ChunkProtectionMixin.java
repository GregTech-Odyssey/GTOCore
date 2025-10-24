package com.gtocore.mixin.opac;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.apache.commons.lang3.function.TriFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.server.IServerData;
import xaero.pac.common.server.claims.protection.ChunkProtection;
import xaero.pac.common.server.parties.party.IServerParty;
import xaero.pac.common.server.player.config.IPlayerConfig;
import xaero.pac.common.server.player.config.api.IPlayerConfigAPI;
import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.Nullable;

@Mixin(ChunkProtection.class)
public class ChunkProtectionMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean hasChunkAccess(IPlayerConfigAPI claimConfig, Entity accessor, UUID accessorId) {
        return true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onBlockInteraction(IServerData<?, ?> serverData, BlockState blockState, Entity entity, InteractionHand hand, ItemStack heldItem, ServerLevel world, BlockPos pos, Direction direction, boolean breaking, boolean messages) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onEntityPlaceBlock(IServerData<?, ?> serverData, Entity entity, ServerLevel world, BlockPos pos, IPlayerConfigOptionSpecAPI<Integer> option) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onItemRightClick(IServerData<?, ?> serverData, InteractionHand hand, ItemStack itemStack, BlockPos pos, LivingEntity entity, boolean messages) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onMobGrief(IServerData<?, ?> serverData, Entity entity) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onEntityInteraction(IServerData<?, ?> serverData, Entity interactingEntityIndirect, Entity interactingEntity, Entity target, ItemStack heldItem, InteractionHand hand, boolean attack, boolean messages, boolean targetExceptions) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onEntityFire(IServerData<?, ?> serverData, Entity target) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void onEntityEnterChunk(IServerData<?, ?> serverData, Entity entity, double goodX, double goodZ, SectionPos newSection, SectionPos oldSection) {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onChorusFruitTeleport(IServerData<?, ?> serverData, Vec3 pos, Entity entity) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void onLightningBolt(IServerData<?, ?> serverData, LightningBolt bolt) {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onCropTrample(IServerData<?, ?> serverData, Entity entity, BlockPos pos) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onUseItemAt(IServerData<?, ?> serverData, Entity entity, ServerLevel world, BlockPos pos, Direction direction, ItemStack itemStack, InteractionHand hand, boolean itemUseAtTargetAllowed, boolean itemUseAtOffsetAllowed, boolean messages) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onFluidSpread(IServerData<?, ?> serverData, ServerLevel world, BlockPos from, BlockPos to) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onDispenseFrom(IServerData<?, ?> serverData, ServerLevel serverLevel, BlockPos from) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onPistonPush(IServerData<?, ?> serverData, ServerLevel world, List<BlockPos> toPush, List<BlockPos> toDestroy, BlockPos pistonPos, Direction direction, boolean extending) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void onEntitiesPushBlock(IServerData<?, ?> serverData, ServerLevel world, BlockPos pos, Block block, List<? extends Entity> entities) {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void onEntitiesCollideWithEntity(IServerData<?, ?> serverData, Entity entity, List<? extends Entity> collidingEntities) {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void onEntityAffectsEntities(IServerData<?, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData, Entity entity, List<Entity> targets) {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onNetherPortal(IServerData<?, ?> serverData, Entity entity, ServerLevel world, BlockPos pos) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onRaidSpawn(IServerData<?, ?> serverData, ServerLevel world, BlockPos pos) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onMobSpawn(IServerData<?, ?> serverData, Entity entity, double x, double y, double z, MobSpawnType spawnReason) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onItemAddedToWorld(IServerData<?, ?> serverData, ItemEntity itemEntity) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onLivingLootEntity(IServerData<?, ?> serverData, LivingEntity livingEntity, Entity lootEntity, DamageSource source) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onEntityPickup(IServerData<?, ?> serverData, Entity entity, Entity pickedEntity, UUID pickedEntityThrowerId, UUID pickedEntityOwnerId, Map<Entity, Set<ChunkPos>> cantPickupCache, TriFunction<IPlayerConfig, Entity, Entity, IPlayerConfigOptionSpecAPI<Integer>> protectionOptionGetter) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onItemPickup(IServerData<?, ?> serverData, Entity entity, ItemEntity itemEntity) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onEntityMerge(IServerData<?, ?> serverData, Entity first, UUID firstThrower, UUID firstOwner, Entity second, UUID secondThrower, UUID secondOwner, IPlayerConfigOptionSpecAPI<Integer> playerOption, IPlayerConfigOptionSpecAPI<Integer> mobOption, IPlayerConfigOptionSpecAPI<Boolean> redirectOption) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onCreateMod(IServerData<?, ?> serverData, ServerLevel world, int posChunkX, int posChunkZ, @Nullable BlockPos sourceOrAnchor, boolean checkNeighborBlocks, boolean affectsBlocks, boolean affectsEntities) {
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public <E> boolean onCreateModAffectPositionedObjects(IServerData<?, ?> serverData, ServerLevel world, List<E> objects, Function<E, ChunkPos> positionGetter, BlockPos contraptionAnchor, boolean checkNeighborBlocks, boolean removeInvalid, boolean affectsBlocks, boolean affectsEntities) {
        return false;
    }
}
