package com.gtocore.integration.emi;

import com.gtolib.GTOCore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.runtime.EmiDrawContext;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModItems;
import vazkii.botania.common.item.BotaniaItems;

import java.util.ArrayList;
import java.util.List;

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
        widgets.addSlot(EmiStack.of(ModItems.kvasirMead), 8, 18)
                .appendTooltip(Component.translatable("gtocore.entry_alfheim.1")).recipeContext(this);

        for (int i = 0; i < 6; i++) {
            widgets.addSlot(AlfheimNeed[i], 36 + 18 * i, 9)
                    .appendTooltip(Component.translatable("gtocore.entry_alfheim.2")).recipeContext(this);
        }
        for (int i = 0; i < 6; i++) {
            widgets.addSlot(AlfheimNeed[i + 6], 36 + 18 * i, 9 + 18)
                    .appendTooltip(Component.translatable("gtocore.entry_alfheim.2")).recipeContext(this);
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
}
