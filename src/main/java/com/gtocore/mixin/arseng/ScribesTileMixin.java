package com.gtocore.mixin.arseng;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import appeng.capabilities.Capabilities;
import com.hollingsworth.arsnouveau.common.block.tile.ModdedTile;
import com.hollingsworth.arsnouveau.common.block.tile.ScribesTile;
import com.hollingsworth.arsnouveau.common.entity.EntityFlyingItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(value = ScribesTile.class, remap = false, priority = 0)
public abstract class ScribesTileMixin extends ModdedTile {

    @Shadow
    public List<ItemStack> consumedStacks;

    public ScribesTileMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Shadow
    public abstract boolean canConsumeItemstack(ItemStack stack);

    /// Replaces Ars Energistics' ME extraction with GTOCore's version
    /// Code under GNU LGPLv3 License
    /// @see gripe._90.arseng.mixin.ScribesTileMixin#takeFromInterfaces
    @Inject(method = "takeNearby",
            at = @At("TAIL"))
    private void takeFromInterfaces(CallbackInfo ci) {
        if (level == null) {
            return;
        }

        var area = BlockPos.betweenClosed(
                worldPosition.north(6).east(6).below(2),
                worldPosition.south(6).west(6).above(2));

        for (var pos : area) {
            var be = level.getBlockEntity(pos);

            if (be != null) {
                var hasExtracted = new AtomicBoolean(false);

                be.getCapability(Capabilities.STORAGE).ifPresent(storage -> {
                    gto$replaceArseng$extract(storage, pos);
                    hasExtracted.set(true);
                });

                if (hasExtracted.get()) {
                    return;
                }

                for (var side : Direction.values()) {
                    be.getCapability(Capabilities.STORAGE, side).ifPresent(storage -> {
                        gto$replaceArseng$extract(storage, pos);
                        hasExtracted.set(true);
                    });

                    if (hasExtracted.get()) {
                        return;
                    }
                }
            }
        }
    }

    @Unique
    private void gto$replaceArseng$extract(MEStorage storage, BlockPos pos) {
        for (var stored : storage.getAvailableStacks()) {
            if (stored.getKey() instanceof AEItemKey item && canConsumeItemstack(item.wrapForDisplayOrFilter())) {
                var extracted = storage.extract(item, 1, Actionable.MODULATE, IActionSource.empty());
                var taken = item.toStack((int) extracted);
                consumedStacks.add(taken);

                var flyingItem = new EntityFlyingItem(level, pos, getBlockPos());
                flyingItem.setStack(taken);
                Objects.requireNonNull(level).addFreshEntity(flyingItem);
                updateBlock();
                return;
            }
        }
    }
}
