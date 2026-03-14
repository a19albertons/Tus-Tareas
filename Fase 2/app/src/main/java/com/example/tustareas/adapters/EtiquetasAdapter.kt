package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelos.Etiqueta

class EtiquetasAdapter(private val etiquetas: List<Etiqueta>): RecyclerView.Adapter<EtiquetasAdapter.EtiquetaViewHolder>() {
    // View Holder
    class EtiquetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEtiqueta : TextView = itemView.findViewById(R.id.nombreEtiqueta)
        val descripcionEtiqueta : TextView = itemView.findViewById(R.id.descipcionEtiqueta)
    }

    // Inflar el contenido de la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtiquetaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_etiquetas, parent, false)
        return EtiquetaViewHolder(view)
    }

    // Sobreescritura de valores
    override fun onBindViewHolder(holder: EtiquetaViewHolder, posicion: Int) {
        val objetoActual = etiquetas[posicion]
        holder.nombreEtiqueta.text = objetoActual.nombre
        holder.descripcionEtiqueta.text = objetoActual.descripcion
    }

    // Tamaño de la lista
    override fun getItemCount(): Int {
        return etiquetas.size
    }


}