package com.gtocore.common.forge;

import com.gtolib.GTOCore;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import appeng.core.definitions.AEBlocks;
import com.google.common.collect.ImmutableSet;

public class GTOVillagerRegistry {

    static String EXOTIC = "exotic_trader";

    public static final DeferredRegister<VillagerProfession> VILLAGERS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, GTOCore.MOD_ID);

    public static final DeferredRegister<PoiType> POIs = DeferredRegister.create(ForgeRegistries.POI_TYPES, GTOCore.MOD_ID);

    public static final RegistryObject<PoiType> EXOTIC_POI = POIs.register("exotic_poi", () -> new PoiType(ImmutableSet.copyOf(AEBlocks.MYSTERIOUS_CUBE.block().getStateDefinition().getPossibleStates()), 1, 1));
    public static final RegistryObject<VillagerProfession> EXOTIC_TRADER = VILLAGERS.register(EXOTIC, () -> new VillagerProfession(
            EXOTIC,
            (x) -> x.get() == EXOTIC_POI.get(),
            (x) -> x.get() == EXOTIC_POI.get(),
            ImmutableSet.of(),
            ImmutableSet.of(),
            SoundEvents.VILLAGER_WORK_LIBRARIAN));
}
