package com.example.tustareas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tustareas.R
import com.example.tustareas.fragmentos.ListarEtiquetasFragmentDirections
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Proyecto
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

/**
 * Clase que gestiona el adaptador de etiquetas.
 *
 * @return List Adapter con las etiquetas a mostrar.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class EtiquetasAdapter(): ListAdapter<Etiqueta, EtiquetasAdapter.EtiquetaViewHolder>(
    EtiquetaComprobacionDiferencias()
) {
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
        val objetoActual = getItem(posicion)
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
     * Clase que gestiona las diferencias entre dos etiquetas.
     * Necesario para poder usar un list adapter
     *
     * @return DiffUtil.ItemCallback con las diferencias entre dos listas de etiquetas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    class EtiquetaComprobacionDiferencias : DiffUtil.ItemCallback<Etiqueta>() {
        /**
         * Comprobar si el contenido de dos etiquetas es el mismo.
         *
         * @param viejaEtiqueta La etiqueta antigua.
         * @param nuevaEtiqueta La etiqueta nueva.
         * @return true si el contenido de las etiquetas es el mismo, false en caso contrario.
         * @author Alberto Noceda <a19albertons@iessanclemenet.net>
         */
        override fun areContentsTheSame(viejaEtiqueta: Etiqueta, nuevaEtiqueta: Etiqueta): Boolean {
            return viejaEtiqueta == nuevaEtiqueta
        }


        /**
         * Comprobar si el id de dos etiquetas es el mismo.
         *
         * @param viejaEtiqueta La etiqueta antigua.
         * @param nuevaEtiqueta La etiqueta nueva.
         * @return true si el id de las etiquetas es el mismo, false en caso contrario.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        override fun areItemsTheSame(viejaEtiqueta: Etiqueta, nuevaEtiqueta: Etiqueta): Boolean {

            return viejaEtiqueta.id == nuevaEtiqueta.id
        }
    }


}