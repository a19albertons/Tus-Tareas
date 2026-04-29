package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.fragmentos.VerMasFragmentDirections
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Clase que define el adapter de ver más
 */
// Modificado a ListAdapter para poder controlar los cambios en el checkbox sin volver al inicio
class VerMasAdapter(private val model: TusTareasModel, private val verMas: Int): ListAdapter<Tarea, VerMasAdapter.VerMasViewHolder>(TareaComprobacionDiferncias()) {
    // Genera un scope para los procesos secundarios
    private val scope = MainScope()

    // View holder
    class VerMasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTarea: TextView = itemView.findViewById(R.id.nombreTarea)
        val otroCampo: TextView = itemView.findViewById(R.id.otroCampo)
        val clickable: MaterialCardView = itemView.findViewById(R.id.clickable)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    // Inflar el contenido de la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerMasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_ver_mas, parent, false)
        return VerMasViewHolder(view)
    }

    // Sobreescritura de valores
    override fun onBindViewHolder(holder: VerMasViewHolder, posicion: Int) {
        val objectoActual = getItem(posicion)
        holder.nombreTarea.text = objectoActual.nombre
        // En función de la entrada carga la prioridad o la fecha en texto
        if (verMas == 1) {
            holder.otroCampo.text = objectoActual.prioridad.name
        }
        else {
            holder.otroCampo.text = DateHelper.timestampToString(objectoActual.fechaLimite)
        }
        // Controla el checkbox
        holder.clickable.setOnClickListener {
            try {
                it.findNavController().navigate(VerMasFragmentDirections.actionVerMasFragmentToTareaDetallesFragment(objectoActual.id))
            }
            catch (_: Exception) {
                Snackbar.make(it, it.context.getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }
        }
        // Gestiona el estado de la tarea
        // Comprueba el estado
        if (objectoActual.estado == Estado.Completada) {
            holder.checkbox.isChecked = true
        }
        else {
            holder.checkbox.isChecked = false
        }
        // Actualiza el click en consecuencia
        holder.checkbox.setOnClickListener {
            model.verMas.clickCheckbox(objectoActual, holder.checkbox)
        }

    }

    // Para evitar volver al inicio al hacer scroll al cambiar un checkbox
    class TareaComprobacionDiferncias : DiffUtil.ItemCallback<Tarea>() {
        // Comprobar en el global
        override fun areContentsTheSame(viejaTarea: Tarea, nuevaTarea: Tarea): Boolean {
            return viejaTarea == nuevaTarea
        }


        // Comprobar algo unico (ids)
        override fun areItemsTheSame(viejaTarea: Tarea, nuevaTarea: Tarea): Boolean {

            return viejaTarea.id == nuevaTarea.id
        }
    }
}
