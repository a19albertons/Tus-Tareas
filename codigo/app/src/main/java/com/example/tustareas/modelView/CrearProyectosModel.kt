package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.tustareas.R
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearProyectosRepository
import com.example.tustareas.util.DateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * Clase que gestiona el submodelo de crear proyecto
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de crear proyectos
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class CrearProyectosModel
    @Inject
    constructor(
        application: Application,
        private val repository: CrearProyectosRepository,
    ) : AndroidViewModel(application) {
        // Variable de proyectoDTO
        private val _proyectoDTO: MutableLiveData<ProyectoDTO> = MutableLiveData()
        val proyectoDTO: MutableLiveData<ProyectoDTO>
            get() = _proyectoDTO

        // Variable lista de tareas temporal
        private val listaTareas: MutableLiveData<List<Tarea>> = MutableLiveData(emptyList<Tarea>())
        private var listaTareasRestante: List<Tarea> = emptyList()

        // Variable lisda de etiquetas temporal
        private val listaEtiquetas: MutableLiveData<List<Etiqueta>> = MutableLiveData(emptyList<Etiqueta>())
        private var listaEtiquetasRestante: List<Etiqueta> = emptyList()

        private val mensajeError: MutableLiveData<Int> = MutableLiveData()

        private val resultado: MutableLiveData<Boolean> = MutableLiveData(false)

        /**
         * Define el proyecto a modificar
         *
         * @param proyectoDTO El proyecto a modificar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun definirProyectoDTO(proyectoDTO: ProyectoDTO) {
            _proyectoDTO.value = proyectoDTO
        }

        /**
         * Observa el proyecto a modificar
         *
         * @return Un LiveData que contiene el proyecto a modificar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun observarProyectoDTO(): LiveData<ProyectoDTO> = proyectoDTO

        /**
         * Actualiza la lista de tareas del proyecto
         *
         * @param lista La nueva lista de tareas del proyecto
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun actualizarTareasDelProyecto(lista: List<Tarea>) {
            _proyectoDTO.value!!.tareas = lista
        }

        /**
         * Obtiene la lista de tareas del proyecto
         *
         * @return Una lista de tareas del proyecto
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerTareasDelProyecto(): List<Tarea> = proyectoDTO.value!!.tareas

        /**
         * Actualiza la lista de etiquetas del proyecto
         *
         * @param lista La nueva lista de etiquetas del proyecto
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun actualizarEtiquetasDelProyecto(lista: List<Etiqueta>) {
            _proyectoDTO.value!!.etiquetas = lista
        }

        /**
         * Obtiene la lista de etiquetas del proyecto
         *
         * @return Una lista de etiquetas del proyecto
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerEtiquetasDelProyecto(): List<Etiqueta> = proyectoDTO.value!!.etiquetas

        /**
         * Añade una tarea a la lista de tareas del proyecto, comprobando que la posición es válida y que no es un valor por defecto
         *
         * @param posicion La posición de la tarea a añadir en la lista de tareas disponibles
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun anadirTareaAlProyecto(posicion: Int) {
            if (listaTareasRestante.isNotEmpty() &&
                // Lista vacia
                posicion >= 0 &&
                posicion < listaTareasRestante.size &&
                // Protegerse de fuera de limites
                listaTareasRestante[posicion].id != 0 // Evitar que sea un valor por defecto de no hay tareas
            ) {
                // Obtener nueva tarea, la lista de tareas y añadirla actualizando las disponibles)
                val tareaAnadir = listaTareasRestante[posicion]
                val nuevasTareasDTO = proyectoDTO.value!!.tareas.toMutableList()
                nuevasTareasDTO.add(tareaAnadir)
                _proyectoDTO.value!!.tareas = nuevasTareasDTO
            }
        }

        /**
         * Añade una etiqueta a la lista de etiquetas del proyecto, comprobando que la posición es válida y que no es un valor por defecto
         *
         * @param posicion La posición de la etiqueta a añadir en la lista de etiquetas disponibles
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun anadirEtiquetaAlProyecto(posicion: Int) {
            if (listaEtiquetasRestante.isNotEmpty() &&
                // Lista vacia
                posicion >= 0 &&
                posicion < listaEtiquetasRestante.size &&
                // Protegerse de fuera de limites
                listaEtiquetasRestante[posicion].id != 0 // Evitar que sea un valor por defecto de no hay etiquetas
            ) {
                // Obtener nueva etiqueta, la lista de etiquetas y añadirla actualizando las disponibles)
                val etiquetaAnadir = listaEtiquetasRestante[posicion]
                val nuevasEtiquetasDTO = proyectoDTO.value!!.etiquetas.toMutableList()
                nuevasEtiquetasDTO.add(etiquetaAnadir)
                _proyectoDTO.value!!.etiquetas = nuevasEtiquetasDTO
            }
        }

        /**
         * Establece la fecha de inicio del proyecto a modificar
         *
         * @param fechaInicio La nueva fecha de inicio del proyecto a modificar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun establecerFechaInicioProyecto(fechaInicio: Date) {
            _proyectoDTO.value!!.proyecto.fechaInicio = fechaInicio
        }

        /**
         * Establece la fecha de fin del proyecto a modificar
         *
         * @param fechaFin La nueva fecha de fin del proyecto a modificar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun establecerFechaFinProyecto(fechaFin: Date) {
            _proyectoDTO.value!!.proyecto.fechaFin = fechaFin
        }

        /**
         * Guarda o modifica un proyecto en función del id del dto del proyecto, si el id es 0 se guarda un nuevo proyecto sino se modifica el existente.
         * Comprueba logicas de negocio, actualzia campos recibidos y lanza mensajes error en caso de error o fallo en las validaciones. Si todo es correcto
         * se eactualiza corectamente.
         *
         * @param nombre El nuevo nombre del proyecto a guardar o modificar
         * @param descripcion La nueva descripción del proyecto a guardar o modificar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun guardarProyecto(
            nombre: String,
            descripcion: String,
        ) {
            // Comprobar que el titulo del proyecto no este vacio
            if (nombre.isBlank()) {
                mensajeError.value = R.string.error_guardar_proyecto
                return
            }

            // Actualizamos los campos de texto con los ultimo
            _proyectoDTO.value!!.proyecto.nombre = nombre
            _proyectoDTO.value!!.proyecto.descripcion = descripcion

            // Generamos un hilo con la nueva tarea
            viewModelScope.launch {
                try {
                    insertarProyectoConTareaYEtiqueta(proyectoDTO.value!!)
                    // Vovlemos a la vista previa
                    resultado.value = true
                } catch (_: Exception) {
                    mensajeError.value = R.string.error_guardar_proyecto
                }
            }
        }

        /**
         * Observa el mensaje de error a mostrar en caso de que haya algun error
         *
         * @return Un LiveData que contiene un entero que representa el recurso del mensaje de error
         * a mostrar en caso de que haya algun error
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun observarMensajeError(): LiveData<Int> = mensajeError

        /**
         * Observa el resultado de la operación de guardar o modificar el proyecto. Si es true, se ha
         * guardado o modificado correctamente y se debe volver a la vista previa. Si es false, no se
         * ha guardado o modificado correctamente y se debe mostrar un mensaje de error
         *
         * @return Un LiveData que contiene un booleano que indica si la operación de guardar o
         * modificar el proyecto ha sido exitosa
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun observarResultado(): LiveData<Boolean> = resultado

        /**
         * Procesa la lista que recibe y no tiene contendio la remplaza por una tarea por defecto que
         * actua como un aviso
         *
         * @param tareas La lista de tareas a procesar
         * @return Una lista de tareas procesada, si la lista recibida no tiene contenido se devuelve
         * una lista con una etiqueta que informa de que no hay tareas disponibles
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun tareasRestantesProcesadas(tareas: List<Tarea>): List<Tarea> {
            // Variable a devolver
            val devolver: List<Tarea>
            // Se comprueba lo obtenido de la base de datos
            if (tareas.isEmpty()) {
                devolver =
                    listOf(
                        Tarea(
                            0,
                            getApplication<Application>().getString(R.string.no_existen_tareas),
                            null,
                            null,
                            Prioridad.NO_ESTABLECIDO,
                            DateHelper.fechaMediaNocheUTC(),
                            Estado.EN_TIEMPO,
                            null,
                        ),
                    )
            } else {
                devolver = tareas
            }
            // Aplicamos la misma lista del spinner para evitar problemas de posiciones
            listaTareasRestante = devolver
            return devolver
        }

        /**
         * Procesa la lista que recibe y no tiene contendio la remplaza por una etiqueta por defecto que
         * actua como un aviso
         *
         * @param etiquetas La lista de etiquetas a procesar
         * @return Una lista de etiquetas procesada, si la lista recibida no tiene contenido se devuelve
         * una lista con una etiqueta que informa de que no hay etiquetas disponibles
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun etiquetasRestantesProcesadas(etiquetas: List<Etiqueta>): List<Etiqueta> {
            // Variable a devolver
            val devolver: List<Etiqueta>
            // Se comprueba lo obtenido de la base de datos
            if (etiquetas.isEmpty()) {
                devolver = listOf(Etiqueta(0, getApplication<Application>().getString(R.string.no_existen_etiquetas)))
            } else {
                devolver = etiquetas
            }
            // Aplicamos la misma lista del spinner para evitar problemas de posiciones
            listaEtiquetasRestante = devolver
            return devolver
        }

        /**
         * Actualiza el filtro de la lista de tareas del proyecto
         *
         * @param lista La nueva lista de tareas del proyecto
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun actualizarFiltroListaTareaProyecto(lista: List<Tarea>) {
            listaTareas.value = lista
        }

        /**
         * Obtiene las tareas restantes (libres) que no tiene el proyecto actual
         *
         * @return Un liveData que contiene una lista de tareas restantes (libres) que no tiene el proyecto actual
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerTareasRestantes(): LiveData<List<Tarea>> =
            listaTareas.switchMap { texto ->
                repository.obtenerTareasRestantes(texto, proyectoDTO.value!!.proyecto.id)
            }

        /**
         * Actualiza el filtro de la lista de etiquetas del proyecto
         *
         * @param lista La nueva lista de etiquetas del proyecto
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun actualizarFiltroListaEtiquetaProyecto(lista: List<Etiqueta>) {
            listaEtiquetas.value = lista
        }

        /**
         * Obtiene las etiquetas restantes (libres) que no tiene el proyecto actual
         *
         * @return Un LiveData que contiene una lista de etiquetas restantes (libres) que no tiene el proyecto actual
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerEtiquetasRestantes(): LiveData<List<Etiqueta>> =
            listaEtiquetas.switchMap { texto ->
                repository.obtenerEtiquetasRestantes(texto)
            }

        /**
         * Inserta un proyecto con sus tareas e etiquetas en la base de datos
         *
         * @param proyectoDTO El proyecto con sus tareas e etiquetas a insertar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        private suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) =
            repository.insertarProyectoConTareaYEtiqueta(proyectoDTO)
    }
