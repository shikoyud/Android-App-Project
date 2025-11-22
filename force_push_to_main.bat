@echo off
echo ============================================
echo FORCE PUSH CODE LÊN MAIN - PHIÊN BẢN ĐƠN GIẢN
echo ============================================
echo.

cd /d "D:\SGU Nam 3 HK1\Mobile\NhatKySelfie"

echo Nhánh hiện tại:
git branch
echo.

echo ⚠️ SẼ GHI ĐÈ HOÀN TOÀN CODE TRÊN MAIN!
echo.
pause

echo.
echo [1] Add tất cả files...
git add -A
echo Done!
echo.

echo [2] Commit...
git commit -m "feat: Complete all features - Push from tuong-huy to main (23/11/2025)"
echo Done!
echo.

echo [3] Checkout sang main (tạo mới nếu không có)...
git checkout -B main
echo Done!
echo.

echo [4] Force push lên GitHub...
git push -f origin main
echo Done!
echo.

echo ============================================
echo ✅ HOÀN THÀNH!
echo ============================================
echo.
echo Code đã được push lên:
echo https://github.com/shikoyud/Android-App-Project/tree/main
echo.
echo Làm mới trang GitHub:
echo 1. Nhấn Ctrl + Shift + R
echo 2. Hoặc xóa cache browser
echo 3. Kiểm tra commit mới nhất
echo.
echo Commit message: "feat: Complete all features - Push from tuong-huy to main"
echo Date: 23/11/2025
echo.
pause

