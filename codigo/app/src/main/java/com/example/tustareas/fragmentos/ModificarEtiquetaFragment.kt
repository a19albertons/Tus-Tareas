package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentModificarEtiquetaBinding
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Etiqueta
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * Clase que gestiona el fragmento de modificación de etiquetas.
 */
class ModificarEtiquetaFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentModificarEtiquetaBinding? = null
    private val binding: FragmentModificarEtiquetaBinding
        get() = _binding!!

    val model: TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )

    lateinit var etiquetaPasada : Etiqueta



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentModificarEtiquetaBinding.inflate(inflater, container, false)
        val view = binding.root
        val args = ModificarEtiquetaFragmentArgs.fromBundle(requireArguments())
        etiquetaPasada = args.etiqueta
        binding.tituloEtiqueta.setText(etiquetaPasada.nombre)
        binding.descipcionEtiqueta.setText(etiquetaPasada.descripcion)



        return view
    }

    // Metodo sobreescrito para controlar los modales de guardado y modicación de etiquetas
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        // Variable que controla que modal
        val flechaRetroceso = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 0 solo la pueden tener las de nueva creación
                if (etiquetaPasada.id == 0) {
                    dialogoGuardado()
                }
                else {
                    dialogoModificado()
                }
            }
        }

        // Modifica el comportamiento en el activity
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, flechaRetroceso)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Dialogo de guardado
    private fun dialogoGuardado() {
        AlertDialog.Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_guardar_etiqueta))
            .setMessage("")
            .setPositiveButton(R.string.guardar) { _, _ ->
                // Logica de guardado y campos que no tolera nulos
                if (binding.tituloEtiqueta.text.toString().trim().isNotEmpty()) {
                    // Pasamos el filtro de nulos del if y actualizamos la clase etiqueta con los datos del formulario
                    etiquetaPasada.nombre = binding.tituloEtiqueta.text.toString().trim()
                    etiquetaPasada.descripcion = binding.descipcionEtiqueta.text.toString().trim()

                    // Generamos un hilo donde se ejecuta la inserción
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.modificarEtiquetas.insertarEtiqueta(etiquetaPasada)
                            // Volvemos a la vista previa
                            findNavController().popBackStack()
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_guardar_etiqueta),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }


                }
                else {
                    Snackbar.make(binding.root, getString(R.string.error_guardar_etiqueta),
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.descartar)) { _, _ ->
                findNavController().popBackStack()
            }
            .setNeutralButton(getString(R.string.continuar),null)
            .show()
    }
    // Dialogo de modificación
    private fun dialogoModificado() {
        AlertDialog.Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_modificar_etiqueta))
            .setMessage("")
            .setPositiveButton(getString(R.string.guardar)) { _, _ ->
                if (binding.tituloEtiqueta.text.toString().trim().isNotEmpty()) {
                    // Pasamos el filtro de nulos del if y actualizamos la clase etiqueta con los datos del formulario
                    etiquetaPasada.nombre = binding.tituloEtiqueta.text.toString().trim()
                    etiquetaPasada.descripcion = binding.descipcionEtiqueta.text.toString().trim()

                    // Generamos un hilo donde se ejecuta la inserción
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            model.modificarEtiquetas.modificarEtiqueta(etiquetaPasada)
                            // Volvemos a la vista previa
                            findNavController().popBackStack()
                        }
                        catch (_: Exception) {
                            Snackbar.make(binding.root, getString(R.string.error_modificar_etiqueta),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }


                }
                else {
                    Snackbar.make(binding.root, getString(R.string.error_modificar_etiqueta),
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.descartar)) { _, _ ->
                findNavController().popBackStack()
            }
            .setNeutralButton(getString(R.string.continuar),null)
            .show()
    }


}