package com.gto.gtocore.common.machine.multiblock.electric.nano;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.machine.feature.multiblock.IHighlightMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IStorageMultiblock;
import com.gto.gtocore.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.machines.MultiBlockC;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.google.common.collect.HashBiMap;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class NanitesIntegratedMachine extends CoilCrossRecipeMultiblockMachine implements IHighlightMachine, IStorageMultiblock {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            NanitesIntegratedMachine.class, CoilCrossRecipeMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    static final HashBiMap<MachineDefinition, Integer> MODULE_MAP = HashBiMap.create(3);

    static {
        MODULE_MAP.put(MultiBlockC.ORE_EXTRACTION_MODULE, 1);
        MODULE_MAP.put(MultiBlockC.BIOENGINEERING_MODULE, 2);
        MODULE_MAP.put(MultiBlockC.POLYMER_TWISTING_MODULE, 3);
    }

    private static final Map<Material, Float> MATERIAL_MAP = Map.of(
            GTMaterials.Iron, 1F,
            GTMaterials.Iridium, 1.1F,
            GTOMaterials.Orichalcum, 1.2F,
            GTOMaterials.Infuscolium, 1.3F,
            GTOMaterials.Draconium, 1.4F,
            GTOMaterials.CosmicNeutronium, 1.5F,
            GTOMaterials.Eternity, 1.6F);

    int chance;

    @DescSynced
    private final Set<BlockPos> poss = new ObjectOpenHashSet<>(4, 0.9F);
    private final IntOpenHashSet module = new IntOpenHashSet(4, 0.9F);

    @DescSynced
    @Persisted
    @Getter
    private final NotifiableItemStackHandler machineStorage;

    public NanitesIntegratedMachine(IMachineBlockEntity holder) {
        super(holder, false, true, false, true, MachineUtils::getHatchParallel);
        machineStorage = createMachineStorage(i -> {
            MaterialEntry entry = ChemicalHelper.getMaterialEntry(i.getItem());
            return entry.tagPrefix() == GTOTagPrefix.NANITES && MATERIAL_MAP.containsKey(entry.material());
        });
    }

    @Override
    public void onMachineChanged() {
        Material material = ChemicalHelper.getMaterialEntry(getStorageStack().getItem()).material();
        chance = (int) (getStorageStack().getCount() * MATERIAL_MAP.get(material));
    }

    static void trimRecipe(GTRecipe recipe, int chance) {
        if (GTValues.RNG.nextInt(100) < chance) {
            var input = new ObjectArrayList<>(recipe.inputs.get(ItemRecipeCapability.CAP));
            input.remove(0);
            var output = new ObjectArrayList<>(recipe.outputs.get(ItemRecipeCapability.CAP));
            output.remove(0);
            recipe.inputs.put(ItemRecipeCapability.CAP, input);
            recipe.outputs.put(ItemRecipeCapability.CAP, output);
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
    public @NotNull Widget createUIWidget() {
        return createUIWidget(super.createUIWidget());
    }

    @Override
    public Set<BlockPos> getHighlightPos() {
        return poss;
    }
}
