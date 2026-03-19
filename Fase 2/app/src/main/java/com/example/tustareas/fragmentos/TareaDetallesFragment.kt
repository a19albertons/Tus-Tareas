package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentTareaDetallesBinding
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.util.DateHelper
import com.google.android.material.chip.Chip

class TareaDetallesFragment : Fragment() {
    private var _binding : FragmentTareaDetallesBinding? = null
    private val binding : FragmentTareaDetallesBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )
    private lateinit var tareaVisualizada : TareaDTO



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTareaDetallesBinding.inflate(inflater, container, false)
        val view = binding.root

        val args = TareaDetallesFragmentArgs.fromBundle(requireArguments())
        val tareaID = args.id
        model.obtenerTareaDTOPorID(tareaID).observe(viewLifecycleOwner) {
            tarea ->
            binding.tituloTarea.text = tarea.tarea.nombre
            binding.descipcionTarea.text = tarea.tarea.descripcion
            binding.fechaCreacionTarea.text = DateHelper.timestampToString(tarea.tarea.fechaCreacion)
            if (tarea.tarea.fechaLimite != null) {
                binding.fechaLimiteTarea.text = DateHelper.timestampToString(tarea.tarea.fechaLimite!!)
            } else {
                binding.fechaLimiteTarea.text = ""
            }
            binding.prioridadTarea.text = tarea.tarea.prioridad.name
            binding.estadoTarea.text = tarea.tarea.estado.name
            // Falta definir etiquetas
            binding.etiquetasTareaGroup.removeAllViews()
            tarea.etiquetas.forEach {
                etiqueta ->
                val chip = Chip(requireContext()).apply {
                    text = etiqueta.nombre
                    setChipBackgroundColorResource(R.color.gray)
                    setTextColor(resources.getColor(R.color.black))
                    isClickable = false
                    isFocusable = false
                    isCheckable = false
                    chipStrokeWidth = 0f

                    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                        .setAllCornerSizes(0f)
                        .build()

                }
                binding.etiquetasTareaGroup.addView(chip)
            }
            tareaVisualizada = tarea
        }

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
                            findNavController().navigate(TareaDetallesFragmentDirections.actionTareaDetallesFragmentToModificarTareasFragment(tareaVisualizada))
                        }
                        true

                    }
                    R.id.action_eliminar_tarea -> {
                        if (::tareaVisualizada.isInitialized) {
                            TODO("Añadir opcion de borrar tarea")
                        }
                        true
                    }
                    else -> false
                }
            }
        })
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}