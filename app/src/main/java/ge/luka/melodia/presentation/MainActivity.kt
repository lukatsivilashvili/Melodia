package ge.luka.melodia.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ge.luka.melodia.presentation.ui.MelodiaApp
import ge.luka.melodia.presentation.ui.theme.MelodiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MelodiaTheme {
                MelodiaApp()
            }
        }
    }
}



