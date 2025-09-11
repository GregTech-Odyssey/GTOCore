package com.gtocore.mixin.mc;

import com.gtocore.common.forge.ServerLangHook;
import com.gtocore.config.GTOStartupConfig;

import net.minecraft.client.resources.language.FormattedBidiReorder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.fml.loading.FMLLoader;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.gtocore.common.forge.ServerLangHook.langs;

@Mixin(Language.class)
public class LanguageMixin {

    @Shadow
    private static Language instance;
    @Unique
    private static Language gto$serverLanguage;
    @Unique
    private static final boolean gto$isDedicatedServer = FMLLoader.getDist().isDedicatedServer();

    @Inject(method = "getInstance", at = @At("HEAD"), cancellable = true)
    private static void getInstance(CallbackInfoReturnable<Language> cir) {
        if (!gto$isDedicatedServer || GTOStartupConfig.serverLang.equals("en_us")) return;
        if (gto$serverLanguage != null) {
            cir.setReturnValue(gto$serverLanguage);
            return;
        }
        cir.setReturnValue(gto$serverLanguage = new Language() {

            @Override
            public @NotNull String getOrDefault(@NotNull String key) {
                return langs.getOrDefault(key, instance.getOrDefault(key));
            }

            @Override
            public @NotNull String getOrDefault(@NotNull String s, @NotNull String s1) {
                return langs.getOrDefault(s, instance.getOrDefault(s, s1));
            }

            @Override
            public boolean has(@NotNull String s) {
                return langs.containsKey(s) || instance.has(s);
            }

            @Override
            public boolean isDefaultRightToLeft() {
                return ServerLangHook.defaultRightToLeft;
            }

            @Override
            public @NotNull FormattedCharSequence getVisualOrder(@NotNull FormattedText formattedText) {
                return FormattedBidiReorder.reorder(formattedText, ServerLangHook.defaultRightToLeft);
            }
        });
    }
}
