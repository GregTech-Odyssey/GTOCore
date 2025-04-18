package com.gto.gtocore.api.playerskill.data;

import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;
import java.util.function.Function;

public record AttributeRecord(Attribute attribute,
                              AttributeModifier.Operation operation,
                              Function<BasicExperienceLevel, Double> valueCalculator) {

    public AttributeModifier getModifier(BasicExperienceLevel expLevel) {
        return new AttributeModifier(UUID.randomUUID(),
                "gtocore.exp." + expLevel.skillType.getEnglishName().toLowerCase() + "_" + attribute.getDescriptionId().toLowerCase() + "_" + operation.name().toLowerCase() + "_bonus",
                valueCalculator.apply(expLevel), operation);
    }
}
