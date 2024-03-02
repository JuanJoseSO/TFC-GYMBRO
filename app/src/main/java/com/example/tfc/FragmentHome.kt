package com.example.tfc


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
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tfc.clasesAuxiliares.CirculosAnimados
import com.example.tfc.clasesAuxiliares.adapters.AdapterRutina
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.HistorialDb
import com.example.tfc.sqlite.sqliteMetodos.RutinaEjercicioDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb
import com.example.tfc.sqlite.sqliteMetodos.UsuarioRutinaDb
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale


class FragmentHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponentes()
        initCalendario()
        initCirculoAnimado()
        initListeners()
    }

    private fun initListeners() {
        btnCrearUsuario.setOnClickListener {
           navegarALogin()
        }
        btnEntrenar.setOnClickListener {
            mostrarListaRutinas()
        }
    }

    private fun mostrarListaRutinas() {
        /*Lo que hacemos en esta funcion es abrir un AlertDialog que estamos inflando con el mismo adapter que la lista de
         rutinas lo que hace que en lugar de ser una lista gris sin estilo sea una lista con un fondo y un formato individual
         para cada ejercicio,quedando mucho mas atractiva visualmente*/
        if (usuarioRutinaDb.getRutinaPorUsuario(userDb.getUsuarioSeleccionado()?.id!!).isEmpty()) {
            //Si no hay rutinas no nos permitirá crearlo
            Toast.makeText(requireContext(), "No hay rutinas creadas", Toast.LENGTH_LONG).show()
        } else {
            //Si hay rutinas
            val listaRutinas =
                usuarioRutinaDb.getRutinaPorUsuario(userDb.getUsuarioSeleccionado()?.id!!)//Obtienemos la lista de rutinas
            val adapter = AdapterRutina(requireContext(), listaRutinas) //Creamos el adapter
            val layoutRutina = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_listas, null)//inflamos la rutina
            layoutRutina.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background)//Le cambiamos el fondo
            val lvRutina =
                layoutRutina.findViewById<ListView>(R.id.lvListas)//Rlacionamos con nuestro layout
            lvRutina.adapter = adapter //Relacionamos con el adapter
            lvRutina.setOnItemClickListener { _, _, position, _ ->
                //Volvemos a la activityPrincipal
                val intent = Intent(requireContext(), ActivityEntrenamiento::class.java)
                intent.putExtra("idRutina",listaRutinas[position].id)
                startActivity(intent)
            }
            //Creamos el Alerdialog y le damos el estilo y un boton atrás
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(layoutRutina)
            builder.setNegativeButton("Atrás") { dialog, _ -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun navegarALogin() {
        if(userDb.contarUsuarios()<9){
            val intent=Intent(requireContext(),ActivityLogin::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(), "Usuarios máximos creados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initCalendario() {
        //Creamos un objeto calendario y le damos formato europeo
        val calendario: Calendar = Calendar.getInstance()
        val formatearFecha = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale("es", "ES"))
        var fechaFormateada = formatearFecha.format(calendario.time)
        //Solo la primera letra será mayuscula
        if (fechaFormateada.isNotEmpty()) {
            fechaFormateada = fechaFormateada.substring(0, 1)
                .uppercase(Locale.getDefault()) + fechaFormateada.substring(1)
        }
        //Mostramos la fecha
        tvFecha.text = fechaFormateada
    }
    private fun initCirculoAnimado(){
        userDb.getUsuarioSeleccionado()?.let {
            circulosAnimados.rellenarCirculo(historialDb.getTiempoDiarioSesion(LocalDate.now().toString(),
                userDb.getUsuarioSeleccionado()?.id
            ), it.objetivoDiario)
        }
    }

    private fun initComponentes() {
        circulosAnimados = requireView().findViewById(R.id.circulosAnimados)
        btnCrearUsuario = requireView().findViewById(R.id.btnCrearUsuario)
        tvFecha = requireView().findViewById(R.id.tvFecha)
        btnEntrenar = requireView().findViewById(R.id.btnEntrenar)
        db = DatabaseHelper(requireContext())
        userDb = UserDb(db)
        historialDb = HistorialDb(db)
        usuarioRutinaDb = UsuarioRutinaDb(db)
        rutinaEjercicioDb = RutinaEjercicioDb(db)
    }

    private lateinit var db: DatabaseHelper
    private lateinit var userDb: UserDb
    private lateinit var historialDb: HistorialDb
    private lateinit var rutinaEjercicioDb: RutinaEjercicioDb
    private lateinit var usuarioRutinaDb: UsuarioRutinaDb
    private lateinit var circulosAnimados: CirculosAnimados
    private lateinit var btnCrearUsuario: AppCompatImageButton
    private lateinit var tvFecha: TextView
    private lateinit var btnEntrenar: AppCompatButton
}