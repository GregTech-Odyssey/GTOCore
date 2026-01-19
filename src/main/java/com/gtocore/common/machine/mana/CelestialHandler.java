package com.gtocore.common.machine.mana;

import com.gtolib.api.data.Dimension;
import com.gtolib.api.data.GTODimensions;

import net.minecraft.world.level.Level;

import earth.terrarium.adastra.api.planets.PlanetApi;

public record CelestialHandler(long maxCapacity) {

    public static final String SOLARIS = "solaris";
    public static final String LUNARA = "lunara";
    public static final String VOIDFLUX = "voidflux";
    public static final String STELLARM = "stellarm";

    public enum Mode {
        VOID,
        OTHERSIDE,
        SPACE,
        ALFHEIM,
        END,
        OVERWORLD
    }

    private long clampToMaxCapacity(long value) {
        return Math.min(maxCapacity, value);
    }

    public Mode initMode(Level world) {
        if (world == null) {
            return Mode.OVERWORLD;
        }
        var dim = world.dimension();

        if (PlanetApi.API.isSpace(world)) {
            return Mode.SPACE;
        } else if (GTODimensions.isVoid(dim)) {
            return Mode.VOID;
        }
        return switch (Dimension.from(dim)) {
            case OTHERSIDE -> Mode.OTHERSIDE;
            case ALFHEIM -> Mode.ALFHEIM;
            case THE_END -> Mode.END;
            default -> Mode.OVERWORLD;
        };
    }

    public Resource increase(Level world, int multiple, long solaris, long lunara, long voidflux, long stellarm, Mode mode) {
        if (world == null) return new Resource(solaris, lunara, voidflux, stellarm);

        switch (mode) {
            case SPACE -> {
                stellarm = clampToMaxCapacity(stellarm + 20L * multiple);
            }
            case VOID -> {
                solaris = clampToMaxCapacity(solaris + 5L * multiple);
                lunara = clampToMaxCapacity(lunara + 5L * multiple);
            }
            case ALFHEIM -> {
                if (world.isDay()) {
                    solaris = clampToMaxCapacity(solaris + 20L * multiple);
                } else if (world.isNight()) {
                    lunara = clampToMaxCapacity(lunara + 20L * multiple);
                }
            }
            case OTHERSIDE -> {
                voidflux = clampToMaxCapacity(voidflux + 50L * multiple);
            }
            case END -> {
                voidflux = clampToMaxCapacity(voidflux + 10L * multiple);
            }
            case OVERWORLD -> {
                if (world.isDay()) {
                    solaris = clampToMaxCapacity(solaris + 10L * multiple);
                } else if (world.isNight()) {
                    lunara = clampToMaxCapacity(lunara + 10L * multiple);
                }
            }
        }
        return new Resource(solaris, lunara, voidflux, stellarm);
    }

    public ResourceResult deductResource(String type, int cost, long parallel, long solaris, long lunara, long voidflux, long stellarm) {
        long totalCost = (long) cost * parallel;
        if (totalCost <= 0) return new ResourceResult(true, solaris, lunara, voidflux, stellarm);

        switch (type) {
            case SOLARIS -> {
                if (solaris < totalCost) return new ResourceResult(false, solaris, lunara, voidflux, stellarm);
                solaris = Math.max(0L, solaris - totalCost);
            }
            case LUNARA -> {
                if (lunara < totalCost) return new ResourceResult(false, solaris, lunara, voidflux, stellarm);
                lunara = Math.max(0L, lunara - totalCost);
            }
            case VOIDFLUX -> {
                if (voidflux < totalCost) return new ResourceResult(false, solaris, lunara, voidflux, stellarm);
                voidflux = Math.max(0L, voidflux - totalCost);
            }
            case STELLARM -> {
                if (stellarm < totalCost) return new ResourceResult(false, solaris, lunara, voidflux, stellarm);
                stellarm = Math.max(0L, stellarm - totalCost);
            }
            case "ANY" -> {
                long remainingCost = totalCost;
                if (remainingCost > 0 && solaris > 0) {
                    long deduct = Math.min(solaris, remainingCost);
                    solaris = Math.max(0, solaris - deduct);
                    remainingCost -= deduct;
                }
                if (remainingCost > 0 && lunara > 0) {
                    long deduct = Math.min(lunara, remainingCost);
                    lunara = Math.max(0, lunara - deduct);
                    remainingCost -= deduct;
                }
                if (remainingCost > 0 && voidflux > 0) {
                    long deduct = Math.min(voidflux, remainingCost);
                    voidflux = Math.max(0, voidflux - deduct);
                    remainingCost -= deduct;
                }
                if (remainingCost > 0 && stellarm > 0) {
                    long deduct = Math.min(stellarm, remainingCost);
                    stellarm = Math.max(0, stellarm - deduct);
                    remainingCost -= deduct;
                }
                if (remainingCost > 0) return new ResourceResult(false, solaris, lunara, voidflux, stellarm);
            }
            default -> {
                return new ResourceResult(false, solaris, lunara, voidflux, stellarm);
            }
        }
        return new ResourceResult(true, solaris, lunara, voidflux, stellarm);
    }

    public record ResourceResult(boolean success, long solaris, long lunara, long voidflux, long stellarm) {}

    public record Resource(long solaris, long lunara, long voidflux, long stellarm) {}
}
