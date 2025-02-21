package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.tool.GTToolItem;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class TreeGrowthSimulator extends StorageMultiblockMachine {

    private int output = 1;
    private float speed = 1;

    public TreeGrowthSimulator(IMachineBlockEntity holder) {
        super(holder, 1, i -> i.getItem() instanceof GTToolItem item && (item.getToolType() == GTToolType.CHAINSAW_LV || item.getToolType() == GTToolType.AXE));
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        ItemStack stack = getStorageStack();
        if (!stack.isEmpty() && stack.getItem() instanceof GTToolItem item) {
            int damag = item.definition$getDamage(stack);
            if (damag > item.definition$getMaxDamage(stack)) {
                machineStorage.setStackInSlot(0, ItemStack.EMPTY);
                return null;
            }
            item.definition$setDamage(stack, damag + 1);
            recipe.duration = (int) (recipe.duration / speed);
            if (output > 1) {
                List<Content> contents = recipe.outputs.get(ItemRecipeCapability.CAP);
                Content content = contents.get(0).copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(2));
                if (contents.size() > 1) {
                    recipe.outputs.put(ItemRecipeCapability.CAP, List.of(content, contents.get(1)));
                } else {
                    recipe.outputs.put(ItemRecipeCapability.CAP, List.of(content));
                }
            }
            return GTORecipeModifiers.overclocking(this, recipe);
        }
        return null;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
    }

    @Override
    protected void onMachineChanged() {
        output = 1;
        speed = 1;
        if (getStorageStack().getItem() instanceof GTToolItem item) {
            GTToolType type = item.getToolType();
            if (type == GTToolType.CHAINSAW_LV) {
                output = 2;
            }
            speed = (float) (1 + 0.5 * Math.sqrt(item.getMaterial().getProperty(PropertyKey.TOOL).getHarvestSpeed()));
        }
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("tooltip.enderio.grinding_ball_main_output", output * 100));
        textList.add(Component.translatable("jade.horseStat.speed", "x " + FormattingUtil.formatNumbers(speed)));
    }
}
