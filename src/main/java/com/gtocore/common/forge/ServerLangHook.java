package com.gtocore.common.forge;

import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.ServerUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;

import com.fast.fastcollection.O2OOpenCacheHashMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class ServerLangHook {

    public static final Gson gto$GSON = new Gson();
    public static final Pattern gto$PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
    public static boolean defaultRightToLeft = false;
    public static O2OOpenCacheHashMap<String, String> langs = null;
    private static final Lock langsLock = new ReentrantLock();
    private static O2OOpenCacheHashMap<String, String> loadingLangs = null;
    private static String currentLang = "en_us";

    public static void reload(MinecraftServer server) {
        if (FMLLoader.getDist() != Dist.DEDICATED_SERVER) return;
        if (Objects.equals(GTOConfig.INSTANCE.misc.serverLang, currentLang)) return;
        currentLang = GTOConfig.INSTANCE.misc.serverLang;
        if (currentLang.equals("en_us")) {
            langs = null;
            return;
        }
        gto$loadLanguage(GTOConfig.INSTANCE.misc.serverLang, server);
    }

    public static void set(MinecraftServer server, String langCode) {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER) {
            GTOConfig.set("misc.serverLang", langCode);
            reload(server);
        }
    }

    private static void gto$loadLanguage(String langName, MinecraftServer server) {
        GTOUtils.asyncExecute(() -> {
            GTOCore.LOGGER.info("Loading language: {}", langName);
            long startTime = System.currentTimeMillis();
            langsLock.lock();
            try {
                loadingLangs = new O2OOpenCacheHashMap<>(10000);
                String langFile = String.format(Locale.ROOT, "lang/%s.json", langName);
                ResourceManager resourceManager = server.getServerResources().resourceManager();
                resourceManager.getNamespaces().forEach((namespace) -> {
                    try {
                        ResourceLocation langResource = ResourceLocation.fromNamespaceAndPath(namespace, langFile);
                        gto$loadLocaleData(resourceManager.getResourceStack(langResource));
                    } catch (Exception exception) {
                        GTOCore.LOGGER.warn("Skipped language file: {}:{}", namespace, langFile, exception);
                    }
                });
            } finally {
                langsLock.unlock();
            }
            long endTime = System.currentTimeMillis();
            GTOCore.LOGGER.info("Finished loading language: {} in {} ms", langName, endTime - startTime);
            server.execute(() -> {
                langs = loadingLangs;
                loadingLangs = null;
                ServerUtils.markServerLangInitialized();
            });
        });
    }

    private static void gto$loadLocaleData(List<Resource> allResources) {
        allResources.forEach((res) -> {
            try {
                gto$loadLocaleData(res.open());
            } catch (IOException ignored) {}

        });
    }

    private static void gto$loadLocaleData(InputStream inputstream) {
        try {
            Map<String, String> modTable = new HashMap<>();
            JsonElement jsonelement = gto$GSON.fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonElement.class);
            JsonObject jsonobject = GsonHelper.convertToJsonObject(jsonelement, "strings");
            jsonobject.entrySet().forEach((entry) -> {
                String s = gto$PATTERN.matcher(GsonHelper.convertToString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
                modTable.put(entry.getKey(), s);
            });
            loadingLangs.putAll(modTable);
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }
}
