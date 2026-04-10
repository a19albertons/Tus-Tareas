# Deseño

## Diagrama da arquitectura

### Diagrama de componentes

```mermaid
flowchart TB
    %% tu
    Cliente[Usuario]

    %% app
    subgraph O dispositivo do usuario
        subgraph Aplicación
            ui[UI]
            ViewModel[ViewModel]
            dao[DAO]
            ServicioDeNotificaciones[Servizo de notificacións]
        end
        SistemaDeNotificaciones[Sistema de notificacións]
        BD[Base de datos]
    end

    %% relacións
    Cliente --> |interactúa| ui
    ui <--> |intercambio de datos| ViewModel
    ViewModel <--> |usa| dao
    dao <--> |interactúa| BD
    ViewModel --> |usa| ServicioDeNotificaciones
    ServicioDeNotificaciones --> |envía notificacións| SistemaDeNotificaciones
    ServicioDeNotificaciones <--> |usa| dao
    SistemaDeNotificaciones <--> |usa| dao

    
```

### Diagrama de Despregamento
```mermaid
flowchart TD
    %% nodo
    Usuario[Usuario]
    subgraph O dispositivo
        subgraph Sistema operativo Android
            Aplicacion[Aplicación]
            Bd[Base de datos]
            SistemaNotificaciones[Sistema de notificacións]
        end
    end

    %% relacións
    Usuario -->|usa| Aplicacion
    Aplicacion -->|usa| Bd
    Aplicacion -->|envía notificacións| SistemaNotificaciones
```

## Diagrama de Base de Datos

```mermaid
erDiagram
    %% taboas
    Tareas {
        id int PK
        Nome varchar
        Descripcion text
        fechaLimite date
        Prioridade String "(alto, media, baixa, ningunha)"
        fechaCreacion date
        Estado String "(en tempo, retrasada, completada)"
    }
    Proyectos {
        id int PK
        Nome varchar
        Descripcion text
        fechaCreacion date
        fechaInicio date
        fechaFin date
    }
    Etiquetas {
        id int PK
        Nome varchar
        Descripcion text
    }
    Notificaciones {
        id int PK
        Titulo varchar
        Mensaxe varchar
        Leida boolean
    }

    %% relacións
    Proyectos o|--o{ Tareas : contén
    Proyectos o{--o{ Etiquetas : ten
    Tareas o{--o{ Etiquetas : ten
    Tareas ||--o| Notificaciones : xera
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

    %% Relacións
    Proyecto "0..1" o-- "0..*" Tarea : contén
    Proyecto "0..*" o-- "0..*" Etiqueta : ten
    Tarea "0..*" o-- "0..*" Etiqueta : ten
    Tarea "1" o-- "0..1" Notificacion : xera

```

## Deseño da interface de usuario

[Enlace a Figma](https://www.figma.com/design/VZCQcw7a7B60PrGtjRBL0c/El-proyecto?node-id=0-1&p=f&t=KTdcYnHBGH3pHU1V-0)