package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IElectricItem;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.IGTTool;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
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

    public TreeGrowthSimulator(MetaMachineBlockEntity holder) {
        super(holder, 1, i -> {
            if (i.getItem() instanceof IGTTool item) {
                return item.getToolType() == GTToolType.CHAINSAW_LV || item.getToolType() == GTToolType.AXE;
            }
            return false;
        });
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        ItemStack stack = getStorageStack();
        if (stack.getItem() instanceof IGTTool item) {
            boolean isElectric = item.isElectric();
            if (isElectric) {
                IElectricItem electricStack = GTCapabilityHelper.getElectricItem(stack);
                if (electricStack == null) {
                    setIdleReason(IdleReason.FELLING_TOOL);
                    return null;
                }
                int eu = 256 * (1 << tier);
                if (electricStack.getCharge() < eu) {
                    setIdleReason(IdleReason.CHARGE);
                    return null;
                } else {
                    electricStack.discharge(eu * (1L << tier), electricStack.getTier(), true, false, false);
                }
            }
            if (!isElectric || GTValues.RNG.nextInt(10) == 0) {
                int damag = item.definition$getDamage(stack);
                if (damag >= item.definition$getMaxDamage(stack)) {
                    machineStorage.setStackInSlot(0, ItemStack.EMPTY);
                    setIdleReason(IdleReason.FELLING_TOOL);
                    return null;
                }
                item.definition$setDamage(stack, damag + 1);
            }
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
            return RecipeModifierFunction.overclocking(this, recipe);
        }
        setIdleReason(IdleReason.FELLING_TOOL);
        return null;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
    }

    @Override
    public void onMachineChanged() {
        output = 1;
        speed = 1;
        if (getStorageStack().getItem() instanceof IGTTool item) {
            GTToolType type = item.getToolType();
            if (type == GTToolType.CHAINSAW_LV) {
                output = 2;
            }
            speed = (float) (1 + 0.5 * Math.sqrt(item.getMaterial().getProperty(PropertyKey.TOOL).getHarvestSpeed()));
            getRecipeLogic().updateTickSubscription();
        }
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("tooltip.enderio.grinding_ball_main_output", output * 100));
        textList.add(Component.translatable("jade.horseStat.speed", "x " + FormattingUtil.formatNumbers(speed)));
    }
}
