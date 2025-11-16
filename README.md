# 📸 Nhật Ký Selfie Mỗi Ngày

Ứng dụng Android giúp bạn ghi lại khoảnh khắc mỗi ngày qua ảnh selfie - một cuốn nhật ký hình ảnh cá nhân của riêng bạn.

---

## 📚 TÀI LIỆU DỰ ÁN

### 📋 Quản lý dự án
- **[PHAN_CONG_CONG_VIEC.md](./PHAN_CONG_CONG_VIEC.md)** - Phân công chi tiết cho 4 thành viên
- **[TOM_TAT_PHAN_CONG.md](./TOM_TAT_PHAN_CONG.md)** - Tóm tắt nhanh phân công
- **[PROGRESS_TRACKING.md](./PROGRESS_TRACKING.md)** - Theo dõi tiến độ hàng tuần
- **[SO_DO_KIEN_TRUC.md](./SO_DO_KIEN_TRUC.md)** - Sơ đồ kiến trúc & workflow

### 🚀 Hướng dẫn
- **[HOW_TO_RUN.md](./HOW_TO_RUN.md)** - Hướng dẫn chạy ứng dụng
- **[HUONG_DAN_PUSH_GITHUB.md](./HUONG_DAN_PUSH_GITHUB.md)** - Hướng dẫn đẩy code lên GitHub

---

## 👥 PHÂN CÔNG NHÓM (4 THÀNH VIÊN)

### 🔵 Thành viên 1: LEADER + CAMERA + STORAGE
- **Vai trò:** Team Leader, quản lý Camera & Lưu trữ
- **Module:** Camera (chụp, filter, edit), FileManager, Integration
- **File:** `camera/`, `ui/capture/`, `data/storage/`

### 🟢 Thành viên 2: DATABASE + REPOSITORY + LOGIC
- **Vai trò:** Backend Developer, Data & Domain Layer
- **Module:** Room Database, Repository, Use Cases
- **File:** `data/`, `domain/`

### 🟡 Thành viên 3: HOME + GALLERY + DETAIL UI
- **Vai trò:** Frontend Developer, Main UI
- **Module:** Home Screen, Gallery, Detail View, Note/Emoji
- **File:** `ui/home/`, `ui/gallery/`, `ui/detail/`

### 🔴 Thành viên 4: NOTIFICATIONS + SETTINGS
- **Vai trò:** System Developer, Background Services
- **Module:** Smart Reminder, Notification, Settings, Backup
- **File:** `notifications/`, `ui/reminder/`, `ui/settings/`, `backup/`

---

## 🌟 Tính năng chính

### ✅ Core Features (BẮT BUỘC)

1. **📷 Chụp & Lưu trữ riêng tư**
   - Chụp ảnh selfie bằng camera trước
   - Lưu vào thư mục riêng (không hiện Gallery)
   - File .nomedia để ẩn khỏi thư viện ảnh hệ thống

2. **🖼️ Giao diện chính**
   - Hiển thị lưới ảnh theo ngày
   - Nhóm: "Hôm nay", "Hôm qua", "17/10/2025"...
   - Tính năng "Ngày này năm xưa" 🎉
   - FAB button để chụp ảnh nhanh

3. **🔍 Xem chi tiết ảnh**
   - Full-screen viewer
   - Pinch-to-zoom (phóng to/thu nhỏ)
   - Swipe để xem ảnh trước/sau
   - Xóa ảnh (có xác nhận)

4. **🗑️ Quản lý ảnh nâng cao**
   - Chọn nhiều ảnh (long press)
   - Xóa nhiều ảnh cùng lúc
   - Contextual Action Bar

5. **🔔 Nhắc nhở thông minh**
   - Cài đặt thời gian nhắc (vd: 8:00 sáng)
   - Kiểm tra: Đã chụp hôm nay chưa?
   - Chỉ nhắc nếu chưa chụp
   - Hoạt động sau khi reboot (WorkManager)

6. **⚙️ Cài đặt**
   - Bật/tắt reminder
   - Chọn thời gian nhắc nhở
   - Xem dung lượng storage

### 🎯 Advanced Features (NÊN CÓ)

7. **🎨 Filter & Chỉnh sửa**
   - 5 filters: B&W, Sepia, Vintage, Warm, Cool
   - Crop: 1:1, 3:4, 16:9
   - Xoay: 90°, 180°, 270°

8. **📝 Note & Emoji**
   - Thêm ghi chú cho mỗi ảnh
   - Chọn emoji cảm xúc
   - Biến thành nhật ký cảm xúc

9. **🕐 On This Day**
   - Hiển thị ảnh cùng ngày năm trước
   - Tạo cảm giác hoài niệm

10. **🔍 Search & Statistics**
    - Tìm ảnh theo ngày/note/emoji
    - Thống kê số ảnh theo tháng

### ⭐ Optional Features (NẾU CÓ THỜI GIAN)

11. **🎬 Time-lapse Video**
    - Tạo video từ các ảnh
    - Chọn khoảng thời gian

12. **☁️ Backup & Sync**
    - Sao lưu lên Google Drive
    - Export/Import ZIP file

13. **🔒 Bảo mật**
    - Khóa app bằng PIN
    - Xác thực vân tay/face ID

---

## 🏗️ Kiến trúc ứng dụng

### Clean Architecture + MVVM

```
┌─────────────────────────────────────────┐
│  UI LAYER (Jetpack Compose)             │
│  ├── HomeScreen       (TV3)             │
│  ├── CaptureScreen    (TV1)             │
│  ├── DetailScreen     (TV3)             │
│  └── SettingsScreen   (TV4)             │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│  DOMAIN LAYER (Use Cases)               │
│  ├── SaveSelfieUseCase       (TV2)      │
│  ├── GetAllSelfiesUseCase    (TV2)      │
│  ├── DeleteSelfiesUseCase    (TV2)      │
│  └── GetOnThisDayUseCase     (TV2)      │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│  DATA LAYER (Repository)                │
│  ├── SelfieRepository        (TV2)      │
│  ├── Room Database           (TV2)      │
│  └── FileManager             (TV1)      │
└─────────────────────────────────────────┘
```

**Xem chi tiết:** [SO_DO_KIEN_TRUC.md](./SO_DO_KIEN_TRUC.md)

---

## 🛠️ Công nghệ sử dụng

- **Kotlin** - Ngôn ngữ lập trình chính
- **Jetpack Compose** - UI declarative, hiện đại
- **Hilt** - Dependency Injection
- **Room** - Local database (SQLite)
- **CameraX** - Camera API
- **Coil** - Load & cache ảnh
- **WorkManager** - Background tasks
- **DataStore** - Preferences
- **Kotlin Coroutines & Flow** - Async programming

---

## 📅 Timeline - 4 tuần

| Tuần | Mục tiêu | Deliverables |
|------|----------|--------------|
| **1** | Nền tảng | Camera cơ bản, Database, Home UI, Reminder setup |
| **2** | Core Features | Filter/Edit, Repository, Multi-select, Notification |
| **3** | Advanced | On This Day, Note/Emoji, Detail View, Settings |
| **4** | Polish & Testing | Bug fixing, UI/UX polish, Integration, Demo |

---

## 🚀 Hướng dẫn chạy dự án

### Yêu cầu hệ thống
- Android Studio Hedgehog (2023.1.1) trở lên
- JDK 17
- Android SDK 24+ (target 34)
- Kotlin 1.9.0+

### Cài đặt

```bash
# 1. Clone repository
git clone https://github.com/shikoyud/Android-App-Project.git

# 2. Mở Android Studio
# File -> Open -> Chọn thư mục dự án

# 3. Sync Gradle
# Android Studio sẽ tự động sync

# 4. Chạy ứng dụng
# Run -> Run 'app' hoặc Shift + F10
```

**Chi tiết:** [HOW_TO_RUN.md](./HOW_TO_RUN.md)

---

## 📊 Tiến độ dự án

- **Tuần hiện tại:** Tuần 1
- **Tiến độ:** 35% (Cấu trúc + UI cơ bản)
- **Còn lại:** Camera, Notification, Filter/Edit, Note/Emoji

**Theo dõi:** [PROGRESS_TRACKING.md](./PROGRESS_TRACKING.md)

---

## 🤝 Quy tắc làm việc

### Git Workflow
```bash
# Mỗi người làm branch riêng
git checkout -b feature/camera-edit       # TV1
git checkout -b feature/database-notes    # TV2
git checkout -b feature/home-ui           # TV3
git checkout -b feature/notifications     # TV4

# Commit thường xuyên
git commit -m "feat: Add filter engine"

# Push và tạo Pull Request
git push origin feature/your-branch
```

### Code Style
- Kotlin coding conventions
- Comment cho logic phức tạp
- Format code: Ctrl+Alt+L
- Review trước khi merge

### Họp nhóm
- **Thứ 2 & Thứ 6** hàng tuần
- Daily standup qua group chat
- Code review mọi PR

---

## 📞 Liên hệ & Hỗ trợ

- **Repository:** https://github.com/shikoyud/Android-App-Project
- **Issues:** Tạo issue trên GitHub
- **Group Chat:** [Link group chat]
- **Leader:** Thành viên 1

---

## 📄 License

Copyright © 2025 Nhóm Nhật Ký Selfie - SGU Mobile Development

---

**📱 Version:** 1.0.0  
**📅 Last Updated:** 16/11/2025  
**👥 Team:** 4 members  
**🚀 Status:** In Development
- **Navigation Compose**: Điều hướng

## 📱 Yêu cầu

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## 🚀 Hướng dẫn chạy

Xem chi tiết trong file [HOW_TO_RUN.md](HOW_TO_RUN.md)

**Tóm tắt nhanh:**
1. Mở project trong Android Studio
2. Nhấn nút **Sync Now** (🐘 Gradle Sync)
3. Chờ sync hoàn tất (2-5 phút lần đầu)
4. Nhấn **Run** ▶️

## 📂 Cấu trúc thư mục

```
app/src/main/java/com/hytu4535/selfiediary/
├── MainActivity.kt                 # Activity chính
├── App.kt                         # Application class
├── ui/                            # Giao diện
│   ├── home/                      # Màn hình chính
│   ├── capture/                   # Chụp ảnh
│   ├── gallery/                   # Thư viện
│   ├── settings/                  # Cài đặt
│   └── common/                    # Shared UI components
├── data/                          # Dữ liệu
│   ├── local/                     # Local storage
│   ├── repository/                # Repositories
│   └── storage/                   # File management
├── domain/                        # Business logic
│   ├── model/                     # Domain models
│   └── usecase/                   # Use cases
├── di/                            # Dependency Injection
├── camera/                        # Camera utilities
├── notifications/                 # Notifications
└── util/                          # Utilities
```

## 🎯 Mục tiêu đề tài

Xây dựng ứng dụng Android cho phép người dùng:
- Chụp và lưu trữ ảnh selfie riêng tư mỗi ngày
- Nhận nhắc nhở thông minh
- Xem lại hành trình thay đổi qua thời gian
- Tạo video time-lapse từ các ảnh
- Sao lưu và bảo mật dữ liệu

## 👨‍💻 Đóng góp

Đây là project đồ án, mọi đóng góp và góp ý đều được hoan nghênh!

## 📄 License

Đồ án môn học - SGU Nam 3 HK1

---

Made with ❤️ by SGU Students
- Hilt (Dependency Injection)

## Cấu trúc dự án
```
app/
├── domain/        # Business logic & models
├── data/          # Database, repository, storage
├── ui/            # UI components (Compose)
├── di/            # Dependency Injection
├── camera/        # Camera utilities
├── notifications/ # Reminder notifications
└── util/          # Helper utilities
```

