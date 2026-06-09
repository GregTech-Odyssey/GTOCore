$ErrorActionPreference = 'Stop'

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot '..\..\..')
$sourcePath = Join-Path $repoRoot 'src\main\java\com\gtocore\common\machine\multiblock\electric\LargeAlgaeFarm.java'

if (-not (Test-Path $sourcePath)) {
    throw "LargeAlgaeFarm.java not found at $sourcePath"
}

$source = Get-Content -Raw -Path $sourcePath
$methodName = 'private void updateLightIntensity()'
$methodStart = $source.IndexOf($methodName)
if ($methodStart -lt 0) {
    throw 'updateLightIntensity() method not found'
}

$methodOpenBrace = $source.IndexOf('{', $methodStart)
if ($methodOpenBrace -lt 0) {
    throw 'updateLightIntensity() opening brace not found'
}

function Find-MatchingBrace {
    param(
        [Parameter(Mandatory = $true)][string] $Text,
        [Parameter(Mandatory = $true)][int] $OpenBraceIndex
    )

    $depth = 0
    for ($i = $OpenBraceIndex; $i -lt $Text.Length; $i++) {
        if ($Text[$i] -eq '{') {
            $depth++
        } elseif ($Text[$i] -eq '}') {
            $depth--
            if ($depth -eq 0) {
                return $i
            }
        }
    }

    throw "No matching brace found for index $OpenBraceIndex"
}

$methodCloseBrace = Find-MatchingBrace -Text $source -OpenBraceIndex $methodOpenBrace
$methodBody = $source.Substring($methodOpenBrace + 1, $methodCloseBrace - $methodOpenBrace - 1)

$divisionMatch = [regex]::Match($methodBody, '/\s*\(\s*float\s*\)\s*total|/\s*total')
if (-not $divisionMatch.Success) {
    throw 'No division by total found in updateLightIntensity(); script may be stale'
}

$guardMatch = [regex]::Match($methodBody, 'if\s*\(\s*total\s*<=\s*0\s*\)\s*\{')
if (-not $guardMatch.Success) {
    throw 'Expected a total <= 0 guard in updateLightIntensity()'
}

if ($guardMatch.Index -gt $divisionMatch.Index) {
    throw 'total <= 0 guard must appear before any division by total'
}

$guardOpenBrace = $methodOpenBrace + 1 + $guardMatch.Index + $guardMatch.Value.LastIndexOf('{')
$guardCloseBrace = Find-MatchingBrace -Text $source -OpenBraceIndex $guardOpenBrace
$guardBody = $source.Substring($guardOpenBrace + 1, $guardCloseBrace - $guardOpenBrace - 1)

$finiteFloatLiteral = '(?:0(?:\.0+)?|1(?:\.0+)?)(?:f|F)?'
$checks = [ordered]@{
    lightIntensity = '(?s)(?:this\.)?lightIntensity\s*=\s*0\s*;'
    redWeight      = "(?s)(?:this\.)?redWeight\s*=\s*$finiteFloatLiteral\s*;"
    greenWeight    = "(?s)(?:this\.)?greenWeight\s*=\s*$finiteFloatLiteral\s*;"
    blueWeight     = "(?s)(?:this\.)?blueWeight\s*=\s*$finiteFloatLiteral\s*;"
    return         = '(?s)\breturn\s*;'
}

foreach ($check in $checks.GetEnumerator()) {
    if (-not [regex]::IsMatch($guardBody, $check.Value)) {
        throw "Guard body is missing required safe assignment or return: $($check.Key)"
    }
}

Write-Host 'LargeAlgaeFarm zero-light weight guard check passed.'
