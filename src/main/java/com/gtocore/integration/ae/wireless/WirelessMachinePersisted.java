package com.gtocore.integration.ae.wireless;

import net.minecraft.nbt.CompoundTag;

import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class WirelessMachinePersisted implements ITagSerializable<CompoundTag>, IContentChangeAware {

    private final WirelessMachine machine;

    Runnable onContentChanged = null;
    String gridConnectedName = "";
    private boolean beSet = false; // 是否已经设置过网格连接

    public WirelessMachinePersisted(WirelessMachine machine) {
        this.machine = machine;
    }

    public void setGridConnectedName(String value) {
        if (!Objects.equals(this.gridConnectedName, value)) {
            this.gridConnectedName = value;
            if (onContentChanged != null) onContentChanged.run();
            // 保持与原 Kotlin 行为：变更后请求同步
            machine.self().requestSync();
        }
    }

    public void setBeSet(boolean value) {
        if (this.beSet != value) {
            this.beSet = value;
            if (onContentChanged != null) onContentChanged.run();
            // 保持与原 Kotlin 行为：变更后请求同步
            machine.self().requestSync();
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("gridName", gridConnectedName);
        tag.putBoolean("beSet", beSet);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt != null) {
            // 使用 setter 以便触发变更通知与同步逻辑（与 Kotlin 行为一致）
            setGridConnectedName(nbt.getString("gridName"));
            setBeSet(nbt.getBoolean("beSet"));
        }
    }

    @Override
    public void setOnContentsChanged(Runnable aa) {
        this.onContentChanged = aa;
    }

    @Override
    public Runnable getOnContentsChanged() {
        return onContentChanged;
    }
}
