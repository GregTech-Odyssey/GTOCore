package com.gtocore.mixin.ftbq;

import dev.ftb.mods.ftbquests.quest.Quest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(value = Quest.class)
public class QuestMixin {

    @Redirect(method = "isVisible", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;anyMatch(Ljava/util/function/Predicate;)Z"), remap = false)
    private boolean anyMatch(Stream instance, Predicate predicate) {
        return true;
    }
}
