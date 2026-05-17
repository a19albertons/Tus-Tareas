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
import com.example.tustareas.util.DateHelper

/**
 * Clase que gestiona el adaptador de tareas retrasadas.
 *
 * @return RecyclerView.Adapter con las tareas retrasadas a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class TareasRetrasadasAdapter :
    ListAdapter<Tarea, TareasRetrasadasAdapter.TareasViewHolder>(
        TareaComprobacionDiferncias(),
    ) {
    /**
     * View holder que almcacena las variables de cada elemento de la lista.
     *
     * @param itemView La vista de un elemento de la lista.
     * @return List Adapter con las variables de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class TareasViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
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
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TareasViewHolder {
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
    override fun onBindViewHolder(
        holder: TareasViewHolder,
        position: Int,
    ) {
        val objetoActual = getItem(position)
        holder.nombreTarea.text = objetoActual.nombre
        holder.fechaLimite.text = DateHelper.timestampToString(objetoActual.fechaLimite)
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
        override fun areContentsTheSame(
            viejaTarea: Tarea,
            nuevaTarea: Tarea,
        ): Boolean = viejaTarea == nuevaTarea

        /**
         * Comprobar si el id de dos tareas es el mismo.
         *
         * @param viejaTarea La tarea antigua.
         * @param nuevaTarea La tarea nueva.
         * @return true si el id de las tareas es el mismo, false en caso contrario.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        override fun areItemsTheSame(
            viejaTarea: Tarea,
            nuevaTarea: Tarea,
        ): Boolean = viejaTarea.id == nuevaTarea.id
    }
}
