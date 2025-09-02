package com.gtocore.mixin.gtm;

import com.gtocore.common.data.GTORecipes;
import com.gtocore.data.Data;
import com.gtocore.data.loot.DungeonLoot;

import com.gtolib.api.recipe.ingredient.CircuitIngredient;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.gtm.AdvancedTerminalBehavior;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.registry.MaterialRegistry;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidContainerIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.common.CommonProxy;
import com.gregtechceu.gtceu.data.loot.DungeonLootLoader;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.pack.GTPackSource;

import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import com.hepdd.gtmthings.data.CustomItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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

    @Inject(method = "registerPackFinders", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/common/data/GTRecipes;recipeRemoval()V"), remap = false, cancellable = true)
    private void registerPackFinders(AddPackFindersEvent event, CallbackInfo ci) {
        if (!GTORecipes.cache) {
            if (!GTCEu.isClientSide()) {
                Data.init();
            }
            DungeonLootLoader.init();
            DungeonLoot.init();
        }
        event.addRepositorySource(new GTPackSource("gtceu:dynamic_data", event.getPackType(), Pack.Position.BOTTOM, GTDynamicDataPack::new));
        ci.cancel();
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @SubscribeEvent
    @Overwrite(remap = false)
    public void commonSetup(FMLCommonSetupEvent event) {
        CustomItems.ADVANCED_TERMINAL.get().getComponents().clear();
        CustomItems.ADVANCED_TERMINAL.get().getComponents().add(new AdvancedTerminalBehavior());
        event.enqueueWork(() -> {
            CraftingHelper.register(SizedIngredient.TYPE, FastSizedIngredient.SERIALIZER);
            CraftingHelper.register(IntCircuitIngredient.TYPE, CircuitIngredient.SERIALIZER);
            CraftingHelper.register(FluidContainerIngredient.TYPE, FluidContainerIngredient.SERIALIZER);
        });
    }
}
