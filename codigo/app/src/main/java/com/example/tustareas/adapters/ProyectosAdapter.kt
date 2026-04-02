package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.fragmentos.ListarProyectosFragmentDirections
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.util.DateHelper
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

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
            try {
                it.findNavController().navigate(ListarProyectosFragmentDirections.actionListarProyectosFragmentToProyectoDetallesFragment(objectoActual.id))
            }
            catch (_: Exception) {
                Snackbar.make(it,it.context.getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    // Tamaño consulta proyectos
    override fun getItemCount(): Int {
        return proyectos.size
    }

}