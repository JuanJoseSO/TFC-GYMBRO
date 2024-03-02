package com.example.tfc.infoUser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tfc.ActivityLogin
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.adapters.AdapterHistorial
import com.example.tfc.clasesAuxiliares.clasesBase.Sesion
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.HistorialDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb

class FragmentInfoUser : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info_user, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponentes()
        initListeners()
        datosUsuario()
    }

    private fun initListeners() {
        btnHistorial.setOnClickListener {
           cargarHistorial()
        }
        btnCambiarUsuario.setOnClickListener{
            mostrarListaUsurios()
        }
        btnModificarUsuario.setOnClickListener{
            modificarUsuario()
        }
        btnEliminarUsuario.setOnClickListener{
            eliminarUsuario()
        }
    }

    private fun modificarUsuario() {
        val intent= Intent(requireContext(),ActivityLogin::class.java)
        intent.putExtra("Modificar",true)
        startActivity(intent)

    }

    private fun eliminarUsuario() {
        //Eliminarmos la lista de usuarios
        userDb.eliminarUsuario()
        //Si no hay usuarios,vamos al login
        if(userDb.contarUsuarios()<=0){
            val intent= Intent(requireContext(),ActivityLogin::class.java)
            startActivity(intent)
        }else{
            //Si hay,seleccionamos
            mostrarListaUsurios()
        }

    }

    private fun cargarHistorial() {/*Lo que hacemos en esta funcion es abrir un AlertDialog que estamos inflando con el mismo adapter que la lista de
        rutinas lo que hace que en lugar de ser una lista gris sin estilo sea una lista con un fondo y un formato individual
        para cada ejercicio,quedando mucho mas atractiva visualmente*/

        //Si hay rutinas
        val listaHistorial =
            historialDb.getSesiones(userDb.getUsuarioSeleccionado()?.id!!)//Obtienemos la lista de rutinas
        //Si no esta vacia,la mostramos
        if (listaHistorial.isNotEmpty()) {
            val adapter = AdapterHistorial(requireContext(), listaHistorial) //Creamos el adapter
            val layoutRutina = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_listas, null)//inflamos la rutina
            layoutRutina.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.background
            )//Le cambiamos el fondo
            val lvSesiones =
                layoutRutina.findViewById<ListView>(R.id.lvListas)//Rlacionamos con nuestro layout
            lvSesiones.adapter = adapter //Relacionamos con el adapter

            //Creamos el Alerdialog y le damos el estilo y un boton atrás
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(layoutRutina)
            builder.setNegativeButton("Atrás") { dialog, _ -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.show()

            //Configuramos el listener al ciclar en un item de la lista y la cerramos
            lvSesiones.setOnItemClickListener { _, _, position, _ ->
                historialDb.getSesion(position)
                enviarIdSesion(listaHistorial, position)
                dialog.dismiss()
            }
        }
        //Si esta vacia se lo indicamos al usuario
        else Toast.makeText(requireContext(), "Aún no has entrenado", Toast.LENGTH_LONG).show()
    }

    //Funcion para mostrar una lista de usuarios en un alertDialog al pulsar el botón de cambiar
    private fun mostrarListaUsurios() {
        val totalUsuarios = userDb.contarUsuarios()
        if (totalUsuarios != 0){
            //Si hay usuarios
            val listaUsuarios = userDb.getUsuarios() //Obtienemos la lista de usuarios

            //Mapeamos los nombres JUNTO a sus IDs
            val nombresUsuario = listaUsuarios.map { it.nombreUsuario }.toTypedArray()
            val idsUsuarios = listaUsuarios.map { it.id }.toTypedArray()
            //Creamos la ventana de dialogo a través de AlertDialog
            val builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Selecciona un usuario")

            builder.setItems(nombresUsuario) { _, which ->
                val idUsuarioSeleccionado = idsUsuarios[which]
                //Seleccionamos el usuarios con una función que devuelve directamente un boolean
                val seleccion = userDb.seleccionUsuario(idUsuarioSeleccionado)

                if (seleccion) {
                    Toast.makeText(
                        requireContext(),
                        "Usuario seleccionado: ${nombresUsuario[which]}",
                        Toast.LENGTH_SHORT
                    ).show()
                    datosUsuario()
                } else {
                    Toast.makeText(
                        requireContext(), "Error al seleccionar usuario", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            builder.setNegativeButton("Atrás") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        }
    }


    private fun enviarIdSesion(lvSesiones: List<Sesion>, position: Int) {
        val fragmentHistorial = FragmentHistorial().apply {
            //Similar a un intent,pero así se configura la navegación entre fragments
            arguments = Bundle().apply {
                putInt("id", lvSesiones[position].idHistorial)
            }
        }
        //Navegamos al fragmento destino,fragmentHistorial
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.contenedor_fragments, fragmentHistorial).addToBackStack(null).commit()
    }

    private fun datosUsuario() {
        val usuario = userDb.getUsuarioSeleccionado()
        if (usuario != null) {
            tvNombre.text = usuario.nombreUsuario
            tvAltura.text = usuario.altura.toString()
            tvPeso.text = usuario.peso.toString()
            tvIMC.text = String.format("%.2f", usuario.imc)
            tvEdad.text = usuario.objetivoDiario.toString()
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    private fun initComponentes() {
        tvNombre = requireView().findViewById(R.id.tvNombre)
        tvAltura = requireView().findViewById(R.id.tvAltura)
        tvPeso = requireView().findViewById(R.id.tvPeso)
        tvIMC = requireView().findViewById(R.id.tvIMC)
        tvEdad = requireView().findViewById(R.id.tvObjetivoDiario)
        btnHistorial = requireView().findViewById(R.id.btnHistorial)
        btnModificarUsuario = requireView().findViewById(R.id.btnModificarUsuario)
        btnCambiarUsuario = requireView().findViewById(R.id.btnCambiarUsuario)
        btnEliminarUsuario = requireView().findViewById(R.id.btnEliminarUsuario)
        db = DatabaseHelper(requireContext())
        userDb = UserDb(db)
        historialDb = HistorialDb(db)
    }

    private lateinit var btnCambiarUsuario: AppCompatButton
    private lateinit var btnEliminarUsuario: AppCompatButton
    private lateinit var btnModificarUsuario: AppCompatButton
    private lateinit var btnHistorial: AppCompatButton
    private lateinit var tvNombre: TextView
    private lateinit var tvAltura: TextView
    private lateinit var tvPeso: TextView
    private lateinit var tvIMC: TextView
    private lateinit var tvEdad: TextView
    private lateinit var db: DatabaseHelper
    private lateinit var userDb: UserDb
    private lateinit var historialDb: HistorialDb
}