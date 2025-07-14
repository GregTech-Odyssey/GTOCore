package com.gtocore.data.recipe.processing;

import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.api.recipe.builder.DataCrystalConstruction;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gtolib.utils.RegistriesUtils.getItem;

public final class ScanningRecipes {

    public static void init() {
        DataCrystalConstruction.buildDataCrystal(false)
                .inputScanning(new ItemStack(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Naquadah)), 1, 1)
                .EUt(VA[IV])
                .duration(200)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .inputScanning(ChemicalHelper.get(TagPrefix.dust, GTMaterials.ActivatedCarbon), 2, 2)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .inputScanning(GTMaterials.Water.getFluid(50050), 3, 2)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .inputScanning(GTMaterials.Steel.getFluid(3000), 4, 2)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .inputScanning(GTMaterials.Iron.getFluid(FluidStorageKeys.PLASMA, 2000), 5, 3)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16)
                .save();
    }
}
