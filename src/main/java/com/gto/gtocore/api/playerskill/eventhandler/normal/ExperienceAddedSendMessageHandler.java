package com.gto.gtocore.api.playerskill.eventhandler.normal;

import com.gto.gtocore.api.playerskill.event.SkillEvent;
import com.gto.gtocore.api.playerskill.event.normal.ExperienceAddedEvent;
import com.gto.gtocore.api.playerskill.eventhandler.SkillEventHandler;

import net.minecraft.network.chat.Component;

public class ExperienceAddedSendMessageHandler extends SkillEventHandler {

    public void handleEvent(SkillEvent event) {
        if (event instanceof ExperienceAddedEvent experienceAddedEvent) {
            Component message = Component.translatable("gtocore.player_exp_status.get_experience",
                    (experienceAddedEvent.postExperience - experienceAddedEvent.preExperience),
                    event.experienceLevel.getName()).withStyle(event.experienceLevel.getNameColor());
            experienceAddedEvent.player.sendSystemMessage(message);
        }
    }
}
