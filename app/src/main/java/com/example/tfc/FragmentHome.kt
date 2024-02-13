package com.example.tfc


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tfc.clasesAuxiliares.ActvityOpcionesUsuario
import com.example.tfc.clasesAuxiliares.CirculosAnimados
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FragmentHome : Fragment() {

    private lateinit var circulosAnimados: CirculosAnimados
    private lateinit var btnUsuarios: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_home, container, false)

        iniciaComponentes(view)

        circulosAnimados.animateCircleFilling()
        calendario(view)
        iniciaListeners()

        return view
    }

    private fun iniciaComponentes(view: View){

        circulosAnimados=view.findViewById(R.id.circulosAnimados)
        btnUsuarios=view.findViewById(R.id.btnUsuarios)
    }

    private fun iniciaListeners() {
        btnUsuarios.setOnClickListener {
            // Crear y mostrar el DialogFragment
            val ventanaEmergente = ActvityOpcionesUsuario()
            ventanaEmergente.show(parentFragmentManager , "ventanaEmergente")
        }
    }

    fun calendario(view: View) {
        val calendario: Calendar = Calendar.getInstance()
        val formatearFecha = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale("es", "ES"))
        var fechaFormateada = formatearFecha.format(calendario.time)
        // Capitalizar solo la primera letra
        if (fechaFormateada.isNotEmpty()) {
            fechaFormateada = fechaFormateada.substring(0, 1)
                .uppercase(Locale.getDefault()) + fechaFormateada.substring(1)
        }


        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        tvFecha.text = fechaFormateada
    }

}