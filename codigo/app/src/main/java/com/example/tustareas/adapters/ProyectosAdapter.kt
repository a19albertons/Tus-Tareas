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

/**
 * Clase que gestiona el adaptador de proyectos.
 *
 * @param proyectos Lista de proyectos a mostrar.
 * @return RecyclerView.Adapter con los proyectos a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ProyectosAdapter(private val proyectos: List<Proyecto>): RecyclerView.Adapter<ProyectosAdapter.ProyectoViewHolder>() {
    /**
     * View holder que almcacena las variables de cada elemento de la lista.
     *
     * @param itemView La vista de un elemento de la lista.
     * @return RecyclerView.ViewHolder con las variables de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class ProyectoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProyecto : TextView = itemView.findViewById(R.id.nombreProyecto)
        val fechaFin : TextView = itemView.findViewById(R.id.fechaFin)
        val clickable : MaterialCardView = itemView.findViewById(R.id.clickable)
    }

    /**
     * Inflar el contenido de la vista
     *
     * @param parent El grupo de vistas padre.
     * @param viewType El tipo de vista.
     * @return TareaViewHolder con la vista inflada.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProyectoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_proyectos, parent, false)
        return ProyectoViewHolder(view)
    }

    /**
     * Sobreescritura de valores de cada elemento de la lista.
     *
     * @param holder El view holder de cada elemento de la lista.
     * @param posicion La posición de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onBindViewHolder(holder: ProyectoViewHolder, posicion: Int) {
        val objectoActual = proyectos[posicion]
        holder.nombreProyecto.text = objectoActual.nombre
        holder.fechaFin.text = DateHelper.timestampToString(objectoActual.fechaFin)
        // Navegación a detalles del proyecto
        holder.clickable.setOnClickListener {
            try {
                it.findNavController().navigate(ListarProyectosFragmentDirections.actionListarProyectosFragmentToProyectoDetallesFragment(objectoActual.id))
            }
            catch (_: Exception) {
                Snackbar.make(it,it.context.getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    /**
     * Devuelve el tamaño de la lista.
     *
     * @return Int con el tamaño de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun getItemCount(): Int {
        return proyectos.size
    }

}