# 🚀 HƯỚNG DẪN PUSH CODE LÊN GITHUB

## 📋 YÊU CẦU

Repository hiện tại: `https://github.com/shikoyud/Android-App-Project.git`

### ⚠️ QUAN TRỌNG: Kiểm tra quyền truy cập

Repository này thuộc về tài khoản `shikoyud`. Bạn CẦN một trong các điều kiện sau:

#### ✅ Tình huống 1: Bạn là chủ tài khoản `shikoyud`
- Chạy file `push_to_github.bat`
- Đăng nhập bằng **Personal Access Token** (xem hướng dẫn bên dưới)

#### ✅ Tình huống 2: Bạn được thêm làm Collaborator
1. Người chủ repository phải vào: https://github.com/shikoyud/Android-App-Project/settings/access
2. Click **"Add people"**
3. Thêm username GitHub của bạn
4. Bạn chấp nhận lời mời qua email
5. Sau đó chạy `push_to_github.bat` và đăng nhập bằng tài khoản của bạn

#### ✅ Tình huống 3: Fork về tài khoản của bạn
1. Vào: https://github.com/shikoyud/Android-App-Project
2. Click nút **"Fork"** ở góc trên bên phải
3. Repository sẽ được copy về: `https://github.com/YOUR_USERNAME/Android-App-Project`
4. Sửa file `push_to_github.bat`:
   - Thay `https://github.com/shikoyud/Android-App-Project.git`
   - Thành `https://github.com/YOUR_USERNAME/Android-App-Project.git`
5. Chạy file `push_to_github.bat`

#### ✅ Tình huống 4: Tạo repository mới của riêng bạn
1. Vào: https://github.com/new
2. Tạo repository mới (ví dụ: `NhatKySelfie`)
3. **KHÔNG CHỌN**: Add README, .gitignore, license
4. Copy URL: `https://github.com/YOUR_USERNAME/NhatKySelfie.git`
5. Sửa file `push_to_github.bat`:
   - Thay `https://github.com/shikoyud/Android-App-Project.git`
   - Thành URL mới của bạn
6. Chạy file `push_to_github.bat`

---

## 🔑 TẠO PERSONAL ACCESS TOKEN

### Bước 1: Truy cập GitHub Settings
Vào: https://github.com/settings/tokens

### Bước 2: Tạo Token mới
1. Click **"Generate new token"** → **"Generate new token (classic)"**
2. **Token name**: `NhatKySelfie-App`
3. **Expiration**: Chọn thời gian hết hạn (khuyến nghị: 90 days)
4. **Select scopes**: 
   - ✅ **repo** (chọn tất cả các ô con trong repo)
     - ✅ repo:status
     - ✅ repo_deployment
     - ✅ public_repo
     - ✅ repo:invite
     - ✅ security_events
5. Kéo xuống cuối, click **"Generate token"**

### Bước 3: Copy Token
⚠️ **QUAN TRỌNG**: Token chỉ hiện 1 lần duy nhất!
- Copy token ngay (dạng: `ghp_xxxxxxxxxxxxxxxxxxxx`)
- Lưu vào notepad để dùng lại

### Bước 4: Sử dụng Token
Khi chạy `push_to_github.bat`, Git sẽ hỏi:
```
Username: shikoyud (hoặc username của bạn)
Password: [DÁN TOKEN VÀO ĐÂY]
```

---

## 🎯 CÁCH SỬ DỤNG

### Cách 1: Chạy file BAT (Khuyến nghị)
1. Double-click file `push_to_github.bat`
2. Nhập Username và Token khi được yêu cầu
3. Đợi quá trình hoàn tất

### Cách 2: Chạy thủ công qua Terminal
```bash
cd /d "D:\SGU Nam 3 HK1\Mobile\NhatKySelfie"

# Xóa file .md không cần thiết
del /f ALL_BUGS_FIXED.md BUG_FIX_REPORT.md FINAL_BUG_FIX_REPORT.md FIND_SYNC_BUTTON.md INDEX.md PROJECT_GUIDE.md START_HERE.md SYNC_GUIDE.md SYNC_GUIDE_DETAILED.md VALIDATION_CHECKLIST.md CHECKLIST.md QUICK_START.md

# Khởi tạo git (nếu chưa có)
git init

# Add tất cả file
git add .

# Commit
git commit -m "Initial commit: Nhat Ky Selfie - Android Project"

# Thêm remote
git remote add origin https://github.com/shikoyud/Android-App-Project.git

# Đổi branch thành main
git branch -M main

# Push
git push -u origin main
```

---

## ❌ XỬ LÝ LỖI THƯỜNG GẶP

### Lỗi 1: "Authentication failed"
**Nguyên nhân**: Sai username/token hoặc không có quyền truy cập

**Giải pháp**:
- Kiểm tra lại username
- Tạo token mới với đúng quyền `repo`
- Kiểm tra xem bạn có quyền push vào repository không

### Lỗi 2: "rejected - fetch first"
**Nguyên nhân**: Repository trên GitHub đã có code

**Giải pháp**:
```bash
git pull origin main --allow-unrelated-histories
# Hoặc force push (cẩn thận - sẽ ghi đè)
git push -u origin main --force
```

### Lỗi 3: "remote origin already exists"
**Nguyên nhân**: Đã có remote trước đó

**Giải pháp**:
```bash
git remote remove origin
git remote add origin https://github.com/shikoyud/Android-App-Project.git
```

### Lỗi 4: "Permission denied"
**Nguyên nhân**: Không có quyền truy cập repository

**Giải pháp**:
- Kiểm tra lại bạn có phải owner/collaborator không
- Hoặc fork repository về tài khoản của bạn
- Hoặc tạo repository mới

---

## ✅ KIỂM TRA SAU KHI PUSH

1. Mở trình duyệt, vào: https://github.com/shikoyud/Android-App-Project
2. Kiểm tra xem code đã xuất hiện chưa
3. Kiểm tra branch `main`
4. Kiểm tra commit history

---

## 📞 CẦN HỖ TRỢ?

Nếu gặp lỗi, hãy chụp màn hình thông báo lỗi và:
1. Kiểm tra lại quyền truy cập repository
2. Đảm bảo đã tạo Personal Access Token đúng cách
3. Thử fork repository hoặc tạo repository mới

---

**Cập nhật**: 12/11/2025

