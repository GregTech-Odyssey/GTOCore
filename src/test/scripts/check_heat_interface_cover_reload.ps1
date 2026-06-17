$ErrorActionPreference = 'Stop'

$path = Join-Path $PSScriptRoot '../../main/java/com/gtocore/common/cover/HeatInterfaceCover.java'
$source = Get-Content -Raw -Path $path

if ($source -notmatch 'handler\.addChangedListener\(\(\) -> \{\s*if \(MetaMachine\.getMachine\(coverHolder\.holder\(\)\) instanceof IRecipeLogicMachine') {
    throw 'HeatInterfaceCover heat change listener must resolve the current machine after reload.'
}

$onLoadMatch = [regex]::Match($source, 'public void onLoad\(\) \{\s*super\.onLoad\(\);\s*handler\.onLoad\(\);')
if (-not $onLoadMatch.Success) {
    throw 'HeatInterfaceCover.onLoad must load the heat handler after the cover is restored.'
}

if ($source -match 'private MetaMachine machine|private MetaMachine getMachine\(\)') {
    throw 'HeatInterfaceCover must not keep a stale machine cache across reload.'
}

Write-Host 'HeatInterfaceCover reload regression check passed.'
