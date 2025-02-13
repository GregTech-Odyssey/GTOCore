package com.gto.gtocore.utils.register;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.chemical.material.GTOMaterialBuilder;
import com.gto.gtocore.data.lang.LangHandler;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class MaterialsRegisterUtils {

    public static final Map<String, LangHandler.ENCN> LANG = GTCEu.isDataGen() ? new HashMap<>() : null;

    private static void addLang(String name, Supplier<LangHandler.ENCN> supplier) {
        if (LANG == null) return;
        LangHandler.ENCN lang = supplier.get();
        if (LANG.containsKey(name)) {
            GTOCore.LOGGER.error("Repetitive Key: {}", name);
            throw new IllegalStateException();
        }
        if (LANG.containsValue(lang)) {
            GTOCore.LOGGER.error("Repetitive Value: {}", lang);
            throw new IllegalStateException();
        }
        LANG.put(name, lang);
    }

    public static GTOMaterialBuilder material(String name, String en, String cn) {
        addLang(name, () -> new LangHandler.ENCN(en, cn));
        return new GTOMaterialBuilder(name);
    }

    public static GTOMaterialBuilder material(String name, String cn) {
        addLang(name, () -> new LangHandler.ENCN(FormattingUtil.toEnglishName(name), cn));
        return new GTOMaterialBuilder(name);
    }
}
