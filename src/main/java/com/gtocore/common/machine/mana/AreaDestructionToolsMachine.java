package com.gtocore.common.machine.mana;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.utils.explosion.ChunkExplosion;
import com.gtolib.utils.explosion.CylinderExplosion;
import com.gtolib.utils.explosion.SphereExplosion;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AreaDestructionToolsMachine extends MetaMachine implements IFancyUIMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AreaDestructionToolsMachine.class, MetaMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableItemStackHandler inventory;

    private int model = 0;
    private int explosiveYield = 0;

    public AreaDestructionToolsMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 9, IO.NONE, IO.BOTH);
        inventory.addChangedListener(() -> {
            Level level = getLevel();
            if (level == null) return;
            model = 0;
            int explosiveEnergy = 0;
            explosiveYield = 0;

            for (int i = 0; i < inventory.getSlots(); i++) {
                var stack = inventory.getStackInSlot(i);
                var item = stack.getItem();
                if (item == GTItems.SHAPE_MOLD_BALL.asItem()) model = 1;
                else if (item == GTItems.SHAPE_MOLD_CYLINDER.asItem()) model = 2;
                else if (item == GTItems.SHAPE_MOLD_BLOCK.asItem()) model = 3;
                else if (item == GTBlocks.INDUSTRIAL_TNT.asItem()) explosiveEnergy += 30 * stack.getCount();
                else if (item == GTOBlocks.NUKE_BOMB.asItem()) explosiveEnergy += 2048 * stack.getCount();
                else if (item == GTOBlocks.NAQUADRIA_CHARGE.asItem()) explosiveEnergy += 3200 * stack.getCount();
                else if (item == GTOBlocks.LEPTONIC_CHARGE.asItem()) explosiveEnergy += 2048000 * stack.getCount();
                else if (item == GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem()) explosiveEnergy += 32000000 * stack.getCount();
            }

            if (model == 1) explosiveYield = (int) Math.cbrt((double) explosiveEnergy / 4) * 10;
            else if (model == 2) explosiveYield = (int) Math.sqrt((double) explosiveEnergy / level.getHeight()) * 18;
            else if (model == 3) explosiveYield = (int) Math.sqrt((double) explosiveEnergy / level.getHeight()) * 16;
        });
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inventory.notifyListeners();
    }

    private void triggerExplosion() {
        BlockPos pos = getPos();
        Level level = getLevel();
        if (level == null) return;

        if (model == 0) return;
        else if (model == 1) SphereExplosion.explosion(pos, level, explosiveYield, true, true, false);
        else if (model == 2) CylinderExplosion.explosion(pos, level, explosiveYield, true, true, false);
        else if (model == 3) ChunkExplosion.explosion(pos, level, explosiveYield, true, true, false);

        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    // 创建UI组件
    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117)
                .setBackground(GuiTextures.DISPLAY)
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150).clickHandler(this::handleDisplayClick)));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int slotIndex = y * 3 + x;
                group.addWidget(new SlotWidget(inventory, slotIndex, 133 + x * 18, 68 + y * 18, true, true).setBackground(GuiTextures.SLOT));
            }
        }

        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private void addDisplayText(List<Component> textList) {
        textList.add(Component.translatable("gtocore.machine.area_destruction_tools.detonate_instruction")
                .append(ComponentPanelWidget.withButton(Component.literal(" [\uD83D\uDCA5]"), "detonate")));

        textList.add(Component.translatable("gtocore.machine.area_destruction_tools.model." + model));
        textList.add(Component.translatable("gtocore.machine.area_destruction_tools.explosive_yield", explosiveYield));
    }

    private void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if ("detonate".equals(componentData)) {
                triggerExplosion();
            }
        }
    }
}
