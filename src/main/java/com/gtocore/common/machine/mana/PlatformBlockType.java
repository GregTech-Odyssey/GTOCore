package com.gtocore.common.machine.mana;

import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public final class PlatformBlockType {

    /**
     * 结构块类型
     */
    public enum BlockType {
        CORE,      // 核心块
        ROAD_X,    // X方向道路
        ROAD_Z,    // Z方向道路
        ROAD_CROSS // 道路交叉点
    }

    // ===================================================================
    // 单个平台结构（链式构建）
    // ===================================================================
    public static final class PlatformBlockStructure {

        private final BlockType type;
        private final List<String[]> depth; // Z -> Y -> X
        private final Char2ObjectMap<Block> blockMapping;
        private final int materials;
        private final int xSize;
        private final int ySize;
        private final int zSize;

        private PlatformBlockStructure(BlockType type,
                                       List<String[]> depth,
                                       Char2ObjectMap<Block> blockMapping,
                                       int materials,
                                       int xSize,
                                       int ySize,
                                       int zSize) {
            this.type = type;
            this.depth = depth;
            this.blockMapping = blockMapping;
            this.materials = materials;
            this.xSize = xSize;
            this.ySize = ySize;
            this.zSize = zSize;
        }

        public static Builder structure(BlockType type) {
            return new Builder(type);
        }

        public BlockType getType() {
            return type;
        }

        public List<String[]> getDepth() {
            return depth;
        }

        public Char2ObjectMap<Block> getBlockMapping() {
            return blockMapping;
        }

        public int getMaterials() {
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

        public Block getBlockAt(int x, int y, int z) {
            if (z < 0 || z >= zSize) throw new IndexOutOfBoundsException("Z index out of bounds");
            if (y < 0 || y >= ySize) throw new IndexOutOfBoundsException("Y index out of bounds");
            if (x < 0 || x >= xSize) throw new IndexOutOfBoundsException("X index out of bounds");
            char symbol = depth.get(z)[y].charAt(x);
            return blockMapping.get(symbol);
        }

        // 链式构建器
        public static final class Builder {

            private final BlockType type;
            private final List<String[]> depth = new ArrayList<>();
            private final Map<Character, Block> symbolMap = new HashMap<>();
            private int materials = 0;

            public Builder(BlockType type) {
                this.type = type;
            }

            // 添加一层（Z方向），每个字符串是一个Y层的行（X方向）
            public Builder addLayer(String... rows) {
                if (rows == null || rows.length == 0) {
                    throw new IllegalArgumentException("Layer cannot be empty");
                }
                depth.add(rows.clone());
                return this;
            }

            // 添加字符到方块的映射
            public Builder where(char symbol, @NotNull Block block) {
                symbolMap.put(symbol, block);
                return this;
            }

            // 设置材料数量
            public Builder materials(int count) {
                this.materials = count;
                return this;
            }

            // 构建结构对象
            public PlatformBlockStructure build() {
                validateStructure(depth);
                if (symbolMap.isEmpty()) {
                    throw new IllegalStateException("No block mappings defined");
                }
                int xSize = depth.get(0)[0].length();
                int ySize = depth.get(0).length;
                int zSize = depth.size();
                return new PlatformBlockStructure(
                        type,
                        depth,
                        new Char2ObjectOpenHashMap<>(symbolMap),
                        materials,
                        xSize,
                        ySize,
                        zSize);
            }
        }
    }

    // ===================================================================
    // 平台预设组（链式构建）
    // ===================================================================
    public static final class PlatformPreset {

        private final String name;
        private final String displayName;
        private final String description;
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

        public PlatformBlockStructure getStructure(BlockType type) {
            for (PlatformBlockStructure s : structures) {
                if (s.getType() == type) return s;
            }
            return null;
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
                validatePreset(structures);
                return new PlatformPreset(name, displayName, description, source, structures);
            }

            private static void validatePreset(List<PlatformBlockStructure> structures) {
                Set<BlockType> types = structures.stream()
                        .map(PlatformBlockStructure::getType)
                        .collect(Collectors.toSet());

                switch (structures.size()) {
                    case 1 -> {
                        if (!types.contains(BlockType.CORE)) {
                            throw new IllegalArgumentException("1 structure must be CORE type");
                        }
                    }
                    case 3 -> {
                        if (!types.equals(Set.of(BlockType.ROAD_X, BlockType.ROAD_Z, BlockType.ROAD_CROSS))) {
                            throw new IllegalArgumentException("3 structures must be ROAD_X, ROAD_Z, ROAD_CROSS");
                        }
                    }
                    case 4 -> {
                        if (!types.equals(Set.of(BlockType.CORE, BlockType.ROAD_X, BlockType.ROAD_Z, BlockType.ROAD_CROSS))) {
                            throw new IllegalArgumentException("4 structures must be CORE, ROAD_X, ROAD_Z, ROAD_CROSS");
                        }
                    }
                    default -> throw new IllegalArgumentException("Preset must contain exactly 1, 3, or 4 structures");
                }
            }
        }
    }

    // ===================================================================
    // 结构校验工具
    // ===================================================================
    private static void validateStructure(List<String[]> depth) {
        if (depth == null || depth.isEmpty()) throw new IllegalArgumentException("Depth cannot be empty");

        int ySize = depth.get(0).length;
        int xSize = depth.get(0)[0].length();
        int zSize = depth.size();

        for (String[] layer : depth) {
            if (layer == null || layer.length != ySize) {
                throw new IllegalArgumentException("All layers must have the same height (y size)");
            }
            for (String row : layer) {
                if (row == null || row.length() != xSize) {
                    throw new IllegalArgumentException("All rows must have the same width (x size)");
                }
            }
        }

        if (xSize % 16 != 0) throw new IllegalArgumentException("X size must be multiple of 16");
        if (zSize % 16 != 0) throw new IllegalArgumentException("Z size must be multiple of 16");

        if (xSize % zSize != 0 && zSize % xSize != 0) {
            throw new IllegalArgumentException("Either X size must be multiple of Z size or vice versa");
        }
    }
}
