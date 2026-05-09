package com.example.tustareas

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

/**
 * Actividad independiente que muestra un error irreversible del fragmento previo o no controlado por el código.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class ErrorNoPrevisto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_no_previsto)

        // Mostrar error en pantalla
        val mensaje = intent.getStringExtra("error") ?: "Error desconocido"
        findViewById<TextView>(R.id.excepcion).text = mensaje

        // Boton para cerrar la aplicación
        findViewById<MaterialButton>(R.id.cerrar).setOnClickListener {
            finishAffinity()
            exitProcess(0)
        }
    }
}