package com.example.tustareas.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentProyectoDetallesBinding
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import com.google.android.material.chip.Chip


class ProyectoDetallesFragment : Fragment() {
    private var _binding : FragmentProyectoDetallesBinding ?= null
    private val binding : FragmentProyectoDetallesBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )


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

        }



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}