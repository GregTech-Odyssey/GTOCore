package com.gtocore.api.accelerator.item;

import com.gtocore.api.accelerator.particle.ParticleBeam;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.item.component.IAddInformation;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.List;

@DataGeneratorScanned
public class ParticleContainmentItem implements IAddInformation {

    @Override
    public void appendTooltips(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        var particle = getContained(stack);
        if (particle == null) {
            tooltipComponents.add(Component.translatable(EMPTY_TOOLTIP));
        } else {
            tooltipComponents.add(Component.translatable(CONTAINS_TOOLTIP, particle.getDefinition().getDisplayName()));
        }
    }

    public static @Nullable ParticleBeam getContained(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null) return null;
        if (!tag.contains(NBT_KEY_PARTICLE)) return null;
        var particleTag = tag.getCompound(NBT_KEY_PARTICLE);
        var particle = ParticleBeam.fromNbt(particleTag);
        if (particle.isEmpty()) return null;
        return particle;
    }

    public static final String NBT_KEY_PARTICLE = "contained_particle";

    @RegisterLanguage(cn = "含有: %s", en = "Contains: %s")
    public static String CONTAINS_TOOLTIP = "particle_containment_item.contains";
    @RegisterLanguage(cn = "无", en = "Empty")
    public static String EMPTY_TOOLTIP = "particle_containment_item.empty";
}
