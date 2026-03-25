package com.example.tustareas.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentProyectoDetallesBinding
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class ProyectoDetallesFragment : Fragment() {
    private var _binding : FragmentProyectoDetallesBinding ?= null
    private val binding : FragmentProyectoDetallesBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )

    private lateinit var proyectoVisualizado : ProyectoDTO


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProyectoDetallesBinding.inflate(inflater, container, false)
        val view = binding.root

        // Obtener argumentos e id del pryecto
        val args = ProyectoDetallesFragmentArgs.fromBundle(requireArguments())
        val id = args.id


        // Obtener el proyecto
        model.obtenerProyectoPorId(id).observe(viewLifecycleOwner) {
            proyecto ->
            binding.tituloProyecto.text = proyecto.proyecto.nombre
            binding.descipcionProyecto.text = proyecto.proyecto.descripcion
            binding.fechaCreacionProyecto.text = DateHelper.timestampToString(proyecto.proyecto.fechaInicio)
            binding.fechaInicioProyecto.text = DateHelper.timestampToString(proyecto.proyecto.fechaInicio)
            binding.fechaFinProyecto.text = DateHelper.timestampToString(proyecto.proyecto.fechaFin)

            // Añadir tareas
            proyecto.tareas.forEach {
                tarea->
                val chip = Chip(requireContext()).apply {
                    text = tarea.nombre
                    setChipBackgroundColorResource(R.color.gray)
                    setTextColor(resources.getColor(R.color.black))
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

                    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                        .setAllCornerSizes(0f)
                        .build()

                }
                binding.tareaProyectoGroup.addView(chip)
            }

            // Añadir etiquetas
            proyecto.etiquetas.forEach { etiqueta ->
                val chip = Chip(requireContext()).apply {
                    text = etiqueta.nombre
                    setChipBackgroundColorResource(R.color.gray)
                    setTextColor(resources.getColor(R.color.black))
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

                    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                        .setAllCornerSizes(0f)
                        .build()

                }
                binding.etiquetasProyectoGroup.addView(chip)
            }

            // damos valor a proyecto
            proyectoVisualizado = proyecto
        }

        // Menu toolbar especifico
        // Invocar el menu del activity
        val activityMenu : MenuHost = requireActivity()
        // Crear un modificador del menu (toolbar)
        activityMenu.addMenuProvider( object : MenuProvider {
            // Reemplaza el menu
            override fun onCreateMenu(menuViejo: Menu, inflaMenuNuevo: MenuInflater) {
                menuViejo.clear()
                inflaMenuNuevo.inflate(R.menu.toolbar_proyectos_detalles, menuViejo)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_editar_proyecto -> {
                        if (::proyectoVisualizado.isInitialized) {
                            try {
                                findNavController().navigate(ProyectoDetallesFragmentDirections.actionProyectoDetallesFragmentToModificarProyectoFragment(proyectoVisualizado))
                            }
                            catch (e: Exception) {
                                Snackbar.make(
                                    binding.root,
                                    "Ha habido un error al intentar\neditar el proyecto",
                                    Snackbar.LENGTH_SHORT
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
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



        return view
    }

    private fun dialogoBorrado() {
        AlertDialog.Builder(requireContext(), com.example.tustareas.R.style.DialogoPersonalizado)
            .setTitle("Desea eliminar este proyecto?")
            .setMessage("Nota: las tareas no serán borradas")
            .setPositiveButton("Eliminar") { _,_ ->
                if (binding.tituloProyecto.text.toString().trim().isNotEmpty()) {
                    // Actualizamos los campos de texto con los ultimo
                    proyectoVisualizado.proyecto.nombre = binding.tituloProyecto.text.toString().trim()
                    proyectoVisualizado.proyecto.descripcion = binding.descipcionProyecto.text.toString().trim()

                    // Generamos un hilo con la nueva tarea
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.eliminarProyectoConTareaYEtiqueta(proyectoVisualizado)
                        }
                        catch (e: Exception) {
                            Snackbar.make(binding.root, "Ha habido un error al intentar\nborrar el proyecto",
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }

                    // Vovlemos a la vista previa
                    findNavController().popBackStack()
                }
                else {
                    // Mensaje en caso de error controlado
                    Snackbar.make(binding.root, "Ha habido un error al intentar\nborrar el proyecto",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}