package com.example.tustareas.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.ListaEtiquetasPresentesAdapter
import com.example.tustareas.databinding.FragmentModificarTareasBinding
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.util.DateHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Clase que gestiona el fragmento de modificación de tareas.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ModificarTareasFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentModificarTareasBinding? = null
    private val binding: FragmentModificarTareasBinding
        get() = _binding!!

    val model: TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )

    private lateinit var tareaDTO : TareaDTO

    // Variables para la gestión de etiquetas
    private lateinit var listaEtiquetas: List<Etiqueta>
    private lateinit var adapter: ListaEtiquetasPresentesAdapter

    /**
     * Crea la vista del fragmento de modificación de tareas y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento de modificación de tareas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentModificarTareasBinding.inflate(inflater, container, false)

        // Recuperamos la tarea pasada por argumentos
        val args = ModificarTareasFragmentArgs.fromBundle(requireArguments())
        tareaDTO = args.tareaDTO

        // Rellenamos los campos con los datos de la tarea pasada
        rellenarCampos()

        // Gestiona el spinner de prioridad
        gestionarSpinnerPrioridad()

        // Gestiona el calendario de fecha limite
        gestionarCalendarioFechaLimite()

        // Gestiona la lista de etiquetas disponibles y presentes
        gestionarMostrarEtiquetas()

        // Gestiona el boton de añadir etiqueta
        gestionarAnadirEtiqueta()


        return binding.root
    }

    /**
     * Hace modificaciones en la vista ya creada para gestionar los eventos de los elementos de la vista.
     *
     * @param view La vista del fragmento de modificación de tareas.
     * @param savedInstanceState El estado guardado de la vista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Modifica la logica por defecto de la flecha de retroceso
        val flechaRetroceso = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 0 solo la pueden tener las de nueva creación
                if (tareaDTO.tarea.id == 0) {
                    dialogoGuardado()
                }
                else {
                    dialogoModificado()
                }
            }
        }

        // Modifica el comportamiento en el activity
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, flechaRetroceso)
    }

    /**
     * Función que se encarga de rellenar los campos del formulario con los datos de la tarea pasada por argumentos.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun rellenarCampos() {
        // Valores del dto
        binding.tituloTarea.setText(tareaDTO.tarea.nombre)
        binding.descipcionTarea.setText(tareaDTO.tarea.descripcion)
        binding.fechaCreacionTarea.text = DateHelper.timestampToString(tareaDTO.tarea.fechaCreacion)
        binding.fechaLimiteTarea.text = DateHelper.timestampToString(tareaDTO.tarea.fechaLimite)
        binding.estadoTarea.text = tareaDTO.tarea.estado.name

        // Refrescar tareas
        model.modificarTareas.actualizarFiltroListaEtiquetaTareas(tareaDTO.etiquetas)
    }

    /**
     * Función que se encarga de gestionar el spinner de prioridad para mostrar las opciones de prioridad disponibles y actualizar la prioridad de la tarea según la selección del usuario.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarSpinnerPrioridad() {
        // Gestión spinner de prioridad
        // Spinner prioridad
        val contenidosSpinerPrioridad = Prioridad.entries.map { getString(it.labelRes()) }
        binding.prioridadTarea.adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_personalizado,
            contenidosSpinerPrioridad
        )
        //Despues del adapter
        binding.prioridadTarea.setSelection(tareaDTO.tarea.prioridad.ordinal)
        binding.prioridadTarea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // getion de prioridad
                when (position) {
                    0-> {
                        binding.prioridadTarea.setSelection(0)
                        tareaDTO.tarea.prioridad = Prioridad.ALTA
                    }
                    1-> {
                        binding.prioridadTarea.setSelection(1)
                        tareaDTO.tarea.prioridad = Prioridad.MEDIA
                    }
                    2-> {
                        binding.prioridadTarea.setSelection(2)
                        tareaDTO.tarea.prioridad = Prioridad.BAJA
                    }
                    3-> {
                        binding.prioridadTarea.setSelection(3)
                        tareaDTO.tarea.prioridad = Prioridad.NO_ESTABLECIDO
                    }
                    else -> {
                        binding.prioridadTarea.setSelection(0)
                        tareaDTO.tarea.prioridad = Prioridad.ALTA
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    /**
     * Función que se encarga de gestionar el calendario de fecha límite para mostrar un selector de fecha al usuario y actualizar la fecha límite de la tarea según la selección del usuario.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarCalendarioFechaLimite() {
        // calendario de fecha limite. No es como en el diseño de Figma
        binding.calendario.setOnClickListener {
            // Creamos una instancia de MaterialDatePicker
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(getString(R.string.fecha_limite))
            // Construimos el datePicker
            val picker = builder.build()

            // Customizamos el boton de confirmar
            picker.addOnPositiveButtonClickListener { eleccion ->

                val fechaEscogidaPorUsuario = Date(eleccion)
                tareaDTO.tarea.fechaLimite = fechaEscogidaPorUsuario
                binding.fechaLimiteTarea.text =
                    DateHelper.timestampToString(fechaEscogidaPorUsuario)
            }

            // Mostramos el datePicker
            picker.show(parentFragmentManager, "escoger fecha")
        }
    }

    /**
     * Lista de etiquetas disponibles para añadir a la tarea. Se obtiene del modelo y se actualiza cada vez que se modifica la lista de etiquetas de la tarea.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarMostrarEtiquetas() {
        // valor por defecto vacio
        listaEtiquetas = listOf(Etiqueta(0, getString(R.string.no_existen_etiquetas)))
        binding.listaEtiquetas.adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_personalizado,
            // Muestra solo el nombre, pero internamente es la clase
            listaEtiquetas.map { it.nombre}
        )
        // Gestionar etiqueta
        model.modificarTareas.obtenerEtiquetasRestantes().observe(viewLifecycleOwner) { etiquetas ->
            if (etiquetas.isEmpty()) {
                listaEtiquetas = listOf(Etiqueta(0, getString(R.string.no_existen_etiquetas)))
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

        // Recycler View etiquetas presentes
        adapter = ListaEtiquetasPresentesAdapter {
            listaEtiquetas ->
            tareaDTO.etiquetas = listaEtiquetas
            model.modificarTareas.actualizarFiltroListaEtiquetaTareas(tareaDTO.etiquetas)
        }
        binding.recyclerViewMostrarEtiquetas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMostrarEtiquetas.adapter = adapter
        adapter.submitList(tareaDTO.etiquetas.toList())
    }

    /**
     * Función que se encarga de gestionar el botón de añadir etiqueta para añadir una etiqueta seleccionada de la lista de etiquetas disponibles a la lista de etiquetas de la tarea y actualizar la vista en consecuencia.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarAnadirEtiqueta() {
        // Boton añadir etiqueta
        binding.anadirEtiqueta.setOnClickListener {
            val posicion = binding.listaEtiquetas.selectedItemPosition
            if (listaEtiquetas.isNotEmpty() // Lista vacia protección
                && posicion >= 0 && posicion < listaEtiquetas.size // Protegerse de fuera de limites
                && listaEtiquetas[posicion].id != 0 // Evitar que sea un valor por defecto de no hay etiquetas
            ) {
                // Obtener nueva etiqueta, la lista de etqiuetas y añadirla actualizando las disponibles
                val etiquetaAnadir = listaEtiquetas[posicion]
                val nuevasEtiqeutasDTO = tareaDTO.etiquetas.toMutableList()
                nuevasEtiqeutasDTO.add(etiquetaAnadir)
                tareaDTO.etiquetas = nuevasEtiqeutasDTO
                // Mandamos la lista con las nuevas etiquetas
                adapter.submitList(tareaDTO.etiquetas.toList())
                model.modificarTareas.actualizarFiltroListaEtiquetaTareas(tareaDTO.etiquetas)
            }
        }
    }

    /**
     * Destruye la vista del fragmento de modificación de tareas y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Muestra un diálogo que se encarga del guardado de una tarea nueva.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun dialogoGuardado() {
        AlertDialog.Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_guardar_tarea))
            .setMessage("")
            .setPositiveButton(getString(R.string.guardar)) { _,_ ->
                if (binding.tituloTarea.text.toString().trim().isNotEmpty()) {
                    // Actualizamos los campos de texto con los ultimo
                    tareaDTO.tarea.nombre = binding.tituloTarea.text.toString().trim()
                    tareaDTO.tarea.descripcion = binding.descipcionTarea.text.toString().trim()
                    if (tareaDTO.tarea.estado != Estado.COMPLETADA) {
                        if (tareaDTO.tarea.fechaLimite == null || tareaDTO.tarea.fechaLimite!!.after(DateHelper.fechaMediaNocheUTC())) {
                            tareaDTO.tarea.estado = Estado.EN_TIEMPO
                        }
                        else {
                            tareaDTO.tarea.estado = Estado.RETRASADA
                        }
                    }

                    // Generamos un hilo con la nueva tarea
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.modificarTareas.insertarTareaConEtiqueta(tareaDTO)
                            // Vovlemos a la vista previa
                            findNavController().popBackStack()
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_guardar_tarea),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }


                }
                else {
                    // Mensaje en caso de error controlado
                    Snackbar.make(binding.root, getString(R.string.error_guardar_tarea),
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.descartar)) { _, _ ->
                findNavController().popBackStack()
            }
            .setNeutralButton(getString(R.string.continuar), null)
            .show()
    }

    /**
     * Muestra un diálogo que se encarga de la modificación de una tarea existente.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun dialogoModificado() {
        AlertDialog.Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_modificado_tarea))
            .setMessage("")
            .setPositiveButton(getString(R.string.guardar)) { _, _ ->
                if (binding.tituloTarea.text.toString().trim().isNotEmpty()) {
                    // Actualizamos los campos de texto con los ultimo
                    tareaDTO.tarea.nombre = binding.tituloTarea.text.toString().trim()
                    tareaDTO.tarea.descripcion = binding.descipcionTarea.text.toString().trim()
                    if (tareaDTO.tarea.estado != Estado.COMPLETADA) {
                        if (tareaDTO.tarea.fechaLimite == null || tareaDTO.tarea.fechaLimite!!.after(DateHelper.fechaMediaNocheUTC())) {
                            tareaDTO.tarea.estado = Estado.EN_TIEMPO
                        }
                        else {
                            tareaDTO.tarea.estado = Estado.RETRASADA
                        }
                    }

                    // Generamos un hilo con la nueva tarea
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.modificarTareas.modificarTareaConEtiqueta(tareaDTO)
                            // Volvemos a la vista previa
                            findNavController().popBackStack()
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_modificar_tarea),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_modificar_tarea),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton(getString(R.string.descartar)) { _, _ ->
                findNavController().popBackStack()
            }
            .setNeutralButton(getString(R.string.continuar), null)
            .show()
    }

}
