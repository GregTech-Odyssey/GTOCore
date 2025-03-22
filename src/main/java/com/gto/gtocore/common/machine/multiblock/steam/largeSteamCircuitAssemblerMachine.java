package com.gto.gtocore.common.machine.multiblock.steam;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class largeSteamCircuitAssemblerMachine extends LargeSteamParallelMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            largeSteamCircuitAssemblerMachine.class, LargeSteamParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private Item item;

    @Persisted
    private int count;

    public largeSteamCircuitAssemblerMachine(IMachineBlockEntity holder) {
        super(holder, 8, 128);
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(GTRecipe recipe) {
        if (count < 16) return null;
        Content content = recipe.outputs.get(ItemRecipeCapability.CAP).get(0);
        if (ItemRecipeCapability.CAP.of(content.getContent()).getItems()[0].getItem() == item) {
            recipe.outputs.put(ItemRecipeCapability.CAP, List.of(content.copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(4))));
            return recipeModifier(1.5).getModifier(this, recipe).apply(recipe);
        }
        return null;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.large_steam_circuit_assembler.engrave_circuit"), "engraveCircuit"));
            textList.add(Component.translatable("gtocore.machine.large_steam_circuit_assembler.circuit", (item == null ? "null" : LocalizationUtils.format(item.getDescriptionId()))));
            if (item != null && count < 16) {
                textList.add(Component.translatable("gui.ae2.Missing", 16 - count));
            }
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        super.handleDisplayClick(componentData, clickData);
        if (!clickData.isRemote && "engraveCircuit".equals(componentData)) {
            for (IMultiPart part : getParts()) {
                if (part instanceof ItemBusPartMachine bus) {
                    NotifiableItemStackHandler inv = bus.getInventory();
                    IO io = inv.getHandlerIO();
                    if (io == IO.IN || io == IO.BOTH) {
                        for (int i = 0; i < inv.getSlots(); i++) {
                            ItemStack stack = inv.getStackInSlot(i);
                            for (TagKey<Item> tagKey : stack.getTags().toList()) {
                                if (tagKey.location().toString().contains("gtceu:circuits/")) {
                                    int c = stack.getCount();
                                    if (stack.getItem() == item) {
                                        c = Math.min(16 - count, c);
                                        count += c;
                                    } else {
                                        c = Math.min(16, c);
                                        count = c;
                                    }
                                    item = stack.getItem();
                                    inv.extractItemInternal(i, c, false);
                                    if (count >= 16) return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
