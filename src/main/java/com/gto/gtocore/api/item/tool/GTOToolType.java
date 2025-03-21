package com.gto.gtocore.api.item.tool;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.item.tool.behavior.DisableShieldBehavior;
import com.gregtechceu.gtceu.common.item.tool.behavior.ToolModeSwitchBehavior;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

public final class GTOToolType {

    public static final GTToolType VAJRA_HV = vajra(GTToolType.builder("hv_vajra").electric(GTValues.HV));
    public static final GTToolType VAJRA_EV = vajra(GTToolType.builder("ev_vajra").electric(GTValues.EV));
    public static final GTToolType VAJRA_IV = vajra(GTToolType.builder("iv_vajra").electric(GTValues.IV));

    private static GTToolType vajra(GTToolType.Builder builder) {
        return builder.idFormat("%s_vajra")
                .toolTag(CustomTags.WRENCHES)
                .toolTag(CustomTags.WRENCH)
                .toolTag(CustomTags.WIRE_CUTTERS)
                .toolTag(ItemTags.PICKAXES)
                .toolTag(ItemTags.SHOVELS)
                .toolTag(ItemTags.AXES)
                .harvestTag(BlockTags.MINEABLE_WITH_PICKAXE)
                .harvestTag(BlockTags.MINEABLE_WITH_SHOVEL)
                .harvestTag(BlockTags.MINEABLE_WITH_AXE)
                .harvestTag(CustomTags.MINEABLE_WITH_WRENCH)
                .toolStats(b -> b.crafting().blockBreaking().sneakBypassUse().attacking().attackDamage(10.0F).attackSpeed(2.0F).behaviors(DisableShieldBehavior.INSTANCE, ToolModeSwitchBehavior.INSTANCE))
                .toolClasses(GTToolType.WRENCH, GTToolType.WIRE_CUTTER, GTToolType.PICKAXE, GTToolType.SHEARS, GTToolType.AXE)
                .build();
    }
}
