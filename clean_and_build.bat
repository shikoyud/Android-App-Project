@echo off
echo ================================
echo CLEAN & REBUILD PROJECT
echo ================================
echo.

cd /d "D:\SGU Nam 3 HK1\Mobile\NhatKySelfie"

echo [1/3] Cleaning project...
call gradlew clean

echo.
echo [2/3] Building project...
call gradlew build --warning-mode all

echo.
echo [3/3] Done!
echo.
echo If successful, try running app again in Android Studio!
echo.
pause

