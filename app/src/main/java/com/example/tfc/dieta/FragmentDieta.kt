package com.example.tfc.dieta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.AdapterDieta
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.DietaDb


class FragmentDieta : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponentes(view)
        initUI()
    }

    private fun initUI() {
        //Eliminamos el divider,gusto personal
        listaDieta.divider=null
        //Recogemos el listado de dietas de la base de datos
        val dietas = dietaDb.getDietas()
        //Los mostramos en la lista,el adaptador ya se encarga de recoger los nombres
        val adapter = AdapterDieta(requireContext(), dietas)
        listaDieta.adapter= adapter
        //Listener para la dieta que seleccionemos desde la lista
        listaDieta.setOnItemClickListener { _, _, position, _->
            val dieta=dietas[position]
            val fragmentImagenDieta = FragmentImagenDieta().apply {
                //Similar a un intent,pero así se configura la navegación entre fragments
                arguments = Bundle().apply {
                    putString("imagen", dieta.imagen)
                }
            }
            //Navegamos al fragmento destino,fragmentImagenDieta
            childFragmentManager.beginTransaction()
                .replace(R.id.contenedor_listas, fragmentImagenDieta)
                .commit()
            }
        }

    override fun onDestroy() {
        DatabaseHelper(requireContext()).close()
        super.onDestroy()
    }

    private fun initComponentes(view: View){
        listaDieta=view.findViewById(R.id.listas)
        dietaDb= DietaDb(DatabaseHelper(requireContext()))
    }

    private lateinit var listaDieta:ListView
    private lateinit var dietaDb: DietaDb
}