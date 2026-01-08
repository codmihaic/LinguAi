package com.example.linguai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.linguai.ui.LinguAIApp
import com.example.linguai.ui.theme.LinguAITheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinguAIApp()
        }
    }
}