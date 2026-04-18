#!/bin/bash

# Directorio donde Android Studio genera la cobertura de pruebas de instrumentación
SEARCH_DIR="app/build/outputs/code_coverage"


echo "Recordar ejecutarlo desde la raiz de la carpeta donde esta el proyecto android (codigo)"


echo "Buscando archivos .ec en $SEARCH_DIR..."

if [ ! -d "$SEARCH_DIR" ]; then
    echo "Error: No se encontró la carpeta $SEARCH_DIR."
    echo "Asegúrate de haber ejecutado las pruebas con cobertura primero."
    exit 1
fi

# Buscar archivos .ec y copiarlos como .exec
find "$SEARCH_DIR" -name "*.ec" -type f | while read -r file; do
    new_file="${file%.ec}.exec"
    cp "$file" "$new_file"
    echo "Convertido: $(basename "$file") -> $(basename "$new_file")"
done

echo "--------------------------------------------------"
echo "Proceso finalizado. Ahora debes añadirlo manualmente en run manage coverage reports"