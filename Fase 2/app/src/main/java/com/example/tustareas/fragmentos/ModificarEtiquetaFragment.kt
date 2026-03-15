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
import com.example.tustareas.databinding.FragmentModificarEtiquetaBinding
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Etiqueta
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ModificarEtiquetaFragment : Fragment() {
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
    ): View? {
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
        AlertDialog.Builder(requireContext())
            .setTitle("Estas seguro de guardar la etiqueta")
            .setMessage("")
            .setPositiveButton("Guardar") { _, _ ->
                if (binding.tituloEtiqueta.text.toString().trim().isNotEmpty()) {
                    // Pasamos el filtro de nulos del if y actualizamos la clase etiqueta con los datos del formulario
                    etiquetaPasada.nombre = binding.tituloEtiqueta.text.toString().trim()
                    etiquetaPasada.descripcion = binding.descipcionEtiqueta.text.toString().trim()

                    // Generamos un hilo donde se ejecuta la inserción
                    viewLifecycleOwner.lifecycleScope.launch {
                        model.insertarEtiqueta(etiquetaPasada)
                    }

                    // Volvemos a la vista previa
                    findNavController().popBackStack()
                }
                else {
                    Snackbar.make(binding.root, "Ha habido un error al guardar\nla nueva etiqueta",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Descartar") { _, _ ->
                findNavController().popBackStack()
            }
            .setNeutralButton("Continuar",null)
            .show()
    }
    // Dialogo de modificación
    private fun dialogoModificado() {
        AlertDialog.Builder(requireContext())
            .setTitle("Estas seguro de los cambios")
            .setMessage("")
            .setPositiveButton("Guardar") { _, _ ->
                if (binding.tituloEtiqueta.text.toString().trim().isNotEmpty()) {
                    // Pasamos el filtro de nulos del if y actualizamos la clase etiqueta con los datos del formulario
                    etiquetaPasada.nombre = binding.tituloEtiqueta.text.toString().trim()
                    etiquetaPasada.descripcion = binding.descipcionEtiqueta.text.toString().trim()

                    // Generamos un hilo donde se ejecuta la inserción
                    viewLifecycleOwner.lifecycleScope.launch {
                        model.modificarEtiqueta(etiquetaPasada)
                    }

                    // Volvemos a la vista previa
                    findNavController().popBackStack()
                }
                else {
                    Snackbar.make(binding.root, "Ha habido un error al guardar\nla modificación",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Descartar") { _, _ ->
                findNavController().popBackStack()
            }
            .setNeutralButton("Continuar",null)
            .show()
    }


}