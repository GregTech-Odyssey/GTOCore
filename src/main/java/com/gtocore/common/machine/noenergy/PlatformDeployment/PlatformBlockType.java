package com.gtocore.common.machine.noenergy.PlatformDeployment;

import com.gtolib.GTOCore;
import com.gtolib.utils.MultiBlockFileReader;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.google.common.collect.ImmutableList;
import com.gto.datasynclib.util.holder.IntObjectHolder;
import org.jetbrains.annotations.Nullable;

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
            extraMaterials = ImmutableList.copyOf(extraMaterials);

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
                // String resourcePath = "assets/" + resource.toString().replace(":", "/");
                // try (InputStream input =
                // PlatformStructurePlacer.class.getClassLoader().getResourceAsStream(resourcePath)) {
                // if (input == null) {
                // throw new FileNotFoundException("Structure file not found: " + resource);
                // }
                // var aisles = PlatformStructurePlacer.parseAllAisles(input);
                // MultiBlockFileReader.save(new File(GTOCore.getFile(), resource.getNamespace() + "/" +
                // resource.getPath() + ".mbs"), aisles);
                // } catch (IOException ignored) {}
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
                    GTOCore.LOGGER.error("Platform registration error: missing name");
                    return null;
                }
                if (resource == null) {
                    GTOCore.LOGGER.error("Platform registration error: missing Structural Resources: {}", name);
                    return null;
                }
                if (symbolMap == null) {
                    GTOCore.LOGGER.error("Platform registration error: missing Block Mapping: {}", name);
                    return null;
                }

                int[] sizes = new int[3];
                try {
                    var data = MultiBlockFileReader.load(PlatformBlockType.class.getClassLoader()
                            .getResourceAsStream("platforms/" + resource.toString().replace(":", "/") + ".mbs"));
                    sizes[0] = data.pattern()[0][0].length();
                    sizes[1] = data.pattern()[0].length;
                    sizes[2] = data.pattern().length;
                } catch (Exception e) {
                    GTOCore.LOGGER.error("Failed to read structure size for {}: {}", resource, e.getMessage());
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
            // 业务规则检查
            if (structures.isEmpty()) {
                throw new IllegalArgumentException("structures must not be empty");
            }
            // 防御性拷贝
            structures = ImmutableList.copyOf(structures);
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
                if (name == null || name.isEmpty()) {
                    GTOCore.LOGGER.error("Platform registration group error: missing name");
                    return null;
                }
                if (structures.isEmpty()) {
                    GTOCore.LOGGER.error("Platform registration group error: preset must contain at least one structure");
                    return null;
                }
                return new PlatformPreset(name, displayName, description, source, structures);
            }
        }
    }
}
