package com.gtocore.mixin.ftbxmodcompat;

import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedQuest;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedQuestCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WrappedQuestCache.class)
public class WrappedQuestCacheMixin {

    @Shadow(remap = false)
    @Final
    private List<WrappedQuest> wrappedQuestsCache;

    @Inject(method = "lambda$rebuildWrappedQuestCache$1", at = @At("HEAD"), remap = false, cancellable = true)
    private void gtocore$includeRewardlessItemTasks(Quest quest, CallbackInfo ci) {
        if (!quest.getRewards().isEmpty()) {
            return;
        }
        if (ClientQuestFile.INSTANCE.selfTeamData.canStartTasks(quest) && quest.showInRecipeMod() && gtocore$hasItemTask(quest)) {
            wrappedQuestsCache.add(new WrappedQuest(quest, List.of()));
        }
        ci.cancel();
    }

    @Unique
    private static boolean gtocore$hasItemTask(Quest quest) {
        return quest.getTasks().stream().anyMatch(ItemTask.class::isInstance);
    }
}
