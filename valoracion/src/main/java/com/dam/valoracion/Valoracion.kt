package com.dam.valoracion

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Componente: valoración con estrellas.
 *
 * Cada parámetro es una **propiedad** del componente. Solo `valor` es obligatorio;
 * el resto trae un valor por defecto que quien lo usa puede cambiar.
 *
 * El componente **no guarda estado propio**: pinta el `valor` que le pasan y, si el
 * usuario pulsa una estrella, lo **avisa** con el evento `onValorCambiado`. Es quien
 * lo usa quien decide guardar el nuevo valor.
 *
 * @param valor número de estrellas encendidas (se ajusta solo entre 0 y [maximo])
 * @param modifier posición y márgenes decididos por la pantalla que lo usa
 * @param maximo número total de estrellas
 * @param tamano lado de cada estrella
 * @param colorActivo color de las estrellas encendidas
 * @param colorInactivo color del contorno de las estrellas apagadas
 * @param onValorCambiado evento: se dispara con la estrella pulsada (1..maximo).
 *   Si es `null`, el componente es de **solo lectura** y no responde a pulsaciones.
 */
@Composable
fun Valoracion(
    valor: Int,
    modifier: Modifier = Modifier,
    maximo: Int = 5,
    tamano: Dp = 32.dp,
    colorActivo: Color = Color(0xFFFFB300),
    colorInactivo: Color = Color(0xFF9E9E9E),
    onValorCambiado: ((Int) -> Unit)? = null,
) {
    require(maximo > 0) { "maximo debe ser mayor que 0" }
    val valorSeguro = valor.coerceIn(0, maximo)

    Row(
        modifier = modifier
            .testTag("valoracion")
            // Lo que lee TalkBack: el número, no "cinco imágenes"
            .semantics { contentDescription = "$valorSeguro de $maximo estrellas" },
        horizontalArrangement = Arrangement.spacedBy(tamano / 8),
    ) {
        for (posicion in 1..maximo) {
            // Solo es pulsable si alguien escucha el evento
            val pulsable = if (onValorCambiado != null) {
                Modifier.clickable(onClickLabel = "Puntuar con $posicion") { onValorCambiado(posicion) }
            } else {
                Modifier
            }
            Estrella(
                encendida = posicion <= valorSeguro,
                colorActivo = colorActivo,
                colorInactivo = colorInactivo,
                modifier = pulsable
                    .testTag("estrella_$posicion")
                    .size(tamano),
            )
        }
    }
}

/** Una estrella dibujada a mano sobre un lienzo (DrawScope): rellena o solo el contorno. */
@Composable
private fun Estrella(
    encendida: Boolean,
    colorActivo: Color,
    colorInactivo: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier) {
        val lado = size.minDimension
        val forma = trazadoEstrella(lado)
        if (encendida) {
            drawPath(forma, colorActivo)
        } else {
            drawPath(forma, colorInactivo, style = Stroke(width = lado * 0.07f))
        }
    }
}

/**
 * Calcula el contorno de una estrella de 5 puntas que cabe en un cuadrado de [lado].
 * Son 10 vértices que alternan el radio exterior (las puntas) y el interior (los huecos).
 */
internal fun trazadoEstrella(lado: Float): Path {
    val centro = lado / 2f
    val radioExterior = lado / 2f * 0.92f      // un poco de margen para que el borde no se corte
    val radioInterior = radioExterior * 0.42f
    return Path().apply {
        for (i in 0 until 10) {
            val radio = if (i % 2 == 0) radioExterior else radioInterior
            val angulo = PI / 5 * i - PI / 2       // empieza en la punta de arriba
            val x = centro + (radio * cos(angulo)).toFloat()
            val y = centro + (radio * sin(angulo)).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
}

/* ---------- Previsualizaciones: el componente en el editor, sin emulador ---------- */

@Preview(name = "Valoracion · solo lectura", showBackground = true)
@Composable
private fun VistaPreviaSoloLectura() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Valoracion(valor = 0)
        Valoracion(valor = 3)
        Valoracion(valor = 7, maximo = 10, tamano = 20.dp, colorActivo = Color(0xFFE53935))
    }
}

@Preview(name = "Valoracion · interactiva", showBackground = true)
@Composable
private fun VistaPreviaInteractiva() {
    var valor by remember { mutableIntStateOf(2) }
    Valoracion(valor = valor, onValorCambiado = { valor = it })
}
