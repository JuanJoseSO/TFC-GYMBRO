package com.example.tfc.dieta

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfc.R
import com.github.chrisbanes.photoview.PhotoView


class FragmentImagenDieta : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_imagen_dieta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponentes(view)
        setImagen()
    }

    //Añadimos la imagen dependiendo de argument que nos envie el fragment padre,similar a un intent
    @SuppressLint("DiscouragedApi")
    private fun setImagen(){
        if(arguments?.getString("imagen")!=null){
            val resourceId= resources.getIdentifier(arguments?.getString("imagen"),"drawable",context?.packageName)
            pvImagen.setImageResource(resourceId)
        }
    }

    private fun initComponentes(view: View){
        pvImagen=view.findViewById(R.id.pvImagen)
    }

    private lateinit var pvImagen : PhotoView
}