package com.gtocore.mixin.ftbq;

import com.gtocore.integration.ftbquests.MEQuestDetectorSubmitHelper;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemTask.class)
public class ItemTaskMixin {

    @Inject(method = "submitTask", at = @At("HEAD"), remap = false)
    private void gtocore$submitFromMe(TeamData teamData, ServerPlayer player, ItemStack craftedItem, CallbackInfo ci) {
        MEQuestDetectorSubmitHelper.submitFromNetwork((ItemTask) (Object) this, teamData, player, craftedItem);
    }
}
