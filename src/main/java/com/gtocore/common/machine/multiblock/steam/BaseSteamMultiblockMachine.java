package com.gtocore.common.machine.multiblock.steam;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
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
public class BaseSteamMultiblockMachine extends SteamParallelMultiblockMachine {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            BaseSteamMultiblockMachine.class, SteamParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    boolean isOC;

    @Persisted
    private int amountOC;

    private final int eut;
    private final double durationMultiplier;

    public BaseSteamMultiblockMachine(MetaMachineBlockEntity holder, int maxParallels, int eut, double durationMultiplier) {
        super(holder, maxParallels);
        this.eut = eut;
        this.durationMultiplier = durationMultiplier;
    }

    BaseSteamMultiblockMachine(MetaMachineBlockEntity holder, int maxParallels, double durationMultiplier) {
        this(holder, maxParallels, 32, durationMultiplier);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(GTRecipe r) {
        Recipe recipe = (Recipe) r;
        long eut = recipe.getInputEUt();
        if (eut < (isOC ? (long) this.eut << 2 : this.eut)) {
            recipe = ParallelLogic.accurateParallel(this, recipe, getMaxParallels());
            if (recipe == null) return null;
            recipe.duration = (int) (recipe.duration * durationMultiplier);
            if (isOC) {
                eut *= recipe.getParallels();
                recipe.setEut(eut << (amountOC << 1));
                recipe.duration = Math.max(1, recipe.duration / (1 << amountOC));
            }
            return recipe;
        }
        return null;
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
