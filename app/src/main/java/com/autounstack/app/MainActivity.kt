package com.autounstack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
    val initialLockScreenState = remember { preferencesManager.isLockScreenServiceEnabled() }
    val isEnabled = remember { mutableStateOf(initialState) }
    val isLockScreenEnabled = remember { mutableStateOf(initialLockScreenState) }
    val scrollState = remember { ScrollState(initial = 0) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Surface(
                color = Color(0xFF1B1B1F),
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Auto Unstack",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .clipToBounds()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Automatically expands grouped Samsung notifications using Android Accessibility Service. No personal data is collected.",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth()
                )

                SettingSwitch(
                    label = "Enable Auto Unstack",
                    checked = isEnabled.value,
                    onCheckedChange = { newState ->
                        isEnabled.value = newState
                        preferencesManager.setServiceEnabled(newState)
                    },
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                SettingSwitch(
                    label = "Enable Lock Screen Auto Unstack",
                    checked = isLockScreenEnabled.value,
                    onCheckedChange = { newState ->
                        isLockScreenEnabled.value = newState
                        preferencesManager.setLockScreenServiceEnabled(newState)
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Note:")
                        }
                        append(" Enabling lock screen notification unstacking might add an extra step cuz you have to scroll through notifications till the end to unlock ur phone. But apparently I never knew till now and realised when building this lock screen feature that even when the screen is completely off and black, you can place ur finger where the fingerprint sensor usually is and unlock directly and you don't have to press power button to press the finger print scanner. So you can enable this setting safely and look at ur notifications unstacked on the lock screen.")
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "How to set up:",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "1. Go to Settings → Accessibility → Installed apps → Auto Unstack. It may appear disabled, but tap it anyway. When the “Unable to activate” popup appears, close it.",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = "2. Go to Settings → Apps → Auto Unstack → ⋮ (three dots) → Allow restricted settings.",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = "3. Go to Settings → Accessibility → Installed apps → Auto Unstack → turn it on.",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = "4. Choose whether to enable Auto Unstack for the unlocked notification shade, the lock screen, or both.",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = "5. Open or scroll through your notifications — visible grouped notifications expand automatically.",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "On some Samsung devices, also disable battery optimization for Auto Unstack so the service stays running.",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SettingSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
