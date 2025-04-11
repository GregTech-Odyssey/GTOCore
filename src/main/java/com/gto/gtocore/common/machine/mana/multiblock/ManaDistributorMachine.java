package com.gto.gtocore.common.machine.mana.multiblock;

import com.gto.gtocore.api.capability.ManaContainerList;
import com.gto.gtocore.api.machine.IIWirelessInteractorMachine;
import com.gto.gtocore.api.machine.mana.feature.IManaMultiblock;
import com.gto.gtocore.api.machine.mana.trait.ManaTrait;
import com.gto.gtocore.api.machine.multiblock.NoRecipeLogicMultiblockMachine;
import com.gto.gtocore.client.ClientUtil;
import com.gto.gtocore.utils.GTOUtils;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class ManaDistributorMachine extends NoRecipeLogicMultiblockMachine implements IManaMultiblock {

    public static final Map<ResourceLocation, Set<ManaDistributorMachine>> NETWORK = new Object2ObjectOpenHashMap<>();

    private int amount = 0;

    private BlockPos centrepos;

    private final ManaTrait manaTrait;

    private final int max;
    private final int radius;

    public static Function<IMachineBlockEntity, ManaDistributorMachine> create(int max, int radius) {
        return holder -> new ManaDistributorMachine(holder, max, radius);
    }

    private ManaDistributorMachine(IMachineBlockEntity holder, int max, int radius) {
        super(holder);
        this.max = max;
        this.radius = radius;
        this.manaTrait = new ManaTrait(this);
    }

    public boolean add(BlockPos pos) {
        if (GTOUtils.calculateDistance(pos, centrepos) > radius) return false;
        if (amount >= max) return false;
        amount++;
        return true;
    }

    public void remove() {
        if (amount > 0) amount--;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.binding_amount", amount));
        textList.add(ComponentPanelWidget.withButton(Component.translatable("gui.enderio.range.show"), "show"));
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (clickData.isRemote && "show".equals(componentData)) {
            ClientUtil.highlighting(MachineUtils.getOffsetPos(2, 2, getFrontFacing(), getPos()), radius);
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (isRemote()) return;
        centrepos = MachineUtils.getOffsetPos(2, 2, getFrontFacing(), getPos());
        IIWirelessInteractorMachine.addToNet(NETWORK, this);
    }

    @Override
    public void onStructureInvalid() {
        centrepos = null;
        IIWirelessInteractorMachine.removeFromNet(NETWORK, this);
        super.onStructureInvalid();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        IIWirelessInteractorMachine.removeFromNet(NETWORK, this);
    }

    @Override
    public @NotNull ManaContainerList getManaContainer() {
        return manaTrait.getManaContainers();
    }

    @Override
    public boolean isGeneratorMana() {
        return false;
    }
}
