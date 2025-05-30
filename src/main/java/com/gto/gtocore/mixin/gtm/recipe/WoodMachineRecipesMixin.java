package com.gto.gtocore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.recipe.WoodTypeEntry;
import com.gregtechceu.gtceu.data.recipe.misc.WoodMachineRecipes;

import net.minecraft.world.item.Items;

import com.kyanite.deeperdarker.DeeperDarker;
import com.kyanite.deeperdarker.content.DDBlocks;
import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.common.registry.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.Arrays;
import java.util.List;

import static com.gregtechceu.gtceu.common.data.GTMaterials.TreatedWood;

@Mixin(WoodMachineRecipes.class)
public class WoodMachineRecipesMixin {

    @Shadow(remap = false)
    private static List<WoodTypeEntry> DEFAULT_ENTRIES;

    @Inject(method = "getDefaultEntries", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;asList([Ljava/lang/Object;)Ljava/util/List;"), remap = false, cancellable = true)
    private static void addEntries(CallbackInfoReturnable<List<WoodTypeEntry>> cir) {
        DEFAULT_ENTRIES = Arrays.asList(
                new WoodTypeEntry.Builder("minecraft", "oak")
                        .planks(Items.OAK_PLANKS, "oak_planks")
                        .log(Items.OAK_LOG).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_OAK_LOG)
                        .wood(Items.OAK_WOOD)
                        .strippedWood(Items.STRIPPED_OAK_WOOD)
                        .door(Items.OAK_DOOR, "oak_door")
                        .trapdoor(Items.OAK_TRAPDOOR, "oak_trapdoor")
                        .slab(Items.OAK_SLAB, "oak_slab")
                        .fence(Items.OAK_FENCE, "oak_fence")
                        .fenceGate(Items.OAK_FENCE_GATE, "oak_fence_gate")
                        .stairs(Items.OAK_STAIRS, "oak_stairs")
                        .boat(Items.OAK_BOAT, "oak_boat")
                        .chestBoat(Items.OAK_CHEST_BOAT, "oak_chest_boat")
                        .sign(Items.OAK_SIGN, "oak_sign")
                        .hangingSign(Items.OAK_HANGING_SIGN, "oak_hanging_sign")
                        .button(Items.OAK_BUTTON, "oak_button")
                        .pressurePlate(Items.OAK_PRESSURE_PLATE, "oak_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "spruce")
                        .planks(Items.SPRUCE_PLANKS, "spruce_planks")
                        .log(Items.SPRUCE_LOG).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_SPRUCE_LOG)
                        .wood(Items.SPRUCE_WOOD)
                        .strippedWood(Items.STRIPPED_SPRUCE_WOOD)
                        .door(Items.SPRUCE_DOOR, "spruce_door")
                        .trapdoor(Items.SPRUCE_TRAPDOOR, "spruce_trapdoor")
                        .slab(Items.SPRUCE_SLAB, "spruce_slab")
                        .fence(Items.SPRUCE_FENCE, "spruce_fence")
                        .fenceGate(Items.SPRUCE_FENCE_GATE, "spruce_fence_gate")
                        .stairs(Items.SPRUCE_STAIRS, "spruce_stairs")
                        .boat(Items.SPRUCE_BOAT, "spruce_boat")
                        .chestBoat(Items.SPRUCE_CHEST_BOAT, "spruce_chest_boat")
                        .sign(Items.SPRUCE_SIGN, "spruce_sign")
                        .hangingSign(Items.SPRUCE_HANGING_SIGN, "spruce_hanging_sign")
                        .button(Items.SPRUCE_BUTTON, "spruce_button")
                        .pressurePlate(Items.SPRUCE_PRESSURE_PLATE, "spruce_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "birch")
                        .planks(Items.BIRCH_PLANKS, "birch_planks")
                        .log(Items.BIRCH_LOG).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_BIRCH_LOG)
                        .wood(Items.BIRCH_WOOD)
                        .strippedWood(Items.STRIPPED_BIRCH_WOOD)
                        .door(Items.BIRCH_DOOR, "birch_door")
                        .trapdoor(Items.BIRCH_TRAPDOOR, "birch_trapdoor")
                        .slab(Items.BIRCH_SLAB, "birch_slab")
                        .fence(Items.BIRCH_FENCE, "birch_fence")
                        .fenceGate(Items.BIRCH_FENCE_GATE, "birch_fence_gate")
                        .stairs(Items.BIRCH_STAIRS, "birch_stairs")
                        .boat(Items.BIRCH_BOAT, "birch_boat")
                        .chestBoat(Items.BIRCH_CHEST_BOAT, "birch_chest_boat")
                        .sign(Items.BIRCH_SIGN, "birch_sign")
                        .hangingSign(Items.BIRCH_HANGING_SIGN, "birch_hanging_sign")
                        .button(Items.BIRCH_BUTTON, "birch_button")
                        .pressurePlate(Items.BIRCH_PRESSURE_PLATE, "birch_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "jungle")
                        .planks(Items.JUNGLE_PLANKS, "jungle_planks")
                        .log(Items.JUNGLE_LOG).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_JUNGLE_LOG)
                        .wood(Items.JUNGLE_WOOD)
                        .strippedWood(Items.STRIPPED_JUNGLE_WOOD)
                        .door(Items.JUNGLE_DOOR, "jungle_door")
                        .trapdoor(Items.JUNGLE_TRAPDOOR, "jungle_trapdoor")
                        .slab(Items.JUNGLE_SLAB, "jungle_slab")
                        .fence(Items.JUNGLE_FENCE, "jungle_fence")
                        .fenceGate(Items.JUNGLE_FENCE_GATE, "jungle_fence_gate")
                        .stairs(Items.JUNGLE_STAIRS, "jungle_stairs")
                        .boat(Items.JUNGLE_BOAT, "jungle_boat")
                        .chestBoat(Items.JUNGLE_CHEST_BOAT, "jungle_chest_boat")
                        .sign(Items.JUNGLE_SIGN, "jungle_sign")
                        .hangingSign(Items.JUNGLE_HANGING_SIGN, "jungle_hanging_sign")
                        .button(Items.JUNGLE_BUTTON, "jungle_button")
                        .pressurePlate(Items.JUNGLE_PRESSURE_PLATE, "jungle_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "acacia")
                        .planks(Items.ACACIA_PLANKS, "acacia_planks")
                        .log(Items.ACACIA_LOG).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_ACACIA_LOG)
                        .wood(Items.ACACIA_WOOD)
                        .strippedWood(Items.STRIPPED_ACACIA_WOOD)
                        .door(Items.ACACIA_DOOR, "acacia_door")
                        .trapdoor(Items.ACACIA_TRAPDOOR, "acacia_trapdoor")
                        .slab(Items.ACACIA_SLAB, "acacia_slab")
                        .fence(Items.ACACIA_FENCE, "acacia_fence")
                        .fenceGate(Items.ACACIA_FENCE_GATE, "acacia_fence_gate")
                        .stairs(Items.ACACIA_STAIRS, "acacia_stairs")
                        .boat(Items.ACACIA_BOAT, "acacia_boat")
                        .chestBoat(Items.ACACIA_CHEST_BOAT, "acacia_chest_boat")
                        .sign(Items.ACACIA_SIGN, "acacia_sign")
                        .hangingSign(Items.ACACIA_HANGING_SIGN, "acacia_hanging_sign")
                        .button(Items.ACACIA_BUTTON, "acacia_button")
                        .pressurePlate(Items.ACACIA_PRESSURE_PLATE, "acacia_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "dark_oak")
                        .planks(Items.DARK_OAK_PLANKS, "dark_oak_planks")
                        .log(Items.DARK_OAK_LOG).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_DARK_OAK_LOG)
                        .wood(Items.DARK_OAK_WOOD)
                        .strippedWood(Items.STRIPPED_DARK_OAK_WOOD)
                        .door(Items.DARK_OAK_DOOR, "dark_oak_door")
                        .trapdoor(Items.DARK_OAK_TRAPDOOR, "dark_oak_trapdoor")
                        .slab(Items.DARK_OAK_SLAB, "dark_oak_slab")
                        .fence(Items.DARK_OAK_FENCE, "dark_oak_fence")
                        .fenceGate(Items.DARK_OAK_FENCE_GATE, "dark_oak_fence_gate")
                        .stairs(Items.DARK_OAK_STAIRS, "dark_oak_stairs")
                        .boat(Items.DARK_OAK_BOAT, "dark_oak_boat")
                        .chestBoat(Items.DARK_OAK_CHEST_BOAT, "dark_oak_chest_boat")
                        .sign(Items.DARK_OAK_SIGN, "dark_oak_sign")
                        .hangingSign(Items.DARK_OAK_HANGING_SIGN, "dark_oak_hanging_sign")
                        .button(Items.DARK_OAK_BUTTON, "dark_oak_button")
                        .pressurePlate(Items.DARK_OAK_PRESSURE_PLATE, "dark_oak_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "bamboo")
                        .planks(Items.BAMBOO_PLANKS, "bamboo_planks")
                        .logTag(TagUtil.createItemTag("bamboo_blocks", true))
                        .log(Items.BAMBOO_BLOCK).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_BAMBOO_BLOCK)
                        .door(Items.BAMBOO_DOOR, "bamboo_door")
                        .trapdoor(Items.BAMBOO_TRAPDOOR, "bamboo_trapdoor")
                        .slab(Items.BAMBOO_SLAB, "bamboo_slab")
                        .fence(Items.BAMBOO_FENCE, "bamboo_fence")
                        .fenceGate(Items.BAMBOO_FENCE_GATE, "bamboo_fence_gate")
                        .stairs(Items.BAMBOO_STAIRS, "bamboo_stairs")
                        .boat(Items.BAMBOO_RAFT, "bamboo_raft")
                        .chestBoat(Items.BAMBOO_CHEST_RAFT, "bamboo_chest_raft")
                        .sign(Items.BAMBOO_SIGN, "bamboo_sign")
                        .hangingSign(Items.BAMBOO_HANGING_SIGN, "bamboo_hanging_sign")
                        .button(Items.BAMBOO_BUTTON, "bamboo_button")
                        .pressurePlate(Items.BAMBOO_PRESSURE_PLATE, "bamboo_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "cherry")
                        .planks(Items.CHERRY_PLANKS, "cherry_planks")
                        .log(Items.CHERRY_LOG).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_CHERRY_LOG)
                        .wood(Items.CHERRY_WOOD)
                        .strippedWood(Items.STRIPPED_CHERRY_WOOD)
                        .door(Items.CHERRY_DOOR, "cherry_door")
                        .trapdoor(Items.CHERRY_TRAPDOOR, "cherry_trapdoor")
                        .slab(Items.CHERRY_SLAB, "cherry_slab")
                        .fence(Items.CHERRY_FENCE, "cherry_fence")
                        .fenceGate(Items.CHERRY_FENCE_GATE, "cherry_fence_gate")
                        .stairs(Items.CHERRY_STAIRS, "cherry_stairs")
                        .boat(Items.CHERRY_BOAT, "cherry_boat")
                        .chestBoat(Items.CHERRY_CHEST_BOAT, "cherry_chest_boat")
                        .sign(Items.CHERRY_SIGN, "cherry_sign")
                        .hangingSign(Items.CHERRY_HANGING_SIGN, "cherry_hanging_sign")
                        .button(Items.CHERRY_BUTTON, "cherry_button")
                        .pressurePlate(Items.CHERRY_PRESSURE_PLATE, "cherry_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "mangrove")
                        .planks(Items.MANGROVE_PLANKS, "mangrove_planks")
                        .log(Items.MANGROVE_LOG).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_MANGROVE_LOG)
                        .wood(Items.MANGROVE_WOOD)
                        .strippedWood(Items.STRIPPED_MANGROVE_WOOD)
                        .door(Items.MANGROVE_DOOR, "mangrove_door")
                        .trapdoor(Items.MANGROVE_TRAPDOOR, "mangrove_trapdoor")
                        .slab(Items.MANGROVE_SLAB, "mangrove_slab")
                        .fence(Items.MANGROVE_FENCE, "mangrove_fence")
                        .fenceGate(Items.MANGROVE_FENCE_GATE, "mangrove_fence_gate")
                        .stairs(Items.MANGROVE_STAIRS, "mangrove_stairs")
                        .boat(Items.MANGROVE_BOAT, "mangrove_boat")
                        .chestBoat(Items.MANGROVE_CHEST_BOAT, "mangrove_chest_boat")
                        .sign(Items.MANGROVE_SIGN, "mangrove_sign")
                        .hangingSign(Items.MANGROVE_HANGING_SIGN, "mangrove_hanging_sign")
                        .button(Items.MANGROVE_BUTTON, "mangrove_button")
                        .pressurePlate(Items.MANGROVE_PRESSURE_PLATE, "mangrove_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "crimson")
                        .planks(Items.CRIMSON_PLANKS, "crimson_planks")
                        .logTag(TagUtil.createItemTag("crimson_stems", true))
                        .log(Items.CRIMSON_STEM).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_CRIMSON_STEM)
                        .wood(Items.CRIMSON_HYPHAE)
                        .strippedWood(Items.STRIPPED_CRIMSON_HYPHAE)
                        .door(Items.CRIMSON_DOOR, "crimson_door")
                        .trapdoor(Items.CRIMSON_TRAPDOOR, "crimson_trapdoor")
                        .slab(Items.CRIMSON_SLAB, "crimson_slab")
                        .fence(Items.CRIMSON_FENCE, "crimson_fence")
                        .fenceGate(Items.CRIMSON_FENCE_GATE, "crimson_fence_gate")
                        .stairs(Items.CRIMSON_STAIRS, "crimson_stairs")
                        .sign(Items.CRIMSON_SIGN, "crimson_sign")
                        .hangingSign(Items.CRIMSON_HANGING_SIGN, "crimson_hanging_sign")
                        .button(Items.CRIMSON_BUTTON, "crimson_button")
                        .pressurePlate(Items.CRIMSON_PRESSURE_PLATE, "crimson_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder("minecraft", "warped")
                        .planks(Items.WARPED_PLANKS, "warped_planks")
                        .logTag(TagUtil.createItemTag("warped_stems", true))
                        .log(Items.WARPED_STEM).removeCharcoalRecipe()
                        .strippedLog(Items.STRIPPED_WARPED_STEM)
                        .wood(Items.WARPED_HYPHAE)
                        .strippedWood(Items.STRIPPED_WARPED_HYPHAE)
                        .door(Items.WARPED_DOOR, "warped_door")
                        .trapdoor(Items.WARPED_TRAPDOOR, "warped_trapdoor")
                        .slab(Items.WARPED_SLAB, "warped_slab")
                        .fence(Items.WARPED_FENCE, "warped_fence")
                        .fenceGate(Items.WARPED_FENCE_GATE, "warped_fence_gate")
                        .stairs(Items.WARPED_STAIRS, "warped_stairs")
                        .sign(Items.WARPED_SIGN, "warped_sign")
                        .hangingSign(Items.WARPED_HANGING_SIGN, "warped_hanging_sign")
                        .button(Items.WARPED_BUTTON, "warped_button")
                        .pressurePlate(Items.WARPED_PRESSURE_PLATE, "warped_pressure_plate")
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder(GTCEu.MOD_ID, "rubber")
                        .planks(GTBlocks.RUBBER_PLANK.asItem(), null)
                        .log(GTBlocks.RUBBER_LOG.asItem()).addCharcoalRecipe()
                        .strippedLog(GTBlocks.STRIPPED_RUBBER_LOG.asItem())
                        .wood(GTBlocks.RUBBER_WOOD.asItem())
                        .strippedWood(GTBlocks.STRIPPED_RUBBER_WOOD.asItem())
                        .door(GTBlocks.RUBBER_DOOR.asItem(), null)
                        .trapdoor(GTBlocks.RUBBER_TRAPDOOR.asItem(), null)
                        .slab(GTBlocks.RUBBER_SLAB.asItem(), null).addSlabRecipe()
                        .fence(GTBlocks.RUBBER_FENCE.asItem(), null)
                        .fenceGate(GTBlocks.RUBBER_FENCE_GATE.asItem(), null)
                        .stairs(GTBlocks.RUBBER_STAIRS.asItem(), null).addStairsRecipe()
                        .boat(GTItems.RUBBER_BOAT.asItem(), null)
                        .chestBoat(GTItems.RUBBER_CHEST_BOAT.asItem(), null)
                        .sign(GTBlocks.RUBBER_SIGN.asItem(), null)
                        .hangingSign(GTBlocks.RUBBER_HANGING_SIGN.asItem(), null)
                        .button(GTBlocks.RUBBER_BUTTON.asItem(), null)
                        .pressurePlate(GTBlocks.RUBBER_PRESSURE_PLATE.asItem(), null)
                        .registerAllTags()
                        .registerAllUnificationInfo()
                        .build(),
                new WoodTypeEntry.Builder(GTCEu.MOD_ID, "treated")
                        .planks(GTBlocks.TREATED_WOOD_PLANK.asItem(), null)
                        .door(GTBlocks.TREATED_WOOD_DOOR.asItem(), null)
                        .trapdoor(GTBlocks.TREATED_WOOD_TRAPDOOR.asItem(), null)
                        .slab(GTBlocks.TREATED_WOOD_SLAB.asItem(), null).addSlabRecipe()
                        .fence(GTBlocks.TREATED_WOOD_FENCE.asItem(), null)
                        .fenceGate(GTBlocks.TREATED_WOOD_FENCE_GATE.asItem(), null)
                        .stairs(GTBlocks.TREATED_WOOD_STAIRS.asItem(), null).addStairsRecipe()
                        .boat(GTItems.TREATED_WOOD_BOAT.asItem(), null)
                        .chestBoat(GTItems.TREATED_WOOD_CHEST_BOAT.asItem(), null)
                        .sign(GTBlocks.TREATED_WOOD_SIGN.asItem(), null)
                        .hangingSign(GTBlocks.TREATED_WOOD_HANGING_SIGN.asItem(), null)
                        .button(GTBlocks.TREATED_WOOD_BUTTON.asItem(), null)
                        .pressurePlate(GTBlocks.TREATED_WOOD_PRESSURE_PLATE.asItem(), null)
                        .material(TreatedWood)
                        .generateLogToPlankRecipe(false)
                        .registerUnificationInfo(false, true, true, true, true, true, true, true, true, true)
                        .build(),
                new WoodTypeEntry.Builder(AdAstra.MOD_ID, "glacian")
                        .planks(ModBlocks.GLACIAN_PLANKS.get().asItem(), "glacian_planks")
                        .door(ModBlocks.GLACIAN_DOOR.get().asItem(), "glacian_door")
                        .trapdoor(ModBlocks.GLACIAN_TRAPDOOR.get().asItem(), "glacian_trapdoor")
                        .slab(ModBlocks.GLACIAN_SLAB.get().asItem(), "glacian_slab")
                        .fence(ModBlocks.GLACIAN_FENCE.get().asItem(), "glacian_fence")
                        .fenceGate(ModBlocks.GLACIAN_FENCE_GATE.get().asItem(), "glacian_fence_gate")
                        .stairs(ModBlocks.GLACIAN_STAIRS.get().asItem(), "glacian_stairs")
                        .button(ModBlocks.GLACIAN_BUTTON.get().asItem(), "glacian_button")
                        .pressurePlate(ModBlocks.GLACIAN_PRESSURE_PLATE.get().asItem(), "glacian_pressure_plate")
                        .build(),
                new WoodTypeEntry.Builder(DeeperDarker.MOD_ID, "echo")
                        .planks(DDBlocks.ECHO_PLANKS.get().asItem(), "echo_planks")
                        .door(DDBlocks.ECHO_DOOR.get().asItem(), "echo_door")
                        .trapdoor(DDBlocks.ECHO_TRAPDOOR.get().asItem(), "echo_trapdoor")
                        .slab(DDBlocks.ECHO_SLAB.get().asItem(), "echo_slab")
                        .fence(DDBlocks.ECHO_FENCE.get().asItem(), "echo_fence")
                        .fenceGate(DDBlocks.ECHO_FENCE_GATE.get().asItem(), "echo_fence_gate")
                        .stairs(DDBlocks.ECHO_STAIRS.get().asItem(), "echo_stairs")
                        .button(DDBlocks.ECHO_BUTTON.get().asItem(), "echo_button")
                        .pressurePlate(DDBlocks.ECHO_PRESSURE_PLATE.get().asItem(), "echo_pressure_plate")
                        .build(),
                new WoodTypeEntry.Builder(DeeperDarker.MOD_ID, "bloom")
                        .planks(DDBlocks.BLOOM_PLANKS.get().asItem(), null)
                        .door(DDBlocks.BLOOM_DOOR.get().asItem(), "bloom_door")
                        .trapdoor(DDBlocks.BLOOM_TRAPDOOR.get().asItem(), "bloom_trapdoor")
                        .slab(DDBlocks.BLOOM_SLAB.get().asItem(), "bloom_slab")
                        .fence(DDBlocks.BLOOM_FENCE.get().asItem(), "bloom_fence")
                        .fenceGate(DDBlocks.BLOOM_FENCE_GATE.get().asItem(), "bloom_fence_gate")
                        .stairs(DDBlocks.BLOOM_STAIRS.get().asItem(), "bloom_stairs")
                        .button(DDBlocks.BLOOM_BUTTON.get().asItem(), "bloom_button")
                        .pressurePlate(DDBlocks.BLOOM_PRESSURE_PLATE.get().asItem(), "bloom_pressure_plate")
                        .build(),
                new WoodTypeEntry.Builder(BotaniaAPI.MODID, "livingwood")
                        .planks(BotaniaBlocks.livingwoodPlanks.asItem(), "livingwood_planks")
                        .slab(BotaniaBlocks.livingwoodPlankSlab.asItem(), "livingwood_planks_slab")
                        .fence(BotaniaBlocks.livingwoodFence.asItem(), "livingwood_fence")
                        .fenceGate(BotaniaBlocks.livingwoodFenceGate.asItem(), "livingwood_fence_gate")
                        .stairs(BotaniaBlocks.livingwoodPlankStairs.asItem(), "livingwood_planks_stairs")
                        .build(),
                new WoodTypeEntry.Builder(BotaniaAPI.MODID, "dreamwood")
                        .planks(BotaniaBlocks.dreamwoodPlanks.asItem(), "dreamwood_planks")
                        .slab(BotaniaBlocks.dreamwoodPlankSlab.asItem(), "dreamwood_planks_slab")
                        .fence(BotaniaBlocks.dreamwoodFence.asItem(), "dreamwood_fence")
                        .fenceGate(BotaniaBlocks.dreamwoodFenceGate.asItem(), "dreamwood_fence_gate")
                        .stairs(BotaniaBlocks.dreamwoodPlankStairs.asItem(), "dreamwood_planks_stairs")
                        .build());
        cir.setReturnValue(DEFAULT_ENTRIES);
    }
}
