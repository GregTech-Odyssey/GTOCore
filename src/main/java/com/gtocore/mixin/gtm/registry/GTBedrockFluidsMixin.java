package com.gtocore.mixin.gtm.registry;

import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.gregtechceu.gtceu.common.data.GTBedrockFluids;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

import static com.gtocore.common.data.GTOBedrockFluids.addVoid;

@Mixin(GTBedrockFluids.class)
public class GTBedrockFluidsMixin {

    @Inject(method = "create", at = @At("RETURN"), remap = false)
    private static void create(ResourceLocation id, Consumer<BedrockFluidDefinition.Builder> consumer, CallbackInfoReturnable<BedrockFluidDefinition> cir) {
        addVoid(cir.getReturnValue());
    }
}
