package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.recipe.condition.RestrictedMachineCondition;
import com.gtocore.common.recipe.condition.VacuumCondition;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.rodLong;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Steel;
import static com.gregtechceu.gtceu.common.data.GTMaterials.TungstenCarbide;
import static com.gtocore.common.data.GTORecipeTypes.LASER_WELDER_RECIPES;

final class LaserWelder {

    public static void init() {
        LASER_WELDER_RECIPES.builder("reactor_thorium_dual")
                .inputItems(GTOItems.REACTOR_THORIUM_SIMPLE, 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_THORIUM_DUAL)
                .EUt(VA[HV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_thorium_quad")
                .inputItems(GTOItems.REACTOR_THORIUM_DUAL, 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_THORIUM_QUAD)
                .EUt(VA[HV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_uranium_dual")
                .inputItems(GTOItems.REACTOR_URANIUM_SIMPLE, 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_URANIUM_DUAL)
                .EUt(VA[EV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_uranium_quad")
                .inputItems(GTOItems.REACTOR_URANIUM_DUAL, 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_URANIUM_QUAD)
                .EUt(VA[EV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_mox_dual")
                .inputItems(GTOItems.REACTOR_MOX_SIMPLE, 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_MOX_DUAL)
                .EUt(VA[IV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_mox_quad")
                .inputItems(GTOItems.REACTOR_MOX_DUAL, 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_MOX_QUAD)
                .EUt(VA[IV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_naquadah_dual")
                .inputItems(GTOItems.REACTOR_NAQUADAH_SIMPLE, 2)
                .outputItems(GTOItems.REACTOR_NAQUADAH_DUAL)
                .EUt(VA[LuV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_naquadah_quad")
                .inputItems(GTOItems.REACTOR_NAQUADAH_DUAL, 2)
                .inputItems(rodLong, TungstenCarbide, 4)
                .outputItems(GTOItems.REACTOR_NAQUADAH_QUAD)
                .EUt(VA[LuV])
                .duration(2400)
                .addCondition(RestrictedMachineCondition.multiblock())
                .save();
    }
}
