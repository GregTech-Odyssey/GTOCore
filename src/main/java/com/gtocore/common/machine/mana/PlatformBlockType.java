package com.gtocore.common.machine.mana;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PlatformBlockType {

    // ===================================================================
    // 单个平台结构（链式构建）
    // ===================================================================
    public static final class PlatformBlockStructure {

        private final String name;
        private final String type;
        @Nullable
        private final String displayName;
        @Nullable
        private final String description;
        @Nullable
        private final String source;
        private final boolean preview;
        private final String resourcePath; // 保存资源路径
        private final Map<Character, BlockState> blockMapping;
        private final int[] materials;
        private final int xSize;
        private final int ySize;
        private final int zSize;

        private PlatformBlockStructure(String name,
                                       String type,
                                       @Nullable String displayName,
                                       @Nullable String description,
                                       @Nullable String source,
                                       boolean preview,
                                       String resourcePath,
                                       Map<Character, BlockState> blockMapping,
                                       int[] materials,
                                       int xSize,
                                       int ySize,
                                       int zSize) {
            this.name = name;
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.source = source;
            this.preview = preview;
            this.resourcePath = resourcePath;
            this.blockMapping = blockMapping;
            this.materials = materials;
            this.xSize = xSize;
            this.ySize = ySize;
            this.zSize = zSize;
        }

        public static Builder structure(String name) {
            return new Builder(name);
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        @Nullable
        public String getDisplayName() {
            return displayName;
        }

        @Nullable
        public String getDescription() {
            return description;
        }

        @Nullable
        public String getSource() {
            return source;
        }

        public boolean getPreview() {
            return preview;
        }

        public String getResourcePath() {
            return resourcePath;
        }

        public Map<Character, BlockState> getBlockMapping() {
            return blockMapping;
        }

        public int[] getMaterials() {
            return materials;
        }

        public int getXSize() {
            return xSize;
        }

        public int getYSize() {
            return ySize;
        }

        public int getZSize() {
            return zSize;
        }

        public static final class Builder {

            private final String name;
            private String type;
            private String displayName;
            private String description;
            private String source;
            private boolean preview = false;
            private String resourcePath;
            private final Map<Character, BlockState> symbolMap = new HashMap<>();
            private final int[] materials = new int[] { 0, 0, 0 };
            private int xSize;
            private int ySize;
            private int zSize;

            public Builder(String name) {
                this.name = name;
            }

            public Builder type(String type) {
                this.type = type;
                return this;
            }

            public Builder displayName(@Nullable String displayName) {
                this.displayName = displayName;
                return this;
            }

            public Builder description(@Nullable String description) {
                this.description = description;
                return this;
            }

            public Builder source(@Nullable String source) {
                this.source = source;
                return this;
            }

            public Builder preview(boolean preview) {
                this.preview = preview;
                return this;
            }

            public Builder resourcePath(String resourcePath) {
                this.resourcePath = resourcePath;
                return this;
            }

            public Builder where(char symbol, @NotNull BlockState block) {
                symbolMap.put(symbol, block);
                return this;
            }

            public Builder where(char symbol, @NotNull Block block) {
                symbolMap.put(symbol, block.defaultBlockState());
                return this;
            }

            public Builder materials(int material, int count) {
                this.materials[material] = count;
                return this;
            }

            public Builder xSize(int xSize) {
                this.xSize = xSize;
                return this;
            }

            public Builder ySize(int ySize) {
                this.ySize = ySize;
                return this;
            }

            public Builder zSize(int zSize) {
                this.zSize = zSize;
                return this;
            }

            public PlatformBlockStructure build() {
                if (name == null || name.isEmpty()) {
                    throw new IllegalStateException("Structure name must be defined (as primary key)");
                }
                if (resourcePath == null || resourcePath.isEmpty()) {
                    throw new IllegalStateException("Resource path must be defined");
                }
                if (symbolMap.isEmpty()) {
                    throw new IllegalStateException("No block mappings defined");
                }
                readDepthFromResource(resourcePath);
                return new PlatformBlockStructure(
                        name,
                        type,
                        displayName,
                        description,
                        source,
                        preview,
                        resourcePath,
                        symbolMap,
                        materials,
                        xSize,
                        ySize,
                        zSize);
            }
        }
    }

    // ===================================================================
    // 平台预设组（按 name 主键管理）
    // ===================================================================
    public static final class PlatformPreset {

        private final String name;
        @Nullable
        private final String displayName;
        @Nullable
        private final String description;
        @Nullable
        private final String source;
        private final List<PlatformBlockStructure> structures;

        private PlatformPreset(String name,
                               @Nullable String displayName,
                               @Nullable String description,
                               @Nullable String source,
                               List<PlatformBlockStructure> structures) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.source = source;
            this.structures = structures;
        }

        public static PresetBuilder preset(String name) {
            return new PresetBuilder(name);
        }

        public String getName() {
            return name;
        }

        @Nullable
        public String getDisplayName() {
            return displayName;
        }

        @Nullable
        public String getDescription() {
            return description;
        }

        @Nullable
        public String getSource() {
            return source;
        }

        public List<PlatformBlockStructure> getStructures() {
            return structures;
        }

        public static final class PresetBuilder {

            private final String name;
            private String displayName;
            private String description;
            private String source;
            private final List<PlatformBlockStructure> structures = new ArrayList<>();

            public PresetBuilder(String name) {
                this.name = name;
            }

            public PresetBuilder displayName(@Nullable String displayName) {
                this.displayName = displayName;
                return this;
            }

            public PresetBuilder description(@Nullable String description) {
                this.description = description;
                return this;
            }

            public PresetBuilder source(@Nullable String source) {
                this.source = source;
                return this;
            }

            public PresetBuilder addStructure(PlatformBlockStructure structure) {
                structures.add(structure);
                return this;
            }

            public PlatformPreset build() {
                if (structures.isEmpty()) {
                    throw new IllegalStateException("Preset must contain at least one structure");
                }
                return new PlatformPreset(name, displayName, description, source, structures);
            }
        }
    }

    /**
     * 从资源文件读取结构数据
     */
    public static List<String[]> readDepthFromResource(String resourcePath) {
        try (InputStream is = PlatformBlockType.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            Pattern aislePattern = Pattern.compile("\\.aisle\\(([^)]+)\\)");
            Pattern stringPattern = Pattern.compile("\"([^\"]+)\"");
            List<String[]> depth = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(".aisle(")) {
                    Matcher aisleMatcher = aislePattern.matcher(line);
                    if (aisleMatcher.find()) {
                        String content = aisleMatcher.group(1);
                        Matcher stringMatcher = stringPattern.matcher(content);
                        List<String> rows = new ArrayList<>();
                        while (stringMatcher.find()) {
                            rows.add(stringMatcher.group(1));
                        }
                        if (!rows.isEmpty()) {
                            depth.add(rows.toArray(new String[0]));
                        }
                    }
                }
            }
            validateStructure(depth);
            return depth;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load structure from resource: " + resourcePath, e);
        }
    }

    /**
     * 获取指定坐标的方块
     */
    public static BlockState getBlockAt(String resourcePath, Map<Character, BlockState> blockMapping, int x, int y, int z) {
        List<String[]> depth = readDepthFromResource(resourcePath);
        if (z < 0 || z >= depth.size()) throw new IndexOutOfBoundsException("Z index out of bounds");
        String[] aisle = depth.get(z);
        if (y < 0 || y >= aisle.length) throw new IndexOutOfBoundsException("Y index out of bounds");
        String row = aisle[y];
        if (x < 0 || x >= row.length()) throw new IndexOutOfBoundsException("X index out of bounds");
        char symbol = row.charAt(x);
        return blockMapping.get(symbol);
    }

    private static void validateStructure(List<String[]> depth) {
        if (depth == null || depth.isEmpty()) throw new IllegalArgumentException("Depth cannot be empty");

        int ySize = depth.get(0).length;
        int xSize = depth.get(0)[0].length();
        int zSize = depth.size();

        for (String[] aisle : depth) {
            if (aisle == null || aisle.length != ySize) {
                throw new IllegalArgumentException("All aisles must have the same height (y size)");
            }
            for (String row : aisle) {
                if (row == null || row.length() != xSize) {
                    throw new IllegalArgumentException("All rows must have the same width (x size)");
                }
            }
        }

        if (xSize % 16 != 0) throw new IllegalArgumentException("X size must be multiple of 16");
        if (zSize % 16 != 0) throw new IllegalArgumentException("Z size must be multiple of 16");
    }
}
