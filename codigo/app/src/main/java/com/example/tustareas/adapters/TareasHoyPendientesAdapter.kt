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

/**
 * Clase que gestiona el adaptador de tareas pendientes de hoy.
 *
 * @return List Adapter con las tareas pendientes de hoy a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class TareasHoyPendientesAdapter(): ListAdapter<Tarea, TareasHoyPendientesAdapter.TareaViewHolder>(
    TareaComprobacionDiferncias()
) {
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
        val objetoActual = getItem(position)
        holder.nombreTarea.text = objetoActual.nombre
        holder.prioridadTarea.text = holder.itemView.context.getString(objetoActual.prioridad.labelRes())
    }

    /**
     * Clase que gestiona las diferencias entre dos tareas.
     * Permite actualizar el estado de las tareas sin volver al inicio, ya que solo se actualiza el elemento que ha cambiado, no toda la lista.
     *
     * @return DiffUtil.ItemCallback con las diferencias entre dos listas de tareas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class TareaComprobacionDiferncias : DiffUtil.ItemCallback<Tarea>() {
        /**
         * Comprobar si el contenido de dos tareas es el mismo.
         *
         * @param viejaTarea La tarea antigua.
         * @param nuevaTarea La tarea nueva.
         * @return true si el contenido de las tareas es el mismo, false en caso contrario.
         * @author Alberto Noceda <a19albertons@iessanclemenet.net>
         */
        override fun areContentsTheSame(viejaTarea: Tarea, nuevaTarea: Tarea): Boolean {
            return viejaTarea == nuevaTarea
        }


        /**
         * Comprobar si el id de dos tareas es el mismo.
         *
         * @param viejaTarea La tarea antigua.
         * @param nuevaTarea La tarea nueva.
         * @return true si el id de las tareas es el mismo, false en caso contrario.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        override fun areItemsTheSame(viejaTarea: Tarea, nuevaTarea: Tarea): Boolean {

            return viejaTarea.id == nuevaTarea.id
        }
    }
}