# ItemIconReport - 游戏资源导出系统

导出 Minecraft 模组中全量资源（图标、标签、配方、元数据），用于构建配方模拟器。

## 输出结构

```
logs/report/GTOCore-{version}/all_{timestamp}/
├── _master_report.json                  # 总体统计
├── item/{modid}/{path}.png              # 物品图标 64x64 PNG (FBO)
├── fluid/{modid}/{path}.png             # 流体图标 64x64 PNG (着色tint)
├── block/{modid}/{path}.png             # 方块图标 64x64 PNG (FBO)
├── tag/
│   ├── item/{tag_name}.json             # 物品标签
│   ├── fluid/{tag_name}.json            # 流体标签
│   └── block/{tag_name}.json            # 方块标签
├── recipe/
│   ├── gregtech/{type}.json             # GT配方 (按类型分文件)
│   └── vanilla/{type}.json              # 原版配方 (按类型分文件)
├── list/
│   ├── emi_stacks.json                  # EMI全量清单 (含tooltip)
│   ├── items.json                       # 物品元数据 (含tooltip)
│   ├── fluids.json                      # 流体元数据
│   └── blocks.json                      # 方块元数据
└── misc/
    ├── gt_environments.json             # GT环境 (超净间/真空/重力/电压)
    ├── gt_machines.json                 # GT机器+多方块元数据
    └── recipe_type_machines.json        # 配方类型→机器映射
```

## Schema 定义

### list/emi_stacks.json
EMI 索引的全量资源清单，基于 `EmiApi.getIndexStacks()` 收集。

```
{
  type: "emi_stacks",
  count: int,
  stacks: [
    {
      id: string,                        // "minecraft:stone"
      type: "item" | "fluid" | "block",  // 资源分类
      namespace: string,                 // mod ID
      path: string,                      // 路径
      description_id: string,            // 翻译键 "block.minecraft.stone"
      name_en: string,                   // 英文名 (lang文件)
      name_zh: string,                   // 中文名 (lang文件)
      display_name: string,              // 运行时显示名 (getHoverName, NBT感知)
      tooltip_en: [string],              // 英文tooltip全部行 (getTooltipLines)
      tooltip_zh: [string],              // 中文tooltip全部行
      icon_file: string                  // 图标相对路径 "item/minecraft/stone.png"
    }
  ]
}
```

### list/items.json
基于 `BuiltInRegistries.ITEM` 注册表的全量物品元数据。

```
{
  type: "item",
  count: int,
  items: [
    {
      id: string,                        // "minecraft:diamond"
      namespace: string,
      path: string,
      tags: [string],                    // 标签列表
      description_id: string,            // 翻译键
      name_en: string,                   // 英文名
      name_zh: string,                   // 中文名
      display_name: string,              // 运行时显示名
      tooltip_en: [string],              // 英文tooltip
      tooltip_zh: [string],              // 中文tooltip
      max_stack_size: int,
      max_damage: int,
      is_fireproof: boolean,
      has_container: boolean
    }
  ]
}
```

### list/fluids.json
基于 `BuiltInRegistries.FLUID` 注册表的全量流体元数据。

```
{
  type: "fluid",
  count: int,
  fluids: [
    {
      id: string,                        // "minecraft:water"
      namespace: string,
      path: string,
      tags: [string],
      description_id: string,            // 翻译键 (FluidType)
      name_en: string,
      name_zh: string,
      display_name: string,              // 运行时显示名 (FluidStack.getDisplayName)
      is_source: boolean,
      bucket_volume: int                 // 固定 1000 mB
    }
  ]
}
```

### list/blocks.json
基于 `BuiltInRegistries.BLOCK` 注册表的全量方块元数据。

```
{
  type: "block",
  count: int,
  blocks: [
    {
      id: string,                        // "minecraft:stone"
      namespace: string,
      path: string,
      tags: [string],
      description_id: string,
      name_en: string,
      name_zh: string,
      hardness: float,
      explosion_resistance: float,
      light_emission: int
    }
  ]
}
```

### misc/gt_machines.json
基于 `GTRegistries.MACHINES` 的 GT 机器/多方块元数据。

```
{
  type: "gt_machines",
  count: int,
  machines: [
    {
      id: string,                        // "gtceu:lv_electric_furnace"
      namespace: string,
      path: string,
      description_id: string,            // 方块翻译键
      name_en: string,
      name_zh: string,
      tier: int,                         // 电压等级序号
      tier_name: string,                 // "LV" / "MV" / ...
      voltage: long,                     // 该等级电压值
      recipe_types: [string],            // 关联配方类型 ID
      is_multiblock: boolean,
      is_generator: boolean              // 仅多方块
    }
  ]
}
```

### misc/recipe_type_machines.json
配方类型 → 机器反向映射，基于 `GTRegistries.RECIPE_TYPES`。

```
{
  type: "recipe_type_machines",
  count: int,
  recipe_types: [
    {
      id: string,                        // "gtceu:electric_blast_furnace"
      namespace: string,
      path: string,
      translation_key: string,           // "gtceu.electric_blast_furnace"
      name_en: string,
      name_zh: string,
      group: string,                     // "electric"/"multiblock"/"generator"/"steam"/"dummy"
      max_item_inputs: int,
      max_item_outputs: int,
      max_fluid_inputs: int,
      max_fluid_outputs: int,
      machine_count: int,
      machines: [string]                 // 可处理该配方的机器 ID 列表
    }
  ]
}
```

### recipe/gregtech/{type}.json
GT 配方按类型分文件导出。

```
{
  recipe_type: string,                   // "gtceu:macerator"
  recipe_type_name: string,
  count: int,
  recipes: [
    {
      id: string,
      type: string,
      duration: int,                     // ticks
      eu_per_tick: long,                 // EU/t
      eu_type: "input" | "output",
      total_eu: long,
      input_items: [ { items: [{item, count}], amount, chance, tier_chance_boost, chance_percent } ],
      output_items: [ ... ],
      input_fluids: [ { fluids: [{fluid, amount}], amount, chance } ],
      output_fluids: [ ... ],
      tick_inputs: { eu: [{content, chance}] },
      data: { ebf_temp, ... },           // 配方附加数据
      conditions: [ { type, class, tooltip, is_reverse } ],
      is_fuel: boolean
    }
  ]
}
```

### tag/{type}/{tag_name}.json

```
{
  tag: string,                           // "forge:ingots/iron"
  type: "item" | "fluid" | "block",
  items: [string],                       // 资源 ID 列表
  count: int
}
```

### misc/gt_environments.json

```
{
  modpack: "GTOCore",
  version: string,
  minecraft_version: "1.20.1",
  cleanroom_types: [ { id, name, tier, description } ],
  vacuum_conditions: [ { tier, name, description } ],
  gravity_conditions: [ { id, name, gravity_level, description } ],
  dimension_conditions: [ { id, name, is_space, has_oxygen } ],
  voltage_tiers: [ { tier, name, voltage, amperage_1a, amperage_4a, amperage_16a } ],
  recipe_modifiers: [ { id, name, description, duration_multiplier, eu_multiplier } ],
  special_conditions: [ { id, name, description } ]
}
```

## 触发方式

```java
ItemIconReport.generateReport();
```

通过 `@DataGeneratorScanned` 注解自动触发，或手动调用。必须在客户端环境运行（需 OpenGL、EMI、RecipeManager）。

## 名称与 Tooltip 获取逻辑

| 字段 | 来源 | 说明 |
|------|------|------|
| `description_id` | `item.getDescriptionId()` / `fluid.getFluidType().getDescriptionId()` | 翻译键 |
| `name_en` | `langEN.get(descriptionId)` — 从 `en_us.json` 加载 | 静态翻译，不含 NBT |
| `name_zh` | `langZH.get(descriptionId)` — 从 `zh_cn.json` 加载 | 静态翻译，不含 NBT |
| `display_name` | `stack.getHoverName().getString()` | 运行时名称，含 NBT（附魔书、药水等） |
| `tooltip_en` | `getTooltipLines()` + 临时切换 EN Language | 完整英文 tooltip（含 GT 属性、附魔、lore） |
| `tooltip_zh` | `getTooltipLines()` + 临时切换 ZH Language | 完整中文 tooltip |

tooltip 通过 `ItemStack.getTooltipLines(player, TooltipFlag.Default.NORMAL)` 获取，与 EMI/JEI 悬浮显示内容一致。
通过临时替换 `Language.getInstance()` 实现双语 tooltip 导出。
