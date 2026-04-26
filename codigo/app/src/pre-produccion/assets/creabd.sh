#!/bin/bash
# Script para crear una base de datos SQLite con la estructura de las entidades Room
# de la aplicación y poblarla con unos datos de ejemplo. Se basa en el esquema actual de
# gestión de tareas y horario de la aplicación.
#
# Las fechas se guardan como INTEGER en formato timestamp (milisegundos) correspondiente a la fecha
# normalizada a medianoche UTC. Es decir, igual que usa el
# TypeConverter de la aplicación.
# Colocar este fichero en la raíz del proyecto y ejecutar:
#   bash creabd.sh
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

-- datos de ejemplo
-- generamos timestamps en ms (strftime('%s')*1000) centrados a medianoche
-- Usamos distintos intervalos para cubrir casos de tareas retrasadas, completadas, sin fecha y con distintos estados/prioridades.

INSERT INTO proyectos (nombre, descripcion, fechaCreacion, fechaInicio, fechaFin) VALUES
    ('Proyecto Alpha', 'Primer proyecto de ejemplo',
         strftime('%s','now','localtime','start of day','utc')*1000,
         strftime('%s','now','localtime','start of day','utc')*1000,
         strftime('%s','now','localtime','+14 days','start of day','utc')*1000),
    ('Proyecto Beta', 'Proyecto completado',
         strftime('%s','now','localtime','-30 days','start of day','utc')*1000,
         strftime('%s','now','localtime','-30 days','start of day','utc')*1000,
         strftime('%s','now','localtime','-1 day','start of day','utc')*1000),
    ('Proyecto Sin Descripción', NULL,
         strftime('%s','now','localtime','start of day','utc')*1000,
         NULL,
         NULL),
    ('Proyecto Hogar', 'Tareas de casa y vida diaria',
         strftime('%s','now','localtime','start of day','utc')*1000,
         strftime('%s','now','localtime','start of day','utc')*1000,
         strftime('%s','now','localtime','+60 days','start of day','utc')*1000);

INSERT INTO etiquetas (nombre, descripcion) VALUES
    ('Urgente', 'Tarea urgente'),
    ('Importante', NULL),
    ('Revisar', 'Revisar antes de enviar'),
    ('Cliente', 'Relacionado con un cliente'),
    ('Bug', NULL),
    ('Personal', 'Tareas personales');

INSERT INTO tareas (nombre, descripcion, fechaLimite, prioridad, fechaCreacion, estado, idProyecto) VALUES
    ('Tarea 1', 'Descripción de tarea 1',
         strftime('%s','now','localtime','+7 days','start of day','utc')*1000,
         'Alta', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea 2', NULL, NULL, 'Media', strftime('%s','now','localtime','start of day','utc')*1000, 'Completada', 1),
    ('Tarea Hoy 1', 'Vencimiento hoy',
         strftime('%s','now','localtime','start of day','utc')*1000,
         'Baja', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea Hoy 2', 'Otra tarea de hoy',
         strftime('%s','now','localtime','start of day','utc')*1000,
         'Media', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea Hoy 3', 'Tercera tarea hoy',
         strftime('%s','now','localtime','start of day','utc')*1000,
         'Alta', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea Hoy 4', 'Cuarta tarea hoy',
         strftime('%s','now','localtime','start of day','utc')*1000,
         'Baja', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea Retrasada', 'Debería aparecer en la lista de tareas retrasadas',
         strftime('%s','now','localtime','-1 day','start of day','utc')*1000,
         'Alta', strftime('%s','now','localtime','-7 days','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea Retrasada (estado)', 'Tiene estado Retrasada explícito',
         strftime('%s','now','localtime','-7 days','start of day','utc')*1000,
         'Media', strftime('%s','now','localtime','-14 days','start of day','utc')*1000, 'Retrasada', 1),
    ('Tarea sin prioridad', NULL,
         strftime('%s','now','localtime','+3 days','start of day','utc')*1000,
         'NoEstablecido', strftime('%s','now','localtime','-1 day','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea sin descripción', NULL,
         strftime('%s','now','localtime','+3 days','start of day','utc')*1000,
         'Media', strftime('%s','now','localtime','-2 days','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea larga', 'Esta es una descripción muy larga para comprobar que las vistas y búsquedas funcionan correctamente en todo el texto. ' ||
                      'Incluye comas, saltos de línea, y una mezcla de caracteres: ñ, á, é, í, ó, ú, ü.',
         strftime('%s','now','localtime','+5 days','start of day','utc')*1000,
         'Baja', strftime('%s','now','localtime','-3 days','start of day','utc')*1000, 'EnTiempo', 1),
    ('Tarea completada vieja', 'Tarea de proyecto ya finalizado',
         strftime('%s','now','localtime','-30 days','start of day','utc')*1000,
         'Alta', strftime('%s','now','localtime','-31 days','start of day','utc')*1000, 'Completada', 2),
    ('Tarea sin fecha', 'Esta tarea no tiene fecha límite establecida',
         NULL,
         'Baja', strftime('%s','now','localtime','-5 days','start of day','utc')*1000, 'EnTiempo', 2),
    ('Tarea futuro', 'Planificada para dentro de un mes',
         strftime('%s','now','localtime','+30 days','start of day','utc')*1000,
         'Media', strftime('%s','now','localtime','-1 day','start of day','utc')*1000, 'EnTiempo', 2),
    ('Tarea proyecto sin descripcion', NULL,
         strftime('%s','now','localtime','+1 day','start of day','utc')*1000,
         'NoEstablecido', strftime('%s','now','localtime','start of day','utc')*1000, 'EnTiempo', 3),
    ('Tarea sin fecha 2', 'Tarea sin vencimiento pero con estado Retrasada',
         NULL,
         'NoEstablecido', strftime('%s','now','localtime','-2 days','start of day','utc')*1000, 'Retrasada', 3),
    ('Tarea con varias etiquetas', 'Relacionada con cliente y urgente',
         strftime('%s','now','localtime','+2 days','start of day','utc')*1000,
         'Alta', strftime('%s','now','localtime','-1 day','start of day','utc')*1000, 'EnTiempo', 4),
    ('Tarea caracteres especiales', 'Título y descripción con acentos y símbolos: ñ, á, ¿, ¡, €, @',
         strftime('%s','now','localtime','+4 days','start of day','utc')*1000,
         'Media', strftime('%s','now','localtime','-2 days','start of day','utc')*1000, 'EnTiempo', 4);

INSERT INTO TareaEtiquetas (idTarea, idEtiqueta) VALUES
    (1,1), (1,2),  -- Tarea 1: Urgente + Importante
    (2,2),          -- Tarea 2: Importante
    (3,3),          -- Tarea Hoy 1: Revisar
    (4,1), (4,4),   -- Tarea Hoy 2: Urgente + Cliente
    (5,5),          -- Tarea Hoy 3: Bug
    (6,6),          -- Tarea Hoy 4: Personal
    (7,1),          -- Tarea Retrasada: Urgente
    (8,3), (8,5),   -- Tarea Retrasada (estado): Revisar + Bug
    (9,2),          -- Tarea sin prioridad: Importante
    (11,4),         -- Tarea larga: Cliente
    (12,5),         -- Tarea completada vieja: Bug
    (13,6),         -- Tarea sin fecha: Personal
    (17,4), (17,1); -- Tarea con varias etiquetas: Cliente + Urgente

INSERT INTO ProyectoEtiquetas (idProyecto, idEtiqueta) VALUES
    (1,1), (1,2),  -- Proyecto Alpha: Urgente + Importante
    (2,5),         -- Proyecto Beta: Bug
    (3,6),         -- Proyecto Sin Descripción: Personal
    (4,4);         -- Proyecto Hogar: Cliente
EOF

echo "Base de datos '$DB' creada con tablas y datos de prueba."
