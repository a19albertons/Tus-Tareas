package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentEtiquetaDetallesBinding
import com.example.tustareas.modelView.TusTareasModel

class EtiquetaDetallesFragment : Fragment() {
    private var _binding: FragmentEtiquetaDetallesBinding? = null
    private val binding: FragmentEtiquetaDetallesBinding
        get() = _binding!!

    val model: TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEtiquetaDetallesBinding.inflate(inflater, container, false)
        val view = binding.root
        val args = EtiquetaDetallesFragmentArgs.fromBundle(requireArguments())
        model.obtenerEtiquetaPorID(args.id).observe(viewLifecycleOwner) {
            etiqueta ->
            binding.tituloEtiqueta.text = etiqueta.nombre
            binding.descipcionEtiqueta.text = etiqueta.descripcion
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}