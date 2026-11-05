package com.example.ed2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnSensorGravedad = findViewById<Button>(R.id.btn_sensor_gravedad)
        val btnSensor2 = findViewById<Button>(R.id.btn_sensor_2)

        btnSensorGravedad.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnSensor2.setOnClickListener {
            val intent = Intent(this, ProximityActivity::class.java)
            startActivity(intent)
        }
    }
}