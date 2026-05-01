package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper

/**
 * Clase que gestiona el adaptador de tareas retrasadas.
 *
 * @param tareas Lista de tareas retrasadas a mostrar.
 * @return RecyclerView.Adapter con las tareas retrasadas a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class TareasRetrasadasAdapter(private var tareas: List<Tarea>) : RecyclerView.Adapter<TareasRetrasadasAdapter.TareasViewHolder>() {

    /**
     * View holder que almcacena las variables de cada elemento de la lista.
     *
     * @param itemView La vista de un elemento de la lista.
     * @return RecyclerView.ViewHolder con las variables de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class TareasViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val nombreTarea: TextView = itemView.findViewById(R.id.nombreTarea)
        val fechaLimite: TextView = itemView.findViewById(R.id.fechaLimite)
    }

    /**
     * Inflar el contenido de la vista
     *
     * @param parent El grupo de vistas padre.
     * @param viewType El tipo de vista.
     * @return TareaViewHolder con la vista inflada.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_tareas_retrasadas, parent, false)
        return TareasViewHolder(view)
    }

    /**
     * Sobreescritura de valores de cada elemento de la lista.
     *
     * @param holder El view holder de cada elemento de la lista.
     * @param position La posición de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onBindViewHolder(holder: TareasViewHolder, position: Int) {
        val objetoActual = tareas[position]
        holder.nombreTarea.text = objetoActual.nombre
        holder.fechaLimite.text = DateHelper.timestampToString(objetoActual.fechaLimite)
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