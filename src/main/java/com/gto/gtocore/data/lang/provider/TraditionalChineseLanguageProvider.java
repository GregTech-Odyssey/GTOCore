package com.gto.gtocore.data.lang.provider;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.LogicalSide;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class TraditionalChineseLanguageProvider extends LanguageProvider implements RegistrateProvider {

    public static final ProviderType<TraditionalChineseLanguageProvider> LANG = ProviderType.register("tw_lang", (p, e) -> new TraditionalChineseLanguageProvider(p, e.getGenerator().getPackOutput()));

    private final AbstractRegistrate<?> owner;

    private TraditionalChineseLanguageProvider(AbstractRegistrate<?> owner, PackOutput packOutput) {
        super(packOutput, owner.getModid(), "zh_tw");
        this.owner = owner;
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    @Override
    public String getName() {
        return "Lang (zh_tw)";
    }

    @Override
    protected void addTranslations() {
        this.owner.genData(LANG, this);
    }
}
