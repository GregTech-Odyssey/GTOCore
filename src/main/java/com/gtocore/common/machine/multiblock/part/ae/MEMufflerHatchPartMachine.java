package com.gtocore.common.machine.multiblock.part.ae;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNodeListener;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.SingleCustomItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;
import com.gtocore.api.machine.IGTOMufflerMachine;
import com.gtocore.common.machine.multiblock.part.maintenance.ModularHatchPartMachine;
import com.gtolib.GTOCore;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.trait.InaccessibleInfiniteHandler;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.gregtechceu.gtceu.common.data.GTMachines.MUFFLER_HATCH;

public class MEMufflerHatchPartMachine extends MEPartMachine implements IGTOMufflerMachine {
    @Persisted
    private final KeyStorage internalBuffer;
    @Persisted
    private final NotifiableItemStackHandler mufflerHatchInv;
    private final InaccessibleInfiniteHandler handler;
    @Persisted
    private int recoveryChance = 0;

    public MEMufflerHatchPartMachine(@NotNull MetaMachineBlockEntity holder) {
        super(holder, IO.NONE);
        internalBuffer = new KeyStorage();
        handler = new InaccessibleInfiniteHandler(this, internalBuffer);
        mufflerHatchInv = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH, SingleCustomItemStackHandler::new);
        mufflerHatchInv.setFilter(stack -> Wrapper.MUFFLER_HATCH_CHECK.containsKey(stack.getItem()));
        mufflerHatchInv.addChangedListener(this::onMufflerChange);
    }

    public void onMufflerChange() {
        var item = mufflerHatchInv.getStackInSlot(0).getItem();
        if (!Wrapper.MUFFLER_HATCH_CHECK.containsKey(item)) {
            recoveryChance = 0;
            return;
        }
        if (!GTOCore.isSimple()) {
            var tier = Wrapper.MUFFLER_HATCH_CHECK.get(item);
            if (getControllers().getFirst() instanceof ITieredMachine machine && machine.getTier() < tier - 2) {
                recoveryChance = 0;
                return;
            }

        }
        recoveryChance = 10 << Wrapper.MUFFLER_HATCH_CHECK.get(item);

    }

    @Override
    public void addedToController(IMultiController controller) {
        super.addedToController(controller);
        onMufflerChange();
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

    @RegisterLanguage(cn = "在槽位放入消声仓以启用", en = "Place MufflerHatch in the corresponding slot to enable")
    private static final String TOOLTIP_KEY = "gtocore.machine.me_muffler_part.tooltip";

    @Override
    public Widget createUIWidget() {
        WidgetGroup group=new WidgetGroup(0, 0, 170, 100);
        WidgetGroup muffler = new WidgetGroup(0, 0, 170, 35);
        muffler.addWidget(new SlotWidget(mufflerHatchInv.storage, 0,160, 4, true, true)
                        .setBackground(GuiTextures.SLOT)
                        .setHoverTooltips(Component.translatable(TOOLTIP_KEY)));
        muffler.addWidget(new ComponentPanelWidget(6, 4 + 11, (list) -> list.add(Component.translatable("gtceu.muffler.recovery_tooltip", recoveryChance))));
        group.addWidget(muffler);
        WidgetGroup meOutput = new WidgetGroup(0, 35, 170, 65);
        meOutput.addWidget(new LabelWidget(5, 0, () -> this.getOnlineField() ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
        meOutput.addWidget(new LabelWidget(5, 10, "gtceu.gui.waiting_list"));
        meOutput.addWidget(new AEListGridWidget.Item(5, 20, 3, this.internalBuffer));
        group.addWidget(meOutput);

        return meOutput;
    }

    @Override
    public void recoverItemsTable(ItemStack recoveryItems) {
        handler.insertInternal(recoveryItems,recoveryItems.getCount());
    }

    @Override
    public boolean isFrontFaceFree() {
        return this.getOnlineField() && recoveryChance != 0;
    }

    @Override
    public int gtolib$getRecoveryChance() {
        return recoveryChance;
    }

    static class Wrapper {
        public static final Map<Item, Integer> MUFFLER_HATCH_CHECK;

        static {
            var mufflerMap = new HashMap<Item, Integer>();
            for (var i : MUFFLER_HATCH) {
                if(i!=null){
                    mufflerMap.put(i.getBlock().asItem(), i.getTier());
                }else{
                    GTOCore.LOGGER.error(new NullPointerException(Arrays.toString(MUFFLER_HATCH)).toString());
                }
            }
            MUFFLER_HATCH_CHECK = mufflerMap;
        }
    }
}
