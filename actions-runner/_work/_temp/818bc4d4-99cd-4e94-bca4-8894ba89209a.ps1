$ErrorActionPreference = 'stop'
$python = "C:\Python311\python.exe"
$podman = "C:\Program Files\RedHat\Podman\podman.exe"
$moddir = "$env:RUNNER_TEMP\pdc"

& $python -m pip install --upgrade pip
if ($LASTEXITCODE -ne 0) { throw "pip upgrade failed" }

& $python -m pip install podman-compose -t $moddir
if ($LASTEXITCODE -ne 0) { throw "pip install podman-compose failed" }

$env:PYTHONPATH = $moddir

& $python -m podman_compose --version

# Pull de la imagen versionada GHCR
& $podman pull $env:IMAGE_TAG
if ($LASTEXITCODE -ne 0) { throw "podman pull failed" }

# DOWN
& $python -m podman_compose down
if ($LASTEXITCODE -ne 0) { Write-Host "no stack to stop"; $LASTEXITCODE = 0 }

# UP 
& $python -m podman_compose up -d
if ($LASTEXITCODE -ne 0) { throw "compose up failed" }

# Ver contenedores
& $podman ps
if ((Test-Path -LiteralPath variable:\LASTEXITCODE)) { exit $LASTEXITCODE }