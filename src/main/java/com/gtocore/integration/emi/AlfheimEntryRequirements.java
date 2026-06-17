package com.gtocore.integration.emi;

import com.gtocore.integration.mythic_botany.AlfheimHelper;

import com.gtolib.GTOCore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import com.lowdragmc.lowdraglib.utils.ColorUtils;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.runtime.EmiDrawContext;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModItems;
import vazkii.botania.common.item.BotaniaItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlfheimEntryRequirements implements EmiRecipe {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private final List<FormattedCharSequence> text;

    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(GTOCore.id("alfheim_entry_requirements"), EmiStack.of(ModItems.kvasirMead));

    private static final EmiStack[] AlfheimNeed = {
            EmiStack.of(BotaniaItems.kingKey),
            EmiStack.of(BotaniaItems.flugelEye),
            EmiStack.of(BotaniaItems.infiniteFruit),
            EmiStack.of(BotaniaItems.thorRing),
            EmiStack.of(BotaniaItems.odinRing),
            EmiStack.of(BotaniaItems.lokiRing),
            EmiStack.of(ModBlocks.mjoellnir.asItem()),
            EmiStack.of(ExtraBotanyItems.excalibur),
            EmiStack.of(ExtraBotanyItems.failnaught),
            EmiStack.of(ExtraBotanyItems.rheinHammer),
            EmiStack.of(ExtraBotanyItems.achillesShield),
            EmiStack.of(ExtraBotanyItems.voidArchives) };

    public AlfheimEntryRequirements() {
        this.text = new ArrayList<>(CLIENT.font.split(Component.translatable("gtocore.entry_alfheim.3"), getDisplayWidth() - 4));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public ResourceLocation getId() {
        return GTOCore.id("alfheim_entry_requirements");
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(
                EmiStack.of(ModItems.kvasirMead),
                EmiStack.of(BotaniaItems.kingKey),
                EmiStack.of(BotaniaItems.flugelEye),
                EmiStack.of(BotaniaItems.infiniteFruit),
                EmiStack.of(BotaniaItems.thorRing),
                EmiStack.of(BotaniaItems.odinRing),
                EmiStack.of(BotaniaItems.lokiRing),
                EmiStack.of(ModBlocks.mjoellnir.asItem()),
                EmiStack.of(ExtraBotanyItems.excalibur),
                EmiStack.of(ExtraBotanyItems.failnaught),
                EmiStack.of(ExtraBotanyItems.rheinHammer),
                EmiStack.of(ExtraBotanyItems.achillesShield),
                EmiStack.of(ExtraBotanyItems.voidArchives));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public int getDisplayWidth() {
        return 150;
    }

    @Override
    public int getDisplayHeight() {
        return 160;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var player = CLIENT.player;
        Objects.requireNonNull(player);
        boolean fullmet = AlfheimHelper.clientCanPlayerUsePortal(player);
        widgets.add(new Slot(EmiStack.of(ModItems.kvasirMead), 8, 18,
                Component.translatable("gtocore.entry_alfheim.1.c"), Component.translatable("gtocore.entry_alfheim.1"),
                metStatus(fullmet, AlfheimHelper.clientPlayerHasKnowledge(player)))).recipeContext(this);

        var metItems = AlfheimHelper.itemObtained(player);
        for (int i = 0; i < 6; i++) {
            // widgets.addSlot(AlfheimNeed[i], 36 + 18 * i, 9)
            // .appendTooltip(Component.translatable("gtocore.entry_alfheim.2")).recipeContext(this);
            widgets.add(new Slot(AlfheimNeed[i], 36 + 18 * i, 9,
                    Component.translatable("gtocore.entry_alfheim.2.c"), Component.translatable("gtocore.entry_alfheim.2"),
                    metStatus(fullmet, metItems.contains(AlfheimNeed[i].getKey())))).recipeContext(this);
        }
        for (int i = 0; i < 6; i++) {
            // widgets.addSlot(AlfheimNeed[i + 6], 36 + 18 * i, 9 + 18)
            // .appendTooltip(Component.translatable("gtocore.entry_alfheim.2")).recipeContext(this);
            widgets.add(new Slot(AlfheimNeed[i + 6], 36 + 18 * i, 9 + 18,
                    Component.translatable("gtocore.entry_alfheim.2.c"), Component.translatable("gtocore.entry_alfheim.2"),
                    metStatus(fullmet, metItems.contains(AlfheimNeed[i + 6].getKey())))).recipeContext(this);
        }

        int y = 54;
        Font font = CLIENT.font;
        int singleLineTotalHeight = font.lineHeight + 2;
        int lineCount = (widgets.getHeight() - y - 4) / singleLineTotalHeight;
        PageManager manager = new PageManager(text, lineCount);

        if (lineCount < text.size()) {
            widgets.addButton(2, 2, 12, 12, 0, 0, () -> true, (mouseX, mouseY, button) -> manager.scroll(-1));
            widgets.addButton(widgets.getWidth() - 14, 2, 12, 12, 12, 0, () -> true, (mouseX, mouseY, button) -> manager.scroll(1));
        }
        widgets.addDrawable(0, y, 0, 0, (raw, mouseX, mouseY, delta) -> {
            EmiDrawContext context = EmiDrawContext.wrap(raw);
            int lo = manager.start();
            for (int i = 0; i < lineCount; i++) {
                int l = lo + i;
                if (l >= manager.lines.size()) return;
                FormattedCharSequence textLine = manager.lines.get(l);
                int drawY = i * singleLineTotalHeight;
                context.drawText(textLine, 4, drawY, 0x000000);
            }
        });
    }

    private static class PageManager {

        public final List<FormattedCharSequence> lines;
        public final int pageSize;
        public int currentPage;

        public PageManager(List<FormattedCharSequence> lines, int pageSize) {
            this.lines = lines;
            this.pageSize = pageSize;
        }

        public void scroll(int delta) {
            currentPage += delta;
            int totalPages = (lines.size() - 1) / pageSize + 1;
            if (currentPage < 0) currentPage = totalPages - 1;
            if (currentPage >= totalPages) currentPage = 0;
        }

        public int start() {
            return currentPage * pageSize;
        }
    }

    private static final int FULL_MET = 2;
    private static final int HALF_MET = 1;
    private static final int NO_MET = 0;

    private int metStatus(boolean fullMet, boolean halfMet) {
        if (fullMet) return FULL_MET;
        if (halfMet) return HALF_MET;
        return NO_MET;
    }

    private static class Slot extends SlotWidget {

        int metStatus;

        public Slot(EmiIngredient ingredient, int x, int y, Component halfMetTooltip, Component noMetTooltip, int metStatus) {
            super(ingredient, x, y);
            this.metStatus = metStatus;
            appendTooltip(switch (metStatus) {
                case FULL_MET -> Component.translatable("gtocore.entry_alfheim.0.c");
                case HALF_MET -> halfMetTooltip;
                case NO_MET -> noMetTooltip;
                default -> Component.empty();
            });
        }

        @Override
        public void drawBackground(GuiGraphics draw, int mouseX, int mouseY, float delta) {
            float lerp = (float) (Math.sin((float) (System.currentTimeMillis() % 1000) / 1000f * Math.PI * 2) * 0.1f + 0.9f);
            int slotColor = ColorUtils.color(255, 139, 139, 139);
            var color = switch (metStatus) {
                case FULL_MET -> ColorUtils.blendColor(0xff00FF00, slotColor, lerp);
                case HALF_MET -> ColorUtils.blendColor(0xff0000FF, slotColor, lerp);
                case NO_MET -> ColorUtils.blendColor(0xffFFFF00, slotColor, lerp);
                default -> 0x00000000;
            };
            var bound = getBounds();
            int x = bound.x();
            int y = bound.y();
            super.drawBackground(draw, mouseX, mouseY, delta);
            draw.fill(x + 1, y + 1, x + 17, y + 17, color);
        }
    }
}
