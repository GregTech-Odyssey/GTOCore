package com.gtocore.common.recipe.condition;

import com.gtolib.api.data.Dimension;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.data.Galaxy;

import com.gregtechceu.gtceu.api.data.DimensionMarker;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.recipe.condition.DimensionCondition;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.gtocore.common.recipe.condition.AbstractRecipeCondition.GALAXY;

public class GalaxyCondition extends DimensionCondition {

    private Galaxy galaxy;
    private DimensionMarker[] dimensions;

    public GalaxyCondition(Galaxy galaxy) {
        super();
        this.galaxy = galaxy;
    }

    public GalaxyCondition() {
        super();
        this.galaxy = Galaxy.NONE;
    }

    @Override
    public RecipeConditionType<?> getType() {
        return GALAXY;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("gtocore.condition.within_galaxy", Component.translatable("gtolib.galaxy.name." + galaxy.name()));
    }

    public RecipeCondition createTemplate() {
        return new GalaxyCondition();
    }

    @Override
    public SlotWidget setupDimensionMarkers(int xOffset, int yOffset) {
        Supplier<DimensionMarker> dimSupplier = () -> getDimensions()[Math.toIntExact((System.currentTimeMillis() / 1000) % getDimensions().length)];
        CustomItemStackHandler handler = new CustomItemStackHandler(1);
        return new SlotWidget(handler, 0, xOffset, yOffset, false, false) {

            @Override
            public void drawOverlay(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
                if (ConfigHolder.INSTANCE.compat.showDimensionTier) {
                    overlay = new TextTexture("T" + (dimSupplier.get().tier >= DimensionMarker.MAX_TIER ? "?" : dimSupplier.get().tier)).scale(0.75F).transform(-3.0F, 5.0F);
                }
                super.drawOverlay(graphics, mouseX, mouseY, partialTicks);
            }

            @Override
            public ItemStack getRealStack(ItemStack itemStack) {
                handler.setStackInSlot(0, dimSupplier.get().getIcon());
                return dimSupplier.get().getIcon();
            }

            @Override
            public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
                super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
            }
        }.setIngredientIO(IngredientIO.INPUT);
    }

    public DimensionMarker[] getDimensions() {
        if (dimensions != null && dimensions.length > 0) {
            return dimensions;
        }
        return dimensions = Stream.of(Dimension.values()).filter(d -> d.getGalaxy() == galaxy)
                .map(Dimension::getLocation)
                .map(GTRegistries.DIMENSION_MARKERS::get)
                .filter(Objects::nonNull)
                .toArray(DimensionMarker[]::new);
    }

    @Override
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        Level level = recipeLogic.machine.self().getLevel();
        return level != null && GTODimensions.getGalaxy(level.dimension().location()) == galaxy;
    }

    public @NotNull JsonObject serialize() {
        JsonObject config = super.serialize();
        config.addProperty("galaxy", this.galaxy.name());
        return config;
    }

    public RecipeCondition deserialize(@NotNull JsonObject config) {
        super.deserialize(config);
        this.galaxy = Galaxy.valueOf(GsonHelper.getAsString(config, "galaxy"));
        dimensions = null;
        return this;
    }

    public RecipeCondition fromNetwork(FriendlyByteBuf buf) {
        super.fromNetwork(buf);
        this.galaxy = Galaxy.valueOf(buf.readUtf());
        dimensions = null;
        return this;
    }

    public void toNetwork(FriendlyByteBuf buf) {
        super.toNetwork(buf);
        buf.writeUtf(galaxy.name());
    }
}
