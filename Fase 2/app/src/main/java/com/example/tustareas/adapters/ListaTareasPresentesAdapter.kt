package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelos.Tarea
import com.google.android.material.snackbar.Snackbar

class ListaTareasPresentesAdapter(private val tareaEliminada: (List<Tarea>) -> Unit) : ListAdapter<Tarea, ListaTareasPresentesAdapter.TareaViewHolder>(TareaDiferenciasComprobacion()) {
    // view holder
    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTarea : TextView = itemView.findViewById(R.id.nombreTarea)
        val eliminarTarea : TextView = itemView.findViewById(R.id.eliminarTarea)
    }

    // Inflar el contenido de la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lista_tareas_presentes, parent, false)
        return TareaViewHolder(view)
    }

    // Sobreescritura de valores
    override fun onBindViewHolder(holder: TareaViewHolder, posicion: Int) {
        val objetoActual = getItem(posicion)
        holder.nombreTarea.text = objetoActual.nombre
        holder.eliminarTarea.setOnClickListener {
            // Aplicamos la eliminación de la etiqueta
            val etiquetas = currentList.toMutableList()
            etiquetas.remove(objetoActual)
            tareaEliminada(etiquetas)
            Snackbar.make(it, it.context.getString(R.string.tarea_eliminada), Snackbar.LENGTH_SHORT).show()
            submitList(etiquetas)
        }
    }

    // Comprobacion
    class TareaDiferenciasComprobacion : DiffUtil.ItemCallback<Tarea>() {
        // Comprobar en el globar
        override fun areContentsTheSame(viejaTarea: Tarea, nuevaEtiqueta: Tarea): Boolean {
            return viejaTarea == nuevaEtiqueta
        }

        // Comprobar algo unico (ids)
        override fun areItemsTheSame(viejaTarea: Tarea, nuevaTarea: Tarea): Boolean {
            return viejaTarea.id == nuevaTarea.id
        }
    }
}