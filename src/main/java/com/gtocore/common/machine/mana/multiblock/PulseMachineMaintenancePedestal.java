package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.common.machine.mana.part.PulseMachineMaintenanceCore;
import com.gtocore.integration.botania.IClientPylon;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.feature.multiblock.IDroneControlCenterMachine;
import com.gtolib.api.machine.impl.part.DroneHatchPartMachine;
import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.misc.Drone;
import com.gtolib.api.network.NetworkPack;
import com.gtolib.utils.ClientUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

@DataGeneratorScanned
public class PulseMachineMaintenancePedestal extends NoEnergyMultiblockMachine implements IDroneControlCenterMachine {

    public static final int RANGE_RADIUS = 12;
    public static final NetworkPack PEDESTAL_PARTICLE_PACK = NetworkPack.registerS2C("gtocore:pedestal_particle",
            (vars, buf) -> {
                buf.writeBlockPos((BlockPos) vars[1]); // from pos
                buf.writeVector3f(((Vec3) vars[2]).toVector3f()); // to vec
                buf.writeInt((int) vars[3]); // color
            },
            (player, buf) -> {
                var level = player.level();
                BlockPos from = buf.readBlockPos();
                double timeOffset = level.dayTime();
                var toF = buf.readVector3f();
                Vec3 to = new Vec3(toF.x(), toF.y(), toF.z());
                int color = buf.readInt();
                for (int i = 0; i < 20; i++) {
                    IClientPylon.particle(from, timeOffset, to, level, color);
                }
            });

    private final Reference2ObjectMap<MetaMachine, Runnable> problems = new Reference2ObjectOpenHashMap<>();
    private PulseMachineMaintenanceCore core;
    @SaveToDisk
    private int totalResolvedProblems = 0;

    public PulseMachineMaintenancePedestal(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public List<DroneHatchPartMachine> getDroneHatchPartMachine() {
        return List.of();
    }

    @Override
    public @Nullable Drone getFirstUsableDrone(BlockPos pos, Predicate<Drone> predicate) {
        return null;
    }

    public void resolveRandomProblem() {
        if (problems.isEmpty() || getLevel() == null) return;
        var randomEntry = problems.reference2ObjectEntrySet().stream().skip((int) (problems.size() * getLevel().random.nextFloat())).findFirst();
        randomEntry.ifPresent(entry -> {
            entry.getValue().run();
            totalResolvedProblems++;
            PEDESTAL_PARTICLE_PACK.send(
                    getLevel().getEntities((Entity) null, new AABB(getPos()).inflate(64), e -> e instanceof ServerPlayer),
                    getPos(), new Vec3(getPos().getX() + 0.5, getPos().getY() + 3, getPos().getZ() + 0.5), 0x00FF00);
        });
    }

    @Override
    public boolean isActiveState() {
        return true;
    }

    public void addProblem(MetaMachine machine, Runnable resolution) {
        problems.put(machine, resolution);
    }

    public void removeProblem(MetaMachine machine) {
        problems.remove(machine);
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof PulseMachineMaintenanceCore c) {
            this.core = c;
            core.setPedestal(this);
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        IIWirelessInteractor.addToNet(this, IDroneControlCenterMachine.class);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        problems.clear();
        core = null;
        IIWirelessInteractor.removeFromNet(this);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        problems.clear();
        core = null;
        IIWirelessInteractor.removeFromNet(this);
    }

    public boolean inRange(BlockPos pos) {
        if (core == null) return false;
        return pos.distSqr(core.getPos()) <= RANGE_RADIUS * RANGE_RADIUS;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable(TIMES, totalResolvedProblems).withStyle(ChatFormatting.GREEN));
        textList.add(ComponentPanelWidget.withButton(
                Component.translatable("gtocore.digital_miner.show_range").append("(" + RANGE_RADIUS + ")"),
                "show"));
    }

    @Override
    public void handleDisplayClick(@NotNull String componentData, ClickData clickData) {
        if (clickData.isRemote && componentData.equals("show")) {
            ClientUtil.highlighting(getPos().above(5), RANGE_RADIUS);
        }
    }

    @RegisterLanguage(cn = "总分发维护用魔力：%s 次", en = "Total mana used for maintenance: %s times")
    public static final String TIMES = "gtocore.pulse_machine_maintenance.times";
}
