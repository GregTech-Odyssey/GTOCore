package com.gto.gtocore.data;

import com.gto.gtocore.data.lang.LangHandler;

import com.tterrag.registrate.providers.ProviderType;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

public class GTODatagen {

    public static void init() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::enInitialize);
    }
}
