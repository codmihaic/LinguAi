package com.example.linguai.ui.splash

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.linguai.R
import com.example.linguai.nav.Screen
import com.example.linguai.ui.UiTextDictionary
import com.example.linguai.ui.UiTextKey
import com.example.linguai.ui.tSystem
import java.util.Locale

@Composable
fun SplashScreen(nav: NavController) {
    val config = LocalConfiguration.current

    val sysLang = remember(config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales[0]?.language ?: Locale.getDefault().language
        } else {
            @Suppress("DEPRECATION")
            config.locale.language
        }
    }

    val welcomeText = tSystem(UiTextKey.SplashWelcome)

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.splash_robot),
                contentDescription = "Splash",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        nav.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 72.dp, start = 24.dp, end = 24.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = welcomeText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color.Black.copy(alpha = 0.55f),
                            blurRadius = 12f
                        )
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}
