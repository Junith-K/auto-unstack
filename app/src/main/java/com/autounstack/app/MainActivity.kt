package com.autounstack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this)
        setContent {
            AppContent(preferencesManager = preferencesManager)
        }
    }
}

@Composable
private fun AppContent(preferencesManager: PreferencesManager) {
    val initialState = remember { preferencesManager.isServiceEnabled() }
    val isEnabled = remember { mutableStateOf(initialState) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Title
            Text(
                text = "Auto Unstack",
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Description
            Text(
                text = "Automatically expands grouped Samsung notifications using Android Accessibility Service. No personal data is collected.",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth()
            )

            // Toggle Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Enable Auto Unstack",
                    fontSize = 16.sp
                )
                Switch(
                    checked = isEnabled.value,
                    onCheckedChange = { newState ->
                        isEnabled.value = newState
                        preferencesManager.setServiceEnabled(newState)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // How to use section
            Text(
                text = "How to use:",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "1. Enable Accessibility Service",
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "2. Pull down notification shade",
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "3. Grouped notifications expand automatically",
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Battery optimization note
            Text(
                text = "Battery optimization may need to be disabled on some Samsung devices.",
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}
