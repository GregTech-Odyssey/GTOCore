package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;
import com.gtocore.common.machine.multiblock.part.maintenance.CMHatchPartMachine;

import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.multiblock.IDroneControlCenterMachine;
import com.gtolib.api.machine.part.DroneHatchPartMachine;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gtocore.common.machine.multiblock.part.maintenance.ModularHatchPartMachine.CLEANROOM_NOT_SET;
import static com.gtocore.common.machine.multiblock.part.maintenance.ModularHatchPartMachine.CURRENT_CLEANROOM;
import static com.gtolib.api.machine.feature.IDroneInteractionMachine.NETWORK;

public class CleanroomProvider extends Extension implements IDroneControlCenterMachine {

    private @Nullable ICleanroomProvider cleanroomType = null;
    private final ObjectList<DroneHatchPartMachine> droneHatchPartMachine = new ObjectArrayList<>();

    public CleanroomProvider(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity, ILargeSpaceStationMachine.twoWayPositionFunction(41));
    }

    @Override
    public void onStructureFormed() {
        droneHatchPartMachine.clear();
        super.onStructureFormed();
        IFilterType filterType = getMultiblockState().getMatchContext().get("FilterType");
        if (filterType != null) {
            this.cleanroomType = switch (filterType.getCleanroomType().getName()) {
                case "sterile_cleanroom" -> CMHatchPartMachine.STERILE_DUMMY_CLEANROOM;
                case "law_cleanroom" -> CMHatchPartMachine.LAW_DUMMY_CLEANROOM;
                default -> CMHatchPartMachine.DUMMY_CLEANROOM;
            };
        }
        IIWirelessInteractor.addToNet(NETWORK, this);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.cleanroomType = null;
        droneHatchPartMachine.clear();
        IIWirelessInteractor.removeFromNet(NETWORK, this);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        this.cleanroomType = null;
        IIWirelessInteractor.removeFromNet(NETWORK, this);
    }

    @Override
    public boolean isActiveState() {
        return isWorkspaceReady();
    }

    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof DroneHatchPartMachine machine) {
            droneHatchPartMachine.add(machine);
        }
    }

    @Override
    public long getEUt() {
        if (cleanroomType == null) {
            return VA[HV];
        }
        return (long) VA[LuV] * cleanroomType.getTypes().size();
    }

    @Override
    public Set<CleanroomType> getTypes() {
        return cleanroomType == null ? Collections.emptySet() : ImmutableSet.copyOf(cleanroomType.getTypes());
    }

    @Override
    public ObjectList<DroneHatchPartMachine> getDroneHatchPartMachine() {
        return droneHatchPartMachine;
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        list.add(Component.translatable(CURRENT_CLEANROOM));
        list.add(getCurrentCleanroom().withStyle(ChatFormatting.GREEN));
        IDroneControlCenterMachine.super.addCustomText(list);
    }

    private MutableComponent getCurrentCleanroom() {
        if (cleanroomType == null || cleanroomType.getTypes().isEmpty()) {
            return Component.translatable(CLEANROOM_NOT_SET);
        }
        MutableComponent result = Component.empty();
        Iterator<CleanroomType> iterator = cleanroomType.getTypes().iterator();
        while (iterator.hasNext()) {
            result.append(Component.translatable(iterator.next().getTranslationKey()));
            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        return result;
    }
}
