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
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
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

    // Variables comunes tareas
    private lateinit var listaTareas : List<Tarea>
    private lateinit var adapterTarea : ListaTareasPresentesAdapter

    //Variables comunes etiquetas
    private lateinit var listaEtiquetas : List<Etiqueta>
    private lateinit var adapterEtiquetas : ListaEtiquetasPresentesAdapter



    /**
     * Crea la vista del fragmento de modificación de proyectos y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento de modificación de proyectos.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentModificarProyectoBinding.inflate(inflater, container, false)

        // Recuperamos el proyecto pasado por argumentos
        val args = ModificarProyectoFragmentArgs.fromBundle(requireArguments())
        proyectoDTO = args.proyectoDTO

        // Gestionar el llenado de los campos
        rellenarCampos()

        // Gestiona la lógica de añadir nuevas tareas a un proyecto
        gestionarMostradoTareas()

        // Gestiona la lógica de eliminar tareas del proyecto
        gestionarEliminacionTareas()

        // Gestiona la lógica de añadir nuevas etiquetas a un proyecto
         gestionarMostradoEtiquetas()

        // Gestiona la lógica de eliminar etiquetas del proyecto
        gestionarEliminacionEtiquetas()

        // Gestiona el boton de añadir tarea
        gestionarAnadirTarea()

        // Gestiona el boton de añadir etiqueta
        gestionarAnadirEtiqueta()

        // Gestiona los calendarios de inicio y fin del proyecto
        gestionarCalendarios()


        return binding.root
    }

    /**
     * Hace modificacines en la vista ya creada para gestionar los eventos de los elementos de la vista.
     *
     * @param view La vista del fragmento de modificación de proyectos.
     * @param savedInstanceState El estado guardado de la vista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gestiona la lógica de la flecha de retroceso
        gestionarFlechaRetroceso()
    }

    /**
     * Función que se encarga de rellenar los campos del formulario con los datos del proyecto pasado por argumentos.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun rellenarCampos() {
        // Carga todos los datos recibidos en el fragmento
        binding.tituloProyecto.setText(proyectoDTO.proyecto.nombre)
        binding.descripcionProyecto.setText(proyectoDTO.proyecto.descripcion)
        binding.fechaCreacionTarea.text = DateHelper.timestampToString(proyectoDTO.proyecto.fechaCreacion)
        binding.fechaInicioProyecto.text = DateHelper.timestampToString(proyectoDTO.proyecto.fechaInicio)
        binding.fechaFinProyecto.text = DateHelper.timestampToString(proyectoDTO.proyecto.fechaFin)

        // Refrescar tareas y etiquetas
        model.modificarProyectos.actualizarFiltroListaEtiquetaProyecto(proyectoDTO.etiquetas)
        model.modificarProyectos.actualizarFiltroListaTareaProyecto(proyectoDTO.tareas)
    }

    /**
     * Función que se encarga de gestionar la lógica de añadir nuevas tareas a un proyecto.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net
     */
    private fun gestionarMostradoTareas() {
        // Gestiona la addición de tareas
        // spinner tareas
        listaTareas = listOf(Tarea(0, getString(R.string.no_existen_tareas), null, null, Prioridad.NO_ESTABLECIDO,
            DateHelper.fechaMediaNocheUTC(), Estado.EN_TIEMPO, null))
        model.modificarProyectos.obtenerTareasRestantes(proyectoDTO.proyecto.id).observe(viewLifecycleOwner) {
                tareas ->
            if (tareas.isEmpty()) {
                listaTareas = listOf(Tarea(0, getString(R.string.no_existen_tareas), null, null, Prioridad.NO_ESTABLECIDO,
                    DateHelper.fechaMediaNocheUTC(), Estado.EN_TIEMPO, null))
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
    }

    /**
     * Función que se encarga de gestionar la lógica de eliminar tareas de un proyecto.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarEliminacionTareas() {
        // Gestiona las tareas del proyecto en la opción de eliminar
        // Recycler view para las tareas
        adapterTarea = ListaTareasPresentesAdapter {
                listaTareas ->
            proyectoDTO.tareas = listaTareas
            model.modificarProyectos.actualizarFiltroListaTareaProyecto(proyectoDTO.tareas)
        }
        binding.recyclerViewMostrarTareas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMostrarTareas.adapter = adapterTarea
        adapterTarea.submitList(proyectoDTO.tareas.toList())
    }

    /**
     * Función que se encarga de gestionar la lógica de añadir nuevas etiquetas a un proyecto.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarMostradoEtiquetas() {
        // Gestiona la addición de etiquetas
        // spinner etiquetas
        listaEtiquetas = listOf(Etiqueta(0, getString(R.string.no_existen_etiquetas), ""))
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
    }

    /**
     * Función que se encarga de gestionar la lógica de eliminar etiquetas de un proyecto.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarEliminacionEtiquetas() {
        // Gestiona las etiquetas del proyecto en la opción de eliminar
        // Recycler view con las etiquetas del proyecto
        adapterEtiquetas = ListaEtiquetasPresentesAdapter {
                listaEtiquetas ->
            proyectoDTO.etiquetas = listaEtiquetas
            model.modificarProyectos.actualizarFiltroListaEtiquetaProyecto(proyectoDTO.etiquetas)
        }
        binding.recyclerViewMostrarEtiquetas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMostrarEtiquetas.adapter = adapterEtiquetas
        adapterEtiquetas.submitList(proyectoDTO.etiquetas.toList())
    }

    /**
     * Función que se encarga de gestionar la lógica de añadir una tarea a un proyecto al pulsar el botón de añadir tarea.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarAnadirTarea() {
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
    }

    /**
     * Función que se encarga de gestionar la lógica de añadir una etiqueta a un proyecto al pulsar el botón de añadir etiqueta.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarAnadirEtiqueta() {
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
    }

    /**
     * Función que se encarga de gestionar la lógica de los calendarios de inicio y fin del proyecto al pulsar los botones correspondientes.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarCalendarios() {
        // Despliega el calendario
        // Calendario
        // Fecha inicio
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

        // Fecha fin
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
    }

    /**
     * Función que se encarga de gestionar la lógica de la flecha de retroceso para mostrar un diálogo de confirmación al usuario antes de salir del fragmento.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarFlechaRetroceso() {
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

    /**
     * Muestra un diálogo que se encarga del guardado de un proyecto nuevo.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
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

    /**
     * Muestra un diálogo que se encarga de la confirmación de las modificaciones realizadas a un proyecto existente.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
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

    /**
     * Destruye la vista del fragmento de modificación de proyectos y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}