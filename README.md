# 📸 Nhật Ký Selfie Mỗi Ngày

Ứng dụng Android giúp bạn ghi lại khoảnh khắc mỗi ngày qua ảnh selfie - một cuốn nhật ký hình ảnh cá nhân của riêng bạn.

## 🌟 Tính năng chính

### ✅ Đã hoàn thành

1. **Giao diện chính đẹp mắt**
   - Hiển thị lưới ảnh selfie theo ngày
   - Nhóm ảnh tự động: "Hôm nay", "Hôm qua", "17/10/2025"...
   - Tính năng "Ngày này năm xưa" 🎉

2. **Thư viện ảnh**
   - Xem tất cả ảnh dạng lưới 3 cột
   - Chọn nhiều ảnh cùng lúc (long press)
   - Xóa nhiều ảnh với xác nhận

3. **Cài đặt & Nhắc nhở**
   - Cài đặt thời gian nhắc nhở hàng ngày
   - Bật/tắt nhắc nhở linh hoạt
   - Menu cài đặt đầy đủ

4. **Kiến trúc Clean & Modern**
   - MVVM + Clean Architecture
   - Jetpack Compose cho UI
   - Hilt Dependency Injection
   - Room Database
   - Kotlin Coroutines & Flow

### 🚧 Đang phát triển

- **Camera tích hợp**: Chụp selfie trực tiếp trong app
- **Lưu trữ riêng tư**: Ảnh không xuất hiện trong Gallery chung
- **Nhắc nhở thông minh**: WorkManager + Notification
- **Filter & Chỉnh sửa**: Crop, xoay, thêm filter
- **Ghi chú & Cảm xúc**: Thêm emoji, note cho mỗi ảnh
- **Time-lapse Video**: Tạo video từ các ảnh
- **Backup & Đồng bộ**: Google Drive, Dropbox
- **Bảo mật**: Khóa app bằng PIN/vân tay

## 🛠️ Công nghệ sử dụng

- **Kotlin**: Ngôn ngữ lập trình chính
- **Jetpack Compose**: UI hiện đại, declarative
- **Hilt**: Dependency Injection
- **Room**: Local database
- **CameraX**: Xử lý camera
- **Coil**: Load và cache ảnh
- **WorkManager**: Background tasks
- **DataStore**: Preferences
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

