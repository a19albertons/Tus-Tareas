package com.example.tustareas.fragmentos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.EtiquetasAdapter
import com.example.tustareas.databinding.FragmentListarEtiquetasBinding
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Etiqueta
import com.google.android.material.snackbar.Snackbar
import kotlin.getValue

class ListarEtiquetasFragment : Fragment() {
    private var _binding: FragmentListarEtiquetasBinding? = null
    private val binding: FragmentListarEtiquetasBinding
        get() = _binding!!

    val model: TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListarEtiquetasBinding.inflate(inflater, container, false)

        // Obtenemos la referncia
        val recyclerView = binding.listaEtiquetas

        // Definimos el layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Gestiona el adapter
        // El valor por defecto vacio se pasa al modelo que estara pendiente de los cambios a traves de una nueva función dedicada
        model.listarEtiquetas.obtenerEtiquetasFiltradas().observe(viewLifecycleOwner) {
                listadoEtiquetas ->
            if (listadoEtiquetas.isEmpty()) {
                binding.sinResultados.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                binding.sinResultados.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
            recyclerView.adapter = EtiquetasAdapter(listadoEtiquetas)
        }
        val filtro = binding.filtro
        filtro.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(texto: CharSequence?, empieza: Int, posicion: Int, siguiente: Int) {

            }
            override fun onTextChanged(texto: CharSequence?, empieza: Int, fin: Int, posicion: Int) {

            }
            override fun afterTextChanged(texto: Editable?) {
                // Actualiza el texto del filtro como si fuese un observer unificado evita los dupliados que antes se generaban
                model.listarEtiquetas.actualizarTextoListadoEtiqueta(texto.toString())
            }

    })
        val boton = binding.anadirEtiqueta
        boton.setOnClickListener {
            try {
                findNavController().navigate(ListarEtiquetasFragmentDirections.actionListarEtiquetasFragmentToModificarEtiquetaFragment(
                    Etiqueta(0,"","")))
            }
            catch (_: Exception) {
                Snackbar.make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}