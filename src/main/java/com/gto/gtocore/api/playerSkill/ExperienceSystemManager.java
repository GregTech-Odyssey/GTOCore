package com.gto.gtocore.api.playerSkill;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExperienceSystemManager {
    public static final ExperienceSystemManager INSTANCE = new ExperienceSystemManager();

    private Map<UUID, PlayerData> playerDataMap;
    private boolean isEnabled;
    private static final String DATA_FILE = "experience_data.json"; // 数据文件路径

    private ExperienceSystemManager() {
        this.playerDataMap = new HashMap<>();
        this.isEnabled = false; // 默认关闭
    }



    public void enableSystem() {
        isEnabled = true;
        loadAllData(); // 启用时加载数据
    }

    public void disableSystem() {
        saveAllData(); // 关闭时保存数据
        isEnabled = false;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void addPlayer(UUID playerId) {
        if (isEnabled) {
            playerDataMap.putIfAbsent(playerId, new PlayerData(playerId));
        }
    }

    public void removePlayer(UUID playerId) {
        playerDataMap.remove(playerId);
    }

    public PlayerData getPlayerData(UUID playerId) {
        return playerDataMap.get(playerId);
    }

    public void addHealthExperience(UUID playerId, int amount) {
        if (isEnabled && playerDataMap.containsKey(playerId)) {
            playerDataMap.get(playerId).addHealthExperience(amount);
        }
    }

    public void addAttackExperience(UUID playerId, int amount) {
        if (isEnabled && playerDataMap.containsKey(playerId)) {
            playerDataMap.get(playerId).addAttackExperience(amount);
        }
    }

    public void addBodyExperience(UUID playerId, int amount) {
        if (isEnabled && playerDataMap.containsKey(playerId)) {
            playerDataMap.get(playerId).addBodyExperience(amount);
        }
    }

    public void saveAllData() {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            JsonObject playerDataJson = new JsonObject();
            entry.getValue().saveData(playerDataJson);
            jsonObject.add(entry.getKey().toString(), playerDataJson);
        }
        writeToFile(jsonObject);
    }

    public void loadAllData() {
        JsonObject jsonObject = readFromFile();
        if (jsonObject != null) {
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                UUID playerId = UUID.fromString(entry.getKey());
                PlayerData playerData = new PlayerData(playerId);
                playerData.loadData(entry.getValue().getAsJsonObject());
                playerDataMap.put(playerId, playerData);
            }
        }
    }

    private void writeToFile(JsonObject jsonObject) {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            Gson gson = new Gson();
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject readFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new JsonObject();
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonObject();
        }
    }
}