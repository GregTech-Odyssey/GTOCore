package com.gtocore.mixin.ftbq;

import com.gtocore.client.forge.GTOComponentHandlerKt;
import com.gtocore.config.GTOConfig;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemTask.class, remap = false)
@OnlyIn(Dist.CLIENT)
public class QuestScreenMixin {

    @Shadow
    private ItemStack itemStack;

    @Inject(method = "addMouseOverText", at = @At("TAIL"))
    private void addInfoTooltip(TooltipList list, TeamData teamData, CallbackInfo ci) {
        if (!GTOConfig.INSTANCE.showEnglishName) return;
        if (itemStack.isEmpty()) return;
        GTOComponentHandlerKt.getEnglish(itemStack).ifPresent(list::add);
    }
}
