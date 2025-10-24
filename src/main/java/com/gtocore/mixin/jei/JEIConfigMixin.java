package com.gtocore.mixin.jei;

import net.minecraft.resources.ResourceLocation;

import jeresources.compatibility.api.JERAPI;
import jeresources.compatibility.minecraft.MinecraftCompat;
import jeresources.config.Settings;
import jeresources.jei.JEIConfig;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.jei.enchantment.EnchantmentCategory;
import jeresources.jei.mob.MobCategory;
import jeresources.jei.plant.PlantCategory;
import jeresources.jei.villager.VillagerWrapper;
import jeresources.jei.worldgen.WorldGenWrapper;
import jeresources.registry.DungeonRegistry;
import jeresources.registry.EnchantmentRegistry;
import jeresources.registry.MobRegistry;
import jeresources.registry.PlantRegistry;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(JEIConfig.class)
public class JEIConfigMixin {

    @Mutable
    @Shadow(remap = false)
    @Final
    public static RecipeType<WorldGenWrapper> WORLD_GEN_TYPE;

    @Mutable
    @Shadow(remap = false)
    @Final
    public static RecipeType<VillagerWrapper> VILLAGER_TYPE;

    @Shadow(remap = false)
    @Final
    public static Map<ResourceLocation, RecipeType<?>> TYPES;

    @Mutable
    @Shadow(remap = false)
    @Final
    public static ResourceLocation WORLD_GEN;

    @Mutable
    @Shadow(remap = false)
    @Final
    public static ResourceLocation VILLAGER;

    @Inject(method = "registerRecipes", at = @At(value = "INVOKE", target = "Lmezz/jei/api/registration/IRecipeRegistration;addRecipes(Lmezz/jei/api/recipe/RecipeType;Ljava/util/List;)V", ordinal = 4), remap = false, cancellable = true)
    private void registerRecipes(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "registerCategories", at = @At(value = "INVOKE", target = "Lmezz/jei/api/registration/IRecipeCategoryRegistration;addRecipeCategories([Lmezz/jei/api/recipe/category/IRecipeCategory;)V"), remap = false, cancellable = true)
    private void registerCategories(IRecipeCategoryRegistration registration, CallbackInfo ci) {
        registration.addRecipeCategories(new DungeonCategory(), new EnchantmentCategory(), new MobCategory(), new PlantCategory());
        DungeonRegistry.getInstance().clear();
        MobRegistry.getInstance().clear();
        PlantRegistry.getInstance().clear();
        new MinecraftCompat().init(false);
        JERAPI.commit(false);
        EnchantmentRegistry.getInstance().removeAll(Settings.excludedEnchants);
        ci.cancel();
    }

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void init(CallbackInfo ci) {
        TYPES.remove(WORLD_GEN);
        TYPES.remove(VILLAGER);
        WORLD_GEN = null;
        VILLAGER = null;
        WORLD_GEN_TYPE = null;
        VILLAGER_TYPE = null;
    }
}
