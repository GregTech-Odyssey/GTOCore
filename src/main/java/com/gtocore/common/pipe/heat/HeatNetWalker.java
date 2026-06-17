package com.gtocore.common.pipe.heat;

import com.gtocore.common.blockentity.HeatPipeBlockEntity;

import com.gtolib.GTOCore;
import com.gtolib.api.capability.IHeatContainer;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.pipenet.PipeNetWalker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class HeatNetWalker extends PipeNetWalker<HeatPipeBlockEntity, HeatPipeProperties, HeatPipeNet> {

    @Nullable
    public static List<HeatRoutePath> createNetData(HeatPipeNet pipeNet, BlockPos sourcePipe) {
        try {
            var walker = new HeatNetWalker(pipeNet, sourcePipe, 1, new ArrayList<>());
            walker.traversePipeNet();
            return walker.routes;
        } catch (Exception e) {
            GTOCore.LOGGER.error("error while create net data for pipe net", e);
        }
        return null;
    }

    private final List<HeatRoutePath> routes;
    private HeatPipeBlockEntity[] pipes = {};
    private int loss;

    public HeatNetWalker(HeatPipeNet pipeNet, BlockPos sourcePipe, int walkedBlocks, List<HeatRoutePath> routes) {
        super(pipeNet, sourcePipe, walkedBlocks);
        this.routes = routes;
    }

    @NotNull
    @Override
    protected HeatNetWalker createSubWalker(HeatPipeNet pipeNet, Direction facingToNextPos, BlockPos nextPos, int walkedBlocks) {
        var walker = new HeatNetWalker(pipeNet, nextPos, walkedBlocks, routes);
        walker.loss = loss;
        walker.pipes = pipes;
        return walker;
    }

    @Override
    protected void checkPipe(HeatPipeBlockEntity pipeTile, BlockPos pos) {
        pipes = ArrayUtils.add(pipes, pipeTile);
        loss += pipeTile.getNodeData().getLossPerBlock();
    }

    @Override
    protected void checkNeighbour(HeatPipeBlockEntity pipeTile, BlockPos pipePos, Direction faceToNeighbour,
                                  @Nullable BlockEntity neighbourTile) {
        // assert that the last added pipe is the current pipe
        if (pipeTile != pipes[pipes.length - 1])
            throw new IllegalStateException("The current pipe is not the last added pipe. Something went seriously wrong!");
        if (GTCapabilityHelper.getBlockEntityGTCapability(IHeatContainer.class, neighbourTile, faceToNeighbour.getOpposite()) != null) {
            routes.add(new HeatRoutePath(pipeTile, faceToNeighbour, getWalkedBlocks()));
        }
    }

    @Override
    protected Class<HeatPipeBlockEntity> getBasePipeClass() {
        return HeatPipeBlockEntity.class;
    }
}
