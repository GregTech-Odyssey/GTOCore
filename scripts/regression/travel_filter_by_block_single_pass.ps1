$ErrorActionPreference = "Stop"

$root = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$file = Join-Path $root "src\main\java\com\gtocore\eio_travel\logic\TravelUtils.java"
$text = Get-Content -Raw -LiteralPath $file

if ($text -match "targets\.toList\(\)") {
    throw "filterByBlock should not materialize all travel targets"
}
if ($text -match "existingBlockTypes") {
    throw "filterByBlock should avoid the old duplicate block-type scan"
}
if ($text -notmatch "return targets\.filter") {
    throw "filterByBlock should filter the target stream directly"
}
if ($text -notmatch "/\*\*[\s\S]*filterByBlock") {
    throw "filterByBlock should document the stored block-id filter behavior"
}

Write-Host "Travel filterByBlock single-pass regression passed."
