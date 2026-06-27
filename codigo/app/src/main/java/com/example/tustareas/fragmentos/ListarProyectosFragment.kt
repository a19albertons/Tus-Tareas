package com.example.tustareas.fragmentos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.ProyectosAdapter
import com.example.tustareas.databinding.FragmentListarProyectosBinding
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.filtros.OrdenarProyectoFin
import com.example.tustareas.filtros.OrdenarProyectosInicio
import com.example.tustareas.modelView.ListarProyectosModel
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.util.DateHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que gestiona el fragmento de listar proyectos.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class ListarProyectosFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentListarProyectosBinding? = null
    val binding: FragmentListarProyectosBinding
        get() = _binding!!

    val model: ListarProyectosModel by viewModels()

    private var adapter: ProyectosAdapter? = null

    /**
     * Crea la vista del fragmento de listar proyectos y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento de listar proyectos.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListarProyectosBinding.inflate(inflater, container, false)

        // Configura el RecyclerView para mostrar los proyectos
        configurarRecyclerView()

        // Configurar el filtro de texto para los proyectos
        actualizarFiltroTexto()

        // Gestiona el filtro de fecha inicio
        configurarFiltroFechaInicio()

        // Gestiona el filtro de fecha fin
        configurarFiltroFechaFin()

        // Gestiona el botón de añadir proyecto
        gestionarBotonAnadirProyecto()

        return binding.root
    }

    /**
     * Función privada que configura el RecyclerView para mostrar los proyectos. Su misión es reducir el llamado código spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun configurarRecyclerView() {
        // Recycler view para mostrar los proyectos
        binding.listaProyectos.layoutManager = LinearLayoutManager(requireContext())

        // definir adapter
        adapter = ProyectosAdapter()
        binding.listaProyectos.adapter = adapter

        // Observador
        model.obtenerProyectosFiltradas().observe(viewLifecycleOwner) { listadoProyectos ->
            if (listadoProyectos.isEmpty()) {
                binding.sinResultados.visibility = View.VISIBLE
                binding.listaProyectos.visibility = View.GONE
            } else {
                binding.sinResultados.visibility = View.GONE
                binding.listaProyectos.visibility = View.VISIBLE
            }
            adapter!!.submitList(listadoProyectos)
        }
    }

    /**
     * Función privada que actualiza el filtro de texto para los proyectos. Su misión es reducir el llamado código spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun actualizarFiltroTexto() {
        // Control filtro texto
        binding.filtro.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(texto: Editable?) {
                    model.actualizarTextoListadoProyectos(texto.toString())
                }

                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {
                }

                override fun onTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {
                }
            },
        )
    }

    /**
     * Función privada que configura el filtro de fecha inicio para los proyectos. Su misión es reducir el llamado código spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iesanclemente.net>
     */
    private fun configurarFiltroFechaInicio() {
        // Filtro por fecha inicio
        val listaInicio =
            listOf(
                getString(R.string.inicio),
                getString(R.string.ascendente),
                getString(R.string.descendente),
            )
        // Define el adaptador para el spinner de inicio
        binding.inicioProyecto.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_personalizado,
                listaInicio,
            )
        // Gestiona la selección del filtro de fecha inicio
        binding.inicioProyecto.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    posicion: Int,
                    id: Long,
                ) {
                    when (posicion) {
                        0 -> model.actualizarInicioProyecto(OrdenarProyectosInicio.INICIO)
                        1 -> model.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_ASC)
                        2 -> model.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_DES)
                    }
                    // Notificación de filtros
                    if (binding.finProyecto.selectedItemPosition != 0 && binding.inicioProyecto.selectedItemPosition != 0) {
                        Snackbar.make(binding.root, getString(R.string.primero_va_fin_despues_inicio), Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    /**
     * Función privada que configura el filtro de fecha fin para los proyectos. Su misión es reducir el llamado código spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun configurarFiltroFechaFin() {
        // Filtro por fecha fin
        val listaFin =
            listOf(
                getString(R.string.fin),
                getString(R.string.ascendente),
                getString(R.string.descendente),
            )
        // Define el adaptador para el spinner de fin
        binding.finProyecto.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_personalizado,
                listaFin,
            )
        // Gestiona la selección del filtro de fecha fin
        binding.finProyecto.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    posicion: Int,
                    id: Long,
                ) {
                    when (posicion) {
                        0 -> model.actualizarFinProyecto(OrdenarProyectoFin.FIN)
                        1 -> model.actualizarFinProyecto(OrdenarProyectoFin.FECHA_ASC)
                        2 -> model.actualizarFinProyecto(OrdenarProyectoFin.FECHA_DES)
                    }
                    // Notificación de filtros
                    if (binding.finProyecto.selectedItemPosition != 0 && binding.inicioProyecto.selectedItemPosition != 0) {
                        Snackbar.make(binding.root, getString(R.string.primero_va_fin_despues_inicio), Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    /**
     * Función privada que gestiona el botón de añadir proyecto. Su misión es reducir el llamado código spaguetti que había en onCreateView y mejorar la legibilidad del código. Se encarga de gestionar el evento de click en el botón de añadir proyecto y de navegar a la vista de modificar proyecto para crear un nuevo proyecto.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarBotonAnadirProyecto() {
        // Boton añadir
        binding.anadirProyecto.setOnClickListener {
            // Mandamos un valor de proyectoDTO totalmente vacio con valores por defecto
            val proyectoDTO =
                ProyectoDTO(
                    proyecto = Proyecto(0, "", "", DateHelper.fechaMediaNocheUTC(), null, null),
                    etiquetas = emptyList(),
                    tareas = emptyList(),
                )
            try {
                findNavController().navigate(
                    ListarProyectosFragmentDirections.actionListarProyectosFragmentToCrearProyectoFragment(proyectoDTO),
                )
            } catch (_: Exception) {
                Snackbar.make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Destruye la vista del fragmento de listar proyectos y libera los recursos asociados a la vista.
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
