package com.dam.valoracion.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dam.valoracion.Valoracion

/**
 * App de demostración: el "catálogo" del componente. Enseña cada propiedad por separado,
 * igual que la paleta de un IDE enseña cómo se ve cada componente antes de usarlo.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    Catalogo()
                }
            }
        }
    }
}

@Composable
fun Catalogo() {
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Catálogo del componente Valoracion", style = MaterialTheme.typography.headlineSmall)

        // 1. Interactiva: la pantalla guarda el valor y el componente avisa al cambiar
        var nota by rememberSaveable { mutableIntStateOf(3) }
        Ejemplo("Interactiva", "Valoracion(valor = nota, onValorCambiado = { nota = it })") {
            Valoracion(valor = nota, onValorCambiado = { nota = it })
            Text("Has elegido $nota de 5")
        }

        // 2. Solo lectura: sin evento no se puede pulsar
        Ejemplo("Solo lectura", "Valoracion(valor = 4)") {
            Valoracion(valor = 4)
        }

        // 3. Propiedades cambiadas: otro máximo, otro tamaño y otro color
        Ejemplo(
            "Propiedades personalizadas",
            "Valoracion(valor = 7, maximo = 10, tamano = 20.dp,\n    colorActivo = Color(0xFFE53935))",
        ) {
            Valoracion(valor = 7, maximo = 10, tamano = 20.dp, colorActivo = Color(0xFFE53935))
        }

        // 4. Grande y en otro color
        Ejemplo(
            "Grande",
            "Valoracion(valor = 2, maximo = 3, tamano = 56.dp,\n    colorActivo = Color(0xFF43A047))",
        ) {
            Valoracion(valor = 2, maximo = 3, tamano = 56.dp, colorActivo = Color(0xFF43A047))
        }
    }
}

/** Tarjeta del catálogo: título, el código que se usa y el componente funcionando. */
@Composable
private fun Ejemplo(titulo: String, codigo: String, contenido: @Composable () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(codigo, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodySmall)
            contenido()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VistaPreviaCatalogo() {
    MaterialTheme { Catalogo() }
}
