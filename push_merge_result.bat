@echo off
echo ============================================
echo PUSH KẾT QUẢ MERGE LÊN GITHUB
echo ============================================
echo.

REM Chuyển sang thư mục dự án
cd /d "D:\SGU Nam 3 HK1\Mobile\NhatKySelfie"

echo [1] Kiểm tra git status...
git status
echo.

echo [2] Kiểm tra bạn đang ở nhánh nào...
git branch --show-current
echo.

echo ============================================
echo BẠN CÓ CHẮC CHẮN MUỐN PUSH KHÔNG?
echo ============================================
echo.
echo Trước khi push, hãy đảm bảo:
echo [x] Đã fix hết conflict (nếu có)
echo [x] Đã test app và chạy OK
echo [x] Đã build thành công
echo [x] Đã commit code
echo.

pause
echo.

echo [3] Đang push lên GitHub...
git push origin tuong-huy
echo.

if errorlevel 1 (
    echo.
    echo ============================================
    echo PUSH THẤT BẠI!
    echo ============================================
    echo.
    echo Có thể bạn chưa commit code.
    echo Hãy chạy:
    echo   git add .
    echo   git commit -m "merge: Merge capture-edit-photo into tuong-huy"
    echo
    echo Sau đó chạy lại file này.
    echo.
    pause
    exit /b 1
)

echo.
echo ============================================
echo PUSH THÀNH CÔNG!
echo ============================================
echo.
echo Kết quả merge đã được push lên GitHub.
echo Kiểm tra tại:
echo https://github.com/shikoyud/Android-App-Project/tree/tuong-huy
echo.

pause

