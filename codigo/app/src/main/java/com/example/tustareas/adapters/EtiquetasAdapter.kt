package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.fragmentos.ListarEtiquetasFragmentDirections
import com.example.tustareas.modelos.Etiqueta
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

/**
 * Clase que gestiona el adaptador de etiquetas.
 *
 * @param etiquetas Lista de etiquetas a mostrar.
 * @return RecyclerView.Adapter con las etiquetas a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class EtiquetasAdapter(private val etiquetas: List<Etiqueta>): RecyclerView.Adapter<EtiquetasAdapter.EtiquetaViewHolder>() {
    /**
     * View holder que almcacena las variables de cada elemento de la lista.
     *
     * @param itemView La vista de un elemento de la lista.
     * @return RecyclerView.ViewHolder con las variables de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class EtiquetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEtiqueta : TextView = itemView.findViewById(R.id.nombreEtiqueta)
        val descripcionEtiqueta : TextView = itemView.findViewById(R.id.descipcionEtiqueta)
        val clickable : MaterialCardView = itemView.findViewById(R.id.clickable)
    }

    /**
     * Inflar el contenido de la vista
     *
     * @param parent El grupo de vistas padre.
     * @param viewType El tipo de vista.
     * @return EtiquetaViewHolder con la vista inflada.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtiquetaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_etiquetas, parent, false)
        return EtiquetaViewHolder(view)
    }

    /**
     * Sobreescritura de valores de cada elemento de la lista.
     *
     * @param holder El view holder de cada elemento de la lista.
     * @param posicion La posición de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onBindViewHolder(holder: EtiquetaViewHolder, posicion: Int) {
        val objetoActual = etiquetas[posicion]
        holder.nombreEtiqueta.text = objetoActual.nombre
        holder.descripcionEtiqueta.text = objetoActual.descripcion
        // Navegamos a la pantalla de detalles de la etiqueta
        holder.clickable.setOnClickListener {
            try {
                it.findNavController().navigate(ListarEtiquetasFragmentDirections.actionListarEtiquetasFragmentToEtiquetaDetallesFragment(objetoActual.id))
            }
            catch (_: Exception) {
                Snackbar.make(it, it.context.getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Devuelve el tamaño de la lista.
     *
     * @return El tamaño de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun getItemCount(): Int {
        return etiquetas.size
    }


}