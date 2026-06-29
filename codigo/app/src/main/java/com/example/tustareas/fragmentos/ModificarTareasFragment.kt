package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.ListaEtiquetasPresentesAdapter
import com.example.tustareas.databinding.FragmentModificarTareasBinding
import com.example.tustareas.modelView.ModificarTareasModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.util.DateHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

/**
 * Clase que gestiona el fragmento de modificación de tareas.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class ModificarTareasFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentModificarTareasBinding? = null
    val binding: FragmentModificarTareasBinding
        get() = _binding!!

    val model: ModificarTareasModel by viewModels()

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentModificarTareasBinding.inflate(inflater, container, false)

        // Recuperamos la tarea pasada por argumentos
        val args = ModificarTareasFragmentArgs.fromBundle(requireArguments())
        model.definirTareaDTO(args.tareaDTO)

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

        // Vigila los errores que surjan al guardar o modificar la tarea
        vigilarError()

        // Vigilar resultado
        vigilarResultado()

        return binding.root
    }

    /**
     * Hace modificaciones en la vista ya creada para gestionar los eventos de los elementos de la vista.
     *
     * @param view La vista del fragmento de modificación de tareas.
     * @param savedInstanceState El estado guardado de la vista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Modifica la logica por defecto de la flecha de retroceso
        val flechaRetroceso =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 0 solo la pueden tener las de nueva creación
                    dialogo()
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
        model.observarTareaDTO().value?.let { tareaDTO ->
            // Valores del dto
            binding.tituloTarea.setText(tareaDTO.tarea.nombre)
            binding.descipcionTarea.setText(tareaDTO.tarea.descripcion)
            binding.fechaCreacionTarea.text =
                DateHelper.timestampToString(tareaDTO.tarea.fechaCreacion)
            binding.fechaLimiteTarea.text = DateHelper.timestampToString(tareaDTO.tarea.fechaLimite)
            binding.estadoTarea.text = getString(tareaDTO.tarea.estado.labelRes())

            // Refrescar tareas
            model.actualizarFiltroListaEtiquetaTareas(tareaDTO.etiquetas)
        }
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
        binding.prioridadTarea.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_personalizado,
                contenidosSpinerPrioridad,
            )
        // Despues del adapter
        binding.prioridadTarea.setSelection(model.prioridadOrdinal())
        binding.prioridadTarea.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    // gestion prioridad
                    model.cambiarPrioridad(position)
                    binding.prioridadTarea.setSelection(position)
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
                model.actualizarFechaLimite(fechaEscogidaPorUsuario)
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
        binding.listaEtiquetas.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_personalizado,
                // Muestra solo el nombre, pero internamente es la clase
                listaEtiquetas.map { it.nombre },
            )
        // Gestionar etiqueta
        model.obtenerEtiquetasRestantes().observe(viewLifecycleOwner) { etiquetas ->
            listaEtiquetas = model.comprobarListaEtiquetas(etiquetas)
            binding.listaEtiquetas.adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    listaEtiquetas.map { it.nombre },
                )
        }

        // Recycler View etiquetas presentes
        adapter =
            ListaEtiquetasPresentesAdapter { listaEtiquetas ->
                model.actualizarEtiquetasTarea(listaEtiquetas)
                model.actualizarFiltroListaEtiquetaTareas(model.obtenerListaEtiquetasTarea())
            }
        binding.recyclerViewMostrarEtiquetas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMostrarEtiquetas.adapter = adapter
        adapter.submitList(model.obtenerListaEtiquetasTarea())
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
            if (listaEtiquetas.isNotEmpty() &&
                // Lista vacia protección
                posicion >= 0 &&
                posicion < listaEtiquetas.size &&
                // Protegerse de fuera de limites
                listaEtiquetas[posicion].id != 0 // Evitar que sea un valor por defecto de no hay etiquetas
            ) {
                // Obtener nueva etiqueta, la lista de etqiuetas y añadirla actualizando las disponibles
                val etiquetaAnadir = listaEtiquetas[posicion]
                val nuevasEtiqeutasDTO = model.obtenerListaEtiquetasTarea().toMutableList()
                nuevasEtiqeutasDTO.add(etiquetaAnadir)
                model.actualizarEtiquetasTarea(nuevasEtiqeutasDTO)
                // Mandamos la lista con las nuevas etiquetas
                adapter.submitList(model.obtenerListaEtiquetasTarea())
                model.actualizarFiltroListaEtiquetaTareas(model.obtenerListaEtiquetasTarea())
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
    private fun dialogo() {
        AlertDialog
            .Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_modificado_tarea))
            .setMessage("")
            .setPositiveButton(getString(R.string.guardar)) { _, _ ->
                model.modificarTarea(
                    binding.tituloTarea.text
                        .toString()
                        .trim(),
                    binding.descipcionTarea.text
                        .toString()
                        .trim(),
                )
            }.setNegativeButton(getString(R.string.descartar)) { _, _ ->
                findNavController().popBackStack()
            }.setNeutralButton(getString(R.string.continuar), null)
            .show()
    }

    /**
     * Función que se encarga de observar el resultado de la operación de guardar o modificar una
     * tarea y mostrar un mensaje de éxito o error al usuario en consecuencia.
     *
     * @author Alberto Noceda <
     */
    private fun vigilarError() {
        model.observarMensajeError().observe(viewLifecycleOwner) { mensaje ->
            Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Función que se encarga de observar el resultado de la operación de guardar o modificar una
     * tarea y navegar hacia atrás en la pila de fragmentos si la operación se ha realizado correctamente.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun vigilarResultado() {
        model.observarResultado().observe(viewLifecycleOwner) { resultado ->
            if (resultado) {
                findNavController().popBackStack()
            }
        }
    }
}
