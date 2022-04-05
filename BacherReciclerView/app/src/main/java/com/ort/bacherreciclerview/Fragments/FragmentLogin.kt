package com.ort.bacherreciclerview.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ort.bacherreciclerview.R
import kotlinx.android.synthetic.main.fragment_login.*

lateinit var v5 : View
lateinit var txtCuenta: TextView
lateinit var txtNoUsuario: TextView
lateinit var txtRegistrarse: TextView
lateinit var edtUserLogin: EditText
lateinit var edtPasswordLogin: EditText
lateinit var btnIniciarSesion: Button
lateinit var ProgressBarLogIn: ProgressBar

lateinit var uidLogIn: String

lateinit var authLogin: FirebaseAuth

class FragmentLogin : Fragment() {
    override fun onStart() {
        super.onStart()
        btnIniciarSesion.setOnClickListener {
            if(edtUserLogin.text.toString() != "" && edtPasswordLogin.text.toString() != ""){
                if (edtUserLogin.text.toString().contains("@")) {
                    loginUser()
                }
                else{
                    Snackbar.make(v5,"Ingrese un mail para el usuario", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Snackbar.make(v5,"Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        txtRegistrarse.setOnClickListener {
            val actionLogintoSignUp = FragmentLoginDirections.actionFragmentLoginToFragmentSignUp()
            v5.findNavController().navigate(actionLogintoSignUp)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v5 = inflater.inflate(R.layout.fragment_login, container, false)

        txtCuenta = v5.findViewById(R.id.txt_cuenta)
        txtNoUsuario = v5.findViewById(R.id.txt_no_usuario)
        txtRegistrarse = v5.findViewById(R.id.txt_registrarse)
        edtUserLogin = v5.findViewById(R.id.user_login)
        edtPasswordLogin = v5.findViewById(R.id.password_login)
        btnIniciarSesion = v5.findViewById(R.id.btn_iniciar_sesion)
        ProgressBarLogIn = v5.findViewById(R.id.progressBar_LogIn)

        ProgressBarLogIn.visibility = View.INVISIBLE

        authLogin = Firebase.auth

        return v5
    }

    private fun loginUser(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtUserLogin.text.toString(), edtPasswordLogin.text.toString())
            .addOnCompleteListener {
                ProgressBarLogIn.visibility = View.VISIBLE
            if (it.isSuccessful) {
                val user = authLogin.currentUser
                uidLogIn = user?.uid.toString()
                ProgressBarLogIn.visibility = View.INVISIBLE

                val actionLogintoRecicler = FragmentLoginDirections.actionFragmentLoginToFragmentRecicler()
                v5.findNavController().navigate(actionLogintoRecicler)
            }
            else {
                Snackbar.make(v5,"La autenticacion fallo", Toast.LENGTH_SHORT).show()
                ProgressBarLogIn.visibility = View.INVISIBLE
            }
        }
    }
}