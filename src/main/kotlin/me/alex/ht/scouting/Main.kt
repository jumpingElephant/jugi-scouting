import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import io.ar.invest.theme.DarkColorPalette
import me.alex.ht.scouting.App

fun main() = application {
    Window(
        title = "Jugi Scouting!",
        onCloseRequest = ::exitApplication,
        state = WindowState(size = WindowSize(962.dp, 768.dp))
    ) {
        MaterialTheme(
            colors = DarkColorPalette
        ) {
            DisableSelection {
                App()
            }
        }
    }
}
