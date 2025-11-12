@echo off
echo ===================================
echo PUSH NHAT KY SELFIE TO GITHUB
echo ===================================
echo.

cd /d "D:\SGU Nam 3 HK1\Mobile\NhatKySelfie"

echo [1/6] Xoa cac file .md khong can thiet...
del /f ALL_BUGS_FIXED.md BUG_FIX_REPORT.md FINAL_BUG_FIX_REPORT.md FIND_SYNC_BUTTON.md INDEX.md PROJECT_GUIDE.md START_HERE.md SYNC_GUIDE.md SYNC_GUIDE_DETAILED.md VALIDATION_CHECKLIST.md CHECKLIST.md QUICK_START.md 2>nul
echo Done!
echo.

echo [2/6] Kiem tra git repository...
git status >nul 2>&1
if %errorlevel% neq 0 (
    echo Git chua duoc khoi tao. Dang khoi tao...
    git init
    echo Done!
) else (
    echo Git da ton tai!
)
echo.

echo [3/6] Them tat ca file vao git...
git add .
echo Done!
echo.

echo [4/6] Commit changes...
git commit -m "Initial commit: Nhat Ky Selfie - Android Project"
echo Done!
echo.

echo [5/6] Them remote repository...
git remote remove origin 2>nul
echo.
echo ========================================
echo XAC NHAN REPOSITORY URL
echo ========================================
echo.
echo Repository mac dinh: https://github.com/shikoyud/Android-App-Project.git
echo.
echo Neu day KHONG PHAI repository cua ban, hay:
echo 1. Fork repository ve tai khoan cua ban
echo 2. Hoac tao repository moi
echo 3. Sau do sua file push_to_github.bat dong 40
echo.
set /p CONFIRM="Ban co muon tiep tuc push vao repository nay? (Y/N): "
if /i not "%CONFIRM%"=="Y" (
    echo.
    echo Da huy! Vui long:
    echo 1. Tao/Fork repository cua ban
    echo 2. Sua dong 40 trong file push_to_github.bat
    echo 3. Thay doi URL thanh repository cua ban
    echo.
    pause
    exit
)
echo.
git remote add origin https://github.com/shikoyud/Android-App-Project.git
echo Done!
echo.

echo [6/6] Doi ten branch thanh main...
git branch -M main
echo Done!
echo.

echo ===================================
echo DANG PUSH LEN GITHUB...
echo ===================================
echo.
echo Luu y: Ban can nhap thong tin dang nhap:
echo - Username: [USERNAME GITHUB CUA BAN]
echo - Password: Personal Access Token (KHONG PHAI password thuong!)
echo.
echo Tao Personal Access Token tai:
echo https://github.com/settings/tokens
echo.
echo Chon: Generate new token (classic)
echo Quyen: Chon "repo" (tat ca)
echo Copy token (chi hien 1 lan!) va dan vao Password
echo.

git push -u origin main

echo.
echo ===================================
if %errorlevel% equ 0 (
    echo THANH CONG! Code da duoc push len:
    echo https://github.com/shikoyud/Android-App-Project
) else (
    echo CO LOI XAY RA!
    echo.
    echo Neu gap loi "rejected", thu lenh:
    echo git push -u origin main --force
)
echo ===================================
echo.
pause

