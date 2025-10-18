package com.gtocore.common.machine.multiblock;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.capability.IExtendWirelessEnergyContainerHolder;
import com.gtolib.api.capability.IIWirelessInteractor;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.utils.BigIntegerUtils;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Scanned
public final class WirelessChargerMachine extends StorageMultiblockMachine implements IExtendWirelessEnergyContainerHolder {

    public static final Map<ResourceLocation, Set<WirelessChargerMachine>> NETWORK = new O2OOpenCacheHashMap<>();
    @RegisterLanguage(cn = "只为玩家提供能量", en = "Only Provide Energy to Player")
    public static final String ONLY_PROVIDE_TO_PLAYER = "gtocore.machine.only_provide_to_player";
    @RegisterLanguage(cn = "为多方块结构的舱室提供能量", en = "Provide Energy to Multiblock Hatch")
    public static final String PROVIDE_TO_MULTIBLOCK_PART = "gtocore.machine.provide_to_multiblock_part";
    @DynamicInitialValue(key = "wireless_charger.amount",
                         easyValue = "1",
                         normalValue = "16",
                         expertValue = "64",
                         typeKey = DynamicInitialValueTypes.KEY_AMOUNT,
                         cn = "力场发生器需求数量",
                         cnComment = """
                                 放入%s个对应等级的力场发生器，即可开启无线功能。
                                 等级越高，综合电流越大，FE物品速度无限。
                                 当等级大于MV时，开启机器后会自动添加到无线网络中。
                                 当等级大于HV时，机器为玩家的充电范围变为无限。
                                 机器内可查看为机器远程充电的距离和最大电流""",
                         en = "Field Generator Required Amount",
                         enComment = """
                                 Put %s Field Generator into the machine, and it will be enabled.
                                 The higher the level, the greater the total current, and the speed of FE items is infinite.
                                 When the level is greater than MV, the machine will be added to the wireless network.
                                 When the level is greater than HV, the machine's range is infinite.
                                 You can view the charging range and maximum current for remote charging in the machine.""")
    private static int amount = 16;
    private int range;
    private int rate;
    private boolean infinite;
    private boolean machine;
    private WirelessEnergyContainer WirelessEnergyContainerCache;
    @Persisted
    public boolean isOnlyProvideToPlayer = false;
    @Persisted
    public boolean isProvideToMultiBlockHatch = false;
    @Persisted
    private boolean isw;

    public WirelessChargerMachine(MetaMachineBlockEntity holder) {
        super(holder, amount, i -> Wrapper.GENERATOR.containsKey(i.getItem()));
        setWorkingEnabled(false);
    }

    @Override
    public boolean hasOverclockConfig() {
        return false;
    }

    @Override
    public boolean hasBatchConfig() {
        return false;
    }

    @Override
    public boolean isRecipeLogicAvailable() {
        return false;
    }

    @Override
    public void onMachineChanged() {
        machine = false;
        infinite = false;
        rate = 0;
        range = 0;
        var stack = getStorageStack();
        if (stack.getCount() == amount) {
            var tier = Wrapper.GENERATOR.get(stack.getItem());
            if (tier != null) {
                getRecipeLogic().setStatus(RecipeLogic.Status.WORKING);
                if (tier > 2) machine = true;
                if (tier > 3) infinite = true;
                rate = 10 * tier;
                range = 8 * (2 << tier);
            } else if (getRecipeLogic().isWorking()) {
                getRecipeLogic().setStatus(RecipeLogic.Status.IDLE);
            }
        }
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        MultiblockDisplayText.builder(textList, isFormed()).setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive()).addEnergyUsageLine(getEnergyContainer());
        if (isFormed()) {
            var count = getStorageStack().getCount();
            if (count != amount) {
                textList.add(Component.translatable("gui.ae2.Missing", amount - count));
            }
            textList.add(Component.translatable("gtocore.machine.generator_array.wireless").append(ComponentPanelWidget.withButton(Component.literal("[").append(isw ? Component.translatable("gtocore.machine.on") : Component.translatable("gtocore.machine.off")).append(Component.literal("]")), "wireless_switch")));
            textList.add(Component.translatable(ONLY_PROVIDE_TO_PLAYER).append(ComponentPanelWidget.withButton(Component.literal("[").append(isOnlyProvideToPlayer ? Component.translatable("gtocore.machine.on") : Component.translatable("gtocore.machine.off")).append(Component.literal("]")), "wireless_switch_player")));
            textList.add(Component.translatable(PROVIDE_TO_MULTIBLOCK_PART).append(ComponentPanelWidget.withButton(Component.literal("[").append(isProvideToMultiBlockHatch ? Component.translatable("gtocore.machine.on") : Component.translatable("gtocore.machine.off")).append(Component.literal("]")), "wireless_switch_part")));
            textList.add(Component.translatable("gtceu.recipe.amperage", (double) rate / 20));
            textList.add(Component.translatable("gui.ae2.WirelessRange", range));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if ("wireless_switch".equals(componentData)) {
                isw = !isw;
            }
            if ("wireless_switch_player".equals(componentData)) {
                isOnlyProvideToPlayer = !isOnlyProvideToPlayer;
            }
            if ("wireless_switch_part".equals(componentData)) {
                isProvideToMultiBlockHatch = !isProvideToMultiBlockHatch;
            }
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        IIWirelessInteractor.addToNet(NETWORK, this);
        onMachineChanged();
    }

    @Override
    public void onStructureInvalid() {
        IIWirelessInteractor.removeFromNet(NETWORK, this);
        super.onStructureInvalid();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        IIWirelessInteractor.removeFromNet(NETWORK, this);
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        if (isWorkingAllowed && isFormed()) {
            var level = getLevel();
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.getServer().tell(new TickTask(1, this::onMachineChanged));
            }
            IIWirelessInteractor.addToNet(NETWORK, this);
        } else {
            IIWirelessInteractor.removeFromNet(NETWORK, this);
        }
        super.setWorkingEnabled(isWorkingAllowed);
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
    }

    public long getEnergyStored() {
        if (isw) {
            var container = getWirelessEnergyContainer();
            if (container != null) {
                return BigIntegerUtils.getLongValue(container.getStorage());
            }
            return 0;
        }
        return getEnergyContainer().getEnergyStored();
    }

    public void removeEnergy(long energy) {
        if (isw) {
            var container = getWirelessEnergyContainer();
            if (container != null) {
                container.unrestrictedRemoveEnergy(energy);
            }
        } else {
            getEnergyContainer().removeEnergy(energy);
        }
    }

    private static class Wrapper {

        private static final Map<Item, Integer> GENERATOR = Map.of(GTItems.FIELD_GENERATOR_LV.get(), 1, GTItems.FIELD_GENERATOR_MV.get(), 2, GTItems.FIELD_GENERATOR_HV.get(), 3, GTItems.FIELD_GENERATOR_EV.get(), 4, GTItems.FIELD_GENERATOR_IV.get(), 5, GTItems.FIELD_GENERATOR_LuV.get(), 6, GTItems.FIELD_GENERATOR_ZPM.get(), 7, GTItems.FIELD_GENERATOR_UV.get(), 8);
    }

    public static void setAmount(final int amount) {
        WirelessChargerMachine.amount = amount;
    }

    public int getRange() {
        return this.range;
    }

    public int getRate() {
        return this.rate;
    }

    public boolean isInfinite() {
        return this.infinite;
    }

    public boolean isMachine() {
        return this.machine;
    }

    public void setWirelessEnergyContainerCache(final WirelessEnergyContainer WirelessEnergyContainerCache) {
        this.WirelessEnergyContainerCache = WirelessEnergyContainerCache;
    }

    public WirelessEnergyContainer getWirelessEnergyContainerCache() {
        return this.WirelessEnergyContainerCache;
    }
}
