package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.api.registries.GTORegistration;
import com.gto.gtocore.common.data.GTOCreativeModeTabs;
import com.gto.gtocore.utils.register.ItemRegisterUtils;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.registry.MaterialRegistry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.ItemEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GTMaterialItems.class)
public final class GTMaterialItemsMixin {

    @Shadow(remap = false)
    static ImmutableTable.Builder<TagPrefix, Material, ItemEntry<TagPrefixItem>> MATERIAL_ITEMS_BUILDER;

    @Shadow(remap = false)
    public static Table<TagPrefix, Material, ItemEntry<TagPrefixItem>> MATERIAL_ITEMS;

    @Inject(method = "generateTools", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;creativeModeTab(Ljava/util/function/Supplier;)V"), remap = false)
    private static void setToolCreativeModeTab(CallbackInfo ci) {
        GTORegistration.REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_ITEM);
    }

    @Inject(method = "generateMaterialItems", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;creativeModeTab(Ljava/util/function/Supplier;)V", shift = At.Shift.AFTER), remap = false, cancellable = true)
    private static void setItemCreativeModeTab(CallbackInfo ci) {
        ci.cancel();
        GTORegistration.REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_MATERIAL_ITEM);
        for (var tagPrefix : TagPrefix.values()) {
            if (tagPrefix.doGenerateItem()) {
                for (MaterialRegistry registry : GTCEuAPI.materialManager.getRegistries()) {
                    GTRegistrate registrate = registry.getRegistrate();
                    for (Material material : registry.getAllMaterials()) {
                        if (tagPrefix.doGenerateItem(material)) {
                            ItemRegisterUtils.generateMaterialItem(registrate, tagPrefix, material, MATERIAL_ITEMS_BUILDER);
                        }
                    }
                }
            }
        }
        MATERIAL_ITEMS = MATERIAL_ITEMS_BUILDER.build();
    }
}
