package com.gto.gtocore.api.pattern;

import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.common.data.GCyMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public class DebugBlockPattern {

    public static final Map<Block, String> BLOCK_MAP = new HashMap<>();
    public RelativeDirection[] structureDir;
    public String[][] pattern;
    public int[][] aisleRepetitions;
    private final Map<Character, Set<String>> symbolMap = new HashMap<>();
    public final Map<Block, Character> legend = new LinkedHashMap<>();

    public DebugBlockPattern() {
        structureDir = new RelativeDirection[] {
                RelativeDirection.LEFT, RelativeDirection.UP, RelativeDirection.FRONT
        };
    }

    public DebugBlockPattern(Level world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this();
        pattern = new String[1 + maxX - minX][1 + maxY - minY];
        aisleRepetitions = new int[pattern.length][2];
        for (int[] aisleRepetition : aisleRepetitions) {
            aisleRepetition[0] = 1;
            aisleRepetition[1] = 1;
        }

        legend.put(Blocks.AIR, ' ');

        char c = 'A'; // auto

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                StringBuilder builder = new StringBuilder();
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (!legend.containsKey(block)) {
                        legend.put(block, c);
                        String name = String.valueOf(c);
                        symbolMap.computeIfAbsent(c, key -> new HashSet<>()).add(name); // any
                        c++;
                    }
                    builder.append(legend.get(block));
                }
                pattern[x - minX][y - minY] = builder.toString();
            }
        }
        var dirs = getDir(Direction.NORTH);
        changeDir(dirs[0], dirs[1], dirs[2]);
    }

    public static RelativeDirection[] getDir(Direction facing) {
        if (facing == Direction.WEST) {
            return new RelativeDirection[] {
                    RelativeDirection.LEFT, RelativeDirection.UP, RelativeDirection.BACK
            };
        } else if (facing == Direction.EAST) {
            return new RelativeDirection[] {
                    RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.FRONT
            };
        } else if (facing == Direction.NORTH) {
            return new RelativeDirection[] {
                    RelativeDirection.BACK, RelativeDirection.UP, RelativeDirection.RIGHT
            };
        } else if (facing == Direction.SOUTH) {
            return new RelativeDirection[] {
                    RelativeDirection.FRONT, RelativeDirection.UP, RelativeDirection.LEFT
            };
        } else if (facing == Direction.DOWN) {
            return new RelativeDirection[] {
                    RelativeDirection.RIGHT, RelativeDirection.BACK, RelativeDirection.UP
            };
        } else {
            return new RelativeDirection[] {
                    RelativeDirection.LEFT, RelativeDirection.FRONT, RelativeDirection.UP
            };
        }
    }

    public void changeDir(RelativeDirection charDir, RelativeDirection stringDir,
                          RelativeDirection aisleDir) {
        if (charDir.isSameAxis(stringDir) || stringDir.isSameAxis(aisleDir) || aisleDir.isSameAxis(charDir)) return;
        char[][][] newPattern = new char[structureDir[0].isSameAxis(aisleDir) ? pattern[0][0].length() :
                structureDir[1].isSameAxis(aisleDir) ? pattern[0].length :
                        pattern.length][structureDir[0].isSameAxis(stringDir) ? pattern[0][0].length() :
                                structureDir[1].isSameAxis(stringDir) ? pattern[0].length :
                                        pattern.length][structureDir[0].isSameAxis(charDir) ? pattern[0][0].length() :
                                                structureDir[1].isSameAxis(charDir) ? pattern[0].length :
                                                        pattern.length];
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[0].length; j++) {
                for (int k = 0; k < pattern[0][0].length(); k++) {
                    char c = pattern[i][j].charAt(k);
                    int x = 0, y = 0, z = 0;
                    if (structureDir[2].isSameAxis(aisleDir)) {
                        if (structureDir[2] == aisleDir) {
                            x = i;
                        } else {
                            x = pattern.length - i - 1;
                        }
                    } else if (structureDir[2].isSameAxis(stringDir)) {
                        if (structureDir[2] == stringDir) {
                            y = i;
                        } else {
                            y = pattern.length - i - 1;
                        }
                    } else if (structureDir[2].isSameAxis(charDir)) {
                        if (structureDir[2] == charDir) {
                            z = i;
                        } else {
                            z = pattern.length - i - 1;
                        }
                    }

                    if (structureDir[1].isSameAxis(aisleDir)) {
                        if (structureDir[1] == aisleDir) {
                            x = j;
                        } else {
                            x = pattern[0].length - j - 1;
                        }
                    } else if (structureDir[1].isSameAxis(stringDir)) {
                        if (structureDir[1] == stringDir) {
                            y = j;
                        } else {
                            y = pattern[0].length - j - 1;
                        }
                    } else if (structureDir[1].isSameAxis(charDir)) {
                        if (structureDir[1] == charDir) {
                            z = j;
                        } else {
                            z = pattern[0].length - j - 1;
                        }
                    }

                    if (structureDir[0].isSameAxis(aisleDir)) {
                        if (structureDir[0] == aisleDir) {
                            x = k;
                        } else {
                            x = pattern[0][0].length() - k - 1;
                        }
                    } else if (structureDir[0].isSameAxis(stringDir)) {
                        if (structureDir[0] == stringDir) {
                            y = k;
                        } else {
                            y = pattern[0][0].length() - k - 1;
                        }
                    } else if (structureDir[0].isSameAxis(charDir)) {
                        if (structureDir[0] == charDir) {
                            z = k;
                        } else {
                            z = pattern[0][0].length() - k - 1;
                        }
                    }
                    newPattern[x][y][z] = c;
                }
            }
        }

        pattern = new String[newPattern.length][newPattern[0].length];
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[0].length; j++) {
                StringBuilder builder = new StringBuilder();
                for (char c : newPattern[i][j]) {
                    builder.append(c);
                }
                pattern[i][j] = builder.toString();
            }
        }

        aisleRepetitions = new int[pattern.length][2];
        for (int[] aisleRepetition : aisleRepetitions) {
            aisleRepetition[0] = 1;
            aisleRepetition[1] = 1;
        }

        structureDir = new RelativeDirection[] { charDir, stringDir, aisleDir };
    }

    public DebugBlockPattern copy() {
        DebugBlockPattern newPattern = new DebugBlockPattern();
        System.arraycopy(this.structureDir, 0, newPattern.structureDir, 0, this.structureDir.length);

        newPattern.pattern = new String[pattern.length][pattern[0].length];
        for (int i = 0; i < pattern.length; i++) {
            System.arraycopy(pattern[i], 0, newPattern.pattern[i], 0, pattern[i].length);
        }

        newPattern.aisleRepetitions = new int[aisleRepetitions.length][2];
        for (int i = 0; i < aisleRepetitions.length; i++) {
            System.arraycopy(
                    aisleRepetitions[i], 0, newPattern.aisleRepetitions[i], 0, aisleRepetitions[i].length);
        }

        symbolMap.forEach((k, v) -> newPattern.symbolMap.put(k, new HashSet<>(v)));

        return newPattern;
    }

    static {
        BLOCK_MAP.put(GTBlocks.LD_ITEM_PIPE.get(), "GTBlocks.LD_ITEM_PIPE");
        BLOCK_MAP.put(GTBlocks.LD_FLUID_PIPE.get(), "GTBlocks.LD_FLUID_PIPE");
        BLOCK_MAP.put(GTBlocks.CASING_WOOD_WALL.get(), "GTBlocks.CASING_WOOD_WALL");
        BLOCK_MAP.put(GTBlocks.CASING_COKE_BRICKS.get(), "GTBlocks.CASING_COKE_BRICKS");
        BLOCK_MAP.put(GTBlocks.CASING_PRIMITIVE_BRICKS.get(), "GTBlocks.CASING_PRIMITIVE_BRICKS");
        BLOCK_MAP.put(GTBlocks.CASING_BRONZE_BRICKS.get(), "GTBlocks.CASING_BRONZE_BRICKS");
        BLOCK_MAP.put(GTBlocks.CASING_INVAR_HEATPROOF.get(), "GTBlocks.CASING_INVAR_HEATPROOF");
        BLOCK_MAP.put(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get(), "GTBlocks.CASING_ALUMINIUM_FROSTPROOF");
        BLOCK_MAP.put(GTBlocks.CASING_STEEL_SOLID.get(), "GTBlocks.CASING_STEEL_SOLID");
        BLOCK_MAP.put(GTBlocks.CASING_STAINLESS_CLEAN.get(), "GTBlocks.CASING_STAINLESS_CLEAN");
        BLOCK_MAP.put(GTBlocks.CASING_TITANIUM_STABLE.get(), "GTBlocks.CASING_TITANIUM_STABLE");
        BLOCK_MAP.put(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get(), "GTBlocks.CASING_TUNGSTENSTEEL_ROBUST");
        BLOCK_MAP.put(GTBlocks.CASING_PTFE_INERT.get(), "GTBlocks.CASING_PTFE_INERT");
        BLOCK_MAP.put(GTBlocks.CASING_HSSE_STURDY.get(), "GTBlocks.CASING_HSSE_STURDY");
        BLOCK_MAP.put(GTBlocks.CASING_PALLADIUM_SUBSTATION.get(), "GTBlocks.CASING_PALLADIUM_SUBSTATION");
        BLOCK_MAP.put(GTBlocks.CASING_TEMPERED_GLASS.get(), "GTBlocks.CASING_TEMPERED_GLASS");
        BLOCK_MAP.put(GTBlocks.CASING_STAINLESS_EVAPORATION.get(), "GTBlocks.CASING_STAINLESS_EVAPORATION");
        BLOCK_MAP.put(GTBlocks.CASING_GRATE.get(), "GTBlocks.CASING_GRATE");
        BLOCK_MAP.put(GTBlocks.CASING_ASSEMBLY_CONTROL.get(), "GTBlocks.CASING_ASSEMBLY_CONTROL");
        BLOCK_MAP.put(GTBlocks.CASING_LAMINATED_GLASS.get(), "GTBlocks.CASING_LAMINATED_GLASS");
        BLOCK_MAP.put(GTBlocks.CASING_ASSEMBLY_LINE.get(), "GTBlocks.CASING_ASSEMBLY_LINE");
        BLOCK_MAP.put(GTBlocks.CASING_BRONZE_GEARBOX.get(), "GTBlocks.CASING_BRONZE_GEARBOX");
        BLOCK_MAP.put(GTBlocks.CASING_STEEL_GEARBOX.get(), "GTBlocks.CASING_STEEL_GEARBOX");
        BLOCK_MAP.put(GTBlocks.CASING_STAINLESS_STEEL_GEARBOX.get(), "GTBlocks.CASING_STAINLESS_STEEL_GEARBOX");
        BLOCK_MAP.put(GTBlocks.CASING_TITANIUM_GEARBOX.get(), "GTBlocks.CASING_TITANIUM_GEARBOX");
        BLOCK_MAP.put(GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get(), "GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX");
        BLOCK_MAP.put(GTBlocks.CASING_STEEL_TURBINE.get(), "GTBlocks.CASING_STEEL_TURBINE");
        BLOCK_MAP.put(GTBlocks.CASING_TITANIUM_TURBINE.get(), "GTBlocks.CASING_TITANIUM_TURBINE");
        BLOCK_MAP.put(GTBlocks.CASING_STAINLESS_TURBINE.get(), "GTBlocks.CASING_STAINLESS_TURBINE");
        BLOCK_MAP.put(GTBlocks.CASING_TUNGSTENSTEEL_TURBINE.get(), "GTBlocks.CASING_TUNGSTENSTEEL_TURBINE");
        BLOCK_MAP.put(GTBlocks.CASING_BRONZE_PIPE.get(), "GTBlocks.CASING_BRONZE_PIPE");
        BLOCK_MAP.put(GTBlocks.CASING_STEEL_PIPE.get(), "GTBlocks.CASING_STEEL_PIPE");
        BLOCK_MAP.put(GTBlocks.CASING_TITANIUM_PIPE.get(), "GTBlocks.CASING_TITANIUM_PIPE");
        BLOCK_MAP.put(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get(), "GTBlocks.CASING_TUNGSTENSTEEL_PIPE");
        BLOCK_MAP.put(GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE.get(), "GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE");
        BLOCK_MAP.put(GTBlocks.MINER_PIPE.get(), "GTBlocks.MINER_PIPE");
        BLOCK_MAP.put(GTBlocks.CASING_PUMP_DECK.get(), "GTBlocks.CASING_PUMP_DECK");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_ULV.get(), "GTBlocks.MACHINE_CASING_ULV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_LV.get(), "GTBlocks.MACHINE_CASING_LV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_MV.get(), "GTBlocks.MACHINE_CASING_MV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_HV.get(), "GTBlocks.MACHINE_CASING_HV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_EV.get(), "GTBlocks.MACHINE_CASING_EV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_IV.get(), "GTBlocks.MACHINE_CASING_IV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_LuV.get(), "GTBlocks.MACHINE_CASING_LuV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_ZPM.get(), "GTBlocks.MACHINE_CASING_ZPM");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_UV.get(), "GTBlocks.MACHINE_CASING_UV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_UHV.get(), "GTBlocks.MACHINE_CASING_UHV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_UEV.get(), "GTBlocks.MACHINE_CASING_UEV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_UIV.get(), "GTBlocks.MACHINE_CASING_UIV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_UXV.get(), "GTBlocks.MACHINE_CASING_UXV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_OpV.get(), "GTBlocks.MACHINE_CASING_OpV");
        BLOCK_MAP.put(GTBlocks.MACHINE_CASING_MAX.get(), "GTBlocks.MACHINE_CASING_MAX");
        BLOCK_MAP.put(GTBlocks.HERMETIC_CASING_LV.get(), "GTBlocks.HERMETIC_CASING_LV");
        BLOCK_MAP.put(GTBlocks.HERMETIC_CASING_MV.get(), "GTBlocks.HERMETIC_CASING_MV");
        BLOCK_MAP.put(GTBlocks.HERMETIC_CASING_HV.get(), "GTBlocks.HERMETIC_CASING_HV");
        BLOCK_MAP.put(GTBlocks.HERMETIC_CASING_EV.get(), "GTBlocks.HERMETIC_CASING_EV");
        BLOCK_MAP.put(GTBlocks.HERMETIC_CASING_IV.get(), "GTBlocks.HERMETIC_CASING_IV");
        BLOCK_MAP.put(GTBlocks.HERMETIC_CASING_LuV.get(), "GTBlocks.HERMETIC_CASING_LuV");
        BLOCK_MAP.put(GTBlocks.HERMETIC_CASING_ZPM.get(), "GTBlocks.HERMETIC_CASING_ZPM");
        BLOCK_MAP.put(GTBlocks.HERMETIC_CASING_UV.get(), "GTBlocks.HERMETIC_CASING_UV");
        BLOCK_MAP.put(GTBlocks.HERMETIC_CASING_UHV.get(), "GTBlocks.HERMETIC_CASING_UHV");
        BLOCK_MAP.put(GTBlocks.BRONZE_HULL.get(), "GTBlocks.BRONZE_HULL");
        BLOCK_MAP.put(GTBlocks.BRONZE_BRICKS_HULL.get(), "GTBlocks.BRONZE_BRICKS_HULL");
        BLOCK_MAP.put(GTBlocks.STEEL_HULL.get(), "GTBlocks.STEEL_HULL");
        BLOCK_MAP.put(GTBlocks.STEEL_BRICKS_HULL.get(), "GTBlocks.STEEL_BRICKS_HULL");
        BLOCK_MAP.put(GTBlocks.COIL_CUPRONICKEL.get(), "GTBlocks.COIL_CUPRONICKEL");
        BLOCK_MAP.put(GTBlocks.COIL_KANTHAL.get(), "GTBlocks.COIL_KANTHAL");
        BLOCK_MAP.put(GTBlocks.COIL_NICHROME.get(), "GTBlocks.COIL_NICHROME");
        BLOCK_MAP.put(GTBlocks.COIL_RTMALLOY.get(), "GTBlocks.COIL_RTMALLOY");
        BLOCK_MAP.put(GTBlocks.COIL_HSSG.get(), "GTBlocks.COIL_HSSG");
        BLOCK_MAP.put(GTBlocks.COIL_NAQUADAH.get(), "GTBlocks.COIL_NAQUADAH");
        BLOCK_MAP.put(GTBlocks.COIL_TRINIUM.get(), "GTBlocks.COIL_TRINIUM");
        BLOCK_MAP.put(GTBlocks.COIL_TRITANIUM.get(), "GTBlocks.COIL_TRITANIUM");
        BLOCK_MAP.put(GTBlocks.BATTERY_EMPTY_TIER_I.get(), "GTBlocks.BATTERY_EMPTY_TIER_I");
        BLOCK_MAP.put(GTBlocks.BATTERY_LAPOTRONIC_EV.get(), "GTBlocks.BATTERY_LAPOTRONIC_EV");
        BLOCK_MAP.put(GTBlocks.BATTERY_LAPOTRONIC_IV.get(), "GTBlocks.BATTERY_LAPOTRONIC_IV");
        BLOCK_MAP.put(GTBlocks.BATTERY_EMPTY_TIER_II.get(), "GTBlocks.BATTERY_EMPTY_TIER_II");
        BLOCK_MAP.put(GTBlocks.BATTERY_LAPOTRONIC_LuV.get(), "GTBlocks.BATTERY_LAPOTRONIC_LuV");
        BLOCK_MAP.put(GTBlocks.BATTERY_LAPOTRONIC_ZPM.get(), "GTBlocks.BATTERY_LAPOTRONIC_ZPM");
        BLOCK_MAP.put(GTBlocks.BATTERY_EMPTY_TIER_III.get(), "GTBlocks.BATTERY_EMPTY_TIER_III");
        BLOCK_MAP.put(GTBlocks.BATTERY_LAPOTRONIC_UV.get(), "GTBlocks.BATTERY_LAPOTRONIC_UV");
        BLOCK_MAP.put(GTBlocks.BATTERY_ULTIMATE_UHV.get(), "GTBlocks.BATTERY_ULTIMATE_UHV");
        BLOCK_MAP.put(GTBlocks.CASING_ENGINE_INTAKE.get(), "GTBlocks.CASING_ENGINE_INTAKE");
        BLOCK_MAP.put(GTBlocks.CASING_EXTREME_ENGINE_INTAKE.get(), "GTBlocks.CASING_EXTREME_ENGINE_INTAKE");
        BLOCK_MAP.put(GTBlocks.SUPERCONDUCTING_COIL.get(), "GTBlocks.SUPERCONDUCTING_COIL");
        BLOCK_MAP.put(GTBlocks.FUSION_COIL.get(), "GTBlocks.FUSION_COIL");
        BLOCK_MAP.put(GTBlocks.FUSION_CASING.get(), "GTBlocks.FUSION_CASING");
        BLOCK_MAP.put(GTBlocks.FUSION_CASING_MK2.get(), "GTBlocks.FUSION_CASING_MK2");
        BLOCK_MAP.put(GTBlocks.FUSION_CASING_MK3.get(), "GTBlocks.FUSION_CASING_MK3");
        BLOCK_MAP.put(GTBlocks.FUSION_GLASS.get(), "GTBlocks.FUSION_GLASS");
        BLOCK_MAP.put(GTBlocks.PLASTCRETE.get(), "GTBlocks.PLASTCRETE");
        BLOCK_MAP.put(GTBlocks.FILTER_CASING.get(), "GTBlocks.FILTER_CASING");
        BLOCK_MAP.put(GTBlocks.FILTER_CASING_STERILE.get(), "GTBlocks.FILTER_CASING_STERILE");
        BLOCK_MAP.put(GTBlocks.CLEANROOM_GLASS.get(), "GTBlocks.CLEANROOM_GLASS");
        BLOCK_MAP.put(GTBlocks.FIREBOX_BRONZE.get(), "GTBlocks.FIREBOX_BRONZE");
        BLOCK_MAP.put(GTBlocks.FIREBOX_STEEL.get(), "GTBlocks.FIREBOX_STEEL");
        BLOCK_MAP.put(GTBlocks.FIREBOX_TITANIUM.get(), "GTBlocks.FIREBOX_TITANIUM");
        BLOCK_MAP.put(GTBlocks.FIREBOX_TUNGSTENSTEEL.get(), "GTBlocks.FIREBOX_TUNGSTENSTEEL");
        BLOCK_MAP.put(GTBlocks.COMPUTER_CASING.get(), "GTBlocks.COMPUTER_CASING");
        BLOCK_MAP.put(GTBlocks.ADVANCED_COMPUTER_CASING.get(), "GTBlocks.ADVANCED_COMPUTER_CASING");
        BLOCK_MAP.put(GTBlocks.COMPUTER_HEAT_VENT.get(), "GTBlocks.COMPUTER_HEAT_VENT");
        BLOCK_MAP.put(GTBlocks.HIGH_POWER_CASING.get(), "GTBlocks.HIGH_POWER_CASING");
        BLOCK_MAP.put(GTBlocks.POWDERBARREL.get(), "GTBlocks.POWDERBARREL");
        BLOCK_MAP.put(GTBlocks.INDUSTRIAL_TNT.get(), "GTBlocks.INDUSTRIAL_TNT");
        BLOCK_MAP.put(GTBlocks.RUBBER_SAPLING.get(), "GTBlocks.RUBBER_SAPLING");
        BLOCK_MAP.put(GTBlocks.RUBBER_LOG.get(), "GTBlocks.RUBBER_LOG");
        BLOCK_MAP.put(GTBlocks.RUBBER_LEAVES.get(), "GTBlocks.RUBBER_LEAVES");
        BLOCK_MAP.put(GTBlocks.RUBBER_WOOD.get(), "GTBlocks.RUBBER_WOOD");
        BLOCK_MAP.put(GTBlocks.RUBBER_PLANK.get(), "GTBlocks.RUBBER_PLANK");
        BLOCK_MAP.put(GTBlocks.RUBBER_SLAB.get(), "GTBlocks.RUBBER_SLAB");
        BLOCK_MAP.put(GTBlocks.RUBBER_FENCE.get(), "GTBlocks.RUBBER_FENCE");
        BLOCK_MAP.put(GTBlocks.RUBBER_SIGN.get(), "GTBlocks.RUBBER_SIGN");
        BLOCK_MAP.put(GTBlocks.RUBBER_WALL_SIGN.get(), "GTBlocks.RUBBER_WALL_SIGN");
        BLOCK_MAP.put(GTBlocks.RUBBER_HANGING_SIGN.get(), "GTBlocks.RUBBER_HANGING_SIGN");
        BLOCK_MAP.put(GTBlocks.RUBBER_WALL_HANGING_SIGN.get(), "GTBlocks.RUBBER_WALL_HANGING_SIGN");
        BLOCK_MAP.put(GTBlocks.RUBBER_PRESSURE_PLATE.get(), "GTBlocks.RUBBER_PRESSURE_PLATE");
        BLOCK_MAP.put(GTBlocks.RUBBER_TRAPDOOR.get(), "GTBlocks.RUBBER_TRAPDOOR");
        BLOCK_MAP.put(GTBlocks.RUBBER_STAIRS.get(), "GTBlocks.RUBBER_STAIRS");
        BLOCK_MAP.put(GTBlocks.RUBBER_BUTTON.get(), "GTBlocks.RUBBER_BUTTON");
        BLOCK_MAP.put(GTBlocks.RUBBER_FENCE_GATE.get(), "GTBlocks.RUBBER_FENCE_GATE");
        BLOCK_MAP.put(GTBlocks.RUBBER_DOOR.get(), "GTBlocks.RUBBER_DOOR");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_PLANK.get(), "GTBlocks.TREATED_WOOD_PLANK");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_SLAB.get(), "GTBlocks.TREATED_WOOD_SLAB");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_FENCE.get(), "GTBlocks.TREATED_WOOD_FENCE");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_SIGN.get(), "GTBlocks.TREATED_WOOD_SIGN");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_WALL_SIGN.get(), "GTBlocks.TREATED_WOOD_WALL_SIGN");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_HANGING_SIGN.get(), "GTBlocks.TREATED_WOOD_HANGING_SIGN");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_WALL_HANGING_SIGN.get(), "GTBlocks.TREATED_WOOD_WALL_HANGING_SIGN");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_PRESSURE_PLATE.get(), "GTBlocks.TREATED_WOOD_PRESSURE_PLATE");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_TRAPDOOR.get(), "GTBlocks.TREATED_WOOD_TRAPDOOR");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_STAIRS.get(), "GTBlocks.TREATED_WOOD_STAIRS");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_BUTTON.get(), "GTBlocks.TREATED_WOOD_BUTTON");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_FENCE_GATE.get(), "GTBlocks.TREATED_WOOD_FENCE_GATE");
        BLOCK_MAP.put(GTBlocks.TREATED_WOOD_DOOR.get(), "GTBlocks.TREATED_WOOD_DOOR");
        BLOCK_MAP.put(GTBlocks.ACID_HAZARD_SIGN_BLOCK.get(), "GTBlocks.ACID_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.ANTIMATTER_HAZARD_SIGN_BLOCK.get(), "GTBlocks.ANTIMATTER_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.BIO_HAZARD_SIGN_BLOCK.get(), "GTBlocks.BIO_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.BOSS_HAZARD_SIGN_BLOCK.get(), "GTBlocks.BOSS_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.CAUSALITY_HAZARD_SIGN_BLOCK.get(), "GTBlocks.CAUSALITY_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.EXPLOSION_HAZARD_SIGN_BLOCK.get(), "GTBlocks.EXPLOSION_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.FIRE_HAZARD_SIGN_BLOCK.get(), "GTBlocks.FIRE_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.FROST_HAZARD_SIGN_BLOCK.get(), "GTBlocks.FROST_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.GENERIC_HAZARD_SIGN_BLOCK.get(), "GTBlocks.GENERIC_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.GREGIFICATION_HAZARD_SIGN_BLOCK.get(), "GTBlocks.GREGIFICATION_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.HIGH_PRESSURE_HAZARD_SIGN_BLOCK.get(), "GTBlocks.HIGH_PRESSURE_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.HIGH_VOLTAGE_HAZARD_SIGN_BLOCK.get(), "GTBlocks.HIGH_VOLTAGE_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.HIGH_TEMPERATURE_HAZARD_SIGN_BLOCK.get(), "GTBlocks.HIGH_TEMPERATURE_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.LASER_HAZARD_SIGN_BLOCK.get(), "GTBlocks.LASER_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.MAGIC_HAZARD_SIGN_BLOCK.get(), "GTBlocks.MAGIC_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.MAGNETIC_HAZARD_SIGN_BLOCK.get(), "GTBlocks.MAGNETIC_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.MOB_INFESTATION_HAZARD_SIGN_BLOCK.get(), "GTBlocks.MOB_INFESTATION_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.MOB_SPAWNER_HAZARD_SIGN_BLOCK.get(), "GTBlocks.MOB_SPAWNER_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.NOISE_HAZARD_SIGN_BLOCK.get(), "GTBlocks.NOISE_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.RADIOACTIVE_HAZARD_SIGN_BLOCK.get(), "GTBlocks.RADIOACTIVE_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.SPATIAL_STORAGE_HAZARD_SIGN_BLOCK.get(), "GTBlocks.SPATIAL_STORAGE_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.TURRET_HAZARD_SIGN_BLOCK.get(), "GTBlocks.TURRET_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.VOID_HAZARD_SIGN_BLOCK.get(), "GTBlocks.VOID_HAZARD_SIGN_BLOCK");
        BLOCK_MAP.put(GTBlocks.YELLOW_STRIPES_BLOCK_A.get(), "GTBlocks.YELLOW_STRIPES_BLOCK_A");
        BLOCK_MAP.put(GTBlocks.YELLOW_STRIPES_BLOCK_B.get(), "GTBlocks.YELLOW_STRIPES_BLOCK_B");
        BLOCK_MAP.put(GTBlocks.RED_GRANITE.get(), "GTBlocks.RED_GRANITE");
        BLOCK_MAP.put(GTBlocks.MARBLE.get(), "GTBlocks.MARBLE");
        BLOCK_MAP.put(GTBlocks.LIGHT_CONCRETE.get(), "GTBlocks.LIGHT_CONCRETE");
        BLOCK_MAP.put(GTBlocks.DARK_CONCRETE.get(), "GTBlocks.DARK_CONCRETE");
        BLOCK_MAP.put(GTBlocks.BRITTLE_CHARCOAL.get(), "GTBlocks.BRITTLE_CHARCOAL");
        BLOCK_MAP.put(GTBlocks.FOAM.get(), "GTBlocks.FOAM");
        BLOCK_MAP.put(GTBlocks.REINFORCED_FOAM.get(), "GTBlocks.REINFORCED_FOAM");
        BLOCK_MAP.put(GTBlocks.PETRIFIED_FOAM.get(), "GTBlocks.PETRIFIED_FOAM");
        BLOCK_MAP.put(GTBlocks.REINFORCED_STONE.get(), "GTBlocks.REINFORCED_STONE");

        BLOCK_MAP.put(GCyMBlocks.CASING_NONCONDUCTING.get(), "GCyMBlocks.CASING_NONCONDUCTING");
        BLOCK_MAP.put(GCyMBlocks.CASING_VIBRATION_SAFE.get(), "GCyMBlocks.CASING_VIBRATION_SAFE");
        BLOCK_MAP.put(GCyMBlocks.CASING_WATERTIGHT.get(), "GCyMBlocks.CASING_WATERTIGHT");
        BLOCK_MAP.put(GCyMBlocks.CASING_SECURE_MACERATION.get(), "GCyMBlocks.CASING_SECURE_MACERATION");
        BLOCK_MAP.put(GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get(), "GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING");
        BLOCK_MAP.put(GCyMBlocks.CASING_LASER_SAFE_ENGRAVING.get(), "GCyMBlocks.CASING_LASER_SAFE_ENGRAVING");
        BLOCK_MAP.put(GCyMBlocks.CASING_LARGE_SCALE_ASSEMBLING.get(), "GCyMBlocks.CASING_LARGE_SCALE_ASSEMBLING");
        BLOCK_MAP.put(GCyMBlocks.CASING_SHOCK_PROOF.get(), "GCyMBlocks.CASING_SHOCK_PROOF");
        BLOCK_MAP.put(GCyMBlocks.CASING_STRESS_PROOF.get(), "GCyMBlocks.CASING_STRESS_PROOF");
        BLOCK_MAP.put(GCyMBlocks.CASING_CORROSION_PROOF.get(), "GCyMBlocks.CASING_CORROSION_PROOF");
        BLOCK_MAP.put(GCyMBlocks.CASING_REACTION_SAFE.get(), "GCyMBlocks.CASING_REACTION_SAFE");
        BLOCK_MAP.put(GCyMBlocks.CASING_ATOMIC.get(), "GCyMBlocks.CASING_ATOMIC");
        BLOCK_MAP.put(GCyMBlocks.CASING_INDUSTRIAL_STEAM.get(), "GCyMBlocks.CASING_INDUSTRIAL_STEAM");

        BLOCK_MAP.put(GCyMBlocks.SLICING_BLADES.get(), "GCyMBlocks.SLICING_BLADES");
        BLOCK_MAP.put(GCyMBlocks.MOLYBDENUM_DISILICIDE_COIL_BLOCK.get(), "GCyMBlocks.MOLYBDENUM_DISILICIDE_COIL_BLOCK");
        BLOCK_MAP.put(GCyMBlocks.ELECTROLYTIC_CELL.get(), "GCyMBlocks.ELECTROLYTIC_CELL");
        BLOCK_MAP.put(GCyMBlocks.CRUSHING_WHEELS.get(), "GCyMBlocks.CRUSHING_WHEELS");
        BLOCK_MAP.put(GCyMBlocks.HEAT_VENT.get(), "GCyMBlocks.HEAT_VENT");
    }
}
