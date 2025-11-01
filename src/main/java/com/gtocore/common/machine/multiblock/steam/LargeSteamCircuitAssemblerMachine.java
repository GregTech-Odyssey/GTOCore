package com.gtocore.common.machine.multiblock.steam;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@Scanned
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class LargeSteamCircuitAssemblerMachine extends BaseSteamMultiblockMachine {

    @Persisted
    private boolean isMultiMode = true;
    @DynamicInitialValue(
                         key = "gtceu.machine.multiblock.steam.large_circuit_assembler.max_parallel",
                         typeKey = DynamicInitialValueTypes.KEY_MAX_PARALLEL,
                         difficultyType = "machine.steam",
                         en = "Max Parallels",
                         cn = "最大并行数",
                         easyValue = "8",
                         normalValue = "4",
                         expertValue = "4")
    private static int MAX_PARALLELS = 4;
    @DynamicInitialValue(
                         key = "gtceu.machine.multiblock.steam.large_circuit_assembler.reduction_duration",
                         typeKey = DynamicInitialValueTypes.KEY_MULTIPLY,
                         difficultyType = "machine.normal",
                         easyValue = "2",
                         normalValue = "4",
                         expertValue = "6",
                         cn = "增产模式耗时为普通的 : %s 倍",
                         cnComment = "更多的时间，更高的产出，一报换一报这很合理",
                         en = "Multiply Mode, Recipe Duration: x %s",
                         enComment = "More time, higher output, this is reasonable")
    private static int RECIPE_DURATION_MULTIPLY = 4;
    @DynamicInitialValue(
                         key = "gtceu.machine.multiblock.steam.large_circuit_assembler.steam_cost",
                         typeKey = DynamicInitialValueTypes.KEY_MULTIPLY,
                         difficultyType = "machine.normal",
                         easyValue = "2",
                         normalValue = "4",
                         expertValue = "6",
                         cn = "增产模式蒸汽消耗为普通的 : %s 倍",
                         en = "Multiply Mode Steam Cost: x %s")
    private static int COST_STEAM_MULTIPLY = 4;
    @DynamicInitialValue(
                         key = "gtceu.machine.multiblock.steam.large_circuit_assembler.multiply",
                         typeKey = DynamicInitialValueTypes.KEY_MULTIPLY,
                         difficultyType = "machine.normal",
                         easyValue = "4",
                         normalValue = "2",
                         expertValue = "2",
                         cn = "增产模式倍率为普通的 : %s 倍",
                         en = "Multiply Mode Multiply Output: x %s")
    private static int PRODUCT_MULTIPLY = 4;
    @DynamicInitialValue(
                         key = "gtceu.machine.multiblock.steam.large_circuit_assembler.engraving_needed_amount",
                         typeKey = DynamicInitialValueTypes.KEY_AMOUNT,
                         difficultyType = "machine.normal",
                         cn = "蚀刻电路所需数量",
                         cnComment = """
                                 执行相应电路组装配方前，需要蚀刻此电路所需的物品数量""",
                         en = "Engraving Circuit Needed Amount",
                         enComment = """
                                 The amount of items needed to engrave the circuit before executing the corresponding circuit assembly recipe
                                 """,
                         easyValue = "8",
                         normalValue = "16",
                         expertValue = "32")
    private static int Engraving_needed_amount = 16;

    @Override
    boolean oc() {
        return true;
    }

    @Persisted
    private Item item;

    @Persisted
    private int count;

    public LargeSteamCircuitAssemblerMachine(MetaMachineBlockEntity holder) {
        super(holder, MAX_PARALLELS, 128, 1);
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(GTRecipe recipe) {
        if (count < Engraving_needed_amount) return null;
        Content content = recipe.outputs.get(ItemRecipeCapability.CAP).get(0);
        if (FastSizedIngredient.getInner(ItemRecipeCapability.CAP.of(content.getContent())).getItems()[0].getItem() == item) {
            if (isMultiMode) {
                recipe.outputs.put(ItemRecipeCapability.CAP, List.of(content.copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(PRODUCT_MULTIPLY))));
                recipe = super.getRealRecipe(recipe);
                if (recipe != null) {
                    recipe.duration = recipe.duration * RECIPE_DURATION_MULTIPLY;
                    ((Recipe) recipe).setEut(recipe.getInputEUt() * COST_STEAM_MULTIPLY);
                }
                return recipe;
            } else {
                return super.getRealRecipe(recipe);
            }
        }
        return null;
    }

    @RegisterLanguage(cn = "增产模式 : ", en = "Is Multiply Mode Enabled : ")
    private static final String IS_MULTIPLY = "gtocore.machine.large_steam_circuit_assembler.is_multiply";

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.large_steam_circuit_assembler.engrave_circuit"), "engraveCircuit"));
            textList.add(Component.translatable("gtocore.machine.large_steam_circuit_assembler.circuit", (item == null ? "null" : Component.translatable(item.getDescriptionId()))));
            if (item != null && count < Engraving_needed_amount) {
                textList.add(Component.translatable("gui.ae2.Missing", Engraving_needed_amount - count));
            }
            if (isMultiMode) textList.add(Component.translatable(IS_MULTIPLY, true).append(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.on"), "toggleMultiMode")));
            if (!isMultiMode) textList.add(Component.translatable(IS_MULTIPLY, false).append(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.off"), "toggleMultiMode")));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        super.handleDisplayClick(componentData, clickData);
        if (!clickData.isRemote && "toggleMultiMode".equals(componentData)) {
            isMultiMode = !isMultiMode;
        }
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
                                        c = Math.min(Engraving_needed_amount - count, c);
                                        count += c;
                                    } else {
                                        c = Math.min(Engraving_needed_amount, c);
                                        count = c;
                                    }
                                    item = stack.getItem();
                                    inv.extractItemInternal(i, c, false);
                                    if (count >= Engraving_needed_amount) return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
