package com.gtocore.mixin.ftbq;

import com.gtocore.integration.ftbquests.MEQuestDetectorRuntime;

import dev.ftb.mods.ftbquests.quest.TeamData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(TeamData.class)
public class TeamDataMixin {

    @Shadow(remap = false)
    @Final
    private UUID teamId;

    @Inject(method = "markDirty", at = @At("TAIL"), remap = false)
    private void gtocore$notifyQuestDetector(CallbackInfo ci) {
        if (!MEQuestDetectorRuntime.isDirtyNotificationSuppressed()) {
            MEQuestDetectorRuntime.notifyTeamDirty(teamId, "team-data-mark-dirty");
        }
    }
}
