package com.gto.gtocore.common.machine.multiblock.steam;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class largeSteamCircuitAssemblerMachine extends LargeSteamParallelMultiblockMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            largeSteamCircuitAssemblerMachine.class, LargeSteamParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    public Item item = null;

    public largeSteamCircuitAssemblerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, 8, args);
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, GTRecipe recipe, OCParams params, OCResult result) {
        if (machine instanceof largeSteamCircuitAssemblerMachine circuitAssemblerMachine) {
            Content content = recipe.outputs.get(ItemRecipeCapability.CAP).get(0);
            if (ItemRecipeCapability.CAP.of(content.getContent()).getItems()[0].getItem() == circuitAssemblerMachine.item) {
                GTRecipe recipe1 = recipe.copy();
                recipe1.tickInputs.put(EURecipeCapability.CAP, List.of(new Content(RecipeHelper.getInputEUt(recipe1) / 4,
                        ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
                recipe1.outputs.put(ItemRecipeCapability.CAP, List.of(content.copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(16))));
                return LargeSteamParallelMultiblockMachine.recipeModifier(machine, recipe1, 1);
            }
        }
        return null;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.large_steam_circuit_assembler.engrave_circuit"), "engraveCircuit"));
            textList.add(Component.translatable("gtocore.machine.large_steam_circuit_assembler.circuit", (item == null ? "null" : LocalizationUtils.format(item.getDescriptionId()))));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        super.handleDisplayClick(componentData, clickData);
        if (!clickData.isRemote && componentData.equals("engraveCircuit")) {
            for (IMultiPart part : getParts()) {
                if (part instanceof ItemBusPartMachine bus) {
                    NotifiableItemStackHandler inv = bus.getInventory();
                    IO io = inv.getHandlerIO();
                    if (io == IO.IN || io == IO.BOTH) {
                        for (int i = 0; i < inv.getSlots(); i++) {
                            ItemStack stack = inv.getStackInSlot(i);
                            for (TagKey<Item> tagKey : stack.getTags().toList()) {
                                if (tagKey.location().toString().contains("gtceu:circuits/")) {
                                    inv.extractItemInternal(i, 1, false);
                                    item = stack.getItem();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
