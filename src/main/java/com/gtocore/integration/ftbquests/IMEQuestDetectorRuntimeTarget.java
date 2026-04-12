package com.gtocore.integration.ftbquests;

import java.util.UUID;

public interface IMEQuestDetectorRuntimeTarget {

    UUID getDetectorBoundTeamId();

    boolean isDetectorRuntimeActive();

    void onDetectorContextChanged(String reason);
}
