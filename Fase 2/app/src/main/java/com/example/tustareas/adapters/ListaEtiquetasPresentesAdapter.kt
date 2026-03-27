package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelos.Etiqueta
import com.google.android.material.snackbar.Snackbar

class ListaEtiquetasPresentesAdapter(private val etiquetaEliminada: (List<Etiqueta>) -> Unit) : ListAdapter<Etiqueta, ListaEtiquetasPresentesAdapter.EtiquetaViewHolder>(EtiquetaDiferenciasComprobacion()) {
    // view holder
    class EtiquetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEtiqueta : TextView = itemView.findViewById(R.id.nombreEtiqueta)
        val eliminarEtiqueta : TextView = itemView.findViewById(R.id.eliminarEtiqueta)
    }

    // Inflar el contenido de la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtiquetaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lista_etiquetas_presentes, parent, false)
        return EtiquetaViewHolder(view)
    }

    // Sobreescritura de valores
    override fun onBindViewHolder(holder: EtiquetaViewHolder, posicion: Int) {
        val objetoActual = getItem(posicion)
        holder.nombreEtiqueta.text = objetoActual.nombre
        holder.eliminarEtiqueta.setOnClickListener {
            // Aplicamos la eliminación de la etiqueta
            val etiquetas = currentList.toMutableList()
            etiquetas.remove(objetoActual)
            etiquetaEliminada(etiquetas)
            Snackbar.make(it, it.context.getString(R.string.etiqueta_eliminada), Snackbar.LENGTH_SHORT).show()
            submitList(etiquetas)
        }
    }

    // Comprobacion
    class EtiquetaDiferenciasComprobacion : DiffUtil.ItemCallback<Etiqueta>() {
        // Comprobar en el globar
        override fun areContentsTheSame(viejaEtiqueta: Etiqueta, nuevaEtiqueta: Etiqueta): Boolean {
            return viejaEtiqueta == nuevaEtiqueta
        }

        // Comprobar algo unico (ids)
        override fun areItemsTheSame(viejaEtiqueta: Etiqueta, nuevaEtiqueta: Etiqueta): Boolean {
            return viejaEtiqueta.id == nuevaEtiqueta.id
        }
    }
}