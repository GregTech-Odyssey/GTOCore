package com.gto.gtocore.api.playerSkill.experienceSub;

public abstract class NormalExperienceLevel extends BasicExperienceLevel {
    protected BodyExperienceLevel bodyExperienceLevel;
    @Override
    public int getMaxLevel(){return (this.bodyExperienceLevel.level)*2 ;}

    public NormalExperienceLevel(BodyExperienceLevel _bodyExperienceLevel) {
        super();
        this.bodyExperienceLevel = _bodyExperienceLevel;

    }
}