# ItemIconReport Enhancement Tasks

## Overview
Enhance the report export system to include:
1. Chinese and English display names for all items/fluids/blocks
2. GT machine metadata with recipe type mapping  
3. GT multiblock machine metadata
4. Complete EMI-aligned stack list (no missing items)

## Tasks

### Task 1: Dual-Language Name Support (i18n)
**Status**: ✅ DONE

- Added `loadLanguageMap()` to load `en_us.json` / `zh_cn.json` from ResourceManager
- Added `tr()` helper for translation lookup
- Added `langEN` / `langZH` static fields
- All three metadata lists (items/fluids/blocks) now export `name_en`, `name_zh`, `description_id`

### Task 2: GT Machine Metadata Export
**Status**: ✅ DONE

- New file: `misc/gt_machines.json`
- Exports all `GTRegistries.MACHINES` with id, bilingual names, tier, voltage, recipe types, is_multiblock, is_generator

### Task 3: Recipe Type → Machine Mapping
**Status**: ✅ DONE

- New file: `misc/recipe_type_machines.json`
- For each `GTRecipeType`: id, bilingual names, group, IO sizes, machine list

### Task 4: GT Multiblock Machine Details
**Status**: ✅ DONE (merged into Task 2)

- `MultiblockMachineDefinition` entries include `is_generator` flag

### Task 5: EMI Stack List Name Export
**Status**: ✅ DONE

- New file: `list/emi_stacks.json`
- Collected during icon export loop with id, type, bilingual names, display_name, icon_file path

### Task 6: Fluid Name Export via FluidType
**Status**: ✅ DONE (merged into Task 1)

- Fluids use `fluid.getFluidType().getDescriptionId()` for translation key lookup

### Task 7: Update README Schema
**Status**: ✅ DONE

- Updated `REPORT_SYSTEM.md` with new directory structure, all new JSON schemas, changelog v1.3

### Task 8: Verify Compilation
**Status**: ✅ DONE

- `gradlew compileJava` — BUILD SUCCESSFUL
