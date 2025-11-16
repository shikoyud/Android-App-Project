# 🎉 DATABASE & REPOSITORY ĐÃ XONG - HƯỚNG DẪN SỬ DỤNG

**Từ:** Nguyễn Tường Huy (Thành viên 2)  
**Ngày:** 16/11/2025  
**Status:** ✅ Hoàn thành 100%, sẵn sàng sử dụng

---

## 📢 THÔNG BÁO

Đã hoàn thành xong phần **Database & Repository**!
Sau đây là hướng dẫn sử dụng database & repository để code phần của mọi người

---

## 🎯 MỌI NGƯỜI CÓ THỂ LÀM GÌ?

### 🔵 **Thành viên 1 (Camera)** - Lưu ảnh sau khi chụp

```kotlin
// Trong CaptureViewModel.kt
@Inject lateinit var saveSelfieUseCase: SaveSelfieUseCase

fun savePhoto(filePath: String) {
    viewModelScope.launch {
        saveSelfieUseCase(
            SelfieEntry(
                filePath = filePath,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}
```

**✅ Chỉ cần gọi 1 hàm, vì database đã xong rồi

---

### 🟡 **Thành viên 3 (UI)** - Hiển thị, tìm kiếm, xóa ảnh

#### 1. Hiển thị tất cả ảnh (HomeScreen)
```kotlin
@Inject lateinit var getAllSelfiesUseCase: GetAllSelfiesUseCase

val selfies = getAllSelfiesUseCase()
    .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

// Trong Compose
val selfies by viewModel.selfies.collectAsState()
```

#### 2. "Ngày này năm xưa" (On This Day)
```kotlin
@Inject lateinit var getOnThisDayUseCase: GetOnThisDayUseCase

val onThisDay = getOnThisDayUseCase.getMostRecent()
    .stateIn(viewModelScope, SharingStarted.Lazily, null)

// Hiển thị: "Cùng ngày này ${entry.yearsAgo} năm trước"
```

#### 3. Xóa nhiều ảnh
```kotlin
@Inject lateinit var deleteSelfiesUseCase: DeleteSelfiesUseCase

fun deleteSelected(ids: List<Long>) {
    viewModelScope.launch {
        deleteSelfiesUseCase(ids)
    }
}
```

#### 4. Thêm note và emoji
```kotlin
@Inject lateinit var updateNoteAndEmojiUseCase: UpdateNoteAndEmojiUseCase

fun updateNote(selfieId: Long, note: String, emoji: String?) {
    viewModelScope.launch {
        updateNoteAndEmojiUseCase(selfieId, note, emoji)
    }
}
```

#### 5. Tìm kiếm ảnh
```kotlin
@Inject lateinit var searchSelfiesUseCase: SearchSelfiesUseCase

fun search(query: String) {
    searchSelfiesUseCase(query).collect { results ->
        // Hiển thị kết quả
    }
}
```

#### 6. Thống kê
```kotlin
@Inject lateinit var getStatisticsUseCase: GetStatisticsUseCase

viewModelScope.launch {
    getStatisticsUseCase().onSuccess { stats ->
        println("Tổng số ảnh: ${stats.totalCount}")
        println("Emoji phổ biến: ${stats.mostUsedEmojis}")
    }
}
```

**✅ Tất cả Use Cases đã sẵn sàng cho bạn dùng!**

---

### 🔴 **Thành viên 4 (Notification)** - Check đã chụp hôm nay chưa

```kotlin
// Trong ReminderWorker.kt
@Inject lateinit var hasSelfieTodayUseCase: HasSelfieTodayUseCase

override suspend fun doWork(): Result {
    val hasSelfieToday = hasSelfieTodayUseCase()
    
    if (!hasSelfieToday) {
        // Chưa chụp → Gửi notification nhắc nhở
        sendNotification()
    } else {
        // Đã chụp rồi → Không gửi gì
    }
    
    return Result.success()
}
```

**✅ Chỉ cần check 1 dòng: `hasSelfieTodayUseCase()` → true/false**

---

## ⚡ BẮT ĐẦU SỬ DỤNG (3 BƯỚC)

### Bước 1: Sync Gradle
```
File → Sync Project with Gradle Files
```

### Bước 2: Import Use Case vào ViewModel
```kotlin
@HiltViewModel
class YourViewModel @Inject constructor(
    private val yourUseCase: YourUseCase
) : ViewModel()
```

### Bước 3: Gọi Use Case
```kotlin
viewModelScope.launch {
    yourUseCase(params)
}

// Hoặc nếu là Flow
yourUseCase().collect { data ->
    // Xử lý data
}
```

**DONE! 🎉**

---

## 📚 TẤT CẢ USE CASES CÓ SẴN

| Use Case | Dùng cho | Thành viên |
|----------|----------|------------|
| `SaveSelfieUseCase` | Lưu ảnh sau khi chụp | TV1 |
| `GetAllSelfiesUseCase` | Hiển thị danh sách ảnh | TV3 |
| `GetOnThisDayUseCase` | "Ngày này năm xưa" | TV3 |
| `DeleteSelfiesUseCase` | Xóa nhiều ảnh | TV3 |
| `UpdateNoteAndEmojiUseCase` | Thêm note/emoji | TV3 |
| `SearchSelfiesUseCase` | Tìm kiếm ảnh | TV3 |
| `GetStatisticsUseCase` | Thống kê | TV3 |
| `HasSelfieTodayUseCase` | Check đã chụp hôm nay | TV4 |
| `GetSelfiesByMonthUseCase` | Lọc theo tháng | TV3 |
| `GetSelfiesByDateRangeUseCase` | Lọc theo khoảng ngày | TV3 |

---

## ⚠️ LƯU Ý QUAN TRỌNG

### ✅ ĐÚNG:
```kotlin
// Dùng Hilt injection
@Inject lateinit var useCase: UseCase

// Gọi trong coroutine
viewModelScope.launch {
    useCase(params)
}

// Collect Flow
useCase().collect { data -> }
```

### ❌ SAI:
```kotlin
// ĐỪNG tự khởi tạo
val useCase = UseCase() // ❌ Sai!

// ĐỪNG gọi Flow như suspend function
val data = useCase() // ❌ Sai! Đây là Flow, không phải data
```

---

## 🆘 GẶP VẤN ĐỀ?

**"Unresolved reference"**  
→ Sync Gradle: `File → Sync Project with Gradle Files`

**"Injection failed"**  
→ Kiểm tra `@HiltViewModel` trong ViewModel và `@AndroidEntryPoint` trong Activity

**"Database migration error"**  
→ Uninstall app cũ: `adb uninstall com.hytu4535.selfiediary` rồi chạy lại

**Không hiểu cách dùng?**  
→ Nhắn tui qua zalo

## ✅ TÓM TẮT

✅ Database đã hoàn thành với 7 fields mới (emoji, tags, note, etc.)  
✅ 10 Use Cases sẵn sàng cho team dùng  
✅ 0 bugs, đã test kỹ  
✅ Code đơn giản, dễ sử dụng  
✅ Có thể bắt đầu code ngay bây giờ!  

---

**Thành viên 2 - Nguyễn Tường Huy**  
*Database & Repository Developer*

