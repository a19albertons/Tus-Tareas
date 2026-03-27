package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.fragmentos.ListarEtiquetasFragmentDirections
import com.example.tustareas.modelos.Etiqueta
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class EtiquetasAdapter(private val etiquetas: List<Etiqueta>): RecyclerView.Adapter<EtiquetasAdapter.EtiquetaViewHolder>() {
    // View Holder
    class EtiquetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEtiqueta : TextView = itemView.findViewById(R.id.nombreEtiqueta)
        val descripcionEtiqueta : TextView = itemView.findViewById(R.id.descipcionEtiqueta)
        val clickable : MaterialCardView = itemView.findViewById(R.id.clickable)
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
        holder.clickable.setOnClickListener {
            try {
                it.findNavController().navigate(ListarEtiquetasFragmentDirections.actionListarEtiquetasFragmentToEtiquetaDetallesFragment(objetoActual.id))
            }
            catch (e: Exception) {
                Snackbar.make(it, it.context.getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    // Tamaño de la lista
    override fun getItemCount(): Int {
        return etiquetas.size
    }


}