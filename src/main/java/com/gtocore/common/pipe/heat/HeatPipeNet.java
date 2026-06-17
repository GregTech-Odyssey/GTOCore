package com.gtocore.common.pipe.heat;

import com.gregtechceu.gtceu.api.pipenet.LevelPipeNet;
import com.gregtechceu.gtceu.api.pipenet.Node;
import com.gregtechceu.gtceu.api.pipenet.PipeNet;
import com.gregtechceu.gtceu.utils.collection.LoopIterator;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

public final class HeatPipeNet extends PipeNet<HeatPipeProperties> {

    private final Long2ObjectOpenHashMap<LoopIterator<HeatRoutePath>> netData = new Long2ObjectOpenHashMap<>();

    public HeatPipeNet(LevelPipeNet<HeatPipeProperties, ? extends PipeNet<HeatPipeProperties>> world) {
        super(world);
    }

    @NotNull
    public LoopIterator<HeatRoutePath> getNetData(long pipePos, BlockPos pos) {
        var path = netData.get(pipePos);
        if (path != null) return path;
        var paths = HeatNetWalker.createNetData(this, pos);
        if (paths == null) return LoopIterator.EMPTY;
        path = new LoopIterator<>(paths.toArray(new HeatRoutePath[0]));
        netData.put(pipePos, path);
        return path;
    }

    @Override
    public void onNeighbourUpdate(BlockPos fromPos) {
        netData.clear();
    }

    @Override
    public void onPipeConnectionsUpdate() {
        netData.clear();
    }

    @Override
    protected void transferNodeData(Long2ObjectOpenHashMap<Node<HeatPipeProperties>> transferredNodes,
                                    PipeNet<HeatPipeProperties> parentNet) {
        super.transferNodeData(transferredNodes, parentNet);
        netData.clear();
        ((HeatPipeNet) parentNet).netData.clear();
    }

    @Override
    protected void writeNodeData(HeatPipeProperties nodeData, CompoundTag tagCompound) {}

    @Override
    protected HeatPipeProperties readNodeData(CompoundTag tagCompound) {
        return HeatPipeProperties.INSTANCE;
    }
}
