package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.integration.emi.multipage.CustomModularEmiRecipe;

import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.config.SidebarSide;
import dev.emi.emi.screen.RecipeScreen;
import dev.emi.emi.screen.RecipeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(RecipeScreen.class)
public abstract class RecipeScreenMixin {

    @Shadow(remap = false)
    int x;

    @Shadow(remap = false)
    int backgroundHeight;

    @Shadow(remap = false)
    int backgroundWidth;

    @Shadow(remap = false)
    public abstract int getResolveOffset();

    @Shadow(remap = false)
    int y;

    @Shadow(remap = false)
    private List<RecipeTab> tabs;

    @Shadow(remap = false)
    private int tab;

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Ldev/emi/emi/EmiRenderHelper;drawNinePatch(Ldev/emi/emi/runtime/EmiDrawContext;Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V", ordinal = 4, remap = false), index = 2)
    private int modifyx(int x) {
        return x + 18 - 18 * gTOCore$getList(gTOCore$getWorkstationAmount());
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Ldev/emi/emi/EmiRenderHelper;drawNinePatch(Ldev/emi/emi/runtime/EmiDrawContext;Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V", ordinal = 4, remap = false), index = 4)
    private int modifyw(int x) {
        return x - 18 + 18 * gTOCore$getList(gTOCore$getWorkstationAmount());
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Ldev/emi/emi/EmiRenderHelper;drawNinePatch(Ldev/emi/emi/runtime/EmiDrawContext;Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V", ordinal = 4, remap = false), index = 5)
    private int modifyh(int x) {
        return 10 + Math.min(gTOCore$getWorkstationAmount(), gTOCore$maxWorkstations()) * 18 + getResolveOffset();
    }

    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "Ldev/emi/emi/api/widget/Widget;keyPressed(III)Z", remap = false))
    public boolean keyPressedHook(Widget instance, int keyCode, int scanCode, int modifiers) {
        if (instance instanceof CustomModularEmiRecipe widget) {
            return widget.keyPressed(keyCode, scanCode, modifiers);
        }
        return instance.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public Bounds getWorkstationBounds(int i) {
        Bounds bounds = Bounds.EMPTY;
        int offset = 0;
        if (i == -1) {
            i = 0;
            offset = -getResolveOffset();
        }
        if (EmiConfig.workstationLocation == SidebarSide.LEFT) {
            bounds = new Bounds(x - (gTOCore$getList(i) * 18), y + 9 + getResolveOffset() + (i % gTOCore$maxWorkstations() * 18) + offset, 18, 18);
        } else if (EmiConfig.workstationLocation == SidebarSide.RIGHT) {
            bounds = new Bounds(x + (gTOCore$getList(i) * backgroundWidth), y + 9 + getResolveOffset() + (i % gTOCore$maxWorkstations() * 18) + offset, 18, 18);
        } else if (EmiConfig.workstationLocation == SidebarSide.BOTTOM) {
            bounds = new Bounds(x + 5 + getResolveOffset() + i * 18 + offset, y + backgroundHeight - 23, 18, 18);
        }
        return bounds;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public int getMaxWorkstations() {
        return Integer.MAX_VALUE;
    }

    @Unique
    private int gTOCore$getWorkstationAmount() {
        return EmiApi.getRecipeManager().getWorkstations(tabs.get(tab).category).size();
    }

    @Unique
    private int gTOCore$getList(int i) {
        return (int) Math.floor((double) i / gTOCore$maxWorkstations()) + 1;
    }

    @Unique
    private int gTOCore$maxWorkstations() {
        return switch (EmiConfig.workstationLocation) {
            case LEFT, RIGHT -> (backgroundHeight - getResolveOffset() - 18) / 18;
            case BOTTOM -> (backgroundWidth - getResolveOffset() - 18) / 18;
            default -> 0;
        };
    }
}
