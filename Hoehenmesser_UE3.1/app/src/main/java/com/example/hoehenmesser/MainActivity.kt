package com.example.hoehenmesser

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var pressureSensor: Sensor
    private var sensorAvailable: Boolean = false
    private var offsetHeight: Double = 0.0
    private var airpressurevalue: Double = 0.0
    private val sharedPrefFile = "sharedPreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreference: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)

        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
            sensorAvailable = true
        }
        else {
            sensorAvailable = false
        }

        val sharedPref = getSharedPreferences("offsetHeightSetting", MODE_PRIVATE)

        val loadOffsetButton = findViewById<Button>(R.id.buttonLoad)
        val saveOffsetButton = findViewById<Button>(R.id.buttonSave)

        /*
            working shared preference reference: https://www.javatpoint.com/kotlin-android-sharedpreferences
         */

        // save offset to sharedPreferences
        saveOffsetButton.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreference.edit()
            editor.putFloat("offsetHeightSetting", offsetHeight.toFloat())
            editor.apply()
            editor.commit()
        }

        // load offset from sharedPreferences
        loadOffsetButton.setOnClickListener {
            offsetHeight = sharedPreference.getFloat("offsetHeightSetting", 0.0F).toDouble()
            updateDisplay()
        }

        val plusButton = findViewById<Button>(R.id.button_plus)
        val minusButton = findViewById<Button>(R.id.button_minus)

        plusButton.setOnClickListener {
            handlePlus()
        }

        minusButton.setOnClickListener {
            handleMinus()
        }

        // load stored offset on opening the app
        offsetHeight = sharedPreference.getFloat("offsetHeightSetting", 0.0F).toDouble()
        updateDisplay()
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
        var textViewOffset = findViewById<TextView>(R.id.offset_val)
        textVieweairpressureval.setText(String.format("%.0f mbar", airpressurevalue))
        textViewehoeheval.setText(String.format("%.0f m", getAltitude()))
        textViewOffset.setText(String.format("Calibration offset: %.0f m", offsetHeight))

    }

}