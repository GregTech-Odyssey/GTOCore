package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.api.recipe.RecipeType;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class RadiationHatchPartMachine extends MultiblockPartMachine implements IMachineLife, IRecipeCapabilityHolder {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(RadiationHatchPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private final NotifiableItemStackHandler inventory;
    @Persisted
    private int radioactivity;
    @Persisted
    private int initialRadioactivity;
    @Persisted
    private int count;
    @Persisted
    private int time;
    @Persisted
    private int inhibitionDose;
    @Persisted
    private int initialTime;
    private final Map<IO, List<RecipeHandlerList>> capabilitiesProxy;
    private final Map<IO, Map<RecipeCapability<?>, List<IRecipeHandler<?>>>> capabilitiesFlat;
    private TickableSubscription radiationSubs;
    private final RecipeHandlerList currentHandlerList;

    public RadiationHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 1, IO.IN, IO.BOTH);
        this.capabilitiesProxy = new EnumMap<>(IO.class);
        this.capabilitiesFlat = new EnumMap<>(IO.class);
        currentHandlerList = RecipeHandlerList.of(IO.IN, inventory);
        addHandlerList(currentHandlerList);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        radiationSubs = subscribeServerTick(radiationSubs, this::checkRadiation);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (radiationSubs != null) {
            radiationSubs.unsubscribe();
            radiationSubs = null;
        }
    }

    private void checkRadiation() {
        if (time > 0) {
            if (count < 1) {
                radioactivity = initialRadioactivity * (initialTime + time) / (initialTime << 1);
            }
            time--;
        } else if (getOffsetTimer() % 20 == 0) {
            radioactivity = 0;
            GTRecipeType[] recipeTypes = getDefinition().getRecipeTypes();
            if (recipeTypes != null) {
                RecipeType recipeType = (RecipeType) recipeTypes[0];
                Recipe recipe = recipeType.lookup().findRecipe(this);
                if (recipe != null && RecipeRunner.handleRecipeIO(this, recipe, IO.IN, Collections.emptyMap())) {
                    count = inventory.storage.getStackInSlot(0).getCount();
                    initialRadioactivity = (int) ((recipe.data.getInt("radioactivity") - inhibitionDose) * (1 + ((double) count / 64)));
                    initialTime = recipe.duration * (inhibitionDose + 200) / 200;
                    time = initialTime;
                    radioactivity = initialRadioactivity;
                }
            }
        }
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117).setBackground(GuiTextures.DISPLAY).addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId())).addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150).clickHandler(this::handleDisplayClick)));
        var size = group.getSize();
        group.addWidget(new SlotWidget(inventory.storage, 0, size.width - 30, size.height - 30, true, true).setBackground(GuiTextures.SLOT));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private void addDisplayText(List<Component> textList) {
        textList.add(Component.translatable("gtocore.machine.radiation_hatch.inhibition_dose", inhibitionDose).append(ComponentPanelWidget.withButton(Component.literal(" [-]"), "Sub")).append(ComponentPanelWidget.withButton(Component.literal(" [+]"), "Add")));
        textList.add(Component.translatable("gtocore.recipe.radioactivity", radioactivity));
        textList.add(Component.translatable("gtocore.machine.radiation_hatch.time", time, initialTime));
    }

    private void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            inhibitionDose = Mth.clamp(inhibitionDose + ("Add".equals(componentData) ? 1 : -1), 0, 40);
        }
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(inventory.storage);
    }

    @Override
    public boolean canShared() {
        return false;
    }

    public int getRadioactivity() {
        return this.radioactivity;
    }

    @Override
    public @Nullable RecipeHandlerList getCurrentHandlerList() {
        return currentHandlerList;
    }

    @Override
    public void setCurrentHandlerList(RecipeHandlerList list, GTRecipe recipe) {}

    @Override
    public Map<IO, List<RecipeHandlerList>> getCapabilitiesProxy() {
        return this.capabilitiesProxy;
    }

    @Override
    public Map<IO, Map<RecipeCapability<?>, List<IRecipeHandler<?>>>> getCapabilitiesFlat() {
        return this.capabilitiesFlat;
    }
}
