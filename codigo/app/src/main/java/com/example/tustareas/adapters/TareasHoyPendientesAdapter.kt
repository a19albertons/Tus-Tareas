package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelos.Tarea

/**
 * Clase que gestiona el adaptador de tareas pendientes de hoy.
 *
 * @param tareas Lista de tareas pendientes de hoy a mostrar.
 * @return RecyclerView.Adapter con las tareas pendientes de hoy a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class TareasHoyPendientesAdapter(private val tareas: List<Tarea>): RecyclerView.Adapter<TareasHoyPendientesAdapter.TareaViewHolder>() {
    /**
     * View holder que almcacena las variables de cada elemento de la lista.
     *
     * @param itemView La vista de un elemento de la lista.
     * @return RecyclerView.ViewHolder con las variables de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTarea : TextView = itemView.findViewById<TextView>(R.id.nombreTarea)
        val prioridadTarea : TextView = itemView.findViewById<TextView>(R.id.prioridadTarea)
    }

    /**
     * Inflar el contenido de la vista
     *
     * @param parent El grupo de vistas padre.
     * @param viewType El tipo de vista.
     * @return TareaViewHolder con la vista inflada.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_tareas_pendientes_hoy, parent, false)
        return TareaViewHolder(view)
    }

    /**
     * Sobreescritura de valores de cada elemento de la lista.
     *
     * @param holder El view holder de cada elemento de la lista.
     * @param position La posición de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val objetoActual = tareas[position]
        holder.nombreTarea.text = objetoActual.nombre
        holder.prioridadTarea.text = objetoActual.prioridad.name
    }

    /**
     * Devuelve el tamaño de la lista.
     *
     * @return Int con el tamaño de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun getItemCount(): Int {
        return tareas.size
    }



}