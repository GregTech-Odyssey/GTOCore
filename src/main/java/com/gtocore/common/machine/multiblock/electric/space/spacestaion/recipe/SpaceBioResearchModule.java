package com.gtocore.common.machine.multiblock.electric.space.spacestaion.recipe;

import com.gtocore.common.machine.multiblock.electric.space.spacestaion.RecipeExtension;
import com.gtocore.common.machine.trait.RadioactivityTrait;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import com.google.common.collect.ImmutableSet;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

@DataGeneratorScanned
public class SpaceBioResearchModule extends RecipeExtension {

    @Persisted
    private final RadioactivityTrait radioactivityTrait;

    @Persisted
    private int radioactivity = 40;

    public SpaceBioResearchModule(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
        radioactivityTrait = new Trait(this);
    }

    @Override
    public Recipe getRealRecipe(@NotNull Recipe recipe) {
        if (recipe.data.contains("filter_casing") && recipe.data.getInt("filter_casing") > core.getTypes().size()) {
            ((IEnhancedRecipeLogic) getRecipeLogic())
                    .gtolib$setIdleReason(Component.translatable(LANGUAGE_INSUFFICIENT_CLEANROOM));
            return null;
        }
        return super.getRealRecipe(recipe);
    }

    @Override
    public Set<BlockPos> getModulePositions() {
        var pos = getPos();
        var fFacing = getFrontFacing();
        var uFacing = getUpwardsFacing();
        boolean isFlipped = isFlipped();
        var hallwayCenter = pos.relative(fFacing, 2).relative(RelativeDirection.LEFT.getRelative(fFacing, uFacing, isFlipped), 23);
        ImmutableSet.Builder<BlockPos> builder = ImmutableSet.builder();
        for (RelativeDirection dir : RelativeDirection.values()) {
            if (dir == RelativeDirection.RIGHT || dir == RelativeDirection.LEFT) continue;
            var newFFacing = dir.getRelative(fFacing, uFacing, isFlipped);
            var newUFacing = RelativeDirection.UP.getRelative(newFFacing, uFacing, isFlipped);
            var shiftedPos = hallwayCenter.relative(newFFacing, 12);
            builder.add(shiftedPos.relative(RelativeDirection.UP.getRelative(newFFacing, newUFacing, isFlipped), 2));
            builder.add(shiftedPos.relative(RelativeDirection.DOWN.getRelative(newFFacing, newUFacing, isFlipped), 2));
            builder.add(shiftedPos.relative(RelativeDirection.LEFT.getRelative(newFFacing, newUFacing, isFlipped), 2));
            builder.add(shiftedPos.relative(RelativeDirection.RIGHT.getRelative(newFFacing, newUFacing, isFlipped), 2));
        }
        return builder.build();
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        list.add(Component.translatable(LANGUAGE_SPACE_RADIATION_INTENSITY, radioactivity)
                .append(ComponentPanelWidget.withButton(Component.literal(" [-]"), "Sub"))
                .append(ComponentPanelWidget.withButton(Component.literal(" [+]"), "Add")));
        super.customText(list);
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            var amount = clickData.isCtrlClick ? 40 : (clickData.isShiftClick ? 8 : 1);
            radioactivity = Mth.clamp(radioactivity + ("Add".equals(componentData) ? amount : -amount), 0, 40);
        }
    }

    private class Trait extends RadioactivityTrait {

        Trait(IMultiblockTraitHolder machine) {
            super(machine);
        }

        @Override
        protected int getRecipeRadioactivity() {
            return super.getRecipeRadioactivity() + radioactivity;
        }
    }

    @RegisterLanguage(cn = "由环境维护舱提供的超净等级不足", en = "Insufficient cleanroom level provided by the Environmental Maintenance Module")
    private static final String LANGUAGE_INSUFFICIENT_CLEANROOM = "gtocore.machine.space_bio_research_module.insufficient_cleanroom";
    @RegisterLanguage(cn = "宇宙辐射强度: %s Sv", en = "Space Radiation Intensity: %s Sv")
    private static final String LANGUAGE_SPACE_RADIATION_INTENSITY = "gtocore.machine.space_bio_research_module.space_radiation_intensity";
}
