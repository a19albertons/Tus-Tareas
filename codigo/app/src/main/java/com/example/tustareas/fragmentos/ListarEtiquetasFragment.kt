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
import com.example.tustareas.modelView.ListarEtiquetasModel
import com.example.tustareas.modelos.Etiqueta
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que gestiona el fragmento de listar etiquetas.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class ListarEtiquetasFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentListarEtiquetasBinding? = null
    val binding: FragmentListarEtiquetasBinding
        get() = _binding!!

    val model: ListarEtiquetasModel by viewModels()

    private var adapter: EtiquetasAdapter? = null

    /**
     * Crea la vista del fragmento de listar etiquetas y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento de listar etiquetas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListarEtiquetasBinding.inflate(inflater, container, false)

        // Configura el RecyclerView para mostrar las etiquetas
        configurarRecyclerView()

        // Actualizar filtro de texto para las etiquetas
        actualizarFiltroTexto()

        // Gestiona el botón de añadir etiqueta
        gestionarBotonAnadirEtiqueta()

        return binding.root
    }

    /**
     * Función privada que configura el RecyclerView para mostrar la lista de etiquetas. Se encarga de establecer el layout manager y el adapter para el RecyclerView, así como de gestionar la visibilidad de los elementos en función de si hay resultados o no.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun configurarRecyclerView() {
        // Definimos el layout manager
        binding.listaEtiquetas.layoutManager = LinearLayoutManager(requireContext())

        // Definir el adapter
        adapter = EtiquetasAdapter()
        binding.listaEtiquetas.adapter = adapter

        // Gestiona el adapter
        // El valor por defecto vacio se pasa al modelo que estara pendiente de los cambios a traves de una nueva función dedicada
        model.obtenerEtiquetasFiltradas().observe(viewLifecycleOwner) { listadoEtiquetas ->
            if (listadoEtiquetas.isEmpty()) {
                binding.sinResultados.visibility = View.VISIBLE
                binding.listaEtiquetas.visibility = View.GONE
            } else {
                binding.sinResultados.visibility = View.GONE
                binding.listaEtiquetas.visibility = View.VISIBLE
            }
            adapter!!.submitList(listadoEtiquetas)
        }
    }

    /**
     * Función privada que actualiza el filtro de texto para las etiquetas. Se encarga de gestionar el evento de cambio de texto en el campo de filtro y de actualizar el texto del filtro en el modelo para que este pueda filtrar las etiquetas en función del texto introducido.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun actualizarFiltroTexto() {
        // gestiona el filtro de texto
        binding.filtro.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    texto: CharSequence?,
                    empieza: Int,
                    posicion: Int,
                    siguiente: Int,
                ) {
                }

                override fun onTextChanged(
                    texto: CharSequence?,
                    empieza: Int,
                    fin: Int,
                    posicion: Int,
                ) {
                }

                override fun afterTextChanged(texto: Editable?) {
                    // Actualiza el texto del filtro como si fuese un observer unificado evita los dupliados que antes se generaban
                    model.actualizarTextoListadoEtiqueta(texto.toString())
                }
            },
        )
    }

    /**
     * Función privada que gestiona el botón de añadir etiqueta. Se encarga de gestionar el evento de click en el botón de añadir etiqueta y de navegar a la vista de modificar etiqueta para crear una nueva etiqueta.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarBotonAnadirEtiqueta() {
        // Gestiona el boton añadir etiqueta
        binding.anadirEtiqueta.setOnClickListener {
            try {
                findNavController().navigate(
                    ListarEtiquetasFragmentDirections.actionListarEtiquetasFragmentToCrearEtiquetaFragment(
                        Etiqueta(0, "", ""),
                    ),
                )
            } catch (_: Exception) {
                Snackbar.make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Destruye la vista del fragmento de listar etiquetas y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Liberar recursos
        adapter = null
    }
}
