package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.api.machine.IGTOMufflerMachine;
import com.gtocore.data.CraftingComponents;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.trait.InaccessibleInfiniteHandler;
import com.gtolib.api.misc.AsyncTask;
import com.gtolib.api.misc.IAsyncTaskHolder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.transfer.item.SingleCustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNodeListener;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@DataGeneratorScanned
public class MEMufflerHatchPartMachine extends MEPartMachine implements IGTOMufflerMachine, IAsyncTaskHolder {

    @Persisted
    private final KeyStorage internalBuffer;
    @Persisted
    private final NotifiableItemStackHandler mufflerHatchInv;
    @Persisted
    private final NotifiableItemStackHandler amplifierInv;
    private final InaccessibleInfiniteHandler handler;

    @DescSynced
    private int recoveryChance = 0;

    private AsyncTask asyncTask;

    private int muffler_tier = 0;

    private static final int COUNT = 1 << (GTOCore.difficulty << 1);
    private static final int MIN_COUNT = 1 << ((GTOCore.difficulty << 1) - 2);

    public MEMufflerHatchPartMachine(@NotNull MetaMachineBlockEntity holder) {
        super(holder, IO.NONE);
        internalBuffer = new KeyStorage();
        handler = new InaccessibleInfiniteHandler(this, internalBuffer);
        mufflerHatchInv = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH, SingleCustomItemStackHandler::new);
        mufflerHatchInv.setFilter(stack -> Wrapper.MUFFLER_HATCH.containsKey(stack.getItem()));
        mufflerHatchInv.addChangedListener(this::onMufflerChange);
        amplifierInv = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH) {

            @Override
            public int getSlotLimit(int slot) {
                return COUNT;
            }
        };
        amplifierInv.setFilter(stack -> Wrapper.AMPLIFIER_TIER_MAP.containsKey(stack.getItem()));
        amplifierInv.addChangedListener(this::onMufflerChange);
    }

    private void onMufflerChange() {
        var amplifierIs = amplifierInv.getStackInSlot(0);
        var item = mufflerHatchInv.getStackInSlot(0).getItem();
        recoveryChance = 0;
        muffler_tier = tier;
        if (Wrapper.MUFFLER_HATCH.containsKey(item)) {
            muffler_tier = Wrapper.MUFFLER_HATCH.get(item);
        }
        if (Objects.equals(Wrapper.AMPLIFIER_TIER_MAP.get(amplifierIs.getItem()), Wrapper.MUFFLER_HATCH.get(item))) {
            var recoveryChanceMin = muffler_tier * 10;
            var recoveryChanceMax = recoveryChanceMin * muffler_tier;
            recoveryChance = (recoveryChanceMax - recoveryChanceMin) * (amplifierIs.getCount() - MIN_COUNT) / (COUNT - MIN_COUNT);
            recoveryChance += recoveryChanceMin;
            recoveryChance = Math.max(recoveryChance, recoveryChanceMin);
        } else {
            recoveryChance = muffler_tier * 10;
        }
    }

    @Override
    protected @NotNull RecipeHandlerList getHandlerList() {
        return RecipeHandlerList.NO_DATA;
    }

    @Override
    public AsyncTask getAsyncTask() {
        return asyncTask;
    }

    @Override
    public void setAsyncTask(AsyncTask task) {
        asyncTask = task;
    }

    @Override
    public void gtolib$insertAsh(MultiblockControllerMachine controller, GTRecipe lastRecipe) {
        AsyncTask.addAsyncTask(this, () -> IGTOMufflerMachine.super.gtolib$insertAsh(controller, lastRecipe));
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
        onMufflerChange();
    }

    @Override
    public boolean canShared() {
        return false;
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
        clearInventory(mufflerHatchInv);
        clearInventory(amplifierInv);
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.superAttachConfigurators(configuratorPanel);
    }

    @RegisterLanguage(cn = "放入消声仓", en = "Insert a Muffler Hatch")
    private static final String MUFFLER_TOOLTIP_KEY = "gtocore.machine.me_muffler_part.muffler_tooltip";
    @RegisterLanguage(cn = "放入相同等级的消声仓以启用", en = "Insert a Muffler Hatch of the same level to enable")
    private static final String MUFFLER_TOOLTIP_KEY_EXPERT = "gtocore.machine.me_muffler_part.muffler_tooltip_expert";
    @RegisterLanguage(cn = "放入相同等级的集控核心以增幅概率", en = "Insert a Control Core of the same level to increase the probability")
    private static final String AMPLIFIER_TOOLTIP_KEY = "gtocore.machine.me_muffler_part.apm_tooltip";

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(0, 0, 170, 110);
        WidgetGroup muffler = new WidgetGroup(0, 0, 170, 25);
        muffler.addWidget(new SlotWidget(mufflerHatchInv.storage, 0, 140, 10, true, true)
                .setBackground(GuiTextures.SLOT)
                .setHoverTooltips(Component.translatable(GTOCore.isExpert() ? MUFFLER_TOOLTIP_KEY_EXPERT : MUFFLER_TOOLTIP_KEY)));
        muffler.addWidget(new SlotWidget(amplifierInv.storage, 0, 120, 10, true, true)
                .setBackground(GuiTextures.SLOT)
                .setHoverTooltips(Component.translatable(AMPLIFIER_TOOLTIP_KEY)));
        muffler.addWidget(new ComponentPanelWidget(6, 15, (list) -> list.add(Component.translatable("gtceu.muffler.recovery_tooltip", recoveryChance))));
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
        handler.insertInternal(recoveryItems, recoveryItems.getCount());
    }

    @Override
    public boolean isFrontFaceFree() {
        return recoveryChance != 0;
    }

    @Override
    public int gtolib$getRecoveryChance() {
        return recoveryChance;
    }

    @Override
    public int getTier() {
        return Math.max(tier, muffler_tier);
    }

    static class Wrapper {

        public static final Map<Item, Integer> MUFFLER_HATCH;
        public static final Map<Item, Integer> AMPLIFIER_TIER_MAP;
        static {
            var mufflerMap = new HashMap<Item, Integer>();
            for (var i : GTMachines.MUFFLER_HATCH) {
                if (i != null && i.getTier() >= GTValues.LuV) {
                    mufflerMap.put(i.getBlock().asItem(), i.getTier());
                }
            }
            MUFFLER_HATCH = Map.copyOf(mufflerMap);
            var amplifierTierMap = new HashMap<Item, Integer>();
            for (int i = GTValues.UV; i <= GTValues.OpV; ++i) {
                amplifierTierMap.put(((ItemStack) CraftingComponents.INTEGRATED_CONTROL_CORE.get(i)).getItem(), i);
            }
            AMPLIFIER_TIER_MAP = Map.copyOf(amplifierTierMap);
        }
    }
}
