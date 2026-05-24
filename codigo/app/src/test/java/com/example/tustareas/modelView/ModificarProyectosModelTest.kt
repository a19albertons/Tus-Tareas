package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tustareas.R
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.ModificarProyectosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date

/**
 * Clase que tiene las pruebas unitarias de modificar proyectos model
 */
class ModificarProyectosModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesaio para corutinas
    @get:Rule
    val ruleCorutinas = MainDispatcherRule()

    // Definición repositorio
    val modificarProyectosRepository = Mockito.mock(ModificarProyectosRepository::class.java)

    // Definición modelo
    val modificarProyectosModel =
        ModificarProyectosModel(Application(), modificarProyectosRepository)

    @Test
    fun guardarProyectoConTareaYEtiquetasNueva() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 0,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 0,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta =
                Etiqueta(
                    id = 0,
                    nombre = "Etiqueta de prueba",
                    descripcion = null,
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modificarProyectosModel.definirProyectoDTO(proyectoDTO)

            // Guardar tarea
            modificarProyectosModel.guardarYModificarProyecto(
                proyectoDTO.proyecto.nombre,
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Verificar que se haya llamado al repositorio para guardar el proyecto
            Mockito.verify(modificarProyectosRepository).insertarProyectoConTareaYEtiqueta(proyectoDTO)
            assert(modificarProyectosModel.observarResultado().value == true)
        }

    @Test
    fun guardarProyectoConTareaYEtiquetasNoValido() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 0,
                    nombre = "",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 0,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta =
                Etiqueta(
                    id = 0,
                    nombre = "Etiqueta de prueba",
                    descripcion = null,
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modificarProyectosModel.definirProyectoDTO(proyectoDTO)

            // Guardar tarea
            modificarProyectosModel.guardarYModificarProyecto(
                "",
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Verificar que se haya llamado al repositorio para guardar el proyecto
            assert(modificarProyectosModel.observarMensajeError().value == com.example.tustareas.R.string.error_guardar_proyecto)
            assert(modificarProyectosModel.observarResultado().value == false)
        }

    @Test
    fun guardarProyectoConTareaYEtiquetasExistentes() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba",
                    descripcion = null,
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modificarProyectosModel.definirProyectoDTO(proyectoDTO)

            // Guardar tarea
            modificarProyectosModel.guardarYModificarProyecto(
                proyectoDTO.proyecto.nombre,
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Verificar que se haya llamado al repositorio para guardar el proyecto
            Mockito.verify(modificarProyectosRepository).modificarProyectoConTareaYEtiqueta(proyectoDTO)
            assert(modificarProyectosModel.observarResultado().value == true)
        }

    @Test
    fun guardarProyectoConTareaYEtiquetasExistenteNoValido() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Guardar tarea
        modificarProyectosModel.guardarYModificarProyecto(
            "",
            proyectoDTO.proyecto.descripcion ?: "",
        )

        // Verificar que se haya llamado al repositorio para guardar el proyecto
        assert(modificarProyectosModel.observarMensajeError().value == R.string.error_modificar_proyecto)
        assert(modificarProyectosModel.observarResultado().value == false)
    }

    @Test
    fun tituloDialogoNueva() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 0,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 0,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 0,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Verificar que el título del diálogo sea el correcto para un proyecto nuevo
        assert(modificarProyectosModel.tituloDialogo() == R.string.confirmar_guardar_proyecto)
    }

    @Test
    fun tituloDialogoExistente() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Verificar que el título del diálogo sea el correcto para un proyecto existente
        assert(modificarProyectosModel.tituloDialogo() == R.string.confirmar_modificar_proyecto)
    }

    @Test
    fun observarProyectoDTO() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Verificar que se pueda observar el DTO del proyecto correctamente
        assert(modificarProyectosModel.observarProyectoDTO().value == proyectoDTO)
    }

    @Test
    fun actualizarTareasDelProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea1 =
            Tarea(
                id = 1,
                nombre = "tarea de prueba 1",
                descripcion = "Descripción de la tarea de prueba 1",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val tarea2 =
            Tarea(
                id = 2,
                nombre = "tarea de prueba 2",
                descripcion = "Descripción de la tarea de prueba 2",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.MEDIA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea1),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Actualizar tareas del proyecto
        modificarProyectosModel.actualizarTareasDelProyecto(listOf(tarea2))

        // Verificar que las tareas del proyecto se hayan actualizado correctamente
        assert(modificarProyectosModel.observarProyectoDTO().value?.tareas == listOf(tarea2))
    }

    @Test
    fun obtenerTareasDelProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea1 =
            Tarea(
                id = 1,
                nombre = "tarea de prueba 1",
                descripcion = "Descripción de la tarea de prueba 1",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val tarea2 =
            Tarea(
                id = 2,
                nombre = "tarea de prueba 2",
                descripcion = "Descripción de la tarea de prueba 2",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.MEDIA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea1, tarea2),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Obtener tareas del proyecto
        val tareasDelProyecto = modificarProyectosModel.obtenerTareasDelProyecto()

        // Verificar que se hayan obtenido las tareas del proyecto correctamente
        assert(tareasDelProyecto == listOf(tarea1, tarea2))
    }

    @Test
    fun actualizarEtiquetasDelProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta1 =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba 1",
                descripcion = null,
            )
        val etiqueta2 =
            Etiqueta(
                id = 2,
                nombre = "Etiqueta de prueba 2",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta1),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Actualizar etiquetas del proyecto
        modificarProyectosModel.actualizarEtiquetasDelProyecto(listOf(etiqueta2))

        // Verificar que las etiquetas del proyecto se hayan actualizado correctamente
        assert(modificarProyectosModel.observarProyectoDTO().value?.etiquetas == listOf(etiqueta2))
    }

    @Test
    fun obtenerEtiquetasDelProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta1 =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba 1",
                descripcion = null,
            )
        val etiqueta2 =
            Etiqueta(
                id = 2,
                nombre = "Etiqueta de prueba 2",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta1, etiqueta2),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Obtener etiquetas del proyecto
        val etiquetasDelProyecto = modificarProyectosModel.obtenerEtiquetasDelProyecto()

        // Verificar que se hayan obtenido las etiquetas del proyecto correctamente
        assert(etiquetasDelProyecto == listOf(etiqueta1, etiqueta2))
    }

    @Test
    fun establecerFechaInicioProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Establecer nueva fecha de inicio para el proyecto
        val nuevaFechaInicio = Date()
        modificarProyectosModel.establecerFechaInicioProyecto(nuevaFechaInicio)

        // Verificar que la fecha de inicio del proyecto se haya actualizado correctamente
        assert(
            modificarProyectosModel
                .observarProyectoDTO()
                .value
                ?.proyecto
                ?.fechaInicio == nuevaFechaInicio,
        )
    }

    @Test
    fun establecerFechaFinProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Establecer nueva fecha de fin para el proyecto
        val nuevaFechaFin = Date()
        modificarProyectosModel.establecerFechaFinProyecto(nuevaFechaFin)

        // Verificar que la fecha de fin del proyecto se haya actualizado correctamente
        assert(
            modificarProyectosModel
                .observarProyectoDTO()
                .value
                ?.proyecto
                ?.fechaFin == nuevaFechaFin,
        )
    }

    @Test
    fun actualizarFiltroListaTareaProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Observar tareas restantes
        modificarProyectosModel.obtenerTareasRestantes()

        // Invocar filtro de tareas restantes
        modificarProyectosModel.actualizarFiltroListaTareaProyecto(listOf())

        // Verificar que se ha llamado al respositorio
        Mockito.verify(modificarProyectosRepository).obtenerTareasRestantes(listOf(), 1)
    }

    @Test
    fun actualizarFiltroListaEtiquetaProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Observar etiquetas restantes
        modificarProyectosModel.obtenerEtiquetasRestantes()

        // Invocar filtro de etiquetas restantes
        modificarProyectosModel.actualizarFiltroListaEtiquetaProyecto(listOf())

        // Verificar que se ha llamado al respositorio
        Mockito.verify(modificarProyectosRepository).obtenerEtiquetasRestantes(listOf())
    }

    @Test
    fun tareasRestantesProcesada() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Procesar tareas restantes
        val resultado = modificarProyectosModel.tareasRestantesProcesadas(listOf(tarea))

        // Verificar que las tareas restantes se hayan procesado correctamente
        assert(resultado == listOf(tarea))
    }

    @Test
    fun etiquetasRestantesProcesada() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Procesar etiquetas restantes
        val resultado = modificarProyectosModel.etiquetasRestantesProcesadas(listOf(etiqueta))

        // Verificar que las etiquetas restantes se hayan procesado correctamente
        assert(resultado == listOf(etiqueta))
    }

    @Test
    fun anadirTareaAlProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea1 =
            Tarea(
                id = 1,
                nombre = "tarea de prueba 1",
                descripcion = "Descripción de la tarea de prueba 1",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta),
                listOf(tarea1),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Añadir nueva tarea al proyecto
        val tarea2 =
            Tarea(
                id = 2,
                nombre = "tarea de prueba 2",
                descripcion = "Descripción de la tarea de prueba 2",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.MEDIA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )

        // Aplica la lista procesada para definir las taras a añadir internamente
        modificarProyectosModel.tareasRestantesProcesadas(listOf(tarea2))

        // Define la tara a añadir
        modificarProyectosModel.anadirTareaAlProyecto(0)

        // Verificar que la nueva tarea se haya añadido correctamente al proyecto
        assert(modificarProyectosModel.observarProyectoDTO().value?.tareas == listOf(tarea1, tarea2))
    }

    @Test
    fun anadirEtiquetaAlProyecto() {
        // Definición proyecto
        val proyecto =
            Proyecto(
                id = 1,
                nombre = "Proyecto de prueba",
                descripcion = "Descripción del proyecto de prueba",
                fechaCreacion = Date(),
                fechaInicio = Date(),
                fechaFin = Date(),
            )
        val tarea =
            Tarea(
                id = 1,
                nombre = "tarea de prueba",
                descripcion = "Descripción de la tarea de prueba",
                fechaCreacion = Date(),
                fechaLimite = Date(),
                prioridad = Prioridad.ALTA,
                estado = Estado.EN_TIEMPO,
                idProyecto = null,
            )
        val etiqueta1 =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta de prueba 1",
                descripcion = null,
            )
        val proyectoDTO =
            ProyectoDTO(
                proyecto,
                listOf(etiqueta1),
                listOf(tarea),
            )

        // Definir DTO de proyecto con tarea y etiqueta
        modificarProyectosModel.definirProyectoDTO(proyectoDTO)

        // Añadir nueva etiqueta al proyecto
        val etiqueta2 =
            Etiqueta(
                id = 2,
                nombre = "Etiqueta de prueba 2",
                descripcion = null,
            )

        // Aplica la lista procesada para definir las etiquetas a añadir internamente
        modificarProyectosModel.etiquetasRestantesProcesadas(listOf(etiqueta2))

        // Define la etiqueta a añadir
        modificarProyectosModel.anadirEtiquetaAlProyecto(0)

        // Verificar que la nueva etiqueta se haya añadido correctamente al proyecto
        assert(modificarProyectosModel.observarProyectoDTO().value?.etiquetas == listOf(etiqueta1, etiqueta2))
    }
}
