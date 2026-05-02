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
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
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


    /**
     * Crea la vista del fragmento de modificación de etiquetas y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento de modificación de etiquetas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentModificarEtiquetaBinding.inflate(inflater, container, false)
        val view = binding.root

        // Recuperamos la etiqueta pasada por argumentos
        val args = ModificarEtiquetaFragmentArgs.fromBundle(requireArguments())
        etiquetaPasada = args.etiqueta

        // Rellenamos los campos con los datos de la etiqueta pasada
        rellenarCampos()

        return view
    }

    /**
     * Hace modificaciones en la vista ya creada para gestionar los eventos de los elementos de la vista.
     *
     * @param view La vista del fragmento de modificación de etiquetas.
     * @param savedInstanceState El estado guardado de la vista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gestiona la lógica de la flecha de retroceso
        gestionarFlechaRetroceso()
    }

    /**
     * Función que se encarga de rellenar los campos del formulario con los datos de la etiqueta pasada por argumentos.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun rellenarCampos() {
        // Rellenamos los campos con los datos de la etiqueta pasada por argumentos
        binding.tituloEtiqueta.setText(etiquetaPasada.nombre)
        binding.descipcionEtiqueta.setText(etiquetaPasada.descripcion)
    }

    /**
     * Función que se encarga de gestionar la lógica de la flecha de retroceso para mostrar un diálogo de confirmación al usuario antes de salir del fragmento.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarFlechaRetroceso() {
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
    }

    /**
     * Destruye la vista del fragmento de modificación de etiquetas y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Muestra un diálogo que se encarga del guardado de una etiqueta nueva.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
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

    /**
     * Muestra un diálogo que se encarga de la modificación de una etiqueta existente.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
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