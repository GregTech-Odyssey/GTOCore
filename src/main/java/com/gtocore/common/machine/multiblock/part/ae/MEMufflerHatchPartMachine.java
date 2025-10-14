package com.gtocore.common.machine.multiblock.part.ae;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNodeListener;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.transfer.item.SingleCustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;
import com.gtocore.api.machine.IGTOMufflerMachine;
import com.gtocore.data.CraftingComponents;
import com.gtolib.GTOCore;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.trait.InaccessibleInfiniteHandler;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MEMufflerHatchPartMachine extends MEPartMachine implements IGTOMufflerMachine {
    @Persisted
    private final KeyStorage internalBuffer;
    @Persisted
    private final NotifiableItemStackHandler mufflerHatchInv;
    @Persisted
    private final NotifiableItemStackHandler amplifierInv;
    private final InaccessibleInfiniteHandler handler;
    @Persisted
    @DescSynced
    private int recoveryChance = 0;

    private static final int COUNT =1<<(GTOCore.difficulty*2);
    private static final int MIN_COUNT= 1<<(GTOCore.difficulty*2-2);

    public MEMufflerHatchPartMachine(@NotNull MetaMachineBlockEntity holder) {
        super(holder, IO.NONE);
        internalBuffer = new KeyStorage();
        handler = new InaccessibleInfiniteHandler(this, internalBuffer,IO.NONE){
            @Override
            public void updateAutoOutputSubscription() {
                if (machine.isOnline()) {
                    updateSubs = getMachine().subscribeServerTick(updateSubs, this::updateTick);
                } else if (updateSubs != null) {
                    updateSubs.unsubscribe();
                    updateSubs = null;
                }
            }
        };
        mufflerHatchInv = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH, SingleCustomItemStackHandler::new);
        mufflerHatchInv.setFilter(stack -> Wrapper.MUFFLER_HATCH.containsKey(stack.getItem()));
        mufflerHatchInv.addChangedListener(this::onMufflerChange);
        amplifierInv = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH){
            @Override
            public int getSlotLimit(int slot) {
                return COUNT;
            }
        };
        amplifierInv.setFilter(stack -> Wrapper.AMPLIFIER_TIER_MAP.containsKey(stack.getItem()));
        amplifierInv.addChangedListener(this::onMufflerChange);
    }

    public void onMufflerChange() {
        var amplifierIs=amplifierInv.getStackInSlot(0);
        var item = mufflerHatchInv.getStackInSlot(0).getItem();
        recoveryChance = 0;
        if (!Wrapper.MUFFLER_HATCH.containsKey(item)) {
            return;
        }
        var tier = Wrapper.MUFFLER_HATCH.get(item);
        if (GTOCore.isExpert() && !getControllers().isEmpty() && getControllers().getFirst() instanceof ITieredMachine machine && machine.getTier()>tier+1) {
            return;
        }
        if(Objects.equals(Wrapper.AMPLIFIER_TIER_MAP.get(amplifierIs.getItem()), Wrapper.MUFFLER_HATCH.get(item))){
            var recoveryChanceMin=tier*10;
            var recoveryChanceMax = recoveryChanceMin*tier;
            recoveryChance=(recoveryChanceMax-recoveryChanceMin)*(amplifierIs.getCount()-MIN_COUNT)/(COUNT-MIN_COUNT);
            recoveryChance+=recoveryChanceMin;
            recoveryChance=Math.max(recoveryChance,recoveryChanceMin);
        }else{
            recoveryChance=Wrapper.MUFFLER_HATCH.get(item)*10;
        }

        RecipeHandlerList.NOTIFY.accept(this);
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
        if(this.getControllers().size()>1){
            controller.onStructureInvalid();
            return;
        }
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

    @RegisterLanguage(cn = "在槽位放入消声仓以启用", en = "Insert a Muffler Hatch into the slot to enable")
    private static final String MUFFLER_TOOLTIP_KEY = "gtocore.machine.me_muffler_part.muffler_tooltip";
    @RegisterLanguage(cn = "在槽位放入相同等级的消声仓以启用", en = "Insert a Muffler Hatch of the same level into the slot to enable")
    private static final String MUFFLER_TOOLTIP_KEY_EXPERT = "gtocore.machine.me_muffler_part.muffler_tooltip_expert";
    @RegisterLanguage(cn = "在槽位放入相同等级的集控核心以增幅概率", en = "Insert a Control Core of the same level into the slot to increase the probability")
    private static final String AMPLIFIER_TOOLTIP_KEY = "gtocore.machine.me_muffler_part.apm_tooltip";
    @RegisterLanguage(cn = "未启用", en = "Disabled")
    private static final String DISABLED_TOOLTIP_KEY = "gtocore.machine.me_muffler_part.gen_tooltip";

    @Override
    public Widget createUIWidget() {
        WidgetGroup group=new WidgetGroup(0, 0, 170, 110);
        WidgetGroup muffler = new WidgetGroup(0, 0, 170, 25);
        muffler.addWidget(new SlotWidget(mufflerHatchInv.storage, 0,140, 10, true, true)
                        .setBackground(GuiTextures.SLOT)
                        .setHoverTooltips(Component.translatable(GTOCore.isExpert()? MUFFLER_TOOLTIP_KEY_EXPERT :MUFFLER_TOOLTIP_KEY)));
        muffler.addWidget(new SlotWidget(amplifierInv.storage, 0,120, 10, true, true)
                        .setBackground(GuiTextures.SLOT)
                        .setHoverTooltips(Component.translatable(AMPLIFIER_TOOLTIP_KEY)));
        muffler.addWidget(new ComponentPanelWidget(6, 15, (list) -> list.add(Component.translatable("gtceu.muffler.recovery_tooltip", recoveryChance!=0?recoveryChance:DISABLED_TOOLTIP_KEY))));
        group.addWidget(muffler);
        WidgetGroup meOutput = new WidgetGroup(0, 35, 170, 65);
        meOutput.addWidget(new LabelWidget(5, 0, () -> this.getOnlineField() ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
        meOutput.addWidget(new LabelWidget(5, 10, "gtceu.gui.waiting_list"));
        meOutput.addWidget(new AEListGridWidget.Item(5, 20, 3, this.internalBuffer));
        group.addWidget(meOutput);

        return group;
    }

    @Override
    public void recoverItemsTable(ItemStack recoveryItems) {
        handler.insertInternal(recoveryItems,recoveryItems.getCount());
    }

    @Override
    public boolean isFrontFaceFree() {
        return recoveryChance != 0;
    }

    @Override
    public int gtolib$getRecoveryChance() {
        return recoveryChance;
    }

    static class Wrapper {
        public static final Map<Item, Integer> MUFFLER_HATCH;
        public static final Map<Item, Integer> AMPLIFIER_TIER_MAP;
        static {
            var mufflerMap = new HashMap<Item, Integer>();
            for (var i : GTMachines.MUFFLER_HATCH) {
                if(i!=null){
                    mufflerMap.put(i.getBlock().asItem(), i.getTier());
                }else{
                    GTOCore.LOGGER.error(new NullPointerException(Arrays.toString(GTMachines.MUFFLER_HATCH)).toString());
                }
            }
            MUFFLER_HATCH = mufflerMap;
            var amplifierTierMap = new HashMap<Item, Integer>();
            for (int i = GTValues.UV; i <= GTValues.OpV; ++i) {
                amplifierTierMap.put(((ItemStack) CraftingComponents.INTEGRATED_CONTROL_CORE.get(i)).getItem(), i);
            }
            AMPLIFIER_TIER_MAP=amplifierTierMap;
        }
    }
}
