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

/**
 * Clase que gestiona el fragmento de detalles de una etiqueta.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class EtiquetaDetallesFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentEtiquetaDetallesBinding? = null
    private val binding: FragmentEtiquetaDetallesBinding
        get() = _binding!!

    val model: TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )

    private lateinit var args : EtiquetaDetallesFragmentArgs

    private lateinit var etiquetaVisualizada : Etiqueta

    /**
     * Crea la vista del fragmento detalles de una etiqueta y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento detalles de una etiqueta.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEtiquetaDetallesBinding.inflate(inflater, container, false)


        // Definición de args
        args = EtiquetaDetallesFragmentArgs.fromBundle(requireArguments())

        // Carga la etiqueta a visualizar
        cargarEtiqueta()


        // Configurar menu toolbar personalizado para el fragmento detalles
        configurarMenu()


        return binding.root
    }

    /**
     * Función privada que carga la etiqueta a visualizar en el fragmento detalles de una etiqueta. Obtiene la etiqueta por su id de los args y actualiza la vista con los datos de la etiqueta.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun cargarEtiqueta() {
        // Obtiene la etiqueta por su id de los args
        model.etiquetaDetalles.obtenerEtiquetaPorID(args.id).observe(viewLifecycleOwner) {
                etiqueta ->

            // Fallo descubierto en base al principio que pasa si hago esto con su contramedida respectiva
            if (etiqueta == null) {
                Snackbar.make(binding.root, getString(R.string.prevencion_fallo_critico), Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack(findNavController().graph.startDestinationId, false)
                return@observe
            }

            binding.tituloEtiqueta.text = etiqueta.nombre
            binding.descipcionEtiqueta.text = etiqueta.descripcion
            etiquetaVisualizada = etiqueta
        }
    }

    /**
     * Función privada que configura el menu toolbar personalizado para el fragmento detalles de una etiqueta. Reemplaza el menu del activity por un menu personalizado para el fragmento detalles y gestiona la navegación a la vista de edición y la eliminación de la etiqueta.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun configurarMenu() {
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
                            try {
                                findNavController().navigate(EtiquetaDetallesFragmentDirections.actionEtiquetaDetallesFragmentToModificarEtiquetaFragment(etiquetaVisualizada))
                            }
                            catch (_: Exception) {
                                Snackbar.make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
                            }
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
    }

    /**
     * Carga un dialogo de confirmación para eliminar una etiqueta
     * Si se confirma la eliminación, se lanza una petición para eliminar la etiqueta en la base de datos
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun dialgoEliminacion() {
        AlertDialog.Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_eliminar_etiqueta))
            .setMessage("")
            .setPositiveButton(getString(R.string.eliminar)) { _, _ ->
                if (::etiquetaVisualizada.isInitialized) {

                    // Lanzamos la eliminación a otro hilos
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.etiquetaDetalles.eliminarEtiqueta(etiquetaVisualizada)
                            // Volvemos a la vista previa
                            findNavController().popBackStack()
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_eliminar_etiqueta), Snackbar.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Snackbar.make(
                        binding.root, getString(R.string.error_eliminar_etiqueta),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            .setNeutralButton(getString(R.string.cancelar), null)
            .show()
    }

    /**
     * Destruye la vista del fragmento detalles de una etiqueta y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
