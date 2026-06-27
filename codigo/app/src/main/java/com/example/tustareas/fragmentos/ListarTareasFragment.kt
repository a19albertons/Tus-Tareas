package com.example.tustareas.fragmentos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.TareasAdapter
import com.example.tustareas.databinding.FragmentListarTareasBinding
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.modelView.ListarTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que gestiona el fragmento de listar tareas.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class ListarTareasFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentListarTareasBinding? = null
    val binding: FragmentListarTareasBinding
        get() = _binding!!

    val model: ListarTareasModel by viewModels()

    private var adapter: TareasAdapter? = null

    /**
     * Crea la vista del fragmento de listar tareas y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento de listar tareas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListarTareasBinding.inflate(inflater, container, false)

        // Configura el RecyclerView para mostrar las tareas
        configurarRecyclerView()

        // Gestiona los errores del adapter
        gestionErrorAdapter()

        // Gestiona el spinner de prioridad
        gestinarSpinnerPrioridad()

        // Gestionar el spinner de estado
        gestionarSpinnerEstado()

        // Gestiona el filtro de texto para las tareas
        gestionarFiltroTexto()

        // Configura que la imagen de tres barras muestre un menú
        configurarMenuTresBarras()

        // Gestiona el botón de añadir tarea
        gestionarBotonAnadirTarea()

        return binding.root
    }

    /**
     * Función privada que configura el RecyclerView para mostrar las tareas. Define el layout, el adapter y observa los cambios en la lista de tareas filtradas para actualizar el adapter y mostrar un mensaje si no hay resultados.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun configurarRecyclerView() {
        // Scroll view de tareas
        // Definir el layout
        binding.listaTareas.layoutManager = LinearLayoutManager(requireContext())

        // Definir el adapter
        adapter = TareasAdapter(model)
        binding.listaTareas.adapter = adapter

        // Actualizado con el nuevo sistema que evita duplicado de observers
        model.obtenerTareasFiltradas().observe(viewLifecycleOwner) { listaTareas ->
            adapter!!.submitList(listaTareas)
            if (listaTareas.isEmpty()) {
                binding.sinResultados.visibility = View.VISIBLE
                binding.listaTareas.visibility = View.GONE
            } else {
                binding.sinResultados.visibility = View.GONE
                binding.listaTareas.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Función privada que gestiona los errores del adapter. Observa los errores del listado de tareas y muestra un mensaje de error utilizando un Snackbar. Después de mostrar el error, limpia el valor del error para evitar mostrar mensajes duplicados al rotar o volver al fragmento.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionErrorAdapter() {
        // Observar errores del listado de tareas
        model.mensajeError.observe(viewLifecycleOwner) { errorResId ->
            errorResId?.let {
                Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
                // Limpiar el error después de mostrarlo para evitar duplicados al rotar o volver
                model.mensajeError.value = null
            }
        }
    }

    /**
     * Función privada que gestiona el spinner de prioridad. Configura el contenido del spinner con las opciones de prioridad y un valor por defecto, y gestiona el evento de selección para actualizar la lista de tareas filtradas en función de la prioridad seleccionada.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestinarSpinnerPrioridad() {
        // spinner prioridad tareas
        val contenidoSpiner = listOf(getString(R.string.prioridad)) + Prioridad.entries.map { getString(it.labelRes()) }
        binding.prioridadTarea.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_personalizado,
                contenidoSpiner,
            )

        // Gestiona el evento de selección del spinner de prioridad
        binding.prioridadTarea.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    var prioridad: Array<Prioridad>
                    // Cambia la prioridad
                    when (position) {
                        0 -> {
                            prioridad = Prioridad.entries.toTypedArray()
                        }
                        1 -> {
                            prioridad = Array(1) { Prioridad.ALTA }
                        }
                        2 -> {
                            prioridad = Array(1) { Prioridad.MEDIA }
                        }
                        3 -> {
                            prioridad = Array(1) { Prioridad.BAJA }
                        }
                        4 -> {
                            prioridad = Array(1) { Prioridad.NO_ESTABLECIDO }
                        }
                        else -> {
                            prioridad = Prioridad.entries.toTypedArray()
                        }
                    }
                    // Observa la lista filtrada
                    model.actualizarPrioridadListadoTareas(prioridad)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    /**
     * Función privada que gestiona el spinner de estado. Configura el contenido del spinner con las opciones de estado y un valor por defecto, y gestiona el evento de selección para actualizar la lista de tareas filtradas en función del estado seleccionado.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarSpinnerEstado() {
        // spinner prioridad tareas
        val contenidoSpinerEstado = listOf(getString(R.string.estado)) + Estado.entries.map { getString(it.labelRes()) }
        binding.estadoTarea.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_personalizado,
                contenidoSpinerEstado,
            )

        // Gestiona el evento de selección del spinner de estado
        binding.estadoTarea.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    var estado: Array<Estado>
                    // Cambia la prioridad
                    when (position) {
                        0 -> {
                            estado = Estado.entries.toTypedArray()
                        }
                        1 -> {
                            estado = Array(1) { Estado.EN_TIEMPO }
                        }
                        2 -> {
                            estado = Array(1) { Estado.RETRASADA }
                        }
                        3 -> {
                            estado = Array(1) { Estado.COMPLETADA }
                        }
                        else -> {
                            estado = Estado.entries.toTypedArray()
                        }
                    }
                    // Observa la lista filtrada
                    model.actualizarEstadoListadoTareas(estado)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    /**
     * Función privada que gestiona el filtro de texto para las tareas. Gestiona el evento de cambio de texto en el campo de filtro y actualiza el texto del filtro en el modelo para que este pueda filtrar las tareas en función del texto introducido.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarFiltroTexto() {
        // Texto filtro
        binding.filtro.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    texto: CharSequence?,
                    empieza: Int,
                    posicion: Int,
                    siguiente: Int,
                ) {
                }

                override fun onTextChanged(
                    texto: CharSequence?,
                    empieza: Int,
                    fin: Int,
                    posicion: Int,
                ) {
                }

                override fun afterTextChanged(texto: Editable?) {
                    // Actualiza el texto del filtro como si fuese un observer unificado evita los dupliados que antes se generaban
                    model.actualizarTextoListadoTareas(texto.toString())
                }
            },
        )
    }

    /**
     * Función privada que configura el menú desplegable de las tres barras. Gestiona el evento de click en la imagen de las tres barras para mostrar un menú desplegable con opciones de ordenación, y gestiona el evento de selección de cada opción para actualizar el texto de ordenación en el modelo y filtrar las tareas en función del criterio seleccionado.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun configurarMenuTresBarras() {
        // Desplegable de las tres barras
        // Muestra un pop up al ser clickado
        binding.menuTareas.setOnClickListener {
            // Nombre que le damos a las 3 barras para configurar el desplegable
            ancla ->
            val customizarTemaDesplegable = ContextThemeWrapper(requireContext(), R.style.fondoBlancoTareas)
            val desplegable = PopupMenu(customizarTemaDesplegable, ancla)
            desplegable.menuInflater.inflate(R.menu.menu_tareas, desplegable.menu)

            desplegable.setOnMenuItemClickListener { clickado ->
                // Toma de decisiones en función de cual sea clickada en base al id
                when (clickado.itemId) {
                    R.id.action_fecha_limite_asc -> {
                        model.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_ASC)
                        true
                    }
                    R.id.action_fecha_limite_des -> {
                        model.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_DES)
                        true
                    }
                    R.id.action_fecha_creacion_asc -> {
                        model.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_ASC)
                        true
                    }
                    R.id.action_fecha_creacion_des -> {
                        model.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_DES)
                        true
                    }
                    else -> false
                }
            }
            desplegable.show()
        }
    }

    /**
     * Función privada que gestiona el botón de añadir tarea. Gestiona el evento de click en el botón de añadir tarea para navegar a la vista de modificar tarea con un objeto TareaDTO vacío para crear una nueva tarea.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarBotonAnadirTarea() {
        // Boton añadir tareas
        binding.anadirTarea.setOnClickListener {
            val tarea = Tarea(0, "", null, null, Prioridad.ALTA, DateHelper.fechaMediaNocheUTC(), Estado.EN_TIEMPO, null)
            val dto = TareaDTO(tarea, emptyList())
            try {
                findNavController().navigate(ListarTareasFragmentDirections.actionListarTareasFragmentToCrearTareasFragment(dto))
            } catch (_: Exception) {
                Snackbar
                    .make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * Destruye la vista del fragmento de listar tareas y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Liberar recursos
        adapter = null
    }
}
