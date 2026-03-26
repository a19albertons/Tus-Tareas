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
import com.google.android.material.snackbar.Snackbar
import java.util.Date

class ListarTareasFragment : Fragment() {
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

        // Obtener recycler view
        val recyclerView = binding.listaTareas

        // Definir el layout
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Definir el adapter
        val adapter = TareasAdapter(emptyList(), model)
        recyclerView.adapter = adapter
        // Actualizado con el nuevo sistema que evita duplicado de observers
        model.obtenerTareasFiltradas().observe(viewLifecycleOwner) {
            listaTareas ->
            adapter.submitList(listaTareas)
            if (listaTareas.isEmpty()) {
                binding.sinResultados.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
            else  {
                binding.sinResultados.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        // spinner prioridad tareas
        val contenidoSpiner = listOf("Prioridad", "Alta", "Media", "Baja", "No establecido")
        binding.prioridadTarea.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
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
                model.actualizarPrioridadListadoTareas(prioridad)

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // spinner prioridad tareas
        val contenidoSpinerEstado = listOf("Estado", "En tiempo", "Retrasado", "Completada")
        binding.estadoTarea.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
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
                model.actualizarEstadoListadoTareas(estado)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Texto filtro
        val filtro = binding.filtro
        filtro.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(texto: CharSequence?, empieza: Int, posicion: Int, siguiente: Int) {

            }
            override fun onTextChanged(texto: CharSequence?, empieza: Int, fin: Int, posicion: Int) {

            }
            override fun afterTextChanged(texto: Editable?) {
                // Actualiza el texto del filtro como si fuese un observer unificado evita los dupliados que antes se generaban
                model.actualizarTextoListadoTareas(texto.toString())
            }

        })

        // Desplegable de las tres barras
        binding.menuTareas.setOnClickListener {
            ancla ->
            val desplegable = PopupMenu(requireContext(), ancla)
            desplegable.menuInflater.inflate(R.menu.menu_tareas, desplegable.menu)

            desplegable.setOnMenuItemClickListener { clickado ->
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


        // Boton añadir tareas
        binding.anadirTarea.setOnClickListener {
            val tarea = Tarea(0, "", null, null, Prioridad.Alta, Date(), Estado.EnTiempo, null)
            val dto = TareaDTO(tarea, emptyList())
            try {
                findNavController().navigate(ListarTareasFragmentDirections.actionListarTareasFragmentToModificarTareasFragment(dto))
            }
            catch (_: Exception) {
                Snackbar.make(binding.root, "Ha habido un error al navegar", Snackbar.LENGTH_SHORT)
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