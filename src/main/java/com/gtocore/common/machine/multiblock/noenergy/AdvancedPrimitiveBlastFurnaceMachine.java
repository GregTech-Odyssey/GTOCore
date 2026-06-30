package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.common.data.GTODamageTypes;

import com.gtolib.api.machine.multiblock.NoEnergyCustomParallelMultiblockMachine;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.ITickSubscription;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AdvancedPrimitiveBlastFurnaceMachine extends NoEnergyCustomParallelMultiblockMachine {

    @SyncToClient
    private BlockPos pos;
    @SyncToClient
    private int height;
    @SaveToDisk
    private double duration = 1;

    @SaveToDisk
    private int temperature = 298;

    private final ConditionalSubscriptionHandler tickSubs;
    private TickableSubscription particleSubscription;
    private Set<BlockPos> heatPositions = Collections.emptySet();
    private AABB heatBounds = new AABB(BlockPos.ZERO);

    public AdvancedPrimitiveBlastFurnaceMachine(MetaMachineBlockEntity holder) {
        super(holder, m -> (long) ((AdvancedPrimitiveBlastFurnaceMachine) m).height << 1);
        tickSubs = new ConditionalSubscriptionHandler(this, this::tickUpdate, 0, () -> isFormed || temperature > 298);
    }

    @Override
    public void onStructureFormedClient() {
        super.onStructureFormedClient();
        particleSubscription = subscribeClientTick(particleSubscription, this::particleTick);
    }

    @Override
    public void onStructureInvalidClient() {
        super.onStructureInvalidClient();
        particleSubscription = ITickSubscription.unsubscribe(particleSubscription);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        height = 0;
        var container = getMultiblockState().getMatchContext().get(GTOPredicates.DataKeys.STEEL_FRAME);
        if (container != null) {
            height = container;
        }
        pos = MachineUtils.getOffsetPos(7, getFrontFacing(), getPos());
        tickSubs.initialize(getLevel());
        heatPositions = getMultiblockState().getMatchContext().getOrDefault(GTOPredicates.DataKeys.BLAST_FURNACE_HEAT, Collections.emptySet());
        updateHeatBounds();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        heatPositions = Collections.emptySet();
        heatBounds = new AABB(BlockPos.ZERO);
    }

    private void tickUpdate() {
        if (getRecipeLogic().isWorking()) {
            if (temperature < 2000) temperature++;
        } else if (temperature > 298) {
            temperature -= 2;
        }
        tickSubs.updateSubscription();
    }

    @Override
    public void onWorking() {
        if (getOffsetTimer() % 40 == 0 && getLevel() != null) {
            var recipe = getRecipeLogic().getLastRecipe();
            if (recipe != null) {
                List<Entity> entities = getLevel().getEntitiesOfClass(Entity.class, heatBounds);
                for (Entity entity : entities) {
                    if (!intersectsHeatPosition(entity)) continue;
                    if (entity instanceof LivingEntity) {
                        entity.hurt(GTODamageTypes.getBlastFurnaceDamageSource(entity), recipe.parallels * 5);
                    } else {
                        entity.kill();
                    }
                }
            }
        }
        super.onWorking();
    }

    private void updateHeatBounds() {
        if (heatPositions.isEmpty()) {
            heatBounds = new AABB(BlockPos.ZERO);
            return;
        }
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (BlockPos heatPosition : heatPositions) {
            minX = Math.min(minX, heatPosition.getX());
            minY = Math.min(minY, heatPosition.getY());
            minZ = Math.min(minZ, heatPosition.getZ());
            maxX = Math.max(maxX, heatPosition.getX());
            maxY = Math.max(maxY, heatPosition.getY());
            maxZ = Math.max(maxZ, heatPosition.getZ());
        }
        heatBounds = new AABB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
    }

    private boolean intersectsHeatPosition(Entity entity) {
        AABB box = entity.getBoundingBox();
        int minX = Mth.floor(box.minX);
        int minY = Mth.floor(box.minY);
        int minZ = Mth.floor(box.minZ);
        int maxX = Mth.floor(box.maxX - 1.0E-7D);
        int maxY = Mth.floor(box.maxY - 1.0E-7D);
        int maxZ = Mth.floor(box.maxZ - 1.0E-7D);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (heatPositions.contains(mutablePos.set(x, y, z))) return true;
                }
            }
        }
        return false;
    }

    @Override
    @Nullable
    protected GTRecipe getRealRecipe(RecipeHandlerUnit unit, GTRecipe recipe) {
        double dm = Math.min(1, 400D / temperature);
        duration = dm;
        recipe = ParallelLogic.accurateParallel(this, unit, recipe, getParallel());
        if (recipe == null) return null;
        recipe.duration = (int) (recipe.duration * dm);
        return recipe;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        duration = 1;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.height", height));
        textList.add(Component.translatable("gtceu.multiblock.hpca.temperature", temperature));
        textList.add(Component.translatable("gtocore.machine.total_time.duration", FormattingUtil.formatNumbers(duration)));
    }

    @OnlyIn(Dist.CLIENT)
    private void particleTick() {
        if (getRecipeLogic().isWorking() && pos != null && getLevel() != null) {
            BlockPos pos1 = MachineUtils.getOffsetPos(-1, 7 + height, getFrontFacing(), pos);
            var facing = getFrontFacing().getOpposite();
            float xPos = facing.getStepX() * 0.76F + pos1.getX() + 0.5F;
            float yPos = facing.getStepY() * 0.76F + pos1.getY() + 0.25F;
            float zPos = facing.getStepZ() * 0.76F + pos1.getZ() + 0.5F;
            float ySpd = facing.getStepY() * 0.1F + 0.2F + 0.1F * GTValues.RNG.nextFloat();
            getLevel().addParticle(ParticleTypes.LARGE_SMOKE, xPos, yPos, zPos, 0, ySpd, 0);
        }
    }

    @Override
    public void animateTick(RandomSource random) {
        if (getRecipeLogic().isWorking() && pos != null && getLevel() != null && ConfigHolder.INSTANCE.machines.machineSounds && GTValues.RNG.nextDouble() < 0.1) {
            getLevel().playLocalSound(pos.getX(), pos.getY() + 2, pos.getZ(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    @Override
    public long getParallel() {
        return super.getParallel() * Math.max(1, temperature / 500);
    }
}
