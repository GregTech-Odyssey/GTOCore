package com.gtocore.common.block;

import com.gtocore.common.blockentity.ManaPipeBlockEntity;
import com.gtocore.common.data.GTOBlockEntities;
import com.gtocore.common.pipe.mana.LevelManaPipeNet;
import com.gtocore.common.pipe.mana.ManaPipeProperties;
import com.gtocore.common.pipe.mana.ManaPipeType;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.PipeBlock;
import com.gregtechceu.gtceu.api.blockentity.PipeBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
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
import vazkii.botania.api.BotaniaForgeCapabilities;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ManaPipeBlock extends PipeBlock<ManaPipeType, ManaPipeProperties, LevelManaPipeNet> {

    public final PipeBlockRenderer renderer;
    @Getter
    public final PipeModel pipeModel;
    private final ManaPipeType pipeType;
    private final ManaPipeProperties properties;

    public ManaPipeBlock(Properties properties, ManaPipeType pipeType) {
        super(properties, pipeType);
        this.pipeType = pipeType;
        this.properties = ManaPipeProperties.INSTANCE;
        this.pipeModel = new PipeModel(pipeType.getThickness(), () -> GTCEu.id("block/pipe/pipe_side"), () -> GTCEu.id("block/pipe/pipe_normal_in"), null, null);
        this.renderer = new PipeBlockRenderer(this.pipeModel);
    }

    @Override
    public @NotNull LevelManaPipeNet getWorldPipeNet(ServerLevel level) {
        return LevelManaPipeNet.getOrCreate(level);
    }

    @Override
    public @NotNull BlockEntityType<? extends PipeBlockEntity<ManaPipeType, ManaPipeProperties>> getBlockEntityType() {
        return GTOBlockEntities.MANA_PIPE.get();
    }

    @Override
    public @NotNull ManaPipeProperties createRawData(BlockState pState, @Nullable ItemStack pStack) {
        return ManaPipeProperties.INSTANCE;
    }

    @Override
    public @NotNull ManaPipeProperties createProperties(PipeBlockEntity<ManaPipeType, ManaPipeProperties> pipeTile) {
        return this.pipeType.modifyProperties(properties);
    }

    @Override
    public @NotNull ManaPipeProperties getFallbackType() {
        return ManaPipeProperties.INSTANCE;
    }

    @Override
    @Nullable
    public PipeBlockRenderer getRenderer(BlockState state) {
        return renderer;
    }

    @OnlyIn(Dist.CLIENT)
    public static BlockColor tintedColor() {
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
            return 0x00A7F7;
        };
    }

    @Override
    public boolean canPipesConnect(PipeBlockEntity<ManaPipeType, ManaPipeProperties> selfTile, Direction side, PipeBlockEntity<ManaPipeType, ManaPipeProperties> sideTile) {
        return selfTile instanceof ManaPipeBlockEntity && sideTile instanceof ManaPipeBlockEntity;
    }

    @Override
    public boolean canPipeConnectToBlock(PipeBlockEntity<ManaPipeType, ManaPipeProperties> selfTile, Direction side, @Nullable BlockEntity tile) {
        return GTCapabilityHelper.getBlockEntityCapability(BotaniaForgeCapabilities.MANA_RECEIVER, tile, side.getOpposite()) != null;
    }
}
