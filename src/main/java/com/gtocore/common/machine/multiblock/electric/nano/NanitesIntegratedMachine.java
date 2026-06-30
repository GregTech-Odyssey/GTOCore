package com.gtocore.common.machine.multiblock.electric.nano;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.data.machines.MultiBlockC;

import com.gtolib.api.machine.feature.multiblock.IStorageMultiblock;
import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.network.chat.Component;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class NanitesIntegratedMachine extends CoilCrossRecipeMultiblockMachine implements IStorageMultiblock {

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

    final IntOpenHashSet module = new IntOpenHashSet();

    @SyncToClient
    @SaveToDisk
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
        chance = 0;
        if (isEmpty()) {
            return;
        }
        Material material = ChemicalHelper.getMaterialEntry(getStorageStack().getItem()).material();
        if (!MATERIAL_TIER_MAP.containsKey(material) || MATERIAL_TIER_MAP.get(material) > getTier()) return;
        chance = Math.min(100, (int) (getStorageStack().getCount() * MATERIAL_MAP.get(material)));
    }

    static void trimRecipe(GTRecipe recipe, int chance) {
        if (GTValues.RNG.nextInt(100) < chance) {
            recipe.itemInputs = RecipeHelper.trimLast(recipe.itemInputs, recipe.itemInputs.size() - 1);
            recipe.itemOutputs = RecipeHelper.trimLast(recipe.itemOutputs, recipe.itemOutputs.size() - 1);
        }
    }

    @Override
    public GTRecipe fullModifyRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipeDefinition definition) {
        if (module.contains(definition.data.getInt(GTORecipeDataKeys.MODULE))) {
            var recipe = super.fullModifyRecipe(unit, definition);
            if (recipe != null) {
                trimRecipe(recipe, chance);
                return recipe;
            }
        }
        return null;
    }

    @Override
    public void onStructureFormed() {
        module.clear();
        super.onStructureFormed();
        onMachineChanged();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("tooltip.emi.chance.consume", Math.max(100 - chance, 0)));
        textList.add(Component.translatable("gui.ae2.AttachedTo", ""));
        module.forEach(i -> textList.add(Component.translatable(MODULE_MAP.get(i).getDescriptionId())));
    }

    @Override
    @NotNull
    public Widget createUIWidget() {
        return createUIWidget(super.createUIWidget());
    }

    @Override
    public NotifiableItemStackHandler getMachineStorage() {
        return this.machineStorage;
    }
}
