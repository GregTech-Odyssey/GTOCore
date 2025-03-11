package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.common.data.GTORecipes;
import com.gto.gtocore.data.Data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.registry.MaterialRegistry;
import com.gregtechceu.gtceu.common.CommonProxy;
import com.gregtechceu.gtceu.data.loot.DungeonLootLoader;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.pack.GTPackSource;

import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.event.AddPackFindersEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(CommonProxy.class)
public class CommonProxyMixin {

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Ljava/util/Collection;forEach(Ljava/util/function/Consumer;)V"), remap = false)
    private static Consumer<MaterialRegistry> modifyArg(Consumer<MaterialRegistry> p) {
        return (registry) -> {};
    }

    @Inject(method = "registerPackFinders", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/data/pack/GTDynamicDataPack;clearServer()V"), remap = false, cancellable = true)
    private void registerPackFinders(AddPackFindersEvent event, CallbackInfo ci) {
        if (!GTORecipes.cache) {
            if (!GTCEu.isClientSide()) {
                Data.init();
            }
            DungeonLootLoader.init();
            event.addRepositorySource(new GTPackSource("gtceu:dynamic_data", event.getPackType(), Pack.Position.BOTTOM, GTDynamicDataPack::new));
        }
        ci.cancel();
    }
}
