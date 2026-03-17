package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.Date

class TareasAdapter(private val tareas: List<Tarea>, private val model: TusTareasModel): RecyclerView.Adapter<TareasAdapter.TareasViewHolder>() {
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
        val objectoActual = tareas[posicion]
        holder.nombreTarea.text = objectoActual.nombre
        holder.fechaLimite.text = DateHelper.timestampToString(objectoActual.fechaLimite)
        holder.clickable.setOnClickListener {
            // Hay que crear el fragmento de detalles
            TODO("Hacer el fragmento de detalles")
        }
        // Gestiona el estado de la tarea
        // Comprueba el estado
        if (objectoActual.estado == Estado.Completada) {
            holder.checkbox.isChecked = true
        }
        // Actualiza el click en consecuencia
        holder.checkbox.setOnClickListener {
            if (holder.checkbox.isChecked) {
                objectoActual.estado = Estado.Completada
                scope.launch {
                    model.modificarTarea(objectoActual)
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
                    model.modificarTarea(objectoActual)
                }
            }
        }

    }

    // Tamaño de la lista
    override fun getItemCount(): Int {
        return tareas.size
    }
}
