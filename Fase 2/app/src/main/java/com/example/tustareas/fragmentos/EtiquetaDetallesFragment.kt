package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentEtiquetaDetallesBinding
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Etiqueta
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class EtiquetaDetallesFragment : Fragment() {
    private var _binding: FragmentEtiquetaDetallesBinding? = null
    private val binding: FragmentEtiquetaDetallesBinding
        get() = _binding!!

    val model: TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )

    lateinit var etiquetaVisualizada : Etiqueta


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
            etiquetaVisualizada = etiqueta
        }

        // Invocar el menu del activity
        val activityMenu : MenuHost = requireActivity()
        // Crear un modificador del menu (toolbar)
        activityMenu.addMenuProvider(object : MenuProvider {
            // Reemplaza el menu
            override fun onCreateMenu(menuViejo: Menu, inflaMenuNuevo: MenuInflater) {
                menuViejo.clear()
                inflaMenuNuevo.inflate(R.menu.toolbar_etiqueta_detalles, menuViejo)
            }

            // Configura la navegación de edicción
            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_editar_etiqueta -> {
                        // Controla que ya tenga el resultado cargado, por lo tanto esta inicializada
                        if (::etiquetaVisualizada.isInitialized) {
                            findNavController().navigate(EtiquetaDetallesFragmentDirections.actionEtiquetaDetallesFragmentToModificarEtiquetaFragment(etiquetaVisualizada))
                        }
                        true
                    }
                    R.id.action_eliminar_etiqueta -> {
                        // Controlar que ya tenga el resultado cargado, por lo tanto esta inicializada
                        if (::etiquetaVisualizada.isInitialized) {
                            dialgoEliminacion()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        return view
    }

    private fun dialgoEliminacion() {
        AlertDialog.Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle("Desea eliminar esta etiqueta")
            .setMessage("")
            .setPositiveButton("Eliminar") { _, _ ->
                if (::etiquetaVisualizada.isInitialized) {

                    // Lanzamos la eliminación a otro hilos
                    viewLifecycleOwner.lifecycleScope.launch {
                        model.eliminarEtiqueta(etiquetaVisualizada)
                    }

                    // Volvemos a la vista previa
                    findNavController().popBackStack()
                } else {
                    Snackbar.make(
                        binding.root, "Ha habido un error al guardar\nla modificación",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
