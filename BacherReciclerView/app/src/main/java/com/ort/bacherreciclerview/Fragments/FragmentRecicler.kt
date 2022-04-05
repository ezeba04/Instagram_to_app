package com.ort.bacherreciclerview.Fragments

import android.app.AlertDialog
import android.app.SearchManager
import androidx.appcompat.widget.SearchView
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ort.bacherreciclerview.Adapters.RecetasListAdapter
import com.ort.bacherreciclerview.Clases.Recetas
import com.ort.bacherreciclerview.R
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*
import kotlin.collections.ArrayList

val db = Firebase.firestore

lateinit var recetaAdd : Recetas

lateinit var txtReciclerProgressBar : TextView
lateinit var progressBarRecicler : ProgressBar

lateinit var userMenuItem: MenuItem
lateinit var logOutItem: MenuItem
lateinit var searchItem: MenuItem

//lateinit var searchView: SearchView

public lateinit var auth: FirebaseAuth

class FragmentRecicler : Fragment() {

    //Declarar variables
    lateinit var v: View


    lateinit var reciclerRecetas: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    var RecetasList : MutableList<Recetas> = ArrayList<Recetas>()
    private lateinit var recetaListAdapter: RecetasListAdapter

    var FilterRecetasList: MutableList<Recetas> = ArrayList<Recetas>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_recicler, container, false)
        reciclerRecetas = v.findViewById(R.id.recicler_recetas)
        txtReciclerProgressBar = v.findViewById(R.id.txt_progressBar)
        progressBarRecicler = v.findViewById(R.id.progressBar)

        txtReciclerProgressBar.visibility = View.VISIBLE
        progressBarRecicler.visibility = View.VISIBLE

        auth = Firebase.auth

        setHasOptionsMenu(true)

        return v
    }


    override fun onStart() {
        super.onStart()
        RecetasList.clear()

        db.collection("Recetas")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (recetasFound in snapshot) {
                        recetaAdd = recetasFound.toObject<Recetas>()
                        recetaAdd.firebaseID = recetasFound.id
                        RecetasList.add(recetaAdd)
                        Log.d(TAG, "ID: ${recetaAdd.creatorID}")
                    }

                    txtReciclerProgressBar.visibility = View.INVISIBLE
                    progressBarRecicler.visibility = View.INVISIBLE

                    //darle un tamano fijo al reciclerView
                    reciclerRecetas.setHasFixedSize(true)

                    //tipo de Layout que va a tener el reciclerView
                    linearLayoutManager = LinearLayoutManager(context)
                    reciclerRecetas.layoutManager = linearLayoutManager

                    FilterRecetasList.addAll(RecetasList)

                    recetaListAdapter = RecetasListAdapter(FilterRecetasList, requireContext()) { x ->
                        pasarFragment(x)
                    }
                    reciclerRecetas.adapter = recetaListAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        userMenuItem = menu.findItem(R.id.action_account)
        logOutItem = menu.findItem(R.id.action_LogOut)
        changeMenuItemsVisibility()

        searchItem = menu.findItem(R.id.action_search)
        onSearchIconSelected()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = when(item.itemId) {

            R.id.action_add -> {
                    val currentUser = auth.currentUser

                    if(currentUser != null){ //si el usuario tiene iniciada la sesion, va al fragment
                        val actionReciclertoAdd = FragmentReciclerDirections.actionFragmentReciclerToFragmentAddRecetas()
                        v.findNavController().navigate(actionReciclertoAdd)
                    }

                    else{ //si no, alert dialog
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Debe iniciar sesion para agregar recetas")
                        builder.setPositiveButton("iniciar sesion") { dialog, which ->
                            val actionReciclertoLogin = FragmentReciclerDirections.actionFragmentReciclerToFragmentLogin()
                            v.findNavController().navigate(actionReciclertoLogin)
                        }

                        builder.setNegativeButton("cancelar") { dialog, which -> }
                        builder.show()
                    }
            }

            R.id.action_account -> {
                Snackbar.make(v, "fav", Snackbar.LENGTH_SHORT).show()
                val actionReciclertoLogin = FragmentReciclerDirections.actionFragmentReciclerToFragmentLogin()
                v.findNavController().navigate(actionReciclertoLogin)
            }

            R.id.action_LogOut -> {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Esta seguro que desea cerrar sesion?")
                builder.setPositiveButton("Cerrar sesion") { dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    changeMenuItemsVisibility()
                }

                builder.setNegativeButton("cancelar") { dialog, which -> }
                builder.show()
            }

            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }


    public fun pasarFragment(Position: Int) : Boolean {

        val pasaje = FragmentReciclerDirections.actionFragmentReciclerToFragmentShowRecetas(RecetasList[Position])
        v.findNavController().navigate(pasaje)

        return true
    }

    private fun changeMenuItemsVisibility(){
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser != null){ //si el usuario tiene iniciada la sesion
            userMenuItem.isVisible = false
            logOutItem.isVisible = true
        }
        else{ //si el usuario no inicio sesion
            userMenuItem.isVisible = true
            logOutItem.isVisible = false
        }
    }



    private fun onSearchIconSelected() {
        if (searchItem != null) {
            //val searchView = MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW as SearchView
            //val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
            val searchView = searchItem.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "query: ${query}")
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "NewText: ${newText}")
                    if(newText!!.isNotEmpty()){
                        FilterRecetasList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())

                        for(RecetaBuscada in RecetasList){
                            Log.d(TAG,"${RecetaBuscada.nombre}")
                            if(RecetaBuscada.nombre.toLowerCase(Locale.getDefault()).contains(search)){
                                FilterRecetasList.add(RecetaBuscada)
                                Log.d(TAG, "encontrada: ${RecetaBuscada.nombre}")
                            }
                        }
//                        FilterRecetasList.forEach {
//                            Log.d(TAG, "entre al for")
//                            if(it.nombre.toLowerCase(Locale.getDefault()).contains(search)){
//                                FilterRecetasList.add(it)
//                                Log.d(TAG, "encontrada: ${it.nombre}")
//                            }
//                        }
                        reciclerRecetas.adapter!!.notifyDataSetChanged()
                    }
                    else{
                        FilterRecetasList.clear()
                        FilterRecetasList.addAll(RecetasList)
                        reciclerRecetas.adapter!!.notifyDataSetChanged()
                    }
                    return false
                }
            })
        }
    }
}