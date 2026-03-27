package com.example.tustareas.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.ListaEtiquetasPresentesAdapter
import com.example.tustareas.adapters.ListaTareasPresentesAdapter
import com.example.tustareas.databinding.FragmentModificarProyectoBinding
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.Date


class ModificarProyectoFragment : Fragment() {
    private var _binding : FragmentModificarProyectoBinding? = null
    private val binding : FragmentModificarProyectoBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )

    private lateinit var proyectoDTO : ProyectoDTO




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentModificarProyectoBinding.inflate(inflater, container, false)
        val view = binding.root

        val args = ModificarProyectoFragmentArgs.fromBundle(requireArguments())
        proyectoDTO = args.proyectoDTO

        binding.tituloProyecto.setText(proyectoDTO.proyecto.nombre)
        binding.descripcionProyecto.setText(proyectoDTO.proyecto.descripcion)
        binding.fechaCreacionTarea.text = DateHelper.timestampToString(proyectoDTO.proyecto.fechaCreacion)
        binding.fechaInicioProyecto.text = DateHelper.timestampToString(proyectoDTO.proyecto.fechaInicio)
        binding.fechaFinProyecto.text = DateHelper.timestampToString(proyectoDTO.proyecto.fechaFin)

        // spinner tareas
        var listaTareas = listOf(Tarea(0, getString(R.string.no_existen_tareas), null, null, Prioridad.NoEstablecido,
            Date(), Estado.EnTiempo, null))
        model.obtenerTareasRestantes().observe(viewLifecycleOwner) {
            tareas ->
            if (tareas.isEmpty()) {
                listaTareas = listOf(Tarea(0, getString(R.string.no_existen_tareas), null, null, Prioridad.NoEstablecido,
                    Date(), Estado.EnTiempo, null))
            }
            else {
                listaTareas = tareas
            }
            binding.listaTareas.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listaTareas.map { it.nombre }
            )
        }

        // Recycler view para las tareas
        val adapterTarea = ListaTareasPresentesAdapter {
                listaTareas ->
            proyectoDTO.tareas = listaTareas
            model.actualizarFiltroListaTareaProyecto(proyectoDTO.tareas)
        }
        binding.recyclerViewMostrarTareas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMostrarTareas.adapter = adapterTarea
        adapterTarea.submitList(proyectoDTO.tareas.toList())

        // spinner etiquetas
        var listaEtiquetas = listOf(Etiqueta(0, getString(R.string.no_existen_etiquetas), ""))
        model.obtenerEtiquetasRestantes().observe(viewLifecycleOwner) {
            etiquetas ->
            if (etiquetas.isEmpty()) {
                listaEtiquetas = listOf(Etiqueta(0, getString(R.string.no_existen_etiquetas), ""))
            }
            else {
                listaEtiquetas = etiquetas
            }
            binding.listaEtiquetas.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listaEtiquetas.map { it.nombre }
            )


        }
        // Recycler view con las etiquetas del proyecto
        val adapterEtiquetas = ListaEtiquetasPresentesAdapter {
                listaEtiquetas ->
            proyectoDTO.etiquetas = listaEtiquetas
            model.actualizarFiltroListaEtiquetaTareas(proyectoDTO.etiquetas)
        }
        binding.recyclerViewMostrarEtiquetas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMostrarEtiquetas.adapter = adapterEtiquetas
        adapterEtiquetas.submitList(proyectoDTO.etiquetas.toList())

        // botones añadir tarea y etiqueta
        binding.anadirTarea.setOnClickListener {
            val posicion = binding.listaTareas.selectedItemPosition
            if (listaTareas.isNotEmpty() // Lista vacia
                && posicion >= 0 && posicion < listaTareas.size // Protegerse de fuera de limites
                && listaTareas[posicion].id != 0 // Evitar que sea un valor por defecto de no hay tareas
            ) {
                // Obtener nueva tarea, la lista de tareas y añadirla actualizando las disponibles)
                val tareaAnadir = listaTareas[posicion]
                val nuevasTareasDTO = proyectoDTO.tareas.toMutableList()
                nuevasTareasDTO.add(tareaAnadir)
                proyectoDTO.tareas = nuevasTareasDTO
                // Mandamos la lista con las nuevas tareas
                adapterTarea.submitList(proyectoDTO.tareas.toList())
                model.actualizarFiltroListaTareaProyecto(proyectoDTO.tareas)
            }
        }


        binding.anadirEtiqueta.setOnClickListener {
            val posicion = binding.listaEtiquetas.selectedItemPosition
            if (listaEtiquetas.isNotEmpty() // Lista vacia
                && posicion >= 0 && posicion < listaTareas.size // Protegerse de fuera de limites
                && listaTareas[posicion].id != 0 // Evitar que sea un valor por defecto de no hay etiquetas
            ) {
                // Obtener nueva etiqueta, la lista de etiquetas y añadirla actualizando las disponibles)
                val etiquetasAnadir = listaEtiquetas[posicion]
                val nuevasEtiquetas = proyectoDTO.etiquetas.toMutableList()
                nuevasEtiquetas.add(etiquetasAnadir)
                proyectoDTO.etiquetas = nuevasEtiquetas
                // Mandamos la lista con las nuevas tareas
                adapterEtiquetas.submitList(proyectoDTO.etiquetas.toList())
                model.actualizarFiltroListaEtiquetaTareas(proyectoDTO.etiquetas)
            }
        }

        // Calendario
        binding.calendarioInicio.setOnClickListener {
            // Creamos una instancia de MaterialDatePicker
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(getString(R.string.fecha_inicio))
            // Construimos el datePicker
            val picker = builder.build()

            // Customizamos el boton de confirmar
            picker.addOnPositiveButtonClickListener { eleccion ->

                val fechaEscogidaPorUsuario = Date(eleccion)
                proyectoDTO.proyecto.fechaInicio = fechaEscogidaPorUsuario
                binding.fechaInicioProyecto.text = DateHelper.timestampToString(fechaEscogidaPorUsuario)
            }

            // Mostramos el datePicker
            picker.show(parentFragmentManager, "escoger fecha inicio")
        }
        binding.calendarioFin.setOnClickListener {
            // Creamos una instancia de MaterialDatePicker
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(getString(R.string.fecha_fin))
            // Construimos el datePicker
            val picker = builder.build()

            // Customizamos el boton de confirmar
            picker.addOnPositiveButtonClickListener { eleccion ->

                val fechaEscogidaPorUsuario = Date(eleccion)
                proyectoDTO.proyecto.fechaFin = fechaEscogidaPorUsuario
                binding.fechaFinProyecto.text = DateHelper.timestampToString(fechaEscogidaPorUsuario)
            }

            // Mostramos el datePicker
            picker.show(parentFragmentManager, "escoger fecha fin")
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val flechaRetroceso = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 0 solo la pueden tener las de nueva creación
                if (proyectoDTO.proyecto.id == 0) {
                    dialogoGuardado()
                } else {
                    dialogoModificado()
                }
            }
        }

        // Modifica el comportamiento en el activity
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, flechaRetroceso)
    }

    private fun dialogoGuardado() {
        AlertDialog.Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_guardar_proyecto))
            .setMessage("")
            .setPositiveButton(getString(R.string.guardar)) { _,_ ->
                if (binding.tituloProyecto.text.toString().trim().isNotEmpty()) {
                    // Actualizamos los campos de texto con los ultimo
                    proyectoDTO.proyecto.nombre = binding.tituloProyecto.text.toString().trim()
                    proyectoDTO.proyecto.descripcion = binding.descripcionProyecto.text.toString().trim()

                    // Generamos un hilo con la nueva tarea
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.insertarProyectoConTareaYEtiqueta(proyectoDTO)
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_guardar_proyecto),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }

                    // Vovlemos a la vista previa
                    findNavController().popBackStack()
                }
                else {
                    // Mensaje en caso de error controlado
                    Snackbar.make(binding.root, getString(R.string.error_guardar_proyecto),
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.descartar)) { _, _ ->
                findNavController().popBackStack()
            }
            .setNeutralButton(getString(R.string.continuar), null)
            .show()
    }

    private fun dialogoModificado() {
        AlertDialog.Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_modificar_proyecto))
            .setMessage("")
            .setPositiveButton(getString(R.string.guardar)) { _,_ ->
                if (binding.tituloProyecto.text.toString().trim().isNotEmpty()) {
                    // Actualizamos los campos de texto con los ultimo
                    proyectoDTO.proyecto.nombre = binding.tituloProyecto.text.toString().trim()
                    proyectoDTO.proyecto.descripcion = binding.descripcionProyecto.text.toString().trim()

                    // Generamos un hilo con la nueva tarea
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.modificarProyectoConTareaYEtiqueta(proyectoDTO)
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_modificar_proyecto),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }

                    // Vovlemos a la vista previa
                    findNavController().popBackStack()
                }
                else {
                    // Mensaje en caso de error controlado
                    Snackbar.make(binding.root, getString(R.string.error_modificar_proyecto),
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.descartar)) { _, _ ->
                findNavController().popBackStack()
            }
            .setNeutralButton(getString(R.string.continuar), null)
            .show()
    }


}