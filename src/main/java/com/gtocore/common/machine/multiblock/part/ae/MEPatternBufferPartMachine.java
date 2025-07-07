package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.data.machines.GTAEMachines;
import com.gtocore.common.machine.trait.InternalSlotRecipeHandler;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.feature.multiblock.IExtendedRecipeCapabilityHolder;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.machine.trait.NotifiableNotConsumableFluidHandler;
import com.gtolib.api.machine.trait.NotifiableNotConsumableItemHandler;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.utils.ItemUtils;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.ButtonConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyInvConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyTankConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import appeng.api.crafting.IPatternDetails;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.api.storage.StorageHelper;
import appeng.crafting.pattern.ProcessingPatternItem;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

@Scanned
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEPatternBufferPartMachine extends MEPatternPartMachine<MEPatternBufferPartMachine.InternalSlot> implements IDataStickInteractable {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEPatternBufferPartMachine.class, MEPatternPartMachine.MANAGED_FIELD_HOLDER);
    @RegisterLanguage(cn = "配方已缓存", en = "Recipe cached")
    private static final String CACHE = "gtocore.pattern_buffer.cache";
    @DescSynced
    private final boolean[] caches;
    @Persisted
    private final NotifiableNotConsumableItemHandler shareInventory;
    @Persisted
    private final NotifiableNotConsumableFluidHandler shareTank;
    @Persisted
    private final NotifiableNotConsumableItemHandler circuitInventorySimulated;
    @Persisted
    private final Set<BlockPos> proxies = new ObjectOpenHashSet<>();
    private final Set<MEPatternBufferProxyPartMachine> proxyMachines = new ReferenceOpenHashSet<>();
    private final InternalSlotRecipeHandler internalRecipeHandler;

    public MEPatternBufferPartMachine(IMachineBlockEntity holder, int maxPatternCount) {
        super(holder, maxPatternCount);
        this.caches = new boolean[maxPatternCount];
        this.shareInventory = createShareInventory();
        this.shareTank = new NotifiableNotConsumableFluidHandler(this, 9, 8 * FluidType.BUCKET_VOLUME);
        this.circuitInventorySimulated = createCircuitInventory();
        this.internalRecipeHandler = new InternalSlotRecipeHandler(this, getInternalInventory());
    }

    NotifiableNotConsumableItemHandler createShareInventory() {
        return new NotifiableNotConsumableItemHandler(this, 9, IO.NONE);
    }

    NotifiableNotConsumableItemHandler createCircuitInventory() {
        NotifiableNotConsumableItemHandler handle = new NotifiableNotConsumableItemHandler(this, 1, IO.NONE);
        handle.setFilter(IntCircuitBehaviour::isIntegratedCircuit);
        handle.shouldSearchContent(false);
        return handle;
    }

    @Override
    InternalSlot[] createInternalSlotArray() {
        return new InternalSlot[maxPatternCount];
    }

    @Override
    boolean patternFilter(ItemStack stack) {
        return stack.getItem() instanceof ProcessingPatternItem;
    }

    @Override
    InternalSlot createInternalSlot(int i) {
        return new InternalSlot(this, i);
    }

    public NotifiableItemStackHandler getCircuitInventory() {
        return circuitInventorySimulated;
    }

    @Override
    public List<RecipeHandlerList> getRecipeHandlers() {
        return internalRecipeHandler.getSlotHandlers();
    }

    public void addProxy(MEPatternBufferProxyPartMachine proxy) {
        proxies.add(proxy.getPos());
        proxyMachines.add(proxy);
    }

    public void removeProxy(MEPatternBufferProxyPartMachine proxy) {
        proxies.remove(proxy.getPos());
        proxyMachines.remove(proxy);
    }

    @UnmodifiableView
    public Set<MEPatternBufferProxyPartMachine> getProxies() {
        if (proxyMachines.size() != proxies.size() && getLevel() != null) {
            proxyMachines.clear();
            for (var pos : proxies) {
                if (MetaMachine.getMachine(getLevel(), pos) instanceof MEPatternBufferProxyPartMachine proxy) {
                    proxyMachines.add(proxy);
                }
            }
        }
        return Collections.unmodifiableSet(proxyMachines);
    }

    private void refundAll(ClickData clickData) {
        if (!clickData.isRemote) {
            for (InternalSlot internalSlot : getInternalInventory()) {
                internalSlot.refund();
            }
        }
    }

    @Override
    @Nullable
    Component appendHoverTooltips(int index) {
        if (caches[index]) {
            return Component.translatable(CACHE);
        }
        return null;
    }

    @Override
    public PatternContainerGroup getTerminalGroup() {
        if (isFormed()) {
            IMultiController controller = getControllers().first();
            MultiblockMachineDefinition controllerDefinition = controller.self().getDefinition();
            if (!customName.isEmpty()) {
                return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), Component.literal(customName), Collections.emptyList());
            } else {
                ItemStack circuitStack = circuitInventorySimulated.storage.getStackInSlot(0);
                int circuitConfiguration = circuitStack.isEmpty() ? -1 : IntCircuitBehaviour.getCircuitConfiguration(circuitStack);
                Component groupName = circuitConfiguration != -1 ? Component.translatable(controllerDefinition.getDescriptionId()).append(" - " + circuitConfiguration) : Component.translatable(controllerDefinition.getDescriptionId());
                return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), groupName, Collections.emptyList());
            }
        } else {
            if (!customName.isEmpty()) {
                return new PatternContainerGroup(AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.getItem()), Component.literal(customName), Collections.emptyList());
            } else {
                return new PatternContainerGroup(AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.getItem()), GTAEMachines.ME_PATTERN_BUFFER.get().getDefinition().getItem().getDescription(), Collections.emptyList());
            }
        }
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new ButtonConfigurator(new GuiTextureGroup(GuiTextures.BUTTON, GuiTextures.REFUND_OVERLAY), this::refundAll).setTooltips(List.of(Component.translatable("gui.gtceu.refund_all.desc"))));
        configuratorPanel.attachConfigurators(new CircuitFancyConfigurator(getCircuitInventory().storage));
        configuratorPanel.attachConfigurators(new FancyInvConfigurator(shareInventory.storage, Component.translatable("gui.gtceu.share_inventory.title")).setTooltips(List.of(Component.translatable("gui.gtceu.share_inventory.desc.0"), Component.translatable("gui.gtceu.share_inventory.desc.1"))));
        configuratorPanel.attachConfigurators(new FancyTankConfigurator(shareTank.getStorages(), Component.translatable("gui.gtceu.share_tank.title")).setTooltips(List.of(Component.translatable("gui.gtceu.share_tank.desc.0"), Component.translatable("gui.gtceu.share_inventory.desc.1"))));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onMachineRemoved() {
        super.onMachineRemoved();
        clearInventory(shareInventory);
    }

    @Override
    public InteractionResult onDataStickShiftUse(Player player, ItemStack dataStick) {
        dataStick.getOrCreateTag().putIntArray("pos", new int[] { getPos().getX(), getPos().getY(), getPos().getZ() });
        return InteractionResult.SUCCESS;
    }

    public record BufferData(Object2LongMap<ItemStack> items, Object2LongMap<FluidStack> fluids) {}

    public BufferData mergeInternalSlots() {
        var items = new Object2LongOpenCustomHashMap<>(ItemUtils.HASH_STRATEGY);
        var fluids = new Object2LongOpenHashMap<FluidStack>();
        for (InternalSlot slot : getInternalInventory()) {
            slot.itemInventory.object2LongEntrySet().fastForEach(e -> items.addTo(e.getKey(), e.getLongValue()));
            slot.fluidInventory.object2LongEntrySet().fastForEach(e -> fluids.addTo(e.getKey(), e.getLongValue()));
        }
        return new BufferData(items, fluids);
    }

    public static final class InternalSlot extends AbstractInternalSlot {

        public InternalSlotRecipeHandler.AbstractRHL rhl;
        private Recipe recipe;
        private final MEPatternBufferPartMachine machine;
        private final int index;
        final InputSink inputSink;
        private Runnable onContentsChanged = () -> {};
        public final Object2LongOpenCustomHashMap<ItemStack> itemInventory = new Object2LongOpenCustomHashMap<>(ItemUtils.HASH_STRATEGY);
        public final Object2LongOpenHashMap<FluidStack> fluidInventory = new Object2LongOpenHashMap<>();
        private List<ItemStack> itemStacks = null;
        private List<FluidStack> fluidStacks = null;

        private InternalSlot(MEPatternBufferPartMachine machine, int index) {
            this.machine = machine;
            this.index = index;
            this.inputSink = new InputSink(this);
        }

        public void setRecipe(@Nullable Recipe recipe) {
            this.recipe = recipe;
            machine.caches[index] = recipe != null;
        }

        public boolean isEmpty() {
            return itemInventory.isEmpty() && fluidInventory.isEmpty();
        }

        private void onContentsChanged() {
            itemStacks = null;
            fluidStacks = null;
            onContentsChanged.run();
        }

        public List<ItemStack> getItems() {
            if (itemStacks == null) {
                List<ItemStack> stacks = new ObjectArrayList<>(itemInventory.size());
                for (ObjectIterator<Object2LongMap.Entry<ItemStack>> it = itemInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                    Object2LongMap.Entry<ItemStack> e = it.next();
                    long count = e.getLongValue();
                    if (count < 1) it.remove();
                    e.getKey().setCount(MathUtil.saturatedCast(count));
                    stacks.add(e.getKey());
                }
                itemStacks = stacks;
            }
            return itemStacks;
        }

        public List<FluidStack> getFluids() {
            if (fluidStacks == null) {
                List<FluidStack> stacks = new ObjectArrayList<>(fluidInventory.size());
                for (ObjectIterator<Object2LongMap.Entry<FluidStack>> it = fluidInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                    Object2LongMap.Entry<FluidStack> e = it.next();
                    long count = e.getLongValue();
                    if (count < 1) it.remove();
                    e.getKey().setAmount(MathUtil.saturatedCast(count));
                    stacks.add(e.getKey());
                }
                fluidStacks = stacks;
            }
            return fluidStacks;
        }

        private void refund() {
            var network = machine.getMainNode().getGrid();
            if (network != null) {
                MEStorage networkInv = network.getStorageService().getInventory();
                var energy = network.getEnergyService();
                for (var it = itemInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                    var entry = it.next();
                    var stack = entry.getKey();
                    var count = entry.getLongValue();
                    if (stack.isEmpty() || count == 0) {
                        it.remove();
                        continue;
                    }
                    var key = AEItemKey.of(stack);
                    if (key == null) continue;
                    long inserted = StorageHelper.poweredInsert(energy, networkInv, key, count, machine.actionSource);
                    if (inserted > 0) {
                        count -= inserted;
                        if (count == 0) it.remove();
                        else entry.setValue(count);
                    }
                }
                for (var it = fluidInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                    var entry = it.next();
                    var stack = entry.getKey();
                    var amount = entry.getLongValue();
                    if (stack.isEmpty() || amount == 0) {
                        it.remove();
                        continue;
                    }
                    var key = AEFluidKey.of(stack);
                    if (key == null) continue;
                    long inserted = StorageHelper.poweredInsert(energy, networkInv, key, amount, machine.actionSource);
                    if (inserted > 0) {
                        amount -= inserted;
                        if (amount == 0) it.remove();
                        else entry.setValue(amount);
                    }
                }
                onContentsChanged();
            }
        }

        @Override
        void onPatternChange() {
            setRecipe(null);
            refund();
        }

        @Override
        boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
            patternDetails.pushInputsToExternalInventory(inputHolder, inputSink);
            if (recipe != null) {
                for (var controller : machine.getControllers()) {
                    if (controller instanceof IExtendedRecipeCapabilityHolder holder && holder.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                        enhancedRecipeLogic.gtolib$getRecipeCache().put(recipe, rhl.rhl);
                    }
                }
                for (var proxy : machine.proxyMachines) {
                    var rhl = (InternalSlotRecipeHandler.AbstractRHL) proxy.getProxySlotRecipeHandler().getProxySlotHandlers().get(index);
                    for (var controller : proxy.getControllers()) {
                        if (controller instanceof IExtendedRecipeCapabilityHolder holder && holder.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                            enhancedRecipeLogic.gtolib$getRecipeCache().put(recipe, rhl.rhl);
                        }
                    }
                }
            }
            onContentsChanged();
            return true;
        }

        @Nullable
        public List<Ingredient> handleItemInternal(List<Ingredient> left, boolean simulate) {
            boolean changed = false;
            for (var it = left.iterator(); it.hasNext();) {
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
                    var stack = entry.getKey();
                    var count = entry.getLongValue();
                    if (stack.isEmpty() || count == 0) {
                        it2.remove();
                        continue;
                    }
                    if (!ingredient.test(stack)) continue;
                    long extracted = Math.min(count, amount);
                    if (!simulate && extracted > 0) {
                        changed = true;
                        count -= extracted;
                        if (count == 0) it2.remove();
                        else entry.setValue(count);
                    }
                    amount -= extracted;
                    if (amount < 1) {
                        it.remove();
                        break;
                    }
                }
            }
            if (changed) onContentsChanged();
            return left.isEmpty() ? null : left;
        }

        @Nullable
        public List<FluidIngredient> handleFluidInternal(List<FluidIngredient> left, boolean simulate) {
            boolean changed = false;
            for (var it = left.iterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                long amount = FastFluidIngredient.getAmount(ingredient);
                for (var it2 = fluidInventory.object2LongEntrySet().fastIterator(); it2.hasNext();) {
                    var entry = it2.next();
                    var stack = entry.getKey();
                    var count = entry.getLongValue();
                    if (stack.isEmpty() || count == 0) {
                        it2.remove();
                        continue;
                    }
                    if (!ingredient.test(stack)) continue;
                    long extracted = Math.min(count, amount);
                    if (!simulate && extracted > 0) {
                        changed = true;
                        count -= extracted;
                        if (count == 0) it2.remove();
                        else entry.setValue(count);
                    }
                    amount -= extracted;
                    if (amount < 1) {
                        it.remove();
                        break;
                    }
                }
            }
            if (changed) onContentsChanged();
            return left.isEmpty() ? null : left;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = super.serializeNBT();
            if (recipe != null) {
                tag.put("recipe", recipe.serializeNBT());
            }
            ListTag itemsTag = new ListTag();
            for (ObjectIterator<Object2LongMap.Entry<ItemStack>> it = itemInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                var entry = it.next();
                var ct = entry.getKey().serializeNBT();
                ct.putLong("real", entry.getLongValue());
                itemsTag.add(ct);
            }
            if (!itemsTag.isEmpty()) tag.put("inventory", itemsTag);
            ListTag fluidsTag = new ListTag();
            for (ObjectIterator<Object2LongMap.Entry<FluidStack>> it = fluidInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                var entry = it.next();
                var ct = entry.getKey().writeToNBT(new CompoundTag());
                ct.putLong("real", entry.getLongValue());
                fluidsTag.add(ct);
            }
            if (!fluidsTag.isEmpty()) tag.put("fluidInventory", fluidsTag);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            setRecipe(Recipe.deserializeNBT(tag.get("recipe")));
            ListTag items = tag.getList("inventory", Tag.TAG_COMPOUND);
            for (Tag t : items) {
                if (!(t instanceof CompoundTag ct)) continue;
                var stack = ItemStack.of(ct);
                var count = ct.getLong("real");
                if (!stack.isEmpty() && count > 0) {
                    itemInventory.put(stack, count);
                }
            }
            ListTag fluids = tag.getList("fluidInventory", Tag.TAG_COMPOUND);
            for (Tag t : fluids) {
                if (!(t instanceof CompoundTag ct)) continue;
                var stack = FluidStack.loadFluidStackFromNBT(ct);
                var amount = ct.getLong("real");
                if (!stack.isEmpty() && amount > 0) {
                    fluidInventory.put(stack, amount);
                }
            }
        }

        public void setOnContentsChanged(final Runnable onContentsChanged) {
            this.onContentsChanged = onContentsChanged;
        }

        public Runnable getOnContentsChanged() {
            return this.onContentsChanged;
        }
    }

    public static final class InputSink implements IPatternDetails.PatternInputSink {

        private final InternalSlot slot;
        private Predicate<ItemStack> itemCallback = i -> false;

        private InputSink(InternalSlot slot) {
            this.slot = slot;
        }

        @Override
        public void pushInput(AEKey key, long amount) {}

        public void pushInput(@Nullable Object stack, long amount) {
            if (amount <= 0L) return;
            if (stack instanceof ItemStack itemStack) {
                if (itemCallback.test(itemStack)) return;
                slot.itemInventory.addTo(itemStack, amount);
            } else if (stack instanceof FluidStack fluidStack) {
                slot.fluidInventory.addTo(fluidStack, amount);
            }
        }

        void setItemCallback(final Predicate<ItemStack> itemCallback) {
            this.itemCallback = itemCallback;
        }
    }

    public NotifiableNotConsumableItemHandler getShareInventory() {
        return this.shareInventory;
    }

    public NotifiableNotConsumableFluidHandler getShareTank() {
        return this.shareTank;
    }

    public InternalSlotRecipeHandler getInternalRecipeHandler() {
        return this.internalRecipeHandler;
    }
}
