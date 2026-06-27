package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentAjustesBinding
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.util.IdiomaApp
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que gestiona el fragmento de ajustes.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class AjustesFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentAjustesBinding? = null
    val binding: FragmentAjustesBinding
        get() = _binding!!

    val model: TusTareasModel by viewModels()

    /**
     * Crea la vista del fragmento ajustes y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento ajustes.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)

        // Gestiona el idioma
        gestionIdioma()

        // Gestiona el tema
        gestionTema()

        return binding.root
    }

    /**
     * Funcion privada que gestiona el Idioma. Su mision es reducir el llamado codigo spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iesanclemente.net>
     */
    private fun gestionIdioma() {
        // Spinner de idiomas
        val idioma = resources.getStringArray(R.array.idiomas)
        val adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_personalizado,
                idioma,
            )
        binding.idioma.adapter = adapter

        // Obtenemos el valor por defecto del modelo y lo deshabilitamos en la primera
        // ejecucíón para evitar un reinicio (recreación
        val idiomaGuardado = model.idioma.value

        // Obtiene la posicion inicial a partir del valor y mirando las entradas del enum class
        val posicionInicial = IdiomaApp.entries.toTypedArray().indexOfFirst { it.nombre == idiomaGuardado }
        binding.idioma.setSelection(posicionInicial, false)

        // Gestiona la elección del idioma
        binding.idioma.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    posicion: Int,
                    id: Long,
                ) {
                    when (posicion) {
                        // Actualizar idioma
                        0 -> model.setIdioma("Sistema")
                        1 -> model.setIdioma("Español")
                        2 -> model.setIdioma("Ingles")
                        3 -> model.setIdioma("Gallego")
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    /**
     * Funcion privada que gestiona el tema. Su mision es reducir el llamado codigo spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iesanclemente.net>
     */
    private fun gestionTema() {
        // Gestiona la elección del modo claro/oscuro/sistema
        // Radio Buttons
        binding.claro.setOnClickListener {
            model.setTema(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding.oscuro.setOnClickListener {
            model.setTema(AppCompatDelegate.MODE_NIGHT_YES)
        }
        binding.sistema.setOnClickListener {
            model.setTema(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        // Vigila que el clickado sea el unico marcado y desmarcar el resto
        model.tema.observe(viewLifecycleOwner) { modo ->
            binding.claro.isChecked = (modo == AppCompatDelegate.MODE_NIGHT_NO)
            binding.oscuro.isChecked = (modo == AppCompatDelegate.MODE_NIGHT_YES)
            binding.sistema.isChecked = (modo == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    /**
     * Destruye la vista del fragmento ajustes y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
