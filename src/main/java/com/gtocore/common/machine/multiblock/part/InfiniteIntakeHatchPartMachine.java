package com.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.recipe.condition.DimensionCondition;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public final class InfiniteIntakeHatchPartMachine extends TieredIOPartMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            InfiniteIntakeHatchPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);

    public static final Map<ResourceLocation, Fluid> AIR_MAP = new Object2ObjectOpenHashMap<>();

    private TickableSubscription intakeSubs;

    @Persisted
    private final NotifiableFluidTank tank;

    @RequireRerender
    @DescSynced
    private boolean isWorking;

    public InfiniteIntakeHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder, GTValues.ULV, IO.IN);
        this.tank = new NotifiableFluidTank(this, 1, 256000, IO.IN, IO.NONE);
        tank.addChangedListener(this::updateTankSubscription);
    }

    public static void init(GTRecipeBuilder recipeBuilder, Consumer<FinishedRecipe> consumer) {
        for (var condition : recipeBuilder.conditions) {
            if (condition instanceof DimensionCondition dimensionCondition) {
                var dim = dimensionCondition.getDimension();
                var fluids = RecipeHelper.getOutputFluids(recipeBuilder);
                if (!fluids.isEmpty()) {
                    AIR_MAP.put(dim, fluids.get(0).getFluid());
                    break;
                }
            }
        }
    }

    @Override
    @Nullable
    public IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTankSubscription));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        unsubscribe();
    }

    @Override
    public void onPaintingColorChanged(int color) {
        getHandlerList().setColor(color, true);
    }

    @Override
    public void onNeighborChanged(@NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        if (getPos().relative(getFrontFacing()).equals(fromPos)) {
            updateTankSubscription();
        }
    }

    private boolean isFrontFaceFree() {
        if (getLevel() instanceof ServerLevel serverLevel) {
            return serverLevel.getBlockState(getPos().relative(getFrontFacing())).isAir();
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientTick() {
        super.clientTick();
        if (isWorking && getOffsetTimer() % 5 == 0) {
            var facing = this.getFrontFacing();
            int stepX = facing.getStepX();
            int stepY = facing.getStepY();
            int stepZ = facing.getStepZ();
            double offset = 2 * GTValues.RNG.nextDouble() + 2;
            var pos = this.getPos().getCenter().add(stepX * 0.5, stepY * 0.5, stepZ * 0.5);
            var center = pos.add(stepX * offset, stepY * offset, stepZ * offset);
            double theta = Math.PI * 2 * GTValues.RNG.nextDouble();
            double x = (1.5F * Math.cos(theta));
            double y = (1.5F * Math.sin(theta));
            var point = new Vec2((float) x, (float) y);
            var randPos = center.add(stepY * point.y + stepZ * point.x, stepX * point.x + stepZ * point.y, stepX * point.y + stepY * point.x);
            var speed = pos.subtract(randPos).scale(0.055);
            getLevel().addParticle(ParticleTypes.CLOUD, randPos.x, randPos.y, randPos.z, speed.x, speed.y, speed.z);
        }
    }

    private void updateTankSubscription() {
        if (isWorkingEnabled() && isFrontFaceFree()) {
            intakeSubs = subscribeServerTick(intakeSubs, this::intake);
            this.isWorking = true;
        } else {
            unsubscribe();
        }
    }

    private void intake() {
        if (getOffsetTimer() % 20 == 0) {
            var fluid = AIR_MAP.get(getLevel().dimension().location());
            if (fluid == null) {
                unsubscribe();
                return;
            }
            if (tank.fillInternal(new FluidStack(fluid, 8000), IFluidHandler.FluidAction.EXECUTE) == 0) {
                unsubscribe();
            } else {
                updateTankSubscription();
            }
        }
    }

    private void unsubscribe() {
        if (intakeSubs != null) {
            intakeSubs.unsubscribe();
            intakeSubs = null;
        }
        isWorking = false;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        super.setWorkingEnabled(workingEnabled);
        updateTankSubscription();
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
