package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.utils.register.BlockRegisterUtils;

import com.gregtechceu.gtceu.api.block.MaterialBlock;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.tterrag.registrate.util.entry.BlockEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(GTMaterialBlocks.class)
public final class GTMaterialBlocksMixin {

    @Shadow(remap = false)
    static ImmutableTable.Builder<TagPrefix, Material, BlockEntry<? extends MaterialBlock>> MATERIAL_BLOCKS_BUILDER;

    @Unique
    private static ImmutableMap<Material, Set<TagPrefix>> ORE_MAP;

    @Unique
    private static final Set<TagPrefix> gTOCore$DEEPSLATE = Set.of(TagPrefix.oreDeepslate, GTOTagPrefix.sculk_stone, GTOTagPrefix.gloomslate);

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private static void registerOreBlock(Material material, GTRegistrate registrate) {
        BlockRegisterUtils.registerOreBlock(material, registrate, ORE_MAP, gTOCore$DEEPSLATE, MATERIAL_BLOCKS_BUILDER);
    }

    @Inject(method = "generateOreBlocks", at = @At("TAIL"), remap = false)
    private static void generateOreBlocks(CallbackInfo ci) {
        ORE_MAP = null;
    }

    static {
        ImmutableMap.Builder<Material, Set<TagPrefix>> OREBuilder = ImmutableMap.builder();
        OREBuilder.put(GTMaterials.Naquadah, Set.of(GTOTagPrefix.io_stone, TagPrefix.oreEndstone, GTOTagPrefix.pluto_stone));
        OREBuilder.put(GTMaterials.Asbestos, Set.of(GTOTagPrefix.moon_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Chalcopyrite, Set.of(GTOTagPrefix.mercury_stone, GTOTagPrefix.enceladus_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.CassiteriteSand, Set.of(GTOTagPrefix.moon_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Wulfenite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Zeolite, Set.of(GTOTagPrefix.mercury_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Trona, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Tungstate, Set.of(TagPrefix.oreEndstone, GTOTagPrefix.ceres_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Cooperite, Set.of(GTOTagPrefix.io_stone, TagPrefix.oreEndstone, GTOTagPrefix.mercury_stone, GTOTagPrefix.enceladus_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Opal, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Silver, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Tantalite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.pluto_stone, GTOTagPrefix.mercury_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Hematite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Grossular, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.pluto_stone, GTOTagPrefix.mercury_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Pyrochlore, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.mercury_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Alunite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.mercury_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Iron, Set.of(GTOTagPrefix.enceladus_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Coal, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Quartzite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.pluto_stone));
        OREBuilder.put(GTMaterials.Topaz, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.mercury_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Molybdenum, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.YellowLimonite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Sapphire, Set.of(GTOTagPrefix.titan_stone));
        OREBuilder.put(GTMaterials.GraniticMineralSand, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Ilmenite, Set.of(GTOTagPrefix.moon_stone, TagPrefix.oreEndstone, GTOTagPrefix.enceladus_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTOMaterials.Desh, Set.of(GTOTagPrefix.venus_stone, GTOTagPrefix.titan_stone));
        OREBuilder.put(GTMaterials.Realgar, Set.of(GTOTagPrefix.mercury_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Sodalite, Set.of(GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Talc, Set.of(GTOTagPrefix.moon_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Diatomite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.moon_stone, GTOTagPrefix.mercury_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Pyrope, Set.of(GTOTagPrefix.titan_stone));
        OREBuilder.put(GTMaterials.VanadiumMagnetite, Set.of(GTOTagPrefix.moon_stone, TagPrefix.oreEndstone, GTOTagPrefix.venus_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Mica, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.ganymede_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Chromite, Set.of(TagPrefix.oreEndstone, GTOTagPrefix.venus_stone, GTOTagPrefix.titan_stone));
        OREBuilder.put(GTMaterials.Pyrite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.enceladus_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.BlueTopaz, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.mercury_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Olivine, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.ceres_stone));
        OREBuilder.put(GTMaterials.Bentonite, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.ceres_stone));
        OREBuilder.put(GTMaterials.Oilsands, Set.of(GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Beryllium, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Diamond, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone));
        OREBuilder.put(GTMaterials.Saltpeter, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.mercury_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Lapis, Set.of(GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Soapstone, Set.of(GTOTagPrefix.moon_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Pentlandite, Set.of(GTOTagPrefix.moon_stone, GTOTagPrefix.mercury_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Spessartine, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.mercury_stone, GTOTagPrefix.ceres_stone));
        OREBuilder.put(GTMaterials.CertusQuartz, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.pluto_stone));
        OREBuilder.put(GTMaterials.GarnetYellow, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Barite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.pluto_stone));
        OREBuilder.put(GTMaterials.Lepidolite, Set.of(GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Ruby, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Copper, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.enceladus_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTOMaterials.Calorite, Set.of(GTOTagPrefix.mercury_stone));
        OREBuilder.put(GTMaterials.Bastnasite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.moon_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Tin, Set.of(GTOTagPrefix.moon_stone, GTOTagPrefix.ganymede_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Gypsum, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Sphalerite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone));
        OREBuilder.put(GTMaterials.Stibnite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.TricalciumPhosphate, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Platinum, Set.of(TagPrefix.oreEndstone, GTOTagPrefix.mercury_stone, GTOTagPrefix.enceladus_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Scheelite, Set.of(TagPrefix.oreEndstone, GTOTagPrefix.ceres_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Cobalt, Set.of(GTOTagPrefix.mercury_stone));
        OREBuilder.put(GTOMaterials.Ostrum, Set.of(GTOTagPrefix.ceres_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Calcite, Set.of(GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Pollucite, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.ganymede_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Redstone, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Aluminium, Set.of(GTOTagPrefix.moon_stone, TagPrefix.oreEndstone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Amethyst, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Spodumene, Set.of(GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Cobaltite, Set.of(GTOTagPrefix.mercury_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Neodymium, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.moon_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTOMaterials.Celestine, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.FullersEarth, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Emerald, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.GreenSapphire, Set.of(GTOTagPrefix.titan_stone));
        OREBuilder.put(GTMaterials.Pyrolusite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.pluto_stone, GTOTagPrefix.mercury_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Magnetite, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.moon_stone, TagPrefix.oreEndstone, GTOTagPrefix.venus_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Lithium, Set.of(TagPrefix.oreEndstone, GTOTagPrefix.ceres_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Cinnabar, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTOMaterials.Zircon, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Titanium, Set.of(TagPrefix.oreEndstone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.GarnetSand, Set.of(GTOTagPrefix.moon_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Electrotine, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.mercury_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.NetherQuartz, Set.of(TagPrefix.oreNetherrack));
        OREBuilder.put(GTMaterials.Chalcocite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.mercury_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Kyanite, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.ganymede_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Monazite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.moon_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Goethite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Magnesite, Set.of(GTOTagPrefix.venus_stone, GTOTagPrefix.titan_stone));
        OREBuilder.put(GTMaterials.Tungsten, Set.of(GTOTagPrefix.ceres_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Tetrahedrite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.Uraninite, Set.of(GTOTagPrefix.moon_stone, TagPrefix.oreEndstone, GTOTagPrefix.pluto_stone, GTOTagPrefix.titan_stone));
        OREBuilder.put(GTMaterials.Galena, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Almandine, Set.of(GTOTagPrefix.titan_stone));
        OREBuilder.put(GTMaterials.Cassiterite, Set.of(GTOTagPrefix.moon_stone, GTOTagPrefix.mercury_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.ganymede_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Powellite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Molybdenite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Bornite, Set.of(TagPrefix.oreNetherrack, TagPrefix.oreEndstone, GTOTagPrefix.mercury_stone, GTOTagPrefix.enceladus_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Graphite, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone));
        OREBuilder.put(GTMaterials.Lead, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.enceladus_stone));
        OREBuilder.put(GTMaterials.Nickel, Set.of(GTOTagPrefix.mercury_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Pitchblende, Set.of(GTOTagPrefix.moon_stone, TagPrefix.oreEndstone, GTOTagPrefix.pluto_stone, GTOTagPrefix.titan_stone));
        OREBuilder.put(GTMaterials.Sulfur, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.io_stone, GTOTagPrefix.venus_stone));
        OREBuilder.put(GTMaterials.Gold, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.moon_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Salt, Set.of(GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Plutonium239, Set.of(GTOTagPrefix.io_stone, TagPrefix.oreEndstone, GTOTagPrefix.pluto_stone));
        OREBuilder.put(GTMaterials.Bauxite, Set.of(GTOTagPrefix.moon_stone, TagPrefix.oreEndstone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Malachite, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.GarnetRed, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.GlauconiteSand, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.moon_stone, GTOTagPrefix.venus_stone, GTOTagPrefix.ceres_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Lazurite, Set.of(GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Garnierite, Set.of(GTOTagPrefix.mercury_stone, GTOTagPrefix.ganymede_stone));
        OREBuilder.put(GTMaterials.Palladium, Set.of(TagPrefix.oreEndstone, GTOTagPrefix.mercury_stone, GTOTagPrefix.enceladus_stone, GTOTagPrefix.mars_stone, GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.BasalticMineralSand, Set.of(GTOTagPrefix.io_stone, GTOTagPrefix.mars_stone));
        OREBuilder.put(GTMaterials.RockSalt, Set.of(GTOTagPrefix.glacio_stone));
        OREBuilder.put(GTMaterials.Apatite, Set.of(GTOTagPrefix.pluto_stone, GTOTagPrefix.titan_stone, GTOTagPrefix.mars_stone));
        ORE_MAP = OREBuilder.build();
    }
}
