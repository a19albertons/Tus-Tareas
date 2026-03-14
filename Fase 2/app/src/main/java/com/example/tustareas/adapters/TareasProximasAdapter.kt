package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper

class TareasProximasAdapter(private var tareas:List<Tarea>) : RecyclerView.Adapter<TareasProximasAdapter.TareasViewHolder>() {

    // View holder
    class TareasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTarea: TextView = itemView.findViewById(R.id.nombreTarea)
        val fechaLimite: TextView = itemView.findViewById(R.id.fechaLimite)
    }

    // Inflar el contenido de la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.adapter_tareas_proximas, parent, false)
        return TareasViewHolder(view)
    }

    // Sobreescritura de valores
    override fun onBindViewHolder(holder: TareasViewHolder, posicion: Int) {
        val objetoActual = tareas[posicion]
        holder.nombreTarea.text = objetoActual.nombre
        holder.fechaLimite.text = DateHelper.timestampToString(objetoActual.fechaLimite)
    }

    // total de elementos
    override fun getItemCount(): Int {
        return tareas.size
    }
}