package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.data.IdleReason;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.item.IElectricItem;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.IGTTool;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@DataGeneratorScanned
public final class TreeGrowthSimulator extends StorageMultiblockMachine {

    @RegisterLanguage(cn = "主产物产出%s%%", en = "Main Output %s%%")
    private static final String MAIN = "gtocore.machine.main_output";

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
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        if (!canMatchFellingTool(getStorageStack())) return null;
        recipe.duration = (int) (recipe.duration / speed);
        if (output > 1) {
            var contents = recipe.itemOutputs;
            var content = contents.get(0).copy(2);
            if (contents.size() > 1) {
                recipe.itemOutputs = List.of(content, contents.get(1));
            } else {
                recipe.itemOutputs = List.of(content);
            }
        }
        return RecipeModifier.overclocking(this, unit, recipe);
    }

    @Override
    public boolean handleRecipeInput(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        ToolUsePlan toolUsePlan = getToolUsePlan();
        if (toolUsePlan == null) return false;
        if (!super.handleRecipeInput(unit, recipe)) return false;
        applyToolUse(toolUsePlan);
        return true;
    }

    private boolean canMatchFellingTool(ItemStack stack) {
        if (!(stack.getItem() instanceof IGTTool item)) {
            setIdleReason(IdleReason.FELLING_TOOL);
            return false;
        }
        if (item.isElectric()) {
            IElectricItem electricStack = GTCapabilityHelper.getElectricItem(stack);
            if (electricStack == null) {
                setIdleReason(IdleReason.FELLING_TOOL);
                return false;
            }
            if (electricStack.getCharge() < getFellingToolChargeCheck()) {
                setIdleReason(IdleReason.CHARGE);
                return false;
            }
        } else if (stack.getDamageValue() >= stack.getMaxDamage()) {
            setIdleReason(IdleReason.FELLING_TOOL);
            return false;
        }
        return true;
    }

    @Nullable
    private ToolUsePlan getToolUsePlan() {
        ItemStack stack = getStorageStack();
        if (!(stack.getItem() instanceof IGTTool item)) {
            setIdleReason(IdleReason.FELLING_TOOL);
            return null;
        }

        boolean isElectric = item.isElectric();
        IElectricItem electricStack = null;
        if (isElectric) {
            electricStack = GTCapabilityHelper.getElectricItem(stack);
            if (electricStack == null) {
                setIdleReason(IdleReason.FELLING_TOOL);
                return null;
            }
            if (electricStack.getCharge() < getFellingToolChargeCheck()) {
                setIdleReason(IdleReason.CHARGE);
                return null;
            }
        }

        boolean damageTool = !isElectric || GTValues.RNG.nextInt(10) == 0;
        boolean applyDamage = false;
        if (damageTool) {
            if (stack.getDamageValue() >= stack.getMaxDamage()) {
                machineStorage.setStackInSlot(0, ItemStack.EMPTY);
                setIdleReason(IdleReason.FELLING_TOOL);
                return null;
            }
            int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) + 1;
            applyDamage = GTValues.RNG.nextInt() % unbreakingLevel == 0;
        }

        return new ToolUsePlan(stack, electricStack, isElectric, applyDamage);
    }

    private void applyToolUse(ToolUsePlan toolUsePlan) {
        if (toolUsePlan.isElectric()) {
            toolUsePlan.electricStack()
                    .discharge(getFellingToolDischarge(), toolUsePlan.electricStack().getTier(), true, false, false);
        }
        if (toolUsePlan.applyDamage()) {
            int damage = toolUsePlan.stack().getDamageValue() + 1;
            if (damage >= toolUsePlan.stack().getMaxDamage()) {
                machineStorage.setStackInSlot(0, ItemStack.EMPTY);
            } else {
                toolUsePlan.stack().setDamageValue(damage);
            }
        }
    }

    private int getFellingToolChargeCheck() {
        return 256 * (1 << tier);
    }

    private long getFellingToolDischarge() {
        return getFellingToolChargeCheck() * (1L << tier);
    }

    private record ToolUsePlan(ItemStack stack, @Nullable IElectricItem electricStack, boolean isElectric,
                               boolean applyDamage) {}

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
        textList.add(Component.translatable(MAIN, output * 100));
        textList.add(Component.translatable("jade.horseStat.speed", "x " + FormattingUtil.formatNumbers(speed)));
    }
}
