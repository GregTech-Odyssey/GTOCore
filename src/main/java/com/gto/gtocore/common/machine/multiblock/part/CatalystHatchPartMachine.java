package com.gto.gtocore.common.machine.multiblock.part;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CatalystHatchPartMachine extends TieredIOPartMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CatalystHatchPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableItemStackHandler inventory;

    public CatalystHatchPartMachine(IMachineBlockEntity holder) {
        super(holder, 7, IO.IN);
        this.inventory = new NotifiableCatalystHandler(this);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public Widget createUIWidget() {
        int rowSize = 4;
        var group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * rowSize + 16);
        var container = new WidgetGroup(4, 4, 18 * rowSize + 8, 18 * rowSize + 8);
        int index = 0;
        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                container.addWidget(new SlotWidget(inventory.storage, index++, 4 + x * 18, 4 + y * 18, true, io.support(IO.IN)).setBackgroundTexture(GuiTextures.SLOT).setIngredientIO(this.io == IO.IN ? IngredientIO.INPUT : IngredientIO.OUTPUT));
            }
        }
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(container);
        return group;
    }

    private static class NotifiableCatalystHandler extends NotifiableItemStackHandler {

        NotifiableCatalystHandler(MetaMachine machine) {
            super(machine, 16, IO.IN, IO.BOTH);
            setFilter(i -> ChemicalHelper.getPrefix(i.getItem()) == GTOTagPrefix.catalyst || i.is(GTOItems.CATALYST_BASE.get()));
        }

        @Override
        public List<Object> getContents() {
            List<ItemStack> stacks = new ArrayList<>();
            for (int i = 0; i < getSlots(); ++i) {
                ItemStack stack = getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Item item = stack.getItem();
                    stacks.add(new ItemStack(item, stack.getDamageValue() * stack.getDamageValue()));
                }
            }
            return Arrays.asList(stacks.toArray());
        }

        @Override
        @Nullable
        public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, @Nullable String slotName, boolean simulate) {
            if (io != IO.IN) return left;
            for (var it = left.listIterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                var items = ingredient.getItems();
                if (items.length == 0 || items[0].isEmpty()) {
                    it.remove();
                    continue;
                }
                int count;
                if (ingredient instanceof SizedIngredient si) count = si.getAmount();
                else count = items[0].getCount();
                for (int slot = 0; slot < storage.getSlots(); ++slot) {
                    ItemStack stored = storage.getStackInSlot(slot);
                    if (items[0].is(stored.getItem())) {
                        int damageValue = stored.getDamageValue();
                        if (damageValue > 9999) {
                            storage.setStackInSlot(slot, GTOItems.CATALYST_BASE.asStack());
                            continue;
                        }
                        int amount = Math.min((int) Math.sqrt(count), 10000 - damageValue);
                        if (!simulate) stored.setDamageValue(damageValue + amount);
                        count = count - amount * amount;
                        if (count < 1) {
                            it.remove();
                            break;
                        }
                    }
                }
                if (count > 0) {
                    if (ingredient instanceof SizedIngredient si) {
                        si.setAmount(count);
                    } else {
                        items[0].setCount(count);
                    }
                }
            }
            return left.isEmpty() ? null : left;
        }
    }

    @Override
    public boolean canShared() {
        return false;
    }
}
