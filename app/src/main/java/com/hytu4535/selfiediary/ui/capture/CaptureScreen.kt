package com.hytu4535.selfiediary.ui.capture

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureScreen(
    onBack: () -> Unit,
    onCaptureSuccess: (imagePath: String) -> Unit,
    viewModel: CaptureViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val flashMode by viewModel.flashMode.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    // CAMERA PERMISSION
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(android.Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        if (hasCameraPermission) {
            // CAMERA PREVIEW
            PreviewScreen(
                viewModel.cameraHelper,
                modifier = Modifier.fillMaxSize()
            )

            // GO BACK BUTTON
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Quay lại",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // TAKE PICTURE BUTTON
            FloatingActionButton(
                onClick = {
                    viewModel.cameraHelper.takePicture(
                        context = context,
                        onImageSaved = { savedUri ->
                            scope.launch(Dispatchers.Main) {
                                onCaptureSuccess(savedUri)
                            }
                        },
                        onError = { message ->
                            ContextCompat.getMainExecutor(context).execute {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                },
                containerColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = "Chụp",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            // TOGGLE LENS BUTTON
            IconButton(
                onClick = viewModel::toggleCameraLens,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 32.dp, end = 32.dp)
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Chuyển camera",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            // TOGGLE FLASH BUTTON
            IconButton(
                onClick = viewModel::changeFlashMode,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 32.dp, start = 32.dp)
            ) {
                val flashIcon = when (flashMode) {
                    ImageCapture.FLASH_MODE_ON -> Icons.Default.FlashOn
                    ImageCapture.FLASH_MODE_OFF -> Icons.Default.FlashOff
                    else -> Icons.Default.FlashAuto
                }
                Icon(
                    flashIcon,
                    contentDescription = "Chuyển flash mode",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }


        } else {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Quay lại",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                "Ứng dụng cần quyền camera để chụp ảnh.",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

