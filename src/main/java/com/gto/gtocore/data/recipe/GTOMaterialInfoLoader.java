package com.gto.gtocore.data.recipe;

import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.ItemMaterialInfo;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.M;

public interface GTOMaterialInfoLoader {

    static void init() {
        ChemicalHelper.registerMaterialInfo(GTMachines.HULL[10].getBlock(), new ItemMaterialInfo(
                new MaterialStack(GTOMaterials.Quantanium, M << 3),
                new MaterialStack(GTOMaterials.Mithril, M)));

        ChemicalHelper.registerMaterialInfo(GTMachines.HULL[11].getBlock(), new ItemMaterialInfo(
                new MaterialStack(GTOMaterials.Adamantium, M << 3),
                new MaterialStack(GTMaterials.Neutronium, M)));

        ChemicalHelper.registerMaterialInfo(GTMachines.HULL[12].getBlock(), new ItemMaterialInfo(
                new MaterialStack(GTOMaterials.Vibranium, M << 3),
                new MaterialStack(GTOMaterials.Taranium, M)));

        ChemicalHelper.registerMaterialInfo(GTMachines.HULL[13].getBlock(), new ItemMaterialInfo(
                new MaterialStack(GTOMaterials.Draconium, M << 3),
                new MaterialStack(GTOMaterials.CrystalMatrix, M)));

        ChemicalHelper.registerMaterialInfo(GTMachines.HULL[14].getBlock(), new ItemMaterialInfo(
                new MaterialStack(GTOMaterials.Chaos, M << 3),
                new MaterialStack(GTOMaterials.CosmicNeutronium, M)));
    }
}
