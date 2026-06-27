package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentCrearEtiquetaBinding
import com.example.tustareas.modelView.CrearEtiquetasModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que gestiona el fragmento de creación de etiquetas.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class CrearEtiquetaFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentCrearEtiquetaBinding? = null
    val binding: FragmentCrearEtiquetaBinding
        get() = _binding!!

    val model: CrearEtiquetasModel by viewModels()

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCrearEtiquetaBinding.inflate(inflater, container, false)

        // Recuperamos la etiqueta pasada por argumentos
        val args = CrearEtiquetaFragmentArgs.fromBundle(requireArguments())
        model.definirEtiqueta(args.etiqueta)

        // Rellenamos los campos con los datos de la etiqueta pasada
        rellenarCampos()

        // Gestiona el error de guardado de la etiqueta
        vigilarError()

        // Vigilar resultado
        vigilarResultado()

        return binding.root
    }

    /**
     * Vigila el resultado de la operación de guardado o modificación de la etiqueta y navega al fragmento
     * en caso de exito.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun vigilarResultado() {
        model.observarResultado().observe(viewLifecycleOwner) { resultado ->
            if (resultado) {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * Hace modificaciones en la vista ya creada para gestionar los eventos de los elementos de la vista.
     *
     * @param view La vista del fragmento de modificación de etiquetas.
     * @param savedInstanceState El estado guardado de la vista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Gestiona la lógica de la flecha de retroceso
        gestionarFlechaRetroceso()
    }

    /**
     * Vigila los mensajes de error y muestra un snackar con el mensaje de error asociado al int
     * que se le entrega de r string
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun vigilarError() {
        model.observarMensajeError().observe(viewLifecycleOwner) { mensaje ->
            Snackbar.make(binding.root, getString(mensaje), Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Función que se encarga de rellenar los campos del formulario con los datos de la etiqueta pasada por argumentos.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun rellenarCampos() {
        // Rellenamos los campos con los datos de la etiqueta pasada por argumentos
        model.observarEtiqueta().value?.let { etiqueta ->
            binding.tituloEtiqueta.setText(etiqueta.nombre)
            binding.descipcionEtiqueta.setText(etiqueta.descripcion)
        }
    }

    /**
     * Función que se encarga de gestionar la lógica de la flecha de retroceso para mostrar un diálogo de confirmación al usuario antes de salir del fragmento.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarFlechaRetroceso() {
        // Variable que controla que modal
        val flechaRetroceso =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 0 solo la pueden tener las de nueva creación
                    dialogo()
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
    private fun dialogo() {
        AlertDialog
            .Builder(requireContext(), R.style.DialogoPersonalizado)
            .setTitle(getString(R.string.confirmar_guardar_etiqueta))
            .setMessage("")
            .setPositiveButton(R.string.guardar) { _, _ ->
                // Logica de guardado y campos que no tolera nulos
                model.guardarEtiqueta(
                    binding.tituloEtiqueta.text
                        .toString()
                        .trim(),
                    binding.descipcionEtiqueta.text
                        .toString()
                        .trim(),
                )
            }.setNegativeButton(getString(R.string.descartar)) { _, _ ->
                findNavController().popBackStack()
            }.setNeutralButton(getString(R.string.continuar), null)
            .show()
    }
}
