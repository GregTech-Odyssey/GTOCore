package com.gtocore.data;

import com.gtocore.data.lang.LangHandler;
import com.gtocore.data.tag.GtoPoiTypeTagsProvider;
import com.gtocore.data.tag.TagsHandler;

import com.gtolib.GTOCore;
import com.gtolib.api.lang.SimplifiedChineseLanguageProvider;
import com.gtolib.api.lang.TraditionalChineseLanguageProvider;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.tterrag.registrate.providers.ProviderType;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.gtocore.common.data.GTODamageTypes.DAMAGE_TYPES_BUILDER;
import static com.gtolib.api.registries.GTORegistration.GTO;

public final class Datagen {

    public static void init() {
        if (GTCEu.isDataGen()) {
            GTO.addDataGenerator(ProviderType.BLOCK_TAGS, TagsHandler::initBlock);
            GTO.addDataGenerator(ProviderType.ITEM_TAGS, TagsHandler::initItem);
            GTO.addDataGenerator(ProviderType.LANG, LangHandler::enInitialize);
            GTO.addDataGenerator(SimplifiedChineseLanguageProvider.LANG, LangHandler::cnInitialize);
            GTO.addDataGenerator(TraditionalChineseLanguageProvider.LANG, LangHandler::twInitialize);
        }
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> future = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(true, new DatapackBuiltinEntriesProvider(generator.getPackOutput(), future, DAMAGE_TYPES_BUILDER, Set.of("minecraft", GTOCore.MOD_ID)));
            generator.addProvider(true, new GtoPoiTypeTagsProvider(generator.getPackOutput(), future, existingFileHelper));
        }
    }
}
