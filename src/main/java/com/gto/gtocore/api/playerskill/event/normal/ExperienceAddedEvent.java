package com.gto.gtocore.api.playerskill.event.normal;

import com.gto.gtocore.api.playerskill.event.SkillEvent;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.world.entity.player.Player;

public class ExperienceAddedEvent extends SkillEvent {

    public final long preExperience;
    public final long postExperience;
    public final Player player;

    public ExperienceAddedEvent(BasicExperienceLevel experienceLevel, long preExperience, long postExperience, Player player) {
        super(experienceLevel);
        this.preExperience = preExperience;
        this.postExperience = postExperience;
        this.player = player;
    }
}
