package com.example.tfc.Entrenamiento



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tfc.R


class FragmentEntrenamiento : Fragment() {
    //Fragmento que forma parte del menú que contrendra un "Swicth" y un contenedor para otros fragmentos
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entrenamiento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Mostramos el fragmento de categorías
        if (savedInstanceState == null) { // Evita agregar el fragmento si ya está presente
            mostrarFragmentCategorias()
        }
    }

    private fun mostrarFragmentCategorias() {
        // Crea una nueva instancia del FragmentCategorias
        val fragmentCategorias = FragmentCategorias()

        // Realiza la transacción del fragmento aquí
        parentFragmentManager.beginTransaction().apply {
            // Usamos childFragmentManager ya que estamos dentro de otro fragmento
            replace(R.id.fragment_contenedor, fragmentCategorias)
            commit()
        }
    }
}
