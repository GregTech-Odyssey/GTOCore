package com.gto.gtocore.common.fluid;

import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOFluids;
import com.gto.gtocore.common.data.GTOItems;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import org.jetbrains.annotations.NotNull;

public abstract class GelidCryotheumFluid extends ForgeFlowingFluid {

    private GelidCryotheumFluid() {
        super(new Properties(GTOFluids.GELID_CRYOTHEUM_TYPE, GTOFluids.GELID_CRYOTHEUM,
                GTOFluids.FLOWING_GELID_CRYOTHEUM).explosionResistance(100.0f).tickRate(3).bucket(GTOItems.GELID_CRYOTHEUM_BUCKET).block(GTOBlocks.GELID_CRYOTHEUM));
    }

    public final static class Source extends GelidCryotheumFluid {

        @Override
        public int getAmount(@NotNull FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return true;
        }
    }

    public final static class Flowing extends GelidCryotheumFluid {

        @Override
        protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return false;
        }
    }
}
