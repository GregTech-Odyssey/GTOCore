package com.gtocore.integration.emi.primordial_reconstructor;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.machines.ManaMultiBlock;
import com.gtocore.common.item.AffixCanvas;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.stack.TagEmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class PrimordialReconstructorDisassemblyEmiCategory extends EmiRecipeCategory {

    public static final PrimordialReconstructorDisassemblyEmiCategory CATEGORY = new PrimordialReconstructorDisassemblyEmiCategory();

    private PrimordialReconstructorDisassemblyEmiCategory() {
        super(GTOCore.id("primordial_reconstructor/disassembly"), EmiStack.of(ManaMultiBlock.THE_PRIMORDIAL_RECONSTRUCTOR.asStack()));
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ManaMultiBlock.THE_PRIMORDIAL_RECONSTRUCTOR.asStack()));
        registry.addRecipe(new DisassemblyTableRecipe());
    }

    @Override
    public Component getName() {
        return Component.translatable("gtocore.emi.primordial_reconstructor.disassembly.title");
    }

    private static final class DisassemblyTableRecipe implements EmiRecipe {

        private static final Minecraft CLIENT = Minecraft.getInstance();
        private static final int ROW_HEIGHT = 27;
        private static final int SLOT_SIZE = 18;
        private static final int NOTE_X = 6;
        private static final int NOTE_LINE_HEIGHT = 11;
        private static final int TABLE_MARGIN = 4;
        private static final int TABLE_Y = 5;

        private final EmiIngredient enchantmentEssence = new TagEmiIngredient(Tags.ENCHANTMENT_ESSENCE, 1);
        private final EmiIngredient affixEssence = new TagEmiIngredient(Tags.AFFIX_ESSENCE, 1);
        private final Layout layout = Layout.create(CLIENT.font);
        private final List<EmiStack> searchableGems = createSearchableGems();
        private final List<EmiStack> essenceOutputs = Stream.concat(
                GTOItems.ENCHANTMENT_ESSENCE.values().stream(),
                GTOItems.AFFIX_ESSENCE.values().stream())
                .map(entry -> EmiStack.of(entry.asItem()))
                .toList();
        private final List<EmiStack> searchableEnchantedBooks = createSearchableEnchantedBooks();
        private final EmiIngredient enchantedBooks = new ListEmiIngredient(searchableEnchantedBooks, 1);
        private final EmiStack enchantedBook = EmiStack.of(enchantedBook(Enchantments.UNBREAKING, 1));
        private final EmiIngredient equipmentExamples = createEquipmentExamples();
        private final EmiStack book = EmiStack.of(Items.BOOK);
        private final EmiStack emptyAffixCanvas = EmiStack.of(GTOItems.AFFIX_CANVAS.asItem());
        private final EmiStack filledAffixCanvas = EmiStack.of(filledAffixCanvas());
        private final EmiStack gem = searchableGems.isEmpty() ? EmiStack.of(Adventure.Items.GEM.get()) : searchableGems.get(0);
        private final EmiStack withdrawalSigil = EmiStack.of(Adventure.Items.SIGIL_OF_WITHDRAWAL.get());

        @Override
        public EmiRecipeCategory getCategory() {
            return CATEGORY;
        }

        @Override
        public @Nullable ResourceLocation getId() {
            return GTOCore.id("primordial_reconstructor/disassembly_modes");
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return List.of(
                    equipmentExamples,
                    enchantedBooks,
                    filledAffixCanvas,
                    emptyAffixCanvas,
                    withdrawalSigil,
                    EmiStack.of(IntCircuitBehaviour.stack(1)),
                    EmiStack.of(IntCircuitBehaviour.stack(2)),
                    EmiStack.of(IntCircuitBehaviour.stack(3)),
                    EmiStack.of(IntCircuitBehaviour.stack(4)));
        }

        @Override
        public List<EmiIngredient> getCatalysts() {
            return List.of();
        }

        @Override
        public boolean supportsRecipeTree() {
            return false;
        }

        @Override
        public boolean hideCraftable() {
            return true;
        }

        @Override
        public List<EmiStack> getOutputs() {
            Stream<EmiStack> gemOutputs = searchableGems.isEmpty() ? Stream.of(gem) : searchableGems.stream();
            return Stream.concat(
                    Stream.concat(essenceOutputs.stream(), Stream.concat(searchableEnchantedBooks.stream(), Stream.of(emptyAffixCanvas))),
                    gemOutputs).toList();
        }

        @Override
        public int getDisplayWidth() {
            return layout.width();
        }

        @Override
        public int getDisplayHeight() {
            return layout.height();
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            addBackground(widgets, layout);
            addWrappedNotes(widgets, layout);
            addHeaders(widgets, layout);

            addMode1(widgets, layout, layout.rowStartY());
            addMode2(widgets, layout, layout.rowStartY() + ROW_HEIGHT);
            addMode3(widgets, layout, layout.rowStartY() + ROW_HEIGHT * 2);
            addMode4(widgets, layout, layout.rowStartY() + ROW_HEIGHT * 3);
        }

        private void addBackground(WidgetHolder widgets, Layout layout) {
            widgets.addDrawable(0, 0, layout.width(), layout.height(), (raw, mouseX, mouseY, delta) -> {
                GuiGraphics graphics = raw;
                graphics.fill(0, 0, layout.width(), layout.height(), 0xFFBDBDBD);

                graphics.fill(layout.tableLeft(), layout.tableY() - 2, layout.tableRight(), layout.tableY() - 1, 0xFF777777);
                graphics.fill(layout.tableLeft(), layout.tableY() + 13, layout.tableRight(), layout.tableY() + 14, 0xFF777777);
                graphics.fill(layout.tableLeft(), layout.tableY() + 27, layout.tableRight(), layout.tableY() + 28, 0xFF777777);
                for (int i = 1; i <= 4; i++) {
                    int y = layout.tableY() + 28 + ROW_HEIGHT * i;
                    graphics.fill(layout.tableLeft(), y, layout.tableRight(), y + 1, 0xFF8C8C8C);
                }
                for (int x : layout.separators()) {
                    int top = x == layout.enchantmentStart() ? layout.tableY() - 2 : layout.headerY() - 2;
                    graphics.fill(x, top, x + 1, layout.tableBottom(), 0xFF8C8C8C);
                }
            });
        }

        private void addHeaders(WidgetHolder widgets, Layout layout) {
            addCenteredText(widgets, "gtocore.emi.primordial_reconstructor.disassembly.input_group", layout.tableLeft(), layout.enchantmentStart() - layout.tableLeft(), layout.tableY() + 2, 0x303030);
            addCenteredText(widgets, "gtocore.emi.primordial_reconstructor.disassembly.output_group", layout.enchantmentStart(), layout.tableRight() - layout.enchantmentStart(), layout.tableY() + 2, 0x303030);
            addCenteredText(widgets, "gtocore.emi.primordial_reconstructor.disassembly.circuit", layout.circuitStart(), layout.circuitWidth(), layout.headerY() + 2, 0x303030);
            addCenteredText(widgets, "gtocore.emi.primordial_reconstructor.disassembly.input", layout.inputStart(), layout.inputWidth(), layout.headerY() + 2, 0x303030);
            addCenteredText(widgets, "gtocore.emi.primordial_reconstructor.disassembly.extra", layout.extraStart(), layout.extraWidth(), layout.headerY() + 2, 0x303030);
            addCenteredText(widgets, "gtocore.emi.primordial_reconstructor.disassembly.enchantment", layout.enchantmentStart(), layout.enchantmentWidth(), layout.headerY() + 2, 0x303030);
            addCenteredText(widgets, "gtocore.emi.primordial_reconstructor.disassembly.affix", layout.affixStart(), layout.affixWidth(), layout.headerY() + 2, 0x303030);
            addCenteredText(widgets, "gtocore.emi.primordial_reconstructor.disassembly.gem", layout.gemStart(), layout.gemWidth(), layout.headerY() + 2, 0x303030);
        }

        private void addMode1(WidgetHolder widgets, Layout layout, int y) {
            addCircuit(widgets, layout, 1, y);
            addSlot(widgets, equipmentExamples, layout.inputSlotX(1), y);
            addSlot(widgets, book, layout.extraSlotX(3), y);
            addSlot(widgets, emptyAffixCanvas, layout.extraSlotX(3) + SLOT_SIZE, y);
            addSlot(widgets, withdrawalSigil, layout.extraSlotX(3) + SLOT_SIZE * 2, y);
            addSlot(widgets, enchantedBook, layout.enchantmentSlotX(), y);
            addSlot(widgets, filledAffixCanvas, layout.affixSlotX(), y);
            addSlot(widgets, gem, layout.gemSlotX(), y);
        }

        private void addMode2(WidgetHolder widgets, Layout layout, int y) {
            addCircuit(widgets, layout, 2, y);
            addSlot(widgets, equipmentExamples, layout.inputSlotX(2), y);
            addSlot(widgets, enchantedBook, layout.inputSlotX(2) + SLOT_SIZE, y);
            addSlot(widgets, emptyAffixCanvas, layout.extraSlotX(2), y);
            addSlot(widgets, withdrawalSigil, layout.extraSlotX(2) + SLOT_SIZE, y);
            addSlot(widgets, enchantmentEssence, layout.enchantmentSlotX(), y);
            addSlot(widgets, filledAffixCanvas, layout.affixSlotX(), y);
            addSlot(widgets, gem, layout.gemSlotX(), y);
        }

        private void addMode3(WidgetHolder widgets, Layout layout, int y) {
            addCircuit(widgets, layout, 3, y);
            addSlot(widgets, equipmentExamples, layout.inputSlotX(2), y);
            addSlot(widgets, filledAffixCanvas, layout.inputSlotX(2) + SLOT_SIZE, y);
            addSlot(widgets, book, layout.extraSlotX(2), y);
            addSlot(widgets, withdrawalSigil, layout.extraSlotX(2) + SLOT_SIZE, y);
            addSlot(widgets, enchantedBook, layout.enchantmentSlotX(), y);
            addSlot(widgets, affixEssence, layout.affixSlotX(), y);
            addSlot(widgets, gem, layout.gemSlotX(), y);
        }

        private void addMode4(WidgetHolder widgets, Layout layout, int y) {
            addCircuit(widgets, layout, 4, y);
            addSlot(widgets, equipmentExamples, layout.inputSlotX(3), y);
            addSlot(widgets, enchantedBook, layout.inputSlotX(3) + SLOT_SIZE, y);
            addSlot(widgets, filledAffixCanvas, layout.inputSlotX(3) + SLOT_SIZE * 2, y);
            addSlot(widgets, withdrawalSigil, layout.extraSlotX(1), y);
            addSlot(widgets, enchantmentEssence, layout.enchantmentSlotX(), y);
            addSlot(widgets, affixEssence, layout.affixSlotX(), y);
            addSlot(widgets, gem, layout.gemSlotX(), y);
        }

        private void addCircuit(WidgetHolder widgets, Layout layout, int circuit, int y) {
            addSlot(widgets, EmiStack.of(IntCircuitBehaviour.stack(circuit)), layout.circuitSlotX(), y);
        }

        private void addSlot(WidgetHolder widgets, EmiIngredient stack, int x, int y) {
            widgets.addSlot(stack, x, y).recipeContext(this);
        }

        private void addWrappedNotes(WidgetHolder widgets, Layout layout) {
            widgets.addDrawable(NOTE_X, layout.noteY(), layout.noteWidth(), layout.noteHeight(), (raw, mouseX, mouseY, delta) -> {
                Font font = CLIENT.font;
                int y = 0;
                for (FormattedCharSequence line : layout.noteLines()) {
                    raw.drawString(font, line, 0, y, 0x404040, false);
                    y += NOTE_LINE_HEIGHT;
                }
            });
        }

        private void addCenteredText(WidgetHolder widgets, String key, int x, int width, int y, int color) {
            widgets.addDrawable(x, y, 0, 0, (raw, mouseX, mouseY, delta) -> {
                Font font = CLIENT.font;
                Component text = Component.translatable(key);
                raw.drawString(font, text, Math.max(0, (width - font.width(text)) / 2), 0, color, false);
            });
        }

        private record Layout(
                              int width,
                              int height,
                              int tableY,
                              int headerY,
                              int rowStartY,
                              int tableLeft,
                              int tableRight,
                              int circuitStart,
                              int circuitWidth,
                              int inputStart,
                              int inputWidth,
                              int extraStart,
                              int extraWidth,
                              int enchantmentStart,
                              int enchantmentWidth,
                              int affixStart,
                              int affixWidth,
                              int gemStart,
                              int gemWidth,
                              int noteY,
                              int noteWidth,
                              List<FormattedCharSequence> noteLines) {

            private static final int COLUMN_PADDING = 8;

            static Layout create(Font font) {
                int circuitWidth = columnWidth(font, "gtocore.emi.primordial_reconstructor.disassembly.circuit", 1);
                int inputWidth = columnWidth(font, "gtocore.emi.primordial_reconstructor.disassembly.input", 3);
                int extraWidth = columnWidth(font, "gtocore.emi.primordial_reconstructor.disassembly.extra", 3);
                int enchantmentWidth = columnWidth(font, "gtocore.emi.primordial_reconstructor.disassembly.enchantment", 1);
                int affixWidth = columnWidth(font, "gtocore.emi.primordial_reconstructor.disassembly.affix", 1);
                int gemWidth = columnWidth(font, "gtocore.emi.primordial_reconstructor.disassembly.gem", 1);

                int tableLeft = TABLE_MARGIN;
                int circuitStart = tableLeft;
                int inputStart = circuitStart + circuitWidth;
                int extraStart = inputStart + inputWidth;
                int enchantmentStart = extraStart + extraWidth;
                int affixStart = enchantmentStart + enchantmentWidth;
                int gemStart = affixStart + affixWidth;
                int tableRight = gemStart + gemWidth;
                int width = tableRight + TABLE_MARGIN;
                int noteWidth = width - NOTE_X * 2;

                List<FormattedCharSequence> noteLines = Stream.of(
                        "gtocore.emi.primordial_reconstructor.disassembly.note_1",
                        "gtocore.emi.primordial_reconstructor.disassembly.note_2",
                        "gtocore.emi.primordial_reconstructor.disassembly.note_3")
                        .flatMap(key -> font.split(Component.translatable(key), noteWidth).stream())
                        .toList();
                int noteHeight = noteLines.size() * NOTE_LINE_HEIGHT;
                int tableY = TABLE_Y;
                int headerY = tableY + 16;
                int rowStartY = tableY + 30;
                int noteY = tableY + 142;
                int height = noteY + noteHeight + 6;

                return new Layout(
                        width,
                        height,
                        tableY,
                        headerY,
                        rowStartY,
                        tableLeft,
                        tableRight,
                        circuitStart,
                        circuitWidth,
                        inputStart,
                        inputWidth,
                        extraStart,
                        extraWidth,
                        enchantmentStart,
                        enchantmentWidth,
                        affixStart,
                        affixWidth,
                        gemStart,
                        gemWidth,
                        noteY,
                        noteWidth,
                        noteLines);
            }

            int tableBottom() {
                return tableY + 136;
            }

            int noteHeight() {
                return noteLines.size() * NOTE_LINE_HEIGHT;
            }

            int circuitSlotX() {
                return slotX(circuitStart, circuitWidth, 1);
            }

            int inputSlotX(int slots) {
                return slotX(inputStart, inputWidth, slots);
            }

            int extraSlotX(int slots) {
                return slotX(extraStart, extraWidth, slots);
            }

            int enchantmentSlotX() {
                return slotX(enchantmentStart, enchantmentWidth, 1);
            }

            int affixSlotX() {
                return slotX(affixStart, affixWidth, 1);
            }

            int gemSlotX() {
                return slotX(gemStart, gemWidth, 1);
            }

            int[] separators() {
                return new int[] { inputStart, extraStart, enchantmentStart, affixStart, gemStart };
            }

            private static int columnWidth(Font font, String headerKey, int slots) {
                int headerWidth = font.width(Component.translatable(headerKey));
                int contentWidth = slots * SLOT_SIZE;
                return Math.max(headerWidth, contentWidth) + COLUMN_PADDING;
            }

            private static int slotX(int start, int width, int slots) {
                return start + Math.max(0, (width - slots * SLOT_SIZE) / 2);
            }
        }

        private static ItemStack enchantedBook(Enchantment enchantment, int level) {
            return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
        }

        private static List<EmiStack> createSearchableEnchantedBooks() {
            return BuiltInRegistries.ENCHANTMENT.stream()
                    .flatMap(enchantment -> IntStream.rangeClosed(enchantment.getMinLevel(), enchantment.getMaxLevel())
                            .mapToObj(level -> EmiStack.of(enchantedBook(enchantment, level))))
                    .toList();
        }

        private static List<EmiStack> createSearchableGems() {
            List<EmiStack> gems = new ArrayList<>();
            GemRegistry.INSTANCE.getValues().forEach(gem -> {
                RarityRegistry.INSTANCE.getOrderedRarities().forEach(rarityHolder -> {
                    if (!rarityHolder.isBound()) return;
                    var rarity = rarityHolder.get();
                    if (gem.clamp(rarity) == rarity) {
                        gems.add(EmiStack.of(GemRegistry.createGemStack(gem, rarity)));
                    }
                });
            });
            return List.copyOf(gems);
        }

        private static EmiIngredient createEquipmentExamples() {
            return new ListEmiIngredient(List.of(
                    Items.DIAMOND_SWORD,
                    Items.DIAMOND_PICKAXE,
                    Items.DIAMOND_HELMET,
                    Items.DIAMOND_CHESTPLATE,
                    Items.DIAMOND_LEGGINGS,
                    Items.DIAMOND_BOOTS)
                    .stream()
                    .map(DisassemblyTableRecipe::enchantedEquipment)
                    .map(EmiStack::of)
                    .toList(), 1);
        }

        private static ItemStack enchantedEquipment(Item item) {
            ItemStack stack = new ItemStack(item);
            EnchantmentHelper.setEnchantments(Map.of(Enchantments.UNBREAKING, 1), stack);
            return stack;
        }

        private static ItemStack filledAffixCanvas() {
            return AffixCanvas.createWithAffixes(List.of(ResourceLocation.fromNamespaceAndPath("apotheosis", "ftbu")));
        }
    }
}
