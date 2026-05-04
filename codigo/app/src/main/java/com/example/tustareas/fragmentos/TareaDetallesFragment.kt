package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentTareaDetallesBinding
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.util.DateHelper
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * Clase que gestiona el fragmento de detalles de tareas.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class TareaDetallesFragment : Fragment() {
    // Variables generales de la clase
    private var _binding : FragmentTareaDetallesBinding? = null
    private val binding : FragmentTareaDetallesBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )

    private lateinit var args : TareaDetallesFragmentArgs
    private lateinit var tareaVisualizada : TareaDTO


    /**
     * Crea la vista del fragmento detalles de una tarea y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento detalles de una tarea.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTareaDetallesBinding.inflate(inflater, container, false)

        // Obtiene el id de la tarea a mostrar
        args = TareaDetallesFragmentArgs.fromBundle(requireArguments())

        // Carga la tarea a mostrar
        cargarTarea()

        // Configura un menu toolbar personalizado para el fragmento
        configurarMenu()

        return binding.root
    }

    /**
     * Función que carga los detalles de una tarea a mostrar en el fragmento detalles de una tarea.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun cargarTarea() {
        // Obtiene los detalles por el id y los asigna al componente visual
        model.tareaDetalles.obtenerTareaDTOPorID(args.id).observe(viewLifecycleOwner) {
            tarea ->

            // Fallo descubierto en base al principio que pasa si hago esto con su contramedida respectiva
            if (tarea == null) {
                Snackbar.make(binding.root, getString(R.string.prevencion_fallo_critico), Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack(findNavController().graph.startDestinationId, false)
                return@observe
            }

            // Define los datos de la tarea en los componentes visuales
            binding.tituloTarea.text = tarea.tarea.nombre
            binding.descipcionTarea.text = tarea.tarea.descripcion
            binding.fechaCreacionTarea.text = DateHelper.timestampToString(tarea.tarea.fechaCreacion)
            if (tarea.tarea.fechaLimite != null) {
                binding.fechaLimiteTarea.text = DateHelper.timestampToString(tarea.tarea.fechaLimite!!)
            } else {
                binding.fechaLimiteTarea.text = ""
            }
            binding.prioridadTarea.text = getString(tarea.tarea.prioridad.labelRes())
            binding.estadoTarea.text = getString(tarea.tarea.estado.labelRes())

            // Añade las etiquetas de la tarea al grupo de chips
            anadirEtiquetas(tarea)

            tareaVisualizada = tarea
        }
    }

    /**
     * Función de apoyo de [cargarTarea] que se encarga de añadir las etiquetas de una tarea al grupo de chips de la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun anadirEtiquetas(tarea: TareaDTO) {
        // Crea un chip por cada etiqueta de la tarea y los añade al grupo de chips
        binding.etiquetasTareaGroup.removeAllViews()
        tarea.etiquetas.forEach {
                etiqueta ->
            // Define la composición del chip
            val chip = Chip(requireContext()).apply {
                // Define el texto del chip como el nombre de la etiqueta
                text = etiqueta.nombre
                setChipBackgroundColorResource(R.color.gray)
                setTextColor(resources.getColor(R.color.black, null))

                // Deshabilitamos la interacción del chip
                isClickable = false
                isFocusable = false
                isCheckable = false
                chipStrokeWidth = 0f

                // Deshabilitamos los minimos de toque de material 3d
                setEnsureMinTouchTargetSize(false)
                // Modificamos los minimos
                chipMinHeight = 0f
                minHeight = 0

                // Definimos 2dp
                val paddingPx = (2 * resources.displayMetrics.density).toInt()

                // Configuramos el padding
                setPadding(paddingPx, paddingPx, paddingPx, paddingPx)

                // Otros paddings
                textStartPadding = 0f
                textEndPadding = 0f

                // Controla el borde
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(0f)
                    .build()

            }
            binding.etiquetasTareaGroup.addView(chip)
        }
    }

    /**
     * Configura un menú personalizado para el fragmento detalles de una tarea, con opciones para modificar o eliminar la tarea.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun configurarMenu() {
        // Invocar el menu del activity
        val activityMenu : MenuHost = requireActivity()
        // Crear un modificador del menu (toolbar)
        activityMenu.addMenuProvider( object : MenuProvider {
            // Reemplaza el menu
            override fun onCreateMenu(menuViejo: Menu, inflaMenuNuevo: MenuInflater) {
                menuViejo.clear()
                inflaMenuNuevo.inflate(R.menu.toolbar_tareas_detalles, menuViejo)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_editar_tarea -> {
                        if (::tareaVisualizada.isInitialized) {
                            try {
                                findNavController().navigate(TareaDetallesFragmentDirections.actionTareaDetallesFragmentToModificarTareasFragment(tareaVisualizada))
                            }
                            catch (_: Exception) {
                                Snackbar.make(
                                    binding.root,
                                    getString(R.string.error_navegar),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                        true

                    }
                    R.id.action_eliminar_tarea -> {
                        if (::tareaVisualizada.isInitialized) {
                            dialogoEliminacion()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /**
     * Destruye la vista del fragmento de detalles de una tarea y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Carga un dialogo de confirmación para eliminar una tarea
     * Si se confirma la eliminación, se lanza una petición para eliminar la tarea en la base de datos
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun dialogoEliminacion() {
        AlertDialog.Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_eliminar_tarea))
            .setMessage("")
            .setPositiveButton(getString(R.string.eliminar)) { _, _ ->
                if (::tareaVisualizada.isInitialized) {
                    // Borra la tarea
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.tareaDetalles.eliminarTarea(tareaVisualizada.tarea)
                            // Volvemos atras
                            findNavController().popBackStack()
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_eliminar_tarea),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }

                }
                else {
                    Snackbar.make(binding.root, getString(R.string.error_eliminar_tarea),
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNeutralButton(getString(R.string.cancelar), null)
            .show()

    }

}