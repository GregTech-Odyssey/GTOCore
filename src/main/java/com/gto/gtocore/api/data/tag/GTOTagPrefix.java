package com.gto.gtocore.api.data.tag;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.chemical.material.GTOMaterial;
import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialFlags;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.mixin.gtm.chemical.MaterialPropertiesAccessor;
import com.gto.gtocore.utils.RLUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import com.kyanite.deeperdarker.DeeperDarker;
import com.kyanite.deeperdarker.content.DDBlocks;
import earth.terrarium.adastra.common.registry.ModBlocks;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.gregtechceu.gtceu.api.GTValues.M;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.NO_SMASHING;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@SuppressWarnings("unused")
public final class GTOTagPrefix extends TagPrefix {

    private int maxDamage;

    private GTOTagPrefix(String name) {
        super(name);
    }

    public static void init() {
        TagPrefix.dustTiny.generationCondition(TagPrefix.Conditions.hasDustProperty.and(mat -> ((MaterialPropertiesAccessor) ((GTOMaterial) mat).gtocore$getProperties()).getPropertyMap().size() > 1 || mat.hasFlag(GTOMaterialFlags.GENERATE_TINY_DUST)));
        TagPrefix.dustSmall.generationCondition(TagPrefix.Conditions.hasDustProperty.and(mat -> ((MaterialPropertiesAccessor) ((GTOMaterial) mat).gtocore$getProperties()).getPropertyMap().size() > 1 || mat.hasFlag(GTOMaterialFlags.GENERATE_SMALL_DUST)));
    }

    public static final TagPrefix moon_stone = oreTagPrefix("moon_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> ModBlocks.MOON_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    RLUtils.ad("block/moon_stone"));

    public static final TagPrefix mars_stone = oreTagPrefix("mars_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> ModBlocks.MARS_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    RLUtils.ad("block/mars_stone"));

    public static final TagPrefix venus_stone = oreTagPrefix("venus_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> ModBlocks.VENUS_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    RLUtils.ad("block/venus_stone"));

    public static final TagPrefix mercury_stone = oreTagPrefix("mercury_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> ModBlocks.MERCURY_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    RLUtils.ad("block/mercury_stone"));

    public static final TagPrefix glacio_stone = oreTagPrefix("glacio_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> ModBlocks.GLACIO_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    RLUtils.ad("block/glacio_stone"));

    public static final TagPrefix titan_stone = oreTagPrefix("titan_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> GTOBlocks.TITAN_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    GTOCore.id("block/titan_stone"));

    public static final TagPrefix pluto_stone = oreTagPrefix("pluto_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> GTOBlocks.PLUTO_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    GTOCore.id("block/pluto_stone"));

    public static final TagPrefix io_stone = oreTagPrefix("io_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> GTOBlocks.IO_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    GTOCore.id("block/io_stone"));

    public static final TagPrefix ganymede_stone = oreTagPrefix("ganymede_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> GTOBlocks.GANYMEDE_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    GTOCore.id("block/ganymede_stone"));

    public static final TagPrefix enceladus_stone = oreTagPrefix("enceladus_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> GTOBlocks.ENCELADUS_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    GTOCore.id("block/enceladus_stone"));

    public static final TagPrefix ceres_stone = oreTagPrefix("ceres_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> GTOBlocks.CERES_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    GTOCore.id("block/ceres_stone"));

    public static final TagPrefix sculk_stone = oreTagPrefix("sculk_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> DDBlocks.SCULK_STONE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(3.5F, 4.5F),
                    DeeperDarker.rl("block/sculk_stone"));

    public static final TagPrefix gloomslate = oreTagPrefix("gloomslate", BlockTags.MINEABLE_WITH_PICKAXE)
            .registerOre(() -> DDBlocks.GLOOMSLATE.get().defaultBlockState(), null,
                    BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(4.0F, 5.0F),
                    DeeperDarker.rl("block/gloomslate"));

    private static final MaterialIconType nanitesIcon = new MaterialIconType("nanites");

    public static final TagPrefix catalyst = new GTOTagPrefix("catalyst")
            .maxDamage(10000)
            .idPattern("%s_catalyst")
            .defaultTagPath("catalyst/%s")
            .unformattedTagPath("catalyst")
            .materialAmount(GTValues.M)
            .materialIconType(new MaterialIconType("catalyst"))
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_CATALYST));

    public static final TagPrefix nanites = new TagPrefix("nanites")
            .idPattern("%s_nanites")
            .defaultTagPath("nanites/%s")
            .unformattedTagPath("nanites")
            .materialAmount(GTValues.M)
            .materialIconType(nanitesIcon)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_NANITES));

    public static final TagPrefix contaminableNanites = new TagPrefix("contaminable_nanites")
            .idPattern("contaminable_%s_nanites")
            .defaultTagPath("contaminable_nanites/%s")
            .unformattedTagPath("contaminable_nanites")
            .materialAmount(GTValues.M)
            .materialIconType(nanitesIcon)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_NANITES));

    public static final TagPrefix milled = new TagPrefix("milled")
            .idPattern("milled_%s")
            .defaultTagPath("milleds/%s")
            .unformattedTagPath("milleds")
            .materialAmount(GTValues.M)
            .materialIconType(new MaterialIconType("milled"))
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_MILLED));

    public static final TagPrefix curvedPlate = new TagPrefix("curved_plate")
            .idPattern("curved_%s_plate")
            .defaultTagPath("curved_plates/%s")
            .unformattedTagPath("curved_plates")
            .materialAmount(GTValues.M)
            .materialIconType(new MaterialIconType("curved_plate"))
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_CURVED_PLATE) || mat.hasFlag(MaterialFlags.GENERATE_ROTOR) || ((mat.hasProperty(PropertyKey.FLUID_PIPE) || mat.hasProperty(PropertyKey.ITEM_PIPE)) && !mat.hasFlag(NO_SMASHING) && mat.getMass() < 240 && mat.getBlastTemperature() < 3600));

    public static final TagPrefix motorEnclosure = new TagPrefix("motor_enclosure")
            .idPattern("%s_motor_enclosure")
            .defaultTagPath("motor_enclosures/%s")
            .unformattedTagPath("motor_enclosures")
            .materialAmount(GTValues.M << 1)
            .materialIconType(new MaterialIconType("motor_enclosure"))
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_COMPONENT));

    public static final TagPrefix pumpBarrel = new TagPrefix("pump_barrel")
            .idPattern("%s_pump_barrel")
            .defaultTagPath("pump_barrels/%s")
            .unformattedTagPath("pump_barrels")
            .materialAmount(GTValues.M * 5 / 2)
            .materialIconType(new MaterialIconType("pump_barrel"))
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_COMPONENT));

    public static final TagPrefix pistonHousing = new TagPrefix("piston_housing")
            .idPattern("%s_piston_housing")
            .defaultTagPath("piston_housings/%s")
            .unformattedTagPath("piston_housings")
            .materialAmount(GTValues.M * 3)
            .materialIconType(new MaterialIconType("piston_housing"))
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_COMPONENT));

    public static final TagPrefix emitterBases = new TagPrefix("emitter_base")
            .idPattern("%s_emitter_base")
            .defaultTagPath("emitter_bases/%s")
            .unformattedTagPath("emitter_bases")
            .materialAmount(GTValues.M << 2)
            .materialIconType(new MaterialIconType("emitter_base"))
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_COMPONENT));

    public static final TagPrefix sensorCasing = new TagPrefix("sensor_casing")
            .idPattern("%s_sensor_casing")
            .defaultTagPath("sensor_casings/%s")
            .unformattedTagPath("sensor_casings")
            .materialAmount(GTValues.M * 9 / 2)
            .materialIconType(new MaterialIconType("sensor_casing"))
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_COMPONENT));

    public static final TagPrefix fieldGeneratorCasing = new TagPrefix("field_generator_casing")
            .idPattern("%s_field_generator_casing")
            .defaultTagPath("field_generator_casing/%s")
            .unformattedTagPath("field_generator_casing")
            .materialAmount(GTValues.M << 3)
            .materialIconType(new MaterialIconType("field_generator_casing"))
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_COMPONENT));

    public static final TagPrefix roughBlank = new TagPrefix("rough_blank")
            .idPattern("%s_rough_blank")
            .defaultTagPath("rough_blank/%s")
            .unformattedTagPath("rough_blank")
            .materialAmount(GTValues.M * 9)
            .materialIconType(MaterialIconType.rawOreBlock)
            .miningToolTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .unificationEnabled(true)
            .generateBlock(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_CERAMIC));

    public static final TagPrefix brick = new TagPrefix("brick")
            .idPattern("%s_brick")
            .defaultTagPath("brick/%s")
            .unformattedTagPath("brick")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.ingot)
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_CERAMIC));

    public static final TagPrefix flakes = new TagPrefix("flake")
            .idPattern("%s_flake")
            .defaultTagPath("flake/%s")
            .unformattedTagPath("flake")
            .materialAmount(GTValues.M / 4)
            .materialIconType(new MaterialIconType("flake"))
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_CERAMIC));

    public static final TagPrefix artificialGem = new TagPrefix("artificial_gem")
            .idPattern("artificial_%s_gem")
            .defaultTagPath("artificial_gem/%s")
            .unformattedTagPath("artificial_gem")
            .materialAmount(GTValues.M << 2)
            .materialIconType(new MaterialIconType("artificial_gem"))
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .tooltip((m, l) -> l.add(Component.translatable("gtocore.tooltip.artificial_gem").withStyle(ChatFormatting.GRAY)))
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_ARTIFICIAL_GEM));

    public static final TagPrefix crystalSeed = new TagPrefix("crystal_seed")
            .idPattern("%s_crystal_seed")
            .defaultTagPath("crystal_seed/%s")
            .unformattedTagPath("crystal_seed")
            .materialAmount(GTValues.M / 8)
            .materialIconType(new MaterialIconType("crystal_seed"))
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(GTOMaterialFlags.GENERATE_CRYSTAL_SEED));
}
