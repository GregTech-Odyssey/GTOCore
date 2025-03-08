package com.gto.gtocore.common.machine.multiblock.electric.space;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.saved.DysonSphereSavaedData;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class SpaceProbeSurfaceReceptionMachine extends ElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SpaceProbeSurfaceReceptionMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    private ResourceKey<Level> dimension;

    @Persisted
    private boolean use;

    public SpaceProbeSurfaceReceptionMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    private ResourceKey<Level> getDimension() {
        if (dimension == null) dimension = Objects.requireNonNull(getLevel()).dimension();
        return dimension;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (use) DysonSphereSavaedData.setDysonUse(getDimension(), true);
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        if (use) {
            DysonSphereSavaedData.setDysonUse(getDimension(), false);
            use = false;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        if (use) {
            DysonSphereSavaedData.setDysonUse(getDimension(), false);
            use = false;
        }
    }

    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        if (!PlanetApi.API.isSpace(getLevel())) return null;
        recipe = GTORecipeModifiers.perfectOverclocking(this, recipe);
        if (!DysonSphereSavaedData.getDimensionUse(getDimension())) {
            double number = (double) DysonSphereSavaedData.getDimensionData(getDimension()).getFirst() / 100;
            if (number > 1) {
                use = true;
                Content content = recipe.outputs.get(FluidRecipeCapability.CAP).get(0);
                recipe.outputs.put(FluidRecipeCapability.CAP, List.of(content.copy(FluidRecipeCapability.CAP, ContentModifier.multiplier(number))));
                return recipe;
            }
        }
        return recipe;
    }

    @Override
    public boolean onWorking() {
        if (super.onWorking()) {
            Level level = getLevel();
            if (level == null) return false;
            if (getOffsetTimer() % 20 == 0) {
                BlockPos pos = MachineUtils.getOffsetPos(8, 28, getFrontFacing(), getPos());
                for (int i = -4; i < 5; i++) {
                    for (int j = -4; j < 5; j++) {
                        if (!level.canSeeSky(pos.offset(i, 0, j))) {
                            return false;
                        }
                    }
                }
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.dyson_sphere.number", DysonSphereSavaedData.getDimensionData(getDimension()).getFirst()));
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
