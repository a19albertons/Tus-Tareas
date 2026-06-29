package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tustareas.R
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearTareasRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date

/**
 * Clase que tiene las pruebas unitarias de modificar tareas model
 */
class CrearTareasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para corutinas
    @get:Rule
    val ruleCorutinas = MainDispatcherRule()

    // Definición repositorio
    val crearTareasRepository = Mockito.mock(CrearTareasRepository::class.java)

    // Definición modelo
    val crearTareasModel = CrearTareasModel(Application(), crearTareasRepository)

    @Test
    fun guardarTareaNueva() =
        runTest {
            // Crear tareaDTO de prueba
            val tarea =
                Tarea(
                    id = 0,
                    nombre = "Tarea 1",
                    descripcion = "Descripción de la tarea 1",
                    fechaCreacion = Date(),
                    prioridad = Prioridad.ALTA,
                    fechaLimite = Date(),
                    estado = Estado.EN_TIEMPO,
                )
            val etiqueta =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta 1",
                    descripcion = "Descripción de la etiqueta 1",
                )
            val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

            // Definir tareaDTO en el modelo
            crearTareasModel.definirTareaDTO(tareaDTO)

            // Guardar tarea
            crearTareasModel.guardarTarea("Tarea 1", "Descripción de la tarea 1")

            // Comprobar que se ha llamado al método del repositorio para guardar la tarea
            Mockito.verify(crearTareasRepository).insertarTareaConEtiqueta(tareaDTO)
            assert(crearTareasModel.observarResultado().value == true)
        }

    @Test
    fun guardarTareaNuevaNoValida() =
        runTest {
            // Crear tareaDTO de prueba
            val tarea =
                Tarea(
                    id = 0,
                    nombre = "Tarea 1",
                    descripcion = "Descripción de la tarea 1",
                    fechaCreacion = Date(),
                    prioridad = Prioridad.ALTA,
                    fechaLimite = Date(),
                    estado = Estado.EN_TIEMPO,
                )
            val etiqueta =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta 1",
                    descripcion = "Descripción de la etiqueta 1",
                )
            val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

            // Definir tareaDTO en el modelo
            crearTareasModel.definirTareaDTO(tareaDTO)

            // Guardar tarea
            crearTareasModel.guardarTarea("", "Descripción de la tarea 1")

            // Comprobar error
            assert(crearTareasModel.observarMensajeError().value == R.string.error_guardar_tarea)
            assert(crearTareasModel.observarResultado().value == false)
        }

    @Test
    fun observarTareaDTO() {
        // Crear tareaDTO de prueba
        val tarea =
            Tarea(
                id = 1,
                nombre = "Tarea 1",
                descripcion = "Descripción de la tarea 1",
                fechaCreacion = Date(),
                prioridad = Prioridad.ALTA,
                fechaLimite = Date(),
                estado = Estado.EN_TIEMPO,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        crearTareasModel.definirTareaDTO(tareaDTO)

        // Comprobar que se ha definido correctamente el tareaDTO
        assert(crearTareasModel.observarTareaDTO().value == tareaDTO)
    }

    @Test
    fun prioridadOrdinal() {
        // Crear tareaDTO de prueba
        val tarea =
            Tarea(
                id = 1,
                nombre = "Tarea 1",
                descripcion = "Descripción de la tarea 1",
                fechaCreacion = Date(),
                prioridad = Prioridad.ALTA,
                fechaLimite = Date(),
                estado = Estado.EN_TIEMPO,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        crearTareasModel.definirTareaDTO(tareaDTO)

        // Comprobar que se ha definido correctamente el tareaDTO
        assert(crearTareasModel.prioridadOrdinal() == Prioridad.ALTA.ordinal)
    }

    @Test
    fun cambiarPrioridadMedia() {
        // Crear tareaDTO de prueba
        val tarea =
            Tarea(
                id = 1,
                nombre = "Tarea 1",
                descripcion = "Descripción de la tarea 1",
                fechaCreacion = Date(),
                prioridad = Prioridad.ALTA,
                fechaLimite = Date(),
                estado = Estado.EN_TIEMPO,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        crearTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a media
        crearTareasModel.cambiarPrioridad(1)

        // Comprobar que se ha cambiado correctamente la prioridad
        assert(
            crearTareasModel
                .observarTareaDTO()
                .value!!
                .tarea.prioridad == Prioridad.MEDIA,
        )
    }

    @Test
    fun cambiarPrioridadAlta() {
        // Crear tareaDTO de prueba
        val tarea =
            Tarea(
                id = 1,
                nombre = "Tarea 1",
                descripcion = "Descripción de la tarea 1",
                fechaCreacion = Date(),
                prioridad = Prioridad.MEDIA,
                fechaLimite = Date(),
                estado = Estado.EN_TIEMPO,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        crearTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a alta
        crearTareasModel.cambiarPrioridad(0)

        // Comprobar que se ha cambiado correctamente la prioridad
        assert(
            crearTareasModel
                .observarTareaDTO()
                .value!!
                .tarea.prioridad == Prioridad.ALTA,
        )
    }

    @Test
    fun cambiarPrioridadBaja() {
        // Crear tareaDTO de prueba
        val tarea =
            Tarea(
                id = 1,
                nombre = "Tarea 1",
                descripcion = "Descripción de la tarea 1",
                fechaCreacion = Date(),
                prioridad = Prioridad.ALTA,
                fechaLimite = Date(),
                estado = Estado.EN_TIEMPO,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        crearTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a baja
        crearTareasModel.cambiarPrioridad(2)

        // Comprobar que se ha cambiado correctamente la prioridad
        assert(
            crearTareasModel
                .observarTareaDTO()
                .value!!
                .tarea.prioridad == Prioridad.BAJA,
        )
    }

    @Test
    fun cambiarPrioridadNoEstablecido() {
        // Crear tareaDTO de prueba
        val tarea =
            Tarea(
                id = 1,
                nombre = "Tarea 1",
                descripcion = "Descripción de la tarea 1",
                fechaCreacion = Date(),
                prioridad = Prioridad.ALTA,
                fechaLimite = Date(),
                estado = Estado.EN_TIEMPO,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        crearTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a no establecido
        crearTareasModel.cambiarPrioridad(3)

        // Comprobar que se ha cambiado correctamente la prioridad
        assert(
            crearTareasModel
                .observarTareaDTO()
                .value!!
                .tarea.prioridad == Prioridad.NO_ESTABLECIDO,
        )
    }

    @Test
    fun cambiarPrioridadNoValida() {
        // Crear tareaDTO de prueba
        val tarea =
            Tarea(
                id = 1,
                nombre = "Tarea 1",
                descripcion = "Descripción de la tarea 1",
                fechaCreacion = Date(),
                prioridad = Prioridad.ALTA,
                fechaLimite = Date(),
                estado = Estado.EN_TIEMPO,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        crearTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a un valor no válido
        crearTareasModel.cambiarPrioridad(-1)

        // Comprobar que se ha cambiado correctamente la prioridad a alta por defecto
        assert(
            crearTareasModel
                .observarTareaDTO()
                .value!!
                .tarea.prioridad == Prioridad.ALTA,
        )
    }

    @Test
    fun actualizarFechaLimite() {
        // Crear tareaDTO de prueba
        val tarea =
            Tarea(
                id = 1,
                nombre = "Tarea 1",
                descripcion = "Descripción de la tarea 1",
                fechaCreacion = Date(),
                prioridad = Prioridad.ALTA,
                fechaLimite = Date(),
                estado = Estado.EN_TIEMPO,
            )
        val etiqueta =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        crearTareasModel.definirTareaDTO(tareaDTO)

        // Actualizar fecha límite
        val nuevaFechaLimite = Date()
        crearTareasModel.actualizarFechaLimite(nuevaFechaLimite)

        // Comprobar que se ha actualizado correctamente la fecha límite
        assert(
            crearTareasModel
                .observarTareaDTO()
                .value!!
                .tarea.fechaLimite == nuevaFechaLimite,
        )
    }

    @Test
    fun comprobarListaEtiquetasConEtiquetas() {
        val etiqueta1 =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val etiqueta2 =
            Etiqueta(
                id = 2,
                nombre = "Etiqueta 2",
                descripcion = "Descripción de la etiqueta 2",
            )
        val etiquetas = listOf(etiqueta1, etiqueta2)

        // Comprobar que se devuelve la lista de etiquetas sin modificaciones
        val resultado = crearTareasModel.comprobarListaEtiquetas(etiquetas)
        assert(resultado.size == 2)
        assert(resultado[0] == etiqueta1)
        assert(resultado[1] == etiqueta2)
    }

    @Test
    fun actualizarEtiquetasTarea() =
        runTest {
            // Crear tareaDTO de prueba
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "Tarea 1",
                    descripcion = "Descripción de la tarea 1",
                    fechaCreacion = Date(),
                    prioridad = Prioridad.ALTA,
                    fechaLimite = Date(),
                    estado = Estado.EN_TIEMPO,
                )
            val etiqueta1 =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta 1",
                    descripcion = "Descripción de la etiqueta 1",
                )
            val etiqueta2 =
                Etiqueta(
                    id = 2,
                    nombre = "Etiqueta 2",
                    descripcion = "Descripción de la etiqueta 2",
                )
            val tareaDTO = TareaDTO(tarea, listOf(etiqueta1))

            // Definir tareaDTO en el modelo
            crearTareasModel.definirTareaDTO(tareaDTO)

            // Actualizar etiquetas de la tarea
            crearTareasModel.actualizarEtiquetasTarea(listOf(etiqueta2))

            // Comprobar que se ha actualizado correctamente la lista de etiquetas de la tarea
            assert(
                crearTareasModel
                    .observarTareaDTO()
                    .value!!
                    .etiquetas.size == 1,
            )
            assert(crearTareasModel.observarTareaDTO().value!!.etiquetas[0] == etiqueta2)
        }

    @Test
    fun obtenerListaEtiquetasTarea() {
        // Crear tareaDTO de prueba
        val tarea =
            Tarea(
                id = 1,
                nombre = "Tarea 1",
                descripcion = "Descripción de la tarea 1",
                fechaCreacion = Date(),
                prioridad = Prioridad.ALTA,
                fechaLimite = Date(),
                estado = Estado.EN_TIEMPO,
            )
        val etiqueta1 =
            Etiqueta(
                id = 1,
                nombre = "Etiqueta 1",
                descripcion = "Descripción de la etiqueta 1",
            )
        val etiqueta2 =
            Etiqueta(
                id = 2,
                nombre = "Etiqueta 2",
                descripcion = "Descripción de la etiqueta 2",
            )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta1, etiqueta2))

        // Definir tareaDTO en el modelo
        crearTareasModel.definirTareaDTO(tareaDTO)

        // Obtener lista de etiquetas de la tarea
        val resultado = crearTareasModel.obtenerListaEtiquetasTarea()

        // Comprobar que se ha obtenido correctamente la lista de etiquetas de la tarea
        assert(resultado.size == 2)
        assert(resultado[0] == etiqueta1)
        assert(resultado[1] == etiqueta2)
    }

    @Test
    fun actualizarFiltroListaEtiquetaTareas() {
        // Lista etiquetas
        val etiqueta = Etiqueta(id = 1, nombre = "Trabajo")
        val lista = listOf(etiqueta)

        // Dejamos a etiquetas restantes como observado
        crearTareasModel.obtenerEtiquetasRestantes().observeForever {}

        // Forzamos la acción
        crearTareasModel.actualizarFiltroListaEtiquetaTareas(lista)

        // Verificamos si ha sido afectaod el filtro de etiquetas restantes
        Mockito.verify(crearTareasRepository).obtenerEtiquetasRestantes(lista)
    }

    @Test
    fun obtenerEtiquetasRestantes() {
        // Lista etiquetas
        val listaEtiquetas = listOf<Etiqueta>()

        // Invocar metodo
        crearTareasModel.obtenerEtiquetasRestantes()

        // Comprobar que se ha llamado al método del repositorio para obtener las etiquetas restantes
        Mockito.verify(crearTareasRepository).obtenerEtiquetasRestantes(listaEtiquetas)
    }
}
