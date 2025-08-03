package com.gtocore.integration.ae;

import com.gtolib.GTOCore;
import com.gtolib.utils.BlockCapabilityCache;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.features.IPlayerRegistry;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.parts.BusSupport;
import appeng.api.parts.IPartHost;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.stacks.AEKeyType;
import appeng.api.storage.IStorageMounts;
import appeng.api.storage.IStorageProvider;
import appeng.api.storage.MEStorage;
import appeng.capabilities.Capabilities;
import appeng.core.AppEng;
import appeng.core.stats.AdvancementTriggers;
import appeng.helpers.InterfaceLogicHost;
import appeng.items.parts.PartModels;
import appeng.me.GridNode;
import appeng.me.service.EnergyService;
import appeng.me.service.StorageService;
import appeng.me.storage.CompositeStorage;
import appeng.me.storage.MEInventoryHandler;
import appeng.me.storage.NullInventory;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.parts.PartModel;
import appeng.parts.automation.StackWorldBehaviors;
import appeng.parts.reporting.AbstractTerminalPart;
import appeng.util.inv.AppEngInternalInventory;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

public class SimpleCraftingTerminal extends AbstractTerminalPart
                                    implements IAEPowerStorage, IStorageProvider {

    public static final ResourceLocation INV_CRAFTING = AppEng.makeId("crafting_terminal_crafting");

    @PartModels
    public static final ResourceLocation MODEL = ResourceLocation.tryBuild(GTOCore.MOD_ID, "block/crafting_terminal");

    public static final IPartModel MODELS = new PartModel(MODEL);

    private final AppEngInternalInventory craftingGrid = new AppEngInternalInventory(this, 9);

    private final StorageBusInventory handler = new StorageBusInventory(NullInventory.of());
    private BlockCapabilityCache<MEStorage> adjacentStorageAccessor;
    @Nullable
    private Map<AEKeyType, ExternalStorageStrategy> externalStorageStrategies;
    private int tick;

    public SimpleCraftingTerminal(IPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean onPartActivate(Player player, InteractionHand hand, Vec3 pos) {
        if (isArsSpellBook(player.getMainHandItem()) || isArsSpellBook(player.getOffhandItem())) return false;
        updateTarget();
        return super.onPartActivate(player, hand, pos);
    }

    private boolean isArsSpellBook(ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getItem() instanceof SpellBook;
    }

    @Override
    public void clearContent() {
        super.clearContent();
        craftingGrid.clear();
    }

    @Override
    public void readFromNBT(CompoundTag data) {
        super.readFromNBT(data);
        this.craftingGrid.readFromNBT(data, "craftingGrid");
    }

    @Override
    public void writeToNBT(CompoundTag data) {
        super.writeToNBT(data);
        this.craftingGrid.writeToNBT(data, "craftingGrid");
    }

    @Override
    public MenuType<?> getMenuType(Player p) {
        return CraftingTermMenu.TYPE;
    }

    @Override
    public InternalInventory getSubInventory(ResourceLocation id) {
        if (id.equals(INV_CRAFTING)) {
            return craftingGrid;
        } else {
            return super.getSubInventory(id);
        }
    }

    @Override
    public IPartModel getStaticModels() {
        return MODELS;
    }

    @Override
    public MEStorage getInventory() {
        if (!isClientSide()) {
            if (this.tick % 10 == 0) {
                if (this.handler.getDelegate() instanceof CompositeStorage compositeStorage) {
                    compositeStorage.onTick();
                }
            }
            this.tick++;
        }
        return this.handler.getDelegate();
    }

    /* StorageProvider */
    @Override
    public void addToWorld() {
        super.addToWorld();
        this.updateNode();
    }

    @Override
    public boolean canBePlacedOn(BusSupport what) {
        return false;
    }

    private void updateNode() {
        GridNode node = (GridNode) this.getMainNode().getNode();
        if (node != null) {
            node.addService(IAEPowerStorage.class, this);
            node.addService(IStorageProvider.class, this);
            node.setIdlePowerUsage(0);
            EnergyService energyService = (EnergyService) node.getGrid().getEnergyService();
            StorageService storageService = (StorageService) node.getGrid().getStorageService();
            storageService.addNode(node, null);
            energyService.addNode(node, null);
        }
    }

    private void updateTarget() {
        if (isClientSide()) {
            return; // Part is not part of level yet or its client-side
        }
        var side = getSide();
        if (side == null) return;
        MEStorage foundMonitor;
        Map<AEKeyType, MEStorage> foundExternalApi = Collections.emptyMap();
        if (adjacentStorageAccessor == null) adjacentStorageAccessor = BlockCapabilityCache.create(Capabilities.STORAGE, getHost().getBlockEntity());
        foundMonitor = adjacentStorageAccessor.find(getHost().getBlockEntity().getBlockPos().relative(side), side, side.getOpposite());

        if (foundMonitor == null) {

            foundExternalApi = new IdentityHashMap<>(2);
            findExternalStorages(foundExternalApi);
        }

        if (this.handler.getDescription() instanceof CompositeStorage compositeStorage && !foundExternalApi.isEmpty()) {
            compositeStorage.setStorages(foundExternalApi);
            return;
        }

        // Update inventory
        MEStorage newInventory;
        if (foundMonitor != null) {
            newInventory = foundMonitor;
            this.checkStorageBusOnInterface();
        } else if (!foundExternalApi.isEmpty()) {
            newInventory = new CompositeStorage(foundExternalApi);
        } else {
            newInventory = NullInventory.of();
        }
        this.handler.setDelegate(newInventory);
    }

    private Map<AEKeyType, ExternalStorageStrategy> getExternalStorageStrategies() {
        if (externalStorageStrategies == null) {
            var host = getHost().getBlockEntity();
            this.externalStorageStrategies = StackWorldBehaviors.createExternalStorageStrategies(
                    (ServerLevel) host.getLevel(),
                    host.getBlockPos().relative(getSide()),
                    getSide().getOpposite());
        }
        return externalStorageStrategies;
    }

    private void findExternalStorages(Map<AEKeyType, MEStorage> storages) {
        for (var entry : getExternalStorageStrategies().entrySet()) {
            var wrapper = entry.getValue().createWrapper(
                    false,
                    this::invalidateOnExternalStorageChange);
            if (wrapper != null) {
                storages.put(entry.getKey(), wrapper);
            }
        }
    }

    private void invalidateOnExternalStorageChange() {
        getMainNode().ifPresent((grid, node) -> {
            grid.getTickManager().alertDevice(node);
        });
    }

    private void checkStorageBusOnInterface() {
        var oppositeSide = getSide().getOpposite();
        var targetPos = getBlockEntity().getBlockPos().relative(getSide());
        var targetBe = getLevel().getBlockEntity(targetPos);

        Object targetHost = targetBe;
        if (targetBe instanceof IPartHost partHost) {
            targetHost = partHost.getPart(oppositeSide);
        }

        if (targetHost instanceof InterfaceLogicHost) {
            var server = getLevel().getServer();
            var player = IPlayerRegistry.getConnected(server, this.getActionableNode().getOwningPlayerId());
            if (player != null) {
                AdvancementTriggers.RECURSIVE.trigger(player);
            }
        }
    }

    @Override
    public void mountInventories(IStorageMounts storageMounts) {
        storageMounts.mount(this.handler, 1);
    }

    private static class StorageBusInventory extends MEInventoryHandler {

        public StorageBusInventory(MEStorage inventory) {
            super(inventory);
        }

        @Override
        protected MEStorage getDelegate() {
            return super.getDelegate();
        }

        @Override
        protected void setDelegate(MEStorage delegate) {
            super.setDelegate(delegate);
        }
    }

    /* PowerStorage */
    @Override
    public double injectAEPower(double amt, Actionable mode) {
        return 0;
    }

    @Override
    public double getAEMaxPower() {
        return Long.MAX_VALUE / 10000;
    }

    @Override
    public double getAECurrentPower() {
        return Long.MAX_VALUE / 10000;
    }

    @Override
    public boolean isAEPublicPowerStorage() {
        return true;
    }

    @Override
    public AccessRestriction getPowerFlow() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public double extractAEPower(double amt, Actionable mode, PowerMultiplier pm) {
        return amt;
    }
}
