$ErrorActionPreference = 'Stop'

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot '..\..')
$sourcePath = Join-Path $repoRoot 'src\main\java\com\gtocore\eio_travel\client\RenderTravelTargets.java'
$source = Get-Content -Raw $sourcePath

$minecraftCalls = ([regex]::Matches($source, 'Minecraft\.getInstance\(\)')).Count
if ($minecraftCalls -gt 1) {
    throw "renderLevel should cache Minecraft.getInstance(); found $minecraftCalls calls."
}

if ($source -cnotmatch 'PoseStack poseStack = event\.getPoseStack\(\);') {
    throw 'renderLevel should cache the PoseStack outside the target loop.'
}

if ($source -cnotmatch 'Vec3 projectedView = mainCamera\.getPosition\(\);') {
    throw 'renderLevel should cache the camera position outside the target loop.'
}

Write-Host 'RenderTravelTargets cache regression check passed.'
