$ErrorActionPreference = 'Stop'

$path = Join-Path $PSScriptRoot '../../main/java/com/gtocore/common/cover/HeatInterfaceCover.java'
$source = Get-Content -Raw -Path $path

if ($source -notmatch 'handler\.addChangedListener\(\(\) -> \{\s*if \(getMachine\(\) instanceof IRecipeLogicMachine') {
    throw 'HeatInterfaceCover heat change listener must resolve the machine lazily after reload.'
}

$onLoadMatch = [regex]::Match($source, 'public void onLoad\(\) \{\s*super\.onLoad\(\);\s*machine = MetaMachine\.getMachine\(coverHolder\.holder\(\)\);\s*handler\.onLoad\(\);')
if (-not $onLoadMatch.Success) {
    throw 'HeatInterfaceCover.onLoad must restore the cached machine before loading the heat handler.'
}

$getMachineMatch = [regex]::Match($source, 'private MetaMachine getMachine\(\) \{\s*if \(machine == null\) \{\s*machine = MetaMachine\.getMachine\(coverHolder\.holder\(\)\);\s*\}\s*return machine;\s*\}')
if (-not $getMachineMatch.Success) {
    throw 'HeatInterfaceCover.getMachine must lazily restore the cached machine reference.'
}

Write-Host 'HeatInterfaceCover reload regression check passed.'
