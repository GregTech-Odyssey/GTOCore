package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.recipe.condition.RestrictedMachineCondition;
import com.gtocore.common.recipe.condition.VacuumCondition;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.rodLong;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Steel;
import static com.gregtechceu.gtceu.common.data.GTMaterials.TungstenCarbide;
import static com.gtocore.common.data.GTORecipeTypes.LASER_WELDER_RECIPES;

final class LaserWelder {

    public static void init() {
        LASER_WELDER_RECIPES.builder("huge_item_import_bus")
                .inputItems(GTMachines.ITEM_IMPORT_BUS[GTValues.IV].asItem())
                .inputItems(GTMachines.QUANTUM_CHEST[GTValues.LuV].asItem())
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 4)
                .outputItems(GTOMachines.HUGE_ITEM_IMPORT_BUS.asItem())
                .EUt(7680)
                .duration(400)
                .save();

        LASER_WELDER_RECIPES.builder("reactor_thorium_dual")
                .inputItems(GTOItems.REACTOR_THORIUM_SIMPLE.asItem(), 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_THORIUM_DUAL.asItem())
                .EUt(VA[HV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_thorium_quad")
                .inputItems(GTOItems.REACTOR_THORIUM_DUAL.asItem(), 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_THORIUM_QUAD.asItem())
                .EUt(VA[HV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_uranium_dual")
                .inputItems(GTOItems.REACTOR_URANIUM_SIMPLE.asItem(), 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_URANIUM_DUAL.asItem())
                .EUt(VA[EV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_uranium_quad")
                .inputItems(GTOItems.REACTOR_URANIUM_DUAL.asItem(), 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_URANIUM_QUAD.asItem())
                .EUt(VA[EV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_mox_dual")
                .inputItems(GTOItems.REACTOR_MOX_SIMPLE.asItem(), 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_MOX_DUAL.asItem())
                .EUt(VA[IV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_mox_quad")
                .inputItems(GTOItems.REACTOR_MOX_DUAL.asItem(), 2)
                .inputItems(rodLong, Steel, 4)
                .outputItems(GTOItems.REACTOR_MOX_QUAD.asItem())
                .EUt(VA[IV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_naquadah_dual")
                .inputItems(GTOItems.REACTOR_NAQUADAH_SIMPLE.asItem(), 2)
                .outputItems(GTOItems.REACTOR_NAQUADAH_DUAL.asItem())
                .EUt(VA[LuV])
                .duration(2400)
                .addCondition(new VacuumCondition(4))
                .save();

        LASER_WELDER_RECIPES.builder("reactor_naquadah_quad")
                .inputItems(GTOItems.REACTOR_NAQUADAH_DUAL.asItem(), 2)
                .inputItems(rodLong, TungstenCarbide, 4)
                .outputItems(GTOItems.REACTOR_NAQUADAH_QUAD.asItem())
                .EUt(VA[LuV])
                .duration(2400)
                .addCondition(RestrictedMachineCondition.multiblock())
                .save();
    }
}
