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
public final class SimplifiedChineseLanguageProvider extends LanguageProvider implements RegistrateProvider {

    public static final ProviderType<SimplifiedChineseLanguageProvider> LANG = ProviderType.register("cn_lang", (p, e) -> new SimplifiedChineseLanguageProvider(p, e.getGenerator().getPackOutput()));

    private final AbstractRegistrate<?> owner;

    private SimplifiedChineseLanguageProvider(AbstractRegistrate<?> owner, PackOutput packOutput) {
        super(packOutput, owner.getModid(), "zh_cn");
        this.owner = owner;
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    @Override
    public String getName() {
        return "Lang (zh_cn)";
    }

    @Override
    protected void addTranslations() {
        this.owner.genData(LANG, this);
    }
}
