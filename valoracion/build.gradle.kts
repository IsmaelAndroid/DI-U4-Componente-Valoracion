// Módulo de LIBRERÍA: aquí vive el componente. No genera un .apk, genera un .aar
// (Android Archive), el "jar" de Android, que además lleva recursos y manifiesto.
plugins {
    alias(libs.plugins.android.library)   // librería, no aplicación
    alias(libs.plugins.kotlin.compose)
    `maven-publish`                        // para poder publicarlo y usarlo desde otros proyectos
}

android {
    namespace = "com.dam.valoracion"      // paquete del componente
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    // Variante que se publica: la release, con el código fuente para poder leerlo desde el IDE
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    // api y no implementation: quien use el componente también necesita ver Compose
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Pruebas del componente (se ejecutan en el emulador)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)   // reciente: las versiones antiguas fallan en los Android más nuevos
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Publicación Maven: las "coordenadas" con las que otros proyectos piden el componente
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.IsmaelAndroid"
                artifactId = "DI-U4-Componente-Valoracion"   // igual que el repositorio de GitHub
                version = "1.0.0"
                from(components["release"])
            }
        }
    }
}
