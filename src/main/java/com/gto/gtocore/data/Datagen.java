package com.gto.gtocore.data;

import com.gto.gtocore.data.lang.LangHandler;
import com.gto.gtocore.data.lang.provider.SimplifiedChineseLanguageProvider;
import com.gto.gtocore.data.lang.provider.TraditionalChineseLanguageProvider;

import com.gregtechceu.gtceu.GTCEu;

import com.tterrag.registrate.providers.ProviderType;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

public interface Datagen {

    static void init() {
        if (GTCEu.isDataGen()) {
            REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagsHandler::initBlock);
            REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagsHandler::initItem);
            REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::enInitialize);
            REGISTRATE.addDataGenerator(SimplifiedChineseLanguageProvider.LANG, LangHandler::cnInitialize);
            REGISTRATE.addDataGenerator(TraditionalChineseLanguageProvider.LANG, LangHandler::twInitialize);
        }
    }
}
