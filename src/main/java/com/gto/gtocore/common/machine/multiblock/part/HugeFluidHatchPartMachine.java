package com.gto.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;

import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HugeFluidHatchPartMachine extends FluidHatchPartMachine {

    public HugeFluidHatchPartMachine(IMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io, 0, tier, args);
    }

    @Override
    protected NotifiableFluidTank createTank(long initialCapacity, int slots, Object... args) {
        return new NotifiableFluidTank(this, slots, Integer.MAX_VALUE, io);
    }

    @Override
    public Widget createUIWidget() {
        if (tier == 1) {
            return super.createSingleSlotGUI();
        }
        int rowSize = 1;
        int colSize = 1;

        for (int i = tier; i > 1; i--) {
            if (tier % i == 0) {
                rowSize = i;
                colSize = tier / i;
            }
        }
        var group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * colSize + 16);
        var container = new WidgetGroup(4, 4, 18 * rowSize + 8, 18 * colSize + 8);

        int index = 0;
        for (int y = 0; y < colSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                if (index >= tier) break;
                container.addWidget(
                        new TankWidget(tank.getStorages()[index++], 4 + x * 18, 4 + y * 18, true, io.support(IO.IN))
                                .setBackground(GuiTextures.FLUID_SLOT));
            }
        }

        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(container);

        return group;
    }
}
