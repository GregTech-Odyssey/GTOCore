package com.gto.gtocore.api.data.tag;

import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialFlags;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.NO_SMASHING;

@SuppressWarnings("unused")
public class GTOTagPrefix {

    public static void init() {}

    private static final MaterialIconType nanoswarmIcon = new MaterialIconType("nanoswarm");

    public static final TagPrefix nanoswarm = new TagPrefix("nanoswarm")
            .idPattern("%s_nanoswarm")
            .defaultTagPath("nanoswarms/%s")
            .unformattedTagPath("nanoswarms")
            .materialAmount(GTValues.M)
            .materialIconType(nanoswarmIcon)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_NANOSWARM));

    public static final TagPrefix contaminableManoswarm = new TagPrefix("contaminable_nanoswarm")
            .idPattern("contaminable_%s_nanoswarm")
            .defaultTagPath("contaminable_nanoswarm/%s")
            .unformattedTagPath("contaminable_nanoswarm")
            .materialAmount(GTValues.M)
            .materialIconType(nanoswarmIcon)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_NANOSWARM));

    public static final TagPrefix milled = new TagPrefix("milled")
            .idPattern("milled_%s")
            .defaultTagPath("milled/%s")
            .unformattedTagPath("milled")
            .materialAmount(GTValues.M)
            .materialIconType(new MaterialIconType("milled"))
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_MILLED));

    public static final TagPrefix curvedPlate = new TagPrefix("curved_plate")
            .idPattern("curved_%s_plate")
            .defaultTagPath("curved_plate/%s")
            .unformattedTagPath("curved_plate")
            .materialAmount(GTValues.M)
            .materialIconType(new MaterialIconType("curved_plate"))
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_CURVED_PLATE) || mat.hasFlag(MaterialFlags.GENERATE_ROTOR) || ((mat.hasProperty(PropertyKey.FLUID_PIPE) || mat.hasProperty(PropertyKey.ITEM_PIPE)) && !mat.hasFlag(NO_SMASHING) && mat.getMass() < 240 && mat.getBlastTemperature() < 3600));

    public static final TagPrefix magiccrystal = new TagPrefix("magiccrystal")
            .idPattern("%s_magiccrystal")
            .defaultTagPath("/%s_magiccrystal")
            .unformattedTagPath("magiccrystal")
            .materialAmount(GTValues.M)
            .materialIconType(new MaterialIconType("magiccrystal"))
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_MAGICCRYSTAL));
}
