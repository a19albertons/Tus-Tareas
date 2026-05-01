package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.modelos.Etiqueta
import com.google.android.material.snackbar.Snackbar

/**
 * Clase que gestiona el adaptador de etiquetas presentes.
 *
 * @param etiquetaEliminada Función que se ejecuta al eliminar una etiqueta.
 * @return ListAdapter con las etiquetas presentes a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ListaEtiquetasPresentesAdapter(private val etiquetaEliminada: (List<Etiqueta>) -> Unit) : ListAdapter<Etiqueta, ListaEtiquetasPresentesAdapter.EtiquetaViewHolder>(EtiquetaDiferenciasComprobacion()) {
    /**
     * View holder que almcacena las variables de cada elemento de la lista.
     *
     * @param itemView La vista de un elemento de la lista.
     * @return RecyclerView.ViewHolder con las variables de cada elemento de la lista.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class EtiquetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEtiqueta : TextView = itemView.findViewById(R.id.nombreEtiqueta)
        val eliminarEtiqueta : TextView = itemView.findViewById(R.id.eliminarEtiqueta)
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lista_etiquetas_presentes, parent, false)
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
        val objetoActual = getItem(posicion)
        holder.nombreEtiqueta.text = objetoActual.nombre
        holder.eliminarEtiqueta.setOnClickListener {
            // Aplicamos la eliminación de la etiqueta
            val etiquetas = currentList.toMutableList()
            etiquetas.remove(objetoActual)
            etiquetaEliminada(etiquetas)
            Snackbar.make(it, it.context.getString(R.string.etiqueta_eliminada), Snackbar.LENGTH_SHORT).show()
            submitList(etiquetas)
        }
    }

    /**
     * Clase que gestiona las diferencias entre dos etiquetas.
     *
     * @return DiffUtil.ItemCallback con las diferencias entre dos listas de etiquetas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class EtiquetaDiferenciasComprobacion : DiffUtil.ItemCallback<Etiqueta>() {
        /**
         * Comprobar si el contenido de dos etiquetas es el mismo.
         *
         * @param viejaEtiqueta La etiqueta antigua.
         * @param nuevaEtiqueta La etiqueta nueva.
         * @return true si el contenido de las etiquetas es el mismo, false en caso contrario.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        override fun areContentsTheSame(viejaEtiqueta: Etiqueta, nuevaEtiqueta: Etiqueta): Boolean {
            return viejaEtiqueta == nuevaEtiqueta
        }

        /**
         * Comprobar si dos etiquetas son la misma.
         *
         * @param viejaEtiqueta La etiqueta antigua.
         * @param nuevaEtiqueta La etiqueta nueva.
         * @return true si las etiquetas son la misma, false en caso contrario.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        override fun areItemsTheSame(viejaEtiqueta: Etiqueta, nuevaEtiqueta: Etiqueta): Boolean {
            return viejaEtiqueta.id == nuevaEtiqueta.id
        }
    }
}