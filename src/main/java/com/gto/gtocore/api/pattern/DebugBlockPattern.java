package com.gto.gtocore.api.pattern;

import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public class DebugBlockPattern {

    public RelativeDirection[] structureDir;
    public String[][] pattern;
    public int[][] aisleRepetitions;
    private final Map<Character, Set<String>> symbolMap;
    public final Map<Block, Character> legend = new LinkedHashMap<>();

    public DebugBlockPattern() {
        symbolMap = new HashMap<>();
        structureDir = new RelativeDirection[] {
                RelativeDirection.LEFT, RelativeDirection.UP, RelativeDirection.FRONT
        };
    }

    public DebugBlockPattern(
                             Level world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
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
}
