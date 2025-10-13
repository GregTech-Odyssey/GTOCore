package com.gtocore.mixin.apotheosis;

import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LootRarity.LootRule.class)
public class LootRarityMixin {

    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"), remap = false)
    private void redirectError(Logger instance, String string, Object a, Object b, Object c, Object d) {}
}
