package com.gtocore.mixin.ae2.crafting;

import com.gtocore.api.ae2.crafting.ICraftingPlanAllocationAccessor;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEKey;
import appeng.crafting.CraftingPlan;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CraftingPlan.class)
public class CraftingPlanMixin implements ICraftingPlanAllocationAccessor {

    @Unique
    private long gtocore$primaryOutputThreshold = 0; // 当主产物接受次数积累到>此值，即可输出到ae，例如为3，那么当主产物积累到4个时就会输出1个
    @Unique
    private long gtocore$primaryOutputThreatAccumulate = 0; // 主产物接受次数积累
    @Unique
    private long gtocore$primaryOutputThreatAccumulateThreshold = 0; // 发送次数

    @Unique
    private Reference2ObjectOpenHashMap<AEKey, Object2LongOpenHashMap<IPatternDetails>> gtocore$allocations; // 某个物品对某些样板优先输出的次数
    // 表示某个AEKey的优先样板列表，key为样板，value为此样板可用的优先数量
    // 如果AEKey对应的优先样板列表为空，则表示此物品所有样板都可以使用
    // 如果AEKey对应的优先样板列表不为空或者Object2LongOpenHashMap<IPatternDetails>每个项目都<=0，则表示此物品只能优先用于这些样板，可优先发配数量为value
    // 请注意，如果优先数量为100，发配给此样板的数量为200，则表示此次发配此物品最大数量为100，如果样板需要8个此物品，则最多发配十二个单位的样板，等待下一次推送样板，如果没有优先样板，则系统会正常工作
    // 如果只有一个优先样板，则其他样板不允许使用此物品（正常逻辑），此优先样板如果优先数量为100，需要8个此物品，则优先单位不限制（y）

    @Unique
    @Override
    public Reference2ObjectOpenHashMap<AEKey, Object2LongOpenHashMap<IPatternDetails>> getGtocore$allocations() {
        return gtocore$allocations;
    }

    @Unique
    public void setGtocore$allocations(Reference2ObjectOpenHashMap<AEKey, Object2LongOpenHashMap<IPatternDetails>> allocations) {
        this.gtocore$allocations = allocations;
    }

    @Unique
    public long gtocore$getPrimaryOutputThreat() {
        return gtocore$primaryOutputThreshold;
    }

    @Unique
    public void gtocore$setPrimaryOutputThreat(long threat) {
        this.gtocore$primaryOutputThreshold = threat;
    }

    @Unique
    public long gtocore$getPrimaryOutputThreatAccumulate() {
        return gtocore$primaryOutputThreatAccumulate;
    }

    @Unique
    public void gtocore$setPrimaryOutputThreatAccumulate(long threat) {
        this.gtocore$primaryOutputThreatAccumulate = threat;
    }

    @Unique
    public long gtocore$getPrimaryOutputThreatAccumulateThreshold() {
        return gtocore$primaryOutputThreatAccumulateThreshold;
    }

    @Unique
    public void gtocore$setPrimaryOutputThreatAccumulateThreshold(long threat) {
        this.gtocore$primaryOutputThreatAccumulateThreshold = threat;
    }
}
