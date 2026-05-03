package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.fragmentos.VerMasFragmentDirections
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

/**
 * Clase que define el adapter de ver más
 * Migrada a ListAdapter para poder controlar los cambios en el checkbox sin volver al inicio
 *
 * @param model El modelo de las tareas para poder actualizar el estado de las tareas.
 * @param verMas El tipo de ver más (1 para prioridad, otro valor para fecha)
 * @return ListAdapter con las tareas a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class VerMasAdapter(private val model: TusTareasModel, private val verMas: Int): ListAdapter<Tarea, VerMasAdapter.VerMasViewHolder>(TareaComprobacionDiferncias()) {

    /**
     * View holder que almcacena las variables de cada elemento de la lista.
     *
     * @param itemView La vista de un elemento de la lista.
     * @return RecyclerView.ViewHolder con las variables de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class VerMasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTarea: TextView = itemView.findViewById(R.id.nombreTarea)
        val otroCampo: TextView = itemView.findViewById(R.id.otroCampo)
        val clickable: MaterialCardView = itemView.findViewById(R.id.clickable)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    /**
     * Inflar el contenido de la vista
     *
     * @param parent El grupo de vistas padre.
     * @param viewType El tipo de vista.
     * @return TareaViewHolder con la vista inflada.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerMasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_ver_mas, parent, false)
        return VerMasViewHolder(view)
    }

    /**
     * Sobreescritura de valores de cada elemento de la lista.
     *
     * @param holder El view holder de cada elemento de la lista.
     * @param posicion La posición de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onBindViewHolder(holder: VerMasViewHolder, posicion: Int) {
        val objectoActual = getItem(posicion)
        holder.nombreTarea.text = objectoActual.nombre
        // En función de la entrada carga la prioridad o la fecha en texto
        if (verMas == 1) {
            holder.otroCampo.text = holder.itemView.context.getString(objectoActual.prioridad.labelRes())
        }
        else {
            holder.otroCampo.text = DateHelper.timestampToString(objectoActual.fechaLimite)
        }
        // Controla el checkbox
        holder.clickable.setOnClickListener {
            try {
                it.findNavController().navigate(VerMasFragmentDirections.actionVerMasFragmentToTareaDetallesFragment(objectoActual.id))
            }
            catch (_: Exception) {
                Snackbar.make(it, it.context.getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }
        }
        // Gestiona el estado de la tarea
        // Comprueba el estado
        if (objectoActual.estado == Estado.COMPLETADA) {
            holder.checkbox.isChecked = true
        }
        else {
            holder.checkbox.isChecked = false
        }
        // Actualiza el click en consecuencia
        holder.checkbox.setOnClickListener {
            model.verMas.actualizarEstado(objectoActual, holder.checkbox.isChecked)
        }

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
