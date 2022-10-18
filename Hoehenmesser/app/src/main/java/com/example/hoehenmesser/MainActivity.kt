package com.example.hoehenmesser

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var pressureSensor: Sensor
    private var sensorAvailable: Boolean = false
    private var offsetHeight: Double = 0.0
    private var airpressurevalue: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
            sensorAvailable = true
        }
        else {
            sensorAvailable = false
        }

        val plusButton = findViewById<Button>(R.id.button_plus)
        val minusButton = findViewById<Button>(R.id.button_minus)

        plusButton.setOnClickListener {
            handlePlus()
        }

        minusButton.setOnClickListener {
            handleMinus()
        }
    }

    override fun onResume() {
        super.onResume()
        if (sensorAvailable) sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        if (sensorAvailable) sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val values = sensorEvent.values[0]
        airpressurevalue = values.toDouble()
        updateDisplay()
    }

    override fun onAccuracyChanged(sensor: Sensor, sensorVal: Int) {
    }

    fun getAltitude(): Double {
        var res = 44330000 * ( 1 - (Math.pow(((airpressurevalue)/1013.25), 1.0/5255.0)) )
        return res + offsetHeight
    }

    fun handlePlus() {
        offsetHeight += 1.0
        updateDisplay()
    }

    fun handleMinus() {
        offsetHeight -= 1.0
        updateDisplay()
    }

    fun updateDisplay() {
        var textVieweairpressureval = findViewById<TextView>(R.id.airpressure_val)
        var textViewehoeheval = findViewById<TextView>(R.id.height_val)
        val altitude = getAltitude()
        textVieweairpressureval.setText(String.format("%.0f mbar", airpressurevalue))
        textViewehoeheval.setText(String.format("%.0f m", getAltitude()))

    }

}