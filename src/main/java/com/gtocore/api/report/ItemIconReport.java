package com.gtocore.api.report;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.*;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiStack;
import org.joml.Matrix4f;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

@DataGeneratorScanned
public class ItemIconReport {

    private static final int ICON_SIZE = 64;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String MOD_VERSION = "0.5.4";
    private static final int IO_THREAD_COUNT = Math.max(4, Runtime.getRuntime().availableProcessors());

    // Dual-language maps loaded at export start
    private static Map<String, String> langEN = Collections.emptyMap();
    private static Map<String, String> langZH = Collections.emptyMap();

    /**
     * Load a specific language's translation map from all mod resource packs.
     * Reuses the same approach as ServerLangHook.
     */
    private static Map<String, String> loadLanguageMap(ResourceManager resourceManager, String langCode) {
        Map<String, String> langMap = new HashMap<>(8000);
        String langFile = String.format(Locale.ROOT, "lang/%s.json", langCode);
        for (String namespace : resourceManager.getNamespaces()) {
            try {
                ResourceLocation langResource = new ResourceLocation(namespace, langFile);
                List<Resource> resources = resourceManager.getResourceStack(langResource);
                for (Resource res : resources) {
                    try (InputStream is = res.open()) {
                        JsonElement elem = com.google.gson.JsonParser.parseReader(
                                new InputStreamReader(is, StandardCharsets.UTF_8));
                        JsonObject obj = GsonHelper.convertToJsonObject(elem, "strings");
                        obj.entrySet().forEach(entry -> {
                            String value = GsonHelper.convertToString(entry.getValue(), entry.getKey());
                            langMap.put(entry.getKey(), value);
                        });
                    } catch (Exception ignored) {}
                }
            } catch (Exception ignored) {}
        }
        return langMap;
    }

    /**
     * Get a translated name from a language map, falling back to the key itself.
     */
    private static String tr(Map<String, String> langMap, String key) {
        return langMap.getOrDefault(key, key);
    }

    /**
     * Create a Language instance backed by a translation map.
     * Used for temporarily swapping the active language to collect bilingual tooltips.
     */
    private static Language createLanguage(Map<String, String> langMap) {
        return new Language() {

            @Override
            public String getOrDefault(String key, String defaultValue) {
                return langMap.getOrDefault(key, defaultValue);
            }

            @Override
            public boolean has(String key) {
                return langMap.containsKey(key);
            }

            @Override
            public boolean isDefaultRightToLeft() {
                return false;
            }

            @Override
            public FormattedCharSequence getVisualOrder(FormattedText text) {
                return FormattedCharSequence.EMPTY;
            }
        };
    }

    // Pre-built Language instances for bilingual tooltip collection
    private static Language langInstanceEN;
    private static Language langInstanceZH;

    /**
     * Get tooltip lines for an ItemStack in a specific language by temporarily swapping
     * the active Language instance. Must be called on the main client thread.
     *
     * @return tooltip as a JsonArray of strings, or empty array on failure
     */
    private static JsonArray getItemTooltip(ItemStack stack, Language targetLang, Language originalLang) {
        JsonArray arr = new JsonArray();
        try {
            Language.inject(targetLang);
            List<Component> lines = stack.getTooltipLines(null, TooltipFlag.Default.NORMAL);
            for (Component line : lines) {
                arr.add(line.getString());
            }
        } catch (Exception ignored) {} finally {
            Language.inject(originalLang);
        }
        return arr;
    }

    /**
     * Get display name for a FluidStack in a specific language.
     */
    private static String getFluidDisplayName(Fluid fluid, Language targetLang, Language originalLang) {
        try {
            Language.inject(targetLang);
            return new FluidStack(fluid, 1000).getDisplayName().getString();
        } catch (Exception e) {
            return "";
        } finally {
            Language.inject(originalLang);
        }
    }

    public static void generateReport() {
        GTOCore.LOGGER.info("Starting full game resource report generation...");

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            try {
                exportAllResources();
            } catch (Exception e) {
                GTOCore.LOGGER.error("Error during resource export", e);
            }
        });
    }

    private static void exportAllResources() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Path baseDir = Paths.get("logs", "report", "GTOCore-" + MOD_VERSION, "all_" + timestamp);

            Path itemDir = baseDir.resolve("item");
            Path fluidDir = baseDir.resolve("fluid");
            Path blockDir = baseDir.resolve("block");
            Path tagDir = baseDir.resolve("tag");
            Path recipeDir = baseDir.resolve("recipe");
            Path listDir = baseDir.resolve("list");
            Path miscDir = baseDir.resolve("misc");

            Files.createDirectories(itemDir);
            Files.createDirectories(fluidDir);
            Files.createDirectories(blockDir);
            Files.createDirectories(tagDir);
            Files.createDirectories(recipeDir);
            Files.createDirectories(listDir);
            Files.createDirectories(miscDir);

            GTOCore.LOGGER.info("Output directory: {}", baseDir.toAbsolutePath());

            Minecraft minecraft = Minecraft.getInstance();
            long startTime = System.currentTimeMillis();

            // Load dual-language translation maps (EN + ZH)
            GTOCore.LOGGER.info("Loading language files for dual-language export...");
            ResourceManager resourceManager = minecraft.getResourceManager();
            langEN = loadLanguageMap(resourceManager, "en_us");
            langZH = loadLanguageMap(resourceManager, "zh_cn");
            langInstanceEN = createLanguage(langEN);
            langInstanceZH = createLanguage(langZH);
            GTOCore.LOGGER.info("Loaded {} EN translations, {} ZH translations", langEN.size(), langZH.size());

            // Phase 1: Export icons + EMI stack manifest (MUST run on render thread for OpenGL context)
            GTOCore.LOGGER.info("=== Phase 1/6: Exporting icons (FBO rendering) ===");
            exportIconsByType(minecraft, itemDir, fluidDir, blockDir, listDir);

            // Phase 2-6: Run in parallel using IO thread pool (no OpenGL needed)
            GTOCore.LOGGER.info("=== Phase 2-6: Exporting tags, recipes, metadata, GT environment (parallel) ===");
            ExecutorService ioPool = Executors.newFixedThreadPool(IO_THREAD_COUNT);
            try {
                CompletableFuture<Void> tagsFuture = CompletableFuture.runAsync(() -> {
                    try {
                        GTOCore.LOGGER.info("[Tags] Starting tag export...");
                        exportTags(tagDir);
                        GTOCore.LOGGER.info("[Tags] Tag export completed.");
                    } catch (Exception e) {
                        GTOCore.LOGGER.error("[Tags] Error exporting tags", e);
                    }
                }, ioPool);

                CompletableFuture<Void> recipesFuture = CompletableFuture.runAsync(() -> {
                    try {
                        GTOCore.LOGGER.info("[Recipes] Starting recipe export...");
                        exportRecipes(minecraft, recipeDir);
                        GTOCore.LOGGER.info("[Recipes] Recipe export completed.");
                    } catch (Exception e) {
                        GTOCore.LOGGER.error("[Recipes] Error exporting recipes", e);
                    }
                }, ioPool);

                CompletableFuture<Void> metadataFuture = CompletableFuture.runAsync(() -> {
                    try {
                        GTOCore.LOGGER.info("[Metadata] Starting metadata list export...");
                        exportMetadataLists(listDir);
                        GTOCore.LOGGER.info("[Metadata] Metadata list export completed.");
                    } catch (Exception e) {
                        GTOCore.LOGGER.error("[Metadata] Error exporting metadata", e);
                    }
                }, ioPool);

                CompletableFuture<Void> envFuture = CompletableFuture.runAsync(() -> {
                    try {
                        GTOCore.LOGGER.info("[GTEnv] Starting GT environment export...");
                        exportGTEnvironments(miscDir);
                        GTOCore.LOGGER.info("[GTEnv] GT environment export completed.");
                    } catch (Exception e) {
                        GTOCore.LOGGER.error("[GTEnv] Error exporting GT environment", e);
                    }
                }, ioPool);

                CompletableFuture.allOf(tagsFuture, recipesFuture, metadataFuture, envFuture).join();
            } finally {
                ioPool.shutdown();
            }

            // Generate master report
            generateMasterReport(baseDir);

            long elapsed = System.currentTimeMillis() - startTime;
            GTOCore.LOGGER.info("All resources exported in {}s. Output: {}", String.format("%.1f", elapsed / 1000.0), baseDir.toAbsolutePath());

        } catch (Exception e) {
            GTOCore.LOGGER.error("Error during resource export", e);
        }
    }

    // ==================== Phase 1: Export icons using FBO rendering ====================

    private static void exportIconsByType(Minecraft minecraft, Path itemDir, Path fluidDir, Path blockDir, Path listDir) throws IOException {
        AtomicInteger totalItemsExported = new AtomicInteger();
        AtomicInteger totalItemsFailed = new AtomicInteger();
        AtomicInteger totalFluidsExported = new AtomicInteger();
        AtomicInteger totalFluidsFailed = new AtomicInteger();
        AtomicInteger totalBlocksExported = new AtomicInteger();
        AtomicInteger totalBlocksFailed = new AtomicInteger();

        Map<String, Integer> itemModCount = new ConcurrentHashMap<>();
        Map<String, Integer> fluidModCount = new ConcurrentHashMap<>();
        Map<String, Integer> blockModCount = new ConcurrentHashMap<>();

        // EMI stack manifest — collect bilingual metadata for all exported stacks
        JsonArray emiStacksArray = new JsonArray();

        // Track exported IDs to avoid duplicates in supplementary registry pass
        Set<ResourceLocation> exportedIds = new HashSet<>();

        java.util.List<EmiStack> allStacks = EmiApi.getIndexStacks();
        GTOCore.LOGGER.info("[Icons] Found {} EMI stacks to export", allStacks.size());

        // Create FBO for item/block rendering (reused across all items)
        RenderTarget fbo = new TextureTarget(ICON_SIZE, ICON_SIZE, true, Minecraft.ON_OSX);

        // IO thread pool for parallel PNG writing (rendering stays on render thread)
        ExecutorService writerPool = Executors.newFixedThreadPool(IO_THREAD_COUNT);
        List<Future<?>> writeFutures = new ArrayList<>();

        try {
            for (int i = 0; i < allStacks.size(); i++) {
                EmiStack stack = allStacks.get(i);
                if (stack.isEmpty()) continue;

                try {
                    ResourceLocation id = stack.getId();
                    if (id == null) continue;
                    exportedIds.add(id); // Track for supplementary pass

                    String modId = id.getNamespace();
                    String path = id.getPath();
                    String stackType = getStackType(stack);

                    Path targetDir;
                    AtomicInteger successCounter;
                    AtomicInteger failCounter;
                    Map<String, Integer> countMap;

                    switch (stackType) {
                        case "fluid" -> {
                            targetDir = fluidDir.resolve(modId);
                            successCounter = totalFluidsExported;
                            failCounter = totalFluidsFailed;
                            countMap = fluidModCount;
                        }
                        case "block" -> {
                            targetDir = blockDir.resolve(modId);
                            successCounter = totalBlocksExported;
                            failCounter = totalBlocksFailed;
                            countMap = blockModCount;
                        }
                        default -> {
                            targetDir = itemDir.resolve(modId);
                            successCounter = totalItemsExported;
                            failCounter = totalItemsFailed;
                            countMap = itemModCount;
                        }
                    }

                    Files.createDirectories(targetDir);

                    // Render on the render thread (current thread) — captures full ItemColors tinting
                    BufferedImage image = captureStackIcon(minecraft, stack, fbo);

                    if (image != null) {
                        // Offload PNG writing to IO thread
                        final Path dir = targetDir;
                        final String itemPath = path;
                        final String mod = modId;
                        writeFutures.add(writerPool.submit(() -> {
                            try {
                                String fileName = itemPath.replace('/', '_') + ".png";
                                ImageIO.write(image, "png", dir.resolve(fileName).toFile());
                                successCounter.incrementAndGet();
                                countMap.merge(mod, 1, Integer::sum);
                            } catch (Exception e) {
                                failCounter.incrementAndGet();
                            }
                        }));
                    } else {
                        failCounter.incrementAndGet();
                    }

                    // Collect EMI stack manifest entry with bilingual names and tooltips
                    try {
                        Language originalLang = Language.getInstance();

                        JsonObject entry = new JsonObject();
                        entry.addProperty("id", id.toString());
                        entry.addProperty("type", stackType);
                        entry.addProperty("namespace", modId);
                        entry.addProperty("path", path);

                        // Resolve translation key and bilingual names/tooltips based on type
                        String descKey;
                        if ("fluid".equals(stackType)) {
                            Object key = stack.getKey();
                            Fluid fluid = key instanceof Fluid f ? f : null;
                            descKey = fluid != null ? fluid.getFluidType().getDescriptionId() : id.toLanguageKey("fluid_type");
                            entry.addProperty("description_id", descKey);
                            entry.addProperty("name_en", tr(langEN, descKey));
                            entry.addProperty("name_zh", tr(langZH, descKey));
                            // Fluid runtime names via FluidStack (language-aware)
                            if (fluid != null) {
                                entry.addProperty("display_name_en",
                                        getFluidDisplayName(fluid, langInstanceEN, originalLang));
                                entry.addProperty("display_name_zh",
                                        getFluidDisplayName(fluid, langInstanceZH, originalLang));
                            }
                        } else {
                            // Items and blocks
                            ItemStack itemStack = stack.getItemStack();
                            if (itemStack != null && !itemStack.isEmpty()) {
                                descKey = itemStack.getDescriptionId();
                                entry.addProperty("description_id", descKey);
                                entry.addProperty("name_en", tr(langEN, descKey));
                                entry.addProperty("name_zh", tr(langZH, descKey));

                                // Include NBT for items that have it (circuit variants, enchanted books, etc.)
                                if (itemStack.hasTag()) {
                                    entry.addProperty("nbt", itemStack.getTag().toString());
                                }

                                // Detect programmed circuit and annotate configuration
                                try {
                                    if (itemStack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                                        int config = IntCircuitBehaviour.getCircuitConfiguration(itemStack);
                                        entry.addProperty("is_circuit", true);
                                        entry.addProperty("circuit_config", config);
                                    }
                                } catch (Exception ignored) {}

                                // Runtime display names (NBT-aware: enchanted books, potions, etc.)
                                try {
                                    Language.inject(langInstanceEN);
                                    entry.addProperty("display_name_en", itemStack.getHoverName().getString());
                                } catch (Exception ignored) {} finally {
                                    Language.inject(originalLang);
                                }
                                try {
                                    Language.inject(langInstanceZH);
                                    entry.addProperty("display_name_zh", itemStack.getHoverName().getString());
                                } catch (Exception ignored) {} finally {
                                    Language.inject(originalLang);
                                }

                                // Bilingual tooltips (full tooltip lines including GT stats, enchantments, lore)
                                entry.add("tooltip_en", getItemTooltip(itemStack, langInstanceEN, originalLang));
                                entry.add("tooltip_zh", getItemTooltip(itemStack, langInstanceZH, originalLang));
                            } else {
                                descKey = id.toLanguageKey(stackType);
                                entry.addProperty("description_id", descKey);
                                entry.addProperty("name_en", tr(langEN, descKey));
                                entry.addProperty("name_zh", tr(langZH, descKey));
                            }
                        }

                        String iconFile = stackType + "/" + modId + "/" + path.replace('/', '_') + ".png";
                        entry.addProperty("icon_file", iconFile);
                        emiStacksArray.add(entry);
                    } catch (Exception ignored) {}

                } catch (Exception e) {
                    // count as failure silently
                }

                if ((i + 1) % 1000 == 0) {
                    GTOCore.LOGGER.info("[Icons] Progress: {}/{} (items:{}, fluids:{}, blocks:{})",
                            i + 1, allStacks.size(),
                            totalItemsExported.get(), totalFluidsExported.get(), totalBlocksExported.get());
                }
            }

            // === Supplementary Registry Pass ===
            // Export icons for ALL registered items/fluids NOT covered by EMI's index.
            // JEI/REI iterate registries directly — this ensures comprehensive coverage
            // regardless of EMI's index state or hidden item configuration.
            GTOCore.LOGGER.info("[Icons] Starting supplementary registry pass (items not in EMI index)...");

            Language originalLangSupp = Language.getInstance();

            // Supplementary: Items (includes BlockItems for block icons)
            int suppItemCount = 0;
            for (Item item : BuiltInRegistries.ITEM) {
                try {
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
                    if (id == null || exportedIds.contains(id)) continue;
                    exportedIds.add(id);

                    ItemStack itemStack = new ItemStack(item);
                    if (itemStack.isEmpty()) continue; // Skip air and uninstantiable items

                    String modId = id.getNamespace();
                    String path = id.getPath();
                    String stackType = (item instanceof BlockItem) ? "block" : "item";

                    Path targetDir;
                    AtomicInteger successCounter;
                    AtomicInteger failCounter;
                    Map<String, Integer> countMap;

                    if ("block".equals(stackType)) {
                        targetDir = blockDir.resolve(modId);
                        successCounter = totalBlocksExported;
                        failCounter = totalBlocksFailed;
                        countMap = blockModCount;
                    } else {
                        targetDir = itemDir.resolve(modId);
                        successCounter = totalItemsExported;
                        failCounter = totalItemsFailed;
                        countMap = itemModCount;
                    }

                    Files.createDirectories(targetDir);
                    BufferedImage image = captureItemIconFBO(minecraft, itemStack, fbo);

                    if (image != null) {
                        final Path dir = targetDir;
                        final String itemPath = path;
                        final String mod = modId;
                        writeFutures.add(writerPool.submit(() -> {
                            try {
                                String fileName = itemPath.replace('/', '_') + ".png";
                                ImageIO.write(image, "png", dir.resolve(fileName).toFile());
                                successCounter.incrementAndGet();
                                countMap.merge(mod, 1, Integer::sum);
                            } catch (Exception e) {
                                failCounter.incrementAndGet();
                            }
                        }));
                    } else {
                        failCounter.incrementAndGet();
                    }

                    // Add to manifest with bilingual names/tooltips
                    try {
                        JsonObject entry = new JsonObject();
                        entry.addProperty("id", id.toString());
                        entry.addProperty("type", stackType);
                        entry.addProperty("namespace", modId);
                        entry.addProperty("path", path);

                        String descKey = itemStack.getDescriptionId();
                        entry.addProperty("description_id", descKey);
                        entry.addProperty("name_en", tr(langEN, descKey));
                        entry.addProperty("name_zh", tr(langZH, descKey));

                        try {
                            Language.inject(langInstanceEN);
                            entry.addProperty("display_name_en", itemStack.getHoverName().getString());
                        } catch (Exception ignored) {} finally {
                            Language.inject(originalLangSupp);
                        }
                        try {
                            Language.inject(langInstanceZH);
                            entry.addProperty("display_name_zh", itemStack.getHoverName().getString());
                        } catch (Exception ignored) {} finally {
                            Language.inject(originalLangSupp);
                        }

                        entry.add("tooltip_en", getItemTooltip(itemStack, langInstanceEN, originalLangSupp));
                        entry.add("tooltip_zh", getItemTooltip(itemStack, langInstanceZH, originalLangSupp));

                        String iconFile = stackType + "/" + modId + "/" + path.replace('/', '_') + ".png";
                        entry.addProperty("icon_file", iconFile);
                        emiStacksArray.add(entry);
                    } catch (Exception ignored) {}

                    suppItemCount++;
                } catch (Exception e) {
                    GTOCore.LOGGER.debug("[Icons] Supplementary item export failed: {}", item, e);
                }
            }

            // Supplementary: Fluids (source forms only, skip flowing variants)
            int suppFluidCount = 0;
            for (Fluid fluid : BuiltInRegistries.FLUID) {
                try {
                    ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);
                    if (id == null || exportedIds.contains(id)) continue;
                    exportedIds.add(id);

                    // Skip non-source fluids (flowing variants, empty fluid)
                    if (!fluid.defaultFluidState().isSource()) continue;

                    String modId = id.getNamespace();
                    String path = id.getPath();

                    Path targetDir = fluidDir.resolve(modId);
                    Files.createDirectories(targetDir);
                    BufferedImage image = captureFluidIconFBO(minecraft, fluid, fbo);

                    if (image != null) {
                        final Path dir = targetDir;
                        final String fPath = path;
                        final String mod = modId;
                        writeFutures.add(writerPool.submit(() -> {
                            try {
                                String fileName = fPath.replace('/', '_') + ".png";
                                ImageIO.write(image, "png", dir.resolve(fileName).toFile());
                                totalFluidsExported.incrementAndGet();
                                fluidModCount.merge(mod, 1, Integer::sum);
                            } catch (Exception e) {
                                totalFluidsFailed.incrementAndGet();
                            }
                        }));
                    } else {
                        totalFluidsFailed.incrementAndGet();
                    }

                    // Add to manifest with bilingual names
                    try {
                        JsonObject entry = new JsonObject();
                        entry.addProperty("id", id.toString());
                        entry.addProperty("type", "fluid");
                        entry.addProperty("namespace", modId);
                        entry.addProperty("path", path);

                        String descKey = fluid.getFluidType().getDescriptionId();
                        entry.addProperty("description_id", descKey);
                        entry.addProperty("name_en", tr(langEN, descKey));
                        entry.addProperty("name_zh", tr(langZH, descKey));

                        entry.addProperty("display_name_en",
                                getFluidDisplayName(fluid, langInstanceEN, originalLangSupp));
                        entry.addProperty("display_name_zh",
                                getFluidDisplayName(fluid, langInstanceZH, originalLangSupp));

                        String iconFile = "fluid/" + modId + "/" + path.replace('/', '_') + ".png";
                        entry.addProperty("icon_file", iconFile);
                        emiStacksArray.add(entry);
                    } catch (Exception ignored) {}

                    suppFluidCount++;
                } catch (Exception e) {
                    GTOCore.LOGGER.debug("[Icons] Supplementary fluid export failed: {}", fluid, e);
                }
            }

            GTOCore.LOGGER.info("[Icons] Supplementary pass complete: {} items, {} fluids from registries (not in EMI index)",
                    suppItemCount, suppFluidCount);

        } finally {
            // Cleanup FBO and restore main render target
            fbo.destroyBuffers();
            minecraft.getMainRenderTarget().bindWrite(true);
        }

        // Wait for all PNG writes to complete
        GTOCore.LOGGER.info("[Icons] Waiting for {} PNG write operations to finish...", writeFutures.size());
        for (Future<?> f : writeFutures) {
            try {
                f.get();
            } catch (Exception ignored) {}
        }
        writerPool.shutdown();

        GTOCore.LOGGER.info("[Icons] Item icons: {} exported, {} failed", totalItemsExported.get(), totalItemsFailed.get());
        GTOCore.LOGGER.info("[Icons] Fluid icons: {} exported, {} failed", totalFluidsExported.get(), totalFluidsFailed.get());
        GTOCore.LOGGER.info("[Icons] Block icons: {} exported, {} failed", totalBlocksExported.get(), totalBlocksFailed.get());

        // Save per-type statistics
        saveStats(itemDir, totalItemsExported.get(), totalItemsFailed.get(), itemModCount);
        saveStats(fluidDir, totalFluidsExported.get(), totalFluidsFailed.get(), fluidModCount);
        saveStats(blockDir, totalBlocksExported.get(), totalBlocksFailed.get(), blockModCount);

        // Write EMI stack manifest with bilingual names
        JsonObject manifestRoot = new JsonObject();
        manifestRoot.addProperty("type", "emi_stacks");
        manifestRoot.addProperty("count", emiStacksArray.size());
        manifestRoot.add("stacks", emiStacksArray);
        Files.writeString(listDir.resolve("emi_stacks.json"), GSON.toJson(manifestRoot));
        GTOCore.LOGGER.info("[Icons] EMI stack manifest exported: {} stacks", emiStacksArray.size());
    }

    /**
     * Capture icon for any EmiStack type using appropriate rendering method.
     * Must be called on the render thread.
     */
    private static BufferedImage captureStackIcon(Minecraft minecraft, EmiStack stack, RenderTarget fbo) {
        Object key = stack.getKey();

        if (key instanceof Fluid fluid) {
            return captureFluidIconFBO(minecraft, fluid, fbo);
        }

        // Items and Blocks: use FBO rendering with full ItemRenderer pipeline
        ItemStack itemStack = stack.getItemStack();
        if (itemStack != null && !itemStack.isEmpty()) {
            return captureItemIconFBO(minecraft, itemStack, fbo);
        }

        return null;
    }

    /**
     * Render an ItemStack to a BufferedImage using FBO (Framebuffer Object).
     * This captures the FULL rendering pipeline including:
     * - Multi-layer model rendering (base + overlay)
     * - ItemColors tinting (GT material colors, dye colors, etc.)
     * - All visual overlays
     *
     * This is the same pipeline EMI uses to display items in its GUI.
     * Must be called on the render thread.
     */
    private static BufferedImage captureItemIconFBO(Minecraft minecraft, ItemStack stack, RenderTarget fbo) {
        try {
            // Save current GL state
            RenderTarget previousTarget = minecraft.getMainRenderTarget();

            // Clear FBO with transparent background
            fbo.setClearColor(0f, 0f, 0f, 0f);
            fbo.clear(Minecraft.ON_OSX);
            fbo.bindWrite(true);

            // Set up orthographic projection matching GUI rendering
            Matrix4f projMatrix = new Matrix4f().setOrtho(
                    0, ICON_SIZE, ICON_SIZE, 0, 1000.0f, 21000.0f);
            RenderSystem.setProjectionMatrix(projMatrix, VertexSorting.DISTANCE_TO_ORIGIN);

            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.setIdentity();
            modelViewStack.translate(0, 0, -11000.0f);
            RenderSystem.applyModelViewMatrix();

            // Standard GUI item lighting (same as inventory rendering)
            Lighting.setupFor3DItems();

            // Render using GuiGraphics — goes through the full ItemRenderer pipeline
            // which calls ItemColors.getColor(stack, tintIndex) for each BakedQuad
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            GuiGraphics guiGraphics = new GuiGraphics(minecraft, bufferSource);

            // Scale to fill the FBO (items render at 16x16 by default, we want ICON_SIZE)
            float scale = ICON_SIZE / 16.0f;
            guiGraphics.pose().scale(scale, scale, scale);

            // This is the key call — full rendering with ItemColors, multi-layer tints
            guiGraphics.renderItem(stack, 0, 0);
            guiGraphics.flush();

            // Read pixels from FBO into BufferedImage
            BufferedImage image = readFBOPixels(fbo);

            // Restore GL state
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
            previousTarget.bindWrite(true);

            // Validate the captured image
            if (image != null && isMissingTexture(image)) {
                return null;
            }
            if (image != null && isBlankImage(image)) {
                return null;
            }

            return image;

        } catch (Exception e) {
            GTOCore.LOGGER.debug("FBO render failed for: {}", BuiltInRegistries.ITEM.getKey(stack.getItem()), e);
            return null;
        }
    }

    /**
     * Capture fluid icon using FBO rendering with tinted sprite quad.
     * Matches EMI's FluidEmiStack rendering approach:
     * 1. Get still texture and tint color via IClientFluidTypeExtensions (Forge API)
     * 2. Render the sprite into FBO using GuiGraphics.blit() with RGBA tint
     * 3. Read pixels from FBO with proper Y-flip
     * Must be called on the render thread.
     */
    private static BufferedImage captureFluidIconFBO(Minecraft minecraft, Fluid fluid, RenderTarget fbo) {
        try {
            IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid);
            if (ext == null) return null;

            ResourceLocation stillTexture = ext.getStillTexture();
            if (stillTexture == null) return null;

            TextureAtlasSprite sprite = minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
            if (sprite == null) return null;

            int tintColor = ext.getTintColor();

            // Save current GL state
            RenderTarget previousTarget = minecraft.getMainRenderTarget();

            // Clear FBO with transparent background
            fbo.setClearColor(0f, 0f, 0f, 0f);
            fbo.clear(Minecraft.ON_OSX);
            fbo.bindWrite(true);

            // Set up orthographic projection matching GUI rendering
            Matrix4f projMatrix = new Matrix4f().setOrtho(
                    0, ICON_SIZE, ICON_SIZE, 0, 1000.0f, 21000.0f);
            RenderSystem.setProjectionMatrix(projMatrix, VertexSorting.DISTANCE_TO_ORIGIN);

            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.setIdentity();
            modelViewStack.translate(0, 0, -11000.0f);
            RenderSystem.applyModelViewMatrix();

            // Render fluid sprite with tint color (same approach as EMI's drawTintedSprite)
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            GuiGraphics guiGraphics = new GuiGraphics(minecraft, bufferSource);

            // Scale to fill the FBO (sprites render at 16x16 by default, we want ICON_SIZE)
            float scale = ICON_SIZE / 16.0f;
            guiGraphics.pose().scale(scale, scale, scale);

            // Extract RGBA components from tint color
            float a = ((tintColor >> 24) & 0xFF) / 255.0f;
            float r = ((tintColor >> 16) & 0xFF) / 255.0f;
            float g = ((tintColor >> 8) & 0xFF) / 255.0f;
            float b = (tintColor & 0xFF) / 255.0f;
            if (a == 0f) a = 1.0f; // Default to fully opaque if alpha is zero

            guiGraphics.blit(0, 0, 0, 16, 16, sprite, r, g, b, a);
            guiGraphics.flush();

            // Read pixels from FBO into BufferedImage
            BufferedImage image = readFBOPixels(fbo);

            // Restore GL state
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
            previousTarget.bindWrite(true);

            // Validate the captured image
            if (image != null && isMissingTexture(image)) return null;
            if (image != null && isBlankImage(image)) return null;

            return image;

        } catch (Exception e) {
            GTOCore.LOGGER.debug("FBO fluid render failed: {}", BuiltInRegistries.FLUID.getKey(fluid), e);
            return null;
        }
    }

    /**
     * Read pixels from FBO into a BufferedImage.
     * Uses RenderSystem.bindTexture (same as Minecraft's Screenshot.takeScreenshot)
     * and flips Y-axis since OpenGL framebuffers store pixels bottom-to-top.
     * NativeImage.getPixelRGBA() returns ABGR despite its name, so channel swap is needed.
     */
    private static BufferedImage readFBOPixels(RenderTarget fbo) {
        try {
            RenderSystem.bindTexture(fbo.getColorTextureId());
            NativeImage nativeImage = new NativeImage(fbo.width, fbo.height, false);
            nativeImage.downloadTexture(0, false);
            nativeImage.flipY();

            BufferedImage image = new BufferedImage(fbo.width, fbo.height, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < fbo.height; y++) {
                for (int x = 0; x < fbo.width; x++) {
                    int pixel = nativeImage.getPixelRGBA(x, y); // Actually ABGR format
                    int a = (pixel >> 24) & 0xFF;
                    int b = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int r = pixel & 0xFF;

                    // Preserve alpha transparency for proper PNG output
                    image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                }
            }

            nativeImage.close();
            return image;
        } catch (Exception e) {
            GTOCore.LOGGER.debug("Failed to read FBO pixels", e);
            return null;
        }
    }

    private static void saveStats(Path dir, int exported, int failed, Map<String, Integer> modCount) throws IOException {
        JsonObject stats = new JsonObject();
        stats.addProperty("total_exported", exported);
        stats.addProperty("total_failed", failed);
        stats.add("by_mod", GSON.toJsonTree(new TreeMap<>(modCount)));
        Files.writeString(dir.resolve("_stats.json"), GSON.toJson(stats));
    }

    private static String getStackType(EmiStack stack) {
        Object key = stack.getKey();
        if (key instanceof Fluid) return "fluid";
        if (key instanceof Item item) {
            if (item instanceof BlockItem) return "block";
            return "item";
        }
        // Fallback to class name check
        String className = stack.getClass().getSimpleName().toLowerCase();
        if (className.contains("fluid")) return "fluid";
        return "item";
    }

    // ==================== Texture utility methods ====================

    /**
     * Detect missing texture (purple/black checkerboard pattern).
     * Skips fully transparent pixels (alpha=0) to work correctly with transparent images.
     */
    private static boolean isMissingTexture(BufferedImage image) {
        if (image == null) return false;

        int width = image.getWidth();
        int height = image.getHeight();
        if (width < 2 || height < 2) return false;

        int purple = 0xF800F8;
        int black = 0x000000;
        int purpleCount = 0;
        int blackCount = 0;
        int opaqueCount = 0;

        // Sample 25 points in a 5x5 grid for robust detection
        for (int gy = 0; gy < 5; gy++) {
            for (int gx = 0; gx < 5; gx++) {
                int px = Math.min(gx * width / 5 + width / 10, width - 1);
                int py = Math.min(gy * height / 5 + height / 10, height - 1);
                int argb = image.getRGB(px, py);
                int a = (argb >> 24) & 0xFF;
                if (a < 10) continue; // Skip transparent pixels
                opaqueCount++;
                int rgb = argb & 0xFFFFFF;
                if (isSimilarColor(rgb, purple)) purpleCount++;
                else if (isSimilarColor(rgb, black)) blackCount++;
            }
        }

        // Need sufficient opaque samples and both colors present
        return opaqueCount >= 6 && purpleCount >= 2 && blackCount >= 2 && (purpleCount + blackCount) * 2 >= opaqueCount;
    }

    /**
     * Check if image has zero visible content — truly empty render (all pixels fully transparent).
     * This replaces the old corner-sampling approach that falsely rejected transparent items
     * like saplings, candles, flowers, and leaves.
     */
    private static boolean isBlankImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Sample a grid of points across the image; if ANY pixel has alpha > 0, the render succeeded
        int step = Math.max(1, Math.min(width, height) / 8);
        for (int y = 0; y < height; y += step) {
            for (int x = 0; x < width; x += step) {
                int a = (image.getRGB(x, y) >> 24) & 0xFF;
                if (a > 0) return false; // Found a visible pixel — not blank
            }
        }
        return true; // Truly empty render
    }

    private static boolean isSimilarColor(int color1, int color2) {
        int diff = Math.abs(((color1 >> 16) & 0xFF) - ((color2 >> 16) & 0xFF)) + Math.abs(((color1 >> 8) & 0xFF) - ((color2 >> 8) & 0xFF)) + Math.abs((color1 & 0xFF) - (color2 & 0xFF));
        return diff < 50;
    }

    // ==================== Phase 2: Export tags ====================

    private static void exportTags(Path tagDir) throws IOException {
        Path itemTagDir = tagDir.resolve("item");
        Path fluidTagDir = tagDir.resolve("fluid");
        Path blockTagDir = tagDir.resolve("block");
        Files.createDirectories(itemTagDir);
        Files.createDirectories(fluidTagDir);
        Files.createDirectories(blockTagDir);

        // Run item, fluid, block tag exports in parallel
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            CompletableFuture<Integer> itemF = CompletableFuture.supplyAsync(() -> {
                try {
                    return exportItemTags(itemTagDir);
                } catch (Exception e) {
                    GTOCore.LOGGER.error("[Tags] Item tag export failed", e);
                    return 0;
                }
            }, pool);

            CompletableFuture<Integer> fluidF = CompletableFuture.supplyAsync(() -> {
                try {
                    return exportFluidTags(fluidTagDir);
                } catch (Exception e) {
                    GTOCore.LOGGER.error("[Tags] Fluid tag export failed", e);
                    return 0;
                }
            }, pool);

            CompletableFuture<Integer> blockF = CompletableFuture.supplyAsync(() -> {
                try {
                    return exportBlockTags(blockTagDir);
                } catch (Exception e) {
                    GTOCore.LOGGER.error("[Tags] Block tag export failed", e);
                    return 0;
                }
            }, pool);

            int itemTags = itemF.join();
            int fluidTags = fluidF.join();
            int blockTags = blockF.join();
            int totalTags = itemTags + fluidTags + blockTags;

            GTOCore.LOGGER.info("[Tags] Completed: {} total ({} item, {} fluid, {} block)", totalTags, itemTags, fluidTags, blockTags);

            JsonObject tagStats = new JsonObject();
            tagStats.addProperty("total_tags", totalTags);
            tagStats.addProperty("item_tags", itemTags);
            tagStats.addProperty("fluid_tags", fluidTags);
            tagStats.addProperty("block_tags", blockTags);
            Files.writeString(tagDir.resolve("_stats.json"), GSON.toJson(tagStats));
        } finally {
            pool.shutdown();
        }
    }

    private static int exportItemTags(Path itemTagDir) throws IOException {
        int count = 0;
        for (var tagEntry : BuiltInRegistries.ITEM.getTags().toList()) {
            TagKey<Item> tagKey = tagEntry.getFirst();
            var holders = tagEntry.getSecond();

            JsonObject tagJson = new JsonObject();
            tagJson.addProperty("tag", tagKey.location().toString());
            tagJson.addProperty("type", "item");

            JsonArray items = new JsonArray();
            holders.forEach(h -> items.add(BuiltInRegistries.ITEM.getKey(h.value()).toString()));
            tagJson.add("items", items);
            tagJson.addProperty("count", items.size());

            String fileName = tagKey.location().getNamespace() + "_" +
                    tagKey.location().getPath().replace('/', '_') + ".json";
            Files.writeString(itemTagDir.resolve(fileName), GSON.toJson(tagJson));
            count++;
        }
        return count;
    }

    private static int exportFluidTags(Path fluidTagDir) throws IOException {
        int count = 0;
        for (var tagEntry : BuiltInRegistries.FLUID.getTags().toList()) {
            TagKey<Fluid> tagKey = tagEntry.getFirst();
            var holders = tagEntry.getSecond();

            JsonObject tagJson = new JsonObject();
            tagJson.addProperty("tag", tagKey.location().toString());
            tagJson.addProperty("type", "fluid");

            JsonArray fluids = new JsonArray();
            holders.forEach(h -> fluids.add(BuiltInRegistries.FLUID.getKey(h.value()).toString()));
            tagJson.add("fluids", fluids);
            tagJson.addProperty("count", fluids.size());

            String fileName = tagKey.location().getNamespace() + "_" +
                    tagKey.location().getPath().replace('/', '_') + ".json";
            Files.writeString(fluidTagDir.resolve(fileName), GSON.toJson(tagJson));
            count++;
        }
        return count;
    }

    private static int exportBlockTags(Path blockTagDir) throws IOException {
        int count = 0;
        for (var tagEntry : BuiltInRegistries.BLOCK.getTags().toList()) {
            var tagKey = tagEntry.getFirst();
            var holders = tagEntry.getSecond();

            JsonObject tagJson = new JsonObject();
            tagJson.addProperty("tag", tagKey.location().toString());
            tagJson.addProperty("type", "block");

            JsonArray blocks = new JsonArray();
            holders.forEach(h -> blocks.add(BuiltInRegistries.BLOCK.getKey(h.value()).toString()));
            tagJson.add("blocks", blocks);
            tagJson.addProperty("count", blocks.size());

            String fileName = tagKey.location().getNamespace() + "_" +
                    tagKey.location().getPath().replace('/', '_') + ".json";
            Files.writeString(blockTagDir.resolve(fileName), GSON.toJson(tagJson));
            count++;
        }
        return count;
    }

    // ==================== Phase 3: Export recipes ====================

    private static void exportRecipes(Minecraft minecraft, Path recipeDir) throws IOException {
        Map<String, Integer> recipeTypeCount = new ConcurrentHashMap<>();

        Path gtRecipeDir = recipeDir.resolve("gregtech");
        Path vanillaRecipeDir = recipeDir.resolve("vanilla");
        Files.createDirectories(gtRecipeDir);
        Files.createDirectories(vanillaRecipeDir);

        // GT and vanilla recipes in parallel
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            // CompletableFuture<Integer> gtF = CompletableFuture.supplyAsync(() -> {
            // try {
            // return exportGTRecipes(gtRecipeDir, recipeTypeCount);
            // } catch (Exception e) {
            // GTOCore.LOGGER.error("[Recipes] GT recipe export failed", e);
            // return 0;
            // }
            // }, pool);

            CompletableFuture<Integer> vanillaF = CompletableFuture.supplyAsync(() -> {
                try {
                    return exportVanillaRecipes(minecraft, vanillaRecipeDir, recipeTypeCount);
                } catch (Exception e) {
                    GTOCore.LOGGER.error("[Recipes] Vanilla recipe export failed", e);
                    return 0;
                }
            }, pool);

            // int gtRecipes = gtF.join();
            int vanillaRecipes = vanillaF.join();
            // int totalRecipes = gtRecipes + vanillaRecipes;

            // GTOCore.LOGGER.info("[Recipes] Completed: {} total ({} GT, {} vanilla)", totalRecipes, gtRecipes,
            // vanillaRecipes);

            JsonObject recipeStats = new JsonObject();
            // recipeStats.addProperty("total_recipes", totalRecipes);
            // recipeStats.addProperty("gt_recipes", gtRecipes);
            recipeStats.addProperty("vanilla_recipes", vanillaRecipes);
            recipeStats.add("by_type", GSON.toJsonTree(new TreeMap<>(recipeTypeCount)));
            Files.writeString(recipeDir.resolve("_stats.json"), GSON.toJson(recipeStats));
        } finally {
            pool.shutdown();
        }
    }
    //
    // private static int exportGTRecipes(Path gtRecipeDir, Map<String, Integer> recipeTypeCount) {
    // int totalCount = 0;
    //
    // try {
    // Files.createDirectories(gtRecipeDir);
    // } catch (IOException e) {
    // GTOCore.LOGGER.error("[Recipes] Cannot create GT recipe directory", e);
    // return 0;
    // }
    //
    // var allRecipes = com.gtolib.api.recipe.RecipeBuilder.RECIPE_MAP;
    // if (allRecipes == null || allRecipes.isEmpty()) {
    // GTOCore.LOGGER.warn("[Recipes] Cannot export GT recipes: RECIPE_MAP is empty");
    // return 0;
    // }
    //
    // GTOCore.LOGGER.info("[Recipes] Found {} GT recipes", allRecipes.size());
    //
    // // Group recipes by GT recipe type
    // Map<GTRecipeType, List<com.gtolib.api.recipe.Recipe>> recipesByType = new HashMap<>();
    // for (var recipe : allRecipes.values()) {
    // if (recipe != null && recipe.recipeType != null) {
    // recipesByType.computeIfAbsent(recipe.recipeType, k -> new ArrayList<>()).add(recipe);
    // }
    // }
    //
    // for (Map.Entry<GTRecipeType, List<com.gtolib.api.recipe.Recipe>> entry : recipesByType.entrySet()) {
    // GTRecipeType recipeType = entry.getKey();
    // List<com.gtolib.api.recipe.Recipe> recipes = entry.getValue();
    // String typeName = recipeType.registryName.getPath();
    //
    // try {
    // JsonObject typeJson = new JsonObject();
    // typeJson.addProperty("recipe_type", recipeType.registryName.toString());
    // typeJson.addProperty("recipe_type_name", typeName);
    // typeJson.addProperty("count", recipes.size());
    //
    // JsonArray recipesArray = new JsonArray();
    // for (com.gtolib.api.recipe.Recipe recipe : recipes) {
    // try {
    // recipesArray.add(serializeGTRecipe(recipe, recipeType));
    // } catch (Exception e) {
    // GTOCore.LOGGER.debug("[Recipes] Cannot serialize recipe: {}", recipe.getId(), e);
    // }
    // }
    // typeJson.add("recipes", recipesArray);
    //
    // Files.writeString(gtRecipeDir.resolve(typeName + ".json"), GSON.toJson(typeJson));
    // recipeTypeCount.put("gt_" + typeName, recipes.size());
    // totalCount += recipes.size();
    //
    // } catch (Exception e) {
    // GTOCore.LOGGER.error("[Recipes] Error exporting GT recipe type: {}", typeName, e);
    // }
    // }
    //
    // return totalCount;
    // }
    //
    // private static JsonObject serializeGTRecipe(com.gtolib.api.recipe.Recipe recipe, GTRecipeType recipeType) {
    // JsonObject json = new JsonObject();
    //
    // json.addProperty("id", recipe.getId().toString());
    // json.addProperty("type", recipeType.registryName.toString());
    // json.addProperty("duration", recipe.duration);
    //
    // long inputEUt = recipe.getInputEUt();
    // long outputEUt = recipe.getOutputEUt();
    // if (inputEUt > 0) {
    // json.addProperty("eu_per_tick", inputEUt);
    // json.addProperty("eu_type", "input");
    // } else if (outputEUt > 0) {
    // json.addProperty("eu_per_tick", outputEUt);
    // json.addProperty("eu_type", "output");
    // }
    // json.addProperty("total_eu", (inputEUt > 0 ? inputEUt : outputEUt) * recipe.duration);
    //
    // // Mana per tick (魔力/t)
    // long inputManat = recipe.getInputMANAt();
    // long outputManat = recipe.getOutputMANAt();
    // if (inputManat > 0) {
    // json.addProperty("mana_per_tick", inputManat);
    // json.addProperty("mana_type", "input");
    // json.addProperty("total_mana", inputManat * recipe.duration);
    // } else if (outputManat > 0) {
    // json.addProperty("mana_per_tick", outputManat);
    // json.addProperty("mana_type", "output");
    // json.addProperty("total_mana", outputManat * recipe.duration);
    // }
    //
    // // CWU per tick (算力/t — Computation Work Units)
    // long cwut = recipe.cwut;
    // if (cwut > 0) {
    // json.addProperty("cwu_per_tick", cwut);
    // boolean isTotalCwu = recipe.data.getBoolean("duration_is_total_cwu");
    // json.addProperty("duration_is_total_cwu", isTotalCwu);
    // if (isTotalCwu) {
    // // duration field represents total CWU, not ticks
    // json.addProperty("total_cwu", (long) recipe.duration);
    // } else {
    // json.addProperty("total_cwu", cwut * recipe.duration);
    // }
    // }
    //
    // // Input items — also detect programmed circuit
    // JsonArray inputItems = new JsonArray();
    // boolean hasCircuit = false;
    // int circuitConfig = -1;
    // for (Content content : recipe.inputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList())) {
    // JsonObject itemObj = serializeItemContent(content, true);
    // inputItems.add(itemObj);
    // // Check if this input is a programmed circuit
    // if (!hasCircuit && itemObj.has("is_circuit") && itemObj.get("is_circuit").getAsBoolean()) {
    // hasCircuit = true;
    // circuitConfig = itemObj.get("circuit_config").getAsInt();
    // }
    // }
    // json.add("input_items", inputItems);
    //
    // // Top-level circuit info for easy querying
    // if (hasCircuit) {
    // json.addProperty("requires_circuit", true);
    // json.addProperty("circuit_config", circuitConfig);
    // }
    //
    // // Output items
    // JsonArray outputItems = new JsonArray();
    // for (Content content : recipe.outputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList())) {
    // outputItems.add(serializeItemContent(content, false));
    // }
    // json.add("output_items", outputItems);
    //
    // // Input fluids
    // JsonArray inputFluids = new JsonArray();
    // for (Content content : recipe.inputs.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList())) {
    // inputFluids.add(serializeFluidContent(content));
    // }
    // json.add("input_fluids", inputFluids);
    //
    // // Output fluids
    // JsonArray outputFluids = new JsonArray();
    // for (Content content : recipe.outputs.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList())) {
    // outputFluids.add(serializeFluidContent(content));
    // }
    // json.add("output_fluids", outputFluids);
    //
    // // Tick inputs (per-tick resource consumption)
    // JsonObject tickInputs = new JsonObject();
    // for (var e : recipe.tickInputs.entrySet()) {
    // String capName = e.getKey().name;
    // JsonArray contents = new JsonArray();
    // for (Content c : e.getValue()) {
    // JsonObject cJson = new JsonObject();
    // cJson.addProperty("content", c.inner.toString());
    // cJson.addProperty("chance", c.chance);
    // contents.add(cJson);
    // }
    // tickInputs.add(capName, contents);
    // }
    // if (tickInputs.size() > 0) json.add("tick_inputs", tickInputs);
    //
    // // Tick outputs (per-tick resource production)
    // JsonObject tickOutputs = new JsonObject();
    // for (var e : recipe.tickOutputs.entrySet()) {
    // String capName = e.getKey().name;
    // JsonArray contents = new JsonArray();
    // for (Content c : e.getValue()) {
    // JsonObject cJson = new JsonObject();
    // cJson.addProperty("content", c.inner.toString());
    // cJson.addProperty("chance", c.chance);
    // contents.add(cJson);
    // }
    // tickOutputs.add(capName, contents);
    // }
    // if (tickOutputs.size() > 0) json.add("tick_outputs", tickOutputs);
    //
    // // Recipe data (temperature, heat, radiation, etc.)
    // if (!recipe.data.isEmpty()) {
    // JsonObject dataJson = new JsonObject();
    // for (String key : recipe.data.getAllKeys()) {
    // var tag = recipe.data.get(key);
    // if (tag != null) dataJson.addProperty(key, tag.getAsString());
    // }
    // json.add("data", dataJson);
    // }
    //
    // // Conditions (cleanroom, vacuum, gravity, etc.)
    // if (!recipe.conditions.isEmpty()) {
    // JsonArray conditions = new JsonArray();
    // for (var condition : recipe.conditions) {
    // JsonObject condJson = new JsonObject();
    // condJson.addProperty("class", condition.getClass().getSimpleName());
    // if (condition.getTooltips() != null) {
    // condJson.addProperty("tooltip", condition.getTooltips().getString());
    // }
    // condJson.addProperty("description", condition.toString());
    // conditions.add(condJson);
    // }
    // json.add("conditions", conditions);
    // }
    //
    // return json;
    // }
    //
    // private static JsonObject serializeItemContent(Content content, boolean isInput) {
    // JsonObject itemJson = new JsonObject();
    //
    // if (content.inner instanceof ItemIngredient ingredient) {
    // JsonArray itemsArray = new JsonArray();
    // try {
    // for (ItemStack stack : ingredient.inner.getItems()) {
    // if (!stack.isEmpty()) {
    // JsonObject stackJson = new JsonObject();
    // ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
    // stackJson.addProperty("item", itemId.toString());
    // stackJson.addProperty("count", stack.getCount());
    // if (stack.hasTag()) stackJson.addProperty("nbt", stack.getTag().toString());
    //
    // // Detect programmed circuit and extract configuration number
    // try {
    // if (stack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
    // int config = IntCircuitBehaviour.getCircuitConfiguration(stack);
    // stackJson.addProperty("is_circuit", true);
    // stackJson.addProperty("circuit_config", config);
    // // Also set on parent itemJson for easy access
    // itemJson.addProperty("is_circuit", true);
    // itemJson.addProperty("circuit_config", config);
    // }
    // } catch (Exception ignored) {}
    //
    // itemsArray.add(stackJson);
    // }
    // }
    // } catch (Exception e) {
    // JsonObject fallback = new JsonObject();
    // fallback.addProperty("item", ingredient.toString());
    // itemsArray.add(fallback);
    // }
    // itemJson.add("items", itemsArray);
    // itemJson.addProperty("amount", ingredient.amount);
    // } else {
    // itemJson.addProperty("content", content.inner.toString());
    // }
    //
    // itemJson.addProperty("chance", content.chance);
    // itemJson.addProperty("tier_chance_boost", content.tierChanceBoost);
    // // chance 10000 = 100%
    // itemJson.addProperty("chance_percent", content.chance / 100.0);
    //
    // return itemJson;
    // }
    //
    // private static JsonObject serializeFluidContent(Content content) {
    // JsonObject fluidJson = new JsonObject();
    //
    // if (content.inner instanceof FluidIngredient ingredient) {
    // var fluid = ingredient.getFluid();
    // if (fluid != null) {
    // fluidJson.addProperty("fluid", BuiltInRegistries.FLUID.getKey(fluid).toString());
    // } else {
    // fluidJson.addProperty("fluid", ingredient.toString());
    // }
    // fluidJson.addProperty("amount", ingredient.amount);
    // if (ingredient.nbt != null) fluidJson.addProperty("nbt", ingredient.nbt.toString());
    // } else {
    // fluidJson.addProperty("content", content.inner.toString());
    // }
    //
    // fluidJson.addProperty("chance", content.chance);
    // fluidJson.addProperty("tier_chance_boost", content.tierChanceBoost);
    // fluidJson.addProperty("chance_percent", content.chance / 100.0);
    //
    // return fluidJson;
    // }

    private static int exportVanillaRecipes(Minecraft minecraft, Path vanillaRecipeDir,
                                            Map<String, Integer> recipeTypeCount) {
        int totalCount = 0;

        if (minecraft.level == null) {
            GTOCore.LOGGER.warn("[Recipes] Cannot export vanilla recipes: world not loaded");
            return 0;
        }

        RecipeManager recipeManager = minecraft.level.getRecipeManager();
        Map<String, List<JsonObject>> recipesByType = new HashMap<>();

        for (Recipe<?> recipe : recipeManager.getRecipes()) {
            if (recipe instanceof GTRecipe) continue;

            try {
                String recipeType = recipe.getType().toString();
                String typeName = recipeType.replace(':', '_');

                JsonObject recipeJson = new JsonObject();
                recipeJson.addProperty("id", recipe.getId().toString());
                recipeJson.addProperty("type", recipeType);

                ItemStack result = recipe.getResultItem(minecraft.level.registryAccess());
                if (!result.isEmpty()) {
                    JsonObject resultJson = new JsonObject();
                    resultJson.addProperty("item", BuiltInRegistries.ITEM.getKey(result.getItem()).toString());
                    resultJson.addProperty("count", result.getCount());
                    if (result.hasTag()) resultJson.addProperty("nbt", result.getTag().toString());
                    recipeJson.add("result", resultJson);
                }

                JsonArray ingredients = new JsonArray();
                recipe.getIngredients().forEach(ingredient -> {
                    JsonObject ingJson = new JsonObject();
                    JsonArray items = new JsonArray();
                    for (ItemStack stack : ingredient.getItems()) {
                        JsonObject itemJson = new JsonObject();
                        itemJson.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
                        itemJson.addProperty("count", stack.getCount());
                        items.add(itemJson);
                    }
                    ingJson.add("items", items);
                    ingredients.add(ingJson);
                });
                recipeJson.add("ingredients", ingredients);

                recipesByType.computeIfAbsent(typeName, k -> new ArrayList<>()).add(recipeJson);
                totalCount++;

            } catch (Exception e) {
                GTOCore.LOGGER.debug("[Recipes] Cannot serialize recipe: {}", recipe.getId(), e);
            }
        }

        for (Map.Entry<String, List<JsonObject>> entry : recipesByType.entrySet()) {
            String typeName = entry.getKey();
            List<JsonObject> recipes = entry.getValue();

            try {
                JsonObject typeJson = new JsonObject();
                typeJson.addProperty("recipe_type", typeName.replace('_', ':'));
                typeJson.addProperty("count", recipes.size());

                JsonArray recipesArray = new JsonArray();
                recipes.forEach(recipesArray::add);
                typeJson.add("recipes", recipesArray);

                Files.writeString(vanillaRecipeDir.resolve(typeName + ".json"), GSON.toJson(typeJson));
                recipeTypeCount.put("vanilla_" + typeName, recipes.size());

            } catch (Exception e) {
                GTOCore.LOGGER.error("[Recipes] Error exporting recipe type: {}", typeName, e);
            }
        }

        return totalCount;
    }

    // ==================== Phase 4: Export metadata lists ====================

    private static void exportMetadataLists(Path listDir) throws IOException {
        // Run sequentially — Language.inject() for bilingual tooltips is NOT thread-safe
        exportItemMetadataList(listDir);
        exportFluidMetadataList(listDir);
        exportBlockMetadataList(listDir);
        GTOCore.LOGGER.info("[Metadata] All metadata lists exported.");
    }

    private static void exportItemMetadataList(Path listDir) throws IOException {
        JsonArray itemsArray = new JsonArray();

        for (Item item : BuiltInRegistries.ITEM) {
            try {
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
                if (id == null) continue;

                JsonObject itemJson = new JsonObject();
                itemJson.addProperty("id", id.toString());
                itemJson.addProperty("namespace", id.getNamespace());
                itemJson.addProperty("path", id.getPath());

                JsonArray tagsArray = new JsonArray();
                BuiltInRegistries.ITEM.getTags().forEach(entry -> {
                    TagKey<Item> tagKey = entry.getFirst();
                    entry.getSecond().forEach(holder -> {
                        if (holder.value() == item) tagsArray.add(tagKey.location().toString());
                    });
                });
                itemJson.add("tags", tagsArray);

                // Bilingual names
                String descKey = item.getDescriptionId();
                itemJson.addProperty("description_id", descKey);
                itemJson.addProperty("name_en", tr(langEN, descKey));
                itemJson.addProperty("name_zh", tr(langZH, descKey));

                try {
                    Language originalLang = Language.getInstance();
                    ItemStack stack = new ItemStack(item);
                    itemJson.addProperty("max_stack_size", item.getMaxStackSize());
                    itemJson.addProperty("max_damage", item.getMaxDamage());
                    itemJson.addProperty("is_fireproof", item.isFireResistant());
                    itemJson.addProperty("has_container", item.getCraftingRemainingItem() != null);

                    // Bilingual runtime display names (NBT-aware)
                    try {
                        Language.inject(langInstanceEN);
                        itemJson.addProperty("display_name_en", stack.getHoverName().getString());
                    } catch (Exception ignored) {} finally {
                        Language.inject(originalLang);
                    }
                    try {
                        Language.inject(langInstanceZH);
                        itemJson.addProperty("display_name_zh", stack.getHoverName().getString());
                    } catch (Exception ignored) {} finally {
                        Language.inject(originalLang);
                    }

                    // Bilingual tooltips (full tooltip lines: GT stats, enchantments, lore, etc.)
                    itemJson.add("tooltip_en", getItemTooltip(stack, langInstanceEN, originalLang));
                    itemJson.add("tooltip_zh", getItemTooltip(stack, langInstanceZH, originalLang));
                } catch (Exception ignored) {}

                itemsArray.add(itemJson);
            } catch (Exception e) {
                GTOCore.LOGGER.debug("[Metadata] Cannot export item metadata: {}", item, e);
            }
        }

        JsonObject root = new JsonObject();
        root.addProperty("type", "item");
        root.addProperty("count", itemsArray.size());
        root.add("items", itemsArray);
        Files.writeString(listDir.resolve("items.json"), GSON.toJson(root));
        GTOCore.LOGGER.info("[Metadata] Item metadata exported: {} items", itemsArray.size());
    }

    private static void exportFluidMetadataList(Path listDir) throws IOException {
        JsonArray fluidsArray = new JsonArray();

        for (Fluid fluid : BuiltInRegistries.FLUID) {
            try {
                ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);
                if (id == null) continue;

                JsonObject fluidJson = new JsonObject();
                fluidJson.addProperty("id", id.toString());
                fluidJson.addProperty("namespace", id.getNamespace());
                fluidJson.addProperty("path", id.getPath());

                JsonArray tagsArray = new JsonArray();
                BuiltInRegistries.FLUID.getTags().forEach(entry -> {
                    TagKey<Fluid> tagKey = entry.getFirst();
                    entry.getSecond().forEach(holder -> {
                        if (holder.value() == fluid) tagsArray.add(tagKey.location().toString());
                    });
                });
                fluidJson.add("tags", tagsArray);

                // Bilingual names
                String descKey = fluid.getFluidType().getDescriptionId();
                fluidJson.addProperty("description_id", descKey);
                fluidJson.addProperty("name_en", tr(langEN, descKey));
                fluidJson.addProperty("name_zh", tr(langZH, descKey));

                try {
                    Language originalLang = Language.getInstance();
                    // Runtime display names via FluidStack (language-aware, handles GT fluid names)
                    fluidJson.addProperty("display_name_en",
                            getFluidDisplayName(fluid, langInstanceEN, originalLang));
                    fluidJson.addProperty("display_name_zh",
                            getFluidDisplayName(fluid, langInstanceZH, originalLang));
                    fluidJson.addProperty("is_source", fluid.isSource(fluid.defaultFluidState()));
                    fluidJson.addProperty("bucket_volume", 1000);
                } catch (Exception ignored) {}

                fluidsArray.add(fluidJson);
            } catch (Exception e) {
                GTOCore.LOGGER.debug("[Metadata] Cannot export fluid metadata: {}", fluid, e);
            }
        }

        JsonObject root = new JsonObject();
        root.addProperty("type", "fluid");
        root.addProperty("count", fluidsArray.size());
        root.add("fluids", fluidsArray);
        Files.writeString(listDir.resolve("fluids.json"), GSON.toJson(root));
        GTOCore.LOGGER.info("[Metadata] Fluid metadata exported: {} fluids", fluidsArray.size());
    }

    private static void exportBlockMetadataList(Path listDir) throws IOException {
        JsonArray blocksArray = new JsonArray();

        for (net.minecraft.world.level.block.Block block : BuiltInRegistries.BLOCK) {
            try {
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                if (id == null) continue;

                JsonObject blockJson = new JsonObject();
                blockJson.addProperty("id", id.toString());
                blockJson.addProperty("namespace", id.getNamespace());
                blockJson.addProperty("path", id.getPath());

                JsonArray tagsArray = new JsonArray();
                BuiltInRegistries.BLOCK.getTags().forEach(entry -> {
                    var tagKey = entry.getFirst();
                    entry.getSecond().forEach(holder -> {
                        if (holder.value() == block) tagsArray.add(tagKey.location().toString());
                    });
                });
                blockJson.add("tags", tagsArray);

                // Bilingual names
                String descKey = block.getDescriptionId();
                blockJson.addProperty("description_id", descKey);
                blockJson.addProperty("name_en", tr(langEN, descKey));
                blockJson.addProperty("name_zh", tr(langZH, descKey));

                try {
                    var defaultState = block.defaultBlockState();
                    blockJson.addProperty("hardness", block.defaultDestroyTime());
                    blockJson.addProperty("explosion_resistance", block.getExplosionResistance());
                    blockJson.addProperty("light_emission", defaultState.getLightEmission());
                } catch (Exception ignored) {}

                blocksArray.add(blockJson);
            } catch (Exception e) {
                GTOCore.LOGGER.debug("[Metadata] Cannot export block metadata: {}", block, e);
            }
        }

        JsonObject root = new JsonObject();
        root.addProperty("type", "block");
        root.addProperty("count", blocksArray.size());
        root.add("blocks", blocksArray);
        Files.writeString(listDir.resolve("blocks.json"), GSON.toJson(root));
        GTOCore.LOGGER.info("[Metadata] Block metadata exported: {} blocks", blocksArray.size());
    }

    // ==================== Phase 5: Export GT environment info ====================

    private static void exportGTEnvironments(Path miscDir) throws IOException {
        JsonObject envJson = new JsonObject();

        envJson.addProperty("modpack", "GTOCore");
        envJson.addProperty("version", MOD_VERSION);
        envJson.addProperty("minecraft_version", "1.20.1");
        envJson.addProperty("generated_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // Cleanroom types
        JsonArray cleanroomTypes = new JsonArray();

        JsonObject cleanroom = new JsonObject();
        cleanroom.addProperty("id", "cleanroom");
        cleanroom.addProperty("name", "Cleanroom");
        cleanroom.addProperty("tier", 1);
        cleanroom.addProperty("description", "Basic clean environment for chip manufacturing and precision processing");
        cleanroomTypes.add(cleanroom);

        JsonObject sterileCleanroom = new JsonObject();
        sterileCleanroom.addProperty("id", "sterile_cleanroom");
        sterileCleanroom.addProperty("name", "Sterile Cleanroom");
        sterileCleanroom.addProperty("tier", 2);
        sterileCleanroom.addProperty("description", "Advanced clean environment for bioengineering and advanced circuit manufacturing");
        sterileCleanroom.addProperty("includes_cleanroom", true);
        cleanroomTypes.add(sterileCleanroom);

        JsonObject lawCleanroom = new JsonObject();
        lawCleanroom.addProperty("id", "law_cleanroom");
        lawCleanroom.addProperty("name", "LAW Cleanroom");
        lawCleanroom.addProperty("tier", 3);
        lawCleanroom.addProperty("description", "Highest-tier clean environment for the most precise manufacturing");
        lawCleanroom.addProperty("includes_sterile_cleanroom", true);
        lawCleanroom.addProperty("includes_cleanroom", true);
        cleanroomTypes.add(lawCleanroom);

        envJson.add("cleanroom_types", cleanroomTypes);

        // Vacuum conditions
        JsonArray vacuumConditions = new JsonArray();
        for (int tier = 1; tier <= 10; tier++) {
            JsonObject vacuum = new JsonObject();
            vacuum.addProperty("tier", tier);
            vacuum.addProperty("name", "Vacuum Tier " + tier);
            vacuum.addProperty("description", "Vacuum environment tier " + tier + ", obtainable via vacuum hatch or space environment");
            vacuumConditions.add(vacuum);
        }
        envJson.add("vacuum_conditions", vacuumConditions);

        // Gravity conditions
        JsonArray gravityConditions = new JsonArray();

        JsonObject normalGravity = new JsonObject();
        normalGravity.addProperty("id", "normal_gravity");
        normalGravity.addProperty("name", "Normal Gravity");
        normalGravity.addProperty("gravity_level", 100);
        normalGravity.addProperty("description", "Standard Earth gravity environment");
        gravityConditions.add(normalGravity);

        JsonObject zeroGravity = new JsonObject();
        zeroGravity.addProperty("id", "zero_gravity");
        zeroGravity.addProperty("name", "Zero Gravity");
        zeroGravity.addProperty("gravity_level", 0);
        zeroGravity.addProperty("description", "Zero gravity environment, obtainable via space or gravity controller");
        gravityConditions.add(zeroGravity);

        envJson.add("gravity_conditions", gravityConditions);

        // Dimension conditions
        JsonArray dimensionConditions = new JsonArray();
        addDimension(dimensionConditions, "minecraft:overworld", "Overworld", false, true);
        addDimension(dimensionConditions, "minecraft:the_nether", "The Nether", false, true);
        addDimension(dimensionConditions, "minecraft:the_end", "The End", false, true);
        addDimension(dimensionConditions, "ad_astra:moon", "Moon", true, false);
        addDimension(dimensionConditions, "ad_astra:mars", "Mars", true, false);
        addDimension(dimensionConditions, "ad_astra:venus", "Venus", true, false);
        addDimension(dimensionConditions, "ad_astra:mercury", "Mercury", true, false);
        addDimension(dimensionConditions, "ad_astra:glacio", "Glacio", true, false);
        envJson.add("dimension_conditions", dimensionConditions);

        // Voltage tiers
        JsonArray voltageTiers = new JsonArray();
        String[] tierNames = { "ULV", "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV", "UEV", "UIV", "UXV", "OpV", "MAX" };
        long[] tierVoltages = { 8, 32, 128, 512, 2048, 8192, 32768, 131072, 524288, 2097152, 8388608, 33554432, 134217728, 536870912, 2147483647L };

        for (int i = 0; i < tierNames.length; i++) {
            JsonObject tier = new JsonObject();
            tier.addProperty("tier", i);
            tier.addProperty("name", tierNames[i]);
            tier.addProperty("voltage", tierVoltages[i]);
            tier.addProperty("amperage_1a", tierVoltages[i]);
            tier.addProperty("amperage_4a", tierVoltages[i] * 4);
            tier.addProperty("amperage_16a", tierVoltages[i] * 16);
            voltageTiers.add(tier);
        }
        envJson.add("voltage_tiers", voltageTiers);

        // Recipe modifiers
        JsonArray recipeModifiers = new JsonArray();

        JsonObject overclock = new JsonObject();
        overclock.addProperty("id", "overclock");
        overclock.addProperty("name", "Overclock");
        overclock.addProperty("description", "Each voltage tier up: duration/4, EU cost*4");
        overclock.addProperty("duration_multiplier", 0.25);
        overclock.addProperty("eu_multiplier", 4.0);
        recipeModifiers.add(overclock);

        JsonObject perfectOverclock = new JsonObject();
        perfectOverclock.addProperty("id", "perfect_overclock");
        perfectOverclock.addProperty("name", "Perfect Overclock");
        perfectOverclock.addProperty("description", "Each voltage tier up: duration/4, EU cost*4, no efficiency loss");
        perfectOverclock.addProperty("duration_multiplier", 0.25);
        perfectOverclock.addProperty("eu_multiplier", 4.0);
        perfectOverclock.addProperty("is_perfect", true);
        recipeModifiers.add(perfectOverclock);

        JsonObject parallel = new JsonObject();
        parallel.addProperty("id", "parallel");
        parallel.addProperty("name", "Parallel");
        parallel.addProperty("description", "Execute multiple recipes simultaneously with linear EU and output scaling");
        recipeModifiers.add(parallel);

        envJson.add("recipe_modifiers", recipeModifiers);

        // Special conditions
        JsonArray specialConditions = new JsonArray();
        addCondition(specialConditions, "rock_breaker", "Rock Breaker", "Requires adjacent lava and water");
        addCondition(specialConditions, "radioactivity", "Radioactivity", "Requires radioactive material or radiation hatch");
        addCondition(specialConditions, "biome_temperature", "Biome Temperature", "Requires a specific biome temperature");
        addCondition(specialConditions, "research", "Research Data", "Requires completing specific research to unlock recipe");
        envJson.add("special_conditions", specialConditions);

        Files.writeString(miscDir.resolve("gt_environments.json"), GSON.toJson(envJson));
        GTOCore.LOGGER.info("[GTEnv] GT environment info exported");

        // Export GT machines & multiblocks metadata
        exportGTMachines(miscDir);

        // Export recipe type → machine mapping
        exportRecipeTypeMachines(miscDir);
    }

    private static void addDimension(JsonArray arr, String id, String name, boolean isSpace, boolean hasOxygen) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("name", name);
        if (isSpace) obj.addProperty("is_space", true);
        obj.addProperty("has_oxygen", hasOxygen);
        arr.add(obj);
    }

    private static void addCondition(JsonArray arr, String id, String name, String desc) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("name", name);
        obj.addProperty("description", desc);
        arr.add(obj);
    }

    // ==================== GT Machine & Recipe Type Metadata ====================

    /**
     * Export all GT machines (single-block and multiblock) with bilingual names,
     * tier info, and associated recipe types.
     */
    private static void exportGTMachines(Path miscDir) throws IOException {
        JsonArray machinesArray = new JsonArray();

        for (MachineDefinition definition : GTRegistries.MACHINES.values()) {
            try {
                JsonObject machineJson = new JsonObject();
                ResourceLocation machineId = definition.getId();
                machineJson.addProperty("id", machineId.toString());
                machineJson.addProperty("namespace", machineId.getNamespace());
                machineJson.addProperty("path", machineId.getPath());

                // Bilingual names
                String descKey = definition.getDescriptionId();
                machineJson.addProperty("description_id", descKey);
                machineJson.addProperty("name_en", tr(langEN, descKey));
                machineJson.addProperty("name_zh", tr(langZH, descKey));

                // Tier info
                int tier = definition.getTier();
                machineJson.addProperty("tier", tier);
                if (tier >= 0 && tier < GTValues.VN.length) {
                    machineJson.addProperty("tier_name", GTValues.VN[tier]);
                    machineJson.addProperty("voltage", GTValues.V[tier]);
                }

                // Recipe types
                GTRecipeType[] recipeTypes = definition.getRecipeTypes();
                if (recipeTypes != null && recipeTypes.length > 0) {
                    JsonArray rtArray = new JsonArray();
                    for (GTRecipeType rt : recipeTypes) {
                        if (rt != null) {
                            rtArray.add(rt.registryName.toString());
                        }
                    }
                    machineJson.add("recipe_types", rtArray);
                }

                // Multiblock info
                boolean isMultiblock = definition instanceof MultiblockMachineDefinition;
                machineJson.addProperty("is_multiblock", isMultiblock);
                if (isMultiblock) {
                    MultiblockMachineDefinition multiDef = (MultiblockMachineDefinition) definition;
                    machineJson.addProperty("is_generator", multiDef.isGenerator());
                }

                machinesArray.add(machineJson);
            } catch (Exception e) {
                GTOCore.LOGGER.debug("[GTMachines] Cannot export machine: {}", definition.getId(), e);
            }
        }

        JsonObject root = new JsonObject();
        root.addProperty("type", "gt_machines");
        root.addProperty("count", machinesArray.size());
        root.add("machines", machinesArray);
        Files.writeString(miscDir.resolve("gt_machines.json"), GSON.toJson(root));
        GTOCore.LOGGER.info("[GTMachines] GT machine metadata exported: {} machines", machinesArray.size());
    }

    /**
     * Export recipe type → machine mapping.
     * For each GT recipe type, lists which machines can process it,
     * along with recipe type metadata (group, IO sizes, bilingual names).
     */
    private static void exportRecipeTypeMachines(Path miscDir) throws IOException {
        // Build reverse map: recipe type → list of machine IDs
        Map<ResourceLocation, List<ResourceLocation>> recipeTypeToMachines = new LinkedHashMap<>();

        for (MachineDefinition definition : GTRegistries.MACHINES.values()) {
            GTRecipeType[] recipeTypes = definition.getRecipeTypes();
            if (recipeTypes == null) continue;
            for (GTRecipeType rt : recipeTypes) {
                if (rt != null) {
                    recipeTypeToMachines
                            .computeIfAbsent(rt.registryName, k -> new ArrayList<>())
                            .add(definition.getId());
                }
            }
        }

        JsonArray recipeTypesArray = new JsonArray();
        for (GTRecipeType recipeType : GTRegistries.RECIPE_TYPES) {
            try {
                JsonObject rtJson = new JsonObject();
                ResourceLocation rtId = recipeType.registryName;
                rtJson.addProperty("id", rtId.toString());
                rtJson.addProperty("namespace", rtId.getNamespace());
                rtJson.addProperty("path", rtId.getPath());

                // Bilingual names — GT recipe types use "gtceu.<path>" as translation key
                String rtKey = "gtceu." + rtId.getPath();
                rtJson.addProperty("translation_key", rtKey);
                rtJson.addProperty("name_en", tr(langEN, rtKey));
                rtJson.addProperty("name_zh", tr(langZH, rtKey));

                // Group (electric, multiblock, generator, steam, dummy)
                if (recipeType.group != null) {
                    rtJson.addProperty("group", recipeType.group);
                }

                // IO sizes
                try {
                    rtJson.addProperty("max_item_inputs", recipeType.getMaxInputs(ItemRecipeCapability.CAP));
                    rtJson.addProperty("max_item_outputs", recipeType.getMaxOutputs(ItemRecipeCapability.CAP));
                    rtJson.addProperty("max_fluid_inputs", recipeType.getMaxInputs(FluidRecipeCapability.CAP));
                    rtJson.addProperty("max_fluid_outputs", recipeType.getMaxOutputs(FluidRecipeCapability.CAP));
                } catch (Exception ignored) {}

                // Machines that process this recipe type
                List<ResourceLocation> machines = recipeTypeToMachines.getOrDefault(rtId, Collections.emptyList());
                JsonArray machinesArr = new JsonArray();
                for (ResourceLocation mId : machines) {
                    machinesArr.add(mId.toString());
                }
                rtJson.addProperty("machine_count", machines.size());
                rtJson.add("machines", machinesArr);

                recipeTypesArray.add(rtJson);
            } catch (Exception e) {
                GTOCore.LOGGER.debug("[RecipeTypes] Cannot export recipe type: {}", recipeType.registryName, e);
            }
        }

        JsonObject root = new JsonObject();
        root.addProperty("type", "recipe_type_machines");
        root.addProperty("count", recipeTypesArray.size());
        root.add("recipe_types", recipeTypesArray);
        Files.writeString(miscDir.resolve("recipe_type_machines.json"), GSON.toJson(root));
        GTOCore.LOGGER.info("[RecipeTypes] Recipe type → machine mapping exported: {} types", recipeTypesArray.size());
    }

    // ==================== Master report ====================

    private static void generateMasterReport(Path baseDir) throws IOException {
        JsonObject report = new JsonObject();

        report.addProperty("generated_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        report.addProperty("gtocore_version", MOD_VERSION);
        report.addProperty("minecraft_version", "1.20.1");

        try {
            report.add("item_stats", JsonParser.parseString(Files.readString(baseDir.resolve("item/_stats.json"))));
        } catch (Exception ignored) {}
        try {
            report.add("fluid_stats", JsonParser.parseString(Files.readString(baseDir.resolve("fluid/_stats.json"))));
        } catch (Exception ignored) {}
        try {
            report.add("block_stats", JsonParser.parseString(Files.readString(baseDir.resolve("block/_stats.json"))));
        } catch (Exception ignored) {}
        try {
            report.add("tag_stats", JsonParser.parseString(Files.readString(baseDir.resolve("tag/_stats.json"))));
        } catch (Exception ignored) {}
        try {
            report.add("recipe_stats", JsonParser.parseString(Files.readString(baseDir.resolve("recipe/_stats.json"))));
        } catch (Exception ignored) {}

        Files.writeString(baseDir.resolve("_master_report.json"), GSON.toJson(report));

        GTOCore.LOGGER.info("=".repeat(50));
        GTOCore.LOGGER.info("Master report generated: {}", baseDir.resolve("_master_report.json"));
        GTOCore.LOGGER.info("=".repeat(50));
    }
}
