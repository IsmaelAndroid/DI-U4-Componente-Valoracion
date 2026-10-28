# ⭐ DI-U4 · Componente personalizado `Valoracion` (librería reutilizable)

**Módulo:** Desarrollo de Interfaces (2º DAM) · **Unidad:** U4.1 Creación de componentes visuales
**Teoría de referencia:** [4.1 Creación de componentes visuales](https://revilofe.github.io/section6/u04/teoria/DI-U4.1.-CreacionDeComponentes/)

Este proyecto **crea** un componente visual propio en Jetpack Compose, una valoración con estrellas, y lo
**empaqueta como librería** para que cualquier otro proyecto lo pueda importar desde GitHub.

Lo importan estos dos proyectos:

| Proyecto | Cómo usa el componente |
|---|---|
| [DI-U4-App-Recetas](https://github.com/IsmaelAndroid/DI-U4-App-Recetas) | 5 estrellas ámbar, interactivas, con los valores por defecto |
| [DI-U4-App-Peliculas](https://github.com/IsmaelAndroid/DI-U4-App-Peliculas) | Solo lectura (media del público) y sobre 10 (tu nota), con otros colores y tamaños |

El paso a paso completo está en el PDF [`Guia-DI-U4-Componente-Valoracion.pdf`](Guia-DI-U4-Componente-Valoracion.pdf).

## 📦 Estructura

```
DI-U4-Componente-Valoracion/
├── valoracion/            ← módulo de LIBRERÍA: el componente (genera un .aar)
│   ├── build.gradle.kts   ← com.android.library + maven-publish
│   └── src/
│       ├── main/java/com/dam/valoracion/Valoracion.kt          ← el componente
│       └── androidTest/java/com/dam/valoracion/ValoracionTest.kt ← sus pruebas
├── app/                   ← app de demostración: catálogo del componente
│   └── src/main/java/com/dam/valoracion/demo/MainActivity.kt
└── jitpack.yml            ← cómo debe compilarlo JitPack
```

## 🧩 El componente

```kotlin
@Composable
fun Valoracion(
    valor: Int,                                   // estrellas encendidas (obligatoria)
    modifier: Modifier = Modifier,
    maximo: Int = 5,                              // número de estrellas
    tamano: Dp = 32.dp,                           // lado de cada estrella
    colorActivo: Color = Color(0xFFFFB300),
    colorInactivo: Color = Color(0xFF9E9E9E),
    onValorCambiado: ((Int) -> Unit)? = null,     // evento; null = solo lectura
)
```

- **Propiedades:** los parámetros con valor por defecto.
- **Evento:** `onValorCambiado` avisa de la estrella pulsada; el componente no guarda estado propio.
- **Dibujo:** cada estrella se pinta con `Canvas` y un `Path` de 10 vértices (DrawScope).
- **Accesibilidad:** TalkBack lee «3 de 5 estrellas».
- **Pruebas:** `ValoracionTest` (4 pruebas con `createComposeRule`).

## 🚀 Usarlo en otro proyecto

1. En `settings.gradle.kts`, dentro de `dependencyResolutionManagement { repositories { … } }`:

   ```kotlin
   maven("https://jitpack.io")
   ```

2. En `app/build.gradle.kts`:

   ```kotlin
   implementation("com.github.IsmaelAndroid:DI-U4-Componente-Valoracion:1.0.0")
   ```

3. En el código:

   ```kotlin
   import com.dam.valoracion.Valoracion

   var nota by remember { mutableIntStateOf(0) }
   Valoracion(valor = nota, onValorCambiado = { nota = it })
   ```

[![](https://jitpack.io/v/IsmaelAndroid/DI-U4-Componente-Valoracion.svg)](https://jitpack.io/#IsmaelAndroid/DI-U4-Componente-Valoracion)

## ▶️ Abrir y ejecutar

1. **Code ▸ Download ZIP**, descomprime en una ruta corta (por ejemplo `C:\DAM\`).
2. Android Studio: **File ▸ Open…** → la carpeta del proyecto → **Trust Project**.
3. Ejecuta la configuración **app** para ver el catálogo del componente.
4. Pruebas del componente (con el emulador arrancado): `./gradlew :valoracion:connectedAndroidTest`.

Requisitos: Android Studio actualizado (AGP 9.1 · Gradle 9.5 · compileSdk 35).
