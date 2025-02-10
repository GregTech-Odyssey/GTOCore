package com.gto.gtocore.common.machine.multiblock.electric.nano;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.machine.feature.ICoilMachine;
import com.gto.gtocore.api.machine.feature.IHighlightMachine;
import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CoilTrait;
import com.gto.gtocore.common.data.machines.MultiBlockMachineC;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.google.common.collect.HashBiMap;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public final class NanitesIntegratedMachine extends StorageMultiblockMachine implements ICoilMachine, IHighlightMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            NanitesIntegratedMachine.class, StorageMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    static final HashBiMap<MachineDefinition, Integer> MODULE_MAP = HashBiMap.create(3);

    static {
        MODULE_MAP.put(MultiBlockMachineC.ORE_EXTRACTION_MODULE, 1);
        MODULE_MAP.put(MultiBlockMachineC.BIOENGINEERING_MODULE, 2);
        MODULE_MAP.put(MultiBlockMachineC.POLYMER_TWISTING_MODULE, 3);
    }

    int chance;
    private final CoilTrait coilTrait;
    @DescSynced
    private final Set<BlockPos> poss = new ObjectOpenHashSet<>(3, 0.9F);
    private final Set<Integer> module = new IntOpenHashSet(3, 0.9F);

    public NanitesIntegratedMachine(IMachineBlockEntity holder) {
        super(holder, 64, i -> ChemicalHelper.getPrefix(i.getItem()) == GTOTagPrefix.nanites);
        coilTrait = new CoilTrait(this, false, true);
    }

    @Override
    protected void onMachineChanged() {
        chance = getStorageStack().getCount();
    }

    static void trimRecipe(GTRecipe recipe, int chance) {
        if (GTValues.RNG.nextInt(100) < chance) {
            recipe.inputs.get(ItemRecipeCapability.CAP).remove(0);
            recipe.outputs.get(ItemRecipeCapability.CAP).remove(0);
        }
    }

    @Override
    public GTRecipe fullModifyRecipe(@NotNull GTRecipe recipe) {
        trimRecipe(recipe, chance);
        return super.fullModifyRecipe(recipe);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        poss.clear();
        module.clear();
        Level level = getLevel();
        if (level == null) return;
        Direction direction = getFrontFacing();
        BlockPos blockPos = MachineUtils.getOffsetPos(45, direction, getPos());
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            poss.add(MachineUtils.getOffsetPos(8, Direction.WEST, blockPos));
            poss.add(MachineUtils.getOffsetPos(8, Direction.EAST, blockPos));
        } else {
            poss.add(MachineUtils.getOffsetPos(8, Direction.NORTH, blockPos));
            poss.add(MachineUtils.getOffsetPos(8, Direction.SOUTH, blockPos));
        }
        link(level, true);
        onMachineChanged();
    }

    private void link(Level level, boolean immediately) {
        if (immediately || getOffsetTimer() % 20 == 0 && level != null) poss.forEach(p -> {
            MetaMachine machine = getMachine(level, p);
            if (machine instanceof NanitesModuleMachine moduleMachine) {
                module.add(MODULE_MAP.get(moduleMachine.getDefinition()));
                moduleMachine.nanitesIntegratedMachine = this;
            }
        });
    }

    @Override
    public boolean onWorking() {
        link(getLevel(), false);
        return super.onWorking();
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) return false;
        int t = recipe.data.getInt("module");
        for (int i : module) {
            if (i == t) return super.beforeWorking(recipe);
        }
        return false;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        link(getLevel(), false);
        textList.add(Component.translatable("tooltip.emi.chance.consume", 100 - chance));
        textList.add(Component.translatable("gui.ae2.AttachedTo", ""));
        module.forEach(i -> textList.add(Component.translatable(MODULE_MAP.inverse().get(i).getDescriptionId())));
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        attachHighlightConfigurators(configuratorPanel);
    }

    @Override
    public int gto$getTemperature() {
        return coilTrait.getTemperature();
    }

    @Override
    public int getCoilTier() {
        return coilTrait.getCoilTier();
    }

    @Override
    public ICoilType getCoilType() {
        return coilTrait.getCoilType();
    }

    @Override
    public Set<BlockPos> getHighlightPos() {
        return poss;
    }
}
