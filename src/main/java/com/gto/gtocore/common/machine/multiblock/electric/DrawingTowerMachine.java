package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.CoilMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.machine.multiblock.part.SpoolHatchPartMachine;
import com.gto.gtocore.utils.FunctionContainer;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class DrawingTowerMachine extends CoilMultiblockMachine {

    private SpoolHatchPartMachine spoolHatchPartMachine;

    private int height;

    private double reduction = 1;

    public DrawingTowerMachine(IMachineBlockEntity holder) {
        super(holder, false, false);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (spoolHatchPartMachine == null && part instanceof SpoolHatchPartMachine spoolHatchPart) {
            spoolHatchPartMachine = spoolHatchPart;
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        FunctionContainer<Integer, ?> container = getMultiblockState().getMatchContext().get("laminated_glass");
        if (container != null) {
            height = container.getValue();
        }
        reduction = 2 / Math.pow(1.5, ((height / 8D) * ((gto$getTemperature() - 5000D) / 900D)));
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        height = 0;
        spoolHatchPartMachine = null;
    }

    @Override
    protected @Nullable GTRecipe getRealRecipe(GTRecipe recipe) {
        if (spoolHatchPartMachine == null) return null;
        CustomItemStackHandler storage = spoolHatchPartMachine.getInventory().storage;
        ItemStack item = storage.getStackInSlot(0);
        int tier = getItemTier(item);

        // Check if the item is a valid spool and matches the required tier in the recipe
        if (tier == recipe.data.getInt("spool")) {
            // Decrease the item count instead of increasing damage
            if (item.getCount() > 1) {
                item.shrink(1); // Reduce the stack size by one
                storage.setStackInSlot(0, item);
            } else {
                storage.setStackInSlot(0, ItemStack.EMPTY); // Remove the item if only one left
            }
            recipe.duration = (int) (recipe.duration * reduction);
            return GTORecipeModifiers.hatchParallel(this, recipe);
        }
        return null;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.height", height));
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", reduction));
    }

    private static int getItemTier(ItemStack item) {
        if (item.isEmpty()) {
            return 0;
        }
        return SpoolHatchPartMachine.SPOOL.getOrDefault(item.getItem(), 0);
    }
}
