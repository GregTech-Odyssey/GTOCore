package com.gtocore.common.block;

import com.gtocore.common.blockentity.HeatPipeBlockEntity;
import com.gtocore.common.data.GTOBlockEntities;
import com.gtocore.common.pipe.heat.HeatPipeProperties;
import com.gtocore.common.pipe.heat.HeatPipeType;
import com.gtocore.common.pipe.heat.LevelHeatPipeNet;

import com.gtolib.api.capability.IHeatContainer;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.PipeBlock;
import com.gregtechceu.gtceu.api.blockentity.PipeBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.client.model.PipeModel;
import com.gregtechceu.gtceu.client.renderer.block.PipeBlockRenderer;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HeatPipeBlock extends PipeBlock<HeatPipeType, HeatPipeProperties, LevelHeatPipeNet> {

    public final PipeBlockRenderer renderer;
    @Getter
    public final PipeModel pipeModel;
    private final HeatPipeType pipeType;
    private final HeatPipeProperties properties;

    public HeatPipeBlock(Properties properties, HeatPipeType pipeType) {
        super(properties, pipeType);
        this.pipeType = pipeType;
        this.properties = HeatPipeProperties.INSTANCE;
        this.pipeModel = new PipeModel(pipeType.getThickness(), () -> GTCEu.id("block/pipe/pipe_side"), () -> GTCEu.id("block/pipe/pipe_normal_in"), null, null);
        this.renderer = new PipeBlockRenderer(this.pipeModel);
    }

    @Override
    public @NotNull LevelHeatPipeNet getWorldPipeNet(ServerLevel level) {
        return LevelHeatPipeNet.getOrCreate(level);
    }

    @Override
    public @NotNull BlockEntityType<? extends PipeBlockEntity<HeatPipeType, HeatPipeProperties>> getBlockEntityType() {
        return GTOBlockEntities.HEAT_PIPE.get();
    }

    @Override
    public @NotNull HeatPipeProperties createRawData(BlockState pState, @Nullable ItemStack pStack) {
        return HeatPipeProperties.INSTANCE;
    }

    @Override
    public @NotNull HeatPipeProperties createProperties(PipeBlockEntity<HeatPipeType, HeatPipeProperties> pipeTile) {
        return this.pipeType.modifyProperties(properties);
    }

    @Override
    public @NotNull HeatPipeProperties getFallbackType() {
        return HeatPipeProperties.INSTANCE;
    }

    @Override
    @Nullable
    public PipeBlockRenderer getRenderer(BlockState state) {
        return renderer;
    }

    @OnlyIn(Dist.CLIENT)
    public static BlockColor tintedColor(Material material) {
        return (blockState, level, blockPos, index) -> {
            if (blockPos != null && level != null && level.getBlockEntity(blockPos) instanceof PipeBlockEntity<?, ?> pipe) {
                if (!pipe.getFrameMaterial().isNull()) {
                    if (index == 3) {
                        return pipe.getFrameMaterial().getMaterialRGB();
                    } else if (index == 4) {
                        return pipe.getFrameMaterial().getMaterialSecondaryRGB();
                    }
                }
                if (pipe.isPainted()) {
                    return pipe.getRealColor();
                }
            }
            return material.getMaterialRGB();
        };
    }

    @Override
    public boolean canPipesConnect(PipeBlockEntity<HeatPipeType, HeatPipeProperties> selfTile, Direction side, PipeBlockEntity<HeatPipeType, HeatPipeProperties> sideTile) {
        return selfTile instanceof HeatPipeBlockEntity && sideTile instanceof HeatPipeBlockEntity;
    }

    @Override
    public boolean canPipeConnectToBlock(PipeBlockEntity<HeatPipeType, HeatPipeProperties> selfTile, Direction side, @Nullable BlockEntity tile) {
        return GTCapabilityHelper.getBlockEntityGTCapability(IHeatContainer.class, tile, side.getOpposite()) != null;
    }
}
