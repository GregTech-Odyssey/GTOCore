package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.data.*;

import com.gregtechceu.gtceu.api.data.worldgen.ores.GeneratedVeinMetadata;
import com.gregtechceu.gtceu.api.gui.misc.ProspectorMode;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public final class GTRecordAdapter {

    public static SharedEntry ore(ResourceKey<Level> dimension, GeneratedVeinMetadata vein) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", vein.id().toString());
        ResourceLocation definition = GTRegistries.ORE_VEINS.getKey(vein.definition());
        tag.putString("definition", definition == null ? "" : definition.toString());
        tag.putLong("origin", vein.originChunk().toLong());
        tag.putLong("center", vein.center().asLong());
        tag.putBoolean("depleted", vein.depleted());
        return new OreVeinRecord(dimension.location(), vein.id(), vein.originChunk().x, vein.originChunk().z,
                tag, SharedEntry.hash(tag), 0).asEntry();
    }

    public static SharedEntry fluid(ResourceKey<Level> dimension, int x, int z, ProspectorMode.FluidInfo fluid) {
        CompoundTag tag = new CompoundTag();
        ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid.fluid());
        tag.putString("fluid", id == null ? "minecraft:empty" : id.toString());
        tag.putInt("yield", fluid.yield());
        tag.putInt("left", fluid.left());
        return new BedrockFluidRecord(dimension.location(), x, z, tag, SharedEntry.hash(tag), 0).asEntry();
    }

    public static SharedEntry bedrockOre(ResourceKey<Level> dimension, int x, int z, ProspectorMode.OreInfo[] ores) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (ProspectorMode.OreInfo ore : ores) {
            CompoundTag value = new CompoundTag();
            value.putString("material", ore.material().getResourceLocation().toString());
            value.putInt("weight", ore.weight());
            value.putInt("left", ore.left());
            value.putInt("yield", ore.yield());
            list.add(value);
        }
        tag.put("ores", list);
        return new BedrockOreRecord(dimension.location(), x, z, tag, SharedEntry.hash(tag), 0).asEntry();
    }
}
