package com.gtocore.integration.jade.provider;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.IExpandedGrid;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.api.networking.IInWorldGridNodeHost;
import appeng.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@DataGeneratorScanned
public final class AEGridProvider extends CapabilityBlockProvider<IExpandedGrid> {

    public static boolean OBSERVE = false;

    @RegisterLanguage(cn = "节点数量：%s", en = "Node Amount: %s")
    private static final String NODE_AMOUNT = "gtocore.jade.node_amount";
    @RegisterLanguage(cn = "网络延迟：%s", en = "Grid Latency: %s")
    public static final String LATENCY = "gtocore.jade.grid_latency";

    public AEGridProvider() {
        super(GTOCore.id("ae_grid_provider"));
    }

    @Nullable
    @Override
    protected IExpandedGrid getCapability(Level level, BlockPos pos, BlockEntity blockEntity, @Nullable Direction side) {
        if (side != null || blockEntity == null) return null;
        IInWorldGridNodeHost cap;
        if (blockEntity instanceof IInWorldGridNodeHost host) {
            cap = host;
        } else {
            cap = blockEntity.getCapability(Capabilities.IN_WORLD_GRID_NODE_HOST).orElse(null);
        }
        if (cap != null) {
            for (var s : Direction.values()) {
                var node = cap.getGridNode(s);
                if (node != null && node.getGrid() instanceof IExpandedGrid grid) {
                    grid.observe();
                    return grid;
                }
            }
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, IExpandedGrid capability) {
        if (capability != null) {
            data.putString("name", capability.toString());
            data.putInt("size", capability.size());
            data.putLong("latency", capability.getLatency());
            Object o;
            if (capability.getPivot() != null && (o = capability.getPivot().getOwner()) != null) {
                if (o instanceof BlockEntity be) {
                    data.putLong("pos", be.getBlockPos().asLong());
                } else if (o instanceof MetaMachine machine) {
                    data.putLong("pos", machine.getPos().asLong());
                }
            }
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        String name = capData.getString("name");
        if (name.isEmpty()) return;
        tooltip.add(Component.translatable(name));
        long pos = capData.getLong("pos");
        if (pos != 0) {
            tooltip.add(Component.translatable("config.jade.plugin_jade.coordinates").append(Component.literal(BlockPos.of(pos).toShortString())));
        }
        tooltip.add(Component.translatable(NODE_AMOUNT, capData.getInt("size")));
        long latency = capData.getInt("latency");
        if (latency == 0) return;
        tooltip.add(Component.translatable(LATENCY, latency).append(" μs"));
    }
}
