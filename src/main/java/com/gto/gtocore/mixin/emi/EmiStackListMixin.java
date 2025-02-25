package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.utils.register.BlockRegisterUtils;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import com.google.common.collect.Lists;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiStackList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(EmiStackList.class)
public class EmiStackListMixin {

    @Shadow(remap = false)
    private static Object2IntMap<EmiStack> strictIndices;

    @Shadow(remap = false)
    private static Object2IntMap<Object> keyIndices;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void reload() {
        List<EmiStackList.IndexGroup> groups = Lists.newArrayList();
        Map<String, EmiStackList.IndexGroup> namespaceGroups = new LinkedHashMap<>();
        for (Item item : EmiPort.getItemRegistry()) {
            if (item == BlockRegisterUtils.REACTOR_CORE.asItem()) continue;
            EmiStack stack = EmiStack.of(item);
            if (stack.isEmpty()) continue;
            namespaceGroups.computeIfAbsent(stack.getId().getNamespace(), (k) -> new EmiStackList.IndexGroup()).stacks.add(stack);
        }
        groups.addAll(namespaceGroups.values());
        EmiStackList.IndexGroup fluidGroup = new EmiStackList.IndexGroup();
        for (Fluid fluid : EmiPort.getFluidRegistry()) {
            if (fluid.isSource(fluid.defaultFluidState()) || (fluid instanceof FlowingFluid ff && ff.getSource() == Fluids.EMPTY)) {
                EmiStack fs = EmiStack.of(fluid);
                if (fs.isEmpty()) continue;
                fluidGroup.stacks.add(fs);
            }
        }
        groups.add(fluidGroup);

        Set<EmiStack> added = new ObjectOpenCustomHashSet<>(new EmiStackList.StrictHashStrategy());

        EmiStackList.stacks = Lists.newLinkedList();
        for (EmiStackList.IndexGroup group : groups) {
            if (group.shouldDisplay()) {
                for (EmiStack stack : group.stacks) {
                    if (!added.contains(stack)) {
                        EmiStackList.stacks.add(stack);
                        added.add(stack);
                    }
                }
            }
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void bake() {
        for (int i = 0; i < EmiStackList.stacks.size(); i++) {
            EmiStack stack = EmiStackList.stacks.get(i);
            strictIndices.put(stack, i);
            keyIndices.put(stack.getKey(), i);
        }
        bakeFiltered();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void bakeFiltered() {
        EmiStackList.filteredStacks = EmiStackList.stacks;
    }
}
