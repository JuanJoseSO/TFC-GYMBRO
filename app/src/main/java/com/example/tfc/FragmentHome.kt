package com.example.tfc


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.example.tfc.clasesAuxiliares.DialogOpcionesUsuario
import com.example.tfc.clasesAuxiliares.CirculosAnimados
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FragmentHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_home, container, false)

        initComponentes(view)
        circulosAnimados.rellenarCirculo()
        initCalendario()
        initListeners()

        return view
    }

    private fun initListeners() {
        btnUsuarios.setOnClickListener {
            // Muestra DialogOpcionesUsuario
            val ventanaEmergente = DialogOpcionesUsuario()
            ventanaEmergente.show(parentFragmentManager , "ventanaEmergente")
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

    private fun initComponentes(view: View){
        circulosAnimados=view.findViewById(R.id.circulosAnimados)
        btnUsuarios=view.findViewById(R.id.btnUsuarios)
        tvFecha=view.findViewById(R.id.tvFecha)
    }

    private lateinit var circulosAnimados: CirculosAnimados
    private lateinit var btnUsuarios: AppCompatImageButton
    private lateinit var tvFecha: TextView
}
