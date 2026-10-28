package com.dam.valoracion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

/** Pruebas del componente: se lanzan en el emulador con ./gradlew :valoracion:connectedAndroidTest */
class ValoracionTest {

    @get:Rule
    val regla = createComposeRule()

    @Test
    fun pintaElValorQueRecibe() {
        regla.setContent { Valoracion(valor = 3) }
        regla.onNodeWithContentDescription("3 de 5 estrellas").assertExists()
    }

    @Test
    fun ajustaLosValoresFueraDeRango() {
        regla.setContent { Valoracion(valor = 99, maximo = 4) }
        regla.onNodeWithContentDescription("4 de 4 estrellas").assertExists()
    }

    @Test
    fun sinEventoEsDeSoloLectura() {
        regla.setContent { Valoracion(valor = 2) }
        regla.onNodeWithTag("estrella_4").assertHasNoClickAction()
    }

    @Test
    fun alPulsarAvisaConLaEstrellaPulsada() {
        regla.setContent {
            var valor by remember { mutableIntStateOf(1) }
            Valoracion(valor = valor, onValorCambiado = { valor = it })
        }
        regla.onNodeWithTag("estrella_4").assertHasClickAction().performClick()
        regla.onNodeWithContentDescription("4 de 5 estrellas").assertExists()
    }
}
