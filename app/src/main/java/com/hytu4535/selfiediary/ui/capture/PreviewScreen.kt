package com.hytu4535.selfiediary.ui.capture

import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.hytu4535.selfiediary.camera.CameraHelper

@Composable
fun PreviewScreen(
    cameraHelper: CameraHelper,
    modifier: Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    ) { view ->
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(view.surfaceProvider)

        cameraHelper.startCamera(
            context,
            lifecycleOwner,
            preview
        )
    }
}