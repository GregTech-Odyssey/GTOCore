package com.gto.gtocore.api.playerskill.experiencelevel.normal;

import com.gto.gtocore.api.playerskill.experiencelevel.NormalExperienceLevel;
import com.gto.gtocore.api.playerskill.experiencelevel.special.BodyExperienceLevel;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class AttackExperienceLevel extends NormalExperienceLevel {

    public AttackExperienceLevel(BodyExperienceLevel _bodyExperienceLevel) {
        super(_bodyExperienceLevel);
    }

    @Override
    public ATTRIBUTE_RECORD[] getAttributeModifiers() {
        return new ATTRIBUTE_RECORD[] {
                new ATTRIBUTE_RECORD(Attributes.ATTACK_DAMAGE,
                        "gtocore.attack_attack_bonus", // level_attribute_bonus
                        UUID.fromString("d9c9b8f0-5a9e-11ee-8c99-0242ac120002"),
                        (expLevel) -> expLevel.getLevel() << 1)
        };
    }

    @Override
    public String getName() {
        return Component.translatable("gtocore.player_exp_status.attack_name").getString();
    }

    @Override
    public int getExperienceForNextLevel() {
        return (int) (100 * Math.pow(1.5, level)); // 示例经验计算
    }
}
