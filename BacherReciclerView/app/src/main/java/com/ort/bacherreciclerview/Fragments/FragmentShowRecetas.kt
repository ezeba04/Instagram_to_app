package com.ort.bacherreciclerview.Fragments

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.ort.bacherreciclerview.Clases.Recetas
import com.ort.bacherreciclerview.R
import java.io.InputStream
import java.net.URL
import kotlinx.android.synthetic.main.activity_main.*


lateinit var v2: View
lateinit var recetaSeleccionada: Recetas
lateinit var ShowNombreReceta: TextView
lateinit var ShowDescripcionReceta: TextView
lateinit var ShowImgReceta: ImageView

var changeFragment: Boolean = false

class FragmentShowRecetas : Fragment() {

    override fun onStart() {
        super.onStart()
        recetaSeleccionada = FragmentShowRecetasArgs.fromBundle(arguments!!).recetas //<-Recetas

        ShowNombreReceta.text = recetaSeleccionada.nombre
        ShowDescripcionReceta.text = recetaSeleccionada.descripcion

        Glide.with(requireContext()).load(recetaSeleccionada.urlImage).centerInside().into(ShowImgReceta)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v2 = inflater.inflate(R.layout.fragment_show_recetas, container, false)
        ShowNombreReceta = v2.findViewById(R.id.show_nombre_receta)
        ShowDescripcionReceta = v2.findViewById(R.id.show_descripcion_receta)
        ShowImgReceta = v2.findViewById(R.id.show_img_receta)

        setHasOptionsMenu(true)

        return v2
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = when(item.itemId) {

            R.id.action_edit -> {
                if(recetaSeleccionada.creatorID == FirebaseAuth.getInstance().currentUser?.uid.toString()) {
                    Snackbar.make(v2, "edit", Snackbar.LENGTH_SHORT).show()
                    val actionShowtoEdit =
                        FragmentShowRecetasDirections.actionFragmentShowRecetasToFragmentEditRecetas(
                            recetaSeleccionada
                        )
                    v2.findNavController().navigate(actionShowtoEdit)
                }
                else{
                    Snackbar.make(v2, "Usted debe haber creado la receta para editarla", Snackbar.LENGTH_SHORT).show()
                }
            }

            R.id.action_delete -> {
                if(recetaSeleccionada.creatorID == FirebaseAuth.getInstance().currentUser?.uid.toString()) {
                    Snackbar.make(v2, "delete", Snackbar.LENGTH_SHORT).show()
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Esta seguro de que desa borrar la receta?")

                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        db.collection("Recetas").document(recetaSeleccionada.firebaseID)
                            .delete()
                            .addOnSuccessListener {
                                Snackbar.make(v2,"Receta eliminada", Snackbar.LENGTH_SHORT).show()
                                //changeFragment = true
                                val actionShowToRecicler = FragmentShowRecetasDirections.actionFragmentShowRecetasToFragmentRecicler()
                                v2.findNavController().navigate(actionShowToRecicler)
                            }

                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->

                    }
                    builder.show()

                    if(changeFragment == true){
                        val actionShowToRecicler = FragmentShowRecetasDirections.actionFragmentShowRecetasToFragmentRecicler()
                        v2.findNavController().navigate(actionShowToRecicler)
                    }
                    else{}

                }
                else{
                    Snackbar.make(v2, "Usted debe haber creado la receta para borrarla", Snackbar.LENGTH_SHORT).show()
                }
            }

            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }

}