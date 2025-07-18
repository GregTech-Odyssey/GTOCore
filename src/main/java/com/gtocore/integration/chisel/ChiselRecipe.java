package com.gtocore.integration.chisel;

import com.gtocore.common.data.machines.MultiBlockG;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import com.periut.chisel.Chisel;
import com.periut.chisel.block.ChiselGroupLookup;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ChiselRecipe {

    private static final ChiselCategory CHISEL_CATEGORY = new ChiselCategory();

    public static void register(EmiRegistry registry) {
        registry.addCategory(CHISEL_CATEGORY);
        registry.addWorkstation(CHISEL_CATEGORY, EmiStack.of(Chisel.chiselSupplier.get()));
        registry.addWorkstation(CHISEL_CATEGORY, EmiStack.of(MultiBlockG.CARVING_CENTER.asStack()));
        Iterator<String> chiselGroupNames = ChiselGroupLookup.getGroupNameIterator();

        while (chiselGroupNames.hasNext()) {
            String chiselGroup = chiselGroupNames.next();
            List<Item> list = ChiselGroupLookup.getBlocksInGroup(chiselGroup);
            if (!list.isEmpty()) {
                registry.addRecipe(new ChiselEmiRecipe(chiselGroup, list));
            }
        }
    }

    private static final class ChiselCategory extends EmiRecipeCategory {

        private ChiselCategory() {
            super(new ResourceLocation("chisel", "chisel_recipes_category"), EmiStack.of(Chisel.chiselSupplier.get()));
        }

        @Override
        public Component getName() {
            return Component.nullToEmpty(I18n.get("rei.chisel.category"));
        }
    }

    private static final class ChiselEmiRecipe implements EmiRecipe {

        private static final ResourceLocation TEXTURE = new ResourceLocation("chisel", "textures/rei_recipes.png");
        private final ResourceLocation id;
        private final List<EmiIngredient> input;
        private final List<EmiStack> output;

        private ChiselEmiRecipe(String chiselGroup, List<Item> list) {
            this.id = new ResourceLocation("chisel", chiselGroup);
            this.input = new ArrayList<>();
            this.output = new ArrayList<>();

            for (Item item : list) {
                this.input.add(EmiStack.of(item));
                this.output.add(EmiStack.of(item));
            }
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return CHISEL_CATEGORY;
        }

        @Override
        public @Nullable ResourceLocation getId() {
            return this.id;
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return this.input;
        }

        @Override
        public List<EmiStack> getOutputs() {
            return this.output;
        }

        @Override
        public int getDisplayHeight() {
            return 200;
        }

        @Override
        public int getDisplayWidth() {
            return 150;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            int startX = 20;
            int startY = 4;
            int j = this.output.size();
            int rows = (int) Math.ceil((double) j / (double) 6.0F);
            widgets.addTexture(TEXTURE, 21, 3, 108, 236 - (18 * (10 - rows) + 11), 0, 10);
            widgets.addSlot(this.input.get(0), startX + 46, startY + 5).drawBack(false);

            for (int x = 0; x < 6; ++x) {
                for (int y = 0; y < rows && 6 * y + x < j; ++y) {
                    widgets.addSlot(this.output.get(6 * y + x), startX + 1 + 18 * x, startY + 44 + 18 * y).drawBack(false).recipeContext(this);
                }
            }
        }
    }
}
