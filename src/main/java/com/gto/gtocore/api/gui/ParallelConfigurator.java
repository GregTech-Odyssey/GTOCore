package com.gto.gtocore.api.gui;

import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;

import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

public final class ParallelConfigurator implements IFancyConfigurator {

    private final IParallelMachine machine;

    public ParallelConfigurator(IParallelMachine machine) {
        this.machine = machine;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gtceu.machine.parallel_hatch.display");
    }

    @Override
    public IGuiTexture getIcon() {
        return GTOGuiTextures.PARALLEL_CONFIG;
    }

    @Override
    public Widget createConfigurator() {
        WidgetGroup group = new WidgetGroup(0, 0, 100, 20);
        group.addWidget(new IntInputWidget(machine::getParallel, machine::setParallel) {

            @Override
            public void writeInitialData(FriendlyByteBuf buffer) {
                super.writeInitialData(buffer);
                buffer.writeVarInt(machine.getMaxParallel());
                setMax(machine.getMaxParallel());
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public void readInitialData(FriendlyByteBuf buffer) {
                super.readInitialData(buffer);
                setMax(buffer.readVarInt());
            }

            @Override
            public void detectAndSendChanges() {
                super.detectAndSendChanges();
                int newMaxParallel = machine.getMaxParallel();
                if (newMaxParallel != getMax()) {
                    setMax(newMaxParallel);
                    writeUpdateInfo(0, buf -> buf.writeVarInt(newMaxParallel));
                }
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
                super.readUpdateInfo(id, buffer);
                if (id == 0) {
                    setMax(buffer.readVarInt());
                }
            }
        }.setMin(0));
        return group;
    }
}
