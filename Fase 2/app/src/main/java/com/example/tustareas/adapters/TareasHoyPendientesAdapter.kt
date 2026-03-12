package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelos.Tarea

class TareasHoyPendientesAdapter(private val tareas: List<Tarea>): RecyclerView.Adapter<TareasHoyPendientesAdapter.TareaViewHolder>() {
    // view holder
    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTarea : TextView = itemView.findViewById<TextView>(R.id.nombreTarea)
        val prioridadTarea : TextView = itemView.findViewById<TextView>(R.id.prioridadTarea)
    }

    // inflar el contenido de la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_tareas_pendientes_hoy, parent, false)
        return TareaViewHolder(view)
    }

    // Sobreescritura de valores
    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val objetoActual = tareas[position]
        holder.nombreTarea.text = objetoActual.nombre
        holder.prioridadTarea.text = objetoActual.prioridad.name
    }

    override fun getItemCount(): Int {
        return tareas.size
    }



}