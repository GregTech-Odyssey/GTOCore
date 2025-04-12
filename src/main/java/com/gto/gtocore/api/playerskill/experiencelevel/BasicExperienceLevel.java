package com.gto.gtocore.api.playerskill.experiencelevel;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import lombok.Getter;

import java.util.UUID;
import java.util.function.Function;

@Getter
public abstract class BasicExperienceLevel {

    protected int level;
    protected int experience;

    public record ATTRIBUTE_RECORD(Attribute attribute, String modifierName, UUID modifierUUID,
                                   Function<BasicExperienceLevel, Integer> valueCalculator) {

        public AttributeModifier getModifier(BasicExperienceLevel expLevel) {
            return new AttributeModifier(modifierUUID, modifierName, valueCalculator.apply(expLevel), AttributeModifier.Operation.ADDITION);
        }
    }

    protected BasicExperienceLevel() {
        this.level = 0;
        this.experience = 0;
    }

    public ChatFormatting getNameColor() {
        return ChatFormatting.GOLD;
    }

    public void saveData(CompoundTag nbt) {
        nbt.putInt("level", level);
        nbt.putInt("experience", experience);
    }

    public void loadData(CompoundTag nbt) {
        this.level = nbt.getInt("level");
        this.experience = nbt.getInt("experience");
    }

    public abstract ATTRIBUTE_RECORD[] getAttributeModifiers();

    public int getMaxLevel() {
        return 20;
    }

    public abstract void addExperience(int amount);

    public abstract String getName();

    public abstract int getExperienceForNextLevel();
}
