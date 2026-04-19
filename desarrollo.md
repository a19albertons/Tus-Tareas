# Desarrollo

---

## Prerequisitos de desarrollo:

- Android studio (Android Studio Panda 3 | 2025.3.3 Patch 1). La más reciente a la hora de redactar esto. No se asegura la ausencia de errores con versiones anteriores.

Después de clonar el repositorio, se tiene que abrir un nuevo proyecto en Android Studio y seleccionar la carpeta de nombre codigo.

## Compilar la aplicación

1. Opción manual
- Abrir android studio
- Barra lateral izquierda > Build Variants > Seleccionar la version que se desea compilar
- Build > Generate app bundles or APKs > Generate APKs
- Ventana inferior derecha > locate
- Copiar a un movil personal o emulador e instalar la aplicación.

2. Opción automatizada
- Tener un movil físico o virtual
- Barra lateral izquierda > Build Variants > Seleccionar la version que se desea probar
- Clickar boton de play

Nota: esta opción esta más pensada para pruebas de la aplicación en Android Studio de cambios hechos en el código

## Pruebas

Para correr pruebas estan las normales ubicadas en test y las avanzadas que requieren de un dispositivo físico o virtual localizadas en androidTest. Una vez ejecutadas el sistema puede autodetectar las de test, pero para las de androidTest. Se deben seguir lo siguientes pasos:

- Ejecutar el script ubicado en la raiz de codigo (androidTestCoverage.sh) con el comando bash ./androidTestCoverage.sh
- Esto generará un nuevo fichero llamado coverage.ec (es un simple cambio de extensión)
- Run > Manage coverage reports.
- Si ya existe un coverage.exec debe ser eliminado de esa ventana (clickar sobre el y simbolo -).
- Clickar en add y seleccionar el fichero coverage.exec generado por el script la ruta desde la carpeta codigo debería ser similar a app/build/outputs/code_coverage/< entorno usado >/connected/< nombre_del_dispositivo >/coverage.exec
- En la ventana marcar el fichero añadido y Tests in 'com.example.tustaeas' Coverage results.
- Clickar en show selected.