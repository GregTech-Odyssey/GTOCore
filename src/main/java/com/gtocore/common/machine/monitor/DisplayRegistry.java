package com.gtocore.common.machine.monitor;

import com.gtolib.GTOCore;

import net.minecraft.resources.ResourceLocation;

import static com.gtocore.data.lang.LangHandler.addCNEN;

public enum DisplayRegistry {

    OWNER("owner", "所有者", "Owner"),

    MANA_CURRENT("mana_current", "当前魔力", "Current Mana"),
    MANA_CURRENT_AS_POOL("mana_current_as_pool", "当前魔力（以池计算）", "Current Mana (as Pool)"),
    MANA_CHANGE("mana_change", "魔力变化", "Mana Change"),
    MANA_CHANGE_AS_POOL("mana_change_as_pool", "魔力变化（以池计算）", "Mana Change (as Pool)"),
    MANA_REMAINING_TIME("mana_remaining_time", "剩余耗尽时间", "Remaining Time Until Depletion"),

    TOTAL_ENERGY("total_energy", "总能量", "Total Energy"),
    ENERGY_TRANSFER_LIMIT("energy_transfer_limit", "能量传输上限", "Energy Transfer Limit"),
    ENERGY_STAT_TITLE("energy_stat_title", "能量统计标题", "Energy Statistics Title"),
    ENERGY_STAT_MINUTE("energy_stat_minute", "能量统计（上一分钟）", "Energy Statistics (Last Minute)"),
    ENERGY_STAT_HOUR("energy_stat_hour", "能量统计（上一小时）", "Energy Statistics (Last Hour)"),
    ENERGY_STAT_DAY("energy_stat_day", "能量统计（上一天）", "Energy Statistics (Last Day)"),
    ENERGY_STAT_NOW("energy_stat_now", "能量统计（当前）", "Energy Statistics (Now)"),
    ENERGY_STAT_REMAINING_TIME("energy_stat_remaining_time", "能量统计（剩余时间）", "Energy Statistics (Remaining Time)"),
    ENERGY_STAT_BOUND_INFO("energy_stat_bound_info", "能源塔绑定信息", "Energy Tower Binding Information"),
    EU_STATUS_BAR("eu_status_bar", "电网存量条", "Energy Grid Status Bar"),

    COMPUTATION_WORK("computation_work", "算力使用情况", "Computation Work Usage"),
    COMPUTATION_WORK_USED("computation_work_used", "算力使用量", "Computation Work Used"),
    COMPUTATION_WORK_UNAVAILABLE("computation_work_unavailable", "算力容器不可用信息", "Computation Work Container Unavailable Information"),

    AE_ERROR("ae_error", "ME监视器错误信息", "ME Monitor Error Information"),

    AE_STATUS_0("ae_status", "监视的物品信息", "Monitored Item Information"),
    AE_STATUS_1("ae_status1", "监视的流体信息", "Monitored Fluid Information"),
    AE_AMOUNT_0("ae_amount", "物品总量统计", "Item Amount"),
    AE_AMOUNT_1("ae_amount1", "流体总量统计", "Fluid Amount"),
    AE_STAT_TITLE("ae_stat_title", "统计标题", "Statistics Title"),
    AE_STAT_MINUTE_0("ae_stat_minute", "上一分钟物品统计", "Last Minute Item Statistics"),
    AE_STAT_MINUTE_1("ae_stat_minute1", "上一分钟流体统计", "Last Minute Fluid Statistics"),
    AE_STAT_HOUR_0("ae_stat_hour", "上一小时物品统计", "Last Hour Item Statistics"),
    AE_STAT_HOUR_1("ae_stat_hour1", "上一小时流体统计", "Last Hour Fluid Statistics"),
    AE_STAT_DAY_0("ae_stat_day", "上一天物品统计", "Last Day Item Statistics"),
    AE_STAT_DAY_1("ae_stat_day1", "上一天流体统计", "Last Day Fluid Statistics"),
    AE_STAT_REMAINING_TIME_0("ae_stat_remaining_time", "剩余时间统计（物品）", "Remaining Time Statistics (Item)"),
    AE_STAT_REMAINING_TIME_1("ae_stat_remaining_time1", "剩余时间统计（流体）", "Remaining Time Statistics (Fluid)"),

    AE_CPU_MONITORED("ae_cpu_monitored", "监视的CPU信息", "Monitored CPU Information"),
    AE_CPU_USAGE("ae_cpu_usage", "CPU使用率", "CPU Usage"),
    AE_CPU_CAPACITY("ae_cpu_capacity", "CPU容量", "CPU Capacity"),
    AE_CPU_MODE("ae_cpu_mode", "CPU模式", "CPU Mode"),
    AE_CPU_CURRENT_CRAFTING("ae_cpu_current_crafting", "当前正在制作的物品", "Currently Crafting Item"),
    AE_CPU_CURRENT_PROGRESS("ae_cpu_current_progress", "当前制作进度", "Current Crafting Progress"),

    CUSTOM_DISPLAY("custom_display", "自定义显示", "Custom Display"),

    MACHINE_NAME("machine_name", "机器名", "Machine Name"),
    MACHINE_ENERGY("machine_energy", "能量", "Energy"),
    MACHINE_PROGRESS("machine_progress", "工作进度", "Progress"),
    MACHINE_RECIPE_LOGIC_EU("machine_recipe_logic_eu", "能耗·产能(EU)", "EU Consumption/Production"),
    MACHINE_RECIPE_LOGIC_MANA("machine_recipe_logic_mana", "能耗·产能(Mana)", "Mana Consumption/Production"),
    MACHINE_RECIPE_OUTPUT("machine_recipe_output", "配方产物", "Recipe Output"),
    MACHINE_RECIPE_OUTPUT_ITEM_1("machine_recipe_output_item1", "配方产物1(物品)", "Recipe Output #1 (Item)"),
    MACHINE_RECIPE_OUTPUT_ITEM_2("machine_recipe_output_item2", "配方产物2(物品)", "Recipe Output #2 (Item)"),
    MACHINE_RECIPE_OUTPUT_ITEM_3("machine_recipe_output_item3", "配方产物3(物品)", "Recipe Output #3 (Item)"),
    MACHINE_RECIPE_OUTPUT_FLUID_1("machine_recipe_output_fluid1", "配方产物1(流体)", "Recipe Output #1 (Fluid)"),
    MACHINE_RECIPE_OUTPUT_FLUID_2("machine_recipe_output_fluid2", "配方产物2(流体)", "Recipe Output #2 (Fluid)"),
    MACHINE_RECIPE_OUTPUT_FLUID_3("machine_recipe_output_fluid3", "配方产物3(流体)", "Recipe Output #3 (Fluid)"),
    MACHINE_MANTENANCE("machine_mantenance", "维护状态", "Mantenance Status");

    private final ResourceLocation id;
    private final String langCN;
    private final String langEN;

    DisplayRegistry(String id, String langCN, String langEN) {
        this.id = GTOCore.id(id);
        this.langCN = langCN;
        this.langEN = langEN;
    }

    public ResourceLocation id() {
        return id;
    }

    public static String langKey(ResourceLocation id) {
        return "gtocore.machine.monitor.display_key." + id.getNamespace() + "." + id.getPath();
    }

    // activates on runData task
    public static void registerLanguage() {
        for (DisplayRegistry displayRegistry : DisplayRegistry.values()) {
            addCNEN(langKey(displayRegistry.id), displayRegistry.langCN, displayRegistry.langEN);
        }
    }
}
