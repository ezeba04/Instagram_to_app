package com.ort.bacherreciclerview.Adapters

import android.content.Context
import android.view.KeyCharacterMap.load
import android.view.LayoutInflater
import android.view.PointerIcon.load
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ort.bacherreciclerview.Clases.Recetas
//import com.ort.bacherreciclerview.Fragments.FragmentRecicler.RecetasList
//import com.ort.bacherreciclerview.Fragments.v
import com.ort.bacherreciclerview.R
import kotlinx.coroutines.withContext
import java.lang.System.load
import java.util.ServiceLoader.load

class RecetasListAdapter(
        private var listaRecetas: MutableList<Recetas>,
        val context: Context,
        val pasarFragment: (Int) -> Boolean): RecyclerView.Adapter<RecetasListAdapter.RecetasHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetasListAdapter.RecetasHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recetas, parent, false)
        return (RecetasHolder(view))
    }

    override fun getItemCount(): Int {

        return listaRecetas.size
    }

    fun setData(newData: ArrayList<Recetas>) {
        this.listaRecetas = newData
        this.notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecetasHolder, position: Int) {

        holder.setNameReceta(listaRecetas[position].nombre)
        holder.setDescripcionReceta(listaRecetas[position].descripcion)

        Glide.with(context).load(listaRecetas[position].urlImage).centerInside().into(holder.getImageView())

        holder.getCardLayout().setOnClickListener() {
            //para pasar de este fragment al otro
            pasarFragment(position)
        }
    }


    class RecetasHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View

        init {
            this.view = v
        }

        fun setNameReceta(name: String) {
            val txtNameReceta: TextView = view.findViewById(R.id.txt_name_item)
            txtNameReceta.text = name
        }

        fun setDescripcionReceta(descripcion: String) {
            val txtDescripcionReceta: TextView = view.findViewById(R.id.txt_descripcion_item)
            txtDescripcionReceta.text = descripcion
            //txtDescripcionReceta.text = descripcion.subSequence(0, 20).toString() + "..."//Para que solo muestre una determinada cantidad de caracteres
        }


        fun getImageView () : ImageView {
            return view.findViewById(R.id.img_item)
        }

        fun getCardLayout(): CardView {
            return view.findViewById(R.id.card_package_item)
        }
    }
}