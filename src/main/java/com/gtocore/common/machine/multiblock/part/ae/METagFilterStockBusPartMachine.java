package com.gtocore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;

import appeng.api.stacks.AEKey;
import appeng.util.prioritylist.IPartitionList;
import com.glodblock.github.extendedae.common.me.taglist.TagPriorityList;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class METagFilterStockBusPartMachine extends MEStockingBusPartMachine implements ITagFilterPartMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(METagFilterStockBusPartMachine.class, MEStockingBusPartMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    private String tagWhite = "";
    @Persisted
    private String tagBlack = "";

    private IPartitionList filter;

    public METagFilterStockBusPartMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new FilterIFancyConfigurator(this));
    }

    @Override
    boolean test(AEKey what) {
        if (filter == null) filter = new TagPriorityList(this.tagWhite, this.tagBlack);
        return filter.isListed(what);
    }

    @Override
    protected CompoundTag writeConfigToTag() {
        CompoundTag tag = new CompoundTag();
        tag.putByte("GhostCircuit", (byte) IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.getStackInSlot(0)));
        tag.putString("TagWhite", tagWhite);
        tag.putString("TagBlack", tagBlack);
        return tag;
    }

    @Override
    protected void readConfigFromTag(CompoundTag tag) {
        if (tag.contains("GhostCircuit")) {
            circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(tag.getByte("GhostCircuit")));
        }
        if (tag.contains("TagWhite")) {
            setTagWhite(tag.getString("TagWhite"));
        }
        if (tag.contains("TagBlack")) {
            setTagBlack(tag.getString("TagBlack"));
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void setTagWhite(final String tagWhite) {
        this.tagWhite = tagWhite;
        filter = new TagPriorityList(this.tagWhite, this.tagBlack);
    }

    @Override
    public void setTagBlack(final String tagBlack) {
        this.tagBlack = tagBlack;
        filter = new TagPriorityList(this.tagWhite, this.tagBlack);
    }

    @Override
    public String getTagWhite() {
        return this.tagWhite;
    }

    @Override
    public String getTagBlack() {
        return this.tagBlack;
    }
}
