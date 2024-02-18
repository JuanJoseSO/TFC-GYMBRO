package com.example.tfc


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tfc.entrenamiento.FragmentEntrenamiento
import com.example.tfc.infoUser.FragmentInfoUser
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityPrincipal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        configurarBottomNavigationView()
        cargarUltimoFragmentoVisitado() //Cargamos HomeFragment por defecto
    }

    //Funcion para configurar la barra de navegación de la aplicacion
    private fun configurarBottomNavigationView() {

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuNavegacion)


        bottomNavigationView.setOnItemSelectedListener { item ->
            //Los distintos fragmentos a los que podemos navegar
            when (item.itemId) {
                R.id.menu_home -> {
                    cargarFragmento(FragmentHome())
                    guardarUltimoFragmento(R.id.menu_home)
                }

                R.id.menu_pesas -> {
                    cargarFragmento(FragmentEntrenamiento())
                    guardarUltimoFragmento(R.id.menu_pesas)
                }

                R.id.menu_perfil -> {
                    cargarFragmento(FragmentInfoUser())
                    guardarUltimoFragmento(R.id.menu_perfil)
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
    fun cargarUltimoFragmentoVisitado() {
        val sharedPreferences = getSharedPreferences("PreferenciasApp", Context.MODE_PRIVATE)
        //Recuperamos el menú de nuevo por que tenemos que actualizar tanto el fragment que se muesta en el contenedor como el botón ilumnado,simple estética
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuNavegacion)
        //Le asignamos el item seleccionada
        bottomNavigationView.selectedItemId = sharedPreferences.getInt("ultimoFragmento", R.id.menu_home)

        when (sharedPreferences.getInt("ultimoFragmento", R.id.menu_home)) { // Valor por defecto es Home
            R.id.menu_home -> cargarFragmento(FragmentHome())
            R.id.menu_pesas -> cargarFragmento(FragmentEntrenamiento())
            R.id.menu_perfil -> cargarFragmento(FragmentInfoUser())
            else -> cargarFragmento(FragmentHome()) //Cargamos el fragmentoHome por defecto
        }
    }

    //Funcion para no volver siempre a FragmentHome,simplemente guardamos con SharedPrefereces el Fragment que usemos
    fun guardarUltimoFragmento(id: Int) {
        val sharedPreferences = getSharedPreferences("PreferenciasApp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("ultimoFragmento", id)
        editor.apply()
    }
}
