# GTOCore 资源报告系统

完整的游戏资源导出系统，导出所有物品/流体/方块的图标、中英文名称、tooltip、标签和配方，用于构建网页配方模拟器。

## 输出结构

```
logs/report/GTOCore-{version}/all_{timestamp}/
├── _master_report.json                  # 总体统计
├── item/{modid}/{path}.png              # 物品图标 64x64 PNG (FBO rendering)
├── fluid/{modid}/{path}.png             # 流体图标 64x64 PNG (着色tint)
├── block/{modid}/{path}.png             # 方块图标 64x64 PNG (FBO rendering)
├── tag/
│   ├── item/{tag_name}.json             # 物品标签
│   ├── fluid/{tag_name}.json            # 流体标签
│   └── block/{tag_name}.json            # 方块标签
├── recipe/
│   ├── gregtech/{type}.json             # GT配方 (按类型分文件)
│   └── vanilla/{type}.json              # 原版配方 (按类型分文件)
├── list/
│   ├── emi_stacks.json                  # EMI全量清单 (含tooltip、中英文名)
│   ├── items.json                       # 物品元数据 (含tooltip、中英文名)
│   ├── fluids.json                      # 流体元数据 (含中英文名)
│   └── blocks.json                      # 方块元数据 (含中英文名)
└── misc/
    ├── gt_environments.json             # GT环境 (超净间/真空/重力/电压)
    ├── gt_machines.json                 # GT机器+多方块元数据
    └── recipe_type_machines.json        # 配方类型→机器映射
```

## 名称与 Tooltip 获取逻辑

| 字段 | 来源 | 说明 |
|------|------|------|
| `description_id` | `item.getDescriptionId()` / `fluid.getFluidType().getDescriptionId()` | 翻译键 |
| `name_en` | 从 `en_us.json` 加载的翻译表 | 静态翻译，不含 NBT |
| `name_zh` | 从 `zh_cn.json` 加载的翻译表 | 静态翻译，不含 NBT |
| `display_name_en` | `Language.inject(EN)` → `stack.getHoverName()` | 运行时英文名（含 NBT：附魔书、药水等） |
| `display_name_zh` | `Language.inject(ZH)` → `stack.getHoverName()` | 运行时中文名（含 NBT） |
| `tooltip_en` | `Language.inject(EN)` → `stack.getTooltipLines(null, NORMAL)` | 完整英文 tooltip（含 GT 属性、附魔、lore） |
| `tooltip_zh` | `Language.inject(ZH)` → `stack.getTooltipLines(null, NORMAL)` | 完整中文 tooltip |

通过 `Language.inject()` 临时切换语言实例，与 EMI/JEI 悬浮显示内容一致。

## Schema 定义

### list/emi_stacks.json

基于 `EmiApi.getIndexStacks()` 在渲染线程收集，包含全量 tooltip。

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | string | 资源 ID（如 `minecraft:stone`） |
| `type` | string | `"item"` / `"fluid"` / `"block"` |
| `namespace` | string | mod ID |
| `path` | string | 路径部分 |
| `description_id` | string | 翻译键 |
| `name_en` | string | 英文名（lang 文件） |
| `name_zh` | string | 中文名（lang 文件） |
| `display_name_en` | string | 运行时英文名（NBT 感知） |
| `display_name_zh` | string | 运行时中文名（NBT 感知） |
| `tooltip_en` | string[] | 英文 tooltip 全部行 |
| `tooltip_zh` | string[] | 中文 tooltip 全部行 |
| `nbt` | string? | NBT 数据（仅有 NBT 的物品：编程电路、附魔书等） |
| `is_circuit` | boolean? | 是否编程电路（仅 `gtceu:programmed_circuit`） |
| `circuit_config` | int? | 编程电路配置编号（0-32） |
| `icon_file` | string | 图标相对路径 |

### list/items.json

基于 `BuiltInRegistries.ITEM` 注册表。

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | string | 物品 ID |
| `namespace` | string | mod ID |
| `path` | string | 路径 |
| `tags` | string[] | 标签列表 |
| `description_id` | string | 翻译键 |
| `name_en` | string | 英文名 |
| `name_zh` | string | 中文名 |
| `display_name_en` | string | 运行时英文名 |
| `display_name_zh` | string | 运行时中文名 |
| `tooltip_en` | string[] | 英文 tooltip |
| `tooltip_zh` | string[] | 中文 tooltip |
| `max_stack_size` | int | 最大堆叠 |
| `max_damage` | int | 最大耐久 |
| `is_fireproof` | boolean | 防火 |
| `has_container` | boolean | 有合成容器 |

### list/fluids.json

基于 `BuiltInRegistries.FLUID` 注册表。

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | string | 流体 ID |
| `namespace` | string | mod ID |
| `path` | string | 路径 |
| `tags` | string[] | 标签列表 |
| `description_id` | string | 翻译键（FluidType） |
| `name_en` | string | 英文名（lang 文件） |
| `name_zh` | string | 中文名（lang 文件） |
| `display_name_en` | string | 运行时英文名（FluidStack） |
| `display_name_zh` | string | 运行时中文名（FluidStack） |
| `is_source` | boolean | 是否源方块 |
| `bucket_volume` | int | 桶容量（1000 mB） |

### list/blocks.json

基于 `BuiltInRegistries.BLOCK` 注册表。

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | string | 方块 ID |
| `namespace` | string | mod ID |
| `path` | string | 路径 |
| `tags` | string[] | 标签列表 |
| `description_id` | string | 翻译键 |
| `name_en` | string | 英文名 |
| `name_zh` | string | 中文名 |
| `hardness` | float | 硬度 |
| `explosion_resistance` | float | 爆炸抗性 |
| `light_emission` | int | 发光等级 |

### misc/gt_machines.json

基于 `GTRegistries.MACHINES`。

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | string | 机器 ID（如 `gtceu:lv_electric_furnace`） |
| `namespace` | string | mod ID |
| `path` | string | 路径 |
| `description_id` | string | 方块翻译键 |
| `name_en` | string | 英文名 |
| `name_zh` | string | 中文名 |
| `tier` | int | 电压等级序号 |
| `tier_name` | string | 等级缩写（LV/MV/...） |
| `voltage` | long | 电压值 |
| `recipe_types` | string[] | 关联配方类型 ID 列表 |
| `is_multiblock` | boolean | 是否多方块 |
| `is_generator` | boolean | 是否发电机（仅多方块） |

### misc/recipe_type_machines.json

基于 `GTRegistries.RECIPE_TYPES`，配方类型 → 机器反向映射。

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | string | 配方类型 ID |
| `namespace` | string | mod ID |
| `path` | string | 路径 |
| `translation_key` | string | 翻译键（`gtceu.{path}`） |
| `name_en` | string | 英文名 |
| `name_zh` | string | 中文名 |
| `group` | string | 组分类（electric/multiblock/generator/steam/dummy） |
| `max_item_inputs` | int | 最大物品输入 |
| `max_item_outputs` | int | 最大物品输出 |
| `max_fluid_inputs` | int | 最大流体输入 |
| `max_fluid_outputs` | int | 最大流体输出 |
| `machine_count` | int | 可处理该配方的机器数量 |
| `machines` | string[] | 机器 ID 列表 |

### recipe/gregtech/{type}.json

GT 配方按类型分文件。每个配方包含：

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | string | 配方 ID |
| `type` | string | 配方类型 |
| `duration` | int | 持续时间（ticks） |
| `eu_per_tick` | long | EU/t |
| `eu_type` | string | `"input"` / `"output"` |
| `total_eu` | long | 总 EU 消耗 |
| `mana_per_tick` | long? | 魔力/t（仅魔力配方存在） |
| `mana_type` | string? | `"input"` / `"output"` |
| `total_mana` | long? | 总魔力消耗 |
| `cwu_per_tick` | long? | 算力/t（CWU/t，仅需要算力的配方存在） |
| `duration_is_total_cwu` | boolean? | true 时 duration 字段表示总 CWU 而非 ticks |
| `total_cwu` | long? | 总算力消耗 |
| `input_items` | array | 输入物品（含 items/amount/chance/tier_chance_boost/chance_percent） |
| `output_items` | array | 输出物品 |
| `input_fluids` | array | 输入流体（含 fluids/amount/chance） |
| `output_fluids` | array | 输出流体 |
| `tick_inputs` | object | 每 tick 输入（eu/mana 等） |
| `data` | object | 附加数据（ebf_temp 等） |
| `requires_circuit` | boolean? | 是否需要编程电路（仅存在时为 true） |
| `circuit_config` | int? | 编程电路配置编号（0-32） |
| `conditions` | array | 配方条件（type/class/tooltip/is_reverse） |
| `is_fuel` | boolean | 是否燃料配方 |

### tag/{type}/{tag_name}.json

| 字段 | 类型 | 说明 |
|------|------|------|
| `tag` | string | 标签 ID（如 `forge:ingots/iron`） |
| `type` | string | `"item"` / `"fluid"` / `"block"` |
| `items` | string[] | 资源 ID 列表 |
| `count` | int | 资源数量 |

### misc/gt_environments.json

| 字段 | 类型 | 说明 |
|------|------|------|
| `cleanroom_types` | array | 超净间类型（id/name/tier/description） |
| `vacuum_conditions` | array | 真空等级（tier/name/description） |
| `gravity_conditions` | array | 重力条件（id/name/gravity_level） |
| `dimension_conditions` | array | 维度条件（id/name/is_space/has_oxygen） |
| `voltage_tiers` | array | 电压等级（tier/name/voltage/amperage） |
| `recipe_modifiers` | array | 配方修改器（overclock/parallel） |
| `special_conditions` | array | 特殊条件 |

## 触发方式

通过 `@DataGeneratorScanned` 注解自动触发，或手动调用 `ItemIconReport.generateReport()`。

**要求**：客户端环境、OpenGL 上下文（FBO 渲染）、EMI mod、已加载世界（RecipeManager）。

## 导出流程

1. **Phase 1**（渲染线程）：
   - FBO 渲染图标 + 收集 EMI 全量清单（含双语 tooltip）
   - **补充注册表遍历**：遍历 `BuiltInRegistries.ITEM` 和 `BuiltInRegistries.FLUID`，导出 EMI 索引中遗漏的物品/流体图标（与 JEI/REI 相同的注册表直接遍历策略）
2. **Phase 2-6**（IO 线程池）：标签、配方、元数据列表（含双语 tooltip）、GT 环境/机器

## 更新日志

### v1.4 (2026-02-17)
- ✅ **编程电路解析** — 配方中检测 `gtceu:programmed_circuit`，导出 `requires_circuit`/`circuit_config` 顶层字段；EMI 清单中标注 `is_circuit`/`circuit_config`/`nbt`
- ✅ **魔力/算力配方解析** — 导出 `mana_per_tick`/`total_mana`（魔力消耗）和 `cwu_per_tick`/`total_cwu`（CWU 算力消耗），含 `duration_is_total_cwu` 特殊模式
- ✅ **补充注册表遍历** — EMI 索引之外，遍历 `BuiltInRegistries.ITEM/FLUID` 补充导出全部物品/流体图标（与 JEI/REI 一致的注册表直接遍历策略）
- ✅ **双语 Tooltip 导出** — 通过 `Language.inject()` 临时切换语言，导出中英文完整 tooltip
- ✅ **运行时显示名** — `display_name_en`/`display_name_zh` 取代 `display_name`，NBT 感知（附魔书、药水等）
- ✅ **流体运行时名称** — 通过 `FluidStack.getDisplayName()` 获取双语流体名
- ✅ **README 精简** — 仅保留结构和 Schema 定义，移除 JS/HTML 示例

### v1.3 (2025-07-20)
- ✅ 双语名称支持 (name_en/name_zh/description_id)
- ✅ EMI 全量清单导出 (emi_stacks.json)
- ✅ GT 机器元数据导出 (gt_machines.json)
- ✅ 配方类型→机器映射 (recipe_type_machines.json)
- ✅ 语言文件加载 (en_us.json + zh_cn.json)

### v1.2
- ✅ 重写 GT 配方解析（EU/t、条件、概率等）
- ✅ 新增 GT 环境信息（超净间/真空/重力/电压）

### v1.1
- ✅ GT 配方分文件导出
- ✅ Missing texture 检测
- ✅ 物品/流体/方块元数据列表

### v1.0
- ✅ FBO 图标导出 (64x64 PNG)
- ✅ 标签导出
- ✅ GT + 原版配方导出

