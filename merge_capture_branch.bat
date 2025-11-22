@echo off
echo ============================================
echo MERGE CAPTURE-EDIT-PHOTO BRANCH
echo ============================================
echo.

REM Chuyển sang thư mục dự án
cd /d "D:\SGU Nam 3 HK1\Mobile\NhatKySelfie"

echo [BUOC 1] Kiểm tra git status...
git status
echo.

echo [BUOC 2] Tạo nhánh tuong-huy và push lên GitHub...
git checkout -b tuong-huy
if errorlevel 1 (
    echo Nhánh tuong-huy đã tồn tại, chuyển sang nhánh đó...
    git checkout tuong-huy
)
echo.

echo [BUOC 3] Add và commit code hiện tại...
git add .
git commit -m "feat: Complete database and repository layer"
echo.

echo [BUOC 4] Push nhánh tuong-huy lên GitHub...
git push -u origin tuong-huy
echo.

echo [BUOC 5] Fetch tất cả các nhánh từ remote...
git fetch origin
echo.

echo [BUOC 6] Kiểm tra nhánh capture-edit-photo có tồn tại không...
git branch -r | findstr "capture-edit-photo"
if errorlevel 1 (
    echo.
    echo ============================================
    echo CẢNH BÁO: Nhánh capture-edit-photo không tồn tại trên GitHub!
    echo ============================================
    echo.
    echo Có thể thành viên 1 chưa push nhánh này lên.
    echo Vui lòng yêu cầu thành viên 1 push nhánh capture-edit-photo lên GitHub.
    echo.
    echo Lệnh để thành viên 1 thực hiện:
    echo   git checkout -b capture-edit-photo
    echo   git add .
    echo   git commit -m "feat: Complete camera capture and edit features"
    echo   git push -u origin capture-edit-photo
    echo.
    pause
    exit /b 1
)
echo.

echo [BUOC 7] Pull code từ nhánh capture-edit-photo về...
git pull origin capture-edit-photo --no-commit --no-ff
if errorlevel 1 (
    echo.
    echo ============================================
    echo CÓ CONFLICT XẢY RA!
    echo ============================================
    echo.
    echo Các file bị conflict:
    git diff --name-only --diff-filter=U
    echo.
    echo Code từ capture-edit-photo đã được pull về local.
    echo Bây giờ bạn cần:
    echo.
    echo 1. Mở Android Studio
    echo 2. Vào VCS -^> Git -^> Resolve Conflicts
    echo 3. Hoặc mở từng file bị conflict và tìm dấu ^<^<^<^<^<^<^< HEAD
    echo 4. Chọn phần code cần giữ lại
    echo 5. Xóa các dấu ^<^<^<^<^<^<^<, =======, ^>^>^>^>^>^>^>
    echo 6. Test kỹ xem app còn chạy không
    echo 7. Sau khi chắc chắn OK, chạy:
    echo    git add .
    echo    git commit -m "fix: Resolve merge conflicts"
    echo    git push origin tuong-huy
    echo.
    pause
    exit /b 0
)
echo.

echo ============================================
echo PULL THÀNH CÔNG - CHƯA CÓ CONFLICT!
echo ============================================
echo.
echo Code từ capture-edit-photo đã được pull về và merge tự động.
echo Bây giờ bạn cần:
echo.
echo 1. Mở Android Studio
echo 2. Kiểm tra kỹ code đã merge
echo 3. Build -^> Clean Project
echo 4. Build -^> Rebuild Project
echo 5. Test app xem còn chạy không
echo 6. Nếu OK, chạy:
echo    git add .
echo    git commit -m "merge: Merge capture-edit-photo into tuong-huy"
echo    git push origin tuong-huy
echo.
echo Nhánh tuong-huy hiện tại:
echo - Đã được push lên GitHub (code cũ của bạn)
echo - Code từ capture-edit-photo đã pull về local
echo - CHƯA push kết quả merge (đợi bạn kiểm tra)
echo.

pause

