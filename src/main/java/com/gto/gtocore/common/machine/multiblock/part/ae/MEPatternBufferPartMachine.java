package com.gto.gtocore.common.machine.multiblock.part.ae;

import com.gto.gtocore.api.machine.trait.NotifiableNotConsumableFluidHandler;
import com.gto.gtocore.api.machine.trait.NotifiableNotConsumableItemHandler;
import com.gto.gtocore.api.recipe.FastSizedIngredient;
import com.gto.gtocore.common.machine.trait.InternalSlotRecipeHandler;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.ButtonConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyInvConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyTankConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.utils.GTMath;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

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
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.api.storage.StorageHelper;
import appeng.crafting.pattern.ProcessingPatternItem;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEPatternBufferPartMachine extends MEPatternPartMachine<MEPatternBufferPartMachine.InternalSlot> implements IDataStickInteractable {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MEPatternBufferPartMachine.class, MEPatternPartMachine.MANAGED_FIELD_HOLDER);

    static final int MAX_PATTERN_COUNT = 27;

    @Getter
    @Persisted
    private final NotifiableNotConsumableItemHandler shareInventory;

    @Getter
    @Persisted
    private final NotifiableNotConsumableFluidHandler shareTank;

    @Persisted
    private final Set<BlockPos> proxies = new ObjectOpenHashSet<>();
    private final Set<MEPatternBufferProxyPartMachine> proxyMachines = new ReferenceOpenHashSet<>();

    @Getter
    private final InternalSlotRecipeHandler internalRecipeHandler;

    public MEPatternBufferPartMachine(IMachineBlockEntity holder) {
        super(holder, MAX_PATTERN_COUNT);
        this.shareInventory = createShareInventory();
        this.shareTank = new NotifiableNotConsumableFluidHandler(this, 9, 8 * FluidType.BUCKET_VOLUME);
        this.internalRecipeHandler = new InternalSlotRecipeHandler(this, getInternalInventory());
        circuitSlotEnabled = true;
    }

    NotifiableNotConsumableItemHandler createShareInventory() {
        return new NotifiableNotConsumableItemHandler(this, 9, IO.NONE);
    }

    @Override
    InternalSlot[] createInternalSlotArray() {
        return new InternalSlot[MAX_PATTERN_COUNT];
    }

    @Override
    boolean patternFilter(ItemStack stack) {
        return stack.getItem() instanceof ProcessingPatternItem;
    }

    @Override
    InternalSlot createInternalSlot() {
        return new InternalSlot(this);
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
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new ButtonConfigurator(
                new GuiTextureGroup(GuiTextures.BUTTON, GuiTextures.REFUND_OVERLAY), this::refundAll)
                .setTooltips(List.of(Component.translatable("gui.gtceu.refund_all.desc"))));
        if (isCircuitSlotEnabled()) {
            configuratorPanel.attachConfigurators(new CircuitFancyConfigurator(getCircuitInventory().storage));
        }
        configuratorPanel.attachConfigurators(new FancyInvConfigurator(
                shareInventory.storage, Component.translatable("gui.gtceu.share_inventory.title"))
                .setTooltips(List.of(
                        Component.translatable("gui.gtceu.share_inventory.desc.0"),
                        Component.translatable("gui.gtceu.share_inventory.desc.1"))));
        configuratorPanel.attachConfigurators(new FancyTankConfigurator(
                shareTank.getStorages(), Component.translatable("gui.gtceu.share_tank.title"))
                .setTooltips(List.of(
                        Component.translatable("gui.gtceu.share_tank.desc.0"),
                        Component.translatable("gui.gtceu.share_inventory.desc.1"))));
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
        var items = new Object2LongOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());
        var fluids = new Object2LongOpenHashMap<FluidStack>();
        for (InternalSlot slot : getInternalInventory()) {
            slot.itemInventory.object2LongEntrySet().fastForEach(e -> items.addTo(e.getKey(), e.getLongValue()));
            slot.fluidInventory.object2LongEntrySet().fastForEach(e -> fluids.addTo(e.getKey(), e.getLongValue()));
        }
        return new BufferData(items, fluids);
    }

    public static class InternalSlot extends AbstractInternalSlot {

        private final MEPatternBufferPartMachine machine;

        @Getter
        @Setter
        private Runnable onContentsChanged = () -> {};

        @Getter
        private final Object2LongOpenCustomHashMap<ItemStack> itemInventory = new Object2LongOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());
        @Getter
        private final Object2LongOpenHashMap<FluidStack> fluidInventory = new Object2LongOpenHashMap<>();
        private final ReentrantLock lock = new ReentrantLock();
        private List<ItemStack> itemStacks = null;
        private List<FluidStack> fluidStacks = null;

        private InternalSlot(MEPatternBufferPartMachine machine) {
            this.machine = machine;
        }

        boolean isEmpty() {
            return isItemEmpty() && isFluidEmpty();
        }

        public boolean isItemEmpty() {
            return itemInventory.isEmpty();
        }

        public boolean isFluidEmpty() {
            return fluidInventory.isEmpty();
        }

        private void onContentsChanged() {
            lock.lock();
            try {
                itemStacks = null;
                fluidStacks = null;
            } finally {
                lock.unlock();
            }
            onContentsChanged.run();
        }

        private void add(AEKey what, long amount, Predicate<ItemStack> itemCallback) {
            if (amount <= 0L) return;
            if (what instanceof AEItemKey itemKey) {
                var stack = itemKey.toStack();
                if (itemCallback.test(stack)) return;
                synchronized (itemInventory) {
                    itemInventory.computeLong(stack, (k, v) -> v == null ? amount : v + amount);
                }
            } else if (what instanceof AEFluidKey fluidKey) {
                var stack = fluidKey.toStack(1);
                synchronized (fluidInventory) {
                    fluidInventory.computeLong(stack, (k, v) -> v == null ? amount : v + amount);
                }
            }
        }

        public List<ItemStack> getItems() {
            lock.lock();
            try {
                if (itemStacks == null) {
                    synchronized (itemInventory) {
                        List<ItemStack> stacks = new ObjectArrayList<>(itemInventory.size());
                        for (Object2LongMap.Entry<ItemStack> e : itemInventory.object2LongEntrySet()) {
                            e.getKey().setCount(GTMath.saturatedCast(e.getLongValue()));
                            stacks.add(e.getKey());
                        }
                        itemStacks = stacks;
                    }
                }
                return itemStacks;
            } finally {
                lock.unlock();
            }
        }

        public List<FluidStack> getFluids() {
            lock.lock();
            try {
                if (fluidStacks == null) {
                    synchronized (fluidInventory) {
                        List<FluidStack> stacks = new ObjectArrayList<>(fluidInventory.size());
                        for (Object2LongMap.Entry<FluidStack> e : fluidInventory.object2LongEntrySet()) {
                            e.getKey().setAmount(GTMath.saturatedCast(e.getLongValue()));
                            stacks.add(e.getKey());
                        }
                        fluidStacks = stacks;
                    }
                }
                return fluidStacks;
            } finally {
                lock.unlock();
            }
        }

        public void refund() {
            var network = machine.getMainNode().getGrid();
            if (network != null) {
                MEStorage networkInv = network.getStorageService().getInventory();
                var energy = network.getEnergyService();

                synchronized (itemInventory) {
                    for (var it = itemInventory.object2LongEntrySet().iterator(); it.hasNext();) {
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
                }

                synchronized (fluidInventory) {
                    for (var it = fluidInventory.object2LongEntrySet().iterator(); it.hasNext();) {
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
                }
                onContentsChanged();
            }
        }

        public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder, Predicate<ItemStack> itemCallback) {
            patternDetails.pushInputsToExternalInventory(inputHolder, (k, a) -> add(k, a, itemCallback));
            onContentsChanged();
            return true;
        }

        public @Nullable List<Ingredient> handleItemInternal(List<Ingredient> left, boolean simulate) {
            boolean changed = false;
            for (var it = left.listIterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }

                var items = ItemUtils.getInnerIngredient(ingredient).getItems();
                if (items.length == 0 || items[0].isEmpty()) {
                    it.remove();
                    continue;
                }
                int amount;
                if (ingredient instanceof FastSizedIngredient si) amount = si.getAmount();
                else amount = items[0].getCount();
                synchronized (itemInventory) {
                    for (var it2 = itemInventory.object2LongEntrySet().iterator(); it2.hasNext();) {
                        var entry = it2.next();
                        var stack = entry.getKey();
                        var count = entry.getLongValue();
                        if (stack.isEmpty() || count == 0) {
                            it2.remove();
                            continue;
                        }
                        if (!ingredient.test(stack)) continue;
                        int extracted = Math.min(GTMath.saturatedCast(count), amount);
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
            }
            if (changed) onContentsChanged();
            return left.isEmpty() ? null : left;
        }

        public @Nullable List<FluidIngredient> handleFluidInternal(List<FluidIngredient> left, boolean simulate) {
            boolean changed = false;
            for (var it = left.listIterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }

                var fluids = ingredient.getStacks();
                if (fluids.length == 0 || fluids[0].isEmpty()) {
                    it.remove();
                    continue;
                }

                int amount = fluids[0].getAmount();
                synchronized (fluidInventory) {
                    for (var it2 = fluidInventory.object2LongEntrySet().iterator(); it2.hasNext();) {
                        var entry = it2.next();
                        var stack = entry.getKey();
                        var count = entry.getLongValue();
                        if (stack.isEmpty() || count == 0) {
                            it2.remove();
                            continue;
                        }
                        if (!ingredient.test(stack)) continue;
                        int extracted = Math.min(GTMath.saturatedCast(count), amount);
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
            }

            if (changed) onContentsChanged();
            return left.isEmpty() ? null : left;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();

            ListTag itemsTag = new ListTag();
            for (var entry : itemInventory.object2LongEntrySet()) {
                var ct = entry.getKey().serializeNBT();
                ct.putLong("real", entry.getLongValue());
                itemsTag.add(ct);
            }
            if (!itemsTag.isEmpty()) tag.put("inventory", itemsTag);

            ListTag fluidsTag = new ListTag();
            for (var entry : fluidInventory.object2LongEntrySet()) {
                var ct = entry.getKey().writeToNBT(new CompoundTag());
                ct.putLong("real", entry.getLongValue());
                fluidsTag.add(ct);
            }
            if (!fluidsTag.isEmpty()) tag.put("fluidInventory", fluidsTag);

            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            ListTag items = tag.getList("inventory", Tag.TAG_COMPOUND);
            for (Tag t : items) {
                if (!(t instanceof CompoundTag ct)) continue;
                var stack = ItemStack.of(ct);
                var count = ct.getLong("real");
                if (!stack.isEmpty() && count > 0) {
                    synchronized (itemInventory) {
                        itemInventory.put(stack, count);
                    }
                }
            }

            ListTag fluids = tag.getList("fluidInventory", Tag.TAG_COMPOUND);
            for (Tag t : fluids) {
                if (!(t instanceof CompoundTag ct)) continue;
                var stack = FluidStack.loadFluidStackFromNBT(ct);
                var amount = ct.getLong("real");
                if (!stack.isEmpty() && amount > 0) {
                    synchronized (fluidInventory) {
                        fluidInventory.put(stack, amount);
                    }
                }
            }
        }
    }
}
