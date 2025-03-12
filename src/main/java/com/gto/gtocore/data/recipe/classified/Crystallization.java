package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTOItems.HIGH_PURITY_SINGLE_CRYSTAL_SILICON;
import static com.gto.gtocore.common.data.GTORecipeTypes.CRYSTALLIZATION_RECIPES;

interface Crystallization {

    static void init() {
        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("ruby"))
                .inputItems(GTOTagPrefix.crystalSeed, GTMaterials.Ruby)
                .inputItems(TagPrefix.dust, GTMaterials.Chromium)
                .outputItems(GTOTagPrefix.artificialGem, GTMaterials.Ruby)
                .inputFluids(GTMaterials.Aluminium.getFluid(432))
                .inputFluids(GTMaterials.Oxygen.getFluid(4000))
                .EUt(120)
                .duration(2000)
                .blastFurnaceTemp(2400)
                .save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("sapphire"))
                .inputItems(GTOTagPrefix.crystalSeed, GTMaterials.Sapphire)
                .inputItems(TagPrefix.dustTiny, GTMaterials.Iron)
                .outputItems(GTOTagPrefix.artificialGem, GTMaterials.Sapphire)
                .inputFluids(GTMaterials.Aluminium.getFluid(432))
                .inputFluids(GTMaterials.Oxygen.getFluid(4000))
                .EUt(120)
                .duration(2000)
                .blastFurnaceTemp(2400)
                .save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("emerald"))
                .inputItems(GTOTagPrefix.crystalSeed, GTMaterials.Emerald)
                .inputItems(TagPrefix.dustSmall, GTMaterials.Silicon, 6)
                .inputItems(TagPrefix.dustSmall, GTMaterials.Beryllium, 3)
                .outputItems(GTOTagPrefix.artificialGem, GTMaterials.Emerald)
                .inputFluids(GTMaterials.Aluminium.getFluid(72))
                .inputFluids(GTMaterials.Oxygen.getFluid(2500))
                .EUt(120)
                .duration(4000)
                .blastFurnaceTemp(3400)
                .save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("diamond"))
                .inputItems(GTOTagPrefix.crystalSeed, GTMaterials.Diamond)
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Diamond)
                .outputItems(GTOTagPrefix.artificialGem, GTMaterials.Diamond)
                .inputFluids(GTMaterials.Carbon.getFluid(576))
                .inputFluids(GTMaterials.Helium.getFluid(1000))
                .EUt(480)
                .duration(6000)
                .blastFurnaceTemp(5200)
                .save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("lepton_trap_crystal"))
                .inputItems(TagPrefix.dust, GTMaterials.Meitnerium)
                .inputItems(TagPrefix.dust, GTMaterials.Molybdenum)
                .inputItems(TagPrefix.dust, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.NaquadahAlloy.getFluid(288))
                .outputItems(GTOItems.LEPTON_TRAP_CRYSTAL.asItem())
                .EUt(3450000)
                .duration(340)
                .blastFurnaceTemp(10900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("periodically_poled_lithium_niobate_boule"))
                .notConsumable(GTOItems.ELECTRON_SOURCE.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.LithiumNiobateNanoparticles, 6)
                .inputFluids(GTMaterials.Xenon.getFluid(1000))
                .outputItems(GTOItems.PERIODICALLY_POLED_LITHIUM_NIOBATE_BOULE.asItem())
                .EUt(1966080)
                .duration(600)
                .blastFurnaceTemp(9900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("silicon_boule"))
                .inputItems(GTOTagPrefix.crystalSeed, GTMaterials.Silicon)
                .inputItems(dustSmall, GalliumArsenide)
                .inputFluids(GTOMaterials.ElectronicGradeSilicon.getFluid(4608))
                .outputItems(SILICON_BOULE)
                .blastFurnaceTemp(1784)
                .duration(3000).EUt(VA[MV]).save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("phosphorus_boule"))
                .inputItems(GTOTagPrefix.crystalSeed, GTMaterials.Silicon)
                .inputItems(dust, Phosphorus, 8)
                .inputItems(dustSmall, GalliumArsenide, 2)
                .inputFluids(GTOMaterials.ElectronicGradeSilicon.getFluid(9216))
                .inputFluids(Nitrogen.getFluid(8000))
                .outputItems(PHOSPHORUS_BOULE)
                .blastFurnaceTemp(2484)
                .duration(4000).EUt(VA[HV]).save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("naquadah_boule"))
                .inputItems(GTOTagPrefix.crystalSeed, GTOMaterials.ElectronicGradeSilicon)
                .inputItems(ingot, Naquadah)
                .inputItems(dust, GalliumArsenide)
                .inputFluids(GTOMaterials.ElectronicGradeSilicon.getFluid(20736))
                .inputFluids(Argon.getFluid(8000))
                .outputItems(NAQUADAH_BOULE)
                .blastFurnaceTemp(5400)
                .duration(15000).EUt(VA[EV]).save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("neutronium_boule"))
                .inputItems(GTOTagPrefix.crystalSeed, GTOMaterials.ElectronicGradeSilicon)
                .inputItems(ingot, Neutronium, 4)
                .inputItems(dust, GalliumArsenide, 2)
                .inputFluids(GTOMaterials.UltraHighPuritySilicon.getFluid(41472))
                .inputFluids(Xenon.getFluid(8000))
                .outputItems(NEUTRONIUM_BOULE)
                .blastFurnaceTemp(6484)
                .duration(18000).EUt(VA[IV]).save();

        CRYSTALLIZATION_RECIPES.recipeBuilder("make_high_purity_single_crystal_silicon")
                .inputItems(GTOTagPrefix.crystalSeed, GTOMaterials.UltraHighPuritySilicon)
                .inputItems(dust, GalliumArsenide)
                .inputFluids(GTOMaterials.UltraHighPuritySilicon.getFluid(20736))
                .inputFluids(Xenon.getFluid(8000))
                .outputItems(HIGH_PURITY_SINGLE_CRYSTAL_SILICON)
                .blastFurnaceTemp(8684)
                .duration(21000).EUt(VA[ZPM]).save();

        CRYSTALLIZATION_RECIPES.recipeBuilder(GTOCore.id("taranium_boulea"))
                .inputItems(GTOTagPrefix.crystalSeed, GTOMaterials.UltraHighPuritySilicon)
                .inputItems(TagPrefix.ingot, GTOMaterials.Taranium, 8)
                .inputItems(TagPrefix.dust, GTMaterials.GalliumArsenide, 4)
                .inputFluids(GTOMaterials.UltraHighPuritySilicon.getFluid(82944))
                .inputFluids(GTMaterials.Radon.getFluid(16000))
                .outputItems(GTOItems.TARANIUM_BOULE.asItem())
                .EUt(122880)
                .duration(24000)
                .blastFurnaceTemp(10500)
                .save();
    }
}
