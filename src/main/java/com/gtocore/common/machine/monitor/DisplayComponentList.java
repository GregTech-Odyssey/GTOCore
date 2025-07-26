package com.gtocore.common.machine.monitor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DisplayComponentList extends ArrayList<IDisplayComponent> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ImmutableList<ResourceLocation> availableOrderedIds;
    private final List<ResourceLocation> remainingOrderedIds = new ArrayList<>();

    public DisplayComponentList(List<ResourceLocation> availableOrderedIds) {
        this.availableOrderedIds = ImmutableList.copyOf(availableOrderedIds);
        remainingOrderedIds.addAll(availableOrderedIds);
    }

    /**
     * 添加一个显示组件到列表中。
     *
     * @param component 要添加的显示组件
     */
    public void addIfAbsent(ResourceLocation id, IDisplayComponent component) {
        if (component == null || id == null) {
            return; // 如果组件或ID为null，则不添加
        }
        if (remainingOrderedIds.remove(id)) {
            // 如果ID在remainingOrderedIds中，直接添加
            this.add(component);
        }
    }

    public void addIfAbsent(ResourceLocation id, FormattedCharSequence text) {
        addIfAbsent(id, DisplayComponent.text(id, text));
    }

    public @NotNull DisplayComponentList sortInner() {
        // 将自己的内容按照availableOrderedIds中的顺序进行排序
        this.sort(idSorter());
        return this;
    }

    private Comparator<IDisplayComponent> idSorter() {
        // 按照ID在availableOrderedIds中的顺序进行排序
        return (o1, o2) -> {
            int index1 = availableOrderedIds.indexOf(o1.getId());
            int index2 = availableOrderedIds.indexOf(o2.getId());
            if (index1 == -1 || index2 == -1) {
                return 0; // 如果没有找到ID，则不排序
            }
            return Integer.compare(index1, index2);
        };
    }
}
