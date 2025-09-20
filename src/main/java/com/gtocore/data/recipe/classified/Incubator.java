package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.recipe.condition.GravityCondition;

import com.gtolib.GTOCore;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import appeng.core.definitions.AEItems;
import com.enderio.base.common.init.EIOFluids;
import earth.terrarium.adastra.common.registry.ModFluids;

import static com.gregtechceu.gtceu.api.GTValues.UEV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.INCUBATOR_RECIPES;

final class Incubator {

    public static void init() {
        INCUBATOR_RECIPES.recipeBuilder("certus_quartz_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/quartzite"))
                .inputItems(TagUtils.createTGTag("ores/certus_quartz"))
                .inputItems(TagUtils.createTGTag("ores/barite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.CERTUS_QUARTZ_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("monazite_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/bastnasite"))
                .inputItems(TagUtils.createTGTag("ores/molybdenum"))
                .inputItems(TagUtils.createTGTag("ores/neodymium"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.MONAZITE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("biomediumraw")
                .inputItems(GTOItems.BIOLOGICAL_CELLS.asStack(64))
                .inputItems(GTOItems.TCETIESEAWEEDEXTRACT.asStack(16))
                .inputFluids(GTMaterials.RawGrowthMedium.getFluid(10000))
                .outputFluids(GTOMaterials.BiomediumRaw.getFluid(10000))
                .EUt(1920)
                .duration(1200)
                .addCondition(new GravityCondition(true))
                .addData("filter_casing", 2)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("iron_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/goethite"))
                .inputItems(TagUtils.createTGTag("ores/yellow_limonite"))
                .inputItems(TagUtils.createTGTag("ores/hematite"))
                .inputItems(TagUtils.createTGTag("ores/malachite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.IRON_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("variation_wood")
                .inputItems(GTOBlocks.BARNARDA_C_LOG.asStack(64))
                .inputItems(new ItemStack(Blocks.CRIMSON_STEM.asItem(), 16))
                .inputItems(TagPrefix.dust, GTMaterials.Wood, 64)
                .inputItems(TagPrefix.dust, GTMaterials.Lapotron)
                .inputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .inputFluids(GTMaterials.Biomass.getFluid(1000))
                .outputItems(GTOBlocks.VARIATION_WOOD.asStack(64))
                .EUt(1966080)
                .duration(2400)
                .addData("filter_casing", 3)
                .addData("radioactivity", 440)
                .save();

        INCUBATOR_RECIPES.builder("barnarda_c_log_from_echo")
                .inputItems("deeperdarker:echo_sapling", 4)
                .inputItems("deeperdarker:echo_log", 32)
                .inputItems(GTOBlocks.BARNARDA_C_LOG.asStack(32))
                .inputItems(TagPrefix.dust, GTOMaterials.StreptococcusPyogenes, 16)
                .inputItems(TagPrefix.dust, GTOMaterials.Shewanella, 16)
                .outputItems(GTOBlocks.BARNARDA_C_LOG.asStack(64))
                .inputFluids(GTOMaterials.BarnardaAir, 10000)
                .EUt(VA[UEV])
                .addData("filter_casing", 3)
                .addData("radioactivity", 440)
                .duration(1200)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("lubricant_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/soapstone"))
                .inputItems(TagUtils.createTGTag("ores/talc"))
                .inputItems(TagUtils.createTGTag("ores/glauconite_sand"))
                .inputItems(TagUtils.createTGTag("ores/pentlandite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.LUBRICANT_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("lapis_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/lazurite"))
                .inputItems(TagUtils.createTGTag("ores/sodalite"))
                .inputItems(TagUtils.createTGTag("ores/lapis"))
                .inputItems(TagUtils.createTGTag("ores/calcite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.LAPIS_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("stem_cells")
                .chancedInput(GTOItems.GLACIO_SPIRIT.asStack(), 2000, 100)
                .inputItems(GTItems.STEM_CELLS.asStack(32))
                .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(400))
                .outputItems(GTItems.STEM_CELLS.asStack(64))
                .EUt(30720)
                .duration(300)
                .addData("filter_casing", 2)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("copper_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/chalcopyrite"))
                .inputItems(TagUtils.createTGTag("ores/iron"))
                .inputItems(TagUtils.createTGTag("ores/pyrite"))
                .inputItems(TagUtils.createTGTag("ores/copper"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.COPPER_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("nickel_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/garnierite"))
                .inputItems(TagUtils.createTGTag("ores/nickel"))
                .inputItems(TagUtils.createTGTag("ores/cobaltite"))
                .inputItems(TagUtils.createTGTag("ores/pentlandite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.NICKEL_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("oilsands_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/oilsands"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.OILSANDS_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("banded_iron_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/goethite"))
                .inputItems(TagUtils.createTGTag("ores/yellow_limonite"))
                .inputItems(TagUtils.createTGTag("ores/hematite"))
                .inputItems(TagUtils.createTGTag("ores/gold"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.BANDED_IRON_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("chorus_fruit")
                .notConsumable(new ItemStack(Blocks.CHORUS_FLOWER.asItem(), 64))
                .inputFluids(GTOMaterials.UnknowWater.getFluid(1000))
                .inputFluids(GTMaterials.EnderPearl.getFluid(100))
                .outputItems(new ItemStack(Items.CHORUS_FRUIT.asItem(), 64))
                .EUt(120)
                .duration(1200)
                .addData("radioactivity", 230)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("magnetite_vein_end_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/magnetite"))
                .inputItems(TagUtils.createTGTag("ores/vanadium_magnetite"))
                .inputItems(TagUtils.createTGTag("ores/chromite"))
                .inputItems(TagUtils.createTGTag("ores/gold"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.MAGNETITE_VEIN_END_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("pitchblende_vein_end_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/pitchblende"))
                .inputItems(TagUtils.createTGTag("ores/uraninite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.PITCHBLENDE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("seaweedbroth")
                .inputItems(new ItemStack(Blocks.KELP.asItem(), 64))
                .inputItems(TagPrefix.dust, GTOMaterials.AlienAlgae, 32)
                .inputItems(TagPrefix.dust, GTOMaterials.AlgaeExtract, 16)
                .inputItems(GTItems.ENERGIUM_DUST.asStack(8))
                .inputItems(TagPrefix.dust, GTOMaterials.Mithril)
                .inputFluids(GTOMaterials.UnknownNutrientAgar.getFluid(50000))
                .inputFluids(GTMaterials.Methane.getFluid(50000))
                .outputFluids(GTOMaterials.SeaweedBroth.getFluid(50000))
                .EUt(7680)
                .duration(4800)
                .addData("filter_casing", 2)
                .addData("radioactivity", 80)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("galena_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/galena"))
                .inputItems(TagUtils.createTGTag("ores/silver"))
                .inputItems(TagUtils.createTGTag("ores/lead"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.GALENA_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("saltpeter_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/saltpeter"))
                .inputItems(TagUtils.createTGTag("ores/diatomite"))
                .inputItems(TagUtils.createTGTag("ores/electrotine"))
                .inputItems(TagUtils.createTGTag("ores/alunite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.SALTPETER_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("molybdenum_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/wulfenite"))
                .inputItems(TagUtils.createTGTag("ores/molybdenite"))
                .inputItems(TagUtils.createTGTag("ores/molybdenum"))
                .inputItems(TagUtils.createTGTag("ores/powellite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.MOLYBDENUM_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("dragon_cells")
                .chancedInput(GTOItems.GLACIO_SPIRIT.asStack(), 6000, 100)
                .inputItems(GTOItems.DRAGON_CELLS.asStack(32))
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(4000))
                .outputItems(GTOItems.DRAGON_CELLS.asStack(64))
                .EUt(491520)
                .duration(1600)
                .addData("filter_casing", 3)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("salts_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/rock_salt"))
                .inputItems(TagUtils.createTGTag("ores/salt"))
                .inputItems(TagUtils.createTGTag("ores/lepidolite"))
                .inputItems(TagUtils.createTGTag("ores/spodumene"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.SALTS_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("redstone_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/redstone"))
                .inputItems(TagUtils.createTGTag("ores/ruby"))
                .inputItems(TagUtils.createTGTag("ores/cinnabar"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.REDSTONE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("apatite_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/apatite"))
                .inputItems(TagUtils.createTGTag("ores/tricalcium_phosphate"))
                .inputItems(TagUtils.createTGTag("ores/pyrochlore"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.APATITE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("xenoxene")
                .inputItems(TagPrefix.dust, GTMaterials.AntimonyTrioxide, 16)
                .inputItems(TagPrefix.dust, GTMaterials.Osmium, 16)
                .inputFluids(GTMaterials.Oil.getFluid(20000))
                .outputFluids(GTOMaterials.Xenoxene.getFluid(20000))
                .EUt(491520)
                .duration(2400)
                .addData("filter_casing", 3)
                .addData("radioactivity", 360)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("sheldonite_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/bornite"))
                .inputItems(TagUtils.createTGTag("ores/cooperite"))
                .inputItems(TagUtils.createTGTag("ores/platinum"))
                .inputItems(TagUtils.createTGTag("ores/palladium"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.SHELDONITE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("mica_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/kyanite"))
                .inputItems(TagUtils.createTGTag("ores/mica"))
                .inputItems(TagUtils.createTGTag("ores/bauxite"))
                .inputItems(TagUtils.createTGTag("ores/pollucite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.MICA_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("nether_quartz_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/nether_quartz"))
                .inputItems(TagUtils.createTGTag("ores/quartzite"))
                .inputItems(new ItemStack(Blocks.ANCIENT_DEBRIS.asItem(), 5))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.NETHER_QUARTZ_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("olivine_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/bentonite"))
                .inputItems(TagUtils.createTGTag("ores/magnetite"))
                .inputItems(TagUtils.createTGTag("ores/olivine"))
                .inputItems(TagUtils.createTGTag("ores/glauconite_sand"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.OLIVINE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("beryllium_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/beryllium"))
                .inputItems(TagUtils.createTGTag("ores/emerald"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.BERYLLIUM_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("mineral_sand_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/basaltic_mineral_sand"))
                .inputItems(TagUtils.createTGTag("ores/granitic_mineral_sand"))
                .inputItems(TagUtils.createTGTag("ores/fullers_earth"))
                .inputItems(TagUtils.createTGTag("ores/gypsum"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.MINERAL_SAND_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("topaz_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/blue_topaz"))
                .inputItems(TagUtils.createTGTag("ores/topaz"))
                .inputItems(TagUtils.createTGTag("ores/chalcocite"))
                .inputItems(TagUtils.createTGTag("ores/bornite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.TOPAZ_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("manganese_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/grossular"))
                .inputItems(TagUtils.createTGTag("ores/pyrolusite"))
                .inputItems(TagUtils.createTGTag("ores/tantalite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.MANGANESE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("cassiterite_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/tin"))
                .inputItems(TagUtils.createTGTag("ores/cassiterite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.CASSITERITE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("naquadah_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/naquadah"))
                .inputItems(TagUtils.createTGTag("ores/plutonium"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.NAQUADAH_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("sapphire_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/almandine"))
                .inputItems(TagUtils.createTGTag("ores/pyrope"))
                .inputItems(TagUtils.createTGTag("ores/sapphire"))
                .inputItems(TagUtils.createTGTag("ores/green_sapphire"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.SAPPHIRE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("diamond_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/graphite"))
                .inputItems(TagUtils.createTGTag("ores/diamond"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.DIAMOND_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("bauxite_vein_end_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/bauxite"))
                .inputItems(TagUtils.createTGTag("ores/ilmenite"))
                .inputItems(TagUtils.createTGTag("ores/aluminium"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.BAUXITE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("garnet_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/red_garnet"))
                .inputItems(TagUtils.createTGTag("ores/yellow_garnet"))
                .inputItems(TagUtils.createTGTag("ores/amethyst"))
                .inputItems(TagUtils.createTGTag("ores/opal"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.GARNET_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("garnet_tin_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/cassiterite_sand"))
                .inputItems(TagUtils.createTGTag("ores/garnet_sand"))
                .inputItems(TagUtils.createTGTag("ores/asbestos"))
                .inputItems(TagUtils.createTGTag("ores/diatomite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.GARNET_TIN_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("biological_cells")
                .chancedInput(GTOItems.GLACIO_SPIRIT.asStack(), 4000, 100)
                .inputItems(GTOItems.BIOLOGICAL_CELLS.asStack(32))
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(400))
                .outputItems(GTOItems.BIOLOGICAL_CELLS.asStack(64))
                .EUt(122880)
                .duration(800)
                .addData("filter_casing", 2)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("glacio_spirit")
                .chancedInput(GTOItems.GLACIO_SPIRIT.asStack(4), 1000, 100)
                .inputItems(TagPrefix.dust, GTOMaterials.Celestine, 16)
                .inputItems(GTOItems.ESSENCE.asItem())
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 100))
                .inputFluids(GTMaterials.Ice.getFluid(900))
                .outputItems(GTOItems.GLACIO_SPIRIT.asStack(64))
                .EUt(30720)
                .duration(2000)
                .addData("radioactivity", 40)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("tetrahedrite_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/tetrahedrite"))
                .inputItems(TagUtils.createTGTag("ores/copper"))
                .inputItems(TagUtils.createTGTag("ores/stibnite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.TETRAHEDRITE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("copper_tin_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/chalcopyrite"))
                .inputItems(TagUtils.createTGTag("ores/zeolite"))
                .inputItems(TagUtils.createTGTag("ores/cassiterite"))
                .inputItems(TagUtils.createTGTag("ores/realgar"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.COPPER_TIN_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("space_essence")
                .inputItems(TagUtils.createTag(GTOCore.id("vein_essence")))
                .inputItems(new ItemStack(AEItems.SKY_DUST.asItem()))
                .inputItems(TagPrefix.dustTiny, GTMaterials.NetherStar)
                .inputFluids(GTMaterials.Biomass.getFluid(100))
                .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(10))
                .outputItems(GTOItems.SPACE_ESSENCE.asItem())
                .EUt(480)
                .duration(1200)
                .addData("radioactivity", 180)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("echo")
                .notConsumable(new ItemStack(Blocks.SCULK_SHRIEKER.asItem(), 64))
                .notConsumable(new ItemStack(Blocks.SCULK_SENSOR.asItem(), 64))
                .inputItems(new ItemStack(Blocks.DIRT.asItem(), 64))
                .inputItems(new ItemStack(Blocks.SCULK_VEIN.asItem(), 64))
                .inputFluids(GTOMaterials.UnknowWater.getFluid(1000))
                .inputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 1000))
                .outputItems(TagPrefix.block, GTMaterials.Sculk, 64)
                .outputFluids(GTMaterials.EchoShard.getFluid(10000))
                .EUt(1920)
                .duration(2400)
                .addData("filter_casing", 3)
                .addData("radioactivity", 380)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("sulfur_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/sulfur"))
                .inputItems(TagUtils.createTGTag("ores/pyrite"))
                .inputItems(TagUtils.createTGTag("ores/sphalerite"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.SULFUR_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("variation_wood1")
                .inputItems(GTOBlocks.BARNARDA_C_LOG.asStack(64))
                .inputItems(new ItemStack(Blocks.WARPED_STEM.asItem(), 16))
                .inputItems(TagPrefix.dust, GTMaterials.Wood, 64)
                .inputItems(TagPrefix.dust, GTMaterials.Lapotron)
                .inputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .inputFluids(GTMaterials.Biomass.getFluid(1000))
                .outputItems(GTOBlocks.VARIATION_WOOD.asStack(64))
                .EUt(1966080)
                .duration(2400)
                .addData("filter_casing", 3)
                .addData("radioactivity", 440)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("coal_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/coal"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.COAL_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("scheelite_vein_essence")
                .inputItems(GTOItems.ESSENCE_SEED.asItem())
                .inputItems(TagUtils.createTGTag("ores/scheelite"))
                .inputItems(TagUtils.createTGTag("ores/tungstate"))
                .inputItems(TagUtils.createTGTag("ores/lithium"))
                .inputFluids(GTMaterials.Biomass.getFluid(10000))
                .inputFluids(GTMaterials.Milk.getFluid(10000))
                .outputItems(GTOItems.SCHEELITE_VEIN_ESSENCE.asStack(64))
                .EUt(480)
                .duration(12000)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("super_cerebrum")
                .inputItems(GTOItems.CEREBRUM.asItem())
                .inputItems(TagPrefix.dustSmall, GTMaterials.Naquadria)
                .outputItems(GTOItems.SUPER_CEREBRUM.asItem())
                .inputFluids(GTOMaterials.RapidlyReplicatingAnimalCells.getFluid(100))
                .inputFluids(GTMaterials.Mutagen.getFluid(100))
                .EUt(7680)
                .duration(200)
                .addData("filter_casing", 2)
                .addData("radioactivity", 60)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("brevibacterium")
                .notConsumable(GTOItems.BREVIBACTERIUM_PETRI_DISH.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.BrevibacteriumFlavium, 16)
                .outputItems(TagPrefix.dust, GTOMaterials.BrevibacteriumFlavium, 64)
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(100))
                .inputFluids(GTMaterials.Bacteria.getFluid(100))
                .EUt(480)
                .duration(200)
                .addData("filter_casing", 2)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("bifidobacteriumm")
                .notConsumable(GTOItems.BIFIDOBACTERIUMM_PETRI_DISH.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.BifidobacteriumBreve, 16)
                .outputItems(TagPrefix.dust, GTOMaterials.BifidobacteriumBreve, 64)
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(100))
                .inputFluids(GTMaterials.Bacteria.getFluid(100))
                .EUt(480)
                .duration(200)
                .addData("filter_casing", 2)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("eschericia")
                .notConsumable(GTOItems.ESCHERICIA_PETRI_DISH.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.EschericiaColi, 16)
                .outputItems(TagPrefix.dust, GTOMaterials.EschericiaColi, 64)
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(100))
                .inputFluids(GTMaterials.Bacteria.getFluid(100))
                .EUt(480)
                .duration(200)
                .addData("filter_casing", 2)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("streptococcus")
                .notConsumable(GTOItems.STREPTOCOCCUS_PETRI_DISH.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.StreptococcusPyogenes, 16)
                .outputItems(TagPrefix.dust, GTOMaterials.StreptococcusPyogenes, 64)
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(100))
                .inputFluids(GTMaterials.Bacteria.getFluid(100))
                .EUt(480)
                .duration(200)
                .addData("filter_casing", 2)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("cupriavidus")
                .notConsumable(GTOItems.CUPRIAVIDUS_PETRI_DISH.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.CupriavidusNecator, 16)
                .outputItems(TagPrefix.dust, GTOMaterials.CupriavidusNecator, 64)
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(100))
                .inputFluids(GTMaterials.Bacteria.getFluid(100))
                .EUt(480)
                .duration(200)
                .addData("filter_casing", 2)
                .save();

        INCUBATOR_RECIPES.recipeBuilder("shewanella")
                .notConsumable(GTOItems.SHEWANELLA_PETRI_DISH.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.Shewanella, 16)
                .outputItems(TagPrefix.dust, GTOMaterials.Shewanella, 64)
                .inputFluids(GTOMaterials.BacterialGrowthMedium.getFluid(100))
                .inputFluids(GTMaterials.Bacteria.getFluid(100))
                .EUt(480)
                .duration(200)
                .addData("filter_casing", 2)
                .save();
        INCUBATOR_RECIPES.builder("spider_eye")
                .chancedInput(new ItemStack(Items.SPIDER_EYE.asItem()), 4000, 100)
                .inputItems(GTItems.STEM_CELLS.asStack())
                .inputItems("botania:mutated_seeds", 4)
                .outputItems(new ItemStack(Items.SPIDER_EYE.asItem(), 64))
                .inputFluids(GTOMaterials.BiohmediumSterilized, 50)
                .inputFluids(GTOMaterials.BloodCells, 100)
                .outputFluids(GTOMaterials.AnimalCells, 400)
                .EUt(1920)
                .duration(400)
                .save();
    }
}
