# Hướng dẫn Chạy Project Nhật Ký Selfie

## 🎯 Yêu cầu hệ thống

- **Android Studio**: Hedgehog (2023.1.1) hoặc mới hơn
- **JDK**: Java 17
- **Android SDK**: API Level 24 trở lên (Android 7.0+)
- **Gradle**: 8.1.4 (đã được cấu hình sẵn)

## 📋 Các bước để chạy project

### Bước 1: Mở Project trong Android Studio

1. Mở Android Studio
2. Chọn **File** → **Open**
3. Tìm đến thư mục `D:\SGU Nam 3 HK1\Mobile\NhatKySelfie`
4. Nhấn **OK**

### Bước 2: Sync Project với Gradle

#### Cách 1: Sử dụng nút Sync (Khuyên dùng)

1. Sau khi mở project, Android Studio sẽ tự động hiển thị thanh thông báo ở trên cùng
2. Nhấn vào nút **"Sync Now"** trên thanh thông báo
3. Hoặc nhấn vào biểu tượng **con voi (Gradle)** 🐘 trên thanh công cụ
4. Chờ quá trình sync hoàn tất (có thể mất 2-5 phút lần đầu)

#### Cách 2: Sử dụng Menu

1. Chọn **File** → **Sync Project with Gradle Files**
2. Chờ quá trình sync hoàn tất

#### Cách 3: Sử dụng Terminal

Nếu các cách trên không hoạt động, bạn có thể sử dụng terminal:

```cmd
cd "D:\SGU Nam 3 HK1\Mobile\NhatKySelfie"
gradlew.bat clean build
```

### Bước 3: Chạy ứng dụng

#### Chạy trên Emulator (Máy ảo)

1. Mở **AVD Manager** (Tools → Device Manager)
2. Tạo một thiết bị ảo mới nếu chưa có:
   - Chọn **Create Device**
   - Chọn Pixel 5 hoặc thiết bị tương tự
   - Chọn API Level 34 (Android 14) hoặc cao hơn
   - Nhấn **Finish**
3. Khởi động emulator
4. Nhấn nút **Run** ▶️ (hoặc Shift+F10)

#### Chạy trên thiết bị thật

1. Bật **Developer Options** và **USB Debugging** trên điện thoại
2. Kết nối điện thoại với máy tính qua USB
3. Chọn thiết bị trong Android Studio
4. Nhấn nút **Run** ▶️

### Bước 4: Kiểm tra lỗi

Nếu gặp lỗi trong quá trình sync hoặc build:

1. **Lỗi Gradle**: Chọn **File** → **Invalidate Caches** → **Invalidate and Restart**
2. **Lỗi Dependencies**: Kiểm tra kết nối internet và thử sync lại
3. **Lỗi JDK**: Đảm bảo đã cài Java 17, vào **File** → **Project Structure** → **SDK Location** để kiểm tra

## 🏗️ Cấu trúc Project

```
NhatKySelfie/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/hytu4535/selfiediary/
│   │   │   │   ├── MainActivity.kt              # Activity chính
│   │   │   │   ├── App.kt                       # Application class với Hilt
│   │   │   │   ├── ui/                          # Giao diện người dùng
│   │   │   │   │   ├── home/                    # Màn hình chính
│   │   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   │   └── HomeViewModel.kt
│   │   │   │   │   ├── capture/                 # Màn hình chụp ảnh
│   │   │   │   │   │   ├── CaptureScreen.kt
│   │   │   │   │   │   └── CaptureViewModel.kt
│   │   │   │   │   ├── gallery/                 # Thư viện ảnh
│   │   │   │   │   │   ├── GalleryScreen.kt
│   │   │   │   │   │   └── GalleryViewModel.kt
│   │   │   │   │   ├── settings/                # Cài đặt
│   │   │   │   │   │   ├── SettingsScreen.kt
│   │   │   │   │   │   └── ReminderSettingsScreen.kt
│   │   │   │   │   └── common/
│   │   │   │   │       ├── theme/               # Theme & Colors
│   │   │   │   │       └── navigation/          # Navigation
│   │   │   │   ├── data/                        # Lớp dữ liệu
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── dao/                 # Room DAO
│   │   │   │   │   │   ├── db/                  # Database
│   │   │   │   │   │   └── entities/            # Database Entities
│   │   │   │   │   ├── repository/              # Repositories
│   │   │   │   │   └── storage/                 # File Manager
│   │   │   │   ├── domain/                      # Business Logic
│   │   │   │   │   ├── model/                   # Domain Models
│   │   │   │   │   └── usecase/                 # Use Cases
│   │   │   │   ├── di/                          # Dependency Injection
│   │   │   │   │   ├── AppModule.kt
│   │   │   │   │   └── RepositoryModule.kt
│   │   │   │   ├── camera/                      # Camera Helper
│   │   │   │   ├── notifications/               # Notification Helper
│   │   │   │   └── util/                        # Utilities
│   │   │   ├── AndroidManifest.xml
│   │   │   └── res/                             # Resources
│   │   └── test/
│   └── build.gradle.kts                         # App build config
├── build.gradle.kts                             # Project build config
├── settings.gradle.kts
└── gradle.properties
```

## 📦 Thư viện đã sử dụng

- **Jetpack Compose**: UI hiện đại
- **Hilt**: Dependency Injection
- **Room Database**: Lưu trữ dữ liệu local
- **CameraX**: Xử lý camera
- **Coil**: Load và hiển thị ảnh
- **WorkManager**: Lên lịch nhắc nhở
- **Navigation Compose**: Điều hướng giữa các màn hình
- **DataStore**: Lưu preferences

## 🎨 Tính năng hiện tại

### ✅ Đã hoàn thành:

1. **Giao diện chính (Home Screen)**
   - Hiển thị lưới ảnh selfie
   - Nhóm ảnh theo ngày
   - Tính năng "Ngày này năm xưa"
   - FAB để chụp ảnh nhanh

2. **Thư viện ảnh (Gallery Screen)**
   - Xem tất cả ảnh dạng lưới
   - Chọn nhiều ảnh
   - Xóa ảnh (có xác nhận)

3. **Cài đặt (Settings Screen)**
   - Cài đặt nhắc nhở
   - Các tùy chọn khác (Time-lapse, Backup, Security)

4. **Cài đặt nhắc nhở (Reminder Settings)**
   - Bật/tắt nhắc nhở
   - Chọn giờ và phút

5. **Kiến trúc Clean Architecture**
   - Data Layer (Repository, DAO, Database)
   - Domain Layer (Models, UseCases)
   - Presentation Layer (ViewModels, Screens)

### 🚧 Cần hoàn thiện:

1. **Chức năng Camera**
   - Tích hợp CameraX
   - Chụp ảnh và lưu vào bộ nhớ riêng

2. **Chức năng File Management**
   - Lưu ảnh vào internal storage
   - Quản lý file

3. **Chức năng Nhắc nhở**
   - Lên lịch với WorkManager
   - Gửi notification

4. **Chức năng nâng cao**
   - Filter & Crop ảnh
   - Thêm emoji/note
   - Time-lapse video
   - Backup & Sync
   - Security (PIN/Biometric)

## 🐛 Troubleshooting

### Lỗi: "Sync failed"
- Kiểm tra kết nối internet
- Thử **File** → **Invalidate Caches** → **Invalidate and Restart**

### Lỗi: "SDK not found"
- Vào **File** → **Project Structure** → **SDK Location**
- Đảm bảo Android SDK đã được cài đặt đúng

### Lỗi: "Build failed"
- Chạy **Build** → **Clean Project**
- Sau đó **Build** → **Rebuild Project**

### Lỗi: "Gradle sync error"
- Xóa thư mục `.gradle` trong project
- Xóa thư mục `.idea` trong project  
- Mở lại project và sync

## 📞 Hỗ trợ

Nếu gặp vấn đề, hãy kiểm tra:
1. **Build Output**: Xem chi tiết lỗi
2. **Logcat**: Kiểm tra log runtime
3. **Gradle Console**: Xem chi tiết quá trình build

---
**Chúc bạn code vui vẻ! 🎉**

