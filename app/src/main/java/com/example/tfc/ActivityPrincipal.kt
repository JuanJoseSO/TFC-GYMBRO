package com.example.tfc


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tfc.Entrenamiento.FragmentEntrenamiento
import com.example.tfc.InfoUser.FragmentInfoUser
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityPrincipal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        configurarBottomNavigationView()
        if (savedInstanceState == null) {
            cargarFragmento(FragmentHome()) //Cargamos HomeFragment por defecto
        }
    }

    //Funcion para configurar la barra de navegación de la aplicacion
    private fun configurarBottomNavigationView() {

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuNavegacion)
            bottomNavigationView.setOnItemSelectedListener { item ->
                //Los distintos fragmentos a los que podemos navegar
                when (item.itemId) {
                    R.id.menu_home -> {
                        cargarFragmento(FragmentHome())
                    }

                    R.id.menu_pesas -> {
                        cargarFragmento(FragmentEntrenamiento())
                    }

                    R.id.menu_perfil -> {
                        cargarFragmento(FragmentInfoUser())
                    }
                }
                true
        }
    }

    //Funcion para cargar fragmentos
    private fun cargarFragmento(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
