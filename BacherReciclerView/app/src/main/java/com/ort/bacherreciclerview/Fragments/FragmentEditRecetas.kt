package com.ort.bacherreciclerview.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.ort.bacherreciclerview.Clases.Recetas
import com.ort.bacherreciclerview.R

lateinit var v4: View
lateinit var btnEditar : Button
lateinit var txtTituloEditar : TextView
lateinit var edtNombreSet : EditText
lateinit var edtDescripcionSet : EditText
lateinit var edtURLSet : EditText

lateinit var recetaAeditar : Recetas

class FragmentEditRecetas : Fragment() {
    override fun onStart() {
        super.onStart()

        recetaAeditar = FragmentShowRecetasArgs.fromBundle(arguments!!).recetas

        edtNombreSet.setText(recetaAeditar.nombre)
        edtDescripcionSet.setText(recetaAeditar.descripcion)
        edtURLSet.setText(recetaAeditar.urlImage)

        txtTituloEditar.text = "Aca podes editar la receta!"

        btnEditar.setOnClickListener {

            if (edtNombreSet.text.toString() != "" && edtDescripcionSet.text.toString() != "" && edtURLSet.text.toString() != "") {
                // Add a new document with a generated id.
                val data = hashMapOf(
                    "nombre" to edtNombreSet.text.toString(),
                    "descripcion" to edtDescripcionSet.text.toString(),
                    "urlImage" to edtURLSet.text.toString(),
                    "creatorID" to FirebaseAuth.getInstance().currentUser?.uid.toString()
                )
                Log.d(ContentValues.TAG, "nombre: ${edtNombreSet.text.toString()}")

                db.collection("Recetas").document(recetaAeditar.firebaseID)
                    .set(data)
                    .addOnSuccessListener { documentReference ->
                        Snackbar.make(v4, "Receta editada", LENGTH_SHORT).show()
                        val actionEditToRecicler = FragmentEditRecetasDirections.actionFragmentEditRecetasToFragmentRecicler()
                        v4.findNavController().navigate(actionEditToRecicler)
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }

            } else {
                Snackbar.make(v4, "ingrese todos los datos de su producto", Snackbar.LENGTH_SHORT)
                    .show()
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v4 = inflater.inflate(R.layout.fragment_add_recetas, container, false)

        btnEditar = v4.findViewById(R.id.btn_save_changes)
        txtTituloEditar = v4.findViewById(R.id.txt_cuenta)
        edtNombreSet = v4.findViewById(R.id.user_login)
        edtDescripcionSet = v4.findViewById(R.id.password_signUp)
        edtURLSet = v4.findViewById(R.id.password_login)

        return v4
    }
}