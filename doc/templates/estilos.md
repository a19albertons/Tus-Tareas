## Reglas de estilo

La aplicación sigue las recomendaciones de estilo de código provistas por Ktlint para un entorno de desarrollo Android. Existen tareas automáticas que permite arreglar el código automáticamente para cumplir con estas reglas. Para ejecutar estas tareas, se pueden usar los siguientes comandos sobre la carpeta codigo:

- Para arreglar el código de toda la aplicación: `./gradlew ktlintFormat`
- Para una validación completa sin arreglar el código (porque no puede arreglar automáticamente todo): `./gradlew ktlintCheck`