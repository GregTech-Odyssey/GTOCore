package com.gtocore.common.machine.multiblock.part.ae;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNodeListener;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMufflerMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.SingleCustomItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;
import com.gtolib.api.machine.trait.InaccessibleInfiniteHandler;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.gregtechceu.gtceu.common.data.GTMachines.MUFFLER_HATCH;

public class MEMufflerHatchPartMachine extends MEPartMachine implements IMufflerMachine {
    @Persisted
    private final KeyStorage internalBuffer;
    @Persisted
    private final NotifiableItemStackHandler mufflerHatchInv;
    private final InaccessibleInfiniteHandler handler;
    @Persisted
    private int recoveryChance=0;

    public MEMufflerHatchPartMachine(@NotNull MetaMachineBlockEntity holder) {
        super(holder, IO.NONE);
        internalBuffer = new KeyStorage();
        handler = new InaccessibleInfiniteHandler(this, internalBuffer);
        mufflerHatchInv = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH, SingleCustomItemStackHandler::new);
        mufflerHatchInv.setFilter(stack -> Wrapper.MUFFLER_HATCH_CHECK.containsKey(stack.getItem()));
        mufflerHatchInv.addChangedListener(this::onMufflerChange);
    }

    public void onMufflerChange(){
        var item=mufflerHatchInv.getStackInSlot(0).getItem();
        if(!Wrapper.MUFFLER_HATCH_CHECK.containsKey(item)){
            recoveryChance=0;
            return;
        }
        recoveryChance=Wrapper.MUFFLER_HATCH_CHECK.get(item);

    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        super.setWorkingEnabled(workingEnabled);
        handler.updateAutoOutputSubscription();
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        handler.updateAutoOutputSubscription();
    }

    @Override
    public void onMachineRemoved() {
        var grid = getMainNode().getGrid();
        if (grid != null && !internalBuffer.isEmpty()) {
            for (var entry : internalBuffer) {
                grid.getStorageService().getInventory().insert(entry.getKey(), entry.getLongValue(), Actionable.MODULATE, getActionSourceField());
            }
        }
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.superAttachConfigurators(configuratorPanel);
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(0, 0, 170, 65);
        group.addWidget(new LabelWidget(5, 0, () -> this.getOnlineField() ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
        group.addWidget(new LabelWidget(5, 10, "gtceu.gui.waiting_list"));
        group.addWidget(new AEListGridWidget.Item(5, 20, 3, this.internalBuffer));
        return group;
    }

    public int getRecoveryChance() {
        return this.recoveryChance;
    }

    @Override
    public void recoverItemsTable(ItemStack recoveryItems) {

    }

    @Override
    public boolean isFrontFaceFree() {
        return this.getOnlineField() && recoveryChance!=0;
    }

    class Wrapper{
        public static final Map<Item, Integer> MUFFLER_HATCH_CHECK;
        static {
            var mufflerMap=new HashMap<Item,Integer>();
            for(var i:MUFFLER_HATCH){
                mufflerMap.put(i.getBlock().asItem(),i.getTier()*10);
            }
            MUFFLER_HATCH_CHECK =mufflerMap;
        }
    }
}
