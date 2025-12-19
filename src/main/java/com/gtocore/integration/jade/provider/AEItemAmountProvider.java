package com.gtocore.integration.jade.provider;

import com.gtolib.GTOCore;
import com.gtolib.api.player.IEnhancedPlayer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.LiquidBlock;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AmountFormat;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

import static net.minecraft.world.item.Items.AIR;

public enum AEItemAmountProvider implements IBlockComponentProvider, IEntityComponentProvider, IServerDataProvider<Accessor<?>> {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        var player = accessor.getPlayer();
        if (accessor.getBlock() != null && player != null) {
            AEKey aeKey;
            if (accessor.getBlock() instanceof LiquidBlock liquidBlock) {
                var fluid = liquidBlock.getFluidState(accessor.getBlockState()).getType();
                aeKey = AEFluidKey.of(fluid);
            } else if (accessor.getBlock().asItem() != AIR) {
                aeKey = AEItemKey.of(accessor.getBlock().asItem());
            } else {
                return;
            }
            IEnhancedPlayer.fetchClientAEData(player, aeKey);
            if (IEnhancedPlayer.isClientAEReachable(player)) {
                tooltip.add(IEnhancedPlayer.getClientAEStatusText(player, aeKey, AmountFormat.FULL));
            }
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        var player = accessor.getPlayer();
        if (accessor.getEntity() instanceof ItemEntity itemEntity && player != null) {
            var stack = itemEntity.getItem();
            IEnhancedPlayer.fetchClientAEData(player, AEItemKey.of(stack));

            if (IEnhancedPlayer.isClientAEReachable(player)) {
                tooltip.add(IEnhancedPlayer.getClientAEStatusText(player, AEItemKey.of(stack), AmountFormat.FULL));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return GTOCore.id("ae_item_amount");
    }

    @Override
    public void appendServerData(CompoundTag data, Accessor<?> accessor) {
        IEnhancedPlayer.of(accessor.getPlayer()).getPlayerData().getMeStorageInfoManager().hookBroadcastChanges();
    }
}
