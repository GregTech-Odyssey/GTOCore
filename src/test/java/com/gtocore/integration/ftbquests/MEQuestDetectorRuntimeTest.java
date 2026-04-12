package com.gtocore.integration.ftbquests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class MEQuestDetectorRuntimeTest {

    @AfterEach
    void tearDown() {
        MEQuestDetectorRuntime.clearAll("test-teardown");
    }

    @Test
    void notifiesOnlyMatchingTeam() {
        UUID teamA = UUID.randomUUID();
        UUID teamB = UUID.randomUUID();
        FakeTarget detectorA = new FakeTarget(teamA, true);
        FakeTarget detectorB = new FakeTarget(teamB, true);

        MEQuestDetectorRuntime.bind(teamA, detectorA);
        MEQuestDetectorRuntime.bind(teamB, detectorB);

        MEQuestDetectorRuntime.notifyTeamDirty(teamA, "unit-test");

        Assertions.assertEquals(1, detectorA.notifications);
        Assertions.assertEquals(0, detectorB.notifications);
    }

    @Test
    void suppressionFlagScopesDirtyNotifications() {
        Assertions.assertFalse(MEQuestDetectorRuntime.isDirtyNotificationSuppressed());

        MEQuestDetectorRuntime.runWithoutDirtyNotification(
                () -> Assertions.assertTrue(MEQuestDetectorRuntime.isDirtyNotificationSuppressed()));

        Assertions.assertFalse(MEQuestDetectorRuntime.isDirtyNotificationSuppressed());
    }

    private static final class FakeTarget implements IMEQuestDetectorRuntimeTarget {

        private final UUID teamId;
        private final boolean active;
        private int notifications;

        private FakeTarget(UUID teamId, boolean active) {
            this.teamId = teamId;
            this.active = active;
        }

        @Override
        public UUID getDetectorBoundTeamId() {
            return teamId;
        }

        @Override
        public boolean isDetectorRuntimeActive() {
            return active;
        }

        @Override
        public void onDetectorContextChanged(String reason) {
            notifications++;
        }
    }
}
