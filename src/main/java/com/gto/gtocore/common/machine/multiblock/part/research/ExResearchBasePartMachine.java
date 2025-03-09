package com.gto.gtocore.common.machine.multiblock.part.research;

import com.gto.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.api.capability.IHPCAComponentHatch;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ExResearchBasePartMachine extends MultiblockPartMachine implements IHPCAComponentHatch, IMachineModifyDrops {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ExResearchBasePartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    @DescSynced
    @RequireRerender
    private boolean damaged;
    @Getter
    protected final int tier;

    ExResearchBasePartMachine(IMachineBlockEntity holder, int tier) {
        super(holder);
        this.tier = tier;
    }

    boolean doesAllowBridging() {
        return true;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    @Override
    public int getDefaultPaintingColor() {
        return 0xFFFFFF;
    }

    @Override
    public boolean canShared() {
        return false;
    }

    // Handle damaged state

    @Override
    public final boolean isBridge() {
        return doesAllowBridging() && !(canBeDamaged() && isDamaged());
    }

    @Override
    public boolean replacePartModelWhenFormed() {
        return false;
    }

    @Override
    public boolean isDamaged() {
        return canBeDamaged() && damaged;
    }

    @Override
    public void setDamaged(boolean damaged) {
        if (!canBeDamaged()) return;
        if (this.damaged != damaged) {
            this.damaged = damaged;
            markDirty();
        }
    }

    @Override
    public void onDrops(List<ItemStack> drops) {
        for (int i = 0; i < drops.size(); ++i) {
            ItemStack drop = drops.get(i);
            if (drop.getItem() == this.getDefinition().getItem()) {
                if (canBeDamaged() && isDamaged()) {
                    if (tier == 3) drops.set(i, GTOBlocks.BIOCOMPUTER_SHELL.asStack());
                    else if (tier == 4) drops.set(i, GTOBlocks.BIOCOMPUTER_SHELL.asStack());
                    else if (tier == 5) drops.set(i, GTOBlocks.BIOCOMPUTER_SHELL.asStack());
                    else drops.set(i, GTOBlocks.BIOCOMPUTER_SHELL.asStack());
                }
                break;
            }
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
