package com.gtocore.common.forge;

import com.gtolib.GTOCore;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class ServerLangHook {

    public static final Gson gto$GSON = new Gson();
    public static final Pattern gto$PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
    public static boolean defaultRightToLeft = false;
    public static Map<String, String> langs = new Object2ObjectOpenHashMap<>(5000);

    public static void gto$loadLanguage(String langName, MinecraftServer server) {
        GTOCore.LOGGER.info("Loading language: {}", langName);
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
    }

    public static void gto$loadLocaleData(List<Resource> allResources) {
        allResources.forEach((res) -> {
            try {
                gto$loadLocaleData(res.open());
            } catch (IOException ignored) {}

        });
    }

    public static void gto$loadLocaleData(InputStream inputstream) {
        try {
            Map<String, String> modTable = new HashMap<>();
            JsonElement jsonelement = gto$GSON.fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonElement.class);
            JsonObject jsonobject = GsonHelper.convertToJsonObject(jsonelement, "strings");
            jsonobject.entrySet().forEach((entry) -> {
                String s = gto$PATTERN.matcher(GsonHelper.convertToString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
                modTable.put(entry.getKey(), s);
            });
            langs.putAll(modTable);
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }
}
