package com.gto.gtocore.mixin.ae2;

import com.gto.gtocore.integration.ae2.BlockingType;
import com.gto.gtocore.integration.ae2.GTOSettings;
import com.gto.gtocore.integration.ae2.IPatternProviderLogic;
import com.gto.gtocore.integration.ae2.PatternProviderTargetCache;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.helpers.patternprovider.PatternProviderTarget;
import appeng.util.ConfigManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternProviderLogic.class)
public class PatternProviderLogicMixin implements IPatternProviderLogic {

    @Unique
    private final PatternProviderTargetCache[] gtocore$targetCaches = new PatternProviderTargetCache[6];

    @Shadow(remap = false)
    @Final
    private ConfigManager configManager;

    @Shadow(remap = false)
    @Final
    private PatternProviderLogicHost host;

    @Shadow(remap = false)
    @Final
    private IActionSource actionSource;

    @Inject(method = "<init>(Lappeng/api/networking/IManagedGridNode;Lappeng/helpers/patternprovider/PatternProviderLogicHost;I)V", at = @At("TAIL"), remap = false)
    private void PatternProviderLogic(IManagedGridNode mainNode, PatternProviderLogicHost host, int patternInventorySize, CallbackInfo ci) {
        configManager.registerSetting(GTOSettings.BLOCKING_TYPE, BlockingType.ALL);
    }

    @Override
    public BlockingType gtocore$getBlocking() {
        return configManager.getSetting(GTOSettings.BLOCKING_TYPE);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @Nullable
    private PatternProviderTarget findAdapter(Direction side) {
        if (gtocore$targetCaches[side.get3DDataValue()] == null) {
            var thisBe = host.getBlockEntity();
            gtocore$targetCaches[side.get3DDataValue()] = new PatternProviderTargetCache(this, (ServerLevel) thisBe.getLevel(), thisBe.getBlockPos().relative(side), side.getOpposite(), actionSource);
        }
        @Nullable
        PatternProviderTarget ret = gtocore$targetCaches[side.get3DDataValue()].find();
        return ret;
    }
}
