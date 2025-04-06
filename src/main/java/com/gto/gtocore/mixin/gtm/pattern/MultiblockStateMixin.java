package com.gto.gtocore.mixin.gtm.pattern;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.pattern.util.PatternMatchContext;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MultiblockState.class)
public class MultiblockStateMixin {

    @Shadow(remap = false)
    @Final
    private PatternMatchContext matchContext;

    @Shadow(remap = false)
    private Map<SimplePredicate, Integer> globalCount;

    @Shadow(remap = false)
    private Map<SimplePredicate, Integer> layerCount;

    @Shadow(remap = false)
    public LongOpenHashSet cache;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected void clean() {
        this.matchContext.reset();
        this.globalCount = new Object2IntOpenHashMap<>();
        this.layerCount = new Object2IntOpenHashMap<>();
        cache = new LongOpenHashSet();
    }

    @Inject(method = "onBlockStateChanged",
            at = @At(
                     value = "INVOKE",
                     target = "Lcom/gregtechceu/gtceu/api/machine/feature/multiblock/IMultiController;checkPatternWithLock()Z"),
            remap = false,
            cancellable = true)
    private void onBlockStateChangedHook(BlockPos pos, BlockState state, CallbackInfo ci, @Local IMultiController controller) {
        // 当结构已经不成型时, 忽略后续的方块更新检查
        if (!controller.isFormed()) {
            ci.cancel();
        }
    }
}
