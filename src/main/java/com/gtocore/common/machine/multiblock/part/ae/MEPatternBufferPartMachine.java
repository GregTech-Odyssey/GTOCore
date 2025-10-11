package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.api.gui.configurators.MultiMachineModeFancyConfigurator;
import com.gtocore.common.data.machines.GTAEMachines;
import com.gtocore.common.machine.trait.InternalSlotRecipeHandler;

import com.gtolib.api.ae2.MyPatternDetailsHelper;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.capability.ISync;
import com.gtolib.api.machine.trait.NotifiableNotConsumableFluidHandler;
import com.gtolib.api.machine.trait.NotifiableNotConsumableItemHandler;
import com.gtolib.api.network.SyncManagedFieldHolder;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.utils.ExpandedO2LMap;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.ButtonConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyInvConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyTankConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.IntIngredientMap;
import com.gregtechceu.gtceu.api.transfer.item.LockableItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.utils.TaskHandler;
import com.gregtechceu.gtceu.utils.collection.OpenCacheHashSet;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.stacks.*;
import appeng.api.storage.MEStorage;
import appeng.api.storage.StorageHelper;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@DataGeneratorScanned
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEPatternBufferPartMachine extends MEPatternPartMachineKt<MEPatternBufferPartMachine.InternalSlot> implements IDataStickInteractable {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MEPatternBufferPartMachine.class, MEPatternPartMachineKt.Companion.getMANAGED_FIELD_HOLDER());
    private static final SyncManagedFieldHolder SYNC_MANAGED_FIELD_HOLDER = new SyncManagedFieldHolder(MEPatternBufferPartMachine.class,
            MEPatternPartMachineKt.getSYNC_MANAGED_FIELD_HOLDER());
    @RegisterLanguage(cn = "配方已缓存", en = "Recipe cached")
    private static final String CACHE = "gtocore.pattern_buffer.cache";
    @RegisterLanguage(cn = "样板独立配置", en = "Pattern independent configuration")
    private static final String INDEPENDENT = "gtocore.pattern_buffer.independent";
    @RegisterLanguage(cn = "总成共享配置", en = "Buffer share configuration")
    private static final String SHARE = "gtocore.pattern_buffer.share";

    @Persisted
    @DescSynced
    private final ArrayList<GTRecipeType> recipeTypes = new ArrayList<>();
    @Persisted
    @DescSynced
    private GTRecipeType recipeType = GTRecipeTypes.DUMMY_RECIPES;

    @DescSynced
    private final boolean[] caches;
    @Persisted
    public final NotifiableNotConsumableItemHandler shareInventory;
    @Persisted
    public final NotifiableNotConsumableFluidHandler shareTank;
    @Persisted
    public final NotifiableItemStackHandler circuitInventorySimulated;

    @Persisted
    private final Set<BlockPos> proxies = new OpenCacheHashSet<>();
    private final Set<MEPatternBufferProxyPartMachine> proxyMachines = new ReferenceOpenHashSet<>();
    public final InternalSlotRecipeHandler internalRecipeHandler;

    protected IntSyncedField configuratorField = ISync.createIntField(this)
            .set(-1)
            .setSenderListener((side, o, n) -> {
                if (side.isServer()) Objects.requireNonNull(Objects.requireNonNull(getLevel()).getServer()).tell(new TickTask(10, () -> freshWidgetGroup.fresh()));
                freshWidgetGroup.fresh();
            }).setReceiverListener((side, o, n) -> {
                if (side.isServer()) Objects.requireNonNull(Objects.requireNonNull(getLevel()).getServer()).tell(new TickTask(10, () -> freshWidgetGroup.fresh()));
                freshWidgetGroup.fresh();
            });

    @Override
    public @NotNull SyncManagedFieldHolder getSyncHolder() {
        return SYNC_MANAGED_FIELD_HOLDER;
    }

    protected ConfiguratorPanel configuratorPanel;

    public MEPatternBufferPartMachine(MetaMachineBlockEntity holder, int maxPatternCount) {
        super(holder, maxPatternCount);
        this.caches = new boolean[maxPatternCount];
        this.shareInventory = createShareInventory();
        this.shareTank = new NotifiableNotConsumableFluidHandler(this, 9, 64000);
        this.circuitInventorySimulated = CircuitHandler.create(this);
        this.internalRecipeHandler = new InternalSlotRecipeHandler(this, getInternalInventory());
    }

    NotifiableNotConsumableItemHandler createShareInventory() {
        var h = new NotifiableNotConsumableItemHandler(this, 9, IO.NONE);
        h.setFilter(stack -> !(stack.getItem() instanceof EncodedPatternItem));
        return h;
    }

    @Override
    public InternalSlot[] createInternalSlotArray() {
        return new InternalSlot[getMaxPatternCount()];
    }

    @Override
    public boolean patternFilter(ItemStack stack) {
        return stack.getItem() instanceof ProcessingPatternItem;
    }

    @Override
    public InternalSlot createInternalSlot(int i) {
        return new InternalSlot(this, i);
    }

    @Override
    public List<RecipeHandlerList> getRecipeHandlers() {
        return internalRecipeHandler.getSlotHandlers();
    }

    void addProxy(MEPatternBufferProxyPartMachine proxy) {
        proxies.add(proxy.getPos());
        proxyMachines.add(proxy);
    }

    void removeProxy(MEPatternBufferProxyPartMachine proxy) {
        proxies.remove(proxy.getPos());
        proxyMachines.remove(proxy);
    }

    public Set<MEPatternBufferProxyPartMachine> getProxies() {
        if (proxyMachines.size() != proxies.size() && getLevel() != null) {
            proxyMachines.clear();
            for (var pos : proxies) {
                if (MetaMachine.getMachine(getLevel(), pos) instanceof MEPatternBufferProxyPartMachine proxy) {
                    proxy.setBuffer(getPos());
                }
            }
        }
        return proxyMachines;
    }

    private void refundAll(ClickData clickData) {
        if (!clickData.isRemote) {
            for (InternalSlot internalSlot : getInternalInventory()) {
                internalSlot.refund();
            }
        }
    }

    private void changeMode(@Nullable GTRecipeType type) {
        this.recipeType = type == null ? GTRecipeTypes.DUMMY_RECIPES : type;
        for (var i : getInternalInventory()) {
            i.rhl.external.recipeType = type;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        var type = recipeType == GTRecipeTypes.DUMMY_RECIPES ? null : recipeType;
        for (var i : getInternalInventory()) {
            i.rhl.external.recipeType = type;
        }
    }

    @Override
    public void onPaintingColorChanged(int color) {
        super.onPaintingColorChanged(color);
        if (getLevel() instanceof ServerLevel serverLevel) {
            TaskHandler.enqueueServerTask(serverLevel, () -> {
                var type = recipeType == GTRecipeTypes.DUMMY_RECIPES ? null : recipeType;
                for (var i : getInternalInventory()) {
                    i.rhl.external.recipeType = type;
                }
            }, 1);
        }
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        super.attachSideTabs(sideTabs);
        sideTabs.attachSubTab(new MultiMachineModeFancyConfigurator(recipeTypes, recipeType, this::changeMode));
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
        this.recipeTypes.clear();
        this.recipeTypes.addAll(MultiMachineModeFancyConfigurator.extractRecipeTypes(this.getControllers()));
    }

    @Override
    public void removedFromController(@NotNull IMultiController controller) {
        super.removedFromController(controller);
        this.recipeTypes.clear();
        this.recipeTypes.addAll(MultiMachineModeFancyConfigurator.extractRecipeTypes(this.getControllers()));
    }

    @Override
    public void onPatternChange(int index) {
        getInternalInventory()[index].setLock(false);
        super.onPatternChange(index);
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        var slot = getDetailsSlotMap().get(patternDetails);
        if (slot != null) {
            return slot.pushPattern(patternDetails, inputHolder);
        }
        return false;
    }

    @Override
    public @Nullable IPatternDetails decodePattern(ItemStack stack, int index) {
        var pattern = super.decodePattern(stack, index);
        if (pattern == null) return null;
        if (!caches[index] && stack.getOrCreateTag().tags.get("recipe") instanceof StringTag stringTag) {
            var recipe = RecipeBuilder.RECIPE_MAP.get(RLUtils.parse(stringTag.getAsString()));
            getInternalInventory()[index].setRecipe(recipe);
        }
        return pattern;
    }

    @Override
    public IPatternDetails convertPattern(IPatternDetails pattern, int index) {
        if (pattern instanceof AEProcessingPattern processingPattern) {
            var sparseInput = processingPattern.getSparseInputs();
            var input = new ObjectArrayList<GenericStack>(sparseInput.length);
            var in = 0;
            var slot = getInternalInventory()[index];
            for (var stack : sparseInput) {
                if (stack != null && stack.what() instanceof AEItemKey what && what.getItem() == CustomItems.VIRTUAL_ITEM_PROVIDER.get() && what.getTag() != null && what.getTag().tags.containsKey("n")) {
                    ItemStack virtualItem = VirtualItemProviderBehavior.getVirtualItem(what.getReadOnlyStack());
                    if (virtualItem.isEmpty()) continue;
                    if (GTItems.PROGRAMMED_CIRCUIT.isIn(virtualItem)) {
                        slot.circuitInventory.storage.setStackInSlot(0, virtualItem);
                    } else {
                        var grid = getGrid();
                        if (grid != null && grid.getStorageService().getInventory().extract(what, 1, Actionable.SIMULATE, getActionSource()) == 1) {
                            slot.setLock(true);
                            var storage = slot.shareInventory.storage;
                            var inSlot = storage.getStackInSlot(in);
                            if (!inSlot.isEmpty()) {
                                storage.setStackInSlot(in, ItemStack.EMPTY);
                                grid.getStorageService().getInventory().insert(AEItemKey.of(inSlot), inSlot.getCount(), Actionable.MODULATE, getActionSource());
                            }
                            storage.setStackInSlot(in, virtualItem);
                            in++;
                        }
                    }
                    continue;
                }
                input.add(stack);
            }
            if (input.size() < sparseInput.length) {
                var stack = PatternDetailsHelper.encodeProcessingPattern(input.toArray(new GenericStack[0]), processingPattern.getSparseOutputs());
                return MyPatternDetailsHelper.CACHE.get(AEItemKey.of(stack));
            }
        }
        return pattern;
    }

    @Override
    @Nullable
    public Component appendHoverTooltips(int index) {
        if (caches[index]) {
            return Component.translatable(CACHE);
        }
        return null;
    }

    @Override
    public void onMouseClicked(int index) {
        if (!isRemote()) return;
        if (configuratorField.get() == index) {
            configuratorField.setAndSyncToServer(-1);
        } else {
            configuratorField.setAndSyncToServer(index);
        }
    }

    @Override
    public void addWidget(WidgetGroup group) {
        group.addWidget(new LabelWidget(81, 2, () -> configuratorField.get() < 0 ? SHARE : INDEPENDENT).setHoverTooltips(Component.translatable("monitor.gui.title.slot").append(String.valueOf(configuratorField.get()))));
    }

    @Override
    public PatternContainerGroup getTerminalGroup() {
        if (isFormed()) {
            IMultiController controller = getControllers().first();
            MultiblockMachineDefinition controllerDefinition = controller.self().getDefinition();
            GTRecipeType rt = controller instanceof IRecipeLogicMachine rlm ? rlm.getRecipeType() : null;
            String lid = rt != null ? rt.registryName.toLanguageKey() : controllerDefinition.getDescriptionId();

            if (!getCustomName().isEmpty()) {
                return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), Component.literal(getCustomName()), Collections.emptyList());
            } else {
                ItemStack circuitStack = circuitInventorySimulated.storage.getStackInSlot(0);
                int circuitConfiguration = circuitStack.isEmpty() ? -1 : IntCircuitBehaviour.getCircuitConfiguration(circuitStack);
                Component groupName = circuitConfiguration != -1 ? Component.translatable(lid).append(" - " + circuitConfiguration) : Component.translatable(lid);
                return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), groupName, Collections.emptyList());
            }
        } else {
            if (!getCustomName().isEmpty()) {
                return new PatternContainerGroup(AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.asItem()), Component.literal(getCustomName()), Collections.emptyList());
            } else {
                return new PatternContainerGroup(AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.asItem()), GTAEMachines.ME_PATTERN_BUFFER.get().getDefinition().asItem().getDescription(), Collections.emptyList());
            }
        }
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        this.configuratorPanel = configuratorPanel;
        configuratorPanel.attachConfigurators(new ButtonConfigurator(new GuiTextureGroup(GuiTextures.BUTTON, GuiTextures.REFUND_OVERLAY), this::refundAll).setTooltips(List.of(Component.translatable("gui.gtceu.refund_all.desc"))));
        configuratorPanel.attachConfigurators(new CircuitFancyConfigurator(circuitInventorySimulated.storage));
        configuratorPanel.attachConfigurators(new FancyInvConfigurator(shareInventory.storage, Component.translatable("gui.gtceu.share_inventory.title")).setTooltips(List.of(Component.translatable("gui.gtceu.share_inventory.desc.0"), Component.translatable("gui.gtceu.share_inventory.desc.1"))));
        configuratorPanel.attachConfigurators(new FancyTankConfigurator(shareTank.getStorages(), Component.translatable("gui.gtceu.share_tank.title")).setTooltips(List.of(Component.translatable("gui.gtceu.share_tank.desc.0"), Component.translatable("gui.gtceu.share_inventory.desc.1"))));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void saveToItem(CompoundTag tag) {
        super.saveToItem(tag);
        tag.put("si", shareInventory.storage.serializeNBT());
        ListTag tanks = new ListTag();
        for (var tank : shareTank.getStorages()) {
            tanks.add(tank.serializeNBT());
        }
        tag.put("st", tanks);
        tag.put("ci", circuitInventorySimulated.storage.serializeNBT());
    }

    @Override
    public void loadFromItem(CompoundTag tag) {
        super.loadFromItem(tag);
        shareInventory.storage.deserializeNBT(tag.getCompound("si"));
        ListTag tanks = tag.getList("st", Tag.TAG_COMPOUND);
        for (int i = 0; i < tanks.size(); i++) {
            shareTank.getStorages()[i].deserializeNBT(tanks.getCompound(i));
        }
        circuitInventorySimulated.storage.deserializeNBT(tag.getCompound("ci"));
    }

    @Override
    public InteractionResult onDataStickShiftUse(Player player, ItemStack dataStick) {
        dataStick.getOrCreateTag().putIntArray("pos", new int[] { getPos().getX(), getPos().getY(), getPos().getZ() });
        return InteractionResult.SUCCESS;
    }

    public record BufferData(Object2LongMap<AEItemKey> items, Object2LongMap<AEFluidKey> fluids) {}

    public BufferData mergeInternalSlots() {
        var items = new ExpandedO2LMap<AEItemKey>();
        var fluids = new ExpandedO2LMap<AEFluidKey>();
        for (InternalSlot slot : getInternalInventory()) {
            slot.itemInventory.object2LongEntrySet().fastForEach(e -> items.addTo(e.getKey(), e.getLongValue()));
            slot.fluidInventory.object2LongEntrySet().fastForEach(e -> fluids.addTo(e.getKey(), e.getLongValue()));
        }
        return new BufferData(items, fluids);
    }

    public static final class InternalSlot extends AbstractInternalSlot {

        public InternalSlotRecipeHandler.AbstractRHL rhl;
        public Recipe recipe;
        private final MEPatternBufferPartMachine machine;
        public final int index;
        private final InputSink inputSink;
        private Runnable onContentsChanged = () -> {};
        public boolean itemChanged = true;
        public boolean fluidChanged = true;
        public final IntIngredientMap itemIngredientMap = new IntIngredientMap();
        public final IntIngredientMap fluidIngredientMap = new IntIngredientMap();
        public final Object2LongOpenHashMap<AEItemKey> itemInventory = new ExpandedO2LMap<>();
        public final Object2LongOpenHashMap<AEFluidKey> fluidInventory = new ExpandedO2LMap<>();

        public final NotifiableNotConsumableItemHandler shareInventory;
        public final NotifiableNotConsumableFluidHandler shareTank;
        public final NotifiableItemStackHandler circuitInventory;
        final LockableItemStackHandler lockableInventory;
        private boolean lock;

        private InternalSlot(MEPatternBufferPartMachine machine, int index) {
            this.machine = machine;
            this.index = index;
            this.shareInventory = machine.createShareInventory();
            this.shareTank = new NotifiableNotConsumableFluidHandler(machine, 9, 64000);
            this.circuitInventory = CircuitHandler.create(machine);
            this.inputSink = new InputSink(this);
            this.lockableInventory = new LockableItemStackHandler(shareInventory.storage);
        }

        public void setLock(boolean lock) {
            if (this.lock) {
                circuitInventory.storage.setStackInSlot(0, ItemStack.EMPTY);
                for (int i = 0; i < 9; i++) {
                    shareInventory.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
            this.lock = lock;
            lockableInventory.setLock(lock);
        }

        public void setRecipe(@Nullable Recipe recipe) {
            this.recipe = recipe;
            machine.caches[index] = recipe != null;
        }

        public boolean isEmpty() {
            return itemInventory.isEmpty() && fluidInventory.isEmpty();
        }

        private void refund() {
            var network = machine.getMainNode().getGrid();
            if (network != null) {
                MEStorage networkInv = network.getStorageService().getInventory();
                var energy = network.getEnergyService();
                for (var it = itemInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                    var entry = it.next();

                    var count = entry.getLongValue();
                    if (count == 0) {
                        it.remove();
                        continue;
                    }
                    var key = entry.getKey();
                    if (key == null) continue;
                    long inserted = StorageHelper.poweredInsert(energy, networkInv, key, count, machine.getActionSourceField());
                    if (inserted > 0) {
                        count -= inserted;
                        if (count == 0) it.remove();
                        else entry.setValue(count);
                    }
                }
                for (var it = fluidInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                    var entry = it.next();
                    var amount = entry.getLongValue();
                    if (amount == 0) {
                        it.remove();
                        continue;
                    }
                    var key = entry.getKey();
                    if (key == null) continue;
                    long inserted = StorageHelper.poweredInsert(energy, networkInv, key, amount, machine.getActionSourceField());
                    if (inserted > 0) {
                        amount -= inserted;
                        if (amount == 0) it.remove();
                        else entry.setValue(amount);
                    }
                }
                itemChanged = true;
                fluidChanged = true;
                onContentsChanged.run();
            }
        }

        @Override
        public void onPatternChange() {
            setRecipe(null);
            refund();
        }

        @Override
        public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
            patternDetails.pushInputsToExternalInventory(inputHolder, inputSink);
            itemChanged = true;
            fluidChanged = true;
            onContentsChanged.run();
            return true;
        }

        @Nullable
        public List<Ingredient> handleItemInternal(List<Ingredient> left, boolean simulate) {
            boolean changed = false;
            for (var it = left.listIterator(0); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                long amount;
                if (ingredient instanceof FastSizedIngredient si) amount = si.getAmount();
                else amount = 1;
                for (var it2 = itemInventory.object2LongEntrySet().fastIterator(); it2.hasNext();) {
                    var entry = it2.next();
                    if (!ingredient.test(entry.getKey().getReadOnlyStack())) continue;
                    var count = entry.getLongValue();
                    long extracted = Math.min(count, amount);
                    if (!simulate && extracted > 0) {
                        changed = true;
                        count -= extracted;
                        if (count < 1) it2.remove();
                        else entry.setValue(count);
                    }
                    amount -= extracted;
                    if (amount < 1) {
                        it.remove();
                        break;
                    }
                }
            }
            if (changed) {
                itemChanged = true;
                fluidChanged = true;
                onContentsChanged.run();
            }
            return left.isEmpty() ? null : left;
        }

        @Nullable
        public List<FluidIngredient> handleFluidInternal(List<FluidIngredient> left, boolean simulate) {
            boolean changed = false;
            for (var it = left.listIterator(0); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                long amount = FastFluidIngredient.getAmount(ingredient);
                for (var it2 = fluidInventory.object2LongEntrySet().fastIterator(); it2.hasNext();) {
                    var entry = it2.next();
                    if (!FastFluidIngredient.testAeKay(ingredient, entry.getKey())) continue;
                    var count = entry.getLongValue();
                    long extracted = Math.min(count, amount);
                    if (!simulate && extracted > 0) {
                        changed = true;
                        count -= extracted;
                        if (count < 1) it2.remove();
                        else entry.setValue(count);
                    }
                    amount -= extracted;
                    if (amount < 1) {
                        it.remove();
                        break;
                    }
                }
            }
            if (changed) {
                itemChanged = true;
                fluidChanged = true;
                onContentsChanged.run();
            }
            return left.isEmpty() ? null : left;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = super.serializeNBT();
            if (recipe != null) {
                tag.put("recipe", recipe.serializeNBT());
            }
            ListTag itemsTag = new ListTag();
            for (var it = itemInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                var entry = it.next();
                var ct = entry.getKey().toTag();
                ct.putLong("real", entry.getLongValue());
                itemsTag.add(ct);
            }
            if (!itemsTag.isEmpty()) tag.put("inventory", itemsTag);
            ListTag fluidsTag = new ListTag();
            for (var it = fluidInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                var entry = it.next();
                var ct = entry.getKey().toTag();
                ct.putLong("real", entry.getLongValue());
                fluidsTag.add(ct);
            }
            if (!fluidsTag.isEmpty()) tag.put("fluidInventory", fluidsTag);
            if (!lock && !shareInventory.isEmpty()) tag.put("inv", shareInventory.storage.serializeNBT());
            if (!shareTank.isEmpty()) {
                ListTag tanks = new ListTag();
                for (var tank : shareTank.getStorages()) {
                    if (tank.isEmpty()) {
                        tanks.add(new CompoundTag());
                    } else tanks.add(tank.serializeNBT());
                }
                tag.put("tank", tanks);

            }
            if (!lock) {
                var c = IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.storage.getStackInSlot(0));
                if (c > 0) tag.putInt("c", c);
            }
            tag.putBoolean("l", lock);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            setRecipe(Recipe.deserializeNBT(tag.get("recipe")));
            ListTag items = tag.getList("inventory", Tag.TAG_COMPOUND);
            for (Tag t : items) {
                if (!(t instanceof CompoundTag ct)) continue;
                var stack = AEItemKey.fromTag(ct);
                if (stack == null) continue;
                var amount = ct.getLong("real");
                if (amount > 0) {
                    itemInventory.put(stack, amount);
                }
            }
            ListTag fluids = tag.getList("fluidInventory", Tag.TAG_COMPOUND);
            for (Tag t : fluids) {
                if (!(t instanceof CompoundTag ct)) continue;
                var stack = AEFluidKey.fromTag(ct);
                if (stack == null) continue;
                var amount = ct.getLong("real");
                if (amount > 0) {
                    fluidInventory.put(stack, amount);
                }
            }
            if (tag.tags.get("inv") instanceof CompoundTag inv) {
                shareInventory.storage.deserializeNBT(inv);
            }
            if (tag.tags.get("tank") instanceof ListTag tanks) {
                for (int i = 0; i < tanks.size(); i++) {
                    var t = tanks.getCompound(i);
                    if (t.isEmpty()) continue;
                    var tank = shareTank.getStorages()[i];
                    tank.deserializeNBT(t);
                }
            }
            var c = tag.getInt("c");
            if (c > 0) circuitInventory.storage.setStackInSlot(0, IntCircuitBehaviour.stack(c));
            setLock(tag.getBoolean("l"));
        }

        @Override
        public void setOnContentsChanged(final Runnable onContentsChanged) {
            this.onContentsChanged = onContentsChanged;
        }

        @Override
        public Runnable getOnContentsChanged() {
            return this.onContentsChanged;
        }
    }

    private record InputSink(InternalSlot slot) implements IPatternDetails.PatternInputSink {

        @Override
        public void pushInput(AEKey key, long amount) {
            if (amount < 1) return;
            if (key instanceof AEItemKey itemKey) {
                slot.itemInventory.addTo(itemKey, amount);
            } else if (key instanceof AEFluidKey fluidKey) {
                slot.fluidInventory.addTo(fluidKey, amount);
            }
        }
    }
}
