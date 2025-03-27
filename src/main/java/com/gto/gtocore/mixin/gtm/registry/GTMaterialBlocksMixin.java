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
    private static final Set<TagPrefix> gTOCore$DEEPSLATE = Set.of(TagPrefix.oreDeepslate, GTOTagPrefix.SCULK_STONE, GTOTagPrefix.GLOOMSLATE);

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
        OREBuilder.put(GTMaterials.Electrotine, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Garnierite, Set.of(GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Oilsands, Set.of(GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Opal, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Pyrite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.ENCELADUS_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTOMaterials.Ostrum, Set.of(GTOTagPrefix.CERES_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.CertusQuartz, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.PLUTO_STONE));
        OREBuilder.put(GTMaterials.GraniticMineralSand, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Amethyst, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTOMaterials.Calorite, Set.of(GTOTagPrefix.MERCURY_STONE));
        OREBuilder.put(GTMaterials.Alunite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Sapphire, Set.of(GTOTagPrefix.TITAN_STONE));
        OREBuilder.put(GTMaterials.VanadiumMagnetite, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Bornite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.ENCELADUS_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Cobaltite, Set.of(GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Cooperite, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.ENCELADUS_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Magnesite, Set.of(GTOTagPrefix.VENUS_STONE, GTOTagPrefix.TITAN_STONE));
        OREBuilder.put(GTMaterials.Gold, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MOON_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Titanium, Set.of(GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Cobalt, Set.of(GTOTagPrefix.MERCURY_STONE));
        OREBuilder.put(GTMaterials.Malachite, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Redstone, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.BlueTopaz, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Spessartine, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.CERES_STONE));
        OREBuilder.put(GTMaterials.Uraninite, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.TITAN_STONE));
        OREBuilder.put(GTMaterials.Pyrochlore, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Goethite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Trona, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Saltpeter, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Spodumene, Set.of(GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Mica, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GANYMEDE_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.GarnetYellow, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Beryllium, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Sulfur, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE));
        OREBuilder.put(GTMaterials.Tungsten, Set.of(GTOTagPrefix.CERES_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Gypsum, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.GarnetSand, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.NetherQuartz, Set.of(TagPrefix.oreNetherrack));
        OREBuilder.put(GTMaterials.Cassiterite, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GANYMEDE_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.GlauconiteSand, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.MOON_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Pyrolusite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Nickel, Set.of(GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Almandine, Set.of(GTOTagPrefix.TITAN_STONE));
        OREBuilder.put(GTMaterials.Calcite, Set.of(GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Soapstone, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Sodalite, Set.of(GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Zeolite, Set.of(GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Lithium, Set.of(GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Sphalerite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE));
        OREBuilder.put(GTMaterials.Silver, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Ruby, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Naquadah, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.PLUTO_STONE));
        OREBuilder.put(GTMaterials.Neodymium, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Palladium, Set.of(GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.ENCELADUS_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Topaz, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Apatite, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Bentonite, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.CERES_STONE));
        OREBuilder.put(GTMaterials.Pollucite, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GANYMEDE_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Talc, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.CassiteriteSand, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Lepidolite, Set.of(GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Coal, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Stibnite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.BasalticMineralSand, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Barite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.PLUTO_STONE));
        OREBuilder.put(GTMaterials.Salt, Set.of(GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Magnetite, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.MOON_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Copper, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.ENCELADUS_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Asbestos, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Scheelite, Set.of(GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Tin, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.GANYMEDE_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.GarnetRed, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Realgar, Set.of(GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Iron, Set.of(GTOTagPrefix.ENCELADUS_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTOMaterials.Celestine, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Galena, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Chalcocite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Chromite, Set.of(GTOTagPrefix.VENUS_STONE, GTOTagPrefix.TITAN_STONE));
        OREBuilder.put(GTMaterials.Tetrahedrite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Molybdenite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Kyanite, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GANYMEDE_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Emerald, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Aluminium, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Platinum, Set.of(GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.ENCELADUS_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Ilmenite, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.ENCELADUS_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTOMaterials.Desh, Set.of(GTOTagPrefix.VENUS_STONE, GTOTagPrefix.TITAN_STONE));
        OREBuilder.put(GTMaterials.Bauxite, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.TricalciumPhosphate, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Quartzite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.PLUTO_STONE));
        OREBuilder.put(GTMaterials.Lead, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Pyrope, Set.of(GTOTagPrefix.TITAN_STONE));
        OREBuilder.put(GTMaterials.YellowLimonite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Tungstate, Set.of(GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Pentlandite, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Graphite, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE));
        OREBuilder.put(GTMaterials.Diatomite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.MOON_STONE, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Lazurite, Set.of(GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Cinnabar, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Grossular, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.RockSalt, Set.of(GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTOMaterials.Zircon, Set.of(GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Monazite, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Molybdenum, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Powellite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Plutonium239, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.PLUTO_STONE));
        OREBuilder.put(GTMaterials.Olivine, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.CERES_STONE));
        OREBuilder.put(GTMaterials.Bastnasite, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Chalcopyrite, Set.of(GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.ENCELADUS_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE, GTOTagPrefix.GANYMEDE_STONE));
        OREBuilder.put(GTMaterials.Wulfenite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.ENCELADUS_STONE));
        OREBuilder.put(GTMaterials.Pitchblende, Set.of(GTOTagPrefix.MOON_STONE, GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.TITAN_STONE));
        OREBuilder.put(GTMaterials.FullersEarth, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Diamond, Set.of(GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE));
        OREBuilder.put(GTMaterials.GreenSapphire, Set.of(GTOTagPrefix.TITAN_STONE));
        OREBuilder.put(GTMaterials.Hematite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.IO_STONE, GTOTagPrefix.VENUS_STONE, GTOTagPrefix.TITAN_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.MARS_STONE));
        OREBuilder.put(GTMaterials.Tantalite, Set.of(TagPrefix.oreNetherrack, GTOTagPrefix.PLUTO_STONE, GTOTagPrefix.MERCURY_STONE, GTOTagPrefix.CERES_STONE, GTOTagPrefix.GLACIO_STONE));
        OREBuilder.put(GTMaterials.Lapis, Set.of(GTOTagPrefix.GLACIO_STONE));
        ORE_MAP = OREBuilder.build();
    }
}
