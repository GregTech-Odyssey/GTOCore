package com.gtocore.data.recipe.research;

import com.gtocore.data.recipe.builder.research.DataCrystalConstruction;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.*;

public final class ScanningRecipes {

    public static void init() {
        /// 基元扫描
        /// buildDataCrystal输入null不生成配方只生成扫描晶片数据, true会生成基元扫描配方, false会生成扫描仪配方
        /// input 需要输入输入被扫描的物品或流体Stack, 数据等级(0-15), 晶片等级(1-5)
        /// EUt 消耗电量
        /// 扫描仪需要输入一个时间 .duration
        /// 基元扫描需要输入一个算力消耗 .CWUt( ) 或 .CWUt( , ) 和一个催化剂 .catalyst
        DataCrystalConstruction.buildDataCrystal(false)
                .input(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Naquadah, 1), 1, 1)
                .EUt(VA[IV])
                .duration(200)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .input(ChemicalHelper.get(TagPrefix.dust, GTMaterials.ActivatedCarbon), 2, 2)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .input(GTMaterials.Water.getFluid(50050), 3, 2)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .input(GTMaterials.Steel.getFluid(3000), 4, 2)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .input(GTMaterials.Iron.getFluid(FluidStorageKeys.PLASMA, 2000), 5, 3)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16, 2048)
                .save();
    }
}
