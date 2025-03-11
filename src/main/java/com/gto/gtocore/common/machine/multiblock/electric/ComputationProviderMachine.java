package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.utils.StringUtils;

import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class ComputationProviderMachine extends StorageMultiblockMachine implements IOpticalComputationProvider {

    private static final Map<Integer, Item> MAINFRAME = Map.of(0,
            GTOItems.OPTICAL_MAINFRAME.asItem(), 1,
            GTOItems.EXOTIC_MAINFRAME.asItem(), 2,
            GTOItems.COSMIC_MAINFRAME.asItem(), 3,
            GTOItems.SUPRACAUSAL_MAINFRAME.asItem(), 4,
            GTOItems.SUPRACHRONAL_MAINFRAME_COMPLEX.asItem());

    private GTRecipe recipe;
    private int allocatedCWUt;
    private int maxCWUt;
    private final boolean inf;

    public ComputationProviderMachine(IMachineBlockEntity holder, boolean inf) {
        super(holder, 8, i -> MAINFRAME.containsValue(i.getItem()));
        this.inf = inf;
    }

    @Override
    public SoundEntry getSound() {
        return GTSoundEntries.COMPUTATION;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
        if (getOverclockVoltage() > 0) recipe = GTORecipeBuilder.ofRaw().EUt(getOverclockVoltage()).duration(20).buildRawRecipe();
    }

    @Override
    protected void onMachineChanged() {
        maxCWUt = 0;
        if (inf) {
            if (getTier() == 14 && getStorageStack().is(MAINFRAME.get(4))) {
                maxCWUt = Integer.MAX_VALUE;
            }
        } else if (getStorageStack().getCount() > 7) {
            if (getTier() == 11 && getStorageStack().is(MAINFRAME.get(0))) {
                maxCWUt = 1024;
            } else if (getTier() == 12 && getStorageStack().is(MAINFRAME.get(1))) {
                maxCWUt = 2048;
            } else if (getTier() == 13 && getStorageStack().is(MAINFRAME.get(2))) {
                maxCWUt = 4096;
            } else if (getTier() == 14 && getStorageStack().is(MAINFRAME.get(3))) {
                maxCWUt = 8192;
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        allocatedCWUt = 0;
        super.onStructureInvalid();
    }

    @Override
    public void afterWorking() {
        allocatedCWUt = 0;
        super.afterWorking();
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        Component cwutInfo = Component.literal(allocatedCWUt + " / " + (inf ? StringUtils.full_color("infinity") : getMaxCWUt())).append(Component.literal(" CWU/t")).withStyle(ChatFormatting.AQUA);
        textList.add(Component.translatable("gtceu.multiblock.hpca.computation", cwutInfo).withStyle(ChatFormatting.GRAY));
    }

    private int requestCWUt(int c, Collection<IOpticalComputationProvider> collection) {
        if (!inf) {
            collection.add(this);
        }
        if (c > maxCWUt) return 0;
        allocatedCWUt = c;
        return c;
    }

    @Override
    public int requestCWUt(int i, boolean b, Collection<IOpticalComputationProvider> collection) {
        if (recipe != null) {
            if (getRecipeLogic().isWorking()) {
                return requestCWUt(i, collection);
            } else if (RecipeRunner.matchTickRecipe(this, recipe)) {
                getRecipeLogic().setupRecipe(recipe);
                if (getRecipeLogic().isWorking()) {
                    return requestCWUt(i, collection);
                }
            }
        }
        collection.add(this);
        return 0;
    }

    @Override
    public int getMaxCWUt(Collection<IOpticalComputationProvider> collection) {
        collection.add(this);
        return isWorkingEnabled() ? maxCWUt : 0;
    }

    @Override
    public boolean canBridge(Collection<IOpticalComputationProvider> collection) {
        collection.add(this);
        return true;
    }
}
