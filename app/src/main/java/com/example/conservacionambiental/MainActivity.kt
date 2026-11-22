
package com.example.conservacionambiental

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.conservacionambiental.ui.areas.AreasScreen
import com.example.conservacionambiental.ui.theme.ConservacionAmbientalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConservacionAmbientalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AreasScreen()
                }
            }
        }
    }
}
