package com.gto.gtocore.client;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.block.CraftingUnitType;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.render.crafting.AbstractCraftingUnitModelProvider;
import appeng.client.render.crafting.CraftingCubeModel;
import appeng.client.render.crafting.LightBakedModel;
import appeng.core.AppEng;
import appeng.hooks.BuiltInModelHooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
final class CraftingUnitModelProvider extends AbstractCraftingUnitModelProvider<CraftingUnitType> {

    private static final List<Material> MATERIALS = new ArrayList<>();

    private static final Material RING_CORNER = aeTexture("ring_corner");
    private static final Material RING_SIDE_HOR = aeTexture("ring_side_hor");
    private static final Material RING_SIDE_VER = aeTexture("ring_side_ver");
    private static final Material LIGHT_BASE = aeTexture("light_base");
    private static final Material STORAGE_1M_LIGHT = texture("crafting_storage_light_1m");
    private static final Material STORAGE_4M_LIGHT = texture("crafting_storage_light_4m");
    private static final Material STORAGE_16M_LIGHT = texture("crafting_storage_light_16m");
    private static final Material STORAGE_64M_LIGHT = texture("crafting_storage_light_64m");
    private static final Material STORAGE_256M_LIGHT = texture("crafting_storage_light_256m");
    private static final Material STORAGE_MAX_LIGHT = texture("crafting_storage_light_max");

    private CraftingUnitModelProvider(CraftingUnitType type) {
        super(type);
    }

    private TextureAtlasSprite getLightMaterial(Function<Material, TextureAtlasSprite> textureGetter) {
        return switch (type) {
            case STORAGE_1M -> textureGetter.apply(STORAGE_1M_LIGHT);
            case STORAGE_4M -> textureGetter.apply(STORAGE_4M_LIGHT);
            case STORAGE_16M -> textureGetter.apply(STORAGE_16M_LIGHT);
            case STORAGE_64M -> textureGetter.apply(STORAGE_64M_LIGHT);
            case STORAGE_256M -> textureGetter.apply(STORAGE_256M_LIGHT);
            case STORAGE_MAX -> textureGetter.apply(STORAGE_MAX_LIGHT);
        };
    }

    @Override
    public List<Material> getMaterials() {
        return Collections.unmodifiableList(MATERIALS);
    }

    @Override
    public BakedModel getBakedModel(Function<Material, TextureAtlasSprite> spriteGetter) {
        return new LightBakedModel(spriteGetter.apply(RING_CORNER), spriteGetter.apply(RING_SIDE_HOR), spriteGetter.apply(RING_SIDE_VER), spriteGetter.apply(LIGHT_BASE), getLightMaterial(spriteGetter));
    }

    private static Material texture(String name) {
        var material = new Material(InventoryMenu.BLOCK_ATLAS, GTOCore.id("block/crafting/" + name));
        MATERIALS.add(material);
        return material;
    }

    private static Material aeTexture(String name) {
        var material = new Material(InventoryMenu.BLOCK_ATLAS, AppEng.makeId("block/crafting/" + name));
        MATERIALS.add(material);
        return material;
    }

    static void initCraftingUnitModels() {
        for (CraftingUnitType type : CraftingUnitType.values()) {
            BuiltInModelHooks.addBuiltInModel(GTOCore.id("block/crafting/" + type.getAffix() + "_formed"), new CraftingCubeModel(new CraftingUnitModelProvider(type)));
        }
    }
}
