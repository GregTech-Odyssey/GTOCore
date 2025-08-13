package com.gtocore.mixin.adastra;

import com.gtocore.common.network.ClientMessage;

import com.gtolib.adastra.IAdDisplayTagName;
import com.gtolib.api.misc.PlanetManagement;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.client.components.LabeledImageButton;
import earth.terrarium.adastra.client.screens.PlanetsScreen;
import earth.terrarium.adastra.common.constants.ConstantComponents;
import earth.terrarium.adastra.common.menus.PlanetsMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlanetsScreen.class)
public abstract class PlanetsScreenMixin extends AbstractContainerScreen<PlanetsMenu> {

    @Shadow(remap = false)
    protected abstract void close();

    @Shadow(remap = false)
    private @Nullable ResourceLocation selectedSolarSystem;

    @Shadow(remap = false)
    @Final
    private List<Button> buttons;

    @Shadow(remap = false)
    private int pageIndex;

    @Shadow(remap = false)
    private @Nullable Planet selectedPlanet;

    @Shadow(remap = false)
    @Final
    public static ResourceLocation BUTTON;

    protected PlanetsScreenMixin(PlanetsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "land", at = @At("HEAD"), remap = false, cancellable = true)
    private void land(ResourceKey<Level> dimension, CallbackInfo ci) {
        if (GTCEu.isDev()) return;
        boolean close = false;
        Player player = getMenu().player();
        ResourceLocation planet = dimension.location();
        ClientMessage.checkPlanetIsUnlocked(planet);
        if (!PlanetManagement.isClientUnlocked(planet)) {
            close = true;
            player.displayClientMessage(Component.translatable("gtocore.ununlocked"), false);
        }
        if (close) {
            close();
            ci.cancel();
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void createPlanetButtons() {
        for (var planet : menu.getSortedPlanets()) {
            if (planet.isSpace()) continue;
            if (!planet.solarSystem().equals(selectedSolarSystem)) continue;
            ResourceLocation resourceLocation = planet.dimension().location();
            int tier = PlanetManagement.calculateTier(planet, getMenu().player().level().dimension().location());
            if (menu.tier() < tier) continue;
            ClientMessage.checkPlanetIsUnlocked(resourceLocation);
            Button widget = addWidget(new LabeledImageButton(10, 0, 99, 20, 0, 0, 20, BUTTON, 99, 40, b -> {
                pageIndex = 2;
                selectedPlanet = planet;
                rebuildWidgets();
            }, menu.getPlanetName(planet.dimension())));
            widget.setTooltip(Tooltip.create(Component.translatable("tooltip.avaritia.tier", tier).append(" ").append(Component.translatable(PlanetManagement.isClientUnlocked(resourceLocation) ? "gtocore.unlocked" : "gtocore.ununlocked"))));
            buttons.add(widget);
        }
    }

    /**
     * @author .
     * @reason This method is used to create a tooltip for the space station recipe.
     */
    @Overwrite(remap = false)
    public Tooltip getSpaceStationRecipeTooltip(ResourceKey<Level> planet) {
        List<Component> tooltip = new ArrayList<>();
        BlockPos pos = this.menu.getLandingPos(planet, false);
        tooltip.add(Component.translatable("tooltip.ad_astra.construct_space_station_at", this.menu.getPlanetName(planet), pos.getX(), pos.getZ()).withStyle(ChatFormatting.AQUA));
        if (!this.menu.isInSpaceStation(planet) && !this.menu.isClaimed(planet)) {
            tooltip.add(ConstantComponents.CONSTRUCTION_COST.copy().withStyle(ChatFormatting.AQUA));
            List<IAdDisplayTagName.CountIngredient> ingredients = ((IAdDisplayTagName) this.menu).gtocore$getAdastraDisplayTagNames().get(planet);
            if (ingredients != null) {
                var gameTime = this.menu.player().level().getGameTime();
                for (IAdDisplayTagName.CountIngredient ingredient : ingredients) {
                    MutableComponent need = Component.empty();
                    Ingredient.Value[] values = ingredient.ingredient().values;
                    int displayWhich = Math.toIntExact((gameTime / 100) % values.length);
                    if (values[displayWhich] instanceof Ingredient.TagValue tagValue) {
                        String tagTranslationKey = gtocore$getTagTranslationKey(ResourceLocation.parse(tagValue.serialize().get("tag").getAsString()));
                        if (tagTranslationKey != null) {
                            need.append(Component.translatable(tagTranslationKey).withStyle(ChatFormatting.DARK_AQUA));
                        } else {
                            need.append(Component.translatable("tooltip.ad_astra.unknown_tag", tagValue.serialize().get("tag")).withStyle(ChatFormatting.RED));
                        }
                    } else if (values[displayWhich] instanceof Ingredient.ItemValue itemValue) {
                        need.append(itemValue.getItems().stream().toList().get(0).getHoverName().copy().withStyle(ChatFormatting.DARK_AQUA));
                    } else {
                        need.append(Component.translatable("tooltip.ad_astra.unknown_ingredient").withStyle(ChatFormatting.RED));
                    }
                    int howMuch = ingredient.holderCount();
                    int amountOwned = ingredient.count();
                    boolean hasEnough = this.menu.player().isCreative() || this.menu.player().isSpectator() || amountOwned >= howMuch;
                    tooltip.add(Component.translatable("tooltip.ad_astra.requirement", amountOwned, howMuch, need.withStyle(ChatFormatting.DARK_AQUA)).copy().withStyle(hasEnough ? ChatFormatting.GREEN : ChatFormatting.RED));
                }

            }
        } else {
            tooltip.add(ConstantComponents.SPACE_STATION_ALREADY_EXISTS);
        }
        return Tooltip.create(CommonComponents.joinLines(tooltip));
    }

    @Unique
    private static @Nullable String gtocore$getTagTranslationKey(ResourceLocation location) {
        String s = gtocore$translatePrefix("tag.item.", location);
        if (s != null) {
            return s;
        }
        return gtocore$translatePrefix("tag.", location);
    }

    @Unique
    private static @Nullable String gtocore$translatePrefix(String prefix, ResourceLocation id) {
        String s = EmiUtil.translateId(prefix, id);
        if (I18n.exists(s)) {
            return s;
        } else {
            if (id.getNamespace().equals("forge")) {
                s = EmiUtil.translateId(prefix, EmiPort.id("c", id.getPath()));
                if (I18n.exists(s)) {
                    return s;
                }
            }

            return null;
        }
    }
}
