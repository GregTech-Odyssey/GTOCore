package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.common.block.*;
import com.gto.gtocore.utils.RLUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.IFusionCasingType;
import com.gregtechceu.gtceu.api.block.MaterialBlock;
import com.gregtechceu.gtceu.api.block.OreBlock;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.MaterialBlockItem;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.common.block.FusionCasingBlock;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTModels;
import com.gregtechceu.gtceu.core.mixins.BlockPropertiesAccessor;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.block.crafting.CraftingUnitBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.crafting.CraftingBlockEntity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.GTValues.VN;
import static com.gregtechceu.gtceu.common.data.GTBlocks.ALL_FUSION_CASINGS;
import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;
import static com.gto.gtocore.common.block.BlockMap.*;

@SuppressWarnings("unused")
public final class GTOBlocks {

    public static final Map<String, String> LANG = GTCEu.isDataGen() ? new HashMap<>() : null;

    public static void addLang(String name, String cn) {
        if (LANG == null) return;
        if (LANG.containsKey(name)) {
            GTOCore.LOGGER.error("Repetitive Key: {}", name);
            throw new IllegalStateException();
        }
        if (LANG.containsValue(cn)) {
            GTOCore.LOGGER.error("Repetitive Value: {}", cn);
            throw new IllegalStateException();
        }
        LANG.put(name, cn);
    }

    static {
        REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_BLOCK);
    }

    public static void init() {}

    private static <T extends Block> BlockBuilder<T, Registrate> block(String name, String cn, NonNullFunction<BlockBehaviour.Properties, T> factory) {
        addLang(name, cn);
        return REGISTRATE.block(name, factory).onRegister(GTOLoots.BLOCKS::add);
    }

    public static void registerOreBlock(Material material, GTRegistrate registrate, ImmutableMap<Material, Set<TagPrefix>> ORE_MAP, Set<TagPrefix> gTOCore$DEEPSLATE, ImmutableTable.Builder<TagPrefix, Material, BlockEntry<? extends MaterialBlock>> MATERIAL_BLOCKS_BUILDER) {
        float destroyTime = (float) material.getMass() / 50;
        float explosionResistance = (float) material.getBlastTemperature() / 500;
        for (var ore : TagPrefix.ORES.entrySet()) {
            if (ore.getKey().isIgnored(material)) continue;
            var oreTag = ore.getKey();
            if (oreTag != TagPrefix.ore) {
                Set<TagPrefix> tagPrefixes = ORE_MAP.get(material);
                if (tagPrefixes == null || (!gTOCore$DEEPSLATE.contains(oreTag) && !tagPrefixes.contains(oreTag))) continue;
            }
            final TagPrefix.OreType oreType = ore.getValue();
            var entry = registrate.block("%s%s_ore".formatted(oreTag != TagPrefix.ore ? FormattingUtil.toLowerCaseUnder(oreTag.name()) + "_" : "", material.getName()), properties -> new OreBlock(properties, oreTag, material, true))
                    .initialProperties(() -> {
                        if (oreType.stoneType().get().isAir()) {
                            return Blocks.IRON_ORE;
                        }
                        return oreType.stoneType().get().getBlock();
                    })
                    .properties(properties -> {
                        BlockBehaviour.Properties p = GTBlocks.copy(oreType.template().get(), properties).noLootTable();
                        p.destroyTime(((BlockPropertiesAccessor) p).getDestroyTime() + destroyTime);
                        p.explosionResistance(((BlockPropertiesAccessor) p).getExplosionResistance() + explosionResistance);
                        return p;
                    })
                    .blockstate(NonNullBiConsumer.noop())
                    .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                    .setData(ProviderType.LOOT, NonNullBiConsumer.noop())
                    .transform(GTBlocks.unificationBlock(oreTag, material))
                    .color(() -> MaterialBlock::tintedColor)
                    .item(MaterialBlockItem::create)
                    .model(NonNullBiConsumer.noop())
                    .onRegister(MaterialBlockItem::onRegister)
                    .color(() -> MaterialBlockItem::tintColor)
                    .build()
                    .register();
            MATERIAL_BLOCKS_BUILDER.put(oreTag, material, entry);
        }
    }

    private static BlockEntry<CraftingUnitBlock> registerCraftingUnitBlock(int tier, CraftingUnitType Type) {
        return block("crafting_storage_" + (tier == -1 ? "max" : tier + "m"), (tier == -1 ? "MAX" : tier + "M") + "合成存储器", p -> new CraftingUnitBlock(Type))
                .blockstate((ctx, provider) -> {
                    String formed = "block/crafting/" + ctx.getName() + "_formed";
                    String unformed = "block/crafting/" + ctx.getName();
                    provider.models().cubeAll(unformed, provider.modLoc("block/crafting/" + ctx.getName()));
                    provider.models().getBuilder(formed);
                    provider.getVariantBuilder(ctx.get()).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(provider.models().getExistingFile(provider.modLoc(state.getValue(AbstractCraftingUnitBlock.FORMED) ? formed : unformed))).build(), AbstractCraftingUnitBlock.POWERED);
                })
                .item(BlockItem::new)
                .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/crafting/" + ctx.getName())))
                .build()
                .register();
    }

    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_1M = registerCraftingUnitBlock(1,
            CraftingUnitType.STORAGE_1M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_4M = registerCraftingUnitBlock(4,
            CraftingUnitType.STORAGE_4M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_16M = registerCraftingUnitBlock(16,
            CraftingUnitType.STORAGE_16M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_64M = registerCraftingUnitBlock(64,
            CraftingUnitType.STORAGE_64M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_256M = registerCraftingUnitBlock(256,
            CraftingUnitType.STORAGE_256M);
    public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_MAX = registerCraftingUnitBlock(-1,
            CraftingUnitType.STORAGE_MAX);

    public static BlockEntityEntry<CraftingBlockEntity> CRAFTING_STORAGE = REGISTRATE
            .blockEntity("crafting_storage", CraftingBlockEntity::new)
            .validBlocks(
                    CRAFTING_STORAGE_1M,
                    CRAFTING_STORAGE_4M,
                    CRAFTING_STORAGE_16M,
                    CRAFTING_STORAGE_64M,
                    CRAFTING_STORAGE_256M,
                    CRAFTING_STORAGE_MAX)
            .onRegister(type -> {
                for (CraftingUnitType craftingUnitType : CraftingUnitType.values()) {
                    AEBaseBlockEntity.registerBlockEntityItem(type, craftingUnitType.getItemFromType());
                    craftingUnitType.getDefinition().get().setBlockEntity(CraftingBlockEntity.class, type, null, null);
                }
            })
            .register();

    public static final BlockEntry<GelidCryotheumBlock> GELID_CRYOTHEUM = block("gelid_cryotheum", "极寒之凛冰", GelidCryotheumBlock::new)
            .blockstate(NonNullBiConsumer.noop())
            .register();

    private static BlockEntry<Block> createTierCasings(String name, String cn, ResourceLocation texture,
                                                       Int2ObjectOpenHashMap<Supplier<?>> map, int tier) {
        return createTierCasings(name, cn, texture, () -> Blocks.IRON_BLOCK, p -> new Block(p) {

            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level,
                                        @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
                tooltip.add(Component.translatable("tooltip.avaritia.tier", tier));
            }
        }, () -> RenderType::cutoutMipped, map, tier);
    }

    private static BlockEntry<Block> createTierGlassCasings(String name, String cn, ResourceLocation texture, int tier) {
        return createTierCasings(name, cn, texture, () -> Blocks.GLASS, p -> new GlassBlock(p) {

            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
                super.appendHoverText(stack, level, tooltip, flag);
                tooltip.add(Component.translatable("tooltip.avaritia.tier", tier));
            }
        }, () -> RenderType::translucent, GLASSMAP, tier);
    }

    private static BlockEntry<Block> createTierCasings(String name, String cn, ResourceLocation texture,
                                                       NonNullSupplier<? extends Block> block,
                                                       NonNullFunction<BlockBehaviour.Properties, Block> blockSupplier,
                                                       Supplier<Supplier<RenderType>> type,
                                                       Int2ObjectOpenHashMap<Supplier<?>> map, int tier) {
        BlockEntry<Block> Block = block(name, cn, blockSupplier)
                .initialProperties(block)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(type)
                .blockstate(GTModels.cubeAllModel(name, texture))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
        map.put(tier, Block);
        return Block;
    }

    private static BlockEntry<ActiveBlock> createActiveCasing(String name, String cn, String baseModelPath) {
        return block(name, cn, ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GTModels.createActiveModel(GTOCore.id(baseModelPath)))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), GTOCore.id(baseModelPath)))
                .build()
                .register();
    }

    private static BlockEntry<ActiveBlock> createActiveTierCasing(String name, String cn, String baseModelPath,
                                                                  Int2ObjectOpenHashMap<Supplier<?>> map, int tier) {
        BlockEntry<ActiveBlock> Block = block(name, cn, p -> (ActiveBlock) new ActiveBlock(p) {

            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
                super.appendHoverText(stack, level, tooltip, flag);
                tooltip.add(Component.translatable("tooltip.avaritia.tier", tier));
            }
        })
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GTModels.createActiveModel(GTOCore.id(baseModelPath)))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), GTOCore.id(baseModelPath)))
                .build()
                .register();
        map.put(tier, Block);
        return Block;
    }

    private static BlockEntry<Block> createCleanroomFilter() {
        var filterBlock = block(CleanroomFilterType.FILTER_CASING_LAW.getSerializedName(), "绝对洁净过滤器机械方块", Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(properties -> properties.strength(2.0f, 8.0f).sound(SoundType.METAL)
                        .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false))
                .addLayer(() -> RenderType::solid)
                .blockstate(NonNullBiConsumer.noop())
                .tag(GTToolType.WRENCH.harvestTags.get(0), CustomTags.TOOL_TIERS[1])
                .item(BlockItem::new)
                .build()
                .register();
        GTCEuAPI.CLEANROOM_FILTERS.put(CleanroomFilterType.FILTER_CASING_LAW, filterBlock);
        return filterBlock;
    }

    private static BlockEntry<Block> createGlassCasingBlock(String name, String cn, ResourceLocation texture) {
        return createCasingBlock(name, cn, GTModels.cubeAllModel(name, texture), GlassBlock::new, () -> Blocks.GLASS, () -> RenderType::translucent);
    }

    private static BlockEntry<Block> createCasingBlock(String name, String cn, ResourceLocation texture) {
        return createCasingBlock(name, cn, GTModels.cubeAllModel(name, texture), Block::new, () -> Blocks.IRON_BLOCK,
                () -> RenderType::solid);
    }

    private static BlockEntry<Block> createCustomModelCasingBlock(String name, String cn) {
        return createCasingBlock(name, cn, NonNullBiConsumer.noop(), Block::new, () -> Blocks.IRON_BLOCK,
                () -> RenderType::solid);
    }

    private static BlockEntry<Block> createCasingBlock(String name, String cn, NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateBlockstateProvider> cons,
                                                       NonNullFunction<BlockBehaviour.Properties, Block> blockSupplier,
                                                       NonNullSupplier<? extends Block> properties,
                                                       Supplier<Supplier<RenderType>> type) {
        return block(name, cn, blockSupplier)
                .initialProperties(properties)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(type)
                .blockstate(cons)
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<Block> createSidedCasingBlock(String name, String cn, ResourceLocation texture) {
        return block(name, cn, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::solid)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().cubeBottomTop(name,
                        texture.withSuffix("/side"),
                        texture.withSuffix("/top"),
                        texture.withSuffix("/top"))))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<Block> createStoneBlock(String name, String cn, ResourceLocation texture) {
        return block(name, cn, Block::new)
                .initialProperties(() -> Blocks.STONE)
                .addLayer(() -> RenderType::solid)
                .blockstate(GTModels.cubeAllModel(name, texture))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<Block> createSandBlock(String name, String cn, ResourceLocation texture) {
        return block(name, cn, Block::new)
                .initialProperties(() -> Blocks.SAND)
                .addLayer(() -> RenderType::solid)
                .blockstate(GTModels.cubeAllModel(name, texture))
                .tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<FusionCasingBlock> createFusionCasing(IFusionCasingType casingType, String cn) {
        BlockEntry<FusionCasingBlock> casingBlock = block(casingType.getSerializedName(), cn, p -> (FusionCasingBlock) new FusionCasings(p, casingType))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(properties -> properties.strength(5.0f, 10.0f).sound(SoundType.METAL))
                .addLayer(() -> RenderType::solid)
                .blockstate((ctx, prov) -> {
                    ActiveBlock block = ctx.getEntry();
                    ModelFile inactive = prov.models().getExistingFile(GTOCore.id(casingType.getSerializedName()));
                    ModelFile active = prov.models().getExistingFile(GTOCore.id(casingType.getSerializedName()).withSuffix("_active"));
                    prov.getVariantBuilder(block).partialState().with(ActiveBlock.ACTIVE, false).modelForState().modelFile(inactive).addModel().partialState().with(ActiveBlock.ACTIVE, true).modelForState().modelFile(active).addModel();
                })
                .tag(GTToolType.WRENCH.harvestTags.get(0), CustomTags.TOOL_TIERS[casingType.getHarvestLevel()])
                .item(BlockItem::new)
                .build()
                .register();
        ALL_FUSION_CASINGS.put(casingType, casingBlock);
        return casingBlock;
    }

    private static BlockEntry<Block> createHermeticCasing(int tier) {
        String tierName = GTValues.VN[tier].toLowerCase(Locale.ROOT);
        return block("%s_hermetic_casing".formatted(tierName), "%s密封机械方块".formatted(GTValues.VN[tier]), Block::new)
                .lang("Hermetic Casing %s".formatted(GTValues.LVT[tier]))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::solid)
                .blockstate(NonNullBiConsumer.noop())
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<CoilBlock> createCoilBlock(CoilType coilType) {
        BlockEntry<CoilBlock> coilBlock = block("%s_coil_block".formatted(coilType.getName()), coilType.getCnLang() + "线圈方块", p -> new CoilBlock(p, coilType))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate((ctx, prov) -> {
                    ActiveBlock block = ctx.getEntry();
                    ModelFile inactive = prov.models().getExistingFile(coilType.getTexture());
                    ModelFile active = prov.models().getExistingFile(coilType.getTexture().withSuffix("_bloom"));
                    prov.getVariantBuilder(block).partialState().with(ActiveBlock.ACTIVE, false).modelForState().modelFile(inactive).addModel().partialState().with(ActiveBlock.ACTIVE, true).modelForState().modelFile(active).addModel();
                })
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), coilType.getTexture()))
                .build()
                .register();
        GTCEuAPI.HEATING_COILS.put(coilType, coilBlock);
        return coilBlock;
    }

    private static BlockEntry<WirelessEnergyUnitBlock> createWirelessEnergyUnit(int tier) {
        String t = VN[tier].toLowerCase(Locale.ROOT);
        String name = t + "_wireless_energy_unit";
        return block(name, GTOValues.VNFR[tier] + "无线能量单元", p -> new WirelessEnergyUnitBlock(p, tier))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().cubeAll(name, GTOCore.id("block/casings/wireless_energy_unit/" + t))))
                .tag(GTToolType.WRENCH.harvestTags.get(0))
                .item(BlockItem::new)
                .build()
                .register();
    }

    public static final BlockEntry<NukeBombBlock> NUKE_BOMB = block("nuke_bomb", "核弹", NukeBombBlock::new)
            .properties(p -> p.mapColor(MapColor.FIRE).instabreak().sound(SoundType.GRASS).ignitedByLava())
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cubeBottomTop(ctx.getName(),
                    GTOCore.id("block/nuke_bomb"),
                    RLUtils.mc("block/tnt_bottom"),
                    RLUtils.mc("block/tnt_top"))))
            .simpleItem()
            .register();

    public static final BlockEntry<RotatedPillarBlock> VARIATION_WOOD = block("variation_wood", "变异原木", RotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASS).strength(1000).sound(SoundType.WOOD))
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate(NonNullBiConsumer.noop())
            .item(BlockItem::new)
            .build()
            .register();

    public static final BlockEntry<RotatedPillarBlock> BARNARDA_C_LOG = block("barnarda_c_log", "巴纳德C原木", RotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASS).strength(100).sound(SoundType.WOOD))
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate(NonNullBiConsumer.noop())
            .item(BlockItem::new)
            .build()
            .register();

    public static final BlockEntry<Block> BARNARDA_C_LEAVES = block("barnarda_c_leaves", "巴纳德C树叶", Block::new)
            .properties(p -> p.mapColor(MapColor.PLANT).strength(50).sound(SoundType.GRASS).noOcclusion())
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate(NonNullBiConsumer.noop())
            .item(BlockItem::new)
            .build()
            .register();

    public static final BlockEntry<CreateAggregationeCore> CREATE_AGGREGATIONE_CORE = block("create_aggregatione_core", "创造稳定核心", CreateAggregationeCore::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().cubeAll("create_aggregatione_core", GTOCore.id("block/create_aggregatione_core"))))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item(BlockItem::new)
            .build()
            .register();

    public static final BlockEntry<Block> LAW_FILTER_CASING = createCleanroomFilter();

    public static final BlockEntry<Block> TITAN_STONE = createStoneBlock("titan_stone", "土卫六岩石", GTOCore.id("block/stone/titan_stone"));
    public static final BlockEntry<Block> PLUTO_STONE = createStoneBlock("pluto_stone", "冥王星岩石", GTOCore.id("block/stone/pluto_stone"));
    public static final BlockEntry<Block> IO_STONE = createStoneBlock("io_stone", "木卫一岩石", GTOCore.id("block/stone/io_stone"));
    public static final BlockEntry<Block> GANYMEDE_STONE = createStoneBlock("ganymede_stone", "木卫三岩石", GTOCore.id("block/stone/ganymede_stone"));
    public static final BlockEntry<Block> ENCELADUS_STONE = createStoneBlock("enceladus_stone", "土卫二岩石", GTOCore.id("block/stone/enceladus_stone"));
    public static final BlockEntry<Block> CERES_STONE = createStoneBlock("ceres_stone", "谷神星岩石", GTOCore.id("block/stone/ceres_stone"));
    public static final BlockEntry<Block> ESSENCE_BLOCK = createStoneBlock("essence_block", "精华方块", GTOCore.id("block/essence_block"));
    public static final BlockEntry<Block> REACTOR_CORE = createStoneBlock("reactor_core", "远古反应核", GTOCore.id("block/reactor_core"));
    public static final BlockEntry<Block> COMMAND_BLOCK_BROKEN = createStoneBlock("command_block_broken", "即将打破的命令方块", GTOCore.id("block/command_block_broken"));
    public static final BlockEntry<Block> CHAIN_COMMAND_BLOCK_BROKEN = createStoneBlock("chain_command_block_broken", "即将打破的连锁命令方块", GTOCore.id("block/chain_command_block_broken"));
    public static final BlockEntry<Block> INFUSED_OBSIDIAN = createStoneBlock("infused_obsidian", "注入龙力的黑曜石", GTOCore.id("block/infused_obsidian"));
    public static final BlockEntry<Block> DRACONIUM_BLOCK_CHARGED = createStoneBlock("draconium_block_charged", "充能龙块", GTOCore.id("block/draconium_block_charged"));
    public static final BlockEntry<Block> SHINING_OBSIDIAN = createStoneBlock("shining_obsidian", "发光的黑曜石", GTOCore.id("block/shining_obsidian"));
    public static final BlockEntry<Block> ENDER_OBSIDIAN = createStoneBlock("ender_obsidian", "末影黑曜石", GTOCore.id("block/ender_obsidian"));

    public static final BlockEntry<Block> TITAN_GRUNT = createSandBlock("titan_grunt", "土卫六表皮", GTOCore.id("block/sand/titan_grunt"));
    public static final BlockEntry<Block> PLUTO_GRUNT = createSandBlock("pluto_grunt", "冥王星表皮", GTOCore.id("block/sand/pluto_grunt"));
    public static final BlockEntry<Block> IO_ASH = createSandBlock("io_ash", "木卫一灰烬", GTOCore.id("block/sand/io_ash"));
    public static final BlockEntry<Block> GANYMEDE_GRUNT = createSandBlock("ganymede_grunt", "木卫三表皮", GTOCore.id("block/sand/titan_grunt"));
    public static final BlockEntry<Block> CERES_GRUNT = createSandBlock("ceres_grunt", "谷神星表皮", GTOCore.id("block/sand/ceres_grunt"));

    public static final BlockEntry<Block> SPACE_ELEVATOR_INTERNAL_SUPPORT = createSidedCasingBlock("space_elevator_internal_support", "太空电梯内部支撑结构", GTOCore.id("block/casings/space_elevator_internal_support"));
    public static final BlockEntry<Block> MODULE_BASE = createSidedCasingBlock("module_base", "太空电梯模块基座", GTOCore.id("block/casings/module_base"));
    public static final BlockEntry<Block> MOLECULAR_COIL = createSidedCasingBlock("molecular_coil", "分子线圈", GTOCore.id("block/casings/molecular_coil"));
    public static final BlockEntry<Block> DYSON_RECEIVER_CASING = createSidedCasingBlock("dyson_receiver_casing", "戴森球能量接收基座机械方块", GTOCore.id("block/casings/dyson_receiver_casing"));
    public static final BlockEntry<Block> DYSON_DEPLOYMENT_MAGNET = createSidedCasingBlock("dyson_deployment_magnet", "戴森球模块部署单元超导磁体", GTOCore.id("block/casings/dyson_deployment_magnet"));
    public static final BlockEntry<Block> SPEEDING_PIPE = createSidedCasingBlock("speeding_pipe", "高速管道", GTOCore.id("block/casings/speeding_pipe"));

    public static final BlockEntry<Block> HERMETIC_CASING_UEV = createHermeticCasing(GTValues.UEV);
    public static final BlockEntry<Block> HERMETIC_CASING_UIV = createHermeticCasing(GTValues.UIV);
    public static final BlockEntry<Block> HERMETIC_CASING_UXV = createHermeticCasing(GTValues.UXV);
    public static final BlockEntry<Block> HERMETIC_CASING_OpV = createHermeticCasing(GTValues.OpV);

    public static final BlockEntry<CoilBlock> URUIUM_COIL_BLOCK = createCoilBlock(CoilType.URUIUM);
    public static final BlockEntry<CoilBlock> ABYSSALALLOY_COIL_BLOCK = createCoilBlock(CoilType.ABYSSALALLOY);
    public static final BlockEntry<CoilBlock> TITANSTEEL_COIL_BLOCK = createCoilBlock(CoilType.TITANSTEEL);
    public static final BlockEntry<CoilBlock> ADAMANTINE_COIL_BLOCK = createCoilBlock(CoilType.ADAMANTINE);
    public static final BlockEntry<CoilBlock> NAQUADRIATICTARANIUM_COIL_BLOCK = createCoilBlock(CoilType.NAQUADRIATICTARANIUM);
    public static final BlockEntry<CoilBlock> STARMETAL_COIL_BLOCK = createCoilBlock(CoilType.STARMETAL);
    public static final BlockEntry<CoilBlock> INFINITY_COIL_BLOCK = createCoilBlock(CoilType.INFINITY);
    public static final BlockEntry<CoilBlock> HYPOGEN_COIL_BLOCK = createCoilBlock(CoilType.HYPOGEN);
    public static final BlockEntry<CoilBlock> ETERNITY_COIL_BLOCK = createCoilBlock(CoilType.ETERNITY);

    public static final BlockEntry<WirelessEnergyUnitBlock> LV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.LV);
    public static final BlockEntry<WirelessEnergyUnitBlock> MV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.MV);
    public static final BlockEntry<WirelessEnergyUnitBlock> HV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.HV);
    public static final BlockEntry<WirelessEnergyUnitBlock> EV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.EV);
    public static final BlockEntry<WirelessEnergyUnitBlock> IV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.IV);
    public static final BlockEntry<WirelessEnergyUnitBlock> LUV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.LuV);
    public static final BlockEntry<WirelessEnergyUnitBlock> ZPM_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.ZPM);
    public static final BlockEntry<WirelessEnergyUnitBlock> UV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UV);
    public static final BlockEntry<WirelessEnergyUnitBlock> UHV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UHV);
    public static final BlockEntry<WirelessEnergyUnitBlock> UEV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UEV);
    public static final BlockEntry<WirelessEnergyUnitBlock> UIV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UIV);
    public static final BlockEntry<WirelessEnergyUnitBlock> UXV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UXV);
    public static final BlockEntry<WirelessEnergyUnitBlock> OPV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.OpV);
    public static final BlockEntry<WirelessEnergyUnitBlock> MAX_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.MAX);

    public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK4 = createFusionCasing(FusionCasings.CasingType.FUSION_CASING_MK4, "聚变机械方块 MK-IV");
    public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK5 = createFusionCasing(FusionCasings.CasingType.FUSION_CASING_MK5, "聚变机械方块 MK-V");

    public static final BlockEntry<ActiveBlock> ADVANCED_FUSION_COIL = createActiveCasing("advanced_fusion_coil", "进阶聚变线圈方块", "block/variant/advanced_fusion_coil");
    public static final BlockEntry<ActiveBlock> FUSION_COIL_MK2 = createActiveCasing("fusion_coil_mk2", "聚变线圈方块MK-II", "block/variant/fusion_coil_mk2");
    public static final BlockEntry<ActiveBlock> IMPROVED_SUPERCONDUCTOR_COIL = createActiveCasing("improved_superconductor_coil", "改良型超导线圈方块", "block/variant/improved_superconductor_coil");
    public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL = createActiveCasing("compressed_fusion_coil", "压缩聚变线圈方块", "block/variant/compressed_fusion_coil");
    public static final BlockEntry<ActiveBlock> ADVANCED_COMPRESSED_FUSION_COIL = createActiveCasing("advanced_compressed_fusion_coil", "进阶压缩聚变线圈方块", "block/variant/advanced_compressed_fusion_coil");
    public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2_PROTOTYPE = createActiveCasing("compressed_fusion_coil_mk2_prototype", "压缩聚变线圈方块MK-II原型", "block/variant/compressed_fusion_coil_mk2_prototype");
    public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2 = createActiveCasing("compressed_fusion_coil_mk2", "压缩聚变线圈方块MK-II", "block/variant/compressed_fusion_coil_mk2");

    public static final BlockEntry<ActiveBlock> POWER_CORE = createActiveCasing("power_core", "太空电梯动力核心", "block/variant/power_core");
    public static final BlockEntry<ActiveBlock> HYPER_CORE = createActiveCasing("hyper_core", "超能核心", "block/variant/hyper_core");
    public static final BlockEntry<ActiveBlock> SUPER_COMPUTATION_COMPONENT = createActiveCasing("super_computation_component", "超级计算机组件", "block/variant/super_computation_component");
    public static final BlockEntry<ActiveBlock> SUPER_COOLER_COMPONENT = createActiveCasing("super_cooler_component", "超级冷却组件", "block/variant/super_cooler_component");
    public static final BlockEntry<ActiveBlock> SPACETIMECONTINUUMRIPPER = createActiveCasing("spacetimecontinuumripper", "时空连续体撕裂方块", "block/variant/spacetimecontinuumripper");
    public static final BlockEntry<ActiveBlock> SPACETIMEBENDINGCORE = createActiveCasing("spacetimebendingcore", "时空扭曲核心方块", "block/variant/spacetimebendingcore");
    public static final BlockEntry<ActiveBlock> QUANTUM_FORCE_TRANSFORMER_COIL = createActiveCasing("quantum_force_transformer_coil", "量子操纵者线圈方块", "block/variant/quantum_force_transformer_coil");
    public static final BlockEntry<ActiveBlock> FISSION_FUEL_COMPONENT = createActiveCasing("fission_fuel_component", "裂变燃料组件", "block/variant/fission_fuel_component");
    public static final BlockEntry<ActiveBlock> FISSION_COOLER_COMPONENT = createActiveCasing("fission_cooler_component", "裂变冷却组件", "block/variant/fission_cooler_component");
    public static final BlockEntry<ActiveBlock> ADVANCED_ASSEMBLY_LINE_UNIT = createActiveCasing("advanced_assembly_line_unit", "进阶装配线控制外壳", "block/variant/advanced_assembly_line_unit");
    public static final BlockEntry<ActiveBlock> SPACE_ELEVATOR_SUPPORT = createActiveCasing("space_elevator_support", "太空电梯支撑结构", "block/variant/space_elevator_support");
    public static final BlockEntry<ActiveBlock> MAGIC_CORE = createActiveCasing("magic_core", "魔法核心", "block/variant/magic_core");

    public static final BlockEntry<Block> ENERGETIC_PHOTOVOLTAIC_BLOCK = createCustomModelCasingBlock("energetic_photovoltaic_block", "充能光伏方块");
    public static final BlockEntry<Block> PULSATING_PHOTOVOLTAIC_BLOCK = createCustomModelCasingBlock("pulsating_photovoltaic_block", "脉冲光伏方块");
    public static final BlockEntry<Block> VIBRANT_PHOTOVOLTAIC_BLOCK = createCustomModelCasingBlock("vibrant_photovoltaic_block", "振动光伏方块");

    public static final BlockEntry<Block> STELLAR_CONTAINMENT_CASING = createTierCasings("stellar_containment_casing", "基础恒星热力容器", GTOCore.id("block/stellar_containment_casing"), SCMAP, 1);
    public static final BlockEntry<Block> ADVANCED_STELLAR_CONTAINMENT_CASING = createTierCasings("advanced_stellar_containment_casing", "高级恒星热力容器", GTOCore.id("block/stellar_containment_casing"), SCMAP, 2);
    public static final BlockEntry<Block> ULTIMATE_STELLAR_CONTAINMENT_CASING = createTierCasings("ultimate_stellar_containment_casing", "终极恒星热力容器", GTOCore.id("block/stellar_containment_casing"), SCMAP, 3);

    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_LV = createTierCasings("component_assembly_line_casing_lv", "部件装配线外壳(LV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_lv"), CALMAP, 1);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_MV = createTierCasings("component_assembly_line_casing_mv", "部件装配线外壳(MV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_mv"), CALMAP, 2);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_HV = createTierCasings("component_assembly_line_casing_hv", "部件装配线外壳(HV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_hv"), CALMAP, 3);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_EV = createTierCasings("component_assembly_line_casing_ev", "部件装配线外壳(EV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_ev"), CALMAP, 4);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_IV = createTierCasings("component_assembly_line_casing_iv", "部件装配线外壳(IV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_iv"), CALMAP, 5);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_LUV = createTierCasings("component_assembly_line_casing_luv", "部件装配线外壳(LuV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_luv"), CALMAP, 6);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_ZPM = createTierCasings("component_assembly_line_casing_zpm", "部件装配线外壳(ZPM)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_zpm"), CALMAP, 7);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UV = createTierCasings("component_assembly_line_casing_uv", "部件装配线外壳(UV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uv"), CALMAP, 8);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UHV = createTierCasings("component_assembly_line_casing_uhv", "部件装配线外壳(UHV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uhv"), CALMAP, 9);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UEV = createTierCasings("component_assembly_line_casing_uev", "部件装配线外壳(UEV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uev"), CALMAP, 10);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UIV = createTierCasings("component_assembly_line_casing_uiv", "部件装配线外壳(UIV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uiv"), CALMAP, 11);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UXV = createTierCasings("component_assembly_line_casing_uxv", "部件装配线外壳(UXV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uxv"), CALMAP, 12);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_OPV = createTierCasings("component_assembly_line_casing_opv", "部件装配线外壳(OPV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_opv"), CALMAP, 13);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_MAX = createTierCasings("component_assembly_line_casing_max", "部件装配线外壳(MAX)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_max"), CALMAP, 14);

    public static final BlockEntry<Block> REMOTE_GRAVITON_FLOW_MODULATOR = createTierCasings("remote_graviton_flow_modulator", "远程引力流调节器", GTOCore.id("block/remote_graviton_flow_modulator"), GRAVITONFLOWMAP, 1);
    public static final BlockEntry<Block> MEDIAL_GRAVITON_FLOW_MODULATOR = createTierCasings("medial_graviton_flow_modulator", "中介引力流调节器", GTOCore.id("block/medial_graviton_flow_modulator"), GRAVITONFLOWMAP, 2);
    public static final BlockEntry<Block> CENTRAL_GRAVITON_FLOW_MODULATOR = createTierCasings("central_graviton_flow_modulator", "中心引力流调节器", GTOCore.id("block/central_graviton_flow_modulator"), GRAVITONFLOWMAP, 3);

    public static final BlockEntry<ActiveBlock> POWER_MODULE = createActiveTierCasing("power_module", "太空电梯动力模块MK1", "block/variant/power_module", SEPMMAP, 1);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_2 = createActiveTierCasing("power_module_2", "太空电梯动力模块MK2", "block/variant/power_module", SEPMMAP, 2);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_3 = createActiveTierCasing("power_module_3", "太空电梯动力模块MK3", "block/variant/power_module", SEPMMAP, 3);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_4 = createActiveTierCasing("power_module_4", "太空电梯动力模块MK4", "block/variant/power_module", SEPMMAP, 4);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_5 = createActiveTierCasing("power_module_5", "太空电梯动力模块MK5", "block/variant/power_module", SEPMMAP, 5);

    public static final BlockEntry<Block> BOROSILICATE_GLASS = createTierGlassCasings("borosilicate_glass", "硼硅玻璃", GTOCore.id("block/casings/borosilicate_glass"), GTValues.HV);
    public static final BlockEntry<Block> TITANIUM_BOROSILICATE_GLASS = createTierGlassCasings("titanium_borosilicate_glass", "钛强化硼玻璃", GTOCore.id("block/casings/titanium_borosilicate_glass"), GTValues.EV);
    public static final BlockEntry<Block> TUNGSTEN_BOROSILICATE_GLASS = createTierGlassCasings("tungsten_borosilicate_glass", "钨强化硼玻璃", GTOCore.id("block/casings/tungsten_borosilicate_glass"), GTValues.IV);
    public static final BlockEntry<Block> HSSS_BOROSILICATE_GLASS = createTierGlassCasings("hsss_borosilicate_glass", "高速钢-S强化硼玻璃", GTOCore.id("block/casings/hsss_borosilicate_glass"), GTValues.LuV);
    public static final BlockEntry<Block> NAQUADAH_BOROSILICATE_GLASS = createTierGlassCasings("naquadah_borosilicate_glass", "硅岩强化硼玻璃", GTOCore.id("block/casings/naquadah_borosilicate_glass"), GTValues.ZPM);
    public static final BlockEntry<Block> TRITANIUM_BOROSILICATE_GLASS = createTierGlassCasings("tritanium_borosilicate_glass", "三钛强化硼玻璃", GTOCore.id("block/casings/tritanium_borosilicate_glass"), GTValues.UV);
    public static final BlockEntry<Block> NEUTRONIUM_BOROSILICATE_GLASS = createTierGlassCasings("neutronium_borosilicate_glass", "中子强化硼玻璃", GTOCore.id("block/casings/neutronium_borosilicate_glass"), GTValues.UHV);
    public static final BlockEntry<Block> ENDERIUM_BOROSILICATE_GLASS = createTierGlassCasings("enderium_borosilicate_glass", "末影强化硼玻璃", GTOCore.id("block/casings/enderium_borosilicate_glass"), GTValues.UEV);
    public static final BlockEntry<Block> TARANIUM_BOROSILICATE_GLASS = createTierGlassCasings("taranium_borosilicate_glass", "塔兰强化硼玻璃", GTOCore.id("block/casings/taranium_borosilicate_glass"), GTValues.UIV);
    public static final BlockEntry<Block> QUARKS_BOROSILICATE_GLASS = createTierGlassCasings("quarks_borosilicate_glass", "夸克强化硼玻璃", GTOCore.id("block/casings/quarks_borosilicate_glass"), GTValues.UXV);
    public static final BlockEntry<Block> DRACONIUM_BOROSILICATE_GLASS = createTierGlassCasings("draconium_borosilicate_glass", "龙强化硼玻璃", GTOCore.id("block/casings/draconium_borosilicate_glass"), GTValues.OpV);
    public static final BlockEntry<Block> COSMIC_NEUTRONIUM_BOROSILICATE_GLASS = createTierGlassCasings("cosmic_neutronium_borosilicate_glass", "宇宙中子强化硼玻璃", GTOCore.id("block/casings/cosmic_neutronium_borosilicate_glass"), GTValues.MAX);

    public static final BlockEntry<Block> INFINITY_GLASS = createGlassCasingBlock("infinity_glass", "无尽强化玻璃", GTOCore.id("block/casings/infinity_glass"));
    public static final BlockEntry<Block> RHENIUM_REINFORCED_ENERGY_GLASS = createGlassCasingBlock("rhenium_reinforced_energy_glass", "铼强化聚能玻璃", GTOCore.id("block/casings/rhenium_reinforced_energy_glass"));
    public static final BlockEntry<Block> ELECTRON_PERMEABLE_NEUTRONIUM_COATED_GLASS = createGlassCasingBlock("electron_permeable_neutronium_coated_glass", "电子渗透中子素涂层玻璃", GTOCore.id("block/casings/electron_permeable_neutronium_coated_glass"));
    public static final BlockEntry<Block> NON_PHOTONIC_MATTER_EXCLUSION_GLASS = createGlassCasingBlock("non_photonic_matter_exclusion_glass", "非光子物质排除玻璃", GTOCore.id("block/casings/non_photonic_matter_exclusion_glass"));
    public static final BlockEntry<Block> OMNI_PURPOSE_INFINITY_FUSED_GLASS = createGlassCasingBlock("omni_purpose_infinity_fused_glass", "全能无限融合玻璃", GTOCore.id("block/casings/omni_purpose_infinity_fused_glass"));
    public static final BlockEntry<Block> HAWKING_RADIATION_REALIGNMENT_FOCUS = createGlassCasingBlock("hawking_radiation_realignment_focus", "霍金辐射重新调整焦点", GTOCore.id("block/casings/hawking_radiation_realignment_focus"));
    public static final BlockEntry<Block> CHEMICAL_GRADE_GLASS = createGlassCasingBlock("chemical_grade_glass", "化学级玻璃", GTOCore.id("block/casings/chemical_grade_glass"));
    public static final BlockEntry<Block> ANTIMATTER_CONTAINMENT_CASING = createGlassCasingBlock("antimatter_containment_casing", "反物质隔离机械方块", GTOCore.id("block/casings/antimatter_containment_casing"));
    public static final BlockEntry<Block> QUANTUM_GLASS = createGlassCasingBlock("quantum_glass", "量子玻璃", GTOCore.id("block/casings/quantum_glass"));

    public static final BlockEntry<Block> FORCE_FIELD_GLASS = createGlassCasingBlock("force_field_glass", "力场玻璃", GTOCore.id("block/force_field_glass"));
    public static final BlockEntry<Block> SPATIALLY_TRANSCENDENT_GRAVITATIONAL_LENS_BLOCK = createGlassCasingBlock("spatially_transcendent_gravitational_lens_block", "超空间引力透镜块", GTOCore.id("block/spatially_transcendent_gravitational_lens_block"));

    public static final BlockEntry<Block> NAQUADRIA_CHARGE = createCasingBlock("naquadria_charge", "超能硅岩爆弹", GTOCore.id("block/naquadria_charge"));
    public static final BlockEntry<Block> LEPTONIC_CHARGE = createCasingBlock("leptonic_charge", "轻子爆弹", GTOCore.id("block/leptonic_charge"));
    public static final BlockEntry<Block> QUANTUM_CHROMODYNAMIC_CHARGE = createCasingBlock("quantum_chromodynamic_charge", "量子色动力学爆弹", GTOCore.id("block/quantum_chromodynamic_charge"));
    public static final BlockEntry<Block> ANNIHILATE_CORE = createCasingBlock("annihilate_core", "湮灭核心", GTOCore.id("block/annihilate_core"));
    public static final BlockEntry<Block> NEUTRONIUM_PIPE_CASING = createCasingBlock("neutronium_pipe_casing", "中子素管道方块", GTOCore.id("block/neutronium_pipe_casing"));
    public static final BlockEntry<Block> INCONEL_625_PIPE = createCasingBlock("inconel_625_pipe", "镍铬基合金-625温和分散管道", GTOCore.id("block/inconel_625_pipe"));
    public static final BlockEntry<Block> HASTELLOY_N_75_PIPE = createCasingBlock("hastelloy_n_75_pipe", "哈斯特洛依合金-N75油膜管道", GTOCore.id("block/hastelloy_n_75_pipe"));
    public static final BlockEntry<Block> NEUTRONIUM_GEARBOX = createCasingBlock("neutronium_gearbox", "中子素齿轮箱机械方块", GTOCore.id("block/neutronium_gearbox"));
    public static final BlockEntry<Block> INCONEL_625_GEARBOX = createCasingBlock("inconel_625_gearbox", "镍铬基合金-625球磨齿轮箱", GTOCore.id("block/inconel_625_gearbox"));
    public static final BlockEntry<Block> HASTELLOY_N_75_GEARBOX = createCasingBlock("hastelloy_n_75_gearbox", "哈斯特洛依合金-N75齿轮箱", GTOCore.id("block/hastelloy_n_75_gearbox"));
    public static final BlockEntry<Block> LASER_COOLING_CASING = createCasingBlock("laser_cooling_casing", "激光冷却方块", GTOCore.id("block/laser_cooling_casing"));
    public static final BlockEntry<Block> SPACETIME_COMPRESSION_FIELD_GENERATOR = createCasingBlock("spacetime_compression_field_generator", "压缩时空力场发生器", GTOCore.id("block/spacetime_compression_field_generator"));
    public static final BlockEntry<Block> DIMENSIONAL_BRIDGE_CASING = createCasingBlock("dimensional_bridge_casing", "维度桥接方块", GTOCore.id("block/dimensional_bridge_casing"));
    public static final BlockEntry<Block> DIMENSIONAL_STABILITY_CASING = createCasingBlock("dimensional_stability_casing", "维度稳定方块", GTOCore.id("block/dimensional_stability_casing"));
    public static final BlockEntry<Block> MACHINE_CASING_CIRCUIT_ASSEMBLY_LINE = createCasingBlock("machine_casing_circuit_assembly_line", "电路装配线控制外壳", GTOCore.id("block/machine_casing_circuit_assembly_line"));
    public static final BlockEntry<Block> HIGH_STRENGTH_CONCRETE = createCasingBlock("high_strength_concrete", "高强度混凝土", GTOCore.id("block/casings/module_base/side"));
    public static final BlockEntry<Block> AGGREGATIONE_CORE = createCasingBlock("aggregatione_core", "聚合核心", GTOCore.id("block/aggregatione_core"));
    public static final BlockEntry<Block> ACCELERATED_PIPELINE = createCasingBlock("accelerated_pipeline", "加速管道", GTOCore.id("block/accelerated_pipeline"));
    public static final BlockEntry<Block> MODULE_CONNECTOR = createCasingBlock("module_connector", "太空电梯模块连接器", GTOCore.id("block/module_connector"));
    public static final BlockEntry<Block> DIMENSION_CREATION_CASING = createCasingBlock("dimension_creation_casing", "维度创造机械方块", GTOCore.id("block/dimension_creation_casing"));
    public static final BlockEntry<Block> MACHINE_CASING_GRINDING_HEAD = createCasingBlock("machine_casing_grinding_head", "坚固钻头", GTOCore.id("block/machine_casing_grinding_head"));
    public static final BlockEntry<Block> CREATE_HPCA_COMPONENT = createCasingBlock("create_hpca_component", "创造计算组件", GTOCore.id("block/create_hpca_component"));
    public static final BlockEntry<Block> SPACETIME_ASSEMBLY_LINE_UNIT = createCasingBlock("spacetime_assembly_line_unit", "超时空装配单元", GTOCore.id("block/spacetime_assembly_line_unit"));
    public static final BlockEntry<Block> SPACETIME_ASSEMBLY_LINE_CASING = createCasingBlock("spacetime_assembly_line_casing", "超时空装配外壳", GTOCore.id("block/spacetime_assembly_line_casing"));
    public static final BlockEntry<Block> HOLLOW_CASING = createCasingBlock("hollow_casing", "中空机械方块", GTOCore.id("block/hollow_casing"));
    public static final BlockEntry<Block> CONTAINMENT_FIELD_GENERATOR = createCasingBlock("containment_field_generator", "遏制场发生器", GTOCore.id("block/containment_field_generator"));
    public static final BlockEntry<Block> DYSON_CONTROL_CASING = createCasingBlock("dyson_control_casing", "戴森球控制中心基座机械方块", GTOCore.id("block/space_elevator_mechanical_casing"));
    public static final BlockEntry<Block> DYSON_CONTROL_TOROID = createCasingBlock("dyson_control_toroid", "戴森球控制中心环形机械方块", GTOCore.id("block/dyson_control_toroid"));
    public static final BlockEntry<Block> DYSON_DEPLOYMENT_CASING = createCasingBlock("dyson_deployment_casing", "戴森球模块部署单元基座机械方块", GTOCore.id("block/dyson_deployment_casing"));
    public static final BlockEntry<Block> DYSON_DEPLOYMENT_CORE = createCasingBlock("dyson_deployment_core", "戴森球模块部署单元核心", GTOCore.id("block/dyson_deployment_core"));
    public static final BlockEntry<Block> STEAM_ASSEMBLY_BLOCK = createCasingBlock("steam_assembly_block", "蒸汽装配方块", GTOCore.id("block/steam_assembly_block"));
    public static final BlockEntry<Block> RESTRAINT_DEVICE = createCasingBlock("restraint_device", "力场约束装置", GTOCore.id("block/restraint_device"));
    public static final BlockEntry<Block> FLOTATION_CELL = createCasingBlock("flotation_cell", "浮选矿池单元", GTOCore.id("block/flotation_cell"));

    public static final BlockEntry<Block> SPACE_ELEVATOR_MECHANICAL_CASING = createCasingBlock("space_elevator_mechanical_casing", "太空电梯机械方块", GTOCore.id("block/space_elevator_mechanical_casing"));
    public static final BlockEntry<Block> MANIPULATOR = createCasingBlock("manipulator", "量子操纵者机械方块", GTOCore.id("block/manipulator"));

    public static final BlockEntry<Block> CREATE_CASING = createCasingBlock("create_casing", "创造机械方块", GTOCore.id("block/casings/create_casing"));
    public static final BlockEntry<Block> SUPERCRITICAL_TURBINE_CASING = createCasingBlock("supercritical_turbine_casing", "超临界涡轮机械方块", GTOCore.id("block/casings/supercritical_turbine_casing"));
    public static final BlockEntry<Block> BLAZE_CASING = createCasingBlock("blaze_casing", "烈焰机械方块", GTOCore.id("block/casings/blaze_casing"));
    public static final BlockEntry<Block> COLD_ICE_CASING = createCasingBlock("cold_ice_casing", "寒冰机械方块", GTOCore.id("block/casings/cold_ice_casing"));
    public static final BlockEntry<Block> RED_STEEL_CASING = createCasingBlock("red_steel_casing", "高气密红钢机器外壳", GTOCore.id("block/casings/red_steel_casing"));
    public static final BlockEntry<Block> MOLECULAR_CASING = createCasingBlock("molecular_casing", "分子机械方块", GTOCore.id("block/casings/molecular_casing"));
    public static final BlockEntry<Block> MULTI_FUNCTIONAL_CASING = createCasingBlock("multi_functional_casing", "多功能机械方块", GTOCore.id("block/casings/multi_functional_casing"));
    public static final BlockEntry<Block> INCONEL_625_CASING = createCasingBlock("inconel_625_casing", "防震镍铬基合金-625机器外壳", GTOCore.id("block/casings/inconel_625_casing"));
    public static final BlockEntry<Block> HASTELLOY_N_75_CASING = createCasingBlock("hastelloy_n_75_casing", "哈斯特洛依合金-N75防水机器外壳", GTOCore.id("block/casings/hastelloy_n_75_casing"));
    public static final BlockEntry<Block> DIMENSION_CONNECTION_CASING = createCasingBlock("dimension_connection_casing", "维度连接机械方块", GTOCore.id("block/casings/dimension_connection_casing"));
    public static final BlockEntry<Block> DIMENSION_INJECTION_CASING = createCasingBlock("dimension_injection_casing", "维度注入机械方块", GTOCore.id("block/casings/dimension_injection_casing"));
    public static final BlockEntry<Block> DIMENSIONALLY_TRANSCENDENT_CASING = createCasingBlock("dimensionally_transcendent_casing", "超维度机械方块", GTOCore.id("block/casings/dimensionally_transcendent_casing"));
    public static final BlockEntry<Block> ECHO_CASING = createCasingBlock("echo_casing", "回响强化机械方块", GTOCore.id("block/casings/echo_casing"));
    public static final BlockEntry<Block> DRAGON_STRENGTH_TRITANIUM_CASING = createCasingBlock("dragon_strength_tritanium_casing", "龙之力量三钛合金机械方块", GTOCore.id("block/casings/extreme_strength_tritanium_casing"));
    public static final BlockEntry<Block> ALUMINIUM_BRONZE_CASING = createCasingBlock("aluminium_bronze_casing", "铝青铜机械方块", GTOCore.id("block/casings/aluminium_bronze_casing"));
    public static final BlockEntry<Block> ANTIFREEZE_HEATPROOF_MACHINE_CASING = createCasingBlock("antifreeze_heatproof_machine_casing", "防冻隔热机械方块", GTOCore.id("block/casings/antifreeze_heatproof_machine_casing"));
    public static final BlockEntry<Block> ENHANCE_HYPER_MECHANICAL_CASING = createCasingBlock("enhance_hyper_mechanical_casing", "强化超能机械方块", GTOCore.id("block/casings/enhance_hyper_mechanical_casing"));
    public static final BlockEntry<Block> EXTREME_STRENGTH_TRITANIUM_CASING = createCasingBlock("extreme_strength_tritanium_casing", "极限强度三钛合金机械方块", GTOCore.id("block/casings/extreme_strength_tritanium_casing"));
    public static final BlockEntry<Block> GRAVITON_FIELD_CONSTRAINT_CASING = createCasingBlock("graviton_field_constraint_casing", "引力场约束方块", GTOCore.id("block/casings/graviton_field_constraint_casing"));
    public static final BlockEntry<Block> HYPER_MECHANICAL_CASING = createCasingBlock("hyper_mechanical_casing", "超能机械方块", GTOCore.id("block/casings/hyper_mechanical_casing"));
    public static final BlockEntry<Block> IRIDIUM_CASING = createCasingBlock("iridium_casing", "铱强化机械方块", GTOCore.id("block/casings/iridium_casing"));
    public static final BlockEntry<Block> LAFIUM_MECHANICAL_CASING = createCasingBlock("lafium_mechanical_casing", "路菲恩机械方块", GTOCore.id("block/casings/lafium_mechanical_casing"));
    public static final BlockEntry<Block> OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING = createCasingBlock("oxidation_resistant_hastelloy_n_mechanical_casing", "抗氧化哈斯特洛伊合金-N机械方块", GTOCore.id("block/casings/oxidation_resistant_hastelloy_n_mechanical_casing"));
    public static final BlockEntry<Block> PIKYONIUM_MACHINE_CASING = createCasingBlock("pikyonium_machine_casing", "皮卡优机械方块", GTOCore.id("block/casings/pikyonium_machine_casing"));
    public static final BlockEntry<Block> SPS_CASING = createCasingBlock("sps_casing", "超临界外壳", GTOCore.id("block/casings/sps_casing"));
    public static final BlockEntry<Block> NAQUADAH_ALLOY_CASING = createCasingBlock("naquadah_alloy_casing", "硅岩合金机械外壳", GTOCore.id("block/casings/hyper_mechanical_casing"));
    public static final BlockEntry<Block> PROCESS_MACHINE_CASING = createCasingBlock("process_machine_casing", "处理机械方块", GTOCore.id("block/casings/process_machine_casing"));
    public static final BlockEntry<Block> FISSION_REACTOR_CASING = createCasingBlock("fission_reactor_casing", "裂变反应堆外壳", GTOCore.id("block/casings/fission_reactor_casing"));
    public static final BlockEntry<Block> DEGENERATE_RHENIUM_CONSTRAINED_CASING = createCasingBlock("degenerate_rhenium_constrained_casing", "简并态铼约束外壳", GTOCore.id("block/casings/degenerate_rhenium_constrained_casing"));
    public static final BlockEntry<Block> PRESSURE_CONTAINMENT_CASING = createCasingBlock("pressure_containment_casing", "压力容器机械方块", GTOCore.id("block/casings/pressure_containment_casing"));
    public static final BlockEntry<Block> AWAKENED_DRACONIUM_CASING = createCasingBlock("awakened_draconium_casing", "觉醒龙外壳", GTOCore.id("block/casings/awakened_draconium_casing"));
    public static final BlockEntry<Block> MAGTECH_CASING = createCasingBlock("magtech_casing", "磁力机械方块", GTOCore.id("block/casings/magtech_casing"));
    public static final BlockEntry<Block> STERILE_CASING = createCasingBlock("sterile_casing", "黄铜加固木制机械方块", GTOCore.id("block/casings/sterile_casing"));
    public static final BlockEntry<Block> COMPRESSOR_CONTROLLER_CASING = createCasingBlock("compressor_controller_casing", "压缩控制机械方块", GTOCore.id("block/casings/compressor_controller_casing"));
    public static final BlockEntry<Block> QUARK_EXCLUSION_CASING = createCasingBlock("quark_exclusion_casing", "夸克排斥机械方块", GTOCore.id("block/casings/quark_exclusion_casing"));
    public static final BlockEntry<Block> NAQUADAH_REINFORCED_PLANT_CASING = createCasingBlock("naquadah_reinforced_plant_casing", "硅岩增强处理机械方块", GTOCore.id("block/casings/naquadah_reinforced_plant_casing"));
    public static final BlockEntry<Block> BOUNDLESS_GRAVITATIONALLY_SEVERED_STRUCTURE_CASING = createCasingBlock("boundless_gravitationally_severed_structure_casing", "无边重力切割结构机械方块", GTOCore.id("block/casings/boundless_gravitationally_severed_structure_casing"));
    public static final BlockEntry<Block> CELESTIAL_MATTER_GUIDANCE_CASING = createCasingBlock("celestial_matter_guidance_casing", "天体物质引导机械方块", GTOCore.id("block/casings/celestial_matter_guidance_casing"));
    public static final BlockEntry<Block> SINGULARITY_REINFORCED_STELLAR_SHIELDING_CASING = createCasingBlock("singularity_reinforced_stellar_shielding_casing", "奇点增强恒星屏蔽机械方块", GTOCore.id("block/casings/singularity_reinforced_stellar_shielding_casing"));

    public static final BlockEntry<Block> STELLAR_ENERGY_SIPHON_CASING = createCasingBlock("stellar_energy_siphon_casing", "恒星能量汲取机械方块", GTOCore.id("block/stellar_energy_siphon_casing"));
    public static final BlockEntry<Block> TRANSCENDENTALLY_AMPLIFIED_MAGNETIC_CONFINEMENT_CASING = createCasingBlock("transcendentally_amplified_magnetic_confinement_casing", "超强放大磁约束机械方块", GTOCore.id("block/transcendentally_amplified_magnetic_confinement_casing"));
    public static final BlockEntry<Block> ANTIMATTER_ANNIHILATION_MATRIX = createCasingBlock("antimatter_annihilation_matrix", "反物质湮灭矩阵", GTOCore.id("block/antimatter_annihilation_matrix"));
    public static final BlockEntry<Block> COMPRESSOR_PIPE_CASING = createCasingBlock("compressor_pipe_casing", "压缩管道机械方块", GTOCore.id("block/compressor_pipe_casing"));
    public static final BlockEntry<Block> EXTREME_DENSITY_CASING = createCasingBlock("extreme_density_casing", "极密机械方块", GTOCore.id("block/extreme_density_casing"));
    public static final BlockEntry<Block> FLOCCULATION_CASING = createCasingBlock("flocculation_casing", "光滑无菌絮凝机械方块", GTOCore.id("block/flocculation_casing"));
    public static final BlockEntry<Block> GRAVITY_STABILIZATION_CASING = createCasingBlock("gravity_stabilization_casing", "重力稳定机械方块", GTOCore.id("block/gravity_stabilization_casing"));
    public static final BlockEntry<Block> HIGH_PRESSURE_RESISTANT_CASING = createCasingBlock("high_pressure_resistant_casing", "高能耐耐机械方块", GTOCore.id("block/high_pressure_resistant_casing"));
    public static final BlockEntry<Block> LASER_CASING = createCasingBlock("laser_casing", "激光机械方块", GTOCore.id("block/laser_casing"));
    public static final BlockEntry<Block> NEUTRONIUM_CASING = createCasingBlock("neutronium_casing", "中子素机械方块", GTOCore.id("block/neutronium_casing"));
    public static final BlockEntry<Block> OZONE_CASING = createCasingBlock("ozone_casing", "臭氧机械方块", GTOCore.id("block/ozone_casing"));
    public static final BlockEntry<Block> PLASMA_HEATER_CASING = createCasingBlock("plasma_heater_casing", "等离子加热机械方块", GTOCore.id("block/plasma_heater_casing"));
    public static final BlockEntry<Block> PROTOMATTER_ACTIVATION_COIL = createCasingBlock("protomatter_activation_coil", "元始物质活化线圈", GTOCore.id("block/protomatter_activation_coil"));
    public static final BlockEntry<Block> RADIATION_ABSORBENT_CASING = createCasingBlock("radiation_absorbent_casing", "辐射吸收机械方块", GTOCore.id("block/radiation_absorbent_casing"));
    public static final BlockEntry<Block> REINFORCED_WOOD_CASING = createSidedCasingBlock("reinforced_wood_casing", "增强木制机械方块", GTOCore.id("block/casings/reinforced_wood_casing"));
    public static final BlockEntry<Block> SHIELDED_ACCELERATOR = createCasingBlock("shielded_accelerator", "屏蔽加速器机械方块", GTOCore.id("block/shielded_accelerator"));
    public static final BlockEntry<Block> NEUTRONIUM_ACTIVE_CASING = createCasingBlock("neutronium_active_casing", "中子素活性机械方块", GTOCore.id("block/neutronium_active_casing"));
    public static final BlockEntry<Block> QUARK_PIPE = createCasingBlock("quark_pipe", "夸克管道", GTOCore.id("block/quark_pipe"));
    public static final BlockEntry<Block> INERT_NEUTRALIZATION_WATER_PLANT_CASING = createCasingBlock("inert_neutralization_water_plant_casing", "惰性中和水处理机械方块", GTOCore.id("block/inert_neutralization_water_plant_casing"));
    public static final BlockEntry<Block> HIGH_ENERGY_ULTRAVIOLET_EMITTER_CASING = createCasingBlock("high_energy_ultraviolet_emitter_casing", "高能紫外线发射器机械方块", GTOCore.id("block/high_energy_ultraviolet_emitter_casing"));
    public static final BlockEntry<Block> REINFORCED_STERILE_WATER_PLANT_CASING = createCasingBlock("reinforced_sterile_water_plant_casing", "加固无菌水处理机械方块", GTOCore.id("block/reinforced_sterile_water_plant_casing"));
    public static final BlockEntry<Block> NEUTRONIUM_STABLE_CASING = createCasingBlock("neutronium_stable_casing", "中子稳定机械方块", GTOCore.id("block/neutronium_stable_casing"));
    public static final BlockEntry<Block> STERILE_WATER_PLANT_CASING = createCasingBlock("sterile_water_plant_casing", "无菌水处理机械方块", GTOCore.id("block/sterile_water_plant_casing"));
    public static final BlockEntry<Block> STABILIZED_NAQUADAH_WATER_PLANT_CASING = createCasingBlock("stabilized_naquadah_water_plant_casing", "稳定硅岩水处理机械方块", GTOCore.id("block/stabilized_naquadah_water_plant_casing"));
    public static final BlockEntry<Block> STRENGTHEN_THE_BASE_BLOCK = createCasingBlock("strengthen_the_base_block", "强化基座方块", GTOCore.id("block/casings/strengthen_the_base_block"));

    public static final BlockEntry<Block> PVC_PLASTIC_MECHANICAL_HOUSING = createCasingBlock("pvc_plastic_mechanical_housing", "PVC塑料机械外壳", GTOCore.id("block/pvc_plastic_mechanical_housing"));
    public static final BlockEntry<Block> PI_HIGH_TEMPERATURE_INSULATION_MECHANICAL_HOUSING = createCasingBlock("pi_high_temperature_insulation_mechanical_housing", "PI高温绝缘机械外壳", GTOCore.id("block/pi_high_temperature_insulation_mechanical_housing"));
    public static final BlockEntry<Block> MC_NYLON_TENSILE_MECHANICAL_SHELL = createCasingBlock("mc_nylon_tensile_mechanical_shell", "MC尼龙抗拉机械外壳", GTOCore.id("block/mc_nylon_tensile_mechanical_shell"));
    public static final BlockEntry<Block> PEEK_WEAR_RESISTANT_MECHANICAL_HOUSING = createCasingBlock("peek_wear_resistant_mechanical_housing", "PEEK耐磨机械外壳", GTOCore.id("block/peek_wear_resistant_mechanical_housing"));
    public static final BlockEntry<Block> REINFORCED_EPOXY_RESIN_MECHANICAL_HOUSING = createCasingBlock("reinforced_epoxy_resin_mechanical_housing", "强化环氧树脂机械外壳", GTOCore.id("block/reinforced_epoxy_resin_mechanical_housing"));
    public static final BlockEntry<Block> PPS_CORROSION_RESISTANT_MECHANICAL_HOUSING = createCasingBlock("pps_corrosion_resistant_mechanical_housing", "PPS耐腐蚀机械外壳", GTOCore.id("block/pps_corrosion_resistant_mechanical_housing"));
    public static final BlockEntry<Block> PBI_RADIATION_RESISTANT_MECHANICAL_ENCLOSURE = createCasingBlock("pbi_radiation_resistant_mechanical_enclosure", "PBI抗辐射机械外壳", GTOCore.id("block/pbi_radiation_resistant_mechanical_enclosure"));

    public static final BlockEntry<Block> CALCIUM_OXIDE_CERAMIC_ANTI_METAL_CORROSION_MECHANICAL_BLOCK = createCasingBlock("calcium_oxide_ceramic_anti_metal_corrosion_mechanical_block", "氧化钙陶瓷抗金属侵蚀机械方块", GTOCore.id("block/calcium_oxide_ceramic_anti_metal_corrosion_mechanical_block"));
    public static final BlockEntry<Block> ZIRCONIA_CERAMIC_HIGH_STRENGTH_BENDING_RESISTANCE_MECHANICAL_BLOCK = createCasingBlock("zirconia_ceramic_high_strength_bending_resistance_mechanical_block", "氧化锆陶瓷高强度耐弯折机械方块", GTOCore.id("block/zirconia_ceramic_high_strength_bending_resistance_mechanical_block"));
    public static final BlockEntry<Block> LITHIUM_OXIDE_CERAMIC_HEAT_RESISTANT_SHOCK_RESISTANT_MECHANICAL_CUBE = createCasingBlock("lithium_oxide_ceramic_heat_resistant_shock_resistant_mechanical_cube", "氧化锂陶瓷防热抗震机械方块", GTOCore.id("block/lithium_oxide_ceramic_heat_resistant_shock_resistant_mechanical_cube"));
    public static final BlockEntry<Block> TITANIUM_NITRIDE_CERAMIC_IMPACT_RESISTANT_MECHANICAL_BLOCK = createCasingBlock("titanium_nitride_ceramic_impact_resistant_mechanical_block", "氮化钛陶瓷抗冲击机械方块", GTOCore.id("block/titanium_nitride_ceramic_impact_resistant_mechanical_block"));
    public static final BlockEntry<Block> STRONTIUM_CARBONATE_CERAMIC_RAY_ABSORBING_MECHANICAL_CUBE = createCasingBlock("strontium_carbonate_ceramic_ray_absorbing_mechanical_cube", "碳酸锶陶瓷射线吸收机械方块", GTOCore.id("block/strontium_carbonate_ceramic_ray_absorbing_mechanical_cube"));
    public static final BlockEntry<Block> MAGNESIUM_OXIDE_CERAMIC_HIGH_TEMPERATURE_INSULATION_MECHANICAL_BLOCK = createCasingBlock("magnesium_oxide_ceramic_high_temperature_insulation_mechanical_block", "氧化镁陶瓷高温绝缘机械方块", GTOCore.id("block/magnesium_oxide_ceramic_high_temperature_insulation_mechanical_block"));
    public static final BlockEntry<Block> BORON_CARBIDE_CERAMIC_RADIATION_RESISTANT_MECHANICAL_CUBE = createCasingBlock("boron_carbide_ceramic_radiation_resistant_mechanical_cube", "碳化硼陶瓷耐辐射机械方块", GTOCore.id("block/boron_carbide_ceramic_radiation_resistant_mechanical_cube"));
    public static final BlockEntry<Block> COBALT_OXIDE_CERAMIC_STRONG_THERMALLY_CONDUCTIVE_MECHANICAL_BLOCK = createCasingBlock("cobalt_oxide_ceramic_strong_thermally_conductive_mechanical_block", "氧化钴陶瓷坚固导热机械方块", GTOCore.id("block/cobalt_oxide_ceramic_strong_thermally_conductive_mechanical_block"));

    public static final BlockEntry<Block> ABS_BLACK_CASING = createCasingBlock("abs_black_casing", "黑色ABS塑料机械外壳", GTOCore.id("block/casings/abs_black_casing"));
    public static final BlockEntry<Block> ABS_BLUE_CASING = createCasingBlock("abs_blue_casing", "蓝色ABS塑料机械外壳", GTOCore.id("block/casings/abs_blue_casing"));
    public static final BlockEntry<Block> ABS_BROWN_CASING = createCasingBlock("abs_brown_casing", "棕色ABS塑料机械外壳", GTOCore.id("block/casings/abs_brown_casing"));
    public static final BlockEntry<Block> ABS_GREEN_CASING = createCasingBlock("abs_green_casing", "绿色ABS塑料机械外壳", GTOCore.id("block/casings/abs_green_casing"));
    public static final BlockEntry<Block> ABS_GREY_CASING = createCasingBlock("abs_grey_casing", "灰色ABS塑料机械外壳", GTOCore.id("block/casings/abs_grey_casing"));
    public static final BlockEntry<Block> ABS_LIME_CASING = createCasingBlock("abs_lime_casing", "黄绿色ABS塑料机械外壳", GTOCore.id("block/casings/abs_lime_casing"));
    public static final BlockEntry<Block> ABS_ORANGE_CASING = createCasingBlock("abs_orange_casing", "橙色ABS塑料机械外壳", GTOCore.id("block/casings/abs_orange_casing"));
    public static final BlockEntry<Block> ABS_RAD_CASING = createCasingBlock("abs_rad_casing", "红色ABS塑料机械外壳", GTOCore.id("block/casings/abs_rad_casing"));
    public static final BlockEntry<Block> ABS_WHITE_CASING = createCasingBlock("abs_white_casing", "白色ABS塑料机械外壳", GTOCore.id("block/casings/abs_white_casing"));
    public static final BlockEntry<Block> ABS_YELLOW_CASING = createCasingBlock("abs_yellow_casing", "黄色ABS塑料机械外壳", GTOCore.id("block/casings/abs_yellow_casing"));
    public static final BlockEntry<Block> ABS_CYAN_CASING = createCasingBlock("abs_cyan_casing", "青色ABS塑料机械外壳", GTOCore.id("block/casings/abs_cyan_casing"));
    public static final BlockEntry<Block> ABS_MAGENTA_CASING = createCasingBlock("abs_magenta_casing", "品红色ABS塑料机械外壳", GTOCore.id("block/casings/abs_magenta_casing"));
    public static final BlockEntry<Block> ABS_PINK_CASING = createCasingBlock("abs_pink_casing", "粉色ABS塑料机械外壳", GTOCore.id("block/casings/abs_pink_casing"));
    public static final BlockEntry<Block> ABS_PURPLE_CASING = createCasingBlock("abs_purple_casing", "紫色ABS塑料机械外壳", GTOCore.id("block/casings/abs_purple_casing"));
    public static final BlockEntry<Block> ABS_LIGHT_BULL_CASING = createCasingBlock("abs_light_bull_casing", "浅蓝色ABS塑料机械外壳", GTOCore.id("block/casings/abs_light_bull_casing"));
    public static final BlockEntry<Block> ABS_LIGHT_GREY_CASING = createCasingBlock("abs_light_grey_casing", "浅灰色ABS塑料机械外壳", GTOCore.id("block/casings/abs_light_grey_casing"));
}
