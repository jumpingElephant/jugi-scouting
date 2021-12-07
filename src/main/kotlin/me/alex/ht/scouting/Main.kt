package me.alex.ht.scouting

import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import io.ar.invest.theme.DarkColorPalette
import me.alex.ht.scouting.data.ApplicationProperties

val LocalApplicationProperties = compositionLocalOf<ApplicationProperties> { error("No Application Properties found!") }

fun main() = application {
    Window(
        title = "Jugi Scouting!",
        onCloseRequest = ::exitApplication,
        state = WindowState(size = WindowSize(962.dp, 768.dp))
    ) {
        val applicationProperties = ApplicationProperties()
        CompositionLocalProvider(
            LocalApplicationProperties provides applicationProperties
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
}
