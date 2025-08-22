package com.gtocore.integration.emi.oreprocessing;

import com.gtocore.common.data.machines.MultiBlockC;
import com.gtocore.data.recipe.generated.GTOOreRecipeHandler;

import com.gtolib.api.recipe.ContentBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.TankWidget;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.integration.xei.entry.fluid.FluidEntryList;
import com.gregtechceu.gtceu.integration.xei.entry.fluid.FluidStackList;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemEntryList;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemStackList;
import com.gregtechceu.gtceu.integration.xei.handlers.fluid.CycleFluidEntryHandler;
import com.gregtechceu.gtceu.integration.xei.handlers.item.CycleItemEntryHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;

import java.util.ArrayList;
import java.util.List;

final class OreByProductWrapper {

    private static final ImmutableList<TagPrefix> IN_PROCESSING_STEPS = ImmutableList.of(TagPrefix.crushed, TagPrefix.crushedPurified, TagPrefix.dustImpure, TagPrefix.dustPure, TagPrefix.crushedRefined);
    private static final ImmutableList<ItemStack> ALWAYS_MACHINES = ImmutableList.of(
            // GTMachines.MACERATOR[GTValues.LV].asStack(),
            MultiBlockC.STEAM_CRUSHER.asStack(), GTMachines.MACERATOR[GTValues.LV].asStack(), GTMachines.CENTRIFUGE[GTValues.LV].asStack(), GTMachines.ORE_WASHER[GTValues.LV].asStack(), GTMachines.THERMAL_CENTRIFUGE[GTValues.LV].asStack(), GTMachines.MACERATOR[GTValues.LV].asStack(), GTMachines.MACERATOR[GTValues.LV].asStack(), GTMachines.CENTRIFUGE[GTValues.LV].asStack());
    private final Int2ObjectMap<Content> chances = new Int2ObjectOpenHashMap<>();
    private final List<ItemEntryList> itemInputs = new ArrayList<>();
    private final NonNullList<ItemStack> itemOutputs = NonNullList.create();
    private final List<FluidEntryList> fluidInputs = new ArrayList<>();
    private boolean hasDirectSmelt = false;
    private boolean hasChemBath = false;
    private boolean hasSeparator = false;
    private boolean hasSifter = false;
    private int currentSlot;

    private static ItemStack patchedAddPrefix(Material mat) {
        final var transformed = GTOOreRecipeHandler.getOutputMaterial(mat);
        return ChemicalHelper.get(TagPrefix.dust, transformed, 1);
    }

    public OreByProductWrapper(Material material) {
        var property = material.getProperty(PropertyKey.ORE);
        int oreMultiplier = property.getOreMultiplier();
        int byproductMultiplier = property.getByProductMultiplier();
        currentSlot = 0;
        Material[] byproducts = (new Material[] { property.getOreByProduct(0, material), property.getOreByProduct(1, material), property.getOreByProduct(2, material), property.getOreByProduct(3, material) });
        // "INPUTS"
        ObjectIntPair<Material> washedIn = property.getWashedIn();
        List<Material> separatedInto = property.getSeparatedInto();
        ItemStackList oreStacks = new ItemStackList();
        var rawOre = ChemicalHelper.get(TagPrefix.rawOre, material);
        oreStacks.add(rawOre);
        itemInputs.add(oreStacks);
        // set up machines as inputs
        List<ItemStack> simpleWashers = new ArrayList<>();
        simpleWashers.add(new ItemStack(Items.CAULDRON));
        simpleWashers.add(GTMachines.ORE_WASHER[GTValues.LV].asStack());
        if (!material.hasProperty(PropertyKey.BLAST) && Minecraft.getInstance().level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(rawOre), Minecraft.getInstance().level).isPresent()) {
            addToInputs(new ItemStack(Blocks.FURNACE));
            hasDirectSmelt = true;
        } else {
            addToInputs(ItemStack.EMPTY);
        }
        for (ItemStack stack : ALWAYS_MACHINES) {
            addToInputs(stack);
        }
        // same amount of lines as a for loop :trol:
        itemInputs.add(ItemStackList.of(simpleWashers));
        itemInputs.add(ItemStackList.of(simpleWashers));
        itemInputs.add(ItemStackList.of(simpleWashers));
        if (!washedIn.first().isNull()) {
            hasChemBath = true;
            addToInputs(GTMachines.CHEMICAL_BATH[GTValues.LV].asStack());
        } else {
            addToInputs(ItemStack.EMPTY);
        }
        if (separatedInto != null && !separatedInto.isEmpty()) {
            hasSeparator = true;
            addToInputs(GTMachines.ELECTROMAGNETIC_SEPARATOR[GTValues.LV].asStack());
        } else {
            addToInputs(ItemStack.EMPTY);
        }
        if (material.hasProperty(PropertyKey.GEM)) {
            hasSifter = true;
            addToInputs(GTMachines.SIFTER[GTValues.LV].asStack());
        } else {
            addToInputs(ItemStack.EMPTY);
        }
        // add prefixes that should count as inputs to input lists (they will not be
        // displayed in actual page)
        for (TagPrefix prefix : IN_PROCESSING_STEPS) {
            itemInputs.add(ItemStackList.of(ChemicalHelper.get(prefix, material)));
        }
        // total number of inputs added
        currentSlot += 21;
        // BASIC PROCESSING
        // begin lots of logic duplication from OreRecipeHandler
        // direct smelt
        if (hasDirectSmelt) {
            ItemStack smeltingResult;
            Material smeltingMaterial = property.getDirectSmeltResult().isNull() ? material : property.getDirectSmeltResult();
            /*
             * if (smeltingMaterial.hasProperty(PropertyKey.INGOT)) {
             * smeltingResult = ChemicalHelper.get(TagPrefix.ingot, smeltingMaterial);
             * } else
             */
            if (smeltingMaterial.hasProperty(PropertyKey.GEM)) {
                smeltingResult = ChemicalHelper.get(TagPrefix.gem, smeltingMaterial);
            } else {
                smeltingResult = ChemicalHelper.get(TagPrefix.dust, smeltingMaterial);
            }
            smeltingResult.setCount(smeltingResult.getCount() * oreMultiplier);
            addToOutputs(smeltingResult);
        } else {
            addEmptyOutputs(1);
        }
        // macerate ore -> crushed
        addToOutputs(material, TagPrefix.crushed, 2 * oreMultiplier);
        if (!ChemicalHelper.get(TagPrefix.gem, byproducts[0]).isEmpty()) {
            addToOutputs(byproducts[0], TagPrefix.gem, 1);
        } else {
            addToOutputs(byproducts[0], TagPrefix.dust, 1);
        }
        addChance(1400, 850);
        // macerate crushed -> impure
        addToOutputs(material, TagPrefix.dustImpure, 1);
        addToOutputs(byproducts[0], TagPrefix.dust, byproductMultiplier);
        addChance(1400, 850);
        // centrifuge impure -> dust
        addToOutputsPatched(material);
        addToOutputs(byproducts[0], TagPrefix.dust, 1);
        addChance(1111, 0);
        // ore wash crushed -> crushed purified
        addToOutputs(material, TagPrefix.crushedPurified, 1);
        addToOutputs(byproducts[0], TagPrefix.dust, 1);
        addChance(3333, 0);
        FluidStackList tagList = new FluidStackList();
        tagList.add(GTMaterials.Water.getFluid(1000));
        tagList.add(GTMaterials.DistilledWater.getFluid(100));
        fluidInputs.add(tagList);
        // TC crushed/crushed purified -> centrifuged
        addToOutputs(material, TagPrefix.crushedRefined, 1);
        addToOutputs(byproducts[1], TagPrefix.dust, byproductMultiplier);
        addChance(3333, 0);
        // macerate centrifuged -> dust
        addToOutputsPatched(material);
        addToOutputs(byproducts[2], TagPrefix.dust, 1);
        addChance(1400, 850);
        // macerate crushed purified -> purified
        addToOutputs(material, TagPrefix.dustPure, 1);
        addToOutputs(byproducts[1], TagPrefix.dust, 1);
        addChance(1400, 850);
        // centrifuge purified -> dust
        addToOutputsPatched(material);
        addToOutputs(byproducts[1], TagPrefix.dust, 1);
        addChance(1111, 0);
        // cauldron/simple washer
        addToOutputs(material, TagPrefix.crushed, 1);
        addToOutputs(material, TagPrefix.crushedPurified, 1);
        addToOutputs(material, TagPrefix.dustImpure, 1);
        addToOutputsPatched(material);
        addToOutputs(material, TagPrefix.dustPure, 1);
        addToOutputsPatched(material);
        // ADVANCED PROCESSING
        // chem bath
        if (hasChemBath) {
            addToOutputs(material, TagPrefix.crushedPurified, 1);
            addToOutputs(byproducts[3], TagPrefix.dust, byproductMultiplier);
            addChance(7000, 580);
            fluidInputs.add(FluidStackList.of(washedIn.first().getFluid(washedIn.rightInt())));
        } else {
            addEmptyOutputs(2);
            fluidInputs.add(new FluidStackList());
        }
        // electromagnetic separator
        if (hasSeparator) {
            // noinspection DataFlowIssue
            TagPrefix prefix = (separatedInto.get(separatedInto.size() - 1).getBlastTemperature() == 0 && separatedInto.get(separatedInto.size() - 1).hasProperty(PropertyKey.INGOT)) ? TagPrefix.nugget : TagPrefix.dust;
            ItemStack separatedStack2 = ChemicalHelper.get(prefix, separatedInto.get(separatedInto.size() - 1), prefix == TagPrefix.nugget ? 2 : 1);
            addToOutputsPatched(material);
            addToOutputs(separatedInto.get(0), TagPrefix.dust, 1);
            addChance(1000, 250);
            addToOutputs(separatedStack2);
            addChance(prefix == TagPrefix.dust ? 500 : 2000, prefix == TagPrefix.dust ? 150 : 600);
        } else {
            addEmptyOutputs(3);
        }
        // sifter
        if (hasSifter) {
            boolean highOutput = material.hasFlag(MaterialFlags.HIGH_SIFTER_OUTPUT);
            ItemStack flawedStack = ChemicalHelper.get(TagPrefix.gemFlawed, material);
            ItemStack chippedStack = ChemicalHelper.get(TagPrefix.gemChipped, material);
            addToOutputs(material, TagPrefix.gemExquisite, 1);
            addGemChance(300, 100, 500, 150, highOutput);
            addToOutputs(material, TagPrefix.gemFlawless, 1);
            addGemChance(1000, 150, 1500, 200, highOutput);
            addToOutputs(material, TagPrefix.gem, 1);
            addGemChance(3500, 500, 5000, 1000, highOutput);
            addToOutputs(material, TagPrefix.dustPure, 1);
            addGemChance(5000, 750, 2500, 500, highOutput);
            if (!flawedStack.isEmpty()) {
                addToOutputs(flawedStack);
                addGemChance(2500, 300, 2000, 500, highOutput);
            } else {
                addEmptyOutputs(1);
            }
            if (!chippedStack.isEmpty()) {
                addToOutputs(chippedStack);
                addGemChance(3500, 400, 3000, 350, highOutput);
            } else {
                addEmptyOutputs(1);
            }
        } else {
            addEmptyOutputs(6);
        }
    }

    public void getTooltip(int slotIndex, List<Component> tooltips) {
        if (chances.containsKey(slotIndex)) {
            Content entry = chances.get(slotIndex);
            float chance = 100 * (float) entry.chance / ContentBuilder.maxChance;
            float boost = entry.tierChanceBoost / 100.0F;
            tooltips.add(FormattingUtil.formatPercentage2Places("gtceu.gui.content.chance_base", chance));
            tooltips.add(FormattingUtil.formatPercentage2Places("gtceu.gui.content.chance_tier_boost_plus", boost));
        }
    }

    public Content getChance(int slot) {
        return chances.get(slot);
    }

    public boolean hasSifter() {
        return hasSifter;
    }

    public boolean hasSeparator() {
        return hasSeparator;
    }

    public boolean hasChemBath() {
        return hasChemBath;
    }

    public boolean hasDirectSmelt() {
        return hasDirectSmelt;
    }

    private void addToOutputs(Material material, TagPrefix prefix, int size) {
        addToOutputs(ChemicalHelper.get(prefix, material, size));
    }

    private void addToOutputsPatched(Material material) {
        addToOutputs(patchedAddPrefix(material));
    }

    private void addToOutputs(ItemStack stack) {
        itemOutputs.add(stack);
        currentSlot++;
    }

    private void addEmptyOutputs(int amount) {
        for (int i = 0; i < amount; i++) {
            addToOutputs(ItemStack.EMPTY);
        }
    }

    private void addToInputs(ItemStack stack) {
        itemInputs.add(ItemStackList.of(stack));
    }

    private void addChance(int base, int tier) {
        // this is solely for the chance overlay and tooltip, neither of which care
        // about the ItemStack
        chances.put(currentSlot - 1, new Content(ItemStack.EMPTY, base, ContentBuilder.maxChance, tier));
    }

    // make the code less :weary:
    private void addGemChance(int baseLow, int tierLow, int baseHigh, int tierHigh, boolean high) {
        if (high) {
            addChance(baseHigh, tierHigh);
        } else {
            addChance(baseLow, tierLow);
        }
    }

    public List<ItemEntryList> getItemInputs() {
        return this.itemInputs;
    }

    public NonNullList<ItemStack> getItemOutputs() {
        return this.itemOutputs;
    }

    public List<FluidEntryList> getFluidInputs() {
        return this.fluidInputs;
    }
}

class OreByProductWidget extends WidgetGroup {

    // XY positions of every item and fluid, in three enormous lists
    private static final ImmutableList<Integer> ITEM_INPUT_LOCATIONS = ImmutableList.of(3, 3,  // ore
            23, 3,  // furnace (direct smelt)
            3, 24,  // macerator (ore -> crushed)
            23, 71,  // macerator (crushed -> impure)
            50, 80,  // centrifuge (impure -> dust)
            24, 25,  // ore washer
            97, 71,  // thermal centrifuge
            70, 80,  // macerator (centrifuged -> dust)
            114, 48,  // macerator (crushed purified -> purified)
            133, 71,  // centrifuge (purified -> dust)
            3, 123,  // cauldron / simple washer (crushed)
            41, 145,  // cauldron (impure)
            102, 145,  // cauldron (purified)
            24, 48,  // chem bath
            155, 71,  // electro separator
            101, 25 // sifter
    );
    private static final ImmutableList<Integer> ITEM_OUTPUT_LOCATIONS = ImmutableList.of(46, 3,  // smelt result: 0
            3, 47,  // ore -> crushed: 2
            3, 65,  // byproduct: 4
            23, 92,  // crushed -> impure: 6
            23, 110,  // byproduct: 8
            50, 101,  // impure -> dust: 10
            50, 119,  // byproduct: 12
            64, 25,  // crushed -> crushed purified (wash): 14
            82, 25,  // byproduct: 16
            97, 92,  // crushed/crushed purified -> centrifuged: 18
            97, 110,  // byproduct: 20
            70, 101,  // centrifuged -> dust: 22
            70, 119,  // byproduct: 24
            137, 47,  // crushed purified -> purified: 26
            155, 47,  // byproduct: 28
            133, 92,  // purified -> dust: 30
            133, 110,  // byproduct: 32
            3, 105,  // crushed cauldron: 34
            3, 145,  // -> purified crushed: 36
            23, 145,  // impure cauldron: 38
            63, 145,  // -> dust: 40
            84, 145,  // purified cauldron: 42
            124, 145,  // -> dust: 44
            64, 48,  // crushed -> crushed purified (chem bath): 46
            82, 48,  // byproduct: 48
            155, 92,  // purified -> dust (electro separator): 50
            155, 110,  // byproduct 1: 52
            155, 128,  // byproduct 2: 54
            119, 3,  // sifter outputs... : 56
            137, 3,  // 58
            155, 3,  // 60
            119, 21,  // 62
            137, 21,  // 64
            155, 21 // 66
    );
    private static final ImmutableList<Integer> FLUID_LOCATIONS = ImmutableList.of(42, 25,  // washer in
            42, 48 // chem bath in
    );
    // Used to set intermediates as both input and output
    private static final ImmutableSet<Integer> FINAL_OUTPUT_INDICES = ImmutableSet.of(0, 4, 8, 10, 12, 16, 20, 22, 24, 28, 30, 32, 40, 44, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66);

    OreByProductWidget(Material material) {
        super(0, 0, 176, 166);
        setClientSideWidget();
        setRecipe(new OreByProductWrapper(material));
    }

    private void setRecipe(OreByProductWrapper recipeWrapper) {
        List<Boolean> itemOutputExists = new ArrayList<>();
        // only draw slot on inputs if it is the ore
        addWidget(new ImageWidget(ITEM_INPUT_LOCATIONS.get(0), ITEM_INPUT_LOCATIONS.get(1), 18, 18, GuiTextures.SLOT));
        boolean hasSifter = recipeWrapper.hasSifter();
        addWidget(new ImageWidget(0, 0, 176, 166, GuiTextures.OREBY_BASE));
        if (recipeWrapper.hasDirectSmelt()) {
            addWidget(new ImageWidget(0, 0, 176, 166, GuiTextures.OREBY_SMELT));
        }
        if (recipeWrapper.hasChemBath()) {
            addWidget(new ImageWidget(0, 0, 176, 166, GuiTextures.OREBY_CHEM));
        }
        if (recipeWrapper.hasSeparator()) {
            addWidget(new ImageWidget(0, 0, 176, 166, GuiTextures.OREBY_SEP));
        }
        if (hasSifter) {
            addWidget(new ImageWidget(0, 0, 176, 166, GuiTextures.OREBY_SIFT));
        }
        List<ItemEntryList> itemInputs = recipeWrapper.getItemInputs();
        CycleItemEntryHandler itemInputsHandler = new CycleItemEntryHandler(itemInputs);
        WidgetGroup itemStackGroup = new WidgetGroup();
        for (int i = 0; i < ITEM_INPUT_LOCATIONS.size(); i += 2) {
            final int finalI = i;
            itemStackGroup.addWidget(new SlotWidget(itemInputsHandler, i / 2, ITEM_INPUT_LOCATIONS.get(i), ITEM_INPUT_LOCATIONS.get(i + 1)).setCanTakeItems(false).setCanPutItems(false).setIngredientIO(IngredientIO.INPUT).setOnAddedTooltips((slot, tooltips) -> recipeWrapper.getTooltip(finalI / 2, tooltips)).setBackground((IGuiTexture) null));
        }
        NonNullList<ItemStack> itemOutputs = recipeWrapper.getItemOutputs();
        CustomItemStackHandler itemOutputsHandler = new CustomItemStackHandler(itemOutputs);
        for (int i = 0; i < ITEM_OUTPUT_LOCATIONS.size(); i += 2) {
            int slotIndex = i / 2;
            float xeiChance = 1.0F;
            Content chance = recipeWrapper.getChance(i / 2 + itemInputs.size());
            IGuiTexture overlay = null;
            if (chance != null) {
                xeiChance = (float) chance.chance / ContentBuilder.maxChance;
                overlay = chance.createOverlay(false, 0, 0, null);
            }
            if (itemOutputs.get(slotIndex).isEmpty()) {
                itemOutputExists.add(false);
                continue;
            }
            itemStackGroup.addWidget(new SlotWidget(itemOutputsHandler, slotIndex, ITEM_OUTPUT_LOCATIONS.get(i), ITEM_OUTPUT_LOCATIONS.get(i + 1)).setCanTakeItems(false).setCanPutItems(false).setIngredientIO(FINAL_OUTPUT_INDICES.contains(i) ? IngredientIO.OUTPUT : IngredientIO.BOTH).setXEIChance(xeiChance).setOnAddedTooltips((slot, tooltips) -> recipeWrapper.getTooltip(slotIndex + itemInputs.size(), tooltips)).setBackground((IGuiTexture) null).setOverlay(overlay));
            itemOutputExists.add(true);
        }
        List<FluidEntryList> fluidInputs = recipeWrapper.getFluidInputs();
        CycleFluidEntryHandler fluidInputsHandler = new CycleFluidEntryHandler(fluidInputs);
        WidgetGroup fluidStackGroup = new WidgetGroup();
        for (int i = 0; i < FLUID_LOCATIONS.size(); i += 2) {
            int slotIndex = i / 2;
            if (!fluidInputs.get(slotIndex).isEmpty()) {
                var tank = new TankWidget(new CustomFluidTank(fluidInputsHandler.getFluidInTank(slotIndex)), FLUID_LOCATIONS.get(i), FLUID_LOCATIONS.get(i + 1), false, false).setIngredientIO(IngredientIO.INPUT).setBackground(GuiTextures.FLUID_SLOT).setShowAmount(false);
                fluidStackGroup.addWidget(tank);
            }
        }
        this.addWidget(itemStackGroup);
        this.addWidget(fluidStackGroup);
        for (int i = 0; i < ITEM_OUTPUT_LOCATIONS.size(); i += 2) {
            // stupid hack to show all sifter slots if the first one exists
            if (itemOutputExists.get(i / 2) || (i > 28 << 1 && itemOutputExists.get(28) && hasSifter)) {
                addWidget(this.widgets.size() - 3, new ImageWidget(ITEM_OUTPUT_LOCATIONS.get(i), ITEM_OUTPUT_LOCATIONS.get(i + 1), 18, 18, GuiTextures.SLOT));
            }
        }
    }
}
