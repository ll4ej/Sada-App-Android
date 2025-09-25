package com.sada.app

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sada.app.ui.theme.SadaAppTheme

class MainActivity : ComponentActivity() {

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.NEARBY_WIFI_DEVICES
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.entries.all { it.value }) {
                    // All permissions granted. Start the app.
                    loadAppContent()
                } else {
                    // Permissions denied. Show a message.
                    setContent {
                        SadaAppTheme {
                            PermissionsDeniedScreen { requestPermissions() }
                        }
                    }
                }
            }

        requestPermissionLauncher.launch(permissions)
    }

    private fun requestPermissions() {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}.launch(permissions)
    }

    private fun loadAppContent() {
        setContent {
            SadaAppTheme {
                // This is where our main app UI will go.
                // For now, it's just a placeholder.
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Text("Hello, Sada App!")
                }
            }
        }
    }
}

@Composable
fun PermissionsDeniedScreen(onRequestAgain: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Permissions Required",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Sada needs Bluetooth and Location permissions to discover nearby users. The app cannot work without them.",
                modifier = Modifier.padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
            Button(onClick = onRequestAgain) {
                Text("Grant Permissions")
            }
        }
    }
}
