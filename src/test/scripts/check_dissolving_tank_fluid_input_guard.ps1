$ErrorActionPreference = 'Stop'

$path = Join-Path $PSScriptRoot '../../main/java/com/gtocore/common/machine/multiblock/electric/DissolvingTankMachine.java'
$source = Get-Content -Raw -Path $path

$guardPattern = 'var fluidList = recipe\.fluidInputs;\s*if \(fluidList\.size\(\) < 2\) \{\s*return null;\s*\}'
if ($source -notmatch $guardPattern) {
    throw 'DissolvingTankMachine must guard recipes with fewer than two fluid inputs before reading get(0)/get(1).'
}

$guardIndex = $source.IndexOf('fluidList.size() < 2')
$firstAccessIndex = $source.IndexOf('fluidList.get(0)')
$secondAccessIndex = $source.IndexOf('fluidList.get(1)')

if ($firstAccessIndex -lt 0 -or $secondAccessIndex -lt 0) {
    throw 'DissolvingTankMachine fluid ratio logic must still read the first two fluid inputs after the guard.'
}

if ($guardIndex -lt 0 -or $guardIndex -gt $firstAccessIndex -or $guardIndex -gt $secondAccessIndex) {
    throw 'DissolvingTankMachine fluid input guard must run before both indexed fluid input reads.'
}

Write-Host 'DissolvingTankMachine fluid input guard regression check passed.'
