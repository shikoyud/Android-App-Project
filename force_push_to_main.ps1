<#
force_push_to_main.ps1
PowerShell script to force-push the repository to origin/main with interactive confirmation and debug output.
Usage: .\force_push_to_main.ps1 -Message "commit message"
#>

param(
    [string]$Message
)

function Exit-WithMessage($msg, $code = 1) {
    Write-Host $msg -ForegroundColor Red
    exit $code
}

# Ensure script runs from its directory (project root)
if ($PSScriptRoot) { Set-Location -Path $PSScriptRoot }

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "FORCE PUSH TO main (PowerShell) - DEBUG ENABLED" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan

Write-Host "Current git branch and last commit:" -ForegroundColor Yellow
& git branch --show-current
& git --no-pager log -1 --oneline
Write-Host ""

if (-not $Message -or $Message.Trim() -eq "") {
    $Message = "feat: force push from local ($(Get-Date -Format 'yyyy-MM-dd_HH:mm:ss'))"
}
Write-Host "Commit message:`n $Message`n" -ForegroundColor Green

Write-Host "WARNING: This will overwrite the remote 'main' branch with your local state." -ForegroundColor Red
$confirm = Read-Host "Type YES to proceed or anything else to abort"
if ($confirm -ne 'YES') { Write-Host 'Aborted by user.'; exit 0 }

Write-Host "`n=== Debug: remote configuration ===" -ForegroundColor Yellow
& git remote -v

Write-Host "`n=== Debug: fetch origin ===" -ForegroundColor Yellow
& git fetch origin --prune --verbose 2>&1 | ForEach-Object { Write-Host $_ }

Write-Host "`n=== Debug: remote show origin ===" -ForegroundColor Yellow
& git remote show origin 2>&1 | ForEach-Object { Write-Host $_ }

Write-Host "`n=== Debug: ls-remote for main ===" -ForegroundColor Yellow
& git ls-remote --heads origin refs/heads/main 2>&1 | ForEach-Object { Write-Host $_ }

Write-Host "`n=== Local and remote commits (after fetch) ===" -ForegroundColor Yellow
$localMain = & git rev-parse --verify main 2>$null
if ($LASTEXITCODE -ne 0) { Write-Host 'local main not found' } else { Write-Host "local main: $localMain" }
$originMain = & git rev-parse --verify origin/main 2>$null
if ($LASTEXITCODE -ne 0) { Write-Host 'origin/main not found' } else { Write-Host "origin/main: $originMain" }

Write-Host "`n[1] git add -A" -ForegroundColor Yellow
& git add -A

Write-Host "`n[2] git commit -m \"$Message\"" -ForegroundColor Yellow
& git commit -m "$Message"
if ($LASTEXITCODE -ne 0) { Write-Host 'No changes to commit or commit failed; continuing...' }

Write-Host "`n[3] checkout main (create/reset local main)" -ForegroundColor Yellow
& git checkout -B main
if ($LASTEXITCODE -ne 0) { Exit-WithMessage 'Failed to checkout main' }

Write-Host "`n[4] force push to origin/main (verbose)" -ForegroundColor Yellow
& git push -f origin main -v 2>&1 | ForEach-Object { Write-Host $_ }
if ($LASTEXITCODE -ne 0) {
    Write-Host "`nPush failed with exit code $LASTEXITCODE" -ForegroundColor Red
    Write-Host "Common causes: protected branch on GitHub, authentication failure, or wrong remote URL." -ForegroundColor Red
    Write-Host "Check 'git remote -v' and confirm you have push permissions to the repository." -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "`n============================================" -ForegroundColor Green
Write-Host "DONE: pushed to origin/main" -ForegroundColor Green
Write-Host "============================================`n" -ForegroundColor Green

Write-Host "If remote main still appears unchanged, check branch protection rules or repo permissions." -ForegroundColor Yellow

Pause

