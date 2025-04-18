package com.gto.gtocore.api.playerskill.eventhandler;

import com.gto.gtocore.api.playerskill.event.SkillEvent;

public abstract class SkillEventHandler {

    public abstract void handleEvent(SkillEvent event);
}
