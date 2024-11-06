package com.gto.gtocore.client;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.block.CraftingUnitType;
import com.gto.gtocore.common.data.GTOBlocks;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
public class CraftingUnitModelProvider extends AbstractCraftingUnitModelProvider<CraftingUnitType> {

    private static final List<Material> MATERIALS = new ArrayList<>();

    private static final Material RING_CORNER = aeTexture("ring_corner");
    private static final Material RING_SIDE_HOR = aeTexture("ring_side_hor");
    private static final Material RING_SIDE_VER = aeTexture("ring_side_ver");
    private static final Material LIGHT_BASE = aeTexture("light_base");
    protected static final Material STORAGE_1M_LIGHT = texture("1m_storage_light");
    protected static final Material STORAGE_4M_LIGHT = texture("4m_storage_light");
    protected static final Material STORAGE_16M_LIGHT = texture("16m_storage_light");
    protected static final Material STORAGE_64M_LIGHT = texture("64m_storage_light");
    protected static final Material STORAGE_256M_LIGHT = texture("256m_storage_light");
    protected static final Material STORAGE_MAX_LIGHT = texture("max_storage_light");

    public CraftingUnitModelProvider(CraftingUnitType type) {
        super(type);
    }

    public TextureAtlasSprite getLightMaterial(Function<Material, TextureAtlasSprite> textureGetter) {
        return switch (this.type) {
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
        TextureAtlasSprite ringCorner = spriteGetter.apply(RING_CORNER);
        TextureAtlasSprite ringSideHor = spriteGetter.apply(RING_SIDE_HOR);
        TextureAtlasSprite ringSideVer = spriteGetter.apply(RING_SIDE_VER);
        TextureAtlasSprite lightBase = spriteGetter.apply(LIGHT_BASE);
        return new LightBakedModel(
                ringCorner,
                ringSideHor,
                ringSideVer,
                lightBase,
                getLightMaterial(spriteGetter));
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

    public static void initCraftingUnitModels() {
        for (CraftingUnitType type : CraftingUnitType.values()) {
            BuiltInModelHooks.addBuiltInModel(
                    GTOCore.id("block/crafting/" + type.getAffix() + "_formed"),
                    new CraftingCubeModel(new CraftingUnitModelProvider(type)));
        }

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(CraftingUnitModelProvider::setRenderLayer);
    }

    private static void setRenderLayer(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_1M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_4M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_16M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_64M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_256M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_MAX.get(), RenderType.cutout());
    }
}
