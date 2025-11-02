package com.gtocore.config;

public enum ConfigDifficulty {
    Default,
    Easy,
    Normal,
    Expert;

    public Difficulty get(){
        if(this==Default)return GTOConfig.INSTANCE.difficulty;
        return Difficulty.values()[this.ordinal()+1];
    }

    public int getInt(){
        return this.get().ordinal()+1;
    }

    public boolean isEasy() {
        return this.get() == Difficulty.Easy;
    }

    public boolean isNormal() {
        return this.get()==Difficulty.Normal;
    }

    public boolean isExpert() {
        return this.get()==Difficulty.Expert;
    }

    public <T> T value(T easyValue, T normalValue, T expertValue) {
        switch (this) {
            case Easy -> {
                return easyValue;
            }
            case Normal -> {
                return normalValue;
            }
            case Expert -> {
                return expertValue;
            }
            default -> {
                return normalValue;
            }
        }
    }
}
