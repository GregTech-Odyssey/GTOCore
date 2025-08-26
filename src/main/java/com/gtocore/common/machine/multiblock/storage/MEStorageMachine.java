package com.gtocore.common.machine.multiblock.storage;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.machine.multiblock.part.ae.IStorageAccess;
import com.gtocore.common.machine.multiblock.part.ae.MEBigStorageAccessPartMachine;

import com.gtolib.ae2.storage.BigCellDataStorage;
import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.feature.multiblock.IStorageMultiblock;
import com.gtolib.api.machine.multiblock.NoRecipeLogicMultiblockMachine;
import com.gtolib.utils.FunctionContainer;
import com.gtolib.utils.NumberUtils;
import com.gtolib.utils.StringUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.hepdd.gtmthings.api.capability.IBindable;
import com.hepdd.gtmthings.utils.BigIntegerUtils;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Scanned
public final class MEStorageMachine extends NoRecipeLogicMultiblockMachine implements IMachineLife, IBindable, IDropSaveMachine, IStorageMultiblock {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEStorageMachine.class, NoRecipeLogicMultiblockMachine.MANAGED_FIELD_HOLDER);
    public static final long infinite = 1000000000000L; // 1T
    @RegisterLanguage(en = "Data Index Position: ", cn = "数据索引位置：")
    private static final String MODE = "gtocore.machine.me_storage.mode";
    @Persisted
    private final NotifiableItemStackHandler machineStorage;
    @Persisted
    private UUID uuid;
    @Persisted
    private boolean player = true;
    private IStorageAccess accessPartMachine;

    public MEStorageMachine(MetaMachineBlockEntity holder) {
        super(holder);
        machineStorage = createMachineStorage(i -> i.getItem() == GTOItems.INFINITE_CELL_COMPONENT.asItem());
    }

    @Override
    public void onMachineChanged() {
        unloadContainer();
        loadContainer();
    }

    private void loadContainer() {
        if (isRemote()) return;
        Level level = getLevel();
        if (level == null) return;
        for (IMultiPart part : getParts()) {
            if (part instanceof IStorageAccess storageAccessPartMachine) {
                accessPartMachine = storageAccessPartMachine;
                break;
            }
        }
        if (accessPartMachine == null) return;
        FunctionContainer<Double, ?> functionContainer = getMultiblockState().getMatchContext().get("MEStorageCore");
        if (functionContainer == null) return;
        if (player) {
            accessPartMachine.setUUID(getOwnerUUID());
        } else {
            if (uuid == null) uuid = UUID.randomUUID();
            accessPartMachine.setUUID(uuid);
        }
        accessPartMachine.setCapacity(functionContainer.getValue());
        accessPartMachine.setInfinite(accessPartMachine.getCapacity() > infinite && getStorageStack().getCount() == 64);
        accessPartMachine.setCheck(true);
    }

    private void unloadContainer() {
        if (accessPartMachine != null) {
            accessPartMachine.setCapacity(0);
            accessPartMachine.setUUID(null);
            accessPartMachine.setCheck(false);
            accessPartMachine.setInfinite(false);
            accessPartMachine = null;
        }
    }

    @Override
    public void onStructureInvalid() {
        unloadContainer();
        super.onStructureInvalid();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (isFormed()) loadContainer();
    }

    @Override
    public void onUnload() {
        unloadContainer();
        super.onUnload();
    }

    @Override
    public Widget createUIWidget() {
        return createUIWidget(super.createUIWidget());
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        textList.add(Component.translatable(MODE).append(ComponentPanelWidget.withButton(Component.literal("[").append(player ? Component.translatable("gtceu.ownership.name.player") : Component.translatable("config.gtceu.option.machines")).append("]"), "switch")));
        if (accessPartMachine != null) {
            if (getOffsetTimer() % 10 == 0) accessPartMachine.setObserve(true);
            textList.add(Component.translatable("gui.ae2.BytesUsed", NumberUtils.numberText(accessPartMachine.getBytes()).append(" / ").append(accessPartMachine.isInfinite() ? StringUtils.full_color("infinity") : NumberUtils.formatDouble(accessPartMachine.getCapacity())).withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
            textList.add(Component.literal(String.valueOf(accessPartMachine.getTypes())).withStyle(ChatFormatting.AQUA).append(Component.literal(" ").append(Component.translatable("gui.ae2.Types").withStyle(ChatFormatting.GRAY))));
            if (accessPartMachine instanceof MEBigStorageAccessPartMachine machine) {
                var data = machine.getCellStorage();
                if (data == BigCellDataStorage.EMPTY) return;
                var map = data.getStoredMap();
                if (map == null) return;
                map.object2ObjectEntrySet().fastForEach(entry -> {
                    var currentAmount = entry.getValue();
                    if (currentAmount.compareTo(BigIntegerUtils.BIG_INTEGER_MAX_LONG) > 0) {
                        textList.add(entry.getKey().getDisplayName().copy().append(": ").append(NumberUtils.numberText(currentAmount.doubleValue())).withStyle(ChatFormatting.GRAY));
                    }
                });
            }
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if ("switch".equals(componentData)) {
                player = !player;
                onMachineChanged();
            }
        }
    }

    @Override
    public void onMachinePlaced(@Nullable LivingEntity player, ItemStack stack) {
        if (player != null) {
            setOwnerUUID(player.getUUID());
        }
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
    }

    @Override
    public boolean saveBreak() {
        return uuid != null;
    }

    @Override
    public boolean savePickClone() {
        return false;
    }

    @Override
    public void saveToItem(CompoundTag tag) {
        if (uuid != null) tag.putUUID("uuid", uuid);
    }

    @Override
    public void loadFromItem(CompoundTag tag) {
        if (tag.hasUUID("uuid")) {
            uuid = tag.getUUID("uuid");
        }
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public NotifiableItemStackHandler getMachineStorage() {
        return this.machineStorage;
    }
}
