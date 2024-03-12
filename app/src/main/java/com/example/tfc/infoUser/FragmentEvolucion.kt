package com.example.tfc.infoUser

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.tfc.R
import com.example.tfc.clasesAuxiliares.clasesListas.AdapterEvolucion
import com.example.tfc.sqlite.DatabaseHelper
import com.example.tfc.sqlite.sqliteMetodos.HistorialPesoDb
import com.example.tfc.sqlite.sqliteMetodos.UserDb

class FragmentEvolucion : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_evolucion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponentes()
        initListeners()
        cargarListaHistorialPeso()
        filtrarLista()
    }

    private fun cargarListaHistorialPeso() {
        val listaHistorialPeso =
            historialPesoDb.getHistorialPesoPorUsuario(userDb.getUsuarioSeleccionado()!!.id)
        adapter = AdapterEvolucion(requireContext(), listaHistorialPeso)
        lvHistorial.adapter = adapter
    }

    private fun filtrarLista() {
        /*Funcion EditText para filtrar la lista segun la funcion de filtrado del adapter,solo requiere
          implementar su último metodo,fuente Stackoverflow*/
        etFiltro.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                adapter.filter.filter(s.toString())
            }
        })
    }
    private fun initListeners(){
        btnEliminarHistorial.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.eliminar_historial))
                .setMessage(getString(R.string.estas_seguro_no_se_puede_deshacer_esta_decisi_n))
                .setPositiveButton("Si") { _, _ ->
                    //Eliminarmos la lista de usuarios
                    historialPesoDb.borrarHistorialPeso(userDb.getUsuarioSeleccionado()!!.id)
                    cargarListaHistorialPeso()
                }.setNegativeButton("No", null).show()
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    private fun initComponentes() {
        lvHistorial = requireView().findViewById(R.id.lvHistorial)
        etFiltro = requireView().findViewById(R.id.etFiltro)
        btnEliminarHistorial = requireView().findViewById(R.id.btnEliminarHistorial)
        db = DatabaseHelper(requireContext())
        historialPesoDb = HistorialPesoDb(db)
        userDb = UserDb(db)
    }

    private lateinit var adapter: AdapterEvolucion
    private lateinit var etFiltro: EditText
    private lateinit var lvHistorial: ListView
    private lateinit var btnEliminarHistorial: AppCompatButton
    private lateinit var db: DatabaseHelper
    private lateinit var historialPesoDb: HistorialPesoDb
    private lateinit var userDb: UserDb
}