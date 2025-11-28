@echo off
REM force_push_to_main.bat - Force push working tree to origin/main with confirmation (with debug info)

REM Change to script directory (project root)
cd /d "%~dp0"

echo ============================================
echo FORCE PUSH TO main (interactive) - DEBUG ENABLED
echo ============================================
echo Current Git status (branch and last commit):
git branch --show-current
git --no-pager log -1 --oneline
echo.

:prepareMsg
rem Use all parameters as commit message if provided, otherwise generate default message
if "%~1"=="" (
    for /f "tokens=1-3 delims=/.- " %%a in ('date /t') do set _DATE=%%a-%%b-%%c
    for /f "tokens=1 delims=." %%t in ('time /t') do set _TIME=%%t
    set "COMMIT_MSG=feat: force push from local (%_DATE% %_TIME%)"
) else (
    set "COMMIT_MSG=%*"
)
echo Commit message: "%COMMIT_MSG%"
echo.
echo WARNING: This will overwrite the remote "main" branch with your local state.
set /p CONFIRM="Type YES to proceed or anything else to abort: "
if /i not "%CONFIRM%"=="YES" (
    echo Aborted by user.
    exit /b 0
)
echo.
echo === Debug: remote configuration ===
git remote -v
echo.
echo === Debug: fetch origin ===
git fetch origin --prune --verbose || echo "git fetch failed"
echo.
echo === Debug: remote show origin ===
git remote show origin || echo "remote show failed"
echo.
echo === Debug: ls-remote for main ===
git ls-remote --heads origin refs/heads/main || echo "ls-remote failed or branch not found on remote"
echo.
echo Local and remote commits (after fetch):
git rev-parse --verify main 2>nul || echo "local main not found"
git rev-parse --verify origin/main 2>nul || echo "origin/main not found"
echo.
echo [1] git add -A
git add -A
echo.
echo [2] git commit -m "%COMMIT_MSG%"
REM commit may fail if there is nothing to commit; continue anyway
git commit -m "%COMMIT_MSG%" 2>nul || (echo No changes to commit or commit failed, continuing...)
echo.
echo [3] checkout main (create/reset local main)
git checkout -B main || (echo "checkout failed" & exit /b 1)
echo.
echo [4] force push to origin/main (verbose)
git push -f origin main -v
if %errorlevel% neq 0 (
    echo Push failed with exit code %errorlevel%.
    echo Check branch protection, remote URL, and authentication.
    exit /b %errorlevel%
)
echo.
echo ============================================
echo DONE: pushed to origin/main (if no errors shown above)
echo ============================================
echo.
pause
