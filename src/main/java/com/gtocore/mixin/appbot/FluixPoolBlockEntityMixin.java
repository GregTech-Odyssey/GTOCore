package com.gtocore.mixin.appbot;

import com.gtolib.utils.MathUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import appbot.ae2.ManaKey;
import appbot.block.FluixPoolBlockEntity;
import appbot.mixins.ManaPoolBlockEntityAccessor;
import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.MEStorage;
import appeng.me.helpers.IGridConnectedBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

@Mixin(value = FluixPoolBlockEntity.class, remap = false)
public abstract class FluixPoolBlockEntityMixin extends ManaPoolBlockEntity implements IGridConnectedBlockEntity {

    public FluixPoolBlockEntityMixin(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Shadow
    public abstract IManagedGridNode getMainNode();

    @Shadow
    private boolean saving;

    @Shadow
    @Final
    private ManaPoolBlockEntityAccessor mana;

    @Shadow
    @Final
    private IActionSource actionSource;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getMaxMana() {
        IGrid grid = this.getMainNode().getGrid();
        if (grid != null && !this.saving) {
            int oldMana = this.mana.getMana();
            long manaCap;
            if (this.getMainNode().isActive()) {
                MEStorage storage = grid.getStorageService().getInventory();
                var extract = MathUtil.saturatedCast(storage.extract(ManaKey.KEY, Integer.MAX_VALUE, Actionable.SIMULATE, this.actionSource));
                this.mana.setMana(extract);
                if (extract < 2000000000) {
                    manaCap = extract + storage.insert(ManaKey.KEY, Integer.MAX_VALUE, Actionable.SIMULATE, this.actionSource);
                } else {
                    manaCap = extract;
                }
            } else {
                this.mana.setMana(0);
                manaCap = 0L;
            }
            if (oldMana != this.mana.getMana()) {
                this.setChanged();
                this.markDispatchable();
            }
            return MathUtil.saturatedCast(manaCap);
        } else {
            return super.getMaxMana();
        }
    }
}
