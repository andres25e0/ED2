package com.example.ed2 // Se deja solo esto para que coincida con tu paquete

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProximityActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var gestorSensores: SensorManager
    private var sensorProximidad: Sensor? = null

    private lateinit var disenoPrincipal: LinearLayout
    private lateinit var imagenEstado: ImageView
    private lateinit var textoEstado: TextView
    private lateinit var textoValorSensor: TextView
    private lateinit var botonMostrarContador: Button
    private lateinit var botonRegresar: Button

    private var contadorBloqueos = 0
    private var estaBloqueada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_proximity)

        disenoPrincipal = findViewById(R.id.disenoPrincipal)
        imagenEstado = findViewById(R.id.imagenEstado)
        textoEstado = findViewById(R.id.textoEstado)
        textoValorSensor = findViewById(R.id.textoValorSensor)
        botonMostrarContador = findViewById(R.id.botonMostrarContador)
        botonRegresar = findViewById(R.id.botonRegresar)

        ViewCompat.setOnApplyWindowInsetsListener(disenoPrincipal) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gestorSensores = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorProximidad = gestorSensores.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (sensorProximidad == null) {
            textoValorSensor.text = "Sensor de proximidad no disponible"
        }

        botonMostrarContador.setOnClickListener {
            Toast.makeText(this, "Bloqueos de pantalla: $contadorBloqueos", Toast.LENGTH_SHORT).show()
        }

        botonRegresar.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorProximidad?.let {
            gestorSensores.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        gestorSensores.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
            val distancia = event.values[0]

            textoValorSensor.text = "Valor del sensor: $distancia cm"

            val rangoMaximo = sensorProximidad?.maximumRange ?: 10f
            val estaCerca = distancia < rangoMaximo

            if (estaCerca) {
                if (!estaBloqueada) {
                    estaBloqueada = true
                    contadorBloqueos++
                    actualizarUI(true)
                }
            } else {
                if (estaBloqueada) {
                    estaBloqueada = false
                    actualizarUI(false)
                }
            }
        }
    }

    private fun actualizarUI(bloqueada: Boolean) {
        if (bloqueada) {
            disenoPrincipal.setBackgroundColor(Color.BLACK)
            imagenEstado.setImageResource(R.drawable.ic_lock)
            imagenEstado.setColorFilter(Color.WHITE)
            textoEstado.text = "Pantalla bloqueada"
            textoEstado.setTextColor(Color.WHITE)
            textoValorSensor.setTextColor(Color.WHITE)
        } else {
            disenoPrincipal.setBackgroundColor(Color.WHITE)
            imagenEstado.setImageResource(R.drawable.ic_lock_open)
            imagenEstado.setColorFilter(Color.BLACK)
            textoEstado.text = "Pantalla no bloqueada"
            textoEstado.setTextColor(Color.BLACK)
            textoValorSensor.setTextColor(Color.BLACK)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}