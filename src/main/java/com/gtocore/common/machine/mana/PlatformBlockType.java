package com.gtocore.common.machine.mana;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class PlatformBlockType {

    // ===================================================================
    // 单平台结构
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
        private final String resourcePath;
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
            return Arrays.copyOf(materials, materials.length);
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
            private String type = "default";
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
                if (xSize % 16 != 0) throw new IllegalArgumentException("X size must be multiple of 16");
                if (zSize % 16 != 0) throw new IllegalArgumentException("Z size must be multiple of 16");
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
    // 平台预设组
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
}
