package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.api.registries.GTORegistration;
import com.gto.gtocore.common.data.GTOCreativeModeTabs;

import com.gregtechceu.gtceu.common.data.GTBlocks;

import net.minecraft.world.level.block.Block;

import com.tterrag.registrate.util.entry.BlockEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.gto.gtocore.common.block.BlockMap.MACHINECASINGMAP;

@Mixin(GTBlocks.class)
public class GTBlocksMixin {

    @Inject(method = "createMachineCasingBlock(I)Lcom/tterrag/registrate/util/entry/BlockEntry;", at = @At(value = "TAIL"), remap = false)
    private static void createMachineCasingBlock(int tier, CallbackInfoReturnable<BlockEntry<Block>> cir) {
        BlockEntry<Block> block = cir.getReturnValue();
        MACHINECASINGMAP.put(tier, block);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;creativeModeTab(Ljava/util/function/Supplier;)V", ordinal = 0), remap = false)
    private static void setBlockCreativeModeTab(CallbackInfo ci) {
        GTORegistration.REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_MATERIAL_BLOCK);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;creativeModeTab(Ljava/util/function/Supplier;)V", ordinal = 1), remap = false)
    private static void setPipeCreativeModeTab(CallbackInfo ci) {
        GTORegistration.REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_MATERIAL_PIPE);
    }
}
