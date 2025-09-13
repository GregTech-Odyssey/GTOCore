package com.gtocore.common.machine.multiblock.electric.processing;

import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.machine.multiblock.part.MachineAccessTerminalPartMachine;

import com.gtolib.api.GTOValues;
import com.gtolib.api.machine.feature.multiblock.IHighlightMachine;
import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProcessingEncapsulatorMachine extends TierCasingMultiblockMachine implements IHighlightMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ProcessingEncapsulatorMachine.class, TierCasingMultiblockMachine.MANAGED_FIELD_HOLDER);
    @DescSynced
    private final List<BlockPos> highlightPos = new ArrayList<>();
    private int moduleCount;
    int processingAmount;
    private final List<MachineAccessTerminalPartMachine> terminalPartMachine = new ArrayList<>(4);
    final Object2IntOpenHashMap<GTRecipeType> typeMap = new Object2IntOpenHashMap<>();

    public ProcessingEncapsulatorMachine(MetaMachineBlockEntity holder) {
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
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof MachineAccessTerminalPartMachine accessTerminalPartMachine) {
            terminalPartMachine.add(accessTerminalPartMachine);
            traitSubscriptions.add(accessTerminalPartMachine.getInventory().addChangedListener(this::updateMap));
        }
    }

    @Override
    public void onStructureFormed() {
        terminalPartMachine.clear();
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
        int glassTier = getCasingTier(GTOValues.GLASS_TIER);
        if (glassTier < GTValues.UIV) {
            processingAmount = 0;
        } else {
            processingAmount = 32 * (1 << (getCasingTier(GTOValues.GLASS_TIER) - GTValues.UIV));
        }
        updateMap();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        processingAmount = 0;
        highlightPos.clear();
        terminalPartMachine.clear();
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        update(false);
        return true;
    }

    private void updateMap() {
        typeMap.clear();
        int ifTier = getCasingTier(GTOValues.INTEGRAL_FRAMEWORK_TIER);
        for (var part : terminalPartMachine) {
            var inv = part.getInventory();
            for (int i = 0; i < inv.getSlots(); i++) {
                if (inv.getStackInSlot(i).getItem() instanceof MetaMachineItem metaMachineItem) {
                    MachineDefinition definition = metaMachineItem.getDefinition();
                    if (definition.getTier() <= ifTier && definition.getRecipeTypes().length > 0 && definition.getRecipeTypes()[0] != GTORecipeTypes.DUMMY_RECIPES) {
                        for (var type : definition.getRecipeTypes()) {
                            typeMap.put(type, definition.getTier());
                        }
                    }
                }
            }
        }
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
        textList.add(Component.translatable("cover.advanced_energy_detector.max").append(Component.translatable("gtocore.tooltip.item.craft_step", processingAmount)));
        textList.add(Component.translatable("gtocore.machine.module", moduleCount));
    }

    @Nullable
    private Recipe getRecipe() {
        if (getTier() > GTValues.UIV) {
            Recipe recipe = getRecipeBuilder().duration(400).EUt(GTValues.VA[getTier()]).buildRawRecipe();
            if (RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        }
        return null;
    }

    @Override
    @NotNull
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }

    @Override
    public List<BlockPos> getHighlightPos() {
        return this.highlightPos;
    }
}
