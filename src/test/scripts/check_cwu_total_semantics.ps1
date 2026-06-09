$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent (Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path))
$builderDir = Join-Path $root 'main/java/com/gtocore/data/recipe/builder/research'
$ordinaryElectricBuilders = @(
    'DataAnalysisRecipeBuilder.java',
    'DataIntegrationRecipeBuilder.java',
    'RecipesDataGenerateRecipeBuilder.java',
    'DataCrystalConstruction.java'
)

$failures = @()
foreach ($file in $ordinaryElectricBuilders) {
    $path = Join-Path $builderDir $file
    $text = Get-Content -Raw $path
    if ($text -match '\.totalCWU\s*\(') {
        $failures += "$file uses GTRecipeBuilder.totalCWU() for an ordinary electric recipe"
    }
}

if ($failures.Count -gt 0) {
    $failures | ForEach-Object { Write-Error $_ }
    exit 1
}
