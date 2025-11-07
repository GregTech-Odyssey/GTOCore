package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.MultiBlockD;
import com.gtocore.common.data.machines.MultiBlockG;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import com.enderio.base.common.init.EIOFluids;

import static com.gregtechceu.gtceu.api.GTValues.UHV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.MICROGRAVITY_NANOFABRICATION_RECIPES;

public class MicrogravityNanofabrication {

    public static void init() {
        MICROGRAVITY_NANOFABRICATION_RECIPES.builder("machining_control_module_mk2")
                .inputItems(GTOItems.FPGA_CHIP.asItem(), 4)
                .inputItems(CustomTags.UV_CIRCUITS)
                .inputItems(GTOTagPrefix.wireFine, GTOMaterials.AbyssalAlloy, 16)
                .inputItems(GTOTagPrefix.foil, GTOMaterials.NickelTitaniumTinHeuslerAlloy, 32)
                .inputItems(GTOTagPrefix.plate, GTOMaterials.CobaltManganeseGalliumHeuslerAlloy, 4)
                .outputItems(GTOItems.MACHINING_CONTROL_MODULE_MK2.asItem())
                .inputFluids(GTOMaterials.TitaniumTi53311S, 576)
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .EUt(524200)
                .duration(500)
                .save();

        MICROGRAVITY_NANOFABRICATION_RECIPES.builder("machining_control_module_mk3")
                .inputItems(GTOItems.FPGA_CHIP.asItem(), 8)
                .inputItems(CustomTags.UHV_CIRCUITS)
                .inputItems(GTOTagPrefix.wireFine, GTOMaterials.TitanSteel, 32)
                .inputItems(GTOTagPrefix.foil, GTOMaterials.RutheniumIronSiliconHeuslerAlloy, 32)
                .inputItems(GTOTagPrefix.plate, GTOMaterials.MagneticControlledShapeMemoryAlloy, 4)
                .inputItems(GTOItems.OPTICAL_PRINTED_CIRCUIT_BOARD.asItem(), 2)
                .outputItems(GTOItems.MACHINING_CONTROL_MODULE_MK3.asItem())
                .inputFluids(GTOMaterials.PlatinumManganeseAntimonyHeuslerAlloy, 576)
                .inputFluids(GTOMaterials.MutatedLivingSolder, 288)
                .EUt(2097100)
                .duration(500)
                .save();

        MICROGRAVITY_NANOFABRICATION_RECIPES.builder("energy_control_module_mk2")
                .inputItems(GTOItems.IGBT_CHIP.asItem(), 4)
                .inputItems(CustomTags.UV_CIRCUITS)
                .inputItems(GTOTagPrefix.wireFine, GTMaterials.YttriumBariumCuprate, 32)
                .inputItems(GTOTagPrefix.foil, GTMaterials.HSSS, 16)
                .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT.asItem(), 4)
                .outputItems(GTOItems.ENERGY_CONTROL_MODULE_MK2.asItem())
                .inputFluids(GTOMaterials.ScalmAlloyS, 576)
                .inputFluids(GTOMaterials.TitaniumTi64, 576)
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .EUt(524200)
                .duration(300)
                .save();

        MICROGRAVITY_NANOFABRICATION_RECIPES.builder("energy_control_module_mk3")
                .inputItems(GTOItems.IGBT_CHIP.asItem(), 8)
                .inputItems(CustomTags.UHV_CIRCUITS)
                .inputItems(GTOTagPrefix.wireFine, GTMaterials.RutheniumTriniumAmericiumNeutronate, 32)
                .inputItems(GTOTagPrefix.foil, GTOMaterials.CarbonNanotubeReinforcedAluminumMatrixComposite, 16)
                .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT.asItem(), 4)
                .outputItems(GTOItems.ENERGY_CONTROL_MODULE_MK3.asItem())
                .inputFluids(GTOMaterials.MoonGoddessTitanium, 576)
                .inputFluids(GTOMaterials.Dalisenite, 576)
                .inputFluids(GTOMaterials.MutatedLivingSolder, 288)
                .EUt(2097100)
                .duration(300)
                .save();

        MICROGRAVITY_NANOFABRICATION_RECIPES.builder("large_algae_farm")
                .inputItems(CustomTags.UHV_CIRCUITS, 8)
                .inputItems(MultiBlockG.ALGAE_FARM.asItem(), 4)
                .inputItems(MultiBlockD.INCUBATOR.asItem())
                .inputItems(GTOItems.SMD_DIODE_OPTICAL.asItem(), 8)
                .inputItems(GTOItems.SMD_INDUCTOR_OPTICAL.asItem(), 16)
                .inputItems(GTOTagPrefix.wireFine, GTOMaterials.AbyssalAlloy, 16)
                .outputItems("gtocore:large_algae_farm")
                .inputFluids(GTOMaterials.MutatedLivingSolder, 6000)
                .inputFluids(EIOFluids.DEW_OF_THE_VOID.getSource(), 10000)
                .EUt(VA[UHV])
                .duration(200)
                .save();
    }
}
