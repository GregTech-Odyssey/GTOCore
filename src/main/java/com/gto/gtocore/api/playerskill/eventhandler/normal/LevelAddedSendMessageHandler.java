package com.gto.gtocore.api.playerskill.eventhandler.normal;

import com.gto.gtocore.api.playerskill.event.SkillEvent;
import com.gto.gtocore.api.playerskill.event.normal.LevelAddedEvent;
import com.gto.gtocore.api.playerskill.eventhandler.SkillEventHandler;

import net.minecraft.network.chat.Component;

public class LevelAddedSendMessageHandler extends SkillEventHandler {

    @Override
    public void handleEvent(SkillEvent event) {
        if (event instanceof LevelAddedEvent levelAddedEvent) {
            Component message = Component.translatable("gtocore.player_exp_status.add_level",
                    event.experienceLevel.getName(),
                    levelAddedEvent.postLevel).withStyle(event.experienceLevel.getNameColor());
            levelAddedEvent.player.sendSystemMessage(message);
        }
    }
}
