package com.gtocore.api.placeholder;

import java.util.List;

/**
 * 具有外部配置源的通用占位符接口。
 * 配置数据（如 NBT）必须从外部 Source 对象 S 中读取。
 * <p>
 * T 占位符所代表的目标对象的类型。
 * C 获取当前目标时所需的上下文对象的类型。
 * S 携带占位符配置信息的源对象类型 (例如 ItemStack)。
 */
public interface IPlaceholder<T, S, C> {

    /**
     * 从源对象中读取配置，并返回此占位符应显示的目标对象列表的列表。
     *
     * @param source 携带配置数据（如 NBT）的源对象。
     * @return 目标对象列表的列表，如果未配置则返回空列表。
     */
    List<List<T>> getTargetLists(S source);

    /**
     * 根据给定的源对象和上下文，返回此占位符当前应显示的目标对象。
     *
     * @param source  携带配置数据（如 NBT）的源对象。
     * @param context 包含所有相关信息的上下文对象。
     * @return 当前活跃的目标对象，如果无法确定或未配置则返回 null。
     */
    T getCurrentTarget(S source, C context);
}
