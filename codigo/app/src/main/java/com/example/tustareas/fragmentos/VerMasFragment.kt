package com.example.tustareas.fragmentos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.adapters.VerMasAdapter
import com.example.tustareas.databinding.FragmentVerMasBinding
import com.example.tustareas.modelView.TusTareasModel
import com.google.android.material.snackbar.Snackbar

/**
 * Clase que gestiona el fragmento de detalles de tareas.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class VerMasFragment : Fragment() {
    // Variables generales de la clase
    private var _binding : FragmentVerMasBinding?= null
    private val binding : FragmentVerMasBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = {requireActivity()}
    )

    /**
     * Crea la vista del fragmento detalles de tareas y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento detalles de tareas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVerMasBinding.inflate(inflater, container, false)
        val view = binding.root

        // args
        val args = VerMasFragmentArgs.fromBundle(requireArguments())
        val origen = args.numeroVerMas

        // configuracion adapter
        binding.listaTareasConCondicionesEnOrigen.layoutManager = LinearLayoutManager(requireContext())
        val adapter = VerMasAdapter( model, origen)
        binding.listaTareasConCondicionesEnOrigen.adapter = adapter

        // valores inicio
        binding.sinResultados.visibility = View.VISIBLE
        binding.listaTareasConCondicionesEnOrigen.visibility = View.GONE

        // filtro de texto copiado de otra clase de este proyecto
        binding.filtro.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(texto: CharSequence?, empieza: Int, posicion: Int, siguiente: Int) {

            }
            override fun onTextChanged(texto: CharSequence?, empieza: Int, fin: Int, posicion: Int) {

            }
            override fun afterTextChanged(texto: Editable?) {
                // Actualiza el texto del filtro como si fuese un observer unificado evita los dupliados que antes se generaban
                model.verMas.actualizarTextoVerMas(texto.toString())
            }

        })

        // consulta en función de origen
        when (origen) {
            // Tarea hoy
            1 -> {
                model.verMas.obtenerTareasTerminanDiaEspecificoConFiltro().observe(viewLifecycleOwner) {
                    tareas ->
                    if (tareas.isEmpty()) {
                        binding.sinResultados.visibility = View.VISIBLE
                        binding.listaTareasConCondicionesEnOrigen.visibility = View.GONE
                    }
                    else  {
                        binding.sinResultados.visibility = View.GONE
                        binding.listaTareasConCondicionesEnOrigen.visibility = View.VISIBLE
                    }
                    adapter.submitList(tareas)
                }
            }
            // Tarea retrasada
            2 -> {
                model.verMas.obtenerTareasRetrasadasConFiltro().observe(viewLifecycleOwner) {
                        tareas ->
                    if (tareas.isEmpty()) {
                        binding.sinResultados.visibility = View.VISIBLE
                        binding.listaTareasConCondicionesEnOrigen.visibility = View.GONE
                    }
                    else {
                        binding.sinResultados.visibility = View.GONE
                        binding.listaTareasConCondicionesEnOrigen.visibility = View.VISIBLE
                    }
                    adapter.submitList(tareas)
                    }
            }
            // Tarea futura
            3 -> model.verMas.obtenerTareasProximasConFiltro().observe(viewLifecycleOwner) {
                tareas ->
                if (tareas.isEmpty()) {
                    binding.sinResultados.visibility = View.VISIBLE
                    binding.listaTareasConCondicionesEnOrigen.visibility = View.GONE
                }
                else {
                    binding.sinResultados.visibility = View.GONE
                    binding.listaTareasConCondicionesEnOrigen.visibility = View.VISIBLE
                }
                adapter.submitList(tareas)
            }
        }

        // Observar errores del listado de tareas
        model.verMas.mensajeError.observe(viewLifecycleOwner) {
                error ->
            error?.let {
                Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
                // Restaurar a null tras ser mostrado
                model.listarTareas.mensajeError.value = null
            }
        }

        return  view
    }

    /**
     * Destruye la vista del fragmento detalles de tareas y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}