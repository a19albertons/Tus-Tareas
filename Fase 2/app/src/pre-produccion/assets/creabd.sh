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
  idProyecto INTEGER NOT NULL,
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
-- datos de ejemplo
-- generamos timestamps en ms (strftime('%s')*1000) centrados a medianoche
INSERT INTO proyectos (nombre, descripcion, fechaCreacion, fechaInicio, fechaFin) VALUES
    ('Proyecto Alpha', 'Primer proyecto de ejemplo',
         strftime('%s','now','localtime','start of day','utc')*1000,
         strftime('%s','now','localtime','start of day','utc')*1000,
         NULL);
INSERT INTO etiquetas (nombre, descripcion) VALUES
    ('Urgente', 'Tarea urgente'),
    ('Importante', NULL);
INSERT INTO tareas (nombre, descripcion, fechaLimite, prioridad, fechaCreacion, estado, idProyecto) VALUES
    ('Tarea 1', 'Descripción de tarea 1',
         strftime('%s','now','localtime','+7 days','start of day','utc')*1000,
         'Alta', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea 2', NULL, NULL, 'Media', strftime('%s','now','localtime','start of day','utc')*1000, 'COMPLETADA', 1),
    ('Tarea Hoy 1', 'Vencimiento hoy',
         strftime('%s','now','localtime','start of day','utc')*1000, 'Baja', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea Hoy 2', 'Otra tarea de hoy',
         strftime('%s','now','localtime','start of day','utc')*1000, 'Media', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea Hoy 3', 'Tercera tarea hoy',
         strftime('%s','now','localtime','start of day','utc')*1000, 'Alta', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea Hoy 4', 'Cuarta tarea hoy',
         strftime('%s','now','localtime','start of day','utc')*1000, 'Baja', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1);
INSERT INTO TareaEtiquetas (idTarea, idEtiqueta) VALUES (1,1), (2,2);
INSERT INTO ProyectoEtiquetas (idProyecto, idEtiqueta) VALUES (1,1);
EOF

echo "Base de datos '$DB' creada con tablas y datos de prueba."
