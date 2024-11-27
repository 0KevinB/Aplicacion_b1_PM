package com.example.aplicacion

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.aplicacion.R

class podometro : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepsSensor: Sensor? = null
    private var stepsCount: Int = 0
    private var initialSteps: Int = 0
    private var isStepCounterSensor: Boolean = false

    private lateinit var tvStepsCount: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    companion object {
        private const val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1001
        private const val TAG = "Ventana9"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podometro)

        tvStepsCount = findViewById(R.id.tvSteps)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepsSensor != null) {
            isStepCounterSensor = true
            Log.d(TAG, "Usando TYPE_STEP_COUNTER")
        } else {
            // Si no está disponible, intenta con TYPE_STEP_DETECTOR
            stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            if (stepsSensor != null) {
                isStepCounterSensor = false
                Log.d(TAG, "Usando TYPE_STEP_DETECTOR")
            }
        }

        if (stepsSensor == null) {
            Log.e(TAG, "No se encontró sensor de pasos")
            Toast.makeText(this, "No se encontró sensor de pasos", Toast.LENGTH_LONG).show()
        } else {
            Log.d(TAG, "Sensor encontrado: ${stepsSensor?.name}")
        }

        btnStart.setOnClickListener {

                registerSensorListener()

        }

        btnStop.setOnClickListener {
            unregisterSensorListener()
            generateReport()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_ACTIVITY_RECOGNITION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    registerSensorListener()
                } else {
                    Log.e(TAG, "Permiso de reconocimiento de actividad denegado")
                    Toast.makeText(this, "Se requiere permiso para contar pasos", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun registerSensorListener() {
        stepsSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
            stepsCount = 0
            initialSteps = -1
            tvStepsCount.text = "0"
            Log.d(TAG, "Sensor de pasos registrado")
            Toast.makeText(this, "Comenzando a contar pasos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unregisterSensorListener() {
        sensorManager.unregisterListener(this, stepsSensor)
        Log.d(TAG, "Sensor de pasos desregistrado")
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.d(TAG, "Evento de sensor recibido: ${event.sensor.type}, Valores: ${event.values.contentToString()}")
        if (event.sensor == stepsSensor) {
            if (isStepCounterSensor) {
                val totalSteps = event.values[0].toInt()
                if (initialSteps == -1) {
                    initialSteps = totalSteps
                }
                stepsCount = totalSteps - initialSteps
            } else {
                stepsCount++
            }
            tvStepsCount.text = stepsCount.toString()
            Log.d(TAG, "Pasos detectados: $stepsCount")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Log.d(TAG, "Precisión del sensor cambiada: $accuracy")
    }

    private fun generateReport() {
        val report = "Total de pasos: $stepsCount"
        Log.d(TAG, "Reporte generado: $report")
        Toast.makeText(this, report, Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        unregisterSensorListener()
    }
}

