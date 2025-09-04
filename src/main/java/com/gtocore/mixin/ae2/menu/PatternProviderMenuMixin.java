package com.gtocore.mixin.ae2.menu;

import com.gtolib.api.ae2.BlockingType;
import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.IPatternProviderMenu;

import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.PatternProviderMenu;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternProviderMenu.class)
public class PatternProviderMenuMixin implements IPatternProviderMenu {

    @Shadow(remap = false)
    @Final
    protected PatternProviderLogic logic;

    @Unique
    @GuiSync(8)
    private BlockingType gtolib$enhancedblockingmode;

    @Inject(method = "<init>*", at = @At("TAIL"), remap = false)
    private void gtolib$init(CallbackInfo ci) {
        gtolib$enhancedblockingmode = BlockingType.NONE;
    }

    @Inject(method = "broadcastChanges", at = @At(value = "INVOKE", target = "Lappeng/helpers/patternprovider/PatternProviderLogic;getUnlockStack()Lappeng/api/stacks/GenericStack;", remap = false))
    private void broadcastChanges(CallbackInfo ci) {
        gtolib$enhancedblockingmode = logic.getConfigManager().getSetting(GTOSettings.BLOCKING_TYPE);
    }

    @Override
    public @NotNull BlockingType gtolib$getBlocking() {
        return gtolib$enhancedblockingmode;
    }
}
