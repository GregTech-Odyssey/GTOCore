package com.gtocore.mixin.ae2.gui;

import com.gtocore.config.GTOConfig;
import com.gtocore.integration.ae.search.EnglishSearchPredicate;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.me.search.RepoSearch;
import appeng.menu.me.common.GridInventoryEntry;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(RepoSearch.class)
@OnlyIn(Dist.CLIENT)
public class RepoSearchMixin {

    @Redirect(method = "getPredicates",
              at = @At(
                       value = "INVOKE",
                       target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z",
                       remap = false,
                       ordinal = 4),
              remap = false)
    private boolean redirectAddNamePredicate(java.util.ArrayList<Object> instance, Object o, @Local(name = "part") String part) {
        if (!GTOConfig.INSTANCE.gamePlay.showEnglishName) return instance.add(o);
        return instance.add(new EnglishSearchPredicate(part).or((Predicate<? super GridInventoryEntry>) o));
    }
}
