package com.gto.gtocore.common.machine.magic;

import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlchemicalBoilerMachine extends SimpleNoEnergyMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AlchemicalBoilerMachine.class, SimpleNoEnergyMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    @DescSynced
    private long time;

    @Persisted
    @DescSynced
    private int divisor;

    @Persisted
    @DescSynced
    private int parallel;

    @Nullable
    protected TickableSubscription tickSubs;

    public AlchemicalBoilerMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, GTMachines.defaultTankSizeFunction, args);
        this.tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
    }

    private void tickUpdate() {
        if (time > 0) {
            time--;
        } else {
            divisor = 1;
            parallel = 1;
            if (isWorkingEnabled() && getOffsetTimer() % 5 == 0) {
                for (int i = 0; i < importItems.getSlots(); i++) {
                    ItemStack stack = importItems.getStackInSlot(i);
                    int count = stack.getCount();
                    long addedTime = 0;
                    if (stack.is(GTOItems.THE_FLAME_OF_ORIGIN.get())) {
                        addedTime = 8000L * count;
                        divisor = 2;
                        parallel = 1;
                    } else if (stack.is(GTOItems.THE_FIRE_OF_INFERNO.get())) {
                        addedTime = 4000000L * count;
                        divisor = 16;
                        parallel = 16;
                    } else if (stack.is(GTOItems.BLAZING_VEIN.get())) {
                        addedTime = 2000000000L * count;
                        divisor = 64;
                        parallel = 64;
                    } else if (FurnaceBlockEntity.getFuel().containsKey(stack.getItem())) {
                        addedTime = (long) FurnaceBlockEntity.getFuel().get(stack.getItem()) * count;
                        divisor = 1;
                        parallel = 1;
                    }
                    if (addedTime > 0) {
                        time += addedTime;
                        importItems.setStackInSlot(i, Items.AIR.getDefaultInstance());
                        break;
                    }
                }
            }
        }
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, GTRecipe recipe, OCParams params, OCResult result) {
        if (machine instanceof AlchemicalBoilerMachine alchemicalBoilerMachine && alchemicalBoilerMachine.time > 0) {
            GTRecipe recipe1 = GTRecipeModifiers.accurateParallel(machine, recipe, alchemicalBoilerMachine.parallel, false).getFirst();
            recipe1.duration = recipe.duration / alchemicalBoilerMachine.divisor;
            return recipe1;
        }
        return null;
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        super.attachTooltips(tooltipsPanel);
        tooltipsPanel.attachTooltips(new IFancyTooltip.Basic(() -> GuiTextures.INFO_ICON,
                () -> List.of(Component.translatable("tooltip.avaritia.time_consume", time).append(" Tick"),
                        Component.translatable("gtceu.multiblock.pyrolyse_oven.speed", 100 / divisor),
                        Component.translatable("gtceu.universal.tooltip.parallel", parallel)),
                () -> true, () -> null));
    }
}
