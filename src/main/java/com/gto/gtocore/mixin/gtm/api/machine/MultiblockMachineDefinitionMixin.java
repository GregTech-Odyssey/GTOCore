package com.gto.gtocore.mixin.gtm.api.machine;

import com.gto.gtocore.api.machine.IMultiblockMachineDefinition;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Supplier;

@Mixin(MultiblockMachineDefinition.class)
public class MultiblockMachineDefinitionMixin implements IMultiblockMachineDefinition {

    @Shadow(remap = false)
    private Supplier<List<MultiblockShapeInfo>> shapes;

    @Shadow(remap = false)
    private @Nullable Supplier<ItemStack[]> recoveryItems;

    @Unique
    private Pattern[] gtocore$patterns;

    @Override
    public void gtocore$clear() {
        shapes = List::of;
        gtocore$patterns = null;
        recoveryItems = null;
    }

    @Override
    public Pattern[] gtocore$getPatterns() {
        return gtocore$patterns;
    }

    @Override
    public void gtocore$setPatterns(Pattern[] patterns) {
        gtocore$patterns = patterns;
    }
}
