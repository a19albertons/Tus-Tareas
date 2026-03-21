package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.util.DateHelper
import com.google.android.material.card.MaterialCardView

class ProyectosAdapter(private val proyectos: List<Proyecto>): RecyclerView.Adapter<ProyectosAdapter.ProyectoViewHolder>() {
    // View holder
    class ProyectoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProyecto : TextView = itemView.findViewById(R.id.nombreProyecto)
        val fechaFin : TextView = itemView.findViewById(R.id.fechaFin)
        val clickable : MaterialCardView = itemView.findViewById(R.id.clickable)
    }

    // Inflar el contenido
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProyectoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_proyectos, parent, false)
        return ProyectoViewHolder(view)
    }

    // Sobreescribir los valores
    override fun onBindViewHolder(holder: ProyectoViewHolder, posicion: Int) {
        val objectoActual = proyectos[posicion]
        holder.nombreProyecto.text = objectoActual.nombre
        holder.fechaFin.text = DateHelper.timestampToString(objectoActual.fechaFin)
        holder.clickable.setOnClickListener {
            TODO("Pendiente de hacer el fragmento de detalles")
        }
    }

    // Tamaño consulta proyectos
    override fun getItemCount(): Int {
        return proyectos.size
    }

}