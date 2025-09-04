package com.gtocore.mixin.gtm;

import com.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraftforge.fml.loading.FMLLoader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GTCEu.class)
public class GTCEuMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean isDev() {
        if (GTOConfig.INSTANCE == null) return false;
        return !FMLLoader.isProduction() || GTOConfig.INSTANCE.dev || GTCEu.isDataGen();
    }
}
