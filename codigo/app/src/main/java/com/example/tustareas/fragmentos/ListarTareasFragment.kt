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
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import com.google.android.material.snackbar.Snackbar

/**
 * Clase que gestiona el fragmento de listar tareas.
 */
class ListarTareasFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentListarTareasBinding? = null
    private val binding: FragmentListarTareasBinding
            get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListarTareasBinding.inflate(inflater, container, false)
        val view = binding.root

        // Scroll view de tareas
        // Definir el layout
        binding.listaTareas.layoutManager = LinearLayoutManager(requireContext())

        // Definir el adapter
        val adapter = TareasAdapter(model)
        binding.listaTareas.adapter = adapter
        // Actualizado con el nuevo sistema que evita duplicado de observers
        model.listarTareas.obtenerTareasFiltradas().observe(viewLifecycleOwner) {
            listaTareas ->
            adapter.submitList(listaTareas)
            if (listaTareas.isEmpty()) {
                binding.sinResultados.visibility = View.VISIBLE
                binding.listaTareas.visibility = View.GONE
            }
            else  {
                binding.sinResultados.visibility = View.GONE
                binding.listaTareas.visibility = View.VISIBLE
            }
        }

        // Observar errores del listado de tareas
        model.listarTareas.mensajeError.observe(viewLifecycleOwner) {
            error ->
            error?.let {
                Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
                // Restaurar a null tras ser mostrado
                model.listarTareas.mensajeError.value = null
            }
        }

        // spinner prioridad tareas
        val contenidoSpiner = listOf(getString(R.string.prioridad)) + Prioridad.entries.map { getString(it.labelRes()) }
        binding.prioridadTarea.adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_personalizado,
            contenidoSpiner
        )
        binding.prioridadTarea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var prioridad: Array<Prioridad>
                // Cambia la prioridad
                when (position) {
                    0-> {
                        prioridad = Prioridad.entries.toTypedArray()
                    }
                    1-> {
                        prioridad = Array(1) { Prioridad.Alta }
                    }
                    2-> {
                        prioridad = Array(1) { Prioridad.Media }
                    }
                    3-> {
                        prioridad = Array(1) { Prioridad.Baja }
                    }
                    4-> {
                        prioridad = Array(1) { Prioridad.NoEstablecido }
                    }
                    else -> {
                        prioridad = Prioridad.entries.toTypedArray()
                    }
                }
                // Observa la lista filtrada
                model.listarTareas.actualizarPrioridadListadoTareas(prioridad)

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Observar errores del listado de tareas
        model.listarTareas.mensajeError.observe(viewLifecycleOwner) { errorResId ->
            errorResId?.let {
                Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
                // Limpiar el error después de mostrarlo para evitar duplicados al rotar o volver
                model.listarTareas.mensajeError.value = null
            }
        }

        // spinner prioridad tareas
        val contenidoSpinerEstado = listOf(getString(R.string.estado)) + Estado.entries.map { getString(it.labelRes()) }
        binding.estadoTarea.adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_personalizado,
            contenidoSpinerEstado
        )
        binding.estadoTarea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var estado: Array<Estado>
                // Cambia la prioridad
                when (position) {
                    0-> {
                        estado = Estado.entries.toTypedArray()
                    }
                    1-> {
                        estado = Array(1) { Estado.EnTiempo }
                    }
                    2-> {
                        estado = Array(1) { Estado.Retrasada }

                    }
                    3-> {
                        estado = Array(1) { Estado.Completada }
                    }
                    else -> {
                        estado = Estado.entries.toTypedArray()
                    }
                }
                // Observa la lista filtrada
                model.listarTareas.actualizarEstadoListadoTareas(estado)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Texto filtro
        binding.filtro.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(texto: CharSequence?, empieza: Int, posicion: Int, siguiente: Int) {

            }
            override fun onTextChanged(texto: CharSequence?, empieza: Int, fin: Int, posicion: Int) {

            }
            override fun afterTextChanged(texto: Editable?) {
                // Actualiza el texto del filtro como si fuese un observer unificado evita los dupliados que antes se generaban
                model.listarTareas.actualizarTextoListadoTareas(texto.toString())
            }

        })

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
                        model.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_ASC)
                        true
                    }
                    R.id.action_fecha_limite_des -> {
                        model.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_DES)
                        true
                    }
                    R.id.action_fecha_creacion_asc -> {
                        model.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_ASC)
                        true
                    }
                    R.id.action_fecha_creacion_des -> {
                        model.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_DES)
                        true
                    }
                    else -> false
                }
            }
            desplegable.show()
        }


        // Boton añadir tareas
        binding.anadirTarea.setOnClickListener {
            val tarea = Tarea(0, "", null, null, Prioridad.Alta, DateHelper.fechaMediaNocheUTC(), Estado.EnTiempo, null)
            val dto = TareaDTO(tarea, emptyList())
            try {
                findNavController().navigate(ListarTareasFragmentDirections.actionListarTareasFragmentToModificarTareasFragment(dto))
            }
            catch (_: Exception) {
                Snackbar.make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}