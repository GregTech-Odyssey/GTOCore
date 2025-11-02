package com.gtocore.config;

import com.gtolib.api.annotation.language.RegisterLanguage;

import dev.toma.configuration.config.Configurable;

import java.lang.reflect.Field;

public class DiffConfig {

    @Configurable
    @Configurable.Comment("Config options for Game Recipe")
    public RecipeConfig recipe = new RecipeConfig();

    @Configurable
    @Configurable.Comment("Config options for Machine")
    public MachineConfig machine = new MachineConfig();

    @Configurable
    @Configurable.Comment("Config options for Player")
    public PlayerConfig player = new PlayerConfig();

    @Configurable
    @Configurable.Comment("Config options for World")
    public WorldConfig world = new WorldConfig();

    public static class RecipeConfig {

        @Configurable
        @Configurable.Comment("gto配方难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.recipe", en = "Not IMPL", cn = "ae2配方难度")
        public ConfigDifficulty normal = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("原版配方难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.recipe", en = "Not IMPL", cn = "原版配方难度")
        public ConfigDifficulty vanilla = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("ae2配方难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.recipe", en = "Not IMPL", cn = "ae2配方难度")
        public ConfigDifficulty ae2 = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("gto配方难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.recipe", en = "Not IMPL", cn = "ae2配方难度")
        public ConfigDifficulty gto = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("gtceu配方难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.recipe", en = "Not IMPL", cn = "ae2配方难度")
        public ConfigDifficulty gtceu = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("mods配方难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.recipe", en = "Not IMPL", cn = "ae2配方难度")
        public ConfigDifficulty mods = ConfigDifficulty.Default;
        // recipe.organ recipe.gto recipe.normal recipe.mods
    }

    public static class MachineConfig {

        @Configurable
        @Configurable.Comment("机器难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.machine", en = "Not IMPL", cn = "机器难度")
        public ConfigDifficulty normal = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("发电机难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.machine", en = "Not IMPL", cn = "发电机难度")
        public ConfigDifficulty generator = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("ae2机器难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.machine", en = "Not IMPL", cn = "ae2机器难度")
        public ConfigDifficulty ae2 = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("蒸汽机器难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.machine", en = "Not IMPL", cn = "蒸汽机器难度")
        public ConfigDifficulty steam = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("调为easy启用轮椅")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.machine", en = "Not IMPL", cn = "调为easy启用轮椅")
        public ConfigDifficulty baby = ConfigDifficulty.Normal;
    }

    public static class PlayerConfig {

        @Configurable
        @Configurable.Comment("生命值")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.player", en = "Not IMPL", cn = "玩家回血")
        public ConfigDifficulty life = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("连锁")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.player", en = "Not IMPL", cn = "玩家回血")
        public ConfigDifficulty ultimine = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("工具")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.player", en = "Not IMPL", cn = "玩家回血")
        public ConfigDifficulty tool = ConfigDifficulty.Default;

        @Configurable
        @Configurable.Comment("危险物质")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.player", en = "Not IMPL", cn = "玩家回血")
        public ConfigDifficulty hazards = ConfigDifficulty.Default;
    }

    public static class WorldConfig {

        @Configurable
        @Configurable.Comment("怪物难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.world", en = "Not IMPL", cn = "怪物难度")
        public ConfigDifficulty monster = ConfigDifficulty.Default;
        @Configurable
        @Configurable.Comment("矿物难度")
        @RegisterLanguage(namePrefix = "config.gtocore.option.diff.world", en = "Not IMPL", cn = "矿物难度")
        public ConfigDifficulty ore = ConfigDifficulty.Default;
    }

    public static DiffConfig get(){
        return GTOConfig.INSTANCE.diffInfo;
    }

    public static ConfigDifficulty getDefault(){
        return ConfigDifficulty.Default;
    }


    public static Difficulty resolve(String path) {//为DynamicInitialValue使用
        try {
            String[] parts = path.split("\\.");

            // 支持多层嵌套访问，如 "category.subcategory.field"
            Object currentObject = GTOConfig.INSTANCE.diffInfo;

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];

                // 获取字段
                Field field = currentObject.getClass().getField(part);

                // 确保字段可访问
                field.setAccessible(true);

                // 如果是最后一个部分，直接返回值
                if (i == parts.length - 1) {
                    Object value = field.get(currentObject);
                    if (value instanceof ConfigDifficulty) {
                        return ((ConfigDifficulty) value).get();
                    } else {
                        return ConfigDifficulty.Default.get();
                    }
                }
                // 否则继续深入嵌套对象
                else {
                    currentObject = field.get(currentObject);
                    if (currentObject == null) {
                        return ConfigDifficulty.Default.get();
                    }
                }
            }
        } catch (Exception e) {
            // 记录日志（可选）
            System.err.println("Failed to get difficulty for path: " + path + ", error: " + e.getMessage());
        }
        return ConfigDifficulty.Default.get();
    }
}
