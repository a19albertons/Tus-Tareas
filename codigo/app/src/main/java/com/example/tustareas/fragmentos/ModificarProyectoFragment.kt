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

/**
 * Clase que gestiona el fragmento de modificación de proyectos.
 */
class ModificarProyectoFragment : Fragment() {
    // Variables generales de la clase
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

        // Carga todos los datos recibidos en el fragmento
        binding.tituloProyecto.setText(proyectoDTO.proyecto.nombre)
        binding.descripcionProyecto.setText(proyectoDTO.proyecto.descripcion)
        binding.fechaCreacionTarea.text = DateHelper.timestampToString(proyectoDTO.proyecto.fechaCreacion)
        binding.fechaInicioProyecto.text = DateHelper.timestampToString(proyectoDTO.proyecto.fechaInicio)
        binding.fechaFinProyecto.text = DateHelper.timestampToString(proyectoDTO.proyecto.fechaFin)

        // Refrescar tareas y etiquetas
        model.modificarProyectos.actualizarFiltroListaEtiquetaProyecto(proyectoDTO.etiquetas)
        model.modificarProyectos.actualizarFiltroListaTareaProyecto(proyectoDTO.tareas)

        // Gestiona la addición de tareas
        // spinner tareas
        var listaTareas = listOf(Tarea(0, getString(R.string.no_existen_tareas), null, null, Prioridad.NoEstablecido,
            Date(), Estado.EnTiempo, null))
        model.modificarProyectos.obtenerTareasRestantes().observe(viewLifecycleOwner) {
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
                R.layout.spinner_personalizado,
                listaTareas.map { it.nombre }
            )
        }

        // Gestiona las tareas del proyecto en la opción de eliminar
        // Recycler view para las tareas
        val adapterTarea = ListaTareasPresentesAdapter {
                listaTareas ->
            proyectoDTO.tareas = listaTareas
            model.modificarProyectos.actualizarFiltroListaTareaProyecto(proyectoDTO.tareas)
        }
        binding.recyclerViewMostrarTareas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMostrarTareas.adapter = adapterTarea
        adapterTarea.submitList(proyectoDTO.tareas.toList())

        // Gestiona la addición de etiquetas
        // spinner etiquetas
        var listaEtiquetas = listOf(Etiqueta(0, getString(R.string.no_existen_etiquetas), ""))
        model.modificarProyectos.obtenerEtiquetasRestantes().observe(viewLifecycleOwner) {
            etiquetas ->
            if (etiquetas.isEmpty()) {
                listaEtiquetas = listOf(Etiqueta(0, getString(R.string.no_existen_etiquetas), ""))
            }
            else {
                listaEtiquetas = etiquetas
            }
            binding.listaEtiquetas.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_personalizado,
                listaEtiquetas.map { it.nombre }
            )


        }

        // Gestiona las etiquetas del proyecto en la opción de eliminar
        // Recycler view con las etiquetas del proyecto
        val adapterEtiquetas = ListaEtiquetasPresentesAdapter {
                listaEtiquetas ->
            proyectoDTO.etiquetas = listaEtiquetas
            model.modificarProyectos.actualizarFiltroListaEtiquetaProyecto(proyectoDTO.etiquetas)
        }
        binding.recyclerViewMostrarEtiquetas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMostrarEtiquetas.adapter = adapterEtiquetas
        adapterEtiquetas.submitList(proyectoDTO.etiquetas.toList())

        // boton añadir tarea
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
                model.modificarProyectos.actualizarFiltroListaTareaProyecto(proyectoDTO.tareas)
            }
        }

        // boton añadir etiqueta
        binding.anadirEtiqueta.setOnClickListener {
            val posicion = binding.listaEtiquetas.selectedItemPosition
            if (listaEtiquetas.isNotEmpty() // Lista vacia
                && posicion >= 0 && posicion < listaEtiquetas.size // Protegerse de fuera de limites
                && listaEtiquetas[posicion].id != 0 // Evitar que sea un valor por defecto de no hay etiquetas
            ) {
                // Obtener nueva etiqueta, la lista de etiquetas y añadirla actualizando las disponibles)
                val etiquetasAnadir = listaEtiquetas[posicion]
                val nuevasEtiquetas = proyectoDTO.etiquetas.toMutableList()
                nuevasEtiquetas.add(etiquetasAnadir)
                proyectoDTO.etiquetas = nuevasEtiquetas
                // Mandamos la lista con las nuevas tareas
                adapterEtiquetas.submitList(proyectoDTO.etiquetas.toList())
                model.modificarProyectos.actualizarFiltroListaEtiquetaProyecto(proyectoDTO.etiquetas)
            }
        }

        // Despliega el calendario
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

        // Modifica la logica por defecto de la flecha de retroceso
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

    // Dialogo de guardado
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
                            model.modificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO)
                            // Vovlemos a la vista previa
                            findNavController().popBackStack()
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_guardar_proyecto),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }


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

    // dialogo de modifiado
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
                            model.modificarProyectos.modificarProyectoConTareaYEtiqueta(proyectoDTO)
                            // Vovlemos a la vista previa
                            findNavController().popBackStack()
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_modificar_proyecto),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }

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