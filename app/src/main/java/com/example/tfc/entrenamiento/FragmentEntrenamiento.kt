package com.example.tfc.entrenamiento


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.tfc.R


class FragmentEntrenamiento : Fragment() {
    //Fragmento que forma parte del menú que contrendra un "Swicth" y un contenedor para otros fragmentos
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entrenamiento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponentes()
        initListeners()
        //Mostramos el fragmento de categorías
        mostrarFragment(FragmentCategorias())
    }

    //Funcion para cambiar el Fragment al que le pasamos el NUEVO fragment a mostrar por parametro
    private fun mostrarFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            //Cambiamos el fragment indicando el contenedor requerido para la tarea definido en el layout
            replace(R.id.fragmentContenedorEntrenamiento, fragment).commit()
        }
    }

    private fun initListeners() {
        //Mostramos distintos fragmentos dependiendo de la posición del Swicht que en realidad son dos radiobuttons
        switchEjercicio.setOnClickListener {
            mostrarFragment(FragmentCategorias())
        }

        switchRutina.setOnClickListener {
            mostrarFragment(FragmentRutina())
        }
    }

    private fun initComponentes() {
        switchEjercicio = requireView().findViewById(R.id.switch_ejercicio)
        switchRutina = requireView().findViewById(R.id.switch_rutina)

    }

    private lateinit var switchEjercicio: RadioButton
    private lateinit var switchRutina: RadioButton
}