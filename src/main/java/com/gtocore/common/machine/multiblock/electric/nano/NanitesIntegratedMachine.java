package com.gtocore.common.machine.multiblock.electric.nano;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.MultiBlockC;

import com.gtolib.api.machine.feature.multiblock.IHighlightMachine;
import com.gtolib.api.machine.feature.multiblock.IStorageMultiblock;
import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class NanitesIntegratedMachine extends CoilCrossRecipeMultiblockMachine implements IHighlightMachine, IStorageMultiblock {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(NanitesIntegratedMachine.class, CoilCrossRecipeMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final Int2ObjectOpenHashMap<MachineDefinition> MODULE_MAP = new Int2ObjectOpenHashMap<>();

    static {
        MODULE_MAP.put(1, MultiBlockC.ORE_EXTRACTION_MODULE);
        MODULE_MAP.put(2, MultiBlockC.BIOENGINEERING_MODULE);
        MODULE_MAP.put(3, MultiBlockC.POLYMER_TWISTING_MODULE);
    }

    private static final Map<Material, Float> MATERIAL_MAP = Map.of(
            GTMaterials.Iron, 1.0F,
            GTMaterials.Iridium, 1.1F,
            GTOMaterials.Orichalcum, 1.2F,
            GTOMaterials.Infuscolium, 1.3F,
            GTOMaterials.Draconium, 1.4F,
            GTOMaterials.CosmicNeutronium, 1.5F,
            GTOMaterials.Eternity, 1.6F);

    private static final Map<Material, Integer> MATERIAL_TIER_MAP = Map.of(
            GTMaterials.Iron, GTValues.ZPM,
            GTMaterials.Iridium, GTValues.UV,
            GTOMaterials.Orichalcum, GTValues.UHV,
            GTOMaterials.Infuscolium, GTValues.UEV,
            GTOMaterials.Draconium, GTValues.UIV,
            GTOMaterials.CosmicNeutronium, GTValues.UXV,
            GTOMaterials.Eternity, GTValues.OpV);

    int chance;
    @DescSynced
    private final List<BlockPos> poss = new ArrayList<>(2);
    private final IntOpenHashSet module = new IntOpenHashSet();
    @DescSynced
    @Persisted
    private final NotifiableItemStackHandler machineStorage;

    public NanitesIntegratedMachine(MetaMachineBlockEntity holder) {
        super(holder, false, true, false, true, MachineUtils::getHatchParallel);
        machineStorage = createMachineStorage(i -> {
            MaterialEntry entry = ChemicalHelper.getMaterialEntry(i.getItem());
            return entry.tagPrefix() == GTOTagPrefix.NANITES && MATERIAL_MAP.containsKey(entry.material());
        });
    }

    @Override
    public void onMachineChanged() {
        if (isEmpty()) {
            chance = 0;
            return;
        }
        Material material = ChemicalHelper.getMaterialEntry(getStorageStack().getItem()).material();
        if (MATERIAL_TIER_MAP.get(material) > getTier()) return;
        chance = (int) (getStorageStack().getCount() * MATERIAL_MAP.get(material));
    }

    static void trimRecipe(GTRecipe recipe, int chance) {
        if (GTValues.RNG.nextInt(100) < chance) {
            var input = new ArrayList<>(recipe.inputs.get(ItemRecipeCapability.CAP));
            input.remove(0);
            var output = new ArrayList<>(recipe.outputs.get(ItemRecipeCapability.CAP));
            output.remove(0);
            recipe.inputs.put(ItemRecipeCapability.CAP, input);
            recipe.outputs.put(ItemRecipeCapability.CAP, output);
        }
    }

    @Override
    protected Recipe fullModifyRecipe(@NotNull Recipe recipe) {
        if (module.contains(recipe.data.getInt("module"))) {
            recipe = super.fullModifyRecipe(recipe);
            if (recipe != null) {
                trimRecipe(recipe, chance);
                return recipe;
            }
        }
        return null;
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
        onMachineChanged();
    }

    @Override
    protected void onStructureFormedAfter() {
        super.onStructureFormedAfter();
        update(getLevel(), true);
    }

    private void update(Level level, boolean immediately) {
        if (immediately || getOffsetTimer() % 20 == 0 && level != null) poss.forEach(p -> {
            MetaMachine machine = getMachine(level, p);
            if (machine instanceof NanitesModuleMachine moduleMachine && moduleMachine.isFormed()) {
                if (module.add(moduleMachine.type)) {
                    getRecipeLogic().updateTickSubscription();
                }
                if (moduleMachine.nanitesIntegratedMachine != this) moduleMachine.getRecipeLogic().updateTickSubscription();
                moduleMachine.nanitesIntegratedMachine = this;
            }
        });
    }

    @Override
    public boolean onWorking() {
        update(getLevel(), false);
        return super.onWorking();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        update(getLevel(), false);
        textList.add(Component.translatable("tooltip.emi.chance.consume", Math.max(100 - chance, 0)));
        textList.add(Component.translatable("gui.ae2.AttachedTo", ""));
        module.forEach(i -> textList.add(Component.translatable(MODULE_MAP.get(i).getDescriptionId())));
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        attachHighlightConfigurators(configuratorPanel);
    }

    @Override
    @NotNull
    public Widget createUIWidget() {
        return createUIWidget(super.createUIWidget());
    }

    @Override
    public List<BlockPos> getHighlightPos() {
        return poss;
    }

    @Override
    public NotifiableItemStackHandler getMachineStorage() {
        return this.machineStorage;
    }
}
