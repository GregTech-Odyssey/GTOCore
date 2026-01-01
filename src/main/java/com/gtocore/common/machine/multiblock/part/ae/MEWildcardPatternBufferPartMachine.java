package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.MyPatternDetailsHelper;
import com.gtolib.api.ae2.pattern.IParallelPatternDetails;
import com.gtolib.api.ae2.stacks.IIngredientConvertible;
import com.gtolib.api.ae2.stacks.TagPrefixKey;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.gui.ktflexible.VBoxBuilder;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.gui.widget.PhantomFluidWidget;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.collection.FastObjectArrayList;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.ProcessingPatternItem;
import com.fast.recipesearch.IntLongMap;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.layout.Align;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;

import static com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget.drawSelectionOverlay;
import static com.lowdragmc.lowdraglib.gui.util.DrawerHelper.drawItemStack;

@DataGeneratorScanned
public class MEWildcardPatternBufferPartMachine extends MEPatternBufferPartMachineKt {

    private List<IPatternDetails> cachedPatterns;
    private boolean dirty = true;
    private boolean lock = false;
    private int scannedPatterns = 0;
    @Getter
    @Setter
    @Persisted
    private int patternPriority = 0;
    @Getter
    @Persisted
    private int maxFluidsOutput = 1;
    @Getter
    @Persisted
    private int maxItemsOutput = 1;
    @Persisted
    private final CustomItemStackHandler blacklistedItems;
    @Persisted
    private final ItemStackTransfer blacklistedItemsStorageTransfer;
    @Persisted
    private final CustomFluidTank[] blacklistedFluids;
    private final Int2ReferenceOpenHashMap<Material> blacklistedMaterials = new Int2ReferenceOpenHashMap<>();

    public MEWildcardPatternBufferPartMachine(@NotNull MetaMachineBlockEntity holder) {
        super(holder, 1);

        blacklistedItems = new CustomItemStackHandler(18);
        blacklistedItemsStorageTransfer = new ItemStackTransfer(36);
        blacklistedFluids = new CustomFluidTank[18];
        Arrays.setAll(blacklistedFluids, i -> new CustomFluidTank(1));

        shareInventory.addChangedListener(this::requestPatternUpdate);
        circuitInventorySimulated.addChangedListener(this::requestPatternUpdate);
        shareTank.addChangedListener(this::requestPatternUpdate);
        getInternalInventory()[0].shareTank.addChangedListener(this::requestPatternUpdate);
        Runnable requestPatternUpdateIfUnlocked = () -> {
            if (!getInternalInventory()[0].isLock()) {
                requestPatternUpdate();
            }
        };
        getInternalInventory()[0].circuitInventory.addChangedListener(requestPatternUpdateIfUnlocked);
        getInternalInventory()[0].shareInventory.addChangedListener(requestPatternUpdateIfUnlocked);
        getInternalInventory()[0].setShouldLockRecipe(false);
    }

    @Override
    public boolean patternFilter(ItemStack stack) {
        return stack.getItem() instanceof ProcessingPatternItem;
    }

    @Override
    public @Nullable IPatternDetails decodePattern(@NotNull ItemStack stack, int index) {
        var pattern = MyPatternDetailsHelper.decodePattern(stack, holder, getGrid());
        if (pattern == null) return null;
        return IParallelPatternDetails.of(pattern, getLevel(), 1);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        loadBlacklistData();
    }

    @Override
    public void onPatternChange(int index) {
        super.onPatternChange(index);
        requestPatternUpdate();
    }

    @Override
    public void onChanged() {
        super.onChanged();
    }

    private void requestPatternUpdate() {
        if (lock) return;
        lock = true;
        this.dirty = true;
        ICraftingProvider.requestUpdate(getMainNode());
        lock = false;
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        try {
            lock = true;
            return getInternalInventory()[0].pushPattern(patternDetails, inputHolder);
        } finally {
            lock = false;
        }
    }

    private void setMaxFluidsOutput(int integer) {
        final int last = this.maxFluidsOutput;
        maxFluidsOutput = Math.max(0, integer);
        if (last != this.maxFluidsOutput) {
            requestPatternUpdate();
        }
    }

    private void setMaxItemsOutput(int integer) {
        final int last = this.maxItemsOutput;
        maxItemsOutput = Math.max(0, integer);
        if (last != this.maxItemsOutput) {
            requestPatternUpdate();
        }
    }

    private void loadBlacklistData() {
        blacklistedMaterials.clear();
        int i = 0;
        for (; i < blacklistedItems.getSlots(); i++) {
            var stack = blacklistedItems.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            var mat = ChemicalHelper.getMaterialStack(stack).material();
            if (mat != GTMaterials.NULL) {
                blacklistedMaterials.put(i, mat);
            }
        }
        for (; i < blacklistedItems.getSlots() + blacklistedFluids.length; i++) {
            var tank = blacklistedFluids[i - blacklistedItems.getSlots()];
            if (tank.isEmpty()) continue;
            var mat = ChemicalHelper.getMaterial(tank.getFluid().getFluid());
            if (mat != GTMaterials.NULL) {
                blacklistedMaterials.put(i, mat);
            }
        }
        requestPatternUpdate();
        ICraftingProvider.requestUpdate(getMainNode());
    }

    /**
     * 开销大约为1.5ms~2ms每次调用
     */
    private void rebuildCacheIfNeeded(List<IPatternDetails> patterns) {
        if (dirty || cachedPatterns == null) {
            dirty = false;
            // profiler start
            long nanos = System.nanoTime();
            AtomicLong substitutingIngredients = new AtomicLong();
            AtomicLong validatingPatterns = new AtomicLong();

            // var patterns = super.getAvailablePatterns();
            var newPatterns = new FastObjectArrayList<IPatternDetails>();

            var sharedInputs = new ArrayList[patterns.size()];
            var tagPrefixInputs = new ArrayList[patterns.size()];
            var sharedOutputs = new ArrayList[patterns.size()];
            var tagPrefixOutputs = new ArrayList[patterns.size()];

            long startSubstituting = System.nanoTime();
            for (int i = 0; i < patterns.size(); i++) {
                var p = patterns.get(i);
                if (p instanceof AEProcessingPattern processingPattern) {

                    var sparseInput = processingPattern.getSparseInputs();
                    var sharedInputList = new ArrayList<GenericStack>(sparseInput.length);
                    var tagPrefixInputList = new ArrayList<GenericStack>(sparseInput.length);
                    for (var stack : sparseInput) {
                        if (stack.what() instanceof TagPrefixKey) {
                            tagPrefixInputList.add(stack);
                        } else {
                            sharedInputList.add(stack);
                        }
                    }
                    sharedInputs[i] = sharedInputList;
                    tagPrefixInputs[i] = tagPrefixInputList;

                    var sparseOutput = processingPattern.getSparseOutputs();
                    var sharedOutputList = new ArrayList<GenericStack>(sparseOutput.length);
                    var tagPrefixOutputList = new ArrayList<GenericStack>(sparseOutput.length);
                    for (var stack : sparseOutput) {
                        if (stack.what() instanceof TagPrefixKey) {
                            tagPrefixOutputList.add(stack);
                        } else {
                            sharedOutputList.add(stack);
                        }
                    }
                    sharedOutputs[i] = sharedOutputList;
                    tagPrefixOutputs[i] = tagPrefixOutputList;
                }
            }
            substitutingIngredients.addAndGet(System.nanoTime() - startSubstituting);

            var blacklistSet = blacklistedMaterials.values();
            GTCEuAPI.materialManager.getRegisteredMaterials().forEach(material -> {
                if (blacklistSet.contains(material)) return;
                for (int i = 0; i < patterns.size(); i++) {
                    var cp = patterns.get(i);
                    if (cp instanceof AEProcessingPattern) {

                        List<GenericStack> input = new ArrayList<>(sharedInputs[i]);
                        var tagPrefixInput = tagPrefixInputs[i];
                        List<GenericStack> output = new ArrayList<>(sharedOutputs[i]);
                        var tagPrefixOutput = tagPrefixOutputs[i];

                        long startSubstituting1 = System.nanoTime();
                        try {
                            for (Object stack : tagPrefixInput) {
                                GenericStack gs = (GenericStack) stack;
                                var tagPrefixKey = (TagPrefixKey) gs.what();
                                var what = tagPrefixKey.getFromMaterial(material);
                                if (what == null) return;
                                input.add(new GenericStack(what, gs.amount()));
                            }
                            for (Object stack : tagPrefixOutput) {
                                GenericStack gs = (GenericStack) stack;
                                var tagPrefixKey = (TagPrefixKey) gs.what();
                                var what = tagPrefixKey.getFromMaterial(material);
                                if (what == null) return;
                                output.add(new GenericStack(what, gs.amount()));
                            }
                        } finally {
                            substitutingIngredients.addAndGet(System.nanoTime() - startSubstituting1);
                        }

                        long startValidating1 = System.nanoTime();
                        var detail = validatePattern(input.toArray(new GenericStack[0]), output.toArray(new GenericStack[0]));
                        if (detail != null) {
                            var converted = IParallelPatternDetails.of(convertPattern(detail, 0), getLevel(), 1);
                            newPatterns.add(converted);
                        }
                        validatingPatterns.addAndGet(System.nanoTime() - startValidating1);
                    }
                }
            });
            cachedPatterns = newPatterns;
            scannedPatterns = cachedPatterns.size();
            if (GTOConfig.INSTANCE.aeLog) {
                GTOCore.LOGGER.info("MEWildcardPatternBufferPartMachine recalculated patterns: {} patterns in {} ms",
                        scannedPatterns, (System.nanoTime() - nanos) / 1_000_000.0);
                GTOCore.LOGGER.info("  substituting ingredients took {} ms ({})%",
                        substitutingIngredients.get() / 1_000_000.0, substitutingIngredients.get() * 100.0 / (System.nanoTime() - nanos));
                GTOCore.LOGGER.info("  validating patterns took {} ms ({})%",
                        validatingPatterns.get() / 1_000_000.0, validatingPatterns.get() * 100.0 / (System.nanoTime() - nanos));
            }
            // profiler end
        }
    }

    @Override
    public @NotNull List<@NotNull IPatternDetails> getAvailablePatterns() {
        var patterns = super.getAvailablePatterns();
        if (patterns.isEmpty()) {
            patterns = readInv();
            if (patterns.isEmpty()) {
                scannedPatterns = 0;
                return patterns;
            }
        }
        rebuildCacheIfNeeded(patterns);
        return cachedPatterns;
    }

    private List<@NotNull IPatternDetails> readInv() {
        var pattern = getPatternInventory().getStackInSlot(0);
        var details = decodePattern(pattern, 0);
        return details == null ? List.of() : List.of(details);
    }

    // ========== Pattern Validation ==========

    private AEProcessingPattern validatePattern(GenericStack[] sparseInput, GenericStack[] sparseOutput) {
        ObjectHolder<Recipe> valid = new ObjectHolder<>(null);
        var inputHolder = virtual(sparseInput);
        if (recipeType == GTORecipeTypes.HATCH_COMBINED) {
            if (!getRecipeTypes().isEmpty()) {
                for (var rt : getRecipeTypes()) {
                    if (rt.findRecipe(inputHolder, r -> {
                        if (checkProb(r)) {
                            valid.value = (Recipe) r;
                            return true;
                        }
                        return false;
                    })) break;
                }
            }
        } else {
            recipeType.findRecipe(inputHolder, r -> {
                if (checkProb(r)) {
                    valid.value = (Recipe) r;
                    return true;
                }
                return false;
            });
        }
        var outPattern = MyPatternDetailsHelper.convertFromGTRecipe(valid.value, maxItemsOutput, maxFluidsOutput);
        // outPattern的output 需要包含 sparseOutput的所有东西
        if (outPattern != null) {
            var outSparse = outPattern.getSparseOutputs();
            for (var reqStack : sparseOutput) {
                boolean found = false;
                for (var outStack : outSparse) {
                    if (reqStack.what() == outStack.what() && reqStack.amount() <= outStack.amount()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return null;
                }
            }
            return outPattern;
        }
        return null;
    }

    private boolean checkProb(GTRecipe recipe) {
        for (var ingredient : recipe.getInputContents(ItemRecipeCapability.CAP)) {
            if (ingredient.chance != 10000 && ingredient.chance != 0) return false;
        }
        for (var ingredient : recipe.getInputContents(FluidRecipeCapability.CAP)) {
            if (ingredient.chance != 10000 && ingredient.chance != 0) return false;
        }
        return true;
    }

    private IRecipeCapabilityHolder virtual(GenericStack[] sparseInput) {
        return new IRecipeCapabilityHolder() {

            @Override
            public @NotNull Map<IO, List<RecipeHandlerList>> getCapabilitiesProxy() {
                return Map.of(IO.IN, List.of(new VirtualList(MEWildcardPatternBufferPartMachine.this, sparseInput)));
            }

            @Override
            public @NotNull Map<IO, Map<RecipeCapability<?>, List<IRecipeHandler<?>>>> getCapabilitiesFlat() {
                return Map.of();
            }
        };
    }

    private static class VirtualList extends RecipeHandlerList {

        private final GenericStack[] sparseInput;

        private VirtualList(MEWildcardPatternBufferPartMachine buffer, GenericStack[] sparseInput) {
            super(IO.IN, null);
            this.sparseInput = sparseInput;
            var slot = buffer.getInternalInventory()[0];
            addHandlers(slot.circuitInventory, slot.shareInventory, slot.shareTank, buffer.circuitInventorySimulated, buffer.shareInventory, buffer.shareTank);
        }

        @Override
        public IntLongMap getIngredientMap(@NotNull GTRecipeType type) {
            var ings = super.getIngredientMap(type);
            for (var stack : sparseInput) {
                var key = stack.what();
                if (key instanceof AEItemKey what && what.getItem() == CustomItems.VIRTUAL_ITEM_PROVIDER.get() && what.getTag() != null && what.getTag().tags.containsKey("n")) {
                    ItemStack virtualItem = VirtualItemProviderBehavior.getVirtualItem(what.getReadOnlyStack());
                    if (virtualItem.isEmpty()) continue;
                    key = AEItemKey.of(virtualItem);
                }
                ((IIngredientConvertible) key).gtolib$convert(Integer.MAX_VALUE, ings);
            }
            return ings;
        }
    }

    // ========== UI Widget ==========

    private static final int left = 22;
    private static final int top = 180;
    private static final int rowSize = 3;
    private static final int colSize = 6;
    private static final int width = 18 * rowSize + 8;
    private static final int height = width - 20;

    @Override
    public void buildToolBoxContent(@NotNull VBoxBuilder $this$buildToolBoxContent) {
        $this$buildToolBoxContent.hBox(14, (s) -> {
            s.setPaddingBottom(4);
            return null;
        }, true, (b) -> {
            b.widget(new LabelWidget(0, 0,
                    () -> Component.translatable(LANG_WILDCARD_PATTERN_BUFFER_LOADED_PATTERNS, scannedPatterns).getString()));
            return null;
        });
        super.buildToolBoxContent($this$buildToolBoxContent);
    }

    @Override
    public @NotNull Widget createUIWidget() {
        var widget = new WidgetGroup(0, 0, 196, 220);

        var dsl = super.createUIWidget();
        widget.addWidget(dsl);

        widget.addWidget(createLabeledConfiguratorWidget(0, 120,
                this::getPatternPriority, this::setPatternPriority,
                LANG_WILDCARD_PATTERN_BUFFER_PRIORITY,
                LANG_WILDCARD_PATTERN_BUFFER_PRIORITY_DESC));

        widget.addWidget(createLabeledConfiguratorWidget(64, 120,
                this::getMaxFluidsOutput, this::setMaxFluidsOutput,
                LANG_WILDCARD_PATTERN_BUFFER_MAX_FLUID_OUTPUT_TYPES,
                LANG_WILDCARD_PATTERN_BUFFER_MAX_FLUID_OUTPUT_TYPES_DESC,
                LANG_WILDCARD_PATTERN_BUFFER_MAX_FLUID_OUTPUT_TYPES_EXAMPLE));

        widget.addWidget(createLabeledConfiguratorWidget(128, 120,
                this::getMaxItemsOutput, this::setMaxItemsOutput,
                LANG_WILDCARD_PATTERN_BUFFER_MAX_ITEM_OUTPUT_TYPES,
                LANG_WILDCARD_PATTERN_BUFFER_MAX_ITEM_OUTPUT_TYPES_DESC,
                LANG_WILDCARD_PATTERN_BUFFER_MAX_ITEM_OUTPUT_TYPES_EXAMPLE));

        WidgetGroup AlignContainer = new WidgetGroup(0, 160, 178, 20);
        Widget labelWidget1 = new LabelWidget(64, 152, LANG_WILDCARD_PATTERN_BUFFER_BLACKLIST)
                .setAlign(Align.CENTER)
                .setHoverTooltips(Component.translatable(LANG_WILDCARD_PATTERN_BUFFER_BLACKLIST_DESC));
        AlignContainer.addWidget(labelWidget1);
        widget.addWidget(AlignContainer);
        widget.addWidget(createFluidBlacklistWidget());
        widget.addWidget(createItemBlacklistWidget());

        return widget;
    }

    private Widget createItemBlacklistWidget() {
        var container = new WidgetGroup(left, top, width, height);
        var innner = new DraggableScrollableWidgetGroup(4, 4, width - 8, height - 8);
        int index = 0;
        for (int y = 0; y < colSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                int finalIndex = index++;
                innner.addWidget(
                        new PhantomSlotWidget(blacklistedItemsStorageTransfer, finalIndex, x * 18, y * 18) {

                            @Override
                            public ItemStack slotClickPhantom(Slot slot, int mouseButton, ClickType clickTypeIn, ItemStack stackHeld) {
                                ItemStack stack = ItemStack.EMPTY;
                                ItemStack stackSlot = slot.getItem();
                                if (!stackSlot.isEmpty()) {
                                    stack = stackSlot.copy();
                                }

                                Material materialSlot = ChemicalHelper.getMaterialStack(stackSlot).material();
                                Material materialHeld = ChemicalHelper.getMaterialStack(stackHeld).material();

                                if (materialHeld == GTMaterials.NULL || mouseButton == 2 || mouseButton == 1) {
                                    // held is empty,right click,middle click
                                    // -> clear slot
                                    fillPhantomSlot(slot, ItemStack.EMPTY);
                                    blacklistedItems.setStackInSlot(finalIndex, ItemStack.EMPTY);
                                    loadBlacklistData();
                                } else if (materialSlot == GTMaterials.NULL) {   // slot is empty
                                    if (!blacklistedMaterials.containsValue(materialHeld)) {
                                        // held is not empty and item not in other slot
                                        // -> add to slot
                                        fillPhantomSlot(slot, stackHeld);
                                        var itemStack = stackHeld.copy();
                                        itemStack.setCount(Integer.MAX_VALUE);
                                        blacklistedItems.setStackInSlot(finalIndex, itemStack);
                                        loadBlacklistData();
                                    }
                                } else {
                                    if (materialSlot != materialHeld) {
                                        // slot item not equal to held item
                                        if (!blacklistedMaterials.containsValue(materialHeld)) {
                                            // item not in other slot
                                            // -> change the slot
                                            fillPhantomSlot(slot, stackHeld);
                                            var itemStack = stackHeld.copy();
                                            itemStack.setCount(Integer.MAX_VALUE);
                                            blacklistedItems.setStackInSlot(finalIndex, itemStack);
                                            loadBlacklistData();
                                        }
                                    }
                                }
                                return stack;
                            }

                            @Override
                            public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
                                super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
                                Position position = getPosition();
                                GuiTextures.SLOT.draw(graphics, mouseX, mouseY, position.x, position.y, 18, 18);
                                GuiTextures.CONFIG_ARROW_DARK.draw(graphics, mouseX, mouseY, position.x, position.y, 18, 18);
                                int stackX = position.x + 1;
                                int stackY = position.y + 1;
                                ItemStack stack;
                                if (getHandler() != null) {
                                    stack = getHandler().getItem();
                                    drawItemStack(graphics, stack, stackX, stackY, 0xFFFFFFFF, null);
                                }
                                if (mouseOverStock(mouseX, mouseY)) {
                                    drawSelectionOverlay(graphics, stackX, stackY + 18, 16, 16);
                                }
                            }

                            private void fillPhantomSlot(Slot slot, ItemStack stackHeld) {
                                if (stackHeld.isEmpty()) {
                                    slot.set(ItemStack.EMPTY);
                                } else {
                                    ItemStack phantomStack = stackHeld.copy();
                                    phantomStack.setCount(1);
                                    slot.set(phantomStack);
                                }
                            }

                            public boolean areItemsEqual(ItemStack itemStack1, ItemStack itemStack2) {
                                return ItemStack.matches(itemStack1, itemStack2);
                            }

                            private boolean mouseOverStock(double mouseX, double mouseY) {
                                Position position = getPosition();
                                return isMouseOver(position.x, position.y + 18, 18, 18, mouseX, mouseY);
                            }

                            @Override
                            public List<Component> getFullTooltipTexts() {
                                var superText = super.getFullTooltipTexts();
                                if (this.slotReference != null) {
                                    var mat = ChemicalHelper.getMaterialStack(this.slotReference.getItem()).material();
                                    if (mat != GTMaterials.NULL) {
                                        superText.addFirst(Component.translatable("metaitem.tool.tooltip.primary_material", mat.getLocalizedName()));
                                    }
                                }
                                return superText;
                            }
                        }
                                .setClearSlotOnRightClick(false)
                                .setChangeListener(this::onChanged));
            }
        }
        container.addWidget(innner);
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return container;
    }

    private Widget createFluidBlacklistWidget() {
        var container = new WidgetGroup(width + 16 + left, top, width, height);
        var inner = new DraggableScrollableWidgetGroup(4, 4, width - 8, height - 8);
        int index = 0;
        int shift = blacklistedItems.getSlots();
        for (int y = 0; y < colSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                int fluidIndex = index++;
                inner.addWidget(new PhantomFluidWidget(
                        this.blacklistedFluids[fluidIndex], fluidIndex,
                        x * 18, y * 18, 18, 18,
                        () -> this.blacklistedFluids[fluidIndex].getFluid(),
                        (fluid -> {
                            int shiftedIndex = fluidIndex + shift;
                            if (fluid.isEmpty()) {
                                this.blacklistedFluids[fluidIndex].setFluid(fluid);
                                if (!blacklistedMaterials.isEmpty() && blacklistedMaterials.containsKey(shiftedIndex)) {
                                    blacklistedMaterials.remove(shiftedIndex);
                                }
                                loadBlacklistData();
                                return;
                            }
                            Material fluidMaterial = ChemicalHelper.getMaterial(fluid.getFluid());
                            for (var entry : blacklistedMaterials.int2ReferenceEntrySet()) {
                                int i = entry.getIntKey() - shift;
                                Material f = entry.getValue();
                                if (i != fluidIndex && f == fluidMaterial) {
                                    return;
                                } else if (i == fluidIndex && f != fluidMaterial) {
                                    setFluid(fluidIndex, fluid);
                                    return;
                                }
                            }
                            setFluid(fluidIndex, fluid);
                        })) {

                    @Override
                    public List<Component> getFullTooltipTexts() {
                        var superTexts = super.getFullTooltipTexts();
                        var mat = ChemicalHelper.getMaterial(getFluid().getFluid());
                        if (mat != GTMaterials.NULL) {
                            superTexts.addFirst(Component.translatable("metaitem.tool.tooltip.primary_material", mat.getLocalizedName()));
                        }
                        return superTexts;
                    }
                }.setShowAmount(false).setBackground(GuiTextures.FLUID_SLOT));
            }
        }
        container.addWidget(inner);
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return container;
    }

    private void setFluid(int index, FluidStack fs) {
        var newFluid = fs.copy();
        newFluid.setAmount(1);
        this.blacklistedFluids[index].setFluid(newFluid);
        loadBlacklistData();
    }

    private Widget createLabeledConfiguratorWidget(int x, int y,
                                                   Supplier<Integer> getter, Consumer<Integer> setter,
                                                   String labelLangKey, String... descLangKey) {
        WidgetGroup priorityGroup = new WidgetGroup(x, y, 60, 40);

        Widget labelWidget = new ImageWidget(
                0, 0, 60, 12,
                new TextTexture(Component.translatable(labelLangKey).getString())
                        .setType(TextTexture.TextType.LEFT_HIDE)
                        .setWidth(65))
                .setHoverTooltips(Arrays.stream(descLangKey).map(Component::translatable).toArray(Component[]::new));
        priorityGroup.addWidget(labelWidget);

        final var priority = getter.get();
        Widget priorityWidget = new IntInputWidget(0, 14, 60, 12, getter, setter)
                .setMin(Integer.MIN_VALUE)
                .setValue(priority);
        priorityGroup.addWidget(priorityWidget);
        return priorityGroup;
    }

    // ========== Localization ==========

    @RegisterLanguage(cn = "样板优先级：", en = "Pattern Priority: ")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_PRIORITY = "gtocore.ae.appeng.pattern.priority";
    @RegisterLanguage(cn = "此样板总成提供的样板优先级。合成计算将优先考虑优先级最高的样板。", en = "The priority for the patterns offered by this provider. The crafting calculation will prioritize patterns with the highest priority.")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_PRIORITY_DESC = "gtocore.ae.appeng.pattern.priority.desc";
    @RegisterLanguage(cn = "通配符样板总成材料黑名单", en = "Wildcard Pattern Provider Material Blacklist")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_BLACKLIST = "gtocore.ae.appeng.wildcard_pattern_buffer.blacklist";
    @RegisterLanguage(cn = "添加到黑名单中的材料将不会被通配符样板总成所使用。", en = "Materials added to the blacklist will not be used by the Wildcard Pattern Provider.")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_BLACKLIST_DESC = "gtocore.ae.appeng.wildcard_pattern_buffer.blacklist.desc";
    @RegisterLanguage(cn = "最大物品输出种数：", en = "Max Item Output Types: ")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_MAX_ITEM_OUTPUT_TYPES = "gtocore.ae.appeng.wildcard_pattern_buffer.max_item_output_types";
    @RegisterLanguage(cn = "自动生成的样板中产物允许的最大物品种类数。", en = "The maximum number of item types allowed in the outputs of auto-generated patterns.")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_MAX_ITEM_OUTPUT_TYPES_DESC = "gtocore.ae.appeng.wildcard_pattern_buffer.max_item_output_types.desc";
    @RegisterLanguage(cn = "例如，生成的样板中，若配方输出既含有物品，又含有流体，将此项设为0，则仅允许流体作为样板的产物。", en = "For example, in generated patterns, if the recipe outputs both items and fluids, setting this to 0 will only allow fluids as outputs of the pattern.")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_MAX_ITEM_OUTPUT_TYPES_EXAMPLE = "gtocore.ae.appeng.wildcard_pattern_buffer.max_item_output_types.example";
    @RegisterLanguage(cn = "最大流体输出种数：", en = "Max Fluid Output Types: ")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_MAX_FLUID_OUTPUT_TYPES = "gtocore.ae.appeng.wildcard_pattern_buffer.max_fluid_output_types";
    @RegisterLanguage(cn = "自动生成的样板中产物允许的最大流体种类数。", en = "The maximum number of fluid types allowed in the outputs of auto-generated patterns.")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_MAX_FLUID_OUTPUT_TYPES_DESC = "gtocore.ae.appeng.wildcard_pattern_buffer.max_fluid_output_types.desc";
    @RegisterLanguage(cn = "例如，生成的样板中，若配方输出含有多种流体，将此项设为1，则仅允许配方中的第一种流体作为样板的产物。", en = "For example, in generated patterns, if the recipe outputs multiple fluids, setting this to 1 will only allow the first fluid in the recipe as the output of the pattern.")
    private static final String LANG_WILDCARD_PATTERN_BUFFER_MAX_FLUID_OUTPUT_TYPES_EXAMPLE = "gtocore.ae.appeng.wildcard_pattern_buffer.max_fluid_output_types.example";
    @RegisterLanguage(cn = "已扫描加载%s种通配符样板。", en = "Scanned and loaded %s wildcard patterns.")
    static final String LANG_WILDCARD_PATTERN_BUFFER_LOADED_PATTERNS = "gtocore.ae.appeng.wildcard_pattern_buffer.loaded_patterns";
}
