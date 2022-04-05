package com.ort.bacherreciclerview.Fragments

import android.content.ContentValues.TAG
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
import com.ort.bacherreciclerview.R

lateinit var v3: View
lateinit var btnSubir : Button
lateinit var txtTitulo : TextView
lateinit var edtNombre : EditText
lateinit var edtDescripcion : EditText
lateinit var edtURL : EditText


class FragmentAddRecetas : Fragment() {
    override fun onStart() {
        super.onStart()

        txtTitulo.text = "Escribi tus propias recetas!"

        btnSubir.setOnClickListener {

            if (edtNombre.text.toString() != "" && edtDescripcion.text.toString() != "" && edtURL.text.toString() != "") {
                // Add a new document with a generated id.
                val data = hashMapOf(
                    "nombre" to edtNombre.text.toString(),
                    "descripcion" to edtNombre.text.toString(),
                    "urlImage" to edtURL.text.toString(),
                    "creatorID" to FirebaseAuth.getInstance().currentUser?.uid.toString()
                )

                db.collection("Recetas")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                        Snackbar.make(v3,"Receta agregada",LENGTH_SHORT).show()
                        val actionAddToRecicler = FragmentAddRecetasDirections.actionFragmentAddRecetasToFragmentRecicler()
                        v3.findNavController().navigate(actionAddToRecicler)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

            } else {
                Snackbar.make(v3, "ingrese todos los datos de su producto", Snackbar.LENGTH_SHORT)
                    .show()
            }

        }
    }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            v3 = inflater.inflate(R.layout.fragment_add_recetas, container, false)

            btnSubir = v3.findViewById(R.id.btn_save_changes)
            txtTitulo = v3.findViewById(R.id.txt_cuenta)
            edtNombre = v3.findViewById(R.id.user_login)
            edtDescripcion = v3.findViewById(R.id.password_signUp)
            edtURL = v3.findViewById(R.id.password_login)

            return v3
        }
}