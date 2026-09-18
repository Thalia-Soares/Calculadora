package com.example.Thalia.unisal.minhacalculadora.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
enum class TemaDoAPP {
    CLARO,
    ESCURO,
    DINAMICO
}
private val DarkColorScheme = darkColorScheme(
    primary = RoxoPrincipalEscuro,
    onPrimary = Preto,

    secondary = RoxoSecundarioEscuro,
    onSecondary = Preto,

    tertiary = RoxoSecundarioEscuro,
    onTertiary = Preto,

    background = FundoEscuro,
    onBackground = Branco,

    surface = FundoEscuro,
    onSurface = Branco,

    surfaceVariant = LilasBotaoEscuro,
    onSurfaceVariant = Branco,

    primaryContainer = RoxoPrincipalEscuro,
    onPrimaryContainer = Preto,

    secondaryContainer = LilasBotaoEscuro,
    onSecondaryContainer = Branco,

    error = RoxoErro,
    onError = Branco
)

private val LightColorScheme = lightColorScheme(
    primary = RoxoPrincipal,
    onPrimary = Branco,

    secondary = RoxoSecundario,
    onSecondary = Branco,

    tertiary = RoxoSecundario,
    onTertiary = Branco,

    background = LilasFundo,
    onBackground = Preto,

    surface = LilasFundo,
    onSurface = Preto,

    surfaceVariant = LilasBotao,
    onSurfaceVariant = Preto,

    primaryContainer = RoxoPrincipal,
    onPrimaryContainer = Branco,

    secondaryContainer = LilasBotao,
    onSecondaryContainer = Preto,

    error = RoxoErro,
    onError = Branco
)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

@Composable
fun MinhaCalculadoraTheme(
    temaDoAPP: TemaDoAPP,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

// Define o esquema de cores de acordo com o tema selecionado
    val esquemaDeCor = when (temaDoAPP) {

        TemaDoAPP.CLARO -> LightColorScheme

        TemaDoAPP.ESCURO -> DarkColorScheme

        TemaDoAPP.DINAMICO -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (isSystemInDarkTheme()) {
                    dynamicDarkColorScheme(context)
                } else {
                    dynamicLightColorScheme(context)
                }
                // Em versões anteriores ao Android 12, utiliza o tema claro
            } else {
                LightColorScheme
            }
        }
    }
    // Aplica o esquema de cores e a tipografia
    MaterialTheme(
        colorScheme = esquemaDeCor,
        typography = CalculadoraTypography,
        content = content
    )
}