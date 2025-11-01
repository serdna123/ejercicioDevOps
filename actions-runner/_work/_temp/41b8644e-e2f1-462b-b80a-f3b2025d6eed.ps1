$ErrorActionPreference = 'stop'
"JWT_SECRET_B64=5F3XsbRejLADs1fQx2na1VOk/VsQJFE3IlqgoGidbSo=" | Out-File -Encoding ASCII .env
"IMAGE_TAG=ghcr.io/serdna123/devops-exercise:e14b146d4c233436a90384f628ffbae8f2cc3497" | Out-File -Encoding ASCII -Append .env
Get-Content .env

if ((Test-Path -LiteralPath variable:\LASTEXITCODE)) { exit $LASTEXITCODE }