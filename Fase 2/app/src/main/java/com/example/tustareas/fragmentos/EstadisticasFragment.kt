package com.example.tustareas.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentEstadisticasBinding
import com.example.tustareas.modelView.TusTareasModel


class EstadisticasFragment : Fragment() {
    private var _binding : FragmentEstadisticasBinding? = null
    private val binding : FragmentEstadisticasBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }


}