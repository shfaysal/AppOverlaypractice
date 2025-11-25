package com.example.appoverlaypractice

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appoverlaypractice.ui.theme.AppOverlayPracticeTheme

class MainActivity : ComponentActivity() {

    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (hasOverlayPermission()) {
            startOverlayService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppOverlayPracticeTheme {
                MainScreen(::onShowOverlayClicked)
            }
        }
    }

    private fun onShowOverlayClicked() {
        if (hasOverlayPermission()) {
            startOverlayService()
        } else {
            requestOverlayPermission()
        }
    }

    private fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        settingsLauncher.launch(intent)
    }

    private fun startOverlayService() {
        if (hasOverlayPermission()) {
            Toast.makeText(this, "Starting service to show overlay in 30 seconds", Toast.LENGTH_SHORT).show()
            startService(Intent(this, OverlayService::class.java))
        } else {
            Toast.makeText(this, "Overlay permission is required", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun MainScreen(onShowOverlayClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(Color.Green)
        )
        Button(onClick = onShowOverlayClicked) {
            Text("Show Overlay in 30s")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppOverlayPracticeTheme {
        MainScreen {}
    }
}