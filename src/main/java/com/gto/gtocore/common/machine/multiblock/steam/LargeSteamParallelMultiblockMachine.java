package com.gto.gtocore.common.machine.multiblock.steam;

import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.machine.multiblock.part.LargeSteamHatchPartMachine;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeSteamParallelMultiblockMachine extends SteamParallelMultiblockMachine {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            LargeSteamParallelMultiblockMachine.class, SteamParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    private boolean isOC;

    @Persisted
    private int amountOC;

    public LargeSteamParallelMultiblockMachine(IMachineBlockEntity holder, int maxParallels) {
        super(holder, maxParallels);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        isOC = getParts().stream().anyMatch(LargeSteamHatchPartMachine.class::isInstance);
    }

    public static RecipeModifier recipeModifier(double reductionDuration) {
        return (machine, r) -> recipe -> {
            long eut = RecipeHelper.getInputEUt(recipe);
            if (machine instanceof LargeSteamParallelMultiblockMachine steamMachine && eut < (steamMachine.isOC ? 128 : 32)) {
                recipe = GTORecipeModifiers.accurateParallel(machine, recipe, steamMachine.maxParallels);
                recipe.duration = (int) (recipe.duration * reductionDuration);
                if (steamMachine.isOC) {
                    recipe.tickInputs.put(EURecipeCapability.CAP, List.of(new Content((long) Math.max(1, eut * Math.pow(3, steamMachine.amountOC)), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
                    recipe.duration = Math.max(1, recipe.duration / (1 << (steamMachine.amountOC)));
                }
                return recipe;
            }
            return null;
        };
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed() && isOC) {
            textList.add(Component.translatable("gtocore.machine.oc_amount", amountOC)
                    .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            Component.translatable("gtocore.machine.steam_parallel_machine.oc")))));

            textList.add(Component.translatable("gtocore.machine.steam_parallel_machine.modification_oc")
                    .append(ComponentPanelWidget.withButton(Component.literal("[-] "), "ocSub"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "ocAdd")));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote && isOC) {
            amountOC = Mth.clamp(amountOC + ("ocAdd".equals(componentData) ? 1 : -1), 0, 4);
        }
    }

    @Override
    public void setCleanroom(@Nullable ICleanroomProvider provider) {
        if (provider instanceof CleanroomMachine) return;
        super.setCleanroom(provider);
    }
}
