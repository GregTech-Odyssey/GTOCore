package com.gtocore.common.machine.multiblock.generator;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.client.forge.ForgeClientEvent;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.feature.multiblock.ICustomHighlightMachine;
import com.gtolib.api.machine.feature.multiblock.IMultiStructureMachine;
import com.gtolib.api.machine.mana.feature.IManaMultiblock;
import com.gtolib.api.machine.mana.trait.ManaTrait;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.misc.ManaContainerList;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.tterrag.registrate.util.entry.BlockEntry;
import earth.terrarium.adastra.api.planets.PlanetApi;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.List;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.GTValues.HV;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.MAINTENANCE;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.api.pattern.Predicates.any;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.Predicates.controller;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gtocore.common.data.GTOMachines.ENERGY_OUTPUT_HATCH_16A;
import static com.gtocore.common.data.machines.ManaMachine.WIRELESS_MANA_OUTPUT_HATCH;
import static com.gtocore.data.IdleReason.INCORRECT_DIRECTION_VOLTA;
import static com.gtocore.data.IdleReason.OBSTRUCTED_VOLTA;
import static net.minecraft.world.level.block.Blocks.AIR;

public final class PhotovoltaicPowerStationMachine extends StorageMultiblockMachine implements IManaMultiblock, IMultiStructureMachine, ICustomHighlightMachine {

    private final int basic_rate;

    private final ManaTrait manaTrait;

    private final BlockPattern patternInSpace;

    private int refreshSky = 0;
    private boolean canSeeSky;

    @DescSynced
    private BlockPos highlightStartPos = BlockPos.ZERO;
    @DescSynced
    private BlockPos highlightEndPos = BlockPos.ZERO;

    public PhotovoltaicPowerStationMachine(MetaMachineBlockEntity holder, int basicRate, Supplier<? extends Block> casing, BlockEntry<?> photovoltaicBlock) {
        super(holder, 64, i -> i.getItem() == BotaniaBlocks.motifDaybloom.asItem());
        basic_rate = basicRate;
        this.manaTrait = new ManaTrait(this);
        this.patternInSpace = getPatternInSpace(getDefinition(), casing, photovoltaicBlock);
    }

    @Override
    public BlockPattern getPattern() {
        if (isInSpace()) {
            return patternInSpace;
        }
        return super.getPattern();
    }

    private boolean isInSpace() {
        Level level = getLevel();
        return level != null && PlanetApi.API.isSpace(level);
    }

    @Override
    public @NotNull ManaContainerList getManaContainer() {
        return manaTrait.getManaContainers();
    }

    @Override
    public boolean isGeneratorMana() {
        return true;
    }

    private boolean canSeeSky(Level level) {
        if (isInSpace()) {
            if (getFrontFacing().getAxis() != Direction.Axis.Y) {
                setIdleReason(INCORRECT_DIRECTION_VOLTA);
                return false;
            }
            BlockPos pos = getPos().above();
            Direction facing = getUpwardsFacing();
            boolean permuteXZ = facing.getAxis() == Direction.Axis.Z;
            int z = !BooleanUtils.xor(new boolean[] { isFlipped(), (getFrontFacing() == Direction.UP) }) ? -11 : 11;
            z *= (facing == Direction.SOUTH || facing == Direction.EAST) ? 1 : -1;
            pos = pos.offset(permuteXZ ? -z : 0, 0, permuteXZ ? 0 : z);
            highlightStartPos = pos.offset(permuteXZ ? -7 : -2, 0, permuteXZ ? -2 : -7);
            highlightEndPos = pos.offset(permuteXZ ? 7 : 2, 0, permuteXZ ? 2 : 7);
            for (int x0 = highlightStartPos.getX(); x0 <= highlightEndPos.getX(); x0++) {
                for (int z0 = highlightStartPos.getZ(); z0 <= highlightEndPos.getZ(); z0++) {
                    BlockPos checkPos = new BlockPos(x0, pos.getY(), z0);
                    if (!level.canSeeSky(checkPos)) {
                        setIdleReason(OBSTRUCTED_VOLTA);
                        return false;
                    }
                }
            }
        } else {
            if (getFrontFacing().getAxis() == Direction.Axis.Y || getUpwardsFacing() != Direction.NORTH) {
                setIdleReason(INCORRECT_DIRECTION_VOLTA);
                return false;
            }
            BlockPos pos = MachineUtils.getOffsetPos(1, 5, getFrontFacing(), getPos());
            highlightStartPos = pos.offset(-3, 0, -3);
            highlightEndPos = pos.offset(3, 0, 3);
            for (int i = -3; i < 4; i++) {
                for (int j = -3; j < 4; j++) {
                    if (!level.canSeeSky(pos.offset(i, 0, j))) {
                        setIdleReason(OBSTRUCTED_VOLTA);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean keepSubscribing() {
        return true;
    }

    @Nullable
    private Recipe getRecipe() {
        Level level = getLevel();
        if (level != null) {
            boolean canSeeSky;
            if (refreshSky > 0) {
                refreshSky--;
                canSeeSky = this.canSeeSky;
            } else {
                this.canSeeSky = canSeeSky = canSeeSky(level);
                refreshSky = 10;
            }
            if (!canSeeSky) return null;
            int eut;
            int basic = (int) (basic_rate * PlanetApi.API.getSolarPower(level));
            if (PlanetApi.API.isSpace(level)) {
                eut = inputFluid(GTMaterials.DistilledWater.getFluid(), basic / 4) ? basic << 4 : 0;
                if (eut == 0) ((IEnhancedRecipeLogic) getRecipeLogic()).gtolib$setIdleReason(Component.translatable("gtceu.recipe_logic.insufficient_in").append(": ").append(GTMaterials.DistilledWater.getLocalizedName()));
            } else {
                eut = (int) (basic * (GTODimensions.isVoid(level.dimension().location()) ? 14 : GTOUtils.getSunIntensity(level.getDayTime()) * 15 / 100 * (level.isRaining() ? (level.isThundering() ? 0.3f : 0.7f) : 1)));
                if (eut == 0) ((IEnhancedRecipeLogic) getRecipeLogic()).gtolib$setIdleReason(Component.translatable("recipe.condition.daytime.day.tooltip"));
            }
            if (eut == 0) return null;
            var builder = getRecipeBuilder().duration(20);
            if (getStorageStack().getCount() == 64) {
                builder.MANAt(-eut);
            } else {
                builder.EUt(-eut);
            }
            Recipe recipe = builder.buildRawRecipe();
            if (RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        }
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    public static BlockPattern getPatternCommon(MultiblockMachineDefinition definition, Supplier<? extends Block> casing, BlockEntry<?> photovoltaicBlock) {
        return FactoryBlockPattern.start(definition, RelativeDirection.BACK, RelativeDirection.UP, RelativeDirection.LEFT)
                .aisle("       ", "       ", "       ", "       ", "AAAAAAA")
                .aisle("       ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("   D   ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("   D   ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("  ~CD  ", "   C   ", "   C   ", " AACAA ", "ABBCBBA")
                .aisle("   D   ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("   D   ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("       ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("       ", "       ", "       ", "       ", "AAAAAAA")
                .where('A', frames(GTMaterials.Aluminium))
                .where('B', blocks(photovoltaicBlock.get()))
                .where('C', blocks(casing.get()))
                .where('D', blocks(casing.get())
                        .or(Predicates.blocks(CONTROL_HATCH.getBlock()).setMaxGlobalLimited(1).setPreviewCount(0))
                        .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1))
                        .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(1))
                        .or(abilities(GTOPartAbility.OUTPUT_MANA).setMaxGlobalLimited(4))
                        .or(abilities(MAINTENANCE).setExactLimit(1)))
                .where('~', controller(blocks(definition.get())))
                .where(' ', any())
                .build();
    }

    public static MultiblockShapeInfo getPatternCommonPreview(MultiblockMachineDefinition definition, Supplier<? extends Block> casing, BlockEntry<?> photovoltaicBlock) {
        return MultiblockShapeInfo.builder()
                .aisle("       ", "       ", "       ", "       ", "AAAAAAA")
                .aisle("       ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("   q   ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("   p   ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("  ~CC  ", "   C   ", "   C   ", " AACAA ", "ABBCBBA")
                .aisle("   n   ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("   m   ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("       ", "       ", "       ", "       ", "ABBCBBA")
                .aisle("       ", "       ", "       ", "       ", "AAAAAAA")
                .where('A', ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Aluminium))
                .where('B', photovoltaicBlock)
                .where('C', casing)
                .where('m', WIRELESS_MANA_OUTPUT_HATCH[HV], Direction.WEST)
                .where('n', ENERGY_OUTPUT_HATCH_16A[HV], Direction.WEST)
                .where('p', CONTROL_HATCH, Direction.WEST)
                .where('q', MAINTENANCE_HATCH, Direction.WEST)
                .where('~', definition, Direction.WEST)
                .where(' ', AIR)
                .build();
    }

    public static BlockPattern getPatternInSpace(MultiblockMachineDefinition definition, Supplier<? extends Block> casing, BlockEntry<?> photovoltaicBlock) {
        return FactoryBlockPattern.start(definition, RelativeDirection.UP, RelativeDirection.BACK, RelativeDirection.RIGHT)
                .aisle("AAAAA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("AAAAA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("AAAAA")
                .aisle("C   C")
                .aisle("CC CC")
                .aisle(" CDC ")
                .where('A', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Aluminium)))
                .where('B', blocks(photovoltaicBlock.get()))
                .where('C', blocks(casing.get())
                        .or(Predicates.blocks(CONTROL_HATCH.getBlock()).setMaxGlobalLimited(1).setPreviewCount(0))
                        .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1))
                        .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(1))
                        .or(abilities(GTOPartAbility.OUTPUT_MANA).setMaxGlobalLimited(4))
                        .or(abilities(MAINTENANCE).setExactLimit(1)))
                .where('D', controller(blocks(definition.get())))
                .where(' ', any())
                .build();
    }

    public static MultiblockShapeInfo getPatternInSpacePreview(MultiblockMachineDefinition definition, Supplier<? extends Block> casing, BlockEntry<?> photovoltaicBlock) {
        return MultiblockShapeInfo.builder()
                .aisle("AAAAA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("AAAAA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("ABBBA")
                .aisle("AAAAA")
                .aisle("C   o")
                .aisle("mn pq")
                .aisle(" CDC ")
                .where('A', ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Aluminium))
                .where('B', photovoltaicBlock)
                .where('C', casing)
                .where('m', WIRELESS_MANA_OUTPUT_HATCH[HV], Direction.UP)
                .where('n', ENERGY_OUTPUT_HATCH_16A[HV], Direction.UP)
                .where('o', FLUID_IMPORT_HATCH[HV], Direction.UP)
                .where('p', CONTROL_HATCH, Direction.UP)
                .where('q', MAINTENANCE_HATCH, Direction.UP)
                .where('D', definition.defaultBlockState()
                        .setValue(MetaMachineBlock.UPWARDS_FACING_PROPERTY, Direction.EAST)
                        .setValue(DirectionalBlock.FACING, Direction.SOUTH))
                .where(' ', AIR)
                .build();
    }

    @Override
    public List<BlockPattern> getMultiPattern() {
        return List.of(super.getPattern(), patternInSpace);
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        attachHighlightConfigurators(configuratorPanel);
    }

    @Override
    public List<ForgeClientEvent.HighlightNeed> getCustomHighlights() {
        return List.of(new ForgeClientEvent.HighlightNeed(highlightStartPos, highlightEndPos, ChatFormatting.YELLOW.getColor()));
    }

    @Override
    public List<Component> getHighlightText() {
        return List.of(Component.translatable("gtocore.machine.highlight_obstruction"));
    }
}
