package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.block.*;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.block.IFusionCasingType;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.common.block.FusionCasingBlock;
import com.gregtechceu.gtceu.common.data.GTModels;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.block.crafting.CraftingUnitBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.crafting.CraftingBlockEntity;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.common.data.GTBlocks.ALL_FUSION_CASINGS;
import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

@SuppressWarnings("unused")
public class GTOBlocks {

    public static final Map<Integer, Supplier<?>> SCMAP = new HashMap<>();
    public static final Map<Integer, Supplier<?>> SEPMMAP = new HashMap<>();
    public static final Map<Integer, Supplier<?>> CALMAP = new HashMap<>();
    public static final Map<Integer, Supplier<?>> MGMAP = new HashMap<>();

    static {
        REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_CORE);
    }

    public static void init() {}

    private static BlockEntry<CraftingUnitBlock> registerCraftingUnitBlock(int tier, CraftingUnitType Type) {
        return REGISTRATE
                .block(tier == -1 ? "max_storage" : tier + "m_storage",
                        p -> new CraftingUnitBlock(Type))
                .blockstate((ctx, provider) -> {
                    String formed = "block/crafting/" + ctx.getName() + "_formed";
                    String unformed = "block/crafting/" + ctx.getName();
                    provider.models().cubeAll(unformed, provider.modLoc("block/crafting/" + ctx.getName()));
                    provider.models().getBuilder(formed);
                    provider.getVariantBuilder(ctx.get())
                            .forAllStatesExcept(state -> {
                                boolean b = state.getValue(AbstractCraftingUnitBlock.FORMED);
                                return ConfiguredModel.builder()
                                        .modelFile(provider.models()
                                                .getExistingFile(provider.modLoc(b ? formed : unformed)))
                                        .build();
                            }, AbstractCraftingUnitBlock.POWERED);
                })
                .defaultLoot()
                .item(BlockItem::new)
                .model((ctx, provider) -> provider.withExistingParent(ctx.getName(),
                        provider.modLoc("block/crafting/" + ctx.getName())))
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

    @SuppressWarnings("all")
    public static BlockEntry<ActiveBlock> createActiveCasing(String name, String baseModelPath) {
        return REGISTRATE.block(name, ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GTModels.createActiveModel(GTOCore.id(baseModelPath)))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), GTOCore.id(baseModelPath)))
                .build()
                .register();
    }

    @SuppressWarnings("all")
    public static BlockEntry<Block> createTierCasings(String name, ResourceLocation texture,
                                                      Map<Integer, Supplier<?>> map, int tier) {
        BlockEntry<Block> Block = REGISTRATE.block(name, p -> (Block) new Block(p) {

            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level,
                                        @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
                tooltip.add(Component.translatable("tooltip.avaritia.tier", tier));
            }
        })
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::solid)
                .blockstate(GTModels.cubeAllModel(name, texture))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
        map.put(tier, Block);
        return Block;
    }

    @SuppressWarnings("all")
    public static BlockEntry<ActiveBlock> createActiveTierCasing(String name, String baseModelPath,
                                                                 Map<Integer, Supplier<?>> map, int tier) {
        BlockEntry<ActiveBlock> Block = REGISTRATE.block("%s".formatted(name), p -> (ActiveBlock) new ActiveBlock(p) {

            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level,
                                        @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
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

    @SuppressWarnings("all")
    private static BlockEntry<Block> createCleanroomFilter(IFilterType filterType) {
        var filterBlock = REGISTRATE.block(filterType.getSerializedName(), Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(properties -> properties.strength(2.0f, 8.0f).sound(SoundType.METAL)
                        .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false))
                .addLayer(() -> RenderType::solid)
                .blockstate(NonNullBiConsumer.noop())
                .tag(GTToolType.WRENCH.harvestTags.get(0), CustomTags.TOOL_TIERS[1])
                .item(BlockItem::new)
                .build()
                .register();
        GTCEuAPI.CLEANROOM_FILTERS.put(filterType, filterBlock);
        return filterBlock;
    }

    private static BlockEntry<Block> createGlassCasingBlock(String name, ResourceLocation texture,
                                                            Supplier<Supplier<RenderType>> type) {
        return createCasingBlock(name, GTModels.cubeAllModel(name, texture), GlassBlock::new, () -> Blocks.GLASS, type);
    }

    public static BlockEntry<Block> createCasingBlock(String name, ResourceLocation texture) {
        return createCasingBlock(name, GTModels.cubeAllModel(name, texture), Block::new, () -> Blocks.IRON_BLOCK,
                () -> RenderType::solid);
    }

    public static BlockEntry<Block> createCustomModelCasingBlock(String name) {
        return createCasingBlock(name, NonNullBiConsumer.noop(), Block::new, () -> Blocks.IRON_BLOCK,
                () -> RenderType::solid);
    }

    @SuppressWarnings("all")
    public static BlockEntry<Block> createCasingBlock(String name, NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateBlockstateProvider> cons,
                                                      NonNullFunction<BlockBehaviour.Properties, Block> blockSupplier,
                                                      NonNullSupplier<? extends Block> properties,
                                                      Supplier<Supplier<RenderType>> type) {
        return REGISTRATE.block(name, blockSupplier)
                .initialProperties(properties)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(type)
                .blockstate(cons)
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    @SuppressWarnings("all")
    public static BlockEntry<Block> createSidedCasingBlock(String name, ResourceLocation texture) {
        return REGISTRATE.block(name, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::solid)
                .blockstate((ctx, prov) -> {
                    prov.simpleBlock(ctx.getEntry(), prov.models().cubeBottomTop(name,
                            texture.withSuffix("/side"),
                            texture.withSuffix("/top"),
                            texture.withSuffix("/top")));
                })
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    @SuppressWarnings("all")
    public static BlockEntry<Block> createStoneBlock(String name, ResourceLocation texture) {
        return REGISTRATE.block(name, Block::new)
                .initialProperties(() -> Blocks.STONE)
                .addLayer(() -> RenderType::solid)
                .blockstate(GTModels.cubeAllModel(name, texture))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    @SuppressWarnings("all")
    public static BlockEntry<Block> createSandBlock(String name, ResourceLocation texture) {
        return REGISTRATE.block(name, Block::new)
                .initialProperties(() -> Blocks.SAND)
                .addLayer(() -> RenderType::solid)
                .blockstate(GTModels.cubeAllModel(name, texture))
                .tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .item(BlockItem::new)
                .build()
                .register();
    }

    @SuppressWarnings("all")
    private static BlockEntry<FusionCasingBlock> createFusionCasing(IFusionCasingType casingType) {
        BlockEntry<FusionCasingBlock> casingBlock = REGISTRATE
                .block(casingType.getSerializedName(), p -> (FusionCasingBlock) new GTOFusionCasingBlock(p, casingType))
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

    @SuppressWarnings("all")
    private static BlockEntry<Block> createHermeticCasing(int tier) {
        String tierName = GTValues.VN[tier].toLowerCase(Locale.ROOT);
        return REGISTRATE
                .block("%s_hermetic_casing".formatted(tierName), Block::new)
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

    @SuppressWarnings("all")
    private static BlockEntry<CoilBlock> createCoilBlock(ICoilType coilType) {
        BlockEntry<CoilBlock> coilBlock = REGISTRATE
                .block("%s_coil_block".formatted(coilType.getName()), p -> new CoilBlock(p, coilType))
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

    public static final BlockEntry<NukeBombBlock> NUKE_BOMB = REGISTRATE
            .block("nuke_bomb", NukeBombBlock::new)
            .properties(p -> p.mapColor(MapColor.FIRE).instabreak().sound(SoundType.GRASS).ignitedByLava())
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cubeBottomTop(ctx.getName(),
                    GTOCore.id("block/nuke_bomb"),
                    new ResourceLocation("minecraft", "block/tnt_bottom"),
                    new ResourceLocation("minecraft", "block/tnt_top"))))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> HERMETIC_CASING_UEV = createHermeticCasing(GTValues.UEV);
    public static final BlockEntry<Block> HERMETIC_CASING_UIV = createHermeticCasing(GTValues.UIV);
    public static final BlockEntry<Block> HERMETIC_CASING_UXV = createHermeticCasing(GTValues.UXV);
    public static final BlockEntry<Block> HERMETIC_CASING_OpV = createHermeticCasing(GTValues.OpV);

    public static final BlockEntry<CoilBlock> COIL_URUIUM = createCoilBlock(CoilType.URUIUM);
    public static final BlockEntry<CoilBlock> COIL_ABYSSALALLOY = createCoilBlock(CoilType.ABYSSALALLOY);
    public static final BlockEntry<CoilBlock> COIL_TITANSTEEL = createCoilBlock(CoilType.TITANSTEEL);
    public static final BlockEntry<CoilBlock> COIL_ADAMANTINE = createCoilBlock(CoilType.ADAMANTINE);
    public static final BlockEntry<CoilBlock> COIL_NAQUADRIATICTARANIUM = createCoilBlock(CoilType.NAQUADRIATICTARANIUM);
    public static final BlockEntry<CoilBlock> COIL_STARMETAL = createCoilBlock(CoilType.STARMETAL);
    public static final BlockEntry<CoilBlock> COIL_INFINITY = createCoilBlock(CoilType.INFINITY);
    public static final BlockEntry<CoilBlock> COIL_HYPOGEN = createCoilBlock(CoilType.HYPOGEN);
    public static final BlockEntry<CoilBlock> COIL_ETERNITY = createCoilBlock(CoilType.ETERNITY);

    public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK4 = createFusionCasing(
            GTOFusionCasingBlock.CasingType.FUSION_CASING_MK4);
    public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK5 = createFusionCasing(
            GTOFusionCasingBlock.CasingType.FUSION_CASING_MK5);

    public static final BlockEntry<ActiveBlock> ADVANCED_FUSION_COIL = createActiveCasing("advanced_fusion_coil",
            "block/variant/advanced_fusion_coil");
    public static final BlockEntry<ActiveBlock> FUSION_COIL_MK2 = createActiveCasing("fusion_coil_mk2",
            "block/variant/fusion_coil_mk2");
    public static final BlockEntry<ActiveBlock> IMPROVED_SUPERCONDUCTOR_COIL = createActiveCasing(
            "improved_superconductor_coil", "block/variant/improved_superconductor_coil");
    public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL = createActiveCasing("compressed_fusion_coil",
            "block/variant/compressed_fusion_coil");
    public static final BlockEntry<ActiveBlock> ADVANCED_COMPRESSED_FUSION_COIL = createActiveCasing(
            "advanced_compressed_fusion_coil", "block/variant/advanced_compressed_fusion_coil");
    public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2_PROTOTYPE = createActiveCasing(
            "compressed_fusion_coil_mk2_prototype", "block/variant/compressed_fusion_coil_mk2_prototype");
    public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2 = createActiveCasing(
            "compressed_fusion_coil_mk2", "block/variant/compressed_fusion_coil_mk2");

    public static final BlockEntry<Block> FILTER_CASING_LAW = createCleanroomFilter(
            CleanroomFilterType.FILTER_CASING_LAW);

    public static final BlockEntry<Block> TITANSTONE = createStoneBlock(
            "titanstone", GTOCore.id("block/stone/titanstone"));
    public static final BlockEntry<Block> PLUTOSTONE = createStoneBlock(
            "plutostone", GTOCore.id("block/stone/plutostone"));
    public static final BlockEntry<Block> IOSTONE = createStoneBlock(
            "iostone", GTOCore.id("block/stone/iostone"));
    public static final BlockEntry<Block> GANYMEDESTONE = createStoneBlock(
            "ganymedestone", GTOCore.id("block/stone/ganymedestone"));
    public static final BlockEntry<Block> ENCELADUSSTONE = createStoneBlock(
            "enceladusstone", GTOCore.id("block/stone/enceladusstone"));
    public static final BlockEntry<Block> CERESSTONE = createStoneBlock(
            "ceresstone", GTOCore.id("block/stone/ceresstone"));
    public static final BlockEntry<Block> ESSENCE_BLOCK = createStoneBlock(
            "essence_block", GTOCore.id("block/essence_block"));
    public static final BlockEntry<Block> REACTOR_CORE = createStoneBlock(
            "reactor_core", GTOCore.id("block/reactor_core"));
    public static final BlockEntry<Block> COMMAND_BLOCK_BROKEN = createStoneBlock(
            "command_block_broken", GTOCore.id("block/command_block_broken"));
    public static final BlockEntry<Block> CHAIN_COMMAND_BLOCK_BROKEN = createStoneBlock(
            "chain_command_block_broken", GTOCore.id("block/chain_command_block_broken"));
    public static final BlockEntry<Block> INFUSED_OBSIDIAN = createStoneBlock(
            "infused_obsidian", GTOCore.id("block/infused_obsidian"));
    public static final BlockEntry<Block> DRACONIUM_BLOCK_CHARGED = createStoneBlock(
            "draconium_block_charged", GTOCore.id("block/draconium_block_charged"));
    public static final BlockEntry<Block> SHINING_OBSIDIAN = createStoneBlock(
            "shining_obsidian", GTOCore.id("block/shining_obsidian"));
    public static final BlockEntry<Block> ENDER_OBSIDIAN = createStoneBlock(
            "ender_obsidian", GTOCore.id("block/ender_obsidian"));

    public static final BlockEntry<Block> TITANGRUNT = createSandBlock(
            "titangrunt", GTOCore.id("block/sand/titangrunt"));
    public static final BlockEntry<Block> PLUTOGRUNT = createSandBlock(
            "plutogrunt", GTOCore.id("block/sand/plutogrunt"));
    public static final BlockEntry<Block> IOASH = createSandBlock(
            "ioash", GTOCore.id("block/sand/ioash"));
    public static final BlockEntry<Block> GANYMEDEGRUNT = createSandBlock(
            "ganymedegrunt", GTOCore.id("block/sand/titangrunt"));
    public static final BlockEntry<Block> CERESGRUNT = createSandBlock(
            "ceresgrunt", GTOCore.id("block/sand/ceresgrunt"));

    public static final BlockEntry<Block> SPACE_ELEVATOR_INTERNAL_SUPPORT = createSidedCasingBlock(
            "space_elevator_internal_support", GTOCore.id("block/casings/space_elevator_internal_support"));
    public static final BlockEntry<Block> MODULE_BASE = createSidedCasingBlock(
            "module_base", GTOCore.id("block/casings/module_base"));
    public static final BlockEntry<Block> MOLECULAR_COIL = createSidedCasingBlock(
            "molecular_coil", GTOCore.id("block/casings/molecular_coil"));
    public static final BlockEntry<Block> DYSON_RECEIVER_CASING = createSidedCasingBlock(
            "dyson_receiver_casing", GTOCore.id("block/casings/dyson_receiver_casing"));
    public static final BlockEntry<Block> DYSON_DEPLOYMENT_MAGNET = createSidedCasingBlock(
            "dyson_deployment_magnet", GTOCore.id("block/casings/dyson_deployment_magnet"));
    public static final BlockEntry<Block> SPEEDING_PIPE = createSidedCasingBlock(
            "speeding_pipe", GTOCore.id("block/casings/speeding_pipe"));
    public static final BlockEntry<Block> RED_STEEL_CASING = createSidedCasingBlock(
            "red_steel_casing", GTOCore.id("block/casings/red_steel_casing"));

    public static final BlockEntry<Block> NAQUADRIA_CHARGE = createCasingBlock(
            "naquadria_charge", GTOCore.id("block/naquadria_charge"));
    public static final BlockEntry<Block> LEPTONIC_CHARGE = createCasingBlock(
            "leptonic_charge", GTOCore.id("block/leptonic_charge"));
    public static final BlockEntry<Block> QUANTUM_CHROMODYNAMIC_CHARGE = createCasingBlock(
            "quantum_chromodynamic_charge", GTOCore.id("block/quantum_chromodynamic_charge"));
    public static final BlockEntry<Block> ANNIHILATE_CORE = createCasingBlock(
            "annihilate_core", GTOCore.id("block/annihilate_core"));
    public static final BlockEntry<Block> NEUTRONIUM_PIPE_CASING = createCasingBlock(
            "neutronium_pipe_casing", GTOCore.id("block/neutronium_pipe_casing"));
    public static final BlockEntry<Block> NEUTRONIUM_GEARBOX = createCasingBlock(
            "neutronium_gearbox", GTOCore.id("block/neutronium_gearbox"));
    public static final BlockEntry<Block> LASER_COOLING_CASING = createCasingBlock(
            "laser_cooling_casing", GTOCore.id("block/laser_cooling_casing"));
    public static final BlockEntry<Block> SPACETIME_COMPRESSION_FIELD_GENERATOR = createCasingBlock(
            "spacetime_compression_field_generator", GTOCore.id("block/spacetime_compression_field_generator"));
    public static final BlockEntry<Block> DIMENSIONAL_BRIDGE_CASING = createCasingBlock(
            "dimensional_bridge_casing", GTOCore.id("block/dimensional_bridge_casing"));
    public static final BlockEntry<Block> DIMENSIONAL_STABILITY_CASING = createCasingBlock(
            "dimensional_stability_casing", GTOCore.id("block/dimensional_stability_casing"));
    public static final BlockEntry<Block> MACHINE_CASING_CIRCUIT_ASSEMBLY_LINE = createCasingBlock(
            "machine_casing_circuit_assembly_line", GTOCore.id("block/machine_casing_circuit_assembly_line"));
    public static final BlockEntry<Block> HIGH_STRENGTH_CONCRETE = createCasingBlock(
            "high_strength_concrete", GTOCore.id("block/casings/module_base/side"));
    public static final BlockEntry<Block> AGGREGATIONE_CORE = createCasingBlock(
            "aggregatione_core", GTOCore.id("block/aggregatione_core"));
    public static final BlockEntry<Block> ACCELERATED_PIPELINE = createCasingBlock(
            "accelerated_pipeline", GTOCore.id("block/accelerated_pipeline"));
    public static final BlockEntry<Block> MODULE_CONNECTOR = createCasingBlock(
            "module_connector", GTOCore.id("block/module_connector"));
    public static final BlockEntry<Block> DIMENSION_CREATION_CASING = createCasingBlock(
            "dimension_creation_casing", GTOCore.id("block/dimension_creation_casing"));
    public static final BlockEntry<Block> MACHINE_CASING_GRINDING_HEAD = createCasingBlock(
            "machine_casing_grinding_head", GTOCore.id("block/machine_casing_grinding_head"));
    public static final BlockEntry<Block> CREATE_AGGREGATIONE_CORE = createCasingBlock(
            "create_aggregatione_core", GTOCore.id("block/create_aggregatione_core"));
    public static final BlockEntry<Block> CREATE_HPCA_COMPONENT = createCasingBlock(
            "create_hpca_component", GTOCore.id("block/create_hpca_component"));
    public static final BlockEntry<Block> SPACETIME_ASSEMBLY_LINE_UNIT = createCasingBlock(
            "spacetime_assembly_line_unit", GTOCore.id("block/spacetime_assembly_line_unit"));
    public static final BlockEntry<Block> SPACETIME_ASSEMBLY_LINE_CASING = createCasingBlock(
            "spacetime_assembly_line_casing", GTOCore.id("block/spacetime_assembly_line_casing"));
    public static final BlockEntry<Block> HOLLOW_CASING = createCasingBlock(
            "hollow_casing", GTOCore.id("block/hollow_casing"));
    public static final BlockEntry<Block> CONTAINMENT_FIELD_GENERATOR = createCasingBlock(
            "containment_field_generator", GTOCore.id("block/containment_field_generator"));
    public static final BlockEntry<Block> DYSON_CONTROL_CASING = createCasingBlock(
            "dyson_control_casing", GTOCore.id("block/space_elevator_mechanical_casing"));
    public static final BlockEntry<Block> DYSON_CONTROL_TOROID = createCasingBlock(
            "dyson_control_toroid", GTOCore.id("block/dyson_control_toroid"));
    public static final BlockEntry<Block> DYSON_DEPLOYMENT_CASING = createCasingBlock(
            "dyson_deployment_casing", GTOCore.id("block/dyson_deployment_casing"));
    public static final BlockEntry<Block> DYSON_DEPLOYMENT_CORE = createCasingBlock(
            "dyson_deployment_core", GTOCore.id("block/dyson_deployment_core"));
    public static final BlockEntry<Block> STEAM_ASSEMBLY_BLOCK = createCasingBlock(
            "steam_assembly_block", GTOCore.id("block/steam_assembly_block"));
    public static final BlockEntry<Block> RESTRAINT_DEVICE = createCasingBlock(
            "restraint_device", GTOCore.id("block/restraint_device"));
    public static final BlockEntry<Block> INCONEL_625_CASING = createCasingBlock(
            "inconel_625_casing", GTOCore.id("block/inconel_625_casing"));
    public static final BlockEntry<Block> INCONEL_625_GEARBOX = createCasingBlock(
            "inconel_625_gearbox", GTOCore.id("block/inconel_625_gearbox"));
    public static final BlockEntry<Block> INCONEL_625_PIPE = createCasingBlock(
            "inconel_625_pipe", GTOCore.id("block/inconel_625_pipe"));
    public static final BlockEntry<Block> HASTELLOY_N_75_CASING = createCasingBlock(
            "hastelloy_n_75_casing", GTOCore.id("block/hastelloy_n_75_casing"));
    public static final BlockEntry<Block> HASTELLOY_N_75_GEARBOX = createCasingBlock(
            "hastelloy_n_75_gearbox", GTOCore.id("block/hastelloy_n_75_gearbox"));
    public static final BlockEntry<Block> HASTELLOY_N_75_PIPE = createCasingBlock(
            "hastelloy_n_75_pipe", GTOCore.id("block/hastelloy_n_75_pipe"));
    public static final BlockEntry<Block> FLOTATION_CELL = createCasingBlock(
            "flotation_cell", GTOCore.id("block/flotation_cell"));

    public static final BlockEntry<Block> CASING_SUPERCRITICAL_TURBINE = createCasingBlock(
            "supercritical_turbine_casing", GTOCore.id("block/supercritical_turbine_casing"));
    public static final BlockEntry<Block> MULTI_FUNCTIONAL_CASING = createCasingBlock(
            "multi_functional_casing", GTOCore.id("block/multi_functional_casing"));
    public static final BlockEntry<Block> CREATE_CASING = createCasingBlock(
            "create_casing", GTOCore.id("block/create_casing"));
    public static final BlockEntry<Block> SPACE_ELEVATOR_MECHANICAL_CASING = createCasingBlock(
            "space_elevator_mechanical_casing", GTOCore.id("block/space_elevator_mechanical_casing"));
    public static final BlockEntry<Block> MANIPULATOR = createCasingBlock(
            "manipulator", GTOCore.id("block/manipulator"));
    public static final BlockEntry<Block> BLAZE_BLAST_FURNACE_CASING = createCasingBlock(
            "blaze_blast_furnace_casing", GTOCore.id("block/blaze_blast_furnace_casing"));
    public static final BlockEntry<Block> COLD_ICE_CASING = createCasingBlock(
            "cold_ice_casing", GTOCore.id("block/cold_ice_casing"));
    public static final BlockEntry<Block> DIMENSION_CONNECTION_CASING = createCasingBlock(
            "dimension_connection_casing", GTOCore.id("block/dimension_connection_casing"));
    public static final BlockEntry<Block> MOLECULAR_CASING = createCasingBlock(
            "molecular_casing", GTOCore.id("block/molecular_casing"));

    public static final BlockEntry<Block> DIMENSION_INJECTION_CASING = createCasingBlock(
            "dimension_injection_casing", GTOCore.id("block/casings/dimension_injection_casing"));
    public static final BlockEntry<Block> DIMENSIONALLY_TRANSCENDENT_CASING = createCasingBlock(
            "dimensionally_transcendent_casing", GTOCore.id("block/casings/dimensionally_transcendent_casing"));
    public static final BlockEntry<Block> ECHO_CASING = createCasingBlock(
            "echo_casing", GTOCore.id("block/casings/echo_casing"));
    public static final BlockEntry<Block> DRAGON_STRENGTH_TRITANIUM_CASING = createCasingBlock(
            "dragon_strength_tritanium_casing", GTOCore.id("block/casings/extreme_strength_tritanium_casing"));
    public static final BlockEntry<Block> ALUMINIUM_BRONZE_CASING = createCasingBlock(
            "aluminium_bronze_casing", GTOCore.id("block/casings/aluminium_bronze_casing"));
    public static final BlockEntry<Block> ANTIFREEZE_HEATPROOF_MACHINE_CASING = createCasingBlock(
            "antifreeze_heatproof_machine_casing", GTOCore.id("block/casings/antifreeze_heatproof_machine_casing"));
    public static final BlockEntry<Block> ENHANCE_HYPER_MECHANICAL_CASING = createCasingBlock(
            "enhance_hyper_mechanical_casing", GTOCore.id("block/casings/enhance_hyper_mechanical_casing"));
    public static final BlockEntry<Block> EXTREME_STRENGTH_TRITANIUM_CASING = createCasingBlock(
            "extreme_strength_tritanium_casing", GTOCore.id("block/casings/extreme_strength_tritanium_casing"));
    public static final BlockEntry<Block> GRAVITON_FIELD_CONSTRAINT_CASING = createCasingBlock(
            "graviton_field_constraint_casing", GTOCore.id("block/casings/graviton_field_constraint_casing"));
    public static final BlockEntry<Block> HYPER_MECHANICAL_CASING = createCasingBlock(
            "hyper_mechanical_casing", GTOCore.id("block/casings/hyper_mechanical_casing"));
    public static final BlockEntry<Block> IRIDIUM_CASING = createCasingBlock(
            "iridium_casing", GTOCore.id("block/casings/iridium_casing"));
    public static final BlockEntry<Block> LAFIUM_MECHANICAL_CASING = createCasingBlock(
            "lafium_mechanical_casing", GTOCore.id("block/casings/lafium_mechanical_casing"));
    public static final BlockEntry<Block> OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING = createCasingBlock(
            "oxidation_resistant_hastelloy_n_mechanical_casing", GTOCore.id("block/casings/oxidation_resistant_hastelloy_n_mechanical_casing"));
    public static final BlockEntry<Block> PIKYONIUM_MACHINE_CASING = createCasingBlock(
            "pikyonium_machine_casing", GTOCore.id("block/casings/pikyonium_machine_casing"));
    public static final BlockEntry<Block> SPS_CASING = createCasingBlock(
            "sps_casing", GTOCore.id("block/casings/sps_casing"));
    public static final BlockEntry<Block> NAQUADAH_ALLOY_CASING = createCasingBlock(
            "naquadah_alloy_casing", GTOCore.id("block/casings/hyper_mechanical_casing"));
    public static final BlockEntry<Block> PROCESS_MACHINE_CASING = createCasingBlock(
            "process_machine_casing", GTOCore.id("block/casings/process_machine_casing"));
    public static final BlockEntry<Block> FISSION_REACTOR_CASING = createCasingBlock(
            "fission_reactor_casing", GTOCore.id("block/casings/fission_reactor_casing"));
    public static final BlockEntry<Block> DEGENERATE_RHENIUM_CONSTRAINED_CASING = createCasingBlock(
            "degenerate_rhenium_constrained_casing", GTOCore.id("block/casings/degenerate_rhenium_constrained_casing"));

    public static final BlockEntry<Block> PRESSURE_CONTAINMENT_CASING = createCasingBlock(
            "pressure_containment_casing", GTOCore.id("block/casings/pressure_containment_casing"));
    public static final BlockEntry<Block> AWAKENED_DRACONIUM_CASING = createCasingBlock(
            "awakened_draconium_casing", GTOCore.id("block/casings/awakened_draconium_casing"));
    public static final BlockEntry<Block> ANTIMATTER_ANNIHILATION_MATRIX = createCasingBlock(
            "antimatter_annihilation_matrix", GTOCore.id("block/antimatter_annihilation_matrix"));
    public static final BlockEntry<Block> BOUNDLESS_GRAVITATIONALLY_SEVERED_STRUCTURE_CASING = createCasingBlock(
            "boundless_gravitationally_severed_structure_casing", GTOCore.id("block/boundless_gravitationally_severed_structure_casing"));
    public static final BlockEntry<Block> CELESTIAL_MATTER_GUIDANCE_CASING = createCasingBlock(
            "celestial_matter_guidance_casing", GTOCore.id("block/celestial_matter_guidance_casing"));
    public static final BlockEntry<Block> COMPRESSOR_CONTROLLER_CASING = createCasingBlock(
            "compressor_controller_casing", GTOCore.id("block/compressor_controller_casing"));
    public static final BlockEntry<Block> COMPRESSOR_PIPE_CASING = createCasingBlock(
            "compressor_pipe_casing", GTOCore.id("block/compressor_pipe_casing"));
    public static final BlockEntry<Block> EXTREME_DENSITY_CASING = createCasingBlock(
            "extreme_density_casing", GTOCore.id("block/extreme_density_casing"));
    public static final BlockEntry<Block> FLOCCULATION_CASING = createCasingBlock(
            "flocculation_casing", GTOCore.id("block/flocculation_casing"));
    public static final BlockEntry<Block> GRAVITY_STABILIZATION_CASING = createCasingBlock(
            "gravity_stabilization_casing", GTOCore.id("block/gravity_stabilization_casing"));
    public static final BlockEntry<Block> HIGH_PRESSURE_RESISTANT_CASING = createCasingBlock(
            "high_pressure_resistant_casing", GTOCore.id("block/high_pressure_resistant_casing"));
    public static final BlockEntry<Block> LASER_CASING = createCasingBlock(
            "laser_casing", GTOCore.id("block/laser_casing"));
    public static final BlockEntry<Block> MAGTECH_CASING = createCasingBlock(
            "magtech_casing", GTOCore.id("block/magtech_casing"));
    public static final BlockEntry<Block> NAQUADAH_REINFORCED_PLANT_CASING = createCasingBlock(
            "naquadah_reinforced_plant_casing", GTOCore.id("block/naquadah_reinforced_plant_casing"));
    public static final BlockEntry<Block> NEUTRONIUM_CASING = createCasingBlock(
            "neutronium_casing", GTOCore.id("block/neutronium_casing"));
    public static final BlockEntry<Block> OZONE_CASING = createCasingBlock(
            "ozone_casing", GTOCore.id("block/ozone_casing"));
    public static final BlockEntry<Block> PLASMA_HEATER_CASING = createCasingBlock(
            "plasma_heater_casing", GTOCore.id("block/plasma_heater_casing"));
    public static final BlockEntry<Block> PROTOMATTER_ACTIVATION_COIL = createCasingBlock(
            "protomatter_activation_coil", GTOCore.id("block/protomatter_activation_coil"));
    public static final BlockEntry<Block> RADIATION_ABSORBENT_CASING = createCasingBlock(
            "radiation_absorbent_casing", GTOCore.id("block/radiation_absorbent_casing"));
    public static final BlockEntry<Block> REINFORCED_WOOD_CASING = createSidedCasingBlock(
            "reinforced_wood_casing", GTOCore.id("block/casings/reinforced_wood_casing"));
    public static final BlockEntry<Block> SHIELDED_ACCELERATOR = createCasingBlock(
            "shielded_accelerator", GTOCore.id("block/shielded_accelerator"));
    public static final BlockEntry<Block> SINGULARITY_REINFORCED_STELLAR_SHIELDING_CASING = createCasingBlock(
            "singularity_reinforced_stellar_shielding_casing", GTOCore.id("block/singularity_reinforced_stellar_shielding_casing"));
    public static final BlockEntry<Block> STELLAR_ENERGY_SIPHON_CASING = createCasingBlock(
            "stellar_energy_siphon_casing", GTOCore.id("block/stellar_energy_siphon_casing"));
    public static final BlockEntry<Block> TRANSCENDENTALLY_AMPLIFIED_MAGNETIC_CONFINEMENT_CASING = createCasingBlock(
            "transcendentally_amplified_magnetic_confinement_casing", GTOCore.id("block/transcendentally_amplified_magnetic_confinement_casing"));
    public static final BlockEntry<Block> NEUTRONIUM_ACTIVE_CASING = createCasingBlock(
            "neutronium_active_casing", GTOCore.id("block/neutronium_active_casing"));
    public static final BlockEntry<Block> STERILE_CASING = createCasingBlock(
            "sterile_casing", GTOCore.id("block/sterile_casing"));

    public static final BlockEntry<Block> QUARK_PIPE = createCasingBlock(
            "quark_pipe", GTOCore.id("block/quark_pipe"));
    public static final BlockEntry<Block> QUARK_EXCLUSION_CASING = createCasingBlock(
            "quark_exclusion_casing", GTOCore.id("block/quark_exclusion_casing"));
    public static final BlockEntry<Block> INERT_NEUTRALIZATION_WATER_PLANT_CASING = createCasingBlock(
            "inert_neutralization_water_plant_casing", GTOCore.id("block/inert_neutralization_water_plant_casing"));
    public static final BlockEntry<Block> HIGH_ENERGY_ULTRAVIOLET_EMITTER_CASING = createCasingBlock(
            "high_energy_ultraviolet_emitter_casing", GTOCore.id("block/high_energy_ultraviolet_emitter_casing"));
    public static final BlockEntry<Block> REINFORCED_STERILE_WATER_PLANT_CASING = createCasingBlock(
            "reinforced_sterile_water_plant_casing", GTOCore.id("block/reinforced_sterile_water_plant_casing"));
    public static final BlockEntry<Block> NEUTRONIUM_STABLE_CASING = createCasingBlock(
            "neutronium_stable_casing", GTOCore.id("block/neutronium_stable_casing"));
    public static final BlockEntry<Block> STERILE_WATER_PLANT_CASING = createCasingBlock(
            "sterile_water_plant_casing", GTOCore.id("block/sterile_water_plant_casing"));
    public static final BlockEntry<Block> STABILIZED_NAQUADAH_WATER_PLANT_CASING = createCasingBlock(
            "stabilized_naquadah_water_plant_casing", GTOCore.id("block/stabilized_naquadah_water_plant_casing"));

    public static final BlockEntry<Block> FORCE_FIELD_GLASS = createGlassCasingBlock(
            "force_field_glass", GTOCore.id("block/force_field_glass"), () -> RenderType::translucent);
    public static final BlockEntry<Block> INFINITY_GLASS = createGlassCasingBlock(
            "infinity_glass", GTOCore.id("block/casings/infinity_glass"), () -> RenderType::translucent);
    public static final BlockEntry<Block> RHENIUM_REINFORCED_ENERGY_GLASS = createGlassCasingBlock(
            "rhenium_reinforced_energy_glass", GTOCore.id("block/casings/rhenium_reinforced_energy_glass"), () -> RenderType::translucent);
    public static final BlockEntry<Block> HSSS_REINFORCED_BOROSILICATE_GLASS = createGlassCasingBlock(
            "hsss_reinforced_borosilicate_glass", GTOCore.id("block/casings/hsss_reinforced_borosilicate_glass"), () -> RenderType::translucent);

    public static final BlockEntry<ActiveBlock> POWER_CORE = createActiveCasing("power_core",
            "block/variant/power_core");
    public static final BlockEntry<ActiveBlock> HYPER_CORE = createActiveCasing("hyper_core",
            "block/variant/hyper_core");
    public static final BlockEntry<ActiveBlock> SUPER_COMPUTATION_COMPONENT = createActiveCasing(
            "super_computation_component", "block/variant/super_computation_component");
    public static final BlockEntry<ActiveBlock> SUPER_COOLER_COMPONENT = createActiveCasing("super_cooler_component",
            "block/variant/super_cooler_component");
    public static final BlockEntry<ActiveBlock> SPACETIMECONTINUUMRIPPER = createActiveCasing(
            "spacetimecontinuumripper", "block/variant/spacetimecontinuumripper");
    public static final BlockEntry<ActiveBlock> SPACETIMEBENDINGCORE = createActiveCasing("spacetimebendingcore",
            "block/variant/spacetimebendingcore");
    public static final BlockEntry<ActiveBlock> quantum_force_transformer_coil = createActiveCasing("quantum_force_transformer_coil", "block/variant/quantum_force_transformer_coil");
    public static final BlockEntry<ActiveBlock> FISSION_FUEL_ASSEMBLY = createActiveCasing("fission_fuel_assembly",
            "block/variant/fission_fuel_assembly");
    public static final BlockEntry<ActiveBlock> COOLER = createActiveCasing("cooler", "block/variant/cooler");
    public static final BlockEntry<ActiveBlock> ADVANCED_ASSEMBLY_LINE_UNIT = createActiveCasing(
            "advanced_assembly_line_unit", "block/variant/advanced_assembly_line_unit");
    public static final BlockEntry<ActiveBlock> SPACE_ELEVATOR_SUPPORT = createActiveCasing("space_elevator_support",
            "block/variant/space_elevator_support");
    public static final BlockEntry<ActiveBlock> MAGIC_CORE = createActiveCasing("magic_core", "block/variant/magic_core");

    public static final BlockEntry<Block> STELLAR_CONTAINMENT_CASING = createTierCasings(
            "stellar_containment_casing", GTOCore.id("block/stellar_containment_casing"), SCMAP, 1);
    public static final BlockEntry<Block> ADVANCED_STELLAR_CONTAINMENT_CASING = createTierCasings(
            "advanced_stellar_containment_casing", GTOCore.id("block/stellar_containment_casing"),
            SCMAP, 2);
    public static final BlockEntry<Block> ULTIMATE_STELLAR_CONTAINMENT_CASING = createTierCasings(
            "ultimate_stellar_containment_casing", GTOCore.id("block/stellar_containment_casing"),
            SCMAP, 3);

    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_LV = createTierCasings(
            "component_assembly_line_casing_lv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_lv"), CALMAP, 1);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_MV = createTierCasings(
            "component_assembly_line_casing_mv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_mv"), CALMAP, 2);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_HV = createTierCasings(
            "component_assembly_line_casing_hv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_hv"), CALMAP, 3);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_EV = createTierCasings(
            "component_assembly_line_casing_ev", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_ev"), CALMAP, 4);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_IV = createTierCasings(
            "component_assembly_line_casing_iv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_iv"), CALMAP, 5);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_LUV = createTierCasings(
            "component_assembly_line_casing_luv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_luv"), CALMAP, 6);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_ZPM = createTierCasings(
            "component_assembly_line_casing_zpm", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_zpm"), CALMAP, 7);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UV = createTierCasings(
            "component_assembly_line_casing_uv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uv"), CALMAP, 8);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UHV = createTierCasings(
            "component_assembly_line_casing_uhv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uhv"), CALMAP, 9);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UEV = createTierCasings(
            "component_assembly_line_casing_uev", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uev"), CALMAP, 10);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UIV = createTierCasings(
            "component_assembly_line_casing_uiv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uiv"), CALMAP, 11);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UXV = createTierCasings(
            "component_assembly_line_casing_uxv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uxv"), CALMAP, 12);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_OPV = createTierCasings(
            "component_assembly_line_casing_opv", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_opv"), CALMAP, 13);
    public static final BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_MAX = createTierCasings(
            "component_assembly_line_casing_max", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_max"), CALMAP, 14);

    public static final BlockEntry<ActiveBlock> POWER_MODULE = createActiveTierCasing("power_module",
            "block/variant/power_module", SEPMMAP, 1);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_2 = createActiveTierCasing("power_module_2",
            "block/variant/power_module", SEPMMAP, 2);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_3 = createActiveTierCasing("power_module_3",
            "block/variant/power_module", SEPMMAP, 3);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_4 = createActiveTierCasing("power_module_4",
            "block/variant/power_module", SEPMMAP, 4);
    public static final BlockEntry<ActiveBlock> POWER_MODULE_5 = createActiveTierCasing("power_module_5",
            "block/variant/power_module", SEPMMAP, 5);

    public static final BlockEntry<Block> ELECTRON_PERMEABLE_NEUTRONIUM_COATED_GLASS = createGlassCasingBlock(
            "electron_permeable_neutronium_coated_glass", GTOCore.id("block/casings/electron_permeable_neutronium_coated_glass"), () -> RenderType::translucent);
    public static final BlockEntry<Block> NON_PHOTONIC_MATTER_EXCLUSION_GLASS = createGlassCasingBlock(
            "non_photonic_matter_exclusion_glass", GTOCore.id("block/casings/non_photonic_matter_exclusion_glass"), () -> RenderType::translucent);
    public static final BlockEntry<Block> SPATIALLY_TRANSCENDENT_GRAVITATIONAL_LENS_BLOCK = createGlassCasingBlock(
            "spatially_transcendent_gravitational_lens_block", GTOCore.id("block/spatially_transcendent_gravitational_lens_block"), () -> RenderType::translucent);
    public static final BlockEntry<Block> OMNI_PURPOSE_INFINITY_FUSED_GLASS = createGlassCasingBlock(
            "omni_purpose_infinity_fused_glass", GTOCore.id("block/casings/omni_purpose_infinity_fused_glass"), () -> RenderType::translucent);
    public static final BlockEntry<Block> HAWKING_RADIATION_REALIGNMENT_FOCUS = createGlassCasingBlock(
            "hawking_radiation_realignment_focus", GTOCore.id("block/casings/hawking_radiation_realignment_focus"), () -> RenderType::translucent);
    public static final BlockEntry<Block> CHEMICAL_GRADE_GLASS = createGlassCasingBlock(
            "chemical_grade_glass", GTOCore.id("block/casings/chemical_grade_glass"), () -> RenderType::translucent);
    public static final BlockEntry<Block> ANTIMATTER_CONTAINMENT_CASING = createGlassCasingBlock(
            "antimatter_containment_casing", GTOCore.id("block/antimatter_containment_casing"), () -> RenderType::translucent);
    public static final BlockEntry<Block> QUANTUM_GLASS = createGlassCasingBlock(
            "quantum_glass", GTOCore.id("block/casings/quantum_glass"), () -> RenderType::translucent);

    public static final BlockEntry<Block> ENERGETIC_PHOTOVOLTAIC_BLOCK = createCustomModelCasingBlock("energetic_photovoltaic_block");
    public static final BlockEntry<Block> PULSATING_PHOTOVOLTAIC_BLOCK = createCustomModelCasingBlock("pulsating_photovoltaic_block");
    public static final BlockEntry<Block> VIBRANT_PHOTOVOLTAIC_BLOCK = createCustomModelCasingBlock("vibrant_photovoltaic_block");

    /// *魔法线方块*
    public static final BlockEntry<Block> MAGIC_MECHANICAL_LOW_CUBE = createTierCasings(
            "magic_mechanical_low_cube", GTOCore.id("block/magic/magic_mechanical_low_cube"),
            MGMAP, 1);
    public static final BlockEntry<Block> MAGIC_MECHANICAL_MIDDLE_CUBE = createTierCasings(
            "magic_mechanical_middle_cube", GTOCore.id("block/magic/magic_mechanical_middle_cube"),
            MGMAP, 2);
    public static final BlockEntry<Block> MAGIC_MECHANICAL_HIGH_CUBE = createTierCasings(
            "magic_mechanical_high_cube", GTOCore.id("block/magic/magic_mechanical_high_cube"),
            MGMAP, 3);

    public static final BlockEntry<Block> MAGIC_COIL_LOW_BLOCK = createTierCasings(
            "magic_coil_low_block", GTOCore.id("block/magic/magic_coil_low_block"),
            MGMAP, 1);
    public static final BlockEntry<Block> MAGIC_COIL_MIDDLE_BLOCK = createTierCasings(
            "magic_coil_middle_block", GTOCore.id("block/magic/magic_coil_middle_block"),
            MGMAP, 2);
    public static final BlockEntry<Block> MAGIC_COIL_HIGH_BLOCK = createTierCasings(
            "magic_coil_high_block", GTOCore.id("block/magic/magic_coil_high_block"),
            MGMAP, 3);

    public static final BlockEntry<Block> MAGIC_GLASS_BLOCK = createGlassCasingBlock(
            "magic_glass_block", GTOCore.id("block/magic/magic_glass_block"), () -> RenderType::translucent);
    public static final BlockEntry<Block> ARCANE_GLASS_BLOCK = createGlassCasingBlock(
            "arcane_glass_block", GTOCore.id("block/magic/arcane_glass_block"), () -> RenderType::translucent);

    public static final BlockEntry<Block> NON_ATTRIBUTE_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "non_attribute_magic_crystals_block", GTOCore.id("block/magic/non_attribute_magic_crystals_block"));
    public static final BlockEntry<Block> CHANGE_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "charge_magic_crystals_block", GTOCore.id("block/magic/charge_magic_crystals_block"));
    public static final BlockEntry<Block> FOCUS_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "focus_magic_crystals_block", GTOCore.id("block/magic/focus_magic_crystals_block"));
    public static final BlockEntry<Block> SHINING_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "shining_magic_crystals_block", GTOCore.id("block/magic/shining_magic_crystals_block"));

    public static final BlockEntry<Block> NATURAL_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "natural_magic_crystals_block", GTOCore.id("block/magic/natural_magic_crystals_block"));
    public static final BlockEntry<Block> OCEAN_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "ocean_magic_crystals_block", GTOCore.id("block/magic/ocean_magic_crystals_block"));
    public static final BlockEntry<Block> COLD_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "cold_magic_crystals_block", GTOCore.id("block/magic/cold_magic_crystals_block"));
    public static final BlockEntry<Block> HIDDEN_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "hidden_magic_crystals_block", GTOCore.id("block/magic/hidden_magic_crystals_block"));
    public static final BlockEntry<Block> ANCIENT_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "ancient_magic_crystals_block", GTOCore.id("block/magic/ancient_magic_crystals_block"));
    public static final BlockEntry<Block> PURGATORY_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "purgatory_magic_crystals_block", GTOCore.id("block/magic/purgatory_magic_crystals_block"));
    public static final BlockEntry<Block> END_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "end_magic_crystals_block", GTOCore.id("block/magic/end_magic_crystals_block"));
    public static final BlockEntry<Block> STARRY_SKY_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "starry_sky_magic_crystals_block", GTOCore.id("block/magic/starry_sky_magic_crystals_block"));
    public static final BlockEntry<Block> ABYSS_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "abyss_magic_crystals_block", GTOCore.id("block/magic/abyss_magic_crystals_block"));
    public static final BlockEntry<Block> SUN_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "sun_magic_crystals_block", GTOCore.id("block/magic/sun_magic_crystals_block"));
    public static final BlockEntry<Block> TIME_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "time_magic_crystals_block", GTOCore.id("block/magic/time_magic_crystals_block"));
    public static final BlockEntry<Block> SPACE_MAGIC_CRYSTALS_BLOCK = createCasingBlock(
            "space_magic_crystals_block", GTOCore.id("block/magic/space_magic_crystals_block"));

    public static final BlockEntry<Block> MAGIC_WOODEN_PLANK = createCasingBlock(
            "magic_wooden_plank", GTOCore.id("block/magic/magic_wooden_plank"));
    public static final BlockEntry<Block> MAGIC_STONE = createCasingBlock(
            "magic_stone", GTOCore.id("block/magic/magic_stone"));
    public static final BlockEntry<Block> MAGIC_STONE_BRICK = createCasingBlock(
            "magice_stone_brick", GTOCore.id("block/magic/magic_stone_brick"));
    public static final BlockEntry<Block> WORLD_SALT_BLOCK = createCasingBlock(
            "world_salt_block", GTOCore.id("block/magic/world_salt_block"));
}
