package com.gtocore.common.machine.noenergy;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;
import com.gtolib.utils.holder.IntObjectHolder;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class PlatformBlockType {

    // ===================================================================
    // 单平台结构
    // ===================================================================
    public record PlatformBlockStructure(
                                         String name,
                                         @Nullable String type,
                                         @Nullable String displayName,
                                         @Nullable String description,
                                         @Nullable String source,
                                         boolean preview,
                                         ResourceLocation resource,
                                         ResourceLocation blockMapping,
                                         int[] materials,
                                         List<IntObjectHolder<ItemStack>> extraMaterials,
                                         int xSize,
                                         int ySize,
                                         int zSize) {

        public PlatformBlockStructure {
            // 非空检查
            Objects.requireNonNull(name, "name must not be null");
            Objects.requireNonNull(resource, "resource must not be null");
            Objects.requireNonNull(blockMapping, "blockMapping must not be null");
            Objects.requireNonNull(materials, "materials must not be null");
            Objects.requireNonNull(extraMaterials, "extraMaterials must not be null");

            // 防御性拷贝
            materials = Arrays.copyOf(materials, materials.length);
            extraMaterials = List.copyOf(extraMaterials);

            // 业务规则检查
            if (xSize % 16 != 0) throw new IllegalArgumentException("X size must be multiple of 16");
            if (zSize % 16 != 0) throw new IllegalArgumentException("Z size must be multiple of 16");
        }

        public static Builder structure(String name) {
            return new Builder(name);
        }

        public static final class Builder {

            private final String name;
            private String type = "default";
            private String displayName;
            private String description;
            private String source;
            private boolean preview = false;
            private ResourceLocation resource;
            private ResourceLocation symbolMap;
            private final int[] materials = new int[] { 0, 0, 0 };
            private final List<IntObjectHolder<ItemStack>> extraMaterials = new ArrayList<>();

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

            public Builder resource(ResourceLocation resource) {
                this.resource = resource;
                return this;
            }

            public Builder symbolMap(ResourceLocation symbolMap) {
                this.symbolMap = symbolMap;
                return this;
            }

            public Builder materials(int material, int count) {
                this.materials[material] = count;
                return this;
            }

            public Builder extraMaterials(String item, int count) {
                extraMaterials.add(new IntObjectHolder<>(count, RegistriesUtils.getItemStack(item)));
                return this;
            }

            public Builder extraMaterials(Item item, int count) {
                extraMaterials.add(new IntObjectHolder<>(count, new ItemStack(item)));
                return this;
            }

            public Builder extraMaterials(ItemStack stack, int count) {
                ItemStack copy = stack.copy();
                copy.setCount(1);
                extraMaterials.add(new IntObjectHolder<>(count, copy));
                return this;
            }

            public PlatformBlockStructure build() {
                if (GTCEu.isDataGen()) return null;
                if (name == null || name.isEmpty()) {
                    return null;
                }
                if (resource == null) {
                    return null;
                }
                if (symbolMap == null) {
                    return null;
                }

                int[] sizes = new int[2];
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(resource).get().open()))) {
                    String line = reader.readLine().trim();
                    if (line.startsWith(".size(") && line.endsWith(")")) {
                        String[] parts = line.substring(6, line.length() - 1).split(",");
                        sizes = new int[] { Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim()) };
                    }
                } catch (Exception e) {
                    return null;
                }

                return new PlatformBlockStructure(
                        name,
                        type,
                        displayName,
                        description,
                        source,
                        preview,
                        resource,
                        symbolMap,
                        materials,
                        extraMaterials,
                        sizes[0],
                        sizes[1],
                        sizes[2]);
            }
        }
    }

    // ===================================================================
    // 平台预设组
    // ===================================================================
    public record PlatformPreset(
                                 String name,
                                 @Nullable String displayName,
                                 @Nullable String description,
                                 @Nullable String source,
                                 List<PlatformBlockStructure> structures) {

        public PlatformPreset {
            // 非空检查
            Objects.requireNonNull(name, "name must not be null");
            Objects.requireNonNull(structures, "structures must not be null");

            // 防御性拷贝
            structures = List.copyOf(structures);

            // 业务规则检查
            if (structures.isEmpty()) {
                throw new IllegalArgumentException("structures must not be empty");
            }
        }

        public static PresetBuilder preset(String name) {
            return new PresetBuilder(name);
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
                if (structure != null) structures.add(structure);
                return this;
            }

            public PlatformPreset build() {
                if (GTCEu.isDataGen()) return null;
                if (structures.isEmpty()) {
                    GTOCore.LOGGER.error("Preset must contain at least one structure");
                    return null;
                }
                return new PlatformPreset(name, displayName, description, source, structures);
            }
        }
    }
}
