# 🔀 HƯỚNG DẪN MERGE VÀ FIX CONFLICT

## 📋 NHỮNG GÌ ĐÃ LÀM

### 1. Tạo nhánh `tuong-huy`
```bash
git checkout -b tuong-huy
```

### 2. Push nhánh lên GitHub
```bash
git add .
git commit -m "feat: Complete database and repository layer"
git push -u origin tuong-huy
```

### 3. Merge nhánh `capture-edit-photo`
```bash
git fetch origin
git merge origin/capture-edit-photo
```

---

## ⚠️ XỬ LÝ CONFLICT

### Các file có thể bị conflict:

#### 1. **build.gradle.kts (app level)**
**Nguyên nhân:** Cả 2 nhánh đều thêm dependencies mới

**Cách fix:**
- Giữ tất cả dependencies từ cả 2 nhánh
- Xóa các dòng trùng lặp
- Đảm bảo thứ tự hợp lý

**Ví dụ:**
```kotlin
dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    
    // Compose (từ cả 2 nhánh)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    
    // Camera (từ capture-edit-photo)
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    
    // Image Processing (từ capture-edit-photo)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("jp.co.cyberagent.android:gpuimage:2.1.0")
    
    // Database (từ tuong-huy)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    
    // Hilt DI (từ tuong-huy)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    
    // WorkManager (từ tuong-huy)
    implementation(libs.androidx.work.runtime.ktx)
}
```

#### 2. **AndroidManifest.xml**
**Nguyên nhân:** Cả 2 nhánh thêm permissions

**Cách fix:**
```xml
<manifest>
    <!-- Từ tuong-huy -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
    
    <!-- Từ capture-edit-photo -->
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-feature android:name="android.hardware.camera" android:required="true"/>
    <uses-feature android:name="android.hardware.camera.front" android:required="false"/>
    
    <application>
        <!-- Receivers từ tuong-huy -->
        <receiver android:name=".notifications.BootReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED"/>
            </intent-filter>
        </receiver>
        
        <!-- Activities - GIỮ CẢ 2 -->
    </application>
</manifest>
```

#### 3. **MainActivity.kt**
**Nguyên nhân:** Cả 2 nhánh sửa navigation

**Cách fix:**
- Merge cả 2 navigation routes
- Đảm bảo tất cả screens đều được định nghĩa

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        // Từ tuong-huy
        composable("home") { HomeScreen(navController) }
        composable("gallery") { GalleryScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("reminder_settings") { ReminderSettingsScreen(navController) }
        
        // Từ capture-edit-photo
        composable("capture") { CaptureScreen(navController) }
        composable("preview/{photoUri}") { backStackEntry ->
            val photoUri = backStackEntry.arguments?.getString("photoUri")
            PreviewScreen(navController, photoUri)
        }
        composable("edit/{photoUri}") { backStackEntry ->
            val photoUri = backStackEntry.arguments?.getString("photoUri")
            EditScreen(navController, photoUri)
        }
        
        // Từ cả 2 nhánh
        composable("detail/{photoId}") { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId")?.toLongOrNull()
            if (photoId != null) {
                DetailScreen(navController, photoId)
            }
        }
    }
}
```

#### 4. **di/AppModule.kt (Hilt Module)**
**Cách fix:**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    // Từ tuong-huy - Database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "selfie_diary_db"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideSelfieDao(database: AppDatabase): SelfieDao {
        return database.selfieDao()
    }
    
    @Provides
    @Singleton
    fun provideSelfieRepository(
        dao: SelfieDao,
        fileManager: FileManager
    ): SelfieRepository {
        return SelfieRepositoryImpl(dao, fileManager)
    }
    
    // Từ capture-edit-photo - Camera & Storage
    @Provides
    @Singleton
    fun provideCameraHelper(@ApplicationContext context: Context): CameraHelper {
        return CameraHelper(context)
    }
    
    @Provides
    @Singleton
    fun provideFileManager(@ApplicationContext context: Context): FileManager {
        return FileManager(context)
    }
    
    @Provides
    @Singleton
    fun provideFilterEngine(): FilterEngine {
        return FilterEngine()
    }
    
    @Provides
    @Singleton
    fun provideImageEditor(@ApplicationContext context: Context): ImageEditor {
        return ImageEditor(context)
    }
}
```

---

## 🛠️ CÁCH FIX CONFLICT BẰNG ANDROID STUDIO

### Cách 1: Dùng Visual Conflict Resolver (KHUYẾN NGHỊ)

1. **Mở Android Studio**

2. **Vào menu:** `VCS` → `Git` → `Resolve Conflicts`

3. **Hoặc click vào thông báo:** "Resolve conflicts" ở góc dưới bên phải

4. **Chọn file bị conflict:**
   - Danh sách các file conflict sẽ hiện ra
   - Click từng file để xem và fix

5. **Trong Merge Dialog:**
   - **Left panel (Your changes):** Code của bạn (tuong-huy)
   - **Center panel (Result):** Kết quả sau khi merge
   - **Right panel (Their changes):** Code từ capture-edit-photo
   
6. **Chọn code cần giữ:**
   - Click `>>` hoặc `<<` để chọn code từ một bên
   - Hoặc click `X` để bỏ cả 2
   - Hoặc edit trực tiếp ở center panel
   
7. **Click "Apply" sau khi fix xong**

8. **Đánh dấu resolved:**
   - Click "Mark as Resolved"
   - Hoặc: `VCS` → `Git` → `Add`

9. **Làm tương tự cho tất cả các file conflict**

### Cách 2: Edit thủ công trong Editor

1. **Mở file bị conflict**

2. **Tìm các dấu hiệu conflict:**
```
<<<<<<< HEAD
// Code của bạn (tuong-huy)
=======
// Code từ capture-edit-photo
>>>>>>> origin/capture-edit-photo
```

3. **Quyết định giữ code nào:**
   - Giữ code của bạn: Xóa phần từ `=======` đến `>>>>>>>`
   - Giữ code từ capture-edit-photo: Xóa phần từ `<<<<<<<` đến `=======`
   - Giữ cả 2: Xóa các dấu conflict, sắp xếp code hợp lý

4. **Xóa tất cả các dấu:** `<<<<<<<`, `=======`, `>>>>>>>`

5. **Save file**

6. **Mark as resolved:**
```bash
git add <file_name>
```

---

## ✅ SAU KHI FIX TẤT CẢ CONFLICT

### 1. Kiểm tra không còn conflict
```bash
git status
# Không còn dòng "both modified" màu đỏ
```

### 2. Build project để kiểm tra
- Click: `Build` → `Rebuild Project`
- Đảm bảo không có lỗi compile

### 3. Commit merge
```bash
git add .
git commit -m "fix: Resolve merge conflicts between tuong-huy and capture-edit-photo"
```

### 4. Push lên GitHub
```bash
git push origin tuong-huy
```

---

## 🧪 TESTING SAU KHI MERGE

### Checklist:
- [ ] App build thành công
- [ ] Database tạo được và lưu data OK
- [ ] Repository functions hoạt động bình thường
- [ ] Camera mở được và chụp ảnh OK
- [ ] Filter & Edit ảnh hoạt động
- [ ] Lưu ảnh sau khi edit OK
- [ ] Hiển thị ảnh trên Gallery OK
- [ ] Delete ảnh hoạt động (cả database lẫn file)
- [ ] Notification vẫn hoạt động
- [ ] Settings vẫn OK

---

## 🚨 NẾU GẶP VẤN ĐỀ

### Lỗi: "Cannot merge, uncommitted changes"
```bash
git stash
git merge origin/capture-edit-photo
git stash pop
```

### Lỗi: "Merge conflict in binary file"
```bash
# Chọn file từ một nhánh
git checkout --theirs path/to/file  # Lấy từ capture-edit-photo
# Hoặc
git checkout --ours path/to/file    # Giữ file của mình
```

### Lỗi build sau khi merge
1. Sync Gradle: `File` → `Sync Project with Gradle Files`
2. Clean: `Build` → `Clean Project`
3. Rebuild: `Build` → `Rebuild Project`
4. Invalidate Caches: `File` → `Invalidate Caches` → `Invalidate and Restart`

### Muốn hủy merge và bắt đầu lại
```bash
git merge --abort
```

---

## 📞 LIÊN HỆ

Nếu gặp conflict phức tạp không fix được:
1. Chụp ảnh màn hình conflict
2. Hỏi trong group chat
3. Hoặc nhờ Leader (Thành viên 1) hỗ trợ

---

**Chúc bạn merge thành công! 🎉**

