package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.common.data.GTODamageTypes;

import com.gtolib.api.machine.multiblock.NoEnergyCustomParallelMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.FunctionContainer;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AdvancedPrimitiveBlastFurnaceMachine extends NoEnergyCustomParallelMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AdvancedPrimitiveBlastFurnaceMachine.class, NoEnergyCustomParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @DescSynced
    private BlockPos pos;
    @DescSynced
    private int height;
    @Persisted
    private double duration = 1;

    @Persisted
    private int temperature = 298;

    private final ConditionalSubscriptionHandler tickSubs;

    public AdvancedPrimitiveBlastFurnaceMachine(MetaMachineBlockEntity holder) {
        super(holder, false, m -> (long) ((AdvancedPrimitiveBlastFurnaceMachine) m).height << 1);
        tickSubs = new ConditionalSubscriptionHandler(this, this::tickUpdate, () -> isFormed || temperature > 298);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        height = 0;
        FunctionContainer<Integer, ?> container = getMultiblockState().getMatchContext().get("SteelFrame");
        if (container != null) {
            height = container.getValue();
        }
        pos = MachineUtils.getOffsetPos(7, getFrontFacing(), getPos());
        tickSubs.initialize(getLevel());
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
    public boolean onWorking() {
        if (getOffsetTimer() % 20 == 0 && getLevel() != null) {
            var recipe = getRecipeLogic().getLastRecipe();
            if (recipe != null) {
                List<Entity> entities = getLevel().getEntitiesOfClass(Entity.class, new AABB(
                        pos.getX() - 4,
                        pos.getY() + 1,
                        pos.getZ() - 4,
                        pos.getX() + 4,
                        pos.getY() + 8 + height,
                        pos.getZ() + 4));
                for (Entity entity : entities) {
                    entity.hurt(GTODamageTypes.getBlastFurnaceDamageSource(entity), recipe.parallels * 10);
                }
            }
        }
        return super.onWorking();
    }

    @Override
    @Nullable
    protected Recipe getRealRecipe(Recipe recipe) {
        double dm = Math.min(1, 400D / temperature);
        duration = dm;
        recipe = ParallelLogic.accurateParallel(this, recipe, getParallel());
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientTick() {
        super.clientTick();
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
    public long getParallelLong() {
        return super.getParallelLong() * Math.max(1, temperature / 500);
    }
}
