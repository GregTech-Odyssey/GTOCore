package com.gto.gtocore.common.machine.multiblock.part;

import com.gto.gtocore.api.machine.part.ItemHatchPartMachine;
import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SpoolHatchPartMachine extends ItemHatchPartMachine implements IInteractedMachine {

    public static Map<Item, Integer> SPOOL;

    static {
        ImmutableMap.Builder<Item, Integer> spoolBuilder = ImmutableMap.builder();
        spoolBuilder.put(GTOItems.SPOOLS_MICRO.get(), 1);
        spoolBuilder.put(GTOItems.SPOOLS_SMALL.get(), 2);
        spoolBuilder.put(GTOItems.SPOOLS_MEDIUM.get(), 3);
        spoolBuilder.put(GTOItems.SPOOLS_LARGE.get(), 4);
        spoolBuilder.put(GTOItems.SPOOLS_JUMBO.get(), 5);
        SPOOL = spoolBuilder.build();
    }

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SpoolHatchPartMachine.class, ItemHatchPartMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    @DescSynced
    @RequireRerender
    private boolean isWorking;

    public SpoolHatchPartMachine(IMachineBlockEntity holder) {
        super(holder, 1, i -> SPOOL.containsKey(i.getItem()));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean beforeWorking(IWorkableMultiController controller) {
        isWorking = true;
        return super.beforeWorking(controller);
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        isWorking = false;
        return super.afterWorking(controller);
    }

    @Override
    public InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                   BlockHitResult hit) {
        // Removed the player damage part
        return InteractionResult.PASS;
    }

    @Override
    public void onDrops(List<ItemStack> list) {
        if (isWorking) {
            super.onDrops(list);
        }
    }
}
