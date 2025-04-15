package com.gto.gtocore.integration.emi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.UUID;

public final class EmiPersist {

    // needsRefresh == true means emi loads before we get SERVER_IDENTIFIER.
    // emi will be reloaded after receives SERVER_IDENTIFIER
    public static boolean needsRefresh = false;

    public JsonArray favorites;
    public HashMap<UUID, JsonObject> byId;
}
