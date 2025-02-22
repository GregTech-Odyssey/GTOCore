package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.data.recipe.generated.WoodRecipes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.WoodTypeEntry;
import com.gregtechceu.gtceu.data.recipe.misc.WoodMachineRecipes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.ULV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Iron;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Wood;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

@Mixin(WoodMachineRecipes.class)
public class WoodMachineRecipesMixin {

    @Shadow(remap = false)
    private static List<WoodTypeEntry> DEFAULT_ENTRIES;

    @Inject(method = "getDefaultEntries", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;asList([Ljava/lang/Object;)Ljava/util/List;"), remap = false, cancellable = true)
    private static void addEntries(CallbackInfoReturnable<List<WoodTypeEntry>> cir) {
        DEFAULT_ENTRIES = WoodRecipes.getEntries();
        cir.setReturnValue(DEFAULT_ENTRIES);
    }

    @Inject(method = "hardWoodRecipes", at = @At("TAIL"), remap = false)
    private static void hardWoodRecipes(Consumer<ResourceLocation> registry, CallbackInfo ci) {
        DEFAULT_ENTRIES = null;
    }

    /**
     * @author .
     * @reason fix null
     */
    @Overwrite(remap = false)
    public static void registerWoodTypeRecipe(Consumer<FinishedRecipe> provider, @NotNull WoodTypeEntry entry) {
        final String name = entry.woodName;
        TagKey<Item> logTag = entry.logTag;
        boolean hasPlanksRecipe = entry.planksRecipeName != null;

        // strip log
        if (entry.log != null && entry.strippedLog != null) LATHE_RECIPES.recipeBuilder("strip_" + entry.woodName + "_log")
                .inputItems(entry.log)
                .outputItems(entry.strippedLog)
                .outputItems(dust, Wood, 1)
                .duration(160).EUt(VA[ULV])
                .save(provider);

        // strip wood
        if (entry.wood != null && entry.strippedWood != null) LATHE_RECIPES.recipeBuilder("strip_" + entry.woodName + "_wood")
                .inputItems(entry.wood)
                .outputItems(entry.strippedWood)
                .outputItems(dust, Wood, 1)
                .duration(160).EUt(VA[ULV])
                .save(provider);

        // lathe stripped log
        if (entry.strippedLog != null) LATHE_RECIPES.recipeBuilder("lathe_stripped_" + entry.woodName + "_log")
                .inputItems(entry.strippedLog)
                .outputItems(rodLong, Wood, 4)
                .outputItems(dust, Wood, 1)
                .duration(160).EUt(VA[ULV])
                .save(provider);

        // lathe stripped wood
        if (entry.strippedWood != null) LATHE_RECIPES.recipeBuilder("lathe_stripped_" + entry.woodName + "_wood")
                .inputItems(entry.strippedWood)
                .outputItems(rodLong, Wood, 4)
                .outputItems(dust, Wood, 1)
                .duration(160).EUt(VA[ULV])
                .save(provider);

        if (entry.generateLogToPlankRecipe) {
            VanillaRecipeHelper.addShapelessRecipe(provider,
                    hasPlanksRecipe ? entry.planksRecipeName : name + "_planks",
                    new ItemStack(entry.planks, 2), logTag);

            // log -> plank saw crafting
            VanillaRecipeHelper.addShapedRecipe(provider, name + "_planks_saw",
                    new ItemStack(entry.planks, 4),
                    "s", "L", 'L', logTag);

            // log -> plank cutting
            CUTTER_RECIPES.recipeBuilder(name + "_planks")
                    .inputItems(logTag)
                    .outputItems(new ItemStack(entry.planks, 6))
                    .outputItems(dust, Wood, 2)
                    .duration(200)
                    .EUt(VA[ULV])
                    .save(provider);
        }

        // door
        if (entry.door != null) {
            final boolean hasDoorRecipe = entry.doorRecipeName != null;
            String recipeName = hasDoorRecipe ? entry.doorRecipeName : name + "_door";
            if (entry.trapdoor != null) {
                VanillaRecipeHelper.addShapedRecipe(provider, recipeName, new ItemStack(entry.door),
                        "PTd", "PRS", "PPs",
                        'P', entry.planks,
                        'T', entry.trapdoor,
                        'R', new UnificationEntry(ring, Iron),
                        'S', new UnificationEntry(screw, Iron));

                // plank -> door assembling
                ASSEMBLER_RECIPES.recipeBuilder(name + "_door")
                        .inputItems(entry.trapdoor)
                        .inputItems(new ItemStack(entry.planks, 4))
                        .inputFluids(Iron.getFluid(GTValues.L / 9))
                        .outputItems(entry.door)
                        .duration(400).EUt(4).save(provider);
            } else {
                VanillaRecipeHelper.addShapedRecipe(provider, recipeName, new ItemStack(entry.door),
                        "PTd", "PRS", "PPs",
                        'P', entry.planks,
                        'T', ItemTags.WOODEN_TRAPDOORS,
                        'R', new UnificationEntry(ring, Iron),
                        'S', new UnificationEntry(screw, Iron));

                // plank -> door assembling
                ASSEMBLER_RECIPES.recipeBuilder(name + "_door")
                        .inputItems(ItemTags.WOODEN_TRAPDOORS)
                        .inputItems(new ItemStack(entry.planks, 4))
                        .inputFluids(Iron.getFluid(GTValues.L / 9))
                        .outputItems(entry.door)
                        .duration(400).EUt(4).save(provider);
            }
        }

        // sign
        if (entry.sign != null && entry.slab != null) {
            final boolean hasSignRecipe = entry.signRecipeName != null;
            String recipeName = hasSignRecipe ? entry.signRecipeName : name + "_sign";
            VanillaRecipeHelper.addShapedRecipe(provider, recipeName, new ItemStack(entry.sign),
                    "LLL", "RPR", "sSd",
                    'P', entry.planks,
                    'R', new UnificationEntry(screw, Iron),
                    'L', entry.slab,
                    'S', entry.getStick());

            // plank -> sign assembling
            ASSEMBLER_RECIPES.recipeBuilder(recipeName)
                    .circuitMeta(4)
                    .inputItems(new ItemStack(entry.slab, 1))
                    .inputItems(entry.getStick(), 1)
                    .inputFluids(Iron.getFluid(GTValues.L / 9))
                    .outputItems(entry.sign, 3)
                    .duration(200).EUt(4).save(provider);

            // hanging sign
            if (entry.hangingSign != null && entry.strippedLog != null) {
                final boolean hasHangingSignRecipe = entry.hangingSignRecipeName != null;
                String recipeNameHanging = hasHangingSignRecipe ? entry.hangingSignRecipeName : name + "_hanging_sign";

                VanillaRecipeHelper.addShapedRecipe(provider, recipeNameHanging, new ItemStack(entry.hangingSign),
                        "LLL", "C C", "RSR",
                        'C', Items.CHAIN,
                        'R', new UnificationEntry(ring, Iron),
                        'S', new ItemStack(entry.sign),
                        'L', new ItemStack(entry.slab));

                ASSEMBLER_RECIPES.recipeBuilder(name + "_hanging_sign")
                        .inputItems(entry.slab, 3)
                        .inputItems(entry.sign)
                        .inputItems(Items.CHAIN, 2)
                        .outputItems(entry.hangingSign)
                        .circuitMeta(5)
                        .duration(150).EUt(4).save(provider);
            }
        }

        // trapdoor
        if (entry.trapdoor != null) {
            final boolean hasTrapdoorRecipe = entry.trapdoorRecipeName != null;
            String recipeName = hasTrapdoorRecipe ? entry.trapdoorRecipeName : name + "_trapdoor";
            VanillaRecipeHelper.addShapedRecipe(provider, recipeName, new ItemStack(entry.trapdoor),
                    "BPS", "PdP", "SPB",
                    'P', entry.planks,
                    'B', new UnificationEntry(bolt, Iron),
                    'S', entry.getStick());

            // plank -> trapdoor assembling
            ASSEMBLER_RECIPES.recipeBuilder(recipeName)
                    .circuitMeta(3)
                    .inputItems(new ItemStack(entry.planks, 2))
                    .inputFluids(Iron.getFluid(GTValues.L / 9))
                    .outputItems(entry.trapdoor)
                    .duration(200).EUt(4).save(provider);
        }

        // stairs
        if (entry.stairs != null) {
            final boolean hasStairRecipe = entry.stairsRecipeName != null;
            if (entry.addStairsCraftingRecipe) {
                VanillaRecipeHelper.addShapedRecipe(provider,
                        hasStairRecipe ? entry.stairsRecipeName : name + "_stairs",
                        new ItemStack(entry.stairs, 4),
                        "P  ", "PP ", "PPP",
                        'P', entry.planks);
            }

            // plank -> stairs assembling
            ASSEMBLER_RECIPES.recipeBuilder(name + "_stairs")
                    .inputItems(new ItemStack(entry.planks, 3))
                    .outputItems(new ItemStack(entry.stairs, 4))
                    .circuitMeta(7)
                    .EUt(1).duration(100).save(provider);
        }

        // slab
        if (entry.slab != null) {
            // plank -> slab crafting
            VanillaRecipeHelper.addShapedRecipe(provider, name + "_slab_saw", new ItemStack(entry.slab, 2),
                    "sS", 'S', entry.planks);

            // plank -> slab cutting
            CUTTER_RECIPES.recipeBuilder(name + "_slab")
                    .inputItems(entry.planks)
                    .outputItems(new ItemStack(entry.slab, 2))
                    .duration(200).EUt(VA[ULV])
                    .save(provider);
        }

        // fence
        if (entry.fence != null) {
            final boolean hasFenceRecipe = entry.fenceRecipeName != null;
            VanillaRecipeHelper.addShapedRecipe(provider, hasFenceRecipe ? entry.fenceRecipeName : name + "_fence",
                    new ItemStack(entry.fence),
                    "PSP", "PSP", "PSP",
                    'P', entry.planks,
                    'S', entry.getStick());

            // plank -> fence assembling
            ASSEMBLER_RECIPES.recipeBuilder(name + "_fence")
                    .inputItems(entry.planks)
                    .outputItems(entry.fence)
                    .circuitMeta(13)
                    .duration(100).EUt(4)
                    .save(provider);
        }

        // fence gate
        if (entry.fenceGate != null) {
            final boolean hasFenceGateRecipe = entry.fenceGateRecipeName != null;
            VanillaRecipeHelper.addShapedRecipe(provider,
                    hasFenceGateRecipe ? entry.fenceGateRecipeName : name + "_fence_gate",
                    new ItemStack(entry.fenceGate),
                    "F F", "SPS", "SPS",
                    'P', entry.planks,
                    'S', entry.getStick(),
                    'F', Items.FLINT);

            VanillaRecipeHelper.addShapedRecipe(provider, name + "_fence_gate_screws",
                    new ItemStack(entry.fenceGate, 2),
                    "IdI", "SPS", "SPS",
                    'P', entry.planks,
                    'S', entry.getStick(),
                    'I', new UnificationEntry(screw, Iron));

            // plank -> fence gate assembling
            ASSEMBLER_RECIPES.recipeBuilder(name + "_fence_gate")
                    .inputItems(new ItemStack(entry.planks, 2))
                    .inputItems(Tags.Items.RODS_WOODEN, 2)
                    .outputItems(entry.fenceGate)
                    .circuitMeta(2)
                    .duration(100).EUt(4).save(provider);
        }

        // boat
        if (entry.boat != null) {
            final boolean hasBoatRecipe = entry.boatRecipeName != null;
            if (entry.slab != null) {
                VanillaRecipeHelper.addShapedRecipe(provider, hasBoatRecipe ? entry.boatRecipeName : name + "_boat",
                        new ItemStack(entry.boat),
                        "PHP", "PkP", "SSS",
                        'P', entry.planks,
                        'S', entry.slab,
                        'H', ItemTags.SHOVELS);
            }

            // plank -> boat assembling
            ASSEMBLER_RECIPES.recipeBuilder(name + "_boat")
                    .inputItems(new ItemStack(entry.planks, 5))
                    .outputItems(entry.boat)
                    .circuitMeta(15)
                    .duration(100).EUt(4).save(provider);

            // chest boat
            if (entry.chestBoat != null) {
                final boolean hasChestBoatRecipe = entry.chestBoatRecipeName != null;
                String recipeName = hasChestBoatRecipe ? entry.chestBoatRecipeName : name + "_chest_boat";
                VanillaRecipeHelper.addShapedRecipe(provider, recipeName,
                        new ItemStack(entry.chestBoat),
                        " B ", "SCS", " w ",
                        'B', entry.boat,
                        'S', new UnificationEntry(bolt, Wood),
                        'C', Tags.Items.CHESTS_WOODEN);

                // boat -> chest boat assembling
                ASSEMBLER_RECIPES.recipeBuilder(name + "_chest_boat")
                        .inputItems(new ItemStack(entry.boat))
                        .inputItems(Tags.Items.CHESTS_WOODEN)
                        .outputItems(entry.chestBoat)
                        .circuitMeta(16)
                        .duration(100).EUt(4).save(provider);
            }
        }

        // button
        if (entry.button != null && entry.pressurePlate != null) {
            VanillaRecipeHelper.addShapedRecipe(provider, name + "_button", new ItemStack(entry.button, 6), "sP",
                    'P', new ItemStack(entry.pressurePlate));

            // plank -> button cutting
            CUTTER_RECIPES.recipeBuilder(name + "_button")
                    .inputItems(new ItemStack(entry.pressurePlate))
                    .outputItems(entry.button, 12)
                    .duration(250).EUt(VA[ULV]).save(provider);
        }

        // preesure plate
        if (entry.pressurePlate != null && entry.slab != null) {
            VanillaRecipeHelper.addShapedRecipe(provider, name + "_pressure_plate",
                    new ItemStack(entry.pressurePlate, 2), "SrS", "LCL", "SdS",
                    'S', new UnificationEntry(bolt, GTMaterials.Wood),
                    'L', entry.slab.asItem(),
                    'C', new UnificationEntry(spring, GTMaterials.Iron));

            ASSEMBLER_RECIPES.recipeBuilder(name + "_pressure_plate")
                    .inputItems(new ItemStack(entry.slab, 2))
                    .inputItems(spring, Iron)
                    .outputItems(entry.pressurePlate)
                    .circuitMeta(7)
                    .duration(100).EUt(VA[ULV]).save(provider);
        }
    }
}
