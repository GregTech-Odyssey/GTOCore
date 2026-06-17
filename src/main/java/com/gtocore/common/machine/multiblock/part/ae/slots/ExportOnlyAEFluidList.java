package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtolib.api.recipe.RecipeType;
import com.gtolib.api.recipe.lookup.IIngredientConvertible;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableContentHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.transfer.fluid.ICustomFluidStackHandler;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlotList;
import com.gregtechceu.gtceu.utils.function.ObjLongPredicate;

import net.minecraftforge.fluids.FluidStack;

import appeng.api.stacks.AEFluidKey;

import com.fast.recipesearch.IntLongMap;
import com.gto.datasynclib.annotations.SaveToDisk;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

@Getter
public class ExportOnlyAEFluidList extends NotifiableContentHandler implements ICustomFluidStackHandler, IConfigurableSlotList {

    @SaveToDisk
    final ExportOnlyAEFluidSlot[] inventory;

    public ExportOnlyAEFluidList(MetaMachine machine, int slots) {
        this(machine, slots, ExportOnlyAEFluidSlot::new);
    }

    ExportOnlyAEFluidList(MetaMachine machine, int slots, Supplier<ExportOnlyAEFluidSlot> slotFactory) {
        super(machine, IO.IN);
        this.inventory = new ExportOnlyAEFluidSlot[slots];
        for (int i = 0; i < slots; i++) {
            this.inventory[i] = slotFactory.get();
            this.inventory[i].setHandler(this);
        }
    }

    @Override
    public boolean updateEmpty() {
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            return false;
        }
        return true;
    }

    @Override
    public int getTanks() {
        return inventory.length;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return inventory[tank].getStack();
    }

    @Override
    public int getTankCapacity(int i) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return true;
    }

    @Override
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {}

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        return FluidStack.EMPTY;
    }

    @Override
    public boolean supportsFill(int tank) {
        return false;
    }

    @Override
    public FluidStack drainInternal(FluidStack resource, FluidAction action) {
        var amount = resource.getAmount();
        if (amount < 1) return FluidStack.EMPTY;
        var drained = 0;
        var simulate = action.simulate();
        for (var storage : inventory) {
            if (storage.stock != null && storage.stock.what() instanceof AEFluidKey fluidKey && fluidKey.matches(resource)) {
                drained += MathUtil.saturatedCast(storage.extract(amount - drained, simulate, true));
                if (drained >= amount) break;
            }
        }
        return drained > 0 ? ICustomFluidStackHandler.copy(resource, drained) : FluidStack.EMPTY;
    }

    @Override
    public boolean canHandleFluid() {
        return true;
    }

    @Override
    public boolean handleRecipeFluid(IO io, GTRecipe recipe, List<Content<FluidIngredient>> fluids, boolean simulate) {
        if (io == IO.IN) {
            boolean changed = false;
            for (var it = fluids.iterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                for (var i : inventory) {
                    var stored = i.stock;
                    if (stored == null) continue;
                    long amount = stored.amount();
                    if (amount == 0) continue;
                    if (stored.what() instanceof AEFluidKey fluidKey && ingredient.inner.testAeKay(fluidKey)) {
                        var drained = i.extract(ingredient.amount, simulate, false);
                        if (drained > 0) {
                            changed = true;
                            ingredient.shrink(drained);
                            if (ingredient.amount <= 0) {
                                it.remove();
                                break;
                            }
                        }
                    }
                }
            }
            if (!simulate && changed) {
                onContentsChanged();
            }
        }
        return fluids.isEmpty();
    }

    @Override
    public boolean forEachFluids(ObjLongPredicate<FluidStack> function) {
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            if (function.test(i.getReadOnlyStack(), stock.amount())) return true;
        }
        return false;
    }

    @Override
    public void fastForEachFluids(ObjLongConsumer<FluidStack> function) {
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            function.accept(i.getReadOnlyStack(), stock.amount());
        }
    }

    @Override
    public void fillSearchMap(@NotNull GTRecipeType type, @NotNull IntLongMap map) {
        boolean specialConverter = ((RecipeType) type).specialConverter;
        for (var i : inventory) {
            if (i.config == null) continue;
            var stock = i.stock;
            if (stock == null || stock.amount() == 0) continue;
            if (stock.what() instanceof AEFluidKey fluidKey) {
                if (specialConverter) {
                    type.convertFluid(i.getReadOnlyStack(), stock.amount(), map);
                } else {
                    ((IIngredientConvertible) (Object) fluidKey).gtolib$convert(stock.amount(), map);
                }
            }
        }
    }

    @Override
    public IConfigurableSlot getConfigurableSlot(int index) {
        return inventory[index];
    }

    @Override
    public int getConfigurableSlots() {
        return inventory.length;
    }

    public boolean isAutoPull() {
        return false;
    }

    public boolean isStocking() {
        return false;
    }
}
