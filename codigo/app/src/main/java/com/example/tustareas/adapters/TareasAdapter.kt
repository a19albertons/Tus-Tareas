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
import com.example.tustareas.fragmentos.ListarTareasFragmentDirections
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Clase que define el adapter de las tareas
 */
// Modificado a ListAdapter para poder controlar los cambios en el checkbox sin volver al inicio
class TareasAdapter(private val model: TusTareasModel): ListAdapter<Tarea, TareasAdapter.TareasViewHolder>(TareaComprobacionDiferncias()) {
    // Genera un scope para los procesos secundarios
    private val scope = MainScope()

    // View holder
    class TareasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTarea: TextView = itemView.findViewById(R.id.nombreTarea)
        val fechaLimite: TextView = itemView.findViewById(R.id.fechaLimite)
        val clickable: MaterialCardView = itemView.findViewById(R.id.clickable)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    // Inflar el contenido de la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_tareas, parent, false)
        return TareasViewHolder(view)
    }

    // Sobreescritura de valores
    override fun onBindViewHolder(holder: TareasViewHolder, posicion: Int) {
        val objectoActual = getItem(posicion)
        holder.nombreTarea.text = objectoActual.nombre
        holder.fechaLimite.text = DateHelper.timestampToString(objectoActual.fechaLimite)
        holder.clickable.setOnClickListener {
            try {
                it.findNavController().navigate(ListarTareasFragmentDirections.actionListarTareasFragmentToTareaDetallesFragment(objectoActual.id))
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
            if (holder.checkbox.isChecked) {
                objectoActual.estado = Estado.Completada
                scope.launch {
                    try {
                        model.listarTareas.modificarTarea(objectoActual)
                    }
                    catch (_: Exception) {
                        Snackbar.make(it,it.context.getString(R.string.error_modificar_checkbox), Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                // Control de la fecha EnTiempo o Retrasada
                if (objectoActual.fechaLimite != null && objectoActual.fechaLimite!! > Date()) {
                    objectoActual.estado = Estado.EnTiempo
                }
                else {
                    objectoActual.estado = Estado.Retrasada
                }
                scope.launch {
                    try {
                        model.listarTareas.modificarTarea(objectoActual)
                    }
                    catch (_: Exception) {
                        Snackbar.make(it,it.context.getString(R.string.error_modificar_checkbox), Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
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
