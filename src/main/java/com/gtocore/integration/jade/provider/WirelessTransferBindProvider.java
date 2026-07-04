package com.gtocore.integration.jade.provider;

import com.gtocore.integration.gtmthings.WirelessTransferBindIndex;
import com.gtocore.integration.gtmthings.WirelessTransferBindIndex.Binding;

import com.gtolib.GTOCore;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import org.apache.commons.lang3.StringUtils;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * When looking at a block that a gtmthings wireless transfer cover is bound to, lists those covers
 * (transfer type, holding block, position, face) in the Jade tooltip. Backed by {@link WirelessTransferBindIndex}.
 */
public final class WirelessTransferBindProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    private static final ResourceLocation UID = GTOCore.id("wireless_transfer_bind_provider");
    private static final String KEY = "binds";

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        var bindings = WirelessTransferBindIndex.get(accessor.getLevel().dimension(), accessor.getPosition());
        if (bindings.isEmpty()) return;
        ListTag list = new ListTag();
        for (Binding b : bindings) {
            CompoundTag tag = new CompoundTag();
            tag.putString("d", b.coverDim().location().toString());
            tag.putInt("x", b.coverPos().getX());
            tag.putInt("y", b.coverPos().getY());
            tag.putInt("z", b.coverPos().getZ());
            tag.putString("f", b.coverFace().getName());
            tag.putString("t", b.transferType() == 2 ? "gtocore.wireless_transfer.type.fluid" : "gtocore.wireless_transfer.type.item");
            tag.putString("b", b.coverBlockId());
            list.add(tag);
        }
        data.put(KEY, list);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        ListTag list = accessor.getServerData().getList(KEY, Tag.TAG_COMPOUND);
        if (list.isEmpty()) return;
        String lookedDim = accessor.getLevel().dimension().location().toString();
        tooltip.add(Component.translatable("gtocore.wireless_transfer.bind_title").withStyle(ChatFormatting.GRAY));
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            String pos = "(" + tag.getInt("x") + ", " + tag.getInt("y") + ", " + tag.getInt("z") + ")";
            tooltip.add(Component.translatable("gtocore.wireless_transfer.bind_entry",
                    Component.translatable(tag.getString("t")),
                    Component.translatable(tag.getString("b")),
                    pos,
                    StringUtils.capitalize(tag.getString("f"))));
            String coverDim = tag.getString("d");
            if (!coverDim.equals(lookedDim)) {
                String path = dimensionPath(coverDim);
                tooltip.add(Component.translatable("gtocore.wireless_transfer.dim_suffix",
                        Component.translatableWithFallback("gtocore.dimension." + path, path)));
            }
        }
    }

    /** "minecraft:the_nether" -> "the_nether"; matches GTO's {@code gtocore.dimension.<path>} lang keys. */
    private static String dimensionPath(String dimensionId) {
        int i = dimensionId.indexOf(':');
        return i < 0 ? dimensionId : dimensionId.substring(i + 1);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
