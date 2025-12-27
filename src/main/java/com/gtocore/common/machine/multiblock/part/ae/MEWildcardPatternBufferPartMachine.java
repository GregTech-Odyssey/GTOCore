package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.MyPatternDetailsHelper;
import com.gtolib.api.ae2.pattern.IParallelPatternDetails;
import com.gtolib.api.ae2.stacks.IIngredientConvertible;
import com.gtolib.api.ae2.stacks.TagPrefixKey;
import com.gtolib.api.machine.trait.ExtendedRecipeHandlerList;

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
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.ProcessingPatternItem;
import com.fast.recipesearch.IntLongMap;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.layout.Align;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget.drawSelectionOverlay;
import static com.lowdragmc.lowdraglib.gui.util.DrawerHelper.drawItemStack;

public class MEWildcardPatternBufferPartMachine extends MEPatternBufferPartMachineKt {

    private List<IPatternDetails> cachedPatterns;
    private boolean dirty = true;
    private boolean lock = false;
    @Getter
    @Setter
    @Persisted
    private int patternPriority = 0;
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
        getAvailablePatterns();
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

    @Override
    public @NotNull Widget createUIWidget() {
        var widget = new WidgetGroup(0, 0, 178, 220);

        var dsl = super.createUIWidget();
        widget.addWidget(dsl);

        WidgetGroup priorityGroup = new WidgetGroup(64, 120, 64, 40);

        Widget labelWidget = new LabelWidget(0, 0, "gtocore.ae.appeng.pattern.priority")
                .setHoverTooltips(Component.translatable("gtocore.ae.appeng.pattern.priority.desc"));
        priorityGroup.addWidget(labelWidget);

        final var priority = patternPriority;
        Widget priorityWidget = new IntInputWidget(0, 14, 60, 12, this::getPatternPriority, this::setPatternPriority)
                .setMin(Integer.MIN_VALUE).setValue(priority);
        priorityGroup.addWidget(priorityWidget);
        widget.addWidget(priorityGroup);

        WidgetGroup AlignContainer = new WidgetGroup(0, 160, 178, 20);
        Widget labelWidget1 = new LabelWidget(64, 152, "gtocore.ae.appeng.wildcard_pattern_buffer.blacklist")
                .setAlign(Align.CENTER)
                .setHoverTooltips(Component.translatable("gtocore.ae.appeng.wildcard_pattern_buffer.blacklist.desc"));
        AlignContainer.addWidget(labelWidget1);
        widget.addWidget(AlignContainer);
        widget.addWidget(createFluidBlacklistWidget());
        widget.addWidget(createItemBlacklistWidget());

        return widget;
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
    private void rebuildCacheIfNeeded() {
        if (dirty || cachedPatterns == null) {
            dirty = false;
            // profiler start
            long nanos = System.nanoTime();
            AtomicLong substitutingIngredients = new AtomicLong();
            AtomicLong validatingPatterns = new AtomicLong();

            var patterns = super.getAvailablePatterns();
            var newPatterns = new FastObjectArrayList<IPatternDetails>();

            var sharedInputs = new ObjectArrayList[patterns.size()];
            var tagPrefixInputs = new ObjectArrayList[patterns.size()];
            var sharedOutputs = new ObjectArrayList[patterns.size()];
            var tagPrefixOutputs = new ObjectArrayList[patterns.size()];

            long startSubstituting = System.nanoTime();
            for (int i = 0; i < patterns.size(); i++) {
                var p = patterns.get(i);
                if (p instanceof AEProcessingPattern processingPattern) {

                    var sparseInput = processingPattern.getSparseInputs();
                    var sharedInputList = new ObjectArrayList<GenericStack>(sparseInput.length);
                    var tagPrefixInputList = new ObjectArrayList<GenericStack>(sparseInput.length);
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
                    var sharedOutputList = new ObjectArrayList<GenericStack>(sparseOutput.length);
                    var tagPrefixOutputList = new ObjectArrayList<GenericStack>(sparseOutput.length);
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
                if (!blacklistSet.contains(material)) return;
                for (int i = 0; i < patterns.size(); i++) {
                    var cp = patterns.get(i);
                    if (cp instanceof AEProcessingPattern) {

                        ObjectArrayList<GenericStack> input = new ObjectArrayList<>(sharedInputs[i]);
                        var tagPrefixInput = tagPrefixInputs[i];
                        ObjectArrayList<GenericStack> output = new ObjectArrayList<>(sharedOutputs[i]);
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

                        var stack = PatternDetailsHelper.encodeProcessingPattern(input.toArray(new GenericStack[0]), output.toArray(new GenericStack[0]));
                        long startValidating1 = System.nanoTime();
                        var detail = MyPatternDetailsHelper.CACHE.get(AEItemKey.of(stack));
                        if (validatePattern(detail)) {
                            var converted = IParallelPatternDetails.of(convertPattern(detail, 0), getLevel(), 1);
                            newPatterns.add(converted);
                        }
                        validatingPatterns.addAndGet(System.nanoTime() - startValidating1);
                    }
                }
            });
            cachedPatterns = newPatterns;
            if (GTOConfig.INSTANCE.aeLog) {
                GTOCore.LOGGER.info("MEWildcardPatternBufferPartMachine recalculated patterns: {} patterns in {} ms",
                        cachedPatterns.size(), (System.nanoTime() - nanos) / 1_000_000.0);
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
            return patterns;
        }
        rebuildCacheIfNeeded();
        return cachedPatterns;
    }

    // ========== Pattern Validation ==========

    private boolean validatePattern(AEProcessingPattern pattern) {
        if (recipeType == GTORecipeTypes.HATCH_COMBINED) {
            return !getRecipeTypes().isEmpty() &&
                    getRecipeTypes().stream().anyMatch(rt -> {
                        AtomicBoolean valid = new AtomicBoolean(false);
                        rt.findRecipe(virtual(pattern), r -> {
                            valid.set(checkProb(r));
                            return valid.get();
                        });
                        return valid.get();
                    });
        } else {
            AtomicBoolean valid = new AtomicBoolean(false);
            recipeType.findRecipe(virtual(pattern), r -> {
                valid.set(checkProb(r));
                return valid.get();
            });
            return valid.get();
        }
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

    private IRecipeCapabilityHolder virtual(AEProcessingPattern pattern) {
        return new IRecipeCapabilityHolder() {

            @Override
            public @NotNull Map<IO, List<RecipeHandlerList>> getCapabilitiesProxy() {
                return Map.of(IO.IN, List.of(new VirtualList(MEWildcardPatternBufferPartMachine.this, pattern)));
            }

            @Override
            public @NotNull Map<IO, Map<RecipeCapability<?>, List<IRecipeHandler<?>>>> getCapabilitiesFlat() {
                return Map.of();
            }
        };
    }

    private static class VirtualList extends ExtendedRecipeHandlerList {

        private final AEProcessingPattern pattern;

        private VirtualList(MEWildcardPatternBufferPartMachine buffer, AEProcessingPattern pattern) {
            super(IO.IN, null);
            this.pattern = pattern;
            var slot = buffer.getInternalInventory()[0];
            addHandlers(slot.circuitInventory, slot.shareInventory, slot.shareTank, buffer.circuitInventorySimulated, buffer.shareInventory, buffer.shareTank);
        }

        @Override
        public IntLongMap getIngredientMap(@NotNull GTRecipeType type) {
            var ings = super.getIngredientMap(type);
            var sparseInput = pattern.getSparseInputs();
            for (var stack : sparseInput) {
                var key = stack.what();
                if (key instanceof AEItemKey what && what.getItem() == CustomItems.VIRTUAL_ITEM_PROVIDER.get() && what.getTag() != null && what.getTag().tags.containsKey("n")) {
                    ItemStack virtualItem = VirtualItemProviderBehavior.getVirtualItem(what.getReadOnlyStack());
                    if (virtualItem.isEmpty()) continue;
                    key = AEItemKey.of(virtualItem);
                }
                ((IIngredientConvertible) key).gtolib$convert(Long.MAX_VALUE, ings);
            }
            return ings;
        }

        @Override
        public boolean findRecipe(IRecipeCapabilityHolder holder, GTRecipeType recipeType, Predicate<GTRecipe> canHandle) {
            var ings = this.getIngredientMap(recipeType);
            if (ings.isEmpty()) return false;
            return recipeType.db.find(ings, canHandle);
        }
    }

    // ========== UI Widget ==========

    private static final int left = 22;
    private static final int top = 180;
    private static final int rowSize = 3;
    private static final int colSize = 6;
    private static final int width = 18 * rowSize + 8;
    private static final int height = width - 20;

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
}
