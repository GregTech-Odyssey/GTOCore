package com.gtocore.api.ae2.crafting;

import com.gtolib.api.ae2.machine.CraftingInterfacePartMachine;

import net.minecraft.network.chat.Component;

import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.helpers.MachineSource;
import appeng.util.ConfigManager;

public interface ICraftingCPUCluster {

    ConfigManager getConfigManager();

    void setName(Component name);

    void setDestroyed(boolean destroyed);

    void setStorage(long storage);

    void setMachineSrc(MachineSource src);

    void setAccelerator(int accelerator);

    void setMachine(CraftingInterfacePartMachine machine);

    static ICraftingCPUCluster of(CraftingCPUCluster cluster) {
        return (ICraftingCPUCluster) (Object) cluster;
    }

    static CraftingCPUCluster create(CraftingInterfacePartMachine machine, MachineSource src, Component name, long storage, int accelerator) {
        var cluster = new CraftingCPUCluster(machine.getPos(), machine.getPos());
        var cl = of(cluster);
        cl.setMachine(machine);
        cl.setMachineSrc(src);
        cl.setName(name);
        cl.setStorage(storage);
        cl.setAccelerator(accelerator);
        return cluster;
    }
}
