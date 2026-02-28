package com.gtocore.mixin.ae2.search;

import com.gtocore.integration.ae.MultiLangNameSearchPredicate;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.me.search.RepoSearch;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
        return instance.add(new MultiLangNameSearchPredicate(part));
    }
}
