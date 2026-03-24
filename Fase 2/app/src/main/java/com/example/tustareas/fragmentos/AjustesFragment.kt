package com.example.tustareas.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentAjustesBinding
import com.example.tustareas.modelView.TusTareasModel


class AjustesFragment : Fragment() {
    private var _binding : FragmentAjustesBinding ?= null
    private val binding : FragmentAjustesBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = {requireActivity()}
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        val view = binding.root




        return view
    }


}