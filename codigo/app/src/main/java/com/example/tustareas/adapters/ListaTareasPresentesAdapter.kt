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

/**
 * Clase que gestiona el adaptador de tareas presentes.
 *
 * @param tareaEliminada Función que se ejecuta al eliminar una tarea.
 * @return ListAdapter con las tareas presentes a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ListaTareasPresentesAdapter(
    private val tareaEliminada: (List<Tarea>) -> Unit,
) : ListAdapter<Tarea, ListaTareasPresentesAdapter.TareaViewHolder>(TareaDiferenciasComprobacion()) {
    /**
     * View holder que almcacena las variables de cada elemento de la lista.
     *
     * @param itemView La vista de un elemento de la lista.
     * @return RecyclerView.ViewHolder con las variables de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class TareaViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val nombreTarea: TextView = itemView.findViewById(R.id.nombreTarea)
        val eliminarTarea: TextView = itemView.findViewById(R.id.eliminarTarea)
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
    ): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lista_tareas_presentes, parent, false)
        return TareaViewHolder(view)
    }

    /**
     * Sobreescritura de valores de cada elemento de la lista.
     *
     * @param holder El view holder de cada elemento de la lista.
     * @param posicion La posición de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onBindViewHolder(
        holder: TareaViewHolder,
        posicion: Int,
    ) {
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

    /**
     * Clase que gestiona las diferencias entre dos tareas.
     *
     * @return DiffUtil.ItemCallback con las diferencias entre dos listas de tareas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class TareaDiferenciasComprobacion : DiffUtil.ItemCallback<Tarea>() {
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
