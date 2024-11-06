package com.gto.gtocore.integration.kjs;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.multiblock.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;
import com.gto.gtocore.utils.Registries;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class GTOKubejsPlugin extends KubeJSPlugin {

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        super.registerClasses(type, filter);
        filter.allow("com.gto.gtocore");
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        super.registerBindings(event);

        event.add("GTOCore", GTOCore.class);
        event.add("GTOMaterials", GTOMaterials.class);
        event.add("GTOBlocks", GTOBlocks.class);
        event.add("GTOItems", GTOItems.class);
        event.add("GTOMachines", GTOMachines.class);
        event.add("GTOCleanroomType", GTOCleanroomType.class);
        event.add("Registries", Registries.class);
        event.add("GravityCondition", GravityCondition.class);
        event.add("VacuumCondition", VacuumCondition.class);
    }
}
