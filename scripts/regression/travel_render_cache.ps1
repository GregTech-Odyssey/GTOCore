$ErrorActionPreference = 'Stop'

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot '..\..')
$sourcePath = Join-Path $repoRoot 'src\main\java\com\gtocore\eio_travel\client\RenderTravelTargets.java'
$source = Get-Content -Raw $sourcePath

function Get-SingleMatch([string] $pattern, [string] $description) {
    $matches = [regex]::Matches($source, $pattern)
    if ($matches.Count -ne 1) {
        throw "renderLevel should contain exactly one $description; found $($matches.Count)."
    }
    return $matches[0]
}

function Assert-BeforeLoop([System.Text.RegularExpressions.Match] $match, [string] $description) {
    if ($match.Index -gt $targetLoop.Index) {
        throw "renderLevel should cache $description before the target loop."
    }
}

$targetLoop = Get-SingleMatch 'for\s*\(\s*ITravelTarget\s+target\s*:\s*targets\s*\)\s*\{' 'target render loop'

Assert-BeforeLoop (Get-SingleMatch 'Minecraft\.getInstance\(\)' 'Minecraft.getInstance() call') 'Minecraft.getInstance()'
Assert-BeforeLoop (Get-SingleMatch 'PoseStack\s+poseStack\s*=\s*event\.getPoseStack\(\);' 'PoseStack cache') 'the PoseStack'
Assert-BeforeLoop (Get-SingleMatch 'Camera\s+mainCamera\s*=\s*minecraft\.gameRenderer\.getMainCamera\(\);' 'main camera cache') 'the main camera'
Assert-BeforeLoop (Get-SingleMatch 'Vec3\s+projectedView\s*=\s*mainCamera\.getPosition\(\);' 'camera position cache') 'the camera position'

Write-Host 'RenderTravelTargets cache regression check passed.'
