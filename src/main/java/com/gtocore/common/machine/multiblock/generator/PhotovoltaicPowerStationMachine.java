package com.gtocore.common.machine.multiblock.generator;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.client.forge.ForgeClientEvent;
import com.gtocore.data.IdleReason;

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

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.tterrag.registrate.util.entry.BlockEntry;
import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.List;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.GTValues.HV;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
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
    private IdleReason idleReason = null;

    @DescSynced
    private BlockPos highlightStartPos_1 = BlockPos.ZERO;
    @DescSynced
    private BlockPos highlightEndPos_1 = BlockPos.ZERO;
    @DescSynced
    private BlockPos highlightStartPos_2 = BlockPos.ZERO;
    @DescSynced
    private BlockPos highlightEndPos_2 = BlockPos.ZERO;

    public PhotovoltaicPowerStationMachine(MetaMachineBlockEntity holder, int basicRate, Supplier<? extends Block> casing, BlockEntry<?> photovoltaicBlock) {
        super(holder, 64, i -> i.getItem() == BotaniaBlocks.motifDaybloom.asItem());
        basic_rate = basicRate;
        this.manaTrait = new ManaTrait(this);
        this.patternInSpace = getPatternInSpace(getDefinition(), casing, photovoltaicBlock);
    }

    @Override
    public boolean handleTickRecipe(@Nullable Recipe recipe) {
        if (recipe != null) {
            long eu = recipe.eut;
            if (eu != 0) {
                if (!generateEnergy(-eu, false)) {
                    IdleReason.setIdleReason(this, IdleReason.INSUFFICIENT_OUT);
                    return false;
                }
            }
            long mana = recipe.manat;
            if (mana != 0) {
                if (!useMana(mana, false)) {
                    IdleReason.setIdleReason(this, IdleReason.INSUFFICIENT_OUT);
                    return false;
                }
            }
        }
        return true;
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
        BlockPos pos;
        if (isInSpace()) {
            if (getFrontFacing().getAxis() == Direction.Axis.Y) {
                setIdleReason(INCORRECT_DIRECTION_VOLTA);
                idleReason = INCORRECT_DIRECTION_VOLTA;
                return false;
            }
            pos = MachineUtils.getOffsetPos(-11, 0, 0, getFrontFacing(), getPos());
            Direction upwards = getFrontFacing();
            boolean permuteXZ = upwards.getAxis() == Direction.Axis.Z;
            if (permuteXZ) {
                highlightStartPos_1 = pos.offset(-1, 0, -7);
                highlightEndPos_1 = pos.offset(1, 0, -1);
                highlightStartPos_2 = pos.offset(-1, 0, 1);
                highlightEndPos_2 = pos.offset(1, 0, 7);
            } else {
                highlightStartPos_1 = pos.offset(-7, 0, -1);
                highlightEndPos_1 = pos.offset(-1, 0, 1);
                highlightStartPos_2 = pos.offset(1, 0, -1);
                highlightEndPos_2 = pos.offset(7, 0, 1);
            }
        } else {
            if (getFrontFacing().getAxis() == Direction.Axis.Y || getUpwardsFacing() != Direction.NORTH) {
                setIdleReason(INCORRECT_DIRECTION_VOLTA);
                idleReason = INCORRECT_DIRECTION_VOLTA;
                return false;
            }
            pos = MachineUtils.getOffsetPos(1, 4, getFrontFacing(), getPos());
            Direction upwards = getFrontFacing();
            boolean permuteXZ = upwards.getAxis() == Direction.Axis.Z;
            if (permuteXZ) {
                highlightStartPos_1 = pos.offset(-3, 0, 1);
                highlightEndPos_1 = pos.offset(3, 0, 2);
                highlightStartPos_2 = pos.offset(-3, 0, -2);
                highlightEndPos_2 = pos.offset(3, 0, -1);
            } else {
                highlightStartPos_1 = pos.offset(1, 0, -3);
                highlightEndPos_1 = pos.offset(2, 0, 3);
                highlightStartPos_2 = pos.offset(-2, 0, -3);
                highlightEndPos_2 = pos.offset(-1, 0, 3);
            }
        }
        for (BlockPos checkPos : BlockPos.betweenClosed(highlightStartPos_1, highlightEndPos_1)) {
            if (!level.canSeeSky(new BlockPos(checkPos.getX(), pos.getY() + 1, checkPos.getZ()))) {
                setIdleReason(OBSTRUCTED_VOLTA);
                idleReason = OBSTRUCTED_VOLTA;
                return false;
            }
        }
        for (BlockPos checkPos : BlockPos.betweenClosed(highlightStartPos_2, highlightEndPos_2)) {
            if (!level.canSeeSky(new BlockPos(checkPos.getX(), pos.getY() + 1, checkPos.getZ()))) {
                setIdleReason(OBSTRUCTED_VOLTA);
                idleReason = OBSTRUCTED_VOLTA;
                return false;
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
            if (!canSeeSky) {
                setIdleReason(idleReason);
                return null;
            }
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
                        .or(Predicates.blocks(CONTROL_HATCH.get()).setMaxGlobalLimited(1).setPreviewCount(0))
                        .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1))
                        .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(1))
                        .or(abilities(GTOPartAbility.OUTPUT_MANA).setMaxGlobalLimited(4))
                        .or(abilities(MAINTENANCE).setExactLimit(1)))
                .where('~', controller(definition))
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
        return FactoryBlockPattern.start(definition)
                .aisle(" CDC ")
                .aisle("CC CC")
                .aisle("C   C")
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
                .where('A', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Aluminium)))
                .where('B', blocks(photovoltaicBlock.get()))
                .where('C', blocks(casing.get())
                        .or(Predicates.blocks(CONTROL_HATCH.get()).setMaxGlobalLimited(1).setPreviewCount(0))
                        .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1))
                        .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(1))
                        .or(abilities(GTOPartAbility.OUTPUT_MANA).setMaxGlobalLimited(4))
                        .or(abilities(MAINTENANCE).setExactLimit(1)))
                .where('D', controller(definition))
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
                .where('D', definition.defaultBlockState())
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
        return List.of(
                new ForgeClientEvent.HighlightNeed(highlightStartPos_1, highlightEndPos_1, ChatFormatting.YELLOW.getColor()),
                new ForgeClientEvent.HighlightNeed(highlightStartPos_2, highlightEndPos_2, ChatFormatting.YELLOW.getColor()));
    }

    @Override
    public List<Component> getHighlightText() {
        return List.of(Component.translatable("gtocore.machine.highlight_obstruction"));
    }
}
