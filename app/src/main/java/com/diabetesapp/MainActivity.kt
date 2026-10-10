package com.diabetesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.diabetesapp.ui.theme.DiabetesAppTheme
import com.diabetesapp.ui.FoodTrackerApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiabetesAppTheme {
                FoodTrackerApp()
            }
        }
    }
}
