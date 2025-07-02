package com.gtocore.mixin.adastra;

import com.gtocore.common.network.ClientMessage;

import com.gtolib.api.misc.PlanetManagement;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.client.components.LabeledImageButton;
import earth.terrarium.adastra.client.screens.PlanetsScreen;
import earth.terrarium.adastra.common.menus.PlanetsMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
