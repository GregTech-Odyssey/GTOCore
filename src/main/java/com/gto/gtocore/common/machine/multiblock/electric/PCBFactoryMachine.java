package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class PCBFactoryMachine extends StorageMultiblockMachine {

    public PCBFactoryMachine(IMachineBlockEntity holder) {
        super(holder, 64, i -> ChemicalHelper.getPrefix(i.getItem()) == GTOTagPrefix.nanites);
    }

    private double reductionEUt = 1, reductionDuration = 1;

    private void getPCBReduction() {
        ItemStack itemStack = getStorageStack();
        String item = ItemUtils.getId(itemStack);
        if (Objects.equals(item, "gtocore:vibranium_nanites")) {
            reductionDuration = (double) (100 - itemStack.getCount()) / 100;
            reductionEUt = 0.25;
        } else if (Objects.equals(item, "gtceu:gold_nanites")) {
            reductionDuration = (100 - (itemStack.getCount() * 0.5)) / 100;
        }
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        getPCBReduction();
        return GTORecipeModifiers.overclocking(this, GTORecipeModifiers.hatchParallel(this, recipe), true, false, reductionEUt, reductionDuration);
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (getOffsetTimer() % 10 == 0) {
            getPCBReduction();
        }
        textList.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", reductionEUt));
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", reductionDuration));
    }
}
