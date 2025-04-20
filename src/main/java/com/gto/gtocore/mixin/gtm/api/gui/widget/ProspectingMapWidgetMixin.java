package com.gto.gtocore.mixin.gtm.api.gui.widget;

import com.gregtechceu.gtceu.api.gui.misc.PacketProspecting;
import com.gregtechceu.gtceu.api.gui.misc.ProspectorMode;
import com.gregtechceu.gtceu.api.gui.texture.ProspectingTexture;
import com.gregtechceu.gtceu.api.gui.widget.ProspectingMapWidget;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.common.item.ProspectorScannerBehavior;
import com.gregtechceu.gtceu.integration.map.cache.server.ServerCache;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.gui.widget.SearchComponentWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Mixin(ProspectingMapWidget.class)
public abstract class ProspectingMapWidgetMixin extends WidgetGroup implements SearchComponentWidget.IWidgetSearch<Object> {

    @Shadow(remap = false)
    private int chunkIndex;
    @Final
    @Shadow(remap = false)
    private int chunkRadius;
    @Shadow(remap = false)
    private int playerChunkX;
    @Shadow(remap = false)
    private int playerChunkZ;
    @Final
    @Shadow(remap = false)
    private ProspectorMode mode;
    @Final
    @Shadow(remap = false)
    private Queue<PacketProspecting> packetQueue = new LinkedBlockingQueue<>();
    @OnlyIn(Dist.CLIENT)
    @Shadow(remap = false)
    private ProspectingTexture texture;

    @Shadow(remap = false)
    protected abstract void addOresToList(Object[][][] data);

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void detectAndSendChanges() {
        var player = gui.entityPlayer;
        var world = player.level();
        var held = player.getItemInHand(InteractionHand.MAIN_HAND);
        while (chunkIndex < (chunkRadius * 2 - 1) * (chunkRadius * 2 - 1)) {

            int row = chunkIndex / (chunkRadius * 2 - 1);
            int column = chunkIndex % (chunkRadius * 2 - 1);

            int ox = column - chunkRadius + 1;
            int oz = row - chunkRadius + 1;

            var chunk = world.getChunk(playerChunkX + ox, playerChunkZ + oz);
            if (mode == ProspectorMode.ORE) {
                ServerCache.instance.prospectAllInChunk(world.dimension(), chunk.getPos(), (ServerPlayer) player);
            }
            PacketProspecting packet = new PacketProspecting(playerChunkX + ox, playerChunkZ + oz, this.mode);
            mode.scan(packet.data, chunk);
            writeUpdateInfo(-1, packet::writePacketData);
            chunkIndex++;
            if (held.getItem() instanceof IComponentItem componentItem) {
                for (var component : componentItem.getComponents()) {
                    if (component instanceof ProspectorScannerBehavior prospector) {
                        if (!player.isCreative() && !prospector.drainEnergy(held, false)) {
                            player.closeContainer();
                        }
                    }
                }
            }
        }
    }

    /**
     * @author .
     * @reason .
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite(remap = false)
    public void updateScreen() {
        super.updateScreen();
        if (packetQueue != null) {
            while (!packetQueue.isEmpty()) {
                var packet = packetQueue.poll();
                texture.updateTexture(packet);
                addOresToList(packet.data);
            }
        }
    }
}
