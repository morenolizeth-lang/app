package com.example.applicacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.applicacion.ui.theme.ApplicacionTheme
import com.example.applicacion.view.InicioScreen
import com.example.applicacion.view.MenuScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplicacionTheme {
                AppNavigation()
            }
        }
    }
}