package com.example.tfc.clasesAuxiliares


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tfc.ActivityLogin
import com.example.tfc.R
import com.example.tfc.SQLite.DatabaseHelper


class DialogOpcionesUsuario : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            //"Infla" el layout personalizado con solo dos botones
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_opciones_usuario, null)


            initComponentes(view)
            initListeners()


            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initListeners(){
        btnCrearUser.setOnClickListener {
            val totalUsuarios = db.contarUsuarios()
            //Si el número de usuarios maximo se alcanzó no nos permitirá crear nuevos usuarios
            if (totalUsuarios >= 9) {  //9 sería el número de usuarios máximos
                Toast.makeText(
                    requireContext(),
                    "Solo puede haber nueve usuarios registrados",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //Si no se llega al máximo de usuarios nos permite crear más
                val intent = Intent(context, ActivityLogin::class.java)
                startActivity(intent)
                dialog?.dismiss()
            }
        }

        btnCambiarUser.setOnClickListener {
            val totalUsuarios = db.contarUsuarios()
            if (totalUsuarios == 0) {
                //Si no hay usuarios no nos permitirá crearlo
                Toast.makeText(requireContext(), "No hay usuarios creados", Toast.LENGTH_LONG).show()
            } else {
                //Si hay usuarios
                val listaUsuarios = db.getUsuarios() //Obtienemos la lista de usuarios

                //Mapeamos los nombres JUNTO a sus IDs
                val nombresUsuario = listaUsuarios.map { it.nombreUsuario }.toTypedArray()
                val idsUsuarios = listaUsuarios.map { it.id }.toTypedArray()
                //Creamos la ventana de dialogo a través de AlertDialog
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Selecciona un usuario")

                builder.setItems(nombresUsuario) { dialog, which ->
                    val idUsuarioSeleccionado = idsUsuarios[which]
                    //Seleccionamos el usuarios con una función que devuelve directamente un boolean
                    val seleccion= db.seleccionUsuario(idUsuarioSeleccionado)

                    if (seleccion) {
                        Toast.makeText(requireContext(), "Usuario seleccionado: ${nombresUsuario[which]}", Toast.LENGTH_SHORT).show()
                        //Este dismiss cierra la lista de usuarios Y las opciones de usuarios
                        cerrarDialog()
                    } else {
                        Toast.makeText(requireContext(), "Error al seleccionar usuario", Toast.LENGTH_SHORT).show()
                    }
                }

                builder.setNegativeButton("Atrás") { dialog, which ->
                    //Este dissmss solo cierra la lista de usuarios
                    dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }
    //Necesitamos esta función para cerrar las opciones al seleccionar usuarios
    private fun cerrarDialog(){
        dismiss()
    }

    private fun initComponentes(vista: View){
        btnCambiarUser=vista.findViewById(R.id.btnCambiarUser)
        btnCrearUser=vista.findViewById(R.id.btnCrearUser)
        db=DatabaseHelper(vista.context)
    }

    private lateinit var btnCrearUser: Button
    private lateinit var btnCambiarUser: Button
    private lateinit var db : DatabaseHelper

}





