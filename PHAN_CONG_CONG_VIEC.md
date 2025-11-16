# PHÂN CÔNG CÔNG VIỆC ĐỒ ÁN - NHẬT KÝ SELFIE

## 📋 Tổng quan dự án
**Số thành viên:** 4 người  
**Thời gian ước tính:** 1.5 tuần  
**Kiến trúc:** Clean Architecture + MVVM + Jetpack Compose + Hilt DI  

---

## 👥 PHÂN CÔNG CHI TIẾT

### 🔵 **THÀNH VIÊN 1 (Nguyễn Nhật Duy): LEADER & CAMERA + STORAGE**
**Vai trò:** Team Leader, quản lý dự án và phát triển module Camera & Storage

#### Nhiệm vụ chính:
1. **Quản lý dự án** (10%)
   - Họp nhóm hàng tuần, theo dõi tiến độ
   - Giải quyết conflict khi merge code
   - Review code của các thành viên khác
   - Tích hợp các module lại với nhau

2. **Camera Module** (45%)
   - ✅ Hoàn thiện CameraHelper.kt (CameraX API)
   - ✅ CaptureScreen.kt - Giao diện chụp ảnh
   - ✅ CaptureViewModel.kt - Logic chụp và lưu ảnh
   - ✅ Xử lý quyền CAMERA runtime
   - 🔲 **Chức năng Filter & Edit sau khi chụp:**
     - Thêm các filter cơ bản (Black&White, Sepia, Vintage, Warm, Cool)
     - Crop ảnh (vuông 1:1, chân dung 3:4, ngang 16:9)
     - Xoay ảnh (90°, 180°, 270°)
     - Giao diện preview và chỉnh sửa
   - 🔲 **Preview và Confirm:**
     - Màn hình preview sau khi chụp
     - Nút: Chụp lại / Chỉnh sửa / Lưu

3. **Storage Module** (45%)
   - ✅ FileManager.kt - Quản lý file ảnh
   - 🔲 **Cải thiện FileManager:**
     - Lưu ảnh vào thư mục riêng tư (getExternalFilesDir)
     - Đảm bảo ảnh KHÔNG xuất hiện trong Gallery
     - Tạo file .nomedia trong thư mục
     - Tối ưu dung lượng ảnh (compress)
   - 🔲 **Metadata Management:**
     - Lưu EXIF data (ngày giờ chụp, vị trí)
     - Quản lý version ảnh (gốc + đã edit)

#### File cần làm việc:
```
app/src/main/java/com/hytu4535/selfiediary/
├── camera/
│   ├── CameraHelper.kt ✅
│   ├── FilterEngine.kt 🔲 (MỚI)
│   └── ImageEditor.kt 🔲 (MỚI)
├── ui/capture/
│   ├── CaptureScreen.kt ✅
│   ├── CaptureViewModel.kt ✅
│   ├── PreviewScreen.kt 🔲 (MỚI)
│   └── EditScreen.kt 🔲 (MỚI)
└── data/storage/
    ├── FileManager.kt ✅
    └── ImageCompressor.kt 🔲 (MỚI)
```

#### Thời gian:
- Tuần 1: Setup Camera + Chụp ảnh cơ bản + Review code nhóm
- Tuần 2: Filter & Edit module + Tích hợp Storage
- Tuần 3: Optimize & Testing + Tích hợp các module
- Tuần 4: Bug fixing + Polish UI + Review tổng thể

---

### 🟢 **THÀNH VIÊN 2 (Nguyễn Tường Huy): DATABASE + REPOSITORY + BUSINESS LOGIC**
**Vai trò:** Backend Developer, xây dựng tầng Data & Domain

#### Nhiệm vụ chính:
1. **Database Layer** (40%)
   - ✅ AppDatabase.kt - Room Database setup
   - ✅ SelfieEntity.kt - Entity model
   - ✅ SelfieDao.kt - Data Access Object
   - 🔲 **Cải thiện Database:**
     - Thêm field: note (ghi chú), emoji (cảm xúc), tags
     - Thêm field: isEdited, editedFilePath
     - Thêm field: isSynced (cho cloud backup)
     - Migration strategy khi update schema
     - Database backup & restore
   - 🔲 **Query nâng cao:**
     - Tìm ảnh theo ngày/tháng/năm
     - Tìm ảnh "On This Day" (cùng ngày năm trước)
     - Search theo note, emoji, tags
     - Statistics (số ảnh theo tháng, emoji phổ biến)

2. **Repository Pattern** (35%)
   - ✅ SelfieRepository interface
   - ✅ SelfieRepositoryImpl.kt
   - 🔲 **Hoàn thiện Repository:**
     - CRUD operations đầy đủ
     - Kết hợp Room + FileManager
     - Error handling & retry logic
     - Caching strategy
     - Batch operations (xóa nhiều ảnh)

3. **Domain Layer - Use Cases** (25%)
   - ✅ SaveSelfieUseCase.kt
   - ✅ GetAllSelfiesUseCase.kt
   - ✅ DeleteSelfiesUseCase.kt
   - ✅ GetOnThisDayUseCase.kt
   - 🔲 **Use Cases mới:**
     - UpdateNoteAndEmojiUseCase.kt
     - SearchSelfiesUseCase.kt
     - GetStatisticsUseCase.kt
     - ExportSelfiesUseCase.kt (cho backup)
     - ApplyFilterUseCase.kt

#### File cần làm việc:
```
app/src/main/java/com/hytu4535/selfiediary/
├── data/
│   ├── local/
│   │   ├── db/AppDatabase.kt ✅
│   │   ├── entities/SelfieEntity.kt ✅ (CẬP NHẬT)
│   │   └── dao/SelfieDao.kt ✅ (CẬP NHẬT)
│   └── repository/
│       ├── SelfieRepository.kt ✅
│       └── SelfieRepositoryImpl.kt ✅ (CẬP NHẬT)
└── domain/
    ├── model/
    │   ├── SelfieEntry.kt ✅
    │   └── OnThisDayEntry.kt ✅
    └── usecase/
        ├── SaveSelfieUseCase.kt ✅
        ├── GetAllSelfiesUseCase.kt ✅
        ├── DeleteSelfiesUseCase.kt ✅
        ├── GetOnThisDayUseCase.kt ✅
        ├── UpdateNoteAndEmojiUseCase.kt 🔲 (MỚI)
        ├── SearchSelfiesUseCase.kt 🔲 (MỚI)
        └── GetStatisticsUseCase.kt 🔲 (MỚI)
```

#### Thời gian:
- Tuần 1: Cải thiện Database schema + Migration + Testing
- Tuần 2: Hoàn thiện Repository + Error handling
- Tuần 3: Implement các Use Cases mới + Unit testing
- Tuần 4: Integration testing + Optimize queries

---

### 🟡 **THÀNH VIÊN 3 (Triệu Phú Lâm): HOME SCREEN + GALLERY + DETAIL VIEW**
**Vai trò:** Frontend Developer, xây dựng giao diện chính

#### Nhiệm vụ chính:
1. **Home Screen - Màn hình chính** (40%)
   - ✅ HomeScreen.kt - Giao diện cơ bản
   - ✅ HomeViewModel.kt
   - 🔲 **Hoàn thiện Home Screen:**
     - Top AppBar: Title + Settings icon
     - "On This Day" section (card nổi bật ở đầu)
     - Photo Grid với RecyclerView/LazyVerticalGrid
     - Group by date headers ("Hôm nay", "Hôm qua", "17/10/2025")
     - FAB button (chụp ảnh mới)
     - Pull-to-refresh
     - Empty state (khi chưa có ảnh)
   - 🔲 **Multi-select mode:**
     - Long press để vào chế độ chọn nhiều
     - Contextual Action Bar (số ảnh đã chọn + nút xóa)
     - Checkbox overlay trên ảnh
     - Select all / Deselect all

2. **Gallery View - Xem danh sách** (30%)
   - ✅ GalleryScreen.kt
   - ✅ GalleryViewModel.kt
   - 🔲 **Cải thiện Gallery:**
     - Grid layout (2-3 cột)
     - Lazy loading (phân trang)
     - Smooth scrolling
     - Item decoration (spacing)
     - Hiển thị emoji/note trên thumbnail
     - Filter by date range
     - Sort options (mới nhất/cũ nhất)

3. **Detail View - Xem chi tiết ảnh** (30%)
   - 🔲 **DetailScreen.kt (MỚI):**
     - Full-screen image viewer
     - Pinch-to-zoom (zoom in/out)
     - Swipe to navigate (ảnh trước/sau)
     - Bottom sheet info (ngày giờ, note, emoji)
     - Action buttons: Edit note/emoji, Delete, Share
     - Confirm dialog khi xóa
   - 🔲 **Note & Emoji Editor:**
     - Bottom sheet để thêm/sửa note
     - Emoji picker dialog
     - Save changes to database

#### File cần làm việc:
```
app/src/main/java/com/hytu4535/selfiediary/
├── ui/home/
│   ├── HomeScreen.kt ✅ (CẬP NHẬT TOÀN BỘ)
│   ├── HomeViewModel.kt ✅ (CẬP NHẬT)
│   └── components/
│       ├── OnThisDayCard.kt 🔲 (MỚI)
│       ├── PhotoGridItem.kt 🔲 (MỚI)
│       └── DateHeader.kt 🔲 (MỚI)
├── ui/gallery/
│   ├── GalleryScreen.kt ✅ (CẬP NHẬT)
│   ├── GalleryViewModel.kt ✅
│   └── components/
│       └── GalleryItem.kt 🔲 (MỚI)
└── ui/detail/
    ├── DetailScreen.kt 🔲 (MỚI)
    ├── DetailViewModel.kt 🔲 (MỚI)
    └── components/
        ├── ZoomableImage.kt 🔲 (MỚI)
        ├── NoteEditor.kt 🔲 (MỚI)
        └── EmojiPicker.kt 🔲 (MỚI)
```

#### Thời gian:
- Tuần 1: Hoàn thiện Home Screen + On This Day feature
- Tuần 2: Multi-select mode + Gallery improvements
- Tuần 3: Detail View + Zoom + Swipe navigation
- Tuần 4: Note & Emoji editor + Polish UI

---

### 🔴 **THÀNH VIÊN 4 (Nguyễn Hoàng Sang): NOTIFICATIONS + REMINDERS + SETTINGS**
**Vai trò:** System Developer, xây dựng hệ thống thông báo và cài đặt

#### Nhiệm vụ chính:
1. **Smart Reminder System** (50%)
   - ✅ ReminderScheduler.kt - Cơ bản
   - ✅ NotificationHelper.kt - Cơ bản
   - 🔲 **Hoàn thiện Reminder:**
     - WorkManager periodic work (hàng ngày)
     - Logic kiểm tra: Đã chụp hôm nay chưa?
     - Gửi notification nếu chưa chụp
     - Deep link: Click notification → mở Camera
     - Notification channel setup (Android 8+)
     - Notification permission (Android 13+)
   - 🔲 **Smart Logic:**
     - Tự động reschedule sau khi chụp
     - Không spam notification
     - Boot receiver (khởi động lại thiết bị)
     - Doze mode handling
     - Battery optimization handling

2. **Settings & Preferences** (30%)
   - ✅ SettingsScreen.kt - Cơ bản
   - ✅ ReminderSettingsScreen.kt
   - 🔲 **Hoàn thiện Settings:**
     - Time picker cho reminder
     - Enable/disable reminder toggle
     - Notification sound settings
     - Vibration settings
     - Preview notification
   - 🔲 **Advanced Settings:**
     - Storage info (dung lượng đã dùng)
     - Clear cache
     - Export/Import data
     - App theme (Light/Dark/System)
     - Language settings

3. **Backup & Sync** (20%)
   - 🔲 **Cloud Backup (Optional):**
     - Google Drive integration
     - Manual backup/restore
     - Auto backup schedule
     - Sync status indicator
   - 🔲 **Local Backup:**
     - Export to ZIP file
     - Import from backup
     - Backup settings

#### File cần làm việc:
```
app/src/main/java/com/hytu4535/selfiediary/
├── notifications/
│   ├── NotificationHelper.kt ✅ (CẬP NHẬT TOÀN BỘ)
│   ├── NotificationChannels.kt 🔲 (MỚI)
│   └── DeepLinkHandler.kt 🔲 (MỚI)
├── ui/reminder/
│   ├── ReminderScheduler.kt ✅ (CẬP NHẬT TOÀN BỘ)
│   ├── ReminderWorker.kt 🔲 (MỚI)
│   └── BootReceiver.kt 🔲 (MỚI)
├── ui/settings/
│   ├── SettingsScreen.kt ✅ (CẬP NHẬT)
│   ├── ReminderSettingsScreen.kt ✅ (CẬP NHẬT)
│   ├── SettingsViewModel.kt 🔲 (MỚI)
│   └── components/
│       ├── TimePickerDialog.kt 🔲 (MỚI)
│       └── SettingsItem.kt 🔲 (MỚI)
└── backup/
    ├── BackupManager.kt 🔲 (MỚI)
    └── GoogleDriveHelper.kt 🔲 (MỚI - OPTIONAL)
```

#### Thời gian:
- Tuần 1: WorkManager + Smart reminder logic + Testing
- Tuần 2: Notification system + Deep linking + Permissions
- Tuần 3: Settings UI + Preferences + Time picker
- Tuần 4: Backup/Restore + Testing + Polish

---

## 📅 TIMELINE TỔNG QUÁT

### **Tuần 1: Foundation (Nền tảng)**
- Thành viên 1: Camera cơ bản + Review code
- Thành viên 2: Database schema + Migration
- Thành viên 3: Home Screen layout
- Thành viên 4: Reminder WorkManager setup
- **Mục tiêu:** Chụp và lưu ảnh được, hiển thị trên Home

### **Tuần 2: Core Features (Tính năng chính)**
- Thành viên 1: Filter & Edit + Storage optimization
- Thành viên 2: Repository + Use Cases
- Thành viên 3: Multi-select + Gallery
- Thành viên 4: Notification system hoàn chỉnh
- **Mục tiêu:** Chụp → Edit → Lưu → Xem → Xóa OK

### **Tuần 3: Advanced Features (Tính năng nâng cao)**
- Thành viên 1: Preview & Confirm flow
- Thành viên 2: Search + Statistics + Testing
- Thành viên 3: Detail View + Note/Emoji
- Thành viên 4: Settings + Backup
- **Mục tiêu:** On This Day + Note/Emoji + Settings OK

### **Tuần 4: Polish & Testing (Hoàn thiện)**
- Tất cả: Bug fixing + UI/UX polish
- Thành viên 1: Tích hợp tất cả module
- Thành viên 2: Performance optimization
- Thành viên 3: UI/UX final touches
- Thành viên 4: Testing toàn diện
- **Mục tiêu:** App hoàn chỉnh, sẵn sàng demo

---

## 🔧 QUY TẮC LÀM VIỆC

### Git Workflow
```bash
# Mỗi thành viên tạo branch riêng
git checkout -b feature/camera-edit       # Thành viên 1
git checkout -b feature/database-notes    # Thành viên 2
git checkout -b feature/home-ui           # Thành viên 3
git checkout -b feature/notifications     # Thành viên 4

# Commit thường xuyên với message rõ ràng
git commit -m "feat: Add filter engine for photos"
git commit -m "fix: Fix crash when deleting multiple photos"

# Push và tạo Pull Request
git push origin feature/your-feature
# Sau đó tạo PR trên GitHub để Leader review
```

### Code Style
- Sử dụng Kotlin coding conventions
- Comment cho các hàm phức tạp
- Đặt tên biến/hàm có ý nghĩa (tiếng Anh)
- TODO comment cho phần chưa làm
- Format code trước khi commit (Ctrl+Alt+L)

### Testing
- Unit test cho Use Cases (Thành viên 2)
- UI test cho màn hình chính (Thành viên 3)
- Integration test cho Reminder (Thành viên 4)
- Manual testing trên thiết bị thật (Tất cả)

### Communication
- Họp nhóm: Thứ 2 & Thứ 6 hàng tuần (online/offline)
- Daily standup: Report tiến độ mỗi ngày qua group chat
- Báo cáo vấn đề: Tạo issue trên GitHub hoặc group chat
- Code review: Mọi PR cần ít nhất 1 người review (ưu tiên Leader)

---

## 📊 CHECKLIST HOÀN THÀNH

### Core Features (BẮT BUỘC) ✅
- [ ] Chụp ảnh bằng camera selfie
- [ ] Lưu ảnh vào thư mục riêng tư (không hiện Gallery)
- [ ] Hiển thị danh sách ảnh theo ngày
- [ ] Xem ảnh full-screen + pinch-to-zoom
- [ ] Xóa ảnh (đơn lẻ và nhiều ảnh)
- [ ] Reminder thông minh (kiểm tra đã chụp chưa)
- [ ] Click notification → mở Camera
- [ ] Settings: Cài đặt thời gian reminder

### Advanced Features (NÊN CÓ) 🎯
- [ ] Filter & Edit ảnh sau khi chụp
- [ ] Thêm note và emoji cho ảnh
- [ ] "On This Day" feature
- [ ] Search ảnh theo ngày/note
- [ ] Multi-select mode (chọn nhiều ảnh)
- [ ] Backup & Restore data

### Optional Features (NẾU CÓ THỜI GIAN) ⭐
- [ ] Time-lapse video từ ảnh
- [ ] Cloud sync (Google Drive)
- [ ] App lock (PIN/Fingerprint)
- [ ] Statistics dashboard
- [ ] Share ảnh ra ngoài

---

## 🆘 HỖ TRỢ & RESOURCES

### Documentation
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [CameraX](https://developer.android.com/training/camerax)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Hilt DI](https://developer.android.com/training/dependency-injection/hilt-android)

### Troubleshooting
- **Camera không hoạt động:** Kiểm tra permissions + CameraX version
- **Notification không hiện:** Kiểm tra notification channel + permissions (Android 13+)
- **WorkManager không chạy sau reboot:** Implement BootReceiver
- **Ảnh vẫn hiện trong Gallery:** Kiểm tra có file .nomedia chưa
- **Crash khi xóa ảnh:** Đảm bảo xóa cả file lẫn database record

### Contact
- **Leader (Thành viên 1):** Review code, giải quyết conflict
- **GitHub Issues:** Tạo issue cho bug/feature request
- **Group Chat:** Hỏi đáp nhanh, chia sẻ kinh nghiệm

---

## 🎯 MỤC TIÊU CUỐI CÙNG

**Sau 4 tuần, ứng dụng cần:**
1. ✅ Chụp, lưu, xem, xóa ảnh selfie hoàn hảo
2. ✅ Reminder thông minh, không spam, hoạt động ổn định
3. ✅ UI/UX đẹp, mượt, dễ sử dụng
4. ✅ Không có bug crash trên thiết bị thật
5. ✅ Code sạch, có comment, dễ maintain
6. ✅ Sẵn sàng demo và bảo vệ đồ án


