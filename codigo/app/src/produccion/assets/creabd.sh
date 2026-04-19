#!/bin/bash
# Script para crear una base de datos SQLite con la estructura de las entidades Room
# y poblarla con unos datos de ejemplo. Las fechas se guardan como INTEGER
# en formato timestamp (milisegundos) correspondiente a la fecha
# año/mes/día (hora normalizada a 00:00). Es decir, igual que usa el
# TypeConverter de la aplicación.
# Colocar este fichero en la raíz del proyecto y ejecutar:
#   bash create_database.sh
# Generará un archivo "baseDatos.db" en la misma carpeta.
DB="baseDatos.db"
# eliminar si existe para recrear desde cero
rm -f "$DB"
sqlite3 "$DB" <<'EOF'
CREATE TABLE proyectos (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  nombre TEXT NOT NULL,
  descripcion TEXT,
  fechaCreacion INTEGER NOT NULL,
  fechaInicio INTEGER,
  fechaFin INTEGER
);
CREATE TABLE etiquetas (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  nombre TEXT NOT NULL,
  descripcion TEXT
);
CREATE TABLE tareas (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  nombre TEXT NOT NULL,
  descripcion TEXT,
  fechaLimite INTEGER,
  prioridad TEXT NOT NULL,
  fechaCreacion INTEGER NOT NULL,
  estado TEXT NOT NULL,
  idProyecto INTEGER,
  FOREIGN KEY(idProyecto) REFERENCES proyectos(id)
);
CREATE TABLE TareaEtiquetas (
  idTarea INTEGER NOT NULL,
  idEtiqueta INTEGER NOT NULL,
  PRIMARY KEY (idTarea, idEtiqueta),
  FOREIGN KEY(idTarea) REFERENCES tareas(id) ON DELETE CASCADE,
  FOREIGN KEY(idEtiqueta) REFERENCES etiquetas(id) ON DELETE CASCADE
);
CREATE TABLE ProyectoEtiquetas (
  idProyecto INTEGER NOT NULL,
  idEtiqueta INTEGER NOT NULL,
  PRIMARY KEY (idProyecto, idEtiqueta),
  FOREIGN KEY(idProyecto) REFERENCES proyectos(id) ON DELETE CASCADE,
  FOREIGN KEY(idEtiqueta) REFERENCES etiquetas(id) ON DELETE CASCADE
);
CREATE TABLE notificaciones (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  titulo TEXT NOT NULL,
  mensaje TEXT NOT NULL,
  leido INTEGER NOT NULL,
  idTarea INTEGER NOT NULL,
  FOREIGN KEY(idTarea) REFERENCES tareas(id) ON DELETE CASCADE
);

-- Índices definidos en los modelos Room (coinciden con @Entity(indices=...))
-- Usamos nombres exactamente como Room genera: index_<table>_<column>[...]
CREATE INDEX IF NOT EXISTS index_TareaEtiquetas_idTarea ON TareaEtiquetas(idTarea);
CREATE INDEX IF NOT EXISTS index_TareaEtiquetas_idEtiqueta ON TareaEtiquetas(idEtiqueta);
CREATE INDEX IF NOT EXISTS index_ProyectoEtiquetas_idProyecto ON ProyectoEtiquetas(idProyecto);
CREATE INDEX IF NOT EXISTS index_ProyectoEtiquetas_idEtiqueta ON ProyectoEtiquetas(idEtiqueta);


EOF

echo "Base de datos '$DB' creada con tablas y datos de prueba."
