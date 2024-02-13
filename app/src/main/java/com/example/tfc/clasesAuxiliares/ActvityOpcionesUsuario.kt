package com.example.tfc.clasesAuxiliares


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tfc.ActivityLogin
import com.example.tfc.R
import com.example.tfc.SQLite.DatabaseHelper


class ActvityOpcionesUsuario : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            //"Infla" el layout personalizado
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_opciones_usuario, null)
            val db = DatabaseHelper(it)

            val usuariosMaximos=1
            // Configura los botones
            val btnCrearUser = view.findViewById<Button>(R.id.btnCrearUser)
            btnCrearUser.setOnClickListener {
                val totalUsuarios = db.contarUsuarios()
                if (totalUsuarios >= usuariosMaximos) {
                    Toast.makeText(requireContext(), "Solo puede haber un usuario registrado", Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(context, ActivityLogin::class.java)
                    startActivity(intent)
                    dialog?.dismiss()
                }
            }



            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    //***************PARA CAMBIAR DE USUARIO

    /* val btnCambiarUser = view.findViewById<Button>(R.id.btnCambiarUser)

     btnCambiarUser.setOnClickListener {
         val totalUsuarios = db.contarUsuarios()
         if(totalUsuarios ==0) {
             Toast.makeText(requireContext(), "No hay usuarios creados", Toast.LENGTH_LONG).show()
                         }
     }*/
}
