package com.example.tfc.dieta



import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.AdapterDieta
import com.example.tfc.entrenamiento.FragmentCategorias
import com.example.tfc.entrenamiento.FragmentEntrenamiento
import com.example.tfc.sqlite.DatabaseHelper


class FragmentDieta : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dieta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponentes(view)
        initUI()


    }

    private fun initUI() {
        //Recogemos el listado de dietas de la base de datos
        val dietas = db.getDietas()
        //Los mostramos en la lista
        val adapter = AdapterDieta(requireContext(), dietas.map{it.nombre})
        listaDieta.adapter= adapter

        //Listener para la dieta que seleccionemos desde la lista
        listaDieta.setOnItemClickListener { _, _, position, _->
            val fragment: Fragment = FragmentImagenDieta()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContenedorDieta, FragmentImagenDieta())
                .addToBackStack(null)
                .commit()
            }
        }


    override fun onDestroy() {
        super.onDestroy()
        db.close() // Asumiendo que `db` es accesible a nivel de clase y su ciclo de vida está bien gestionado
    }



    private fun initComponentes(view: View){
        listaDieta=view.findViewById(R.id.listaDieta)
        db= DatabaseHelper(requireContext())
    }

    private lateinit var listaDieta:ListView
    private lateinit var db: DatabaseHelper
}