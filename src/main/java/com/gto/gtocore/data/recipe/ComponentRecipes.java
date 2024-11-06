package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES;

public class ComponentRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("electric_motor_lv_steel"), ELECTRIC_MOTOR_LV.asStack(), "CWR",
                "WMW", "RWC", 'C', new UnificationEntry(GTOTagPrefix.curvedPlate, Steel), 'W',
                new UnificationEntry(wireGtSingle, Copper), 'R', new UnificationEntry(rod, Steel), 'M',
                new UnificationEntry(rod, SteelMagnetic));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("electric_motor_mv"), ELECTRIC_MOTOR_MV.asStack(), "CWR",
                "WMW", "RWC", 'C', new UnificationEntry(GTOTagPrefix.curvedPlate, Aluminium), 'W',
                new UnificationEntry(wireGtDouble, Cupronickel), 'R', new UnificationEntry(rod, Aluminium), 'M',
                new UnificationEntry(rod, SteelMagnetic));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("electric_motor_hv"), ELECTRIC_MOTOR_HV.asStack(), "CWR",
                "WMW", "RWC", 'C', new UnificationEntry(GTOTagPrefix.curvedPlate, StainlessSteel), 'W',
                new UnificationEntry(wireGtDouble, Electrum), 'R', new UnificationEntry(rod, StainlessSteel), 'M',
                new UnificationEntry(rod, SteelMagnetic));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("electric_motor_ev"), ELECTRIC_MOTOR_EV.asStack(), "CWR",
                "WMW", "RWC", 'C', new UnificationEntry(GTOTagPrefix.curvedPlate, Titanium), 'W',
                new UnificationEntry(wireGtDouble, Kanthal), 'R', new UnificationEntry(rod, Titanium), 'M',
                new UnificationEntry(rod, NeodymiumMagnetic));
        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("electric_motor_iv"), ELECTRIC_MOTOR_IV.asStack(), "CWR",
                "WMW", "RWC", 'C', new UnificationEntry(GTOTagPrefix.curvedPlate, TungstenSteel), 'W',
                new UnificationEntry(wireGtDouble, Graphene), 'R', new UnificationEntry(rod, TungstenSteel), 'M',
                new UnificationEntry(rod, NeodymiumMagnetic));

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("electric_motor_lv_steel"))
                .inputItems(GTOTagPrefix.curvedPlate, Steel, 2)
                .inputItems(rod, Steel, 2)
                .inputItems(rod, SteelMagnetic)
                .inputItems(wireGtSingle, Copper, 4)
                .outputItems(ELECTRIC_MOTOR_LV)
                .duration(100).EUt(VA[LV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("electric_motor_mv"))
                .inputItems(GTOTagPrefix.curvedPlate, Aluminium, 2)
                .inputItems(rod, Aluminium, 2)
                .inputItems(rod, SteelMagnetic)
                .inputItems(wireGtDouble, Cupronickel, 4)
                .outputItems(ELECTRIC_MOTOR_MV)
                .duration(100).EUt(VA[LV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("electric_motor_hv"))
                .inputItems(GTOTagPrefix.curvedPlate, StainlessSteel, 2)
                .inputItems(rod, StainlessSteel, 2)
                .inputItems(rod, SteelMagnetic)
                .inputItems(wireGtDouble, Electrum, 4)
                .outputItems(ELECTRIC_MOTOR_HV)
                .duration(100).EUt(VA[LV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("electric_motor_ev"))
                .inputItems(GTOTagPrefix.curvedPlate, Titanium, 2)
                .inputItems(rod, Titanium, 2)
                .inputItems(rod, NeodymiumMagnetic)
                .inputItems(wireGtDouble, Kanthal, 4)
                .outputItems(ELECTRIC_MOTOR_EV)
                .duration(100).EUt(VA[LV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("electric_motor_iv"))
                .inputItems(GTOTagPrefix.curvedPlate, TungstenSteel, 2)
                .inputItems(rod, TungstenSteel, 2)
                .inputItems(rod, NeodymiumMagnetic)
                .inputItems(wireGtDouble, Graphene, 4)
                .outputItems(ELECTRIC_MOTOR_IV)
                .duration(100).EUt(VA[LV]).save(provider);

        ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id("electric_motor_luv"))
                .inputItems(rodLong, SamariumMagnetic)
                .inputItems(rodLong, HSSS, 2)
                .inputItems(ring, HSSS, 2)
                .inputItems(round, HSSS, 4)
                .inputItems(wireFine, Ruridit, 64)
                .inputItems(GTOTagPrefix.curvedPlate, HSSS, 2)
                .inputFluids(SolderingAlloy.getFluid(L))
                .inputFluids(Lubricant.getFluid(250))
                .outputItems(ELECTRIC_MOTOR_LuV)
                .scannerResearch(b -> b
                        .researchStack(ELECTRIC_MOTOR_IV.asStack())
                        .duration(900)
                        .EUt(VA[EV]))
                .duration(600).EUt(6000).save(provider);

        ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id("electric_motor_zpm"))
                .inputItems(rodLong, SamariumMagnetic)
                .inputItems(rodLong, Osmiridium, 4)
                .inputItems(ring, Osmiridium, 4)
                .inputItems(round, Osmiridium, 8)
                .inputItems(wireFine, Europium, 64)
                .inputItems(wireFine, Europium, 32)
                .inputItems(GTOTagPrefix.curvedPlate, Osmiridium, 2)
                .inputFluids(SolderingAlloy.getFluid(L * 2))
                .inputFluids(Lubricant.getFluid(500))
                .outputItems(ELECTRIC_MOTOR_ZPM)
                .scannerResearch(b -> b
                        .researchStack(ELECTRIC_MOTOR_LuV.asStack())
                        .duration(1200)
                        .EUt(VA[IV]))
                .duration(600).EUt(24000).save(provider);

        ASSEMBLY_LINE_RECIPES.recipeBuilder(GTOCore.id("electric_motor_uv"))
                .inputItems(rodLong, SamariumMagnetic)
                .inputItems(rodLong, Tritanium, 4)
                .inputItems(ring, Tritanium, 4)
                .inputItems(round, Tritanium, 8)
                .inputItems(wireFine, Americium, 64)
                .inputItems(wireFine, Americium, 64)
                .inputItems(GTOTagPrefix.curvedPlate, Tritanium, 2)
                .inputFluids(SolderingAlloy.getFluid(L * 4))
                .inputFluids(Lubricant.getFluid(1000))
                .inputFluids(Naquadria.getFluid(L * 4))
                .outputItems(ELECTRIC_MOTOR_UV)
                .stationResearch(b -> b
                        .researchStack(ELECTRIC_MOTOR_ZPM.asStack())
                        .CWUt(32)
                        .EUt(VA[ZPM]))
                .duration(600).EUt(100000).save(provider);
    }
}
