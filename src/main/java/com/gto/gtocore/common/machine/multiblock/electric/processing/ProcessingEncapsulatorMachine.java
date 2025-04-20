package com.gto.gtocore.common.machine.multiblock.electric.processing;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.feature.multiblock.IHighlightMachine;
import com.gto.gtocore.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProcessingEncapsulatorMachine extends TierCasingMultiblockMachine implements IHighlightMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ProcessingEncapsulatorMachine.class, TierCasingMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Getter
    @DescSynced
    private final List<BlockPos> highlightPos = new ArrayList<>();

    private int moduleCount;

    public ProcessingEncapsulatorMachine(IMachineBlockEntity holder) {
        super(holder, GTOValues.INTEGRAL_FRAMEWORK_TIER, GTOValues.GLASS_TIER);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        attachHighlightConfigurators(configuratorPanel);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        highlightPos.clear();
        var centerPosition = MachineUtils.getOffsetPos(26, 0, getFrontFacing(), getPos());
        for (int i = 3; i < 34; i += 6) {
            for (int j = -1; j < 2; j += 2) {
                int y = i * j;
                highlightPos.add(centerPosition.offset(22, y, 0));
                highlightPos.add(centerPosition.offset(-22, y, 0));
                highlightPos.add(centerPosition.offset(0, y, 22));
                highlightPos.add(centerPosition.offset(0, y, -22));
            }
        }
        for (int j = -1; j < 2; j += 2) {
            int y = 40 * j;
            highlightPos.add(centerPosition.offset(34, y, 5));
            highlightPos.add(centerPosition.offset(34, y, -5));
            highlightPos.add(centerPosition.offset(-34, y, 5));
            highlightPos.add(centerPosition.offset(-34, y, -5));
        }
        update(true);
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        update(false);
        return true;
    }

    private void update(boolean promptly) {
        if (promptly || getOffsetTimer() % 40 == 0) {
            moduleCount = 0;
            Level level = getLevel();
            if (level == null) return;
            for (BlockPos blockPoss : highlightPos) {
                if (getMachine(level, blockPoss) instanceof EncapsulatorExecutionModuleMachine executionModuleMachine && executionModuleMachine.isFormed()) {
                    executionModuleMachine.encapsulatorMachine = this;
                    moduleCount++;
                }
            }
        }
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        update(false);
        textList.add(Component.translatable("gtocore.machine.module", moduleCount));
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (getTier() > GTValues.UIV) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().duration(400).EUt(GTValues.VA[getTier()]).buildRawRecipe();
            if (RecipeRunnerHelper.matchRecipeTickInput(this, recipe)) return recipe;
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }
}
