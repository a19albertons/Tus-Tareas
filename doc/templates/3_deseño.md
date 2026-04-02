# Deseño

## Diagrama da arquitectura

### Diagrama de componentes

```mermaid
flowchart TB
    %% tu
    Cliente[Usuario]

    %% app
    subgraph El dispositivo del usuario
        subgraph Aplicacion
            ui[ui]
            ViewModel[ViewModel]
            dao[dao]
            ServicioDeNotificaciones[Servicio de notificaciones]
        end
        SistemaDeNotificaciones[Sistema de notificaciones]
        BD[Base de datos]
    end

    %% relaciones
    Cliente --> |interactua| ui
    ui <--> |intercambio de datos| ViewModel
    ViewModel <--> |usa| dao
    dao <--> |interactua| BD
    ViewModel --> |usa| ServicioDeNotificaciones
    ServicioDeNotificaciones --> |envia notificaciones| SistemaDeNotificaciones
    ServicioDeNotificaciones <--> |usa| dao
    SistemaDeNotificaciones <--> |usa| dao

    
```

### Diagrama de Despliegue
```mermaid
flowchart TD
    %% nodo
    Usuario[Usuario]
    subgraph El dispositivo
        subgraph Sistema operativo android
            Aplicacion[Aplicación]
            Bd[Base de datos]
            SistemaNotificaciones[Sistema de notificaciones]
        end
    end

    %% relaciones
    Usuario -->|usa| Aplicacion
    Aplicacion -->|usa| Bd
    Aplicacion -->|envia notificaciones| SistemaNotificaciones
```

## Diagrama de Base de Datos

```mermaid
erDiagram
    %% tablas
    Tareas {
        id int PK
        Nombre varchar
        Descripcion text
        fechaLimite date
        Prioridad String "(alto, media, baja, ninguna)"
        fechaCreacion date
        Estado String "(en tiempo, retrasada, completada)"

    }
    Proyectos {
        id int PK
        Nombre varchar
        Descripcion text
        fechaCreacion date
        fechaInicio date
        fechaFin date
    }
    Etiquetas {
        id int PK
        Nombre varchar
        Descripcion text
    }
    Notificaciones {
        id int PK
        Titulo varchar
        Mensaje varchar
        Leida boolean
    }

    %% relaciones
    Proyectos o|--o{ Tareas : contiene
    Proyectos o{--o{ Etiquetas : tiene
    Tareas o{--o{ Etiquetas : tiene
    Tareas ||--o| Notificaciones : genera
```

## Diagrama de clases

```mermaid
classDiagram
    class Tarea {
        -int id
        -String nombre
        -String descripcion
        -Date fechaLimite
        -Prioridad prioridad
        -Date fechaCreacion
        -Estado estado
        -List~Etiqueta~ etiquetas
        -int idProyecto
    }

    class Proyecto {
        -int id
        -String nombre
        -String descripcion
        -Date fechaCreacion
        -Date fechaInicio
        -Date fechaFin
        -List~Tarea~ tareas
        -List~Etiqueta~ etiquetas
    }

    class Etiqueta {
        -int id
        -String nombre
        -String descripcion
    }
    class Notificacion {
        -int id
        -String titulo
        -String mensaje
        -boolean leida
        -int idTarea
    }

    %% Relaciones
    Proyecto "0..1" o-- "0..*" Tarea : contiene
    Proyecto "0..*" o-- "0..*" Etiqueta : tiene
    Tarea "0..*" o-- "0..*" Etiqueta : tiene
    Tarea "1" o-- "0..1" Notificacion : genera

```

## Deseño de interface de usuarios

[enlace a figma](https://www.figma.com/design/VZCQcw7a7B60PrGtjRBL0c/El-proyecto?node-id=0-1&p=f&t=KTdcYnHBGH3pHU1V-0)