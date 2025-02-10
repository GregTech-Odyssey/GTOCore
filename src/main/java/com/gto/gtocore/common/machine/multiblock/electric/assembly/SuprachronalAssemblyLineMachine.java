package com.gto.gtocore.common.machine.multiblock.electric.assembly;

import com.gto.gtocore.api.machine.feature.IHighlightMachine;
import com.gto.gtocore.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SuprachronalAssemblyLineMachine extends CrossRecipeMultiblockMachine implements IHighlightMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SuprachronalAssemblyLineMachine.class, CrossRecipeMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @DescSynced
    private final Set<BlockPos> poss;
    private int module;

    public SuprachronalAssemblyLineMachine(IMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallel);
        poss = new HashSet<>(2);
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        attachHighlightConfigurators(configuratorPanel);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        poss.clear();
        BlockPos pos = getPos();
        Direction facing = getFrontFacing();
        if (MachineUtils.isUD(facing)) {
            Direction direction = getUpwardsFacing();
            poss.add(MachineUtils.getOffsetPos(0, 0, 3, direction, pos));
            poss.add(MachineUtils.getOffsetPos(0, 0, -3, direction, pos));
        } else {
            poss.add(MachineUtils.getOffsetPos(0, 0, 3, facing, pos));
            poss.add(MachineUtils.getOffsetPos(0, 0, -3, facing, pos));
        }
        getSuprachronalAssemblyLineModule();
    }

    private void getSuprachronalAssemblyLineModule() {
        Level level = getLevel();
        if (level != null) {
            module = 0;
            for (BlockPos i : poss) {
                MetaMachine metaMachine = getMachine(level, i);
                if (metaMachine instanceof SuprachronalAssemblyLineModuleMachine machine && machine.isFormed()) {
                    machine.assemblyLineMachine = this;
                    module++;
                }
            }
        }
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (getOffsetTimer() % 20 == 0) {
            getSuprachronalAssemblyLineModule();
        }
        return true;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (getOffsetTimer() % 20 == 0) {
            getSuprachronalAssemblyLineModule();
        }
        textList.add(Component.translatable("gtocore.machine.module.am", module));
    }

    @Override
    public Set<BlockPos> getHighlightPos() {
        return poss;
    }
}
