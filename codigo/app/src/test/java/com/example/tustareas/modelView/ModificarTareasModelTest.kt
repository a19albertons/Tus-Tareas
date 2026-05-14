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
import com.example.tustareas.repository.ModificarTareasRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date


/**
 * Clase que tiene las pruebas unitarias de modificar tareas model
 */
class ModificarTareasModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para corutinas
    @get:Rule
    val ruleCorutinas = MainDispatcherRule()

    // Definición repositorio
    val modificarTareasRepository = Mockito.mock(ModificarTareasRepository::class.java)

    // Definición modelo
    val modificarTareasModel = ModificarTareasModel(Application(), modificarTareasRepository)

    @Test
    fun tituloDialogoNueva() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 0,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val tareaDTO = TareaDTO(tarea, emptyList())

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Comprobar que el título del diálogo es el esperado
        assert(modificarTareasModel.tituloDialogo() == R.string.confirmar_guardar_tarea)
    }

    @Test
    fun tituloDialogoExistentes() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val tareaDTO = TareaDTO(tarea, emptyList())

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Comprobar que el título del diálogo es el esperado
        assert(modificarTareasModel.tituloDialogo() == R.string.confirmar_modificado_tarea)
    }

    @Test
    fun guardarTareaNueva() = runTest {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 0,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Guardar tarea
        modificarTareasModel.guardarYModificarTarea("Tarea 1", "Descripción de la tarea 1")

        // Comprobar que se ha llamado al método del repositorio para guardar la tarea
        Mockito.verify(modificarTareasRepository).insertarTareaConEtiqueta(tareaDTO)
        assert(modificarTareasModel.observarResultado().value == true)
    }

    @Test
    fun guardarTareaNuevaNoValida() = runTest {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 0,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Guardar tarea
        modificarTareasModel.guardarYModificarTarea("", "Descripción de la tarea 1")

        // Comprobar error
        assert(modificarTareasModel.observarMensajeError().value == R.string.error_guardar_tarea)
        assert(modificarTareasModel.observarResultado().value == false)
    }

    @Test
    fun guardarTareaExistente() = runTest {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.COMPLETADA
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Guardar tarea
        modificarTareasModel.guardarYModificarTarea("Tarea 1", "Descripción de la tarea 1")

        // Comprobar que se ha llamado al método del repositorio para modificar la tarea
        Mockito.verify(modificarTareasRepository).modificarTareaConEtiqueta(tareaDTO)
        assert(modificarTareasModel.observarResultado().value == true)
    }

    @Test
    fun guardarTareaExistenteNoValida() = runTest {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Guardar tarea
        modificarTareasModel.guardarYModificarTarea("", "Descripción de la tarea 1")

        // Comprobar error
        assert(modificarTareasModel.observarMensajeError().value == R.string.error_modificar_tarea)
        assert(modificarTareasModel.observarResultado().value == false)
    }

    @Test
    fun observarTareaDTO() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Comprobar que se ha definido correctamente el tareaDTO
        assert(modificarTareasModel.observarTareaDTO().value == tareaDTO)
    }

    @Test
    fun prioridadOrdinal() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Comprobar que se ha definido correctamente el tareaDTO
        assert(modificarTareasModel.prioridadOrdinal() == Prioridad.ALTA.ordinal)
    }

    @Test
    fun cambiarPrioridadMedia() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a media
        modificarTareasModel.cambiarPrioridad(1)

        // Comprobar que se ha cambiado correctamente la prioridad
        assert(modificarTareasModel.observarTareaDTO().value!!.tarea.prioridad == Prioridad.MEDIA)
    }

    @Test
    fun cambiarPrioridadAlta() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.MEDIA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a alta
        modificarTareasModel.cambiarPrioridad(0)

        // Comprobar que se ha cambiado correctamente la prioridad
        assert(modificarTareasModel.observarTareaDTO().value!!.tarea.prioridad == Prioridad.ALTA)
    }

    @Test
    fun cambiarPrioridadBaja() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a baja
        modificarTareasModel.cambiarPrioridad(2)

        // Comprobar que se ha cambiado correctamente la prioridad
        assert(modificarTareasModel.observarTareaDTO().value!!.tarea.prioridad == Prioridad.BAJA)
    }

    @Test
    fun cambiarPrioridadNoEstablecido() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a no establecido
        modificarTareasModel.cambiarPrioridad(3)

        // Comprobar que se ha cambiado correctamente la prioridad
        assert(modificarTareasModel.observarTareaDTO().value!!.tarea.prioridad == Prioridad.NO_ESTABLECIDO)
    }

    @Test
    fun cambiarPrioridadNoValida() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Cambiar prioridad a un valor no válido
        modificarTareasModel.cambiarPrioridad(-1)

        // Comprobar que se ha cambiado correctamente la prioridad a alta por defecto
        assert(modificarTareasModel.observarTareaDTO().value!!.tarea.prioridad == Prioridad.ALTA)
    }

    @Test
    fun actualizarFechaLimite() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Actualizar fecha límite
        val nuevaFechaLimite = Date()
        modificarTareasModel.actualizarFechaLimite(nuevaFechaLimite)

        // Comprobar que se ha actualizado correctamente la fecha límite
        assert(modificarTareasModel.observarTareaDTO().value!!.tarea.fechaLimite == nuevaFechaLimite)
    }

    @Test
    fun comprobarListaEtiquetasConEtiquetas() {
        val etiqueta1 = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val etiqueta2 = Etiqueta(
            id = 2,
            nombre = "Etiqueta 2",
            descripcion = "Descripción de la etiqueta 2",
        )
        val etiquetas = listOf(etiqueta1, etiqueta2)

        // Comprobar que se devuelve la lista de etiquetas sin modificaciones
        val resultado = modificarTareasModel.comprobarListaEtiquetas(etiquetas)
        assert(resultado.size == 2)
        assert(resultado[0] == etiqueta1)
        assert(resultado[1] == etiqueta2)
    }

    @Test
    fun actualizarEtiquetasTarea() = runTest {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta1 = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val etiqueta2 = Etiqueta(
            id = 2,
            nombre = "Etiqueta 2",
            descripcion = "Descripción de la etiqueta 2",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta1))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Actualizar etiquetas de la tarea
        modificarTareasModel.actualizarEtiquetasTarea(listOf(etiqueta2))

        // Comprobar que se ha actualizado correctamente la lista de etiquetas de la tarea
        assert(modificarTareasModel.observarTareaDTO().value!!.etiquetas.size == 1)
        assert(modificarTareasModel.observarTareaDTO().value!!.etiquetas[0] == etiqueta2)
    }

    @Test
    fun obtenerListaEtiquetasTarea() {
        // Crear tareaDTO de prueba
        val tarea = Tarea(
            id = 1,
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fechaCreacion = Date(),
            prioridad = Prioridad.ALTA,
            fechaLimite = Date(),
            estado = Estado.EN_TIEMPO
        )
        val etiqueta1 = Etiqueta(
            id = 1,
            nombre = "Etiqueta 1",
            descripcion = "Descripción de la etiqueta 1",
        )
        val etiqueta2 = Etiqueta(
            id = 2,
            nombre = "Etiqueta 2",
            descripcion = "Descripción de la etiqueta 2",
        )
        val tareaDTO = TareaDTO(tarea, listOf(etiqueta1, etiqueta2))

        // Definir tareaDTO en el modelo
        modificarTareasModel.definirTareaDTO(tareaDTO)

        // Obtener lista de etiquetas de la tarea
        val resultado = modificarTareasModel.obtenerListaEtiquetasTarea()

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
        modificarTareasModel.obtenerEtiquetasRestantes().observeForever {}

        // Forzamos la acción
        modificarTareasModel.actualizarFiltroListaEtiquetaTareas(lista)

        // Verificamos si ha sido afectaod el filtro de etiquetas restantes
        Mockito.verify(modificarTareasRepository).obtenerEtiquetasRestantes(lista)
    }

    @Test
    fun obtenerEtiquetasRestantes() {
        // Lista etiquetas
        val listaEtiquetas = listOf<Etiqueta>()

        // Invocar metodo
        modificarTareasModel.obtenerEtiquetasRestantes()

        // Comprobar que se ha llamado al método del repositorio para obtener las etiquetas restantes
        Mockito.verify(modificarTareasRepository).obtenerEtiquetasRestantes(listaEtiquetas)
    }



}