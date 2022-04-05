package com.ort.bacherreciclerview.Fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.findNavController
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ort.bacherreciclerview.R

lateinit var v6: View
lateinit var txtSignUp : TextView
lateinit var edtUserSingUp: EditText
lateinit var edtPasswordSingUp: EditText
lateinit var edtPassword2SingUp: EditText
lateinit var btnSaveChanges: Button
lateinit var progressBarSignUp: ProgressBar

lateinit var uidSignUp: String

lateinit var authSignUP: FirebaseAuth

class FragmentSignUp : Fragment() {

    override fun onStart() {
        super.onStart()

        btnSaveChanges.setOnClickListener{
            if (edtUserSingUp.text.toString() != "" && edtPasswordSingUp.text.toString() != "" && edtPassword2SingUp.text.toString() != ""){
                if (edtPasswordSingUp.text.toString() == edtPassword2SingUp.text.toString()){
                    if (edtUserSingUp.text.toString().contains("@")) {
                        createUser()
                    }
                    else{
                        Snackbar.make(v6,"Ingrese un mail valido para el usuario",LENGTH_SHORT).show()
                    }
                }
                else{
                    Snackbar.make(v6,"Debe ingresar la misma contraseña en ambos campos",LENGTH_SHORT).show()
                }
            }
            else{
                Snackbar.make(v6,"Ingrese usuario y contraseña",LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v6 = inflater.inflate(R.layout.fragment_sign_up, container, false)

        txtSignUp = v6.findViewById(R.id.txt_singUp)
        edtUserSingUp = v6.findViewById(R.id.user_signUp)
        edtPasswordSingUp = v6.findViewById(R.id.password_signUp)
        edtPassword2SingUp = v6.findViewById(R.id.password2_signUp)
        btnSaveChanges = v6.findViewById(R.id.btn_save_changes)
        progressBarSignUp = v6.findViewById(R.id.progressBar_SignUp)

        progressBarSignUp.visibility = View.INVISIBLE

        authSignUP = Firebase.auth

        return v6
    }

    private fun createUser(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtUserSingUp.text.toString(), edtPasswordSingUp.text.toString())
            .addOnCompleteListener {
                progressBarSignUp.visibility = View.VISIBLE
            if (it.isSuccessful) {
                progressBarSignUp.visibility = View.INVISIBLE

                val actionSignUptoRecicler = FragmentSignUpDirections.actionFragmentSignUpToFragmentRecicler()
                v6.findNavController().navigate(actionSignUptoRecicler)
            }
            else {
                Snackbar.make(v6,"La autenticacion fallo",LENGTH_SHORT).show()
                progressBarSignUp.visibility = View.INVISIBLE
            }
        }
    }
}
