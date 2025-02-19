package com.gto.gtocore.mixin.mc;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;

import com.kyanite.deeperdarker.content.DDBlocks;
import mythicbotany.register.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Set;

import static earth.terrarium.adastra.common.registry.ModBlocks.*;

@Mixin(value = OreConfiguration.class, priority = 0)
public class OreConfigurationMixin {

    @Unique
    private static Set<Block> EXCLUDED_ORE;

    @ModifyVariable(method = "<init>(Ljava/util/List;IF)V", at = @At("HEAD"), index = 1, argsOnly = true)
    private static List<TargetBlockState> gtocore$init(List<TargetBlockState> targetStates) {
        if (EXCLUDED_ORE == null) {
            EXCLUDED_ORE = Set.of(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.NETHER_QUARTZ_ORE, MOON_DESH_ORE.get(), DEEPSLATE_DESH_ORE.get(), MOON_IRON_ORE.get(), MOON_ICE_SHARD_ORE.get(), DEEPSLATE_ICE_SHARD_ORE.get(), MARS_IRON_ORE.get(), MARS_DIAMOND_ORE.get(), MARS_OSTRUM_ORE.get(), DEEPSLATE_OSTRUM_ORE.get(), MARS_ICE_SHARD_ORE.get(), MERCURY_IRON_ORE.get(), VENUS_COAL_ORE.get(), VENUS_GOLD_ORE.get(), VENUS_DIAMOND_ORE.get(), VENUS_CALORITE_ORE.get(), DEEPSLATE_CALORITE_ORE.get(), GLACIO_ICE_SHARD_ORE.get(), GLACIO_COAL_ORE.get(), GLACIO_COPPER_ORE.get(), GLACIO_IRON_ORE.get(), GLACIO_LAPIS_ORE.get(), DDBlocks.SCULK_STONE_COAL_ORE.get(), DDBlocks.SCULK_STONE_IRON_ORE.get(), DDBlocks.SCULK_STONE_COPPER_ORE.get(), DDBlocks.SCULK_STONE_GOLD_ORE.get(), DDBlocks.SCULK_STONE_REDSTONE_ORE.get(), DDBlocks.SCULK_STONE_EMERALD_ORE.get(), DDBlocks.SCULK_STONE_LAPIS_ORE.get(), DDBlocks.SCULK_STONE_DIAMOND_ORE.get(), DDBlocks.GLOOMSLATE_COAL_ORE.get(), DDBlocks.GLOOMSLATE_IRON_ORE.get(), DDBlocks.GLOOMSLATE_COPPER_ORE.get(), DDBlocks.GLOOMSLATE_GOLD_ORE.get(), DDBlocks.GLOOMSLATE_REDSTONE_ORE.get(), DDBlocks.GLOOMSLATE_EMERALD_ORE.get(), DDBlocks.GLOOMSLATE_LAPIS_ORE.get(), DDBlocks.GLOOMSLATE_DIAMOND_ORE.get(), ModBlocks.elementiumOre, ModBlocks.dragonstoneOre, ModBlocks.goldOre);
        }
        return targetStates.stream()
                .filter(targetState -> !EXCLUDED_ORE.contains(targetState.state.getBlock()))
                .toList();
    }
}
