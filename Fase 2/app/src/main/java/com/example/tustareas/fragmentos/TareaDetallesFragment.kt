package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}