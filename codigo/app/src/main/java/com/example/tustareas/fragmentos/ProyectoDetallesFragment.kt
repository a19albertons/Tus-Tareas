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
import com.example.tustareas.databinding.FragmentProyectoDetallesBinding
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelView.ProyectoDetallesModel
import com.example.tustareas.util.DateHelper
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Clase que gestiona el fragmento de detalles de proyectos.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class ProyectoDetallesFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentProyectoDetallesBinding? = null
    private val binding: FragmentProyectoDetallesBinding
        get() = _binding!!

    val model: ProyectoDetallesModel by viewModels()

    private lateinit var args: ProyectoDetallesFragmentArgs
    private lateinit var proyectoVisualizado: ProyectoDTO

    /**
     * Crea la vista del fragmento detalles de un proyecto y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento detalles de un proyecto.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProyectoDetallesBinding.inflate(inflater, container, false)

        // Obtener argumentos e id del pryecto
        args = ProyectoDetallesFragmentArgs.fromBundle(requireArguments())

        // Carga el proyecto a visualizar
        cargarProyecto()

        // Gestiona el menu de opciones
        configurarMenu()

        return binding.root
    }

    /**
     * Función privada que carga el proyecto a visualizar en el fragmento detalles de un proyecto. Obtiene el proyecto por su id de los args y actualiza la vista con los datos del proyecto, las tareas asociadas al proyecto y las etiquetas asociadas al proyecto.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun cargarProyecto() {
        // Obtener el proyecto
        model.obtenerProyectoPorId(args.id).observe(viewLifecycleOwner) { proyecto ->

            // Fallo descubierto en base al principio que pasa si hago esto con su contramedida respectiva
            if (proyecto == null) {
                Snackbar.make(binding.root, getString(R.string.prevencion_fallo_critico), Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack(findNavController().graph.startDestinationId, false)
                return@observe
            }

            binding.tituloProyecto.text = proyecto.proyecto.nombre
            binding.descipcionProyecto.text = proyecto.proyecto.descripcion
            binding.fechaCreacionProyecto.text = DateHelper.timestampToString(proyecto.proyecto.fechaCreacion)
            binding.fechaInicioProyecto.text = DateHelper.timestampToString(proyecto.proyecto.fechaInicio)
            binding.fechaFinProyecto.text = DateHelper.timestampToString(proyecto.proyecto.fechaFin)

            // Gestiona añadido tareas
            anadirTareas(proyecto)

            // Gestiona añadido etiquetas
            anadirEtiquetas(proyecto)

            // damos valor a proyecto
            proyectoVisualizado = proyecto
        }
    }

    /**
     * Función de apoyo usada por [cargarProyecto] que se encarga de añadir las tareas asociadas a un proyecto a la vista del fragmento detalles de un proyecto. Crea un chip por cada tarea y lo añade al grupo de chips de tareas.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun anadirTareas(proyecto: ProyectoDTO) {
        // Añadir tareas
        proyecto.tareas.forEach { tarea ->
            val chip =
                Chip(requireContext()).apply {
                    text = tarea.nombre
                    setChipBackgroundColorResource(R.color.gray)
                    setTextColor(resources.getColor(R.color.black, null))

                    // Deshabilitamos la interaccion con las chips
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
                    shapeAppearanceModel =
                        shapeAppearanceModel
                            .toBuilder()
                            .setAllCornerSizes(0f)
                            .build()
                }
            binding.tareaProyectoGroup.addView(chip)
        }
    }

    /**
     * Función de apoyo usada por [cargarProyecto] que se encarga de añadir las etiquetas asociadas a un proyecto a la vista del fragmento detalles de un proyecto. Crea un chip por cada etiqueta y lo añade al grupo de chips de etiquetas.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun anadirEtiquetas(proyecto: ProyectoDTO) {
        // Añadir etiquetas
        proyecto.etiquetas.forEach { etiqueta ->
            val chip =
                Chip(requireContext()).apply {
                    text = etiqueta.nombre
                    setChipBackgroundColorResource(R.color.gray)
                    setTextColor(resources.getColor(R.color.black, null))
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

                    shapeAppearanceModel =
                        shapeAppearanceModel
                            .toBuilder()
                            .setAllCornerSizes(0f)
                            .build()
                }
            binding.etiquetasProyectoGroup.addView(chip)
        }
    }

    /**
     * Función privada que configura el menu toolbar personalizado para el fragmento detalles de un proyecto. Reemplaza el menu del activity por un menu personalizado para el fragmento detalles y gestiona la navegación a la vista de edición y la eliminación del proyecto.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun configurarMenu() {
        // Menu toolbar especifico
        // Invocar el menu del activity
        val activityMenu: MenuHost = requireActivity()

        // Crear un modificador del menu (toolbar)
        activityMenu.addMenuProvider(
            object : MenuProvider {
                // Reemplaza el menu
                override fun onCreateMenu(
                    menuViejo: Menu,
                    inflaMenuNuevo: MenuInflater,
                ) {
                    menuViejo.clear()
                    inflaMenuNuevo.inflate(R.menu.toolbar_proyectos_detalles, menuViejo)
                }

                override fun onMenuItemSelected(item: MenuItem): Boolean =
                    when (item.itemId) {
                        R.id.action_editar_proyecto -> {
                            if (::proyectoVisualizado.isInitialized) {
                                try {
                                    findNavController().navigate(
                                        ProyectoDetallesFragmentDirections.actionProyectoDetallesFragmentToModificarProyectoFragment(
                                            proyectoVisualizado,
                                        ),
                                    )
                                } catch (_: Exception) {
                                    Snackbar
                                        .make(
                                            binding.root,
                                            getString(R.string.error_navegar),
                                            Snackbar.LENGTH_SHORT,
                                        ).show()
                                }
                            }
                            true
                        }
                        R.id.action_eliminar_proyecto -> {
                            if (::proyectoVisualizado.isInitialized) {
                                dialogoBorrado()
                            }
                            true
                        }
                        else -> false
                    }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED,
        )
    }

    /**
     * Carga un dialogo de confirmación para eliminar un proyecto
     * Si se confirma la eliminación, se lanza una petición para eliminar el proyecto en la base de datos
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun dialogoBorrado() {
        AlertDialog
            .Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_eliminar_proyecto))
            .setMessage(getString(R.string.aviso_confirmar_eliminar_proyecto))
            .setPositiveButton(getString(R.string.eliminar)) { _, _ ->
                if (binding.tituloProyecto.text
                        .toString()
                        .trim()
                        .isNotEmpty()
                ) {
                    // Actualizamos los campos de texto con los ultimo
                    proyectoVisualizado.proyecto.nombre =
                        binding.tituloProyecto.text
                            .toString()
                            .trim()
                    proyectoVisualizado.proyecto.descripcion =
                        binding.descipcionProyecto.text
                            .toString()
                            .trim()

                    // Generamos un hilo con la nueva tarea
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.eliminarProyectoConTareaYEtiqueta(proyectoVisualizado)
                            // Vovlemos a la vista previa
                            findNavController().popBackStack()
                        } catch (_: Exception) {
                            Snackbar
                                .make(
                                    binding.root,
                                    getString(R.string.error_eliminar_proyecto),
                                    Snackbar.LENGTH_SHORT,
                                ).show()
                        }
                    }
                } else {
                    // Mensaje en caso de error controlado
                    Snackbar
                        .make(
                            binding.root,
                            getString(R.string.error_eliminar_proyecto),
                            Snackbar.LENGTH_SHORT,
                        ).show()
                }
            }.setNeutralButton(getString(R.string.cancelar), null)
            .show()
    }

    /**
     * Destruye la vista del fragmento detalles de un proyecto y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
